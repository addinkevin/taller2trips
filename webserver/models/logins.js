var mongoose = require('mongoose');

var loginSchema = mongoose.Schema({
    id_usuario: String,
    provider: String,
    pais: String,
    fecha: {
        type: Date,
        default: Date.now
    }
});

module.exports = mongoose.model('Login', loginSchema);
