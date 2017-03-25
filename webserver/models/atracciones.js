var mongoose = require('mongoose');

var atraccionSchema = mongoose.Schema({
    // mongoose automaticamente agrega el campo id que corresponde al _id de mongodb
    nombre: String,
    descripcion: String,
    id_ciudad: ObjectId,
    costo: Number,
    rating: {
        type: Number,
        default: 0
    }
    cantidad_votos {
        type: Number,
        default: 0
    }
    hora_apertura: Date,
    hora_cierre: Date,
    duracion: String,
    clasificacion: String,
    latitud: Number,
    longitud Number
});

module.exports = mongoose.model('Atraccion', atraccionSchema);
