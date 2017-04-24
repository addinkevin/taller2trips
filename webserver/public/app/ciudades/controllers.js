var ciudadesApp = angular.module('tripsApp.ciudades');

ciudadesApp.controller('ciudadesController', [ '$scope', function ciudadesController( $scope ) {
}]);

ciudadesApp.controller('ciudadesAddController',
    [ '$scope', '$location', '$http', 'ServerService', 'CiudadService',
        function ($scope, $location, $http, ServerService, CiudadService) {
            $scope.ciudad = CiudadService.createNewCiudad();

            $scope.idiomaFormulario = $scope.ciudad.idiomas[0];

            $scope.updateDescripcion = function() {
                $scope.idiomaFormulario.statusModificado = true;
            };

            $scope.alert = {
                class : 'hide',
                msg: ''
            };

            $scope.setErrorMsg = function(msgError) {
                var msg = "<div class='alert alert-danger alert-fixed text-center'>" +
                    "<button type='button' class='close' data-dismiss='alert'>&times;</button>" +
                    msgError +
                    "</div>";
                $("#errorContainer").html(msg);
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

                var alMenosUnIdioma = false;
                for (var i = 0; i < ciudad.idiomas.length; i++) {
                    var idioma = ciudad.idiomas[i];
                    if (ciudad.descripcion[idioma.code] != "") {
                        alMenosUnIdioma = true;
                        break;
                    }
                }

                if (!alMenosUnIdioma) {
                    $scope.alert.msg = "Debes ingresar la descripción para al menos un idioma.";
                    return false;
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
                        $scope.setErrorMsg(error.msg);
                    } else {
                        ServerService.updateCiudadImage($scope.ciudad, function (data, error) {
                            if (error) {
                                // TODO Chequear error aqui.
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
    [ '$scope', '$location', '$http', '$routeParams', 'ServerService', 'CiudadService',
        function ($scope, $location, $http, $routeParams, ServerService, CiudadService) {
            $scope.ciudad = {};

            $scope.alert = { class: 'hide', msg: '' };

            $scope.updateDescripcion = function() {
                $scope.idiomaFormulario.statusModificado = true;
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

            $scope.getCiudad = function(idCiudad) {
                ServerService.getCiudad(idCiudad, function(data, error) {
                    if (error) {
                        console.log(error.msg);
                    } else {
                        $scope.ciudad = CiudadService.cargarInformacionAdicional(data);
                        $scope.idiomaFormulario = $scope.ciudad.idiomasCargados[0];
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

            $scope.setErrorMsg = function(msgError) {
                var msg = "<div class='alert alert-danger alert-fixed text-center'>" +
                    "<button type='button' class='close' data-dismiss='alert'>&times;</button>" +
                    msgError +
                    "</div>";
                $("#errorContainer").html(msg);
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

                var alMenosUnIdioma = false;
                for (var i = 0; i < ciudad.idiomas.length; i++) {
                    var idioma = ciudad.idiomas[i];
                    if (ciudad.descripcion[idioma.code] != "") {
                        alMenosUnIdioma = true;
                        break;
                    }
                }

                if (!alMenosUnIdioma) {
                    $scope.alert.msg = "Debes ingresar la descripción para al menos un idioma.";
                    return false;
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
                        $scope.setErrorMsg(error.msg);
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
    [ '$scope' , '$http', '$location', 'ServerService', 'IdiomaService',
    function($scope, $http, $location, ServerService, IdiomaService) {

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
            var idiomas = IdiomaService.getIdiomas();
            for (var i = 0; i < data.length; i++) {
                var ciudad = data[i];

                ciudad.idiomasCargados = [];
                ciudad.idiomasNoCargados = [];

                for (var j = 0; j < idiomas.length; j++) {
                    var idioma = idiomas[j];
                    if (ciudad.descripcion[idioma.code] != "") {
                        ciudad.idiomasCargados.push(idioma);
                    } else {
                        ciudad.idiomasNoCargados.push(idioma);
                    }
                }
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