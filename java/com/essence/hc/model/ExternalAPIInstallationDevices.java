package com.essence.hc.model;

import java.util.List;;

public class ExternalAPIInstallationDevices {

	private List<ExternalAPIDevice> devicesList;
	private boolean isSynchronized;

	public List<ExternalAPIDevice> getDevicesList() {
		return this.devicesList;
	}

	public void setDevicesList(List<ExternalAPIDevice> devicesList) {
		this.devicesList = devicesList;
	}

	public boolean getIsSyncronized() {
		return this.isSynchronized;
	}

	public void setIsSyncronized(boolean isSyncronized) {
		this.isSynchronized = isSyncronized;
	}
}