(function () {
	"use strict";
	
	function MonthlyActivityBarsChart(config) {
		// Defaults
	    var props = {
	      timeFormat: "%d-%b-%y",
	      data:   [],
	      xi:     'date',
	      yi:     'index',
	      groupsKey: 'devices',
	      groupClass: 'device',
	      groupId: 'id',
	      hiddens: [],
	      width:  960,
	      height: 500,
	      margin: [ 30, 20, 50, 50 ], // top, right, bottom, left
	      yOffset: 3,
	      rtl: false,
	      locale: false
	    };

	    if ( config !== "undefined" ) {
	      for( var prop in config ) {
	        if ( config.hasOwnProperty(prop) ) {
	          props[prop] = config[prop];
	        }
	      }
	    }
	    
	    var parseDate = d3.time.format(props.timeFormat).parse,
    		localeTimeFormat = (props.locale) ? props.locale.timeFormat : d3.time.format ;
	    
	    function dXi(d) { return d[ props.xi ]; };
	    function dYi(d) { return d[ props.yi ]; };
	    function dGroup(d) { return d[props.groupsKey]; };
	    function dClass(d) { return d[props.groupClass]; };
	    function dGroupId(d) {
	    	var group;
	    	
	    	return (props.groupId instanceof Array) 
	    		?  props.groupId.map(function (g) { return d[g]; }).join("_")
	    		: d[props.groupId];
	    };
	    
	    function X(d) { return x( dXi(d) ); };
	    function Y(d) { return y( dYi(d) ); };
	    
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
	    
	    function normalizeData(d) {
	    	var group = d[props.groupsKey],
	    		y0    = 0;
	    	
	    	if ( typeof dXi(d) === "string" ) {
	      	  d[props.xi] = parseDate( dXi(d) );  
	        } else {
	      	  // TODO: add to configuration var to set the fix timezone to dates come in UTC or server timezone
	      	  var date = new Date(dXi(d));
	      	  date.setTime(date.getTime() + ( date.getTimezoneOffset() * 60000 ));
	      	  d[props.xi] = date;
	        }
	    	
	    	d[props.yi] = updateGroup(group);
	    };
	    
	    function updateGroup(group){
	    	var total = 0,
	    		y0    = 0;
	    	
	    	group.forEach(function (d) {
	    		var type = dGroupId(d).toString();
	    		
	    		if ( props.hiddens.indexOf(type) !== -1 ){
	    			d.y0 = y0;
	    			d.y1 = y0;
	    		} else {
	    			d.y0 = y0;
	    			d.y1 = y0 += +dYi(d);
	    			total += +dYi(d);
	    		}
	    	});
	    	return total;
	    };
	    
	    function firstDayOfMonth() {
	    	
	    };
	    
	    // Scales
	    var x = d3.scale.ordinal(),
	    	y = d3.scale.linear();
	    
	    // Axis
	    var monthDayAxis = d3.svg.axis()
        	.scale(x)
        	.ticks(d3.time.day)
        	.tickFormat(localeTimeFormat("%e"))
        	.tickPadding(20)
        	.orient("bottom");
	    
	    function chart(selection) {
	    	selection.each(function (data) {
	    		data.forEach(normalizeData);
	    		props.data = data;
	    		chart.render.call(this);
	    	});
	    };
	    
	    chart.show = function (type) {
	    	if ( props.hiddens.indexOf(type) !== -1 ) props.hiddens.splice(props.hiddens.indexOf(type), 1);
	    	
	    	chart.update.call(this);
	    };
	    
	    chart.hide = function (type) {
	    	if ( props.hiddens.indexOf(type) === -1 ) props.hiddens.push(type);
	    	
	    	chart.update.call(this);
	    };
	    
	    chart.update = function ( axis ) {
			props.data.forEach(function (d) {
	    		var group = d[props.groupsKey];
	    		
	    		d[props.yi] = updateGroup(group);
	    	});
			
			var $this = d3.select(this), 
    			yMax  = d3.max(props.data, dYi),
				yMin  = d3.min(props.data, dYi),	    	
				median = props.data.map(dYi).reduce(reduceSumFunc) / props.data.length;
			
			$this.selectAll('.group rect')
				.transition().duration(500)
	    		.attr("y", function (d) { return y(d.y1); })
	    		.attr("height", function (d) { return y(d.y0) - y(d.y1); });
	    	$this.selectAll('.group text')
	    		.transition().duration(500)
	    		.attr("y", function (d) {
	    			return (d[props.groupsKey].length) ? 
	    					y(d[props.groupsKey][d[props.groupsKey].length - 1].y1) - 5 : 
	    					y(0) - 5; 
	    		})
	    		.text(dYi)
	    		.attr("class", function (d){
	    			var total = dYi(d); 
		    		if ( total === yMax ) return "max";
		    		if ( total === yMin ) return "min";
		    		return "";
	    		});
	    	
	    	$this.selectAll('.median')
	    		.transition().duration(500)
	        	.attr("y1", y(median))
	        	.attr("y2", y(median));
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
	    
	    chart.median = function (){
	    	return props.data.map(dYi).reduce(reduceSumFunc) / props.data.length;
	    };
	    
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
	    	    yMax   = d3.max(data, dYi),
	    	    yMin   = d3.min(data, dYi),
	    	    median = data.map(dYi).reduce(reduceSumFunc) / data.length,    	
	    	    yTop = d3.max(data, function(dataDay) {		
		    		return d3.sum(dataDay.deviceIndexData, function(dataAct) {
		    			return dataAct.index;
		    		});
		    	}); // This is the maximum index including devices currently hidden
	    	
	    	x.rangeRoundBands(xProps, .3).domain(data.map(dXi));
		    y.rangeRound([ h, 0 ]).domain([ 0, yTop ]);	// range based on yTop to avoid data going out of range if a hidden device gets selected
		    
		    monthDayAxis.tickSize(h);
		    
		    // Trend data
		    // COMMENTED TREND not in scope 
		    /*var trendData = [],
		    	ys      = data.map(dYi),
		    	xSeries = d3.range(1, 7),
		        ySeries = ys.splice(0, 6),
		        leastSquaresCoeff = leastSquares(xSeries, ySeries),
		        i = -1;
		    
		    trendData.push({
		    	x: dXi(data[0]),
	    		y: leastSquaresCoeff[0] + leastSquaresCoeff[1]
		    });
		    
		    while (ySeries.length) {
		    	xSeries = d3.range(1, ySeries.length + 1);
		    	i += ySeries.length;
		    	
		    	leastSquaresCoeff = leastSquares(xSeries, ySeries);
		    	
		    	trendData.push({
		    		x: dXi(data[i]),
		    		y: leastSquaresCoeff[0] * xSeries.length + leastSquaresCoeff[1]
		    	});
		    	
		    	ySeries = ys.splice(0, 6);
		    };*/
		    
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
		      .attr("x2", w)
		      .attr("transform", "translate(0, " + (h + 10) + ")");
		
		    var bottomAxes = gEnter.append("g")
	        	.attr("class", "x axis bottom")
	        	//.attr("transform", "translate(0," + h + ")")
	        	.call(monthDayAxis);
		    
		    var monthName = bottomAxes.append("text")
	        	.attr("class", "monthname")
		        .attr("y", h + 60)
		        .attr("x", X(firstMonthDay))
		        .text(localeTimeFormat("%B")(dXi(firstMonthDay)));
		    
		    var group = gEnter.selectAll('.group')
		    	.data(data).enter()
		    	.append("g")
		    	.attr("class", "group")
		    	.attr("transform", function (d) { return "translate(" + X(d) + ",0)"; });
		    group.selectAll('rect')
		    	.data(dGroup)
		    	.enter().append("rect")
		    	.attr("width", x.rangeBand())
		    	.attr("y", function (d) { return y(d.y1); })
		    	.attr("height", function (d) { return y(d.y0) - y(d.y1); })
		    	.attr("class", dClass);
		    group.append("text")
		    	.text(dYi)
		    	.attr("x", x.rangeBand() / 2)
		    	.attr("y", function (d) {
		    		
		    		return (d[props.groupsKey].length > 0) ? 
		    				y(d[props.groupsKey][d[props.groupsKey].length - 1].y1) - 5 : y(0) - 5; 
		    	})
		    	.attr("class", function (d) {
		    		var total = dYi(d); 
		    		if ( total === yMax ) return "max";
		    		if ( total === yMin ) return "min";
		    	});
		    if ( median > 0) {
			    gEnter.append("line")
			        .attr("class", "line median")
			        .attr("x1", 0)
			        .attr("x2", w)
			        .attr("y1", y(median))
			        .attr("y2", y(median));
		    }
		   
		    // COMMENTED TREND not in scope
		    /*var gTrend = gEnter.append("g")
		    	.datum(trendData)
		    	.attr("class", "trend-group");
			var trendLine = d3.svg.line()
	  			.x(function (d) {
	  				var _x = x(d.x); 
	  				if ( d === trendData[trendData.length - 1] ) _x += x.rangeBand();
	  				else if ( d !== trendData[0] ) _x += x.rangeBand() / 2;
	  			
	  				return  _x; 
	  			}).y(function (d) { return y(d.y); })
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
		
		    gTrend.append("path")
		      .attr("class", "trend-shadow")
		      .attr("d", trendLine)
		      .attr("marker-end", "url(#trendMarker)");
		      
		    gTrend.append("path")
		      .attr("class", "trend")
		      .attr("d", trendLine)
		      .attr("marker-end", "url(#trendMarker)");
		    trendData.shift();
		    trendData.pop();
		    gTrend.selectAll(".trend-point")
		    	.data(trendData).enter()
		    	.append("circle")
		    	.attr("class", "trend-point")
		    	.attr("cx", function (d) { return x(d.x) + x.rangeBand() / 2; })
		    	.attr("cy", function (d) { return y(d.y); })
		    	.attr("r", 7);*/
	    };
	    
	    return chart;
	};
	
	window.MonthlyActivityBarsChart = MonthlyActivityBarsChart;
})();