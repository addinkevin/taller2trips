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

        function setFormularioErrorMsg(msgError) {
            var msg = "<div class='alert alert-danger text-center'>" +
                "<button type='button' class='close' data-dismiss='alert'>&times;</button>" +
                msgError +
                "</div>";
            $("#error-en-formulario").html(msg);
        }

        function setErrorMsg(msgError) {
            var msg = "<div class='alert alert-danger alert-fixed text-center'>" +
                "<button type='button' class='close' data-dismiss='alert'>&times;</button>" +
                msgError +
                "</div>";
            $("#errorContainer").html(msg);
        }

        function setInfoMsg(msgInfo) {
            var msg = "<div class='alert alert-info alert-fixed text-center'>" +
                msgInfo +
                "</div>";
            $("#infoContainer").html(msg);
        }

        function checkNombre() {
            if ($scope.recorrido.nombre.length == 0) {
                setFormularioErrorMsg("Debe ingresar un nombre para el recorrido");
                return false;
            }
            return true;
        }

        function checkDescripcion() {
            var alMenosUnIdioma = false;
            for (var i = 0; i < $scope.idiomas.length; i++) {
                var idioma = $scope.idiomas[i];
                if ($scope.recorrido.descripcion[idioma.code] != "") {
                    alMenosUnIdioma = true;
                    break;
                }
            }

            if (!alMenosUnIdioma) {
                setFormularioErrorMsg("Debe ingresar la descripción del recorrido en al menos un idioma");
            }

            return alMenosUnIdioma;
        }

        function checkListado() {
            if ($scope.recorrido.listadoAtracciones.length == 0){
                setFormularioErrorMsg("Debe ingresar al menos una atracción para el recorrido.");
                return false;
            }
            return true;
        }

        function checkCiudad() {
            if (!$scope.recorrido.ciudad || !$scope.recorrido.ciudad._id) {
                setFormularioErrorMsg("Se debe ingresar la ciudad del recorrido. Primero debe crear una.");
                return false;
            }
            return true;
        }

        function estaFormularioOk() {
            return  !(!checkNombre() || !checkDescripcion() || !checkCiudad() || !checkListado());
        }

        $scope.submitAddRecorrido = function () {
            if (!estaFormularioOk()) return;
            RecorridosService.agregarRecorrido($scope.recorrido).then(function success(res) {
                $location.url('/recorridos/');
            }, function error(res) {
                if (res.status == 405) {
                    setErrorMsg("Error al agregar recorrido: (Nombre recorrido, ciudad) ya repetido");
                } else {
                    setErrorMsg("Error inesperado al agregar el recorrido.");
                }
            });
        };

        $scope.submitEditRecorrido = function() {
            if (!estaFormularioOk()) return;
            RecorridosService.editarRecorrido($scope.recorrido).then(function success(res) {
                $location.url('/recorridos/');
            }, function error(res) {
                if (res.status == 405) {
                    setErrorMsg("Error al guardar recorrido: (Nombre recorrido, ciudad) ya repetido");
                } else {
                    setErrorMsg("Error inesperado al guardar el recorrido.");
                }
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
                setErrorMsg("Error inesperado al cargar el listado de ciudades.");
                throw Error("No se pudo cargar el listado de ciudades");
            });
        };

        var getAtracciones = function() {
            return RecorridosService.getAtracciones($scope.recorrido.ciudad).then(function success(res) {
                $scope.atracciones = res.data;
                console.log(res.data);
            }, function error(res) {
                setErrorMsg("Error inesperado al cargar las atracciones disponibles.");
                throw Error("No se pudo cargar el listado de atracciones");
            });
        };

        $scope.initAdd = function() {
            $scope.title = "Agregar recorrido";
            $scope.submit = "Agregar recorrido";
            $scope.disabled = false;

            getCiudades().then(function() {
                getAtracciones();
            }, function() {});
        };

        var getRecorrido = function() {
            return RecorridosService.getRecorrido($scope.editForm).then(function success(res) {
                $scope.recorrido = res.data[0];
            }, function error(res) {
                setErrorMsg("Error inesperado al cargar el recorrido para su edición.");
                throw Error("No se pudo cargar el recorrido");
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
            }, function() {
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