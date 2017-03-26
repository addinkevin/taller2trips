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
        .when('/ciudades/remove', {
            template: '<p> borrado </p>'
        });
}]);

ciudades.service('CiudadesService', function() {
    this.ciudades = [
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

    this.setCiudades = function(ciudades) {
        this.ciudades = ciudades;
    }

    this.addCiudad = function(ciudad) {
        this.ciudades.push(ciudad);
    }

    this.getCiudades = function() {
        return this.ciudades;
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