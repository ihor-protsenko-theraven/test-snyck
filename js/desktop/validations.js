$(function ($) {
	$.validator.setDefaults({
		ignore: ':hidden:not(.duration)',
		onkeyup: false,
		errorElement: 'em',
		errorPlacement: function (error, element) {
			var $el = element.is('.combobox') 
				? element.parent() 
			  : element.is('.duration') 
				? element.parent().find('.duration-input-group:last input')
				: element,
				
				$ev_target = element.is('.combobox') 
					? element.parent().find('input')
				  : element.is('.duration')
				    ? element.parent().find('.duration-input-group input')
					: element;

			$el.qtip({ 
				content: { text: error },
				position: { my: 'bottom right', at: 'top right' },
				show: { target: $ev_target, event: 'focus' },
				hide: { target: $ev_target, event: 'focusout' },
				style: { classes: 'qtip-red' }
			});
		},
		success: function (label, element) {
			var $el = $(element).is('.combobox') 
				? $(element).parent()
			  : $(element).is('.duration')
			    ? $(element).parent().find('.duration-input-group:last input')
				: $(element);
			$el.qtip('destroy');
		},
		highlight: function (element, errorClass) {
			var $el = $(element);
			if ($el.is('.combobox')) {
				$el.combobox('instance').input.addClass(errorClass);
			} else if ($el.is('.duration')) {
				$el.parent().addClass(errorClass);
			} else if ($el.is('select, :checkbox, :radio')) {
				$el.parent().addClass(errorClass);
			} else {
				$el.addClass(errorClass);
			}
			
		},
		unhighlight: function (element, errorClass, validClass) {
			var $el = $(element);
			
			if ($el.is('.combobox-input')) {
				setTimeout(function () {
					if ( $el.prev('select').valid() ) $el.removeClass(errorClass);
				});
			} else if ($el.is('.combobox')) {
				$el.combobox('instance').input.removeClass(errorClass);
			} else if ($el.is('.duration')) {
				$el.parent().removeClass(errorClass);
			}else if ($el.is('select, :checkbox, :radio')) {
				$el.parent().removeClass(errorClass);
			} else {
				$el.removeClass(errorClass);
			}
		},
		onfocusout: false,
		invalidHandler: function(form, validator) {	// focus to the first
													// invalid field
			var errors = validator.numberOfInvalids();
			if (errors) {
				
				let element = validator.errorList[0].element;
				setTimeout(function() {
					var tog = $(element).children()
					
					if ($(element).is("select") && $(element).siblings('.combobox-input')) {
						$(element).siblings('.combobox-input').focus();
					} else {
						$(element).focus();
					}
				}, 500);
			}
		}
	});	
	
	$.validator.addMethod('mobilePhoneADL', function(phone) {
		// Mobile phones in ADL have to be E164 compliance. See EIC15-2751
		
		if (!phone) {
			return !phone;
		}
		
		parsed_number = libphonenumber.parseNumber(phone, null);
		if (!parsed_number.phone || !parsed_number.country) {
			console.log("Not a valid E164 phone number: ", phone);
			return false;
		}
		e164PhoneNumber = libphonenumber.formatNumber(parsed_number, 'E.164')
		if (e164PhoneNumber === phone) {
			return true;
		}
		return false;
	});
	
	$.validator.addMethod('e164ordigits', function(phone) {


		let isDigitsSeq =  !phone || phone.match( /^[+]?[1-9]\d{8,14}$/); 
				
		if (isDigitsSeq) {
			return true;
		}


		parsed_number = libphonenumber.parseNumber(phone, null);
		if (!parsed_number.phone || !parsed_number.country) {
			console.log("Not a valid E164 phone number: ", phone);
			return false;
		}
		e164PhoneNumber = libphonenumber.formatNumber(parsed_number, 'E.164')
		if (e164PhoneNumber === phone) {
			return true;
		}
		return false;
	});
	
	$.validator.addMethod('passwordADL', function(passwordVal,element,params) {
		
		if (sessionStorage.getItem("charGroupsMinimumPolicy") != null) {
			params = sessionStorage.getItem("charGroupsMinimumPolicy");
		}
		else sessionStorage.setItem("charGroupsMinimumPolicy",params);
		
		// 
		var numberInPassword = false,
        	charInPassword = false,
        	SymbolInPassword = false,
        	CapitalInPassword = false,
        	types = 0;
		if (passwordVal.match("[0-9]") && passwordVal.match("[0-9]").length > 0) {
            numberInPassword = true;
            types++;
        }
        if (passwordVal.match("[A-Z]") && passwordVal.match("[A-Z]").length > 0) {
            CapitalInPassword = true;
            types++;
        }
        if (passwordVal.match("[a-z]") && passwordVal.match("[a-z]").length > 0) {
            charInPassword = true;
            types++;
        }
        if (passwordVal.match("[!,@,#,$,%,^,&,*,?,_,~,-.,(,)]") && passwordVal.match("[!,@,#,$,%,^,&,*,?,_,~,-.,(,)]").length > 0) {
            SymbolInPassword = true;
            types++;
        }
        return (types >= params);
	});
	
	$.validator.addMethod('emailADL', function(email) {
		return (!email || email.match(/^[a-zA-Z0-9_\.-]+\@([a-zA-Z0-9-]+\.)+[a-zA-Z]{2,20}$/));
	});
	
	$('form[data-validate]:not([data-ajax])').validate();
	
	$(document).on('modal:update', '.modal', function (event) {
		$(this).find('form[data-validate]:not([data-ajax])').validate();
	});
});