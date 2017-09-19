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
