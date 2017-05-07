var mongoose = require('mongoose');

var recorridoSchema = mongoose.Schema({
    // mongoose automaticamente agrega el campo id que corresponde al _id de mongodb
    nombre: String,
    ids_atracciones: [String],
    id_ciudad: String
});

module.exports = mongoose.model('Recorrido', recorridoSchema);
