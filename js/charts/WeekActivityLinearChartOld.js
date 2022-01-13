(function() {
  "use strict";

  function WeekActivityLinearChartOld(config) {
    // Defaults
    var props = {
      timeFormat: "%d-%b-%y",
      data:   [],
      xi:     'date',
      yi:     'close',
      width:  960,
      height: 500,
      margin: [ 30, 20, 50, 50 ], // top, right, bottom, left
      yOffset: 3,
      rtl: false,
      locale: false,
      extendActiveArea: false
    };

    if ( config !== "undefined" ) {
      for( var prop in config ) {
        if ( config.hasOwnProperty(prop) ) {
          props[prop] = config[prop];
        }
      }
    }

    // Utils
    var parseDate = d3.time.format(props.timeFormat).parse,
    	localeTimeFormat = (props.locale) ? props.locale.timeFormat : d3.time.format ;

    function dXi(d) { return d[ props.xi ]; };
    function dYi(d) { return d[ props.yi ]; };
    function normalizeData(d) {
      if ( typeof dXi(d) === "string" ) {
    	  d[props.xi] = parseDate( dXi(d) );  
      } else {
    	  // TODO: add configuration to fix timezone to dates come in UTC or server timezone
    	  var date = new Date(dXi(d));
    	  date.setTime(date.getTime() + ( date.getTimezoneOffset() * 60000 ));
    	  d[props.xi] = date;
      }
       
      d[props.yi] = +dYi(d);
    };

    function X(d) { return x( dXi(d) ); };
    function Y(d) { return y( dYi(d) ); };

    function yMedian(median, xRange){
      var data = [];

      xRange.forEach(function (x) {
        var d = {};
        d[props.xi] = x;
        d[props.yi] = median;

        data.push(d);
      });

      return data;
    };
    
    function reduceSumFunc(prev, cur) { return prev + cur; };
    
    function leastSquares(xSeries, ySeries) {
      var xBar = xSeries.reduce(reduceSumFunc) * 1 / xSeries.length,
          yBar = ySeries.reduce(reduceSumFunc) * 1 / ySeries.length;

      var ssXX = xSeries.map(function(d) { return Math.pow(d - xBar, 2); })
            .reduce(reduceSumFunc),
          ssYY = ySeries.map(function(d) { return Math.pow(d - yBar, 2); })
            .reduce(reduceSumFunc),
          ssXY = xSeries.map(function(d, i) { return (d - xBar) * (ySeries[i] - yBar); })
            .reduce(reduceSumFunc);

      var slope = ssXY / ssXX,
          intercept = yBar - ( xBar * slope ),
          rSquare   = Math.pow(ssXY, 2) / (ssXX * ssYY);

      return [slope, intercept, rSquare];
    };

    // Scales
    var x = d3.time.scale(),
        y = d3.scale.linear();

    // Axis
    var weekDayAxis = d3.svg.axis()
        .scale(x)
        .ticks(d3.time.days)
        .tickFormat(localeTimeFormat("%a"))
        .tickSize(-props.height + props.margin[0] + props.margin[2])
        .tickPadding(15)
        .orient("top");

    var monthDayAxis = d3.svg.axis()
        .scale(x)
        .ticks(d3.time.day)
        .tickFormat(localeTimeFormat("%e"))
        .tickSize(0)
        .tickPadding(15)
        .orient("bottom");

    // Draw utils
    var line = d3.svg.line().x(X).y(Y);

    function chart(selection) {      
      
      selection.each(function (data) {
    	data.forEach( normalizeData );
    	props.data = data;
    	chart.render.call(this);
      });
    };
    
    chart.width = function (_) {
    	if ( !arguments.length ) return props.width;
    	
    	props.width = _;
    	return chart;
    };
    
    chart.height = function (_) {
    	if ( !arguments.length ) return props.height;
    	
    	props.height = _;
    	return chart;
    };
    
    chart.margin = function (_) {
    	if ( !arguments.length ) return props.margin;
    	
    	props.margin = _;
    	return chart;
    };
    
    chart.data = function (_) {
    	if ( !arguments.length ) return props.data;
    	
    	_.forEach( normalizeData );
    	props.data = _;
    	
    	return chart;
    }
    
    chart.render = function () {
    	var data = props.data,
    		w = props.width - props.margin[1] - props.margin[3],
        	h = props.height - props.margin[0] - props.margin[2];
	
	    if ( dXi(data[0]).getMonth() != dXi(data[data.length - 1]).getMonth() ) {
	      var firstMonthDay = data.filter(function (d) {
	        return dXi(d).getDate() === 1;
	      }).shift();
	    } else {
	      var firstMonthDay = data[0];
	    }
	
	    var xProps = (props.rtl) ? [ w, 0 ] : [ 0, w ],
	    	xRange = d3.extent(data, dXi),
	        yRange = d3.extent(data, dYi),
	        offset = Math.ceil((yRange[1] - yRange[0]) / 10) * props.yOffset,
	        median = data.map(dYi).reduce(reduceSumFunc) / data.length;
	    
	    if (offset === 0) offset = props.yOffset;
	    
	    x.range(xProps).domain(xRange);
	    y.range([ h, 0 ]).domain([ (yRange[0] > 0) ? yRange[0] - offset : yRange[0],
	                                          yRange[1] + offset]);
	    


		    
		    
	    //	    Trend data
	    // COMMENTED TREND not in scope
	    /*var xSeries = d3.range(1, data.map(dXi).length + 1),
	        ySeries = data.map(dYi),
	        leastSquaresCoeff = leastSquares(xSeries, ySeries);
	
	    var trendData = [
	      dXi(data[0]), // x1
	      leastSquaresCoeff[0] + leastSquaresCoeff[1], // y1
	      dXi(data[data.length - 1]), // x2
	      leastSquaresCoeff[0] * xSeries.length + leastSquaresCoeff[1] // y2
	    ];*/
	    
	    d3.select(this).selectAll("svg").remove();
	    var svg = d3.select(this).selectAll("svg").data([data]);
	
	    var gEnter = svg.enter()
	        .append("svg").append("g")
	        .attr("transform", "translate(" + props.margin[3] + ","
	                                        + props.margin[0] + ")");
	
	    svg .attr("width", props.width)
	        .attr("height", props.height );
	    
	    var yAxis = gEnter.append("g")
	    	.attr("class", "y axis");
	    
	    yAxis.append("line")
	      .attr("x1", 0)
	      .attr("x2", w + 100)
	      .attr("transform", "translate(-50, 0)");
	    yAxis.append("line")
	      .attr("x1", 0)
	      .attr("x2", w + 100)
	      .attr("transform", "translate(-50, " + h + ")");
	
	    gEnter.append("g")
	        .attr("class", "x axis top")
	        .call(weekDayAxis);
	
	    var bottomAxes = gEnter.append("g")
	        .attr("class", "x axis bottom")
	        .attr("transform", "translate(0," + h + ")")
	        .call(monthDayAxis);
	    
	    var monthName = bottomAxes.append("text")
	        .attr("class", "monthname")
	        .attr("y", 60)
	        .attr("x", X(firstMonthDay))
	        .text(localeTimeFormat("%B")(dXi(firstMonthDay))); 
	
	    gEnter.append("line")
	        .attr("class", "line median")
	        .attr("x1", 0)
	        .attr("x2", w + 100)
	        .attr("y1", y(median))
	        .attr("y2", y(median))
	        .attr("transform", "translate(-50, 0)")
	
	    gEnter.append("path")
	        .attr("class", "line")
	        .attr("d", line);
	    
	    // COMMENTED TREND not in scope
	    /*var gTrend = gEnter.append("g");
	
	    gTrend.append("marker")
	      .attr("id", "trendMarker")
	      .attr("viewBox", "0 0 10 10")
	      .attr("refX", 0)
	      .attr("refY", 5)
	      .attr("markerUnits", "strokeWidth")
	      .attr("markerWidth", 4)
	      .attr("markerHeight", 5)
	      .attr("orient", "auto")
	      .append("path")
	        .attr("d", "M 0 0 L 10 5 L 0 10 z");
	
	    gTrend.append("line")
	      .attr("class", "trend-shadow")
	      .attr("x1", x(trendData[0]))
	      .attr("y1", y(trendData[1]))
	      .attr("x2", x(trendData[2]))
	      .attr("y2", y(trendData[3]))
	      .attr("stroke-width", 10)
	      .attr("marker-end", "url(#trendMarker)");
	    gTrend.append("line")
	      .attr("class", "trend")
	      .attr("x1", x(trendData[0]))
	      .attr("y1", y(trendData[1]))
	      .attr("x2", x(trendData[2]))
	      .attr("y2", y(trendData[3]))
	      .attr("stroke-width", 10)
	      .attr("marker-end", "url(#trendMarker)");*/
	
	    data.forEach(point);
	
	    function point(d) {
	      var yValue = dYi(d),
	      	  x = X(d),
	      	  y = Y(d),
	          point  = gEnter.append("g")
	            .attr("class", "point");
	      if ( yValue === yRange[1] || yValue === 0 ) {
	        var tFill = point.append("rect"),
	        	className = (yValue > 0 ) ? " max" : " min"
	            t = point.append("text").text(yValue).attr("class", "point-text " + className),
	            tBox = t[0][0].getBBox(),
	            tw   = tBox.width,
	            th   = tBox.height,
	            fSize   = parseInt(window.getComputedStyle(t[0][0], null).getPropertyValue('font-size'));
	        
	        point.attr("class", point.attr("class") + className);
	        
	        t.attr("x", X(d))
	          .attr("y", Y(d) - fSize * 0.86);
	        tFill.attr("x", t.attr("x") - tw /2)
	          .attr("y", t.attr("y") - (fSize * 1.14) )
	          .attr("width", tw)
	          .attr("height", th);
		  } else if ( yValue === yRange[0] ) {
	        var tFill = point.append("rect"),
	            t     = point.append("text").text(yValue).attr("class", "point-text min"),
	            tBox = t[0][0].getBBox(),
	            tw   = tBox.width,
	            th   = tBox.height,
	            fSize   = parseInt(window.getComputedStyle(t[0][0], null).getPropertyValue('font-size'));
	        
	        point.attr("class", point.attr("class") + " min");
	        
	        t.attr("x", X(d))
	          .attr("y", Y(d) + th );
	        tFill.attr("x", t.attr("x") - tw / 2)
	          .attr("y", t.attr("y") - (fSize * 1.14) )
	          .attr("width", tw)
	          .attr("height", th);
	      }
	           
	      point.append("circle")
	      	.attr("class", "showable_datum")
	      	.attr("cx", x)
	      	.attr("cy", y)
	      	.attr("r", 9);
	      
	      if (props.extendActiveArea) {
	      // bigger invisible area, so it's easier to touch it to make the tip appear
		      point.append("circle")
		      	.attr("class", "bounding_box")
		      	.attr("cx", x)
		      	.attr("cy", y)
		      	.attr("r", 40);
	      };
	    };
    };
    
    return chart;
  };

  window.WeekActivityLinearChartOld = WeekActivityLinearChartOld;
})();
