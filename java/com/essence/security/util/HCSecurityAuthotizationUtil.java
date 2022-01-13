package com.essence.security.util;

import java.util.ArrayList;
import java.util.List;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;

import com.essence.hc.model.User;

public class HCSecurityAuthotizationUtil {

	public void setUserEnabled(User user) {
		if (user != null) {
			user.setEnabled(true);
		}
	}

	// generic authorization
	public void setUserAuthorization(User user) {
		if (user != null) {
			List<GrantedAuthority> AUTHORITIES = new ArrayList<GrantedAuthority>();
			AUTHORITIES.add(new SimpleGrantedAuthority("ROLE_USER"));
			if (user.getUserType() != null)
				AUTHORITIES.add(new SimpleGrantedAuthority(user.getUserType().toString()));
			user.setAUTHORITIES(AUTHORITIES);
		}
	}
	
	public void updateUserAuthorization(User user){
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();

		List<GrantedAuthority> updatedAuthorities = new ArrayList<>(auth.getAuthorities());
		
		if (user != null) {
			updatedAuthorities.add(new SimpleGrantedAuthority("ROLE_USER"));
			
			if (user.getUserType() != null)
				updatedAuthorities.add(new SimpleGrantedAuthority(user.getUserType().toString()));
			
			user.setAUTHORITIES(updatedAuthorities);
		}

		Authentication newAuth = new UsernamePasswordAuthenticationToken(user, auth.getCredentials(), updatedAuthorities);

		SecurityContextHolder.getContext().setAuthentication(newAuth);
	}

}
