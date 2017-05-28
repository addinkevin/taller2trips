var express = require('express');
var router = express.Router();
var Favorito = require('../models/favoritos');
var Login = require('../models/logins');

router.get('/reporte/atraccionesFavoritas', function(req, res) {
    var doce_meses = 365*24*3600*1000;
    var tiempo_inicio = Date.now() - doce_meses;
    var fecha_inicio = new Date(tiempo_inicio);
    Favorito.aggregate(
        [
            {$match: {id_atraccion: {$ne: null}, fecha: {$gte: fecha_inicio}  }},
            {$group:
                {
                    _id: "$id_atraccion",
                    id_atraccion: {$first: "$id_atraccion"},
                    value: {$sum: 1}
                }
            },
            {$sort: {value: -1}},
            {$limit: 10}
        ], function(err, resultado) {
            if (err) {
                res.send(err);
            }
            else {
                Favorito.populate(resultado, {path: "id_atraccion", select: "nombre"}, function(err, favoritos) {
                    if (err) {
                        res.send(err);
                    }
                    else {
                        res.status(200).json(favoritos);
                    }
                });
            }
        }
    );
});

router.get('/reporte/usuariosUnicosGlobales', function(req, res) {
    Login.find(function(err, logins) {
        if (err) {
            res.send(err);
        }
        else {
            res.status(200).json(logins);
        }
    });
});

module.exports = router;
