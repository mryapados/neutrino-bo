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
			$http.get(backConfig.basePath + '/@front/labels/' + options.key + '.json')
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