package org.neutrinocms.bo.conf;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;

import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

@Component
public class NeutrinoBOProperties {
	
	@Resource
	private Environment environment;
		
	private Long maxFileSize;
	private Long maxRequestSize;
	private Integer fileSizeThreshold;
	private String uploadDir;
	
	@PostConstruct
	public void init() {
		maxFileSize =  Long.parseLong(environment.getProperty("http.multipart.max-file-size"));
		maxRequestSize = Long.parseLong(environment.getProperty("http.multipart.max-request-size"));
		fileSizeThreshold = Integer.parseInt(environment.getProperty("http.multipart.file-size-threshold"));
		uploadDir  = environment.getProperty("upload.dir");	
	}
	
	public Long getMaxFileSize() {
		return maxFileSize;
	}
	public Long getMaxRequestSize() {
		return maxRequestSize;
	}
	public Integer getFileSizeThreshold() {
		return fileSizeThreshold;
	}
	public String getUploadDir() {
		return uploadDir;
	}
}
