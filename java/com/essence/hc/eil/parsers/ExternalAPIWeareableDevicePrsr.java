package com.essence.hc.eil.parsers;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;

import com.essence.hc.model.Activity.ActivityType;
import com.essence.hc.model.Alert.AlertType;
import com.essence.hc.model.Device.DeviceType;
import com.essence.hc.model.WearableDevice;

@JsonIgnoreProperties(ignoreUnknown = true)
public class ExternalAPIWeareableDevicePrsr implements IParser<WearableDevice>{

	String deviceIdentifier;
	int deviceId;
	String activityType;
	String label;
	String deviceType;
	int deviceFamily;
	
	
	@Override
	public WearableDevice parse() {
		WearableDevice wearableDevice = new WearableDevice();
		
		wearableDevice.setDeviceIdentifier(deviceIdentifier);
		wearableDevice.setDeviceId(deviceId);
		wearableDevice.setActivityType(ActivityType.valueOf(activityType.toUpperCase()));
		wearableDevice.setLabel(label);
		wearableDevice.setDeviceType(DeviceType.valueOf(deviceType.toUpperCase()));
		wearableDevice.setDeviceFamily(AlertType.getById(deviceFamily));
		
		return wearableDevice;
	}


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


	public String getActivityType() {
		return activityType;
	}


	public void setActivityType(String activityType) {
		this.activityType = activityType;
	}


	public String getLabel() {
		return label;
	}


	public void setLabel(String label) {
		this.label = label;
	}


	public String getDeviceType() {
		return deviceType;
	}


	public void setDeviceType(String deviceType) {
		this.deviceType = deviceType;
	}


	public int getDeviceFamily() {
		return deviceFamily;
	}


	public void setDeviceFamily(int deviceFamily) {
		this.deviceFamily = deviceFamily;
	}
	 
	
}
