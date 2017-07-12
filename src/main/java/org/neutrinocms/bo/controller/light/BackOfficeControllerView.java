package org.neutrinocms.bo.controller.light;

import org.neutrinocms.core.bean.NData;
import org.neutrinocms.core.exception.ControllerException;
import org.neutrinocms.core.exception.ResourceNotFoundException;
import org.neutrinocms.core.exception.ServiceException;
import org.neutrinocms.core.model.IdProvider;
import org.neutrinocms.core.model.independant.Folder;
import org.neutrinocms.core.model.notranslation.NoTranslation;
import org.neutrinocms.core.model.translation.Translation;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class BackOfficeControllerView extends BackOfficeController {

	@RequestMapping(value = BO_VIEW_URL, method = RequestMethod.GET)
	public ModelAndView view(@ModelAttribute("type") String type, @ModelAttribute("id") Integer id) throws ControllerException, ResourceNotFoundException   {
		try {
			Folder folder = getBOFolder();
			ModelAndView modelAndView = baseView(BO_VIEW_PAGE, folder);
			Class<?> object = entityLocator.getEntity(type).getClass();
			
			System.out.println("		Superclass = " + object.getSuperclass());
			
			modelAndView.addObject("objectType", object.getSimpleName());
			
			NData<IdProvider> tData = backOfficeService.findOne(object, id);
			modelAndView.addObject("fields", tData.getFields());
			modelAndView.addObject("objectView", tData.getObjectData());
			modelAndView.addObject("objectName", tData.getObjectData().getName());
			if (Translation.class.isAssignableFrom(object)){
				Translation translation = (Translation) tData.getObjectData();
				modelAndView.addObject("objectLang", translation.getLang());
				modelAndView.addObject("objectBaseType", Translation.class.getSimpleName());
			} else if (NoTranslation.class.isAssignableFrom(object)){
				modelAndView.addObject("objectBaseType", NoTranslation.class.getSimpleName());
			}
			
			modelAndView.addObject("boResources", backOfficeService.getResources(object));
			modelAndView.addObject("boViewUrl", backOfficeService.getViewUrl(object));
			
			return modelAndView;
		} catch (ServiceException e) {
			throw new ControllerException(e);
		} catch (ClassNotFoundException e) {
			throw new ResourceNotFoundException(type + " Not found !", e);
		}
		
	}


}












