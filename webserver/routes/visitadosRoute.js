var express = require('express');
var router = express.Router();
var Visitado = require('../models/visitados');
var User = require('../models/users');

router.get('/visitado/buscar', function(req, res) {
    var query = req.query;
	var cantidad = req.headers["cantidad"];
	var salto = req.headers["salto"];
	
	if(cantidad && salto && !isNaN(cantidad) && !isNaN(salto)) {
        Visitado.
            find(query).
            sort({"_id": -1}).
            limit(Number(cantidad)).
            skip(Number(salto)).
			populate('id_atraccion').
            exec(function(err, visitados) {
                if (err) {
                    res.send(err);
                }
                else {
                    res.status(200).json(visitados);
                }
            });
	} 
    else {
	    res.status(405).json({"msj": "input invalido"});
	}
});

router.post('/visitado', function(req, res) {
    User.findById(req.body.id_usuario, function(err, usuario) {
        if (err) res.status(405).json({"msj": "input invalido"});
        else if (usuario === null) res.status(404).json({"msj": "usuario no encontrado"});
        else {
            var visitado = new Visitado({
                id_usuario: req.body.id_usuario,
                id_atraccion: req.body.id_atraccion,
            });

            visitado.save(function(err, visitado) {
                if (err) {
                    res.status(405).json({"msj": "input invalido"});
                }
                else {
                    res.status(201).json(visitado);
                }
            });
        }
    });
});

router.delete('/visitado/:id_visitado', function(req,res) {
    Visitado.remove({_id: req.params.id_visitado}, function (err) {
        if (err) {
            res.send(err)
        }
        else {
            res.status(200).json({"msj": "exito"});
        }
    });
});

module.exports = router;
