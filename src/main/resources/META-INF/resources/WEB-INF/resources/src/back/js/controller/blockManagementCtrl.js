(function() {
var bModule = angular.module("backApp");

bModule.controller('BlockManagementCtrl', function ($scope, $rootScope, BlockManagementService) {
	$scope.init = function(folderId, pageId, activeObjectId) {
		BlockManagementService.init(folderId, pageId, activeObjectId).then(function() {
			
		});
	}
});

}());