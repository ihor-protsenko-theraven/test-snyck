/**
 * 
 */


//Qtips
$(function ($) {
	function initTips($container) {
		var $tips = $container.find('[data-hastip]');
		if ($tips.length > 0) {
			$tips.each(function () {
				var $el = $(this),
					options = {
						content: { attr: 'title' },
						position: { my: 'bottom center', at: 'top center', viewport: $(window) }	
					};
				
				if ( $el.data('hastip-classes') ) {
					options.style = { classes: $el.data('hastip-classes') };
				}
				$el.qtip(options);
			});
		}
	};
	
	function hideTips (event) {
		var $qtips = $(this).find('[data-hasqtip]');
		
		if ( $qtips.length ) {
			$qtips.qtip('hide');
		}
	};
	
	initTips($(document));
	
	$(window).on('scroll', hideTips);
	 //$.app.scroll.me.on('scroll', hideTips);
	
	console.log($(document).find(".main_content table[data-pagination]"));
	$('.main_content table[data-pagination]').on('table:added_page', function (event, $data) {
		initTips($data);
	});
});
