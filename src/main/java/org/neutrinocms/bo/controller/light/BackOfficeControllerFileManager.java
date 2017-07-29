package org.neutrinocms.bo.controller.light;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.neutrinocms.bo.exception.light.StorageException;
import org.neutrinocms.bo.service.light.StorageService;
import org.neutrinocms.bo.wrapper.light.CompressRequestWrapper;
import org.neutrinocms.bo.wrapper.light.MoveRequestWrapper;
import org.neutrinocms.bo.wrapper.light.RemoveRequestWrapper;
import org.neutrinocms.bo.wrapper.light.ResultEncapsulatorWrapper;
import org.neutrinocms.core.bean.NFile;
import org.neutrinocms.core.conf.NeutrinoCoreProperties;
import org.neutrinocms.core.exception.ControllerException;
import org.neutrinocms.core.exception.ResourceNotFoundException;
import org.neutrinocms.core.exception.ServiceException;
import org.neutrinocms.core.model.independant.Folder;
import org.neutrinocms.core.util.CommonUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartRequest;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping(value = "${bo.url}" + CommonUtil.BO_FILE_URL)
public class BackOfficeControllerFileManager extends BackOfficeController {
	@Autowired
	NeutrinoCoreProperties neutrinoCoreProperties;	
	
	protected static final String BO_FILE_PARAM_ACTION = "action";
	protected static final String BO_FILE_PARAM_PATH = "path";
	protected static final String BO_FILE_PARAM_ITEMS = "items[]";
	protected static final String BO_FILE_PARAM_TOFILENAME = "toFilename";
	
	@Autowired
	private StorageService fileService;

	@RequestMapping(value = BO_FILE_HOME_URL, method = RequestMethod.GET)
	public ModelAndView home() throws ControllerException, ResourceNotFoundException   {
		Folder folder = getBOFolder();
		return baseView(BO_FILE_HOME_PAGE, folder);
	}
	
	@RequestMapping(value = BO_FILE_SINGLE_URL, method = RequestMethod.GET)
	public ModelAndView single(@RequestParam(value = "navbar", required = false, defaultValue = "true") Boolean navbar, @RequestParam(value = "multi", required = false, defaultValue = "true") Boolean multi, @RequestParam(value = "sidebar", required = false, defaultValue = "true") Boolean sidebar) throws ControllerException, ResourceNotFoundException   {
		Folder folder = getBOFolder();
		ModelAndView modelAndView = baseView(BO_FILE_SINGLE_PAGE, folder);
		modelAndView.addObject("navbar", navbar);
		modelAndView.addObject("multi", multi);
		modelAndView.addObject("sidebar", sidebar);
		return modelAndView;
	}

	@RequestMapping(value = BO_FILE_ADD_URL, method = RequestMethod.POST)
	public @ResponseBody ResultEncapsulatorWrapper add(MultipartRequest multipartRequest, @RequestParam("destination") String filename) {
		
		boolean success = true;
		String errorMessage = null;
		try {
			Map<String, MultipartFile> files = multipartRequest.getFileMap();
			for (Map.Entry<String, MultipartFile> entry : files.entrySet()){
				fileService.store(entry.getValue());
			}
		} catch (StorageException e) {
			success = false;
			errorMessage = e.getMessage();
		}
		Map<String, Object> result = new HashMap<>();
		result.put("success", success);
		result.put("error", errorMessage);
		return new ResultEncapsulatorWrapper(result);
	}
	
	@RequestMapping(value = BO_FILE_REMOVE_URL, method = RequestMethod.POST)
	public @ResponseBody ResultEncapsulatorWrapper remove(@RequestBody RemoveRequestWrapper removeRequest) throws ControllerException {
		boolean success = true;
		String errorMessage = null;
		try {
			for (String item : removeRequest.getItems()) {
				fileService.delete(item);
			}
		} catch (Exception e) {
			success = false;
			errorMessage = e.getMessage();
		}
		Map<String, Object> result = new HashMap<>();
		result.put("success", success);
		result.put("error", errorMessage);
		return new ResultEncapsulatorWrapper(result);
	}
	
	@RequestMapping(value = BO_FILE_RENAME_URL, method = RequestMethod.POST)
	public @ResponseBody ResultEncapsulatorWrapper rename(@RequestBody Map<String, String> params) throws ControllerException {
		boolean success = true;
		String errorMessage = null;
		try {
			String item = params.get("item");
			String newItemPath = params.get("newItemPath");
			fileService.move(item, newItemPath);
			
		} catch (Exception e) {
			success = false;
			errorMessage = e.getMessage();
		}
		Map<String, Object> result = new HashMap<>();
		result.put("success", success);
		result.put("error", errorMessage);
		return new ResultEncapsulatorWrapper(result);
	}
	
	@RequestMapping(value = BO_FILE_MOVE_URL, method = RequestMethod.POST)
	public @ResponseBody ResultEncapsulatorWrapper move(@RequestBody MoveRequestWrapper moveRequest) throws ControllerException {
		boolean success = true;
		String errorMessage = null;
		try {
			String newItemPath = moveRequest.getNewPath();
			for (String item : moveRequest.getItems()) {
				fileService.move(item, newItemPath + "/" + item);
			}
		} catch (Exception e) {
			success = false;
			errorMessage = e.getMessage();
		}
		Map<String, Object> result = new HashMap<>();
		result.put("success", success);
		result.put("error", errorMessage);
		return new ResultEncapsulatorWrapper(result);
	}
	

	@RequestMapping(value = BO_FILE_LIST_URL, method = RequestMethod.POST)
	public @ResponseBody ResultEncapsulatorWrapper list(@RequestBody Map<String, String> params) throws ControllerException {
		try {
			String path = params.get("path");
			boolean onlyFolders = params.get("onlyFolders") == "true";

			List<NFile> list = fileService.list(path, onlyFolders);
			return new ResultEncapsulatorWrapper(list);
		} catch (ServiceException e) {
			throw new ControllerException(e);
		}
	}

	
	@RequestMapping(value = BO_FILE_DOWNLOAD_URL, method = RequestMethod.GET)
	public HttpEntity<FileSystemResource> getFile(@RequestParam(BO_FILE_PARAM_ACTION) String action, @RequestParam(BO_FILE_PARAM_PATH) String path) throws ControllerException {
		try {
			FileSystemResource fileSystemResource = new FileSystemResource(fileService.getPath(path).toFile());

			HttpHeaders header = new HttpHeaders();
		    header.setContentType(MediaType.APPLICATION_OCTET_STREAM);
		    header.set(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + fileSystemResource.getFilename());			
			return new HttpEntity<FileSystemResource>(fileSystemResource, header);
			
		} catch (Exception e) {
			throw new ControllerException(e);
		}
	}
	
	@RequestMapping(value = BO_FILE_DOWNLOADMULTIPLE_URL, method = RequestMethod.GET)
	public HttpEntity<FileSystemResource> getFileMultiple(@RequestParam(BO_FILE_PARAM_ACTION) String action, @RequestParam(BO_FILE_PARAM_ITEMS) List<String> items, @RequestParam(BO_FILE_PARAM_TOFILENAME) String toFileName) throws ControllerException {
		try {
			FileSystemResource fileSystemResource = new FileSystemResource(fileService.compress(items, null, toFileName).toFile());

			HttpHeaders header = new HttpHeaders();
		    header.setContentType(MediaType.APPLICATION_OCTET_STREAM);
		    header.set(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + fileSystemResource.getFilename());			
			return new HttpEntity<FileSystemResource>(fileSystemResource, header);
			
		} catch (Exception e) {
			throw new ControllerException(e);
		}
	}

	
	@RequestMapping(value = BO_FILE_COMPRESS_URL, method = RequestMethod.POST)
	public @ResponseBody ResultEncapsulatorWrapper compress(@RequestBody CompressRequestWrapper compressRequest) throws ControllerException {
		boolean success = true;
		String errorMessage = null;
		try {
			fileService.compress(compressRequest.getItems(), fileService.getPath(compressRequest.getDestination()).toString() , compressRequest.getCompressedFilename()).toFile();
		} catch (Exception e) {
			success = false;
			errorMessage = e.getMessage();
		}
		Map<String, Object> result = new HashMap<>();
		result.put("success", success);
		result.put("error", errorMessage);
		return new ResultEncapsulatorWrapper(result);
	}
	
	
	
	
	
	
}
