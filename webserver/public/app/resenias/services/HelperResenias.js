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
            var resultados = res.data;
            console.log("Resultados:", resultados);
            for (var i = 0; i < resultados.length; i++) {
                var promise = self._getNombreYApellidoUsuario(resultados[i]);

                promises.push(promise);
            }
            return $q
                .all(promises)
                .then(function success(values) {
                    callback(res.data, null);
                }, function error() {
                    callback(res.data, null);
                });

        }, function error(res) {
            callback([], {msg:"No se pudo traer las reseñas del servidor."});
        });
    };

    this._getHTTPRequest = function(data) {
        return $http({
            method: 'GET',
            url : '/api/resenia/buscar',
            params: data
        });
    };

    this._getNombreYApellidoUsuario = function(resenia) {
        var idUsuario = resenia.id_usuario;
        return $http({
            method: 'GET',
            url : '/api/usuario/' + idUsuario,
            data: {
                id_usuario: idUsuario
            }
        })
           .then(function(res) {
               resenia.usuario = res.data;
        });
    };

    this.setEstadoBloquearUsuario = function(usuario, newStatus) {
        usuario.bloqueado = newStatus;
        return $http({
            method: 'PUT',
            url : '/api/usuario/',
            data: usuario
        });
    };

    this.actualizarElEstadoDeBloqueoRestoDeResenias = function(resultados, usuario) {
        for (var i = 0; i < resultados.length; i++) {
            var usuarioResenia = resultados.usuario;
            if (usuarioResenia && usuarioResenia.id_usuario == usuario.id_usuario) {
                usuarioResenia.bloqueado = usuario.bloqueado;
            }
        }
    };

    this.desbloquearUsuario = function(usuario) {
        return this.setEstadoBloquearUsuario(usuario, false);
    };

    this.bloquearUsuario = function(usuario) {
        return this.setEstadoBloquearUsuario(usuario, true);
    };

    this.borrarResenia = function(resenia) {
        return $http({
            method: 'DELETE',
            url : '/api/resenia/' + resenia._id,
            params: {
                id_resenia : resenia._id
            }
        });
    };


}]);