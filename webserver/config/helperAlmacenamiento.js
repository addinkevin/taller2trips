var constants = require('./constants');
var multer = require('multer');
var mkdirp = require('mkdirp');

exports.crearDirectorioPlanosAtracciones = function() {
    mkdirp(constants.dirPlanosAtracciones, function(err) {
        if (err) {
            console.log(err);
        }
    });
};

exports.crearDirectorioVideosAtracciones = function() {
    mkdirp(constants.dirVideosAtracciones, function(err) {
        if (err) {
            console.log(err);
        }
    });
};

exports.crearDirectorioAudiosAtracciones = function() {
    mkdirp(constants.dirAudiosAtracciones, function(err) {
        if (err) {
            console.log(err);
        }
    });
};

var storagePlanosAtracciones = multer.diskStorage({
    destination: function(req, file, cb) {
        cb(null, constants.dirPlanosAtracciones)
    },
    filename: function(req, file, cb) {
        cb(null, req.params.id_atraccion + "_plano.png")
    }
});

var storageVideosAtracciones = multer.diskStorage({
    destination: function(req, file, cb) {
        cb(null, constants.dirVideosAtracciones)
    },
    filename: function(req, file, cb) {
        cb(null, req.params.id_atraccion + "_video.mp4")
    }
});

var storageAudiosAtracciones = multer.diskStorage({
    destination: function(req, file, cb) {
        cb(null, constants.dirAudiosAtracciones)
    },
    filename: function(req, file, cb) {
        console.log(req.body);
        cb(null, req.params.id_atraccion + "_audio_" + req.body.idioma + ".mp4")
    }
});

exports.uploadPlanosAtracciones = multer({
    storage: storagePlanosAtracciones
});

exports.uploadVideosAtracciones = multer({
    storage: storageVideosAtracciones
});

exports.uploadAudiosAtracciones = multer({
    storage: storageAudiosAtracciones
});

