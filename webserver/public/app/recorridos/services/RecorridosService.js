var recorridos = angular.module('tripsApp.recorridos');

recorridos.service('RecorridosService', ['$http', 'IdiomaService', function ($http, IdiomaService) {
    this.getRecorridos = function() {
        return $http({
            method: 'GET',
            url : '/api/recorridoPopulate'
        });
    };

    this.getCiudades = function() {
        return $http({
            method: 'GET',
            url : '/api/ciudad'
        });
    };

    this.getAtracciones = function(ciudad) {
        console.log(ciudad);
        return $http({
            method: 'GET',
            url : '/api/atraccion',
            params: {
                id_ciudad: ciudad._id
            }
        });
    };

    this.deleteRecorrido = function(recorrido) {
        return $http({
            method: 'DELETE',
            url : '/api/recorrido/'+recorrido._id,
        });
    };

    this.getDescriptionObject = function() {
        // TODO HACERLO BIENNNNNNN
        return { es: "", en:"", pt: ""};
    };

    this.getListadoDeAtracciones = function(recorrido) {
        var listado = [];
        for (var i = 0; i < recorrido.ids_atracciones.length; i++) {
            var atraccionRecorrido = recorrido.ids_atracciones[i];
            listado.push(atraccionRecorrido.nombre);
        }
        return listado.join(' , ');
    };


    this.obtenerIdsAtraccciones = function(atracciones) {
        var ids = [];
        for (var i = 0; i < atracciones.length; i++) {
            ids.push(atracciones[i]._id);
        }
        return ids;
    };

    this.agregarRecorrido = function(recorrido) {
        var data = {
            "nombre": recorrido.nombre,
            "descripcion": JSON.stringify(recorrido.descripcion),
            "id_ciudad": recorrido.ciudad._id,
            "ids_atracciones": this.obtenerIdsAtraccciones(recorrido.listadoAtracciones).join()
        };

        return $http({
            method: 'POST',
            url : '/api/recorrido',
            data: data
        });
    };

    this.editarRecorrido = function(recorrido) {
        var data = {
            "_id": recorrido._id,
            "nombre": recorrido.nombre,
            "descripcion": JSON.stringify(recorrido.descripcion),
            "id_ciudad": recorrido.ciudad._id,
            "ids_atracciones": this.obtenerIdsAtraccciones(recorrido.listadoAtracciones).join()
        };

        return $http({
            method: 'PUT',
            url : '/api/recorrido',
            data: data
        });
    };


    this.getRecorrido = function(IdRecorrido) {
        return $http({
            method: 'GET',
            url : '/api/recorrido/'+IdRecorrido
        });
    }

}]);