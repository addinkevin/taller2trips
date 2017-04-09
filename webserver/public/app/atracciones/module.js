var atracciones = angular.module('tripsApp.atracciones', ['ngRoute']);

atracciones.config(['$routeProvider', function config($routeProvider) {
    $routeProvider
        .when('/atracciones', {
            templateUrl: 'app/atracciones/views/listado.html',
            controller: 'atraccionesListadoController'
        })
        .when('/atracciones/listado', {
            templateUrl: 'app/atracciones/views/listado.html',
            controller: 'atraccionesListadoController'
        })
        .when('/atracciones/add', {
            templateUrl: 'app/atracciones/views/add.html',
            controller: 'atraccionesAddEditController'
        })
        .when('/atracciones/edit/:id', {
            templateUrl: 'app/atracciones/views/add.html',
            controller: 'atraccionesAddEditController'
        });
}]);
