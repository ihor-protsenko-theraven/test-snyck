package com.essence.hc.model;

public class PasswordExpirationData {
	private int passwordExpirationDays;
	private int aboutToExpireDays;
	
	public int getPasswordExpirationDays() {
		return passwordExpirationDays;
	}
	public void setPasswordExpirationDays(int passwordExpirationDays) {
		this.passwordExpirationDays = passwordExpirationDays;
	}
	public int getAboutToExpireDays() {
		return aboutToExpireDays;
	}
	public void setAboutToExpireDays(int aboutToExpireDays) {
		this.aboutToExpireDays = aboutToExpireDays;
	}
}
