package com.essence.hc.model;

import com.essence.hc.model.Activity.ActivityType;
import com.essence.hc.model.Alert.AlertType;
import com.essence.hc.model.Device.DeviceType;

public class WearableDevice {

	String deviceIdentifier;
	int deviceId;
	ActivityType activityType;
	String label;
	DeviceType deviceType;
	AlertType deviceFamily;
	
	
	public String getDeviceIdentifier() {
		return deviceIdentifier;
	}
	public void setDeviceIdentifier(String deviceIdentifier) {
		this.deviceIdentifier = deviceIdentifier;
	}
	public int getDeviceId() {
		return deviceId;
	}
	public void setDeviceId(int deviceId) {
		this.deviceId = deviceId;
	}
	public ActivityType getActivityType() {
		return activityType;
	}
	public void setActivityType(ActivityType activityType) {
		this.activityType = activityType;
	}
	public String getLabel() {
		return label;
	}
	public void setLabel(String label) {
		this.label = label;
	}
	public DeviceType getDeviceType() {
		return deviceType;
	}
	public void setDeviceType(DeviceType deviceType) {
		this.deviceType = deviceType;
	}
	public AlertType getDeviceFamily() {
		return deviceFamily;
	}
	public void setDeviceFamily(AlertType deviceFamily) {
		this.deviceFamily = deviceFamily;
	}
	
}
