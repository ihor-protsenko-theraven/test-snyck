package com.essence.security.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.dao.AbstractUserDetailsAuthenticationProvider;
import org.springframework.security.authentication.encoding.PasswordEncoder;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;

import com.essence.hc.service.UserService;
import com.essence.hc.model.User;

public class HCUserDetailsProvider extends AbstractUserDetailsAuthenticationProvider {


		
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
			// Password encrypting with SHA
			String pwd = auth.getCredentials().toString();
			String pwdSHA = passwordEncoder.encodePassword(pwd, "ADL");
			User usr = userService.GetUserWithSecurity(auth.getName(), pwd, pwdSHA, "US", "ADL", false);
			if (usr != null) {
				logger.info("returning userDetails for {}", usr.getUsername());
				return usr; 
			}else{
				logger.error("User {} not found or invalid credentials",auth.getName());
				throw new BadCredentialsException("Bad Credentials");
			}
		}
		
		@Autowired
		private UserService userService;
		@Autowired
		private PasswordEncoder passwordEncoder;	
}
