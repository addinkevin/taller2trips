var express = require('express');
var constants = require('../config/constants');
var ciudadesRoute = require('./ciudadesRoute');
var atraccionesRoute = require('./atraccionesRoute');

module.exports = function(app, passport) {
    var router = express.Router();

    /* GET home page. */
    router.get('/', function(req, res, next) {
        res.render('index', { message: req.flash(constants.loginMessageType) });
    });

    router.post('/', passport.authenticate('login', {
        successRedirect : '/menu',
        failureRedirect : '/',
        failureFlash : true
    }));

    router.get('/menu', isLoggedIn, function(req, res, next) {
        res.render('menu');
    });

    router.get('/logout', function(req, res) {
        req.logout();
        res.redirect('/');
    });

    function isLoggedIn(req, res, next) {
        //return next(); // Uncomment to test without authentication.

        if (req.isAuthenticated())
            return next();

        res.redirect('/');
    }

    app.use('/', router);
    app.use('/api', ciudadesRoute);
    app.use('/api', atraccionesRoute);
};
