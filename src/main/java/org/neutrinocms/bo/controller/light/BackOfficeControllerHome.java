package org.neutrinocms.bo.controller.light;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.JspException;

import org.neutrinocms.core.exception.ControllerException;
import org.neutrinocms.core.exception.ResourceNotFoundException;
import org.neutrinocms.core.model.independant.Folder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class BackOfficeControllerHome extends BackOfficeController {

	@RequestMapping(value = BO_HOME_URL, method = RequestMethod.GET)
	public ModelAndView home() throws JspException, ResourceNotFoundException, ControllerException   {
		Folder folder = getBOFolder();
		return baseView(BO_HOME_PAGE, folder);
	}
	
	@RequestMapping(value = BO_LANGUAGE_URL, method = RequestMethod.GET)
	public ModelAndView language(HttpServletRequest request, RedirectAttributes redirectAttributes) throws ControllerException {
		String referer = request.getHeader("Referer");
		ModelAndView modelAndView = new ModelAndView("redirect:" + referer);
		return modelAndView;
	}
	
}












