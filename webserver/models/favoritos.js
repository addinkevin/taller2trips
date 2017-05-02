var mongoose = require('mongoose');
var Schema = mongoose.Schema;
var favoritoSchema = mongoose.Schema({
    // mongoose automaticamente agrega el campo id que corresponde al _id de mongodb
    id_usuario: String,
    id_ciudad: String,
    id_atraccion: String,
    id_recorrido: String
});

module.exports = mongoose.model('Favorito', favoritoSchema);
