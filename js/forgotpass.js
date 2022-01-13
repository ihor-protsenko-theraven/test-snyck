/**
 * 
 */

var $docRoot="/";

	
/* Forgot form */
$(document).on('pagecreate', '.forgot', function () {
	
	  var $form   = $(this).find('#forgot_form'); 
	     
	   
	  //TODO -> Validate must be loaded from validations.js but it is not working. Check it to avoid duplicate code 
	  //      This happens as well in changepassword.js 
	
	
	$form.validate();
	
	// Show error popup
	function showMessage (message) {
		var $popup = $('#popupMessage');
		
		if (message) {
			$popup.find('.warning').html("<p class='text-justify'>" + message + "</p>");
		}
		
		$('#splash_forgot').css('display','none');
		$('#h2_splash_forgot').css('display','none');
		$('#img_splash_forgot').css('margin-top','0%');
		
		$popup.popup('open', { transition: 'fade' });
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
	}

	
	// Form success handler
	function ajaxSuccessHandler (data, textStatus, jQxhr) {
		console.log(data);
		if (data != null) {
			
			if (data.retCode == 0) {
				showMessage(I18n.t('forgot.ok_message'));
        	} else {
        		showMessage(I18n.t('forgot.error_message'));
        	};  
        	
		} else {
			// put the correct message and show the dialog.
			showMessage(I18n.t('forgot.reset.error_message'));
		}
	}
	
	$form.attr('novalidate', 'novalidate');
	
	// Form submit via ajax
	$form.submit(function (event) {

		  
	    if (!event.isDefaultPrevented())
	    {    
	      if (!$form.valid())  return false;
	      // Show Splash
	        
	      $('#contentForgot').hide();
	      $('#splash_forgot').show();
	      $('#h2_splash_forgot').css('display','none');
	      $('#img_splash_forgot').css('margin-top','10%');
	      
	       $.post('processForgot', $form.serializeArray(), ajaxSuccessHandler, 'json').fail(ajaxErrorHandler); 
	    }
	      // prevent normal send of form
	      return false;
	  });
});

/* SPLASH SCREEN */
$(document).on('pageshow', '.forgot', function (event, ui) {
	var TIMING     = 0,
		$page      = $(this),
		$splash    = $page.find('#splash'),
		$form     = $(this).find('#forgot_form')
		$pwd      = $form.find('#pwd_user');
	
	$('#splash_forgot').css('display','none');
	$('#h2_splash_forgot').css('display','none');
	$('#img_splash_forgot').css('margin-top','0%');
	
	checkScreenSize();
	isMobile();
	

		
});

$(document).on('click', 'a[data-ajax=false]', function (event) {
	window.location = $(this).attr('href');
	return false;
});


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

