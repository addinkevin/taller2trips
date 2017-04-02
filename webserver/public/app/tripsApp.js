var tripsApp = angular.module('tripsApp', ["ngRoute", 'tripsApp.ciudades', 'tripsApp.atracciones']);

tripsApp.config(function config($routeProvider, $locationProvider) {
    $routeProvider.when('/home', {
        templateUrl: 'app/home.html'
    })
        .otherwise({ redirectTo: '/home'});
    $locationProvider.hashPrefix('');
});

tripsApp.service('GoogleMaps', function() {
    this.maps = {};
    this.createMap = function(mapId, properties) {
        this.maps[mapId] = {};
        this.maps[mapId].map = new google.maps.Map(document.getElementById('map'), properties);
        return this.maps[mapId];
    };

    this.getMap = function(mapId) {
        return this.maps[mapId];
    };

});

tripsApp.service('Common', function() {
   //this ..// TODO
});

tripsApp.directive('customOnChange', function() {
    return {
        restrict: 'A',
        link: function (scope, element, attrs) {
            var onChangeFunc = scope.$eval(attrs.customOnChange);
            element.bind('change', onChangeFunc);
        }
    };
});