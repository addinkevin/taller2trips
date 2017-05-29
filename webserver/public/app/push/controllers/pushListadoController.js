var push = angular.module('tripsApp.push');

push.controller('pushListadoController',
    [ '$scope', '$http', '$location', '$routeParams', 'IdiomaService', 'PushService',
        function($scope, $http$, $location, $routeParams, IdiomaService, PushService) {
    
            function setErrorMsg(msgError) {
                var msg = "<div class='alert alert-danger alert-fixed text-center'>" +
                    "<button type='button' class='close' data-dismiss='alert'>&times;</button>" +
                    msgError +
                    "</div>";
                $("#errorContainer").html(msg);
            }

            function setInfoMsg(msgInfo) {
                var msg = "<div class='alert alert-info alert-fixed text-center'>" +
                    "<button type='button' class='close' data-dismiss='alert'>&times;</button>" +
                    msgInfo +
                    "</div>";

                $("#infoContainer").html(msg);
            }

            $scope.pushes = [];

            $scope.getNombreCiudad = function(push) {
                return push.id_ciudad.nombre;
            };

            $scope.getFechaDePublicacion = function(push) {
                return push.fecha + " " + push.hora;
            };

            $scope.getEstado = function(push) {
                if (push.enviado) {
                    return "Enviado";
                } else {
                    return "Aun no enviado";
                }
            };

            $scope.enviarPush = function(push) {
                PushService.enviarPush(push).then(function success(res) {
                    push.enviado = true;
                    setInfoMsg("Notificación push enviada");
                }, function error() {
                    setErrorMsg("No pudo enviarse la notificación push");
                });
            };

            $scope.deletePush = function(push) {
                PushService.deletePush(push).then(function success(res) {
                    $location.url('/push');
                }, function error(res) {
                    setErrorMsg("No se pudo borrar la push. Intentelo nuevamente.");
                });
            };

            function agregarListadoDeIdiomas(push) {
                push.idiomasCargados = [];
                push.idiomasNoCargados = [];

                var idiomas = IdiomaService.getIdiomas();

                for (var i = 0; i < idiomas.length; i++) {
                    var idioma = idiomas[i];
                    if (push.descripcion[idioma.code] != "") {
                        push.idiomasCargados.push(idioma);
                    } else {
                        push.idiomasNoCargados.push(idioma);
                    }
                }
            }

            function makeIdiomas() {
                for (var i = 0; i < $scope.pushes.length; i++) {
                    var push = $scope.pushes[i];
                    agregarListadoDeIdiomas(push);
                }
            }

            function makeImages() {
                for (var i = 0; i < $scope.pushes.length; i++) {
                    var push = $scope.pushes[i];
                    push.imagen = { imgSrc: '/api/push/'+ push._id + '/imagen'};
                }
            }

            function loadPushes() {
                PushService.getPushes().then(function success(res) {
                    $scope.pushes = res.data;
                    makeIdiomas();
                    makeImages();
                }, function error(res) {
                    setErrorMsg("No se pudo cargar las pushes. Intentelo nuevamente.");
                });
            }

            $scope.init = function() {
                loadPushes();
            };

            $scope.init();
        }
    ]);