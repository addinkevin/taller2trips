var atraccionesApp = angular.module('tripsApp.atracciones');

atraccionesApp.controller('atraccionesListadoController',
    [ '$scope', '$http', '$location', 'ServerService', 'IdiomaService',
        function($scope, $http, $location, ServerService, IdiomaService) {
            $scope.deleteAtraccion = function(atraccionId) {
                ServerService.deleteAtraccion(atraccionId, function(data, err) {
                    $location.url('/atracciones/');
                });
            };

            $scope.atracciones = [];

            $scope.getNombreCiudad = function(idCiudad) {
                return $scope.mapCiudades[idCiudad].nombre;
            };

            $scope.agregarListadoDeIdiomas = function(atraccion) {
                atraccion.idiomasCargados = [];
                atraccion.idiomasNoCargados = [];

                var idiomas = IdiomaService.getIdiomas();

                for (var i = 0; i < idiomas.length; i++) {
                    var idioma = idiomas[i];
                    if (atraccion.descripcion[idioma.code] != "") {
                        atraccion.idiomasCargados.push(idioma);
                    } else {
                        atraccion.idiomasNoCargados.push(idioma);
                    }
                }
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
                                    $scope.agregarListadoDeIdiomas(atraccion);
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
        "Ideal para visitar con niños",
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

    this.monedas = [ 'u$s', '$', 'R$', '€' ];
    this.monedaCosto = "u$s";
    this.montoCosto = 0;
    this.duracion = 0;
    this.clasificacionSelected = this.clasificaciones[0];
    this.horaApertura = "";
    this.horaCierre = "";
    this.ciudadSelected = "";
    this.idiomasAudio = [];
    this.imagenes = [];
    this.audios = {};
    this.videos = [];
    this.planos = [];
    this.mapInfo = {};

}

atraccionesApp.controller('atraccionesAddEditController',
    [ '$scope', '$location', '$http','$routeParams', 'ServerService', '$q', 'IdiomaService',
        function ($scope, $location, $http, $routeParams, ServerService, $q, IdiomaService) {

            $scope.sendingInformation = false;
            $scope.editForm = $routeParams.id;
            $scope.ciudades = [];
            $scope.atraccion = new Atraccion();
            $scope.atraccion.idiomas = IdiomaService.getIdiomas();
            $scope.idiomaFormulario = $scope.atraccion.idiomas[0];
            $scope.deleteRequests = [];

            $scope.alert = {
                class : 'hide',
                msg: ''
            };

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


            $scope.validateAtraccion = function(atraccion) {
                var validadorFormatoHora =  function(data) {
                    var regex = /^([01]?[0-9]|2[0-3]):[0-5][0-9]$/;
                    return regex.test(data);
                };

                var validadorCosto = function(data) {
                    return ((data !== "") && (+data >= 0));
                };

                var validadorDuracion = function(data) {
                    return (data !== "" && +data >= 0 && +data == parseInt(data));
                };

                var validadorImagenes = function(data) {
                    return data.length > 0;
                };

                var validadorDescripcion = function(data) {
                    var alMenosUnIdioma = false;
                    for (var i = 0; i < $scope.atraccion.idiomas.length; i++) {
                        var idioma = $scope.atraccion.idiomas[i];
                        if ($scope.atraccion.descripcion[idioma.code] != "") {
                            alMenosUnIdioma = true;
                            break;
                        }
                    }

                    return alMenosUnIdioma;
                };

                var validateList = [
                    {
                        parametro : 'nombre',
                        msg: "Debes especificar el nombre de la atracción!"
                    },
                    {
                        parametro: 'descripcion',
                        msg: "Debes especificar la descripción para al menor un idioma!",
                        validador: validadorDescripcion
                    },
                    {
                        parametro: 'imagenes',
                        msg: "Debes agregar al menos una imagen",
                        validador: validadorImagenes
                    },
                    {
                        parametro: 'monedaCosto',
                        msg: "Debes especificar la moneda del costo de la atracción"
                    },
                    {
                        parametro: 'montoCosto',
                        msg: "Debes especificar el costo de la atracción. Debe ser mayor o igual a 0.",
                        validador: validadorCosto
                    },
                    {
                        parametro: 'horaApertura',
                        msg: "Debe especificarse una hora de apertura que cumpla con el formato HH:MM (Rango: 00:00 - 23:59)",
                        validador: validadorFormatoHora
                    },
                    {
                        parametro: 'horaCierre',
                        msg: "Debe especificarse una hora de cierre que cumpla con el formato HH:MM (Rango: 00:00 - 23:59)",
                        validador: validadorFormatoHora
                    },
                    {
                        parametro: 'duracion',
                        msg: "Debes especificar la duración de la atracción. Debe ser un entero mayor o igual a 0",
                        validador: validadorDuracion
                    }
                ];

                for (var i = 0; i < validateList.length; i++) {
                    var validador = validateList[i].validador;
                    var parametro = validateList[i].parametro;
                    var msg = validateList[i].msg;
                    if ((!validador && !atraccion[parametro]) || (validador && !validador(atraccion[parametro]))) {
                        $scope.alert.msg = msg;
                        return false;
                    }
                }
                return true;
            };

            $scope.setInfoMsg = function(msgError) {
                var msg = "<div class='alert alert-info alert-fixed text-center'>" +
                    msgError +
                    "</div>";
                $("#infoContainer").html(msg);
            };

            $scope.createInfoInternacionalizacionAtraccionNueva = function() {
                $scope.atraccion.descripcion = {};
                $scope.atraccion.idiomasCargados = [];
                $scope.atraccion.idiomasNoCargados = [];

                for (var i = 0; i < $scope.atraccion.idiomas.length; i++) {
                    $scope.atraccion.idiomas[i].statusCargado = false;
                    $scope.atraccion.idiomas[i].statusModificado = false;
                    $scope.atraccion.descripcion[$scope.atraccion.idiomas[i].code] = "";
                    $scope.atraccion.idiomasNoCargados.push($scope.atraccion.idiomas[i]);
                }
            };

            $scope.initAdd = function() {
                $scope.title = "Agregar atracción";
                $scope.submitButton = "Agregar";
                $scope.createInfoInternacionalizacionAtraccionNueva();
                $scope.createMap();
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
                for (var i = 0; i < data.idiomas_audio.length; i++) {
                    var idioma = data.idiomas_audio[i];
                    var audUrl = '/api/atraccion/'+ $scope.atraccion._id + '/audio?idioma=' + idioma + '&date=' + new Date().getTime();
                    $scope.atraccion.audios[idioma] = [{audSrc:audUrl, idiomaAudio:idioma}];
                }
            };

            $scope.loadAtraccionVideos = function(data) {
                var vidUrl = '/api/atraccion/'+ $scope.atraccion._id + '/video';
                // REFACTOR. LA API TENDRIA QUE SER REST. VER
                $http.get(vidUrl).then(
                    function success(res) {
                        $scope.atraccion.videos.push({vidSrc:vidUrl});
                    },
                    function error(res) {
                        $scope.atraccion.videos = [];
                    }
                );
            };

            $scope.loadAtraccionPlanos = function(data) {
                var imgUrl = '/api/atraccion/'+ $scope.atraccion._id + '/plano';
                $http.get(imgUrl).then(
                    function success(res) {
                        $scope.atraccion.planos.push({imgSrc:imgUrl});
                    },
                    function error(res) {
                        $scope.atraccion.planos = [];
                    }
                );
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
                                'lng': 'longitud',
                                'idiomasAudio': 'idiomas_audio'
                            }
                        );

                        $scope.atraccion.ciudadSelected = $scope.ciudades.find(
                            function(element) {
                                return element._id == data.id_ciudad;
                            }
                        );

                        $scope.loadAtraccionPlanos(data);
                        $scope.loadAtraccionImagenes(data);
                        $scope.loadAtraccionVideos(data);
                        $scope.loadAtraccionAudios(data);
                        $scope.createMap();
                    }
                });
            };

            $scope.editAtraccion = function(atraccion) {
                if (!$scope.validateAtraccion(atraccion)) {
                    $scope.alert.class = '';
                    return;
                }

                $scope.setInfoMsg("Enviando información al servidor");
                $scope.sendingInformation = true;

                ServerService.editAtraccion(atraccion, function(data, err) {
                    if (err) {
                        console.log(err.msg);
                        $location.url('/atracciones/');
                    } else {
                        var promiseDeleteRecursos = $scope.sendDeleteRequests();
                        var promiseAddRecursos = $scope.addRecursos(atraccion);
                        $q.all([promiseDeleteRecursos, promiseAddRecursos]).then(
                            function success() {
                                $location.url('/atracciones/');
                            }, function error() {
                                $location.url('/atracciones/');
                            }
                        )
                    }
                });
            };

            $scope.submitEditAtraccion = function() {
                console.log("Edit submit atraccion");
                $scope.editAtraccion($scope.atraccion);
            };

            $scope.submitAtraccion = function() {
                if ($scope.sendingInformation) return;
                if ($scope.editForm) {
                    $scope.submitEditAtraccion();
                } else {
                    $scope.submitAddAtraccion();
                }
            };

            $scope.sendDeleteRequests = function() {
                var requests = [];

                for (var i = 0; i < $scope.deleteRequests.length; i++) {
                    requests.push($scope.deleteRequests[i]());
                }

                return $q.all(requests);
            };

            $scope.deleteAudio = function(id, atraccionAudio) {
                $scope.idiomaFormulario.statusModificado = true;

                $scope.atraccion.audios[$scope.idiomaFormulario.code].splice(id,1);
                if (!atraccionAudio.audFile) { // Alojada en el server hay que mandar el request de delete.
                    $scope.deleteRequests.push(
                        function() {
                            return ServerService.deleteAudioAtraccion($scope.atraccion, atraccionAudio, function(data, error) {
                                if (error) {
                                    console.log(error.msg);
                                }
                            });
                        }
                    );
                }
            };

            $scope.deletePlano = function(id, atraccionPlano) {
                $scope.atraccion.planos.splice(id,1);
                if (!atraccionPlano.imgFile) { // Alojada en el server hay que mandar el request de delete.
                    $scope.deleteRequests.push(
                        function() {
                            return ServerService.deletePlanoAtraccion($scope.atraccion, atraccionPlano, function(data, error) {
                                if (error) {
                                    console.log(error.msg);
                                }
                            });
                        }
                    );
                }
            };

            $scope.deleteImage = function(id, atraccionImagen) {
                $scope.atraccion.imagenes.splice(id,1);
                if (!atraccionImagen.imgFile) { // Alojada en el server hay que mandar el request de delete.
                    $scope.deleteRequests.push(
                        function() {
                            return ServerService.deleteImageAtraccion($scope.atraccion, atraccionImagen, function(data, error) {
                                if (error) {
                                    console.log(error.msg);
                                }
                            });
                        }
                    );
                }
            };

            $scope.deleteVideo = function(id, atraccionVideo) {
                $scope.atraccion.videos.splice(id,1);
                if (!atraccionVideo.vidFile) { // Alojada en el server hay que mandar el request de delete.
                    $scope.deleteRequests.push(
                        function() {
                            return ServerService.deleteVideoAtraccion($scope.atraccion, atraccionVideo, function(data, error) {
                                if (error) {
                                    console.log(error.msg);
                                }
                            });
                        }
                    );
                }
            };

            $scope.uploadImageClick = function(event) {
                if (!event.target.files[0]) return;
                $scope.atraccion.imagenes.push({
                    imgSrc: window.URL.createObjectURL(event.target.files[0]),
                    imgFile: event.target.files[0]
                });
                console.log($scope.atraccion.imagenes);
                $scope.$digest();
            };

            $scope.uploadVideoClick = function(event) {
                if (!event.target.files[0]) return;
                if ($scope.atraccion.videos[0]) {
                    $scope.deleteVideo(0,$scope.atraccion.videos[0]);
                }
                $scope.atraccion.videos.push({
                    vidSrc: window.URL.createObjectURL(event.target.files[0]),
                    vidFile: event.target.files[0]
                });
                $scope.$digest();
            };

            $scope.uploadAudioClick = function(event) {
                if (!event.target.files[0]) return;

                $scope.idiomaFormulario.statusModificado = true;

                var newAudio = {
                    audSrc: window.URL.createObjectURL(event.target.files[0]),
                    audFile: event.target.files[0],
                    idiomaAudio: $scope.idiomaFormulario.code
                };

                if (!$scope.atraccion.audios[$scope.idiomaFormulario.code]) {
                    $scope.atraccion.audios[$scope.idiomaFormulario.code] = [newAudio];
                } else {
                    if ($scope.atraccion.audios[$scope.idiomaFormulario.code][0]) {
                        $scope.deleteAudio(0, $scope.atraccion.audios[$scope.idiomaFormulario.code][0]);
                    }

                    $scope.atraccion.audios[$scope.idiomaFormulario.code].push(newAudio);
                }

                $scope.$digest();
            };

            $scope.uploadPlanoClick = function(event) {
                if (!event.target.files[0]) return;

                if ($scope.atraccion.planos[0]) {
                    $scope.deletePlano(0, $scope.atraccion.planos[0]);
                }

                $scope.atraccion.planos.push({
                    imgSrc: window.URL.createObjectURL(event.target.files[0]),
                    imgFile: event.target.files[0]
                });
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

            $scope.addRecursosPlanos = function(atraccion) {
                return ServerService.uploadPlanosAtraccion(atraccion, function(data,err) {
                    if (err) {
                        console.log(err.msg);
                    }
                });
            };

            $scope.addRecursosImagenes = function(atraccion) {
                return ServerService.uploadImagesAtraccion(atraccion, function(data,err) {
                    if (err) {
                        console.log(err.msg);
                    }
                });
            };

            $scope.addRecursosAudios = function(atraccion) {
                return ServerService.uploadAudiosAtraccion(atraccion, function(data,err) {
                    if (err) {
                        console.log(err.msg);
                    }
                });
            };

            $scope.addRecursosVideos = function(atraccion) {
                return ServerService.uploadVideosAtraccion(atraccion, function(data,err) {
                    if (err) {
                        console.log(err.msg);
                    }
                });
            };

            $scope.addRecursos = function(atraccion) {
                var promisePlanos = $scope.addRecursosPlanos(atraccion);
                var promiseImagenes = $scope.addRecursosImagenes(atraccion);
                var promiseVideos = $scope.addRecursosVideos(atraccion);
                var promiseAudios = $scope.addRecursosAudios(atraccion);

                return $q.all([ promisePlanos, promiseImagenes, promiseAudios, promiseVideos]);
            };

            $scope.addAtraccion = function(atraccion) {
                if (!$scope.validateAtraccion(atraccion)) {
                    $scope.alert.class = '';
                    return;
                }

                $scope.setInfoMsg("Enviando información al servidor...");
                $scope.sendingInformation = true;

                ServerService.addAtraccion(atraccion, function(data, err) {
                    if (err) {
                        console.log(err.msg);
                        $location.url('/atracciones/');
                    } else {
                        $scope.addRecursos(atraccion).then(
                            function success() {
                                $location.url('/atracciones/');
                            },
                            function error() {
                                $location.url('/atracciones/');
                            }
                        );
                    }
                });
            };

            $scope.inicializar = function() {
                $scope.loadCiudades();
                if ($scope.editForm) {
                    $scope.initEdit();
                } else {
                    $scope.initAdd();
                }
            };

            $scope.createMap = function() {
                var map = new google.maps.Map(document.getElementById('map'), {
                    zoom: 13,
                    center: {lat: $scope.atraccion.lat, lng: $scope.atraccion.lng}
                });

                var input = document.getElementById('pac-input');
                var searchBox = new google.maps.places.SearchBox(input);
                map.controls[google.maps.ControlPosition.TOP_LEFT].push(input);

                // Bias the SearchBox results towards current map's viewport.
                map.addListener('bounds_changed', function() {
                    searchBox.setBounds(map.getBounds());
                });

                var markers = [];

                searchBox.addListener('places_changed', function() {
                    var places = searchBox.getPlaces();

                    if (places.length == 0) {
                        return;
                    }


                    // Clear out the old markers.
                    markers.forEach(function(marker) {
                        marker.setMap(null);
                    });
                    markers = [];

                    // For each place, get the icon, name and location.
                    var bounds = new google.maps.LatLngBounds();
                    places.forEach(function(place) {
                        if (!place.geometry) {
                            console.log("Returned place contains no geometry");
                            return;
                        }
                        var icon = {
                            url: place.icon,
                            size: new google.maps.Size(71, 71),
                            origin: new google.maps.Point(0, 0),
                            anchor: new google.maps.Point(17, 34),
                            scaledSize: new google.maps.Size(25, 25)
                        };

                        var newMarker = new google.maps.Marker({
                            map: map,
                            icon: icon,
                            title: place.name,
                            position: place.geometry.location
                        });

                        // Create a marker for each place.
                        markers.push(newMarker);

                        if (place.geometry.viewport) {
                            // Only geocodes have viewport.
                            bounds.union(place.geometry.viewport);
                        } else {
                            bounds.extend(place.geometry.location);
                        }
                    });
                    map.fitBounds(bounds);
                });

                var marker = new google.maps.Marker({
                    position: {lat: $scope.atraccion.lat, lng: $scope.atraccion.lng},
                    map: map,
                    title: 'Click atraccion',
                    mapTypeId: 'roadmap'
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