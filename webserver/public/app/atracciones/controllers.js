var atraccionesApp = angular.module('tripsApp.atracciones');

atraccionesApp.controller('atraccionesListadoController',
    [ '$scope' , 'AtraccionesService', '$http', '$location', '$timeout',
        function($scope, AtraccionesService, $http, $location, $timeout) {
            $scope.deleteAtraccion = function(atraccionId) {
                AtraccionesService.removeAtraccion($scope.atracciones[atraccionId]);
                $location.url('/atracciones');
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

atraccionesApp.controller('atraccionesAddController',
    [ '$scope', 'AtraccionesService', '$location', '$http',
        function ($scope, AtraccionesService, $location, $http) {
            console.log("AngularJs Controller: atraccionesAddController");

            $scope.atraccion = {lat:-35, lng:-58 };
            $scope.idiomas = ['es','en','fr','kr','jp'];
            $scope.monedas = [ 'u$s', '$', 'R$', '€' ];
            $scope.monedaCosto = "u$s";
            $scope.mapInfo = {};
            $scope.idiomaAudio = "";
            $scope.clasificaciones = [
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
            $scope.clasificacionSelected = $scope.clasificaciones[0];
            $scope.ciudadSelected = "";
            $scope.imagenes = [];
            $scope.audios = [];
            $scope.videos = [];
            $scope.planos = [];

            $scope.updateImageClick = function(event) {
                $scope.ciudad.imgSrc = window.URL.createObjectURL(event.target.files[0]);
                console.log($scope.ciudad.imgSrc);
                $scope.$digest();
            };

            $scope.submitAddAtraccion = function() {
                //console.log("Agregado la ciudad:", $scope.atraccion);
                $scope.addAtraccion($scope.atraccion);
                //$location.url('/atracciones/');
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
                $scope.mapInfo.map = map;
                $scope.mapInfo.marker = marker;

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


            $scope.createMap();

            $scope.loadCiudades = function() {
                $http({
                    method: 'GET',
                    url : '/api/ciudad'
                })
                    .then(function sucess(res) {
                        console.log("GET OK /api/ciudad");
                        $scope.ciudades = res.data;
                        $scope.ciudadSelected = $scope.ciudades[0];

                    }, function error(res) {

                    });
            };
            $scope.loadCiudades();

            $scope.addAtraccion = function(atraccion) {
                var data = {
                    "nombre": atraccion.nombre,
                    "descripcion": atraccion.descripcion,
                    "costo_monto": atraccion.montoCosto,
                    "costo_moneda": $scope.monedaCosto,
                    "hora_apertura": atraccion.horaApertura,
                    "hora_cierre": atraccion.horaCierre,
                    "duracion": atraccion.duracion,
                    "clasificacion": $scope.clasificacionSelected,
                    "id_ciudad": $scope.ciudadSelected._id,
                    "latitud": atraccion.lat,
                    "longitud": atraccion.lng
                };

                $http({
                    method: 'POST',
                    url : '/api/atraccion',
                    data: data
                })
                    .then(function sucess(res) {
                        atraccion._id = res.data._id;
                        $location.url('/atracciones');
                    }, function error(res) {

                    });
            }

        }
    ]
);