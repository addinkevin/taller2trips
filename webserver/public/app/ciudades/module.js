var ciudades = angular.module('tripsApp.ciudades', ['ngRoute']);

ciudades.config(['$routeProvider', function config($routeProvider) {
    $routeProvider
        .when('/ciudades/add', {
            templateUrl: 'app/ciudades/views/add.html',
            controller: 'ciudadesAddController'
        })
        .when('/ciudades/listado', {
            templateUrl: 'app/ciudades/views/listado.html',
            controller: 'ciudadesListadoController'
        })
        .when('/ciudades/remove', {
            template: '<p> borrado </p>'
        });
}]);

ciudades.service('CiudadesService', function() {
    this.ciudades = {};
    this.setCiudades = function(ciudades) {
        this.ciudades = ciudades;
    }

    this.getCiudades = function() {
        return this.ciudades;
    }

});