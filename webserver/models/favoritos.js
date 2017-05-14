var mongoose = require('mongoose');
var Schema = mongoose.Schema;
var favoritoSchema = mongoose.Schema({
    // mongoose automaticamente agrega el campo id que corresponde al _id de mongodb
    id_usuario: {type: Schema.Types.ObjectId, ref: 'User'},
    id_ciudad: {type: Schema.Types.ObjectId, ref: 'Ciudad'},
    id_atraccion: {type: Schema.Types.ObjectId, ref: 'Atraccion'},
    id_recorrido: {type: Schema.Types.ObjectId, ref: 'Recorrido'}
});

module.exports = mongoose.model('Favorito', favoritoSchema);
