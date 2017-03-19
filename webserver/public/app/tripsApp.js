var tripsApp = angular.module('tripsApp', ["ngRoute", 'tripsApp.ciudades']);

tripsApp.config(function config($routeProvider, $locationProvider) {
    $routeProvider.when('/', {
        templateUrl: 'app/home.html'
    })
        .otherwise({ redirectTo: '/'});
    $locationProvider.hashPrefix('');
});