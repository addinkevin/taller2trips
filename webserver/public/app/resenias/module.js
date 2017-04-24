var resenias = angular.module('tripsApp.resenias', ['ngRoute']);

resenias.config(['$routeProvider', function config($routeProvider) {
    $routeProvider
        .when('/resenias/', {
            templateUrl: 'app/resenias/views/listado.html',
            controller: 'reseniasController'
        })
        .when('/resenias/:id', {
            templateUrl: 'app/resenias/views/listado.html',
            controller: 'reseniasController'
        });
}]);
