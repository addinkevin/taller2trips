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
        function ciudadesAddControler($scope, CiudadesService, $location, $http) {
            console.log("AngularJs Controller: ciudadesAddController");

            $scope.form = {
                ciudad: {}
            };

            $scope.updateImageClick = function(event) {
                $scope.form.ciudad.imgSrc = window.URL.createObjectURL(event.target.files[0]);
                console.log($scope.form.ciudad.imgSrc);
                $scope.$digest();
            }

            $scope.submitAddCiudad = function() {
                console.log("Agregado la ciudad:", $scope.form.ciudad);
                CiudadesService.addCiudad($scope.form.ciudad);
                $location.url('/ciudades/');
            }
        }
        ]
);
ciudadesApp.controller('ciudadesListadoController',
    [ '$scope' , 'CiudadesService', '$http',
    function($scope, CiudadesService, $http) {
        $scope.deleteCiudad = function(ciudadId) {
            // TODO
        };

        $scope.ciudades = CiudadesService.getCiudades();
}]);