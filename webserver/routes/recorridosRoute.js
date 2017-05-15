var express = require('express');
var router = express.Router();
var Recorrido = require('../models/recorridos');
var normalizar = require('../utils/normalizar');

router.get('/recorridoPopulate', function(req, res) {
    Recorrido
        .find({})
        .populate('ids_atracciones')
        .populate('id_ciudad')
        .exec(function(err, recorridos) {
            if (err) {
                res.send(err);
            }
            else {
                res.status(200).json(recorridos);
            }
        });
});

router.get('/recorridoPopulateFiltro', function(req, res) {
    var query = {};
    var matchNombreCiudad, matchNombreRecorrido;
    if (req.query.nombre_ciudad !== undefined) {
        matchNombreCiudad = new RegExp(req.query.nombre_ciudad, 'i');
    } else {
        matchNombreCiudad = new RegExp('.*');
    }

    if (req.query.nombre_recorrido !== undefined) {
        matchNombreRecorrido = new RegExp(req.query.nombre_recorrido, 'i');
    } else {
        matchNombreRecorrido = new RegExp('.*');
    }

    Recorrido
        .find({nombre: matchNombreRecorrido})
        .populate('ids_atracciones')
        .populate( {
            path: 'id_ciudad',
            select: 'nombre',
            match: { 'nombre': matchNombreCiudad }
        })
        .exec(function(err, recorridos) {
            if (err) {
                res.send(err);
            }
            else {
                recorridos = recorridos.filter(function(recorridos) {
                    return recorridos.id_ciudad;
                });
                res.status(200).json(recorridos);
            }
        });
});

router.get('/recorrido', function(req, res) {
    Recorrido.find(function (err, recorridos) {
        if (err) {
            res.send(err);
        }
        else {
            res.status(200).json(recorridos);
        }
    })
});

router.get('/recorrido/buscar', function(req, res) {
    var query = req.query;
    var cantidad = req.headers["cantidad"];
    var salto = req.headers["salto"];
	if(cantidad && salto && !isNaN(cantidad) && !isNaN(salto)) {
        Recorrido.
            find(query).
            sort({"_id": -1}).
            limit(Number(cantidad)).
            skip(Number(salto)).
            populate('ids_atracciones').
            populate('id_ciudad').
            exec(function(err, recorridos) {
                if (err) {
                    res.send(err);
                }
                else {
                    res.status(200).json(recorridos);
                }
            });
	} 
    else {
	    res.status(405).json({"msj": "input invalido"});
	}
});

router.get('/recorrido/:id_recorrido', function(req, res) {
    busqueda = Recorrido.
        find({_id: req.params.id_recorrido}).
        populate('ids_atracciones').
        populate('id_ciudad');
    busqueda.exec(function(err, recorrido) {
        if (err) {
            res.status(405).json({"msj": "input invalido"});
        }
        else if (recorrido === null) {
            res.status(404).json({"msj": "recorrido no encontrado"});
        }
        else {
            res.status(200).json(recorrido);
        }
    });
});


router.post('/recorrido', function(req, res) {
    req.body.descripcion = JSON.parse(req.body.descripcion);

    atracciones = req.body.ids_atracciones.split(",");
    var recorrido = new Recorrido({
        nombre: normalizar.nombre(req.body.nombre),
        ids_atracciones: atracciones,
        id_ciudad: normalizar.nombre(req.body.id_ciudad),
        descripcion: req.body.descripcion
    });

    recorrido.save(function(err, recorrido) {
        if (err) {
            res.status(405).json({"msj": "input invalido"});
        }
        else {
            res.status(201).json(recorrido);
        }
    });

});

router.put('/recorrido', function(req, res) {
    req.body.descripcion = JSON.parse(req.body.descripcion);
    atracciones = req.body.ids_atracciones.split(",");
    var recorrido = {
        nombre: normalizar.nombre(req.body.nombre),
        descripcion: req.body.descripcion,
        ids_atracciones: atracciones,
        id_ciudad: normalizar.nombre(req.body.id_ciudad)
    };

    Recorrido.update({_id: req.body._id}, recorrido, function (err) {
        if (err) {
            console.log(err);
            res.status(405).json({"msj": "input invalido"});
        }
        else {
            res.status(200).json({"msj": "exito"})
        }
    });
});

router.delete('/recorrido/:id_recorrido', function(req,res) {
    Recorrido.remove({_id: req.params.id_recorrido}, function (err) {
        if (err) {
            res.send(err)
        }
        else {
            res.status(200).json({"msj": "exito"});
        }
    });
});

module.exports = router;
