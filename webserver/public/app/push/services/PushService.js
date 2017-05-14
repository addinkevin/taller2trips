var push = angular.module('tripsApp.push');

push.service('PushService',  ['$http', 'IdiomaService', '$timeout', function ($http, IdiomaService, $timeout) {
    this.getDescriptionObject = function() {
        return { es: "", en:"", pt: ""};
    };

    this.getCiudades = function() {
        return $http.get('/api/ciudad');
    };

    this._addImagePush = function(push) {
        var url = '/api/push/' + push._id + '/imagen';
        return $http({
            method: 'POST',
            url: url,
            headers: {
                'Content-Type': undefined
            },
            data: {
                imagen: push.imagen.imgFile
            },
            transformRequest: function (data, headersGetter) {
                var formData = new FormData();
                angular.forEach(data, function (value, key) {
                    formData.append(key, value);
                });

                return formData;
            }
        });
    };

    this.addPush = function(push) {
        var data = {
            "nombre": push.nombre,
            "descripcion": JSON.stringify(push.descripcion),
            "id_ciudad": push.ciudad._id,
            "fecha": push.fecha,
            "hora": push.hora,
            "link": push.link
        };

        var self = this;
        return $http({
            method: 'POST',
            url : '/api/push',
            data: data
        }).then(function success(res) {
            push._id = res.data._id;
            if (push.imagen && push.imagen.imgFile) {
                return self._addImagePush(push);
            }
        });
    };

    this.editPush = function(push) {
        var data = {
            "_id": push._id,
            "nombre": push.nombre,
            "descripcion": JSON.stringify(push.descripcion),
            "id_ciudad": push.ciudad._id,
            "fecha": push.fecha,
            "hora": push.hora,
            "link": push.link
        };

        var self = this;
        return $http({
            method: 'PUT',
            url : '/api/push',
            data: data
        }).then(function success(res) {
            if (push.imagen && push.imagen.imgFile) {
                return self._addImagePush(push);
            }
        });
    };

    this.getPush = function(idPush) {
        return $http({
            method: 'GET',
            url : '/api/push/' + idPush
        });
    };

    this.deletePush = function(push) {
        return $http({
            method: 'DELETE',
            url : '/api/push/' + push._id
        });
    };

    this.getPushes = function() {
        return $http({
            method: 'GET',
            url : '/api/push/'
        });
    };

    this.enviarPush = function(push) {
        return $http({
            method: 'POST',
            url: '/api/push/' + push._id
        });
    }

}]);