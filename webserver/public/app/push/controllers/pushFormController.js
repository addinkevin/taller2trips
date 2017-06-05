var push = angular.module('tripsApp.push');

push.controller('pushFormController',
    [ '$scope', '$http', '$location', '$routeParams', 'IdiomaService', 'PushService',
    function($scope, $http$, $location, $routeParams, IdiomaService, PushService) {

        var editForm = $routeParams.id;
        $scope.push = {};
        $scope.push.nombre = "";
        $scope.push.link = "";
        $scope.push.descripcion = PushService.getDescriptionObject();
        $scope.idiomas = IdiomaService.getIdiomas();
        $scope.idiomaSelected = $scope.idiomas[0];
        $scope.ciudades = [];

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
            if ($scope.push.nombre.length == 0) {
                setFormularioErrorMsg("Debe ingresar un nombre para la notificación push");
                return false;
            }
            return true;
        }

        function checkLink() {
            if ($scope.push.link.length == 0) {
                setFormularioErrorMsg("Debe ingresar un link para la notificación push");
                return false;
            }
            return true;
        }

        function checkDescripcion() {
            var alMenosUnIdioma = false;
            for (var i = 0; i < $scope.idiomas.length; i++) {
                var idioma = $scope.idiomas[i];
                if ($scope.push.descripcion[idioma.code] != "") {
                    alMenosUnIdioma = true;
                    break;
                }
            }

            if (!alMenosUnIdioma) {
                setFormularioErrorMsg("Debe ingresar la descripción del recorrido en al menos un idioma");
            }

            return alMenosUnIdioma;
        }

        function checkImagen() {
            if (!$scope.push.imagen) {
                setFormularioErrorMsg("Debe subir una imagen para la notificación push.");
                return false;
            }

            return true;
        }

        function checkFecha() {
            if (!$scope.push.fechahora) {
                setFormularioErrorMsg("Debe especificar una fecha para la publicación de la notificación push");
                return false;
            }

            if ($scope.push.fechahora < new Date()) {
                setFormularioErrorMsg("Debe especificar una fecha posterior al dia de hoy");
                return false;
            }

            return true;
        }

        function checkCiudad() {
            if (!$scope.push.ciudad || $scope.push.ciudad._id) {
                setFormularioErrorMsg("Se debe ingresar la ciudad del push. Primero debe crear una.");
                return false;
            }
            return true;
        }


        function estaFormularioOk() {
            return  !(!checkNombre() || !checkLink() || !checkDescripcion() || !checkCiudad() || !checkFecha() || !checkImagen());
        }

        function submitAddPush() {
            if (!estaFormularioOk()) return;
            createFecha();
            createHora();
            PushService.addPush($scope.push).then(function success() {
                $location.url('/push/');
            }, function error() {
                setErrorMsg("No se pudo agregar el push");
            });
        }

        function submitEditPush() {
            if (!estaFormularioOk()) return;
            createFecha();
            createHora();
            PushService.editPush($scope.push).then(function success() {
                $location.url('/push/');
            }, function error() {
                setErrorMsg("No se pudo guardar la edición del push.");
            });
        }

        function createFecha() {
            var dia = parseInt($scope.push.fechahora.getDate());
            var mes = parseInt($scope.push.fechahora.getMonth()) + 1;
            var anio = parseInt($scope.push.fechahora.getFullYear());

            if (dia < 10) {
                dia = '0' + dia;
            }
            if (mes < 10) {
                mes = '0' + mes;
            }

            $scope.push.fecha = dia + "/" + mes + "/" + anio;
            console.log($scope.push.fecha);
        }

        function createHora() {
            var hora = parseInt($scope.push.fechahora.getHours());
            var minutos = parseInt($scope.push.fechahora.getMinutes());

            if (hora < 10) {
                hora = '0' + hora;
            }

            if (minutos < 10) {
                minutos = '0' + minutos;
            }

            $scope.push.hora = hora + ":" + minutos;
            console.log($scope.push.hora);
        }

        $scope.submitPush = function() {
            if (editForm) {
                submitEditPush();
            } else {
                submitAddPush();
            }
        };

        var getCiudades = function() {
            return PushService.getCiudades().then(function success(res) {
                $scope.ciudades = res.data;
            }, function error(res) {
                setErrorMsg("Error inesperado al cargar el listado de ciudades.");
                throw Error("No se pudo cargar el listado de ciudades");
            });
        };


        function initAdd() {
            $scope.title = "Agregar push";
            $scope.submit = "Agregar push";

            getCiudades().then(function (){
                if ($scope.ciudades.length > 0) {
                    $scope.push.ciudad = $scope.ciudades[0];
                }
            });
        }

        function createFechaHora(push) {
            var fechaSplit = push.fecha.split("/");
            var horaSplit = push.hora.split(":");
            console.log(fechaSplit);
            console.log(horaSplit);

            var year = parseInt(fechaSplit[2]);
            var month = parseInt(fechaSplit[1]) - 1;
            var day = parseInt(fechaSplit[0]);

            var hours = parseInt(horaSplit[0]);
            var minutes = parseInt(horaSplit[1]);

            push.fechahora = new Date(year, month, day, hours, minutes, 0, 0);
        }

        function getPush() {
            return PushService.getPush(editForm).then(function success(res) {
                $scope.push = res.data;
                createFechaHora($scope.push);
                $scope.push.imagen = { imgSrc: '/api/push/'+ $scope.push._id + '/imagen'};
                $scope.push.ciudad = $scope.ciudades.find(
                    function(element) {
                        return element._id == res.data.id_ciudad._id;
                    }
                );
            }, function error() {
                setErrorMsg("Error inesperado al cargar el push para su edición.");
                throw Error("No se pudo cargar el push");
            });
        }

        function initEdit() {
            $scope.title = "Editar push";
            $scope.submit = "Guardar push";

            getCiudades().then(function() {
               return getPush();
            });
        }

        function init() {
            if (editForm) {
                initEdit();
            } else {
                initAdd();
            }
        }

        init();

        $scope.uploadImageClick = function(event) {
            if (!event.target.files[0]) return;
            $scope.push.imagen = {
                imgSrc: window.URL.createObjectURL(event.target.files[0]),
                imgFile: event.target.files[0]
            };

            $scope.$digest();
        };

    }
]);