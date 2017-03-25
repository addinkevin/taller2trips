var mongoose = require('mongoose');

var ciudadSchema = mongoose.Schema({
    // mongoose automaticamente agrega el campo id que corresponde al _id de mongodb
    nombre: String,
    descripcion: String,
    pais: String,
    foto: {type: String, default: ""}
});

module.exports = mongoose.model('Ciudad', ciudadSchema);
