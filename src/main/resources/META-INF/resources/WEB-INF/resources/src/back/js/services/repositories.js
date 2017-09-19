(function() {
	var bModule = angular.module('backServices');

	bModule.factory("TemplateRepository", function($http, backConfig) {
		return {
			exist: function(context, kind, path, name) {
				return $http.get(
						backConfig.serverRest + '/templates/exist/', 
					{params:{'context': context, 'kind': kind, 'path': path, 'name': name}}
				).then(function(response) {
					return response.data;
				})
				.catch(function(error){
					console.log(error.status);
					console.log(error);
				});
			}, 
		};
	});
	
	bModule.factory("TemplateResource", function($resource, backConfig) {
	  	var API_URI = backConfig.serverRest + '/template';  				
	  	return $resource(API_URI, {id: '@id'}, {
	  		create: {method: 'POST'},
	  		get:    {method: 'GET'},
	  		getAll: {method: 'GET', isArray: true},
	  		remove: {method: 'DELETE'},
	  		update: {method: 'PUT'}
	  	});
	});
	
	bModule.factory("LangResource", function($resource, backConfig) {
	  	var API_URI = backConfig.serverRest + '/lang';  				
	  	return $resource(API_URI, {id: '@id', code: '@code'}, {
	  		create: {method: 'POST'},
	  		get:    {method: 'GET'},
	  		getAll: {method: 'GET', isArray: true},
	  		remove: {method: 'DELETE'},
	  		update: {method: 'PUT'}
	  	});
	});
	
	
	bModule.factory("FolderResource", function($resource, backConfig) {
	  	var API_URI = backConfig.serverRest + '/folder';  				
	  	return $resource(API_URI, {id: '@id', name: '@name'}, {
	  		create: {method: 'POST'},
	  		get:    {method: 'GET'},
	  		getAll: {method: 'GET', isArray: true},
	  		remove: {method: 'DELETE'},
	  		update: {method: 'PUT'}
	  	});
	});
	
	bModule.factory("PageResource", function($resource, backConfig) {
	  	var API_URI = backConfig.serverRest + '/page';  				
	  	return $resource(API_URI, {id: '@id'}, {
	  		create: {method: 'POST'},
	  		get:    {method: 'GET'},
	  		getAll: {method: 'GET', isArray: true},
	  		remove: {method: 'DELETE'},
	  		update: {method: 'PUT'}
	  	});
	});
	
	bModule.factory("BlockResource", function($resource, backConfig) {
	  	var API_URI = backConfig.serverRest + '/block';  				
	  	return $resource(API_URI, {modelId: '@modelId', pageId: '@pageId', activeObjectId: '@activeObjectId', positionId: '@positionId'}, {
	  		create: {method: 'POST'},
	  		get:    {method: 'GET'},
	  		getAll: {method: 'GET', isArray: true},
	  		remove: {method: 'DELETE'},
	  		update: {method: 'PUT'}
	  	});
	});
	
	bModule.factory("MapTemplateResource", function($resource, backConfig) {
	  	var API_URI = backConfig.serverRest + ' /mapTemplate';			
	  	return $resource(API_URI, {id: '@id'}, {
	  		save:   {method: 'POST'},
	  		get:    {method: 'GET'},
	  		getAll: {method: 'GET', isArray: true},
	  		remove: {method: 'DELETE'},
	  		update: {method: 'PUT'}
	  	});
	});
	
	bModule.factory("TObjectResource", function($resource, backConfig) {
	  	var API_URI = backConfig.serverRest + ' /tobject';			
	  	return $resource(API_URI, {id: '@id'}, {
	  		save:   {method: 'POST'},
	  		get:    {method: 'GET'},
	  		getAll: {method: 'GET', isArray: true},
	  		remove: {method: 'DELETE'},
	  		update: {method: 'PUT'}
	  	});
	});
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	bModule.factory("PositionRepository", function($http, backConfig) {
		return {
			get: function(positionName) {
				return $http.get(backConfig.serverRest + '/positions/'+ positionName)
				.then(function(response) {
					return response.data;
				})
				.catch(function(error){
					console.log(error.status);
					console.log(error);
				});
			},
			getAll: function() {
				return $http.get(backConfig.serverRest + '/positions/')
				.then(function(response) {
		            return response.data;
				})
				.catch(function(error){
					console.log(error.status);
					console.log(error);
				});

			}

		};
	});

	bModule.factory("MapTemplateRepository", function($http, backConfig) {
		return {
			remove: function(idTemplateBlock) {
				return $http.delete(backConfig.serverRest + '/removemapblock/' + idTemplateBlock)
				.then(function(response) {

				})
				.catch(function(error){
					console.log(error.status);
					console.log(error);
				});
			},
			add: function(dataObj) {
	    		return $http.post(backConfig.serverRest + '/addmapblock', dataObj).then(function(response) {
	    			return response.data;
				}).catch(function(error){
					console.log(error.status);
					console.log(error);
				});

			}
		};
	});
	
	

	

	
	
}());