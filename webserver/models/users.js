var mongoose = require('mongoose');

var userSchema = mongoose.Schema({
    username: String,
    password: String,
    email:    String,
    nombre:   String,
    apellido: String,
    bloqueado: Boolean
});

userSchema.methods.validPassword = function(password) {
    return password == this.password;
};

module.exports = mongoose.model('User', userSchema);
