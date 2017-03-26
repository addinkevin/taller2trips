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
            controller: 'atraccionesAddController'
        })
        .when('/atracciones/edit/:id', {
            templateUrl: 'app/atracciones/views/edit.html',
            controller: 'atraccionesEditController'
        });
}]);

atracciones.service('AtraccionesService', function() {
    this.loadAtracciones = function() {
        // TODO
        return [];
    };

    this.atracciones = this.loadAtracciones();

    this.setAtracciones = function(atracciones) {
        this.atracciones = atracciones;
    };

    this.addAtraccion = function(atraccion) {
        this.atracciones.push(atraccion);
    };

    this.getAtracciones = function() {
        return this.atracciones;
    };

    this.removeAtraccion = function (atraccion) {
        var index = this.ciudades.indexOf(atraccion);
        if (index > -1) {
            this.atracciones.splice(index, 1);
        }
    };

});
