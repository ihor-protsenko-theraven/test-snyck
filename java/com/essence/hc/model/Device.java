/**
 * 
 */
package com.essence.hc.model;

import java.io.Serializable;

import com.essence.hc.model.Activity.ActivityType;

/**
 * Each hardware component, placed at the patient's installation to gather and provide 
 * data to the system panel (e.g: Photo cameras, Medicine dispenser, Medical equipment, etc)
 * 
 * This is a map of internal API getDeviceListByUserID call.
 * From 2.5.5 version, DeviceExternal class has been added that implemetns the device on the way that
 * extenal API getDevices call expects.
 *  
 * @author oscar.canalejo
 *
 */
public class Device  implements Serializable {

	private static final long serialVersionUID = 1L;
	
	public static enum DeviceType{ PRESENCE_DETECTOR, PHOTO_CAM, VIDEO, MED_DISPENSER, MEDICAL, EMERGENCYPENDANTADVANCED }
	public static enum DeviceState{ ON, OFF }
	public static enum DeviceEPAStatus{
		NOT_PAIRED(0),
		PAIRED_AND_RESPONSIVE(1),
		PAIRED_AND_UNRESPONSIVE(2),
		UNKNOWN(-1);
		
		private int id;
		
		DeviceEPAStatus(int id) {
			this.id = id;
		}
		
		public int getId(){
			return this.id;
		}
		
		public static DeviceEPAStatus byId(int id){
			for (DeviceEPAStatus epaStatus : DeviceEPAStatus.values()) {
				if ( epaStatus.getId() == id ) return epaStatus;
			}
			
			return UNKNOWN;
		}
	}

	private String id;
	private DeviceType type;
	private String alias;
	private DeviceState state;
	
	public ActivityType activityTypeID;

	public int controlPanelID;
	public int devRoomID;
	public int errorMessageID;
	public int installationID;
	public int familyId;
	public boolean isIPD;
	public String label;
	public int correlatedDevID; //Correlated Device. (since v1.2, for Bathroom-Restroom device correlation feature)
	public Device correlatedDev;
	public DeviceEPAStatus mobilePairingStatus;
	

	public ActivityType getActivityTypeID() {
		return activityTypeID;
	}
	public void setActivityTypeID(ActivityType activityTypeID) {
		this.activityTypeID = activityTypeID;
	}	

	public int getInstallationID() {
		return installationID;
	}
	public void setInstallationID(int installationID) {
		this.installationID = installationID;
	}
	public boolean isIsIPD() {
		return isIPD;
	}
	public void setIsIPD(boolean isIPD) {
		this.isIPD = isIPD;
	}
	public String getLabel() {
		return label;
	}
	public void setLabel(String label) {
		this.label = label;
	}
	
	
	public int getFamilyId() {
		return familyId;
	}
	public void setFamilyId(int familyId) {
		this.familyId = familyId;
	}
	public int getControlPanelID() {
		return controlPanelID;
	}
	public void setControlPanelID(int controlPanelID) {
		this.controlPanelID = controlPanelID;
	}
	public int getDevRoomID() {
		return devRoomID;
	}
	public void setDevRoomID(int iDevRoomID) {
		this.devRoomID = iDevRoomID;
	}
	public int getErrorMessageID() {
		return errorMessageID;
	}
	public void setErrorMessageID(int errorMessageID) {
		this.errorMessageID = errorMessageID;
	}
	public boolean isIPD() {
		return isIPD;
	}
	public void setIPD(boolean isIPD) {
		this.isIPD = isIPD;
	}
	
	public DeviceEPAStatus getMobilePairingStatus(){
		return this.mobilePairingStatus;
	}
	
	public void setMobilePairingStatus(DeviceEPAStatus mobilePairingStatus) {
		this.mobilePairingStatus = mobilePairingStatus;
	}
	
	//OLD
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public DeviceType getType() {
		return type;
	}
	public void setType(DeviceType type) {
		this.type = type;
	}
	public String getAlias() {
		return alias;
	}
	public void setAlias(String alias) {
		this.alias = alias;
	}
	public DeviceState getState() {
		return state;
	}
	public void setState(DeviceState state) {
		this.state = state;
	}
	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	public Device getCorrelatedDev() {
		return correlatedDev;
	}
	public void setCorrelatedDev(Device correlatedDev) {
		this.correlatedDev = correlatedDev;
	}
	public int getCorrelatedDevID() {
		return correlatedDevID;
	}
	public void setCorrelatedDevID(int correlatedDevID) {
		this.correlatedDevID = correlatedDevID;
	}	
}
