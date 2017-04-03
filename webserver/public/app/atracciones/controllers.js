var atraccionesApp = angular.module('tripsApp.atracciones');

atraccionesApp.controller('atraccionesListadoController',
    [ '$scope', '$http', '$location', 'ServerService',
        function($scope, $http, $location, ServerService) {
            $scope.deleteAtraccion = function(atraccionId) {
                ServerService.deleteAtraccion(atraccionId, function(data, err) {
                    $location.url('/atracciones/');
                });
            };

            $scope.atracciones = [];

            $scope.getNombreCiudad = function(idCiudad) {
                return $scope.mapCiudades[idCiudad].nombre;
            }

            $scope.getAtracciones = function() {
                $http.get('/api/ciudad/')
                    .then(function success(res) {
                        console.log("GET OK /api/ciudad");
                        $scope.ciudades = res.data;
                        $scope.mapCiudades = {}
                        for (var i = 0; i < $scope.ciudades.length; i++) {
                            $scope.mapCiudades[$scope.ciudades[i]._id] = $scope.ciudades[i];
                        }
                        $http.get('/api/atraccion')
                            .then(function sucess(res) {
                                console.log("GET OK /api/atraccion");
                                $scope.atracciones = res.data;
                            }, function error(res) {

                            });
                    }, function error(res) {

                    });
            };

            $scope.getAtracciones();
        }]);

function Atraccion() {
    this.lat = -35;
    this.lng = -58;
    this.clasificaciones = [
        "Atracciones y lugares de interés",
        "Compras",
        "Museos",
        "Niños",
        "Conciertos y espectáculos",
        "Naturaleza y parques",
        "Vida nocturna",
        "Actividades al aire libre",
        "Clases y talleres",
        "Diversion y juegos",
        "Comida y bebida",
        "Transporte",
        "Zoologicas y acuarios",
        "Casinos y juegos de azar",
        "Spas"
    ];

    this.idiomas = ['es','en','fr','kr','jp'];
    this.monedas = [ 'u$s', '$', 'R$', '€' ];
    this.monedaCosto = "u$s";
    this.montoCosto = 0;
    this.duracion = 0;
    this.clasificacionSelected = this.clasificaciones[0];
    this.horaApertura = "";
    this.horaCierre = "";
    this.ciudadSelected = "";
    this.idiomaAudio = "";
    this.imagenes = [];
    this.audios = [];
    this.videos = [];
    this.planos = [];
    this.mapInfo = {};

}

atraccionesApp.controller('atraccionesAddEditController',
    [ '$scope', '$location', '$http','$routeParams', 'ServerService',
        function ($scope, $location, $http, $routeParams, ServerService) {
            $scope.editForm = $routeParams.id;
            $scope.atraccion = new Atraccion();

            $scope.initAdd = function() {
                $scope.title = "Agregar atracción";
                $scope.submitButton = "Agregar";
            };

            $scope.initEdit = function() {
                $scope.title = "Editar atracción";
                $scope.submitButton = "Guardar";
                $scope.loadAtraccion();
            };

            $scope.submitAddAtraccion = function() {
                console.log("Add submit atraccion");
                $scope.addAtraccion($scope.atraccion);
            };

            $scope.copyProperties = function(objDest, objSrc, properties) {
                for (var key in properties) {
                    objDest[key] = objSrc[properties[key]];
                }
            };

            $scope.loadAtraccion = function() {
                ServerService.getAtraccion($routeParams.id, function (data, err) {
                    if (err) {
                        console.log(err.msg);
                    } else {
                        $scope.copyProperties(
                            $scope.atraccion,
                            data,
                            {
                                '_id': '_id',
                                'nombre': 'nombre',
                                'descripcion': 'descripcion',
                                'montoCosto': 'costo_monto',
                                'monedaCosto': 'costo_moneda',
                                'duracion': 'duracion',
                                'clasificacionSelected': 'clasificacion',
                                'horaApertura': 'hora_apertura',
                                'horaCierre': 'hora_cierre',
                                'lat': 'latitud',
                                'lng': 'longitud'
                            }
                        );

                        $scope.atraccion.ciudadSelected = $scope.ciudades.find(
                            function(element) {
                                return element._id == data.id_ciudad;
                            }
                        );
                    }
                });
            };

            $scope.editAtraccion = function(atraccion) {
                ServerService.editAtraccion(atraccion, function(data, err) {
                    if (err) {
                        console.log(err.msg);
                    } else {
                        $location.url('/atracciones/');
                    }
                });
            };

            $scope.submitEditAtraccion = function() {
                console.log("Edit submit atraccion");
                $scope.editAtraccion($scope.atraccion);
            };

            $scope.submitAtraccion = function() {
                if ($scope.editForm) {
                    $scope.submitEditAtraccion();
                } else {
                    $scope.submitAddAtraccion();
                }
            };

            $scope.createMap = function() {
                var map = new google.maps.Map(document.getElementById('map'), {
                    zoom: 4,
                    center: {lat: $scope.atraccion.lat, lng: $scope.atraccion.lng}
                });

                var marker = new google.maps.Marker({
                    position: {lat: $scope.atraccion.lat, lng: $scope.atraccion.lng},
                    map: map,
                    title: 'Click atraccion'
                });

                map.addListener('click', function(event) {
                    marker.setPosition(event.latLng);
                    $scope.atraccion.lat = event.latLng.lat();
                    $scope.atraccion.lng = event.latLng.lng();
                    $scope.$digest();
                });
                $scope.atraccion.mapInfo.map = map;
                $scope.atraccion.mapInfo.marker = marker;

            };

            $scope.uploadImageClick = function(event) {
                $scope.imagenes.push({imgSrc: window.URL.createObjectURL(event.target.files[0])});
                console.log($scope.imagenes);
                $scope.$digest();
            };

            $scope.uploadVideoClick = function(event) {
                $scope.videos.push({vidSrc: window.URL.createObjectURL(event.target.files[0])});
                $scope.$digest();
            };

            $scope.uploadAudioClick = function(event) {
                $scope.audios.push({idiomaAudio: $scope.idiomaAudio, audSrc: window.URL.createObjectURL(event.target.files[0])});
                console.log($scope.audios);
                $scope.$digest();
            };

            $scope.uploadPlanoClick = function(event) {
                $scope.planos.push({imgSrc: window.URL.createObjectURL(event.target.files[0])});
                $scope.$digest();
            };


            $scope.loadCiudades = function() {
                ServerService.getCiudades(function(data, err) {
                    if (err) {
                        console.log(err.msg);
                    } else {
                        $scope.ciudades = data;
                        $scope.atraccion.ciudadSelected = $scope.ciudades[0];
                    }
                });
            };


            $scope.addAtraccion = function(atraccion) {
                ServerService.addAtraccion(atraccion, function(data, err) {
                    if (err) {
                        console.log(err.msg);
                    } else {
                        $location.url('/atracciones');
                    }
                });
            };

            $scope.inicializar = function() {
                console.log($scope.editForm);
                $scope.loadCiudades();
                if ($scope.editForm) {
                    $scope.initEdit();
                } else {
                    $scope.initAdd();
                }
                $scope.createMap();

            };

            $scope.inicializar();
        }
    ]
);