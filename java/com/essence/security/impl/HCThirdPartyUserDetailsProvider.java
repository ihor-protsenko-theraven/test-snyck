package com.essence.security.impl;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.dao.AbstractUserDetailsAuthenticationProvider;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetails;

import com.essence.hc.model.User;
import com.essence.hc.service.UserService;
import com.essence.security.util.HCSecurityAuthotizationUtil;

public class HCThirdPartyUserDetailsProvider extends AbstractUserDetailsAuthenticationProvider {

	@Autowired
	private UserService userService;

	protected final Logger logger = LoggerFactory.getLogger(getClass());
	private HCSecurityAuthotizationUtil authorizationUtil = new HCSecurityAuthotizationUtil();

	@Override
	protected void additionalAuthenticationChecks(UserDetails arg0, UsernamePasswordAuthenticationToken arg1)
			throws AuthenticationException {
		// TODO Auto-generated method stub

	}

	@Override
	protected UserDetails retrieveUser(String username, UsernamePasswordAuthenticationToken auth)
			throws AuthenticationException {

		logger.info("Authenticating third party user.");

		// TODO improve using web authentication details
		String code = auth.getCredentials().toString();

		// TODO THE URL HAS TO PROVIDE THE externalAuthenticationProvider TYPE
		User thirdPartyUser = this.obtainThirdPartyUser(code, "MedicalGuardian");
		
		if (thirdPartyUser != null) {
			this.authorizationUtil.setUserEnabled(thirdPartyUser);
			logger.info("Third party user credentials retrieved.");
			return thirdPartyUser;
		} else {
			logger.error("Third party user with code {} failure on validation request", code);
			throw new BadCredentialsException("Bad Credentials");
		}
	}

	private User obtainThirdPartyUser(String code, String vendor) {
		return userService.getThirdPartyUserWithToken(code, vendor);
	}

}