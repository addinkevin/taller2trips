var reportes = angular.module("tripsApp.reportes");

reportes.controller("Report1Controller", [ '$scope', function($scope) {
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
            .text("Visitas");

        svg.append("text")
            .attr("transform",
                "translate(" + (w/2) + " ," +
                (h + m.top + 20) + ")")
            .style("text-anchor", "middle")
            .text("Atracciones");
    }

    var data = [
        {
            label: "Bariloche",
            value: 1200
        },
        {
            label: "Obelisco",
            value: 800
        },
        {
            label: "Chocolates",
            value: 400
        },
        {
            label: "A",
            value: 200
        },
        {
            label: "B",
            value: 100
        },
        {
            label: "C",
            value: 90
        },
        {
            label: "D",
            value: 80
        },
        {
            label: "E",
            value: 50
        },
        {
            label: "F",
            value: 20
        },
        {
            label: "G",
            value: 10
        }
    ];

    createReport1(data, "#graficoReporte1", "label", "value");

}]);