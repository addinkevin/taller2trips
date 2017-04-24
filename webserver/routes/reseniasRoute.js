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
    var query = req.query
	var cantidad = req.headers["cantidad"];
	var salto = req.headers["salto"];
	
	if(cantidad && salto && !isNaN(cantidad) && !isNaN(salto)) { 
		if (req.query.descripcion !== undefined) query.descripcion = new RegExp(req.query.descripcion);
        if (req.query.nombre_ciudad !== undefined) query.nombre_ciudad = new RegExp(req.query.nombre_ciudad);
        if (req.query.descripcion !== undefined) query.nombre_atraccion = new RegExp(req.query.nombre_atraccion);
		console.log(query);
		Resenia.find(query, function(err, resenias) {
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

router.get('/resenia/buscar', function(req, res) {
    var query = req.query;
    if (req.query.descripcion !== undefined) query.descripcion = new RegExp(req.query.descripcion);
    if (req.query.nombre_ciudad !== undefined) query.nombre_ciudad = new RegExp(req.query.nombre_ciudad);
    if (req.query.descripcion !== undefined) query.nombre_atraccion = new RegExp(req.query.nombre_atraccion);
    Resenia.find(query, function(err, resenias) {
        if (err) {
            res.send(err);
        }
        else {
            res.status(200).json(resenias);
        }
    })
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
    var nombre_ciudad = "";
    var nombre_atraccion = "";

    Ciudad.findById(req.body.id_ciudad, function(err, ciudad) {
       if (err) {
           res.status(405).json({"msj": "input invalido"});
       }
       else if (ciudad === null) {
           res.status(404).json({"msj": "ciudad no encontrada"});
       }
       else {
           nombre_ciudad = ciudad.nombre;
           Atraccion.findById(req.body.id_atraccion, function(err, atraccion) {
               if (err) {
                   res.status(405).json({"msj": "input invalido"});
               }
               else if (atraccion === null) {
                   res.status(404).json({"msj": "atraccion no encontrada"});
               }
               else {
                   nombre_atraccion = atraccion.nombre;
                   var resenia = new Resenia({
                       id_usuario: req.body.id_usuario,
                       descripcion: req.body.descripcion,
                       id_ciudad: req.body.id_ciudad,
                       nombre_ciudad: nombre_ciudad,
                       id_atraccion: req.body.id_atraccion,
                       nombre_atraccion: nombre_atraccion,
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
               }
           });
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
