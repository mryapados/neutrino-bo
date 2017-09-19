package org.neutrinocms.bo.controller.rest;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.neutrinocms.core.controller.AbstractController;
import org.neutrinocms.core.dto.BlockDto;
import org.neutrinocms.core.dto.FolderDto;
import org.neutrinocms.core.dto.LangDto;
import org.neutrinocms.core.dto.MapTemplateSimpleDto;
import org.neutrinocms.core.dto.PageDto;
import org.neutrinocms.core.dto.PositionDto;
import org.neutrinocms.core.dto.TemplateDto;
import org.neutrinocms.core.dto.TranslationDto;
import org.neutrinocms.core.exception.ControllerException;
import org.neutrinocms.core.exception.FormException;
import org.neutrinocms.core.exception.JSPNotFoundException;
import org.neutrinocms.core.exception.ResourceNotFoundException;
import org.neutrinocms.core.exception.ServiceException;
import org.neutrinocms.core.model.independant.Folder;
import org.neutrinocms.core.model.independant.MapTemplate;
import org.neutrinocms.core.model.independant.Position;
import org.neutrinocms.core.model.translation.Lang;
import org.neutrinocms.core.model.translation.Page;
import org.neutrinocms.core.model.translation.Template;
import org.neutrinocms.core.model.translation.Template.TemplateKind;
import org.neutrinocms.core.model.translation.Translation;
import org.neutrinocms.core.service.FolderService;
import org.neutrinocms.core.service.LangService;
import org.neutrinocms.core.service.MapTemplateService;
import org.neutrinocms.core.service.NDataService;
import org.neutrinocms.core.service.PageService;
import org.neutrinocms.core.service.PositionService;
import org.neutrinocms.core.service.TObjectService;
import org.neutrinocms.core.service.TemplateControllerExecutor;
import org.neutrinocms.core.service.TemplateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.security.access.annotation.Secured;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

@RestController
@Scope("prototype")
@RequestMapping(value = "/@back")
@Secured({ "ROLE_WEBMASTER", "ROLE_ADMIN" })
public class BackController extends AbstractController {
	
	@Autowired
	private LangService langService;
	
	@Autowired
	private FolderService folderService;
	
	@Autowired
	private PageService pageService;
	
	@Autowired
	private TemplateService templateService;

	@Autowired
	private PositionService positionService;
	
	@Autowired
	private MapTemplateService mapTemplateService;
	
	@Autowired
	private TObjectService tObjectService;
	
	@Autowired
	private NDataService nDataService;

	@Autowired
	private TemplateControllerExecutor templateControllerExecutor ;
	
	
	
//	@Deprecated
//	@RequestMapping(value = "/templates/exist/", method = RequestMethod.GET)
//	public @ResponseBody Boolean checkJSPExist(@ModelAttribute("context") String context, @ModelAttribute("type") String type, @ModelAttribute("path") String path, @ModelAttribute("name") String name) throws ServiceException {
//		System.out.println("context = " + context);
//		System.out.println("type = " + type);
//		System.out.println("path = " + path);
//		System.out.println("name = " + name);
//		
//		TemplateDto templateDto = new TemplateDto();
//		templateDto.setKind(TemplateKind.valueOf(type));
//		templateDto.setPath(path);
//		templateDto.setName(name);
//		Template template = TemplateDto.to(templateDto);
//		return templateService.checkJSPExist(common.getWebInfFolder(), context, template);
//	}

	@Deprecated
	@RequestMapping(value = "/positions", method = RequestMethod.GET)
	public @ResponseBody Map<String, PositionDto> getPositions() throws ServiceException {
		Iterable<Position> positions = positionService.findAll();
		Map<String, PositionDto> mapPositions = new HashMap<>();
		for (Position position : positions) {
			mapPositions.put(position.getName(), PositionDto.fromWithoutMapTemplate(position));
		}
		return mapPositions;
	}

	@Deprecated
	@RequestMapping(value = "/templates/{name}", method = RequestMethod.GET)
	public @ResponseBody TemplateDto getTemplate(@PathVariable(value = "name") String templateName) throws ServiceException {
		Template template = templateService.findByName(templateName);
		return TemplateDto.from(template);
	}
	@Deprecated
	@RequestMapping(value = "/templates/{name}", method = RequestMethod.PUT)
	public @ResponseBody TemplateDto updateTemplate(@PathVariable(value = "name") String templateName,@Valid @RequestBody TemplateDto templateDto, BindingResult result) throws ServiceException, FormException {
		if (result.hasErrors()){
			List<String> errors = new ArrayList<>();
			for (ObjectError objectError : result.getAllErrors()) {
				errors.add(objectError.getDefaultMessage());
				System.out.println(objectError.getDefaultMessage());
			}
			throw new FormException("form errors", result.getAllErrors());			
		}
		return TemplateDto.from(templateService.save(TemplateDto.to(templateDto)));
	}

	
	

	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	


	@RequestMapping(value = "/parsedblock/{pageId}/{mapTemplateId}/{activeObjectId}", method = RequestMethod.GET)
	public ModelAndView getParsedBlockWithActiveObject(HttpServletRequest request, HttpServletResponse response, @PathVariable(value = "pageId") Integer pageId, @PathVariable(value = "mapTemplateId") Integer mapTemplateId, @PathVariable(value = "activeObjectId") Integer activeObjectId, @RequestParam(value = "folderId", required = false) Integer folderId, Folder folder) throws ControllerException, ResourceNotFoundException {
		return getParsedBlock(request, response, pageId, mapTemplateId, activeObjectId, folderId, folder);
	}

	@RequestMapping(value = "/parsedblock/{pageId}/{mapTemplateId}/", method = RequestMethod.GET)
	public ModelAndView getParsedBlockWithoutActiveObject(HttpServletRequest request, HttpServletResponse response, @PathVariable(value = "pageId") Integer pageId, @PathVariable(value = "mapTemplateId") Integer mapTemplateId, @RequestParam(value = "folderId", required = false) Integer folderId, Folder folder) throws ControllerException, ResourceNotFoundException {
		return getParsedBlock(request, response, pageId, mapTemplateId, null, folderId, folder);
	}
	public ModelAndView getParsedBlock(HttpServletRequest request, HttpServletResponse response, Integer pageId, Integer mapTemplateId, Integer activeObjectId, Integer folderId, Folder folder) throws ControllerException, ResourceNotFoundException {
		ModelAndView modelAndView = null;
		try {
			if (folderId != null) folder = folderService.findOne(folderId);
			MapTemplate mapTemplate =  mapTemplateService.findOne(mapTemplateId);
			if (mapTemplate == null) throw new ResourceNotFoundException("mapTemplate id " + mapTemplateId + " not found !");
			
			Page page = pageService.findOne(pageId);
			if (page == null) throw new ResourceNotFoundException("Page id " + pageId + " not found !");
			Template block = mapTemplate.getBlock();

			Translation activeObject = null;
			if (activeObjectId != null){
				activeObject = tObjectService.findOne(activeObjectId);
				if (activeObject == null) throw new ResourceNotFoundException("activeObject id " + activeObjectId + " not found !");
			}
			
			//TODO params
			ModelMap modelMap = templateControllerExecutor.execute(block.getController(), page, mapTemplate.getModel(), activeObject, block, folder, page.getLang(),  null, null);	
			Map<String, Object> nDatas = nDataService.getNDatas(mapTemplate);
			
			modelAndView = baseView(page, block, activeObject, folder);
			
			modelAndView.addObject("page", page);
			modelAndView.addObject("activeBlock", block);
			modelAndView.addObject("nDatas", nDatas);
			modelAndView.addAllObjects(modelMap);
			
			response.addHeader("Object-Type", "parsedBlock");  
		} catch (JSPNotFoundException e) {
			throw new ResourceNotFoundException(e);
		} catch (ServiceException e) {
			throw new ControllerException(e);
		}
		return modelAndView;
	}
	

	@RequestMapping(value = "/tobject", method = RequestMethod.GET, params={"id"})
	public @ResponseBody TranslationDto getTObject(@RequestParam(value = "id", required = true) Integer id) throws ControllerException, ResourceNotFoundException {
		try {
			Translation translation = tObjectService.findOne(id);
			return TranslationDto.from(translation);
		} catch (ServiceException e) {
			throw new ControllerException(e);
		}
	}
	
	
	@RequestMapping(value = "/folder", method = RequestMethod.GET)
	public @ResponseBody List<FolderDto> getFolders() throws ControllerException, ResourceNotFoundException {
		try {
			List<Folder> folders = (List<Folder>) folderService.findAll();
			List<FolderDto> foldersDto = new ArrayList<>();
			for (Folder folder : folders) {
				foldersDto.add(FolderDto.from(folder));
			}
			return foldersDto;
		} catch (ServiceException e) {
			throw new ControllerException(e);
		}
	}
	@RequestMapping(value = "/folder", method = RequestMethod.GET, params={"id"})
	public @ResponseBody FolderDto getFolder(@RequestParam(value = "id", required = true) Integer id) throws ControllerException, ResourceNotFoundException {
		try {
			Folder folder = folderService.findOne(id);
			return FolderDto.from(folder);
		} catch (ServiceException e) {
			throw new ControllerException(e);
		}
	}
	@RequestMapping(value = "/folder", method = RequestMethod.GET, params={"name"})
	public @ResponseBody FolderDto getFolderByName(@RequestParam(value = "name", required = true) String name) throws ControllerException, ResourceNotFoundException {
		Folder folder = folderService.findByName(name);
		return FolderDto.from(folder);
	}

	@RequestMapping(value = "/page", method = RequestMethod.GET)
	public @ResponseBody PageDto getPage(@RequestParam(value = "id", required = true) Integer id) throws ControllerException, ResourceNotFoundException {
		try {
			Page page = pageService.findOne(id);
			return PageDto.from(page);
		} catch (ServiceException e) {
			throw new ControllerException(e);
		}
	}
	
	@RequestMapping(value = "/template", method = RequestMethod.GET)
	public @ResponseBody List<TemplateDto> getTemplates() throws ControllerException, ResourceNotFoundException {
		try {
			List<Template> templates = templateService.findAllBlockAndPageBlock();
			List<TemplateDto> templatesDto = new ArrayList<>();
			for (Template template : templates) {
				templatesDto.add(TemplateDto.from(template));
			}
			return templatesDto;
		} catch (ServiceException e) {
			throw new ControllerException(e);
		}
	}
	@RequestMapping(value = "/template", method = RequestMethod.GET, params={"id"})
	public @ResponseBody TemplateDto getTemplate(@RequestParam(value = "id", required = true) Integer id) throws ControllerException, ResourceNotFoundException {
		try {
			Template template = templateService.findOne(id);
			return TemplateDto.from(template);
		} catch (ServiceException e) {
			throw new ControllerException(e);
		}
	}
	
	
	@RequestMapping(value = "/lang", method = RequestMethod.GET)
	public @ResponseBody List<LangDto> getLangs() throws ControllerException, ResourceNotFoundException {
		try {
			Iterable<Lang> langs = langService.findAll();
			List<LangDto> langsDto = new ArrayList<>();
			for (Lang lang : langs) {
				langsDto.add(LangDto.from(lang));
			}
			return langsDto;
		} catch (ServiceException e) {
			throw new ControllerException(e);
		}
	}
	@RequestMapping(value = "/lang", method = RequestMethod.GET, params={"id"})
	public @ResponseBody LangDto getLang(@RequestParam(value = "id", required = true) Integer id) throws ControllerException, ResourceNotFoundException {
		try {
			Lang lang = langService.findOne(id);
			return LangDto.from(lang);
		} catch (ServiceException e) {
			throw new ControllerException(e);
		}
	}
	@RequestMapping(value = "/lang", method = RequestMethod.GET, params={"code"})
	public @ResponseBody LangDto getLangByCode(@RequestParam(value = "code", required = true) String code) throws ControllerException, ResourceNotFoundException {
		try {
			Lang lang = langService.findByCode(code);
			return LangDto.from(lang);
		} catch (ServiceException e) {
			throw new ControllerException(e);
		}
	}

	
	//Model : Page ou PageBlock
	//ActiveObject : ActiveObject
	//Position : Position
	@RequestMapping(value = "/block", method = RequestMethod.GET, params={"modelId", "pageId", "activeObjectId", "positionId"})
	public @ResponseBody List<BlockDto> getBlocksForPosition(
			@RequestParam(value = "modelId", required = true) Integer modelId, 
			@RequestParam(value = "pageId", required = true) Integer pageId, 
			@RequestParam(value = "activeObjectId", required = true) Integer activeObjectId, 
			@RequestParam(value = "positionId", required = true) Integer positionId) throws ControllerException, ResourceNotFoundException {
		
		try {
			List<BlockDto> blockDtos = new ArrayList<>();
			
			List<Translation> models = new ArrayList<>();
			models.add(templateService.findOne(modelId));
			models.add(pageService.findOne(pageId));
			if (activeObjectId != null){
				models.add(tObjectService.findOne(activeObjectId));
			}
	
			Position pos = positionService.findOneForObjectsWithMaps(models, positionId);
	
			if (pos != null){
				List<MapTemplate> mapTemplates;
				mapTemplates = pos.getMapTemplates();
				for (MapTemplate mapTemplate : mapTemplates) {
					blockDtos.add(BlockDto.from(mapTemplate));
				}
			}		
		return blockDtos;
		} catch (ServiceException e) {
			throw new ControllerException(e);
		}
	}
	
	@RequestMapping(value = "/mapTemplate", method = RequestMethod.POST)
	public @ResponseBody BlockDto saveMapTemplate(@Valid @RequestBody MapTemplateSimpleDto mapTemplateSimpleDto, BindingResult result) throws ControllerException, ResourceNotFoundException, FormException {
		try {
			if (result.hasErrors()){
				List<String> errors = new ArrayList<>();
				for (ObjectError objectError : result.getAllErrors()) {
					errors.add(objectError.getDefaultMessage());
					System.out.println(objectError.getDefaultMessage());
				}
				throw new FormException("form errors", result.getAllErrors());			
			}
	
			MapTemplate mapTemplate = new MapTemplate();
			mapTemplate.setModel(tObjectService.findOne(mapTemplateSimpleDto.getModelId()));
			mapTemplate.setBlock(templateService.findOne(mapTemplateSimpleDto.getBlockId()));
			mapTemplate.setPosition(positionService.findOne(mapTemplateSimpleDto.getPositionId()));
			mapTemplate.setOrdered(mapTemplateSimpleDto.getOrdered());
		return BlockDto.from(mapTemplateService.saveAndOrder(mapTemplate));
		} catch (ServiceException e) {
			throw new ControllerException(e);
		}
	}
	@RequestMapping(value = "/mapTemplate", method = RequestMethod.DELETE, params={"id"})
	public BlockDto deleteMapTemplate(@RequestParam(value = "id", required = true) Integer id) throws ControllerException, ResourceNotFoundException {
		try {
			MapTemplate mapTemplate = mapTemplateService.findOne(id);
			mapTemplateService.removeById(id);
			return BlockDto.from(mapTemplate);
		} catch (ServiceException e) {
			throw new ControllerException(e);
		}
	}
	
	
}