var ciudadesApp = angular.module('tripsApp.ciudades');

ciudadesApp.controller('ciudadesController', [ '$scope', function ciudadesController( $scope ) {
}]);

ciudadesApp.controller('ciudadesAddController',
    [ '$scope', '$location', '$http', 'ServerService',
        function ($scope, $location, $http, ServerService) {
            $scope.ciudad = {};

            $scope.alert = {
                class : 'hide',
                msg: ''
            };


            $scope.updateImageClick = function(event) {
                $scope.ciudad.imgFile = event.target.files[0];
                if (event.target.files && event.target.files[0]) {
                    var reader = new FileReader();

                    reader.onload = function (e) {
                        $scope.ciudad.imgSrc = e.target.result;
                        $scope.$digest();

                    }

                    reader.readAsDataURL(event.target.files[0]);
                } else {
                    $scope.ciudad.imgSrc = "";
                    $scope.$digest();
                }

            };

            $scope.validateCiudad = function(ciudad) {
                var ok = true;

                if (!ciudad.nombre) {
                    $scope.alert.msg = "Debes especificar el nombre de la ciudad!";
                    return false;
                }

                if (!ciudad.pais) {
                    $scope.alert.msg = "Debes especificar el país de la ciudad";
                    return false;
                }

                if (!ciudad.descripcion) {
                    $scope.alert.msg = "Debes especificar la descripción de la ciudad";
                    return false;
                }

                if (!ciudad.file) {
                    $scope.alert.msg = "Debes subir una imagen!";
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
                }
            };

            $scope.submitEditCiudad = function() {
                var ciudadObject = $scope.ciudad;
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

        $scope.deleteCiudad = function(ciudadId) {
            ServerService.deleteCiudad(ciudadId, function(data, err) {
                $location.url('/ciudades/');
            });
        };

        $scope.getCiudades = function() {
            ServerService.getCiudades(function(data, err) {
                if (err) {
                    console.log(err.msg);
                } else {
                    $scope.ciudades = data;
                }
            });
        };

        $scope.getCiudades();

}]);