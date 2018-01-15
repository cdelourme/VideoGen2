
videogenApp.controller('mainCtrl', function($rootScope,$scope,$http,$routeParams,videoFactory){

	$scope.generate = function(){
		videoFactory.generate();
	}
	$scope.change = function(){
		console.log("change");
	}
})
