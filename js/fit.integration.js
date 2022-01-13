// LOGIN STATUS FOR EXTERNAL SYSTEM (FIT)

$('#logoutLink').on('click', function(){
	if($('#logoutLink').attr('data-login-state')){
		sessionStorage.setItem($('#logoutLink').attr('data-login-state'), 'true');
	}
});

$('#moveFitLink').on('click', function(){
	if($('#moveFitLink').attr('data-login-state')){
		sessionStorage.setItem($('#moveFitLink').attr('data-login-state'), 'true');
	}
});

$(function ($) {	
	if($('#externalUserLogged').attr('data-is-external-user')){
		sessionStorage.setItem("externalUserLogged", $('#externalUserLogged').attr('data-is-external-user'));
		sessionStorage.setItem("caregiverType", $('#caregiverType').attr('data-caregiver-type'));
		// Next parameters are needed for a correct routing in Change Password popup "OK" option,
		// as far as the onclick URL is finished via Javascript comparing these values.
		sessionStorage.setItem("isMobileSession","true");
		sessionStorage.setItem("isMobileMenuSession","true");
	}
	
});