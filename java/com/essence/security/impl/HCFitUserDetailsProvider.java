package com.essence.security.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.dao.AbstractUserDetailsAuthenticationProvider;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;

import com.essence.hc.service.UserService;
import com.essence.hc.model.User;

public class HCFitUserDetailsProvider extends AbstractUserDetailsAuthenticationProvider {


		
		protected final Logger logger = LoggerFactory.getLogger(getClass());
		
		@Override
		protected void additionalAuthenticationChecks(UserDetails arg0,
				UsernamePasswordAuthenticationToken arg1) throws AuthenticationException {
			// TODO Auto-generated method stub

		}

		@Override
		protected UserDetails retrieveUser(String username,
				UsernamePasswordAuthenticationToken auth) throws AuthenticationException {
			
			logger.info("Authenticating user: {}", username);
			String token = auth.getCredentials().toString();
			User usr = userService.getUserWithToken(token);
			if (usr != null) {
				logger.info("returning userDetails for {}", usr.getUsername());
				usr.setSessionStartedByExternalSystem(true);
				return usr; 
			}else{
				logger.error("User {} not found or invalid credentials",auth.getName());
				throw new BadCredentialsException("Bad Credentials");
			}
		}
		
		@Autowired
		private UserService userService;
}
