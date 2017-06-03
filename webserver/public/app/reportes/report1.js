var reportes = angular.module("tripsApp.reportes");

reportes.controller("Report1Controller", [ '$scope', '$http', function($scope, $http) {
    var m = {top: 50, right: 50, bottom: 50, left: 50}
        , h = 500 - m.top - m.bottom
        , w = 960 - m.left - m.right;

    function createReport1(data, containerId, labelId, valueId){
        //Draw svg
        var svg = d3.select(containerId).append("svg")
            .attr("width", w + m.left + m.right)
            .attr("height", h + m.top + m.bottom)
            .append("g")
            .attr("transform", "translate(" + m.left + "," + m.top + ")");

        //Axes and scales
        var xScale = d3.scaleBand().rangeRound([0, w]).round(true).padding(.1);
        xScale.domain(data.map(function(d,i) { return d[labelId]; }));

        var yhist = d3.scaleLinear()
            .domain([0, d3.max(data, function(d) { return d[valueId]; })])
            .range([h, 0]);

        //var xAxis = d3.axisBottom()
          //  .scale(xScale);

        var yAxis = d3.axisLeft()
            .scale(yhist);

        //Draw histogram
        var bar = svg.selectAll(".bar")
            .data(data)
            .enter().append("g")
            .attr("class", "bar");

        var delta = 10;//data.lengt;

        bar.append("rect")
            .attr("x", function(d, i) { return i * w/delta; }) //return xScale(d[labelId]); })
            .attr("width", (w/delta)-5)
            .attr("y", function(d) { return yhist(d[valueId]); })
            .attr("height", function(d) { return h - yhist(d[valueId]); });

        bar.append("text")
            .attr("dy", "-.25em")
            .attr("text-anchor", "middle")
            .attr("x", function(d,i) { return (i * w/delta) + ((w/delta)-5)/2 })
            .attr("y", function(d) { return yhist(d[valueId]); })
            .text(function(d) { return d[valueId] });

        bar.append("text")
            .attr("text-anchor", "middle")
            .attr("x", function(d,i) { return (i * w/delta) + ((w/delta)-5)/2 })
            .attr("y", function(d) { return h + 20; })//yhist(d[valueId]); })
            .text(function(d) { return d[labelId]; });

        //Draw axes
        //svg.append("g")
          //  .attr("class", "x axis")
          //  .attr("transform", "translate(0," + h + ")")
          //  .call(xAxis);

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
            .text("Favoritos");

        svg.append("text")
            .attr("transform",
                "translate(" + (w/2) + " ," +
                (h + m.top + 20) + ")")
            .style("text-anchor", "middle")
            .text("Atracciones");
    }

    var data = [ ];

    $scope.mostrarMsg = false;
    $scope.msg = "No hubo favoritos agregados en ninguna atracción en los ultimos 12 meses.";

    function init() {
        $http.get('/api/reporte/atraccionesFavoritas').then(function success(res) {
            data = [];
            res.data.forEach(function(x) {
                if (x && x.id_atraccion && x.id_atraccion.nombre) {
                    x.nombre = x.id_atraccion.nombre;
                    data.push(x);
                }
            });
            if (data.length == 0) {
                $scope.mostrarMsg = true;
                $scope.msg = "No hubo favoritos agregados en ninguna atracción en los ultimos 12 meses.";
            } else {
                $scope.mostrarMsg = false;
                createReport1(data, "#graficoReporte1", "nombre", "value");
            }
        }, function error() {
            $scope.mostrarMsg = true;
            $scope.msg = "Error al cargar los datos";
        });
    }

    init();

}]);