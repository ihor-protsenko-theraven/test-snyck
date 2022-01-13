// loader
(function ($){
	var $loader = null,
		$loaderTpl = function () {
			return $('<div class="loading"><img class="loader_icon" src="' + $.app.rootPath + '/images/loader.png" alt="' + I18n.t('loading') + '"></div>');
		};
		
	$.app.loader = {
			/** show app loader and blocks screen clicks with overlay */
			show: function () {
				if (!$loader || $loader.length <= 0) {
					$loader = $loaderTpl();
				}
				return $loader.appendTo('body');
			},
			/** hide app loader */
			hide: function () {
				if ($loader.length > 0) {
					$loader.detach();
					clearSelection();	// to prevent text from being selected if the user double clicks and loader is shown after the first click
				}
				return $loader;
			}
	};
	
	function clearSelection() {
	    if (window.getSelection) {
	        window.getSelection().removeAllRanges();
	    } else if (document.selection) {
	        document.selection.empty();
	    }
	}
	
})($);

$(function () {
	var $checker = $('<div style=""><div style="width:100px;" /><div style="width:100%;" /></div>').appendTo('body'),
		$first = $checker.find(':first-child'),
		$last  = $checker.find(':nth-child(2)'),
		zoom   = $last.width() / $first.width();
	
	function checkZoom() {
		var newZoom = $last.width() / $first.width();
		if (zoom != newZoom) {			
			$(window).triggerHandler('zoom');
			$.app.scroll.resize();
			zoom = newZoom;
		}
		
		if ( window.requestAnimationFrame)
			window.requestAnimationFrame(checkZoom);
		else
			setTimeout(checkZoom, 5);
	}
	
	function update() {
		( window.requestAnimationFrame ) 
			? window.requestAnimationFrame(checkZoom) 
			: setTimeout(checkZoom, 5); 
	}
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
			$radio.parent().addClass('checked');
		}
	}
	
	$(document).on('change', '.radio :radio', changeHandler);
});

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

// DROPDOWN
$(function () {
		
	function toggleDropdown ($dropdown) {
		$('.dropdown.active').not($dropdown).removeClass('active');
		$dropdown.toggleClass('active');
		
		if ( $dropdown.removeClass('reverse').is('.active') ) {
			var $list = $dropdown.find('> ul'),
			    $viewport = $list.closest($.app.scroll.me);
			
			if (!$viewport.length) $list.closest('.modal_content, body');
			
			var top = $list.offset().top + $list.height(),
				scrollTop = $viewport.offset().top + $viewport.height();
			
			if ( top > scrollTop ) {
				$dropdown.addClass('reverse');
			} else {
				$dropdown.removeClass('reverse');
			}
		}
	};
	
	$(document).on('click', '*:not(.dropdown button)', function (event) {
		$('.dropdown.active').removeClass('active reverse');
	});
	
	$(document).on('click', '.dropdown button', function (event) {
		toggleDropdown($(this).parent());		
		return false;
	});
});

// FIXED TABLE
$(function () {
	$('[data-fixed-table]').each(function () {
		var $table = $(this),
			$header  = ($table.data('fixed-table')) ? $( $table.data('fixed-table') ) : $('<table />').insertBefore($table.parent());
		
		$table.find('thead').clone().appendTo($header);
		
		var $ths = $header.find('th');
		
		$table.find('thead th').each(function (i) {
			$($ths[i]).width( $(this).width() );
		})
		.end().css('margin-top', -$header.height());
	});
	
	$(window).on('resize', function () {
		$('[data-fixed-table]').each(function () {
			
			var $table = $(this),
				$header  = ($table.data('fixed-table')) ? $( $table.data('fixed-table') ) : $table.parent().prev();
				$ths = $header.find('th');
			
			$table.find('thead th').each(function (i) {
				$($ths[i]).width( $(this).width() );
			});
		});
	});
});