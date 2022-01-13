/**
 * Modals reports grafics
 */

 import DOMPurify from 'dompurify';
$(function ($) {
	
	if ( $('#monitoring').length || $('#analysis').length ) {
		sessionStorage.setItem('reportBackUrl', window.location.href);
	}
	
	$('[data-report-back]').on('click', function () {
		window.location.href = sessionStorage.getItem('reportBackUrl') || $.app.rootPath + "/admin/residents_in_alarm"; 
	});
	
	// DAY STORY
	(function () {
		if ( $('.day-story').length ) {
			var $container = DOMPurify.sanitize($('.day-story')),
				intervalTiming = 250,
				$bars, $points,
				$lines;
			
			var finalWidth;
			
			var $datepicker     = $('#report_date');
			
			//ESC17-6138 If computer date is ahead server date we avoid maxdate=1 options
			var computerDate = new Date();
			var datepickerDate = $datepicker.datepicker('getDate');
			datepickerDate.setDate(datepickerDate.getDate() +1);
			var datepickerDay = datepickerDate.getDate();
			var computerDay = computerDate.getDate();
	
			
			if (datepickerDay == computerDay){
				$datepicker.datepicker( "option", "maxDate", 0);
			}
			
			function positionBar ($bar) {
				var horary = $bar.data('horary').split('_'),
					$lineStart = $lines.find('.line:eq(' + horary[0] + ')'),
					$lineEnd   = $lines.find('.line:eq(' + horary[1] + ')');
				if ($lineStart.length > 0 && $lineEnd.length > 0) {
					if ($.app.isRTL) {
						var linesWidth    = $lines.width(),
							startPosition = linesWidth - $lineStart.position().left,
						
							finalWidth    = linesWidth - $lineEnd.position().left - startPosition;
						$bar.css({ 'right': startPosition, width: finalWidth });
					} else {
						var startPosition = $lineStart.position().left,
						
						finalWidth    = $lineEnd.position().left - startPosition;
						$bar.css({ 'left': startPosition, width: finalWidth });
					}
				}
				// Check Missing Info
				if($bar.hasClass( "missinginfo" )) {
					var IMAGE_W = 40;
					var halfImageW = IMAGE_W / 2;
					// Has start or end missing info images
					var top = (IMAGE_W - $bar.height())/2;
					top='-' + top + 'px';
					
					// EIC15-2162 Check if there is another missinginfo bar that share with this start or end
					var dupAtStart = false, dupAtEnd = false;
					var myHorary = $bar.data('horary').split('_');
					$bar.siblings('.missinginfo').each(function(i) { 
						var horary = $(this).data('horary').split('_');
						if (horary[0]===myHorary[1]) {
							dupAtEnd = true;
						} 
						if (horary[1]===myHorary[0]){
							dupAtStart = true;
						}
						if (dupAtEnd && dupAtStart) {
							return false;
						}
						
					});
					
					$bar.children()
						.css( "top",top)
						.each(function(i) { 
							if ($(this).hasClass( "start" )) {
								if (dupAtStart) {
									$(this).css("display", "none");
								} else {
									$(this).css( "left",'-' + halfImageW+'px');
								}
							} else {
								 if (dupAtEnd) {
									 $(this).css("display", "none");
								 } else {
									 var left = ''+(finalWidth - halfImageW);
										$(this).css( "left",left+'px');
								 }
							}
						})
						.css("width",IMAGE_W + "px")
						.css("height",IMAGE_W + "px");
				}
			}
			
			function positionPoint($point, drawBars) {
				var $line = $lines.find('.line:eq(' + $point.data('point') + ')');
	
				if ( $line.length ) {
					if ( $.app.isRTL ) {						
						$point.css({ right: $lines.width() - $line.position().left });
					} else {
						$point.css({ left: $line.position().left });
					}
					
					if (drawBars) {
						if ( $point.is('.on') && !$point.nextAll('.off[data-point='+ $point.data('point') +']').length ) {
							var $next = $point.next();
							
							if ($point.data('point') < $next.data('point')) {
								$('<span class="bar" data-horary="'+ $point.data('point') +'_'+ $next.data('point') +'"></span>').appendTo($point.parent());
							} else if ( $point.data('point') != $next.data('point') && !$point.nextAll('.off').length ) {
								var today = new Date($container.find('.grafics-carousel').data('now')),
									nowTime = new Date($container.find('.grafics-carousel').data('now'));
								today.setHours(0, 0, 0, 0);
								if (today >= $('.hasDatepicker').datepicker("getDate")){
									var end = (today > $('.hasDatepicker').datepicker("getDate")) ? 48 : 1 + calculatePosition(nowTime);
									$('<span class="bar" data-horary="'+ $point.data('point') +'_' + end + '"></span>').appendTo($point.parent());
								}
							}
						}
						
						if ( $point.is('.off') && !$point.siblings('.on[data-point='+ $point.data('point') +']').length ) {
							if ( !$point.prevAll().length ){
								$('<span class="bar" data-horary="0_'+ $point.data('point') +'"></span>').appendTo($point.parent());
							} else if( $point.prev().is('.off') ) {
								$('<span class="bar" data-horary="'+ $point.prev('.off').data('point') +'_'+ $point.data('point') +'"></span>').appendTo($point.parent());
							}
						}
					}
				}
			};
	
			function update(el, newDate) {
				var $this = $(el);
				var  url  = $this.data('url') + $.datepicker.formatDate('yymmdd', newDate);
                
				formatDateDatePicker();
				
				$.ajax({
					url: url, 
					beforeSend: function(){$.app.loader.show();}
				})
				.done(function(data){ 
					$container.html(data);
					setup();
				})
				.always(function(data){ 
					$.app.loader.hide(); 
				});
			}
			
			function setup() {
				$points =  $container.find('.point');
				$lines = $container.find('.lines');
				
				var i = 0;
				
				$points.each(function () {
					positionPoint( $(this), true );
				});
				
				$bars   = $container.find('.bar');
				
				if ($container.data('interval')) {
					clearInterval($container.data('interval'));
					$container.removeData('interval');
				}
				
				// Interval for first delayed bars animation
				$container.data('intetval', setInterval(function () {
					if (i >= $bars.length) {
						clearInterval($container.data('interval'));
						$container.removeData('interval');
						return false;
					}
					
					positionBar( $($bars.get(i)) );
					
					i++;
				}, intervalTiming));
				
				var qTipPosition = ($.app.isRTL) ? { my: 'bottom left', at: 'top left' } : { my: 'bottom right', at: 'top right' };
				$bars.filter('[title]').qtip({ content: {	text: tooltipText }, position: qTipPosition });
				$points.filter('[title]').qtip({ content: { text: tooltipText }, position: qTipPosition });
			}
			
			function tooltipText() {
				return '<span class="lapse">' + $(this).attr('title').replace(/#/g, '</span><span class="lapse">') + '</span>';
			}
			
			setup();
			
			var repositionTimeout;
			function repositionBars() {
				if (repositionTimeout) clearTimeout(repositionTimeout);
				repositionTimeout = setTimeout(function () {
					$bars.each(function () {
						positionBar( $(this) );
					});
					
					$points.each(function () {
						positionPoint( $(this), false );
					});
				}, 50);
			}
			
			function calculatePosition(time) {
				var position = time.getHours() * 2;
				
				if ( time.getMinutes() > 15 ) {
					position += 1;
				} else if (time.getMinutes() > 45) {
					position += 2;
				}
				
				return position;
			}
			
			$(window).on('resize', repositionBars);
			$(window).on('zoom', repositionBars);
			
			$('.hasDatepicker').on('change', function (event) {
				var now  = new Date().setHours(0, 0, 0, 0),
					date = $(this).datepicker('getDate');
				
				formatDateDatePicker();
				
				if ( date < now ) {
					$('.next-day').show();
				} else {
					$('.next-day').hide();
				}
				
				update(this, date);
			});
	
			$('.prev-day').on('click', function (event) {
				//console.log("prev-day")
				var $this = $(this).next();
				var date  = $this.datepicker('getDate');
				var now   = new Date().setHours(0, 0, 0);
				
				
				date.setDate(date.getDate() - 1);
				$this.datepicker('setDate', date);
				
				if ( date < now ) {
					$('.next-day').show();
				}
				
				update($this, date);
			});
	
			$('.next-day').on('click', function (event) {
				//console.log("next-day")
				var $this = $(this).prev();
				var date  = $this.datepicker('getDate');
				var now   = new Date().setHours(0, 0, 0, 0);
				
				date.setDate(date.getDate() + 1);
				
				$this.datepicker('setDate', date);
				
				if ( date >= now ) {
					$('.next-day').hide();
				}
				
				update($this, date);
			});
			
			$('.day-story .label').qtip({ content: { text: function(){ return $(this).html(); } }});
		}
	})();
	
	// ACTIVITY LEVEL
	(function () {
		if ( $('.activity-level').length ) {
			var $container =DOMPurify.sanitize($('.activity-level')),
				$bars  = $container.find('.bar');
			
			setTimeout(function () {
				$bars.each(function () {
					var $bar = $(this);
					$bar.css('height', $bar.data('percent'));
				});
			}, 250);
		}
	})();
	
	// WEEKLY REPORT
	(function () {
		if ( $('.weekly-report').length ) {
			var $container = DOMPurify.sanitize($('.weekly-report')),
				i, $grafic, $bars, $lines;
			
			var $datepicker     = $('#report_date');
			$datepicker.datepicker( "option", "maxDate", -1);
		
			function calculateBarsPositions (ev) {
			  for (i = $bars.length - 1; i >= 0; i--) {
		    	var $bar     = $($bars[i]),
		      		position = $bar.data('position').split('_'),
		          	start    = position[0],
		          	end      = position[1],
		          	diff     = end - start,
		          	priority = 50 - $bar.data('priority');
		    	
		    		if (false && $.app.isRTL) { // ADDED FALSE FOR FORCE GRAFIC IN LTL
		    			var linesWidth = $lines.width(),
		    				initialPosition = linesWidth - $lines.eq(start).position().left,
			          		finalWidth      = linesWidth - $lines.eq(end).position().left - initialPosition;
		    			$bar.css({ right: initialPosition, width: finalWidth, zIndex: priority });
		    		} else {
		    			var initialPosition = $lines.eq(start).position().left,
		          			finalWidth      = $lines.eq(end).position().left - initialPosition;
		    			$bar.css({ left: initialPosition, width: finalWidth, zIndex: priority });
		    		}
		    		
		    		// Check Missing Info
					if($bar.hasClass( "missinginfo" )) {
						var IMAGE_W = 40;
						var halfImageW = IMAGE_W / 2;
						// Has start or end missing info images
						var top = (IMAGE_W - $bar.height())/2;
						top='-' + top + 'px';
						$bar.children()
							.css( "top",top)
							.each(function(i) { 
								if ($(this).hasClass( "start" )) {
									$(this).css( "left",'-' + halfImageW+'px');
								} else {
									var left = ''+(finalWidth - halfImageW);
									$(this).css( "left",left+'px');
								}
							})
							.css("width",IMAGE_W + "px")
							.css("height",IMAGE_W + "px");
					}
		      		
		    	};
		  	}
		
			function setup() {
				var qTipPosition = ($.app.isRTL) ? { my: 'bottom left', at: 'top left' } : { my: 'bottom right', at: 'top right' };
				
				$grafic = $container.find('.detail_grafic .grafic_container'),
				$bars   = $grafic.find('.bar'),
		      	$lines  = $grafic.find('.lines li');
				
			  	
				$bars.qtip({ content: { attr: 'title' }, position: qTipPosition });
				
				setTimeout(calculateBarsPositions, 10);
				
				$container.find(':checkbox').on('change', function (event) {
			  		var $check = $(this);
			  		if ($check.is(':checked')) {
			  			$bars.filter($check.val()).removeClass('hidden');
			  		} else {
			  			$bars.filter($check.val()).addClass('hidden');
			  		}
			  	});
			}
			
			var repositionTimeout;
			function repositionBars() {
				if (repositionTimeout) clearTimeout(repositionTimeout);
				
				repositionTimeout = setTimeout(calculateBarsPositions, 50);
			};
			
			$(window).on('resize', repositionBars);
			$(window).on('zoom', repositionBars);
			
			function update(el, newDate) {
				var $this = $(el);
				var  url  = $this.data('url') + $.datepicker.formatDate('yymmdd', newDate);
				var filterStates = [];
		
				saveFilterStates();
				
				$.ajax({
					url: url, 
					beforeSend: function(){$.app.loader.show();}
				})
				.done(function(data){ 
					$container.html(data);
					setFilterStates();
					$container.find(':checkbox').uniform();
					setup();
					setBarsVisibility();
				})
				.always(function(data){ $.app.loader.hide(); });
				
				function saveFilterStates() {
					$container.find(':checkbox').each( function(index, elem) {
						filterStates[$(elem).val()] = $(elem).is(':checked');
					});
				}
				
				function setFilterStates() {
					$container.find(':checkbox').each( function(index, elem) {
						if (filterStates[$(elem).val()] === false) {
							//$(elem).removeAttr('checked');
							$(elem).prop('checked', false);
						}
					});
				}
				
				function setBarsVisibility() {
					$container.find(':checkbox:unchecked').each( function(index, elem) {
						$bars.filter($(elem).val()).addClass('hidden');
					});
				}
			}	
		  	
		  	setup();
		  	
		  	$('[data-report-print]').on('click', function (event) {
		  		/*var $print = $.app.modals.$tpl($container.html(), 'print', true)
		  			.addClass('weekly-report')
		  			.appendTo('body');*/
		  		
		  		$('body').addClass('print');
	  			calculateBarsPositions();
	  			
  				window.print();
  			
  				$('body').removeClass('print');
  				calculateBarsPositions();
		  		 		  		
		  		return false; 
		  	});
		  	
		  	$('.hasDatepicker').on('change', function (event) {
		  		var $this = $(this),
		  			date  =  $(this).datepicker('getDate'),
		  			now   = new Date();
		  		
		  		formatDateDatePicker();
		  		now.setHours(0, 0, 0, 0);
		  		
		  		if ( date < now ) {
		  			$('.next-day').show();
		  		} else {
		  			date = now;
		  			$this.datepicker('setDate', date);
		  			$('.next-day').hide();
		  		}
		  		
				update(this, date);
			});
		  	
		  	$('.prev-day').on('click', function (event) {
				var $this = $(this).next();
				var date  = $this.datepicker('getDate'),
					now   = new Date();
				
				now.setHours(0, 0, 0, 0);
				date.setDate(date.getDate() - 7);
				$this.datepicker('setDate', date);
				formatDateDatePicker();
				
				if ( date < now ) {
					$('.next-day').show();
				}
				
				update($this, date);
			});
		
			$('.next-day').on('click', function (event) {
				var $this = $(this).prev();
				var date  = $this.datepicker('getDate');
				var today = new Date();
				var yesterday = new Date();
				yesterday.setDate(today.getDate() - 1);
				
				yesterday.setHours(0, 0, 0, 0);
				date.setDate(date.getDate() + 7);
				
				if ( date >= yesterday ){
					date = yesterday;
					$('.next-day').hide();
				}
				
				$this.datepicker('setDate', date);
				update($this, date);
			});
		}
	})();
	
	// Activity Index
	(function () {
		var chartContainer = document.getElementById('activity_index_chart_old');
		
		if ( chartContainer ) {
			var $datepicker     = $('#report_date'),
				w = chartContainer.clientWidth - 5,
				h = chartContainer.clientHeight - 5;
			
			var chart = WeekActivityLinearChartOld({
					timeFormat: '%a %b %d %Y',
					yi: 'index',
					width:  w,
					height: h,
					margin: [ h * 0.15, // Top    15%
					          w * 0.1,  // Right  10% 
					          h * 0.20, // Bottom 20% 
					          w * 0.1 ], // Left   10%
					rtl: $.app.isRTL,
					locale: (typeof d3GlobLocale !== "undefined") ? d3GlobLocale : false
				});
			
			$.app.loader.show();
			
			var url = getUrl();
			d3.json(url, function (error, data) {
				if (!error){
					$.app.loader.hide();
					window.history.replaceState(data, 'initial');
					d3.select(chartContainer).datum(data).call(chart);
					tips();
					
					$(window).on('resize', debounce(function () {
						resize();
						tips();
					}, 50));
					
					window.addEventListener('popstate', function (event) {
						if ( event.state ) {
							var date = $.datepicker.parseDate('yymmdd', window.location.pathname.match(/\d+$/)[0]),
								now  = new Date();
							$datepicker.datepicker("setDate", date);
							now.setHours(0, 0, 0, 0);
							
							if ( date < now ) {
					  			$('.next-day').show();
					  		} else {
					  			$('.next-day').hide();
					  		}
							
							chart.data(event.state).render.call(chartContainer, true);
							tips();
						}	
					});
				} else{
					$.app.loader.hide();
					$('#system-error-modal').addClass('active');
				}
				
			});
			
			function update(date) {
				var url = getUrl();
				
				$.app.loader.show();
				
				d3.json(url, function (error, data) {					
					$.app.loader.hide();
					window.history.replaceState(data, url.replace(/\.json$/, ''), url.replace(/\.json$/, ''));
					
					chart.data(data).render.call(chartContainer);
					tips();
				});
			};
			
			function resize() {
				var w = chartContainer.clientWidth - 5,
					h = chartContainer.clientHeight - 5,
					margin = [ h * 0.15,  // Top    15%
					           w * 0.1,   // Right  10% 
					           h * 0.20,  // Bottom 20% 
					           w * 0.1 ]; // Left   10%
				
				
				
				chart.width(w)
					.height(h)
					.margin(margin)
					.render.call(chartContainer);
			};
			
			function tips() {
				chart.data().forEach(function (d, i) {
					var tipOpts = {
							content: { text: I18n.t('activity_index.total_activities') + ' <strong>' + d.index + '</strong>' },
							position: { my: 'bottom right', at: 'top center', viewport: $(chartContainer) },
							style: { classes: 'qtip-clear-blue qtip-padding' }
					};
					
					$(chartContainer).find('.point:eq(' + i + ') circle').qtip(tipOpts)
				});
			};
			
			$('.hasDatepicker').on('change', function (event) {
		  		var $this = $(this),
		  			date  =  $(this).datepicker('getDate'),
		  			now   = new Date();
		  		
		  		formatDateDatePicker();
		  		now.setHours(0, 0, 0, 0);
		  		
		  		if ( date < now ) {
		  			$('.next-day').show();
		  		} else {
		  			date = now;
		  			$this.datepicker('setDate', date);
		  			$('.next-day').hide();
		  		}
		  		
				update(date);
			});
		  	
		  	$('.prev-day').on('click', function (event) {
				var $this = $(this).next();
				var date  = $this.datepicker('getDate'),
					now   = new Date();
				
				now.setHours(0, 0, 0, 0);
				date.setDate(date.getDate() - 7);
				$this.datepicker('setDate', date);
				
				if ( date < now ) {
					$('.next-day').show();
				}
				
				update(date);
			});
		
			$('.next-day').on('click', function (event) {
				var $this = $(this).prev();
				var date  = $this.datepicker('getDate');
				var today = new Date();
				
				today.setHours(0, 0, 0, 0);
				date.setDate(date.getDate() + 7);
				
				if ( date >= today ){
					date = today;
					$('.next-day').hide();
				}
				
				$this.datepicker('setDate', date);
				update(date);
			});
			
			$('[data-report-print]').on('click', function (event) {
				$('body').addClass('print');
	  			resize();
	  			
  				window.print();
  			
  				$('body').removeClass('print');
  				resize();
		  		 		  		
		  		return false; 
			});
		}
		
		function getCurrentDate() {
			return $datepicker.datepicker('getDate');
		};
		
		function getUrl(){
			return chartContainer.getAttribute('data-url') + d3.time.format('%Y%m%d')(getCurrentDate()) + '.json';
		};
		
		function debounce(fn, wait) {
			var timeout;
			
			return function () {
				var context = this,
					args    = arguments,
					later   = function () {
						timeout = null;
						fn.apply(context, args);
					}
				clearTimeout(timeout);
				timeout = setTimeout(later, wait);
			};
		};
		
		function fakeData(date) {
			var startDate = (date) ? date : new Date(),
				data      = [];
			
			startDate.setDate( startDate.getDate() - 7 );
			
			for (var i = 0; i < 7; i++) {
				startDate.setDate(startDate.getDate() + 1);
				
				data.push({
					date: startDate.toDateString(),
					index:  rand(15, 35)
				});
			}
			
			return data;
		};
	})();

	
	// New Activity Index
	(function () {
		var chartContainer = document.getElementById('activity_index_chart');
		
		if ( chartContainer ) {
			
			var ActivitiesClassDevicesEnum = Object.freeze({restroom: 'TOILET_ROOM_SENSOR', bathroom: 'BATHROOM_SENSOR', diningroom: 'DINING_ROOM', bedroom: 'BEDROOM_SENSOR', livingroom: 'LIVING_ROOM', otherroom: 'OTHER_ROOM', outOfHome: 'FRONT_DOOR' });
			var ActivitiesstringEnum = Object.freeze({restroom: 'restroom', bathroom: 'bathroom', diningroom: 'diningroom', bedroom: 'bedroom', livingroom: 'livingRoom', otherroom: 'otherroom', outOfHome: 'outOfHome' });
			
			$types_controls = $('#bars_types_controls');
			
			var $datepicker     = $('#report_date'),
				w = chartContainer.clientWidth - 5,
				h = chartContainer.clientHeight - 5;
			
			// EIC15-2055 
			$datepicker.datepicker( "option", "maxDate", -1);
//			console.log("Max Date: ", $datepicker.datepicker( "option", "maxDate" ));
			
			var chart = WeekActivityLinearChart({
					timeFormat: '%a %b %d %Y',
					width:  w,
					height: h,
					margin: [ h * 0.1, // Top    1%
					          w * 0.1,  // Right  10% 
					          h * 0.1, // Bottom 20% 
					          w * 0.1 ], // Left   10%
					rtl: $.app.isRTL,
					locale: (typeof d3GlobLocale !== "undefined") ? d3GlobLocale : false
				});
			
			$.app.loader.show();
			
			var url = getUrl();
			d3.json(url, function (error, data) {
				if (!error){
					
						
					$.app.loader.hide();
					window.history.replaceState(data, 'initial');
					updateControls(data);
					d3.select(chartContainer).datum(data).call(chart);
					tips();
					
					$(window).on('resize', debounce(function () {
						resize();
						tips();
					}, 50));
					
					$types_controls.on('change', ':checkbox', function () {
						var fn = (this.checked) ? chart.show : chart.hide;
						fn.call(chartContainer, this.value);
					});
					
					window.addEventListener('popstate', function (event) {
						if ( event.state ) {
							var date = $.datepicker.parseDate('yymmdd', window.location.pathname.match(/\d+$/)[0]),
								now  = new Date();
							$datepicker.datepicker("setDate", date);
							now.setHours(0, 0, 0, 0);
							
							if ( date < now ) {
					  			$('.next-day').show();
					  		} else {
					  			$('.next-day').hide();
					  		}
							
							chart.data(event.state).render.call(chartContainer, true);
							tips();
						}	
					});
					
				} else{
					$.app.loader.hide();
					$('#system-error-modal').addClass('active');
				}							

			});
			
			function update(date) {
				var url = getUrl();
				
				 formatDateDatePicker();
				
				$.app.loader.show();
				
				d3.json(url, function (error, data) {					
					$.app.loader.hide();
					window.history.replaceState(data, url.replace(/\.json$/, ''), url.replace(/\.json$/, ''));
					
					chart.data(data).render.call(chartContainer);
					updateControls(data);
					tips();
				});
			};
						
			function updateControls(data) {
				//Generate array of activities with posas
				var ExistingTypes = {};
				var keys = Object.keys(ActivitiesClassDevicesEnum);
				keys.forEach(function (fieldactivity, j) {
					data.forEach(function (dailyactivities, i) {
						if (fieldactivity === ActivitiesstringEnum.outOfHome ? dailyactivities[fieldactivity] : dailyactivities[fieldactivity] > 0)
							if (!ExistingTypes[ActivitiesClassDevicesEnum[fieldactivity]]) {
								ExistingTypes[ActivitiesClassDevicesEnum[fieldactivity]] = {};
								return;
						}
					});
				});				

				var hiddensbars=chart.hiddens();
				
				//Generate html for icons				
				var barTpls = [];
				for (var key in ExistingTypes) {
					barTpls.push(showBarTpl(key, hiddensbars.indexOf(key) !== -1));
				}
				
				$types_controls.html("");
				for (var i in barTpls) {
					barTpls[i].appendTo($types_controls);
				}
				$types_controls.find(':checkbox').uniform();
				$.app.scroll.resize();
			};
			
			
			function showBarTpl(key, hiddenbar) {
				var disabled = false;
				var className = key.toLowerCase();
				var valuetext = I18n.t('monthly_report.activities.' + key);
				var checked = ($('#show_' + key).parent().hasClass('checked') || !$('#show_' + key).length);
				var checked = !hiddenbar;
				var html = ["<label for='show_", key, "' class='show_devices_control", disabled ? " disabled" : "" ,"' id='label_control_", key ,"'>",
					 	"<div><div class='location_square_legend ", className, "'></div>",
					 	"<span class='icon location ", className, "'></span></div>",
						"<div><input type='checkbox' id='show_", key, "' value='", key ,"'", checked ? ' checked' : '', disabled ? ' disabled' : '' , "/></span>",
					 	"<span class='label-text'>", I18n.t('monthly_report.activities.' + key), "</span></div>" ];
				return $(html.join(""));
			}
			
			function resize() {
				var w = chartContainer.clientWidth - 5,
					h = chartContainer.clientHeight - 5,
					margin = [ h * 0.15,  // Top    15%
					           w * 0.1,   // Right  10% 
					           h * 0.20,  // Bottom 20% 
					           w * 0.1 ]; // Left   10%
												
				chart.width(w)
					.height(h)
					.margin(margin)
					.render.call(chartContainer);
			};
			
			function tips() {
				chart.data().forEach(function (d, i) {
					var tipOptslastactivity = {
							content: { text: I18n.t('activity_index.last_activity_tip') + ' <strong>' + chart.converttimetostring(d.lastActivity) + '</strong>' },
							position: { my: 'bottom right', at: 'top center', viewport: $(chartContainer) },
							style: { classes: 'qtip-clear-blue qtip-padding' }
					};
					$(chartContainer).find('.naipoint:eq(' + i + ') circle').qtip(tipOptslastactivity)
					
					var tipOptsfirstactivity = {
						content: { text: I18n.t('activity_index.first_activity_tip') + ' <strong>' + chart.converttimetostring(d.firstActivity) + '</strong>' },
						position: { my: 'bottom right', at: 'top center', viewport: $(chartContainer) },
						style: { classes: 'qtip-clear-blue qtip-padding' }
					};
					$(chartContainer).find('.naipoint:eq(' + (chart.data().length + i) + ') circle').qtip(tipOptsfirstactivity)
				});

				chart.data().forEach(function (d, i) {
					d.activities.forEach(function (subbar, j) {
						var tipOptsbardevice = {
//								content: { text:  I18n.t('monthly_report.activities.' + subbar.i18n) + ' <strong>' + subbar.value + '</strong>' },
								content: { text:  I18n.t('monthly_report.activities.' + subbar.i18n) + ' ' + subbar.value},
								position: { my: 'bottom right', at: 'top center', viewport: $(chartContainer) },
								style: { classes: 'qtip-clear-blue qtip-padding' }
						};
						$(chartContainer).find('.subbar:eq(' + (((i)*7)+j) + ') rect').qtip(tipOptsbardevice)
					});
				});
			};
			
			
			
			$('.hasDatepicker').on('change', function (event) {
		  		var $this = $(this),
		  			date  =  $(this).datepicker('getDate'),
		  			now   = new Date();
	  		
		  		formatDateDatePicker();
		  		now.setHours(0, 0, 0, 0);
		  		if ( date < now ) {
		  			$('.next-day').show();
		  		} else {
		  			date = now;
		  			$this.datepicker('setDate', date);
		  			$('.next-day').hide();
		  		}
				update(date);
			});
		  	
		  	$('.prev-day').on('click', function (event) {
				var $this = $(this).next();
				var date  = $this.datepicker('getDate'),
					now   = new Date();
				
				now.setHours(0, 0, 0, 0);
				date.setDate(date.getDate() - 7);
				$this.datepicker('setDate', date);
				
				if ( date < now ) {
					$('.next-day').show();
				}
				
				update(date);
			});
		
			$('.next-day').on('click', function (event) {
				var $this = $(this).prev();
				var date  = $this.datepicker('getDate');
				var today = new Date();
				
				today.setHours(0, 0, 0, 0);
				date.setDate(date.getDate() + 7);
				
				if ( date >= today ){
					date = today;
					$('.next-day').hide();
				}
				
				$this.datepicker('setDate', date);
				update(date);
			});
			
			$('[data-report-print]').on('click', function (event) {
				$('body').addClass('print');
	  			resize();
	  			
  				window.print();
  			
  				$('body').removeClass('print');
  				resize();
		  		 		  		
		  		return false; 
			});
		}
		
		function getCurrentDate() {
			return $datepicker.datepicker('getDate');
		};
		
		function getUrl(){
			return chartContainer.getAttribute('data-url') + d3.time.format('%Y%m%d')(getCurrentDate()) + '.json';
		};
		
		function debounce(fn, wait) {
			var timeout;
			
			return function () {
				var context = this,
					args    = arguments,
					later   = function () {
						timeout = null;
						fn.apply(context, args);
					}
				clearTimeout(timeout);
				timeout = setTimeout(later, wait);
			};
		};
		
		function fakeData(date) {
			var startDate = (date) ? date : new Date(),
				data      = [];
			
			startDate.setDate( startDate.getDate() - 7 );
			
			for (var i = 0; i < 7; i++) {
				startDate.setDate(startDate.getDate() + 1);
				
				data.push({
					date: startDate.toDateString(),
					index:  rand(15, 35)
				});
			}
			
			return data;
		};
	})();

	
	//Step Count
	(function () {
		var chartContainer = document.getElementById('step_count_chart');
		
		if ( chartContainer ) {															
			
			var $datepicker     = $('#report_date'),
				w = chartContainer.clientWidth - 5,
				h = chartContainer.clientHeight - 5;
			
			var chart = StepCountChart({
					timeFormat: '%Y-%m-%dT%H:%M:%S',					
					width:  w,
					height: h,
					margin: [ h * 0.1, // Top    1%
					          w * 0.1,  // Right  10% 
					          h * 0.1, // Bottom 20% 
					          w * 0.1 ], // Left   10%
					rtl: $.app.isRTL,
					locale: (typeof d3GlobLocale !== "undefined") ? d3GlobLocale : false
				});
			
			$.app.loader.show();			
						
			var url = getUrl();
			d3.json(url, function (error, data) {
				if (!error){										
					setResidentsDropdown(data, 0); //populate resident dropdown, 0 is selected by default 					
					setAverages(currentMonitoredUser()); //calculate averages and set value in data bubbles
					
					$.app.loader.hide();
					window.history.replaceState(data, 'initial');

					// Chart just render last week data
					d3.select(chartContainer).datum(data[0].stepCountDailyData.slice(-7)).call(chart);
					
					$(window).on('resize', debounce(function () {
						resize();
					}, 50));
					
					window.addEventListener('popstate', function (event) {
						if ( event.state ) {
							var date = $.datepicker.parseDate('yymmdd', window.location.pathname.match(/\d+$/)[0]),
								now  = new Date();
							$datepicker.datepicker("setDate", date);
							now.setHours(0, 0, 0, 0);
							
							if ( date < now ) {
					  			$('.next-day').show();
					  		} else {
					  			$('.next-day').hide();
					  		}
							
							chart.data(event.state).render.call(chartContainer, true);							
						}	
					});
					
				} else{
					$.app.loader.hide();
					$('#system-error-modal').addClass('active');
				}					
				
				initChartControls(chart);
				
			});
			
			function initChartControls(chart) {
				
				//add click listener to display step count for that day on central data bubble
				var el = document.getElementsByClassName("STEP_COUNT_SENSOR");
				chart.data().forEach(function (d, i) {					
					el[i].addEventListener('click', function() {	
						setCurrentDaySteps(d);
						if (document.getElementsByClassName("step_count_selected_day")[0]){
							document.getElementsByClassName("step_count_selected_day")[0].classList.remove("step_count_selected_day");	
						}						
						el[i].classList.add("step_count_selected_day");
						
						}, false);						
				});				
				
				
				var elBack = document.getElementsByClassName("STEP_COUNT_SENSOR_BACKGROUND");
				chart.data().forEach(function (d, i) {					
					elBack[i].addEventListener('click', function() {
						
						if (document.getElementsByClassName("step_count_selected_day")[0]){
							document.getElementsByClassName("step_count_selected_day")[0].classList.remove("step_count_selected_day");	
						}						
						el[i].classList.add("step_count_selected_day");
						
						setCurrentDaySteps(d);
						}, false);						
				});
				
				//start with selecting leftmost bar
				setCurrentDaySteps(chart.data()[chart.data().length-1]);
				el[el.length-1].classList.add("step_count_selected_day");	
				
			}			
				
			function setResidentsDropdown(data, selected){				
				//check if resident can be selected
				
				//show selected name 
				var button_user = document.getElementById("step_count_resident_dropdown_button");
				button_user.innerHTML = data[selected].wearableDevice.label; 
				
				//set monitored user's name
				var monitored_user = document.getElementById("monitored-user-name");
				monitored_user.innerHTML = document.getElementById("monitored-user-name").innerHMTL = data[selected].wearableDevice.label;					        	
				
				//set which dropdown is currently selected
				var dropdown = document.getElementById("step_count_resident_dropdown");
				dropdown.setAttribute('selected', selected);
				dropdown.innerHTML = "";
				
				//populate dropdown				
				for (var i = 0; i<data.length; i++) {
					if (data[i].stepCountDailyData.length != 0){
						var aTag = document.createElement('a');			        			        			        
				        aTag.innerHTML = data[i].wearableDevice.label;			        
				        var item = document.createElement('li');			
				        item.setAttribute('class', 'wearable_device');
				        item.setAttribute('id', i);			        			        
				        item.appendChild(aTag);			        
				        dropdown.appendChild(item);	
					}			        
				}								
								
				//callback for selecting wearable device
				var el = document.getElementsByClassName('wearable_device');				
				for (var i=0; i<el.length; i++){												
					el[i].addEventListener('click', function() {														
						dropdown.setAttribute('selected', this.id);			        	
						//update  monitored user's name
			        	var monitored_user = document.getElementById("monitored-user-name");
						monitored_user.innerHTML = document.getElementById("monitored-user-name").innerHMTL = data[this.id].wearableDevice.label;							        							
						date =  $(this).datepicker('getDate');
						update(date);						
					}, false);											
				}
				
			};
			
			function setAverages(user){							 
				var residentId  = chartContainer.getAttribute('data-url').split('/')[4]; 
				var url = "/ADL/admin/step_count_average/" + residentId + ".json";
				
				d3.json(url, function (error, data) {					
					var stepCountData = data[currentMonitoredUser()].stepCountDailyData;
					var average_30 = 0;
					var average_7 = 0;
					for (i = 0; i < 30; i++) {
						if (i > 23) {average_7 += stepCountData[i].stepCount;}
					    average_30 += stepCountData[i].stepCount;
					} 
					
					average_30 = average_30/30;
					average_7 = average_7/7;
					
					var average_element_30 = document.getElementById("avg_steps_30");				
					var average_element_7 = document.getElementById("avg_steps_7");
					
					average_element_30.innerHTML = Math.floor(average_30);
					average_element_7.innerHTML = Math.floor(average_7);																													
				});						
			}						
			
			function setCurrentDaySteps(data){
				var day_steps = document.getElementById('day_steps');
				var step_icon = document.getElementById('step_icon');
				
				if (data.stepCount != 0 && data.stepCount != null) {
					day_steps.innerHTML = data.stepCount;
					day_steps.classList.add('known');
					day_steps.classList.remove('unknown');
					step_icon.setAttribute('src', $.app.rootPath + "/stylesheets/svg/step-count-icons/step_count_black.svg" );
				} else {
					day_steps.innerHTML = document.getElementById("no_activity_info_text").value;
					day_steps.classList.add('unknown');
					day_steps.classList.remove('known');
					step_icon.setAttribute('src', $.app.rootPath + "/stylesheets/svg/general-icons/unknow.svg" );				
				}
							
				//highlighting selected date on axis
				var prevSelected = document.getElementsByClassName('date_selected')[0];
				if (prevSelected) prevSelected.classList.remove('date_selected');
				
				var dateFormatter = d3.time.format("%d %a");
				$('text').each(function (i, t)
				{					
					if (t.innerHTML == dateFormatter(data.day)) {
						t.classList.add('date_selected');
					}
				});
							
				dateFormatter = d3.time.format("%a, %d/%m/%y");
				residentDateFormatter = d3.time.format('%d/%m/%Y');
				var residentDate = new Date (parseInt(document.getElementById("step_count_resident_panel_date").getAttribute("data-value")));				
			
				residentDate.setDate(residentDate.getDate() - 1);					

				if (residentDate.getTime() == data.day.getTime()) {
					document.getElementById('steps_date').innerHTML = "Yesterday";
				} else {	
					document.getElementById('steps_date').innerHTML = dateFormatter(data.day);
				}													
			}
			
			function currentMonitoredUser() {
				return document.getElementById('step_count_resident_dropdown').getAttribute("selected");
			}
			
			function update(date) {
				var url = getUrl();
				
				formatDateDatePicker();
 
				$.app.loader.show();
				
				d3.json(url, function (error, data) {					
					$.app.loader.hide();
					window.history.replaceState(data, url.replace(/\.json$/, ''), url.replace(/\.json$/, ''));					
					var drop = document.getElementById('step_count_resident_dropdown');					
					chart.data(data[currentMonitoredUser()].stepCountDailyData.slice(-7)).render.call(chartContainer);					
					initChartControls(chart);						
					setAverages(currentMonitoredUser());
					setResidentsDropdown(data, currentMonitoredUser());
				});				
			};
													
			function resize() {
				var w = chartContainer.clientWidth - 5,
					h = chartContainer.clientHeight - 5,
					margin = [ h * 0.15,  // Top    15%
					           w * 0.1,   // Right  10% 
					           h * 0.20,  // Bottom 20% 
					           w * 0.1 ]; // Left   10%
													
				chart.width(w)
					.height(h)
					.margin(margin)
					.render.call(chartContainer);
				
				initChartControls(chart);
			};
													
			$('.hasDatepicker').on('change', function (event) {
		  		var $this = $(this),
		  			date  =  $(this).datepicker('getDate'),
		  			now   = new Date();
	  		
		  		formatDateDatePicker();
		  		now.setHours(0, 0, 0, 0);
		  		if ( date < now ) {
		  			$('.next-day').show();
		  		} else {
		  			date = now;
		  			$this.datepicker('setDate', date);
		  			$('.next-day').hide();
		  		}
				update(date);
			});
		  	
		  	$('.prev-day').on('click', function (event) {
				var $this = $(this).next();
				var date  = $this.datepicker('getDate'),
					now   = new Date();
				
				now.setHours(0, 0, 0, 0);
				date.setDate(date.getDate() - 7);
				$this.datepicker('setDate', date);
				
				if ( date < now ) {
					$('.next-day').show();
				}
				
				update(date);
			});
		
			$('.next-day').on('click', function (event) {
				var $this = $(this).prev();
				var date  = $this.datepicker('getDate');
				var today = new Date();
				
				today.setHours(0, 0, 0, 0);
				date.setDate(date.getDate() + 7);
				
				if ( date >= today ){
					date = today;
					$('.next-day').hide();
				}
				
				$this.datepicker('setDate', date);
				update(date);
			});
			
			$('[data-report-print]').on('click', function (event) {
				$('body').addClass('print');
	  			resize();
	  			
  				window.print();
  			
  				$('body').removeClass('print');
  				resize();
		  		 		  		
		  		return false; 
			});
		}
		
		function getCurrentDate() {
			return $datepicker.datepicker('getDate');
		};
		
		function getUrl(){
			return chartContainer.getAttribute('data-url') + d3.time.format('%Y%m%d')(getCurrentDate()) + '.json';
		};
		
		function debounce(fn, wait) {
			var timeout;
			
			return function () {
				var context = this,
					args    = arguments,
					later   = function () {
						timeout = null;
						fn.apply(context, args);
					}
				clearTimeout(timeout);
				timeout = setTimeout(later, wait);
			};
		};
		
	
	})();
	
	// New Activity Index
	(function()
	{
		var chartContainer = document.getElementById('activity_index_table');
		if (chartContainer)
		{
			var ActivitiesClassDevicesEnum = Object.freeze(
			{
				restroom: 'TOILET_ROOM_SENSOR',
				bathroom: 'BATHROOM_SENSOR',
				diningroom: 'DINING_ROOM',
				bedroom: 'BEDROOM_SENSOR',
				livingroom: 'LIVING_ROOM',
				otherroom: 'OTHER_ROOM',
				outOfHome: 'FRONT_DOOR'
			});
			var ActivitiesstringEnum = Object.freeze(
			{
				restroom: 'restroom',
				bathroom: 'bathroom',
				diningroom: 'diningroom',
				bedroom: 'bedroom',
				livingroom: 'livingRoom',
				otherroom: 'otherroom',
				outOfHome: 'outOfHome'
			});
			$types_controls = $('#bars_types_controls');
			var $datepicker = $('#report_date'),
				w = chartContainer.clientWidth - 5,
				h = chartContainer.clientHeight - 5;
			var chart = WeekActivityLinearChart(
			{
				timeFormat: '%a %b %d %Y',
				width: w,
				height: h,
				margin: [h * 0.1, // Top    1%
					          w * 0.1, // Right  10% 
					          h * 0.1, // Bottom 20% 
					          w * 0.1], // Left   10%
				rtl: $.app.isRTL,
				locale: (typeof d3GlobLocale !== "undefined") ? d3GlobLocale : false
			});
			$.app.loader.show();
			var url = getUrl();
			d3.json(url, function(error, data)
			{
				if (!error)
				{
			
					$.app.loader.hide();
					window.history.replaceState(data, 'initial');
					updateControls(data);
					//console.log (data);
					var mainDiv = $("#activity_index_table");
					$(data).each(
						function (index)
						{
							var subItem =  "<div style='border: 1px solid black; float:none'>";
							$.each(
								this,
								function (key, value)
								{
									subItem += "<div style='height:1.5em'><div style='width:30%;float:left'><b>" + key + "</b></div><div style='width:70%;float:left'>" + value + "</div></div>";
								}
							);
								subItem += "</div>";
								mainDiv.append (subItem);
						}
					);
					
				}
				else
				{
					$.app.loader.hide();
					$('#system-error-modal').addClass('active');
				}
			});

			function update(date)
			{
				var url = getUrl();
				$.app.loader.show();
				d3.json(url, function(error, data)
				{
					$.app.loader.hide();
					window.history.replaceState(data, url.replace(/\.json$/, ''), url.replace(/\.json$/, ''));
					chart.data(data).render.call(chartContainer);
					updateControls(data);
					tips();
				});
			};

			function updateControls(data)
			{
				//Generate array of activities with posas
				var ExistingTypes = {};
				var keys = Object.keys(ActivitiesClassDevicesEnum);
				keys.forEach(function(fieldactivity, j)
				{
					data.forEach(function(dailyactivities, i)
					{
						if (fieldactivity === ActivitiesstringEnum.outOfHome ? dailyactivities[fieldactivity] : dailyactivities[fieldactivity] > 0)
							if (!ExistingTypes[ActivitiesClassDevicesEnum[fieldactivity]])
							{
								ExistingTypes[ActivitiesClassDevicesEnum[fieldactivity]] = {};
								return;
							}
					});
				});
	
				var hiddensbars = chart.hiddens();
				//Generate html for icons				
				var barTpls = [];
				for (var key in ExistingTypes)
				{
					barTpls.push(showBarTpl(key, hiddensbars.indexOf(key) !== -1));
				}
				$types_controls.html("");
				for (var i in barTpls)
				{
					barTpls[i].appendTo($types_controls);
				}
				$types_controls.find(':checkbox').uniform();
				$.app.scroll.resize();
			};

			function showBarTpl(key, hiddenbar)
			{
				var disabled = false;
				var className = key.toLowerCase();
				var valuetext = I18n.t('monthly_report.activities.' + key);
				var checked = ($('#show_' + key).parent().hasClass('checked') || !$('#show_' + key).length);
				var checked = !hiddenbar;
				var html = ["<label for='show_", key, "' class='show_devices_control", disabled ? " disabled" : "", "' id='label_control_", key, "'>",
				 	"<input type='checkbox' id='show_", key, "' value='", key, "'", checked ? ' checked' : '', disabled ? ' disabled' : '', "/>",
					"<span class='icon location ", className, "'></span>",
				 	"<span class='label-text'>", I18n.t('monthly_report.activities.' + key), "</span>"];
				return $(html.join(""));
			}

			function resize()
			{
				var w = chartContainer.clientWidth - 5,
					h = chartContainer.clientHeight - 5,
					margin = [h * 0.15, // Top    15%
					           w * 0.1, // Right  10% 
					           h * 0.20, // Bottom 20% 
					           w * 0.1]; // Left   10%
				chart.width(w).height(h).margin(margin).render.call(chartContainer);
			};

			function tips()
			{
				chart.data().forEach(function(d, i)
					{
						var tipOptslastactivity = {
							content:
							{
								text: I18n.t('activity_index.last_activity_tip') + ' <strong>' + chart.converttimetostring(d.lastActivity) + '</strong>'
							},
							position:
							{
								my: 'bottom right',
								at: 'top center',
								viewport: $(chartContainer)
							},
							style:
							{
								classes: 'qtip-clear-blue qtip-padding'
							}
						};
						$(chartContainer).find('.naipoint:eq(' + i + ') circle').qtip(tipOptslastactivity)
						var tipOptsfirstactivity = {
							content:
							{
								text: I18n.t('activity_index.first_activity_tip') + ' <strong>' + chart.converttimetostring(d.firstActivity) + '</strong>'
							},
							position:
							{
								my: 'bottom right',
								at: 'top center',
								viewport: $(chartContainer)
							},
							style:
							{
								classes: 'qtip-clear-blue qtip-padding'
							}
						};
						$(chartContainer).find('.naipoint:eq(' + (chart.data().length + i) + ') circle').qtip(tipOptsfirstactivity)
					});
				chart.data().forEach(function(d, i)
					{
						d.activities.forEach(function(subbar, j)
						{
							var tipOptsbardevice = {
								//								content: { text:  I18n.t('monthly_report.activities.' + subbar.i18n) + ' <strong>' + subbar.value + '</strong>' },
								content:
								{
									text: I18n.t('monthly_report.activities.' + subbar.i18n) + ' ' + subbar.value
								},
								position:
								{
									my: 'bottom right',
									at: 'top center',
									viewport: $(chartContainer)
								},
								style:
								{
									classes: 'qtip-clear-blue qtip-padding'
								}
							};
							$(chartContainer).find('.subbar:eq(' + (((i) * 7) + j) + ') rect').qtip(tipOptsbardevice)
						});
					});
			};
			$('.hasDatepicker').on('change', function(event)
				{
					var $this = $(this),
						date = $(this).datepicker('getDate'),
						now = new Date();
					
					formatDateDatePicker();
					now.setHours(0, 0, 0, 0);
					if (date < now)
					{
						$('.next-day').show();
					}
					else
					{
						date = now;
						$this.datepicker('setDate', date);
						$('.next-day').hide();
					}
					update(date);
				});
			$('.prev-day').on('click', function(event)
				{
					var $this = $(this).next();
					var date = $this.datepicker('getDate'),
						now = new Date();
					now.setHours(0, 0, 0, 0);
					date.setDate(date.getDate() - 7);
					$this.datepicker('setDate', date);
					if (date < now)
					{
						$('.next-day').show();
					}
					update(date);
				});
			$('.next-day').on('click', function(event)
				{
					var $this = $(this).prev();
					var date = $this.datepicker('getDate');
					var today = new Date();
					today.setHours(0, 0, 0, 0);
					date.setDate(date.getDate() + 7);
					if (date >= today)
					{
						date = today;
						$('.next-day').hide();
					}
					$this.datepicker('setDate', date);
					update(date);
				});
			$('[data-report-print]').on('click', function(event)
				{
					$('body').addClass('print');
					resize();
					window.print();
					$('body').removeClass('print');
					resize();
					return false;
				});
		}

		function getCurrentDate()
		{
			return $datepicker.datepicker('getDate');
		};

		function getUrl()
		{
			return chartContainer.getAttribute('data-url') + d3.time.format('%Y%m%d')(getCurrentDate()) + '.json';
		};

		function debounce(fn, wait)
		{
			var timeout;
			return function()
			{
				var context = this,
					args = arguments,
					later = function()
					{
						timeout = null;
						fn.apply(context, args);
					}
				clearTimeout(timeout);
				timeout = setTimeout(later, wait);
			};
		};

		function fakeData(date)
		{
			var startDate = (date) ? date : new Date(),
				data = [];
			startDate.setDate(startDate.getDate() - 7);
			for (var i = 0; i < 7; i++)
			{
				startDate.setDate(startDate.getDate() + 1);
				data.push(
				{
					date: startDate.toDateString(),
					index: rand(15, 35)
				});
			}
			return data;
		};
	})();
	
	
	(function () {
		var chartContainer = document.getElementById('monthly_report_chart');
		
		if ( chartContainer ) {
			var showed_types    = [],
				$types_controls = $('#bars_types_controls'),
				$datepicker     = $('#report_date'),
				$average        = $('#average'),
				$hide_bars      = $('#hide_bars');
			
			var $datepicker     = $('#report_date');
			// EIC15-2055 
			$datepicker.datepicker( "option", "maxDate", -1);
			//		console.log("Max Date: ", $datepicker.datepicker( "option", "maxDate" ));
			
			var w = chartContainer.clientWidth - 5,
				h = chartContainer.clientHeight - 5;
		
			var chart = MonthlyActivityBarsChart({
					timeFormat: '%a %b %d %Y',
					groupsKey:  'deviceIndexData',
					groupClass: 'activityType',
					groupId:    ['deviceId', 'activityType' ],
					width:  w,
					height: h,
					margin: [ h * 0.1,    // Top
					          w * 0.03,   // Right  
					          h * 0.2,   // Bottom 
					          w * 0.03 ], // Left
					rtl: $.app.isRTL,
					locale: (typeof d3GlobLocale !== "undefined") ? d3GlobLocale : false
				});
			
			//$.app.loader.show();
			var url = getUrl();
			d3.json(url, function (error, data){
				//$.app.loader.hide();
				
				if ( error ) {
					console.error(error)
					throw error;
				}
				
				removeUnknowns(data);
				window.history.replaceState(data, 'initial');
				//console.log(data);
				updateControls(data);
				
				d3.select(chartContainer).datum(data).call(chart);
				tips();
				$average.text(chart.median().toFixed(2));
				
				$(window).on('resize', debounce(function () {
					resize();
					tips();
				}, 50));
				
				$types_controls.on('change', ':checkbox', function () {
					var fn = (this.checked) ? chart.show : chart.hide;
					
					fn.call(chartContainer, this.value);
					
					$average.text(chart.median().toFixed(2));
				});
				
				window.addEventListener('popstate', function (event) {
					if ( event.state ) {
						var date = $.datepicker.parseDate('yymmdd', window.location.pathname.match(/\d+$/)[0]),
							now  = new Date();
						$datepicker.datepicker("setDate", date);
						now.setHours(0, 0, 0, 0);
						
						if ( date < now ) {
				  			$('.next-day').show();
				  		} else {
				  			$('.next-day').hide();
				  		}
						
						chart.data(event.state).render.call(chartContainer, true);
						tips();
						$average.text(chart.median().toFixed(2));
					}	
				});
			});						
			
			function update(date) {
				$.app.loader.show();
				
				formatDateDatePicker();
				
				var url = getUrl();
				
				d3.json(url, function (error, data){
					removeUnknowns(data);
					
					window.history.replaceState(data, url.replace(/\.json$/, ''), url.replace(/\.json$/, ''));
					$.app.loader.hide();
					
					if ( error ) {
						console.error(error)
						throw error;
					}
					
					chart.data(data).render.call(chartContainer, true);
					updateControls(data);
					tips();
					$average.text(chart.median().toFixed(2));
				});
			};
			
			function removeUnknowns(data){
				data.forEach(function (activities) {
					activities.deviceIndexData.forEach(function (d, i) {
						if ( !d.activityType ) activities.deviceIndexData.splice(i, 1);
					});
				});
			};
			
			function updateControls(data) {
				var newTypes = {};
				data.forEach(function (activities, i) {
					activities.deviceIndexData.forEach(function (d, i) {
						if (!newTypes[d.deviceId + "_" + d.activityType]) {
							var newD = newTypes[d.deviceId + "_" + d.activityType] = {};
							for ( var k in d ) { if ( d.hasOwnProperty(k) ) newD[k] = d[k]; }
						} else {
							newTypes[d.deviceId + "_" + d.activityType].index += d.index;
						}
					});
				});
				
				var barTpls = [];
				for (var key in newTypes) {
					var type = newTypes[key];
					
					if (type.activityType) {
						barTpls.push(showBarTpl(key, type));
					}
				}
				$types_controls.html("");
				for (var i in barTpls) {
					barTpls[i].appendTo($types_controls);
				}
				$types_controls.find(':checkbox').uniform();
				$.app.scroll.resize();
			};
			
			function resize() {
				var w = chartContainer.clientWidth - 5,
					h = chartContainer.clientHeight - 5,
					margin = [ h * 0.1,    // Top
						       w * 0.03,   // Right  
						       h * 0.2,   // Bottom 
						       w * 0.03 ]; // Left   10%
				
				chart.width(w)
					.height(h)
					.margin(margin)
					.render.call(chartContainer);
				$.app.scroll.resize();
			};
			
			$datepicker.on('change', function (event) {
		  		var $this = $(this),
		  			date  =  $(this).datepicker('getDate'),
		  			now   = new Date();
		  		
		  		now.setHours(0, 0, 0, 0);
		  		
		  		if ( date < now ) {
		  			$('.next-day').show();
		  		} else {
		  			date = now;
		  			$this.datepicker('setDate', date);
		  			$('.next-day').hide();
		  		}
		  		
				update(date);
			});
		  	
		  	$('.prev-day').on('click', function (event) {
				var $this = $(this).next();
				var date  = $this.datepicker('getDate'),
					now   = new Date();
				
				now.setHours(0, 0, 0, 0);
				date.setDate(date.getDate() - 30);
				$this.datepicker('setDate', date);
				
				if ( date < now ) {
					$('.next-day').show();
				}
				
				update(date);
			});
		
			$('.next-day').on('click', function (event) {
				var $this = $(this).prev();
				var date  = $this.datepicker('getDate');
				var today = new Date();
				
				today.setHours(0, 0, 0, 0);
				date.setDate(date.getDate() + 30);
				
				if ( date >= today ){
					date = today;
					$('.next-day').hide();
				}
				
				$this.datepicker('setDate', date);
				update(date);
			});
			
			$('[data-report-print]').on('click', function (event) {
				$('body').addClass('print');
	  			resize();
	  			
  				window.print();
  			
  				$('body').removeClass('print');
  				resize();
		  		 		  		
		  		return false; 
			});
		}
		
		function tips() {
			chart.data().forEach(function (day, i) {
				day.deviceIndexData.forEach(function (d, j){
					var text = (d.deviceLabel) ? d.deviceLabel : 
								( d.activityType ) ? I18n.t('monthly_report.activities.' + d.activityType) + " " + I18n.t('monthly_report.activity')
												  : I18n.t('monthly_report.activities.unknown') + " " + I18n.t('monthly_report.activity');
					var tipOpts = {
							content: { text: text + ' <strong>' + d.index + '</strong>' },
							position: { my: 'bottom right', at: 'top center', viewport: $(chartContainer) },
							style: { classes: 'qtip-clear-blue qtip-padding' }
					};
					
					$(chartContainer).find('.group:eq(' + i + ') rect:eq(' + j + ')').qtip(tipOpts);
				});
			});
		};
		
		function showBarTpl(key, d) {
			var disabled = d.index <= 0;
			var className = d.activityType ? d.activityType.toLowerCase() : "unknown";
			var checked = ($('#show_' + key).parent().hasClass('checked') || !$('#show_' + key).length); 
			var html = ["<label for='show_", key, "' class='show_devices_control", disabled ? " disabled" : "" ,"' id='label_control_", key ,"'>",
			 	"<input type='checkbox' id='show_", key, "' value='", key ,"'", checked ? ' checked' : '', disabled ? ' disabled' : '' , "/>",
			 	"<span class='icon location ", className, "'></span>",
			 	"<span class='label-text'>", I18n.t("monthly_report.activities." + d.activityType), "</span>" ];
			
			return $(html.join(""));
		}			
		
		
		function getCurrentDate() {
			return $datepicker.datepicker('getDate');
		};
		
		function getUrl(){
			return chartContainer.getAttribute('data-url') + d3.time.format('%Y%m%d')(getCurrentDate()) + '.json';
		};
		
		function fakeData(date, nDays) {
			var FAKE_DEVICES = ['OTHER_ROOM', 'FRIDGE_DOOR', 'FRONT_DOOR', 'BEDROOM_SENSOR', 'TOILET_ROOM_SENSOR', 'BATHROOM_SENSOR', 'LIVING_ROOM', 'DINING_ROOM'],
				startDate = (date) ? date : new Date(),
				nDays     = (nDays) ? nDays : 30,
				data      = [];
			
			startDate.setDate(startDate.getDate() - nDays);
			
			for ( var i = 0; i < nDays; i++){
				var devicesData = [],
					totalIndex  = 0;
				
				startDate.setDate( startDate.getDate() + 1 );
				
				FAKE_DEVICES.forEach(function (device, i) {
					var index = rand(3, 7);
					devicesData.push({
						activityType: device,
						index:  index
					});
					
					totalIndex += index;
				});
				
				data.push({
					date: startDate.toDateString(),
					index: totalIndex,
					deviceIndexData: devicesData
				});
			}
			
			return data;
		};
	})();
	
	function diff(arr, arrDiff) {
		return arr.filter(function (i) { return arrDiff.indexOf(i) === -1; });
	};
	
	function rand(min, max) {
		return Math.floor(Math.random() * (max - min + 1)) + min;
	};
});

function debounce(fn, wait) {
	var timeout;
	
	return function () {
		var context = this,
			args    = arguments,
			later   = function () {
				timeout = null;
				fn.apply(context, args);
			}
		clearTimeout(timeout);
		timeout = setTimeout(later, wait);
	};
};

function formatDateDatePicker() 
{
	var dateFormatter = $.app.dateFormatter;
	dateFormatter = dateFormatter.replace("yyyy","yy");
	$('.hasDatepicker').datepicker( "option", "dateFormat", dateFormatter.toLowerCase());
}

