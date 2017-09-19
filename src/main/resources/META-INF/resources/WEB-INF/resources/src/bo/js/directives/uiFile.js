(function() {

	var fModule = angular.module("boApp");
	fModule.directive('uiFile', ['$parse', '$q', 'boConfig', function($parse, $q, boConfig) {		
        return {
            restrict: 'EA',
            scope: {
            	url: '=',
            },
			controller: ['$scope', function ( $scope ) {
				var filename = $scope.url.substring($scope.url.lastIndexOf('/')+1);
				$scope.name = filename;
				
    			var doesFileExist = function(url) 
    			{
    				var deferred = $q.defer();
    			    var request = new XMLHttpRequest();
    			    request.onreadystatechange = function()
    			    {
    			        if (request.readyState == 4)
    			        {
    					    if (request.status == "404") {
    					    	deferred.resolve(false);
    					    } else {
    					    	deferred.resolve(true);
    					    }
    			        }
    			    }; 
    			    request.open('GET', url);
    			    request.send();
    			    return deferred.promise;
    			}
    			
            	doesFileExist($scope.url).then(function(exist) {
            		var code = null;
            		if (!exist) code ='404';
            		else code = $scope.url.split('.').pop().toLowerCase();

                	var basePath = boConfig.tplPath + '/resources/';
                	var extensions = 
                    {
                		'png': $scope.url,
                		'jpg': $scope.url,
                		'jpeg': $scope.url,
                		'gif': $scope.url,
                		'bmp': $scope.url,
                		'pdf': basePath + 'pdf.png',
                		'ND': basePath + 'nd.png',
                		'404': basePath + '404.png',
                		'LOADING': basePath + 'loading.png',
                    }
                	var result = extensions[code];
                    if (result == null){
                    	result = extensions['ND'];
                    }
                   
                    $scope.src = result;
            		
            		
    			});
			}], 
            templateUrl: boConfig.tplPath + '/ui-file.html',
            link: function(scope, element, attrs) {

            }
        };
	}]);



	
}());