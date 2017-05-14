var push = angular.module('tripsApp.push', ['ngRoute']);

push.config(['$routeProvider', function config($routeProvider) {
    $routeProvider
        .when('/push/', {
            templateUrl: 'app/push/views/listado.html',
            controller: 'pushListadoController'
        })
        .when('/push/listado', {
            templateUrl: 'app/push/views/listado.html',
            controller: 'pushListadoController'
        })
        .when('/push/add', {
            templateUrl: 'app/push/views/form.html',
            controller: 'pushFormController'
        })
        .when('/push/edit/:id', {
            templateUrl: 'app/push/views/form.html',
            controller: 'pushFormController'
        });
}]);
