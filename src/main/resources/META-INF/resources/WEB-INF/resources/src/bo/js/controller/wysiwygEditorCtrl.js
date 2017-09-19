var fModule = angular.module('boApp');

fModule.controller('WysiwygEditorCtrl', ['$scope', 'textAngularManager', function($scope, textAngularManager) {
    $scope.version = textAngularManager.getVersion();
    $scope.versionNumber = $scope.version.substring(1);
    $scope.disabled = false;
}]);
