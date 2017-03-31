var toRad = function(number) {
    return number * Math.PI / 180;
};


exports.calcularDistancia = function(lat1, long1, lat2, long2) {
    var RADIO_TIERRA = 6371;

    var dx = lat2 - lat1;
    var dLat = toRad(dx);
    var dy = long2 - long1;
    var dLong = toRad(dy);

    var a = Math.sin(dLat/2) * Math.sin(dLat/2) + 
            Math.cos(toRad(lat1)) * Math.cos(toRad(lat2)) * 
            Math.sin(dLong/2) * Math.sin(dLong/2);
    var c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1-a)); 
    return c * RADIO_TIERRA;
};
  
