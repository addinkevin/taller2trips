var puntos = angular.module('tripsApp.puntos');

puntos.controller('ModalPuntos', ['$scope', '$uibModal', 'PuntosService', function ($scope, $uibModal, PuntosService) {
    $scope.showFormPuntos = function (punto) {
        var modalInstance = $uibModal.open({
            ariaLabelledBy: 'modal-title',
            ariaDescribedBy: 'modal-body',
            templateUrl: 'app/puntos/views/form.html',
            controller: 'ModalInstanceCtrl',
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

    $scope.editarPunto = function(atraccion, punto) {
        $scope.showFormPuntos(punto).then(function (result) {

        }, function() {

        })
    }
}]);

puntos.controller('ModalInstanceCtrl', ['$scope', '$uibModalInstance', 'PuntosService', 'IdiomaService', 'punto',
    function ($scope, $uibModalInstance, PuntosService, IdiomaService, punto) {
        $scope.punto = punto;

        $scope.idiomas = IdiomaService.getIdiomas();
        $scope.idiomaModalSelected = $scope.idiomas[0];

        $scope.ok = function () {
            $uibModalInstance.close($scope.punto);
        };

        $scope.cancel = function () {
            $uibModalInstance.dismiss('cancel');
        };

        $scope.submitPunto = function() {
            $uibModalInstance.close($scope.punto);
        };

        $scope.uploadImageClick = function() {
            if (!event.target.files[0]) return;
            $scope.punto.imagenes.push({
                imgSrc: window.URL.createObjectURL(event.target.files[0]),
                imgFile: event.target.files[0]
            });
            $scope.$digest();
        };

        $scope.deleteImage = function(id, puntoImagen) {
            $scope.punto.imagenes.splice(id,1);
            if (!puntoImagen.imgFile) { // Alojada en el server hay que mandar el request de delete.
                $scope.deleteRequests.push(
                    function() {
                        return new Promise.resolve();
                        return ServerService.deleteImageAtraccion($scope.atraccion, atraccionImagen, function(data, error) {
                            if (error) {
                                console.log(error.msg);
                            }
                        });
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

            $scope.$digest();
        };

        $scope.deleteAudio = function(id, puntoAudio) {
            $scope.punto.audios[$scope.idiomaFormulario.code].splice(id,1);
            if (!puntoAudio.audFile) { // Alojada en el server hay que mandar el request de delete.
                $scope.deleteRequests.push(
                    function() {
                        return new Promise.resolve();
                        return ServerService.deleteAudioAtraccion($scope.atraccion, atraccionAudio, function(data, error) {
                            if (error) {
                                console.log(error.msg);
                            }
                        });
                    }
                );
            }
        };
}]);
