var express = require('express');
var router = express.Router();
var Favorito = require('../models/favoritos');
var Ciudad = require('../models/ciudades');
var Atraccion = require('../models/atracciones');
var User = require('../models/users');
//TODO: Agregar recorrido cuando esten

router.get('/favorito/buscar', function(req, res) {
    var query = req.query;
	var cantidad = req.headers["cantidad"];
	var salto = req.headers["salto"];
    var tipo_busqueda = req.headers["tipo_busqueda"];
	
	if(cantidad && salto && !isNaN(cantidad) && !isNaN(salto)) {
        busqueda = Favorito.
            find(query).
            sort({"_id": -1}).
            limit(Number(cantidad)).
            skip(Number(salto))

        if (tipo_busqueda === "atracciones" || tipo_busqueda === "todos") busqueda.populate('id_atraccion');
        if (tipo_busqueda === "usuarios" || tipo_busqueda === "todos") busqueda.populate('id_usuario');
        if (tipo_busqueda === "ciudades" || tipo_busqueda === "todos") busqueda.populate('id_ciudad');
        //if (tipo_busqueda === "recorridos" || tipo_busqueda === "todos") busqueda.populate('id_recorrido');
            
        busqueda.exec(function(err, favoritos) {
                if (err) {
                    res.send(err);
                }
                else {
                    /*
                    resenias = resenias.filter(function(resenia) {
                        return resenia.id_atraccion && resenia.id_ciudad;
                    });
                   */
                    res.status(200).json(favoritos);
                }
            })

	} 
    else {
	    res.status(405).json({"msj": "input invalido"});
	}
});

router.post('/favorito', function(req, res) {
    User.findById(req.body.id_usuario, function(err, usuario) {
        if (err) res.status(405).json({"msj": "input invalido"});
        else if (usuario === null) res.status(404).json({"msj": "usuario no encontrado"});
        else {
            var favorito = new Favorito({
                id_usuario: req.body.id_usuario,
                id_ciudad: req.body.id_ciudad,
                id_atraccion: req.body.id_atraccion,
                id_recorrido: req.body.id_recorrido
            });

            favorito.save(function(err, favorito) {
                if (err) {
                    res.status(405).json({"msj": "input invalido"});
                }
                else {
                    res.status(201).json(favorito);
                }
            });
        }
    });
});

router.delete('/favorito/:id_favorito', function(req,res) {
    Favorito.remove({_id: req.params.id_favorito}, function (err) {
        if (err) {
            res.send(err)
        }
        else {
            res.status(200).json({"msj": "exito"});
        }
    });
});

module.exports = router;
