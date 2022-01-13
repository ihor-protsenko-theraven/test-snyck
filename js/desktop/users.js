var $select;
// Add User
$(function($) {
	// EIC15-2639
	$('#ppad').hover(function (event) {
		$('#user_password').blur();
	},function (event) {
		$('#user_password').focus();
	});
	
	var $user_panel = $('#user-panel'),
		$user_sn    = $('#user_serialNumber'),
		$user_sncustomer = $('#user_serialNumCustomer');
	
	if ( $('#users-list').length || $('#residents').length) {
		sessionStorage.setItem('usersBackUrl', window.location.href);
	}
	
	$('[data-users-back]').on('click', function (event) {
		
		var redirectURL = sessionStorage.getItem('usersBackUrl');
		
		if(redirectURL == null){
			var userType = $('[data-user-type]').attr('data-user-type');
			
			if(userType != null){
				if(userType == 1){
					window.location.href = $.app.rootPath + "/admin/users";
				}else if(userType == 2){
					window.location.href = $.app.rootPath + "/admin/residents";
				}
			}
			
		}else{
			event.preventDefault();
			window.location.href = redirectURL;
		}
		
	});
	
	$('[data-input-users-back]').on('submit', function (event) {
		
		if(event.handled !== true){
			
			var redirectURL = sessionStorage.getItem('usersBackUrl').replace(/%5B/g, '[').replace(/%5D/g, ']');
			
			if(redirectURL == null){
				var userType = $('[data-user-type]').attr('data-user-type');
				
				if(userType == 1){
					redirectURL = $.app.rootPath + "/admin/users";
				}else if(userType == 2){
					redirectURL = $.app.rootPath + "/admin/residents";
				}	
			}
	
			redirectURL = encodeURI(redirectURL);
			
			if($('[data-input-users-back]').find('#redirectURL').length > 0){
				
				$('[data-input-users-back]').find('#redirectURL').eq(0).attr("name", "user[redirectURL]").val(redirectURL);
				
			}else{
				var input = $("<input>")
				.attr("id", "redirectURL")
	            .attr("type", "hidden")
	            .attr("name", "user[redirectURL]").val(redirectURL);
			
				$('[data-input-users-back]').append($(input));
			}
			
			
			event.handled = true;
			
		}
		
	});
	
	function setVisibility(elementId, communicationInterface) {
	    var communicationInterfaces = $("#user-panel option:selected").attr("data-communication-interfaces"); 
		
        if (communicationInterfaces && communicationInterfaces.indexOf(communicationInterface) >= 0) {
			$(elementId).closest("li").css('visibility', 'visible');
			$(elementId).closest("li").css('position', 'relative');
			$(elementId).attr('required', '');
		} else {
			$(elementId).closest("li").css('visibility', 'hidden');			
			$(elementId).closest("li").css('position', 'absolute');
			$(elementId).removeAttr('required');
		}
	}

	// set default visibility for simNumber and landlineNumber fields
	setVisibility("#user_simNumber", "Cellular");
	setVisibility("#user_landlineNumber", "PSTN");
	
	$user_panel.on('comboboxselect', function (event) {
		var $selected = $user_panel.find(':selected'),
			value = $user_panel.val(),
			sn_value = $selected.data('helptext'),
			sn_customer_value = $selected.data('sncustomer'),
			$related_option;
		
		setVisibility("#user_simNumber", "Cellular");
		setVisibility("#user_landlineNumber", "PSTN");
		
		if (sn_value) sn_value = sn_value.toString();
		
		if (sn_value && sn_value.length > 0 && value.length > 0) $related_option = $user_sn.find('[value=' + sn_value + '][data-helptext=' + value + ']');
		
		if ( sn_value && $related_option && $related_option.length ) {
			$related_option.attr('selected', true);
			$user_sn.combobox('instance').input.val(sn_value);
		} else {
			$user_sn.find('[value=""]').attr('selected', true);
			$user_sn.combobox('instance').input.val('');
		}
		
		if ( sn_customer_value ) {
			$user_sncustomer.val( sn_customer_value );
		} else {
			$user_sncustomer.val("");
		}
	});
	$user_sn.on('comboboxselect', function (event) {
		var $selected = $user_sn.find(':selected'),
			value = $user_sn.val(),
			panel_value = $selected.data('helptext'),
			sn_customer_value = $selected.data('sncustomer'),
			$related_option;
		
		if (panel_value) panel_value = panel_value.toString();
		
		if (panel_value && panel_value.length > 0 && value.length > 0) $related_option = $user_panel.find('[value=' + panel_value + '][data-helptext=' + value + ']');
		
		if ( panel_value && $user_panel.length && $related_option && $related_option.length ) {
			$related_option.attr('selected', true);
			$user_panel.combobox('instance').input.val(panel_value);
		} else {
			$user_panel.find('[value=""]').attr('selected', true);
			$user_panel.combobox('instance').input.val('');
		}
		
		if ( sn_customer_value ) {
			$user_sncustomer.val( sn_customer_value );
		} else {
			$user_sncustomer.val("");
		}
	});
	
	$(document).on('change', '#user_service_type', function () {
		
		$user_panel.find('option').removeClass('no-autocomplete');
		$user_sn.find('option').removeClass('no-autocomplete');
		$user_panel.find('option').attr('disabled','diabled');
		
		var panelType = $('#user_panel_type').val();
		var serviceType = this.value;
		
		if (panelType && serviceType){
			
			$user_panel.find(':not([data-panel-type="' + panelType + '"])').addClass('no-autocomplete');
			$user_sn.find(':not([data-panel-type="' + panelType + '"])').addClass('no-autocomplete');
			$user_panel.find(':not([data-service-type="' + serviceType + '"])').addClass('no-autocomplete');
			$user_sn.find(':not([data-service-type="' + serviceType + '"])').addClass('no-autocomplete');
			$user_panel.find('option').removeAttr('disabled'); 
			
			if ( $user_panel.find( '[value="' + $user_panel.val() + '"].no-autocomplete' ).length ) {
				$user_panel.find('[value=""]').attr('selected', true).end()
					.trigger('comboboxselect').combobox('instance').input.val("");
				$user_sn.find('[value=""]').attr('selected', true).end()
					.trigger('comboboxselect').combobox('instance').input.val("");
			}
		}
	});

	$(document).on('change', '#user_panel_type', function () {
		
		$user_panel.find('option').removeClass('no-autocomplete');
		$user_sn.find('option').removeClass('no-autocomplete');
		$user_panel.find('option').attr('disabled','diabled');
		
		var panelType = this.value;
		var serviceType = $('#user_service_type').val();
		
		if (panelType && serviceType){
			if (serviceType === "PERSE") {
				serviceType = "EXPRESS";
			}
			$user_panel.find(':not([data-panel-type="' + panelType + '"])').addClass('no-autocomplete');
			$user_sn.find(':not([data-panel-type="' + panelType + '"])').addClass('no-autocomplete');
			$user_panel.find(':not([data-service-type="' + serviceType + '"])').addClass('no-autocomplete');
			$user_sn.find(':not([data-service-type="' + serviceType + '"])').addClass('no-autocomplete');
			$user_panel.find('option').removeAttr('disabled');  
			
			if ( $user_panel.find( '[value="' + $user_panel.val() + '"].no-autocomplete' ).length ) {
				$user_panel.find('[value=""]').attr('selected', true).end()
					.trigger('comboboxselect').combobox('instance').input.val("");
				$user_sn.find('[value=""]').attr('selected', true).end()
					.trigger('comboboxselect').combobox('instance').input.val("");
			}
		}
	});
});

//
$(function($) {
	if ( $('#add_user').length ) {
		var $residentExtraFields   = $('#resident-extra-fields, #user_service_type_field, #user_panel_type_field'),
			$loginExtraFields      = $('#login-extra-fields'),
			$accountNumberField    = $('#user_accountNumber_field'),
			$mobileOnDesktopField  = $('#user_mobileModeOnDesktop_field'),
			$cellField             = $('#user_cellPhone'),
			$cellLabel             = $('label[for=user_cellPhone]'),
			$emailField            = $('#user_email'),
			$emailLabel            = $('label[for=user_email]'),
			$genderField           = $('#user_gender_field');
			$timeZonesField		   = $('#user_timeZones_field'),
			$activeHandsFreeFields = $('#active_handsfree_field');
		
		function extraFields(role_selected) {
			switch (role_selected) {
			case "ROLE_CAREGIVER":
			case "ROLE_ADMIN":
				$residentExtraFields.hide();
				$loginExtraFields.show();
				break;
			case "ROLE_MONITORED":
				$residentExtraFields.show();
				$loginExtraFields.hide();
				break;
			default:
				$residentExtraFields.hide();
				$loginExtraFields.hide();
				break;
			}
			
			if ( role_selected == 'ROLE_CAREGIVER' ){
				//$('#user_mobileModeOnDesktop').prop('checked',role_selected=="ROLE_CAREGIVER" );
				$('#user_mobileModeOnDesktop').prop('checked',true );
				$('#user_mobileModeOnDesktop').parent().addClass("checked");
				$mobileOnDesktopField.show(); 
			}else{
				$('#user_mobileModeOnDesktop').parent().removeClass("checked");
				$('#user_mobileModeOnDesktop').prop('checked',false );
				$mobileOnDesktopField.hide();
			}
			
			( role_selected == '' || role_selected == 'ROLE_CAREGIVER' || role_selected === 'ROLE_ADMIN' )
			 ? $genderField.appendTo($('.form-list:eq(0)'))
		     : $genderField.prependTo($('.form-list:eq(1)'));
				
			
			( role_selected == 'ROLE_MONITORED' ) 
				? ($accountNumberField.show(), $timeZonesField.show(), $activeHandsFreeFields.show()) : 
					($accountNumberField.hide(), $timeZonesField.hide(), $activeHandsFreeFields.hide());
				
		};
		
		$(document).on('change', '#user_roles', function (event) {
			var $this = $(this);
			
			extraFields($this.val());
			$.app.scroll.resize();
		});
	
		extraFields($('#user_roles').val());		
		$.app.scroll.resize();
		
		
		function enableAccountsValue(){
			
			// TODO this does not check that the service type value be one allowed
			// TODO refactor, 98% duplicated code from on change event on user_service_type element
			if($('#user_service_type').val().length > 0){
			
				var $user_panel = $('#user-panel'),
				$user_sn    = $('#user_serialNumber'),
				$user_sncustomer = $('#user_serialNumCustomer');
				
				var serviceType = $('#user_service_type').val();
				
				$user_panel.find('option').removeClass('no-autocomplete');
				$user_sn.find('option').removeClass('no-autocomplete');
				$user_panel.find('option').attr('disabled','diabled');
				
				if (serviceType){
					$user_panel.find(':not([data-service-type="' + serviceType + '"])').addClass('no-autocomplete');
					$user_sn.find(':not([data-service-type="' + serviceType + '"])').addClass('no-autocomplete');
					$user_panel.find('option').removeAttr('disabled');  
					
					if ( $user_panel.find( '[value="' + $user_panel.val() + '"].no-autocomplete' ).length ) {
						$user_panel.find('[value=""]').attr('selected', true).end()
							.trigger('comboboxselect').combobox('instance').input.val("");
						$user_sn.find('[value=""]').attr('selected', true).end()
							.trigger('comboboxselect').combobox('instance').input.val("");
					}
				}
			}
		}
		
		enableAccountsValue();
		
		
	}
});

$(function(){
	$("#add_user, #edit_user").submit(function(event){
		var $userForm = $(this);
		
		if($("#user_activeService").is(':checked')){ // checks if active
														// feature is selected
			if($('#user-panel').length > 0 && $('#user-panel').find(':selected').length > 0 && 
					!$('#user-panel').find(':selected').data('activeservice')){ // checks
																			// if
																			// panel
																			// supports
																			// active
																			// feature
				
				// creates the modal html body
				var popupTitle = I18n.t('pop_ups.active_pop_up_title');
				var popupMessage = I18n.t('pop_ups.active_pop_up_message');
				
				var $popupContent = '<header class="header_modal"><h1 class="section_title">' + popupTitle + '</h1></header>'
				+ '<article><p class="form_modal_text" style="font-size:1.2em; padding-left:0.5em; padding-right: 0.5em;">' + popupMessage + '</p></article>'
				+ '<footer class="footer_modal">'
					+ '<button id="users_active_modal_confirm_btn" class="btn grey" data-rel="close"><strong>' + I18n.t('buttons.ok') + '</strong></button>' + ' '
					+ '<button id="users_active_modal_cancel_btn" class="btn grey" data-rel="close"><strong>' + I18n.t('buttons.cancel') + '</strong></button>'
				+ '</footer>';
				
				// Manage confirmation/cancellation
				
				$.app.modals.$tpl($popupContent, 'users_active_modal', true).appendTo('body').trigger('modal:create').trigger('modal:update').trigger('modal.show');
				
				$(document).on('click', '#users_active_modal_confirm_btn', function() {
					if($userForm.valid()){
						$('#button_add_user').attr('disabled', 'true');
						$userForm.unbind('submit');
					}
				});
				
				$(document).on('click', '#users_active_modal_cancel_btn', function() {
					$("#user_activeService").focus();
					
					$('html, body').animate({
					    scrollTop: ($('#user_activeService').offset().top)
					},500);
				});
				
				return false;
			}
		}
		
		
		/*
		 * https://github.com/jquery-validation/jquery-validation/issues/361
		 * 
		 * the method valid(), when there is a remote validation,
		 * is designed in a way that instead of return "pending"
		 * when the result of a remote validation is in progress,
		 * returns true. Could be that the result be false, so
		 * you can not trust on it.
		 */
		
		/*if(!$userForm.valid()){
			return false;
		}
		
		$('#button_add_user').attr('disabled', 'true');*/
		
		/*
		 * https://sharpeneddeveloper.blogspot.com.es/2015/07/jquery-validation-valid-lies.html
		 * 
		 * you have to raise the validation, and wait
		 * until all remotes be completed.
		 * 
		 * 
		 * https://jqueryvalidation.org/valid/
		 * https://jqueryvalidation.org/validate/
		 * 
		 * Also the plugin docs says that you first use validate()
		 * before use valid()
		 * 
		 */
		
		$userForm.valid(); // raises the validation
		
		var validationInterval = setInterval(function(){
			var validator = $userForm.validate(); // gets the current validator associated to the form 
			
			if(validator.pendingRequest === 0){ // checks if no pending remote validations
				clearInterval(validationInterval);
				
				if($userForm.valid()){
					$('#button_add_user').attr('disabled', 'true');
					$userForm.unbind('submit');
				}else{
					return false;
				}
			}
			
		}, 30); // checks every 30ms if remote validations have finished 
		
	});
});

// LIST USERS
$(function ($) {
	if ($('#users-list').length > 0 ) {
		$(document).on('ajax:success', '.delete_user', function (event, data, textStatus, jQXhr) {
			$(this).parents('tr').remove();
		});
	}
});
$(function ($) {
	if ($('#tabMain tr').length < 20 && $('#tabMain tr').length >0) {		
		$('#pagination-list-spinner').remove();		
	}
});

// EDIT USERS
$(function ($) {
	if ($('#edit_users').length > 0) {
		$('#types_all').on('change', function (event) {
			var $this = $(this),
				checked = $this.is(':checked');
			
			$(':checkbox[id^=types_]').not($this).prop('checked', checked).uniform('update');
		});
	}
});

//NEW-EDIT DEVICES
$(function ($){    	
	var ADL_ACTIVITIES = [0, 1, 2, 3, 4, 5, 6, 7, 20, 21],
		SAFETY_ACTIVITIES = [8, 9, 10, 11, 12, 13, 14, 99, 201],
		BED_SENSOR = 21,
		first, prevValue;
	
	function isAdl(value){
		return ADL_ACTIVITIES.indexOf(parseInt(value)) !== -1;
	};
	
	function isSafety(value){
		return SAFETY_ACTIVITIES.indexOf(parseInt(value)) !== -1;
	};
	
	function isBedSensor(value){
		return BED_SENSOR == parseInt(value);
	};
	
	function isNot(value){
		return (ADL_ACTIVITIES.concat(SAFETY_ACTIVITIES).indexOf(parseInt(value)) === -1) && (!isBedSensor(value));
	};
	
	$('body').on('modal:update', '.edit-device, .add-device', function () { 
		first = true; 
		prevValue = $('#device-type').val();
	});
    $(document).on('change', '#device-type', function(){
    	var value = $(this).val(),
    		$modal   = $(this).parents('.modal_content'),
    		$adlSelect = $modal.find('#device-number-adl'),
    		$adl       = $adlSelect.closest('li'),
    		$safetySelect = $modal.find('#device-number-safety'),
    		$safety       = $safetySelect.closest('li'),
    		$relatedSelect = $modal.find('#device-related'),
    		$related = $relatedSelect.closest('li'),
    		$ipdInput = $modal.find('#ipd'),
    		$ipd = $ipdInput.closest('li'),
    		$bedSensorSelect = $modal.find('#bed_sensor_device'),
    		$bedSensor = $bedSensorSelect.closest('li'),
    		$bedSensorAssociatedSelect = $modal.find('#bed_sensor_associated_device'),
    		$bedSensorAssociated = $bedSensorAssociatedSelect.closest('li');
    	
    	if ( isNot(value) ) { // new value not safety, not adl and not Bed Sensor
    		$adl.hide();
        	$bedSensor.hide();
        	$bedSensorAssociated.hide();
        	$safety.hide();
    	} else if (isAdl(value) ){
    		$adl.show();
        	$bedSensor.hide();
        	$bedSensorAssociated.hide();
            $safety.hide();
            $ipd.show();
            
            if (!first) {
            	var optionToSelect = $adlSelect.find('[value="' + $safetySelect.val() + '"]');
            	
            	(optionToSelect.length) ? optionToSelect.attr('selected', true) : $adlSelect.val("");
            	
            	$adlSelect.uniform('update').change();
            	$safetySelect.val("").uniform('update');
            }
    	} else if (isBedSensor(value)){
        	$bedSensor.show();
        	$bedSensorAssociated.show();
    		$adl.hide();
            $safety.hide();
            $ipd.show();
    	} else if (isSafety(value)){
    		$safety.show();
            $adl.hide();
        	$bedSensor.hide();
        	$bedSensorAssociated.hide();
            $ipd.hide();
            $ipdInput.removeAttr('checked');
            
            if (!first) {
            	var optionToSelect = $safetySelect.find('[value="' + $adlSelect.val() + '"]'); 
            	
            	(optionToSelect.length) ? optionToSelect.attr('selected', true) : $safetySelect.val(""); 
            	
            	$safetySelect.uniform('update').change();
            	$adlSelect.val("").uniform('update');
            }
    	}
    	
    	// Bathroom and can associate restroom
    	if ( value === '5' ) {
    		$related.show();
    	} else {
    		$relatedSelect.val("0").uniform('update').change();
    		$related.hide();
    	}
    	
    	if (first) first = false;
    	prevValue = value;
    	
    	//$modal.css('margin-top', -$modal.height() / 2);
    });
});

// adminNewUser.jsp setDefault
$(function($) {

	$('#user_activeDefaultPhoneNumber').change(function (event) {
		// Disable activeEmergencyPhone if activeDefaultPhoneNumber is checked
		var isChecked = $('#user_activeDefaultPhoneNumber').is(":checked");
		$('#user_activeEmergencyPhone').prop('disabled', isChecked);
		$('#user_activeEmergencyPhone').prop('required', !isChecked);
		
		if(isChecked && ($('label[for="user_activeEmergencyPhone"] > sup').length > 0)){
			$('label[for="user_activeEmergencyPhone"] > sup').remove();
		} else if (!isChecked){
			$('label[for="user_activeEmergencyPhone"]').append('<sup class="ui-mandatory">*</sup>');
		}

	});

	// If Creating a user, listen service type changes to override default
	// values and enableCall default values

	$('#user_service_type').change(function (event) {

		// Updates default allowResidentOverride and EnableCall

		var isChecked, isEnableCallChecked;
		if ($('#user_service_type').val() === 'EXPRESS') {
			isChecked = $.app.vendorConfigurationSettings.defaultAllowResidentOverrideByExpress;
			isEnableCallChecked = $.app.vendorConfigurationSettings.defaultOutgoingCallByExpress;
		} else if ($('#user_service_type').val() === 'ANALYTICS') {
			isChecked = $.app.vendorConfigurationSettings.defaultAllowResidentOverrideByAnalytics;
			isEnableCallChecked = $.app.vendorConfigurationSettings.defaultOutgoingCallByAnalytics;
		}

		$('#user_activeAllowResidentOverride').prop('checked', isChecked);
		var cl = isChecked ? "checked" : "checker";
		$('#user_activeAllowResidentOverride').parent().attr("class",cl);

		$('#user_activeOutgoingCall').prop('checked', isEnableCallChecked);
		cl = isEnableCallChecked ? "checked" : "checker";
		$('#user_activeOutgoingCall').parent().attr("class",cl);


	});
	
	// Field validations
	$('#user_activeEmergencyPhone,#user_email,#user_cellPhone,#user_phoneAtHome,#user_password,#user_confirmPassword,#user_dtmfCode,#user_simNumber,#user_landlineNumber').focusout(function() {
		$(this).valid();
	});

});
