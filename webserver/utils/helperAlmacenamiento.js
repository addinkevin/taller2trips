var constants = require('../config/constants');
var multer = require('multer');
var mkdirp = require('mkdirp');
var fs = require('fs');
var glob = require('glob');

exports.crearDirectorioVideosPuntos = function() {
    mkdirp(constants.dirVideosPuntos, function(err) {
        if (err) {
            console.log(err);
        }
    });
};

exports.crearDirectorioAudiosPuntos = function() {
    mkdirp(constants.dirAudiosPuntos, function(err) {
        if (err) {
            console.log(err);
        }
    });
};

exports.crearDirectorioImagenesPuntos = function() {
    mkdirp(constants.dirImagenesPuntos, function(err) {
        if (err) {
            console.log(err);
        }
    });
};
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

exports.crearDirectorioImagenesNotificacionesPush = function() {
    mkdirp(constants.dirImagenesNotificacionesPush, function(err) {
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

var storageVideosPuntos = multer.diskStorage({
    destination: function(req, file, cb) {
        cb(null, constants.dirVideosPuntos)
    },
    filename: function(req, file, cb) {
        cb(null, req.params.id_punto + "_video.mp4")
    }
});

var storageAudiosPuntos = multer.diskStorage({
    destination: function(req, file, cb) {
        cb(null, constants.dirAudiosPuntos)
    },
    filename: function(req, file, cb) {
        cb(null, req.params.id_punto + "_audio_" + req.body.idioma + ".mp3")
    }
});

var storageImagenesPuntos = multer.diskStorage({
    destination: function(req, file, cb) {
        cb(null, constants.dirImagenesPuntos)
    },
    filename: function(req, file, cb) {
        cb(null, req.params.id_punto + "_imagen_" + Date.now() + ".png")
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
        cb(null, req.params.id_atraccion + "_audio_" + req.body.idioma + ".mp3")
    }
});

var storageImagenesAtracciones = multer.diskStorage({
    destination: function(req, file, cb) {
        cb(null, constants.dirImagenesAtracciones)
    },
    filename: function(req, file, cb) {
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

var storageImagenesNotificacionesPush = multer.diskStorage({
    destination: function(req, file, cb) {
        cb(null, constants.dirImagenesNotificacionesPush)
    },
    filename: function(req, file, cb) {
        cb(null, req.params.id_push + ".png")
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

exports.uploadVideosPuntos = multer({
    storage: storageVideosPuntos
});

exports.uploadAudiosPuntos = multer({
    storage: storageAudiosPuntos
});

exports.uploadImagenesPuntos = multer({
    storage: storageImagenesPuntos
});

exports.uploadImagenesCiudad = multer({
    storage: storageImagenesCiudad
});

exports.uploadImagenesNotificacionesPush = multer({
    storage: storageImagenesNotificacionesPush
});

exports.borrarMediaPuntos = function(idPunto) {
    fs.unlink(constants.dirVideosPuntos + idPunto + "_video.mp4", function(err) {
             if (err) console.log(err)} );
    glob.glob(constants.dirAudiosPuntos + idPunto + "*", function(err, files) {
        for (var i = 0; i < files.length; i++) {
            fs.unlink(files[i], function(err) {
                if (err) console.log(err)} );
        }
    });
    glob.glob(constants.dirImagenesPuntos + idPunto + "*", function(err, files) {
        for (var i = 0; i < files.length; i++) {
            fs.unlink(files[i], function(err) {
                     if (err) console.log(err)} );
        }
    });
};

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

