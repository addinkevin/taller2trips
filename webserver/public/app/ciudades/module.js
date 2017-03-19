var ciudades = angular.module('tripsApp.ciudades', ['ngRoute']);

ciudades.config(['$routeProvider', function config($routeProvider) {
    $routeProvider.when('/ciudades', {
        templateUrl: 'app/ciudades/views/ciudades.html',
        controller: 'ciudadesController'
    });
}]);
