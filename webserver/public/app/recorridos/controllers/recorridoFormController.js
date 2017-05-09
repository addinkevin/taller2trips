var recorridos = angular.module('tripsApp.recorridos');

recorridos.controller('recorridoFormController', [ '$scope', '$http', '$routeParams', '$location', 'RecorridosService', 'IdiomaService',
    function($scope, $http, $routeParams, $location, RecorridosService, IdiomaService) {
        $scope.editForm = $routeParams.id;
        $scope.recorrido = {};
        $scope.recorrido.nombre = "";
        $scope.idiomas = IdiomaService.getIdiomas();
        $scope.idiomaSelected = $scope.idiomas[0];
        $scope.recorrido.descripcion = RecorridosService.getDescriptionObject();
        $scope.ciudades = [];
        $scope.atracciones = [];
        $scope.recorrido.listadoAtracciones = [];


        $scope.submitAddRecorrido = function () {
            RecorridosService.agregarRecorrido($scope.recorrido).then(function success() {
                $location.url('/recorridos/');
            }, function error() {
                $location.url('/recorridos/');
            });
        };

        $scope.submitEditRecorrido = function() {
            RecorridosService.editarRecorrido($scope.recorrido).then(function success() {
                $location.url('/recorridos/');
            }, function error() {
                $location.url('/recorridos/');
            });
        };

        $scope.submitRecorrido = function() {
            if ($scope.editForm) {
                $scope.submitEditRecorrido();
            } else {
                $scope.submitAddRecorrido();
            }
        };

        var move = function (origin, destination) {
            var temp = $scope.recorrido.listadoAtracciones[destination];
            $scope.recorrido.listadoAtracciones[destination] = $scope.recorrido.listadoAtracciones[origin];
            $scope.recorrido.listadoAtracciones[origin] = temp;
        };

        $scope.listadoMoveUp = function(id, atraccion) {
            if (id == 0) return;
            move(id, id-1);
        };

        $scope.listadoMoveDown = function(id, atraccion) {
            if (id == $scope.recorrido.listadoAtracciones.length - 1) return;
            move(id, id+1);
        };

        $scope.listadoRemove = function(id, atraccion) {
            $scope.recorrido.listadoAtracciones.splice(id,1);
        };

        function estaAtracccionRepetidaEnListado(atraccion) {
            var result = $scope.recorrido.listadoAtracciones.find(function(element) {
                return element._id === atraccion._id;
            });

            return result;
        }

        $scope.agregarAtraccion = function() {
            if (!$scope.atraccionSelected) return;
            if (estaAtracccionRepetidaEnListado($scope.atraccionSelected)) return;
            $scope.recorrido.listadoAtracciones.push($scope.atraccionSelected);
        };

        var getCiudades = function() {
            return RecorridosService.getCiudades().then(function success(res) {
                $scope.ciudades = res.data;
                if ($scope.ciudades.length > 0) {
                    $scope.recorrido.ciudad = $scope.ciudades[0];
                }
            }, function error(res) {
            });
        };

        var getAtracciones = function() {
            return RecorridosService.getAtracciones($scope.recorrido.ciudad).then(function success(res) {
                $scope.atracciones = res.data;
                console.log(res.data);
            }, function error(res) {

            });
        };

        $scope.initAdd = function() {
            $scope.title = "Agregar recorrido";
            $scope.submit = "Agregar recorrido";
            $scope.disabled = false;
            getCiudades().then(function() {
                getAtracciones();
            });
        };

        var getRecorrido = function() {
            return RecorridosService.getRecorrido($scope.editForm).then(function success(res) {
                $scope.recorrido = res.data[0];
            }, function error(res) {

            });
        };

        $scope.initEdit = function() {
            $scope.title = "Editar recorrido";
            $scope.submit = "Guardar edición de recorrido";
            $scope.disabled = true;
            getRecorrido().then(function() {
                console.log($scope.recorrido);
                $scope.ciudades = [$scope.recorrido.id_ciudad];
                $scope.recorrido.ciudad = $scope.ciudades[0];
                getAtracciones();
                $scope.recorrido.listadoAtracciones = $scope.recorrido.ids_atracciones;
            });
        };


        $scope.cambioDeCiudad = function() {
            console.log("Cambio de ciudad", $scope.recorrido.ciudad);
            getAtracciones().then(function() {
                $scope.recorrido.listadoAtracciones = [];
            });
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