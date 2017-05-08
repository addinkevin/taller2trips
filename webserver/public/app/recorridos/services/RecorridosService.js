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
        return $http({
            method: 'GET',
            url : '/api/atraccion',
            params: {
                id_ciudad: ciudad.id_ciudad
            }
        });
    };

    this.deleteRecorrido = function(recorrido) {

    };

    this.getDescriptionObject = function() {
        // TODO HACERLO BIENNNNNNN
        return { es: "", en:"", pt: ""};
    };

    this.getCodeIdioma = function(idioma) {
        // TODO HACERLO BIENNNNNNN
        if (idioma == "Español") {
            return 'es';
        } else if (idioma == "Ingles") {
            return 'en';
        } else {
            return 'pt';
        }
    };

    this.getListadoDeAtracciones = function(recorrido) {
        var listado = [];
        for (var i = 0; i < recorrido.ids_atracciones.length; i++) {
            var atraccionRecorrido = recorrido.ids_atracciones[i];
            listado.push(atraccionRecorrido.nombre);
        }
        return listado.join(' , ');
    }
}]);