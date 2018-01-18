/**
 *  routeApp application
 */
var videogenApp = angular.module('videogenApp', [
    // Dépendancies
    'ngRoute',
    'ngResource'
]);

/**
 *  Route systeme manager
 */
videogenApp.config(['$routeProvider',
    function($routeProvider) {

        $routeProvider.when('/page1', { templateUrl: 'views/page1.html', controller: 'mainCtrl'});
        $routeProvider.when('/page2', { templateUrl: 'views/page2.html', controller: 'customCtrl'});

        $routeProvider.otherwise({ redirectTo: '/page1'});
    }
]);
