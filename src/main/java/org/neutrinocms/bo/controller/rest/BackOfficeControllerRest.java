package org.neutrinocms.bo.controller.rest;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.neutrinocms.bo.service.light.BackOfficeService;
import org.neutrinocms.core.bean.NField;
import org.neutrinocms.core.exception.ControllerException;
import org.neutrinocms.core.exception.ResourceNotFoundException;
import org.neutrinocms.core.exception.ServiceException;
import org.neutrinocms.core.model.IdProvider;
import org.neutrinocms.core.specification.IdProviderSpecificationBuilder;
import org.neutrinocms.core.util.EntityLocator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "${api.url}/${bo.url}")
//@Secured({ "ROLE_WEBMASTER", "ROLE_ADMIN", "ROLE_BO" })
public class BackOfficeControllerRest {
		
	@Autowired
	protected EntityLocator entityLocator;

	@Autowired
	protected BackOfficeService backOfficeService;
	
	@RequestMapping(value = "list/{entityName}", method = RequestMethod.GET)
	public Page<IdProvider> list(
			@PathVariable("entityName") String entityName, 
			Pageable pageRequest, 
			@RequestParam(value = "search", required = false) String search) throws ControllerException, ResourceNotFoundException {
		try {
	        IdProviderSpecificationBuilder builder = new IdProviderSpecificationBuilder();
	        Pattern pattern = Pattern.compile("(\\w+?)(:|<|>)(\\w+?),");
	        Matcher matcher = pattern.matcher(search + ",");
	        while (matcher.find()) {
	            builder.with(matcher.group(1), matcher.group(2), matcher.group(3));
	        }
	        Specification<IdProvider> spec = builder.build();

			Class<?> object = entityLocator.getEntity(entityName).getClass();
			return backOfficeService.getFullObjects(object, pageRequest, spec);

		} catch (ClassNotFoundException e) {
			throw new ResourceNotFoundException(e);
		} catch (ServiceException e) {
			throw new ControllerException(e);
		}

	}
		
	@RequestMapping(value = "fields/{entityName}", method = RequestMethod.GET)
	public List<NField> fields(@PathVariable("entityName") String entityName) throws ControllerException, ResourceNotFoundException {
		try {
			Class<?> object = entityLocator.getEntity(entityName).getClass();
			return backOfficeService.getNFields(object);
			
		} catch (ClassNotFoundException e) {
			throw new ResourceNotFoundException(e);
		} catch (ServiceException e) {
			throw new ControllerException(e);
		}
	}
	


	
}