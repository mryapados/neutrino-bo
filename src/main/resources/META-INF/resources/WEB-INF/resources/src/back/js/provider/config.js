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
