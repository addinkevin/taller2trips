var puntos = angular.module('tripsApp.puntos');

puntos.service('PuntosService', ['$http', 'IdiomaService', function ($http, IdiomaService) {
    this.createNewPunto = function() {
        var punto = {};
        punto.descripcion = {'es': '', 'en': '', 'pt': ''};
        punto.imagenes = [];
        punto.idiomas_audio = [];
        punto.audios = [];
        return punto;
    };
}]);