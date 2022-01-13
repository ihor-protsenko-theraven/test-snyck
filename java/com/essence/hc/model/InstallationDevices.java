package com.essence.hc.model;

import java.util.List;
import com.essence.hc.model.Device;

public class InstallationDevices {
	
	private List<Device> devicesList;
	private boolean isSynchronized;
	
	public List<Device> getDevicesList() {
		return this.devicesList;
	}
	
	public void setDevicesList(List<Device> devicesList) {
		this.devicesList = devicesList;
	}
	
	public boolean getIsSyncronized() {
		return this.isSynchronized;
	}
	
	public void setIsSyncronized(boolean isSyncronized) {
		this.isSynchronized = isSyncronized;
	}
}
