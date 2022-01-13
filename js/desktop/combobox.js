(function ($) {
	$.widget('custom.combobox_autocomplete', $.ui.autocomplete, {
		_renderItem: function ( ul, item ) {
			var $li = $('<li>')
					.attr('data-value', item.value)
					.append( item.label );
			
			$.each($(item.option).data(), function (key, value) {
				$li.attr('data-' + key, value);
			});
			
			return $li.appendTo( ul );
		}
	});
	
	$.widget('custom.combobox', {
		options: {
			appendTo: null
		},
		val: function(value) {
			if ( typeof value === undefined ) return this.element.val();
			
			this.element.val(value);
			this.input.val(value);
		},
		_create: function() {
			this.wrapper = $( "<div>" )
				.addClass( "combobox" )
				.insertAfter( this.element )
				.wrapInner(this.element);
			
			//this.element.hide();
			this._createAutocomplete();
			this._createShowAllButton();
		},
		 
		_createAutocomplete: function() {
			var self = this,
				id       = this.element.attr('id'),
				tabindex = this.element.attr('tabindex'),
				selected = this.element.children( ":selected" ),
				value = selected.val() ? $.trim(selected.text()) : "",
				placeholder = this.element.find('[value=""]').length ? this.element.find('[value=""]').text() : '';
			
			this.input = $( "<input type='text'>" )
				.appendTo( this.wrapper )
				.val( value )
				.addClass( "combobox-input" )
				.combobox_autocomplete({
					delay: 0,
					minLength: 0,
					source: $.proxy(this, "_source"),
					appendTo: this.options.appendTo
				})
				.attr('tabindex', tabindex)
				.attr('placeholder', placeholder)
				.attr('id', id + "_combobox")
				.qtip({
					content: { text: I18n.t('combobox.not_in_list') }, // FIXME: configure i18n in plugin instead of I18n app js
					position: { my: 'bottom right', at: 'top right' },
					show: { event: false },
					hide: { event: false }
				});
			
			$('label[for=' + id + ']').attr('for', this.input.attr('id'));
			this.element.attr('tabindex', -1);
			
			this._on( this.input, {
				combobox_autocompleteselect: function( event, ui ) {
					ui.item.option.selected = true;
					self._trigger( "select", event, {
						item: ui.item.option
					});
				},
			
				combobox_autocompletechange: "_removeIfInvalid"
			});
		},
		 
		_createShowAllButton: function() {
			var input = this.input,
				wasOpen = false;
		
			$( "<a>" )
				.attr( "tabIndex", -1 )
				.appendTo( this.wrapper )
				.addClass( "combobox-toggle" )
				.mousedown(function() {
					wasOpen = input.combobox_autocomplete( "widget" ).is( ":visible" );
				})
				.click(function() {
					input.focus();
		
					// Close if already visible
					if ( wasOpen ) {
						return;
					}
		 
					// Pass empty string as value to search for, displaying all results
					input.combobox_autocomplete( "search", "" );
				});
		},
		 
		_source: function( request, response ) {
			var matcher = new RegExp( "^" + $.ui.autocomplete.escapeRegex(request.term), "i" );
			
			response( this.element.children( "option:not(.no-autocomplete):not([disabled])" ).map(function() {
				var text = $.trim( $( this ).text() );
				
				if ( this.value && ( !request.term || matcher.test(text) ) )
					return {
						label: text,
						value: text,
						option: this
					};
			}) );
		},
		 
		_removeIfInvalid: function( event, ui ) {
			// Selected an item, nothing to do
			if ( ui.item ) {
				return;
			}
		
			// Search for a match (case-insensitive)
			var self  = this,
				value = this.input.val(),
				valueLowerCase = value.toLowerCase(),
				valid = false;
			this.element.children( "option" ).each(function() {
				if ( $.trim($( this ).text()).toLowerCase() === valueLowerCase ) {
					this.selected = valid = true;
					return false;
				}
			});
		 
			// Found a match, nothing to do
			if ( valid ) {
				return;
			}
		 
			// Remove invalid value
			this.input
				.val( "" )
				.qtip( "show" );
			this.element.val( "" );
			this._delay(function() {
				this.input.qtip( "hide" );
			}, 3000 );
			this.input.combobox_autocomplete( "instance" ).term = "";
		},
		 
		_destroy: function() {
			var inputId  = this.input.attr('id'),
				tabIndex = this.input.attr('tabindex');
			
			this.element.attr('tabindex', tabindex).insertAfter(this.wrapper);
			this.wrapper.remove();
			
			$('label[for=' + inputId +']').attr('for', this.element.attr('id'));
		}
	});
})(jQuery);