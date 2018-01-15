/**
Service pour manipuler les videos.
*/

videogenApp.factory('videoFactory', function( $http, $rootScope ){
	var factory = {};
	// Retourne une video generee automatiquement
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
	// Retourne toutes les images pour composer la video
	factory.getThumbnail = function() {
		$rootScope.media=[];
		console.log("methode getThumbnail factory");
				return $http({
					method: 'GET',
					url: 'http://localhost:9200/custom'
				}).then(function successCallback(response) {
						console.log(response.data);
						$rootScope.media = response.data;
						//console.log(typeof $rootScope.generatedVideo);
						console.log("methode retour data factory");
				}, function errorCallback(response) {
					console.log("error");
				})
		  };
	return factory;
});
