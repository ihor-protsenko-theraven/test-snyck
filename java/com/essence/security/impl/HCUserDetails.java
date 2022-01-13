package com.essence.security.impl;

import java.util.Collection;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

public class HCUserDetails implements UserDetails{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private List<GrantedAuthority> AUTHORITIES;
	private String username;
	private String password;
	
	public HCUserDetails(String user, String pwd, List<GrantedAuthority> authList){
		this.username = user;
		this.password = pwd;
		this.AUTHORITIES = authList;
	}
	
	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return AUTHORITIES;
	}

	@Override
	public String getPassword() {
		// TODO Auto-generated method stub
		return this.password;
	}

	@Override
	public String getUsername() {
		// TODO Auto-generated method stub
		return this.username;
	}

	@Override
	public boolean isAccountNonExpired() {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public boolean isAccountNonLocked() {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public boolean isEnabled() {
		// TODO Auto-generated method stub
		return true;
	}

}
