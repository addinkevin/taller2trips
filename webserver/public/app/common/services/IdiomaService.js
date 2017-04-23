var tripsApp = angular.module('tripsApp');

tripsApp.service('IdiomaService', ['$http', function ($http) {
    this.getIdiomas = function() {
        // TODO. El servidor debería devolverme los idiomas disponibles.
        //return { 'es': 'Español', 'en' : 'English', 'pt' : 'Portugues' };
        return [
            {
                'code': 'es',
                'nombre': 'Español'
            },
            {
                'code': 'en',
                'nombre': 'Ingles'
            },
            {
                'code': 'pt',
                'nombre': 'Portugues'
            }
        ];
    };
}]);