var resenias = angular.module('tripsApp.resenias');

resenias.service('HelperResenias', [ '$http', '$q', function($http, $q) {
    this.getResultados = function(filtros, callback) {
        var data = {};
        var self = this;
        for (var i = 0; i < filtros.length; i++) {
            var filtro = filtros[i];
            data[filtro.filtro.filtroName] = filtro.contenido;
        }

        var promiseResultados = this._getHTTPRequest(data);
        promiseResultados.then(function success(res) {
            var promises = [];
            for (var i = 0; i < res.data.length; i++) {
                var resenia = res.data[i];
                var promise = self._getNombreYApellidoUsuario(resenia.id_usuario).then(function(res) {
                    resenia.usuario = res.data;
                });

                promises.push(promise);

                return $q
                    .all(promises)
                    .then(function success(values) {
                        callback(res.data, null);
                    }, function error() {
                        callback(res.data, null);
                    });
            }
            callback(res.data, null);
        }, function error(res) {
            callback([], {msg:"No se pudo traer las reseñas del servidor."});
        });
    };

    this._getHTTPRequest = function(data) {
        return $http({
            method: 'GET',
            url : '/api/resenia/buscar',
            data: data
        });
    };

    this._getNombreYApellidoUsuario = function(idUsuario) {
        return $http({
            method: 'GET',
            url : '/api/usuario/' + idUsuario,
            data: {
                id_usuario: idUsuario
            }
        });
    };

    this.bloquearUsuario = function(usuario) {
        usuario.bloqueado = true;
        return $http({
            method: 'PUT',
            url : '/api/usuario/',
            data: usuario
        });
    };

    this.borrarResenia = function(resenia) {
        return $http({
            method: 'DELETE',
            url : '/api/resenia/' + resenia._id,
            data: {
                id_resenia : resenia._id
            }
        });
    };
}]);