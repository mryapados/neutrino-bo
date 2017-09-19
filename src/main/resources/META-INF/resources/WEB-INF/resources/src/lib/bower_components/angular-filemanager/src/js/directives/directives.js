(function(angular) {
    'use strict';
    var app = angular.module('FileManagerApp');

    app.directive('angularFilemanager', ['$parse', 'fileManagerConfig', function($parse, fileManagerConfig) {
        return {
            restrict: 'EA',
            templateUrl: fileManagerConfig.tplPath + '/main.html',
            link: function(scope, element, attrs) {

            	var multiSelect = fileManagerConfig.multiSelect;
            	if ('multiSelect' in attrs){
            		if (attrs.multiSelect === 'true' || attrs.multiSelect === '') multiSelect = true;
            		else if (attrs.multiSelect === 'false') multiSelect = false;
            	}
            	$parse('multiSelect').assign(scope, multiSelect);
            	
            	var navbar = fileManagerConfig.navbar;
            	if ('navbar' in attrs){
            		if (attrs.navbar === 'true' || attrs.navbar === '') navbar = true;
            		else if (attrs.navbar === 'false') navbar = false;
            	}
            	$parse('navbar').assign(scope, navbar);

            }
        };
    }]);

    app.directive('ngFile', ['$parse', function($parse) {
        return {
            restrict: 'A',
            link: function(scope, element, attrs) {
                var model = $parse(attrs.ngFile);
                var modelSetter = model.assign;

                element.bind('change', function() {
                    scope.$apply(function() {
                        modelSetter(scope, element[0].files);
                    });
                });
            }
        };
    }]);

    app.directive('ngRightClick', ['$parse', function($parse) {
        return function(scope, element, attrs) {
            var fn = $parse(attrs.ngRightClick);
            element.bind('contextmenu', function(event) {
                scope.$apply(function() {
                    event.preventDefault();
                    fn(scope, {$event: event});
                });
            });
        };
    }]);
    
})(angular);
