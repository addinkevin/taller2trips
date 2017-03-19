angular.module('tripsApp').controller('tripsAppController', [ '$scope', function tripsAppController( $scope ) {
    $scope.menuItems = [
        { url: '#', description: 'Home'},
        { url: '#/ciudades', description: 'Ciudades'}
    ];

    $scope.activeMenu = $scope.menuItems[0];
    
    $scope.setActive = function (menuItem) {
        $scope.activeMenu = menuItem;
    }
}]);