package com.essence.hc.interceptors;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import com.essence.hc.model.User;
import com.essence.hc.service.UserService;
import com.essence.hc.util.AppConfig;
import com.essence.hc.util.Util;
import com.essence.hc.util.VendorConfig;
import com.essence.security.SecurityService;

/**
 *  
 * @author enrique.arias
 *
 */
public class WebInterceptor implements HandlerInterceptor {

	@Autowired private SecurityService securityService;
	@Autowired private UserService userService;
	
	protected static final Logger logger = LoggerFactory.getLogger(WebInterceptor.class);
	

	@Override
	public boolean preHandle(HttpServletRequest req, HttpServletResponse resp,
			Object obj) throws Exception {

		String url = req.getRequestURI();
		
//		logger.debug("\n******************************************************************************");
//		logger.debug("Web Interceptor pre-handle for URL: {}", url);
//		logger.debug("Session ID = {}", req.getSession().getId());
		
		User currentUser = securityService.getPrincipal();

		if (currentUser != null) {

			if ((url.startsWith("/ADL/locallogin")) || (url.startsWith("/ADL/forgotForm"))) {
//				securityService.logOut();
				req.getSession().invalidate();;
				logger.info("--------------- User's Session Invalidated");
			}else {
				long sessionExpirationTime = AppConfig.getSessionExpirationTime(currentUser.getUserType());
//				logger.debug("\nSession Expiration time for role {} is {}",currentUser.getUserType(), sessionExpirationTime);
				
				if ((sessionExpirationTime > 0) && (!currentUser.isSessionExpirationDisabled())) {
					/* 
					 * The presence of an 'activity' parameter resets the expire session time
					 * this way we can reset it when there was client interaction with no call to server
					 */
					if (req.getParameter("activity") == null && 
						(url.equalsIgnoreCase("/ADL/home/status") || url.equalsIgnoreCase("/ADL/admin/alertCount"))){
						
						long restTime = (System.currentTimeMillis() - currentUser.getExpireSession());
						long restTime1= (System.currentTimeMillis() - req.getSession().getLastAccessedTime());
						logger.info("\nChecking Session Expiration for user {}. time since last activity: {}", currentUser.getNick(), restTime);
						logger.info("Time Since Last Activity: {}", restTime);
						logger.info("Time Since Last Session Accessed: {}", restTime1);

						if(restTime >= sessionExpirationTime){
							logger.info("--------------- User's Session Expired");
							currentUser.setExpireSession(System.currentTimeMillis());
							currentUser.setSessionExpired(true);
						}
					}else {
						currentUser.setExpireSession(System.currentTimeMillis());
					}
				}
			}
		}
		//	logger.info("\n******************************************************************************");
		return true;
	}
		
	@Override
	public void afterCompletion(HttpServletRequest arg0,
			HttpServletResponse arg1, Object arg2, Exception arg3)
			throws Exception {
	}

	@Override
	public void postHandle(HttpServletRequest req, HttpServletResponse resp,
			Object obj, ModelAndView modelAndView) throws Exception {
		
		if (modelAndView != null) {
			modelAndView.getModelMap().addAttribute("currentUser", (User) securityService.getPrincipal());
			modelAndView.getModelMap().addAttribute("vendorConfiguration", new VendorConfig());
			modelAndView.getModelMap().addAttribute("appConfiguration", new AppConfig());
			
			// if there is a message to be shown in a modal at the beginning, force the page not to be cached so it is reloaded in case the user presses back history button
			if (modelAndView.getModelMap().containsAttribute("flashMessageCode") || modelAndView.getModelMap().containsAttribute("flashMessage")) {
				resp.setHeader("Pragma","No-cache");     
				resp.setHeader("Cache-Control","no-cache, must-revalidate, no-store, post-check=0, pre-check=0");
				resp.setDateHeader("Expires", -1);
			};
		}
		
	}
	
	
//	@Override
//	public boolean preHandle(HttpServletRequest req, HttpServletResponse resp,
//			Object obj) throws Exception {
//		
//
//		String url = req.getRequestURI();
//		restTime= System.currentTimeMillis();
//		
//		User currentUser = (User) securityService.getPrincipal();
//
//		logger.info("\n");
//		logger.info("******************************************************************************");
//		logger.info("Checking URL {}", url);
//		
//		if (url.equalsIgnoreCase("/ADL/home/status")){
//			restTime = restTime-currentUser.getExpireSession();
//			if(restTime>=900000){
//				logger.info("\n\nUser's Session is Expired");
//				Util.setExpired(true);
//				currentUser.setExpireSession(System.currentTimeMillis());
//				return true;				
//			} else {
//				Util.setExpired(false);
//			}
//			return true;
//		}else{
//			logger.info("\n\nUser's Session is Alive");
//			currentUser.setExpireSession(System.currentTimeMillis());
//			Util.setExpired(false);
//			return true;
//		}
//	}

}
