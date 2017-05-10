var push = angular.module('tripsApp.push');

push.controller('pushFormController',
    [ '$scope', '$http', '$location', '$routeParams', 'IdiomaService', 'PushService',
    function($scope, $http$, $location, $routeParams, IdiomaService, PushService) {

        var editForm = $routeParams.id;
        $scope.push = {};

        function setFormularioErrorMsg(msgError) {
            var msg = "<div class='alert alert-danger text-center'>" +
                "<button type='button' class='close' data-dismiss='alert'>&times;</button>" +
                msgError +
                "</div>";
            $("#error-en-formulario").html(msg);
        }

        function setErrorMsg(msgError) {
            var msg = "<div class='alert alert-danger alert-fixed text-center'>" +
                "<button type='button' class='close' data-dismiss='alert'>&times;</button>" +
                msgError +
                "</div>";
            $("#errorContainer").html(msg);
        }

        function setInfoMsg(msgInfo) {
            var msg = "<div class='alert alert-info alert-fixed text-center'>" +
                msgError +
                "</div>";
            $("#infoContainer").html(msg);
        }

        function submitAddPush() {

        }

        function submitEditPush() {

        }

        $scope.submitPush = function() {
            if (editForm) {
                submitEditPush();
            } else {
                submitAddPush();
            }
        };


        function initAdd() {

        }

        function initEdit() {

        }

        function init() {
            if (editForm) {
                initEdit();
            } else {
                initAdd();
            }
        }

        init();

    }
]);