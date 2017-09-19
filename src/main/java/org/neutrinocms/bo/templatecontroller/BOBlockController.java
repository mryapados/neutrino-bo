package org.neutrinocms.bo.templatecontroller;

import org.apache.log4j.Logger;
import org.neutrinocms.bo.service.light.BackOfficeService;
import org.neutrinocms.core.bo.annotation.BlockMapping;
import org.neutrinocms.core.bo.annotation.TemplateController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.ui.ModelMap;

@TemplateController
@Component
public class BOBlockController {
	
	private Logger logger = Logger.getLogger(BOBlockController.class);
	
	@Autowired
	private BackOfficeService backOfficeService;
	
	@BlockMapping(value = "@bo_block_menu_objects")
	public ModelMap boBlockMenuObjects(){
		logger.debug("Enter in 'boBlockMenuObjects'");
		ModelMap modelMap = new ModelMap();
		modelMap.addAttribute("translationLinks", backOfficeService.getListTranslationObjectType());
		modelMap.addAttribute("noTranslationLinks", backOfficeService.getListNoTranslationObjectType());
		return modelMap;
	}
	
	
}
