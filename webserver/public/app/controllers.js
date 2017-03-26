angular.module('tripsApp').controller('tripsAppController', [ '$scope', function tripsAppController( $scope ) {
    $scope.menuItems = [
        { url: '#/home', description: 'Home' },
        { url: '#/ciudades', description: 'Ciudades' },
        { url: '#/Atracciones', description: 'Atracciones' }
    ];

    $scope.activeMenu = $scope.menuItems[0];
    
    $scope.setActive = function (menuItem) {
        $scope.activeMenu = menuItem;
    };



}]);