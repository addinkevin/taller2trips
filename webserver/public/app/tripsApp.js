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

tripsApp.directive('customOnChange', function() {
    return {
        restrict: 'A',
        link: function (scope, element, attrs) {
            var onChangeFunc = scope.$eval(attrs.customOnChange);
            element.bind('change', onChangeFunc);
        }
    };
});

tripsApp.service('ServerService', [ '$http', function($http) {
        this.getCiudades = function(callback) {
        $http.get('/api/ciudad').then(
            function(res) {
                var ciudades = res.data;
                callback(ciudades, null);
            }, function(res) {
                error(null, {msg: "No se pudo hacer el get /api/ciudad"});
            }
        );
    };

    this.getCiudad = function(ciudadId, callback) {
        $http.get('/api/ciudad/'+ciudadId).then(
            function success(res) {
                callback(res.data, null);
            },
            function error(res) {
                callback(null, {msg:"Error al hacer el get de la ciudad "+ciudadId});
            }
        );
    };

    this.deleteCiudad = function(ciudadId, callback) {
        $http.delete('/api/ciudad/'+ciudadId).then(
            function success(res) {
                callback(null, null);
            },
            function error(res) {
                callback(null, {msg:"No se pudo borrar la ciudad:"+ ciudadId});
            }
        );
    };

    this.updateCiudadImage = function(ciudadObject, callback) {
        var url = '/api/ciudad/' + ciudadObject._id + '/imagen';
        $http({
            method: 'POST',
            url: url,
            headers: {
                'Content-Type': undefined
            },
            data: {
                imagen: ciudadObject.imgFile
            },
            transformRequest: function (data, headersGetter) {
                var formData = new FormData();
                angular.forEach(data, function (value, key) {
                    formData.append(key, value);
                });

                return formData;
            }
        }).then(
            function success(res) {
                callback(null, null);
            }, function error(res) {
                callback(null, {msg:"No se pudo updatear la imagen de la ciudad:"+ciudadObject._id });
            });
    };

    this.updateCiudadInfo = function(ciudadObject, callback) {
        $http({
            method: 'PUT',
            url : '/api/ciudad',
            data: {
                    _id:ciudadObject._id,
                    nombre:ciudadObject.nombre,
                    descripcion:ciudadObject.descripcion,
                    pais:ciudadObject.pais
            }
        })
            .then(function success(res) {
                callback(null, null);
            }, function error(res) {
                callback(null, {msg:"No se pudo hacer el update de la ciudad:"+ciudadObject._id});
            });
    };

    this.addCiudad = function(ciudadObject, callback) {
        $http({
            method: 'POST',
            url : '/api/ciudad',
            data:
            {
                nombre: ciudadObject.nombre,
                descripcion: ciudadObject.descripcion,
                pais: ciudadObject.pais
            }
        })
            .then(function success(res) {
                ciudadObject._id = res.data._id;
                callback(ciudadObject, null);
            }, function error(res) {
                callback(ciudadObject, {msg:"No se pudo agregar la ciudad"});
            });
    }

}]);