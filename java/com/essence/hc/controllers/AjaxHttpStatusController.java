package com.essence.hc.controllers;

import java.io.IOException;

import javax.servlet.http.HttpServletResponse;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.essence.hc.model.ThirdPartyUser;
import com.essence.hc.model.User;
import com.essence.security.SecurityService;

@Controller
public class AjaxHttpStatusController {

	@Autowired
	SecurityService securityService;
	
	protected final Logger logger =  (Logger) LogManager.getLogger(AjaxHttpStatusController.class);
	
	@RequestMapping(value="/unauthorized")
	public void handleAuthenticationException(HttpServletResponse response) throws IOException {
			
		logger.error("\n");
		logger.error("------------- HTTP STATUS HANDLED -------------");
		logger.error("\n- 401 UNAUTHORIZED -");
		logger.error("-----------------------------------------------------");
		logger.error("\n");
				
		User currentUser = (User) securityService.getPrincipal();
		
		if(currentUser instanceof ThirdPartyUser){
			response.sendRedirect(((ThirdPartyUser) currentUser).getErrorRedirectUrl());
		}else{
			response.sendError( HttpServletResponse.SC_UNAUTHORIZED);
		}
	}
	
}
