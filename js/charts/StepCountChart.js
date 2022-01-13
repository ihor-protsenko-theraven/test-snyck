(function () {
	"use strict";

  function StepCountChart(config) {

    //Enums Constants
    //var GraphEnum = Object.freeze({LAST: 0, FIRST: 1, ACTIVITY: 2});
    var ValuesEnum = Object.freeze({noactivity: -1});
    var DeviceEnum = Object.freeze({desktop: 1, mobile: 2});
    var ActivitiesEnum = Object.freeze({stepCount: 0});
    var ActivitiesstringEnum = Object.freeze({stepCount: 'stepCount'});
    var ActivitiesClassDevicesEnum = Object.freeze({stepCount: 'STEP_COUNT_SENSOR'});

 // Defaults  
    var props = {
      timeFormat: "%d-%b-%y",
      data:   [],
      xi:     'day',
      yi:     'stepCount',
      width:  960,
      height: 250, //200
      heighti : 1, // 0.20,0.20,0.70
      overlapheighti : 0,
      marginbetweencharts : 65,
      margintopfirstchart : 45, //35
      margin: [ 20, 20, 50, 50 ], // top, right, bottom, left 20, 20, 50, 50
      yOffset: 1,
      rtl: false,
      locale: false,
      extendActiveArea: false,
      hiddens: [],
      device: DeviceEnum.desktop,
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

    //Get raw data 
    function dXi(d) {     	
    	//return d[ props.xi ]; 
    	return d[ 'startTime' ]; 
    	};
    function dYi(d) {     	
    	//return d[ props.yi ];
    	return d[ 'stepCount' ];
    	};

    // Scales
    var x = d3.time.scale.utc();
    var y = d3.scale.linear();

    //Get data for scale
    function X(d) { 
    	return x( d['day'] ); 
    	};
    	
    //Return scale for a value	
    function Y(d) { 
    	return y( d['stepCount'] ); 
    	};
        
    chart.show = function (type) {
    	if ( props.hiddens.indexOf(type) !== -1 ) 
    		props.hiddens.splice(props.hiddens.indexOf(type), 1);
    	chart.update.call(this);
    };
    
    chart.hide = function (type) {
    	if ( props.hiddens.indexOf(type) === -1 ) 
    		props.hiddens.push(type);
    	chart.update.call(this);
    };    
    
    //Accessors
    var accessordXi = function (d) { return d[ 'day' ]; };
    var accessordYi = function (d) { return d[ 'stepCount' ]; };
        
    //Accessors Scales
    var accessorX = function (d) { return X(d,0);};
    var accessorY = function (d) { return Y(d,0);};

    //Top graph
    function topgraph() {
        var top = props.height * (props.heighti * props.overlapheighti);
        var space = props.marginbetweencharts * props.overlapheighti;
    	return top + space; 
    };
    	
    function normalizeData(d) {
      if ( typeof dXi(d) === "string" ) {
    	  d[props.xi] = parseDate( dXi(d));  
      } else {
    	  // TODO: add configuration to fix timezone to dates come in UTC or server timezone
    	  var date = new Date(dXi(d));
    	  date.setTime(date.getTime() + ( date.getTimezoneOffset() * 60000 ));
    	  d[props.xi] = date;
      }      
    };

    function convertstringtotime(timestring){
    	if (timestring !== null && timestring !== undefined && timestring !== "")
    		{
    			var minutes= convertminutestonumeric(Number(timestring.substring(3,5)));
//    			var valor=timestring.substring(0,2) + "." + timestring.substring(3,5);
//    			var valor=timestring.substring(0,2) + "." + minutes.toString();
    			var valor = timestring.substring(0, 2) + "." + minutes.toString().substring(2, minutes.length);
    			return Number(valor);
    		}
    	else
    		return -1;
    }

    function convertminutestonumeric(minutes){
    	return (minutes / 60).toFixed(2);
    }
    
    function converttimetostring(time){
    	if (time === "") return "";
    	var hour = Math.floor(time);
    	var minutes = Math.floor(60 * (time % 1));
    	
    	//12-24H CLOCK OPERATIONS
    	var timeFormat = $.app.timeFormatter;
    	var formattedTime =  formatTime(zp(hour,2)+":"+zp(minutes,2), timeFormat);
    	
    	return formattedTime;
    }

    var zp = function(n,c) { 
    	var s = String(n); 
    	if (s.length< c) { return zp("0" + n,c) } 
    	else { return s } 
    }

    // Axis
    var weekDayAxis = d3.svg.axis()
        .scale(x)
        .ticks(d3.time.days)
        .tickFormat(props.device === DeviceEnum.desktop ? localeTimeFormat("%d %a") : localeTimeFormat("%a"))
        .tickSize(0)
        .tickPadding(15)
        .orient("top");
    
    var weekNumberAxis = d3.svg.axis()
    	.scale(x)
    	.ticks(d3.time.days)
    	.tickFormat(localeTimeFormat("%d"))
    	.tickSize(0)
    	.tickPadding(15)
    	.orient("top");    

     var valueAxis = d3.svg.axis()
     	.scale(y)
     	.ticks(10)     	 // This is a suggestion to d3	   
 	    .orient("left") 	             
     
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
    };
    
    chart.hiddens = function () { 
    	return props.hiddens;
    };
    	
    chart.converttimetostring = function (time) { 
    	return converttimetostring(time);
   	};
        	
    chart.render = function () {    	    
    	
    	var data = props.data, // Just last week data
		
    	w = props.width - props.margin[1] - props.margin[3],
    	h = props.height - props.margin[0] - props.margin[2];
    	
    	var xProps = (props.rtl) ? [ w, 0 ] : [ 0, w ];
    			
    	var xRange = d3.extent(data,accessordXi),
        yRange = d3.extent(data,accessordYi);
    	yRange[0] = 0;
    	
    	var thisWeekHasYData = yRange[1] > 0;
        
	    x.range(xProps).domain(xRange);
		y.range([h, 0]).domain(yRange);

	    d3.select(this).selectAll("svg").remove();
	    
	    var svg = d3.select(this).selectAll("svg").data([data]);
	    var gEnter = svg.enter()
	        .append("svg").append("g")
	        .attr("transform", "translate(" + props.margin[3] + ","
	                                        + props.margin[0] + ")");
	
	    svg .attr("width", props.width)
	        .attr("height", props.height );
	    
	    if (props.device === DeviceEnum.mobile)
	    	{
			    // top day string axis
			    gEnter.append("g")
		        .attr("class", "x naiaxis top")
		        .attr("transform", "translate(0,-15)")
		        .call(weekNumberAxis);
	    	}

	    gEnter.append("g")
        .attr("class", "x naiaxis top")
        .call(weekDayAxis);
	    
	    //STACK ACTIVITIES
	    var gStackActivities=gEnter.append("g")
	    	.attr("width", props.width)
	        .attr("height", h *  props.heighti)
	        .attr("transform", "translate(0,"
	                                        + (props.device === DeviceEnum.desktop ? topgraph() : (topgraph()))  + ")");
	    
	    // hide the scale axis when the total steps of showed 7 days in the chart be 0
	    var yTicksParentNode = gEnter.append("g").attr("transform", "translate(-90, 0)")
	    .style({ 'stroke': (thisWeekHasYData ?'black' : 'transparent'), 'fill': 'none', 'stroke-width': '0.5px'})
	    .call(valueAxis);
	    
	    // Clone lastTick - Inner Line and Text attributes needed  
	    var lastTick = yTicksParentNode.selectAll(".tick:last-of-type");
	    var ltn = lastTick.node();
	    if (ltn && thisWeekHasYData) {
	    	
	    	//Calc axis tickStep
	    	var tg = yTicksParentNode.selectAll(".tick text");
		    var tickStep = Number(tg[0][2].textContent.replace(",", ""))-Number(tg[0][1].textContent.replace(",", "")); 
	    	
		    // Clone ltn atts
		    var attr = ltn.attributes;
		    var lastTickValue = Number(ltn.textContent.replace(",", ""));
		    
		    var lineX2;
		    var lineAttrs = lastTick.selectAll("line").node().attributes;
		    for (var j = 0; j < lineAttrs.length; j++) { // Iterate on attributes and skip on "id"
		        if (lineAttrs[j].name == "x2") lineX2 = lineAttrs[j].value;
		    }
		    var textX;
		    var textDy;
		    var textAtts = lastTick.selectAll("text").node().attributes;
		    for (var j = 0; j < textAtts.length; j++) { // Iterate on attributes and skip on "id"
		        if (textAtts[j].name == "x") textX = textAtts[j].value;
		        if (textAtts[j].name == "dy") textDy = textAtts[j].value;
		    }
		    var lastTickValue = Number(lastTick.selectAll("text").node().textContent.replace(",", ""));
		    
		    // Remove last tick rule
		    if ((yRange[1]-lastTickValue) < tickStep/2) {
		    	lastTick.remove();
		    }
		    
		    // Add new tick for y max value
		    lastTick = yTicksParentNode.append("g");
		    
		    for (var j = 0; j < attr.length; j++) { // Iterate on attributes and skip on "id"
		        if (attr[j].nodeName == "id") continue;
		        lastTick.attr(attr[j].name,attr[j].value);
		    }
		    // Upper tick
		    lastTick.attr("transform", "translate(0, 0)");
		    
		    // Add inner line and text elements
		    lastTick.append("line")         
		    .style("stroke", "black")  
		    .attr("x2", lineX2)     
		    .attr("y2", 0);
		    lastTick.append("text")      
		    .style("text-anchor", "end") 
		    .attr("x", textX)  
		    .attr("dy", textDy) 
		    .attr("y", 0).text(d3.format(",")(yRange[1]));
	    }
	    showstackchart (data, gStackActivities);
	
    };
	    
    function showstackchart (data, gEnter) {
    	
    	var w = props.width - props.margin[1] - props.margin[3];
    	var h = (props.height - props.margin[0] - props.margin[2]) *  props.heighti;
    	var xProps = (props.rtl) ? [ w, 0 ] : [ 0, w ];
    	
    	var x = d3.scale.ordinal().rangeRoundBands(xProps, .3);
    	var y = d3.scale.linear().rangeRound([h, 0]);
    	
    	//Transform data
    	var keys = Object.keys(ActivitiesEnum);
    	data.forEach(function(d) {
    		var y0 = 0;
    		d.activities = []; 
    		keys.forEach(function (fieldactivity, i) {
    			d.activities.push (
	    			{
	    	    		name: fieldactivity, 
	    	    		y0: y0, 
	    	    		y1: y0 += fieldactivity !== ActivitiesstringEnum.outOfHome && props.hiddens.indexOf(ActivitiesClassDevicesEnum[fieldactivity]) === -1 ? +d[fieldactivity] : 0,
	    	    		value: d[fieldactivity],	//for tips 
	    	    		i18n: ActivitiesClassDevicesEnum[fieldactivity] //for tips 
	    	    	}     			
    			);
    		});
    		d.total = d.activities[d.activities.length - 1].y1;    			
    	  });
    	
    	
		x.domain(data.map(function(d) { return dXi(d); }));
  		y.domain([0, d3.max(data, function(d) { return d.total; })]);    // Just Last Week 	  	
    	
  		var widthband=x.rangeBand();
  		
  		var dates = gEnter.selectAll(".Date")
        .data(data)
        .enter().append("g")
        .attr("class", "group")
        .attr("transform", function(d) { 
        		return "translate(" + (X(d) - (widthband/2)) + ",0)"; 
        	});

  		var bars = dates.selectAll("rect")
        .data(function(d) { return d.activities; })
        .enter().append("g").attr("class", "subbar");
  		  		
  		//background bars 
  		bars.append("rect")
  		.attr("width", widthband)  		
  		.attr("y", 0)
  		.attr("height", function(d) { return y(d.y0); })
  		.attr("class", "STEP_COUNT_SENSOR_BACKGROUND")
  		
  		//actual bars
  		bars.append("rect")
        .attr("width", widthband)
        .attr("y", function(d) {
        					if (y(d.y0) - y(d.y1) == 0){return 0;}
    						else {return y(d.y1);}
        })
        .attr("height", function(d) {
							if (y(d.y0) - y(d.y1) == 0){return y(d.y0)}
							else {return y(d.y0) - y(d.y1);}
		})
        .attr("class",  function(d) {
        					if (y(d.y0) - y(d.y1) == 0){return "STEP_COUNT_SENSOR empty_bar";}
    						else {return "STEP_COUNT_SENSOR";}
        				});
      
    }
    
    
    chart.update = function ( axis ) {
    	var keys = Object.keys(ActivitiesEnum);
    	props.data.forEach(function (d) {
			var y0 = 0;
			var group = d.activities;
			
    		keys.forEach(function (fieldactivity, i) {
	    		d.activities[i].y0 = y0; 
	    		d.activities[i].y1 = y0; 
    		});
    		d.total = d.activities[d.activities.length - 1].y1;    			
    	});
		actoutofhome(props.data);

		
		var $this = d3.select(this); 

    	var w = props.width - props.margin[1] - props.margin[3];
    	var h = (props.height - props.margin[0] - props.margin[2]) *  props.heighti;
    	var xProps = (props.rtl) ? [ w, 0 ] : [ 0, w ];
    	var x = d3.scale.ordinal().rangeRoundBands(xProps, .4);
    	var y = d3.scale.linear().rangeRound([h, 0]);
		
  		
    	x.domain(props.data.map(function(d) { return dXi(d,2); }));
    	y.domain([0, d3.max(props.data, function(d) { return d.total; })]);    	  	
    	
		$this.selectAll('.subbar rect')
			.transition().duration(500)
    		.attr("y", function (d) { return y(d.y1); })
    		.attr("height", function (d) { return y(d.y0) - y(d.y1); });
		
		$this.selectAll('.subbar text')
		.transition().duration(500)
		.text(function(d) { return d.y0 === d.y1 || d.name === ActivitiesstringEnum.outOfHome ? "" : d.value  })
        .attr("x", (x.rangeBand() / 2))
        .attr("y", function(d) { return y(d.y1)+ ((y(d.y0) - y(d.y1))/2) ; });

	
    };
        
    function max(data,selector) {
    	selector = selector || DefaultSelector;
        var l = data.length;
        var max = selector(data[0]);
        while (l-- > 0)
            if (selector(data[l]) > max) max = selector(data[l]);
        return max;
    }; 
    
    function min (data,selector) {
    	selector = selector || DefaultSelector;
        var l = this.length;
        var min = selector(data[0]);
        while (l-- > 0)
            if (selector(data[l]) < min) min = selector(data[l]);
        return min;
    };      
    
    function where (data, predicate, context) {
        context = context || window;
        var arr = [];
        var l = data.length;
        for (var i = 0; i < l; i++)
            if (predicate.call(context, data[i], i, data) === true) arr.push(data[i]);
        return arr;
    };  	    
    
    function indexof (data, predicate, context) {
        context = context || window;
        var arr = [];
        var l = data.length;
        for (var i = 0; i < l; i++)
            if (predicate.call(context, data[i], i, data) === true) 
            	return i;
        return -1;
    };  	
    
    function DefaultEqualityComparer(a, b) {
        return a === b || a.valueOf() === b.valueOf();
    };	    
    
    function DefaultSelector(t) { 
        return t;
    }; 	    
    
    return chart;
  
	};
  window.StepCountChart = StepCountChart;
})();
