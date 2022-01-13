package com.essence.hc.model;

public class ControlPanelAssociation {

	/**
	 * The code of panels to which the service package may be assigned, e.g.: Family
	 * panels (code = 8) may be assigned “Family” or “PERS-E” service packages.
	 */
	private int controlPanelServicePackage;
	
	private String panelDeviceType;
	
	public int getControlPanelServicePackage() {
		return controlPanelServicePackage;
	}
	public void setControlPanelServicePackage(int controlPanelServicePackage) {
		this.controlPanelServicePackage = controlPanelServicePackage;
	}
	public String getPanelDeviceType() {
		return panelDeviceType;
	}
	public void setPanelDeviceType(String panelDeviceType) {
		this.panelDeviceType = panelDeviceType;
	}
}
