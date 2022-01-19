package com.essence.hc.interceptors;

import java.net.URLEncoder;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.security.web.util.AntPathRequestMatcher;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.HandlerMapping;
import org.springframework.web.servlet.ModelAndView;

import com.essence.hc.model.Patient;
import com.essence.hc.model.User;
import com.essence.hc.model.User.UserType;
import com.essence.hc.service.UserService;
import com.essence.security.SecurityService;

/**
 * This interceptor checks if in the http call there is a param.  called "residentId",
 * if this param. exists then the resident user is loaded in session;
 * In case the residentId is another the resident object is replaced by new one.
 *  
 * @author enrique.arias
 *
 */
public class AdminInterceptor implements HandlerInterceptor {
	protected static final Logger logger = LoggerFactory.getLogger(AdminInterceptor.class);
	
	@Autowired private SecurityService securityService;
	@Autowired private UserService userService;
	
//	private AntPathRequestMatcher stepCountPathMatcher = new AntPathRequestMatcher("/admin/step_count/**/*.json");
	private AntPathRequestMatcher pathMatcher = new AntPathRequestMatcher("/admin/step_count*/**");
	
	
	@Override
	public void afterCompletion(HttpServletRequest arg0,
			HttpServletResponse arg1, Object arg2, Exception arg3)
			throws Exception {
	}

	@Override
	public void postHandle(HttpServletRequest req, HttpServletResponse resp,
			Object obj, ModelAndView modelAndView) throws Exception {
		
		if (modelAndView != null)
			modelAndView.getModelMap().addAttribute("currentUser", (User) securityService.getPrincipal());
	}

	@Override
	public boolean preHandle(HttpServletRequest req, HttpServletResponse resp,
			Object obj) throws Exception {
		
		Map<?, ?> pathVariables = (Map<?, ?>) req.getAttribute(HandlerMapping.URI_TEMPLATE_VARIABLES_ATTRIBUTE);
		User   currentUser 		= securityService.getPrincipal();
		
		// check to prevent a caregiver user with forced mobile view from accessing admin pages
		if (currentUser.getMobileModeOnDesktop() && currentUser.getUserType() == UserType.ROLE_CAREGIVER) {
			
			// See: EIC15-1626: Exception added in order to retrieve step_count data
			if (pathMatcher.matches(req)) {
				return true;
			}
			
			logger.info("Caregiver with forced mobile view trying to enter desktop view: redirect to mobile view");
			String url = URLEncoder.encode(req.getRequestURI(),"utf-8" );
			url = url.substring(0, url.indexOf("/admin")) + "/home/main";
			resp.sendRedirect(url);
			return false;
		}
		
		if (pathVariables.containsKey("residentId")) {
			String residentId       = (String) pathVariables.get("residentId");
			
			if (currentUser != null) {
				if (currentUser.getCurrentPatient() == null || !currentUser.getCurrentPatient().getStringUserId().equals(residentId)) {
					currentUser.setCurrentPatient((Patient)userService.getUser(residentId));
				}
			}
		}
		return true;
	}

}
