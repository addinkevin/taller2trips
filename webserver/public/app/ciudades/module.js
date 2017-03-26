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

ciudades.service('CiudadesService', function() {
    this.loadCiudades = function() {
        return [
            {
                name:"CABA",
                description:"Ciudad de mierda",
                imgSrc:'http://www.buenosaires.travel/wp-content/buenosaires_uploads/obelisco.jpg',
                pais:'Argentina'
            },
            {
                name:"Rosario",
                description:"Ciudad de mierda",
                imgSrc:'http://www.buenosaires.travel/wp-content/buenosaires_uploads/obelisco.jpg',
                pais:'Argentina'
            }
        ];
    };

    this.ciudades = this.loadCiudades();

    this.setCiudades = function(ciudades) {
        this.ciudades = ciudades;
    };

    this.addCiudad = function(ciudad) {
        this.ciudades.push(ciudad);
    };

    this.getCiudades = function() {
        return this.ciudades;
    };

    this.getCiudad = function (ciudadId) {
        return this.ciudades[ciudadId];
    };

    this.updateCiudad = function (ciudadId, newCiudadObject) {
        this.ciudades[ciudadId] = newCiudadObject;
    };


    this.removeCiudad = function (ciudad) {
        var index = this.ciudades.indexOf(ciudad);
        if (index > -1) {
            this.ciudades.splice(index, 1);
        }
    }
});

ciudades.directive('customOnChange', function() {
    return {
        restrict: 'A',
        link: function (scope, element, attrs) {
            var onChangeFunc = scope.$eval(attrs.customOnChange);
            element.bind('change', onChangeFunc);
        }
    };
});