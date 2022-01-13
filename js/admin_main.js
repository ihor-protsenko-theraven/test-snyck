$.app = {};
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

// Modals
$(function ($){
  var $modals, $links_modal;

  $modals = $('.modal');
  $links_modal = $('[data-rel=modal]');

  $links_modal.on('click', function (event) {
    var href, $modal;

    href = $(this).attr('href');
    $modal = $modals.filter(href);
    console.log($modal);
    if ($modal.length > 0) {
      $modal.addClass('active');
    }

    return false;
  });
});

// set main content height
$(function ($){
  var $window, $main_header,
      $main_nav, $filter_nav,
      $main_content, $main_footer;

  $window       = $(window);
  $main_header  = $('.main_header');
  $main_nav     = $('.main_nav');
  $filter_nav   = $('.filter_nav');
  $main_content = $('.main_content');
  $main_footer  = $('.main_footer');

  function calculateContentHeight () {
    var fHeight = $window.innerHeight() - $main_header.height() - $main_nav.height() - $filter_nav.height() - $main_footer.height();
    $main_content.css('height', fHeight);

    return fHeight;
  }

  $window.on('resize', function (event) {
    calculateContentHeight();
  });

  calculateContentHeight();

  $.app.scroll = $main_content.niceScroll({ mousescrollstep: 5, scrollspeed: 100 });
});

// datepickers
$(function ($) {
  var $datepickers = $('[type=date]');

  $datepickers.attr('type', 'text').datepicker();
});

// uniform checkboxes
$(function ($) {
  var $checkboxes = $(':checkbox');

  $checkboxes.uniform();
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

    if (Modernizr.csstransitions){
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

    if (Modernizr.csstransitions){
      setTimeout(function () {
        $btn.css('width', '0');
        setTimeout(function () {
          $target.addClass('hidden');
        }, 250)
      }, 500);
    } else {
      $btn.css('width', '0');
      $target.addClass('hidden');
    }
  }

  $('[data-rel=show_filters]').on('click', function (event) {
    var $this, href, $target;

    $this = $(this);
    href  = $this.attr('href')
    $target = $(href);

    if ($target.length > 0) {
      show($target);
    }

    return false;
  });

  $filters.on('click', '.btn', function (event) {
    hide($filters);
    return false;
  });
  $filters.on('change', ':checkbox[id$=_all]', function (event) {
    var $this, id;
    $this = $(this);
    id    = $this.attr('id').replace(/_all$/, '');

    $(':checkbox[id^=' + id + ']').not($this).prop('checked', $this.is(':checked')).uniform('update');
  });
  $filters.on('click', function (event) {
    if (event.target.id == 'filters') {
      hide($filters);
      return false;
    }
  });
});

// short link simulator
$(function ($) {
  var $short_links = $('.short_link');

  $short_links.on('click', function (event) {
    $(this).toggleClass('reverse');

    return false;
  });
});

// AJAX FOR TABLE RESULTS (FIRST LOAD AND PAGINATION)
$(function ($) {
  var $main_content = $('.main_content'),
      $load         = $('#load'),
      $loadRow      = $('<tr>'
        +   '<td colspan="6" class="loadrow">'
        +     '<span class="icon drop_down"></span>'
        +     '<strong>Loading...</strong>'
        +     '<span class="icon drop_down"></span>'
        +   '</td>'
        + '</tr>'),
      requesting    = false;
      endPages      = false;
      maxScroll     = 0,
      // VARS USED TO SIMULATE END OF PAGES
      counterPage   = 0,
      maxPages      = 3;

  function loadTable (url) {
    requesting = true;

    $loadRow.appendTo($load);

    $.get(url, function (data, textStatus, jQXhr) {
      if (data) {
        $load.append(data);
        $.app.scroll.resize();
      } else {
        endPages = true;
      }

      $loadRow.detach();
      updateMaxScroll();
      requesting = false;
    });
  };

  function updateMaxScroll () {
    maxScroll = $main_content[0].scrollHeight - $main_content.height();
  };

  function bindScroll () {
    $main_content.on('scroll', function (event) {
      if (!requesting && !endPages && $main_content.scrollTop() >= maxScroll) {
        var url = (++counterPage < maxPages) ? 'ajax/event-log-page.html' : 'ajax/empty.html'; // USED TO SIMULATE END OF PAGES
        loadTable(url);
      }
    });
  };

  //Load data
  $('#load_data').on('click', function(event) {
    loadTable('ajax/event-log-page.html');
    bindScroll();

    return false;
  });
});
