angular.module('tripsApp').controller('tripsAppController', [ '$scope','$http', function tripsAppController( $scope, $http) {
    $scope.menuItems = [
        { url: '#/home', description: 'Home' },
        { url: '#/ciudades', description: 'Ciudades' },
        { url: '#/atracciones', description: 'Atracciones' }
    ];

    $scope.activeMenu = $scope.menuItems[0];
    
    $scope.setActive = function (menuItem) {
        $scope.activeMenu = menuItem;
    };

    (function(){

        var object = {
            "nombre": "Bariloche",
            "descripcion": "Bariloche es una linda ciudad.",
            "pais": "Argentina"
        }

        /*
        $http.post('/api/ciudad',object).then( function successCallback(response) {
            // this callback will be called asynchronously
            // when the response is available
            console.log("OK:",response);
        }, function errorCallback(response) {
            // called asynchronously if an error occurs
            // or server returns response with an error status.
            console.log("FAIL:",response);
        });
        */
        /*
        $http.get('/api/ciudad').then( function successCallback(response) {
            // this callback will be called asynchronously
            // when the response is available
            console.log("OK:",response);
        }, function errorCallback(response) {
            // called asynchronously if an error occurs
            // or server returns response with an error status.
            console.log("FAIL:",response);
        });
        */


    })();

}]);