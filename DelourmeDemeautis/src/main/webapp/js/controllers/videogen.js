//'use strict';

/**
 * Déclaration de l'application routeApp
 */
var videogenApp = angular.module('videogenApp', [
    // Dépendances du "module"
    'ngRoute',
    'ngResource'
]);

/**
 * Configuration des routes
 */
videogenApp.config(['$routeProvider',
    function($routeProvider) {
        // Système de routage
        $routeProvider.when('/page1', { templateUrl: 'views/page1.html', controller: 'mainCtrl'});
        $routeProvider.when('/page2', { templateUrl: 'views/page2.html', controller: 'customCtrl'});

        $routeProvider.otherwise({ redirectTo: '/page1'});
    }
]);
