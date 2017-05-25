var puntos = angular.module('tripsApp.puntos');

puntos.controller('ModalPuntos', ['$scope', '$uibModal', 'PuntosService', function ($scope, $uibModal, PuntosService) {
    $scope.showFormPuntos = function (punto) {
        var modalInstance = $uibModal.open({
            ariaLabelledBy: 'modal-title',
            ariaDescribedBy: 'modal-body',
            templateUrl: 'app/puntos/views/form.html',
            controller: 'ModalInstanceCtrl',
            keyboard: false,
            backdrop: 'static',
            scope: $scope,
            size: 'lg',
            resolve: {
                punto: function () {
                    return punto;
                }
            }
        });

        return modalInstance.result;
    };

    $scope.agregarPunto = function(atraccion) {
        var punto = PuntosService.createNewPunto();
        $scope.showFormPuntos(punto).then(function (result) {
            atraccion.ids_puntos.push(result);
        }, function() {

        })
    };

    $scope.editarPunto = function(atraccion, punto, id) {
        $scope.showFormPuntos(punto).then(function (result) {
            for (var i = 0; i < result.deleteRequests.length; i++) {
                $scope.deleteRequests.push(result.deleteRequests[i]);
            }

            result.deleteRequests = [];

            atraccion.ids_puntos[id] = result;

        }, function() {

        })
    }
}]);

puntos.controller('ModalInstanceCtrl', ['$scope', '$uibModalInstance', 'PuntosService', 'IdiomaService', 'punto',
    function ($scope, $uibModalInstance, PuntosService, IdiomaService, punto) {
        $scope.punto = PuntosService.copiarPunto(punto);

        $scope.idiomas = IdiomaService.getIdiomas();
        $scope.idiomaModalSelected = $scope.idiomas[0];

        function setFormularioErrorMsg(msgError) {
            var msg = "<div class='alert alert-danger text-center'>" +
                "<button type='button' class='close' data-dismiss='alert'>&times;</button>" +
                msgError +
                "</div>";
            $("#error-en-formulario-punto").html(msg);
        }

        $scope.ok = function () {
            $uibModalInstance.close($scope.punto);
        };

        $scope.cancel = function () {
            $uibModalInstance.dismiss('cancel');
        };

        $scope.submitPunto = function() {
            if (!estaFormularioOk()) return;
            $uibModalInstance.close($scope.punto);
        };

        function checkNombre() {
            if ($scope.punto.nombre.length == 0) {
                setFormularioErrorMsg("Debe ingresar un nombre para el punto de interés");
                return false;
            }
            return true;
        }

        function checkDescripcion() {
            var alMenosUnIdioma = false;
            for (var i = 0; i < $scope.idiomas.length; i++) {
                var idioma = $scope.idiomas[i];
                if ($scope.punto.descripcion[idioma.code] != "") {
                    alMenosUnIdioma = true;
                    break;
                }
            }

            if (!alMenosUnIdioma) {
                setFormularioErrorMsg("Debe ingresar la descripción del punto de interés en al menos un idioma");
            }

            return alMenosUnIdioma;
        }

        function checkImagen() {
            if ($scope.punto.imagenes.length == 0) {
                setFormularioErrorMsg("Debe subir al menos una imagen para el punto de interés.");
                return false;
            }

            return true;
        }

        function estaFormularioOk() {
            return  !(!checkNombre() || !checkDescripcion() || !checkImagen());
        }


        $scope.uploadImageClick = function() {
            if (!event.target.files[0]) return;
            $scope.punto.imagenes.push({
                imgSrc: window.URL.createObjectURL(event.target.files[0]),
                imgFile: event.target.files[0]
            });
            event.target.value = "";
            $scope.$digest();
        };

        $scope.deleteImage = function(id, puntoImagen) {
            $scope.punto.imagenes.splice(id,1);
            if (!puntoImagen.imgFile) { // Alojada en el server hay que mandar el request de delete.
                $scope.punto.deleteRequests.push(
                    function() {
                        return PuntosService.deleteImagenPunto($scope.punto, puntoImagen);
                    }
                );
            }
        };

        $scope.uploadAudioClick = function() {
            if (!event.target.files[0]) return;

            var newAudio = {
                audSrc: window.URL.createObjectURL(event.target.files[0]),
                audFile: event.target.files[0],
                idiomaAudio: $scope.idiomaModalSelected.code
            };

            if (!$scope.punto.audios[$scope.idiomaModalSelected.code]) {
                $scope.punto.audios[$scope.idiomaModalSelected.code] = [newAudio];
            } else {
                if ($scope.punto.audios[$scope.idiomaModalSelected.code][0]) {
                    $scope.deleteAudio(0, $scope.punto.audios[$scope.idiomaModalSelected.code][0]);
                }

                $scope.punto.audios[$scope.idiomaModalSelected.code].push(newAudio);
            }

            event.target.value = "";
            $scope.$digest();
        };

        $scope.deleteAudio = function(id, puntoAudio) {
            $scope.punto.audios[$scope.idiomaFormulario.code].splice(id,1);
            if (!puntoAudio.audFile) { // Alojada en el server hay que mandar el request de delete.
                $scope.punto.deleteRequests.push(
                    function() {
                        return PuntosService.deleteAudioPunto($scope.punto, puntoAudio);
                    }
                );
            }
        };

        $scope.uploadVideoClick = function(event) {
            if (!event.target.files[0]) return;
            if ($scope.punto.videos[0]) {
                $scope.deleteVideo(0,$scope.punto.videos[0]);
            }
            $scope.punto.videos.push({
                vidSrc: window.URL.createObjectURL(event.target.files[0]),
                vidFile: event.target.files[0]
            });
            event.target.value = "";
            $scope.$digest();
        };

        $scope.deleteVideo = function(id, puntoVideo) {
            $scope.punto.videos.splice(id,1);
            if (!puntoVideo.vidFile) { // Alojada en el server hay que mandar el request de delete.
                $scope.punto.deleteRequests.push(
                    function() {
                        return PuntosService.deleteVideoPunto($scope.punto, puntoVideo);
                    }
                );
            }
        };
}]);
