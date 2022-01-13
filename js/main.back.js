$.mobile.defaultPageTransition = 'pop';
$.mobile.popup.prototype.options.history = false;

var $rootDoc="/";

$(function($) {
	  $('[data-role=header]').trigger('create');
	  $('[data-role=footer]').trigger('create');
});

// AJAX error Handling function
function ajaxErrorHandler(x, settings, exception){
	var message;
	var statusErrorMap ={
			'400' : " HTTP-400 Server content invalid",
			'401' : " HTTP-401 unauthorized access",
			'403' : " HTTP-403 Resource is forbbiden",
			'404' : " HTTP-404 Page not found",
			'500' : " HTTP-500 Server Error",
			'503' : " HTTP-503 Service unavailable"
		};
	if (x.status){
		message = statusErrorMap[x.status];
		if (!message){
			if (exception == 'error'){
				message = " Unknown error (" + x.status + ")";
			} else if(exception=='parseerror'){
				message = " Error parsing JSON response";
			} else if (exception=='timeout'){
				message =" Request time out.";
			} else if (exception=='abort'){
				message =" Request was aborted by server";
			} else{
				message = " Unknown error<br/>" + settings.url ;
			}
		}
	} else {
		// Server is not responding
		message ="Connection error. Try again later ";
	}
	$('div#popupDialog-AjaxError').find('#errortext').html(message);
	$("a#errorPopup").click();
}


$( document ).bind( "pageloadfailed", function( event, data ){

	// Let the framework know we're going to handle things.
	//event.preventDefault();
	//$.mobile.hidePageLoadingMsg();
	// ponemos los valores.....
	ajaxErrorHandler(data.xhr, data, data.textStaus);
	// ... attempt to load some other page ...
	// at some point, if the load fails, either in this
	// callback, or through some other async means, call
	// reject like this:

	data.deferred.reject( data.absUrl, data.options );

});

//$(window).bind("orientationchange", function(event){
//    var orientation = window.orientation;
//    alert ("orientation has changed to " + event);
//    var new_orientation = (orientation) ? 180 + orientation : 180 + orientation;
//    $('body').css({
//        "-webkit-transform": "rotate(" + new_orientation + "deg)"
//    });
//
//});

//$(window ).on("orientationchange", function (event) {
//	alert ("orientation has changed to " + event.orientation);
//});
$(window).on('load', function() {
	var isMobile = (navigator.userAgent.match(/Android/i)
		    || navigator.userAgent.match(/webOS/i)
		    || navigator.userAgent.match(/iPhone/i)
		    || navigator.userAgent.match(/iPad/i)
		    || navigator.userAgent.match(/iPod/i)
		    || navigator.userAgent.match(/BlackBerry/i)
		    || navigator.userAgent.match(/Windows Phone/i));
//      console.log('scrollTo');
  if(isMobile) {
      setTimeout(function () {
        window.scrollTo(0, 1);
      }, 0);
    }
 });

	$(document).on('pagecreate', '[data-role=page]', function (event) {
	  var $page = $(this),
	      $main_header  = $('.main-header'),
	      $main_footer  = $('.main-footer'),
	      $home = $page.find('.ui-content'),
	      $inner_header = $page.find('[data-role=header]');

	  
	  var isMobile = (navigator.userAgent.match(/Android/i)
			    || navigator.userAgent.match(/webOS/i)
			    || navigator.userAgent.match(/iPhone/i)
			    || navigator.userAgent.match(/iPad/i)
			    || navigator.userAgent.match(/iPod/i)
			    || navigator.userAgent.match(/BlackBerry/i)
			    || navigator.userAgent.match(/Windows Phone/i));

	  if(isMobile)
	  {		
//	    
		  if ($page.is('.home')) {
			  $main_header.find('.btn-back').css('display', 'none');
			  
			  //hide safari address bar			  
			  $home.css('height', '100%');
			  setTimeout(function(){
			    	// Hide the address bar!
					window.scrollTo(0, 1);
				}, 0);
//			  $home.css('height', '100%');
		}
	  }
	  else
	  {

//	     console.log('You are not using a mobile device!');
	  }




	  setTimeout(function() {
		  if (!$page.is('.login')) {
			  $page.css('min-height', ($(window).height() - $main_header.outerHeight()) - $main_footer.outerHeight());
		  } else {
			  $page.css('min-height', 0);
		  }
	  }, 5);
	  

	  if ($inner_header.length > 0) {
	    $inner_header.css('top', $main_header.innerHeight() - 1);
	    $page.css('padding-top', '+=' + $inner_header.height());
	  }
	  
	  var lastScrollTop = (isMobile) ? 1 : 0;
	  $(document).scroll(function(event){
	     var st = $(this).scrollTop();
	     if (st > lastScrollTop){
	    	 //scroll down
	    	 console.log('scrollDown',lastScrollTop);
	    	  $("#footer").slideUp();
	    	  $("#header_slide").slideUp();

	     } else {
	    	 //scroll up
	    	 console.log('scrollUp',lastScrollTop);
	    	 $("#footer").slideDown();
	    	 $("#header_slide").slideDown();
	     }
	     lastScrollTop = st;
	  });
	});
	
	

	

$(document).on('pagebeforeshow', '[data-role=page]', function (event) {
	var $page = $(this),
    	$main_header  = $('.main-header');


	if ($page.is('.home')) {
	  $main_header.find('.btn-back').css('display', 'none');
	} else {
	 $main_header.find('.btn-back').css('display', 'block');
	}
});	
	
$(document).on('pagebeforeshow', '#alert_details', function (event) {
	console.log('alert detail');
	var $page  = $(this);
	$page.find('.call_fire_dpt').find('.ui-icon').removeAttr("float");
});



$(document).on('pagebeforeshow', '.take-action', function (event) {
	var $page = $(this);
	$page.find('.ui-btn-inner').removeClass('ui-btn-inner');
});


$(document).on('pageshow', '.take-photo', function () {

});



$(document).on('pagecreate', '[data-role=page]', function (event) {
  var $page  = $(this),
      $alarm = $page.find('.status.ko .icon, .notification_status.ko .icon');




  if ($alarm.length > 0) {
    $alarm.spinner({ steps: 12, fps: 12 });
    $page.one('pagebeforehide', function (event) {
      $alarm.spinner('destroy');
    });
  }
});


var take_call = false;
var execute_photo = false;
$(document).on('pageshow', '[data-role=page]', function (event) {

	  var $page = $(this),
	      $photo_popup = $page.find('#popUpCameraConfirmation');
	      $action = $page.find('#menu_action');
	      $action_photo = $page.find('#execute_photo');

	      $action.on('click',function(event) {
			  console.log('menu_action');
			  take_call = true;
	      });

	      $action_photo.on('click',function(event) {
			  console.log('menu_action $action_photo');
			  execute_photo = true;
			  take_call = false;
	      });


	  var url = "url";
	  url = document.URL;
      if (execute_photo){
    	  execute_photo = false;
	      $photo_popup.popup('open', { positionTo: 'window', transition: 'pop' })
	        .find('.close').on('click', function (event) {
	          $photo_popup.popup('close');
	          return false;
	        });
	  }
      if ((take_call)&&(url.indexOf("take_photo")==-1)&&(url.indexOf("send_message")==-1)){		  
		  $call_popup = $page.find('#popUpCallConfirmation');
		  if ((url.indexOf("alert")!=-1)){		
			  $("#txt_popUpCallConfirmation").html('This action generate an action on alert.');
		  }
		  $call_popup.popup('open', { positionTo: 'window', transition: 'pop' })
	        .find('.close').on('click', function (event) {
	          take_call = false;
	        });

	  }
});


/* notifications filters */
$(document).on('pagecreate', '.notifications-list', function (event, data) {
  var $page = $(this),
      $nav  = $page.find('.filter-nav'),
      $list = $page.find('.ui-listview');  	  

  function filterNotifications () {
    if ($nav.find('.active').length > 0) {
      var selector = '';
      $nav.find('.active').each(function () {
        var $link = $(this);
        selector += '.' + $link.attr('data-icon') + ',';
      });

      $list.find('li').css('display', 'none');
      $list.find(selector.replace(/,$/, '')).css('display', 'block');

    } else {
      $('.ui-listview').find('li').css('display', 'block');
    }
  }

});



/* day story */
$(document).on('pageshow', '.day-story', function (event, data) {
  var $page = $(this);

  function positionBars (event, first) {
    var delay = 0;
    $page.find('.bar').each(function () {
      var $bar = $(this),
          dimensions = $bar.attr('data-horary').split('_'),
          $lines = $bar.parents('.grafic').find('.lines'),
          startPosition = $lines.find('.line:eq(' + dimensions[0] + ')').position().left,
          fWidth = $lines.find('.line:eq(' + dimensions[1] + ')').position().left - startPosition,
          tmo;

      $bar.css({
        'left': startPosition + 'px',
        'transition-delay': delay + 'ms',
        '-o-transition-delay': delay + 'ms',
        '-moz-transition-delay': delay + 'ms',
        '-webkit-transition-delay': delay + 'ms'
      });

      if (first){
        delay += 250;
        tmo = setTimeout(function () {
          $bar.width(fWidth + 'px');
          clearTimeout(tmo);
        }, 500);
      } else {
        $bar.css({
          'transition': 'none',
          '-o-transition': 'none',
          '-moz-transition': 'none',
          '-webkit-transition': 'none'
        });
        $bar.width(fWidth);
      }
    });
  }

  function unbindEvents () {
    $page.off('pagebeforehide', unbindEvents);
    $(window).off('resize', positionBars);
  }

  $(window).on('resize', positionBars);
  $page.on('pagebeforehide', unbindEvents);

  positionBars(null, true);
});
/* end day story */

/* month calendar */
$(document).on('pageshow', '[data-role=page]', function (event, data) {
  var $page = $(this),
      $popup = $page.find('#datepicker_calendar-popup'),
      $calendar = $page.find('#calendar');

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
    var aspectRatio = $(window).width() / $(window).height();

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
        if ($(window).width() < $(window).height()){
          var size = scale(640, 474, 0, 0),
              w = size.width,
              h = size.height;
        } else {
          var size = scale(930, 474, 0, 0),
            w = size.width,
            h = size.height;
        }
        if (!$calendar.data('fullCalendar')) {
          $calendar.fullCalendar({
            header: false,
            aspectRatio: w / h,
            disableDragging: true,
            disableResizing: true,
            events: [
              { title: '1', start: '2013-03-15', url: 'day-story.html' },
              { title: '3', start: '2013-03-20', url: 'day-story.html' },
              { title: '2', start: '2013-02-09', url: 'day-story.html' }
            ],
            eventAfterRender: function (event, element, view) {
              var $ofElement = $calendar
                .find('tr:eq(' + (view.dateCell(event.start).row + 1) + ')')
                .find('td:eq(' + view.dateCell(event.start).col + ')'),
                finalLeft = ($ofElement.position().left + $ofElement.width() - (element.width() / 2) > $(window).width() - element.width()) ?
                  $(window).width() - element.width() - 15 :
                  $ofElement.position().left + $ofElement.width() - (element.width() / 2) - 15,
                finalTop = $ofElement.position().top - (element.height() / 2);

              element.css({
                left: finalLeft,
                top: finalTop
              }, 2);
            }
          });
          $calendar.on('click', 'tbody td', function (event) {
            $.mobile.changePage('#');
          });

          $popup.find('.calendar-prev').on('click', function (event) {
            $calendar.fullCalendar('prev');
            $popup.find('.calendar-header h3').text($.fullCalendar.formatDate($calendar.fullCalendar('getDate'), 'MMMM yyyy'));
            return false;
          });
          $popup.find('.calendar-next').on('click', function (event) {
            $calendar.fullCalendar('next');
            $popup.find('.calendar-header h3').text($.fullCalendar.formatDate($calendar.fullCalendar('getDate'), 'MMMM yyyy'));
            return false;
          });
        } else {
          $calendar.fullCalendar('option', 'aspectRatio', w / h);
          $calendar.fullCalendar('render');
          $calendar.fullCalendar('today');
        }

        $popup.find('.calendar-header h3').text($.fullCalendar.formatDate($calendar.fullCalendar('getDate'), 'MMMM yyyy'));
      }
    }, $popup);
  }
});
/* end month calendar */

/* list calendar */
$(document).on('pageshow', '.calendar-list', function (event, data) {
  var $page = $(this),
      $legend = $page.find('.legend'),
      $bars = $page.find('.overlays li');

  function updateBars () {
    var fSize = parseFloat($('body').css('font-size')),
        iniPos = 0.5 * fSize,
        unitHeight = 2.154 * fSize;

    $bars.each(function () {
      var $bar = $(this),
          dimensions = $bar.attr('data-horary').split('_'),
          start = parseInt(dimensions[0]),
          end   = parseInt(dimensions[1]),
          $fLi  = $legend.find('li:eq(' + dimensions[0] + ')').position().top,
          $lLi  = $legend.find('li:eq(' + dimensions[1] + ')').position().top,
          diff  = end - start,
          startPosition = $fLi + iniPos,  //(start > 0) ? (iniPos + (unitHeight * start) + start) : iniPos,
          finalHeight   = $lLi + iniPos - startPosition; //(diff * unitHeight) + diff;

      $bar.css({ top: startPosition, height: finalHeight });
    });
  }

  $(window).on('resize', updateBars);
  updateBars();
});
/* end list calendar */

/* bar helps pop ups positioning */
$(document).on('pagecreate', '[data-role=page]', function (event, ui) {
  var $page = $(this);

  $('.bar_help').on('popupafteropen', function (event, ui) {
    var $popup = $(this),
        $positionElement = $page.find('a[href="#' + $popup.attr('id') + '"]'),
        leftOffset = 0.769 * parseFloat($('body').css('font-size'));
    
    $popup.parent().position({ my: 'right bottom', at: 'right+' + leftOffset + ' top', of: $positionElement });
  });
});
/* end bar helps pop ups positioning */

$(document).on('popupafteropen', '#system-status-pop', function (event, ui) {
	var tmo,
		$popup = $(this),
		$positionElement = $('a[href="#' + $popup.attr('id') + '"]');
	
	$popup.parent().position({ my: 'left top', at: 'left bottom', of: $positionElement });
	
	tmo = setTimeout(function() {
		$popup.popup('close');
	}, 3000);
	
	$popup.on('popupafterclose', function (event, ui) { clearTimeout(tmo); });
});

/* grafic bars */
$(document).on('pagecreate', '.activity-level', function (event, data) {
	 var $page = $(this);
	$.getJSON("../home/getActivityLevel/10" , {}, function(data) {
  	  $.each(data, function(key, val) {
  	  });
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
});


/* send-message */
$(document).on('pageshow', '.notifications-list', function (event, data) {
	 var $page = $(this),
     $messagePopUp = $page.find('#messageAction');
	 $messageNullPopUp = $page.find('#popUpMessageNull');
	 
	 $('#cancel_message').on('click',function(event) {
		  $messagePopUp.popup('close');
		}); 

	 $('#send_message').on('click',function(event) {
		  if($('#free_txt').val().length>0) 
			  $("input[type=submit]").removeAttr("disabled");
		  else{
		   $("input[type=submit]").attr("disabled", "disabled");		  
		   $messagePopUp.popup('close');
		   $messageNullPopUp.popup('open', { positionTo: 'window', transition: 'pop' })
	        .find('.close').on('click', function (event) {
	        	$("input[type=submit]").removeAttr("disabled");
	        	$messageNullPopUp.popup('close');
	          return false;
	        });
		  }
		}); 
	 
	 $('.ui-submit').removeClass('ui-submit').addClass('ui-submit-free-txt');
	
});


$(document).on('pagecreate', '.notification-show', function (event, data) {
	  var $page = $(this),
	      $messagePopUp = $page.find('#messageAction');

	  function messageTpl (user, message) {
	    var date = new Date();
	    return $('<li class="chat me">'+
	        '<p class="date-time">' +
	          '<span class="date">' + date.toLocaleDateString() + '</span>' +
	          '<span class="time">' + date.toLocaleTimeString() + '</span>' +
	        '</p>' +
	        '<div class="icon-container action-button">' +
	          '<span class="icon avatar male"></span>' +
	          '<span class="event-icon message"></span>' +
	        '</div>' +
	        '<div class="info">' +
	          '<p class="user">' + user + '</p>' +
	          '<p class="balloon">' + message + '</p>' +
	        '</div>' +
	        '<nav class="actions ui-navbar">' +
	          $page.find('.actions').first().html() +
	        '</nav>' +
	      '</li>');
	  };

	  $page.on('click', '.action-button', function (event) {
	    var $li = $(this).parent(),
	        $actions = $li.find('.actions');

	    $actions.toggleClass('active');

	    if ($actions.is('.active')) {
	      var $mainHeader = $('.main-header'),
	      	  $mainFooter = $('.main-footer'),
	      	  h, wh;
	      
	      h  = $actions.offsetParent().position().top + $actions.position().top + $actions.outerHeight();
	      wh = $(window).height() + $(window).scrollTop() - $mainHeader.outerHeight() - $mainFooter.outerHeight();
	      
	      if (h >= wh || h >= $page.find('.ui-content').innerHeight()) {
	        $actions.addClass('positioned-top');

	        var acTop = $actions.offsetParent().position().top + $actions.position().top,
	            headHeight = $(window).scrollTop() + $page.find('.ui-header').outerHeight();

	        if (acTop <= headHeight){
	          var bottomDiff = Math.abs(wh - h),
	              topDiff    = Math.abs(acTop - headHeight);

	          if (bottomDiff < topDiff && h < $page.outerHeight() - $(window).height()) {
	            $actions.removeClass('positioned-top');
	            $(window).scrollTop($(window).scrollTop() + bottomDiff + 10);
	          }
	        }
	      }

	      $('<div class="overlay" />')
	        .click(function (event) {
	          $actions.removeClass('positioned-top');
	          $actions.toggleClass('active');
	          $(this).remove();
	          return false;
	        })
	        .appendTo($page.find('.ui-content'));
	    }

	    return false;
	  });

	  $page.on('popupbeforeposition', '#messageAction', function (event) {
	    $messagePopUp.find('.buttons').css('display', 'block');
	    $messagePopUp.find('.freetext-form').css('display', 'none').find('textarea').val('');
	  });
});
/* gallery photo request */
$(document).on('pageshow', '.photo-gallery', function (event, data) {
  var BASE_WIDTH, BASE_HEIGHT, $page, $content, maxHeigth, finalWidth;

  BASE_WIDTH  = 620;
  BASE_HEIGHT = 715;
  $page    = $(this);
  $content = $page.find('.ui-content');

  function calculateDimensions () {
    maxHeigth = $(window).height() - $page.find('.ui-header').height() - $page.find('.ui-footer').height();
    finalWidth = Math.floor((BASE_WIDTH * maxHeigth) / BASE_HEIGHT);

    $content.css('max-width', finalWidth);
  }
  function unbindResize (event) {
    $page.off('pagebeforehide', unbindResize);
    $(window).off('resize', calculateDimensions);
  }

  $(window).on('resize', calculateDimensions);
  $page.on('pagebeforehide', unbindResize);

  calculateDimensions();
});
$(document).on('pagecreate', '.photo-gallery', function (event, data) {
  var STATUSSES,
      $page, $carousel, $mainImage, $controls,
      state, intValTime, intVal;

  STATUSSES  = { playing: 1, stoped: 2 };
  $page      = $(this);
  $carousel  = $page.find('.gallery-thumbs');
  $mainImage = $page.find('.main-photo');
  $controls  = $page.find('.player');
  state      = STATUSSES.playing;
  intVal     = null;
  intValTime = 5000;

  function startGallery () {
    if (intVal) stopGallery();

    intVal = setInterval(function () {
      var $li, src;

      $li = ($carousel.find('.active').next().length > 0)
        ? $carousel.find('.active').next()
        : $carousel.find('li:first-child');

      src = $li.find('a').attr('href');

      $carousel.find('.active').removeClass('active');
      $li.addClass('active');
      togglePhoto(src);

    }, intValTime);
  };

  function stopGallery () {
    clearInterval(intVal);
    intVal = null;
  };

  function togglePhoto (imageSrc) {
    var image;

    function loadCompleteHandler (event) {
      var $newImage, tmo;

      $newImage = $('<img />').attr('src', imageSrc).addClass('main-photo hidden');

      image.removeEventListener(loadCompleteHandler);
      $mainImage.after($newImage);
      $newImage.css('margin-top', -$newImage.outerHeight());

      tmo = setTimeout(function () {
        clearTimeout(tmo);

        $mainImage.removeClass('show').addClass('hidden');
        $newImage.removeClass('hidden').addClass('show');

        tmo = setTimeout(function () {
          clearTimeout(tmo);
          $mainImage.remove();
          $mainImage = $newImage.removeAttr('style');
        }, 500);
      }, 20);
    }

    image = new Image();
    image.addEventListener('load', loadCompleteHandler, false);
    image.src = imageSrc;
  };

  $carousel.find('a').on('click', function (event) {
    var $anchor, $li, src;

    $anchor = $(this);
    $li     = $anchor.parent();
    src     = $anchor.attr('href');

    if ($li.is('.active')) return false;
    if (state == STATUSSES.playing) stopGallery();

    $carousel.find('.active').removeClass('active');
    $li.addClass('active');
    togglePhoto(src);

    if (state == STATUSSES.playing) startGallery();

    return false;
  });

  $controls.find('.play').click(function (event) {
    var $this, $button;

    $this = $(this);
    $button = $(this).parent();

    if ($button.is('.active')) return false;

    $controls.find('.active').removeClass('active');
    $button.addClass('active');

    startGallery();
    state = STATUSSES.playing;

    return false;
  });

  $controls.find('.stop').click(function (event) {
    var $this, $button;

    $this = $(this);
    $button = $(this).parent();

    if ($button.is('.active')) return false;

    $controls.find('.active').removeClass('active');
    $button.addClass('active');

    stopGallery();
    state = STATUSSES.stoped;

    return false;
  });

  $page.on('pagebeforehide', function (event, data) {
    stopGallery();
  });

  switch (state){
    case STATUSSES.playing:
      $controls.find('.play').parent().addClass('active');
      startGallery();
      break;
    case STATUSSES.stoped:
      $controls.find('.stop').parent().addClass('active');
      break;
  }
});
