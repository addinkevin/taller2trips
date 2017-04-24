var mongoose = require('mongoose');
var Schema = mongoose.Schema;
var reseniaSchema = mongoose.Schema({
    // mongoose automaticamente agrega el campo id que corresponde al _id de mongodb
    id_usuario: String,
    descripcion: String,
    id_ciudad: {type: Schema.Types.ObjectId, ref: 'Ciudad' },
    id_atraccion: {type: Schema.Types.ObjectId, ref: 'Atraccion' },
    id_userSocial: String,
    provider: String,
	name: String,
    idioma: String,
    calificacion: Number
});

module.exports = mongoose.model('Resenia', reseniaSchema);
