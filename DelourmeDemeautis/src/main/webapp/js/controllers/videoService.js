/*
*	Service in order to manage http requests
*/

videogenApp.factory('videoFactory', function( $http, $rootScope ){
	var factory = {};
	// ask to the server a new random video.
	factory.generate = function() {
		$rootScope.generatedVideo='';
				return $http({
					method: 'GET',
					url: 'http://localhost:9200/generate'
				}).then(function successCallback(response) {
						$rootScope.generatedVideo = response.data;
				}, function errorCallback(response) {
					console.log("error : http://localhost:9200/generate");
				})
		  };

	// Ask to the server the list of pictures in order to compose a playlist
	factory.getThumbnail = function() {
		$rootScope.medias=[];
				return $http({
					method: 'GET',
					url: 'http://localhost:9200/custom'
				}).then(function successCallback(response) {
						$rootScope.medias = response.data;
				}, function errorCallback(response) {
					console.log("error : http://localhost:9200/custom");
				})
		  };

		// Send to the server a list of video links and GET a new VOD link
		factory.generateVOD = function() {
			return $http({
				method: 'POST',
				url: 'http://localhost:9200/generateVOD',
				data: $rootScope.videos
			}).then(function successCallback(response) {
					$rootScope.generatedVOD = response.data;
			}, function errorCallback(response) {
				console.log("error : http://localhost:9200/generateVOD");
			})
		};
	return factory;
});
