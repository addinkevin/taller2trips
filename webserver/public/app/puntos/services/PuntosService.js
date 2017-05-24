var puntos = angular.module('tripsApp.puntos');

puntos.service('PuntosService', ['$http', 'IdiomaService', '$q', function ($http, IdiomaService, $q) {
    this.createNewPunto = function() {
        var punto = {};
        punto.nombre = "";
        punto.descripcion = {'es': '', 'en': '', 'pt': ''};
        punto.imagenes = [];
        punto.idiomas_audio = [];
        punto.audios = {};
        punto.videos = [];
        punto.deleteRequests = [];
        return punto;
    };

    this.loadPuntoAudios = function(punto) {
        for (var i = 0; i < punto.idiomas_audio.length; i++) {
            var idioma = punto.idiomas_audio[i];
            var audUrl = '/api/punto/'+ punto._id + '/audio?idioma=' + idioma + '&date=' + new Date().getTime();
            punto.audios[idioma] = [{audSrc:audUrl, idiomaAudio:idioma}];
        }
    };

    this.loadPuntoVideo = function(punto) {
        var vidUrl = '/api/punto/'+ punto._id + '/video';
        $http.get(vidUrl).then(
            function success(res) {
                punto.videos.push({vidSrc:vidUrl});
            },
            function error(res) {
                punto.videos = [];
            }
        );
    };

    this.loadPuntoImagenes = function(punto) {
        var imagenes = [];
        for (var i = 0; i < punto.imagenes.length; i++) {
            var imgUrl = '/api/punto/'+ punto._id + '/imagen?filename='+punto.imagenes[i];
            imagenes.push({imgSrc:imgUrl});
        }

        punto.imagenes = imagenes;
    };


    this.loadRecursosPunto = function(punto) {
        if (punto.cargado) return;
        punto.audios = {};
        punto.videos = [];
        punto.deleteRequests = [];
        punto.cargado = true;
        this.loadPuntoAudios(punto);
        this.loadPuntoVideo(punto);
        this.loadPuntoImagenes(punto);
    };

    function clone(obj) {
        var copy;

        if (null == obj || "object" != typeof obj) return obj;

        if (obj instanceof Date) {
            copy = new Date();
            copy.setTime(obj.getTime());
            return copy;
        }

        if (obj instanceof Array) {
            copy = [];
            for (var i = 0, len = obj.length; i < len; i++) {
                copy[i] = clone(obj[i]);
            }
            return copy;
        }

        if (obj instanceof Object) {
            copy = {};
            for (var attr in obj) {
                if (obj.hasOwnProperty(attr)) copy[attr] = clone(obj[attr]);
            }
            return copy;
        }

        throw new Error();
    }

    this.copiarPunto = function(punto) {
        return clone(punto);
    };

    this.deleteAudioPunto = function(punto, puntoAudio) {
        var audUrl = puntoAudio.audSrc;
        return $http.delete(audUrl);
    };

    this.deleteImagenPunto = function(punto, puntoImagen) {
        var imgUrl = puntoImagen.imgSrc;
        return $http.delete(imgUrl);
    };

    this.deleteVideoPunto = function(punto, puntoVideo) {
        var vidUrl = puntoVideo.vidSrc;
        return $http.delete(vidUrl);
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

    this.uploadAudiosPunto = function(punto) {
        var self = this;
        var url = 'api/punto/' + punto._id + '/audio';

        var requests = [];

        var audios = [];

        Object.keys(punto.audios).forEach(function(key) {
            for (var i = 0; i < punto.audios[key].length; i++) {
                var audFile = punto.audios[key][i].audFile;
                if (audFile) {
                    requests.push(self._uploadFormData(url, {
                        idioma: punto.audios[key][i].idiomaAudio,
                        audio: audFile
                    }));
                }
            }
        });

        return $q.all(requests);
    };

    this.uploadVideosPunto = function(punto) {
        var url = 'api/punto/' + punto._id + '/video';

        var requests = [];

        for (var i = 0; i < punto.videos.length; i++) {
            var vidFile = punto.videos[i].vidFile;
            if (vidFile) {
                requests.push(this._uploadFormData(url, {
                    video: vidFile
                }));
            }
        }

        return $q.all(requests);
    };

    this.uploadImagenesPunto = function(punto) {
        var url = '/api/punto/' + punto._id + '/imagen';
        var requests = [];

        for (var i = 0; i < punto.imagenes.length; i++) {
            var imgFile = punto.imagenes[i].imgFile;
            if (imgFile) {
                requests.push(this._uploadFormData(url, {
                    imagen: imgFile
                }));
            }
        }

        return $q.all(requests);
    };

    this.addPunto = function(atraccion, punto) {
        var data = {
            "nombre": punto.nombre,
            "descripcion": JSON.stringify(punto.descripcion),
            "id_atraccion": atraccion._id
        };

        return $http({
            method: 'POST',
            url : '/api/punto',
            data: data
        })
            .then(function success(res) {
                punto._id = res.data._id;
            });

    };

    this.editPunto = function (atraccion, punto) {
        var data = {
            "_id": punto._id,
            "nombre": punto.nombre,
            "descripcion": JSON.stringify(punto.descripcion),
            "id_atraccion": atraccion._id
        };

        return $http({
            method: 'PUT',
            url : '/api/punto',
            data: data
        });
    };

    this.deletePunto = function(punto) {
        return $http({
            method: 'DELETE',
            url : '/api/punto/' + punto._id
        });
    };

    this.uploadRecursosPunto = function(punto) {
        console.log("Subuiendo recursos de:", punto);
        var requests = [this.uploadImagenesPunto(punto),this.uploadAudiosPunto(punto),this.uploadVideosPunto(punto)];
        return $q.all(requests);
    };

    this.procesarPuntosDeInteres = function(atraccion) {
        var requests = [];
        var self = this;
        for (var i = 0; i < atraccion.ids_puntos.length; i++) {
            var punto = atraccion.ids_puntos[i];
            var req;
            if (punto._id === undefined) {
                req = this.addPunto(atraccion, punto);
            } else {
                req = this.editPunto(atraccion, punto);
            }

            req.then(function() {
                return self.uploadRecursosPunto(punto);
            });

            requests.push(req);
        }

        return $q.all(requests);
    };
}]);