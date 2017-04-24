var ciudadesApp = angular.module('tripsApp.ciudades');

ciudadesApp.service('CiudadService', [ 'IdiomaService', 'ServerService', function(IdiomaService, ServerService) {
    this.createNewCiudad = function() {
        var ciudad = {};

        ciudad.descripcion = {};

        ciudad.idiomas = IdiomaService.getIdiomas();

        ciudad.idiomasCargados = [];
        ciudad.idiomasNoCargados = [];

        for (var i = 0; i < ciudad.idiomas.length; i++) {
            ciudad.idiomas[i].statusCargado = false;
            ciudad.idiomas[i].statusModificado = false;
            ciudad.descripcion[ciudad.idiomas[i].code] = "";
            ciudad.idiomasNoCargados.push(ciudad.idiomas[i]);
        }

        return ciudad;
    }

    this.cargarInformacionAdicional = function(ciudad) {
        ciudad.idiomasCargados = [];
        ciudad.idiomasNoCargados = [];

        ciudad.idiomas = IdiomaService.getIdiomas();

        for (var i = 0; i < ciudad.idiomas.length; i++) {
            var idioma = ciudad.idiomas[i];
            idioma.statusModificado = false;

            if (ciudad.descripcion[idioma.code] != "") {
                idioma.statusCargado = true;
                ciudad.idiomasCargados.push(idioma);
            } else {
                idioma.statusCargado = false;
                ciudad.idiomasNoCargados.push(idioma);
            }
        }

        return ciudad;
    };
}]);