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

tripsApp.service('ServerService', [ '$http', '$q', function($http, $q) {
        this.getCiudades = function(callback) {
        $http.get('/api/ciudad').then(
            function(res) {
                var ciudades = res.data;
                callback(ciudades, null);
            }, function(res) {
                callback(null, {msg: "No se pudo hacer el get /api/ciudad"});
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
    };

    this.addAtraccion = function(atraccion, callback) {
        var data = {
            "nombre": atraccion.nombre,
            "descripcion": atraccion.descripcion,
            "costo_monto": atraccion.montoCosto,
            "costo_moneda": atraccion.monedaCosto,
            "hora_apertura": atraccion.horaApertura,
            "hora_cierre": atraccion.horaCierre,
            "duracion": atraccion.duracion,
            "clasificacion": atraccion.clasificacionSelected,
            "id_ciudad": atraccion.ciudadSelected._id,
            "latitud": atraccion.lat,
            "longitud": atraccion.lng
        };

        $http({
            method: 'POST',
            url : '/api/atraccion',
            data: data
        })
            .then(function success(res) {
                atraccion._id = res.data._id;
                callback(res.data, null);
            }, function error(res) {
                callback(null, {msg: "Error al agregar la atracción" });
            });

    };

    this.editAtraccion = function (atraccion, callback) {
        var data = {
            "_id": atraccion._id,
            "nombre": atraccion.nombre,
            "descripcion": atraccion.descripcion,
            "costo_monto": atraccion.montoCosto,
            "costo_moneda": atraccion.monedaCosto,
            "hora_apertura": atraccion.horaApertura,
            "hora_cierre": atraccion.horaCierre,
            "duracion": atraccion.duracion,
            "clasificacion": atraccion.clasificacionSelected,
            "id_ciudad": atraccion.ciudadSelected._id,
            "latitud": atraccion.lat,
            "longitud": atraccion.lng
        };

        $http({
            method: 'PUT',
            url : '/api/atraccion',
            data: data
        })
            .then(function success(res) {
                callback(res.data, null);
            }, function error(res) {
                callback(null, {msg: "Error al modificar la atracción" });
            });
    };


    this.deleteAtraccion = function(atraccionId, callback) {
        var url = '/api/atraccion/' + atraccionId;
        $http.delete(url).then(
            function success() {
                callback(null, null);
            },
            function error() {
                callback(null, {msg:"No se pudo borrar la atracción" });
            }
        );
    };

    this.getAtraccion = function(atraccionId, callback) {
        $http.get('/api/atraccion/'+atraccionId).then(
            function success(res) {
                callback(res.data, null);
            },
            function error(res) {
                callback(null, {msg:"Error al hacer el get de la atraccion "+atraccionId});
            }
        );
    };

    this._uploadFormData = function(url, data) {
        return $http({
            method: 'POST',
            url: url,
            headers: {
                'Content-Type': undefined
            },
            data: data,
            transformRequest: function (data, headersGetter) {
                var formData = new FormData();
                angular.forEach(data, function (value, key) {
                    formData.append(key, value);
                });

                return formData;
            }
        });
    };

    this.uploadVideosAtraccion = function(atraccion, callback) {
        var url = 'api/atraccion/' + atraccion._id + '/video';

        var requests = [];

        for (var i = 0; i < atraccion.videos.length; i++) {
            var vidFile = atraccion.videos[i].vidFile;
            console.log(vidFile);
            if (vidFile) {
                requests.push(this._uploadFormData(url, {
                    video: vidFile
                }));
            }
        }

        $q
            .all(requests)
            .then(function success(values) {
                callback(null,null);
            }, function error() {
                callback(null, {msg:"No fue posible subir todos los videos." } );
            });
    };

    this.uploadImagesAtraccion = function(atraccion, callback) {

        var url = '/api/atraccion/' + atraccion._id + '/imagen';
        var requests = [];

        for (var i = 0; i < atraccion.imagenes.length; i++) {
            var imgFile = atraccion.imagenes[i].imgFile;
            if (imgFile) {
                requests.push(this._uploadFormData(url, {
                    imagen: imgFile
                }));
            }
        }

        $q
            .all(requests)
            .then(function success(values) {
                callback(null,null);
            }, function error() {
                callback(null, {msg:"No fue posible subir todas las imagenes." });
            });
    };

    this.deleteVideoAtraccion = function(atraccion, atraccionVideo, callback) {
        var vidUrl = atraccionVideo.vidSrc;
        console.log(vidUrl);
        $http.delete(vidUrl).then(
            function success() {
                callback(null, null);
            },
            function error() {
                callback(null, {msg:"No se pudo borrar el video de la atracción" });
            }
        );

    };

    this.deleteImageAtraccion = function(atraccion, atraccionImage, callback) {
        var imgUrl = atraccionImage.imgSrc;

        $http.delete(imgUrl).then(
            function success() {
                callback(null, null);
            },
            function error() {
                callback(null, {msg:"No se pudo borrar la image de la atracción" });
            }
        );
    };
}]);