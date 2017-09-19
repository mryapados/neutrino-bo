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