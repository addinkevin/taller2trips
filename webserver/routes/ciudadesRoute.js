var express = require('express');
var constants = require('../config/constants');
var router = express.Router();
var Ciudad = require('../models/ciudades');
var Atraccion = require('../models/atracciones');
var almacen = require('../utils/helperAlmacenamiento');
var normalizar = require('../utils/normalizar');

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
            res.status(405).json({"msj": "input invalido"});
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
    req.body.descripcion = JSON.parse(req.body.descripcion);
    var ciudad = new Ciudad({
        nombre: normalizar.nombre(req.body.nombre),
        pais: normalizar.nombre(req.body.pais),
        descripcion: req.body.descripcion
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
    req.body.descripcion = JSON.parse(req.body.descripcion);
    var ciudad = {
        nombre: normalizar.nombre(req.body.nombre),
        descripcion: normalizar.nombre(req.body.descripcion),
        pais: req.body.pais,
    };

    Ciudad.update({_id: req.body._id}, ciudad, function (err) {
        if (err) {
            console.log(err);
            res.status(405).json({"msj": "input invalido"});
            //res.send(err);
        }
        else {
            res.status(200).json({"msj": "exito"})
        }
    });
});

router.delete('/ciudad/:id_ciudad', function(req,res) {
    // Valido que no tenga atracciones relacionadas
    Atraccion.find({id_ciudad: req.params.id_ciudad}, function(err, atracciones) {
        if (err) {
            res.send(err);
        }
        else if (atracciones.length > 0) {
            res.status(409).json({"msj": "No se puede eliminar la ciudad, tiene atracciones relacionadas"});
        }
        else {
            Ciudad.remove({_id: req.params.id_ciudad}, function (err) {
                if (err) {
                    res.send(err)
                }
                else {
                    res.status(200).json({"msj": "exito"});
                }
            });
        }
    });
});

//TODO: poner en inicializacion de aplicacion
almacen.crearDirectorioImagenesCiudad();

router.post('/ciudad/:id_ciudad/imagen', almacen.uploadImagenesCiudad.single("imagen"), function(req, res) {
    res.status(200).json({"msj": "exito"});
});

router.get('/ciudad/:id_ciudad/imagen', function(req, res) {
    var file = constants.dirImagenesCiudad + req.params.id_ciudad + ".png";
    res.download(file);
});


module.exports = router;
