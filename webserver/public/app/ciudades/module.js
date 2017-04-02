var ciudades = angular.module('tripsApp.ciudades', ['ngRoute']);

ciudades.config(['$routeProvider', function config($routeProvider) {
    $routeProvider
        .when('/ciudades', {
            templateUrl: 'app/ciudades/views/listado.html',
            controller: 'ciudadesListadoController'
        })
        .when('/ciudades/listado', {
            templateUrl: 'app/ciudades/views/listado.html',
            controller: 'ciudadesListadoController'
        })
        .when('/ciudades/add', {
            templateUrl: 'app/ciudades/views/add.html',
            controller: 'ciudadesAddController'
        })
        .when('/ciudades/edit/:id', {
            templateUrl: 'app/ciudades/views/edit.html',
            controller: 'ciudadesEditController'
        });
}]);
