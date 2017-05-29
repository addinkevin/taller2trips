var reportes = angular.module("tripsApp.reportes");

reportes.controller("Report2Controller", [ '$scope', '$http', '$uibModal', function($scope, $http, $uibModal) {
    function createReport2(data, containerId, labelId, valueId, m, h, w) {
        //Draw svg
        var svg = d3.select(containerId).append("svg")
            .attr("width", w + m.left + m.right)
            .attr("height", h + m.top + m.bottom)
            .style("overflow-x", "scroll")
            .append("g")
            .attr("transform", "translate(" + m.left + "," + m.top + ")");

        var yhist = d3.scaleLinear()
            .domain([0, d3.max(data, function(d) { return d[valueId]; })])
            .range([h, 0]);

        var yAxis = d3.axisLeft()
            .scale(yhist);

        //Draw histogram
        var bar = svg.selectAll(".bar")
            .data(data)
            .enter().append("g")
            .attr("class", "bar");

        bar.append("rect")
            .attr("x", function(d, i) { return i * w/data.length; }) //return xScale(d[labelId]); })
            .attr("width", (w/data.length)-5)
            .attr("y", function(d) { return yhist(d[valueId]); })
            .attr("height", function(d) { return h - yhist(d[valueId]); });

        bar.append("text")
            .attr("dy", "-.25em")
            .attr("text-anchor", "middle")
            .attr("x", function(d,i) { return (i * w/data.length) + ((w/data.length)-5)/2 })
            .attr("y", function(d) { return yhist(d[valueId]); })
            .text(function(d) { return d[valueId] });

        bar.append("text")
            .attr("text-anchor", "middle")
            .attr("x", function(d,i) { return (i * w/data.length) + ((w/data.length)-5)/2 })
            .attr("y", function(d) { return h + 20; })//yhist(d[valueId]); })
            .text(function(d) { return d[labelId]; });

        svg.append("g")
            .attr("class", "y axis")
            .call(yAxis)
            .append("text")
            .attr("transform", "rotate(-90)")
            .attr("y", 6)
            .attr("dy", ".71em")
            .style("text-anchor", "end");

        // text label for the y axis
        svg.append("text")
            .attr("transform", "rotate(-90)")
            .attr("y", 0 - m.left)
            .attr("x",0 - (h / 2))
            .attr("dy", "1em")
            .style("text-anchor", "middle")
            .text("Cantidad");

        svg.append("text")
            .attr("transform",
                "translate(" + (w/2) + " ," +
                (h + m.top + 20) + ")")
            .style("text-anchor", "middle")
            .text("Atracciones");
    }

    var data = [ ];

    $scope.popup1 = {
        opened: false
    };

    $scope.popup2 = {
        opened: false
    };

    $scope.today = function() {
        $scope.fechaInicio = new Date();
        $scope.fechaFin = new Date();
    };
    $scope.today();

    $scope.dateOptions = {
        minMode: 'month'
    };

    $scope.format = "MM/yyyy";
    $scope.altInputFormats = ['M/yyyy'];
    $scope.open1 = function() {
        $scope.popup1.opened = true;
    };

    $scope.open2 = function() {
        $scope.popup2.opened = true;
    };

    $scope.showModal = function (title, msg) {
        var modalInstance = $uibModal.open({
            templateUrl: 'app/reportes/modal.html',
            keyboard: false,
            backdrop: 'static',
            controller: function($scope, $uibModalInstance) {
                $scope.titleModal = title;
                $scope.msgModal = msg;

                $scope.okModal = function () {
                    $uibModalInstance.close();
                };

                $scope.cancelConfirm = function () {
                    $uibModalInstance.dismiss('cancel');
                };
            }
        });

        return modalInstance.result;
    };

    function validarFechas() {
        if (!$scope.fechaInicio || !$scope.fechaFin) {
            $scope.showModal("Error!", "Debes especificar una fecha de inicio y una fecha de fin.");
            return false;
        }

        if ($scope.fechaInicio > $scope.fechaFin) {
            $scope.showModal("Error!", "La fecha de fin debe ser superior a la fecha de inicio.");
            return false;
        }

        return true;
    }

    function getRango(fechaInicio, fechaFin) {
        console.log(fechaInicio, fechaFin);
        var mesInicio = fechaInicio.getMonth()+1;
        var anioInicio = fechaInicio.getFullYear();
        var mesFin = fechaFin.getMonth()+1;
        var anioFin = fechaFin.getFullYear();

        var lista = [];
        for (var anio = anioInicio; anio <= anioFin; anio++) {
            var _mesInicio = 1;
            var _mesFin = 12;
            if (anio == anioInicio) {
                _mesInicio = mesInicio
            }
            if (anio == anioFin) {
                _mesFin = mesFin;
            }
            for (var mes = _mesInicio; mes <= _mesFin; mes++) {
                lista.push(mes+"/"+anio);
            }
        }

        return lista;
    }

    function getFechaInicio() {
        return new Date($scope.fechaInicio.getFullYear(), $scope.fechaInicio.getMonth(), 1);
    }

    function getFechaFin() {
        return new Date($scope.fechaFin.getFullYear(), $scope.fechaFin.getMonth()+1, 0);
    }

    $scope.cargarGrafico2 = function() {
        if (!validarFechas()) return;

        var _fechaInicio = getFechaInicio();
        var _fechaFin = getFechaFin();

        var info = {
            "anio_inicio": _fechaInicio.getFullYear(),
            "mes_inicio": _fechaInicio.getMonth()+1,
            "dia_inicio": _fechaInicio.getDate(),
            "anio_fin": _fechaFin.getFullYear(),
            "mes_fin": _fechaFin.getMonth()+1,
            "dia_fin": _fechaFin.getDate()
        };

        $http({
            method: 'GET',
            url: '/api/reporte/usuariosUnicosGlobales',
            params: info
        }).then(function success(res) {
            data = res.data;
            data.forEach(function(x) {
                x.fecha = x._id.mes + "/" + x._id.anio;
            });

            var _data = [];
            var fechas = getRango(_fechaInicio, _fechaFin);

            fechas.forEach(function (fecha) {
                var d = data.find(function(x) { return x.fecha == fecha} );
                if (d) {
                    _data.push(d);
                } else {
                    _data.push({'fecha': fecha, 'value': 0 });
                }
            });

            data = _data;

            var m = {top: 50, right: 50, bottom: 50, left: 50}
                , h = 500 - m.top - m.bottom
                , w = 150*data.length - m.left - m.right;

            d3.select("#graficoReporte2").selectAll("svg").remove();
            createReport2(data, "#graficoReporte2", "fecha", "value", m, h, w);
        });

    }


}]);