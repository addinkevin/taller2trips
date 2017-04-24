var resenias = angular.module('tripsApp.resenias');


resenias.controller('reseniasController', [ '$scope', 'HelperResenias', function($scope, HelperResenias) {
    console.log("Reseñas controller");

    $scope.setErrorMsg = function(msgError) {
        var msg = "<div class='alert alert-danger alert-fixed text-center'>" +
            "<button type='button' class='close' data-dismiss='alert'>&times;</button>" +
            msgError +
            "</div>";
        $("#errorContainer").html(msg);
    };

    $scope.setInfoMsg = function(msgError) {
        var msg = "<div class='alert alert-info alert-fixed text-center'>" +
            msgError +
            "</div>";
        $("#infoContainer").html(msg);
    };

    $scope.showTable = false;
    $scope.opcionesDeFiltro = [
        {
            'filtroName': 'id_atraccion',
            'descripcion': 'Atraccion'
        },
        {
            'filtroName': 'id_ciudad',
            'descripcion': 'Ciudad'
        },
        {
            'filtroName': 'descripcion',
            'descripcion': 'Descripción'
        },
        {
            'filtroName': 'calificacion',
            'descripcion': 'Calificación (1 - 5)'
        },
        {
            'filtroName': 'descripcion',
            'descripcion': 'Descripción'
        },
        {
            'filtroName': 'id_usuario',
            'descripcion': 'ID Usuario'
        },
        {
            'filtroName': 'idioma',
            'descripcion': 'Idioma'
        }
    ];

    $scope.filtroSelected = $scope.opcionesDeFiltro[0];
    $scope.contenidoFiltro = "";
    $scope.filtros = [];
    $scope.resultados = [];

    $scope.agregarFiltro = function() {
        $scope.filtros.push( {
            filtro: $scope.filtroSelected,
            contenido: $scope.contenidoFiltro
        });
    };

    $scope.deleteFiltro = function(id) {
        $scope.filtros.splice(id,1);
    };

    $scope.buscarResenias = function() {
        console.log("Buscando reseñas...");
        $scope.showTable = true;
        HelperResenias.getResultados($scope.filtros, function(data, error) {
            if (error) {
                $scope.resultados = [];
            } else {
                $scope.resultados = data;
                console.log(data);
            }
        });
    };

    $scope.getCiudad = function(id, resultado) {
        return resultado.id_ciudad;
    };

    $scope.getAtraccion = function(id, resultado) {
        return resultado.id_atraccion;
    };

    $scope.getNombreDeUsuario = function(id, resultado) {
        var result = "";
        if (result.usuario) {
            result = (resultado.usuario.nombre || "") + " " + (resultado.usuario.apellido || "");
        }
        result = result + " (" + resultado.id_usuario + ")";
        return result;
    };

    $scope.getCalificacion = function(id, resultado) {
        return "★".repeat(resultado.calificacion);
    };

    $scope.getIdioma = function(id, resultado) {
        return resultado.idioma;
    };

    $scope.getDescripcion = function(id, resultado) {
        return resultado.descripcion;
    };

    $scope.bloquearUsuario = function(id, resultado) {
        HelperResenias.bloquearUsuarios(resultado.usuario).then(function success(res) {
            $scope.setInfoMsg("Usuario bloqueado.");
        }, function error(res) {
            $scope.setErrorMsg("No pudo bloquearse el usuario. Intentelo en otro momento.");
        });
    };

    $scope.borrarResenia = function(id, resultado) {
        HelperResenias.borrarResenia(resultado).then(function success(res) {
            $scope.resultados.splice(id,1);
            $scope.setInfoMsg("Reseña borrada correctamente.");
        }, function error(res) {
            $scope.setErrorMsg("No se pudo borrar la reseña.");
        });

    };

}]);