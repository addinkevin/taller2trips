var express = require('express');
var constants = require('../config/constants');
var router = express.Router();
var Ciudad = require('../models/ciudades');

router.get('/ciudad', function(req, res) {
    Ciudad.find(function (err, ciudades) {
        if (err) {
            res.send(err);
        }
        res.status(200).json(ciudades);
    })
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


router.delete('/ciudades/:ciudad', function(req,res) {
    Ciudad.remove({
        _id : req.params.ciudad
    }, function(err, ciudad) {
        if (err) {
            res.send(err);
        }

        Ciudad.find(function (err, ciudades) {
            if (err) {
                res.send(err);
            }
            res.json(ciudades);
        });
    })
});

module.exports = router;
