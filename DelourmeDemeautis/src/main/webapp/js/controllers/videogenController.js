
videogenApp.controller('mainCtrl', function($rootScope,$scope,$http,$routeParams,videoFactory){

	$scope.generate = function(){
		videoFactory.generate();
	}
	$scope.change = function(){
		console.log("change");
	}
})

videogenApp.controller('customCtrl', function($rootScope,$scope,$http,$routeParams,videoFactory){

	$scope.listeVideo = [];

	$scope.generate = function(){
		console.log($scope.listeVideo );
		//videoFactory.generate();
	}

	$scope.generateIHM = function(){
		videoFactory.getThumbnail().then(function() {
  		console.log();
			$scope.refresh();
			});
	}
	$scope.addVideo = function(value1, value2){
		console.log(value1);
		console.log(value2);
	}

	// on affiche toutes les videos pour un média donné
	$scope.addLigne = function( media, i ){
		maDiv = document.createElement("div");
		maDiv.id = 'media_i';
		document.getElementById('ihm').appendChild(maDiv);

		var taille2 = media.videolocation.length;
		for (var j = 0; j < taille2; j++) {
			//cmd="<img ng-click=\"addVideo("+media.videolocation[j]+","+i+")\" src=\"public/"+ media.thumbnailLocation[j] +"\" height=120 width=120 />"
			img = "<img ng-click=\"addVideo("+media.videolocation[j]+","+i+")\" src=\"public/"+ media.thumbnailLocation[j] +"\" height=120 width=120 />"
			cmd = "<label><input type=\"checkbox\" checklist-model=\"listeVideo\" value=\""+media.videolocation[j]+"\"> "+img+"</label>";
console.log(cmd);
			maDiv.innerHTML += cmd;
		}
		document.getElementById('ihm').appendChild(maDiv);
		document.getElementById('ihm').appendChild(document.createElement("br"));
	}

	// ajoute une ligne de texte à la page
	$scope.addTexte = function( texte ){
		monTexte = document.createElement("p");
		monTexte.innerHTML = texte;
		document.getElementById('ihm').appendChild(monTexte);
		document.getElementById('ihm').appendChild(document.createElement("br"));
	}

	// lance l'affichage des images suivant le type de média
	$scope.refresh = function(){
		$scope.listeVideo.splice(0, 20);

		document.getElementById('ihm').innerHTML="";
		var taille = $rootScope.media.length;
		for (var i = 0; i < taille; i++) {
		  var media = $rootScope.media[i];

			if (media.type == "org.xtext.example.mydsl.videoGen.impl.AlternativesMediaImpl"){
				$scope.addTexte("il nous en faut une :)");
			}else if (media.type == "org.xtext.example.mydsl.videoGen.impl.MandatoryMediaImpl"){
				$scope.addTexte("Celle ci est obligatoire :)");
			}else if (media.type == "org.xtext.example.mydsl.videoGen.impl.OptionalMediaImpl"){
				$scope.addTexte("Seulement si vous le souhaitez :)");
			}
			$scope.addLigne(media, i);
		}
	}
})
