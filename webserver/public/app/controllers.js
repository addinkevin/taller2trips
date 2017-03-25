angular.module('tripsApp').controller('tripsAppController', [ '$scope', function tripsAppController( $scope ) {
    $scope.menuItems = [
        { url: '#/home', description: 'Home', subItems: [

        ]},
        { url: '#/ciudades', description: 'Ciudades', subItems: [
            {url: '#/ciudades/listado', description: 'Ver listado'},
            {url: '#/ciudades/add', description: 'Agregar ciudad'},
            {url: '#/ciudades/remove', description: 'Borrar ciudad'}
        ]}
    ];

    $scope.activeMenu = $scope.menuItems[0];
    
    $scope.setActive = function (menuItem) {
        $scope.activeMenu = menuItem;
    };



}]);