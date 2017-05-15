var express = require('express');
var fs = require('fs');
var constants = require('../config/constants');
var router = express.Router();
var Atraccion = require('../models/atracciones');
var almacen = require('../utils/helperAlmacenamiento');
var helperAtracciones = require('../utils/helperAtracciones');
var haversine = require('../utils/haversine');

router.get('/atraccion', function(req, res) {
    //TODO: Ver como arreglar este horror de codigo
    var id_ciudad = req.query.id_ciudad;
    if (id_ciudad !== undefined) {
        Atraccion.find({id_ciudad: req.query.id_ciudad}, function (err, atracciones) {
            if (err) {
                res.send(err);
            }
            else {   
                res.status(200).json(atracciones);
            }
        })
    }
    else {
        Atraccion.find(function (err, atracciones) {
            if (err) {
                res.send(err);
            }
            else {   
                res.status(200).json(atracciones);
            }
        })
    }
});

router.get('/atraccion/cercania', function(req, res) {
    Atraccion.find(function (err, atracciones) {
        if (err) {
            res.send(err);
        }
        else {
           var atraccionesCercanas = [];
           for (var i = 0; i < atracciones.length; i++) {
               distanciaAtraccionUsuario = haversine.calcularDistancia(req.query.latitud, req.query.longitud, 
                                                                       atracciones[i].latitud, atracciones[i].longitud);
               if (distanciaAtraccionUsuario <= req.query.radio) {
                   atraccionesCercanas.push(atracciones[i]);
               }
           }
           res.status(200).json(atraccionesCercanas);
        }
    });
});


router.get('/atraccion/:id_atraccion', function(req, res) {
    Atraccion.findById(req.params.id_atraccion, function(err, atraccion) {
        if (err) {
            res.send(err);
        }
        else if (atraccion === null) {
            res.status(404).json({"msj": "atraccion no encontrada"});
        }
        else {
            var idioma = req.headers["idioma"];
            atraccion.clasificacion = helperAtracciones.obtenerClasificacion(atraccion.clasificacion, idioma);
            res.status(200).json(atraccion);
        }
    });
});
        

router.post('/atraccion', function(req, res) {
    req.body.descripcion = JSON.parse(req.body.descripcion);
    var atraccion = new Atraccion({
        nombre: req.body.nombre,
        descripcion: req.body.descripcion,
        id_ciudad: req.body.id_ciudad,
        costo_monto: req.body.costo_monto,
        costo_moneda: req.body.costo_moneda,
        hora_apertura: req.body.hora_apertura,
        hora_cierre: req.body.hora_cierre,
        duracion: req.body.duracion,
        clasificacion: req.body.clasificacion,
        latitud: req.body.latitud,
        longitud: req.body.longitud
    });

    atraccion.save(function(err, atraccion) {
        if (err) {
            res.status(405).json({"msj": "input invalido"});
        }
        else {
            res.status(201).json(atraccion);
        }
    });

});

router.put('/atraccion', function(req, res) {
    req.body.descripcion = JSON.parse(req.body.descripcion);
    var atraccion = {
        nombre: req.body.nombre,
        descripcion: req.body.descripcion,
        id_ciudad: req.body.id_ciudad,
        costo_monto: req.body.costo_monto,
        costo_moneda: req.body.costo_moneda,
        hora_apertura: req.body.hora_apertura,
        hora_cierre: req.body.hora_cierre,
        duracion: req.body.duracion,
        clasificacion: req.body.clasificacion,
        latitud: req.body.latitud,
        longitud: req.body.longitud
    }

    Atraccion.update({_id: req.body._id}, atraccion, function (err) {
        if (err) {
            res.send(err);
        }
        else {
            res.status(200).json({"msj": "exito"})
        }
    });
});

router.delete('/atraccion/:id_atraccion', function(req,res) {
    Atraccion.remove({_id: req.params.id_atraccion}, function (err) {
        if (err) {
            res.send(err)
        }
        else {
            res.status(200).json({"msj": "exito"});
            almacen.borrarMediaAtracciones(req.params.id_atraccion);
        }
    });
});

//TODO: poner todo esto en iniciazacion de aplicacion
almacen.crearDirectorioPlanosAtracciones();
almacen.crearDirectorioVideosAtracciones();
almacen.crearDirectorioAudiosAtracciones();
almacen.crearDirectorioImagenesAtracciones();

router.post('/atraccion/:id_atraccion/plano', almacen.uploadPlanosAtracciones.single("plano"), function(req, res) {
    res.status(200).json({"msj": "exito"});
});

router.get('/atraccion/:id_atraccion/plano', function(req, res) {
    var file = constants.dirPlanosAtracciones + req.params.id_atraccion + "_plano.png";
    res.download(file);
});

router.delete('/atraccion/:id_atraccion/plano', function(req, res) {
    var file = constants.dirPlanosAtracciones + req.params.id_atraccion + "_plano.png";
    fs.unlink(file, function(err) {
        if (err) {
            res.status(404).json({"msj": "Plano no encontrado"});
        }
        else {
            res.status(200).json({"msj": "exito"});
        }
    });
});

router.post('/atraccion/:id_atraccion/video', almacen.uploadVideosAtracciones.single("video"), function(req, res) {
    res.status(200).json({"msj": "exito"});
});

router.get('/atraccion/:id_atraccion/video', function(req, res) {
    var file = constants.dirVideosAtracciones + req.params.id_atraccion + "_video.mp4";
    res.download(file);
});

router.delete('/atraccion/:id_atraccion/video', function(req, res) {
    var file = constants.dirVideosAtracciones + req.params.id_atraccion + "_video.mp4";
    fs.unlink(file, function(err) {
        if (err) {
            res.status(404).json({"msj": "Video no encontrado"});
        }
        else {
            res.status(200).json({"msj": "exito"});
        }
    });
});
    

router.post('/atraccion/:id_atraccion/audio', almacen.uploadAudiosAtracciones.single("audio"), function(req, res) {
    Atraccion.findById(req.params.id_atraccion, function(err, atraccion) {
        if (err) {
            res.send(err)
        }
        else if (atraccion === null) {
            res.status(404).json({"msj": "atraccion no encontrada"})
        }
        else {
            //TODO: Revisar uniqueness del idioma antes de pushear al array
            atraccion.idiomas_audio.push(req.body.idioma.toLowerCase());
            atraccion.save();
            res.status(200).json({"msj": "exito"});
        }
    });
});

router.get('/atraccion/:id_atraccion/audio', function(req, res) {
    var file = constants.dirAudiosAtracciones + req.params.id_atraccion + "_audio_" + req.query.idioma.toLowerCase() + ".mp3";
    res.download(file);
});

router.delete('/atraccion/:id_atraccion/audio', function(req, res) {
    var file = constants.dirAudiosAtracciones + req.params.id_atraccion + "_audio_" + req.query.idioma.toLowerCase() + ".mp3";
    fs.unlink(file, function(err) {
        if (err) {
            res.status(404).json({"msj": "Audio no encontrado"});
        }
        else {
            Atraccion.findById(req.params.id_atraccion, function(err, atraccion) {
                if (err) {
                    throw (err)
                }
                else {
                    var index = atraccion.idiomas_audio.indexOf(req.query.idioma);
                    if (index !== -1) {
                        atraccion.idiomas_audio.splice(index, 1);
                        atraccion.save();
                        res.status(200).json({"msj": "exito"});
                    }
                    else {
                        res.status(404).json({"msj": "Audio no encontrada"});
                    }
                }
            });
        }
    });
});


router.post('/atraccion/:id_atraccion/imagen', almacen.uploadImagenesAtracciones.single("imagen"), function(req, res) {
    Atraccion.findById(req.params.id_atraccion, function(err, atraccion) {
        if (err) {
            res.send(err)
        }
        else if (atraccion === null) {
            res.status(404).json({"msj": "atraccion no encontrada"})
        }
        else {
            atraccion.imagenes.push(req.file.filename);
            atraccion.save();
            res.status(200).json({"msj": "exito"});
        }
    });
});

router.get('/atraccion/:id_atraccion/imagen', function(req, res) {
    var file = constants.dirImagenesAtracciones + req.query.filename;
    res.download(file);
});

router.delete('/atraccion/:id_atraccion/imagen', function(req, res) {
    var file = constants.dirImagenesAtracciones + req.query.filename;
    fs.unlink(file, function(err) {
        if (err) {
            res.status(404).json({"msj": "Imagen no encontrada"});
        }
        else {
            Atraccion.findById(req.params.id_atraccion, function(err, atraccion) {
                if (err) {
                    throw (err)
                }
                else {
                    var index = atraccion.imagenes.indexOf(req.query.filename);
                    if (index !== -1) {
                        atraccion.imagenes.splice(index, 1);
                        atraccion.save();
                        res.status(200).json({"msj": "exito"});
                    }
                    else {
                        res.status(404).json({"msj": "Imagen no encontrada"});
                    }
                }
            });
        }
    });
});

module.exports = router;
