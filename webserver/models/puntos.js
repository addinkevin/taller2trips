var mongoose = require('mongoose');
var Schema = mongoose.Schema;

var puntoSchema = mongoose.Schema({
    // mongoose automaticamente agrega el campo id que corresponde al _id de mongodb
    nombre: String,
    descripcion: {
        es: String,
        en: String,
        pt: String
    },
    id_atraccion: {type: Schema.Types.ObjectId, ref: 'Atraccion'},
    idiomas_audio: [String],
    imagenes: [String]
});

module.exports = mongoose.model('Punto', puntoSchema);
