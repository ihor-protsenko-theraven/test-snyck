package com.essence.security.model;

import java.security.acl.Permission;

public class HCPermission implements Permission {

	public HCPermission(int value){
		this.value = value;
	}
	// Permision value
	private int value;

	public int getValue() {
		return value;
	}

	public void setValue(int value) {
		this.value = value;
	}
	
}
