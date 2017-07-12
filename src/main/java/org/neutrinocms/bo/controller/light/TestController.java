package org.neutrinocms.bo.controller.light;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.neutrinocms.core.bean.NDatas;
import org.neutrinocms.core.bean.NField;
import org.neutrinocms.core.dto.IdProviderDto;
import org.neutrinocms.core.exception.ControllerException;
import org.neutrinocms.core.exception.ResourceNotFoundException;
import org.neutrinocms.core.exception.ServiceException;
import org.neutrinocms.core.exception.UtilException;
import org.neutrinocms.core.model.IdProvider;
import org.neutrinocms.core.model.independant.Folder;
import org.neutrinocms.core.model.notranslation.NoTranslation;
import org.neutrinocms.core.model.translation.Lang;
import org.neutrinocms.core.model.translation.Template;
import org.neutrinocms.core.model.translation.Translation;
import org.neutrinocms.core.service.TemplateService;
import org.neutrinocms.core.specification.IdProviderSpecification;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.domain.Specifications;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class TestController {

	@RequestMapping(value = "test/", method = RequestMethod.GET)
	public ModelAndView test() {
	
			ModelAndView modelAndView = new ModelAndView("/test");


			return modelAndView;

	}

	
}