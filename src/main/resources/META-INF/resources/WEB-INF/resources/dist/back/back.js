(function() {
	var bModule = angular.module('backApp', [ 'backServices', 'ui.bootstrap', 'ngSanitize', 'pascalprecht.translate', 'ngCookies', 'ngResource', 'frontApp' ]);
	bModule.config(function($translateProvider) {
		var $cookies;
		angular.injector(['ngCookies']).invoke(['$cookies', function(_$cookies_) {
			$cookies = _$cookies_;
		}]);
		
		$translateProvider.useSanitizeValueStrategy('sanitize');
		var lang = $cookies.get('language');
		if (lang == undefined){lang = 'en';}
		$translateProvider.preferredLanguage(lang);
		$translateProvider.useLoader('i18nLoader');
	});
	bModule.run(function($cookies) {

		
	});
	bModule.factory('i18nLoader', function ($http, $q, backConfig) {
	    return function (options) {
		    var deferred = $q.defer();
			$http.get(backConfig.basePath + '/@front/labels/' + options.key)
			.then(function(response) {
	            deferred.resolve(response.data);
			})
			.catch(function(error){
				console.log(error.status);
				console.log(error);
				deferred.reject(options.key);
			});
			return deferred.promise;
	    };
	});
}());
(function() {
var bModule = angular.module("backApp");

bModule.controller('BlockManagementCtrl', function ($scope, $rootScope, BlockManagementService) {
	$scope.init = function(folderId, pageId, activeObjectId) {
		BlockManagementService.init(folderId, pageId, activeObjectId).then(function() {
			
		});
	}
});

}());
(function() {
var bModule = angular.module("backApp");

bModule.controller('BlockSelectionCtrl', function($scope, BlockManagementService) {
	BlockManagementService.init().then(function() {

		BlockManagementService.initTemplates().then(function() {
			$scope.templates = BlockManagementService.getTemplates();
		});
		BlockManagementService.initLangs().then(function() {
			$scope.langs = BlockManagementService.getLangs();
		});
		
		
		$scope.currentLang = '';
		$scope.switchLang = function(codeLang) {
			$scope.currentLang = codeLang;
		};

		$scope.openValue = false;
		$scope.switchDisplay = function() {
			$scope.openValue = !$scope.openValue;
		};
		
		
	});
	


});


}());

(function() {
var bModule = angular.module("backApp");

bModule.controller('ModalDemoCtrl', function ($scope, $uibModal, $log) {

  $scope.items = ['item1', 'item2', 'item3'];

  $scope.open = function (size) {
    var modalInstance;
    var modalScope = $scope.$new();
    modalScope.ok = function () {
            modalInstance.close(modalScope.selected);
    };
    modalScope.cancel = function () {
            modalInstance.dismiss('cancel');
    };      
    
    modalInstance = $uibModal.open({
      template: '<my-modal></my-modal>',
      size: size,
      scope: modalScope
      }
    );

    modalInstance.result.then(function (selectedItem) {
      $scope.selected = selectedItem;
    }, function () {
      $log.info('Modal dismissed at: ' + new Date());
    });
  };
});

}());
(function() {
var bModule = angular.module("backApp");
	
bModule.controller('TemplateModalCtrl', function($scope, $uibModalInstance, backConfig, template, page, TemplateService) {	
	$scope.exist = function() {
		return TemplateService.getExist(page.context, template.kind, template.path, template.name)
		.then(function(data) {
			$scope.scriptExist = {
				exist: data, 
				path: '\"...' + template.path + '/' + template.name + '.jsp\"',
			};
		});
	};
	
	$scope.cancel = function() {
		$uibModalInstance.dismiss('cancel');
	};
	
	$scope.updateTemplate = function(form) {
		if (form.$valid) {
			TemplateService.saveTemplate(template).then(function(data) {
				$uibModalInstance.close(data);
			}).catch(function(error) {
				$scope.template.errors = error;
			}).finally(function() {
				
			});
		} else {
			$uibModalInstance.dismiss('cancel');
		}

	};
	
	$scope.template = {
		templateModal: backConfig.URL_TEMPLATE_MODAL, 
		template: template,
		errors: null, 
	};
	
	$scope.exist();
});

}());
(function() {
var bModule = angular.module('backApp');

bModule.controller('UiPositionCtrl', function($scope, $uibModal, BlockManagementService, TemplateService, BlockService, backConfig) {
	BlockManagementService.init().then(function() {
		const SCOPE_ACTIVEMODEL = 'ACTIVEMODEL';
		const SCOPE_ACTIVEPAGE = 'ACTIVEPAGE';
		const SCOPE_ACTIVEOBJECT = 'ACTIVEOBJECT';
		const BLOCK_KIND_PAGEBLOCK = 'PAGEBLOCK';
		
		$scope.debug = false;
		
		$scope.folder = BlockManagementService.getFolder();
		$scope.page = BlockManagementService.getPage();
		$scope.activeObject = BlockManagementService.getActiveObject();
		
		BlockService.getBlocksForModelPosition($scope.modelId, $scope.page.id, $scope.activeObjectId, $scope.positionId).then(function(data) {
			
			$scope.blocks = data;
		});
		
		TemplateService.getTemplate($scope.modelId).then(function(data) {
			$scope.model = data;
		});
		
		var activeObjectId = $scope.activeObject == null ? '' : $scope.activeObject.id;
		$scope.getParsedBlock = function(block) {
			return backConfig.serverRest + '/parsedblock/' + $scope.page.id + '/' + block.idMapTemplate + '/' + activeObjectId + '?folderId=' + $scope.folder.id;
		}
		
	    $scope.$on('dropEvent_' + $scope.positionId + '@' + $scope.modelId, function (evt, block, ordered) {
	    	console.log($scope.modelId);
	    	console.log($scope.positionId + '@' + $scope.modelId, $scope.page, block, ordered);
	    	
			var setMapBlock = function(choice) {
				var scopeChoice = null;
				if (choice == SCOPE_ACTIVEMODEL) scopeChoice = $scope.modelId;
				else if (choice == SCOPE_ACTIVEPAGE) scopeChoice = $scope.page.id;
				else if (choice == SCOPE_ACTIVEOBJECT) scopeChoice = activeObjectId;
				
				console.log(scopeChoice);
				
		        BlockManagementService.setMapBlock(scopeChoice, block.id, $scope.positionId, ordered).then(function(data){
		        	if (ordered < 0){
		        		$scope.blocks.splice(0, 0, data);
		        	} else {
		        		$scope.blocks.push(data);
		        	}
				});
			}
	    	
			console.log($scope.model.kind);
			//Les blocks posés sur un PAGEBLOCK sont forcément posés sur le modèle
			if ($scope.model.kind == BLOCK_KIND_PAGEBLOCK){
				setMapBlock(SCOPE_ACTIVEMODEL);
			} else {
				var activeObjectNull = $scope.activeObject == null;
				console.log(activeObjectNull);
				var instance = $uibModal.open({
					templateUrl: backConfig.URL_TEMPLATE_MODAL_CHOICE_SCOPE,
					size: 'sm',
					resolve: {
			            buttons: function(){
			            	return {activeModel : true, activePage : true, activeObject : !activeObjectNull};
			            },
			            nbCol: function(){
			            	return activeObjectNull ? 2 : 3
			            },
					},
					controller: function ( $scope, $uibModalInstance, buttons, nbCol ) {
						$scope.buttons = buttons;
						$scope.classCol = 'col-xs-' + (12 / nbCol);
						
						$scope.scopeActiveModel = function() {
							$uibModalInstance.close(SCOPE_ACTIVEMODEL);
						};	
						$scope.scopeActivePage = function() {
							$uibModalInstance.close(SCOPE_ACTIVEPAGE);
						};	
						$scope.scopeActiveObject = function() {
							$uibModalInstance.close(SCOPE_ACTIVEOBJECT);
						};	
						
					}, 
				});
				instance.result.then(function(choice) {
					setMapBlock(choice);
				});
			}


	    }); 
		
	    $scope.remove = function(blockIndex) {
	    	BlockManagementService.removeMapBlock($scope.blocks[blockIndex].idMapTemplate).then(function(data) {
				$scope.blocks.splice(blockIndex, 1);
			});
		}
	    
	    
	    
	    
//        self.getPathTemplateForm = function() {
//        	return backConfig.URL_TEMPLATE_MODAL;
//        }
        
        $scope.open = function(blockId) {
			var instance = $uibModal.open({
				templateUrl: backConfig.URL_TEMPLATE_MODAL_EDIT,
				controller: 'TemplateModalCtrl',
				size: 'lg',
				resolve: {
					template: function(){
						return blockId;
					}, 
					page: function() { return $scope.page;}, 
				}
			});
			instance.result.then(function(savedTemplate) {
				
				if (templates) {
					// TODO Update templates list
					
					//var oldTemplate =getByIdFilter(templates, blockId);
					//var index = templates.indexOf(oldTemplate);

					//templates[index] = savedTemplate;
	
				
	
				}

				
				
				
			});
		};
	});
	

});


}());

(function() {
	
	var bModule = angular.module("backApp");
	bModule.directive("drag", ["$rootScope", function ($rootScope) {

        function dragStart(evt, element, dragStyle) {
            element.addClass(dragStyle);
//            evt.originalEvent.dataTransfer.setData( "id", evt.target.id );
//            evt.originalEvent.dataTransfer.effectAllowed = 'move';
        };

        function dragEnd(evt, element, dragStyle) {
            element.removeClass(dragStyle);
        };

        return {
            restrict: 'A',
            link: function (scope, element, attrs) {
                attrs.$set('draggable', 'true');
                scope.dragStyle = attrs["dragstyle"];
                element.bind('dragstart', function (evt) {
                    $rootScope.draggedElement = scope[attrs["drag"]];
                    dragStart(evt, element, scope.dragStyle);
                });
                element.bind('dragend', function (evt) {
                    dragEnd(evt, element, scope.dragStyle);
                });
            }
        }
    }]);

	bModule.directive("drop", ['$rootScope', function ($rootScope) {

        function dragEnter(evt, element, dropStyle) {
            evt.preventDefault();
            element.addClass(dropStyle);
        };

        function dragLeave(evt, element, dropStyle) {
            element.removeClass(dropStyle);
        };

        function dragOver(evt) {
            evt.preventDefault();
        };

        function drop(evt, element, dropStyle) {
            evt.preventDefault();
            element.removeClass(dropStyle);
        };

        return {
            restrict: 'A',
            link: function (scope, element, attrs) {
                scope.dropStyle = attrs["dropstyle"];
                element.bind('dragenter', function (evt) {
                    dragEnter(evt, element, scope.dropStyle);
                });
                element.bind('dragleave', function (evt) {
                    dragLeave(evt, element, scope.dropStyle);
                });
                element.bind('dragover', dragOver);
                element.bind('drop', function (evt) {
                    drop(evt, element, scope.dropStyle);

                    var ordered = 0;
                    if (attrs.drop == 'top') ordered = -1;
                    else if (attrs.drop == 'bottom') ordered = -1;

                    var pathEvent = 'dropEvent_' + scope.positionId + '@' + scope.modelId;
                    $rootScope.$broadcast(pathEvent, $rootScope.draggedElement, ordered);

                });
            }
        }
    }]);
	
	

	bModule.directive('uiPosition', function(backConfig, BlockManagementService){
		return {
			scope: {
				positionId: '@',
				modelId: '@',
				activeObjectId: '@',
			},
			restrict: 'E',
			templateUrl: backConfig.tplPath + '/ui-position.html', 
			controller: 'UiPositionCtrl', 
			link: function(scope, element, attrs){
				
	        },
		};
	});
	

	
	bModule.directive('myModal', function() {
	    return {
	        restrict: 'E',
	        templateUrl: 'myModalContent.html',
	        controller: function ($scope) {
	          $scope.selected = {
	            item: $scope.items[0] 
	          };
	        }
	    };
	});

	
	
	
}());
(function() {
	
//	var module = angular.module("backApp");
//	module.filter('getById', function($filter) {
//		return function(list, id, idAttr) {
//			var filterAttr = 'id';
//			if (idAttr && angular.isString(idAttr)) {
//				filterAttr = idAttr;
//			}
//			var filterObject = {};
//			filterObject[filterAttr] = id;
//			var filtersData = $filter('filter')(list, filterObject, true);
//			if (filtersData) {
//				return filtersData[0];
//			} else {
//				return null;
//			}
//		};
//	});
	
	
	
	
}());


(function(angular) {
    'use strict';
    angular.module('backApp').provider('backConfig', function() {

        var values = {
        	basePath: '',
        	serverRest: '/@back',
        	tplPath: 'src/back/js/templates',
            i18nPath: 'src/back/js/i18n',
        };

        return {
            $get: function() {
                return values;
            },
            set: function (constants) {
                angular.extend(values, constants);
            }
        };

    });
})(angular);

(function() {
	var bModule=angular.module("backServices",[]);
	bModule.config(function($httpProvider) {
		$httpProvider.interceptors.push('Interceptor');
	});
	
	bModule.factory('Interceptor', function($q) {
		return {
			responseError: function(rejection) {
				if (rejection.status === 404) {
					rejection.data = '<div class="bg-danger"><h2>Object not found</h2><p>' + rejection.config.url + '</p></div>'
					return $q.resolve(rejection);
				}
				return $q.reject(rejection);
			}
		};
	});
}());
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
(function() {
	var bModule = angular.module('backServices');
	bModule.constant('$backjPath', {
		URL_SERVER_REST: '/neutrino/',
		URL_TEMPLATE_JS: '/neutrino/resources/src/back/js/templates/',
		URL_TEMPLATE_MODAL: '/neutrino/resources/src/back/js/templates/templateForm.html',
		URL_TEMPLATE_MODAL_ADD: '/neutrino/resources/src/back/js/templates/modalTemplateFormAdd.html',
		URL_TEMPLATE_MODAL_EDIT: '/neutrino/resources/src/back/js/templates/modalTemplateFormEdit.html',
		URL_TEMPLATE_MODAL_CHOICE_SCOPE: '/neutrino/resources/src/back/js/templates/choiceScopeModal.html',
		
		
		
		
	});

}());
(function() {
	var bModule = angular.module('backServices');

	bModule.factory("TemplateRepository", function($http, backConfig) {
		return {
			exist: function(context, kind, path, name) {
				return $http.get(
						backConfig.serverRest + '/templates/exist/', 
					{params:{'context': context, 'kind': kind, 'path': path, 'name': name}}
				).then(function(response) {
					return response.data;
				})
				.catch(function(error){
					console.log(error.status);
					console.log(error);
				});
			}, 
		};
	});
	
	bModule.factory("TemplateResource", function($resource, backConfig) {
	  	var API_URI = backConfig.serverRest + '/template';  				
	  	return $resource(API_URI, {id: '@id'}, {
	  		create: {method: 'POST'},
	  		get:    {method: 'GET'},
	  		getAll: {method: 'GET', isArray: true},
	  		remove: {method: 'DELETE'},
	  		update: {method: 'PUT'}
	  	});
	});
	
	bModule.factory("LangResource", function($resource, backConfig) {
	  	var API_URI = backConfig.serverRest + '/lang';  				
	  	return $resource(API_URI, {id: '@id', code: '@code'}, {
	  		create: {method: 'POST'},
	  		get:    {method: 'GET'},
	  		getAll: {method: 'GET', isArray: true},
	  		remove: {method: 'DELETE'},
	  		update: {method: 'PUT'}
	  	});
	});
	
	
	bModule.factory("FolderResource", function($resource, backConfig) {
	  	var API_URI = backConfig.serverRest + '/folder';  				
	  	return $resource(API_URI, {id: '@id', name: '@name'}, {
	  		create: {method: 'POST'},
	  		get:    {method: 'GET'},
	  		getAll: {method: 'GET', isArray: true},
	  		remove: {method: 'DELETE'},
	  		update: {method: 'PUT'}
	  	});
	});
	
	bModule.factory("PageResource", function($resource, backConfig) {
	  	var API_URI = backConfig.serverRest + '/page';  				
	  	return $resource(API_URI, {id: '@id'}, {
	  		create: {method: 'POST'},
	  		get:    {method: 'GET'},
	  		getAll: {method: 'GET', isArray: true},
	  		remove: {method: 'DELETE'},
	  		update: {method: 'PUT'}
	  	});
	});
	
	bModule.factory("BlockResource", function($resource, backConfig) {
	  	var API_URI = backConfig.serverRest + '/block';  				
	  	return $resource(API_URI, {modelId: '@modelId', pageId: '@pageId', activeObjectId: '@activeObjectId', positionId: '@positionId'}, {
	  		create: {method: 'POST'},
	  		get:    {method: 'GET'},
	  		getAll: {method: 'GET', isArray: true},
	  		remove: {method: 'DELETE'},
	  		update: {method: 'PUT'}
	  	});
	});
	
	bModule.factory("MapTemplateResource", function($resource, backConfig) {
	  	var API_URI = backConfig.serverRest + ' /mapTemplate';			
	  	return $resource(API_URI, {id: '@id'}, {
	  		save:   {method: 'POST'},
	  		get:    {method: 'GET'},
	  		getAll: {method: 'GET', isArray: true},
	  		remove: {method: 'DELETE'},
	  		update: {method: 'PUT'}
	  	});
	});
	
	bModule.factory("TObjectResource", function($resource, backConfig) {
	  	var API_URI = backConfig.serverRest + ' /tobject';			
	  	return $resource(API_URI, {id: '@id'}, {
	  		save:   {method: 'POST'},
	  		get:    {method: 'GET'},
	  		getAll: {method: 'GET', isArray: true},
	  		remove: {method: 'DELETE'},
	  		update: {method: 'PUT'}
	  	});
	});
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	bModule.factory("PositionRepository", function($http, backConfig) {
		return {
			get: function(positionName) {
				return $http.get(backConfig.serverRest + '/positions/'+ positionName)
				.then(function(response) {
					return response.data;
				})
				.catch(function(error){
					console.log(error.status);
					console.log(error);
				});
			},
			getAll: function() {
				return $http.get(backConfig.serverRest + '/positions/')
				.then(function(response) {
		            return response.data;
				})
				.catch(function(error){
					console.log(error.status);
					console.log(error);
				});

			}

		};
	});

	bModule.factory("MapTemplateRepository", function($http, backConfig) {
		return {
			remove: function(idTemplateBlock) {
				return $http.delete(backConfig.serverRest + '/removemapblock/' + idTemplateBlock)
				.then(function(response) {

				})
				.catch(function(error){
					console.log(error.status);
					console.log(error);
				});
			},
			add: function(dataObj) {
	    		return $http.post(backConfig.serverRest + '/addmapblock', dataObj).then(function(response) {
	    			return response.data;
				}).catch(function(error){
					console.log(error.status);
					console.log(error);
				});

			}
		};
	});
	
	

	

	
	
}());