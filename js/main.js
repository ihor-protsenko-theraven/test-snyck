/**
 * main.js specific screens javascripts
 * 
 * @author Samuel Santiago <samuelsantia@gmail.com>
 */

 import DOMPurify from 'dompurify';

$.app.isMobile = {
		Android: function() {
	        return navigator.userAgent.match(/Android/i);
	    },
	    BlackBerry: function() {
	        return navigator.userAgent.match(/BlackBerry/i);
	    },
	    iOS: function() {
	        return navigator.userAgent.match(/iPhone|iPad|iPod/i);
	    },
	    Opera: function() {
	        return navigator.userAgent.match(/Opera Mini/i);
	    },
	    Windows: function() {
	        return navigator.userAgent.match(/IEMobile/i);
	    },
	    Any: function () {
	    	return navigator.userAgent.match(/Android|BlackBerry|iPhone|iPad|iPod|Opera Mini|IEMobile/i);
	    }
};
// Variable for change locale
$reload = false;

// No cache for ajax requests to prevent Internet Explorer and Microsoft Edge
// from caching them, other browsers never do
$.ajaxSetup({cache: false});

// RETURN TO LOGIN IF IS INSIDE APP
$(document).on('pagecreate', '.login', function (event, ui) {
	var $page = $(this);
	
	if ($('#main-header').length > 0) {
		window.location.href = $.app.rootPath + '/login';
	}
	
});
	
// ALERT POPUP
// $(function ($) {
	/*
	 * var $popup = $('#system-status-pop').popup({ positionTo: 'window',
	 * transition: 'fade' });
	 */
	// Notification popup, use it when perform async function and can continue
	// navigating across app to show the async result
	$.app.notificationPopup = $('#notification-popup').popup({
		positionTo: 'window',
		transition: 'fade'
	});
	
	$.app.notificationPopup.on('click', '[data-rel=close]', function () {
		$.app.notificationPopup.popup('close');
	});
	
// });

// BACK CONFIG MENU ACTIVATION
$(function ($) {
	var $link_back = $('.main-header .link-back'),
		$link_config = $('.main-header .link-config');
	
	$(document).on('pagebeforeshow', '[data-role=page]', function (event, ui) {
		var $page = $(this);
		
		if ($page.is('.home')) {
			$link_back.removeClass('active');
			$link_config.addClass('active');
		} else {
			$link_back.addClass('active');
			$link_config.removeClass('active');
		}
	});
});

$(document).on('click', 'a[data-ajax=false]', function (event) {
	window.location = $(this).attr('href');
	return false;
});

$(document).on('pagecreate', '[data-role=page]', function (event) {
	$(this).find('a[data-ajax=false]').on('click', function () {
		window.location = $(this).attr('href');
		return false;
	});
});

$(document).on('pagecreate', '.home', function (event, ui) {
	if($reload){
		location.reload();
		$reload = false;
	}
	
});

$(document).on('pagecreate', '.select-language', function (event, ui) {
	var $page = $(this);
	$page.find('.item-wrap').on('click', function (event) {
		$reload = true;
// window.location.href = $.app.rootPath + $('#change_lang li a').attr('href');
		
	});
});

// USE POPUP MANAGER TO OPEN jQuery mobile popups
$(document).on("pagecreate", "[data-role=page]", function(event) {
	var $page = $(this);
	
	$page.find('[data-rel=popup]').on("click", function (event) {
		var $link  = $(this),
			priority = $link.data('priority') || "low",
			target = $link.attr('href'),
			$target = $(target);
		
		$.app.popupManager.pushPopup(priority, $target);
		
		return false;
	});
});

// ACTIVE MENU
$(function ($) {
	var $main_nav = $('#main-nav'),
		STATUS_SELECTORS     = '.home, .day-story, .day-story-list, .activity-level, .step-count-device-list, .step_count_chart',
		HISTORY_SELECTORS    = '.history, .event-detail, .events-log',
		MORE_SELECTORS       = '.take-action',
		TAKE_PHOTO_SELECTORS = '.take-photo, .photo-detail',
		$ccs_menu = $('.panel-wrap-container');
	
	$(document).on('pagebeforeshow', '[data-role=page]', function (event, ui) {
		var $page = $(this);
		
		$main_nav.find('.active').removeClass('active');
		if ($page.is(STATUS_SELECTORS)) {
			// $ccs_menu.css("position","fixed");
			$main_nav.find('a.status').parent().addClass('active');
		} else if ($page.is(HISTORY_SELECTORS)) {
			// $ccs_menu.css("position","relative");
			$main_nav.find('a.history').parent().addClass('active');
		} else if ($page.is(MORE_SELECTORS)) {
			// $ccs_menu.css("position","relative");
			$main_nav.find('a.more').parent().addClass('active');
		} else if ($page.is(TAKE_PHOTO_SELECTORS)) {
			// $ccs_menu.css("position","relative");
			$main_nav.find('a.take-photo').parent().addClass('active');
		}
	});
// $main_nav.click(function(){
// $ccs_menu.css("position","relative");
// });
	
	$main_nav.find('a.status').parent().addClass('active');
});
	
// DAY STORY CAROUSEL
$(document).on('pagecreate', '.day-story', function (event, ui) {
	var $page     = $(this),
		$carousel = $page.find('.grafics-carousel'),
		$pagination = $page.find('.pagination'),
		AM_POSITION = 0,
		PM_POSITION = ($.app.isRTL) ? $(window).width() : -$(window).width(),
		currentPosition = AM_POSITION;
	
		
		 function swipeDayStoryReportIfNoAMEvents(){
			    
			 var $am_events = $(".grafics-carousel .am .bar-container");
			 var isEmpty = true;

			 $am_events.each(function(){
				 if($(this).find("span").addClass("bar").length > 0){
					 isEmpty = false;
					 return;
				 }
			 });

			 if(isEmpty){
				 $carousel.addClass('pm').removeClass('am');
				 currentPosition = PM_POSITION;
				 $pagination.find('.active').removeClass('active').end().find('[href="#pm"]').addClass('active');
			 }
		 }

		 swipeDayStoryReportIfNoAMEvents();

		 
	$carousel.swipe({
		allowPageScroll: $.fn.swipe.pageScroll.VERTICAL,
		swipeStatus: function (event, phase, direction, distance, fingerCount) {
			if (phase == 'move' && (direction == 'left' || direction == 'right')) {
				var position;
				
				if (direction == 'left') {
					position = currentPosition - distance;
					$carousel.addClass('cancel-animate').css(Modernizr.prefixed('transform'), 'translate3d('+position+'px,0px,0px)');
					
				}
				if (direction == 'right') {
					position = currentPosition + distance;
					$carousel.addClass('cancel-animate').css(Modernizr.prefixed('transform'), 'translate3d('+position+'px,0px,0px)');
				}
				
				
			}
			if (phase == 'end' || phase == 'cancel') {
				// currentPosition = $carousel.position().left;
				var swipeThreshold	= 0.075 * $pagination.width();  // minimum
																	// distance
																	// to swipe
																	// to change
																	// half day,
																	// 7.5% of
																	// the width
																	// of the
																	// graphic
				var step = 0;
				if (direction == 'left' && distance >= swipeThreshold) {
					step = 1;
				} else if (direction == 'right' && distance >= swipeThreshold) {
					step = -1;
				} 
				
				if ($.app.isRTL) { step = -step; }
				
				if (currentPosition == AM_POSITION && step == 1) {
					$carousel.addClass('pm').removeClass('am');
					currentPosition = PM_POSITION;
					$pagination.find('.active').removeClass('active')
						.end().find('[href="#pm"]').addClass('active');
				} else if (currentPosition == PM_POSITION && step == -1) {
					$carousel.addClass('am').removeClass('pm');
					currentPosition = AM_POSITION;
					$pagination.find('.active').removeClass('active')
						.end().find('[href="#am"]').addClass('active');
				}
				
				
				/*
				 * if ($.app.isRTL) { if (-PM_POSITION / 2 < currentPosition) {
				 * $carousel.addClass('pm').removeClass('am'); currentPosition =
				 * PM_POSITION;
				 * $pagination.find('.active').removeClass('active')
				 * .end().find('[href=#pm]').addClass('active'); } else {
				 * $carousel.addClass('am').removeClass('pm'); currentPosition =
				 * AM_POSITION;
				 * $pagination.find('.active').removeClass('active')
				 * .end().find('[href=#am]').addClass('active'); } } else { if
				 * (PM_POSITION / 2 > currentPosition) {
				 * $carousel.addClass('pm').removeClass('am'); currentPosition =
				 * PM_POSITION;
				 * $pagination.find('.active').removeClass('active')
				 * .end().find('[href=#pm]').addClass('active'); } else {
				 * $carousel.addClass('am').removeClass('pm'); currentPosition =
				 * AM_POSITION;
				 * $pagination.find('.active').removeClass('active')
				 * .end().find('[href=#am]').addClass('active'); } }
				 */
				
				$carousel.removeClass('cancel-animate').removeAttr('style');
				
			}
		}
	});
	
	$pagination.find('a').on('click', function (event) {
		var $link = $(this);
		
		$carousel.removeClass('am, pm');
		$carousel.addClass($link.attr('href').replace('#', ''));
		currentPosition = ($carousel.is('.pm')) ? PM_POSITION : AM_POSITION;
	
		$pagination.find('.active').removeClass('active');
		$link.addClass('active');
		
		return false;
	});
});

// DAY STORY BARS
$(document).on('pageshow', '.day-story', function (event, ui) {
	var $page = $(this),
		$lines = $page.find('.grafic .lines'),
		$bars,
		$points = $page.find('.point'),
		intervalTiming = 250,
		interval = null,
		i = 0;
	
	checkScreenSize();
	isMobile();
	
	// Get the schedule data bar and position and resize the bar
	function positionBar ($bar) {
		var horary = $bar.data('horary').split('_');
		
		if ($.app.isRTL) {
			var linesWidth    = $lines.width(),
				startPosition = linesWidth - $lines.find('.line:eq(' + horary[0] + ')').position().left,
				finalWidth    = linesWidth - $lines.find('.line:eq(' + horary[1] + ')').position().left - startPosition;
			$bar.delay(250).css({right: startPosition, width: finalWidth});
		} else {
			var startPosition = $lines.find('.line:eq(' + horary[0] + ')').position().left,
				finalWidth    = $lines.find('.line:eq(' + horary[1] + ')').position().left - startPosition;
			$bar.css({left: startPosition, width: finalWidth});
		}
		
		// Check Missing Info
		if($bar.hasClass( "missinginfo" )) {
			var IMAGE_W = 80;
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
	
	function positionPoint( $point, drawBars ) {
		var $line = $lines.find('.line:eq(' + $point.data('point') + ')');
		
		if ( $.app.isRTL ) {
			$point.css({ right: $lines.width() - $line.position().left }).fadeIn(500);
		} else {
			$point.css({ left: $line.position().left }).fadeIn(500);
		}
		
		if (drawBars) {
			if ( $point.is('.on') && !$point.nextAll('.off[data-point='+ $point.data('point') +']').length ) {
				var $next = $point.next();
				
				if ($point.data('point') < $next.data('point')) {
					$('<span class="bar" data-horary="'+ $point.data('point') +'_'+ $next.data('point') +'"></span>').appendTo($point.parent());
				} else if ( $point.data('point') != $next.data('point') && !$point.nextAll('.off').length ) {
					var $calendar = $page.find('#calendar'),
						today = new Date(),
						nowTime = new Date($page.find('.grafics-carousel').data('time-ms'));
					today.setHours(0, 0, 0, 0);
					
					var end = (today > d3.time.format("%Y%m%d").parse($calendar.data("startdate").toString()) ) ? 24 : calculatePosition(nowTime);
					
					$('<span class="bar" data-horary="'+ $point.data('point') +'_' + end + '"></span>').appendTo($point.parent());
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
	
	$points.each(function () { positionPoint($(this), true); });
	$bars = $page.find('.bar');
	
	// Interval for first delayed bars animation
	interval = setInterval(function () {
		if (i >= $bars.length) {
			clearInterval(interval);
			interval = null;
			return false;
		}
		
		var $bar = $($bars.get(i));
		if($bar.data('horary')) {
			positionBar($bar);
		}
		
		i++;
	}, intervalTiming);
	
	function calculatePosition(time) {
		var position = time.getHours() * 2;
		
		if (time.getHours() > 12) {
			position -= 24;
		}
		
		if ( time.getMinutes() > 15 ) {
			position += 1;
		} else if (time.getMinutes() > 45) {
			position += 2;
		}
		
		return position;
	}
	
	// If the window is resized the bars will be repositioned
	$(window).on('resize.day-story', function (event) {
		$points.each(function () { positionPoint($(this)); });
		
		$bars.each(function () { positionBar($(this)); });
	});
	
	// Clear all events and intervals before hide the page
	$page.on('pagebeforehide', function (event, ui) {
		clearInterval(interval);
		interval = null;
		i = 0;
		
		$(window).off('resize.day-story');
	});
});

// DAY STORY LIST
$(document).on('pageshow', '.day-story-list', function (event, ui) {
	var slots = new Array(48),
		slotsOn = new Array(48),
		slotsOff = new Array(48),
		$page = $(this),
		$overlays = $page.find('.overlays li'),
		$lines    = $page.find('.lines');
	
	function checkOverlay($overlay) {
		var horary = $overlay.data('horary').split('_'), 
			start  = parseInt(horary[0]),
			end    = parseInt(horary[1]),
			priority = parseInt($overlay.data('priority')),
			i = start;
		
		for (i; i < end; i++) {
			var $slotOverlay = slots[i];
			
			if ($slotOverlay && $slotOverlay.attr('id') != $overlay.attr('id')) {
				var slotHorary = $slotOverlay.data('horary').split('_'),
					slotStart  = parseInt(slotHorary[0]),
					slotEnd    = parseInt(slotHorary[1]),
					slotPriority = parseInt($slotOverlay.data('priority'));
				
				if (start == slotStart && end == slotEnd) {
					console.log("SAME SLOT", start, end)
					if (priority <= slotPriority) {
						// overlay have more priority than slot overlay
						// hide slot overlay
						// slot i is now overlay
						$slotOverlay.css('display', 'none');
						for (var j = start; j <= i; j++) slots[j] = $overlay;
					} else {
						$overlay.css('display', 'none');
					}
				} else if (start == slotStart) {
					console.log("SAME START")
					if (end > slotEnd) {
						// overlay have more time than slot overlay
						// move overlay start to slot overlay end
						$overlay
							.data('horary', slotEnd + '_' + end)
							.attr('data-horary', slotEnd + '_' + end);
						
						// for (var j = slotStart; j < slotEnd; j++) slots[j] =
						// null;
						for (var k = start; k < i; k++) slots[k] = null;
						
						checkOverlay($overlay);
						break;
					} else {
						$slotOverlay
							.data('horary', end + '_' + slotEnd)
							.attr('data-horary', end + '_' + slotEnd);
						
						for (var j = slotStart; j < slotEnd; j++) slots[j] = null;
						for (var k = start; k < i; k++) slots[k] = null;
						
						checkOverlay($slotOverlay);
						checkOverlay($overlay);
						break;
					}
				} else if (end == slotEnd) {
					console.log("SAME END")
					
					if (start < slotStart) {
						// overlay have more time than slot overlay
						// move overlay end to slot overlay start
						$overlay
							.data('horary', start + '_' + slotStart)
							.attr('data-horary', start + '_' + slotStart);
						
						// for (var j = slotStart; j < slotEnd; j++) slots[j] =
						// null;
						for (var k = start; k < i; k++) slots[k] = null;
						
						checkOverlay($overlay);
						break;
					} else {
						$slotOverlay
							.data('horary', slotStart + '_' + start)
							.attr('data-horary', slotStart + '_' + start);
						
						for (var j = slotStart; j < slotEnd; j++) slots[j] = null;
						for (var k = start; k < i; k++) slots[k] = null;
						
						checkOverlay($slotOverlay);
						checkOverlay($overlay);
						break;
					}
				} else {
					if (start > slotStart && end > slotEnd){
						console.log("START AND END > SLOT START AND SLOT END")
						$overlay
							.data('horary', slotEnd + '_' + end)
							.attr('data-horary', slotEnd + '_' + end);
						
						// for (var j = slotStart; j < slotEnd; j++) slots[j] =
						// null;
						for (var k = start; k < i; k++) slots[k] = null;
						
						checkOverlay($overlay);
						break;
					} else if (start < slotStart && end < slotEnd) {
						console.log("SLOT START AND SLOT END > START AND END")
						$slotOverlay
							.data('horary', end + '_' + slotEnd)
							.attr('data-horary', end + '_' + slotEnd);

						for (var j = slotStart; j < slotEnd; j++) slots[j] = null;
						for (var k = start; k < i; k++) slots[k] = null;
						
						checkOverlay($overlay);
						checkOverlay($slotOverlay);
						break;
					} else if (start > slotStart && end < slotEnd) {
						// new overlay in middle slot overlay
						// clone old overlay
						$cloneOverlay = $slotOverlay.clone().appendTo($page.find('.overlays'))
							.data('horary', end + '_' + slotEnd)
							.attr('data-horary', end + '_' + slotEnd);
						$slotOverlay
							.data('horary', slotStart + '_' + start)
							.attr('data-horary', slotStart + '_' + start);
						$overlays = $page.find('.overlays li');
						
						for (var j = slotStart; j < slotEnd; j++) slots[j] = null;
						for (var k = start; k < i; k++) slots[k] = null;
						
						checkOverlay($cloneOverlay);
						checkOverlay($slotOverlay);
						checkOverlay($overlay);
						break;
						
					} else if (start < slotStart && end > slotEnd) {
						// slot overlay in middle new overlay
						// clone new overlay
						$cloneOverlay = $overlay.clone().appendTo($page.find('.overlays'))
							.attr('id', $overlay.attr('id') + '_clone')
							.data('horary', slotEnd + '_' + end)
							.attr('data-horary', slotEnd + '_' + end);
						$overlay
							.data('horary', start + '_' + slotStart)
							.attr('data-horary', start + '_' + slotStart);
						$overlays = $page.find('.overlays li');
						
						for (var j = slotStart; j < slotEnd; j++) slots[j] = null;
						for (var k = start; k < i; k++) slots[k] = null;
						
						checkOverlay($cloneOverlay);
						checkOverlay($overlay);
						checkOverlay($slotOverlay);
						break;
					}
				}
			} else {
				slots[i] = $overlay;
			}
		}
		
	}
	
	function checkPoint($point) {
		var position = $point.data('point');
		
		if ( $point.is('.on') ) {
			if ( slotsOn[position] ) {
				$point.hide(0);
			} else {
				slotsOn[position] = $point;
			}
		} else {
			if ( slotsOff[position] ) {
				$point.hide(0);
			} else {
				slotsOff[position] = $point;
			}
		}
	};
	
	function positionOverlay($overlay) {
		var horary = $overlay.data('horary').split('_'),
			start = parseInt(horary[0]),
			end   = parseInt(horary[1]),
			$startLine    = $lines.eq(start).find('hr'),
			$endLine      = $lines.eq(end).find('hr');
		
			if ( $startLine.length && $endLine.length ) {
				var startPosition = $startLine.position().top,
					finalHeight   = $endLine.position().top - startPosition;
		
				$overlay.css({ top: startPosition, height: finalHeight, opacity: 1 });
			}
	}
	
	function positionPoint($overlay) {
		var position = $overlay.data('point');
		
		if ( $overlays.filter('[data-horary^=' + position + ']').length ) {
			// hides open close text if starts at same of activity
			$overlay.find('.text').hide();
		}
		
		$overlay.css({ top: $lines.eq(position).find('hr').position().top - 2, opacity: 1 });
	};
	
	$overlays.each(function () {
		( !$(this).is('.point') ) ? checkOverlay($(this)) : checkPoint($(this));
	});
	
	$overlays.each(function () {
		var $overlay = $(this);
		
		if ( $overlay.is('.point') ){
			positionPoint($overlay);
		} else {
			positionOverlay($overlay);
		}
	});
	
	// DELETE ME
	// for (var i = 0; i < slots.length; i++) { (slots[i]) ? console.log(i,
	// slots[i].get(0)) : console.log(i);}
	
	$(window).on('resize.day-story-list', function (event) {
		slots = new Array(48);
		$overlays.each(function () {
			if ( !$(this).is('.point') ) checkOverlay($(this));
		});
		$overlays.each(function () {		
			if ( $overlay.is('.point') ){
				positionPoint($overlay);
			} else {
				positionOverlay($overlay);
			}
		});
	});
	
	$page.on('pagebeforehide', function (event, ui) {
		$(window).off('resize.day-story-list');
	});
});

// MONTH CALENDAR
$(document).on('pageshow', '[data-role=page]', function (event, data) {
  var $page = $(this),
      $popup = DOMPurify.sanitize($page.find('#datepicker_calendar-popup')),
      $calendar = $page.find('#calendar'),
      fc = $calendar.data('fullCalendar');

  function scale( width, height, padding, border ) {
    var scrWidth = $( window ).width() - 30,
        scrHeight = $( window ).height() - 30,
        ifrPadding = 2 * padding,
        ifrBorder = 2 * border,
        ifrWidth = width + ifrPadding + ifrBorder,
        ifrHeight = height + ifrPadding + ifrBorder,
        h, w;

    if ( ifrWidth < scrWidth && ifrHeight < scrHeight ) {
        w = ifrWidth;
        h = ifrHeight;
    } else if ( ( ifrWidth / scrWidth ) > ( ifrHeight / scrHeight ) ) {
        w = scrWidth;
        h = ( scrWidth / ifrWidth ) * ifrHeight;
    } else {
        h = scrHeight;
        w = ( scrHeight / ifrHeight ) * ifrWidth;
    }

    return {
        'width': w - ( ifrPadding + ifrBorder ),
        'height': h - ( ifrPadding + ifrBorder )
    };
  };

  if ($calendar.length > 0 && !$calendar.data('fullCalendar')) {
    var aspectRatio = $(window).width() / $(window).height(),
    	today = new Date();

    $calendar.attr('width', 0).attr('height', 0);

    $page.on({
      popupbeforeposition: function (event) {
        if ($(window).width() < $(window).height()){
          var size = scale(640, 474, 0, 0),
              w = size.width,
              h = size.height;
        } else {
          var size = scale(960, 474, 0, 0),
              w = size.width,
              h = size.height;
        }

        $calendar.css( { "width" : w, "height" : h } );
      },
      popupafterclose: function (event) {
        $calendar.css( { "width" : 0, "height" : 0 } );
      },
      popupafteropen: function (event) {
    	var initialMonth = parseInt($calendar.data('startdate').toString().substring(4, 6)) - 1;
      		initialYear  = parseInt($calendar.data('startdate').toString().substring(0, 4));
    	
        if ($(window).width() < $(window).height()){
          var size = scale(640, 474, 0, 0),
              w = size.width,
              h = size.height;
        } else {
          var size = scale(930, 474, 0, 0),
            w = size.width,
            h = size.height;
        }
        
        if (!fc) {
        	var options = {
	            header: false,
	            aspectRatio: w / h,
	            disableDragging: true,
	            disableResizing: true,
	            month: initialMonth,
	            year: initialYear,
	            dayRender: function (date, cell) {
	            	var calendarDate = this.calendar.getDate(),
	            		initialMonth = parseInt($calendar.data('startdate').toString().substring(4, 6)) - 1,
	          			initialYear  = parseInt($calendar.data('startdate').toString().substring(0, 4)),
	            		initialDay   = parseInt($calendar.data('startdate').toString().substring(6));
	            	
	            	if (date.getDate() == initialDay && date.getMonth() == initialMonth && date.getFullYear() == initialYear) {
	            		$(cell).addClass('selected');
	            	}
	            	if ( ( date.getDate() > today.getDate() && 
	            		   ( date.getMonth() >= today.getMonth() && date.getFullYear() >= today.getFullYear() ) )
	            			|| ( date.getMonth() > today.getMonth() && date.getFullYear() >= today.getFullYear() )
	            			|| date.getFullYear() > today.getFullYear()) {
	            		$(cell).addClass('disabled');
	            	}
	            },
	            dayClick: function (date) {
	            	var parseDate = $.fullCalendar.formatDate(date, 'yyyyMMdd');
	            	
	            	if ($(this).is('.disabled')) {
	            		return false;
	            	}
	            	
	            	if ( $calendar.data('url') ){
	            		$.mobile.changePage($calendar.data('url').replace('__DATE__', parseDate));
	            	} else if ($.mobile.activePage.is('.day-story')) {
	            		$.mobile.changePage($.app.rootPath + '/home/day_story/' + parseDate);
	            	} else if ($.mobile.activePage.is('.day-story-list')) {
	            		$.mobile.changePage($.app.rootPath + '/home/day_story_list/' + parseDate);
	            	} else {
	            		$calendar.triggerHandler('daychange', [date])
	            	}
	            }
	          };
        	
          $calendar.fullCalendar($.extend({}, $.fullCalendar.defaultOptions || {}, options));
          
          fc = $calendar.data('fullCalendar');
          
          $popup.find('.prev').on('click', function (event) {
            $calendar.fullCalendar('prev');
            var date = $calendar.fullCalendar('getDate');
            $popup.find('.calendar-header h3').html(
            		fc.formatDate(date, 'MMMM')
            		+ ' <span class="year">'
            		+ fc.formatDate(date, 'yyyy')
            		+ '</span>');
            
            if ( (date.getMonth() < today.getMonth() && date.getFullYear() == today.getFullYear()) 
            		|| date.getFullYear() <= today.getFullYear()) {
            	$popup.find('.next').fadeIn(200);
            }
            
            return false;
          });
          $popup.find('.next').on('click', function (event) {
            $calendar.fullCalendar('next');
            var date = $calendar.fullCalendar('getDate');
            
            if (date.getMonth() > today.getMonth() && date.getFullYear() >= today.getFullYear()) {
            	$calendar.fullCalendar('prev');
            	date = $calendar.fullCalendar('getDate');
            }
            $popup.find('.calendar-header h3').html(
            		fc.formatDate(date, 'MMMM')
            		+ ' <span class="year">'
            		+ fc.formatDate(date, 'yyyy')
            		+ '</span>');
            
            if (date.getMonth() >= today.getMonth() && date.getFullYear() >= today.getFullYear()) {
            	$popup.find('.next').fadeOut(200);
            }
            
            return false;
          });
        } else {
          $calendar.fullCalendar('option', 'aspectRatio', w / h);
          $calendar.fullCalendar('render');
          $calendar.fullCalendar('month', initialMonth);
          $calendar.fullCalendar('year', initialYear);
        }
        var date = $calendar.fullCalendar('getDate');
        $popup.find('.calendar-header h3').html(
        		fc.formatDate(date, 'MMMM')
        		+ ' <span class="year">'
        		+ fc.formatDate(date, 'yyyy')
        		+ '</span>');
        
        if (date.getMonth() >= today.getMonth() && date.getFullYear() >= today.getFullYear()) {
        	$popup.find('.next').fadeOut(0);
        }
      }
    }, $popup);
  }
});

// Page load failed handler when retrieved through $.mobile.changePage()

$(document).on("pageloadfailed", function(event, data){
   
	event.preventDefault();	
	
	$.mobile.hidePageLoadingMsg();
	
	// var errMessage = data.xhr.status + " " + data.xhr.statusText;
	
	// Invalid TOKEN - 401 response - after closing the popup must redirect to login page
	if (data && data.xhr && data.xhr.status == 401) {
		show_error_message("Invalid session", "Your session is no longer valid. Please login again", true);
	} else {
		show_error_message(I18n.t('alerts.popup.page_load_failed.title'), I18n.t('alerts.popup.page_load_failed.message'), false);
	}
    
    data.deferred.reject(data.absUrl, data.options);
});

function show_error_message(title, message, mustLogin) {
	
	
	var errorPopup = function (id, title, error) {
		return ['<div id="', id, '" class="call-popup">',
		        	'<header>', title, '</header>',
		        	'<div class="popup-content">', error, '</div>',
		        	'<div class="popup-actions">',
		        		'<button" data-rel="close" class="btn-inline btn-primary autosizes">', I18n.t('buttons.close') ,'</button>',
		        	'</div>',
		        '</div>'].join('');
	};
	
	
	$popup = $(errorPopup("errorPopup", title, message)).popup({
		transition: 'fade'
	});
	
	$popup.on('click', '[data-rel=close]', function (event) {
		$popup.popup('close');
		$.mobile.loading('hide');
		if (mustLogin) {
			window.location.href = $.app.rootPath + '/login';
		}
		return false;
	});
		
	/*
	 * $popup.html(message).trigger('popupcreate'); if (
	 * !$popup.parent().is('.ui-popup-active') ){ // esto sí que habría que
	 * mantenerlo $.app.popupManager.pushPopup("medium", $popup); }
	 */
	
	$.app.popupManager.pushPopup("medium", $popup);
  	  
}

// MOVEMENT LEVEL
$(document).on('pageshow', '.activity-level', function (event, data) {
  var $page = $(this);
  $page.find('.bar').each(function () {
    var $bar   = $(this),
        fHeight = $bar.attr('data-percent'),
        tmo;

     tmo = setTimeout(function () {
      $bar.height(fHeight);
      clearTimeout(tmo);
    }, 500);
  });
});

// ACTIVITY INDEX
$(document).on('pageshow', '.activity-index-old', function (event, data) {
	var $page = $(this),
		chartContainer = document.getElementById('activity_index_chart_old');
	
	var chart,
		$calendar = $page.find('#calendar'),
		$datepicker = $page.find('#datepicker');
	
	// $.mobile.loading('show');
	
	d3.json(getUrl(), function (error, data) {
	
		var w = chartContainer.clientWidth - 5,
			h = chartContainer.clientHeight - 5;
		
		chart = WeekActivityLinearChart({
			timeFormat: '%a %b %d %Y',
			yi: 'index',
			width:  w,
			height: h,
			margin: [ h * 0.15, // Top 15%
			          w * 0.1,  // Right 10%
			          h * 0.20, // Bottom 20%
			          w * 0.1 ], // Left 10%
			rtl: $.app.isRtl,
			locale: (typeof d3GlobLocale !== 'undefined') ? d3GlobLocale : false,
			extendActiveArea: true
		});
		
		d3.select(chartContainer).datum(data).call(chart);
		$.mobile.loading('hide');
		
		tips();
		
		$(window).on('resize.chart', debounce(function () {
			var w = chartContainer.clientWidth - 5,
				h = chartContainer.clientHeight - 5,
				margin = [ h * 0.15,  // Top 15%
				           w * 0.1,   // Right 10%
				           h * 0.20,  // Bottom 20%
				           w * 0.1 ]; // Left 10%
			
			
			
			chart.width(w)
				.height(h)
				.margin(margin)
				.render.call(chartContainer);
			tips();
		}, 50));
		
		$page.on('pagebeforehide', function () {
			$(window).off('resize.chart');
		});
	});	
	
	function update(date) {
		var url = getUrl(date);
		
		$.mobile.loading('show');
		
		d3.json(url, function (error, data) {
			// var data = fakeData(date);
			
			$.mobile.loading('hide');
			$datepicker.html( "<span class='icon icon-calendar'></span>" +$.fullCalendar.formatDate(date, $.app.dateCalendarFormatter, $.fullCalendar.defaultOptions) ); 
			updateCalendaer(date);
			chart.data(data).render.call(chartContainer);
			tips();
		}, 500);
	};
	
	function tips() {
		chart.data().forEach(function (d, i) {
			var datumCircle = $(chartContainer).find('.point:eq(' + i + ') circle.showable_datum');
			var tipOpts = {
					content: { text: I18n.t('activity_index.total_activities') + ' <strong>' + d.index + '</strong>' },
					position: { my: 'bottom right', at: 'top center', viewport: $(chartContainer), target: datumCircle },
					style: { classes: 'qtip-clear-blue qtip-padding' },
					events: {
						show: function() { datumCircle.css('stroke-width', '2') },
						hide: function() { datumCircle.css('stroke-width','0') }
					}
			};
			
			$(chartContainer).find('.point:eq(' + i + ') circle.bounding_box').qtip(tipOpts)
		});
	};
	
	$calendar.on('daychange', function (event, date) {
  		var now   = new Date();
  		
  		now.setHours(0, 0, 0, 0);
  		
  		if ( date < now ) {
  			$('.select-date .next').fadeIn(250);
  		} else {
  			date = now;
  			$('.select-date .next').fadeOut(250);
  		}
  		
		update(date);
		$calendar.closest('.ui-popup').popup('close');
	});
  	
  	$('.select-date .prev').on('click', function (event) {
		var date  = getCurrentDate(),
			now   = new Date();
		
		now.setHours(0, 0, 0, 0);
		date.setDate(date.getDate() - 7);
		
		if ( date < now ) {
			$('.select-date .next').fadeIn(250);
		}
		
		update(date);
	});

	$('.select-date .next').on('click', function (event) {
		var date  = getCurrentDate();
		var today = new Date();
		
		today.setHours(0, 0, 0, 0);
		date.setDate(date.getDate() + 7);
		
		if ( date >= today ){
			date = today;
			$('.select-date .next').fadeOut(250);
		}
		
		update(date);
	});
	
	function getCurrentDate() {
		if ( $calendar.data('fullCalendar') ) {
			$calendar.fullCalendar('getDate');
		}
		return d3.time.format('%Y%m%d').parse($calendar.data('startdate').toString());
	};
	
	function getUrl(date){
		return chartContainer.getAttribute('data-url') + d3.time.format('%Y%m%d')(date || getCurrentDate()) + '.json';
	};
	
	function updateCalendaer(date) {
		if ( $calendar.data('fullCalendar') ) {
			$calendar.fullCalendar('gotoDate', date);
		}
		
		$calendar.data('startdate', $.fullCalendar.formatDate(date, 'yyyyMMdd'));
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
	
	function rand(min, max) {
		return Math.floor(Math.random() * (max - min + 1)) + min;
	};
});



$(document).on('pageshow', '.activity-index', function (event, data) {
	var $page = $(this),
		chartContainer = document.getElementById('activity_index_chart');
	
	var ActivitiesClassDevicesEnum = Object.freeze({restroom: 'TOILET_ROOM_SENSOR', bathroom: 'BATHROOM_SENSOR', diningroom: 'DINING_ROOM', bedroom: 'BEDROOM_SENSOR', livingroom: 'LIVING_ROOM', otherroom: 'OTHER_ROOM', outOfHome: 'FRONT_DOOR' });
	var ActivitiesstringEnum = Object.freeze({restroom: 'restroom', bathroom: 'bathroom', diningroom: 'diningroom', bedroom: 'bedroom', livingroom: 'livingRoom', otherroom: 'otherroom', outOfHome: 'outOfHome' });
	var ActivitiessvgiconEnum = Object.freeze({TOILET_ROOM_SENSOR: 'TOILET_ROOM_SENSOR', BATHROOM_SENSOR: 'BATHROOM_SENSOR', DINING_ROOM: 'FRIDGE_DOOR', BEDROOM_SENSOR: 'BEDROOM_SENSOR', LIVING_ROOM: 'LIVING_ROOM', OTHER_ROOM: 'OTHER_ROOM', FRONT_DOOR: 'MAIN_DOOR' });
	var DeviceEnum = Object.freeze({desktop: 1, mobile: 2});
	
	$types_controls = $('#bars_types_controls');
	
	var chart,
		$calendar = $page.find('#calendar'),
		$datepicker = $page.find('#datepicker');
	
	// $.mobile.loading('show');
	
	d3.json(getUrl(), function (error, data) {
	
		var w = chartContainer.clientWidth - 5,
			h = chartContainer.clientHeight - 5;
		
		chart = WeekActivityLinearChart({
			timeFormat: '%a %b %d %Y',
// yi: 'index',
			width:  w,
			height: h,
			margin: [ h * 0.12, // Top 15%
			          w * 0.1,  // Right 10%
			          h * 0.05, // Bottom 20%
			          w * 0.1 ], // Left 10%
			rtl: $.app.isRtl,
			heighti : [0.15,0.15,0.66],
			locale: (typeof d3GlobLocale !== 'undefined') ? d3GlobLocale : false,
			extendActiveArea: true,
			device: DeviceEnum.mobile,
		});
		
		d3.select(chartContainer).datum(data).call(chart);
		$.mobile.loading('hide');
		updateControls(data);
		tips();
		
		$(window).on('resize.chart', debounce(function () {
			var w = chartContainer.clientWidth - 5,
				h = chartContainer.clientHeight - 5,
				margin = [ h * 0.12,  // Top 15%
				           w * 0.1,   // Right 10%
				           h * 0.05,  // Bottom 20%
				           w * 0.1 ]; // Left 10%
			
			
			
			chart.width(w)
				.height(h)
				.margin(margin)
				.render.call(chartContainer);
			
			tips();
		}, 50));
		
		$page.on('pagebeforehide', function () {
			$(window).off('resize.chart');
		});
	});	
	
	function update(date) {
		var url = getUrl(date);
		
		$.mobile.loading('show');
		
		d3.json(url, function (error, data) {
			// var data = fakeData(date);
			
			$.mobile.loading('hide');
			$datepicker.html( "<span class='icon icon-calendar'></span>" + $.fullCalendar.formatDate(date, $.app.dateCalendarFormatter, $.fullCalendar.defaultOptions) );
			updateCalendaer(date);
			chart.data(data).render.call(chartContainer);
			updateControls(data);
			tips();
		}, 500);
	};
	
	function updateControls(data) {
		// Generate array of activities with posas
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
		
		// Generate html for icons
		var barTpls = [];
		for (var key in ExistingTypes) {
			barTpls.push(showBarTpl(key));
		}
		
		$types_controls.html("");
		for (var i in barTpls) {
			barTpls[i].appendTo($types_controls);
		}
		
		showtipBarTpl(ExistingTypes);
// $.app.scroll.resize();
	};
	
	function showBarTpl(key) {
		var value= ActivitiessvgiconEnum[key];
		var html = ["<div class='label-container'>",
				"<img class='icon icon-activity' src='/ADL/stylesheets/svg/activity-icons/"+ ActivitiessvgiconEnum[key].toLowerCase() +".svg'/>",
				"<div class='rectangle-legend " + ActivitiessvgiconEnum[key] + "-legend'></div></div>"];
		return $(html.join(""));
	}
	
	function showtipBarTpl(ExistingTypes) {
		var counter=0;
		for (var key in ExistingTypes) {		
			var tipOptsicondevice = {
					content: { text:  I18n.t('monthly_report.activities.' + key)},
					position: { my: 'bottom center', at: 'top center', viewport: $(chartContainer) },
					style: { classes: 'qtip-clear-blue qtip-padding' }
			};
			var x=$types_controls;
			$types_controls.find('svg:eq(' + (counter++) + ')').qtip(tipOptsicondevice);
		};
	}	
	
	function tips() {
		chart.data().forEach(function (d, i) {
			var datumCircle = $(chartContainer).find('.naipoint:eq(' + i + ') circle.showable_datum');
			var tipOptslastactivity = {
					content: { text: I18n.t('activity_index.last_activity_tip') + ' <strong>' + chart.converttimetostring(d.lastActivity) + '</strong>' },
					position: { my: 'bottom right', at: 'top center', viewport: $(chartContainer), target: datumCircle },
					style: { classes: 'qtip-clear-blue qtip-padding' },
					events: {
						show: function() { datumCircle.css('stroke-width', '2') },
						hide: function() { datumCircle.css('stroke-width','0') }
					}
			};
			$(chartContainer).find('.naipoint:eq(' + i + ') circle.bounding_box').qtip(tipOptslastactivity)
			
			var datumCirclefirst = $(chartContainer).find('.naipoint:eq(' +  (chart.data().length + i) + ') circle.showable_datumfirst');
			var tipOptsfirstactivity = {
				content: { text: I18n.t('activity_index.first_activity_tip') + ' <strong>' + chart.converttimetostring(d.firstActivity) + '</strong>' },
				position: { my: 'bottom right', at: 'top center', viewport: $(chartContainer), target: datumCirclefirst },
				style: { classes: 'qtip-clear-blue qtip-padding' },
				events: {
					show: function() { datumCirclefirst.css('stroke-width', '2') },
					hide: function() { datumCirclefirst.css('stroke-width','0') }
				}
			};
			$(chartContainer).find('.naipoint:eq(' + (chart.data().length + i) + ') circle.bounding_box').qtip(tipOptsfirstactivity)
		});

		
		
		chart.data().forEach(function (d, i) {
			d.activities.forEach(function (subbar, j) {
				var tipOptsbardevice = {
// content: { text: I18n.t('monthly_report.activities.' + subbar.i18n) + '
// <strong>' + subbar.value + '</strong>' },
						content: { text:  I18n.t('monthly_report.activities.' + subbar.i18n)+ ' ' + subbar.value},
						position: { my: 'bottom right', at: 'top center', viewport: $(chartContainer) },
						style: { classes: 'qtip-clear-blue qtip-padding' }
				};
				$(chartContainer).find('.subbar:eq(' + (((i)*7)+j) + ') rect').qtip(tipOptsbardevice)
			});
		});
	};
	
	
	$calendar.on('daychange', function (event, date) {
  		var now   = new Date();
  		
  		now.setHours(0, 0, 0, 0);
  		
  		if ( date < now ) {
  			$('.select-date .next').fadeIn(250);
  		} else {
  			date = now;
  			$('.select-date .next').fadeOut(250);
  		}
  		
		update(date);
		$calendar.closest('.ui-popup').popup('close');
	});
  	
  	$('.select-date .prev').on('click', function (event) {
		var date  = getCurrentDate(),
			now   = new Date();
		
		now.setHours(0, 0, 0, 0);
		date.setDate(date.getDate() - 7);
		
		if ( date < now ) {
			$('.select-date .next').fadeIn(250);
		}
		
		update(date);
	});

	$('.select-date .next').on('click', function (event) {
		var date  = getCurrentDate();
		var today = new Date();
		
		today.setHours(0, 0, 0, 0);
		date.setDate(date.getDate() + 7);
		
		if ( date >= today ){
			date = today;
			$('.select-date .next').fadeOut(250);
		}
		
		update(date);
	});
	
	function getCurrentDate() {
		if ( $calendar.data('fullCalendar') ) {
			$calendar.fullCalendar('getDate');
		}
		return d3.time.format('%Y%m%d').parse($calendar.data('startdate').toString());
	};
	
	function getUrl(date){
		return chartContainer.getAttribute('data-url') + d3.time.format('%Y%m%d')(date || getCurrentDate()) + '.json';
	};
	
	function updateCalendaer(date) {
		if ( $calendar.data('fullCalendar') ) {
			$calendar.fullCalendar('gotoDate', date);
		}
		
		$calendar.data('startdate', $.fullCalendar.formatDate(date, 'yyyyMMdd'));
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
	
	function rand(min, max) {
		return Math.floor(Math.random() * (max - min + 1)) + min;
	};
});


// Step Count Chart

$(document).on('pageshow', '.step_count_chart', function (event, data) {
	
		var $page = $(this);
		var chartContainer = document.getElementById('step_count_chart');
		
		if ( chartContainer ) {															
			
			var $datepicker = $page.find('#datepicker'),
				w = chartContainer.clientWidth - 5,
				h = chartContainer.clientHeight - 5;
			
			var chart = StepCountChart({
					timeFormat: '%Y-%m-%dT%H:%M:%S',					
					width:  w,
					height: h,
					margin: [ h * 0.1, // Top 1%
					          w * 0.1,  // Right 10%
					          h * 0.1, // Bottom 20%
					          w * 0.1 ], // Left 10%
					rtl: $.app.isRTL,
					locale: (typeof d3GlobLocale !== "undefined") ? d3GlobLocale : false
				});
			
			var $calendar = $page.find('#calendar');
			
			var deviceId = getDeviceId();
			var residentId = getResidentId();
			
			$.mobile.loading('show');		
									
			var url = getUrl();
			d3.json(url, function (error, data) {
				
				if (!error){
					
					var deviceData = getStepCountDataForDevice(data, deviceId);
					// var lastSevenDeviceDataRecords =
					// getLatestRecords(deviceData, 7);
					var chartHeight;
					
					setAverages(residentId, deviceId);
					
					$.mobile.loading('hide');

					// d3.select(chartContainer).datum(lastSevenDeviceDataRecords).call(chart);
					
					d3.select(chartContainer).datum(deviceData.slice(-7)).call(chart);
					
					// this is a workaround to achieve that chart container has
					// the same height as the chart. Probably will not work very
					// fine with resize.
					chartHeight = document.getElementById("step_count_chart").getElementsByTagName("svg")[0].getAttribute("height");
					$("#step_count_chart").css('height', chartHeight);
					
					$(window).on('resize', debounce(function () {
						resize();
					}, 50));
					
					
					initChartControls(chart);	
					
				} else{
					$.mobile.loading('hide');
					showErrorPopUp("Error getting data");
				}					
				
			});
			
			
			function getStepCountDataForDevice(stepCountDeviceReports, deviceId){
				for(var i = 0; i < stepCountDeviceReports.length; i++ ){
					if(stepCountDeviceReports[i].wearableDevice.deviceId == deviceId){
						return stepCountDeviceReports[i].stepCountDailyData;
					}
				}
			}
			
			function getLatestRecords(records, lastDays){
				
				if(lastDays != null){
					if(lastDays <= records.length){
						return records.slice(records.length - lastDays, records.length);
					}else{
						return null;
					}
				}
				
				return records;
				
			}
			
			function initChartControls(chart) {
				
				// add click listener to display step count for that day on
				// central data bubble
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
				
				
				// start with selecting leftmost bar
				setCurrentDaySteps(chart.data()[chart.data().length-1]);
				el[el.length-1].classList.add("step_count_selected_day");	
				
			}
			
			function setAverages(residentId, device){							 
				
				var url = "/ADL/admin/step_count_average/" + residentId + ".json";
				
				d3.json(url, function (error, data) {
					
					if(!error){
					
						var stepCountData = getStepCountDataForDevice(data, device);
						var average_30 = 0;
						var average_7 = 0;
						for (i = 0; i < stepCountData.length; i++) {
							if (i > 23) {average_7 += stepCountData[i].stepCount;}
						    average_30 += stepCountData[i].stepCount;
						} 
						
						average_30 = average_30/30;
						average_7 = average_7/7;
						
						var average_element_30 = document.getElementById("avg_steps_30");				
						var average_element_7 = document.getElementById("avg_steps_7");
						
						average_element_30.innerHTML = Math.floor(average_30);
						average_element_7.innerHTML = Math.floor(average_7);
					
					} else {
						$.mobile.loading('hide');
						showErrorPopUp("Error getting data");	
						
					}
					
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
							
				// highlighting selected date on axis
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
				var residentDate = new Date (parseInt(getResidentDate()));				
			
				residentDate.setDate(residentDate.getDate() - 1);					

				if ($.fullCalendar.formatDate(residentDate, 'yyyyMMdd') == $.fullCalendar.formatDate(data.day, 'yyyyMMdd')) {
					document.getElementById('steps_date').innerHTML = "Yesterday";
				} else {	
					document.getElementById('steps_date').innerHTML = dateFormatter(data.day);
				}													
			}
			
			function currentMonitoredUser() {
				return document.getElementById('step_count_resident_dropdown').getAttribute("selected");
			}
			
			
			function update(date, deviceId) {
				var url = getUrl(date);
				
				$.mobile.loading('show');
				
				d3.json(url, function (error, data) {
					
					if(!error){
					
						$.mobile.loading('hide');
						
						$datepicker.html( "<span class='icon icon-calendar'></span>" + $.fullCalendar.formatDate(date, $.app.dateCalendarFormatter, $.fullCalendar.defaultOptions) );
						updateCalendaer(date);
						
						chart.data(getStepCountDataForDevice(data, deviceId).slice(-7)).render.call(chartContainer);
						initChartControls(chart);
					
					} else {
						$.mobile.loading('hide');
						showErrorPopUp("Error getting data");
					}	
					
				}, 500);
			};
			
			function updateCalendaer(date) {
				if ( $calendar.data('fullCalendar') ) {
					$calendar.fullCalendar('gotoDate', date);
				}
				
				$calendar.data('startdate', $.fullCalendar.formatDate(date, 'yyyyMMdd'));
			};
			
													
			function resize() {
				var w = chartContainer.clientWidth - 5,
					h = chartContainer.clientHeight - 5,
					margin = [ h * 0.15,  // Top 15%
					           w * 0.1,   // Right 10%
					           h * 0.20,  // Bottom 20%
					           w * 0.1 ]; // Left 10%
													
				chart.width(w)
					.height(h)
					.margin(margin)
					.render.call(chartContainer);
				
				initChartControls(chart);
			};
			
			function showErrorPopUp(message) {
				
				var $popup = $('#popup_call_fails');
				
				if (message) {
					$popup.find('.warning').html("<span class='icon icon-warning'></span>" + message);
				}
				
				$popup.popup('open', { transition: 'fade' });
			};

													
			$calendar.on('daychange', function (event, date) {
		  		var now   = new Date(parseInt(getResidentDate()));
		  		
		  		now.setHours(0, 0, 0, 0);
		  		
		  		if ( date < now ) {
		  			$('.select-date .next').fadeIn(250);
		  		} else {
		  			date = now;
		  			$('.select-date .next').fadeOut(250);
		  		}
		  		
				update(date, getDeviceId());
				$calendar.closest('.ui-popup').popup('close');
			});
		  	
		  	$('.select-date .prev').on('click', function (event) {
				var date  = getCurrentDate(),
					now   = new Date(parseInt(getResidentDate()));
				
				now.setHours(0, 0, 0, 0);
				date.setDate(date.getDate() - 7);
				
				if ( date < now ) {
					$('.select-date .next').fadeIn(250);
				}
				
				update(date, getDeviceId());
			});

			$('.select-date .next').on('click', function (event) {
				var date  = getCurrentDate();
				var today = new Date(parseInt(getResidentDate()));
				
				today.setHours(0, 0, 0, 0);
				date.setDate(date.getDate() + 7);
				
				if ( date >= today ){
					date = today;
					$('.select-date .next').fadeOut(250);
				}
				
				update(date, getDeviceId());
			});
		}
		
		function getResidentDate() {
			return document.getElementById("stepCountResidentDate").getAttribute("data-residentDate");
		};
		
		function getCurrentDate() {
			if ( $calendar.data('fullCalendar') ) {
				$calendar.fullCalendar('getDate');
			}
			return d3.time.format('%Y%m%d').parse($calendar.data('startdate').toString());
		};
		
		function getUrl(date){
			if(date != null){
				return chartContainer.getAttribute('data-url') + d3.time.format('%Y%m%d')(date) + '.json';
			}else{
				return chartContainer.getAttribute('data-url') + d3.time.format('%Y%m%d')(getCurrentDate()) + '.json';
			}
		};
		
		function getDeviceId(){
			return chartContainer.getAttribute('data-device');
		};
		
		function getResidentId(){
			return chartContainer.getAttribute('data-residentId');
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
		
	
	
	
});

function converttimetostring(time){
	if (!time) return "";
	var value=time.toString();
	var values = value.split(".");
	var hour= values.length > 0 ? values[0] : "";
	var minutes=values.length > 1 ? values[1] : "";
	minutes = minutes.length === 0 ? '00' : minutes;
	minutes = minutes.length === 1 ? minutes + '0' : minutes;
	return hour + ":" + minutes; 
}


function fetchHiddenParams(el) {
	var $this = $(el);
	var params = {};

	$this.find('input[type=hidden]').each(function(index, value){
		
		params[$(value).attr('name')] = $(value).val();
	});
	
	return params;
}

// TAKE ACTION
$(document).on('pagecreate', '.take-action', function (event, data) {
	var $page = $(this),
		$popup = $page.find('#popup-takeaction')// .popup('option',
												// 'transition', 'fade');
												// //commented because a bug in
												// jquery mobile when change
												// transition on initialized
												// popup
	
	
	$page.find('.listview a[data-rel=ajaxmodal]').on('click', function (event) {
		var $link = $(this);
		
		if ($link.hasClass('execute-action')) {
			$.post($link.attr('href'), fetchHiddenParams(this), function (data, textStatus, jQXhr) {
				$popup.html(data).trigger('popupcreate');
				if ( !$popup.parent().is('.ui-popup-active') ){ // prevents to
																// add to queue
																// twice when is
																// opened
					$.app.popupManager.pushPopup("medium", $popup);
				}
			});
		} else {
			$.get($link.attr('href'), function (data, textStatus, jQXhr) {
				$popup.html(data).trigger('popupcreate');
				if ( !$popup.parent().is('.ui-popup-active') ){ // prevents to
																// add to queue
																// twice when is
																// opened
					$.app.popupManager.pushPopup("medium", $popup);
				}
			});
		}
		
		return false;
	});
	
	$page.find('#register_call').on('click', function (event) {
		 var rootPath = $.app.rootPath || '',
			path     = rootPath + '/actions/call_police/call';					
		 $.getJSON(path, function (data, textStatus, jQXhr) {});			
	});
});

// TAKE PHOTO
$.app.requestingPhoto = false;
$(document).on('pagecreate', '.take-photo', function (event, data) {
	 var $page = $(this),
	 	 $popup = DOMPurify.sanitize($.app.notificationPopup);
	 
	 if ( $.app.requestingPhoto ) {
		 $(this).find('.listview a').addClass('disabled'); 
	 }
	 
	 $page.find('.listview a').on('click', function (event) {  
		
		var $link = $(this);
		
		if ($link.hasClass('disabled')) {
			// a photo request is already on course
			event.preventDefault();
			return;
		}
			
		beforeTakePhoto();
		if ($link.hasClass('execute-action')) {		
			$.post($link.attr('href'), fetchHiddenParams(this), function (data, textStatus, jQXhr) {
				$popup.html(data).trigger('popupcreate');
				if ( !$popup.parent().is('.ui-popup-active') ){
					$.app.popupManager.pushPopup("medium", $popup);
				}
			}).always(afterTakePhoto);
		} else {
			$.get($link.attr('href'), function (data, textStatus, jQXhr) {
				$popup.html(data).trigger('popupcreate');
				if ( !$popup.parent().is('.ui-popup-active') ){
					$.app.popupManager.pushPopup("medium", $popup);
				}
			}).always(afterTakePhoto);
		}
		
		return false; 
	 });	 
	 
	function beforeTakePhoto() {
		// before a photo request, show loader and disable links for cameras
		$.app.requestingPhoto = true;
		$.mobile.loading('show');
		$('.take-photo .listview a').addClass('disabled');
	};
	 
	function afterTakePhoto() {
		// response arrived, hide loader and enable links for cameras
		$.app.requestingPhoto = false;
		$.mobile.loading('hide');
		$('.take-photo .listview a').removeClass('disabled');
	};
	
});
$(document).on('pageshow', '.take-photo', function () {
	 if ( $.app.requestingPhoto ) {
		 $.mobile.loading('show');
	 }
});

// PHOTO DETAIL
$(document).on('pagecreate', '.photo-detail', function (event, data) {
	var $page = $(this),
		$carouselImages = $page.find('#carousel-images').css('overflow', 'visible'),
		$carouselNav    = $page.find('#carousel-nav'),
		currentPosition = $carouselImages.position().left,
		leftLimit       = 0,
		rightLimit      = $carouselImages.find('li').length - 1,
		index = 0;
	
	function autoSwipe(_index) {
		index = (_index < leftLimit) ? leftLimit : (_index > rightLimit) ? rightLimit : _index;
		var position = -$carouselImages.find('li:eq(' + index + ')').position().left; 
		
		$carouselImages
			.removeClass('cancel-animate')
			.css(Modernizr.prefixed('transform'), 'translate3d('+position+'px,0px,0px)');
		currentPosition = position;
		
		$carouselNav.find('.active').removeClass('active');
		$carouselNav.find('a:eq(' + index + ')').addClass('active');
	}
	
	$carouselNav.on('click', 'a', function (event) {
		autoSwipe($carouselNav.find('a').index(this));
		
		return false;
	});
	
	$carouselImages.swipe({
		allowPageScroll: $.fn.swipe.pageScroll.VERTICAL,
		swipeStatus: function (event, phase, direction, distance, fingerCount) {
			
			if (phase == 'move' && (direction == 'left' || direction == 'right')) {
				var position;
				
				if (direction == 'left') {
					position = currentPosition - distance;
					$carouselImages.addClass('cancel-animate').css(Modernizr.prefixed('transform'), 'translate3d('+position+'px,0px,0px)');
					
				}
				if (direction == 'right') {
					position = currentPosition + distance;
					$carouselImages.addClass('cancel-animate').css(Modernizr.prefixed('transform'), 'translate3d('+position+'px,0px,0px)');
				}
				
				
			}
			if (phase == 'end' || phase == 'cancel') {
				/*
				 * currentPosition = $carouselImages.position().left;
				 * 
				 * var swipeTo = ($.app.isRTL) ? Math.round(currentPosition /
				 * $carouselImages.width()) : Math.round(-currentPosition /
				 * $carouselImages.width());
				 * 
				 * autoSwipe(swipeTo)
				 */
				
				var swipeThreshold	= 0.15 * $carouselImages.width();  // minimum
																		// distance
																		// to
																		// swipe
																		// to
																		// change
																		// image,
																		// 20%
																		// of
																		// the
																		// width
																		// of an
																		// image
				
				var step = 0;
				if (direction == 'left' && distance >= swipeThreshold) {
					step = 1;
				} else if (direction == 'right' && distance >= swipeThreshold) {
					step = -1;
				} 
				
				if ($.app.isRTL) { step = -step; }
				
				autoSwipe(index + step);
			}
		}
	});
});


// BROADCAST MESSAGE POP UP / CLOSE EVENT POP UP
$(document).on('popupcreate', '.take-action-popup, #closeAlarmPopup', function (event, ui) {
	
	var $popup  = $(this), 
		$radios = $popup.find(':radio'),
		$text   = $popup.find(':text'),
		$form   = $popup.find('form'),
		$reasonSelect 	= $popup.find('select[name=smsgReason]'),
		$detailsInput 	= $popup.find('input[name=smsgDetails]');
		
// $text.on('keyup change paste', function (event) {
// $radios.removeAttr('checked').parent().removeClass('checked');
// });
// $radios.on('change', function (event) {
// if ($(this).is(':checked')) {
// $text.val('');
// }
// });
	
	function showErrorQtip($input) {
		if ( !$input.data('qtip') ) {
			$input.qtip({
				content: { text: I18n.t('validations.required') },
				position: { my: 'bottom right', at: 'top right', container: $popup, viewport: $(window) },
				show: { event: 'focus', solo: true },
				hide: { event: 'blur' },
				style: { classes: 'qtip-red qtip-rounded qtip-shadow' }
			});
		} 
	};
	
	function validateRadios(){
		var valid = !$radios.length || !!$radios.filter(':checked').length;
		
		if ( !valid ) {
			$radios.addClass('changed invalid').parent().addClass('invalid');
			showErrorQtip($radios.first());
		} else {
			$radios.removeClass('invalid').parent().removeClass('invalid');
			$radios.first().qtip('destroy');
		}
		
		return valid;
	};
	
	function validateSelect() {
		if ($reasonSelect.length && $reasonSelect.val() === '') { // not valid
			$reasonSelect.addClass('changed invalid');
			showErrorQtip($reasonSelect);
			return false;
		} else {
			$reasonSelect.removeClass('invalid');
			$reasonSelect.qtip('destroy');
			return true;
		}
	}
	
	function validateText() {
		var valid = !$text.prop('required') || !/^\s*$/.test( $text.val() );
		
		if ( !valid ){
			$text.addClass('invalid changed');
			showErrorQtip($text);
		} else {
			$text.removeClass('invalid');
			$text.qtip('destroy');
		}
		
		return valid;
	};
	
	$form.attr('novalidate', 'novalidate');
	
	$form.on('change', ':radio.changed', validateRadios);
	$form.on('keyup', ':text.changed', validateText);
	$reasonSelect.change(function() {
		var detailsRequired = $reasonSelect.find('option[value=' + $reasonSelect.val() + ']').attr('data-details-required');
		
		if (detailsRequired === 'true') {
			$detailsInput.attr('required', true);
		} else {
			$detailsInput.removeAttr('required').removeClass('invalid');
			$text.qtip('destroy');
		}
		
		if ($reasonSelect.val()) {
			$reasonSelect.removeClass('empty');
		} else {
			$reasonSelect.addClass('empty');
		}
		
		validateSelect();
	});

	
	$form.on('submit', function (event) {
		var validRadios = validateRadios(),
			validSelect = validateSelect(),
			validText   = validateText(),
			valid = validRadios && validSelect && validText;
		
		
		if (!valid) {
			var $firstInvalid = $form.find('input.invalid, select.invalid').first();
			$firstInvalid.focus();
			return false;
		}
		
		if ($popup.is('#closeAlarmPopup')) {
			
			if($form.find('[type=submit]')){
				$form.find('[type=submit]').attr('disabled', 'disabled');
			}
			
			$.post($form.attr('action'), $form.serialize(), function (data, textStatus, jQXhr) {
				// console.log(data);
				
				// ESC17-4590 -> 167 is an error that needs to be shown to the user (Xss potential attack)
				if (data == 167) {
					document.getElementById('alertXssErrorMessageText').className = "advise";
					$("input").removeAttr('disabled');
				}
				else if (data >= 0)  {
					$popup.dispose = true;
					$popup.popup('close');
					$.mobile.changePage($.app.rootPath + '/home/history', {reloadPage: true, changeHash: false});
				} else {
					document.getElementById('alertErrorMessageText').className = "advise";
				}
			});
		} else {
			// $popup.find('#close-popup').trigger('click');
			$.post($form.attr('action'), $form.serialize(), function (data, textStatus, jQXhr) {
				// console.log(data);
				$popup.html(data).trigger('popupcreate');
				if ( !$popup.parent().is('.ui-popup-active') ){
					$.app.popupManager.pushPopup("low", $popup);
				}
				
				// Refresh the page when the bradocast message is in events log
				// page
				if ($form.parents('.events-log').length > 0) {
					$.mobile.changePage($.mobile.path.getLocation(), {reloadPage: true, changeHash: false});
				}
					
			});
		}
		
		return false;
	});
	
	$popup.on('popupafterclose', function (event) {
		// Workaround for ESC17-6930 (issue with Broadcast action after jQuery update):
		$.mobile.changePage($.app.rootPath + '/home/history', {reloadPage: true, changeHash: false});
	});
});


function isMobile(){

	var isMobile = {
	    Android: function() {
	        return navigator.userAgent.match(/Android/i);
	    },
	    BlackBerry: function() {
	        return navigator.userAgent.match(/BlackBerry/i);
	    },
	    iOS: function() {
	        return navigator.userAgent.match(/iPhone|iPad|iPod/i);
	    },
	    Opera: function() {
	        return navigator.userAgent.match(/Opera Mini/i);
	    },
	    Windows: function() {
	        return navigator.userAgent.match(/IEMobile/i);
	    }
	 };		

	 if ((isMobile.Android())|| (isMobile.BlackBerry()) || (isMobile.iOS()) ||  (isMobile.Opera()) || (isMobile.Windows())){
		 return true;
	 }else{
		 $("#orientation_change" ).remove();
		 return false;
	 }
}


function checkScreenSize(){
	
		var isMobile = {
		    Android: function() {
		        return navigator.userAgent.match(/Android/i);
		    },
		    BlackBerry: function() {
		        return navigator.userAgent.match(/BlackBerry/i);
		    },
		    iOS: function() {
		        return navigator.userAgent.match(/iPhone|iPad|iPod/i);
		    },
		    Opera: function() {
		        return navigator.userAgent.match(/Opera Mini/i);
		    },
		    Windows: function() {
		        return navigator.userAgent.match(/IEMobile/i);
		    }
		 };		
		
		 setTimeout(function(){  
		      if (isMobile.iOS()){ 
		        if($(window).height() < $(window).width()){ 
		            $("#orientation_change").css('display', 'block');   
		          } 
		          else if($(window).height() > $(window).width()){ 
		            $("#orientation_change").css('display', 'none'); 
		          } 
		      }
		      else if(isMobile.Android() || isMobile.BlackBerry() || isMobile.Opera() || isMobile.Windows()){ 
		          if(screen.height < screen.width){ 
		              $("#orientation_change").css('display', 'block');   
		            } 
		            else if(screen.height > screen.width){ 
		              $("#orientation_change").css('display', 'none'); 
		            } 
		        } 
		 	}, 500); 
}

$(window).on('resize', checkScreenSize);

// Adjust Eula height in Mobile if it exists
$(document).on('pagecreate', function () {
	var $iframe = $('#eulaIframeMobile');
	if ($iframe.length) {
		var height =$(window).height() - $('header').outerHeight(true) - $('div.subheader').outerHeight(true) - $('footer').outerHeight(true) - 20;
		$iframe.height(height);
	}
});

// Handle Embedded map in mobile view
$(document).on('pageshow', function () {
	if (typeof mobileMapId !== 'undefined' && !!mobileMapId && !!evLat && !! evLng && !!evAccuracy) {
		
		if ($('#map_container').length > 0) { 
			// Set the height for the MAP
			var height =$(window).outerHeight(true) - $('#main-header').outerHeight(true) - $('#main-footer').outerHeight(true) - $('#showAlertLocationBtn').outerHeight(true);
			
			$('#map_container','.event-detail .content').height(height);
		}
		
		if ($('#alert_tabs_loc').length > 0) { 
			$('#showAlertDetailsBtn').click(function() {
				$(this).addClass("sel");$('#showAlertLocationBtn').removeClass("sel");
				$('#alert_details_' + alertId).show();
				$('#map_container').hide();
			});
			$('#showAlertLocationBtn').click(function() {
				$(this).addClass("sel");$('#showAlertDetailsBtn').removeClass("sel");
				$('#alert_details_' + alertId).hide();
				$('#map_container').show();
				// Create the map
				initMapByHTMLElementId(evLat, evLng, evAccuracy, mobileMapId);
			});
			
		}
	}
});

	

