var Atraccion = require('../models/atracciones');

exports.actualizarCalificacion = function(id_atraccion, calificacion) {
    Atraccion.findById(id_atraccion, function(err, atraccion) {
        if (err) console.log(err);
        else if (atraccion === null) console.log("Atraccion con id " + id_atraccion + " no existe");
        else {
            console.log(atraccion);
            atraccion.cantidad_votos = atraccion.cantidad_votos + 1;
            atraccion.rating = atraccion.rating + (calificacion - atraccion.rating) / atraccion.cantidad_votos;
            atraccion.save();
        }
    });
};

