/**
 * plugins.js
 * javascripts plugins for application
 * 
 * @author Samuel Santiago <samuelsantia@gmail.com>
 */

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

/*! svg4everybody v1.0.0 | github.com/jonathantneal/svg4everybody */
(function (document, uses, requestAnimationFrame, CACHE, IE9TO11) {
	function embed(svg, g) {
		if (g) {
			var
			viewBox = g.getAttribute('viewBox'),
			fragment = document.createDocumentFragment(),
			clone = g.cloneNode(true);

			if (viewBox) {
				svg.setAttribute('viewBox', viewBox);
			}

			while (clone.childNodes.length) {
				fragment.appendChild(clone.childNodes[0]);
			}

			svg.appendChild(fragment);
		}
	}

	function onload() {
		var xhr = this, x = document.createElement('x'), s = xhr.s;

		x.innerHTML = xhr.responseText;

		xhr.onload = function () {
			s.splice(0).map(function (array) {
				embed(array[0], x.querySelector('#' + array[1].replace(/(\W)/g, '\\$1')));
			});
		};

		xhr.onload();
	}

	function onframe() {
		var use;

		while ((use = uses[0])) {
			var
			svg = use.parentNode,
			url = use.getAttribute('xlink:href').split('#'),
			url_root = url[0],
			url_hash = url[1];

			svg.removeChild(use);

			if (url_root.length) {
				var xhr = CACHE[url_root] = CACHE[url_root] || new XMLHttpRequest();

				if (!xhr.s) {
					xhr.s = [];

					xhr.open('GET', url_root);

					xhr.onload = onload;

					xhr.send();
				}

				xhr.s.push([svg, url_hash]);

				if (xhr.readyState === 4) {
					xhr.onload();
				}

			} else {
				embed(svg, document.getElementById(url_hash));
			}
		}

		requestAnimationFrame(onframe);
	}

	if (IE9TO11) {
		onframe();
	}
})(
	document,
	document.getElementsByTagName('use'),
	window.requestAnimationFrame || window.setTimeout,
	{},
	navigator.userAgent.indexOf('Firefox') > -1 || /Trident\/[567]\b/.test(navigator.userAgent) || (navigator.userAgent.match(/AppleWebKit\/(\d+)/) || [])[1] < 537 //IE9TO11
);

// jQuery checker
$(function ($) {
	
	function changeHandler() {
		var $checkbox = $(this),
			$checker  = $checkbox.parent();
	
		if ($checkbox.is(':checked')) {
			$checker.addClass('checked');
		} else {
			$checker.removeClass('checked');
		}
	}
	
	$(document).on('change', '.checker :checkbox', changeHandler);
	
	$('.checker :checkbox').each(changeHandler);
});

// jQuery radio
$(function ($) {
	function changeHandler() {
		var $radio = $(this),
			$group = $('[name="' + $radio.attr('name') + '"]');
		
		if ($radio.is(':checked')) {
			$group.each(function () {
				$(this).parent().removeClass('checked');
			});
			$radio.parent().addClass('checked');
		} else {
			$radio.parent().addClass('checked')
		}
	}
	
	$(document).on('change', '.radio :radio', changeHandler);
});

// jQuery mobile popup with close buttons
$(document).on('popupcreate', '[data-role=popup]', function (event, ui) {
	var $popup = $(this),
		$close = $popup.find('[data-rel=close]');
	
	$close.on('click', function (event) {
		$popup.popup('close', { transition: 'fade' });
		
		return false;
	});
});

// Header footer and page positioned
$(function ($) {
	var $win     = $(window),
		$header  = $('#main-header'),
		$footer  = $('#main-footer'),
		$content = $('[data-role=page] .content');
	
	if ($header.length > 0 || $footer.length > 0) {
		$content = $('[data-role=page] .content');
		
		function recolocateContent() {
			//$content.removeAttr('style');
			
			if ($header.length > 0) {
				$content.css('padding-top', $header.outerHeight());
			}
			if ($footer.length > 0) {
				if (parseInt($content.css('padding-botton', 'inherit').css('padding-bottom')) > 0) {
					$content.css('padding-bottom', '+=' + $footer.outerHeight());
				} else {
					$content.css('padding-bottom', $footer.outerHeight());
				}
			}
		}
		
		// FIXME: when mobile trigger resize when hide navbar add more padding bottom and the page grow up
		/*$win.on('resize', function (event) {
			recolocateContent();
		});*/
		
		$(document).on('pagecreate', '[data-role=page]', function (event, ui) {
			$content = $(this).find('.content');
			
			recolocateContent();
		});	
		
		recolocateContent();
	}
});

// Sliders toggle
$(function ($) {
	$.app = $.app || {};
	$.app.sliders = {};
	
	$.app.sliders.init = function ($container) {
		$container.find('[data-rel=slide-show]').on('click', function (event) {
			var $link = $(this),
				$slider  = $($link.attr('href')),
				$overlay = $($link.attr('href') + '-screen'),
				isActive = $slider.is('.active');
			
			switch ($slider.data('slider-direction')) {
				case 'down':
					(isActive) 
						? $slider.css('top', -$slider.height())
						: $slider.addClass('cancel-animate').css('top', -$slider.height());
					break;
			}
			
			var timingCallback = parseFloat($slider.css(Modernizr.prefixed('transitionDuration'))) * 1000 + 1;
			
			(isActive) ? $('html').removeClass('no-scroll') : $('html').addClass('no-scroll');
			
			
			setTimeout(function () {
				if (isActive) {
					var cb = function () {
						$slider.removeClass('cancel-animate').toggleClass('active').removeAttr('style');
						$overlay.removeClass('in').addClass('ui-screen-hidden');
					};
					$slider.on('slider:before_hide', function (event) {
						if (!event.isDefaultPrevented()) {
							$slider.off('slider:before_hide');
							cb();
						}	
					});
					$slider.trigger('slider:before_hide', [cb]);
				} else{
					var cb = function () {
						$slider.removeClass('cancel-animate').toggleClass('active').removeAttr('style');
						$overlay.removeClass('ui-screen-hidden').addClass('in');
					};
					$slider.on('slider:before_show', function (event) {
						if (!event.isDefaultPrevented()) {
							$slider.off('slider:before_show');
							cb();
						}
					});
					$slider.trigger('slider:before_show', [cb]);
				} 
			}, timingCallback);
				
			return false;
		});
	};
	
	$.app.sliders.init($(document));
});

// Panels
$(function ($) {
	$.app = $.app || {};
	$.app.panels = {};
	
	$.app.panels.init = function ($container) {
		$container.find('[data-rel=panel]').each(function (event) {
			var $link    = $(this),
				$panel   = $($link.attr('href')),
				$overlay = $($link.attr('href') + '-screen');
			
			if (!$overlay.parent().is('.panel-wrap-content')){
				$panel.parent().find('> *:not(.panel)').wrapAll('<div class="panel-wrap-container"><div class="panel-wrap-content"></div></div>');
			}
		});
		$container.find('[data-rel=panel]').on('click', function (event) {
			var $link    = $(this),
				$panel   = $($link.attr('href')),
				$overlay = $($link.attr('href') + '-screen');
			
			$panel.addClass('active');
			$overlay.parent().addClass('panel-left-active')
				.parent().css('overflow', 'hidden');
			
			$('html').addClass('no-scroll');
			
			// PANEL CLOSE EVENTS
			$overlay.addClass('in').removeClass('ui-screen-hidden').on('click.app-panels', function (event) {
				$overlay.off('click.app-panels');
				$(window).off('pagebeforechange.app-panels');
				
				$overlay.removeClass('in').addClass('ui-screen-hidden')
					.parent().removeClass('panel-left-active')
					.parent().css('overflow-x', 'hidden');
				$panel.removeClass('active');
				
				var timingCallback = parseFloat($panel.css(Modernizr.prefixed('transitionDuration'))) * 1000 + 1;
				
				setTimeout(function () {
					$overlay.parent().parent().removeAttr('style');
				}, timingCallback);
				
				$('html').removeClass('no-scroll');
				
				return false;
			});
			
			$(window).on('pagebeforechange.app-panels',function (event, data) {
				$overlay.off('click.app-panels');
				$(window).off('pagebeforechange.app-panels');
				
				$overlay.removeClass('in').addClass('ui-screen-hidden')
					.parent().removeClass('panel-left-active');
				$panel.removeClass('active');
				
				var timingCallback = parseFloat($panel.css(Modernizr.prefixed('transitionDuration'))) * 1000 + 1;
				
				setTimeout(function () {
					$.mobile.changePage(data.toPage, data.options);
				}, timingCallback);
				
				$('html').removeClass('no-scroll');
				
				return false;
			});
			
			return false;
		});
	};
	
	$.app.panels.init($(document));
});

(function ($) {
	/**
	 * Requirements:
	 *  - the link must have a 'data-rel' attribute with the id reference of the panel to use.
	 *
	 */
	$.fn.panelify = function() {
		var $this = $(this);
		//console.log("this", this);
		$this.on('click', function (event) {
			var $link    = $(this),
				  $panel   = $($link.attr('href')),
				  $overlay = $($link.attr('href') + '-screen');
			
			$panel.trigger('panel:open');
			
			$panel.addClass('active');
			$overlay.parent().addClass('panel-left-active');
			//$overlay.parent().addClass('with-subpanel');
			$overlay.parent().parent().addClass('with-subpanel');
			
			$('html').addClass('no-scroll');
			
			// PANEL CLOSE EVENTS
			$overlay.addClass('in').removeClass('ui-screen-hidden').on('click.app-panels', function (event) {
				$overlay.off('click.app-panels');
				$(window).off('pagebeforechange.app-panels');
				
				$overlay.removeClass('in').addClass('ui-screen-hidden')
					.parent().removeClass('panel-left-active')
					.parent().removeClass('with-subpanel');
				$panel.removeClass('active');
				$panel.trigger('panel:close');
				
				$('html').removeClass('no-scroll');
				return false;
			});
			
			$(window).on('pagebeforechange.app-panels',function (event, data) {
				$overlay.off('click.app-panels');
				$(window).off('pagebeforechange.app-panels');
				
				$overlay.removeClass('in').addClass('ui-screen-hidden')
					.parent().removeClass('panel-left-active')
					.parent().removeClass('with-subpanel');
				$panel.removeClass('active');
				
				var timingCallback = parseFloat($panel.css(Modernizr.prefixed('transitionDuration'))) * 1000 + 1;
				
				setTimeout(function () {
					$.mobile.changePage(data.toPage, data.options);
				}, timingCallback);
				
				$panel.trigger('panel:close');
				$('html').removeClass('no-scroll');
				return false;
			});
			
			$panel.on('click', '.group', function(event, data){
				var $this = $(this).find('.collapsible');
				if ($this.hasClass('collapsed')) {
					$this.removeClass('collapsed');
					$this.parent().next().slideDown(300);
				} else {
					$this.addClass('collapsed');
					$this.parent().next().slideUp(300);
				}
				
				return false;
			});
			
			return false;
		});
	};
})($);

(function ($){
	$.app = $.app || {};
	$.app.isRTL = $('html').is('[dir=rtl]');
})($);