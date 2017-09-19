(function(angular) {
    'use strict';
    angular.module('boApp').provider('boConfig', function() {

        var values = {
        	basePath: '',
        	tplPath: 'src/bo/js/templates',
            i18nPath: 'src/bo/js/i18n',
            filePath: 'files/'
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
