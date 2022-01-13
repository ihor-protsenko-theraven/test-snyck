package com.essence.hc.persistenceexternal.impl;

import com.essence.hc.eil.requestprocessors.RequestProcessor;
import com.essence.hc.eil.requestprocessors.impl.DummyRequestProcessor;
import com.essence.hc.model.ExternalAPILogin;
import com.essence.hc.model.PasswordExpirationData;
import com.essence.hc.model.ResponseStatus;
import com.essence.hc.model.ThirdPartyUser;
import com.essence.hc.model.User;
import com.essence.hc.persistence.UserDAO;
import com.essence.hc.persistence.impl.ExternalDAOImpl;
import com.essence.hc.persistenceexternal.ExternalLoginDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ExternalLoginDAOImpl extends ExternalDAOImpl implements ExternalLoginDAO 
{
	@Autowired
	UserDAO userDao;


	/**
	 * logins into the family installation API and returns the session token
	 * @param username
	 * @param password
	 * @return token
	 */
	
	@Override
	public ExternalAPILogin login(String username, String password) {
		HashMap<String, String> reqParams = new HashMap<String, String>();
		reqParams.put("userName", username);
		reqParams.put("password", password);
		ExternalAPILogin login = reqProcessor.performRequest(ExternalAPILogin.class, "external_login", reqParams);
		if (login.getResponse().equals("111")) {
			logger.debug("ATT: User password has expired");
		}
		PasswordExpirationData ped = login.getPasswordExpirationData();
		if (ped !=null && ped.getAboutToExpireDays()>= ped.getPasswordExpirationDays()) {
			logger.debug("ATT: User password has expired");
		}
		return login;
	}

	@Override
	public ThirdPartyUser thirdPartyLogin(String token, String externalPartyAuthenticationProvider) {

		HashMap<String, Object> reqParams = new HashMap<String, Object>();
		HashMap<String, Object> externalPartyLoginDetails = new HashMap<String, Object>();
		
		externalPartyLoginDetails.put("code", token);
		externalPartyLoginDetails.put("externalPartyAuthenticationProviderType", externalPartyAuthenticationProvider);
		
		reqParams.put("ExternalPartyLoginDetails", externalPartyLoginDetails);

		ThirdPartyUser thirdPartyUser = reqProcessor.performRequest(ThirdPartyUser.class, "external_party_login", reqParams);

		return thirdPartyUser;
	}

	
	  @Override
	  public boolean logout(String token) 
	  {	
		   HashMap<String, String> reqHeaders = new HashMap<String, String>();
		   reqHeaders.put("Authorization", "Token " + token);
		   ResponseStatus response = reqProcessor.performRequest(ResponseStatus.class, "external_logout", null, reqHeaders);
		   return (response.getNumErr() == 0);
	  }
}

