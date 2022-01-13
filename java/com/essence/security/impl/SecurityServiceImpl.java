package com.essence.security.impl;

//import java.security.Permission;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.context.SecurityContextHolder;

import com.essence.hc.model.User;
import com.essence.security.SecurityService;
import com.essence.security.model.HCPermission;

public class SecurityServiceImpl implements SecurityService{

	
	
	
	protected final Logger logger = LoggerFactory.getLogger(getClass());
	
	/**
	 * Returns the authenticated user or null
	 * @return
	 */
	public User getPrincipal(){
		// Obtenemos el usuario autentificado
		User user = null;
		try{
			user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		} catch (Exception e){
			logger.warn("Error Getting User from Security Context");
			return null;
		}
		
		return user;
	}

	
	@Override
	public void logOut() {
		SecurityContextHolder.getContext().setAuthentication(null);
	}


	/**
	 * Gets The permissions of the APP for the diffeent roles defined 
	 */
	@Override
	public Map<String, HCPermission> getPermissionsTable() {
		
		// TODO: All of this is a DUMMY. Need to call EIL layer to get the real ROLES
        Map<String, HCPermission> result = new HashMap<String, HCPermission>();
        result.put("ROLE_CAREGIVER", new HCPermission(511)); // All Permissions
        result.put("ROLE_USER",  new HCPermission(387)); // MSG_WRITE + MSG_READ + CALL_PATIENT + CLOSE_EVT 
        result.put("ROLE_ADMIN", new HCPermission(  3)); // MSG_WRITE + MSG_READ
        return result;
	}
		
}
