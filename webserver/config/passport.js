var User = require('../models/users');
var Strategy = require('passport-local').Strategy;
var constants = require('./constants');

module.exports = function (passport) {
    passport.serializeUser(function(user, cb) {
        cb(null, user.id);
    });

    passport.deserializeUser(function(id, cb) {
        User.findById(id, function(err, user) {
            cb(err, user);
        });
    });

    passport.use('login', new Strategy({
            passReqToCallback : true
        },
        function(req, username, password, cb) {
            User.findOne({ 'username' :  username }, function(err, user) {
                if (err)
                    return cb(err);

                if (!user)
                    return cb(null, false, req.flash(constants.loginMessageType, constants.noUserFound));

                if (!user.validPassword(password))
                    return cb(null, false, req.flash(constants.loginMessageType, constants.wrongPassword));

                return cb(null, user);
            });
        })
    );
};