package com.essence.hc.listeners;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.web.session.HttpSessionDestroyedEvent;

import com.essence.hc.model.User;
import com.essence.hc.service.UserService;

public class SessionDestroyedListener implements ApplicationListener<HttpSessionDestroyedEvent> {
	
	private Logger logger = LoggerFactory.getLogger(getClass());
	
	@Autowired
	private UserService userService;
	
	@Override
	public void onApplicationEvent(HttpSessionDestroyedEvent event) {
		for (SecurityContext context: event.getSecurityContexts()) {
			User principal = (User) context.getAuthentication().getPrincipal();
			if (principal != null) {
				logger.info("session destroyed, token {}", principal.getToken());
				userService.logout(principal);
				context.setAuthentication(null);
			}
		}
	}

}
