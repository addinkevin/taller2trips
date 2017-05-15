var Atraccion = require('../models/atracciones');
var constants = require('../config/constants');

var clasificaciones = {
    "Atracciones y lugares de interés": {
        "es": "Atracciones y lugares de interés",
        "en": "General interest atractions",
        "pt": "Atrações e pontos turísticos"
    },
    "Compras": {
        "es": "Compras",
        "en": "Shopping",
        "pt": "Compras"
    },
    "Museos": {
        "es": "Museos",
        "en": "Museums",
        "pt": "Museus"
    },
    "Ideal para visitar con niños": {
        "es": "Ideal para visitar con niños",
        "en": "Ideal for children",
        "pt": "Ideal para visitar com crianças"
    },
    "Conciertos y espectáculos": {
        "es": "Conciertos y espectáculos",
        "en": "Concerts and shows",
        "pt": "Concertos e espectáculos"
    },
    "Naturaleza y parques": {
        "es": "Naturaleza y parques",
        "en": "Nature and parks",
        "pt": "Natureza e Parques"
    },
    "Vida nocturna": {
        "es": "Vida nocturna",
        "en": "Nightlife",
        "pt": "Vida noturna"
    },
    "Actividades al aire libre": {
        "es": "Actividades al aire libre",
        "en": "Outdoor activities",
        "pt": "Atividades ao ar livre"
    },
    "Clases y talleres": {
        "es": "Clases y talleres",
        "en": "Classes and workshops",
        "pt": "Aulas e workshops"
    },
    "Diversion y juegos": {
        "es": "Diversion y juegos",
        "en": "Fun and games",
        "pt": "Diversao e jogos"
    },
    "Comida y bebida": {
        "es": "Comida y bebida",
        "en": "Food and beverages",
        "pt": "Comida e bebida"
    },
    "Transporte": {
        "es": "Transporte",
        "en": "Transportation",
        "pt": "Transporte"
    },
    "Zoologicas y acuarios": {
        "es": "Zoologicas y acuarios",
        "en": "Zoos and aquariums",
        "pt": "Zoológico e aquários"
    },
    "Casinos y juegos de azar": {
        "es": "Casinos y juegos de azar",
        "en": "Casinos and gambling",
        "pt": "Cassinos e jogos de azar"
    },
    "Spas": {
        "es": "Spas",
        "en": "Spas",
        "pt": "Spas"
    }
};

exports.obtenerClasificacion = function(clasificacion, idioma) {
    if (idioma === undefined || (idioma !== "en" && idioma !== "es" && idioma !== "pt")) idioma = constants.idiomaDefault;
    return clasificaciones[clasificacion][idioma];
}

exports.actualizarCalificacion = function(id_atraccion, calificacion) {
    Atraccion.findById(id_atraccion, function(err, atraccion) {
        if (err) console.log(err);
        else if (atraccion === null) console.log("Atraccion con id " + id_atraccion + " no existe");
        else {
            console.log(atraccion);
            atraccion.cantidad_votos = atraccion.cantidad_votos + 1;
            atraccion.rating = atraccion.rating + (calificacion - atraccion.rating) / atraccion.cantidad_votos;
            atraccion.save();
        }
    });
};

