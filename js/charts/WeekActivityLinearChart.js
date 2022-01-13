(function () {
	"use strict";

  function WeekActivityLinearChart(config) {

    //Enums Constants
    var GraphEnum = Object.freeze({LAST: 0, FIRST: 1, ACTIVITY: 2});
    var ValuesEnum = Object.freeze({noactivity: -1});
    var DeviceEnum = Object.freeze({desktop: 1, mobile: 2});
    var ActivitiesEnum = Object.freeze({restroom: 0, bathroom: 1, diningroom: 2, bedroom: 3, livingroom: 4, otherroom: 5, outOfHome: 6 });
    var ActivitiesstringEnum = Object.freeze({restroom: 'restroom', bathroom: 'bathroom', diningroom: 'diningroom', bedroom: 'bedroom', livingroom: 'livingRoom', otherroom: 'otherroom', outOfHome: 'outOfHome' });
    var ActivitiesClassDevicesEnum = Object.freeze({restroom: 'TOILET_ROOM_SENSOR', bathroom: 'BATHROOM_SENSOR', diningroom: 'DINING_ROOM', bedroom: 'BEDROOM_SENSOR', livingroom: 'LIVING_ROOM', otherroom: 'OTHER_ROOM', outOfHome: 'FRONT_DOOR' });

 // Defaults  
    var props = {
      timeFormat: "%d-%b-%y",
      data:   [],
      xi:     ['date','date','date'],
      yi:     ['lastActivity','firstActivity',''],
      width:  960,
      height: 250, //200
      heighti : [0.20,0.20,0.70], // 0.20,0.20,0.70
      overlapheighti : [0,1,1],
      marginbetweencharts : 65,
      margintopfirstchart : 45, //35
      margin: [ 20, 20, 50, 50 ], // top, right, bottom, left 20, 20, 50, 50
      yOffset: 3,
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
    function dXi(d,graphEnum) { 
    	return d[ props.xi[graphEnum] ]; 
    	};
    function dYi(d,graphEnum) { 
    	return d[ props.yi[graphEnum] ];
    	};

    // Scales
    var x = d3.time.scale.utc();
    var y = d3.scale.linear();

    //Get data for scale
    function X(d,graphEnum) { 
    	return x( dXi(d,graphEnum) ); 
    	};
    	
    //Return scale for a value	
    function Y(d,graphEnum) { 
    	return y( dYi(d,graphEnum) ); 
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
    var accessordXi = [function (d) { return d[ props.xi[GraphEnum.LAST] ]; }, function (d) { return d[ props.xi[GraphEnum.FIRST] ]; }, function (d) { return d[ props.xi[GraphEnum.ACTIVITY] ]; }];
    var accessordYi = [function (d) { return d[ props.yi[GraphEnum.LAST] ]; }, function (d) { return d[ props.yi[GraphEnum.FIRST] ]; }, function (d) { return d[ props.yi[GraphEnum.ACTIVITY] ]; }];
    
    
    //Accessors Scales
    var accessorX = [function (d) { return X(d,GraphEnum.LAST); }, function (d) { return X(d,GraphEnum.FIRST); }, function (d) { return X(d,GraphEnum.ACTIVITY); }];
    var accessorY = [function (d) { return Y(d,GraphEnum.LAST); }, function (d) { return Y(d,GraphEnum.FIRST); }, function (d) { return Y(d,GraphEnum.ACTIVITY); }];

    //Top graph
    function topgraph(graphEnum) {
        var top=0;
        var space=0;
        for (var i = 0; i <= GraphEnum.ACTIVITY; i++)
            if (i<graphEnum)
            	{
            	top+=props.height * (props.heighti[i] * props.overlapheighti[i]);
            	space += props.marginbetweencharts * props.overlapheighti[i];
            	}
    	return top + space; 
    	};

    	
    function normalizeData(d) {
      if ( typeof dXi(d,GraphEnum.LAST) === "string" ) {
    	  d[props.xi[GraphEnum.LAST]] = parseDate( dXi(d, GraphEnum.LAST) );  
      } else {
    	  // TODO: add configuration to fix timezone to dates come in UTC or server timezone
    	  var date = new Date(dXi(d, GraphEnum.LAST));
    	  date.setTime(date.getTime() + ( date.getTimezoneOffset() * 60000 ));
    	  d[props.xi[GraphEnum.LAST]] = date;
      }
      d[props.yi[GraphEnum.LAST]] = convertstringtotime(dYi(d,GraphEnum.LAST));
      d[props.yi[GraphEnum.FIRST]] = convertstringtotime(dYi(d,GraphEnum.FIRST));
      
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

    /*function convertminutestonumeric(minutes){
    	return (minutes * 99 / 59).toFixed(0);
    }*/

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
    

//  
    

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
    	
    	
    	var data = props.data,
    		w = props.width - props.margin[1] - props.margin[3],
        	h = props.height - props.margin[0] - props.margin[2];

    	var xProps = (props.rtl) ? [ w, 0 ] : [ 0, w ],
		    	xRange = d3.extent(data,accessordXi[GraphEnum.LAST]),
		        yRange = d3.extent(data,accessordYi[GraphEnum.LAST]),
		        offset = Math.ceil((yRange[1] - yRange[0]) / 10) * props.yOffset;
		    
		    if (offset === 0) offset = props.yOffset;
		    x.range(xProps).domain(xRange);
		    y.range([ h, 0 ]).domain([ (yRange[0] > 0) ? yRange[0] - offset : yRange[0],
		                                          yRange[1] + offset]);
    	

		    
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
	    
	    //LAST TIME
	    
	    var gLastTime=gEnter.append("g")
	    	.attr("width", props.width)
	        .attr("height", props.height *  props.heighti[GraphEnum.LAST])
	        .attr("transform", "translate(0,"
	                                        + (topgraph(GraphEnum.LAST) + props.device === DeviceEnum.desktop ? props.margintopfirstchart : props.margintopfirstchart - 20) + ")");
	    
	    showlinearchart (data,GraphEnum.LAST, gLastTime );

	    //FIRST TIME
	    
	    
	    var gFirstTime=gEnter.append("g")
	    	.attr("width", props.width)
//	        .attr("height", props.height *  props.heighti[GraphEnum.FIRST])
	        .attr("height", props.height *  props.heighti[GraphEnum.FIRST])
	        .attr("transform", "translate(0,"
	                                        + (topgraph(GraphEnum.FIRST) + props.device === DeviceEnum.desktop ? props.margintopfirstchart : props.margintopfirstchart - 20) + ")");
	    
	    showlinearchart (data,GraphEnum.FIRST, gFirstTime );
	    
	    //STACK ACTIVITIES
	    var gStackActivities=gEnter.append("g")
	    	.attr("width", props.width)
	        .attr("height", h *  props.heighti[GraphEnum.ACTIVITY])
	        .attr("transform", "translate(0,"
	                                        + (props.device === DeviceEnum.desktop ? topgraph(GraphEnum.ACTIVITY) : (topgraph(GraphEnum.ACTIVITY)))  + ")");
	    
	    //+ (props.device === DeviceEnum.desktop ? topgraph(GraphEnum.ACTIVITY) : (topgraph(GraphEnum.ACTIVITY) -25))  + ")");

	    showstackchart (data, GraphEnum.ACTIVITY, gStackActivities);
	
    };
	    
    function showstackchart (data, graphEnum, gEnter) {
    	
    	var w = props.width - props.margin[1] - props.margin[3];
    	var h = (props.height - props.margin[0] - props.margin[2]) *  props.heighti[graphEnum];
    	var xProps = (props.rtl) ? [ w, 0 ] : [ 0, w ];
    	var x = d3.scale.ordinal().rangeRoundBands(xProps, .4);
    	var y = d3.scale.linear().rangeRound([h, 0]);
    
    	
    	//Color
    	var color = d3.scale.ordinal()
    	.range(["#98abc5", "#8a89a6", "#7b6888", "#6b486b", "#a05d56", "#d0743c", "#333"]);
    	color.domain(d3.keys(data[0]).filter(function(key) { return key !== "date" && key !== "firstActivity" && key !== "lastActivity" && key !== "activities" && key !== "total"; }));
    	
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
    	actoutofhome(data);
    	
		x.domain(data.map(function(d) { return dXi(d,graphEnum); }));
  		y.domain([0, d3.max(data, function(d) { return d.total; })]);    	  	
    	
  		var widthband=x.rangeBand();
  		
  		var dates = gEnter.selectAll(".Date")
        .data(data)
        .enter().append("g")
        .attr("class", "group")
        .attr("transform", function(d) { return "translate(" + (X(d,graphEnum) - (widthband/2)) + ",0)"; });

  		var bars = dates.selectAll("rect")
        .data(function(d) { return d.activities; })
        .enter().append("g").attr("class", "subbar");
  		
  		bars.append("rect")
        .attr("width", x.rangeBand())
        .attr("y", function(d) { return y(d.y1); })
        .attr("height", function(d) { return y(d.y0) - y(d.y1); })
        .attr("class", function(d) { return ActivitiesClassDevicesEnum[d.name]; });
  		
  		
  		
  		var xprueba=(x.rangeBand() / 2);
  		
  		bars.append("text")
  		.text(function(d) { return d.y0 === d.y1 || d.name === ActivitiesstringEnum.outOfHome ? "" : d.value  })
        .attr("x", (x.rangeBand() / 2))
//        .attr("x", props.device === DeviceEnum.desktop ? (x.rangeBand() / 2) : ((x.rangeBand() / 2) - 10) )
        .attr("dx", function(d) { return props.device === DeviceEnum.desktop ? "0em" : (d.value.toString().length === 1 ? "-0.25em" : "-0.5em")})
        .attr("dy", "0.5em")
        .attr("y", function(d) { return y(d.y1)+ ((y(d.y0) - y(d.y1))/2) ; })
        .style("stroke", '#ffffff');  		
  		
    }
    
    
    chart.update = function ( axis ) {
    	var keys = Object.keys(ActivitiesEnum);
    	props.data.forEach(function (d) {
			var y0 = 0;
			var group = d.activities;
			
    		keys.forEach(function (fieldactivity, i) {
	    		d.activities[i].y0 = y0; 
	    		d.activities[i].y1 = y0 += fieldactivity !== ActivitiesstringEnum.outOfHome && props.hiddens.indexOf(ActivitiesClassDevicesEnum[fieldactivity]) === -1 ? +d[fieldactivity] : 0; 
    		});
    		d.total = d.activities[d.activities.length - 1].y1;    			
    	});
		actoutofhome(props.data);

		
		var $this = d3.select(this); 

    	var w = props.width - props.margin[1] - props.margin[3];
    	var h = (props.height - props.margin[0] - props.margin[2]) *  props.heighti[GraphEnum.ACTIVITY];
    	var xProps = (props.rtl) ? [ w, 0 ] : [ 0, w ];
    	var x = d3.scale.ordinal().rangeRoundBands(xProps, .4);
    	var y = d3.scale.linear().rangeRound([h, 0]);
		
  		
    	x.domain(props.data.map(function(d) { return dXi(d,GraphEnum.ACTIVITY); }));
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

    
    function actoutofhome(data) {
	    var maxposas = max(data, function(d){ return d.total});
	    where(data,  function(d){ return  d.outOfHome === true }).forEach(function(d) {
		    	var activityoutofhome= where(d.activities, function(d){ return  d.name === ActivitiesstringEnum.outOfHome });
		    	if (activityoutofhome.length > 0)
		    		{
				    	if (d.total === 0)
				    	{
			    			activityoutofhome[0].y0=0;
			    			activityoutofhome[0].y1=maxposas;
			    			activityoutofhome[0].value=maxposas;
				    	}
				    	else
				    		{
				    			activityoutofhome[0].y0=d.total;
				    			activityoutofhome[0].y1=d.total;
				    			activityoutofhome[0].value=0;
				    		}
		    		}
	    });
    };

    
    function showlinearchart (data, graphEnum, gEnter) {
    	var w = props.width - props.margin[1] - props.margin[3];
    	var h = props.height *  props.heighti[graphEnum];
    	var xProps = (props.rtl) ? [ w, 0 ] : [ 0, w ],
    			xRange = d3.extent(data,accessordXi[graphEnum]),
		        yRange = d3.extent(where(data, function(d){ return dYi(d,graphEnum) !== ValuesEnum.noactivity }),accessordYi[graphEnum]),
		        offset = Math.ceil((yRange[1] - yRange[0]) / 10) * props.yOffset;
		    
		    if (offset === 0) offset = props.yOffset;
		    
	    
		    x.range(xProps).domain(xRange);
		
		    var maxlastactivity=d3.max(where(data, function(d){ return dYi(d,GraphEnum.LAST) !== ValuesEnum.noactivity }), function(d) { return dYi(d,GraphEnum.LAST); })
		    var maxfirstactivity=d3.max(where(data, function(d){ return dYi(d,GraphEnum.FIRST) !== ValuesEnum.noactivity }), function(d) { return dYi(d,GraphEnum.FIRST); })
		    var minlastactivity=d3.min(where(data, function(d){ return dYi(d,GraphEnum.LAST) !== ValuesEnum.noactivity }), function(d) { return dYi(d,GraphEnum.LAST); })
		    var minfirstactivity=d3.min(where(data, function(d){ return dYi(d,GraphEnum.FIRST) !== ValuesEnum.noactivity }), function(d) { return dYi(d,GraphEnum.FIRST); })
		    
		    yRange[0]= minlastactivity < minfirstactivity ? minlastactivity : minfirstactivity;   
		    yRange[1]= maxlastactivity > maxfirstactivity ? maxlastactivity : maxfirstactivity;

		    
		    if (yRange[0] === undefined)
		    	{
		    		yRange[0] = 0;
		    		minlastactivity=0;
		    		minfirstactivity=0;	
		    	}

		    if (yRange[1] === undefined)
	    	{
	    		yRange[1] = 23.59;
	    		minlastactivity=23.59;
	    		minfirstactivity=23.59;	
	    	}
		    
//		    yRange[0] = yRange[0] === undefined ? 0 : yRange[0];
//		    yRange[1] = yRange[1] === undefined ? 23.59 : yRange[1];
		    
		    y.range([ h, 0 ]).domain([ yRange[0], yRange[1] ]);

		    
		    
//		    //LINES SEPARATOR MAX AND MIN OF FIRST AND LAST
//		    gEnter.append("line")
//	        .attr("class", "line " + (graphEnum === GraphEnum.FIRST ? "medianfirstactivity" : "medianlastactivity"))
//	        .attr("x1", 0)
//	        .attr("x2", w)
//	        .attr("y1", y(graphEnum === GraphEnum.FIRST ?  minfirstactivity : minlastactivity))
//	        .attr("y2", y(graphEnum === GraphEnum.FIRST ?  minfirstactivity : minlastactivity));	    
//	        
//		    gEnter.append("line")
//	        .attr("class", "line " + (graphEnum === GraphEnum.FIRST ? "medianfirstactivity" : "medianlastactivity"))
//	        .attr("x1", 0)
//	        .attr("x2", w)
//	        .attr("y1", y(graphEnum === GraphEnum.FIRST ?  maxfirstactivity : maxlastactivity))
//	        .attr("y2", y(graphEnum === GraphEnum.FIRST ?  maxfirstactivity : maxlastactivity));
		    
		    
		    //LINES SEPARATOR MAX AND MIN OF FIRST AND LAST
//		    if (props.device === DeviceEnum.desktop)
//		    	{
				    var emptyvalues = graphEnum === GraphEnum.FIRST ?  minfirstactivity === undefined || maxfirstactivity === undefined : minlastactivity === undefined || maxlastactivity === undefined; 
				    if (!emptyvalues)
				    {
			    		var medianFirstActivity = (minfirstactivity + maxfirstactivity) / 2;
			    		var medianLastActivity = (minlastactivity + maxlastactivity) / 2;
			    		var differenceBetweenMedians = medianFirstActivity - medianLastActivity;
			    		var closeToEachOther = Math.abs(differenceBetweenMedians) < 4;
			    		var additionalInterval = (closeToEachOther) ? (4 - differenceBetweenMedians) : 0;

					    gEnter.append("line")
				        .attr("class", "line " + (graphEnum === GraphEnum.FIRST ? "medianfirstactivity" : "medianlastactivity"))
				        .attr("x1", 0 - (props.margin[1]/3))
				        .attr("x2", w + (props.margin[3]/3))
				        .attr("y1", y(graphEnum === GraphEnum.FIRST ? (medianFirstActivity + additionalInterval) : medianLastActivity))
				        .attr("y2", y(graphEnum === GraphEnum.FIRST ? (medianFirstActivity + additionalInterval) : medianLastActivity))	    
				        
				        if (props.device === DeviceEnum.desktop)
				        {
					         gEnter.append("text")
						     .attr("class", (graphEnum === GraphEnum.FIRST ? "first_activity" : "last_activity"))
						     .attr("x", (props.rtl) ?  w + 100  : -100)
						     .attr("y", y(graphEnum === GraphEnum.FIRST ? (medianFirstActivity + additionalInterval) : medianLastActivity))
						     .text(I18n.t((graphEnum === GraphEnum.FIRST ? 'activity_index.first_activity' : 'activity_index.last_activity')));
				        }
			    	}
//		    	}		    
		    

	    // Transform data adding two properties nextfirstActivity and nextlastActivity
				    
	    var sections = [];
	    var currentSection = [];
	    data.forEach(function(currentItem) {
	    	var currenIndex=indexof(data, function(item){ return item.date === currentItem.date });
	    	var currentValue=dYi(currentItem,graphEnum);
	    	if (currentValue !== ValuesEnum.noactivity)
	    		{
    				if (currentSection.length === 0)
    					sections.push(currentSection);
    				currentSection.push(currentItem);
	    		}	
	    	else
	    		currentSection  = [];
	    });
	   
	    var line = d3.svg.line().x(accessorX[graphEnum]).y(accessorY[graphEnum]);
	    sections.forEach(function(section) {
	    	gEnter.append("path")
	    	.attr("class", "nailine")
	    	.attr("d", line(section));
	    });
	    
				    
    	//data.forEach(function(d) {
	    //	var currenIndex=indexof(data, function(item){ return item.date === d.date });
	    //	d.nextfirstActivity = currenIndex !== data.length-1 ? data[currenIndex+1].firstActivity : data[currenIndex].firstActivity;
	    //	d.nextlastActivity = currenIndex !== data.length-1 ? data[currenIndex+1].lastActivity : data[currenIndex].lastActivity; 
	    //  });		    
		    
		//var line = d3.svg.line().x(accessorX[graphEnum]).y(accessorY[graphEnum]);
		//Lines
    	//gEnter.append("path")
    	//.attr("class", "nailine")
    	//.attr("d", line(where(data, function(d){ return dYi(d,graphEnum) !== ValuesEnum.noactivity && d["next"+props.yi[graphEnum]] !== ValuesEnum.noactivity })));
	    
        var l = data.length;
        for (var i = 0; i < l; i++)
        	point(data[i], graphEnum,gEnter, xRange, yRange);
        

    }

    function point(d,graphEnum,gEnter, xRange, yRange) 
    {
	      var yValue = dYi(d,graphEnum)
	      
//	      if (yValue === 0) return;
	      
       	  var x = X(d,graphEnum)
	      var y = Y(d,graphEnum)
	      var point  = gEnter.append("g")
	            .attr("class", "naipoint");
	      
      
	      var tFill = point.append("rect"),
	    	className = (yValue > -1 ) ? " max" : " min",
	        t = point.append("text").text(converttimetostring(yValue)).attr("class", "naipoint-text " + className),
	        tBox = t[0][0].getBBox(),
	        tw   = tBox.width,
	        th   = tBox.height,
	        fSize   = parseInt(window.getComputedStyle(t[0][0], null).getPropertyValue('font-size'));
	        point.attr("class", point.attr("class") + className);
	        t.attr("x", X(d,graphEnum))
	          .attr("y", graphEnum === GraphEnum.LAST ? (Y(d,graphEnum) - (fSize * 0.86)) : (Y(d,graphEnum) + (fSize * 1.6)))
	          .style("visibility", yValue === ValuesEnum.noactivity ? "hidden" : "visible");
	        tFill.attr("x", t.attr("x") - tw /2)
	          .attr("y", t.attr("y") - (fSize * 1.14) )
	          .attr("width", tw)
	          .attr("height", th)
	          .style("visibility", yValue === ValuesEnum.noactivity ? "hidden" : "visible");	        
	      
	      point.append("circle")
	      	.attr("class", graphEnum === GraphEnum.FIRST ? "showable_datumfirst" :  "showable_datum")
	      	.attr("cx", x)
	      	.attr("cy", y)
//	      	.attr("r",  9)
	      	.attr("r", props.device === DeviceEnum.desktop ? 9 : 4)
	        .style("visibility", yValue === ValuesEnum.noactivity ? "hidden" : "visible");
	      
	      if (props.extendActiveArea) {
	      // bigger invisible area, so it's easier to touch it to make the tip appear
		      point.append("circle")
		      	.attr("class", "bounding_box")
		      	.attr("cx", x)
		      	.attr("cy", y)
		      	.attr("r", 20)
		        .style("visibility", yValue === ValuesEnum.noactivity ? "hidden" : "visible");
	      };
	    };
    
	    
    //Group By
    // var res = groupBy(data, function(t){ return dXi(d).getDate() }); 
    function groupby(data, selector, comparer) {
    	var grp = [];
        var l = data.length;
        comparer = comparer || DefaultEqualityComparer;
        selector = selector || DefaultSelector;

        for (var i = 0; i < l; i++) {
            var k = selector(data[i]);
            var g = grp.first(function (u) { return comparer(u.key, k); });

            if (!g) {
                g = [];
                g.key = k;
                grp.push(g);
            }

            g.push(data[i]);
        }
        return grp;	    	
    };
    
    //var arr1 = [1, 2, 3, 4, 5, 6, 7, 8];
    //((var max1 = arr.max();  // 8 	    
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
    
    //var arr = [1, 2, 3, 4, 5];
    //var res = arr.where(function(t){ return t > 2 }) ;  // [3, 4, 5] 
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
  window.WeekActivityLinearChart = WeekActivityLinearChart;
})();
