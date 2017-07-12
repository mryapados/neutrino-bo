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
public class BackOfficeControllerList extends BackOfficeController {
	@Autowired
	private TemplateService templateService;

	protected static final String BO_OBJECTS = "objects/";
	
	@RequestMapping(value = BO_LIST_URL, method = RequestMethod.GET)
	public ModelAndView list(@ModelAttribute("type") String type, Pageable pageRequest) throws ControllerException, ResourceNotFoundException {

		try {
			Folder folder = getBOFolder();
			ModelAndView modelAndView = baseView(BO_LIST_PAGE, folder);
			
			Class<?> object = entityLocator.getEntity(type).getClass();
			modelAndView.addObject("objectType", object.getSimpleName());
			if (Translation.class.isAssignableFrom(object)){
				modelAndView.addObject("objectBaseType", Translation.class.getSimpleName());
			} else if (NoTranslation.class.isAssignableFrom(object)){
				modelAndView.addObject("objectBaseType", NoTranslation.class.getSimpleName());
			}
			
			modelAndView.addObject("boResources", backOfficeService.getResources(object));
			
			
			NDatas<IdProvider> tDatas = backOfficeService.findAll(object, pageRequest);

			modelAndView.addObject("objectDatas", tDatas.getObjectDatas());
			modelAndView.addObject("datas", tDatas.getObjectDatas().getContent());
			modelAndView.addObject("fields", tDatas.getFields());

			return modelAndView;
		} catch (ServiceException e) {
			throw new ControllerException(e);
		} catch (ClassNotFoundException e) {
			throw new ResourceNotFoundException(type + " Not found !", e);
		}
		
	}

	@RequestMapping(value = BO_BLOCK_LIST_URL + "{type}/{id}/{field}", method = RequestMethod.GET)
	public ModelAndView getAssignableblocklist(HttpServletRequest request, HttpServletResponse response, @PathVariable(value = "type") String ownerType, @PathVariable(value = "id") Integer ownerId, @PathVariable(value = "field") String ownerField, Pageable pageRequest) throws ResourceNotFoundException, ControllerException {
		try {
			Lang lang = common.getLang(LocaleContextHolder.getLocale().getLanguage());
			Folder folder = getBOFolder();
			
			org.neutrinocms.core.model.translation.Page page = common.getPage(folder, BO_LIST_PAGE, lang);
			Template block = templateService.identify(folder, BO_BLOCK_LIST, lang);

			ModelAndView modelAndView = baseView(page, block, null, folder);

			modelAndView.addObject("page", page);
			modelAndView.addObject("activeBlock", block);
			response.addHeader("Object-Type", "parsedBlock");  

			Class<?> ownerObject = entityLocator.getEntity(ownerType).getClass();
			NField nField = backOfficeService.getNField(ownerObject, ownerField);

			Boolean many = Iterable.class.isAssignableFrom(nField.getClazz());
			modelAndView.addObject("many", many);
			
			Class<?> recipientObject = entityLocator.getEntity(many ? nField.getOfClassName() : nField.getClassName()).getClass();
			modelAndView.addObject("objectType", recipientObject.getSimpleName());

			if (Translation.class.isAssignableFrom(recipientObject.getClass())){
				modelAndView.addObject("objectBaseType", Translation.class.getSimpleName());
			} else if (NoTranslation.class.isAssignableFrom(recipientObject.getClass())){
				modelAndView.addObject("objectBaseType", NoTranslation.class.getSimpleName());
			}
		
			String recipientField = nField.getReverseJoin();
			NDatas<IdProvider> tDatas = null;
			if (recipientField != null){
				Specification<IdProvider> spec = IdProviderSpecification.itsFieldIsAffectedTo(recipientField, ownerId);
				spec = Specifications.where(spec).or(IdProviderSpecification.isNotAffected(recipientField));
				tDatas = backOfficeService.findAll(recipientObject, pageRequest, spec);
			} else {
				tDatas = backOfficeService.findAll(recipientObject, pageRequest);
			}
			
			modelAndView.addObject("objectDatas", tDatas.getObjectDatas());
			modelAndView.addObject("datas", tDatas.getObjectDatas().getContent());
			modelAndView.addObject("fields", tDatas.getFields());

			return modelAndView;
			
		} catch (ServiceException e) {
			throw new ControllerException(e);
		} catch (ClassNotFoundException e) {
			throw new ResourceNotFoundException("Ressource not found !", e);
		} catch (UtilException e) {
			throw new ControllerException(e);
		}
	}

	@RequestMapping(value = BO_LIST_URL + BO_OBJECTS + "{type}", method = RequestMethod.GET)
	public @ResponseBody List<IdProviderDto> getObjects(@PathVariable(value = "type") String type, @RequestParam("id") Integer[] ids) throws ControllerException, ResourceNotFoundException {
		try {
			Class<?> object;
			object = entityLocator.getEntity(type).getClass();
			
			// Permet de limiter la requete
			Pageable pageRequest = new PageRequest(0, BO_MAX_REQUEST_ELEMENT);
			List<Integer> list = new ArrayList<>(Arrays.asList(ids));
			
			Specification<IdProvider> spec = IdProviderSpecification.idIn(list);
			Page<IdProvider> datas = backOfficeService.getFullObjects(object, pageRequest, spec);
			
			List<IdProviderDto> idProviderDtos = new ArrayList<>();
			for (IdProvider idProvider : datas) {
				idProviderDtos.add(IdProviderDto.from(idProvider));
			}
			
			return idProviderDtos;
		} catch (ClassNotFoundException e) {
			throw new ResourceNotFoundException(type + " Not found !", e);
		} catch (ServiceException e) {
			throw new ControllerException(e);
		}

	}

}