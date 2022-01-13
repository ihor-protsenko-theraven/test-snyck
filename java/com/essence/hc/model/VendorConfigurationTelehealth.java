package com.essence.hc.model;

import java.io.Serializable;

public class VendorConfigurationTelehealth implements Serializable {
	private static final long serialVersionUID = 1L;
	private boolean enabled;
	
	public VendorConfigurationTelehealth(boolean enabled) {
	    this.enabled = enabled;
	}
	
	public VendorConfigurationTelehealth() {
	}
	
	public boolean isEnabled() {
		return enabled;
	}

	public void setsEnabled(boolean enabled2) {
		this.enabled = enabled2;
	}
}