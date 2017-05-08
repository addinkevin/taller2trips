var recorridos = angular.module('tripsApp.recorridos');

recorridos.controller('recorridoListadoController', [ '$scope', '$http', '$location', 'RecorridosService',
    function($scope, $http, $location, RecorridosService) {
        $scope.recorridos = [];

        $scope.deleteRecorrido = function (recorrido) {
            RecorridosService.deleteRecorrido(recorrido).then(function success(res) {
                $location.url('/recorridos');
            }, function error(res) {

            });
        };

        $scope.getNombreCiudad = function (recorrido) {
            return recorrido.id_ciudad.nombre;
        };

        $scope.getListadoDeAtracciones = function (recorrido) {
            return RecorridosService.getListadoDeAtracciones(recorrido);
        };

        $scope.getLinksImagenes = function(recorrido) {
            console.log(recorrido);
            var links = [];
            for (var i = 0; i < recorrido.ids_atracciones.length; i++) {
                var atraccion = recorrido.ids_atracciones[i];
                links.push({src: $scope.getImgAtraccionUrl(atraccion), alt: "", caption: atraccion.nombre});
            }
            return links;
        };

        $scope.getSomeImage = function(recorrido) {
            return $scope.getImgAtraccionUrl(recorrido.ids_atracciones[0]);
        };

        $scope.getImgAtraccionUrl = function(atraccion) {
            return '/api/atraccion/'+atraccion._id+'/imagen?filename='+atraccion.imagenes[0];
        };

        var loadRecorridos = function() {
            RecorridosService.getRecorridos().then(function success(res) {
                $scope.recorridos = res.data;
            }, function error(res) {

            });
        };

        $scope.init = function() {
            loadRecorridos();
        };

        $scope.init();



}]);