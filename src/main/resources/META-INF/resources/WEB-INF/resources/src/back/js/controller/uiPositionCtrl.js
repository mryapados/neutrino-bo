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
