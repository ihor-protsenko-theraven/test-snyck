/**
 * Description: Javascript file to support Change Password (anonymous in RootUrl/ADL/ChangePassword 
 */

/**
 * 
 */

	
/* Forgot form */
$(document).on('pagecreate', '.change', function () {
	
	var $form     = $(this).find('#changepassword_form');
	
      //ESC17-5989: Hide the Footer, as if the virtual keyboard is enabled there is no enough space
      $('#main-footer').hide();
	
	 // If tooltip doesn´t have the password policy parameters, replace wildcards 
	  // with session variables from  Session Storage 
	     
	  var $title = $("#policies_tooltip").attr("title"); 
	  $title = $title.replace("{0}",sessionStorage.getItem("minimumLengthPolicy")); 
	  $title = $title.replace("{1}",sessionStorage.getItem("maximumLengthPolicy")); 
	  $title = $title.replace("{2}",sessionStorage.getItem("charGroupsMinimumPolicy"));   
	  $("#policies_tooltip").attr("title",$title); 
	 
	  // 
	  var $minLength = $("#newPassword").attr("minLength"); 
	  $("#newPassword").attr("minLength",$minLength.replace("{0}",sessionStorage.getItem("minimumLengthPolicy"))); 
	  var $maxLength = $("#newPassword").attr("maxLength"); 
	  $("#newPassword").attr("maxLength",$maxLength.replace("{1}",sessionStorage.getItem("maximumLengthPolicy"))); 
	  $("#confirmPassword").attr("maxLength",$maxLength.replace("{1}",sessionStorage.getItem("maximumLengthPolicy"))); 
	  var $minBlocks = $("#newPassword").attr("data-rule-passwordADL"); 
	  $("#newPassword").attr("data-rule-passwordADL",$minBlocks.replace("{2}",sessionStorage.getItem("charGroupsMinimumPolicy")));
	  
	  // Marker for the login page to try autologin 
	  localStorage.removeItem("autologin-due-to-pwd-change");
	  localStorage.removeItem("u");
	  localStorage.removeItem("p");
	 
	 // State machine for Change Password form 
	  var isExpiredSession = sessionStorage.getItem("isExpiredSession");
	  var isMobileSession = sessionStorage.getItem("isMobileSession");
	  var isAboutToExpireSession = sessionStorage.getItem("isAboutToExpireSession");
	  var isMobileMenuSession = sessionStorage.getItem("isMobileMenuSession");
	  

	  var $URLCancel = $('#button_cancel').attr('onclick');

	  if ((typeof isExpiredSession !== 'undefined') && (isExpiredSession == "true"))
	  {
		  $URLCancel = $URLCancel + "/login'";
	  }
	  else if ((typeof isAboutToExpireSession !== 'undefined') && (isAboutToExpireSession == "true"))
	  {
		  if ((typeof isMobileMenuSession !== 'undefined') && (isMobileMenuSession == "true"))
		  {		
			  $URLCancel = $URLCancel + "/home/main'";
		  }
		  else
		  {
			  $URLCancel = $URLCancel + "/admin/residents_in_alarm'";
		  }
	  }
	  else if ((typeof isMobileMenuSession !== 'undefined') && (isMobileMenuSession == "true"))
	  {
		  $URLCancel = $URLCancel + "/home/main'";
		// Hide Header as far as we are logged.
		  $('#header_logo').hide();
	  }
	  
	  $('#button_cancel').attr('onclick',$URLCancel);
	  
	  
	  // EIC15-2688 Init containers as it is not doing it from mobile menu option
	  // TODO: Check all the script files include in JSP file and see how we can
	  //       import validations and tooltip styles to avoid the code below
	  
	  var $tips = $('#changepassword_form').find('[data-hastip]');
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

	  
	$form.validate({
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
	
	// Show error popup
	function showMessage (message) {
		var $popup = $('#popupMessage');
		
		
		
		if (message) {
			$popup.find('.warning').html("<p class='text-justify'>" + message + "</p>");
		}
		
		$('#splash').css('display','none');
		$('#h2_splash').css('display','none');
		$('#img_splash').css('margin-top','0%');
		
		$popup.popup('open', { transition: 'fade' });
	}
	
	function hideMessage()
	{
		$popup.hide();
	}
	
	// AJAX error Handling function		 
	function ajaxErrorHandler(x, settings, exception){
		var message,
			statusErrorMap = {
				'400' : "Server content invalid",
				'401' : "unauthorized access",
				'403' : "Resource is forbbiden",
				'404' : "Page not found",
				'500' : "Server Error",
				'503' : "Service unavailable"
			};
		
		if (x.status) {
			message = statusErrorMap[x.status];
			
			if (!message){
				switch (exception) {
					case 'error':
						message = "Unknown error\n";
						break;
					case 'parseerror':
						message = "Error parsing JSON response";
						break;
					case 'timeout':
						message ="Request time out.";
						break;
					case 'abort':
						message ="Request was aborted by server";
						break;
					default:
						message = "Unknown error<br/>" + settings.url ;;
						break;
				}
			}
		} else {
			// Server is not responding
			message ="Connection error. <br/>Try again later ";
		}
		
		showMessage(message);
		
		// Successful changepasswords ends in autologin. Remove from memory for every case
		localStorage.removeItem('u');
		localStorage.removeItem('p');
	}

	
	// Form success handler
	function ajaxSuccessHandler (data, textStatus, jQxhr) {
		console.log(data);
 
		
		 var $URLPopup = $('#button_popup').attr('onclick');
		
		if (data != null) {
					
			if (data.retCode == 0){
				  if ((typeof isExpiredSession !== 'undefined') && (isExpiredSession == "true"))
				  {
					  localStorage.setItem("autologin-due-to-pwd-change", true);
					  $URLPopup = $URLPopup  + "/login'";
				  }
				  else if ((typeof isAboutToExpireSession !== 'undefined') && (isAboutToExpireSession == "true"))
				  {
					  if ((typeof isMobileSession !== 'undefined') && (isMobileSession == "true"))
					  {
						  $URLPopup = $URLPopup  + "/home/main'";
					  }
					  else
					  {
						  $URLPopup = $URLPopup  + "/admin/residents_in_alarm'";
					  }
				  }
				  else if ((typeof isMobileMenuSession !== 'undefined') && (isMobileMenuSession == "true"))
				  {
					  $URLPopup = $URLPopup  + "/home/main'";
					  
				  }
        	}
			else
			{
				  if ((typeof isExpiredSession !== 'undefined') && (isExpiredSession == "true"))
				  {
					  $URLPopup = $URLPopup  + "/changePassword'";
				  }
				  else if ((typeof isAboutToExpireSession !== 'undefined') && (isAboutToExpireSession == "true"))
				  {
					  if ((typeof isMobileMenuSession !== 'undefined') && (isMobileMenuSession == "true"))
					  {
						  $URLPopup = $URLPopup  + "/home/changePassword'";
					  }
					  else
					  {
						  $URLPopup = $URLPopup  + "/admin/changePassword'";
					  }
				  }
				  else if ((typeof isMobileMenuSession !== 'undefined') && (isMobileMenuSession == "true"))
				  {
					  $URLPopup = $URLPopup  + "/home/changePassword'";
				  }
			}
		} else {
			 $URLPopup = $URLPopup  + "/login'";
			data.message = 'forgot.reset.error_message';
		}
		
		 $('#button_popup').attr('onclick',$URLPopup);
		
		showMessage( I18n.t(data.message));
	};  
	
	
	$form.attr('novalidate', 'novalidate');
	
	// Form submit via ajax
	$form.submit(function (event) {
		   if (!event.isDefaultPrevented()) 
		    {     
		      if (!$form.valid())  return false; 
		      const f = $form.serializeArray();
		      localStorage.setItem("u", f[0].value);
		      localStorage.setItem("p", f[2].value);
			  $.post('changePassword', $form.serializeArray(), ajaxSuccessHandler, 'json').fail(ajaxErrorHandler);
		    }
			  
			return false;
	});
	
});


function deleteSessionStorage()
{
	 // Delete session storage.
	  sessionStorage.removeItem("isAboutToExpireSession"); // Sesion about to expire
	  sessionStorage.removeItem("isExpiredSession");		 // Session expired
	  sessionStorage.removeItem("isMobileMenuSession");    // Accesing from Change Password session in mob. app.
	  sessionStorage.removeItem("RootDoc");    // Accesing from Change Password session in mob. app.	
}

function isMobile(){

		var isMobile = {
		    Android: function() {
		        return navigator.userAgent.match(/Android/i);
		    },
		    BlackBerry: function() {
		        return navigator.userAgent.match(/BlackBerry/i);
		    },
		    iOS: function() {
		        return navigator.userAgent.match(/iPhone|iPad|iPod/i);
		    },
		    Opera: function() {
		        return navigator.userAgent.match(/Opera Mini/i);
		    },
		    Windows: function() {
		        return navigator.userAgent.match(/IEMobile/i);
		    }
		 };		

		 if ((isMobile.Android())|| (isMobile.BlackBerry()) || (isMobile.iOS()) ||  (isMobile.Opera()) || (isMobile.Windows())){
			 return true;
		 }else{
			 $("#orientation_change" ).remove();
			 return false;
		 }
}



function checkScreenSize(){

 		var isMobile = {
 		    Android: function() {
 		        return navigator.userAgent.match(/Android/i);
 		    },
 		    BlackBerry: function() {
 		        return navigator.userAgent.match(/BlackBerry/i);
 		    },
 		    iOS: function() {
 		        return navigator.userAgent.match(/iPhone|iPad|iPod/i);
 		    },
 		    Opera: function() {
 		        return navigator.userAgent.match(/Opera Mini/i);
 		    },
 		    Windows: function() {
 		        return navigator.userAgent.match(/IEMobile/i);
 		    }
 		 };		

 		setTimeout(function(){  
 					if (isMobile.iOS()){
    	
 						if($(window).height() < $(window).width()){
 								$("#orientation_change").css('display', 'block');	
 						}
 						else if($(window).height() > $(window).width()){
 								$("#orientation_change").css('display', 'none');
 						}
 					}
 					else if(isMobile.Android() || isMobile.BlackBerry() || isMobile.Opera() || isMobile.Windows()){
 						if(screen.height < screen.width){
 								$("#orientation_change").css('display', 'block');	
 						}
 						else if(screen.height > screen.width){
 								$("#orientation_change").css('display', 'none');
 						}
 					} 
 		}, 500); 
}


$(window).on('resize', checkScreenSize);

