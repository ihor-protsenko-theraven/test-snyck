import DOMPurify from 'dompurify';
var query = DOMPurify.sanitize(decodeURIComponent(window.location.search.match(/inputSearch=(.*?)(&|$)/)[1]).replace('+', ' '));

var calculateEventLogHeight = function() {
    var $main_content    = $('.main_content');
    var $history_header  = $main_content.children('header');
    var $event_nav       = $main_content.find('.history-info .main_nav');
    var $event_container = $main_content.find('.history-info .container');
    var fHeight          = $main_content.innerHeight() - $event_nav.outerHeight() - $history_header.outerHeight();
    var marginHeight     = $event_container.outerHeight(true) - $event_container.outerHeight(false);    
    var fHeight          = $main_content.innerHeight() - $event_nav.outerHeight(true) - $history_header.outerHeight() - marginHeight;
    var paddingTop       = parseInt($event_container.css('padding-top'));
    
    $event_container.css('max-height', fHeight - paddingTop);
    $event_container.find('.content').css('max-height', fHeight - paddingTop - paddingTop);
    $event_container.find('.content').niceScroll({ mousescrollstep: 10, scrollspeed: 200 });
    
    return fHeight;
};
    
$(function() {
    var requesting    = false;
    var self          = this;
    var lastScrollTop = 0;
        
    var toggleEventLoader = function() {
        if ( $('.history-info .loader').is(":visible") ) {
            $('.history-info .loader').fadeOut(300);
        } else {
            $('.history-info .loader').fadeIn(300);
        }
    };

    var getCurrentPage = function() {
        var currentPage = $('.history-list .list').data('current-page');
        
        if (typeof currentPage == 'undefined'){ 
            //console.log('currentPage undefined');
            currentPage = 0; 
            setCurrentPage(0);
        } else {
            currentPage = parseInt(currentPage);
        }
        
      
        
        return currentPage;
    };

    var setCurrentPage = function(page) {
        $('.history-list .list').data('current-page', page);
    };
    
    var getLastAlertId = function() {
        var lastAlertId = $('.history-list .list').data('last-alert');
        
        if (typeof lastAlertId == 'undefined'){
        	lastAlertId = calculateLastAlertId();
        } 
        lastAlertId = parseInt(lastAlertId);
        
        return lastAlertId;
    };

    var setLastAlertId = function(alertId) {
        $('.history-list .list').data('last-alert', alertId);
    };
    
    var calculateLastAlertId = function() {
    	lastAlertId = $('.history-list .list tr').not('#history-list-spinner').last().attr('id');
    	setLastAlertId(lastAlertId);
    	
    	return lastAlertId
    }

    var getResidentId = function() {
        return $('.history-list').data('resident-id');
    };
    
    /**
     * Show the on progress icon
     */
    var before = function(data) {
        //var $list = $('.history-list .list table tbody');
        //$list.append('<tr id="history-list-spinner"><td colspan="3" style="text-align:center;background-color:#f0f0f0;">\
    	//  <img class="loader_icon" style="width:3em;height:auto;" src="${RootPath}/images/loader.png" alt="loader"/>
    };

    var always = function() {
        //$('.history-list .list table tbody #history-list-spinner').detach();
        requesting = false;
    };
        
    var fail = function(data) {
//        alert('mal');
    };

    var done = function(data) {
        var $spinner = DOMPurify.sanitize($('.history-list .list table tbody #history-list-spinner'));
        
        $spinner.before(data);
        $('.history-list .list').getNiceScroll().resize();
        
        if ($(data).filter('tr').size() < 20) {
            $spinner.hide();
            setCurrentPage(-1);
            setLastAlertId(0);
        } else {
            setCurrentPage(getCurrentPage() + 1);
            calculateLastAlertId();
            
        }
    };

    var getFilters = function() {
        var filters = {};
        $('.filter-status:checked').each(function(i, v) {
                filters['state[]'] = $(v).val();
            }
        );
        
        $('.filter-types:checked').each(function(i, v) {
                filters['type[]'] = $(v).val();
            }
        );
        
        return filters;
    };

    var scrollHandler = function (event) {
        if (requesting) return false;
        
        var st           = $(this).scrollTop();
        var $list        = $('.history-list .list');
        var scrollHeight = $list.find('table').height();
        
        // When the scroll is addressing to bottom it takes place the 
        // call to the next page
        if (st > lastScrollTop) { //checking downscroll
            // When reached bottom, call next page
            //$('.history-list .list').scrollTop() >= ($('.history-list .list table').height() - ($('.history-list .list').height() + 50))
            if( $list.scrollTop() >= ( scrollHeight - ($list.height() +20 ) ) ) {
                requesting = true;
                var currentPage = getCurrentPage();
                var lastAlertId = getLastAlertId();
                
                if (currentPage >= 0) {
                    var filters = getFilters();
                    var params = {};
                    
                    currentPage++;
                    
                    // Looping filters
                    if (Object.size(filters) > 0) { Object.push(params, filters); }
                    
                    var url = '/ADL/admin/history/' + getResidentId() + '/' + currentPage + '?lastAlert=' + lastAlertId;
                    //console.log('url', url);
                    // Preparing call
                    $.get(url, params, done, 'html')
                        .fail(fail)
                        .always(always);
                }	
            }
        }
        
        lastScrollTop = st;
        
        return false;
    };
    // Handler for .ready() called.
    $('.history-list .list').on('click', 'tr', function(){
        toggleEventLoader();
        
        var $this = $(this);
        

        var selectedAlertSesionId = $this.attr("data-session-id");
        
        $('.history-list .list tbody tr.selected').removeClass('selected');
        
        var $selectedItemDiv = $('.selected-item');
        var $selectedItem = $($this.clone().removeClass('linkable'));
        $selectedItem.find('.icon-notification').addClass('history rounded-borders');
        $selectedItem.find("#icon-arrow-right").attr("style", "visibility: visible");
       

        $('.selected-item .selected-event tbody').html($selectedItem);
        $selectedItemDiv.effect("highlight", {}, 500);
        $this.addClass('selected');
       
        $('tr.linkable').find('td').each(function( index ) {
        	  $( this ).removeClass('event_row');
        	  let dsid = $( this ).parent().attr("data-session-id");
        	  if (selectedAlertSesionId === dsid) {
        		  $( this ).addClass('event_row');
        	  }
        });
        
       
        var host = $('.history-list').data('host');
        var url  = host + '/admin/event-info/' + $this.attr('id');
        // if the previously selected alert was new it will change its status to viewed
        if ($selectedItemDiv.data('status-alert-select').toLowerCase() == 'new' && $selectedItemDiv.data('id-alert-select')) {
        	url += '?viewed=' + $selectedItemDiv.data('id-alert-select');
        	$selectedItemDiv.removeData('id-alert-select');
        }
        var idSelectedItem = $this.attr('id');
        
    
        $.get(url, function(data){
            $('#tab-event-detail').trigger('click');
            $('#load-content').html(data).text();
            DOMPurify.sanitize($('#tab-event-log').attr('href', $('#tab-event-log').attr('href').replace(/\d+$/, $this.attr('id'))));
            
            if ($('#tab-take-action').attr('href')!=null){
           	    $('#tab-take-action').attr('href', $('#tab-take-action').attr('href').replace(/\=.+/, "=" + $('#event-detail').data('session-state')+"&alertId="+$this.attr('id')+"&sessionId="+$this.data('session-id')));
            }
            
           	if ($('#status_detail_alert').data('status-alert')==null){
//       		$selectedItem.find('.status').text('VIEWED');	
           	}
           	else{ 
	           		//console.log('holaaa:',$('#status_detail_alert .text').text().toLowerCase());
	           		if ($('#status_detail_alert').data('status-alert').toLowerCase()=='in_progress'){
	           			$selectedItem.find('.status').text($('#status_detail_alert .text').text().toLowerCase());
	           			$('.selected').find('.status').text($('#status_detail_alert .text').text().toLowerCase());
	           		}else{
	           			//console.log('adiiooos:',$('#event-detail').data('stauts-current'));
	        			$selectedItem.find('.status').text($('#status_detail_alert .text').text().toLowerCase());
	        			$('.selected').find('.status').text($('#status_detail_alert .text').text().toLowerCase());       			
	        			}
	           		$selectedItem.find('.status').addClass($('#status_detail_alert').data('status-alert').toLowerCase());
	           		$('.selected').find('.status').addClass($('#status_detail_alert').data('status-alert').toLowerCase());
	           		$("[data-session-id='" + $this.data('session-id') + "']").find('.grid_3').removeClass('event_row');
	           		$("[data-session-id='" + $this.data('session-id') + "']").find('.grid_10').removeClass('event_row');
	           		$("[data-session-id='" + $this.data('session-id') + "']").find('.grid_3').addClass('event_row_selected');
	           		$("[data-session-id='" + $this.data('session-id') + "']").find('.grid_10').addClass('event_row_selected');
	           	}
           	
           if ($('#status_detail_alert').data('status-alert') && $('#status_detail_alert').data('status-alert').toLowerCase()=='closed'){
        	   
        		$('#tab-take-action').attr('disabled', true);
        		$('#tab-take-action').click(function () {return false;});
        		
//        		$('#tab-take-photo').attr('disabled', true);
//        		$('#tab-take-photo').click(function () {return false;});
           }else{        	
        		$('#tab-take-action').unbind('click');
        		$('#tab-take-action').attr('disabled', false);
        		
//        		$('#tab-take-photo').unbind('click');
//        		$('#tab-take-photo').attr('disabled', false);
           }
           
           	
            calculateEventLogHeight();
        }, 'html')
        .fail(function(data){
            alert("Communication error");
        })
        .always(toggleEventLoader);
        
        //if ($('#closeAlarmPopup').length > 0) {
            var closeEvUrl = $('#closeAlarmPopup form').attr('action').replace(/\d+$/, $this.attr('id'));
            $('#closeAlarmPopup form').attr('action', closeEvUrl);
            $('#closeAlarmPopup form #sessionId').val($this.data('session-id'));
        //}
        
        return false;
    });
    
    $('.history-info .main_nav').on('click', '.tab', function(){
    	var $this = $(this);
    	
        $this.parent().siblings().find('.tab').removeClass('active');
        $this.addClass('active');
       
        if (/^#/.test($this.attr('href'))) {
        	var tabId       = $(this).attr('id');
        	var contentId = tabId.slice( tabId.indexOf('-') + 1, tabId.length );
       
        	//console.log('contentId', contentId);
        
        	$('#' + contentId).siblings().removeClass('active');
        	$('#' + contentId).siblings().hide();
        	$('#' + contentId).show();
        	$('#' + contentId).addClass('active');
        } else {
        	$.app.loader.show();
        	$.get($this.attr('href'), function (data, textStatus, jQXhr) {
        		$.app.loader.hide();
        		$('#load-content').find('> .content').hide().removeClass('active');
        		$(data).appendTo('#load-content').trigger('tab:content_create');
        		calculateEventLogHeight();
        	});
        }
        
        return false;
    });
    
    $(document).on('click', '#tab-take-action-sim', function (event) {
    	$('#tab-take-action').click();
    	return false;
    });
    
    $('.history-list .list').scroll(scrollHandler);
    
    //$('.history-list .list tr:first-child').addClass('selected');	// not needed because the class is added in the JSP
    
    // photo detail
    (function ($) {
    	function autoSwipe(_index, $carouselImages, $carouselNav) {
    		var leftLimit  = 0,
    			rightLimit = $carouselImages.find('li').length - 1,
    			index      = (_index < leftLimit) ? leftLimit : (_index > rightLimit) ? rightLimit : _index,
    			position   = -$carouselImages.find('li:eq(' + index + ')').position().left;
    		
    		$carouselImages.animate({ left: position }, 200);
    		
    		if (index == leftLimit) {
    			$carouselImages.parent().find('.carousel-prev').removeClass('active');
    		} else if (index == rightLimit) {
    			$carouselImages.parent().find('.carousel-next').removeClass('active');
    		} else {
    			$carouselImages.parent().find('.carousel-prev').addClass('active');
    			$carouselImages.parent().find('.carousel-next').addClass('active');
    		}
    		
    		$carouselNav.find('.active').removeClass('active');
    		$carouselNav.find('a:eq(' + index + ')').addClass('active');
    	};
    	
    	$('.history-info').on('click', '.event-photo .pagination a', function (event) {
    		var $carouselNav = $(this).parents('.pagination'),
    			$carouselImages = $carouselNav.parents('.history-info').find('#carousel-images');
    		
    		autoSwipe($carouselNav.find('a').index(this), $carouselImages, $carouselNav);
    		
        	return false;
        });
    	
    	$('.history-info').on('click', '.event-photo .carousel-next', function (event) {
    		var $carouselNav    = $(this).parents('.history-info').find('#carousel-nav'),
				$carouselImages = $(this).parents('.history-info').find('#carousel-images'),
				currentIndex    = $carouselNav.find('a').index($carouselNav.find('.active').get(0));
    		
    		autoSwipe(currentIndex + 1, $carouselImages, $carouselNav);
    		
    		return false;
    	});
    	
    	$('.history-info').on('click', '.event-photo .carousel-prev', function (event) {
    		var $carouselNav    = $(this).parents('.history-info').find('#carousel-nav'),
				$carouselImages = $(this).parents('.history-info').find('#carousel-images'),
				currentIndex    = $carouselNav.find('a').index($carouselNav.find('.active').get(0));
    		
    		autoSwipe(currentIndex - 1, $carouselImages, $carouselNav);
    		
    		return false;
    	});
    })($);
    
    $(document).on('tab:content_create', '.take-action', function (event) {
    	var $tab = $(this);
    	
    	$tab.find('.tab_action').on('click', function (event) {
    		$.app.loader.show();
    		$.get($(this).attr('href'), function (data, textStatus, jQXhr) {
    			$.app.loader.hide();
    			$('#load-content').find('> .content').hide().removeClass('active');
        		$(data).appendTo('#load-content').trigger('tab:content_create');
    		});
    		return false;
    	});
    });
    
    // Event log take action modal
    $(document).on('modal:update', '.event-log-take-action', function (event, data) {
    	var $modal = $(this);
    	
    	$modal.find('.call-button').qtip({
    		position: { my: 'bottom center', at: 'top center' }
    	});
    	
    	$modal.find('.disabled').on('click', function (event){
    		event.preventDefault();
    		return false;
    	});
    });
    
    // 
    $(document).on('ajax:success', '#broadcast-form', function (event, data) {
    	if ($('#tab-event-log').is('.active')) {
    		$('#tab-event-log').click();
    	}
    });
    
    // CLOSE EVENT POP UP
    $(document).on('modal:hide', '#closeAlarmPopup', function (event) {
    	var $closeEventPop = $(this),
        	$radios        = $closeEventPop.find(':radio'),
        	$text          = $closeEventPop.find(':text'),
        	$form          = $closeEventPop.find('form');
    	
    	$radios.attr('checked', false).qtip('destroy').parent().removeClass('checked error');
    	$text.qtip('destroy').val("");
    	$form.data('validator').resetForm();
    });
    /*$(document).one('modal:show', '#closeAlarmPopup', function (event, data) {
        var $closeEventPop = $(this),
            $radios        = $closeEventPop.find(':radio'),
            $text          = $closeEventPop.find(':text'),
            $form          = $closeEventPop.find('form');
        
        //$closeEventPop.unbind(event);
//        $text.on('keyup change paste', function (event) {
//            $radios.removeAttr('checked').parent().removeClass('checked');
//        });
//        $radios.on('change', function (event) {
//            if ($(this).is(':checked')) {
//                $text.val('');
//            }
//        });
        
        $form.on('submit', function (event) {
            var valid = false;
            
            $radios.each(function () {
                if ($(this).is(':checked')) {
                    valid = true;
                }
            });
            if ($text.val() === '') {
                valid = false;
            }
            
            if (!valid) return false;
        });
    });*/
});

// set main content height
$(function ($){
    var $main_content, $main_footer, $list_header, $list;

    $main_content    = $('.main_content');
    $history_header  = $main_content.children('header');
    $history_list    = $main_content.children('.history-list');
    $list_header     = $main_content.find('.history-list .selected-item');
    $list            = $main_content.find('.history-list .list');
    $event_container = $main_content.find('.history-info');
    
    //console.log('history');
    if ($('#alert-select').data('status-alert-select')!=null && $('#alert-select').data('status-alert-select').toLowerCase()=='closed'){
		$('#tab-take-action').attr('disabled', true);
		$('#tab-take-action').click(function () {return false;});
		
//		$('#tab-take-photo').attr('disabled', true);
//		$('#tab-take-photo').click(function () {return false;});
   }else{        	
		$('#tab-take-action').unbind('click');
		$('#tab-take-action').attr('disabled', false);
		
//		$('#tab-take-photo').unbind('click');
//		$('#tab-take-photo').attr('disabled', false);
   }
    
    
    function calculateHistoryListHeight () {
        var fHeightList     = $main_content.innerHeight() - $history_header.outerHeight() - $list_header.outerHeight();
        var fHeightContents = $main_content.innerHeight() - $history_header.outerHeight();
        
        $list.css('height', fHeightList);
        $history_list.css('height', fHeightContents);
        $event_container.css('height', fHeightContents);
        
        return fHeightContents;
    }

    $(window).on('resize', function (event) {
        calculateHistoryListHeight();
        calculateEventLogHeight();
    });

    calculateHistoryListHeight();
    calculateEventLogHeight();
    
    $list.niceScroll({ mousescrollstep: 10, scrollspeed: 200 });
});

// New Alert
$(function ($){
    $(document).on('modal:update', '.new-alert', function (event) {    	
    	/*$('form#new-alert').on('ajax:success', function (event, data, textStatus, jQXhr) {
			console.log("event", event);
			console.log("data", data);
			console.log("textStatus", textStatus);
		});
		
		$('form#new-alert').on('ajax:error', function (event, data, textStatus, jQXhr) {
			console.log("event", event);
			console.log("data", data);
			console.log("textStatus", textStatus);
		});*/
        $(document).on('change', '#issue-type', function(){
        	var item = $(this).data('devices');
            if ($(this).val() == item) {
                // SHOW device select
                $('#device-number').parent().parent().show();              
            } else {
                // HIDE device select
                $('#device-number').parent().parent().hide();
            }
        });
        
        $('#device-number').parent().parent().hide();
    });
    
});

// Close alert popup
$(function ($){
	$(document).on('modal:show', '#closeAlarmPopup', function (event) {
		var $reasonSelect = $(this).find('select[name=smsgReason]');
		var $detailsInput = $(this).find('input[name=smsgDetails]');
		
		$reasonSelect[0].selectedIndex = 0;
		$reasonSelect.uniform().change();
		
		$reasonSelect.change(function() {
			var detailsRequired = $reasonSelect.find('option[value=' + $reasonSelect.val() + ']').attr('data-details-required')
			if (detailsRequired === 'true') {
				$detailsInput.attr('required', true);
			} else {
				$detailsInput.removeAttr('required').removeClass('error');
			}
		});
	});
});

// Init google maps
$(function ($){
	$(document).on('click', '#showLocationButton', function (event) {
		
		// TODO save number of requests to Google Maps API by
		// - storing on variable the info that has been retrieved
		// - if the alert is the same as the one on previous request, not make again the request
		// (the #map div and #alertGeolocationInfo are loaded with the same info from the previous request)
		
		var latitude = $('#showLocationButton').data('latitude');
		var longitude = $('#showLocationButton').data('longitude');
		var horizontalAccuracy = $('#showLocationButton').data('horizontal-accuracy');
		
		initMap(latitude, longitude, horizontalAccuracy);
		
		$('#alertGeolocationInfo .cardInfoPrincipal').empty();
		$('#alertGeolocationInfo .cardInfoSecondary').empty();
		
		getInverseGeocodingInfo(latitude, longitude, function(inverseGeocoding){
			$('#alertGeolocationInfo .cardInfoPrincipal').append( inverseGeocoding);
			$('#alertGeolocationInfo .cardInfoSecondary').append( latitude + ", " + longitude);
		});
		
		
		
	});
});
