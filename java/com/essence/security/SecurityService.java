package com.essence.security;

import java.util.Map;

import com.essence.hc.model.User;
import com.essence.security.model.HCPermission;

public interface SecurityService {


	/**
	 * Returns User Object from the SecurityContext
	 * @return
	 */
	public User getPrincipal();
	
	/**
	 * Deletes current security Context
	 */
	public void logOut();
	
	/**
	 * Gets the permissions table map from the Main Authority
	 * @return
	 */
	public Map<String, HCPermission> getPermissionsTable();
}
