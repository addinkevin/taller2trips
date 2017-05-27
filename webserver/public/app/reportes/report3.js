var reportes = angular.module("tripsApp.reportes");

reportes.controller("Report3Controller", [ '$scope', function($scope) {
    var data = [
        {"number":  4, "pais": "Argentina"},
        {"number":  8, "pais": "Brazil"},
        {"number": 15, "pais": "Portugal"},
        {"number": 16, "pais": "EE.UU"},
        {"number": 23, "pais": "Canada"},
        {"number": 42, "pais": "Chile"}
    ];

    var data2 = [
        {"number":  4, "pais": "Twitter"},
        {"number":  8, "pais": "Facebook"},
        {"number": 15, "pais": "Invitado"},
    ];

    var labelIdGrafico1 = "pais";
    var valueIdGrafico1 = "number";

    var labelIdGrafico2 = "pais";
    var valueIdGrafico2 = "number";

    var _scope = $scope;
    $scope.mostrarPais = false;

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
            .text(function(d) { console.log(d.data); return d3.format(".2%")(d.data.porcentaje);});

        function angle(d) {
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
            .text(function(d) { return d.data[valueId]; });

        function angle(d) {
            var a = (d.startAngle + d.endAngle) * 90 / Math.PI - 90;
            return a > 90 ? a - 180 : a;
        }
    }

    function mostrarInformacionDePais(d) {
        $scope.mostrarPais = true;
        $scope.pais = d.data[labelIdGrafico1];
        $scope.cantidad = d.data[valueIdGrafico1];
        d3.select("#graficoReporte3Pais").selectAll("svg").remove();
        createReport3Pais(data2, "#graficoReporte3Pais", labelIdGrafico2, valueIdGrafico2);
        $scope.$digest();
    }

    createReport3(data, "#graficoReporte3", "pais", "number");

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

}]);