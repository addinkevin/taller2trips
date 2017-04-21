var mongoose = require('mongoose');

var atraccionSchema = mongoose.Schema({
    // mongoose automaticamente agrega el campo id que corresponde al _id de mongodb
    nombre: String,
    descripcion: {
        es: String,
        en: String,
        pt: String
    }
    id_ciudad: String,
    costo_monto: Number,
    costo_moneda: String,
    rating: {
        type: Number,
        default: 0
    },
    cantidad_votos: {
        type: Number,
        default: 0
    },
    hora_apertura: String,
    hora_cierre: String,
    duracion: Number,
    clasificacion: String,
    latitud: Number,
    longitud: Number,
    idiomas_audio: [String],
    imagenes: [String]
});

module.exports = mongoose.model('Atraccion', atraccionSchema);
