var request = require('request');

// TODO: Arreglar este infierno de funcion para que sea legible y soporte mas de un provider.
// los puntos de mejora son pasar de callbacks a promises, cambiar el lookup asumiendo un 
// solo resultado de Splex y validar match de provider en el request con el devuelto.
exports.procesarLogin = function(auth_token, req, res) {
    request.get("https://api.splex.rocks/social-accounts",
    {
        'auth': {
            'bearer': auth_token
        }
    },
    function(err, response, body) {
        //TODO: Ver si puede devolver un body vacio
        if (err) {
            res.json(err);
        }
        else {
            body = JSON.parse(body);
            console.log(body.data.socialAccounts[0].data);
            User.findOne(
                {
                    'ids_sociales': {$elemMatch: {
                        provider: body.data.socialAccounts[0].provider,
                        id_social: body.data.socialAccounts[0].data.user_id
                    }}
                }, function(err, usuario) {
                    if (err) {
                        res.json(err);
                    }
                    else {
                        if (usuario === null) {
                            var nuevo_usuario = new User({
                                email: req.body.email,
                                nombre: req.body.nombre,
                                apellido: req.body.apellido,
                                ids_sociales: [
                                    {
                                        provider: body.data.socialAccounts[0].provider,
                                        id_social: body.data.socialAccounts[0].data.user_id
                                    }
                                ]
                            });
                            nuevo_usuario.save();
                            res.status(201).json(nuevo_usuario);
                        }
                        else {
                            res.status(200).json(usuario);
                        }
                    }
                }
            );
        };
    });
}
