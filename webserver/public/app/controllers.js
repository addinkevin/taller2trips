angular.module('tripsApp').controller('tripsAppController', [ '$scope','$http', '$location',
    function tripsAppController( $scope, $http, $location) {
        $scope.menuItems = [
            { url: '#/home', description: 'Inicio', path:'/home' },
            { url: '#/ciudades', description: 'Ciudades' , path:'/ciudades'},
            { url: '#/atracciones', description: 'Atracciones', path:'/atracciones' }
        ];

        $scope.activeMenu = $scope.menuItems[0];

        $scope.setActive = function (menuItem) {
            $scope.activeMenu = menuItem;
        };

        $scope.isActive = function(menuItem) {
            return $location.path() == menuItem.url.slice(1);
        };

}]);