var tripsApp = angular.module('tripsApp', ["ngRoute", 'tripsApp.ciudades', 'tripsApp.atracciones', 'tripsApp.resenias', 'tripsApp.push']);

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

tripsApp.directive('onErrorVideo', function() {
    return {
        restrict: 'A',
        link: function (scope, element, attrs) {
            var func = scope.$eval(attrs.onErrorVideo);
            element.bind('error', func);
        }
    };
});

tripsApp.service('ServerService', [ '$http', '$q', function($http, $q) {
    this.getCiudades = function(callback) {
        return $http.get('/api/ciudad').then(
            function(res) {
                var ciudades = res.data;
                callback(ciudades, null);
            }, function(res) {
                callback(null, {msg: "No se pudo hacer el get /api/ciudad"});
            }
        );
    };

    this.getCiudad = function(ciudadId, callback) {
        return $http.get('/api/ciudad/'+ciudadId).then(
            function success(res) {
                callback(res.data, null);
            },
            function error(res) {
                callback(null, {msg:"Error al hacer el get de la ciudad "+ciudadId});
            }
        );
    };

    this.deleteCiudad = function(ciudadId, callback) {
        return $http.delete('/api/ciudad/'+ciudadId).then(
            function success(res) {
                callback(null, null);
            },
            function error(res) {
                if (res.status == 409) {
                    // Borrado de una ciudad que tiene atracciones.
                    callback(null, { msg: "No puedes borrar una ciudad que tiene atracciones agregadas." });
                } else {
                    callback(null, {msg:"No se pudo borrar la ciudad. Inténtelo nuevamente" });
                }
            }
        );
    };

    this.updateCiudadImage = function(ciudadObject, callback) {
        var url = '/api/ciudad/' + ciudadObject._id + '/imagen';
        return $http({
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
        return $http({
            method: 'PUT',
            url : '/api/ciudad',
            data: {
                    _id:ciudadObject._id,
                    nombre:ciudadObject.nombre,
                    descripcion:JSON.stringify(ciudadObject.descripcion),
                    pais:ciudadObject.pais
            }
        })
            .then(function success(res) {
                callback(null, null);
            }, function error(res) {
                if (res.status == 405) {
                    callback(null, { msg: "No puedes repetir el par (ciudad, pais) debido a que ya existe. " });
                } else {
                    callback(null, {msg:"No se pudo hacer el update de la ciudad:"+ciudadObject._id});
                }
            });
    };

    this.addCiudad = function(ciudadObject, callback) {
        return $http({
            method: 'POST',
            url : '/api/ciudad',
            data:
            {
                nombre: ciudadObject.nombre,
                descripcion: JSON.stringify(ciudadObject.descripcion),
                pais: ciudadObject.pais
            }
        })
            .then(function success(res) {
                ciudadObject._id = res.data._id;
                callback(ciudadObject, null);
            }, function error(res) {
                if (res.status == 405) {
                    callback(null, { msg: "No puedes agregar la ciudad de dicho país debido a que ya fue agregada." });
                } else {
                    callback(ciudadObject, {msg: "No se pudo agregar la ciudad"});
                }
            });
    };

    this.addAtraccion = function(atraccion, callback) {
        var data = {
            "nombre": atraccion.nombre,
            "descripcion": JSON.stringify(atraccion.descripcion),
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

        return $http({
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
            "descripcion": JSON.stringify(atraccion.descripcion),
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

        return $http({
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
        return $http.delete(url).then(
            function success() {
                callback(null, null);
            },
            function error() {
                callback(null, {msg:"No se pudo borrar la atracción" });
            }
        );
    };

    this.getAtraccion = function(atraccionId, callback) {
        return $http.get('/api/atraccion/'+atraccionId).then(
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

    this.uploadAudiosAtraccion = function(atraccion, callback) {
        var self = this;
        var url = 'api/atraccion/' + atraccion._id + '/audio';

        var requests = [];

        var audios = [];

        Object.keys(atraccion.audios).forEach(function(key) {
            for (var i = 0; i < atraccion.audios[key].length; i++) {
                var audFile = atraccion.audios[key][i].audFile;
                if (audFile) {
                    requests.push(self._uploadFormData(url, {
                        idioma: atraccion.audios[key][i].idiomaAudio,
                        audio: audFile
                    }));
                }
            }
        });

        return $q
            .all(requests)
            .then(function success(values) {
                callback(null,null);
            }, function error() {
                callback(null, {msg:"No fue posible subir todos los audios." } );
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

        return $q
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

        return $q
            .all(requests)
            .then(function success(values) {
                callback(null,null);
            }, function error() {
                callback(null, {msg:"No fue posible subir todas las imagenes." });
            });
    };

    this.uploadPlanosAtraccion = function(atraccion, callback) {
        var url = '/api/atraccion/' + atraccion._id + '/plano';
        var requests = [];

        for (var i = 0; i < atraccion.planos.length; i++) {
            var imgFile = atraccion.planos[i].imgFile;
            if (imgFile) {
                requests.push(this._uploadFormData(url, {
                    plano: imgFile
                }));
            }
        }

        return $q
            .all(requests)
            .then(function success(values) {
                callback(null,null);
            }, function error() {
                callback(null, {msg:"No fue posible subir los planos." });
            });
    };

    this.deleteAudioAtraccion = function(atraccion, atraccionAudio, callback) {
        var audUrl = atraccionAudio.audSrc;
        return $http.delete(audUrl).then(
            function success() {
                callback(null, null);
            },
            function error() {
                callback(null, {msg:"No se pudo borrar el audio de la atracción" });
            }
        );
    };

    this.deleteVideoAtraccion = function(atraccion, atraccionVideo, callback) {
        var vidUrl = atraccionVideo.vidSrc;
        console.log(vidUrl);
        return $http.delete(vidUrl).then(
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

        return $http.delete(imgUrl).then(
            function success() {
                callback(null, null);
            },
            function error() {
                callback(null, {msg:"No se pudo borrar la image de la atracción" });
            }
        );
    };

    this.deletePlanoAtraccion = function(atraccion, atraccionPlano, callback) {
        var imgUrl = atraccionPlano.imgSrc;

        return $http.delete(imgUrl).then(
            function success() {
                callback(null, null);
            },
            function error() {
                callback(null, {msg:"No se pudo borrar el plano de la atracción" });
            }
        );
    };
}]);