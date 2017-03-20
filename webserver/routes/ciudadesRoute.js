var express = require('express');
var constants = require('../config/constants');
var router = express.Router();
var Ciudad = require('../models/ciudades');

router.get('/ciudades', function(req, res) {
    Ciudad.find(function (err, ciudades) {
        if (err) {
            res.send(err);
        }
        res.json(ciudades);
    })
});

router.post('/ciudades', function(req, res) {
    console.log(req.body);

    var ciudad = new Ciudad({
        name: req.body.name,
        center: req.body.center,
        radio: req.body.radio
    });

    ciudad.save(function(err) {
        if (err) {
            res.send(err);
        }

        Ciudad.find(function (err, ciudades) {
            if (err) {
                res.send(err);
            }
            console.log("Ciudades");
            console.log(ciudades);
            res.json(ciudades);
        });
    });

});


router.delete('/ciudades/:ciudad', function(req,res) {
    Ciudad.remove({
        _id : req.params.ciudad
    }, function(err, ciudad) {
        if (err) {
            res.send(err);
        }

        Ciudad.find(function (err, ciudades) {
            if (err) {
                res.send(err);
            }
            res.json(ciudades);
        });
    })
});

module.exports = router;