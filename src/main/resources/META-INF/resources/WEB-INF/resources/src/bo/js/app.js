(function() {

	var fModule = angular.module('boApp', ['frontServices', 'ui.bootstrap', 'textAngular', 'ngSanitize', 'pascalprecht.translate', 'ngCookies', 'ngResource']);
	fModule.config(function($translateProvider) {
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
	fModule.run(function($cookies) {

		
	});
	fModule.factory('i18nLoader', function ($http, $q, $frontPath) {
	    return function (options) {
		    var deferred = $q.defer();
		    $http.get($frontPath.URL_I18N + options.key + '.json')
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