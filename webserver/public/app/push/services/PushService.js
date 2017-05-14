var push = angular.module('tripsApp.push');

push.service('PushService',  ['$http', 'IdiomaService', '$timeout', function ($http, IdiomaService, $timeout) {
    this.id = 1;
    this.pushes = [
        {
            _id: 0,
            nombre: 'Nombre del push',
            link: 'Link1',
            imagen: {imgSrc: '/obelisco.png'},
            descripcion: { es: "Descr", en:"", pt: ""},
            id_ciudad: { _id: 'asd', nombre:'Bariloche' },
            fecha: "03/10/2017",
            hora: "23:43"
        }
    ];

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
                imagen: push.imagen.imgSrc.imgFile
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
            push._id = res.data._id;
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
            url : '/api/push/' + idPush
        });
    };

    this.getPushes = function() {
        return $http({
            method: 'GET',
            url : '/api/push/'
        });
    };

}]);