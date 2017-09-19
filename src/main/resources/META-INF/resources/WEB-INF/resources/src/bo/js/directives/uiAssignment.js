(function() {

	var fModule = angular.module("boApp");

	//Directives
	fModule.directive('uiObjectAssignment', ['$parse', 'boConfig', function($parse, boConfig) {
		return {
			restrict: 'E',
			require : '?ngModel',
			transclude: true,
			templateUrl: boConfig.tplPath + '/ui-object-assignement.html', 
			controller: ['$scope', 'UiAssignmentService', function ( $scope, UiAssignmentService ) {
				$scope.open = function(size) {
					UiAssignmentService.getObjects($scope[$scope.model], $scope.type, $scope.id, $scope.field, $scope.kind, $scope.many, $scope.disablePreChecked, size).then(function(objects) {
						$parse($scope.model).assign($scope.$parent, objects);
					})
					.catch(function(error){
						console.log(error.status);
						console.log(error);
					});
				};
			}], 
			scope : true,
			link: function(scope, element, attrs, ngModelController){
				scope.type = attrs['type'];
				scope.field = attrs['field'];
				scope.id = attrs['id'];
				scope.model = attrs['ngModel'];
				scope.kind = attrs['kind'];
				scope.many = (attrs['many'] == 'true');
				scope.disablePreChecked = (attrs['disablePreChecked'] == 'true');
	        },
		};
	}]);
	
	fModule.directive('uiFileAssignment', ['$parse', 'boConfig', function($parse, boConfig) {
		return {
			restrict: 'E',
			require : '?ngModel',
			transclude: true,
			templateUrl: boConfig.tplPath + '/ui-file-assignement.html', 
			controller: ['$scope', 'UiAssignmentService', 'idProvidersFilter', function ( $scope, UiAssignmentService, idProvidersFilter ) {
				$scope.add = function(size) {
					UiAssignmentService.getObjects($scope[$scope.model], $scope.type, $scope.id, $scope.field, $scope.kind, $scope.many, $scope.disablePreChecked, size).then(function(objects) {
						var values = [];
						angular.extend(values, $scope[$scope.model]);
						console.log(values);
						angular.forEach(objects, function(value, key) {
							if (values.indexOf(value) == -1) values.push(value);
						});
						$parse($scope.model).assign($scope.$parent, values, $parse);
					})
					.catch(function(error){
						console.log(error.status);
						console.log(error);
					});
				};
				
				$scope.clear = function() {


//					console.log($scope.model);
//					console.log($scope.$parent[$scope.model]);
//					console.log($scope[$scope.model]);
//					
//					
//					var empty = $scope[$scope.model];
//					console.log(empty);
//				
//					
//					while(empty.length > 0) {
//						empty.pop();
//					}
//					console.log(empty);
					
					
					var empty = [];
					
					$parse($scope.model).assign($scope.$parent, empty);
					
					
					//$scope.$parent.files = [];
					
					//$parse(attrs.ngModel).assign(scope, idProvidersFilter(attrs.value, 'toArray'));
					
					
					var empty =[];
					$parse($scope.model).assign($scope.$parent, empty);
					
					
					
					
				};
			}], 
			scope : true,
			link: function(scope, element, attrs, ngModelController){
				scope.type = attrs['type'];
				scope.field = attrs['field'];
				scope.id = attrs['id'];
				scope.model = attrs['ngModel'];
				scope.kind = attrs['kind'];
				scope.many = (attrs['multiSelect'] == 'true');
				scope.disablePreChecked = (attrs['disablePreChecked'] == 'true');
	        },
		};
	}]);
	
	fModule.directive('input', ['$parse', 'idProvidersFilter', 'stringsFilter', '$compile', '$http', 'boConfig', function($parse, idProvidersFilter, stringsFilter, $compile, $http, boConfig) {
		return {
			restrict : 'E',
			require : '?ngModel',
			controller: ['$scope', '$element', '$attrs', function ( $scope, $element, $attrs ) {
		        if ($attrs.assign){
		        	
		        	//Permet de désactive la déselection des objets pré-selectionnés
		        	var disablePreChecked = false;
		        	if ($attrs.disablePreChecked){
		        		disablePreChecked = $attrs.disablePreChecked;
		        	}
		        	
		        	//Variables permettant l'identification des dépendances
		        	var res = $attrs.assign.split('_');
		        	var type = res[0];
		        	var id = res[1];
		        	var field = res[2];
		        	
		        	//Permet de définir quelle sous directive est à afficher
		        	var kind = $attrs.kind;
		        	if (kind === undefined) kind = 'object';
		        	
		        	//l'attribut many n'est utile que pour l'assignement des 'file' car le many pour les types objet est fourni coté serveur
		        	var many = $attrs.many;
		        	if (many === undefined) many = '';
		        	else {
		        		var attributeName = kind == 'file' ? 'multi-select' : 'many';
		        		many = ' ' + attributeName + '="' + many + '"';	        	
		        	}

		        	//variable bidirectionnel contenant le résultat
		        	var model = $attrs.ngModel;
		        	
		        	//Affiche la sous directive en fonction de 'kind'
			        var uiAssign = $compile('<data-ui-' + kind + '-assignment type="' + type + '" field="' + field + '" id="' + id + '" ng-model="' + model + '" kind="' + kind + '"' + many + ' disable-pre-checked="' + disablePreChecked + '"/></data-ui-' + kind + '-assignment>')( $scope );
			        $element.parent().append(uiAssign);

			        //Affiche dynamiquement une liste d'éléments correspondant à la selection
			        var modelText = $attrs.ngModel + 'Text';			        
			        if (kind == 'file'){
			        	var uiAssignText = $compile('<ul class="linked"><li data-ng-repeat="r in ' + modelText + ' | orderBy: \'name\'"><ui-file url="r" /></li><li data-ng-show="' + model+ '.length - ' + modelText + '.length > 0"><strong><a href="#">{{' + model + '.length - ' + modelText + '.length}} <span>Others results...</span></a></strong></li></ul>')( $scope );
			        	$element.parent().append(uiAssignText);
			        	$scope.$watch(model, function() {
				        	var max = 5;
							var objects = $scope[model];
							var files = [];
				      		if (objects && objects.length > 0){
				      			if (!max) max = objects.length;
				      			else if (objects.length < max) max = objects.length;
				      			for(var i = 0; i < max; i++) {
				      				files.push(boConfig.basePath + '/' + objects[i]);
				      			}
				      		}
				      		$scope[modelText] = files;
				        });

			        } else {
			        	var uiAssignText = $compile('<ul class="linked"><li data-ng-repeat="r in ' + modelText + ' | orderBy: \'name\'"><a class="linked" href="#">{{r.name}}</a></li><li data-ng-show="' + model+ '.length - ' + modelText + '.length > 0"><strong><a href="#">{{' + model + '.length - ' + modelText + '.length}} <span>Others results...</span></a></strong></li></ul>')( $scope );
				        $element.parent().append(uiAssignText);
				        $scope.$watch(model, function() {
				        	
				        	var max = 5;
							var objects = $scope[model];
				      		if (objects && objects.length > 0){
				      			if (!max) max = objects.length;
				      			else if (objects.length < max) max = objects.length;
				      			
				      			//Le type de la collection est identifié par le 1er de la liste
				      			var type = objects[0].type;
				      			
				      			//Chaque id est récupéré
				      			var ids = [];
				      			for(var i = 0; i < max; i++) {
				      				if (objects[i].id){
				      					ids.push(objects[i].id);
				      				}
				      			}

				      			//Récupère la liste des objets avec leur nom via une requête au serveur
				      			if (ids.length > 0){
						      		$http.get(boConfig.basePath + '/bo/list/objects/' + type, {params:{'id': ids}}).then(function(data) {
						      			$scope[modelText] = data.data;
						      			
									})
				      			}

				      		}

				        });
			        }

		        }
		    }],
			link : function(scope, element, attrs, ngModelController) {
	        	var kind = attrs.kind;
	        	if (kind === undefined) kind = 'object';

	        	var many = attrs.many == 'true';

				if (attrs.ngModel && attrs.assign) {
					
					if (kind == 'object'){
						$parse(attrs.ngModel).assign(scope, idProvidersFilter(attrs.value, 'toArray'));
						ngModelController.$parsers.push(function(data) {
							// convert data from view format to model format
							return idProvidersFilter(data, 'toArray'); // converted
						});
						ngModelController.$formatters.push(function(data) {
							// convert data from model format to view format
							return idProvidersFilter(data);  // converted
						});
					} else if (kind == 'file'){
						$parse(attrs.ngModel).assign(scope, stringsFilter(attrs.value, 'toArray'));
						ngModelController.$parsers.push(function(data) {
							// convert data from view format to model format
							return stringsFilter(data, 'toArray'); // converted
						});
						ngModelController.$formatters.push(function(data) {
							// convert data from model format to view format
							return stringsFilter(data, null, !many);  // converted
						});
					} else {
						$parse(attrs.ngModel).assign(scope, attrs.value);
					}
					
				}
			}
		}
	}]);
	
	
	//Filter
	fModule.filter('idProviders', function () {
		return function(object, method) {
			if (!method) method = 'toString';
			if (method == 'toArray'){
		    	var res = object.split(",");
		    	var idproviders = [];
		    	angular.forEach(res, function(value) {
		    		if (value != ''){
			    		var idprovider = value.split("_");
			    		idproviders.push({type:idprovider[0], id:Number(idprovider[1])});  
		    		}
		    	});
		    	return idproviders;
			} else {
				var res = '';
		    	angular.forEach(object, function(value) {
		    		res += value.type + '_' + value.id + ',';
		    	});
		    	return res;
			}
		};
	});

	fModule.filter('strings', function () {
		
		var encode = function(value) {
    		var encodedStr = value.replace(/[\u00A0-\u9999<>\&]/gim, function(i) {
    			return '&#'+i.charCodeAt(0)+';';
    		});
    		encodedStr = encodedStr.replace(/,/g, "&c");
    		encodedStr = encodedStr.replace(/\[/g, "&o");
    		encodedStr = encodedStr.replace(/\]/g, "&f");
    		return encodedStr;
		};
		
		var decode = function(value) {
			
			var decodeEntities = (function() {
			  // this prevents any overhead from creating the object each time
			  var element = document.createElement('div');

			  function decodeHTMLEntities (str) {
			    if(str && typeof str === 'string') {
			      // strip script/html tags
			      str = str.replace(/<script[^>]*>([\S\s]*?)<\/script>/gmi, '');
			      str = str.replace(/<\/?\w(?:[^"'>]|"[^"]*"|'[^']*')*>/gmi, '');
			      element.innerHTML = str;
			      str = element.textContent;
			      element.textContent = '';
			    }

			    return str;
			  }

			  return decodeHTMLEntities;
			})();
			

    		var decodedStr = decodeEntities(value);
    		decodedStr = decodedStr.replace(/&c/g, ",");
    		decodedStr = decodedStr.replace(/&o/g, "[");
    		decodedStr = decodedStr.replace(/&f/g, "]");
    		
    		return decodedStr;
		};
		
		return function(object, method, oneToArray) {
			if (!method) method = 'toString';
			if (!oneToArray) oneToArray = false;

			if (method == 'toArray'){
				if (object.charAt(0) == '[' && object.charAt(object.length - 1) == ']'){
					var res = object.substring(1, object.length - 1); 
			    	var res = res.split(",");
			    	var strings = [];
			    	angular.forEach(res, function(value) {
			    		if (value != ''){
				    		strings.push(decode(value));  
			    		}
			    	});
			    	return strings;
				} else {
					console.log(object);
					var strings = [];
					if (object != '') strings.push(decode(object));
					return strings;
				}

			} else {
				if (object && object.length == 1 && oneToArray) return object[0];
				var res = '';
		    	angular.forEach(object, function(value) {

		    		
		    		res += encode(value) + ',';
		    		
		    	});
		    	if (res != ''){
		    		res = '[' + res + ']';
		    	}
		    	return res;
			}
		};
	});
	
	

    //Services
    fModule.service('UiAssignmentService', ['$q', '$log', '$uibModal', 'boConfig', function($q, $log, $uibModal, boConfig) {		
		self = this;
		self.getObjects = function(values, objectType, objectId, objectField, kind, many, disablePreChecked, modalSize, pageSize, page) {
			if (!values) $log.error('values is mandatory !');
			if (!objectType) $log.error('objectType is mandatory !');
			if (!objectField) $log.error('objectField is mandatory !');
			if (!kind) kind = 'object';	
			if (!modalSize) modalSize = 'lg';
			if (!pageSize) pageSize = 5;
			if (!page) page = 0;

			var deferred = $q.defer();
			var typeCtrl = kind.charAt(0).toUpperCase() + kind.slice(1);
			var instance = $uibModal.open({
				templateUrl: boConfig.tplPath + '/ui-' + kind + '-assignement-modal.html',
				controller: 'Ui' + typeCtrl + 'AssignmentModalCtrl',
				size: modalSize,
				resolve: {
		            values: function(){
		            	return values;
		            },
					urlInfos: function () {
		                return {
		                	type: objectType, 
		                	field: objectField, 
		                	id: objectId, 
		                	many: many,
							pageable: {size: pageSize, page: page,},
						}
		            },
		            disablePreChecked: function(){
		                return disablePreChecked;
		            },
		            kind: function(){
		            	return kind;
		            },
				}
			});
			instance.result.then(function(data) {
				deferred.resolve(data);
			}, function () {
				
		        console.log('Modal dismissed at: ' + new Date());
		    });
			return deferred.promise;
			
		};
		

	}]);
	
	
	//Controller
    fModule.controller('UiObjectAssignmentModalCtrl', ['$rootScope', '$scope', '$uibModalInstance', 'boConfig', 'values', 'urlInfos', 'kind', 'disablePreChecked', function($rootScope, $scope, $uibModalInstance, boConfig, values, urlInfos, kind, disablePreChecked) {			
		$scope.init = function(disable) {
			$scope.values = [];
			angular.extend($scope.values, values);
			for(var i = 0; i < $scope.values.length; i++) {
				$scope.lastCheckedId = $scope.values[i].id;
				$scope['chk' + $scope.values[i].id] = true;
				//en Many, on ne peut pas enlever des objets déjà assignés en base
				if (disable === true) $scope['dsb' + $scope.values[i].id] = true;
			}
		};

		$scope.init(disablePreChecked);
		
		$scope.updateValues = function(type, id, value) {
			if (value) $scope.values.push({type: type, id: id});
			else {
				var index = null;
				for(var i = 0; i < $scope.values.length; i++) {
					if ($scope.values[i].id == id && $scope.values[i].type == type){
						index = i;
						break;
					}
				}
				if (index != null) $scope.values.splice(index, 1);
			}
		};
		$scope.overrideValues = function(type, id) {
			$scope.values = [{type: type, id: id}];
		};

		$scope.mkParams = function(pageable) {
			var params = '';
			var sep = '?';
			if (pageable.size){
				params += sep + 'size=' + pageable.size;
				sep = '&';
			}
			if (pageable.sort){
				params += sep + 'sort=' + pageable.sort;
				sep = '&';
			}
			if (pageable.page){
				params += sep + 'page=' + pageable.page;
				sep = '&';
			}
			return params;
		};
		
		$scope.mkUrl = function(urlInfos) {
			var id = urlInfos.id == '' ? 0 : urlInfos.id;
			var url = boConfig.basePath + '/bo/blocklist/' + urlInfos.type + '/' + id + '/' + urlInfos.field;
			$scope.urlInfos = urlInfos;
			$scope.urlMaked = url + $scope.mkParams(urlInfos.pageable);
			$scope.init();
		};
		
		$scope.updateUrlType = function(type) {
			urlInfos.type = type;
			$scope.mkUrl(urlInfos);
		};
		$scope.updateUrlPageableSize = function(size) {
			urlInfos.pageable.size = size;
			$scope.mkUrl(urlInfos);
		};
		$scope.updateUrlPageableSort = function(sort) {
			urlInfos.pageable.sort = sort;
			$scope.mkUrl(urlInfos);
		};
		$scope.updateUrlPageablePage = function(page) {
			urlInfos.pageable.page = page;
			$scope.mkUrl(urlInfos);
		};
		
		$scope.mkUrl(urlInfos);


		$scope.cancel = function() {
			$uibModalInstance.dismiss('cancel');
		};
		$scope.save = function(form) {
			if (form.$valid) {
				$uibModalInstance.close($scope.values);
			} else {
				$uibModalInstance.dismiss('cancel');
			}
		};
		
	}]);

	fModule.controller('UiFileAssignmentModalCtrl', ['$rootScope', '$scope', '$uibModalInstance', 'boConfig', 'values', 'urlInfos', 'kind', 'disablePreChecked', function($rootScope, $scope, $uibModalInstance, boConfig, values, urlInfos, kind, disablePreChecked) {
		$scope.mkUrl = function(urlInfos) {
			var url = boConfig.basePath + '/bo/file/single/?navbar=false&multi=' + urlInfos.many;
			$scope.urlMaked = url;
		}
		$scope.mkUrl(urlInfos);
		
		$rootScope.$on('filemanagerSelectItemChanged', function(event, data) {
			$scope.values = [];
			for(var i = 0; i < data.length; i++) {
				var item = data[i].model;
				if (item.type == 'file'){
					$scope.values.push(boConfig.basePath + '/' + boConfig.filesPath + item.fullPath());
				}
			}
			if ($scope.templateForm){
				$scope.templateForm.$dirty = $scope.values.length > 0;
			}
		});


		$scope.cancel = function() {
			$uibModalInstance.dismiss('cancel');
		};
		$scope.save = function(form) {
			if (form.$valid) {
				$uibModalInstance.close($scope.values);
			} else {
				$uibModalInstance.dismiss('cancel');
			}
		};
		
	}]);
	
}());