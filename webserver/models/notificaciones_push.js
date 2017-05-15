var mongoose = require('mongoose');
var Schema = mongoose.Schema;

var notificacionPushSchema = mongoose.Schema({
    // mongoose automaticamente agrega el campo id que corresponde al _id de mongodb
    nombre: String,
    descripcion: {
        en: String,
        es: String,
        pt: String
    },
    id_ciudad: {type: Schema.Types.ObjectId, ref: 'Ciudad'},
    link: String,
    fecha: {
        type: String,
        default: ""
    },
    hora: {
        type: String,
        default: ""
    },
    enviada: {
        type: Boolean,
        default: false
    }
});

module.exports = mongoose.model('NotificacionPush', notificacionPushSchema);
