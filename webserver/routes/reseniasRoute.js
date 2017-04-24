var express = require('express');
var router = express.Router();
var Resenia = require('../models/resenia');
var Ciudad = require('../models/ciudades');
var Atraccion = require('../models/atracciones');

router.get('/resenia', function(req, res) {
    Resenia.find(function (err, resenias) {
        if (err) {
            res.send(err);
        }
        else {
            res.status(200).json(resenias);
        }
    })
});

router.get('/resenia/paginas', function(req, res) {
	var query = req.query;
	var cantidad = req.headers["cantidad"];
	var salto = req.headers["salto"];
	
	if(cantidad && salto && !isNaN(cantidad) && !isNaN(salto)) { 
		Resenia.find(function (err, resenias) {
			if (err) {
				res.send(err);
			}
			else {
				res.status(200).json(resenias);
			}
		}).sort({"created_at": 1})
		.limit(Number(cantidad))
		.skip(Number(salto));
	} else {
		res.status(400).json({"msj": "param err"});
	}
});

router.get('/resenia/buscar/paginas', function(req, res) {
    var query = req.query;
	var cantidad = req.headers["cantidad"];
	var salto = req.headers["salto"];
	
	if(cantidad && salto && !isNaN(cantidad) && !isNaN(salto)) { 
		if (req.query.descripcion !== undefined) query.descripcion = new RegExp(req.query.descripcion, 'i');
        var matchNombreCiudad, matchNombreAtraccion;
        if (query.nombre_ciudad !== undefined) {
            matchNombreCiudad = new RegExp(req.query.nombre_ciudad, 'i');
            delete query.nombre_ciudad;
        } else {
            matchNombreCiudad = new RegExp('.*');
        }

        if (query.nombre_atraccion !== undefined) {
            matchNombreAtraccion = new RegExp(req.query.nombre_atraccion, 'i');
            delete query.nombre_atraccion;
        } else {
            matchNombreAtraccion = new RegExp('.*');
        }

        Resenia
            .find(query)
            .populate( {
                path: 'id_atraccion',
                select: 'nombre',
                match: { 'nombre': matchNombreAtraccion }
            })
            .populate( {
                path: 'id_ciudad',
                select: 'nombre',
                match: { 'nombre': matchNombreCiudad }
            })
            .sort({"created_at": 1})
            .limit(Number(cantidad))
            .skip(Number(salto))
            .exec(function(err, resenias) {
                if (err) {
                    res.send(err);
                }
                else {
                    resenias = resenias.filter(function(resenia) {
                        return resenia.id_atraccion && resenia.id_ciudad;
                    });
                    res.status(200).json(resenias);
                }
            })


	} else {
	res.status(400).json({"msj": "param err"});
	}
});

router.get('/resenia/buscar', function(req, res) {
    var query = req.query;
    if (req.query.descripcion !== undefined) query.descripcion = new RegExp(req.query.descripcion, 'i');

    var matchNombreCiudad, matchNombreAtraccion;
    if (query.nombre_ciudad !== undefined) {
        matchNombreCiudad = new RegExp(req.query.nombre_ciudad, 'i');
        delete query.nombre_ciudad;
    } else {
        matchNombreCiudad = new RegExp('.*');
    }

    if (query.nombre_atraccion !== undefined) {
        matchNombreAtraccion = new RegExp(req.query.nombre_atraccion, 'i');
        delete query.nombre_atraccion;
    } else {
        matchNombreAtraccion = new RegExp('.*');
    }

    Resenia
        .find(query)
        .populate( {
            path: 'id_atraccion',
            select: 'nombre',
            match: { 'nombre': matchNombreAtraccion }
        })
        .populate( {
            path: 'id_ciudad',
            select: 'nombre',
            match: { 'nombre': matchNombreCiudad }
        })
        .exec(function(err, resenias) {
            if (err) {
                res.send(err);
            }
            else {
                resenias = resenias.filter(function(resenia) {
                    return resenia.id_atraccion && resenia.id_ciudad;
                });
                res.status(200).json(resenias);
            }
        });
});

router.get('/resenia/:id_resenia', function(req, res) {
    Resenia.findById(req.params.id_resenia, function(err, resenia) {
        if (err) {
            res.send(err);
        }
        else if (resenia === null) {
            res.status(404).json({"msj": "resenia no encontrada"});
        }
        else {
            res.status(200).json(resenia);
        }
    });
});


router.post('/resenia', function(req, res) {
    var resenia = new Resenia({
        id_usuario: req.body.id_usuario,
        descripcion: req.body.descripcion,
        id_ciudad: req.body.id_ciudad,
        id_atraccion: req.body.id_atraccion,
        id_userSocial: req.body.id_userSocial,
        provider: req.body.provider,
        calificacion: req.body.calificacion,
        idioma: req.body.idioma
    });

    resenia.save(function(err, resenia) {
        if (err) {
            res.status(405).json({"msj": "input invalido"});
        }
        else {
            res.status(201).json(resenia);
        }
    });
});

router.delete('/resenia/:id_resenia', function(req,res) {
    Resenia.remove({_id: req.params.id_resenia}, function (err) {
        if (err) {
            res.send(err)
        }
        else {
            res.status(200).json({"msj": "exito"});
        }
    });
});

module.exports = router;
