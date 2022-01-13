package com.essence.hc.model;

public class DeviceAssociation {

	private String type;
	private String value;
	private ExternalAPIDevice associatedDevice;
	
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}
	public ExternalAPIDevice getAssociatedDevice() {
		return associatedDevice;
	}
	public void setAssociatedDevice(ExternalAPIDevice associatedDevice) {
		this.associatedDevice = associatedDevice;
	}
	
}
