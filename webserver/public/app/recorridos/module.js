var recorridos = angular.module('tripsApp.recorridos', ['ngRoute']);

recorridos.config(['$routeProvider', function config($routeProvider) {
    $routeProvider
        .when('/recorridos/', {
            templateUrl: 'app/recorridos/views/listado.html',
            controller: 'recorridoListadoController'
        })
        .when('/recorridos/add', {
            templateUrl: 'app/recorridos/views/form.html',
            controller: 'recorridoFormController'
        })
        .when('/recorridos/edit/:id', {
            templateUrl: 'app/recorridos/views/form.html',
            controller: 'recorridoFormController'
        });
}]);
