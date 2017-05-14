var recorridos = angular.module('tripsApp.recorridos');

recorridos.controller('recorridoListadoController', [ '$scope', '$http', '$location', 'IdiomaService', 'RecorridosService',
    function($scope, $http, $location, IdiomaService, RecorridosService) {
        $scope.recorridos = [];
        $scope.myInterval = 0;
        $scope.noWrapSlides = false;
        $scope.active = 0;

        $scope.opcionesDeFiltro = [
            {
                'filtroName': 'nombre_recorrido',
                'descripcion': 'Nombre del recorrido'
            },
            {
                'filtroName': 'nombre_ciudad',
                'descripcion': 'Ciudad'
            }
        ];

        $scope.filtroSelected = $scope.opcionesDeFiltro[0];
        $scope.filtros = [];
        $scope.contenidoFiltro = "";

        $scope.agregarFiltro = function() {
            if ($scope.contenidoFiltro == "") {
                return;
            }
            var element = $scope.filtros.find(function(element) {
                return element.filtro == $scope.filtroSelected;
            });
            if (element) {
                element.contenido = $scope.contenidoFiltro;
            } else {
                $scope.filtros.push( {
                    filtro: $scope.filtroSelected,
                    contenido: $scope.contenidoFiltro
                });
            }
            $scope.contenidoFiltro = "";
        };

        $scope.deleteFiltro = function(id) {
            $scope.filtros.splice(id,1);
        };

        $scope.buscarRecorridos = function() {
            console.log("Buscando recorridos...");
            // TODO
        };

        $scope.deleteRecorrido = function (recorrido) {
            RecorridosService.deleteRecorrido(recorrido).then(function success(res) {
                $location.url('/recorridos');
            }, function error(res) {

            });
        };

        $scope.getNombreCiudad = function (recorrido) {
            return recorrido.id_ciudad.nombre;
        };

        $scope.getListadoDeAtracciones = function (recorrido) {
            return RecorridosService.getListadoDeAtracciones(recorrido);
        };

        $scope.getLinksImagenes = function(recorrido) {
            var links = [];
            for (var i = 0; i < recorrido.ids_atracciones.length; i++) {
                var atraccion = recorrido.ids_atracciones[i];
                links.push({src: $scope.getImgAtraccionUrl(atraccion), alt: "", caption: atraccion.nombre, id: i});
            }
            return links;
        };

        $scope.getSomeImage = function(recorrido) {
            return $scope.getImgAtraccionUrl(recorrido.ids_atracciones[0]);
        };

        $scope.getImgAtraccionUrl = function(atraccion) {
            return '/api/atraccion/'+atraccion._id+'/imagen?filename='+atraccion.imagenes[0];
        };

        var makeSlides = function() {
            for (var i = 0; i < $scope.recorridos.length; i++) {
                var recorrido = $scope.recorridos[i];
                recorrido.slides = $scope.getLinksImagenes(recorrido);
                console.log(recorrido.slides);
            }
        };

        var loadRecorridos = function() {
            RecorridosService.getRecorridos().then(function success(res) {
                $scope.recorridos = res.data;
                makeSlides();
                makeIdiomas();
            }, function error(res) {

            });
        };

        function makeIdiomas() {
            for (var i = 0; i < $scope.recorridos.length; i++) {
                var recorrido = $scope.recorridos[i];
                agregarListadoDeIdiomas(recorrido);
            }
        }

        function agregarListadoDeIdiomas(recorrido) {
            recorrido.idiomasCargados = [];
            recorrido.idiomasNoCargados = [];

            var idiomas = IdiomaService.getIdiomas();

            for (var i = 0; i < idiomas.length; i++) {
                var idioma = idiomas[i];
                if (recorrido.descripcion[idioma.code] != "") {
                    recorrido.idiomasCargados.push(idioma);
                } else {
                    recorrido.idiomasNoCargados.push(idioma);
                }
            }
        }

        $scope.init = function() {
            loadRecorridos();
        };

        $scope.init();



}]);