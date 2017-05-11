var push = angular.module('tripsApp.push');

push.service('PushService',  ['$http', 'IdiomaService', '$timeout', function ($http, IdiomaService, $timeout) {
    this.id = 1;
    this.pushes = [
        {
            _id: 0,
            nombre: 'Identificador1',
            link: 'Link1',
            descripcion: { es: "Descr", en:"", pt: ""}
        }
    ];

    this.getDescriptionObject = function() {
        return { es: "", en:"", pt: ""};
    };

    this.getCiudades = function() {
        return new Promise(function (resolve, reject) {
            $timeout(function() {
                resolve({
                    data: [ { nombre: 'Bariloche'}, { nombre: 'Mar del plata'}]
                });
            }, 100);
        });
    };

    this.addPush = function(push) {
        var self = this;
        push.id = this.id;
        this._id = this.id + 1;
        return new Promise(function (resolve,reject) {
            $timeout(function() {
                self.pushes.push(push);
                resolve();
            }, 100);
        });
    };

    this.editPush = function(push) {
        var element = this.pushes.find(function(element) {
            return element._id == push._id;
        });

        var self = this;
        return new Promise(function(resolve, reject) {
            if (!element) {
                reject();
            }

            self.pushes[self.pushes.indexOf(element)] = push;
            resolve();
        });
    };

    this.getPush = function(idPush) {
        var element = this.pushes.find(function(element) {
            return element._id == push._id;
        });

        var self = this;
        return new Promise(function(resolve, reject) {
            if (!element) {
                reject();
            }

            resolve(element);
        });
    };

}]);