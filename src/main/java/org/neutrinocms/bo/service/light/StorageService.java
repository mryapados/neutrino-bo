package org.neutrinocms.bo.service.light;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.PosixFileAttributeView;
import java.nio.file.attribute.PosixFileAttributes;
import java.nio.file.attribute.PosixFilePermission;
import java.nio.file.attribute.PosixFilePermissions;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.stream.Stream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import javax.annotation.PostConstruct;

import org.apache.log4j.Logger;
import org.neutrinocms.bo.conf.NeutrinoBOProperties;
import org.neutrinocms.bo.exception.light.StorageException;
import org.neutrinocms.bo.exception.light.StorageFileNotFoundException;
import org.neutrinocms.core.bean.NFile;
import org.neutrinocms.core.bo.annotation.BOService;
import org.neutrinocms.core.conf.NeutrinoCoreProperties;
import org.neutrinocms.core.exception.ServiceException;
import org.neutrinocms.core.util.CommonUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.util.FileSystemUtils;
import org.springframework.web.multipart.MultipartFile;


@Service
@Scope(value = "singleton")
@BOService
public class StorageService implements IStorageService{
	private Logger logger = Logger.getLogger(StorageService.class);
	
	@Autowired
	protected CommonUtil common;
	
	@Autowired
	protected NeutrinoCoreProperties neutrinoCoreProperties;
	
	@Autowired
	protected NeutrinoBOProperties neutrinoBOProperties;
	
    private Path rootLocation;
    
    @PostConstruct
    private void initialize(){
    	this.rootLocation = Paths.get(common.getWebInfFolder() + neutrinoBOProperties.getUploadDir());
    }
    
	public List<NFile> list(String path, boolean onlyFolders) throws ServiceException  {
    	try{
			File dir = new File(rootLocation + path);
			File[] fileList = dir.listFiles();
			List<NFile> nFiles = new ArrayList<>();
			if (fileList != null) {
				for (File f : fileList) {
					if (!f.exists() || (onlyFolders && !f.isDirectory())) {
						continue;
					}
					nFiles.add(mkNFile(f));
				}
			}
			return nFiles;
    	} catch (IOException e) {
			throw new ServiceException("todo", e);
		}
	}

    private NFile mkNFile(java.io.File file) throws IOException{
    	if (!file.exists()) throw new FileNotFoundException();
    	Date d = new Date(file.lastModified());
    	return new NFile(file.getName(), getPermissions(file), d, file.length(), file.isFile() ? "file" : "dir", null);
    }
    private String getPermissions(java.io.File f) throws IOException {
		// http://www.programcreek.com/java-api-examples/index.php?api=java.nio.file.attribute.PosixFileAttributes
		PosixFileAttributeView fileAttributeView = Files.getFileAttributeView(f.toPath(), PosixFileAttributeView.class);
		if (fileAttributeView == null) return null;
		PosixFileAttributes readAttributes = fileAttributeView.readAttributes();
		Set<PosixFilePermission> permissions = readAttributes.permissions();
		return PosixFilePermissions.toString(permissions);
	}


    @Override
    public void store(MultipartFile file) {
        try {
            if (file.isEmpty()) {
                throw new StorageException("Failed to store empty file " + file.getOriginalFilename());
            }
            Files.copy(file.getInputStream(), this.rootLocation.resolve(file.getOriginalFilename()));
        } catch (IOException e) {
            throw new StorageException("Failed to store file " + file.getOriginalFilename(), e);
        }
    }

    @Override
    public Stream<Path> loadAll() {
        try {
            return Files.walk(this.rootLocation, 1)
                    .filter(path -> !path.equals(this.rootLocation))
                    .map(path -> this.rootLocation.relativize(path));
        } catch (IOException e) {
            throw new StorageException("Failed to read stored files", e);
        }

    }

    public Path getPath(String filename) {
        return Paths.get(rootLocation + filename);
    }
    
    @Override
    public Path load(String filename) {
        return rootLocation.resolve(filename);
    }

    private void compressSubDirectory(String basePath, File dir, ZipOutputStream zout) throws IOException {
    	logger.debug("Zipping the directory: " + dir.getName());
        File[] files = dir.listFiles();
        for (File file : files) {
            if (file.isDirectory()) {
                String path = basePath + file.getName() + "/";
                zout.putNextEntry(new ZipEntry(path));
                compressSubDirectory(path, file, zout);
                zout.closeEntry();
            } else {
            	compressFile(basePath, file, zout);
            }
        }
    }
    private void compressFile(String basePath, File file, ZipOutputStream zout) throws IOException {
    	logger.debug("Zipping the file: " + file.getName());
        byte[] buffer = new byte[4096];    	
        FileInputStream fin = new FileInputStream(file);
        zout.putNextEntry(new ZipEntry(basePath + file.getName()));
        int length;
        while ((length = fin.read(buffer)) > 0) {
            zout.write(buffer, 0, length);
        }
        zout.closeEntry();
        fin.close();
    }
	public Path compress(List<String> files, String destLocation, String destFileName) {
		FileOutputStream fos = null;
		ZipOutputStream zipOut = null;
		Path path = null;
		if (destLocation != null) path = Paths.get(destLocation);
		else path = Paths.get(neutrinoCoreProperties.getTempDir());
		try {
			if (destFileName == null) destFileName = String.valueOf(files.hashCode());
			path = Paths.get(path + File.separator + destFileName);
			fos = new FileOutputStream(path.toFile());
			zipOut = new ZipOutputStream(new BufferedOutputStream(fos));
			for (String filePath : files) {
				File input = getPath(filePath).toFile();
				if (input.isDirectory()){
					compressSubDirectory("", input, zipOut);
				} else {
					compressFile("", input, zipOut);
				}
			}
			zipOut.close();
			logger.debug("Done... Zipped the files...");
		} catch (FileNotFoundException e) {
			throw new StorageFileNotFoundException("Could not found file", e);
		} catch (IOException e) {
			throw new StorageException("error", e);
		} finally {
			try {
				if (fos != null){
					fos.close();
					return path;
				}
			} catch (Exception e) {
				throw new StorageException("error", e);
			}
		}
		throw new StorageFileNotFoundException("no FileOutputStream");
	}

    @Override
    public Resource loadAsResource(String filename) {
        try {
            Path file = load(this.rootLocation + filename);
            Resource resource = new UrlResource(file.toUri());
            if(resource.exists() || resource.isReadable()) {
                return resource;
            }
            else {
                throw new StorageFileNotFoundException("Could not read file: " + filename);

            }
        } catch (MalformedURLException e) {
            throw new StorageFileNotFoundException("Could not read file: " + filename, e);
        }
    }
    
	public void move(String filename, String newFilename) {
		try {
			File afile = new File(rootLocation + filename);
			if (!afile.renameTo(new File(rootLocation + newFilename))){
				throw new StorageException("Failed to move file : " + filename + " to " + newFilename);
			}
		}catch(Exception e){
			throw new StorageException(e.getMessage(), e);
    	}
	}

    @Override
    public void deleteAll() {
        FileSystemUtils.deleteRecursively(rootLocation.toFile());
    }
    
    public void delete(String path) {
    	FileSystemUtils.deleteRecursively(Paths.get(rootLocation + path).toFile());
    }

    @Override
    public void init() {
        try {
            Files.createDirectory(rootLocation);
        } catch (IOException e) {
            throw new StorageException("Could not initialize storage", e);
        }
    }


    
}
