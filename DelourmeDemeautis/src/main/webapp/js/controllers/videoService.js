/**
Service pour manipuler les videos.
*/

videogenApp.factory('videoFactory', function( $http, $rootScope ){
	var factory = {};
	// Retourne toutes les personnes
	factory.generate = function() {
		$rootScope.generatedVideo='';
				return $http({
					method: 'GET',
					url: 'http://localhost:9200/generate'
				}).then(function successCallback(response) {
						//console.log(response.data);
						$rootScope.generatedVideo = response.data;
						//console.log(typeof $rootScope.generatedVideo);

				}, function errorCallback(response) {
				})
		  };

	return factory;
});
