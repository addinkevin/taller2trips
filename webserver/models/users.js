var mongoose = require('mongoose');

var userSchema = mongoose.Schema({
    username: String,
    password: String,
    email:    String,
    nombre:   String,
    apellido: String,
    ids_sociales: [
        {
            provider: String,
            id_social: String
        }
    ],
    bloqueado: {
        type: Boolean,
        default: false
    },
    token_push: {
        type: String,
        default: ""
    }
});

userSchema.methods.validPassword = function(password) {
    return password == this.password;
};

module.exports = mongoose.model('User', userSchema);
