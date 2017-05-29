var express = require('express');
var fs = require('fs');
var constants = require('../config/constants');
var router = express.Router();
var Punto = require('../models/puntos');
var almacen = require('../utils/helperAlmacenamiento');
var normalizar = require('../utils/normalizar');

router.get('/punto', function(req, res) {
    Punto.find(function (err, puntos) {
        if (err) {
            res.send(err);
        }
        else {   
            res.status(200).json(puntos);
        }
    })
});

router.get('/punto/:id_punto', function(req, res) {
    Punto.findById(req.params.id_punto, function(err, punto) {
        if (err) {
            res.send(err);
        }
        else if (punto === null) {
            res.status(404).json({"msj": "punto no encontrada"});
        }
        else {
            res.status(200).json(punto);
        }
    });
});
        

router.post('/punto', function(req, res) {
    req.body.descripcion = JSON.parse(req.body.descripcion);

    var punto = new Punto({
        nombre: normalizar.nombre(req.body.nombre),
        descripcion: req.body.descripcion,
        id_atraccion: req.body.id_atraccion
    });

    punto.save(function(err, punto) {
        if (err) {
            res.status(405).json({"msj": "input invalido"});
        }
        else {
            res.status(201).json(punto);
        }
    });

});

router.put('/punto', function(req, res) {
    req.body.descripcion = JSON.parse(req.body.descripcion);
    
    var punto = {
        nombre: normalizar.nombre(req.body.nombre),
        descripcion: req.body.descripcion,
        id_atraccion: req.body.id_atraccion
    }

    Punto.update({_id: req.body._id}, punto, function (err) {
        if (err) {
            res.send(err);
        }
        else {
            res.status(200).json({"msj": "exito"})
        }
    });
});

router.delete('/punto/:id_punto', function(req,res) {
    Punto.remove({_id: req.params.id_punto}, function (err) {
        if (err) {
            res.send(err)
        }
        else {
            res.status(200).json({"msj": "exito"});
            almacen.borrarMediaPuntos(req.params.id_punto);
        }
    });
});

//TODO: poner todo esto en iniciazacion de aplicacion
almacen.crearDirectorioVideosPuntos();
almacen.crearDirectorioAudiosPuntos();
almacen.crearDirectorioImagenesPuntos();

router.post('/punto/:id_punto/video', almacen.uploadVideosPuntos.single("video"), function(req, res) {
    res.status(200).json({"msj": "exito"});
});

router.get('/punto/:id_punto/video', function(req, res) {
    var file = constants.dirVideosPuntos + req.params.id_punto + "_video.mp4";
    res.download(file);
});

router.delete('/punto/:id_punto/video', function(req, res) {
    var file = constants.dirVideosPuntos + req.params.id_punto + "_video.mp4";
    fs.unlink(file, function(err) {
        if (err) {
            res.status(404).json({"msj": "Video no encontrado"});
        }
        else {
            res.status(200).json({"msj": "exito"});
        }
    });
});
    

router.post('/punto/:id_punto/audio', almacen.uploadAudiosPuntos.single("audio"), function(req, res) {
    Punto.findById(req.params.id_punto, function(err, punto) {
        if (err) {
            res.send(err)
        }
        else if (punto === null) {
            res.status(404).json({"msj": "punto no encontrada"})
        }
        else {
            //TODO: Revisar uniqueness del idioma antes de pushear al array
            punto.idiomas_audio.push(req.body.idioma.toLowerCase());
            punto.save();
            res.status(200).json({"msj": "exito"});
        }
    });
});

router.get('/punto/:id_punto/audio', function(req, res) {
    var file = constants.dirAudiosPuntos + req.params.id_punto + "_audio_" + req.query.idioma.toLowerCase() + ".mp3";
    res.download(file);
});

router.delete('/punto/:id_punto/audio', function(req, res) {
    var file = constants.dirAudiosPuntos + req.params.id_punto + "_audio_" + req.query.idioma.toLowerCase() + ".mp3";
    fs.unlink(file, function(err) {
        if (err) {
            res.status(404).json({"msj": "Audio no encontrado"});
        }
        else {
            Punto.findById(req.params.id_punto, function(err, punto) {
                if (err) {
                    throw (err)
                }
                else {
                    var index = punto.idiomas_audio.indexOf(req.query.idioma);
                    if (index !== -1) {
                        punto.idiomas_audio.splice(index, 1);
                        punto.save();
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


router.post('/punto/:id_punto/imagen', almacen.uploadImagenesPuntos.single("imagen"), function(req, res) {
    Punto.findById(req.params.id_punto, function(err, punto) {
        if (err) {
            res.send(err)
        }
        else if (punto === null) {
            res.status(404).json({"msj": "punto no encontrada"})
        }
        else {
            punto.imagenes.push(req.file.filename);
            punto.save();
            res.status(200).json({"msj": "exito"});
        }
    });
});

router.get('/punto/:id_punto/imagen', function(req, res) {
    var file = constants.dirImagenesPuntos + req.query.filename;
    res.download(file);
});

router.delete('/punto/:id_punto/imagen', function(req, res) {
    var file = constants.dirImagenesPuntos + req.query.filename;
    fs.unlink(file, function(err) {
        if (err) {
            res.status(404).json({"msj": "Imagen no encontrada"});
        }
        else {
            Punto.findById(req.params.id_punto, function(err, punto) {
                if (err) {
                    throw (err)
                }
                else {
                    var index = punto.imagenes.indexOf(req.query.filename);
                    if (index !== -1) {
                        punto.imagenes.splice(index, 1);
                        punto.save();
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
