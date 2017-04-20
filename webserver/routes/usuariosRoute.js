var express = require('express');
var router = express.Router();
var User = require('../models/users');

router.get('/usuario', function(req, res) {
    User.find(function (err, usuarios) {
        if (err) {
            res.send(err);
        }
        else {
            res.status(200).json(usuarios);
        }
    })
});

router.get('/usuario/:id_usuario', function(req, res) {
    User.findById(req.params.id_usuario, function(err, usuario) {
        if (err) {
            res.send(err);
        }
        else if (usuario === null) {
            res.status(404).json({"msj": "usuario no encontrado"});
        }
        else {
            res.status(200).json(usuario);
        }
    });
});


router.post('/usuario', function(req, res) {
    var usuario = new User({
        username: req.body.username,
        email: req.body.email,
        nombre: req.body.nombre,
        apellido: req.body.apellido
    });

    usuario.save(function(err, usuario) {
        if (err) {
            res.status(405).json({"msj": "input invalido"});
        }
        else {
            res.status(201).json(usuario);
        }
    });

});

router.put('/usuario', function(req, res) {
    var usuario = {
        username: req.body.username,
        email: req.body.email,
        nombre: req.body.nombre,
        apellido: req.body.apellido,
        bloqueado: req.body.bloqueado
    };

    User.update({_id: req.body._id}, usuario, function (err) {
        if (err) {
            res.send(err);
        }
        else {
            res.status(200).json({"msj": "exito"})
        }
    });
});

router.delete('/usuario/:id_usuario', function(req,res) {
    User.remove({_id: req.params.id_usuario}, function (err) {
        if (err) {
            res.send(err)
        }
        else {
            res.status(200).json({"msj": "exito"});
        }
    });
});

module.exports = router;
