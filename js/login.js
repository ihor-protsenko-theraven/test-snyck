
import DOMPurify from 'dompurify';
var $docRoot = "/";

var $postmateAvailable = isPostmateAvailable();

// For FIT/External Systems login requests
// false: receive the handshake and store login data. when come again to login
// page, check if login data is stored and if yes fires login.
// true: receive the handshake and fires login.
var $directExternalLogin = false;
var $loginForced = false;

function isPostmateAvailable() {

	if ((navigator.userAgent.match(/IEMobile/i) == null)
			&& (navigator.userAgent.match(/Trident/) == null)) {
		return true;
	} else {
		return false;
	}
}

if ($postmateAvailable) {

	var $handshakePostmate = new Postmate.Model({
		// Expose your model to the Parent. Property values may be functions,
		// promises, or regular values
		id : "adl web app"
	});
} else {
	console.warn("postmate functions are not available for this browser")
}

$(document)
		.on(
				'pagecreate',
				'.login',
				function() {
					var $form = $(this).find('#login_form'), $user = $form
							.find('#user_name'), $pwd = $form.find('#pwd_user'), $remember = $form
							.find('#remember');

					// Delete Login related SessionStorage keys
					sessionStorage.removeItem("isAboutToExpireSession"); // Sesion
																			// about
																			// to
																			// expire
					sessionStorage.removeItem("isMobileSession"); // Web Page
																	// running
																	// in a
																	// mobile
																	// device
					sessionStorage.removeItem("isExpiredSession"); // Session
																	// expired
					sessionStorage.removeItem("isMobileMenuSession"); // Accesing
																		// from
																		// Change
																		// Password
																		// session
																		// in
																		// mob.
																		// app.

					// Integration with FIT/ExternalSystems. Check is has to
					// redirect parent
					if ($form.attr('data-user-log-state') == "login-state") {
						console.log("redirect");
					}

					// FIXME: add a service to interval alerts
					// si habia interval lo borramos
					if (window['callGetAlerts'] != undefined)
						window.clearInterval(callGetAlerts);

					// show disabled cookies popup
					setTimeout(function() {
						if (!are_cookies_enabled()) {
							$('#noCookies').popup('open', {
								transition : 'fade'
							});
						}
					}, 500);

					// if has remember cookie sets to the form
					if ($.cookie("remember")) {
						$user.val($.cookie("remember"));
						$remember.attr('checked', true);
					}

					function validateInput(event) {
						var $input = $(this).addClass('changed');

						if (/^\s*$/.test(this.value) && $input.attr('required')) {
							if (!$input.data('qtip')) {
								$input
										.addClass('invalid')
										.qtip(
												{
													content : {
														text : I18n
																.t('validations.required')
													},
													position : {
														my : 'bottom right',
														at : 'top right',
														container : $('#login_form')
													},
													show : {
														event : 'focus',
														solo : true
													},
													hide : {
														event : 'blur'
													},
													style : {
														classes : 'qtip-red qtip-rounded qtip-shadow'
													}
												});
							}

							if (event) {
								$input.qtip('show');
							}

							return false;
						} else {
							$input.qtip('destroy').removeClass('invalid');

							return true;
						}
					}

					// form validations
					function validateForm(autofocus) {
						var valid = true;

						$('input').each(function() {
							if (!validateInput.call(this))
								valid = false;
						});

						if (autofocus && !valid) {
							$('input.invalid:first').focus();
						}

						/*
						 * if (!valid) {
						 * $form.find('[type=submit]').attr('disabled',
						 * 'disabled').addClass('disabled'); } else {
						 * $form.find('[type=submit]').removeAttr('disabled').removeClass('disabled'); }
						 */

						return valid;
					}

					$(document).on('keyup', 'input.changed', validateInput);
					$(document).on('blur', 'input.changed', validateInput);

					// Show Info (or error) popups

					function showPopUp(formToOpen, message) {
						var $popup = $(formToOpen);

						if (message) {
							$popup.find('.warning').html(
									"<span class='icon icon-warning'></span>"
											+ message);
						}

						$('#splash').css('display', 'none');
						$('#h2_splash').css('display', 'none');
						$('#img_splash').css('margin-top', '0%');

						$popup.popup('open', {
							transition : 'fade'
						});
					}
					
					$(document)
							.on('pageshow','.login',
									function(event, ui) {
										var TIMING = 0, $page = $(this), $splash = $page
												.find('#splash'), $form = $(this).find(
												'#login_form')
										$pwd = $form.find('#pwd_user');

										checkScreenSize();
										isMobile();

										// EXTERNAL SYSTEM/FIT INTEGRATION
										if ($loginForced == false) {
											var redirectURL;

											if (sessionStorage.getItem('externalUserLogged') == 'true') {

												if (sessionStorage.getItem('userLoggedOut') != null) {

													if (sessionStorage.getItem('userLoggedOut') == 'true') {
														if (sessionStorage
																.getItem('logoutRedirectUrl') != null) {
															redirectURL = sessionStorage
																	.getItem('logoutRedirectUrl');
														}
													}

												} else if (sessionStorage.getItem('userMoveFIT') != null) {

													if (sessionStorage.getItem('userMoveFIT') == 'true') {
														if (sessionStorage.getItem('moveToFitUrl') != null) {
															redirectURL = sessionStorage
																	.getItem('moveToFitUrl');
														}
													}

												} else { // session timeout

													if (window.location.search.length > 0) {
														if (window.location.search
																.indexOf('timeOut') !== -1) {

															if (sessionStorage
																	.getItem('caregiverType') == "MASTER") {
																redirectURL = sessionStorage
																		.getItem("sessionTimeoutUrl");
															} else {
																// window.close();
																redirectURL = sessionStorage
																		.getItem("sessionTimeoutUrl");
															}
														}
													}
												}
											}

											if (redirectURL != null) {
												window.location = redirectURL;
											} else {
												$('#contentLogin').show();

												// Delete sessions
												sessionStorage.clear();
	
												// Delete pwd always
												$(window).on('load',function() {
													$pwd.val('');
												});
	
												if ($page.find('#splash').length > 0
														&& (window.location.href.indexOf("login") <= -1)) {
													var temp = DOMPurify.sanitize( window.location.origin);
													var url = DOMPurify.sanitize(temp + window.location.pathname);
													document.location.replace(url);
													console.log('splash');
												} else {
													console.log('no splash');
												}
												
												if (!!localStorage.getItem("autologin-due-to-pwd-change")
														&& !!localStorage.getItem("u")
														&& !!localStorage.getItem("p")) {
													const f = {
														'user' : localStorage.getItem("u"),
														'pwd' : localStorage.getItem("p")
													}
													localStorage.removeItem("u");
													localStorage.removeItem("p");
													localStorage.removeItem("autologin-due-to-pwd-change");
													// Send the form
													$('#splash').show();
													$('#h2_splash').css('display', 'none');
													$('#img_splash').css('margin-top', '10%');
													$.post('processLogin', f, ajaxSuccessHandler, 'json').fail(
															ajaxErrorHandler);
												} else {
													$('#contentLogin').show();
													setTimeout(function() {
														$splash.fadeOut(0);
													}, 0);
												}
											}
										}
									});

					// AJAX error Handling function
					function ajaxErrorHandler(x, settings, exception) {
						var message, statusErrorMap = {
							'400' : "Server content invalid",
							'401' : "unauthorized access",
							'403' : "Resource is forbbiden",
							'404' : "Page not found",
							'500' : "Server Error",
							'503' : "Service unavailable"
						};

						if (x.status) {

							// EIC15-2680 - AcceptEula - Applicative Error or
							// System Error after 3 retries
							if (x.status == 505) {
								// the web application should logout and return
								// to the Login screen.
								window.location.href = $.app.rootPath
										+ '/login';
								console
										.log("Applicative error in the acceptEula request");
								return;
							}
							message = statusErrorMap[x.status];

							if (!message) {
								switch (exception) {
								case 'error':
									message = "Unknown error\n";
									break;
								case 'parseerror':
									message = "Error parsing JSON response";
									break;
								case 'timeout':
									message = "Request time out.";
									break;
								case 'abort':
									message = "Request was aborted by server";
									break;
								default:
									message = "Unknown error<br/>"
											+ settings.url;
									;
									break;
								}
							}
						} else {
							// Server is not responding
							message = "Connection error. <br/>Try again later ";
						}

						showPopUp('#popup_login_fails', message);
					}

					// Form success handler
					function ajaxSuccessHandler(data, textStatus, jQxhr) {
						console.log(data);

						$rootDoc = data.documentRoot;

						if (data != null) {

							// data.code == 0 -> Succesful Login. Needs to check
							// isAboutToExpire
							// data.code == 1 -> Bad credentials exception
							// data.code == 2 -> Password Expired. Need to check
							// Reason and GetPolicy

							if (data.code == 0) {

								// Login correct. data.payload should not be
								// null
								var passwordExpirationData = data.payload.passwordExpirationData;
								var eula = data.payload.eula;
								console.log("Eula: ", eula);
								var showEula = !eula.acceptanceDate;

								var isAboutToExpire = passwordExpirationData.passwordExpirationDays <= passwordExpirationData.aboutToExpireDays

								if ($remember.is(':checked')) {
									$.cookie("remember", $user.val(), {
										expires : 10
									});
								}

								$(function() {
									var expiredSection = document
											.getElementById('expiredsection').innerHTML;
									expiredSection = expiredSection
											.replace(
													"XXX",
													passwordExpirationData.passwordExpirationDays);
									document.getElementById("expiredsection").innerHTML = expiredSection;
								});

								// If everything works fine, next URL will
								// depend on mobile version or not.
								// If it is mobile app (activate mobile view in
								// app), we activate the flag
								// for changepassword form.

								if ((data.forceMobileMode !== 'undefined')
										&& (data.forceMobileMode == true)) {
									nextURLOk = $rootDoc + '/home/main';
									sessionStorage.setItem("isMobileSession",
											isMobile());
									sessionStorage.setItem(
											"isMobileMenuSession", true);
								} else {
									nextURLOk = $rootDoc
											+ '/admin/residents_in_alarm';
								}

								if (isAboutToExpire) {

									sessionStorage.setItem(
											"isAboutToExpireSession", true);
									sessionStorage.setItem("isMobileSession",
											isMobile());

									if ((data.forceMobileMode !== 'undefined')
											&& (data.forceMobileMode == true)) {
										nextURLOk = $rootDoc + '/home/main';
										nextURLCancel = $rootDoc + '/home/main';
										nextURLChange = $rootDoc
												+ '/home/changePassword';

										sessionStorage.setItem(
												"isMobileMenuSession", true);
										sessionStorage.setItem(
												"isMobileSession", isMobile());

									} else {
										nextURLOk = $rootDoc
												+ '/admin/residents_in_alarm';
										nextURLCancel = $rootDoc
												+ '/admin/residents_in_alarm';
										nextURLChange = $rootDoc
												+ '/admin/changePassword';
									}

									$('#btn_cancelAboutToExpire').attr(
											"onclick",
											"location.href='" + nextURLCancel
													+ "'; ");
									$('#btn_ChangePass').attr(
											"onclick",
											"location.href='" + nextURLChange
													+ "'; ");
								}

								if (showEula) {
									// Hide touchable and show Eula section
									var height = $(window).height()
											- $('header').outerHeight(true)
											- $('#acceptEula')
													.outerHeight(true)
											- $('h3.section_title')
													.outerHeight(true) - 20;
									$('#eulaIframe').height(height);
									$('#eula').show();
									$('#contentLogin').hide();
									$('#splash').hide();
									$('#eulaIframe').attr('src', eula.link);
									$('#declineEula').click(
											function() {
												window.location.href = $rootDoc
														+ "/login";
											});
									$('#acceptEula')
											.click(
													function() {
														// Tries to accept eula
														$
																.ajax({
																	traditional : true,
																	type : "POST",
																	url : 'acceptEula',
																	data : eula.version,
																	contentType : 'text/plain',
																	success : function() {
																		if (isAboutToExpire) {
																			$(
																					'#eula')
																					.hide();
																			showPopUp('#popup_abouttoexpire');
																		} else {
																			window.location.href = nextURLOk;
																		}
																	},
																	error : ajaxErrorHandler
																});

													});

								} else {
									if (isAboutToExpire) {
										showPopUp('#popup_abouttoexpire');
										$('#contentLogin').hide();
										$('#splash').hide();
									} else {
										window.location.href = nextURLOk;
									}
								}
							}

							else if (data.code == 1) {

								showPopUp('#popup_login_fails');
								$pwd.val("");
							} else if (data.code == 2) {
								sessionStorage
										.setItem("isExpiredSession", true);
								sessionStorage.setItem("isMobileSession",
										isMobile());
								sessionStorage.setItem("minimumLengthPolicy",
										data.payload.passwordMinimumLength);
								sessionStorage.setItem("maximumLengthPolicy",
										data.payload.passwordMaximumLength);
								sessionStorage.setItem(
										"charGroupsMinimumPolicy",
										data.payload.charGroupsMinimum);

								nextURLChange = $rootDoc
										+ '/ADL/changePassword'

								$('#btn_accessChangeForm').attr(
										"onclick",
										"location.href='" + nextURLChange
												+ "'; ");

								showPopUp('#popup_expired');
								$pwd.val("");
							} else {
								showPopUp('#popup_login_fails');
								$pwd.val("");
							}

						} else {
							// put the correct message and show the dialog.
							showPopUp('#popup_login_fails');
							$pwd.val("");
						}
					}

					$form.attr('novalidate', 'novalidate');

					// Form submit via ajax
					$form.submit(function(event) {
						// if form is not valid prevent send
						if (!validateForm(true)) {
							event.preventDefault();
							return false;
						}

						// If remember me is not checked delete browser cookie
						// independently if the form is send
						if (!$remember.is(':checked')) {
							$.removeCookie("remember");
						}

						$('#splash').show();
						$('#h2_splash').css('display', 'none');
						$('#img_splash').css('margin-top', '10%');

						// send the ajax form
						$.post('processLogin', $form.serializeArray(),
								ajaxSuccessHandler, 'json').fail(
								ajaxErrorHandler);

						// prevent normal send of form
						return false;
					});

					/* Login from FIT / external system */

					if ($postmateAvailable) {
						if ($directExternalLogin == true) {
							window.addEventListener("message", externalLogin,
									false);
						} else {
							window.addEventListener("message",
									externalHandshakeRequest, false);
						}
					}

					/* Login from FIT */

					/*
					 * if ($postmateAvailable){
					 * window.addEventListener("message", externalLogin, false); }
					 */

					function externalLogin(event) {

						var trustedLoginRequestSitesArray = trustedLoginRequestSites
								.split(",");

						if ($.inArray(event.origin,
								trustedLoginRequestSitesArray) != -1) {

							var model = event.data.model;

							if (model.user && model.password && model.token
									&& model.logoutRedirectUrl
									&& model.sessionTimeoutUrl
									&& model.moveToFitUrl) {

								$handshakePostmate
										.then(function(parent) {
											parent
													.emit('handshake-success',
															'You are a trusted site. Processing login request.');
										});

								sessionStorage.setItem('logoutRedirectUrl',
										model.logoutRedirectUrl);
								sessionStorage.setItem('sessionTimeoutUrl',
										model.sessionTimeoutUrl);
								sessionStorage.setItem('moveToFitUrl',
										model.moveToFitUrl);

								$('#user_name').val(model.user);
								$('#pwd_user').val(model.password);

								$
										.post(
												'processPreLogin',
												"user=" + model.user + "&pwd="
														+ model.password,
												function(data, textStatus,
														jQxhr) {

													if (data != null) {
														if (data.code == 0) {
															$
																	.post(
																			'processSharedToken',
																			"token="
																					+ model.token,
																			ajaxSuccessHandler,
																			'json')
																	.fail(
																			ajaxErrorHandler);
														}
													}

												}, 'json').error(
												ajaxErrorHandler);
							} else {

								$handshakePostmate
										.then(function(parent) {
											parent
													.emit(
															'handshake-data-error',
															'Missing model fields. Expected user, password, token, logoutRedirectUrl, sessionTimeoutUrl and moveToFitUrl fields.');
										});
							}

						} else {
							$handshakePostmate.then(function(parent) {
								parent.emit('handshake-origin-error',
										'You are not a trusted site.');
							});
						}
					}

					/*
					 * if ($postmateAvailable){
					 * window.addEventListener("message",
					 * externalHandshakeRequest, false); }
					 */

					function externalHandshakeRequest(event) {

						var trustedLoginRequestSitesArray = trustedLoginRequestSites
								.split(",");

						if ($.inArray(event.origin,
								trustedLoginRequestSitesArray) != -1) {

							var model = event.data.model;

							if (model.user && model.password && model.token
									&& model.logoutRedirectUrl
									&& model.sessionTimeoutUrl
									&& model.moveToFitUrl) {

								$handshakePostmate
										.then(function(parent) {
											parent
													.emit('handshake-success',
															'You are a trusted site. Storing login data.');
										});

								sessionStorage.setItem('user', model.user);
								sessionStorage.setItem('password',
										model.password);
								sessionStorage.setItem('token', model.token);

								sessionStorage.setItem('logoutRedirectUrl',
										model.logoutRedirectUrl);
								sessionStorage.setItem('sessionTimeoutUrl',
										model.sessionTimeoutUrl);
								sessionStorage.setItem('moveToFitUrl',
										model.moveToFitUrl);

							} else {

								$handshakePostmate
										.then(function(parent) {
											parent
													.emit(
															'handshake-data-error',
															'Missing model fields. Expected user, password, token, logoutRedirectUrl, sessionTimeoutUrl and moveToFitUrl fields.');
										});
							}

						} else {
							try {
								$handshakePostmate.then(function(parent) {
									parent.emit('handshake-origin-error',
										'You are not a trusted site.');
								});
							} catch(e) {
							    console.log(e);
							};
						}
					}

					function forceLogin() {

						if (sessionStorage.getItem('user')
								&& sessionStorage.getItem('password')
								&& sessionStorage.getItem('token')) {

							var user = sessionStorage.getItem('user');
							var password = sessionStorage.getItem('password');
							var token = sessionStorage.getItem('token');

							sessionStorage.removeItem('user');
							sessionStorage.removeItem('password');
							sessionStorage.removeItem('token');

							$('#user_name').val(user);
							$('#pwd_user').val(password);

							$.post(
									'processPreLogin',
									"user=" + user + "&pwd=" + password,
									function(data, textStatus, jQxhr) {

										if (data != null) {
											if (data.code == 0) {
												$.post('processSharedToken',
														"token=" + token,
														ajaxSuccessHandler,
														'json').fail(
														ajaxErrorHandler);
											}
										}

									}, 'json').error(ajaxErrorHandler);

						} else {
							console
									.error('Missing storage items. Expected user, password and token');
							return;
						}

					}

					// Integration with FIT/ExternalSystems
					if ($directExternalLogin == false) {
						if (are_cookies_enabled()) {
							if (sessionStorage.getItem('user')
									&& sessionStorage.getItem('password')
									&& sessionStorage.getItem('token')) {
								forceLogin();
								$loginForced = true;
							}
						} else {
							$loginForced = true;
						}
					}

				});

$(document).on('click', 'a[data-ajax=false]', function(event) {
	window.location = $(this).attr('href');
	return false;
});

function isMobile() {

	var isMobile = {
		Android : function() {
			return navigator.userAgent.match(/Android/i);
		},
		BlackBerry : function() {
			return navigator.userAgent.match(/BlackBerry/i);
		},
		iOS : function() {
			return navigator.userAgent.match(/iPhone|iPad|iPod/i);
		},
		Opera : function() {
			return navigator.userAgent.match(/Opera Mini/i);
		},
		Windows : function() {
			return navigator.userAgent.match(/IEMobile/i);
		}
	};

	if ((isMobile.Android()) || (isMobile.BlackBerry()) || (isMobile.iOS())
			|| (isMobile.Opera()) || (isMobile.Windows())) {
		sessionStorage.setItem('forceMobileMode', data.forceMobileMode);
		return true;
	} else {
		$("#orientation_change").remove();
		return false;
	}

}

// Check if cookies are enabled
function are_cookies_enabled() {
	var cookieEnabled = (navigator.cookieEnabled) ? true : false;

	if (typeof navigator.cookieEnabled == "undefined" && !cookieEnabled) {
		document.cookie = "testcookie";
		cookieEnabled = (document.cookie.indexOf("testcookie") != -1) ? true
				: false;
	}
	return (cookieEnabled);
}

/*
 * function checkScreenSize(){
 * 
 * if(isMobile()){
 * 
 * if(screen.height < screen.width){ $("#orientation_change").css('display',
 * 'block'); } else if(screen.height > screen.width){
 * $("#orientation_change").css('display', 'none'); } } }
 */

function checkScreenSize() {

	var isMobile = {
		Android : function() {
			return navigator.userAgent.match(/Android/i);
		},
		BlackBerry : function() {
			return navigator.userAgent.match(/BlackBerry/i);
		},
		iOS : function() {
			return navigator.userAgent.match(/iPhone|iPad|iPod/i);
		},
		Opera : function() {
			return navigator.userAgent.match(/Opera Mini/i);
		},
		Windows : function() {
			return navigator.userAgent.match(/IEMobile/i);
		}
	};

	setTimeout(function() {
		if (isMobile.iOS()) {

			if ($(window).height() < $(window).width()) {
				$("#orientation_change").css('display', 'block');
			} else if ($(window).height() > $(window).width()) {
				$("#orientation_change").css('display', 'none');
			}
		} else if (isMobile.Android() || isMobile.BlackBerry()
				|| isMobile.Opera() || isMobile.Windows()) {
			if (screen.height < screen.width) {
				$("#orientation_change").css('display', 'block');
			} else if (screen.height > screen.width) {
				$("#orientation_change").css('display', 'none');
			}
		}
	}, 500);
}

$(window).on('resize', checkScreenSize);
