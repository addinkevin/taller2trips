var mongoose = require('mongoose');
var Schema = mongoose.Schema;
var visitadoSchema = mongoose.Schema({
    // mongoose automaticamente agrega el campo id que corresponde al _id de mongodb
    id_usuario: {type: Schema.Types.ObjectId, ref: 'User'},
    id_atraccion: {type: Schema.Types.ObjectId, ref: 'Atraccion'},
});

visitadoSchema.index({id_usuario: 1, id_atraccion: 1}, {unique: true});

module.exports = mongoose.model('Visitado', visitadoSchema);
