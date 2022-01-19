$.app = $.app || {};
// Avoid `console` errors in browsers that lack a console.
(function() {
  var method;
  var noop = function () {};
  var methods = [
    'assert', 'clear', 'count', 'debug', 'dir', 'dirxml', 'error',
    'exception', 'group', 'groupCollapsed', 'groupEnd', 'info', 'log',
    'markTimeline', 'profile', 'profileEnd', 'table', 'time', 'timeEnd',
    'timeStamp', 'trace', 'warn'
  ];
  var length = methods.length;
  var console = (window.console = window.console || {});

  while (length--) {
    method = methods[length];

    // Only stub undefined methods.
    if (!console[method]) {
      console[method] = noop;
    }
  }
}());

(function ($) {
	$.app.isRTL = $('html').is('[dir=rtl]'); 
})($);


// [data]

$(document).on('modal:show', '.modal', function () { 
	
	var formElements = this.getElementsByClassName('data-single-submit-form');
	
	for(var i = 0; i < formElements.length; i++){
		
		$(formElements[i]).on('submit', function () {
			if($(this).validate().errorList.length == 0){
				if($(this).find('[type=submit]')){
					$(this).find('[type=submit]').attr('disabled', 'disabled');
				}
			}
		})
	}
	
});



// Modals
$(function ($){
  var $modals, $links_modal;

  $modals = $('.modal');
  $links_modal = $('[data-rel=modal]');

  function $getNewModalTPL(_html, _href, _active) {
	  var active = (_active) ? 'active' : '',
		  html   = '<div class="modal ' + active + '" id="' + _href.replace(/\//gi, '') + '">'
		  	 		+ '<div class="modal_content">'
		  	 			+ _html
		  	 		+ '</div>'
		  	 	+ '</div>';
	  
	  return $(html);
  }
  
  $(document).on('click', '[data-rel=modal]', function (event) {
	  event.preventDefault();
	  
	  var $link, href, $modal;

	  $link = $(this);
	  href  = $link.attr('href');
	  
	  if ( /^#|\./.test(href) ) {
		  $modal = $modals.filter(href);
		  if ($modal.length > 0) { 
			  $modal.addClass('active').trigger('modal:show');
		  }
		  
		  /*$modal.find('.modal_content').css({
			  'margin-left': -$modal.find('.modal_content').width() / 2,
			  'margin-top': -$modal.find('.modal_content').height() / 2 
		  });*/
	  } else {
		  var method = $link.data('method') || 'get',
		  	  params = {};
		  
		  $link.find(':hidden').each(function (i, el) {
			 params[$(el).attr('name')] = $(el).val(); 
		  });
		  
		  $.ajax({
				url: href, 
				beforeSend: function(){ $.app.loader.show(); },
				data: params,
				type: method.toUpperCase()
		  })
		  .done(function(data, textStatus, jQXhr){ 
		  	$modal = DOMPurify.sanitize($('#' + href.replace(/\//gi, '').split('?')[0]));
			  if ($modal.length > 0) {
				  $modal.find('.modal_content').html(data);
				  $modal.trigger('modal:update').addClass('active').trigger('modal:show');
			  } else {
				  $modal = $getNewModalTPL(data, href, true);
				  if ($link.data('modal-cssclass')) {
					  $modal.addClass($link.data('modal-cssclass'));
				  }
				  $modal.appendTo('body').trigger('modal:create').trigger('modal:update').trigger('modal:show');
			  }  
			  
			  /*$modal.find('.modal_content').css({
				  'margin-left': -$modal.find('.modal_content').width() / 2,
				  'margin-top': -$modal.find('.modal_content').height() / 2 
			  });*/
		   })
		  .always(function(data){ $.app.loader.hide(); });
	  }
	  
	  return false;
  });
  
  $(document).on('modal:show', '.modal', function (event) { $('body').addClass('modal-opened'); });
  $(document).on('modal:hide', '.modal', function (event) { $('body').removeClass('modal-opened'); });
  /*$(document).on('modal:show', '.modal', function (event) {
	  var $modal = $(this);
	  $modal.find('.modal_content').css({
		  'margin-left': -$modal.find('.modal_content').width() / 2,
		  'margin-top': -$modal.find('.modal_content').height() / 2 
	  });
  });*/
  
  $(document).on('click', '.modal [data-rel=close]', function (event) {
	  $(this).parents('.modal').removeClass('active').trigger('modal:hide');
	  return false;
  });
  
  $(document).on('click', '.modal', function (event) {
	 if ($(event.target).is('.modal:not(.keep-active)')) {
		 $(this).removeClass('active').trigger('modal:hide');
	 } 
  });
  
  $.app.modals = {
		  $tpl: $getNewModalTPL
  };
});

//SPINNER ALL PAGES
$(document).on('click', '.tab, .linkable, .nav_spinner, .notifications.active', function (event) {
	$('.all').show();
});

// set main content height
$(function ($){
  var $window, $main_header,
      $main_nav, $filter_nav,
      $main_content, $main_footer,
      $main_scroll;

  $window       = $(window);
  $main_header  = $('.main_header');
  $main_nav     = $('.main_nav');
  $filter_nav   = $('.filter_nav');
  $main_content = $('.main_content');
  $main_footer  = $('.main_footer');
  $main_scroll  = $('#main_scroll');

  if ( !$main_scroll.length )  {
	  $main_scroll = $main_content;
  } else{
	  var prevHeight = 0;
	  $main_scroll.prevAll(':not(:hidden)').each(function (i, el) {
		  prevHeight += $(el).outerHeight();
	  });
	  
	  $main_scroll.css('top', prevHeight - 1 + 'px');
  }
  
  // @deprecated remove if pass all test in browsers, now the height is set by css
  /*function calculateContentHeight () {
	var numFilters= $(".filter_nav").length;
    var fHeight = $window.innerHeight() - $main_header.outerHeight() - $main_nav.outerHeight() - ($filter_nav.outerHeight()*numFilters) - $main_footer.outerHeight();
    $main_content.css('height', fHeight);

    return fHeight;
  }*/

  /*$window.on('resize', function (event) {
    calculateContentHeight();
  });

  calculateContentHeight();*/
  
  $main_scroll.on('scroll', function () {
	 $(this).find('input:focus').blur();
  });
  
  $.app.scroll = $main_scroll.niceScroll({ mousescrollstep: 5, scrollspeed: 100 });
});


// datepickers
/**
 *
 * datas
 *  - data-maxdate
 *  - data-navigation-days
 */
$(function ($) {
	
	function initDatePickers($container) {
		var $datepickers = $container.find('[data-attribute="datepicker"]');
		
		$datepickers.attr('type', 'text').each(function () {
			var $datepicker = $(this),
				options = {};
  
			if ($datepicker.data('maxdate') !== null) {
				options.maxDate = $datepicker.data('maxdate');
				/*try {
					var date = new Date();
					date.setDate(date.getDate() + parseInt(options.maxDate));
					options.maxDate = date;
					console.log('max date', options.maxDate);
				} catch(e) {
					console.error(e);
				}*/
			}
			
			if ($datepicker.data('yearrange') !== null) {
				options.yearRange = $datepicker.data('yearrange');
			}
			
			if ($datepicker.data('changemonth') !== null) {
				options.changeMonth = $datepicker.data('changemonth');
			}
			
			if ($datepicker.data('changeyear') !== null) {
				options.changeYear = $datepicker.data('changeyear');
			}
			
			if ($datepicker.data('dateformat') !== null) {
				options.dateFormat = $datepicker.data('dateformat');
			}

			$datepicker.datepicker(options);

			// Navigation days
			if (typeof $datepicker.data('navigation-days') != 'undefined') {
				$datepicker.parent().addClass('arrows');
				$datepicker.before("<a href='#' class='prev-day'></a>");
				$datepicker.after("<a href='#' class='next-day'></a>");

				var currentDate = new Date();
				currentDate.setHours(0,0,0,0);

				if (currentDate.getTime() <= $datepicker.datepicker('getDate').getTime()) {
					$datepicker.next('.next-day').hide();
				} else {
					$datepicker.next('.next-day').show();
				}
			}
			
			var val =  $('#report_date').val();
			if (val) {
				var dateFormatter = $.app.dateFormatter;
				dateFormatter = dateFormatter.replace("yyyy","yy").toLowerCase();
				
				$('.hasDatepicker').datepicker( "option", "dateFormat", dateFormatter);
				$( ".hasDatepicker" ).datepicker("setDate", $.datepicker.parseDate(dateFormatter, val ));
			}
		});

	}
  
	$(document).on('modal:update', '.modal', function (event) {
	  initDatePickers($(this));
	});
  
  initDatePickers($(this));
});

// uniform forms
$(function ($) {
	var $checkboxes = $(':checkbox:not(.no-uniform)'),
		//$radios     = $(':radio'),
  		$selectors  = $('select:not(.no-uniform,.combobox,.readonly)'),
  		$readonlySelects = $('select.readonly:not(.no-uniform,.combobox)'),
  		$durationInputs   = $('input.duration');
  
  function initDurationInput() {
	  var $input = $(this);
	  var unit = $input.data('unit');
	  
	  if( unit ) {
		  return $input.durationInput({ unit: unit });
	  }
	  
	  return $input.durationInput();
  };
	
  $(document).on('change', 'select', function (event) {
	  var $select = $(this);

	  switch($select.val()) {
	  case '':
	  //case '0':
		  $select.parent().addClass('empty');
		  break;
	  default:
		  $select.parent().removeClass('empty');
	  }
  });
  
  $(document).on('modal:update', '.modal', function (event, settings) {
	  var $selects = $(this).find('select:not(.no-uniform,.combobox,.readonly)'),
	  	  $readonlySelects = $(this).find('select.readonly:not(.no-uniform,.combobox)'),
	  	  $durationInputs = $(this).find('input.duration');
	  	  
	  $(this).find(':checkbox').uniform();
	  //$(this).find(':radio').uniform();
	  $selects.uniform();
	  $durationInputs.each(initDurationInput);
	  $readonlySelects.uniform({ disabledClass: 'readonly' });
	  
	  if ( !settings || !settings.preventChange ) {
		  $selects.change();
		  $readonlySelects.change();
	  }
  });
  
  $checkboxes.uniform();
  //$radios.uniform();
  $selectors.uniform().change();
  $durationInputs.each(initDurationInput);
  $readonlySelects.uniform({ disabledClass: 'readonly' }).change();
});

// Combobox
$(function ($) {
	var $combobox = $('.combobox');
	
	$(document).on('modal:update', '.modal', function (event) {
		$(this).find('.combobox').combobox({ appendTo: $(this) });
	});
	
	$combobox.combobox({ appendTo: $.app.scroll.me });
});

// Check filter submit
$(function ($) {
	$(document).on('change', '.check_filter_submit', function (event) {
		$(this.form).submit();
	});
});

// filters
$(function ($) {
  var $filters = $('#filters');

  function show ($target) {
    var $btn, $wrap, fHeight, needScroll;

    $btn  = $target.find('.btn').first();
    $wrap = $target.find('.wrap');
    needScroll = false;

    $target.removeClass('hidden');

    $wrap.css('height', 'auto');
    fHeight = $wrap.height();

    if (fHeight + $wrap.offset().top > $(window).height() - 15) {
      fHeight = $(window).height() - 15 - $wrap.offset().top;
      needScroll = true;
    }

    if (Modernizr.csstransitions) {
      $wrap.css('height', 0);

      setTimeout(function () {
        $btn.css('width', '100%');

        setTimeout(function () {
          $wrap.css('height', fHeight);

          setTimeout(function () {
            if ($wrap.getNiceScroll().length > 0 && needScroll) {
              $wrap.getNiceScroll().resize().show();
            } else if(needScroll) {
              $wrap.niceScroll({ mousescrollstep: 5, scrollspeed: 100 });
            }
          }, 500);
        }, 250);
      }, 5);
    } else {
      $btn.css('width', '100%');
      $wrap.css('height', fHeight);
    }
    
    $filters.css('height', $(window).innerHeight() - $('.main_header').outerHeight() - $('.main_nav').outerHeight() - $filters.position().top);
    
    updateFilterCheckboxes();
    $filters.find(':checkbox').each(function() {
    	if ($(this).prop('checked'))
    		$(this).attr('data-original-checked', 'checked');
    });
  }

  function hide ($target) {
    var $btn, $wrap;

    $btn  = $target.find('.btn').first();
    $wrap = $target.find('.wrap');

    $target.removeClass('hidden');
    $wrap.css('height', 0);

    if ($wrap.getNiceScroll().length > 0) {
      $wrap.getNiceScroll().hide();
    }

    if (Modernizr.csstransitions) {
      setTimeout(function () {
        $btn.css('width', '0');
        setTimeout(function () {
          $target.addClass('hidden');
        }, 250);
      }, 500);
    } else {
      $btn.css('width', '0');
      $target.addClass('hidden');
    }
  }

  $('[data-rel=show_filters]').on('click', function (event) {
    var $this, href, $target;

    $this = $(this);
    href  = $this.attr('href');
    $target = $(href);

    if ($target.length > 0) {
      show($target);
    }

    return false;
  });
  
  // behaviour of checkboxes with id ended in _all
  $(document).on('change', ':checkbox[id$=_all]', function (event) {
    var $this, id, checked;
    $this = $(this);
    id    = $this.attr('id').replace(/_all$/, '');
    checked = $this.is(':checked');

    $(':checkbox[id^=' + id + ']').not($this).prop('checked', checked).uniform('update');
  });
  $(document).on('change',':checkbox', function (event) {
	var $this		= $(this),
		id			= $this.attr('id').substr(0, $this.attr('id').indexOf('_')),
		$container	= $this.parents('.modal');
	if (!$container.length) $container = $(document);
	var	$all   		= $container.find(':checkbox[id=' + id + '_all]');
	// if there is a checkbox with the same beginning and ended in _all it is updated based on all related checkboxes
	if ($all.length) {
		updateOneAllCheckBox($all, $container);
	}
  });
  function updateAllCheckBox($container) {
	  var $container = $container || $(document);
	  
	  $container.find(':checkbox[id$=_all]').each(function () {
		  updateOneAllCheckBox($(this), $container);
	  });
  };
  function updateOneAllCheckBox($checkbox, $container) {
	  var $container = $container || $(document);
	  
	  var id    = $checkbox.attr('id').replace(/_all$/, ''),
  	  	$allChecked = $container.find(':checkbox[id^=' + id + ']:checked').not($checkbox);

	  if ($allChecked.length) {
		  $checkbox.prop('checked', 'checked').uniform('update');
	  } else  {
		  $checkbox.prop('checked', '').uniform('update');
	  }
  };
  updateAllCheckBox();
  $(document).on('modal:update', '.modal', function () { updateAllCheckBox($(this)); });
  
  // user filters
  $filters.on('click', '.btn-close', function (event) {	 
	 $filters.find(':checkbox').each(function () {
		 var $check = $(this),
	 	 	 checked = $check.attr('data-original-checked') || '';
		 $check.prop('checked', checked).uniform('update'); 
	 });
    hide($filters);
    return false;
  });
  // specific behaviour of checkboxes in user filters
  var $panelTypeAllCheck = $('#panel-types_all'),
  	  $serviceTypeAllCheck = $('#service-types_all'),
  	  $userTypesAllCheck = $('#user-types_all'),
  	  $userTypesMonitoredCheck = $('#user-types_ROLE_MONITORED'),
  	  $userTypesChecks = $(':checkbox[id^=user-types]:not([id$=_all])'),
  	  $enableActiveServiceChecks = $(':checkbox[id^=EnableActiveService]');
  
  $filters.on('change', '#panel-types_all', function (event) {
	  var $this = $(this);
	  var checked = $this.is(':checked');
	  
	  if ( checked ) {
		  $userTypesMonitoredCheck.prop('checked', true).uniform('update');
		  $userTypesAllCheck.prop('checked', true).uniform('update');
	  } else {
		  $userTypesMonitoredCheck.prop('checked', false).uniform('update');
		  if ($userTypesChecks.filter(':checked').length === 0) {
			  $userTypesAllCheck.prop('checked', false).uniform('update');
		  }
	  }
	  if ( $('#user-types_ROLE_MONITORED').length ) {
		  $('user-types_ROLE_MONITORED').prop('checked', checked).uniform('update');
	  }
  });
  $filters.on('change', '#service-types_all', function (event) {
	  var $this = $(this);
	  var checked = $this.is(':checked');
	  
	  if ( checked ) {
		  $enableActiveServiceChecks.prop('disabled', false).uniform('update');
		  $userTypesMonitoredCheck.prop('checked', true).uniform('update');
		  $userTypesAllCheck.prop('checked', true).uniform('update');
	  } else {
		  $enableActiveServiceChecks.prop('disabled', true).prop('checked', false).uniform('update');
		  $userTypesMonitoredCheck.prop('checked', false).uniform('update');
		  
		  if ($userTypesChecks.filter(':checked').length === 0) {
			  $userTypesAllCheck.prop('checked', false).uniform('update');
		  }
	  }
	  
	  if ( $('#user-types_ROLE_MONITORED').length ) {
		  $('user-types_ROLE_MONITORED').prop('checked', checked).uniform('update');
	  }
  });
  $filters.on('change', '#user-types_ROLE_MONITORED', function (event) {
    var $this = $(this);
    var checked = $this.is(':checked');
    $(':checkbox[id^=panel-types]').prop('checked', checked).uniform('update');
    $(':checkbox[id^=service-types]').prop('checked', checked).uniform('update');
    $(':checkbox[id^=EnableActiveService]').prop('checked', checked).prop('disabled', !checked).uniform('update');
  });
  $filters.on('change', '#user-types_all', function (event) {
	 var $this = $(this);
	 var checked = $(this).is(':checked');
	 $(':checkbox[id^=panel-types]').prop('checked', checked).uniform('update');
	 $(':checkbox[id^=service-types]').prop('checked', checked).uniform('update');
	 $(':checkbox[id^=EnableActiveService]').prop('checked', checked).prop('disabled', !checked).uniform('update');
  });
  $filters.on('change', ':checkbox[name^=panelTypeFilter]', updatePanelTypeCheckboxes); 
  $filters.on('change', ':checkbox[name^=serviceTypeFilter]', updateServiceTypeCheckboxes); 
  
  function updateFilterCheckboxes() {
	updatePanelTypeCheckboxes();
	updateServiceTypeCheckboxes();
	updateAllCheckBox();
  };
  function updatePanelTypeCheckboxes() {
	var $allChecked = $(':checkbox[name^=panelTypeFilter]:checked');
		
	if ($allChecked.length === 0) {
		$('#user-types_ROLE_MONITORED').prop('checked', '').uniform('update');			
		if ($userTypesChecks.filter(':checked').length === 0) {
			$userTypesAllCheck.prop('checked', false).uniform('update');
		}
	} else  {
		$userTypesAllCheck.prop('checked', true).uniform('update');
		$('#user-types_ROLE_MONITORED').prop('checked', 'checked').uniform('update');
	}
  };
  function updateServiceTypeCheckboxes() {
	var $allChecked = $(':checkbox[name^=serviceTypeFilter]:checked');
	
	if ($allChecked.length === 0) {
		$('#user-types_ROLE_MONITORED').prop('checked', '').uniform('update');
		$('[id^=EnableActiveService]').prop('checked', '').attr('disabled', 'disabled').uniform('update');
		
		if ($userTypesChecks.filter(':checked').length === 0) {
			$userTypesAllCheck.prop('checked', false).uniform('update');
		}
	} else  {
		$userTypesAllCheck.prop('checked', true).uniform('update');
		$('#user-types_ROLE_MONITORED').prop('checked', 'checked').uniform('update');
		$('[id^=EnableActiveService]').removeAttr('disabled').uniform('update');
	}
  };
      
  $filters.on('click', function (event) {
    if (event.target.id == 'filters') {
      hide($filters);
      return false;
    }
  });
});

// filter nav searchs button
$(function ($) {
	var $search = $('.search');
	
	if ($search.length > 0) {
		$('label[for=' + $search.attr('id') + ']').on('click', function (event) {
			$(this).parents('form').submit();
		});
		$search.on('keypress', function (event) {
			if (event.which == 13) {
				$(this.form).submit();
				return false;
			}
		});
	}
});

//Residents List qtips
$(function ($) {
	if ($('.has_tip').length > 0) {
		function initTip () {
			var $this = $(this),
				$target = $this.find('.icon'),
				qTipPosition = ($.app.isRTL) 
					? { my: 'top right', at: 'bottom center', target: $target }
					: { my: 'top left', at: 'bottom  center', target: $target };
			
			$this.qtip({
				content: {
					text: $this.find('.tiptext').html(),
					title: $this.attr('title')
				},
				style: { classes: 'qtip-shadow qtip-white qtip-base-font qtip-padding' },
				position: qTipPosition,
				show: 'click',
				hide: { fixed: true, delay: 250, effect: function () { $(this).fadeOut(250); }  }
			});
		};
		$('.has_tip').each(initTip);
		var $table = $('.has_tip').first().closest('table');
		
		if ($table.length > 0) {
			$table.on('table:added_page', function (event, $items) {
				$items.find('.has_tip').each(initTip);
			});
			
			$table.on('alerts:change_table', function (event) {
				$(this).find('.has_tip').each(initTip);
			});
		}
	}
});

// tr linkable action
$(function ($) {
	$(document).on('click', '.linkable[data-url]', function (event) {
		//console.log(event.target)
		if ($(event.target).is('.actions, .has_tip')) {
			return false;
		}
		
		window.location.href = $(this).data('url');
		return false;
	});
	
	$(document).on('click', '.linkable[data-url] a, .linkable[data-url] .has_tip', function (event) { event.stopPropagation(); });
});

// Table pagination on main_content
$(function($) {
    var $tableList = $('.main_content table[data-pagination]');
    if ($tableList.length > 0) {
        var $scrollEl     = $tableList.parent();
        var requesting    = false;
        var lastScrollTop = 0;
        
        var getCurrentPage = function(element) {
            var currentPage = $(element).data('current-page');
            
            if (typeof currentPage == 'undefined'){ 
                //console.log('currentPage undefined');
                currentPage = 0; 
                setCurrentPage(element, 0);
            } else {
                currentPage = parseInt(currentPage);
            }
            
            return currentPage;
        };

        var setCurrentPage = function(element, page) {
            $(element).data('current-page', page);
        };
        
        var doneScrollPagination = function(data) {
        	var $data = $(data);
			tableList.find( '#pagination-list-spinner').before(DOMPurify.sanitize($data));
            $tableList.triggerHandler('table:added_page', [ $data ]);
           // console.log("data size",$(data).filter('tr').size());
            
            if ($data.filter('tr').size() < 20) {
                //console.log("less than 20");
            	// workaround for IE alignment problems when updating the table
    			$('#pagination-list-spinner').show(function() {
    				$('#pagination-list-spinner').hide(function(){
    				});
    			});
            	
                setCurrentPage($scrollEl, -1);
            } else {
                //console.log("equal 20");
            	// workaround for IE alignment problems when updating the table
    			$('#pagination-list-spinner').hide(function() {
    				$('#pagination-list-spinner').show(function(){
    				});
    			});
                setCurrentPage($scrollEl, getCurrentPage($scrollEl) + 1);
            }
            $.app.scroll.resize();
        };
        
        var failScrollPagination = function(data) {
//            alert('mal');
        };
        
        var alwaysScrollPagination = function(data) {
            requesting = false;
        };
        
        var scrollHandler = function (event) {
            if (requesting) return false;
            
            var st    = $(this).scrollTop();
            var $list = $(this);        

            // When the scroll is addressing to bottom it takes place the 
            // call to the next page
            if (st > lastScrollTop) { //checking downscroll
                var scrollHeight = $list.find('table').height() + parseInt($list.find('table').css('margin-top'));
                
                // When reached bottom, call next page
                if( $list.scrollTop() >= ( scrollHeight - ($list.height() + 10 ) ) ) {
                    requesting = true;
                    var currentPage = getCurrentPage(this);

                    //console.log("current page", currentPage);
                    
                    if (currentPage >= 0) {
                        var url     = $list.find('table').data('pagination');
                        //var filters = getFilters();
                        //var params  = {};
                                            
                        // Looping filters
                        //if (Object.size(filters) > 0) { Object.push(params, filters); }
                        
                        //var url = '/ADL/admin/history/' + getResidentId() + '/' + currentPage++;
                        // Preparing call
                        $.get(url, { page: (++currentPage) }, doneScrollPagination, 'html')
                            .fail(failScrollPagination)
                            .always(alwaysScrollPagination);
                    }
                }
            }
            
            lastScrollTop = st;
            
            return false;
        };
        
        $scrollEl.on('scroll', scrollHandler);
    }
});

// Modal form capture
/**
 * Datas:
 *   data-type="html" 
 *   data-skipsuccessdefault='true'
 *   data-skiperrordefault='true'
 */
$(function ($) {
	var tplSuccess = function (title, message) {
			return '<header class="header_modal"><h1 class="section_title">' + title + '</h1></header>'
					+ '<article><h3>' + message + '</h3></article>'
					+ '<footer class="footer_modal"><button class="btn grey" data-rel="close"><strong>' + I18n.t('buttons.ok') + '</strong></button></footer>'; 
		},
		handleRemote = function ($form) {
			var dataType    = $form.data('type') || 'html';

			$form.trigger('ajax:before');

			// FUNCIONES PARA ANTES DE MANDARLO
			// Mostrar spinner
			$.app.loader.show();

			$.ajax($form.attr('action'), {
				data: $form.serializeArray(),
				type: $form.attr('method'),
				dataType: dataType,
				success: function (data, textStatus, jQXhr) {
					$form.trigger('ajax:success', arguments);
        
					if ($form.data('skipsuccessdefault')) return false;
					// FUNCIONES PARA TODOS CUANDO ES SUCCESS
					if ($('.modal.active').length > 0) $('.modal.active').removeClass('active');
				
					//console.log('AJAX REQUES SUCCESS => ', data, textStatus,jQXhr, dataType);
					var html;
					switch (dataType) {
						case 'json':
							html = tplSuccess(data.title, data.message);
							break;
						case 'html':
							html = data;							
							break;
						default: 
							html = null;
							console.error('data type ', dataType, ' default response not defined');
							break;
					}
					if (html) {
						$.app.modals.$tpl(html, $form.attr('action') + '-success', true).on('modal:hide', function (event) {
							$(this).remove();
						}).addClass('flash')
						.appendTo('body').find('[data-rel=close]:first').focus();
					}
				},
				error: function (jQXhr, textStatus, errorThrown) {
					
					if(jQXhr.status == "401"){
						window.location = $.app.rootPath + '/unauthorized';
						
					} else {
						$form.trigger('ajax:error', arguments);
						
						$.app.loader.hide();
					
						if ($form.data('skiperrorsdefault')) return false;
						// FUNCIONES PARA TODOS CUANDO ES ERROR
						console.log('AJAX REQUEST ERROR => ', jQXhr, textStatus, errorThrown);
					}
					
				},
				complete: function() {
					// Cerrar spinner
					$.app.loader.hide();
				}
			});
		};
	
	$('form[data-ajax][data-validate]').validate({ 
		submitHandler: function (form) {
			handleRemote($(form)); 
		} 
	});
	
	$(document).on('submit', 'form[data-ajax]:not([data-validate])', function (event) {
		handleRemote($(this));
		return false;
	});
	
	$(document).on('modal:update', '.modal', function (event) {
		$(this).find('form[data-ajax][data-validate]').validate({ 
			submitHandler: function (form) {
				handleRemote($(form)); 
			} 
		});
	});
});

// LINKS
$(function ($) {
	var $confirmTpl = function (title, message, $link) {
		var acceptBtn = $link.clone()
							.removeAttr('class')
							.removeAttr('data-confirm')
							.removeData('confirm')
							.html('<strong>' + I18n.t('buttons.ok') + '</strong>')
							.addClass('btn blue confirm_button')[0].outerHTML,
			removeBtn = $link.clone()
							.removeAttr('class')
							.removeAttr('data-confirm')
							.removeData('confirm')
							.html('<strong>' + I18n.t('buttons.remove') + '</strong>')
							.addClass('btn blue confirm_button')[0].outerHTML,
			html = '<header class="header_modal"><h1 class="section_title">' + title + '</h1></header>'
					+ '<article><p>' + message + '</p></article>'
					+ '<footer class="footer_modal">';
		
			if ($link[0].pathname.includes('removeFromAccount')) html = html + removeBtn;
			else html = html + acceptBtn;
			
			html = html + ' ' + '<button class="close btn mixed" data-rel="close"><strong>' + I18n.t('buttons.cancel') + '</strong></button>'
					+ '</footer>';
		
		return $.app.modals.$tpl(html, $link.attr('href'), true).addClass('confirm'); 
	};
		
	$(document).on('click', '[data-confirm]', function (event) {
		var $link = $(this);
		
		if ($link.data('confirm') || $link.data('confirm') != '') {
			event.stopImmediatePropagation();
			
			var $confirm = $confirmTpl($link.attr('title'), $link.data('confirm'), $link)
					.on('modal:hide', function (event) {
						$(this).remove();
					})
					.appendTo('body'),
				$acceptLink = $confirm.find('.btn.blue');
			
			if ($acceptLink.is('[data-ajax]')) {
				$acceptLink.on('ajax:success', function (event, data, textStatus, jQXhr) {
					$link.trigger('ajax:success', [data, textStatus, jQXhr]);
				});
				
				$acceptLink.on('ajax:error', function (event, jQXhr, textStatus, errorThrown) {
					$link.trigger('ajax:error', [jQXhr, textStatus, errorThrown]);
				});
			}
			
			return false;
		}
	});
	
	/**
	 * Datas:
	 *   data-type="html" 
	 *   data-skipsuccessdefault='true'
	 *   data-skiperrordefault='true'
	 */
	$(document).on('click', 'a[data-ajax]', function (event) {
		var $link    = $(this),
			method   = $link.data('method') || 'get',
			dataType = $link.data('type') || 'html';
		
		event.stopImmediatePropagation();
		
		if ( $link.is('[disabled]') ) return false;
		
		//console.log("before before");
		$link.trigger('ajax:before');
		
		// FUNCIONES PARA ANTES DE MANDARLO
		// Mostrar spinner
		$.app.loader.show();
		
		$.ajax($link.attr('href'), {
		    type: method,
		    dataType: dataType,
		    success: function (data, textStatus, jQXhr) {
		    	$link.trigger('ajax:success', arguments);
		        
		    	if ($link.data('skipsuccessdefault')) return false;
		        // FUNCIONES PARA TODOS CUANDO ES SUCCESS
		    	if ($('.modal.active').length > 0) $('.modal.active').removeClass('active');
				//console.log('AJAX REQUES SUCCESS => ', $link, textStatus,jQXhr);
				
				var html;
				switch (dataType) {
					case 'json':
						html = tplSuccess(data.title, data.message);
						break;
					case 'html':
						html = data;							
						break;
					default: 
						html = null;
						console.error('data type ', dataType, ' default response not defined');
						break;
				}
				if (html) {
					$.app.modals.$tpl(html, $link.attr('href') + '-success', true).on('modal:hide', function (event) {
						$(this).remove();
					}).addClass('flash')
					.appendTo('body').find('[data-rel=close]:first').focus();
				}
		    },
		    error: function (jQXhr, textStatus, errorThrown) {
		    	
		    	if(jQXhr.status == "401"){
					window.location = $.app.rootPath + '/unauthorized';
					
				} else {
		    	
			    	$link.trigger('ajax:error', arguments);
			        
			    	// Cerrar spinner
					$.app.loader.hide();
			    	
			    	if ($link.data('skiperrorsdefault')) return false;
			        // FUNCIONES PARA TODOS CUANDO ES ERROR
			    	//alert('Muy Mal ' + textStatus + ' ' + errorThrown);
			    	console.log('AJAX REQUEST ERROR => ', jQXhr, textStatus, errorThrown);
		    	}
		    },
		    complete: function() {
		    	// Cerrar spinner
				$.app.loader.hide();
		    }
		});
		
		return false;
	});
	
	$(document).on('click', '[data-method]:not([data-rel=modal])', function (event) {
		var $link = $(this);
		$('<form action="' + $link.attr('href') + '" method="' + $link.data('method') + '" style="display:none;"></form>').appendTo("body").submit();
		
		return false;
	});
	
	$(document).on('change', '.select-lang', function(event) {
		$('#select-lang').find('#current-url').val(document.URL);
		$('#select-lang').submit();
	});
});

