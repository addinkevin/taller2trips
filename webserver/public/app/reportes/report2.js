var reportes = angular.module("tripsApp.reportes");

reportes.controller("Report2Controller", [ '$scope', function($scope) {
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

    var data = [
        {
            label: "1/2017",
            value: 1200
        },
        {
            label: "2/2017",
            value: 800
        },
        {
            label: "3/2017",
            value: 400
        },
        {
            label: "4/2017",
            value: 200
        },
        {
            label: "5/2017",
            value: 100
        },
        {
            label: "6/2017",
            value: 0
        },
        {
            label: "7/2017",
            value: 0
        }
    ];

    var m = {top: 50, right: 50, bottom: 50, left: 50}
        , h = 500 - m.top - m.bottom
        , w = 100*data.length - m.left - m.right;

    createReport2(data, "#graficoReporte2", "label", "value", m, h, w);

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