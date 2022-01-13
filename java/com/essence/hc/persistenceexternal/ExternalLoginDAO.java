package com.essence.hc.persistenceexternal;

import com.essence.hc.model.ExternalAPILogin;
import com.essence.hc.model.ThirdPartyUser;

public interface ExternalLoginDAO {

	ExternalAPILogin login(String username, String password);

	ThirdPartyUser thirdPartyLogin(String token, String externalPartyAuthenticationProvider);

	boolean logout(String token);
}
