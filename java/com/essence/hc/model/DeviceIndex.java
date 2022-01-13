package com.essence.hc.model;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.codehaus.jackson.annotate.JsonProperty;

import com.essence.hc.model.Activity.ActivityType;

/**
 * @author oscar.canalejo
 *
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class DeviceIndex {

	private ActivityType activityType;
	private int index;
	private String deviceLabel;
	private int deviceId;
	
	public ActivityType getActivityType() {
		return activityType;
	}
	public void setActivityType(ActivityType activityType) {
		this.activityType = activityType;
	}
	public int getIndex() {
		return index;
	}
	public void setIndex(int index) {
		this.index = index;
	}
	
	public String getDeviceLabel() {
		return deviceLabel;
	}

	public void setDeviceLabel(String deviceLabel) {
		this.deviceLabel = deviceLabel;
	}
	public int getDeviceId() {
		return deviceId;
	}
	public void setDeviceId(int deviceId) {
		this.deviceId = deviceId;
	}	
	
	
	
}
