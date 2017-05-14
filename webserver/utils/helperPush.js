var admin = require('firebase-admin');
var User = require('../models/users');
var Favorito = require('../models/favoritos');


exports.inicializarMensajeriaGoogle = function() {
    var serviceAccount = require("../config/secrets/taller-trips-firebase-adminsdk-4jvcd-fc574636c1.json");
    admin.initializeApp({
          credential: admin.credential.cert(serviceAccount),
            databaseURL: "https://taller-trips.firebaseio.com/"
    });
}

var contieneElemento = function(array, valor) {
    return array.indexOf(valor) != -1;
}

var procesarQuery = function(res) {
    var i;
    var tokens = [];
    for (i = 0; i < res.length; i++) {
        var token = res[i].id_usuario.token_push;
        if (!contieneElemento(tokens, token) && token !== "") {
            tokens.push(token);
        };
    };
    return tokens;
}

var procesarNotificacion = function(tokens, push) {
    tokens = procesarQuery(tokens);
    var titulo = push.nombre;
    var notificacion = push.toObject();
    delete notificacion.id_ciudad;
    delete notificacion.fecha;
    delete notificacion.hora;
    delete notificacion.nombre;
    delete notificacion.__v;
    var payload = {
        notification: {
            title: titulo,
            body: JSON.stringify(notificacion) 
        }
    };
    var options = {};
    admin.messaging().sendToDevice(tokens, payload, options)
        .then(function(response) {
            console.log("Mensaje enviado exitosamente:", response);
        })
        .catch(function(error) {
            console.log("Error enviando mensaje:", error);
        });
};

exports.mandarNotificaciones = function(push) {
    Favorito.
        find({id_ciudad: push.id_ciudad}).
        select('id_usuario').
        populate('id_usuario', 'token_push').
        exec(
            function (err, tokens) {
                if (err) {
                    console.log(err);
                }
                else {
                    procesarNotificacion(tokens, push);
                }
            }
        );
};


