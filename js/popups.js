/**
  * popups.js
 * Popup manager: manages a priority queue of popups, making sure they are shown in order according to priority
 * pushPopup method should be used for inserting a new popup in the queue
 * 
 * @author Ruben Sanchez Pelegrin <rubenspelegrin@gmail.com>
 */

'use strict';

$(function ($) {
	$.app = $.app || {};
	
	var popupPriorities = {
		very_high:	1,
		high:		2,
		medium:		3,
		low:		4,
		very_low:	5
	};
	
	var PopupManager = function() {
		this.firstPopup = null;
		this.lastPopup = null;
		this.freezed = false;
	};
	
	/**
	 * Inserts a new popup in the queue
	 * @param {number} priority 	Priority for this popup - integer (the lower the higher priority)
	 * @param {Object} $popup 		jQuery object of the popup
	 * @callbak [callback] 			Function to be executed after the popup closes and leaves the queue
	 * @param {...}					Arguments for the callback function
	 */
	PopupManager.prototype.pushPopup = function (_priority, $popup, callback) {
		
		var priority = parseInt(_priority);
		if (isNaN(priority)) {
			priority = popupPriorities[_priority];
			if (priority === undefined) {
				console.error('priority not defined');
				return;
			}
		}

		var params = [];
		for (var i = 3; i < arguments.length; i++) {
			params.push(arguments[i]);
		};
		var popup = new Popup(priority, $popup, callback, params);
		var showNow = false;
		
		if (this.firstPopup === null) {
			this.firstPopup = popup;
			this.lastPopup = popup;
			showNow = true;
		} else {
			// check if the popup is already in the queue, in that case we don't add it
			var p = this.firstPopup;
			while (p !== null) {
				if (p.$popup === $popup) {
					return;
				} else {
					p = p.nextPopup;
				}
			}
			
			var popupCompare = this.lastPopup;
			var isLast = true;
			var isFirst = false;
			
			while (popup.priority < popupCompare.priority && !isFirst){
				//higher priority, goes up in the queue
				if (popupCompare.prevPopup === null) {
					// it reached the top of the queue
					popupCompare.prevPopup = popup;
					popupCompare.nextPopup = popup.nextPopup;
					popup.nextPopup = popupCompare;
					this.firstPopup = popup;
					if (isLast) {	
						this.lastPopup = popupCompare;
						isLast = false;
					}
					showNow = true;
					isFirst = true;
				} else {
					//go up in the queue and continue
					popup.prevPopup = popupCompare.prevPopup;
					if (popup.prevPopup !== null) popup.prevPopup.nextPopup = popup;
					popupCompare.prevPopup = popup;
					popupCompare.nextPopup = popup.nextPopup;
					if (popup.nextPopup !== null) popup.nextPopup.prevPopup = popupCompare;
					popup.nextPopup = popupCompare;
					if (isLast) {
						this.lastPopup = popupCompare;
						isLast = false;
					}
					popupCompare = popup.prevPopup;
				}
			}
			if (!isFirst) {
				popupCompare.nextPopup = popup;
				popup.prevPopup = popupCompare;
			}
			if (isLast) {	
				this.lastPopup = popup;
			}
		}
		
		if (showNow) {
			var secondPopup = this.firstPopup.nextPopup;
			var manager = this;
			if (secondPopup !== null) {
				// There are more popups, which means that there was one open (the new second) that needs to be closed and reopened after the new one is closed
				secondPopup.$popup.off('popupafterclose');
				secondPopup.$popup.one('popupafterclose', function () {
					manager.openFirstPopup();		
				});
				secondPopup.$popup.popup("close");
			} else {
				this.openFirstPopup();
			}
			
		}
		
	};

	PopupManager.prototype.openFirstPopup = function () {
		if (this.firstPopup === null || this.freezed) return;
		var popup = this.firstPopup;
		var $popup = popup.$popup;
		var self = this;

		$('.ui-loader').css('visibility', 'hidden');	// hide loader so it doesn't collide with the popup
		$popup.popup('open');
		$popup.one('popupafterclose', function (event) {
			self.popupClosed();
		});
	};

	PopupManager.prototype.popupClosed = function () {
		// execute callback for closed popup
		if ($.isFunction(this.firstPopup.callback)) {
			this.firstPopup.callback.apply(this.firstPopup.$popup, this.firstPopup.params);
		}

		// remove the first element in the queue
		if (this.firstPopup === null) return;
		this.firstPopup = this.firstPopup.nextPopup;

		if (this.firstPopup !== null) {
			this.firstPopup.prevPopup = null;
			//there were more items in the queue, show the new first if the queue is not freezed
			if (!this.freezed) {
				this.openFirstPopup();
			} 
		} else {
			this.lastPopup = null;
			// make loader visible again (it will only appear if it was being shown)
			$('.ui-loader').css('visibility', 'visible');
		}		
	};
	
	// Freezes the queue, and then pending popups are not open when the current one is closed
	PopupManager.prototype.freeze = function () {
		this.freezed = true;
	};
	
	// Unfreezes the queue, showing the first popup (if any)
	PopupManager.prototype.unfreeze = function () {
		this.freezed = false;
		this.openFirstPopup();
	};

	/* Popup class */
	function Popup(priority, $popup, callback, params) {
		this.priority = priority;
		this.$popup = $popup;
		this.callback = callback;
		this.params = params;
		this.nextPopup = null;
		this.prevPopup = null;
	}

	$.app.popupManager = new PopupManager();
});

// When a page is loaded the queue is unfreezed
$(document).on('pageshow', function() {
	if($.app.popupManager) {
		$.app.popupManager.unfreeze();
	}
});
