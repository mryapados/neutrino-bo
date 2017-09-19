(function() {
	var bModule=angular.module("backServices",[]);
	bModule.config(function($httpProvider) {
		$httpProvider.interceptors.push('Interceptor');
	});
	
	bModule.factory('Interceptor', function($q) {
		return {
			responseError: function(rejection) {
				if (rejection.status === 404) {
					rejection.data = '<div class="bg-danger"><h2>Object not found</h2><p>' + rejection.config.url + '</p></div>'
					return $q.resolve(rejection);
				}
				return $q.reject(rejection);
			}
		};
	});
}());