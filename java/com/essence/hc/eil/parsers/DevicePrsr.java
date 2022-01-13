package com.essence.hc.eil.parsers;

import org.codehaus.jackson.annotate.JsonProperty;

import com.essence.hc.eil.exceptions.ParseException;
import com.essence.hc.model.Activity.ActivityType;
import com.essence.hc.model.Device;


public class DevicePrsr implements IParser<Device> {

	private int activityTypeID;
	private int controlPanelID;
	private int deviceID;
	private int errorMessageID;
	private int installationID;
	private boolean isIPD;
	private String label;
	private int iDvFamily;
	private int iDvRelated;
	private int mobilePairingStatus;
	
	public DevicePrsr() {
	}

	
	private ActivityType ReturnEnumValue(int i) {
		
		switch(i) {
		case 0:
			return ActivityType.OTHER_ROOM;
		case 1:
			return ActivityType.FRIDGE_DOOR;
		case 2:
			return ActivityType.FRONT_DOOR;
		case 3:
			return ActivityType.BEDROOM_SENSOR;
		case 4:
			return ActivityType.TOILET_ROOM_SENSOR;
		case 5:
			return ActivityType.BATHROOM_SENSOR;
		case 6:
			return ActivityType.LIVING_ROOM;
		case 7:
			return ActivityType.DINING_ROOM;
			
//			case 8:
//			return ActivityType.STUDY_ROOM;
		case 8:
			return ActivityType.SMOKE_DETECTOR;
		case 9:
			return ActivityType.WATER_LEAKAGE;
		case 10:
			return ActivityType.EP;
		case 11:
			return ActivityType.SPBP;
		case 12:
			return ActivityType.VPD;
		case 13:
			return ActivityType.EPA;
		case 14:
			return ActivityType.EPPlus;
		}
		// TODO: error. We need to do something here
		return ActivityType.LIVING_ROOM;
	}

	@Override
	public Device parse() {
//		logger.info("\nParsing " + getClass() + "...\n");
		
		Device device = new Device();
		
		try{
			device.setActivityTypeID( ReturnEnumValue(activityTypeID));
			device.setControlPanelID(controlPanelID);
			device.setDevRoomID(deviceID);
			device.setErrorMessageID(errorMessageID);
			device.setInstallationID(installationID);
			device.setIsIPD(isIPD);
			device.setLabel(label);
			device.setFamilyId(iDvFamily);
			device.setCorrelatedDevID(iDvRelated);
			device.setMobilePairingStatus(Device.DeviceEPAStatus.byId(mobilePairingStatus));
			
		}catch(Exception ex) {
			throw new ParseException(ex,"Unexpected parse error");
		}
		return device;
	}

	@JsonProperty("sAcvtType")
	public int getsActivityTypeID() {
		return activityTypeID;
	}

	@JsonProperty("sAcvtType")
	public void setsActivityTypeID(int sActivityTypeID) {
		this.activityTypeID = sActivityTypeID;
	}

	@JsonProperty("iCpId")
	public int getControlPanelID() {
		return controlPanelID;
	}

	@JsonProperty("iCpId")
	public void setControlPanelID(int controlPanelID) {
		this.controlPanelID = controlPanelID;
	}

	@JsonProperty("iDvRoomId")
	public int getDeviceID() {
		return deviceID;
	}

	@JsonProperty("iDvRoomId")
	public void setDeviceID(int deviceID) {
		this.deviceID = deviceID;
	}

	@JsonProperty("iErrHndlrId")
	public int getErrorMessageID() {
		return errorMessageID;
	}

	@JsonProperty("iErrHndlrId")
	public void setErrorMessageID(int sErrorMessageID) {
		this.errorMessageID = sErrorMessageID;
	}

	@JsonProperty("iRecId")
	public int getInstallationID() {
		return installationID;
	}

	@JsonProperty("iRecId")
	public void setInstallationID(int sInstallationID) {
		this.installationID = sInstallationID;
	}

	@JsonProperty("IsIPD")
	public boolean getsIsIPD() {
		return isIPD;
	}

	@JsonProperty("IsIPD")
	public void setsIsIPD(boolean sIsIPD) {
		this.isIPD = sIsIPD;
	}

	@JsonProperty("sLbl")
	public String getsLabel() {
		return label;
	}

	@JsonProperty("sLbl")
	public void setsLabel(String sLabel) {
		this.label = sLabel;
	}

	public int getiDvFamily() {
		return iDvFamily;
	}

	public void setiDvFamily(int iDvFamily) {
		this.iDvFamily = iDvFamily;
	}
	@JsonProperty("iDevRelated")
	public int getiDvRelated() {
		return iDvRelated;
	}
	@JsonProperty("iDevRelated")
	public void setiDvRelated(int iDvRelated) {
		this.iDvRelated = iDvRelated;
	}
	
	@JsonProperty("mobilePairingStatus")
	public int getMobilePairingStatus(){
		return this.mobilePairingStatus;
	}
	
	@JsonProperty("mobilePairingStatus")
	public void setMobilePairingStatus(int mobilePairingStatus) {
		this.mobilePairingStatus = mobilePairingStatus;
	}
}
