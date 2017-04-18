var constants = require('./constants');
var multer = require('multer');
var mkdirp = require('mkdirp');
var fs = require('fs');
var glob = require('glob');

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

exports.crearDirectorioImagenesAtracciones = function() {
    mkdirp(constants.dirImagenesAtracciones, function(err) {
        if (err) {
            console.log(err);
        }
    });
};

exports.crearDirectorioImagenesCiudad = function() {
    mkdirp(constants.dirImagenesCiudad, function(err) {
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
        cb(null, req.params.id_atraccion + "_audio_" + req.body.idioma + ".mp3")
    }
});

var storageImagenesAtracciones = multer.diskStorage({
    destination: function(req, file, cb) {
        cb(null, constants.dirImagenesAtracciones)
    },
    filename: function(req, file, cb) {
        console.log(req.body);
        cb(null, req.params.id_atraccion + "_imagen_" + Date.now() + ".png")
    }
});

var storageImagenesCiudad = multer.diskStorage({
    destination: function(req, file, cb) {
        cb(null, constants.dirImagenesCiudad)
    },
    filename: function(req, file, cb) {
        cb(null, req.params.id_ciudad + ".png")
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

exports.uploadImagenesAtracciones = multer({
    storage: storageImagenesAtracciones
});

exports.uploadImagenesCiudad = multer({
    storage: storageImagenesCiudad
});

exports.borrarMediaAtracciones = function(idAtraccion) {
    fs.unlink(constants.dirVideosAtracciones + idAtraccion + "_video.mp4", function(err) {
             if (err) console.log(err)} );
    fs.unlink(constants.dirPlanosAtracciones + idAtraccion + "_plano.png", function(err) {
             if (err) console.log(err)} );
    glob.glob(constants.dirAudiosAtracciones + idAtraccion + "*", function(err, files) {
        for (var i = 0; i < files.length; i++) {
            fs.unlink(files[i], function(err) {
                if (err) console.log(err)} );
        }
    });
    glob.glob(constants.dirImagenesAtracciones + idAtraccion + "*", function(err, files) {
        for (var i = 0; i < files.length; i++) {
            fs.unlink(files[i], function(err) {
                     if (err) console.log(err)} );
        }
    });
};

