package org.neutrinocms.bo.controller.light;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.neutrinocms.core.bean.NData;
import org.neutrinocms.core.conf.NeutrinoCoreProperties;
import org.neutrinocms.core.exception.ControllerException;
import org.neutrinocms.core.exception.ResourceNotFoundException;
import org.neutrinocms.core.exception.ServiceException;
import org.neutrinocms.core.model.IdProvider;
import org.neutrinocms.core.model.independant.Folder;
import org.neutrinocms.core.model.notranslation.NoTranslation;
import org.neutrinocms.core.model.translation.Lang;
import org.neutrinocms.core.model.translation.Translation;
import org.neutrinocms.core.util.CommonUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class BackOfficeControllerEdit extends BackOfficeController {
	@Autowired
	NeutrinoCoreProperties neutrinoCoreProperties;
	
	protected static final String REDIRECT = "redirect:/";
	protected static final String REDIRECT_TYPE = "type";
	protected static final String REDIRECT_ID = "id";
	
	protected static final String ATTR_BORESOURCES = "boResources";
	protected static final String ATTR_OBJECTTYPE = "objectType";
	protected static final String ATTR_OBJECTBASETYPE = "objectBaseType";
	protected static final String ATTR_OBJECTLANG = "objectLang";
	protected static final String ATTR_FIELD = "fields";
	protected static final String ATTR_OBJECTVIEW = "objectView";
	protected static final String ATTR_OBJECTEDIT = "objectEdit";
	protected static final String ATTR_OBJECTNAME = "objectName";
	protected static final String ATTR_TYPE = "type";
	protected static final String ATTR_ID = "id";
	protected static final String ATTR_LG = "lg";
	
	protected static final String COPY = " [Copy]";
	
	@RequestMapping(value = BO_EDIT_URL, method = RequestMethod.GET)
	public ModelAndView edit(@RequestParam(ATTR_TYPE) String type, @RequestParam(ATTR_ID) Integer id) throws ControllerException, ResourceNotFoundException   {
		return edit(type, id, null, false);
	}
	
	@RequestMapping(value = BO_EDIT_URL, method = RequestMethod.POST)
	public ModelAndView save(@RequestParam(ATTR_TYPE) String type, @RequestParam(ATTR_ID) Integer id, @Valid @ModelAttribute(ATTR_OBJECTEDIT) IdProvider data, BindingResult result, HttpServletRequest request, RedirectAttributes redirectAttributes) throws ControllerException, ResourceNotFoundException {
		ModelAndView modelAndView = null;
		if (result.hasErrors()) {
			System.err.println(result.getAllErrors());
			modelAndView = edit(type, id, null, true);
		} else{
			try {
				IdProvider idProvider = backOfficeService.saveData(data);
				modelAndView = new ModelAndView(REDIRECT + neutrinoCoreProperties.getBoUrl() + BO_VIEW_URL);
				redirectAttributes.addAttribute(REDIRECT_TYPE, idProvider.getObjectType());
				redirectAttributes.addAttribute(REDIRECT_ID, idProvider.getId());
				
			} catch (ServiceException e) {
				throw new ControllerException(e);
			}
		}
		return modelAndView;
	}
	
	public ModelAndView edit(String type, Integer id, Lang lang, Boolean saveError) throws ControllerException, ResourceNotFoundException   {
		//Si id = null cela signifie que c'est un nouveau objet, lang est utile si c'est un objet de type Translation
		try {
			Folder folder = getBOFolder();
			ModelAndView modelAndView = baseView(BO_EDIT_PAGE, folder);

			Class<?> object = entityLocator.getEntity(type).getClass();

			modelAndView.addObject(ATTR_OBJECTTYPE, object.getSimpleName());

			if (Translation.class.isAssignableFrom(object)){
				modelAndView.addObject(ATTR_OBJECTBASETYPE, Translation.class.getSimpleName());
			} else if (NoTranslation.class.isAssignableFrom(object)){
				modelAndView.addObject(ATTR_OBJECTBASETYPE, NoTranslation.class.getSimpleName());
			}
			
			modelAndView.addObject(ATTR_BORESOURCES, backOfficeService.getResources(object));

			NData<IdProvider> tData;
			if (id == null) {
				tData = backOfficeService.add(object);
				if (Translation.class.isAssignableFrom(object)) {
					Translation translation = (Translation) backOfficeService.translate((Translation) tData.getObjectData(), lang);
					modelAndView.addObject(ATTR_OBJECTLANG, translation.getLang());
				}
			} else {
				tData = backOfficeService.findOne(object, id);
			}

			modelAndView.addObject(ATTR_FIELD, tData.getFields());

			IdProvider objectData = tData.getObjectData();
			if (saveError != null && saveError) {
				modelAndView.addObject(ATTR_OBJECTVIEW, objectData);
			} else {
				modelAndView.addObject(ATTR_OBJECTVIEW, objectData);
				modelAndView.addObject(ATTR_OBJECTEDIT, objectData);
			}
			modelAndView.addObject(ATTR_OBJECTNAME, tData.getObjectData().getName());

			return modelAndView;

		} catch (ServiceException e) {
			throw new ControllerException(e);
		} catch (ClassNotFoundException e) {
			throw new ResourceNotFoundException(type + " Not found !", e);
		}

	}
	
	
	@RequestMapping(value = BO_NEW_TRANSLATION_URL, method = RequestMethod.GET)
	public ModelAndView add(@RequestParam(ATTR_TYPE) String type, @RequestParam(ATTR_LG) String langCode, @RequestParam(value = ATTR_ID, required = false) Integer id, HttpServletRequest request, RedirectAttributes redirectAttributes) throws ControllerException, ResourceNotFoundException   {
		try {
			Lang lang = langService.findByCode(langCode);
			if (lang == null) throw new ResourceNotFoundException(langCode + " Not found !");	
			if (id == null) return edit(type, id, lang, false);
			IdProvider added = copy(type, id, lang);
			ModelAndView modelAndView = new ModelAndView(REDIRECT + neutrinoCoreProperties.getBoUrl() + BO_EDIT_URL);
			redirectAttributes.addAttribute(REDIRECT_TYPE, added.getObjectType());
			redirectAttributes.addAttribute(REDIRECT_ID, added.getId());
			return modelAndView;
		} catch (ServiceException e) {
			throw new ControllerException(e);
		}
	}
	
	@RequestMapping(value = BO_NEW_URL, method = RequestMethod.GET)
	public ModelAndView add(@RequestParam(ATTR_TYPE) String type, @RequestParam(value = ATTR_ID, required = false) Integer id, HttpServletRequest request, RedirectAttributes redirectAttributes) throws ControllerException, ResourceNotFoundException   {
		if (id == null) return edit(type, id, null, false);
		IdProvider added = copy(type, id, null);
		ModelAndView modelAndView = new ModelAndView(REDIRECT + neutrinoCoreProperties.getBoUrl() + BO_EDIT_URL);
		redirectAttributes.addAttribute(REDIRECT_TYPE, added.getObjectType());
		redirectAttributes.addAttribute(REDIRECT_ID, added.getId());
		return modelAndView;
	}
	
	
	@RequestMapping(value = BO_NEW_TRANSLATION_URL, method = RequestMethod.POST)
	public ModelAndView neww(@RequestParam(ATTR_TYPE) String type, @RequestParam(ATTR_LG) String langCode, @Valid @ModelAttribute(ATTR_OBJECTEDIT) IdProvider data, BindingResult result, HttpServletRequest request, RedirectAttributes redirectAttributes) throws ControllerException, ResourceNotFoundException {
		try {
			Lang lang = langService.findByCode(langCode);
			if (lang == null) throw new ResourceNotFoundException(langCode + " Not found !");	

			ModelAndView modelAndView = null;
			if (result.hasErrors()) {
				modelAndView = edit(type, null, lang, true);
			} else{
				try {
					Translation translation = (Translation) backOfficeService.translate((Translation) data, lang);
					translation = (Translation) backOfficeService.saveData(translation);
					
					modelAndView = new ModelAndView(REDIRECT + neutrinoCoreProperties.getBoUrl() + BO_VIEW_URL);
					redirectAttributes.addAttribute(REDIRECT_TYPE, translation.getObjectType());
					redirectAttributes.addAttribute(REDIRECT_ID, translation.getId());
					
				} catch (ServiceException e) {
					throw new ControllerException(e);
				}
			}
			return modelAndView;
		
		} catch (ServiceException e) {
			throw new ControllerException(e);
		}
	}
	
	@RequestMapping(value = BO_NEW_URL, method = RequestMethod.POST)
	public ModelAndView neww(@RequestParam(ATTR_TYPE) String type, @Valid @ModelAttribute(ATTR_OBJECTEDIT) IdProvider data, BindingResult result, HttpServletRequest request, RedirectAttributes redirectAttributes) throws ControllerException, ResourceNotFoundException {
		ModelAndView modelAndView = null;
		if (result.hasErrors()) {
			modelAndView = edit(type, null, null, true);
		} else{
			try {
				IdProvider idProvider = backOfficeService.saveData(data);					
				
				modelAndView = new ModelAndView(REDIRECT + neutrinoCoreProperties.getBoUrl() + BO_VIEW_URL);
				redirectAttributes.addAttribute(REDIRECT_TYPE, idProvider.getObjectType());
				redirectAttributes.addAttribute(REDIRECT_ID, idProvider.getId());
				
			} catch (ServiceException e) {
				throw new ControllerException(e);
			}
		}
		return modelAndView;
	}

	public IdProvider copy(String type, Integer id, Lang lang) throws ControllerException, ResourceNotFoundException   {
		try {
			if (id == 0) throw new ControllerException("Id = 0");
			
			Class<?> object = entityLocator.getEntity(type).getClass();
			if (Translation.class.isAssignableFrom(object)) {
				Translation base = (Translation) backOfficeService.getFullObject(object, id, null);
				if (lang == null) lang = base.getLang();
				Translation translation = (Translation) backOfficeService.translate(base, lang);
				translation.setName(translation.getName() + COPY);
				return backOfficeService.saveData(translation);
			} else if(NoTranslation.class.isAssignableFrom(object)){
				NoTranslation noTranslation = (NoTranslation) backOfficeService.getFullObject(object, id, null);
				noTranslation.setId(null);
				noTranslation.setName(noTranslation.getName() + COPY);
				return backOfficeService.saveData(noTranslation);
			} else {
				IdProvider idProvider = backOfficeService.getFullObject(object, id, null);
				idProvider.setId(null);
				return backOfficeService.saveData(idProvider);
			}

		} catch (ServiceException e) {
			throw new ControllerException(e);
		} catch (ClassNotFoundException e) {
			throw new ResourceNotFoundException(type + " Not found !", e);
		}
	}

}












