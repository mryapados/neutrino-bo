package org.neutrinocms.bo.controller.light;

import java.util.ArrayList;
import java.util.List;

import org.neutrinocms.core.exception.ControllerException;
import org.neutrinocms.core.exception.ResourceNotFoundException;
import org.neutrinocms.core.exception.ServiceException;
import org.neutrinocms.core.model.IdProvider;
import org.neutrinocms.core.util.CommonUtil;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class BackOfficeControllerRemove extends BackOfficeController {


	@RequestMapping(value = BO_REMOVE_URL, method = RequestMethod.POST) 
	public ModelAndView delete(@ModelAttribute("type") String type, @RequestParam("id") Integer id, RedirectAttributes redirectAttributes) throws ControllerException, ResourceNotFoundException {
		ModelAndView modelAndView = new ModelAndView("redirect:/" + CommonUtil.BO_URL + BO_LIST_URL);
		redirectAttributes.addAttribute("type", type);
		try {
			delete(type,  new Integer[]{id});
			redirectAttributes.addFlashAttribute("success", true);
		} catch (ControllerException e) {
			modelAndView = new ModelAndView("redirect:/" + CommonUtil.BO_URL + BO_VIEW_URL);
			redirectAttributes.addAttribute("id", id);
			redirectAttributes.addFlashAttribute("error", e);
			redirectAttributes.addFlashAttribute("success", false);
		}
		return modelAndView;
	}
	
	@RequestMapping(value = BO_REMOVES_URL, method = RequestMethod.POST) 
	public ModelAndView delete(@RequestParam("type") String type, @RequestParam("id") Integer[] ids, RedirectAttributes redirectAttributes) throws ResourceNotFoundException {
		ModelAndView modelAndView = new ModelAndView("redirect:/" + CommonUtil.BO_URL + BO_LIST_URL);
		try {
			delete(type, ids);
			redirectAttributes.addFlashAttribute("success", true);
		} catch (ControllerException e) {
			redirectAttributes.addFlashAttribute("error", e);
			redirectAttributes.addFlashAttribute("success", false);
		}
		redirectAttributes.addAttribute("type", type);
		return modelAndView;
	}
	
	public void delete(String type, Integer[] ids) throws ControllerException, ResourceNotFoundException {
		try {
			Class<?> object;
			object = entityLocator.getEntity(type).getClass();
			List<IdProvider> idProviders = new ArrayList<>();
			for (Integer id : ids) {
				IdProvider data = backOfficeService.getFullObject(object, id, null);
				idProviders.add(data);
			}
			backOfficeService.removeDatas(idProviders);
			
		} catch (ClassNotFoundException e) {
			throw new ResourceNotFoundException(type + " Not found !", e);
		} catch (ServiceException e) {
			throw new ControllerException(e);
		}
	}

}