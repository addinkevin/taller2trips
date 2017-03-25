var express = require('express');
var constants = require('../config/constants');
var router = express.Router();
var Ciudad = require('../models/ciudades');

router.get('/ciudad', function(req, res) {
    Ciudad.find(function (err, ciudades) {
        if (err) {
            res.send(err);
        }
        else {   
            res.status(200).json(ciudades);
        }
    })
});

router.get('/ciudad/:id_ciudad', function(req, res) {
    Ciudad.findById(req.params.id_ciudad, function(err, ciudad) {
        if (err) {
            res.send(err);
        }
        else if (ciudad === null) {
            res.status(404).json({"msj": "ciudad no encontrada"});
        }
        else {
            res.status(200).json(ciudad);
        }
    });
});
        

router.post('/ciudad', function(req, res) {
    console.log(req.body);

    var ciudad = new Ciudad({
        nombre: req.body.nombre,
        descripcion: req.body.descripcion,
        pais: req.body.pais
    });

    ciudad.save(function(err, ciudad) {
        if (err) {
            res.status(405).json({"msj": "input invalido"});
        }
        else {
            res.status(201).json(ciudad);
        }
    });

});

router.put('/ciudad', function(req, res) {
    var ciudad = {
        nombre: req.body.nombre,
        descripcion: req.body.descripcion,
        pais: req.body.pais,
        foto: req.body.foto
    }

    Ciudad.update({_id: req.body._id}, ciudad, function (err) {
        if (err) {
            res.send(err);
        }
        else {
            res.status(200).json({"msj": "exito"})
        }
    });
});

router.delete('/ciudad/:id_ciudad', function(req,res) {
    Ciudad.remove({_id: req.params.id_ciudad}, function (err) {
        if (err) {
            res.send(err)
        }
        else {
            res.status(200).json({"msj": "exito"});
        }
    });
});

module.exports = router;
