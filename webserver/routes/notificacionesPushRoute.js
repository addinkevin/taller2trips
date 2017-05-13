var express = require('express');
var constants = require('../config/constants');
var router = express.Router();
var NotificacionPush = require('../models/notificaciones_push');
var almacen = require('../utils/helperAlmacenamiento');

router.get('/push', function(req, res) {
    NotificacionPush.find(function (err, notificaciones) {
        if (err) {
            res.send(err);
        }
        else {
            res.status(200).json(notificaciones);
        }
    })
});

router.get('/push/:id_push', function(req, res) {
    NotificacionPush.findById(req.params.id_push, function(err, push) {
        if (err) {
            res.status(405).json({"msj": "input invalido"});
        }
        else if (push === null) {
            res.status(404).json({"msj": "push no encontrada"});
        }
        else {
            res.status(200).json(push);
        }
    });
});


router.post('/push', function(req, res) {
    req.body.descripcion = JSON.parse(req.body.descripcion);
    var push = new NotificacionPush({
        nombre: req.body.nombre,
        descripcion: req.body.descripcion,
        id_ciudad: req.body.id_ciudad,
        link: req.body.link,
        fecha: req.body.fecha,
        hora: req.body.hora
    });

    push.save(function(err, push) {
        if (err) {
            res.status(405).json({"msj": "input invalido"});
        }
        else {
            res.status(201).json(push);
        }
    });

});

router.put('/push', function(req, res) {
    req.body.descripcion = JSON.parse(req.body.descripcion);
    var push = {
        nombre: req.body.nombre,
        descripcion: req.body.descripcion,
        id_ciudad: req.body.id_ciudad,
        link: req.body.link,
        fecha: req.body.fecha,
        hora: req.body.hora
    };

    NotificacionPush.update({_id: req.body._id}, push, function (err) {
        if (err) {
            res.status(405).json({"msj": "input invalido"});
        }
        else {
            res.status(200).json({"msj": "exito"})
        }
    });
});

router.delete('/push/:id_push', function(req,res) {
    NotificacionPush.remove({_id: req.params.id_push}, function (err) {
        if (err) {
            res.send(err)
        }
        else {
            res.status(200).json({"msj": "exito"});
        }
    });
});

//TODO: poner en inicializacion de aplicacion
almacen.crearDirectorioImagenesNotificacionesPush();

router.post('/push/:id_push/imagen', almacen.uploadImagenesNotificacionesPush.single("imagen"), function(req, res) {
    res.status(200).json({"msj": "exito"});
});

router.get('/push/:id_push/imagen', function(req, res) {
    var file = constants.dirImagenesNotificacionesPush + req.params.id_push + ".png";
    res.download(file);
});

module.exports = router;
