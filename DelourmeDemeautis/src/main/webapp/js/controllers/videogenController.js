/*
*		Controller for Page1
*/
videogenApp.controller('mainCtrl', function($rootScope,$scope,$http,$routeParams,videoFactory){

	// Ask for new random video
	$scope.generate = function(){
		videoFactory.generate();
	}

})


/*
*		Controller for Page2
*/
videogenApp.controller('customCtrl', function($rootScope,$scope,$http,$routeParams,videoFactory){

	$rootScope.videos = [];					// list of videos from checked pictures
	$rootScope.generatedVOD = "";		// link to the generated VOD

	// Ask for a new VOD
	$scope.generate = function(){
		videoFactory.generateVOD();
	}

	//Fill the table with all pictures and texts
	//Promise is for waiting the end of the factory method.
	$scope.generateIHM = function(){
		videoFactory.getThumbnail().then(function() {
			$rootScope.generatedVOD = "";
			});
	}

	// We add videos in the list. If the video is already present, we delete it.
	$scope.addVideo = function( texte ){
	 	var index = $rootScope.videos.indexOf(texte);
		if (index >= 0) {
			$rootScope.videos.splice(index, 1);
		}
		else {
			$rootScope.videos.push(texte);
		}
	};

	// convert the classname to a simple text.
	$scope.getTexte = function( texte ){
		var retour = "";
		if (texte == "org.xtext.example.mydsl.videoGen.impl.AlternativesMediaImpl"){
			retour = "il nous en faut une :)";
		}else if (texte == "org.xtext.example.mydsl.videoGen.impl.MandatoryMediaImpl"){
			retour = "Celle ci est obligatoire :)";
		}else if (texte == "org.xtext.example.mydsl.videoGen.impl.OptionalMediaImpl"){
			retour = "Seulement si vous le souhaitez :)";
		}
		return retour;
	}

})
