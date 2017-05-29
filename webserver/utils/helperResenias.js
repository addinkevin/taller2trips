var Resenia = require('../models/resenia');

exports.borrarReseniasAsociadaAtraccion = function(atraccion) {
    Resenia.remove({id_atraccion: atraccion}, function(err) {
        if (err) {
            console.log(err);
        }
        else {
            console.log("BORRADA");
        }
    });
};

