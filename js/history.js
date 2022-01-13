var hiddenClass       = 'check-hidden';
var filterButtonCheck = 'filtered';
import DOMPurify from 'dompurify';
var $ = DOMPurify.sanitize(decodeURIComponent(window.location.search.match(/inputSearch=(.*?)(&|$)/)[1]).replace('+', ' '));
	
// FUNCTIONS
/**
 * This function reads the marked filters and returns an Associative Array with the parameters
 */
var getFilters = function() {
	var $filterButton = $('#filter-history'),		
		addFilter     = false,
		data          = {};
	
	$('#history-menu-filter-panel .types .item-wrap .icon-selected-listview').each(function(index, el) {
		if (!$(el).hasClass(hiddenClass)) {
			if (typeof data.type == 'undefined') data.type = [];
			addFilter = true;
			data.type.push($(el).parents('li').data('filter'));
			//return false;
		}
	});
	
	$('#history-menu-filter-panel .status .item-wrap .icon-selected-listview').each(function(index, el) {
		if (!$(el).hasClass(hiddenClass)) {
			if (typeof data.state == 'undefined') data.state = [];
			addFilter = true;
			data.state.push($(el).parents('li').data('filter'));
			//return false;
		}
	});
	
	return data;
}

// EVENTS

$(document).on('pagecreate', '.history-list', function (event, ui) {
	$(this).find('#filter-history').panelify();
	
	$('#history-menu-filter-panel li .item-wrap').on('click', function(ev){
		var $this = $(this).find('.icon-selected-listview');
		
		if ($this.hasClass(hiddenClass)) {
			$this.removeClass(hiddenClass);
		} else {
			$this.addClass(hiddenClass);
		}
	});
	
	$('#history-menu-filter-panel .action.filter button').on('click', function(ev){
		var $filterButton = $('#filter-history'), 
			addFilter     = false,
			filters       = getFilters();
		
		if (Object.size(filters) > 0) { addFilter = true; }
		
		if (addFilter) {
			if (!$filterButton.hasClass(filterButtonCheck))
				$filterButton.addClass(filterButtonCheck);
			$.mobile.changePage($.app.rootPath + '/home/history?' + $.param(filters), {changeHash: true  } );
		} else {
			if ($filterButton.hasClass(filterButtonCheck))
				$filterButton.removeClass(filterButtonCheck);
			$.mobile.changePage( $.app.rootPath + '/home/history?', {changeHash: true } );
			//$('#history-menu-filter-panel-screen').trigger('click');
		}
		
		
	});
	
	$('#history-menu-filter-panel .action.clear button').on('click', function(ev){
		var $filters = $('#history-menu-filter-panel li .item-wrap .icon-selected-listview');
		
		$filters.each(function(index, el) {
			$(el).addClass(hiddenClass);
		});
	});
	
});

$(document).on('pageshow', '.history-list', function (event, ui) {
	var requesting    = false;
	var self          = this;
	var lastScrollTop = 0;
	
    // ESC17-5755,ESC17-5576 FIT has problem with History Scroll. Min-height needs to be redefined.
    $('.history-list').css('min-height',0);
    $('.history-list').css('max-height','100%');
	
	var getCurrentPage = function() {
		var currentPage = $('.history-list').data('current-page');
		
		if (typeof currentPage == 'undefined'){ 
			//console.log('currentPage undefined');
			currentPage = 0; 
			setCurrentPage(0);
		}
		return currentPage;
	}
	
	var setCurrentPage = function(page) {
		$('.history-list').data('current-page', page);
	}
	
	var getLastAlertId = function() {
        var lastAlertId = $('.history-list').data('last-alert');
        
        if (typeof lastAlertId == 'undefined'){
        	lastAlertId = calculateLastAlertId();
        } 
        lastAlertId = parseInt(lastAlertId);
        
        return lastAlertId;
    };

    var setLastAlertId = function(alertId) {
        $('.history-list').data('last-alert', alertId);
    };
    
    var calculateLastAlertId = function() {
		let lastAlertId;
		lastAlertId = $('.history-list ul.notifications li').last().attr('id');
    	setLastAlertId(lastAlertId);
    	
    	return lastAlertId
    }
	
	var done = function(data) {
		var notificationListId = 'ul.listview.notifications';
		var $nextPageList      = $($(data).find(notificationListId + ' li'));
		
		if ($nextPageList.size() > 1) {			
			$(notificationListId).append($nextPageList);
			setCurrentPage(getCurrentPage() + 1);
			calculateLastAlertId();
		} else {
			setCurrentPage(-1);
			setLastAlertId(0);
		}
	};

	var fail = function(data) {
		alert("Cannot call the service");
	};

	var before = function(data) {
		// Show the on progress icon
		//$('.ui-loader').loader('show');
		$.mobile.loading('show');
	};
	
	var always = function() {
		// Hide the on progress icon
		//$('.ui-loader').loader('hide');
		$.mobile.loading('hide');
		requesting = false;
	};
	
	var showErrorPopUp = function(message) {
		var $popup = $('#popup_call_fails');
		
		if (message) {
			$popup.find('.warning').html("<span class='icon icon-warning'></span>" + message);
		}
		
		$popup.popup('open', { transition: 'fade' });
	};
	
	// AJAX error Handling function		 
	var ajaxErrorHandler = function(x, settings, exception){
		var message,
			statusErrorMap = {
				'400' : "Server content invalid",
				'401' : "Unauthorized access",
				'403' : "Resource is forbbiden",
				'404' : "Page not found",
				'500' : "Server Error",
				'503' : "Service unavailable"
			};

		if (x.status) {
			message = statusErrorMap[x.status];
			
			if (!message){
				switch (exception) {
					case 'error':
						message = "Unknown error\n";
						break;
					case 'parseerror':
						message = "Error parsing JSON response";
						break;
					case 'timeout':
						message ="Request time out.";
						break;
					case 'abort':
						message ="Request was aborted by server";
						break;
					default:
						message = "Unknown error<br/>" + settings.url ;;
						break;
				}
			}
		} else {
			// Server is not responding
			message ="Connection error. <br/>Try again later ";
		}
		
		console.log("message", message);
		
		showErrorPopUp(message);
	};
	
	var scrollHandler = function (event) {
		var st = $(this).scrollTop();
		
		if (requesting) return false;
		
		// When the scroll is addressing to bottom it takes place the 
		// call to the next page
		if (st > lastScrollTop) { //checking downscroll
			// When reached bottom, call next page
			if($(window).scrollTop() + $(window).height() > $(document).height() - 100) {
				requesting = true;
				var currentPage = getCurrentPage();
				var lastAlertId = getLastAlertId();
				
				if (currentPage >= 0) {
					var filters = getFilters(), params = { 'page': currentPage + 1, 'lastAlert': lastAlertId };
					
					// Looping filters
					if (Object.size(filters) > 0) { Object.push(params, filters); }
					
					before();
					
					// Preparing call
					$.get(document.URL, params, 'html')
						.done(done)
						.fail(ajaxErrorHandler)
						.always(always);
					/*var r = new RemoteCaller(document.URL, {
						params: params, 
						responseType: 'html',
						done: done, 
						fail: fail,
						before: before,
						always: always
					});*/
					// Calling to the service 
					//r.get();
				}	
			}
		}
		
		lastScrollTop = st;
		
		return false;
	};
	
	$('#history-menu-filter-panel').on('panel:open', function (event) {
		$('.history-list').off('scroll', scrollHandler);
	});
	
	$('#history-menu-filter-panel').on('panel:close', function (event) {
		$('.history-list').scroll(scrollHandler);
	});
	
	$('.history-list').scroll(scrollHandler);
	
	$(this).one('pagebeforehide', function (event, data) {
		 $('.history-list').off('scroll', scrollHandler); 
		 //$('.ui-loader').loader('hide');
		 $.mobile.loading('hide');
	});
});

//$(function ($) {
//	$(document).on('click', '.btn-take-action', function(){
//		var $link = $($(this).next());
//
//		console.log("$link.data('title')", $link.data('title'));
//
//		$('.events-log #takeActionPopup header').text($link.data('title'));
//		$link.trigger('click');
//		
//		return false;
//	});
//});


//TAKE ACTION EVENTS LOG
$(document).on('pagecreate', '.events-log', function (event, data) {
	var $page = $(this),
		$popup = $page.find('#takeActionPopup'); //.popup('option', 'transition', 'fade'); //commented because a bug in jquery mobile when change transition on initialized popup
	
	function takeAction(el) {
		var $link = $(el);
		var href = $link.attr('href');
		
		if (typeof href == 'undefined') {
			href = $link.data('href');
		}
		
		//console.log('href', href);
		
		$.get(href, function (data, textStatus, jQXhr) {
			$popup.html(data).trigger('popupcreate');
			if ( !$popup.parent().is('.ui-popup-active') )
				$.app.popupManager.pushPopup("very_low", $popup);
		});
		
		return false;
	}
	
	$page.find('.btn-take-action').on('click', function (event) {
		return takeAction(this);
	});
	
	$('#takeActionPopup').on('click', '.btn-take-action', function(){
		return takeAction(this);
	});
	
});
