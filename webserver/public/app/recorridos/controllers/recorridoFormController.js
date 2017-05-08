var recorridos = angular.module('tripsApp.recorridos');

recorridos.controller('recorridoFormController', [ '$scope', '$http', '$routeParams', 'RecorridosService', 'IdiomaService',
    function($scope, $http, $routeParams, RecorridosService, IdiomaService) {
        $scope.editForm = $routeParams.id;
        $scope.recorrido = {};
        $scope.recorrido.nombre = "";
        $scope.idiomas = IdiomaService.getIdiomas();
        $scope.idiomaSelected = $scope.idiomas[0];
        $scope.recorrido.descripcion = RecorridosService.getDescriptionObject();
        $scope.ciudades = [];
        $scope.atracciones = [];
        $scope.recorrido.listadoAtracciones = [];


        $scope.submitRecorrido = function() {

        };

        $scope.agregarAtraccion = function() {
            $scope.recorrido.listadoAtracciones.push($scope.atraccionSelected);
        };

        var getCiudades = function() {
            RecorridosService.getCiudades().then(function success(res) {
                $scope.ciudades = res.data;
                if ($scope.ciudades.length > 0) {
                    $scope.recorrido.ciudad = $scope.ciudades[0];
                    getAtracciones();
                }
            }, function error(res) {
            });
        };

        var getAtracciones = function() {
            RecorridosService.getAtracciones($scope.recorrido.ciudad).then(function success(res) {
                $scope.atracciones = res.data;
                console.log(res.data);
            }, function error(res) {

            });
        };

        $scope.initAdd = function() {
            $scope.title = "Agregar recorrido";
            $scope.disabled = false;
            getCiudades();
        };

        $scope.initEdit = function() {
            $scope.title = "Editar recorrido";
            $scope.disabled = true;
        };


        $scope.cambioDeCiudad = function() {
            console.log("Cambio de ciudad", $scope.recorrido.ciudad);
            getAtracciones();
        };

        $scope.init = function() {
            console.log("Recorrido Form Controller init...");
            if ($scope.editForm) {
                $scope.initEdit();
            } else {
                $scope.initAdd();
            }
        };

        $scope.init();

}]);