package com.essence.security.impl;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.PermissionEvaluator;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;

import com.essence.security.SecurityService;
import com.essence.security.model.HCPermission;


/**
 * This class is in charge of check actions allowed by role dinamically 
 * @author manuel.garcia
 *
 */
public class HCPermissionEvaluator implements PermissionEvaluator {

	protected final Logger logger = LoggerFactory.getLogger(getClass());

	// Permission Map
	private Map<String, HCPermission> permissions = new HashMap<String, HCPermission>();

	@Autowired
	private SecurityService securityService;
	
	/** Only for testing purposes. In the future this will come from a database**/
	private static final int MSG_WRITE	  =   1;
	private static final int MSG_READ	  =   2;
	private static final int TAKE_PHOTO	  =   4;
	private static final int SEE_PHOTO	  =   8;
	private static final int CALL_POLICE  =  16;
	private static final int CALL_FIREDPT =  32;
	private static final int CALL_AMBUL	  =  64;
	private static final int CALL_PANEL = 128;
	private static final int CLOSE_EVT	  = 256;
    /*********************************************************************/

		
	
	/**
	 * Load of permissions
	 * @param permissionNameToPermissionMap
	 */
	public void setPermissionTable() {
		this.permissions = securityService.getPermissionsTable();
	}

	@Override
	public boolean hasPermission(Authentication auth, Object obj, Object permission) {
		// Call to our test method
		// For now only Strings are allowed for permission
		// Valid strings are : read , write, exec, create, close
		if (this.permissions.isEmpty())
			this.setPermissionTable();
		if (canHandle(auth, obj, permission))
			return hasPermission(auth, (String) obj, permission);
		else{
			logger.error("Cant handle permission for {},{}", obj, permission);
			return false;
		}
	}

	@Override
	public boolean hasPermission(Authentication auth, Serializable targetId,
			String targetType, Object permission) {
		 logger.error("Id and Class permissions are not supported by " + this.getClass().toString());
//		if (canHandle(auth, targetId, permission))
//			return hasPermission(auth, (String) targetId, permission);
//		else
			return false;
	}
	
	/**
	 * Method for our security needs. Still dont use secured elements. Only roles vs Actions
	 */
	private boolean hasPermission(Authentication auth, String securedElement, Object permission){
		logger.debug("testing permission from {} to access {} for {}", auth.getName(), securedElement, permission);
		// Here we can check Dinamically user ROLES to check permissions
		// Search on the Map.
		int action = (Integer)permission;
		logger.debug("testing permission for {} on action {}", auth.getName(), action);
	    for (GrantedAuthority grantedAuthority : auth.getAuthorities()) {
	    	logger.debug("Testing permission for role {}", grantedAuthority.getAuthority());
	    	int perm = this.permissions.get(grantedAuthority.getAuthority()).getValue();
	    	logger.debug("Permission for role {} are {}", grantedAuthority.getAuthority(), perm);
	    	if ( (perm & action) != 0)
	    		return true;
	    }
	    logger.info("User has no permission for that action");
	    return false;
	}	

	private boolean canHandle(Authentication authentication, Object targetDomainObject, Object permission) {
		return targetDomainObject != null && authentication != null && permission instanceof Integer;
	}

}
