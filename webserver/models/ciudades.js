var mongoose = require('mongoose');

var ciudadSchema = mongoose.Schema({
    name: String,
    center: {
        lat: String,
        lng: String
    },
    radio: String
});

module.exports = mongoose.model('Ciudad', ciudadSchema);