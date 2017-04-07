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
            };

            $scope.getAtracciones = function() {
                $http.get('/api/ciudad/')
                    .then(function success(res) {
                        console.log("GET OK /api/ciudad");
                        $scope.ciudades = res.data;
                        $scope.mapCiudades = {};
                        for (var i = 0; i < $scope.ciudades.length; i++) {
                            $scope.mapCiudades[$scope.ciudades[i]._id] = $scope.ciudades[i];
                        }
                        $http.get('/api/atraccion')
                            .then(function sucess(res) {
                                console.log("GET OK /api/atraccion");
                                console.log(res.data);
                                $scope.atracciones = res.data;
                                for (var i = 0; i < $scope.atracciones.length; i++) {
                                    var atraccion = $scope.atracciones[i];
                                    var imgUrl = "";
                                    if (atraccion.imagenes) {
                                        imgUrl = '/api/atraccion/'+atraccion._id+'/imagen?filename='+atraccion.imagenes[0];
                                    }
                                    atraccion.preview = imgUrl;
                                }

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
    this.idioma = 'es';
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
            $scope.ciudades = [];
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

            $scope.loadAtraccionAudios = function(data) {
                var audUrl = '/api/atraccion/'+ $scope.atraccion._id + '/audio?idioma=es';
                $scope.atraccion.audios.push({audSrc:audUrl});
            };

            $scope.loadAtraccionVideos = function(data) {
                var vidUrl = '/api/atraccion/'+ $scope.atraccion._id + '/video';
                $scope.atraccion.videos.push({vidSrc:vidUrl});
            };

            $scope.loadAtraccionImagenes = function(data) {
                for (var i = 0; i < data.imagenes.length; i++) {
                    var imgUrl = '/api/atraccion/'+ $scope.atraccion._id + '/imagen?filename='+data.imagenes[i];
                    $scope.atraccion.imagenes.push({imgSrc:imgUrl});
                }
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

                        $scope.loadAtraccionImagenes(data);
                        $scope.loadAtraccionVideos(data);
                        $scope.loadAtraccionAudios(data);
                    }
                });
            };

            $scope.editAtraccion = function(atraccion) {
                ServerService.editAtraccion(atraccion, function(data, err) {
                    if (err) {
                        console.log(err.msg);
                    } else {
                        $location.url('/atracciones/');
                        $scope.addRecursosImagenes(atraccion);
                        $scope.addRecursosVideos(atraccion);
                        $scope.addRecursosAudios(atraccion);
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

            $scope.deleteAudio = function(id, atraccionAudio) {
                $scope.atraccion.audios.splice(id,1);
                if (!atraccionAudio.audFile) { // Alojada en el server hay que mandar el request de delete.
                    ServerService.deleteAudioAtraccion($scope.atraccion, atraccionAudio, function(data, error) {
                        if (error) {
                            console.log(error.msg);
                        }
                    });
                }
            };

            $scope.deleteImage = function(id, atraccionImagen) {
                console.log("Delete img",id,atraccionImagen);
                $scope.atraccion.imagenes.splice(id,1);
                if (!atraccionImagen.imgFile) { // Alojada en el server hay que mandar el request de delete.
                    ServerService.deleteImageAtraccion($scope.atraccion, atraccionImagen, function(data, error) {
                        if (error) {
                            console.log(error.msg);
                        }
                    });
                }
            };

            $scope.deleteVideo = function(id, atraccionVideo) {
                $scope.atraccion.videos.splice(id,1);
                if (!atraccionVideo.vidFile) { // Alojada en el server hay que mandar el request de delete.
                    ServerService.deleteVideoAtraccion($scope.atraccion, atraccionVideo, function(data, error) {
                        if (error) {
                            console.log(error.msg);
                        }
                    });
                }
            };

            $scope.uploadImageClick = function(event) {
                $scope.atraccion.imagenes.push({
                    imgSrc: window.URL.createObjectURL(event.target.files[0]),
                    imgFile: event.target.files[0]
                });
                console.log($scope.atraccion.imagenes);
                $scope.$digest();
            };

            $scope.uploadVideoClick = function(event) {
                $scope.atraccion.videos[0] = {
                    vidSrc: window.URL.createObjectURL(event.target.files[0]),
                    vidFile: event.target.files[0]
                };
                $scope.$digest();
            };

            $scope.uploadAudioClick = function(event) {
                $scope.atraccion.audios[0] = {
                    audSrc: window.URL.createObjectURL(event.target.files[0]),
                    audFile: event.target.files[0]
                };
                $scope.$digest();
            };

            $scope.uploadPlanoClick = function(event) {
                $scope.atraccion.planos.push({imgSrc: window.URL.createObjectURL(event.target.files[0])});
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

            $scope.addRecursosImagenes = function(atraccion) {
                ServerService.uploadImagesAtraccion(atraccion, function(data,err) {
                    if (err) {
                        console.log(err.msg);
                    }
                });
            };

            $scope.addRecursosAudios = function(atraccion) {
                ServerService.uploadAudiosAtraccion(atraccion, function(data,err) {
                    if (err) {
                        console.log(err.msg);
                    }
                });
            };

            $scope.addRecursosVideos = function(atraccion) {
                ServerService.uploadVideosAtraccion(atraccion, function(data,err) {
                    if (err) {
                        console.log(err.msg);
                    }
                });
            };

            $scope.addRecursos = function(atraccion) {
                // Agregado de imagenes.
                $scope.addRecursosImagenes(atraccion);
                $scope.addRecursosVideos(atraccion);
                $scope.addRecursosAudios(atraccion);
            };

            $scope.addAtraccion = function(atraccion) {
                ServerService.addAtraccion(atraccion, function(data, err) {
                    if (err) {
                        console.log(err.msg);
                    } else {
                        $scope.addRecursos(atraccion);
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

            $scope.inicializar();
        }
    ]
);