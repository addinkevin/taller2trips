var mongoose = require('mongoose');

var reseniaSchema = mongoose.Schema({
    // mongoose automaticamente agrega el campo id que corresponde al _id de mongodb
    id_usuario: String,
    descripcion: String,
    id_ciudad: String,
    id_atraccion: String,
	id_userSocial: String,
	provider: String,
    idioma: String,
    calificacion: Number
});

module.exports = mongoose.model('Resenia', reseniaSchema);
