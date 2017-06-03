var reportes = angular.module("tripsApp.reportes");

reportes.controller("Report3Controller", [ '$scope', '$http', '$uibModal', function($scope, $http, $uibModal) {
    var data = [ ];

    var labelIdGrafico1 = "pais";
    var valueIdGrafico1 = "value";

    var labelIdGrafico2 = "label";
    var valueIdGrafico2 = "value";

    var lastData = null;
    var lastElement = null;

    var m = {top: 50, right: 50, bottom: 50, left: 50}
        , h = 500 - m.top - m.bottom
        , w = 600 - m.left - m.right
        , r = Math.min(h,w)/3;

    var moverArc = function(element, d) {
        d3.select(element).transition()
            .attr("transform",function(d){
                if (!d.data._expanded){
                    d.data._expanded = true;
                    var a = d.startAngle + (d.endAngle - d.startAngle)/2 - Math.PI/2;
                    var x = Math.cos(a) * 20;
                    var y = Math.sin(a) * 20;
                    // move it away from the circle center
                    return 'translate(' + x + ',' + y + ')';
                } else {
                    d.data._expanded = false;
                    lastData = null;
                    // move it back
                    return 'translate(0,0)';
                }
            });
    };

    function createReport3(data, containerId, labelId, valueId) {
        lastData = null;
        lastElement = null;
        //Draw svg
        var svg = d3.select(containerId).append("svg")
            .attr("width", w + m.left + m.right)
            .attr("height", h + m.top + m.bottom);

        var g = svg.append("g").attr("transform", "translate(" + w / 2 + "," + h / 2 + ")");

        var color = d3.scaleOrdinal(["#98abc5", "#8a89a6", "#7b6888", "#6b486b", "#a05d56", "#d0743c", "#ff8c00"]);

        var pie = d3.pie()
            .sort(null)
            .value(function(d) { return d[valueId]; });

        var path = d3.arc()
            .outerRadius(r)
            .innerRadius(0);

        var value = d3.arc()
            .outerRadius(r + 10)
            .innerRadius(0);

        var label = d3.arc()
            .outerRadius(r + 20)
            .innerRadius(r + 20);

        var arc = g.selectAll(".arc")
            .data(pie(data))
            .enter().append("g")
            .attr("class", "arc");

        var total = d3.sum(data, function(d) {
            return d[valueId];
        });

        data.forEach(function(d) {
            d.porcentaje = d[valueId]  / total;
        });

        g.selectAll(".arc")
            .on("click", function(d,i) {
                console.log(d, i);
                if (lastElement) moverArc(lastElement, lastData);
                lastElement = this;
                lastData = d;
                moverArc(this,d);
                mostrarInformacionDePais(d);
            });

        arc.append("path")
            .attr("d", path)
            .attr("fill", function(d) { return color(d.data[labelId]); });


        var outerRadius = r;
        arc.append("svg:text")
            .attr("transform", function(d) {
                d.outerRadius = outerRadius + 50;
                d.innerRadius = outerRadius + 45;
                return "translate(" + label.centroid(d) + ")";
            })
            .attr("text-anchor", "middle")
            .style("fill", "Purple")
            .style("font", "bold 12px Arial")
            .text(function(d, i) { return d.data[labelId]; });

        arc.filter(function(d) { return d.endAngle - d.startAngle > .2; }).append("svg:text")
            .attr("dy", ".35em")
            .attr("text-anchor", "middle")
            .attr("transform", function(d) {
                d.outerRadius = outerRadius;
                d.innerRadius = outerRadius/2;
                return "translate(" + value.centroid(d) + ")rotate(" + angle(d) + ")";
            })
            .style("fill", "White")
            .style("font", "bold 12px Arial")
            .text(function(d) { return d.data[valueId] + "(" + d3.format(".2%")(d.data.porcentaje) + ")"; });

        function angle(d) {
            return 0;
            var a = (d.startAngle + d.endAngle) * 90 / Math.PI - 90;
            return a > 90 ? a - 180 : a;
        }
    }

    function createReport3Pais(data, containerId, labelId, valueId) {
        //Draw svg
        var svg = d3.select(containerId).append("svg")
            .attr("width", w + m.left + m.right)
            .attr("height", h + m.top + m.bottom);

        var g = svg.append("g").attr("transform", "translate(" + w / 2 + "," + h / 2 + ")");

        var color = d3.scaleOrdinal(["#98abc5", "#8a89a6", "#7b6888", "#6b486b", "#a05d56", "#d0743c", "#ff8c00"]);

        var total = d3.sum(data, function(d) {
            return d[valueId];
        });

        data.forEach(function(d) {
            d.porcentaje = d[valueId]  / total;
        });

        var pie = d3.pie()
            .sort(null)
            .value(function(d) { return d[valueId]; });

        var path = d3.arc()
            .outerRadius(r)
            .innerRadius(0);

        var value = d3.arc()
            .outerRadius(r + 10)
            .innerRadius(0);

        var label = d3.arc()
            .outerRadius(r + 25)
            .innerRadius(r + 25);

        var arc = g.selectAll(".arc")
            .data(pie(data))
            .enter().append("g")
            .attr("class", "arc");

        arc.append("path")
            .attr("d", path)
            .attr("fill", function(d) { return color(d.data[labelId]); });


        var outerRadius = r;
        arc.append("svg:text")
            .attr("transform", function(d) {
                d.outerRadius = outerRadius + 50;
                d.innerRadius = outerRadius + 45;
                return "translate(" + label.centroid(d) + ")";
            })
            .attr("text-anchor", "middle")
            .style("fill", "Purple")
            .style("font", "bold 12px Arial")
            .text(function(d, i) { return d.data[labelId]; });

        arc.filter(function(d) { return d.endAngle - d.startAngle > .2; }).append("svg:text")
            .attr("dy", ".35em")
            .attr("text-anchor", "middle")
            .attr("transform", function(d) {
                d.outerRadius = outerRadius;
                d.innerRadius = outerRadius/2;
                return "translate(" + value.centroid(d) + ")rotate(" + angle(d) + ")";
            })
            .style("fill", "White")
            .style("font", "bold 12px Arial")
            .text(function(d) { return d.data[valueId] + "(" + d3.format(".2%")(d.data.porcentaje) + ")"; });

        function angle(d) {
            return 0;
            var a = (d.startAngle + d.endAngle) * 90 / Math.PI - 90;
            return a > 90 ? a - 180 : a;
        }
    }

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

    function getFechaInicio() {
        return new Date($scope.fechaInicio.getFullYear(), $scope.fechaInicio.getMonth(), 1);
    }

    function getFechaFin() {
        return new Date($scope.fechaFin.getFullYear(), $scope.fechaFin.getMonth()+1, 0);
    }

    function mostrarInformacionDePais(d) {
        $scope.mostrarPais = true;
        $scope.pais = d.data[labelIdGrafico1];
        $scope.cantidad = d.data[valueIdGrafico1];
        d3.select("#graficoReporte3Pais").selectAll("svg").remove();
        createReport3Pais(d.data.redes, "#graficoReporte3Pais", labelIdGrafico2, valueIdGrafico2);
        $scope.$digest();
    }

    $scope.mostrarPais = false;
    $scope.mostrarGrafico = false;
    $scope.mostrarMsg = false;
    $scope.msg = "No hay datos";

    $scope.cargarGrafico3 = function() {
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
            url: '/api/reporte/usuariosUnicosPaisProvider',
            params: info
        }).then(function success(res) {
            data = [];

            res.data.forEach(function(x) {
                if (x && x._id && x._id.pais) {
                    x.pais = x._id.pais;
                    data.push(x);
                }
            });

            if (data.length == 0) {
                $scope.mostrarMsg = true;
                $scope.mostrarGrafico = false;
            } else {
                $scope.mostrarMsg = false;
                $scope.mostrarPais = false;
                $scope.mostrarGrafico = true;
                d3.select("#graficoReporte3").selectAll("svg").remove();
                createReport3(data, "#graficoReporte3", "pais", "value");
            }

        });

    }

}]);