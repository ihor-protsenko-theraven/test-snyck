/**
 * 
 */
package com.essence.hc.model;

import java.io.Serializable;
import java.util.List;

/**
 * Patient's ADL System Installation
 * A {@link Panel} plus a set of {@link Device} components located at the patient's home, 
 * 
 * @author oscar.canalejo
 *
 */
public class Installation implements Serializable {
	
	private static final long serialVersionUID = 1L;	
	public static enum InstallationStatus{ OK, KO }
	
	private InstallationStatus status;
    private Panel panel;
	private List<Device> devices;
	
	
	public InstallationStatus getStatus() {
		return status;
	}

	public void setStatus(InstallationStatus status) {
		this.status = status;
	}

	public List<Device> getDevices() {
		return devices;
	}

	public void setDevices(List<Device> devices) {
		this.devices = devices;
	}

	public Panel getPanel() {
		return panel;
	}

	public void setPanel(Panel panel) {
		this.panel = panel;
	}

}
