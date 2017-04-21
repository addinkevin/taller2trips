var mongoose = require('mongoose');

var ciudadSchema = mongoose.Schema({
    // mongoose automaticamente agrega el campo id que corresponde al _id de mongodb
    nombre: String,
    descripcion: {
        en: String,
        es: String,
        pt: String
    },
    pais: String
});

module.exports = mongoose.model('Ciudad', ciudadSchema);
