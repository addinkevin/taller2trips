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
    var fecha_inicio = new Date(req.query.anio_inicio, req.query.mes_inicioi - 1, req.query.dia_inicio);
    var fecha_fin = new Date(req.query.anio_fin, req.query.mes_fin - 1, req.query.dia_fin);
    Login.aggregate(
        [
            {$match: {fecha: {$gte: fecha_inicio, $lt: fecha_fin}}},
            {$project:
                {
                    "id_usuario": 1,
                    "provider": 1,
                    "pais": 1,
                    "mes": {"$month": "$fecha"},
                    "anio": {"$year": "$fecha"}
                }
            },
            {$group:
                {
                    _id: {
                        "mes": "$mes",
                        "anio": "$anio",
                        "id_usuario": "$id_usuario"
                    }
                }
            },
            {$group:
                {
                    _id: {
                        "mes": "$_id.mes",
                        "anio": "$_id.anio"
                    },
                    value: {$sum: 1}
                }
            },
            {$sort: {"_id.anio": 1, "_id.mes": 1}}
        ], function(err, resultados) {
            if (err) {
                res.send(err);
            }
            else {
                res.status(200).json(resultados);
            }
        }
    )
});

router.get('/reporte/usuariosUnicosPaisProvider', function(req, res) {
    var fecha_inicio = new Date(req.query.anio_inicio, req.query.mes_inicioi - 1, req.query.dia_inicio);
    var fecha_fin = new Date(req.query.anio_fin, req.query.mes_fin - 1, req.query.dia_fin);
    Login.aggregate(
        [
            {$match: {fecha: {$gte: fecha_inicio, $lt: fecha_fin}}},
            {
                $group: {
                    _id: {
                        "id_usuario": "$id_usuario",
                        "pais": "$pais",
                    },
                    provider: {$addToSet: "$provider"}
                }
            },
            {$unwind: "$provider"},
            {
                $group: {
                    _id: {
                        "pais": "$_id.pais",
                        "provider": "$provider"
                    },
                    value: {$sum: 1}
                }
            },
            {
                $group: {
                    _id: {
                        "pais": "$_id.pais"
                    },
                    redes: {$push: {label: "$_id.provider", value: "$value"}},
                    value: {$sum: "$value"} 
                }
            },
            {$sort: {"_id.pais": 1}} 
        ], function(err, resultado) {
            if (err) {
                res.send(err);
            }
            else {
                res.status(200).json(resultado);
            }
        }
    );
});

module.exports = router;

