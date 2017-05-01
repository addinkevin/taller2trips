var push = angular.module('tripsApp.push', ['ngRoute']);

push.config(['$routeProvider', function config($routeProvider) {
    $routeProvider
        .when('/push', {
            templateUrl: 'app/push/views/form.html',
            controller: 'pushListadoController'
        })
        .when('/push/listado', {
            templateUrl: 'app/push/views/listado.html',
            controller: 'pushListadoController'
        })
        .when('/push/form', {
            templateUrl: 'app/push/views/form.html',
            controller: 'pushFormController'
        })
        .when('/push/form/:id', {
            templateUrl: 'app/push/views/form.html',
            controller: 'pushFormController'
        });
}]);
