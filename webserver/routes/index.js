var express = require('express');
var constants = require('../config/constants');
var ciudadesRoute = require('./ciudadesRoute');
var atraccionesRoute = require('./atraccionesRoute');
var reseniasRoute = require('./reseniasRoute');
var usuariosRoute = require('./usuariosRoute');
var favoritosRoute = require('./favoritosRoute');
var recorridosRoute = require('./recorridosRoute');
var notificacionesPushRoute = require('./notificacionesPushRoute');

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
        if (req.isAuthenticated())
            return next();

        res.redirect('/');
    }

    app.use('/', router);
    app.use('/api', ciudadesRoute);
    app.use('/api', atraccionesRoute);
    app.use('/api', reseniasRoute);
    app.use('/api', usuariosRoute);
    app.use('/api', favoritosRoute);
    app.use('/api', recorridosRoute);
    app.use('/api', notificacionesPushRoute);
};
