package org.neutrinocms.bo.dbinitializer;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;

import org.apache.log4j.Logger;
import org.neutrinocms.bo.model.Link;
import org.neutrinocms.bo.service.light.LinkService;
import org.neutrinocms.core.conf.NeutrinoCoreProperties;
import org.neutrinocms.core.exception.ServiceException;
import org.neutrinocms.core.model.Authority;
import org.neutrinocms.core.model.AuthorityName;
import org.neutrinocms.core.model.independant.Folder;
import org.neutrinocms.core.model.independant.MapTemplate;
import org.neutrinocms.core.model.independant.NData;
import org.neutrinocms.core.model.independant.NSchema;
import org.neutrinocms.core.model.independant.NSchema.ScopeType;
import org.neutrinocms.core.model.independant.NType;
import org.neutrinocms.core.model.independant.NType.ValueType;
import org.neutrinocms.core.model.independant.Position;
import org.neutrinocms.core.model.independant.User;
import org.neutrinocms.core.model.translation.Lang;
import org.neutrinocms.core.model.translation.Page;
import org.neutrinocms.core.model.translation.Template;
import org.neutrinocms.core.model.translation.Translation;
import org.neutrinocms.core.service.AuthorityService;
import org.neutrinocms.core.service.FolderService;
import org.neutrinocms.core.service.LangService;
import org.neutrinocms.core.service.MapTemplateService;
import org.neutrinocms.core.service.NDataService;
import org.neutrinocms.core.service.NSchemaService;
import org.neutrinocms.core.service.PageService;
import org.neutrinocms.core.service.PositionService;
import org.neutrinocms.core.service.TObjectService;
import org.neutrinocms.core.service.TemplateService;
import org.neutrinocms.core.service.UserService;
import org.neutrinocms.core.util.IdProviderUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.encoding.ShaPasswordEncoder;
import org.springframework.stereotype.Component;


@Component
public class InitBO {
	private Logger logger = Logger.getLogger(InitBO.class);
	
	public InitBO() {

	}
	
	@Autowired
	private NeutrinoCoreProperties applicationProperties;
	
	@Autowired
	private AuthorityService authorityService;
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private LangService langService;
	
	@Autowired
	private TemplateService templateService;
	
	@Autowired
	private PositionService positionService;
	
	@Autowired
	private MapTemplateService mapTemplateService;
	
	@Autowired
	private PageService pageService;
	
	@Autowired
	private FolderService folderService;

	@Autowired
	private TObjectService tObjectService;

	@Autowired
	private NSchemaService nSchemaService;

	@Autowired
	private NDataService nDataService;
	
	@Autowired
	private LinkService linkService;
	
	@Autowired
	private IdProviderUtil idProviderUtil;
	
	Map<String, Folder> mapfolders;
	
	@PostConstruct
	public void run() throws ServiceException, InstantiationException, IllegalAccessException {
		logger.debug("init");
		if (!applicationProperties.getHibernateHbm2ddlAuto().equals("create-drop")) return;
		
		initFolders();
		
		initLangs();
		
		initAuthorities();
		initUsers();

		initBo();
	}

	private Lang langEN;
	private Lang langFR;
	private List<Lang> langs;
	private void initLangs() throws ServiceException{
		logger.debug("init langs");
		langs = new ArrayList<>();

		langEN = new Lang("en", "English");
		langs.add(langEN);
		langService.save(langEN);
		
		langFR = new Lang("fr", "Français");
		langs.add(langFR);
		langService.save(langFR);
		
	}
	
	private void initFolders() throws ServiceException{
		logger.debug("init initFolders");
		
		mapfolders = new HashMap<>();

		List<String> serverNames = null;
		Folder folder = null;
		
		folder = new Folder();
		folder.setName("back");
		serverNames = new ArrayList<>();
		serverNames.add("back");
		folder.setServerName(serverNames);
		folder.setPath("back/");
		folderService.save(folder);
		mapfolders.put(folder.getName(), folder);
	}
	
	private HashMap<AuthorityName, Authority> mapAuthorities;
	private void initAuthorities() throws ServiceException{
		logger.debug("init Authorities");
		mapAuthorities = new HashMap<>();
		for (AuthorityName authorityName : AuthorityName.values()) {
			Authority authority = new Authority();
			authority.setName(authorityName);
			authorityService.save(authority);
			mapAuthorities.put(authorityName, authority);
		}
	}
	
	private void initUsers() throws ServiceException{
		logger.debug("init users");
		User user = new User();
		user.setLogin("admin");
		ShaPasswordEncoder sha = new ShaPasswordEncoder();
		user.setEncryptPassword(sha.encodePassword("852963", null));
		user.setEnabled(true);
		user.setLastPasswordResetDate(new Date());
		List<Authority> authorities = new ArrayList<Authority>();
		authorities.add(mapAuthorities.get(AuthorityName.ROLE_ADMIN));
		user.setAuthorities(authorities);
		userService.save(user);

		user = new User();
		user.setLogin("mrYapados");
		sha = new ShaPasswordEncoder();
		user.setEncryptPassword(sha.encodePassword("852963", null));
		user.setEnabled(true);
		user.setLastPasswordResetDate(new Date());
		authorities = new ArrayList<Authority>();
		authorities.add(mapAuthorities.get(AuthorityName.ROLE_USER));
		user.setAuthorities(authorities);
		userService.save(user);
		
	}

	
	
	
	private Position mkPosition(String name) throws ServiceException{
		Position position = new Position();
		position.setName(name);
		positionService.save(position);
		return position;
	}
	private Position addPosition(Map<String, Position> mapPosition, String name) throws ServiceException {
		Position position = mkPosition(name);
		mapPosition.put(name, position);
		return position;
	}
	
	private MapTemplate addMapTemplate(Translation model, Translation block, Position position, Integer ordered) throws ServiceException{
		MapTemplate mapTemplate = new MapTemplate();
		mapTemplate.setModel(model);
		mapTemplate.setBlock((Template) block);
		mapTemplate.setPosition(position);
		mapTemplate.setOrdered(ordered);
		mapTemplateService.save(mapTemplate);
		return mapTemplate;
	}
	private Map<Lang, MapTemplate> addMapTemplate(Map<Lang, Translation> models, Map<Lang, Translation> blocks, Position position) throws ServiceException{
		Map<Lang, MapTemplate> mapTemplates = new HashMap<>();
		for (Lang lang : langs) {
			mapTemplates.put(lang, addMapTemplate(models.get(lang), blocks.get(lang), position, orderBlock(position)));
		}
		return mapTemplates;
	}
	
	private Map<Position, Integer> countPosition;
	private Integer orderBlock(Position position){
		if (countPosition == null) countPosition = new HashMap<>();
		Integer count;
		if (!countPosition.containsKey(position)){
			countPosition.put(position, 0);
		}
		count = countPosition.get(position) + 10;
		countPosition.put(position, count);
		return count;
	}
	
	

	private Map<Lang, Translation> mkPage(Page base, Folder folder, String name, String context, Map<Lang, Translation> models, Map<Lang, Translation> parents) throws ServiceException{
		List<Folder> folders = new ArrayList<>();
		folders.add(folder);
		return mkPage(base, folders, name, context, models, parents);
	}
	private Map<Lang, Translation> mkPage(Page base, List<Folder> folders, String name, String context, Map<Lang, Translation> models, Map<Lang, Translation> parents) throws ServiceException{
		Map<Lang, Translation> map = new HashMap<>();
		Page first = null;
		for (Lang lang : langs) {
			Page page = null;
			if (first == null){
				page = pageService.translate(base, lang);
				page.setName(name);
				if (base.getDescription() == null) page.setDescription(name + " Page description " + lang.getCode());
				page.setContext(context);
				page.setModel((Template) models.get(lang));
				page.setFolders(folders);
				if (parents != null) page.setParent((Page) parents.get(lang));
				first = page;
			} else {
				page = pageService.translate(first, lang);
				page.setFolders(folders);
				page.setName(name);
				if (base.getDescription() == null) page.setDescription(name + " Page description " + lang.getCode());
				if (parents != null) page.setParent((Page) parents.get(lang));
			}
						
			pageService.save(page);
			map.put(lang, page);
		}
		return map;
	}

	
	private Map<Lang, Translation> mkModel(Folder folder, String name) throws ServiceException{
		List<Folder> folders = new ArrayList<>();
		folders.add(folder);
		return mkModel(folders, name);
	}
	private Map<Lang, Translation> mkModel(List<Folder> folders, String name) throws ServiceException{
		return mkModel(folders, name, name + "/" + name);
	}
	private Map<Lang, Translation> mkModel(Folder folder, String name, String path) throws ServiceException{
		List<Folder> folders = new ArrayList<>();
		folders.add(folder);
		return mkModel(folders, name, path);
	}
	private Map<Lang, Translation> mkModel(List<Folder> folders, String name, String path) throws ServiceException{
		Map<Lang, Translation> map = new HashMap<>();
		Template first = null;
		for (Lang lang : langs) {
			Template template = null;
			if (first == null){
				template = templateService.translate(new Template(), lang);
				template.setName(name);
				template.setDescription(name + " Model description " + lang.getCode());
				template.setMetaTitle("{0}");
				template.setMetaDescription("MetaDescription");
				template.setPath(path);
				template.setKind(Template.TemplateKind.PAGE);
				template.setFolders(folders);
				templateService.save(template);
				first = template;
			} else {
				template = templateService.translate(first, lang);
				template.setFolders(folders);
				template.setName(name);
				template.setDescription(name + " Model description " + lang.getCode());
				templateService.save(template);
			}
			map.put(lang, template);
		}
		return map;

	}
	
	


	
	
	private Map<Lang, Translation> mkPageBlock(Folder folder, String name) throws ServiceException{
		List<Folder> folders = new ArrayList<>();
		folders.add(folder);
		return mkPageBlock(folders, name);
	}
	private Map<Lang, Translation> mkPageBlock(Folder folder, String name, String path) throws ServiceException{
		List<Folder> folders = new ArrayList<>();
		folders.add(folder);
		return mkPageBlock(folders, name, path);
	}
	private Map<Lang, Translation> mkPageBlock(Folder folder, String name, String path, NSchema nSchema) throws ServiceException{
		List<Folder> folders = new ArrayList<>();
		folders.add(folder);
		return mkPageBlock(folders, name, path, nSchema);
	}
	private Map<Lang, Translation> mkPageBlock(List<Folder> folders, String name) throws ServiceException{
		return mkPageBlock(folders, name, name + "/" + name, null);
	}
	private Map<Lang, Translation> mkPageBlock(List<Folder> folders, String name, String path) throws ServiceException{
		return mkPageBlock(folders, name, path, null);
	}
	private Map<Lang, Translation> mkPageBlock(List<Folder> folders, String name, String path, NSchema nSchema) throws ServiceException{
		Map<Lang, Translation> map = new HashMap<>();
		Template first = null;
		for (Lang lang : langs) {
			Template template = null;
			if (first == null){
				template = templateService.translate(new Template(), lang);
				template.setName(name);
				template.setDescription(name + " PageBlock description " + lang.getCode());
				template.setPath(path);
				template.setKind(Template.TemplateKind.PAGEBLOCK);
				template.setSchema(nSchema);
				template.setFolders(folders);
				templateService.save(template);
				first = template;
			} else {
				template = templateService.translate(first, lang);
				template.setFolders(folders);
				template.setName(name);
				template.setDescription(name + " PageBlock description " + lang.getCode());
				templateService.save(template);
			}
			map.put(lang, template);
		}
		return map;
	}
	
	
	
	
	
	
	
	private Map<Lang, Translation> mkElement(Folder folder, String name) throws ServiceException{
		List<Folder> folders = new ArrayList<>();
		folders.add(folder);
		return mkElement(folders, name);
	}
	private Map<Lang, Translation> mkElement(Folder folder, String name, String path) throws ServiceException{
		List<Folder> folders = new ArrayList<>();
		folders.add(folder);
		return mkElement(folders, name, path);
	}
	private Map<Lang, Translation> mkElement(Folder folder, String name, String path, NSchema nSchema) throws ServiceException{
		List<Folder> folders = new ArrayList<>();
		folders.add(folder);
		return mkElement(folders, name, path, nSchema);
	}
	private Map<Lang, Translation> mkElement(List<Folder> folders, String name) throws ServiceException{
		return mkElement(folders, name, name + "/" + name, null);
	}
	private Map<Lang, Translation> mkElement(List<Folder> folders, String name, String path) throws ServiceException{
		return mkElement(folders, name, path, null);
	}
	private Map<Lang, Translation> mkElement(List<Folder> folders, String name, String path, NSchema nSchema) throws ServiceException{
		Map<Lang, Translation> map = new HashMap<>();
		Template first = null;
		for (Lang lang : langs) {
			Template template = null;
			if (first == null){
				template = templateService.translate(new Template(), lang);
				template.setName(name);
				template.setDescription(name + " Element description " + lang.getCode());
				template.setPath(path);
				template.setKind(Template.TemplateKind.ELEMENT);
				template.setSchema(nSchema);
				template.setFolders(folders);
				first = template;
			} else {
				template = templateService.translate(first, lang);
				template.setFolders(folders);
				template.setName(name);
				template.setDescription(name + " Element description " + lang.getCode());
				
			}
			template.setController(name);
			templateService.save(template);
			map.put(lang, template);
		}
		return map;
	}
	
	
	private Map<Lang, Translation> mkBlock(Folder folder, String name) throws ServiceException{
		List<Folder> folders = new ArrayList<>();
		folders.add(folder);
		return mkBlock(folders, name);
	}
	private Map<Lang, Translation> mkBlock(Folder folder, String name, String path) throws ServiceException{
		List<Folder> folders = new ArrayList<>();
		folders.add(folder);
		return mkBlock(folders, name, path);
	}
	private Map<Lang, Translation> mkBlock(Folder folder, String name, String path, NSchema nSchema) throws ServiceException{
		List<Folder> folders = new ArrayList<>();
		folders.add(folder);
		return mkBlock(folders, name, path, nSchema);
	}
	private Map<Lang, Translation> mkBlock(List<Folder> folders, String name) throws ServiceException{
		return mkBlock(folders, name, name + "/" + name, null);
	}
	private Map<Lang, Translation> mkBlock(List<Folder> folders, String name, String path) throws ServiceException{
		return mkBlock(folders, name, path, null);
	}
	private Map<Lang, Translation> mkBlock(List<Folder> folders, String name, String path, NSchema nSchema) throws ServiceException{
		Map<Lang, Translation> map = new HashMap<>();
		Template first = null;
		for (Lang lang : langs) {
			Template template = null;
			if (first == null){
				template = templateService.translate(new Template(), lang);
				template.setName(name);
				template.setDescription(name + " Block description " + lang.getCode());
				template.setPath(path);
				template.setKind(Template.TemplateKind.BLOCK);
				template.setSchema(nSchema);
				template.setFolders(folders);
				first = template;
			} else {
				template = templateService.translate(first, lang);
				template.setFolders(folders);
				template.setName(name);
				template.setDescription(name + " Block description " + lang.getCode());
				
			}
			template.setController(name);
			templateService.save(template);
			map.put(lang, template);
		}
		return map;
	}

		
	private NSchema mkNSchema(Map<String, NType> columns, NSchema.ScopeType scopeType) throws ServiceException{
		NSchema nSchema = new NSchema();
		nSchema.setColumns(columns);
		nSchema.setScope(scopeType);
		nSchemaService.save(nSchema);
		return nSchema;
	}
	
	private Link addLink(Link master, Lang lang, String name, String title, String description, String url, String picto) throws ServiceException{
			Link link = linkService.translate(master, lang);
			link.setName(name);
			link.setDescription(description);
			link.setTitle(title);
			link.setUrl(url);
			link.setPicto(picto);
			link.setFolders(new ArrayList<>());
			linkService.save(link);
			return link;
	}
	
	private void generateMenu(Folder folder, Map<Lang, Translation> models, Position position) throws ServiceException{
		List<Folder> folders = new ArrayList<>();
		folders.add(folder);
		generateMenu(folders, models, position);
	}
	private void generateMenu(List<Folder> folders, Map<Lang, Translation> models, Position position) throws ServiceException{
		Map<String, NType> columns = new HashMap<>();
		columns.put("title", new NType(NType.ValueType.VARCHAR50));
		columns.put("links", new NType(NType.ValueType.COLLECTION, NType.ValueType.TOBJECT));
		Map<Lang, Translation> bMenuFree = mkBlock(folders, "@bo_block_menu_free", "header/menu/headerFree", mkNSchema(columns, ScopeType.ONE));
		
		Map<Lang, Translation> bMenuObjects = mkBlock(folders, "@bo_block_menu_objects", "header/menu/headerObjects");
		

		NData nData = null;
		NData nDataCollection = null;
		NData nDataCollectionItem = null;
		
		Map<Lang, MapTemplate> mtHeaderMenu1 = addMapTemplate(models, bMenuFree, position);
		
		Map<Lang, MapTemplate> mtHeaderMenu2 = addMapTemplate(models, bMenuObjects, position);
		
		//Menu Translated objects
		//EN
		nData = new NData();
		nData.setvVarchar50("Translated objects");
		nData.setVarType(ValueType.VARCHAR50);
		nData.setPropertyName("title");
		nData.setMapTemplate(mtHeaderMenu1.get(langEN));
		nDataService.save(nData);
		
		nDataCollection = new NData();
		nDataCollection.setvCollection(true);
		nDataCollection.setVarType(ValueType.COLLECTION);
		nDataCollection.setPropertyName("links");
		nDataCollection.setMapTemplate(mtHeaderMenu1.get(langEN));
		nDataService.save(nDataCollection);
		
		Link lkProjectEN = addLink(new Link(), langEN, "@bo_link_1", "Projects", null, null, null);
		nDataCollectionItem = new NData();
		nDataCollectionItem.setvTObject(lkProjectEN);
		nDataCollectionItem.setVarType(ValueType.TOBJECT);
		nDataCollectionItem.setOrdered(5);
		nDataCollectionItem.setData(nDataCollection);
		nDataService.save(nDataCollectionItem);
		
		Link lkAlbumEN = addLink(new Link(), langEN, "@bo_link_2", "Albums", null, null, null);
		nDataCollectionItem = new NData();
		nDataCollectionItem.setvTObject(lkAlbumEN);
		nDataCollectionItem.setVarType(ValueType.TOBJECT);
		nDataCollectionItem.setOrdered(10);
		nDataCollectionItem.setData(nDataCollection);
		nDataService.save(nDataCollectionItem);
		
		//FR
		nData = new NData();
		nData.setvVarchar50("Objets traduisibles");
		nData.setVarType(ValueType.VARCHAR50);
		nData.setPropertyName("title");
		nData.setMapTemplate(mtHeaderMenu1.get(langFR));
		nDataService.save(nData);
		
		nDataCollection = new NData();
		nDataCollection.setvCollection(true);
		nDataCollection.setVarType(ValueType.COLLECTION);
		nDataCollection.setPropertyName("links");
		nDataCollection.setMapTemplate(mtHeaderMenu1.get(langFR));
		nDataService.save(nDataCollection);
		
		Link lkProjectFR = addLink(new Link(), langFR, "@bo_link_1", "Projets", null, null, null);
		nDataCollectionItem = new NData();
		nDataCollectionItem.setvTObject(lkProjectFR);
		nDataCollectionItem.setVarType(ValueType.TOBJECT);
		nDataCollectionItem.setOrdered(5);
		nDataCollectionItem.setData(nDataCollection);
		nDataService.save(nDataCollectionItem);
		
		Link lkAlbumFR = addLink(new Link(), langFR, "@bo_link_2", "Albums", null, null, null);
		nDataCollectionItem = new NData();
		nDataCollectionItem.setvTObject(lkAlbumFR);
		nDataCollectionItem.setVarType(ValueType.TOBJECT);
		nDataCollectionItem.setOrdered(10);
		nDataCollectionItem.setData(nDataCollection);
		nDataService.save(nDataCollectionItem);
		
	}

	@SuppressWarnings("unused")
	private void initBo() throws ServiceException{
		//Folder
		Folder folder = mapfolders.get("back");
		
		Map<Lang, Translation> mlogin = mkModel(folder, "login", "login/login");
		Map<Lang, Translation> pgLogin = mkPage(new Page(), folder, "login", "static", mlogin, null);
				
		// Positions
		Map<String, Position> mapPosition = new HashMap<>();
		Position pHeader = addPosition(mapPosition, "@bo_header");
		Position pheaderMenu = addPosition(mapPosition, "@bo_headerMenu");
		Position pNav = addPosition(mapPosition, "@bo_nav");
		Position pAside = addPosition(mapPosition, "@bo_aside");
		Position pArticle = addPosition(mapPosition, "@bo_article");
		Position pFooter = addPosition(mapPosition, "@bo_footer");
		
		// Models
		Map<Lang, Translation> mHome = mkModel(folder, "@bo_model_home", "home/home");
		Map<Lang, Translation> mList = mkModel(folder, "@bo_model_list", "default/default");
		Map<Lang, Translation> mView = mkModel(folder, "@bo_model_view", "default/default");
		Map<Lang, Translation> mEdit = mkModel(folder, "@bo_model_edit", "default/default");
		Map<Lang, Translation> mFile = mkModel(folder, "@bo_model_file", "file/file");
		Map<Lang, Translation> mFileSingle = mkModel(folder, "@bo_model_file_single", "file/single");
		
		// Pages
		Map<Lang, Translation> pgHome = mkPage(new Page(), folder, "@bo_page_home", "home", mHome, null);
		Map<Lang, Translation> pgList = mkPage(new Page(), folder, "@bo_page_list", "default", mList, pgHome);
		Map<Lang, Translation> pgView = mkPage(new Page(), folder, "@bo_page_view", "default", mView, pgHome);
		Map<Lang, Translation> pgEdit = mkPage(new Page(), folder, "@bo_page_edit", "default", mEdit, pgHome);
		Map<Lang, Translation> pgFile = mkPage(new Page(), folder, "@bo_page_file", "default", mFile, pgHome);
		Map<Lang, Translation> pgFileSingle = mkPage(new Page(), folder, "@bo_page_file_single", "default", mFileSingle, null);
		
		// PageBlocks
		Map<Lang, Translation> pbHeader = mkPageBlock(folder, "@bo_pageblock_header", "header/header");
		
		// PageBlocks
		Map<Lang, Translation> pbFooter = mkPageBlock(folder, "@bo_pageblock_footer", "footer/footer");
				
		// Blocks
		generateMenu(folder, pbHeader, pheaderMenu);
		
		
		Map<Lang, Translation> bList = mkBlock(folder, "@bo_block_list", "list/list");
		Map<Lang, Translation> bView = mkBlock(folder, "@bo_block_view", "view/view");
		Map<Lang, Translation> bEdit = mkBlock(folder, "@bo_block_edit", "edit/edit");
		Map<Lang, Translation> bFile = mkBlock(folder, "@bo_block_file", "file/file");
		
		Map<Lang, Translation> bNgList = mkBlock(folder, "@bo_ng_block_list", "list/nglist");

		
		// Set MapTemplate
		Map<Lang, MapTemplate> mtHeaderList = addMapTemplate(mList, pbHeader, pHeader);
		Map<Lang, MapTemplate> mtArticleList = addMapTemplate(mList, bList, pArticle);
		Map<Lang, MapTemplate> mtFooterList = addMapTemplate(mList, pbFooter, pFooter);
		
		Map<Lang, MapTemplate> mtHeaderView = addMapTemplate(mView, pbHeader, pHeader);
		Map<Lang, MapTemplate> mtArticleView = addMapTemplate(mView, bView, pArticle);
		Map<Lang, MapTemplate> mtFooterView = addMapTemplate(mView, pbFooter, pFooter);
		
		Map<Lang, MapTemplate> mtHeaderEdit = addMapTemplate(mEdit, pbHeader, pHeader);
		Map<Lang, MapTemplate> mtArticleEdit = addMapTemplate(mEdit, bEdit, pArticle);
		Map<Lang, MapTemplate> mtFooterEdit = addMapTemplate(mEdit, pbFooter, pFooter);
		
//		Map<Lang, MapTemplate> mtHeaderFile = addMapTemplate(mFile, pbHeader, pHeader);
//		Map<Lang, MapTemplate> mtArticleFile = addMapTemplate(mFile, bFile, pArticle);
//		Map<Lang, MapTemplate> mtFooterFile = addMapTemplate(mFile, pbFooter, pFooter);
	
	}
	
	

	
	
	
	
}
