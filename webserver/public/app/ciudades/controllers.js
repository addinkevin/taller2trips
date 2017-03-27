var ciudadesApp = angular.module('tripsApp.ciudades');

ciudadesApp.controller('ciudadesController', [ '$scope', function ciudadesController( $scope ) {
}]);

ciudadesApp.controller('OLDciudadesAddController',
    [ '$scope', 'GoogleMaps', 'CiudadesService', '$location', '$http',
        function ciudadesAddControler($scope, GoogleMaps, CiudadesService, $location, $http) {
            $scope.ciudad = {
                name: "",
                center : {lat: 37, lng: -95},
                radio: 1000,
                imageFile: "",
                imageBin: ""
            };

            $scope.addCiudad = function() {
                $http.post('/api/ciudades', $scope.ciudad)
                    .then(function(data) {
                            console.log(data);
                            CiudadesService.setCiudades(data);
                            $location.url('/ciudades/listado');
                        }, function(data) {
                            console.log('Err:' + data);
                    });
            };

            $scope.drawCircle = function(map, city) {
                return new google.maps.Circle({
                    strokeColor: '#FF0000',
                    strokeOpacity: 0.8,
                    strokeWeight: 2,
                    fillColor: '#FF0000',
                    fillOpacity: 0.35,
                    map: map,
                    center: city.center,
                    radius: city.radio
                });
            };

            $scope.initMap = function initMap() {
                var properties = {
                    zoom: 4,
                    center: $scope.ciudad.center,
                    mapTypeId: google.maps.MapTypeId.TERRAIN
                };

                var mapObject = GoogleMaps.createMap('map', properties);
                var map = mapObject.map;
                mapObject.marker = new google.maps.Marker({
                    position: properties.center,
                    map: map,
                    title: 'Click ciudad'
                });

                map.addListener('click', function(event) {
                    mapObject.marker.setPosition(event.latLng);
                    $scope.ciudad.center.lat = event.latLng.lat();
                    $scope.ciudad.center.lng = event.latLng.lng();
                    $scope.$digest();
                });

                mapObject.marker.cityCircle = $scope.drawCircle(map, $scope.ciudad);
                mapObject.marker.cityCircle.bindTo('center', mapObject.marker, 'position');

            };

            $scope.addChangeRadio = function() {
                var radio = $scope.ciudad.radio;
                if (radio < 0) radio = 1000;

                GoogleMaps.getMap('map').marker.cityCircle.setRadius(radio);
            };

}]);


ciudadesApp.controller('ciudadesAddController',
    [ '$scope', 'CiudadesService', '$location', '$http',
        function ($scope, CiudadesService, $location, $http) {
            $scope.ciudad = {};

            $scope.updateImageClick = function(event) {
                $scope.ciudad.imgSrc = event.target.files[0];
                console.log($scope.ciudad.imgSrc);
                $scope.$digest();
            };

            $scope.submitAddCiudad = function() {
                $scope.addCiudad($scope.ciudad);
                $location.url('/ciudades/');
            };

            $scope.addCiudad = function(ciudad) {
                $http({
                    method: 'POST',
                    url : '/api/ciudad',
                    data: ciudad
                })
                    .then(function sucess(res) {
                        ciudad._id = res.data._id;
                        var url = '/api/ciudad/' + ciudad._id + '/imagen';
                        $http({
                            method: 'POST',
                            url: url,
                            headers: {
                                //'Content-Type: multipart/form-data; boundary=----WebKitFormBoundary7MA4YWxkTrZu0gW'
                                'Content-Type': undefined
                            },
                            data: {
                                imagen: ciudad.imgSrc
                            },
                            transformRequest: function (data, headersGetter) {
                                var formData = new FormData();
                                angular.forEach(data, function (value, key) {
                                    formData.append(key, value);
                                });

                                return formData;
                            }
                        })
                            .then(function success(res) {

                            }, function error(res) {

                            });

                    }, function error(res) {

                    });
            }
        }
        ]
);

ciudadesApp.controller('ciudadesEditController',
    [ '$scope', 'CiudadesService', '$location', '$http', '$routeParams',
        function ($scope, CiudadesService, $location, $http, $routeParams) {
            $scope.ciudad = CiudadesService.getCiudad($routeParams.id);

            $scope.updateImageClick = function(event) {
                //$scope.ciudad.imgSrc = window.URL.createObjectURL(event.target.files[0]);
                $scope.ciudad.imgSrc = event.target.files[0];
                console.log($scope.ciudad.imgSrc);
                $scope.$digest();
            };

            $scope.submitEditCiudad = function() {
                console.log("Editando ciudad:", $scope.ciudad);
                CiudadesService.updateCiudad($routeParams.id, $scope.ciudad);
                $location.url('/ciudades');
            }
        }
        ]
);

ciudadesApp.controller('ciudadesListadoController',
    [ '$scope' , 'CiudadesService', '$http', '$location',
    function($scope, CiudadesService, $http, $location) {

        $scope.ciudades = [];

        $scope.deleteCiudad = function(ciudadId) {
            CiudadesService.removeCiudad($scope.ciudades[ciudadId]);
            $location.url('/ciudades/');
        };


        $scope.getCiudades = function() {
            $http.get('/api/ciudad').then( function successCallback(response) {
                console.log('GET /api/ciudad', response);
                $scope.ciudades = response.data;
                for (var i = 0; i < $scope.ciudades.length; i ++) {
                    var _ciudad = $scope.ciudades[i];
                    $http({
                        url: '/api/ciudad/'+_ciudad._id+'/imagen/',
                        method: 'GET',
                        headers: { 'Content-Type': undefined},
                        transformRequest: angular.identity
                    }).then(function success(res) {
                        console.log('GET OK /api/ciudad/'+_ciudad._id+'/imagen/', res);
                        _ciudad.imgSrc = res.data;
                    }, function fail(res) {
                        console.log('GET FAIL /api/ciudad/'+_ciudad._id+'/imagen/', res);
                    });
                }
            });
        };

        $scope.getCiudades();

}]);