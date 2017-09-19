(function() {
	var bModule = angular.module('backServices');

	bModule.service('BlockManagementService', function($rootScope, $q, FolderService, PageService, TemplateService, LangService, BlockService, MapTemplateService, TObjectService) {
		var folder = null;
		var page = null;
		var activeObject = null;
		self = this;

		self.getFolder = function() {
			return folder;
		};
		self.getPage = function() {
			return page;
		};
		self.getActiveObject = function() {
			return activeObject;
		};
		
		var templates = null;
		self.getTemplates = function() {
			return templates;
		};
		var langs = null;
		self.getLangs = function() {
			return langs;
		};
		
		
		self.initFolder = function(folderId) {
			var deferred = $q.defer();
			if (!folder) {
				console.log('init folder');
				FolderService.getFolder(folderId).then(function(data){
					console.log('folder initialized');
					folder = data;
					deferred.resolve();
				});
			} else {
				deferred.resolve();
			}
			return deferred.promise;
		};
		
		self.initPage = function(pageId) {
			var deferred = $q.defer();
			if (!page) {
				console.log('init page');
				PageService.getPage(pageId).then(function(data){
					console.log('page initialized');
					page = data;
					deferred.resolve();
				});
			} else {
				deferred.resolve();
			}
			return deferred.promise;
		};
		
		self.initActiveObject = function(activeObjectId) {
			var deferred = $q.defer();
			if (!activeObject) {
				if (!activeObjectId) deferred.resolve();
				else {
					TObjectService.getTObject(activeObjectId).then(function(data){
						activeObject = data;
						console.log(activeObject);
						deferred.resolve();
					});
				}
			} else {
				deferred.resolve();
			}
			return deferred.promise;
		};
		

		self.initTemplates = function() {
			var deferred = $q.defer();
			if (!templates) {
				TemplateService.getTemplates().then(function(data) {
					templates = data;
					deferred.resolve();
				});
			} else {
				deferred.resolve();
			}
			return deferred.promise;
		};
		self.initLangs = function() {
			var deferred = $q.defer();
			if (!langs) {
				LangService.getLangs().then(function(data) {
					langs = data;
					deferred.resolve();
				});
			} else {
				deferred.resolve();
			}
			return deferred.promise;
		};


		var initialized = $q.defer();
		self.init = function(folderId, pageId, activeObjectId) {
			if (folderId && pageId){
				console.log('init');
				$q.all([self.initFolder(folderId), self.initPage(pageId), self.initActiveObject(activeObjectId)]).then(function() {
					
				}).catch(function(error) {
					console.log(error);
					initialized.reject();
				})
				.finally(function() {
					console.log('fin init');
					initialized.resolve();
				});
			}
			return initialized.promise;
		};
		
		self.setMapBlock = function(modelId, blockId, positionId, ordered) {
    		var dataObj = {
    				modelId : modelId,
    				blockId : blockId,
    				positionId : positionId, 
    				ordered : ordered
    		};
			var deferred = $q.defer();
			MapTemplateService.save(dataObj).then(function(data) {
				deferred.resolve(data);
			});
			return deferred.promise;
		}
		
		self.removeMapBlock = function(blockId) {
			var deferred = $q.defer();
			MapTemplateService.remove(blockId).then(function(data) {
				deferred.resolve(data);
			});
			return deferred.promise;
		}
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		

	});
	

	
	bModule.service('TemplateService', function(TemplateResource, TemplateRepository) {
		self = this;
		self.getTemplate = function(id) {
			return TemplateResource.get({id : id}).$promise;
		};
		self.getTemplates = function() {
			return TemplateResource.getAll().$promise;
		};
		self.getExist = function(context, kind, path, name) {
			return TemplateRepository.exist(context, kind, path, name);
		};
		self.saveTemplate = function(template) {


			var fn = (template.id) ? TemplateResource.update : TemplateResource.create;
			return fn.call(TemplateResource, {}, template).$promise;
//			.then(function(savedTemplate) {
//				//if (movies) {
//					if (!template.id) {
//						//movies.push(savedTemplate);
//					} else {
//						//var oldMovie =getByIdFilter(movies, movie.id);
//						//var index = movies.indexOf(oldMovie);
//
//						//movies[index] = savedTemplate;
//
//					}
//
//				//}
//				return savedTemplate;
//			});

			//return fn.call(TemplateResource, {}, template).$promise;

		};
	});
	bModule.service('LangService', function(LangResource) {
		self = this;
		self.getLang = function(id) {
			return LangResource.get({id : id}).$promise;
		};
		self.getLangByCode = function(code) {
			return LangResource.get({code : code}).$promise;
		};
		self.getLangs = function() {
			return LangResource.getAll().$promise;
		};
	});
	bModule.service('FolderService', function(FolderResource) {
		self = this;
		self.getFolder = function(id) {
			return FolderResource.get({id : id}).$promise;
		};
		self.getFolderByName = function(name) {
			return FolderResource.get({name : name}).$promise;
		};
		self.getFolders = function() {
			return FolderResource.getAll().$promise;
		};
	});
	bModule.service('PageService', function(PageResource) {
		self = this;
		self.getPage = function(id) {
			return PageResource.get({id : id}).$promise;
		};
		self.getPages = function() {
			return PageResource.getAll().$promise;
		};
	});	
	bModule.service('BlockService', function(BlockResource) {
		self = this;
		self.getBlocksForModelPosition = function(modelId, pageId, activeObjectId, positionId) {
			return BlockResource.getAll({modelId : modelId, pageId : pageId, activeObjectId : activeObjectId, positionId : positionId}).$promise;
		};
	});
	bModule.service('MapTemplateService', function(MapTemplateResource) {
		self = this;
		self.save = function(mapTemplate) {
			var fn = (mapTemplate.id) ? MapTemplateResource.update : MapTemplateResource.save;
			return fn.call(MapTemplateResource, {}, mapTemplate).$promise;
		};
		self.remove = function(id) {
			return MapTemplateResource.remove({id:id}).$promise;
		};
	});
	bModule.service('TObjectService', function(TObjectResource) {
		self = this;
		self.getTObject = function(id) {
			return TObjectResource.get({id : id}).$promise;
		};
	});
	

}());