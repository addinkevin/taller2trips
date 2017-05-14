exports.normalizarNombre = function(str) {
    return str.replace(/\w\S*/g, function(txt){return txt.charAt(0).toUppe    rCase() + txt.substr(1).toLowerCase();});
}
