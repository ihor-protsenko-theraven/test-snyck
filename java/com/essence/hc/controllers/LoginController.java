package com.essence.hc.controllers;

import java.io.IOException;
import java.util.Locale;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.encoding.PasswordEncoder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.servlet.i18n.SessionLocaleResolver;
import org.springframework.web.util.WebUtils;

import com.essence.hc.eil.exceptions.AuthenticationException;
import com.essence.hc.exceptions.IncorrectEulaVersionException;
import com.essence.hc.exceptions.NewPasswordInvalidByHistoryException;
import com.essence.hc.exceptions.NewPasswordInvalidByPolicyException;
import com.essence.hc.exceptions.PasswordInvalidException;
import com.essence.hc.model.DateFormatHelper;
import com.essence.hc.model.Eula;
import com.essence.hc.model.Language;
import com.essence.hc.model.PasswordExpirationData;
import com.essence.hc.model.PasswordPolicy;
import com.essence.hc.model.ThirdPartyUser;
import com.essence.hc.model.User;
import com.essence.hc.model.User.CaregiverType;
import com.essence.hc.model.User.UserType;
import com.essence.hc.service.PasswordPolicyService;
import com.essence.hc.service.UserService;
import com.essence.security.SecurityService;
import com.essence.security.impl.HCFitUserDetailsProvider;
import com.essence.security.util.HCSecurityAuthotizationUtil;

/*
import nl.captcha.Captcha;
import nl.captcha.backgrounds.FlatColorBackgroundProducer;
import nl.captcha.gimpy.DropShadowGimpyRenderer;
import nl.captcha.noise.CurvedLineNoiseProducer;
import nl.captcha.noise.StraightLineNoiseProducer;
import nl.captcha.servlet.CaptchaServletUtil;
import nl.captcha.text.producer.DefaultTextProducer;
import nl.captcha.text.renderer.DefaultWordRenderer;
*/

@Controller
@SessionAttributes({ "retries", "CAPTCHA_OBJECT" }) // Session var to count
													// failed login retries and
													// captcha object
@RequestMapping("/")
public class LoginController {

	@Autowired
	private UserService userService;
	@Autowired
	private SecurityService securityService;
	@Autowired
	private PasswordEncoder passwordEncoder;

	@Autowired
	private PasswordPolicyService passwordPolicyService;

	@Autowired
	private HCFitUserDetailsProvider hcFitUserDetailsProvider;

	private HCSecurityAuthotizationUtil authorizationUtil = new HCSecurityAuthotizationUtil();

	// captcha dimensions
	// private static int _width = 200;
	// private static int _height = 50;
	// private static long _ttl =1; // expires immediately

	protected final Logger logger =  (Logger) LogManager.getLogger(LoginController.class);

	protected final Locale newLocale = LocaleContextHolder.getLocale();

	/*
	 * EIC15-2533: Forgot your password screen.
	 * 
	 * Forgot form included in Login Controller because it is shown before
	 * authentication
	 */

	/**
	 * EIC15-2533: New mappings for Forgot Form controller and Change Password
	 * (anonymous) form.
	 * 
	 * @param model
	 * @return
	 */

	@RequestMapping(value = "forgotForm", method = RequestMethod.GET)
	public String ShowForgotForm(ModelMap model, HttpServletRequest request,
			@RequestParam(value = "logout", required = false) String logout) {

		// logout previous user if there was an active session

		return "forgotForm";
	}

	@RequestMapping(value = "processForgot", method = RequestMethod.POST, produces = "application/json")
	public @ResponseBody ForgotResponse processForgot(@RequestParam(value = "username") String username) {

		ForgotResponse response = new ForgotResponse();

		logger.info("Forgot process. User -> " + username);
		try {
			passwordPolicyService.resetPassword(username);
			response.retCode = 0;
		} catch (Throwable t) {
			response.retCode = 1;
		}

		return response;
	}

	@RequestMapping(value = "changePassword", method = RequestMethod.GET)
	public String showChangePassword(ModelMap model, HttpServletRequest request,
			@RequestParam(value = "logout", required = false) String logout,
			@RequestParam(value = "isMobile", required = false) String isMobile) {

	
		if ((isMobile != null) && (isMobile.compareTo("true")) == 0) {
			model.addAttribute("isMobile", true);
		}

		return "changePassword";
	}

	@RequestMapping(value = "changePassword", method = RequestMethod.POST, produces = "application/json")
	public @ResponseBody ChangePassResponse processChangePassword(ModelMap model, HttpServletRequest request,
			@RequestParam(value = "username") String username,
			@RequestParam(value = "currentPassword") String currentPassword,
			@RequestParam(value = "newPassword") String newPassword) {

		ChangePassResponse response = new ChangePassResponse(-1, request.getContextPath(), "validations.servererror");

		try {
			passwordPolicyService.changeExpiredPassword(username, currentPassword, newPassword);
			response.retCode = 0;
			response.message = "validations.passwordchanged";
		} catch (NewPasswordInvalidByPolicyException e) {
			response.retCode = 2;
			// xv.2.5.1 -> These texts are keys for javascript client errors
			// (app-en_LANG.js)
			response.message = "validations.passwordstrength";
		} catch (NewPasswordInvalidByHistoryException e) {
			response.retCode = 3;
			response.message = "validations.passwordhistory";
		} catch (PasswordInvalidException e) {
			response.retCode = 4;

			response.message = "validations.passparam_wrong";
		} catch (Throwable t) {
			response.retCode = 1;
			response.message = "validations.servererror";
		}
		return response;
	}

	/**
	 * show the login form. Prints an error message if login is unsuccessful
	 * 
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "locallogin", method = RequestMethod.GET)
	public String ShowForm(ModelMap model, HttpServletRequest request,
			@RequestParam(value = "logout", required = false) String logout,
			@RequestParam(value = "isTokenExpired", required = false) String isTokenExpired) {

		// logout previous user if there was an active session
		if (securityService.getPrincipal() != null) {
			request.getSession().invalidate();
			logger.info("--------------- User's Session Invalidated");
		}

		// Forcing version to update, so Tomcat doesn't need to be restarted
		// when API version changes
		userService.getVersion();

		return "locallogin";
	}

	@RequestMapping(value = "login", method = RequestMethod.GET)
	public String publicLogin(ModelMap model, HttpServletRequest request,
			@RequestParam(value = "logout", required = false) String logout,
			@RequestParam(value = "isTokenExpired", required = false) String isTokenExpired,
			@RequestParam(value = "newSession", required = false) String newSession,
			@RequestParam(value = "token", required = false) String token,
			@RequestParam(value = "redirected", required = false) String redirectedFromApplication,
			@RequestParam(value = "eulaUrl", required = false) String eulaUrl) {

		if (token != null) {
			return this.autologinWithToken(request, token, redirectedFromApplication, eulaUrl);
		} else {
			return "redirect:/locallogin?newSession=true";
		}
	}

	private String autologinWithToken(HttpServletRequest request, String token, 
			String redirectedFromApplication, String eulaUrl) {
		try {
			this.authenticateUserWithToken(request, token);

			User currentUser = securityService.getPrincipal();
			currentUser.setToken(token);
			currentUser.setRedirectedFromApplication(redirectedFromApplication);
			
			if (currentUser.getUserType() == UserType.ROLE_CAREGIVER && currentUser.isSessionStartedByExternalSystem()) {
				if (userService.isCaregiverMaster()) {
					currentUser.setCaregiverType(CaregiverType.MASTER);
				} else {
					currentUser.setCaregiverType(CaregiverType.STANDARD);
				}
			}
			
			Eula eula = new Eula();
			eula.setLink(eulaUrl);
			currentUser.setEula(eula);
			
			// disable session expiration for autologin with token (when token is in the URL)
			currentUser.setSessionExpirationDisabled(true);
			
			this.afterLoginSuccess(currentUser, request);

			this.authorizationUtil.setUserEnabled(currentUser);
			this.authorizationUtil.updateUserAuthorization(currentUser);

			logger.info("Autologin success");

			return "redirect:/home/main";

		} catch (AuthenticationException e) {
			SecurityContextHolder.getContext().setAuthentication(null);
			logger.error("Authorization exception in autologin", e);
		}

		logger.warn("Not processed autologin for user with token {}", request.getParameter("token"));
		return "redirect:/login";
	}

	private void authenticateUserWithToken(HttpServletRequest request, String token) {
		// Must be called from request filtered by Spring Security,
		// otherwise SecurityContextHolder is not updated
		UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken("dummy_user",
				token); // TODO use webauthentication
												// details
		authenticationToken.setDetails(new WebAuthenticationDetails(request));
		// Authentication provider
		Authentication authentication = this.hcFitUserDetailsProvider.authenticate(authenticationToken);
		logger.debug("Logging in with [{}]", authentication.getPrincipal());
		SecurityContextHolder.getContext().setAuthentication(authentication);
	}

	@RequestMapping(value = "encryptPwd/{pwdId}", method = RequestMethod.GET)
	public @ResponseBody String ShowPwd(ModelMap model, @PathVariable("pwdId") String pwdId) {
		final String passwordEncoder = StringEscapeUtils.escapeHtml4(passwordEncoder.encodePassword(pwdId, "ADL"));
		return  passwordEncoder;
	}

	@RequestMapping(value = "loginErr", method = RequestMethod.GET, produces = "application/json")
	public @ResponseBody LoginResponse genericLoginError(
			@RequestParam(value = "login_error", required = true) String err) {

		boolean challenge = false;
		if (logger.isDebugEnabled())
			logger.debug("Err authenticating");
		if (!err.isEmpty()) {
			logger.info("Entrando en login Error....{}", err);
		}

		// Automatically the framework serializes output to JSON
		return new LoginResponse(1, "Login error", challenge, "", false, null);
	}

	@RequestMapping(value = "passwordExpired", method = RequestMethod.GET, produces = "application/json")
	public @ResponseBody LoginResponse passwordExpiredHandler(
			@RequestParam(value = "reason", required = true) String reason,
			@RequestParam(value = "charGroupsActive", required = true) boolean charGroupsActive,
			@RequestParam(value = "charGroupsMinimum", required = true) int charGroupsMinimum,
			@RequestParam(value = "historyActive", required = true) boolean historyActive,
			@RequestParam(value = "numOfHistoryGenerations", required = true) int numOfHistoryGenerations,
			@RequestParam(value = "passwordExpirationDays", required = true) int passwordExpirationDays,
			@RequestParam(value = "maximumLength", required = true) int maximumLength,
			@RequestParam(value = "minimumLength", required = true) int minimumLength) {

		// Reason has value and is the PasswordExpirationReason
		// PasswordExpirationReason expirationReason =
		// PasswordExpirationReason.valueOf(reason);

		logger.debug("Login Error: Password Expired due to {}", reason);

		PasswordPolicy pp = new PasswordPolicy();
		pp.setMaximumLength(maximumLength);
		pp.setMinimumLength(minimumLength);
		pp.setCharGroupsActive(charGroupsActive);
		pp.setCharGroupsMinimum(charGroupsMinimum);
		pp.setIsHistoryActive(historyActive);
		pp.setNumOfHistoryGenerations(numOfHistoryGenerations);
		pp.setPasswordExpirationDays(passwordExpirationDays);

		return new LoginResponse(2, reason, "", false, pp);
	}

	// @RequestMapping(value="processLogin",method = RequestMethod.POST )
	// public @ResponseBody int ProcessLogin(
	// @ModelAttribute("user") String login,
	// @ModelAttribute("pwd") String pwd,
	// ModelMap model){
	// logger.info("Procesando login estandar para {} con password {}", login,
	// pwd);
	// return 0;
	// }

	/**
	 * This method is called when a user has been successfully loged in the system
	 * 
	 * @param request
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "Success", method = RequestMethod.GET, produces = "application/json")
	public @ResponseBody LoginResponse Success(HttpServletRequest request, ModelMap model) {

		String path = request.getContextPath();

		User currentUser = (User) securityService.getPrincipal();
		this.afterLoginSuccess(currentUser, request);

		model.addAttribute("retries", 0);

		SucessfulLoginPayload payload = new SucessfulLoginPayload();
		payload.setPasswordExpirationData(currentUser.getPasswordExpirationData());
		payload.setEula(currentUser.getEula());

		// TODO: Check users about to expire: user.getPasswordExpirationData()

		return new LoginResponse(0, "Login OK", false, path, currentUser.getMobileModeOnDesktop(), payload);
	}

	/**
	 * This method is called when a user has been successfully loged in the system.
	 * 
	 * To complete the login from external system, is neccessary to call from client
	 * to sharedToken to provide User token for externalAPI
	 * 
	 * @param request
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "PreLoginSuccess", method = RequestMethod.GET, produces = "application/json")
	public @ResponseBody PreLoginResponse PreLoginSuccess(HttpServletRequest request, ModelMap model) {

		model.addAttribute("retries", 0);

		return new PreLoginResponse(0, "PreLogin OK.");
	}

	/**
	 * 
	 * @param model
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "processSharedToken", method = RequestMethod.POST)
	public @ResponseBody LoginResponse externalLoginRequest(@RequestBody MultiValueMap<String, String> params,
			ModelMap model, HttpServletRequest request) {

		logger.info("---- External shared token : " + params.get("token"));

		String path = request.getContextPath();

		User currentUser = (User) securityService.getPrincipal();

		// TODO check if token is valid (really not valid tokens are going to
		// fail in external API requests)
		currentUser.setToken(params.getFirst("token"));

		if (currentUser.getUserType() == UserType.ROLE_CAREGIVER && currentUser.isSessionStartedByExternalSystem()) {

			if (userService.isCaregiverMaster()) {
				currentUser.setCaregiverType(CaregiverType.MASTER);
			} else {
				currentUser.setCaregiverType(CaregiverType.STANDARD);
			}

		}

		this.afterLoginSuccess(currentUser, request);

		model.addAttribute("retries", 0);

		return new LoginResponse(0, "Login OK", false, path, currentUser.getMobileModeOnDesktop(),
				currentUser.getPasswordExpirationData());
	}

	private void afterLoginSuccess(User currentUser, HttpServletRequest request) {

		Language.setLanguageList(userService.getLanguages());
		currentUser.setLanguage(Language.getLanguage(currentUser.getLanguageId()));
		Language language = currentUser.getLanguage();
		logger.info("Locale My Session............yyyy.............................{}", language.getLanguageKey());

		Locale locale = new Locale(language.getLanguageKey().substring(0, 2),
				language.getLanguageKey().substring(3, 5));
		WebUtils.setSessionAttribute(request, SessionLocaleResolver.LOCALE_SESSION_ATTRIBUTE_NAME, locale);
		userService.loadVendorConfigurationSettings(currentUser.getVendor());
		currentUser.setVendorId(Integer.parseInt(currentUser.getVendor().getConfigSettings().getVendorId()));
		userService.loadVendorTimeZones(currentUser.getVendor(), currentUser.getLanguageId());

		DateFormatHelper.setSessionDateFormat(currentUser.getLanguageId(), request);
	}

	@RequestMapping(value = "logout", method = RequestMethod.GET)
	public String Logout(ModelMap model, HttpServletRequest request) {
		// securityService.logOut();
		String redirectTo = "redirect:locallogin?logout=success";
		
		boolean isCordova = Boolean.valueOf(String.valueOf(request.getSession().getAttribute("isCordova")));
		
		if (isCordova) redirectTo = redirectTo + "&isCordova=True";

		User currentUser = (User) securityService.getPrincipal();

		if (currentUser instanceof ThirdPartyUser) {
			redirectTo = "redirect:" + ((ThirdPartyUser) currentUser).getLogoutRedirectUrl();
		}
		
		currentUser.setEnabled(false);
		request.getSession().invalidate();
		logger.info("User logged out");
		return redirectTo;
	}

	// /**
	// * Session Timeout Handling
	// * @param model
	// * @param locale
	// * @return
	// */
	// @RequestMapping(value="sessionStatus", method = RequestMethod.GET)
	// public @ResponseBody <T> RetriableResponse<T> getSessiontatus(Model
	// model, Locale locale){
	// User currentUser = (User) securityService.getPrincipal();
	// RetriableResponse<T> response = new RetriableResponse<T>();
	// if (currentUser.isSessionExpired()) {
	// currentUser.setSessionExpired(false);
	// response.setRetryTime(30000);
	// }
	// return response;
	// }

	@RequestMapping(value = "sessionTimeout", method = RequestMethod.GET)
	public String ShowTimeOut(HttpServletRequest request) {

		logger.info("User has invalid session");

		String redirectTo = "redirect:/login?timeOut";

		User currentUser = (User) securityService.getPrincipal();

		if (currentUser != null) {
			// securityService.logOut();
			if (currentUser instanceof ThirdPartyUser) {
				redirectTo = "redirect:" + ((ThirdPartyUser) currentUser).getLogoutRedirectUrl();
			}

			request.getSession().invalidate();
		}

		return redirectTo;

	}

	@RequestMapping(value = "acceptEula", method = RequestMethod.POST)
	public void acceptEula(@RequestBody String version, HttpServletResponse response) throws IOException {

		// User currentUser = (User) securityService.getPrincipal();
		// logger.info("Accept Eula request for version for user with ID: {}",
		// currentUser.getId());
		try {
			passwordPolicyService.acceptEula(version);
		} catch (IncorrectEulaVersionException e) {
			response.sendError(HttpServletResponse.SC_HTTP_VERSION_NOT_SUPPORTED);
		} catch (RuntimeException e) {
			response.sendError(HttpServletResponse.SC_HTTP_VERSION_NOT_SUPPORTED);
		}
	}

	/**
	 * The next functions are for captcha functionality
	 */

	/*
	 * @RequestMapping(value="captcha", method = RequestMethod.GET) public void
	 * CreateCaptcha( ModelMap model, HttpServletResponse response){ Captcha
	 * captcha;
	 * 
	 * if (model.get("CAPTCHA_OBJECT") == null) { captcha =
	 * buildAndSetCaptcha(model); }
	 * 
	 * captcha = (Captcha) model.get("CAPTCHA_OBJECT"); if (shouldExpire(captcha)) {
	 * captcha = buildAndSetCaptcha(model); }
	 * 
	 * CaptchaServletUtil.writeImage(response, captcha.getImage()); }
	 * 
	 * 
	 * private Captcha buildAndSetCaptcha(ModelMap model) { Captcha captcha = new
	 * Captcha.Builder(_width, _height) .addText(new DefaultTextProducer(),new
	 * DefaultWordRenderer()) //.addBackground(new GradiatedBackgroundProducer())
	 * //.gimp(new FishEyeGimpyRenderer()) // .gimp() .gimp(new
	 * DropShadowGimpyRenderer()) // .addBorder() .addNoise(new
	 * StraightLineNoiseProducer(new Color(0xe0,0xe0,0xe0),3)) .addNoise(new
	 * CurvedLineNoiseProducer(new Color(0xe0,0xe0,0xe0),3f)) .addNoise()
	 * .addBackground(new FlatColorBackgroundProducer(new Color(0xe0,0xe0,0xe0)))
	 * .build(); if (logger.isDebugEnabled())
	 * logger.debug("Cadena de respuesta: {}",captcha.getAnswer());
	 * model.addAttribute("CAPTCHA_OBJECT", captcha); return captcha; }
	 * 
	 * /** Set the length of time the CAPTCHA will live in session, in milliseconds.
	 * 
	 * @param ttl
	 */

	/*
	 * static void setTtl(long ttl) { if (ttl < 0) { ttl = 0; } _ttl = ttl; }
	 */

	/**
	 * Get the time to live for the CAPTCHA, in milliseconds.
	 * 
	 * @return
	 */

	/*
	 * static long getTtl() { return _ttl; }
	 * 
	 * // Expire the CAPTCHA after a given number of minutes static boolean
	 * shouldExpire(Captcha captcha) { long ts = captcha.getTimeStamp().getTime();
	 * long now = new Date().getTime(); long diff = now - ts;
	 * 
	 * return diff >= _ttl; }
	 */

	@ExceptionHandler(AuthenticationException.class)
	public void handleAuthenticationException(Exception ex, HttpServletRequest request, HttpServletResponse response)
			throws IOException {

		logger.error("\n");
		logger.error("------------- AUTHENTICATION EXCEPTION DETECTED -------------");
		logger.error("\n- Error {}, while processing request: {}.", ex.getMessage(), request.getRequestURI());
		logger.error("-----------------------------------------------------");
		logger.error("\n");

		User currentUser = (User) securityService.getPrincipal();

		if (currentUser instanceof ThirdPartyUser) {
			request.getSession().invalidate();
			response.sendRedirect(((ThirdPartyUser) currentUser).getErrorRedirectUrl());
		} else {
			request.getSession().invalidate();
			response.sendError(HttpServletResponse.SC_UNAUTHORIZED);
		}
	}

	private class ForgotResponse {
		private int retCode;

		public int getRetCode() {
			return retCode;
		}

		public void setRetCode(int newCode) {
			retCode = newCode;
		}
	}

	private class ChangePassResponse {
		private int retCode;
		private String message;
		private String documentRoot; // webApp path to jscript

		public ChangePassResponse() {
		};

		public ChangePassResponse(int retCode, String message, String documentRoot) {
			this.retCode = retCode;
			this.setDocumentRoot(message);
			this.setMessage(message);
		}

		public int getRetCode() {
			return retCode;
		}

		public void setRetCode(int newCode) {
			retCode = newCode;
		}

		public String getMessage() {
			return message;
		}

		public void setMessage(String newMessage) {
			message = newMessage;
		}

		public String getDocumentRoot() {
			return documentRoot;
		}

		public void setDocumentRoot(String documentRoot) {
			this.documentRoot = documentRoot;
		}

	}

	private class LoginResponse {

		private int code;
		private String message;
		private boolean InCaptchaChallenge;
		private String documentRoot; // webApp path to jscript
		private boolean forceMobileMode;

		// In order to pass a json in the response.
		private Object payload;

		public LoginResponse(int code, String msg, String docRoot, boolean forceMobileMode, Object payload) {
			this.code = code;
			this.message = msg;
			this.InCaptchaChallenge = false;
			this.setDocumentRoot(docRoot);
			this.forceMobileMode = forceMobileMode;
			this.payload = payload;
		}

		public LoginResponse(int code, String msg, boolean captcha, String docRoot, boolean forceMobileMode,
				Object payload) {
			this.code = code;
			this.message = msg;
			this.InCaptchaChallenge = captcha;
			this.setDocumentRoot(docRoot);
			this.forceMobileMode = forceMobileMode;
			this.payload = payload;
		}

		public int getCode() {
			return code;
		}

		public String getMessage() {
			return message;
		}

		public boolean isInCaptchaChallenge() {
			return InCaptchaChallenge;
		}

		public String getDocumentRoot() {
			return documentRoot;
		}

		public Boolean getForceMobileMode() {
			return forceMobileMode;
		}

		public void setDocumentRoot(String documentRoot) {
			this.documentRoot = documentRoot;
		}

		public Object getPayload() {
			return payload;
		}

		public void setPayload(Object payload) {
			this.payload = payload;
		}

	}

	private class PreLoginResponse {

		private int code;
		private String message;

		public PreLoginResponse(int code, String msg) {
			this.code = code;
			this.message = msg;
		}

		public int getCode() {
			return code;
		}

		public String getMessage() {
			return message;
		}

	}

	private class SucessfulLoginPayload {
		private PasswordExpirationData passwordExpirationData;
		private Eula eula;

		public PasswordExpirationData getPasswordExpirationData() {
			return passwordExpirationData;
		}

		public void setPasswordExpirationData(PasswordExpirationData passwordExpirationData) {
			this.passwordExpirationData = passwordExpirationData;
		}

		public Eula getEula() {
			return eula;
		}

		public void setEula(Eula eula) {
			this.eula = eula;
		}
	}

}
