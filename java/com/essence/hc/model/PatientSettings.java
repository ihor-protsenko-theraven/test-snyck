package com.essence.hc.model;

public class PatientSettings {

	private boolean enableCall;
	private String phoneNumberType;
	private String customPhoneNumber;
	private boolean allowModifications;

	public static enum ActivePhoneType {
		DEFAULT("Default"), CUSTOM("Custom");

		private String phoneType;

		ActivePhoneType(String phoneType) {
			this.phoneType = phoneType;
		}

		public String getPhoneType() {
			return phoneType;
		}
	}

	public boolean isDefaultPhoneNumberType() {
		return this.phoneNumberType.equals("Default");
	}

	public boolean isEnableCall() {
		return enableCall;
	}

	public void setEnableCall(boolean enableCall) {
		this.enableCall = enableCall;
	}

	public String getPhoneNumberType() {
		return phoneNumberType;
	}

	public void setPhoneNumberType(String phoneNumberType) {
		this.phoneNumberType = phoneNumberType;
	}

	public String getCustomPhoneNumber() {
		return customPhoneNumber;
	}

	public void setCustomPhoneNumber(String customPhoneNumber) {
		this.customPhoneNumber = customPhoneNumber;
	}

	public boolean isAllowModifications() {
		return allowModifications;
	}

	public void setAllowModifications(boolean allowModifications) {
		this.allowModifications = allowModifications;
	}

}
