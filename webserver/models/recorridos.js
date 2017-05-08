var mongoose = require('mongoose');
var Schema = mongoose.Schema;
var recorridoSchema = mongoose.Schema({
    // mongoose automaticamente agrega el campo id que corresponde al _id de mongodb
    nombre: String,
    ids_atracciones: [{ type: Schema.Types.ObjectId, ref: 'Atraccion'}],
    descripcion: {
        en: String,
        es: String,
        pt: String
    },
    id_ciudad: {type: Schema.Types.ObjectId, ref: 'Ciudad' }
});

module.exports = mongoose.model('Recorrido', recorridoSchema);
