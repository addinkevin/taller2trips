var ciudadesApp = angular.module('tripsApp.ciudades');

ciudadesApp.controller('ciudadesController', [ '$scope', function ciudadesController( $scope ) {
}]);

ciudadesApp.controller('ciudadesAddController',
    [ '$scope', '$location', '$http', 'ServerService', 'CiudadService',
        function ($scope, $location, $http, ServerService, CiudadService) {
            $scope.ciudad = CiudadService.createNewCiudad();

            $scope.idiomaFormulario = $scope.ciudad.idiomas[0];

            $scope.updateDescripcion = function() {
                $scope.idiomaFormulario.statusModificado = ($scope.ciudad.descripcion[$scope.idiomaFormulario.code].length > 0);
            };

            $scope.alert = {
                class : 'hide',
                msg: ''
            };

            $scope.getStatusIdioma = function(idioma) {
                if ( idioma.statusCargado ) {
                    return "Idiomas cargados";
                }

                return "Idiomas no cargados";
            };

            $scope.getIndicativo = function(idioma) {
                if (idioma.statusModificado) {
                    return " *";
                } else {
                    return "";
                }
            };

            $scope.updateImageClick = function(event) {
                $scope.ciudad.imgFile = event.target.files[0];
                if (event.target.files && event.target.files[0]) {
                    var reader = new FileReader();

                    reader.onload = function (e) {
                        $scope.ciudad.imgSrc = e.target.result;
                        $scope.$digest();

                    };

                    reader.readAsDataURL(event.target.files[0]);
                } else {
                    $scope.ciudad.imgSrc = "";
                    $scope.$digest();
                }

            };

            $scope.validateCiudad = function(ciudad) {
                var validateList = [
                    {
                        parametro : 'nombre',
                        msg: "Debes especificar el nombre de la ciudad!"
                    },
                    {
                        parametro: 'pais',
                        msg: "Debes especificar el país de la ciudad"

                    },
                    {
                        parametro: 'descripcion',
                        msg: "Debes especificar la descripción de la ciudad"
                    },
                    {
                        parametro: 'imgFile',
                        msg: "Debes subir una imagen!"
                    }
                ];

                for (var i = 0; i < validateList.length; i++) {
                    var parametro = validateList[i].parametro;
                    var msg = validateList[i].msg;
                    if (!ciudad[parametro]) {
                        $scope.alert.msg = msg;
                        return false;
                    }
                }
                return true;
            };

            $scope.submitAddCiudad = function() {
                if (!$scope.validateCiudad($scope.ciudad)) {
                    $scope.alert.class = '';
                    return;
                }
                ServerService.addCiudad($scope.ciudad, function (data, error) {
                    if (error) {
                        console.log(error.msg);
                        $location.url('/ciudades/');
                    } else {
                        ServerService.updateCiudadImage($scope.ciudad, function (data, error) {
                            if (error) {
                                console.log(error.msg);
                                $location.url('/ciudades/');
                            } else {
                                $location.url('/ciudades/');
                            }
                        });
                    }
                });

            };
        }
    ]
);

ciudadesApp.controller('ciudadesEditController',
    [ '$scope', '$location', '$http', '$routeParams', 'ServerService',
        function ($scope, $location, $http, $routeParams, ServerService) {
            $scope.ciudad = {};

            $scope.alert = { class: 'hide', msg: '' };

            $scope.getCiudad = function(idCiudad) {
                ServerService.getCiudad(idCiudad, function(data, error) {
                    if (error) {
                        console.log(error.msg);
                    } else {
                        $scope.ciudad = data;
                        $scope.ciudad.imgSrc = "/api/ciudad/"+idCiudad+"/imagen";
                    }
                });
            };

            $scope.updateImageClick = function(event) {
                $scope.ciudad.imgFile = event.target.files[0];
                if (event.target.files && event.target.files[0]) {
                    var reader = new FileReader();

                    reader.onload = function (e) {
                        $scope.ciudad.imgSrc = e.target.result;
                        $scope.$digest();

                    };

                    reader.readAsDataURL(event.target.files[0]);
                } else {
                    $scope.ciudad.imgSrc = "/api/ciudad/"+$scope.ciudad._id+"/imagen";
                    $scope.$digest();
                }
            };

            $scope.validateCiudad = function(ciudad) {
                var validateList = [
                    {
                        parametro : 'nombre',
                        msg: "Debes especificar el nombre de la ciudad!"
                    },
                    {
                        parametro: 'pais',
                        msg: "Debes especificar el país de la ciudad"

                    },
                    {
                        parametro: 'descripcion',
                        msg: "Debes especificar la descripción de la ciudad"
                    }
                ];

                for (var i = 0; i < validateList.length; i++) {
                    var parametro = validateList[i].parametro;
                    var msg = validateList[i].msg;
                    if (!ciudad[parametro]) {
                        $scope.alert.msg = msg;
                        return false;
                    }
                }
                return true;
            };

            $scope.submitEditCiudad = function() {
                var ciudadObject = $scope.ciudad;

                if (!$scope.validateCiudad(ciudadObject)) {
                    $scope.alert.class = '';
                    return;
                }

                ServerService.updateCiudadInfo(ciudadObject, function (data, error) {
                    if (error) {
                        console.log(error);
                        $location.url('/ciudades/');
                    } else {
                        if (!ciudadObject.imgFile) {
                            $location.url('/ciudades/');
                        }

                        ServerService.updateCiudadImage(ciudadObject, function (data, error) {
                            if (error) {
                                console.log(error);
                            }
                            $location.url('/ciudades/');
                        });
                    }
                });
            };

            $scope.getCiudad($routeParams.id);
        }
    ]
);

ciudadesApp.controller('ciudadesListadoController',
    [ '$scope' , '$http', '$location', 'ServerService',
    function($scope, $http, $location, ServerService) {

        $scope.ciudades = [];

        $scope.setErrorMsg = function(msgError) {
            var msg = "<div class='alert alert-danger alert-fixed text-center'>" +
                    "<button type='button' class='close' data-dismiss='alert'>&times;</button>" +
                    msgError +
                    "</div>";
            $("#errorContainer").html(msg);
        };

        $scope.deleteCiudad = function(ciudadId) {
            ServerService.deleteCiudad(ciudadId, function(data, err) {
                if (err) {
                    $scope.setErrorMsg(err.msg);
                } else {
                    $location.url('/ciudades/');
                }
            });
        };

        $scope.processList = function(data) {
            for (var i = 0; i < data.length; i++) {
                var ciudad = data[i];
                var idiomasCargados = [];
                Object.keys(ciudad.descripcion).forEach(function(key,index) {
                    if (ciudad.descripcion[key] != "") {
                        idiomasCargados.push(key);
                    }
                });

                ciudad.idiomasCargados = idiomasCargados;
            }
        };

        $scope.getCiudades = function() {
            ServerService.getCiudades(function(data, err) {
                if (err) {
                    console.log(err.msg);
                } else {
                    $scope.processList(data);
                    $scope.ciudades = data;
                }
            });
        };

        $scope.getCiudades();
}]);