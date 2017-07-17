package org.neutrinocms.bo.service.light;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.SortedSet;
import java.util.TreeMap;
import java.util.TreeSet;

import javax.persistence.EntityManager;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.PersistenceContext;

import org.apache.log4j.Logger;
import org.neutrinocms.core.bean.NData;
import org.neutrinocms.core.bean.NDatas;
import org.neutrinocms.core.bean.NField;
import org.neutrinocms.core.bean.NResource;
import org.neutrinocms.core.bo.annotation.BOField;
import org.neutrinocms.core.bo.annotation.BOField.SortType;
import org.neutrinocms.core.bo.annotation.BOResource;
import org.neutrinocms.core.bo.annotation.BOResources;
import org.neutrinocms.core.bo.annotation.BOViewUrl;
import org.neutrinocms.core.constant.CacheConst;
import org.neutrinocms.core.exception.ServiceException;
import org.neutrinocms.core.model.IdProvider;
import org.neutrinocms.core.model.notranslation.NoTranslation;
import org.neutrinocms.core.model.translation.Lang;
import org.neutrinocms.core.model.translation.Translation;
import org.neutrinocms.core.service.TemplateService;
import org.neutrinocms.core.service.TranslationService;
import org.neutrinocms.core.util.CommonUtil;
import org.neutrinocms.core.util.EntityLocator;
import org.neutrinocms.core.util.IdProviderUtil;
import org.neutrinocms.core.util.ServiceLocator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.context.annotation.Scope;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.web.util.HtmlUtils;



@Service
@Scope(value = "singleton")
public class BackOfficeService {
	
	
	
	@PersistenceContext
	private EntityManager em;

	@Autowired
	private TranslationService<Translation> translationService;
	
	@Autowired
	private TemplateService templateService;
	
	@Autowired
	private ServiceLocator customServiceLocator;
	
	@Autowired
	private EntityLocator entityLocator;
	
	@Autowired
	private CommonUtil commonUtil;
	
	@Autowired
	private  IdProviderUtil idProviderUtil;
	
	
	private Logger logger = Logger.getLogger(BackOfficeService.class);
		
	private List<Field> getFields(Class<?> classObject) throws ServiceException{
		List<Field> fields = new ArrayList<>();
		Class<?> superClass = classObject.getSuperclass();
		if (superClass != null){
			fields.addAll(getFields(classObject.getSuperclass()));
		}
		Field[] classObjectFields = classObject.getDeclaredFields();
		for (Field classObjectField : classObjectFields) {
			BOField annotation = classObjectField.getAnnotation(BOField.class);
			if (annotation != null){
				fields.add(classObjectField);
			}
		}
		return fields;
	}

	private Map<String, Field> getMapFields(Class<?> classObject) throws ServiceException{
		Map<String, Field> fields = new HashMap<>();
		Class<?> superClass = classObject.getSuperclass();
		if (superClass != null){
			fields.putAll(getMapFields(classObject.getSuperclass()));
		}
		Field[] classObjectFields = classObject.getDeclaredFields();
		for (Field classObjectField : classObjectFields) {
			BOField annotation = classObjectField.getAnnotation(BOField.class);
			if (annotation != null){
				fields.put(classObjectField.getName(), classObjectField);
			}
		}
		return fields;
	}
	

	private NField mkNFieldFromBOField(Field field, BOField nType){
		List<String> enumDatas = null;
		if (!nType.ofEnum().equals(BOField.Default.class)){
			enumDatas = new ArrayList<>();
			for (Enum<?> e : nType.ofEnum().getEnumConstants()) {
				enumDatas.add(e.name());
			}
		}
		Class<?> clazz = null;
		String className = null;
		String ofClassName = null;
		
	    Type type = field.getGenericType();
	    if (type instanceof ParameterizedType) {
	        ParameterizedType pType = (ParameterizedType)type;
	        clazz = ((Class<?>) pType.getRawType());
	        className = clazz.getSimpleName();
	        ofClassName = ((Class<?>) pType.getActualTypeArguments()[0]).getSimpleName();
	    } else {
	    	clazz = ((Class<?>) field.getType());
	    	className = clazz.getSimpleName();
	    }
	    
		NField nField = new NField(field, nType.type(), nType.ofType(), field.getName(), clazz, className, ofClassName, nType.inList(), nType.inView(), nType.editable(), nType.sortBy(), nType.sortPriority(), nType.defaultField(), nType.displayOrder(), nType.tabName(), nType.groupName(), enumDatas, nType.defaultValue());
	
		String reverseJoin = null;
		Boolean reverseIsCollection = null;
		
		if (reverseJoin == null){
			OneToMany oneToManyAnnotation = field.getAnnotation(OneToMany.class);
			if (oneToManyAnnotation != null) {
				reverseJoin = oneToManyAnnotation.mappedBy();
				reverseIsCollection = false;
			}
		}
		if (reverseJoin == null){
			ManyToMany manyToManyAnnotation = field.getAnnotation(ManyToMany.class);
			if (manyToManyAnnotation != null) {
				reverseJoin = manyToManyAnnotation.mappedBy();
				reverseIsCollection = true;
			}
		}
		if (reverseJoin != null && reverseJoin.equals("")) reverseJoin = null;
		nField.setReverseJoin(reverseJoin);
		nField.setReverseIsCollection(reverseIsCollection);
		
		return nField;
	}
	
	
	public List<NField> getNFields(Class<?> entity) throws ServiceException{		
		List<Field> fields = getFields(entity);
		return getNFields(fields);
	}	
	
	private List<NField> getNFields(List<Field> fields) throws ServiceException {
		List<NField> nfFields = new ArrayList<>();
		for (Field field : fields) {
			BOField nType = field.getAnnotation(BOField.class);
			if (nType != null){
				nfFields.add(mkNFieldFromBOField(field, nType));
			}
		}
		return nfFields;
	}
	

	public NField getNField(Class<?> entity, String fieldName)throws ServiceException {
		Map<String, Field> mapFields = getMapFields(entity);
		Field field = mapFields.get(fieldName);
		if (field == null) return null;
		BOField nType = field.getAnnotation(BOField.class);
		if (nType != null){
			return mkNFieldFromBOField(field, nType);
		}
		return null;
	}
	

	
	
	
	//Retourne une liste de NField par GroupName par TabName
	private Map<String, Map<String, List<NField>>> getMapNField(List<Field> fields) throws ServiceException{
		Map<String, Map<String, List<NField>>> nfTabsGroupsFields = new HashMap<>();
		for (Field field : fields) {
			BOField nType = field.getAnnotation(BOField.class);
			if (nType != null){
				if (!nfTabsGroupsFields.containsKey(nType.tabName())){
					nfTabsGroupsFields.put(nType.tabName(), new HashMap<>());
				}
				Map<String, List<NField>> nfGroupsFields = nfTabsGroupsFields.get((nType.tabName()));
				
				if (!nfGroupsFields.containsKey(nType.groupName())){
					nfGroupsFields.put(nType.groupName(), new ArrayList<>());
				}
				List<NField> nfFields = nfGroupsFields.get((nType.groupName()));
				nfFields.add(mkNFieldFromBOField(field, nType));
			}
		}
		return nfTabsGroupsFields;
	}

	public String getViewUrl(Class<?> entity) throws ServiceException{
		BOViewUrl viewUrl = entity.getAnnotation(BOViewUrl.class);
		if (viewUrl != null) return viewUrl.value();
		else return null;
	}

	private NResource mkNResourceFromBOResource(BOResource nResource){
		return new NResource(nResource.type(), nResource.value());
	}
	public List<NResource> getResources(Class<?> entity) throws ServiceException{
		List<NResource> list = new ArrayList<>();
		BOResources resources = entity.getAnnotation(BOResources.class);
		if (resources != null){
			BOResource[] value = resources.value();
			for (BOResource boResource : value) {
				list.add(mkNResourceFromBOResource(boResource));
			}
		}
		return list;
	}
	
	
	public NDatas<IdProvider> findAll(Class<?> entity, Pageable pageable) throws ServiceException{		
		return findAll(entity, pageable, null);
	}

	public NDatas<IdProvider> findAll(Class<?> entity, Pageable pageable, Specification<IdProvider> spec) throws ServiceException{		
		List<Field> fields = getFields(entity);
		List<NField> nFields = getNFields(fields);
		pageable = transformPageRequest(nFields, pageable);
		return new NDatas<IdProvider>(nFields, idProviderUtil.getFullObjects(entity, pageable, spec));
	}
		
	private Pageable transformPageRequest(List<NField> nfFields, Pageable pageRequest){
		Sort sort = pageRequest.getSort();
		Map<Integer, Sort> treeMap = new TreeMap<>();
		for (NField nField : nfFields) {
			if (nField.getSortBy() != SortType.NULL){
				Direction direction = null;
				if (nField.getSortBy() == SortType.ASC) direction = Direction.ASC;
				else direction = Direction.DESC;
				Integer key = nField.getSortPriority() * 100;
				while (treeMap.containsKey(key)) key += 1;
				treeMap.put(key, new Sort(direction, nField.getName()));
			}
		}
		for (Map.Entry<Integer, Sort> andSort : treeMap.entrySet()) {
			if (sort == null) sort = andSort.getValue();
			else sort = sort.and(andSort.getValue());
		}
		return new PageRequest(pageRequest.getPageNumber(), pageRequest.getPageSize(), sort);
	}

	public NData<IdProvider> findOne(Class<?> entity, Integer id) throws ServiceException {
		return findOne(entity, id, null);
	}
	public NData<IdProvider> findOne(Class<?> entity, Integer id, Specification<IdProvider> spec) throws ServiceException {
		List<Field> fields = getFields(entity);
		Map<String, Map<String, List<NField>>> nMapFields = getMapNField(fields);
		return new NData<IdProvider>(nMapFields, idProviderUtil.getFullObject(entity, id, spec));
	}
	
	
	
	
	public Translation translate(Translation base, Lang lang) throws ServiceException{
		try {
			Class<?> entity = base.getClass();
			Object service = customServiceLocator.getService(entity.getSimpleName());
			Class<?> clazz = service.getClass();
			
			Method[] methods = clazz.getMethods();
			for (Method method : methods) {
				System.out.println(method.getName() + " - " + method.getParameterTypes());
			}
			
			Method translate;
			Object paramsObj[] = {base, lang};
			try {
				//Try to find method from his service
				Class<?> params[] = {entity, Lang.class};
				translate = clazz.getMethod("translate", params);
			} catch (NoSuchMethodException e) {
				//find method from TranslationService
				Class<?> params[] = {Translation.class, Lang.class};
				translate = clazz.getMethod("translate", params);
			}

			return (Translation) translate.invoke(service, paramsObj);
			
		} catch (ClassNotFoundException e) {
			logger.error("getData -> ClassNotFoundException", e);
			throw new ServiceException("Error getList", e);
		} catch (SecurityException e) {
			logger.error("getData -> SecurityException", e);
			throw new ServiceException("Error getList", e);
		} catch (IllegalAccessException e) {
			logger.error("getData -> IllegalAccessException", e);
			throw new ServiceException("Error getList", e);
		} catch (NoSuchMethodException e) {
			logger.error("getData -> NoSuchMethodException", e);
			throw new ServiceException("Error getList", e);
		} catch (IllegalArgumentException e) {
			logger.error("getData -> IllegalArgumentException", e);
			throw new ServiceException("Error getList", e);
		} catch (InvocationTargetException e) {
			logger.error("getData -> InvocationTargetException", e);
			throw new ServiceException("Error getList", e);
		}
	}
	

	
	
//	@SuppressWarnings("unchecked")
//	public IdProvider add(Class<?> entity) throws ServiceException {
//		try {
//			return (IdProvider) entity.newInstance();
//		} catch (InstantiationException e) {
//			throw new ServiceException("add -> Error", e) ;
//		} catch (IllegalAccessException e) {
//			throw new ServiceException("add -> Error", e) ;
//		}
//	}
	
	@SuppressWarnings("unchecked")
	public NData<IdProvider> add(Class<?> entity) throws ServiceException {
		try {
			List<Field> fields = getFields(entity);
			Map<String, Map<String, List<NField>>> nMapFields = getMapNField(fields);
			return new NData<IdProvider>(nMapFields, (IdProvider) entity.newInstance());
		} catch (InstantiationException e) {
			throw new ServiceException("add -> Error", e) ;
		} catch (IllegalAccessException e) {
			throw new ServiceException("add -> Error", e) ;
		}
	}
	
//	public NData<IdProvider> copy(Class<?> entity, Integer id) throws ServiceException {
//		List<Field> fields = getFields(entity);
//		Map<String, Map<String, List<NField>>> nMapFields = getMapNField(fields);
//		IdProvider data = null;
//		if (id == 0){
//			data = add(entity);
//		} else {
//			data = getData(entity, id, null);
//			data.setId(null);
//			saveData(data);
//
//		}
//		return new NData<IdProvider>(nMapFields, data);
//	}
	
	private IdProvider completeData(Class<?> entity, IdProvider data, List<NField> nFields, IdProvider origin) throws ServiceException{
		// get data original if id != null
		IdProvider result = data;
		// for each field not editable, set original value to data field
		for (NField nField : nFields) {
			if (!nField.isEditable()){
				Field field = nField.getField();
				setFieldValue(result, field, getFieldValue(origin, field));
			}
		}
		return result;
	}
	
	private IdProvider persistData(Class<?> entity, IdProvider data) throws ServiceException{
		try {
			Class<?> params[] = { Object.class };
			Object paramsObj[] = { data };
	
			logger.debug("persistData -> Look for " + entity.getSimpleName());			
			Object service = customServiceLocator.getService(entity.getSimpleName());
			logger.debug("persistData -> Entity found " + entity.getSimpleName());	
	
			Class<?> clazz = service.getClass();
	
			Method save = clazz.getMethod("save", params);
			IdProvider saved = (IdProvider) save.invoke(service, paramsObj);
			
			return saved;
		} catch (ClassNotFoundException e) {
			logger.error("persistData -> ClassNotFoundException", e);
			throw new ServiceException("Error getList", e);
		} catch (NoSuchMethodException e) {
			logger.error("persistData -> NoSuchMethodException", e);
			throw new ServiceException("Error saveData", e);
		} catch (IllegalAccessException e) {
			logger.error("persistData -> IllegalAccessException", e);
			throw new ServiceException("Error saveData", e);
		} catch (InvocationTargetException e) {
			logger.error("persistData -> InvocationTargetException", e);
			throw new ServiceException("Error saveData", e);
		}
	}

	@CacheEvict(value = {CacheConst.IDPROVIDERFIEDDVALUE, CacheConst.TRANSLATION_IDENTIFY, CacheConst.JSP}, allEntries = true)
	public IdProvider saveData(IdProvider data) throws ServiceException{
		IdProvider result = data;

		Class<?> entity = data.getClass();

		List<Field> fields = getFields(entity);
		List<NField> nFields = getNFields(fields);
		
		IdProvider origin = null; 
		if (data != null && data.getId() != null) {
			origin = idProviderUtil.getFullObject(entity, data.getId(), null);
			result = completeData(entity, result, nFields, origin);
		}
		
		
		
		result = persistData(entity, result);

		//persistReverse(data, entity, nFields, origin);
		
		
		
		commonUtil.init();
		
		
		return result;
	}
	

	
	

	
	
	

	
	public static Type[] findGenericTypeOfField(Field field) throws IllegalArgumentException{
		Type genericFieldType = field.getGenericType();
		if(genericFieldType instanceof ParameterizedType){
		    ParameterizedType aType = (ParameterizedType) genericFieldType;
		    return  aType.getActualTypeArguments();
		} else throw new IllegalArgumentException();
	}
	
	public void persistReverse(IdProvider data, Class<?> classObject, List<NField> nFields, IdProvider origin) throws ServiceException{
		for (NField nField : nFields) {
			if (nField.getReverseJoin() != null){
				Object object = getFieldValue(data, nField.getField());
				if (object instanceof Iterable){
					Object originObject = null;
					if (origin != null) originObject = getFieldValue(origin, nField.getField());	
					
					System.out.println("         INSTANCE OF Iterable");
					Class<?> clazz = (Class<?>) findGenericTypeOfField(nField.getField())[0];
					Map<String, Field> clazzFields = getMapFields(clazz);
					System.out.println("             clazz " + clazz);
					System.out.println("             Field : " + nField.getReverseJoin());
					for (Map.Entry<String, Field> e : clazzFields.entrySet()) {
						Field field = e.getValue();
						System.out.println("             	 Field : " + field.getName());
					}
					Field clazzField = clazzFields.get(nField.getReverseJoin());
					System.out.println("             Field found : " + (clazzField != null));
					
					if (originObject != null){
						List<?> originList = (List<?>) originObject;
						for (Object object2 : originList) {
							IdProvider mapped = (IdProvider) object2;
							System.out.println("             MAPPED " + mapped.getName() + " - " + mapped.getClass());

							Object mappedFieldValue = getFieldValue(mapped, clazzField);
							if (mappedFieldValue instanceof Iterable){
								// ManyToMany
								List<Object> mappedList = (List<Object>) getFieldValue(mapped, clazzField);
								System.out.println("             mappedList : " + mappedList.toString());
								for (Object object3 : mappedList) {
									System.out.println("             object3 : ");
								}

								//System.out.println("             DEJA DANS LA LISTE : " + mappedList.contains(data));


							} else {
								// ManyToOne
			
							}
						}
					}

					List<?> list = (List<?>) object;
					Boolean reverseIsCollection = nField.getReverseIsCollection();
					
					for (Object object2 : list) {
						IdProvider mapped = (IdProvider) object2;
						System.out.println("             MAPPED " + mapped.getName() + " - " + mapped.getClass());

						Object mappedFieldValue = getFieldValue(mapped, clazzField);
						
						if (reverseIsCollection){
							// ManyToMany
							List<Object> mappedList = (List<Object>) getFieldValue(mapped, clazzField);
							System.out.println("             DEJA DANS LA LISTE : " + mappedList.contains(data));
							if (!mappedList.contains(data)){
								mappedList.add(data);
							}
							setFieldValue(mapped, clazzField, mappedList);
							saveData(mapped);
						} else {
							// ManyToOne
							if (mappedFieldValue == null){
								setFieldValue(mapped, clazzField, data);
								saveData(mapped);
							} else if (!mappedFieldValue.equals(data)) {
								throw new ServiceException("Can't override field value on " + nField.getReverseJoin());
							}
						}
						
					}
				} else {
					if (object != null) System.out.println("		object is : " + object.getClass().toString());
				}				
				
			}
		}
		
		return;
	}
	
	
	
	
	private Object getFieldValue(Object object, Field field) throws ServiceException {
		try {
			field.setAccessible(true);
			return field.get(object);
		} catch (IllegalAccessException e) {
			logger.error("Failed to get value from field", e);
			throw new ServiceException("Erreur getFieldValue", e);
		}
	}
	
	private void setFieldValue(Object object, Field field, Object value) throws ServiceException {
		try {
			field.setAccessible(true);
			field.set(object, value);
		} catch (IllegalAccessException e) {
			logger.error("Failed to get value from field", e);
			throw new ServiceException("Erreur setFieldValue", e);
		}
	}
	
	
	
	
	
	
	
	
	
	
	
	
	@SuppressWarnings("unchecked")
	public IdProvider removeDatas(List<IdProvider> datas) throws ServiceException{
		try {
			Class<?> entity = datas.get(0).getClass();

			Class<?> params[] = { Iterable.class };
			Object paramsObj[] = { datas };

			logger.debug("getDatas -> Look for " + entity.getSimpleName());			
			Object service = customServiceLocator.getService(entity.getSimpleName());
			logger.debug("getDatas -> Entity found " + entity.getSimpleName());	

			Class<?> clazz = service.getClass();
//			Method[] methods = clazz.getMethods();
//			for (Method method : methods) {
//				System.out.println(method.getName());
//				Class<?>[] parameterTypes = method.getParameterTypes();
//				for (Class<?> class1 : parameterTypes) {
//					System.out.println("	" + class1.getName());
//				}
//			}
			Method remove = clazz.getMethod("remove", params);
			return (IdProvider) remove.invoke(service, paramsObj);
			
		} catch (ClassNotFoundException e) {
			logger.error("removeDatas -> ClassNotFoundException", e);
			throw new ServiceException("Error getList", e);
		} catch (NoSuchMethodException e) {
			logger.error("removeDatas -> NoSuchMethodException", e);
			throw new ServiceException("Error removeDatas", e);
		} catch (IllegalAccessException e) {
			logger.error("removeDatas -> IllegalAccessException", e);
			throw new ServiceException("Error removeDatas", e);
		} catch (InvocationTargetException e) {
			logger.error("removeDatas -> InvocationTargetException", e);
			throw new ServiceException("Error removeDatas", e);
		}
	}
	
	
	
	
	
	@Deprecated
	public List<?> getAllNotAffected(String entityName, String fieldName, Integer ownerId, Integer startPosition, Integer maxResult) throws ServiceException{
		String query = "SELECT e FROM " + entityName + " e WHERE e." + fieldName + " IS NULL OR e." + fieldName + ".id = :ownerId";
		System.out.println(query);
		return em.createQuery(query)
				.setParameter("ownerId", ownerId)
				.setFirstResult(startPosition)
				.setMaxResults(maxResult)
				.getResultList();
	}
	@Deprecated
	public List<?> getAll(String entityName, Integer startPosition, Integer maxResult) throws ServiceException{
		String query = "SELECT e FROM " + entityName + " e";
		System.out.println(query);
		return em.createQuery(query)
				.setFirstResult(startPosition)
				.setMaxResults(maxResult)
				.getResultList();
	}
	
	
	
	
	
	
	
	private List<String> getListObjectType(Class<?> from){
		SortedSet<String> result = new TreeSet<>();
		Map<String, Object> entities = entityLocator.getEntities();
		for (Map.Entry<String, Object> entity : entities.entrySet()) {
			if (from.isAssignableFrom(entity.getValue().getClass())){
				String entityName = entity.getValue().getClass().getSimpleName();
				if (customServiceLocator.isServiceExist(entityName)){
					result.add(entityName);
				}
			}
		}
		return new ArrayList<>(result);
	}
	

	public List<String> getListTranslationObjectType(){
		return getListObjectType(Translation.class);
	}
	public List<String> getListNoTranslationObjectType(){
		return getListObjectType(NoTranslation.class);
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	private static final Character BINDER_COLLECTION_CHAR_FIRST = 91; // "["
	private static final Character BINDER_COLLECTION_CHAR_LAST = 93; // "["
	private static final Character BINDER_COLLECTION_CHAR_SEPARATOR = 44; // ","
	private static final Character BINDER_IDPROVIDER_CHAR_SEPARATOR = 95; // "_"
	private static final String BINDER_STRING_COMMA_REPLACE = "&c";
	private static final String BINDER_STRING_CHAR_FIRST_REPLACE = "&o";
	private static final String BINDER_STRING_CHAR_LAST_REPLACE = "&f";
	
	public IdProvider stringToIdProvider(String objectTypeId) throws IllegalArgumentException{
		if (objectTypeId == null || objectTypeId.equals("")) return null;
		if (objectTypeId.substring(objectTypeId.length() - 1).equals(",")){
			objectTypeId = objectTypeId.substring(0, objectTypeId.length()-1);
		}
		
		String[] identifier = objectTypeId.split((BINDER_IDPROVIDER_CHAR_SEPARATOR).toString());
    	
    	String objectType = identifier[0];
    	Class<?> cls;
		try {
			cls = entityLocator.getEntity(objectType).getClass();
		} catch (ClassNotFoundException e) {
			throw new IllegalArgumentException(objectType + " Not found !", e);
		}
		if (cls == null){
            throw new IllegalArgumentException ("Unknown idProvider type:" + objectType);
		}

    	Integer id = null;
    	if (identifier.length > 1){
    		try {
	    		id = Integer.parseInt(identifier[1]);
			} catch (NumberFormatException e) {
				throw new IllegalArgumentException("Can't parse " + identifier[1] + " !", e);
			}
    	}

    	if (id == null){
    		try {
    			return ((IdProvider) cls.newInstance());
    		} catch (InstantiationException e) {
    			throw new IllegalArgumentException (e.getMessage(),  e);
    		} catch (IllegalAccessException e) {
    			throw new IllegalArgumentException (e.getMessage(),  e);
    		} 
    	} else {
    		try {
    			return idProviderUtil.getFullObject(cls, id);
			} catch (NumberFormatException e) {
				throw new IllegalArgumentException("Can't parse " + identifier[1] + " !", e);
			} catch (ServiceException e) {
				throw new IllegalArgumentException("Can't get " + cls.getName() + ", id = " + id + " !", e);
			}
    	}
	}
	public boolean isNormalCollection(String expr){
    	Character first = expr.charAt(0);
    	Character last = expr.charAt(expr.length() - 1);
    	return (first == BINDER_COLLECTION_CHAR_FIRST && last == BINDER_COLLECTION_CHAR_LAST);
	}
	public String idProviderToString(IdProvider idProvider){
		return idProvider.getObjectType() + BINDER_IDPROVIDER_CHAR_SEPARATOR + idProvider.getId().toString();
	}
	public String idProvidersToString(Collection<?> collection){
		StringBuilder result = new StringBuilder();
	    for (Object object : collection) {
	    	IdProvider idProvider = (IdProvider) object;		    		
	    	result.append(idProvider.getObjectType() + BINDER_IDPROVIDER_CHAR_SEPARATOR + idProvider.getId().toString() + BINDER_COLLECTION_CHAR_SEPARATOR);
		}
	    return result.toString();
	}
	public List<IdProvider> stringToIdProviders(String expr){
		List<IdProvider> list = new ArrayList<>();
		String[] idProviders = expr.split((BINDER_COLLECTION_CHAR_SEPARATOR).toString());
		for (String string : idProviders) {
			IdProvider item = stringToIdProvider(string);
			list.add(item);
		}
		return list;
	}
	public String collectionToString(Collection<?> collection){
		StringBuilder result = new StringBuilder();
		result.append(BINDER_COLLECTION_CHAR_FIRST);
	    for (Object object : collection) {
	    	result.append(collectionToStringEncode(object.toString()) + BINDER_COLLECTION_CHAR_SEPARATOR);
		}
	    result.append(BINDER_COLLECTION_CHAR_LAST);
	    return result.toString();
	}
	public String collectionToStringEncode(String expr){
		String ret = HtmlUtils.htmlEscape(expr.toString());
		ret = ret.replace((BINDER_COLLECTION_CHAR_SEPARATOR).toString(), BINDER_STRING_COMMA_REPLACE);
		ret = ret.replace((BINDER_COLLECTION_CHAR_FIRST).toString(), BINDER_STRING_CHAR_FIRST_REPLACE);
		ret = ret.replace((BINDER_COLLECTION_CHAR_LAST).toString(), BINDER_STRING_CHAR_LAST_REPLACE);
		return ret;
	}
	public String collectionToStringDecode(String expr){
		String ret = expr.replace(BINDER_STRING_CHAR_LAST_REPLACE, (BINDER_COLLECTION_CHAR_LAST).toString());
		ret = ret.replace(BINDER_STRING_CHAR_FIRST_REPLACE, (BINDER_COLLECTION_CHAR_FIRST).toString());
		ret = ret.replace(BINDER_STRING_COMMA_REPLACE, (BINDER_COLLECTION_CHAR_SEPARATOR).toString());
		return HtmlUtils.htmlUnescape(ret.toString());
	}
	public Collection<String> stringToCollection(Class<? extends Collection> collectionType, String expr) throws IllegalArgumentException{
		try {
			Collection<String> list = collectionType.newInstance();
			String string = expr.substring(1, expr.length() - 1);
			String[] split = string.split((BINDER_COLLECTION_CHAR_SEPARATOR).toString());
			for (String e : split) {
				list.add(collectionToStringDecode(e));
			}
			return list;

		} catch (InstantiationException e) {
			throw new IllegalArgumentException("todo", e);
		} catch (IllegalAccessException e) {
			throw new IllegalArgumentException("todo", e);
		}
	}

	public Page<IdProvider> getFullObjects(Class<?> object, Pageable pageRequest, Specification<IdProvider> spec) throws ServiceException {
		return idProviderUtil.getFullObjects(object, pageRequest, spec);
	}

	public IdProvider getFullObject(Class<?> object, Integer id, Specification<IdProvider> spec) throws ServiceException {
		return idProviderUtil.getFullObject(object, id, spec);
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
}
