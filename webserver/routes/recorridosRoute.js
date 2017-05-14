var express = require('express');
var router = express.Router();
var Recorrido = require('../models/recorridos');

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

router.get('/recorrido/:id_recorrido', function(req, res) {
    busqueda = Recorrido.
        find({_id: req.params.id_recorrido}).
        populate('ids_atracciones')
        .populate('id_ciudad');
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
        nombre: req.body.nombre,
        ids_atracciones: atracciones,
        id_ciudad: req.body.id_ciudad,
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
        nombre: req.body.nombre,
        descripcion: req.body.descripcion,
        ids_atracciones: atracciones,
        id_ciudad: req.body.id_ciudad
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
