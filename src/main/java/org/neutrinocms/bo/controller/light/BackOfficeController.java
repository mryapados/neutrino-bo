package org.neutrinocms.bo.controller.light;

import java.beans.PropertyEditorSupport;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.neutrinocms.bo.service.light.BackOfficeService;
import org.neutrinocms.core.controller.AbstractController;
import org.neutrinocms.core.exception.ControllerException;
import org.neutrinocms.core.exception.ResourceNotFoundException;
import org.neutrinocms.core.exception.ServiceException;
import org.neutrinocms.core.exception.UtilException;
import org.neutrinocms.core.model.IdProvider;
import org.neutrinocms.core.model.independant.Folder;
import org.neutrinocms.core.service.LangService;
import org.neutrinocms.core.util.CommonUtil;
import org.neutrinocms.core.util.EntityLocator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.CustomCollectionEditor;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;


@Controller
@RequestMapping(value = "${bo.url}")
@Secured({ "ROLE_WEBMASTER", "ROLE_ADMIN", "ROLE_BO" })
public abstract class BackOfficeController extends AbstractController {
	
	protected static final String BO_HOME_URL = "";
	protected static final String BO_HOME_PAGE = "@bo_page_home";
	
	protected static final String BO_LANGUAGE_URL = "language/";
	
	protected static final String BO_EDIT_URL = "edit/";
	protected static final String BO_EDIT_PAGE = "@bo_page_edit";
	
	protected static final String BO_VIEW_URL = "view/";
	protected static final String BO_VIEW_PAGE = "@bo_page_view";
	
	protected static final String BO_NEW_URL = "new/";
	protected static final String BO_NEW_TRANSLATION_URL = "new/translation/";
	
	protected static final String BO_REMOVES_URL = "removes/";
	protected static final String BO_REMOVE_URL = "remove/";
	
	protected static final String BO_LIST_URL = "list/";
	protected static final String BO_LIST_PAGE = "@bo_page_list";

	protected static final String BO_BLOCK_LIST_URL = "blocklist/";
	protected static final String BO_BLOCK_LIST = "@bo_ng_block_list";

	protected static final String BO_FILE_HOME_URL = "";
	protected static final String BO_FILE_HOME_PAGE = "@bo_page_file";
	protected static final String BO_FILE_SINGLE_URL = "single/";
	protected static final String BO_FILE_SINGLE_PAGE = "@bo_page_file_single";
	protected static final String BO_FILE_ADD_URL = "add/";
	protected static final String BO_FILE_LIST_URL = "list/";
	protected static final String BO_FILE_REMOVE_URL = "remove/";
	protected static final String BO_FILE_RENAME_URL = "rename/";
	protected static final String BO_FILE_MOVE_URL = "move/";
	protected static final String BO_FILE_DOWNLOAD_URL = "download/";
	protected static final String BO_FILE_DOWNLOADMULTIPLE_URL = "downloadMultiple/";
	protected static final String BO_FILE_COMPRESS_URL = "compress/";
	
	protected static final Integer BO_MAX_REQUEST_ELEMENT = 1000;
		
	@Autowired
	protected EntityLocator entityLocator;
	
	@Autowired
	protected LangService langService;
	
	@Autowired
	protected BackOfficeService backOfficeService;
	
	protected Folder getBOFolder() throws ResourceNotFoundException, ControllerException{
		try {
			return common.getFolder(CommonUtil.BACK);
		} catch (UtilException e) {
			throw new ControllerException("Can't obtain BO folder", e);
		}
	}
	
	private ModelMap init() throws ServiceException{
		ModelMap modelMap = new ModelMap();
		modelMap.addAttribute("langs", langService.findAll());		
		modelMap.addAttribute("boContext", "/admin");
		return modelMap;
	}
	
	@Override
	public ModelAndView baseView(String pageName, Folder folder) throws ControllerException, ResourceNotFoundException {
		try{
			ModelAndView modelAndView = super.baseView(pageName, folder);
			modelAndView.addAllObjects(init());
			return modelAndView;
		} catch (ServiceException e) {
			throw new ControllerException(e);
		}
	}

	@InitBinder("objectEdit")
	protected void initBinderIdProvider(WebDataBinder binder) {
		System.out.println("1 - initBinderIdProvider " + binder.getFieldDefaultPrefix() + " " + binder.getFieldMarkerPrefix() + " " + binder.getFieldDefaultPrefix() + " " + binder.getObjectName() + " - " + binder.getTarget());

		binder.registerCustomEditor(IdProvider.class, new PropertyEditorSupport() {
		    @Override 
		    public void setAsText(final String objectTypeId) throws IllegalArgumentException
		    {
		    	if(objectTypeId == null || objectTypeId == "") setValue(null);
		    	else {
		    		setValue(backOfficeService.stringToIdProvider(objectTypeId));
		    	}
		    }
		    @Override
		    public String getAsText() {
			    if(getValue() == null) return "";
			    return backOfficeService.idProviderToString((IdProvider) getValue());
		    }
		});

		binder.registerCustomEditor(List.class, new CustomCollectionEditor(List.class) {

			@Override
			protected Object convertElement(Object element) {
				return element;
			}
			
			@Override
			public void setAsText(final String listString) {
		    	if(listString == null || listString.trim().length() == 0) {
		    		setValue(null);
		    		return;
		    	}

		    	if (backOfficeService.isNormalCollection(listString)){
					setValue(backOfficeService.stringToCollection(ArrayList.class, listString));
		    	} else {
		    		setValue(backOfficeService.stringToIdProviders(listString));
		    	}
			}

			@Override
			public String getAsText() {
				Collection<?> collection = (Collection<?>) getValue();
				
				if (collection == null) return "";
				
				//Get object type from first element
				Class<?> clazz = null;
				if (collection.size() > 0){
					clazz = collection.iterator().next().getClass();
				}
				
				if (clazz == null) return "";
				
				if (IdProvider.class.isAssignableFrom(clazz)){
					return backOfficeService.idProvidersToString(collection);
				} else {
					return backOfficeService.collectionToString(collection);
				}
			}
		});

		binder.registerCustomEditor(Set.class, new CustomCollectionEditor(Set.class) {

			@Override
			protected Object convertElement(Object element) {
				return element;
			}
			
			@Override
			public void setAsText(final String listString) {
		    	if(listString == null || listString.trim().length() == 0) {
		    		setValue(null);
		    		return;
		    	}

		    	if (backOfficeService.isNormalCollection(listString)){
					setValue(backOfficeService.stringToCollection(HashSet.class, listString));
		    	} else {
		    		setValue(backOfficeService.stringToIdProviders(listString));
		    	}
			}

			@Override
			public String getAsText() {
				Collection<?> collection = (Collection<?>) getValue();
				
				if (collection == null) return "";
				
				//Get object type from first element
				Class<?> clazz = null;
				if (collection.size() > 0){
					clazz = collection.iterator().next().getClass();
				}
				
				if (clazz == null) return "";
				
				if (IdProvider.class.isAssignableFrom(clazz)){
					return backOfficeService.idProvidersToString(collection);
				} else {
					return backOfficeService.collectionToString(collection);
				}
			}
		});
		
	}
	

}












