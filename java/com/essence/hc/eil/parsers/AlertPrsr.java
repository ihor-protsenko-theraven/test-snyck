package com.essence.hc.eil.parsers;

import java.util.Date;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;

import com.essence.hc.eil.exceptions.ParseException;
import com.essence.hc.model.Alert;
import com.essence.hc.model.Alert.AlertState;
import com.essence.hc.model.Alert.AlertType;

@JsonIgnoreProperties(ignoreUnknown = true)
public class AlertPrsr implements IParser<Alert> {
	
	 private String iAlrtId;             
     private int iPatientId;
     private int iCpId;
     private int iAlrtQId;                    
     private int iAlrtGrpId;                  
     private int iAlrtType;  
     private String iAlrtSessionId;
     private String sAlrtType;                
     private int iAlrtState;                  
     private Date dAlrtStatusDateTime;    
     private String sAlrtDescription;         
     private String sPrm1;                    
     private String sPrm2;                    
     private String sPrm3;                    
     private int iSubStsGrpId;             
	 private String sAlertInfo;
	 private LocationPrsr location;
	 private int eventCode;
	 private int deviceType;
	 private String mobileId;
	
	public AlertPrsr() {
	}


	@Override
	public Alert parse() {
//		logger.info("\nParsing " + getClass() + "...\n");
		Alert alert = new Alert();
		try {			
//				logger.info("\nssetId: "+iAlrtId +"<----\n");
				alert.setId(iAlrtId);

				AlertType aTypes[] = AlertType.values();
				int i= 0;
				while (i <= aTypes.length && alert.getType() == null) {
					if (aTypes[i].getId() == iAlrtType)
						alert.setType(aTypes[i]);
					i++;
				}
				/*
				 * Save images urls if it's a photo alert 
				 */
				if (alert.getType() == AlertType.VIDEO) {
					if (sPrm2 != null) {
//						sPrm2 = sPrm2.replaceAll("localhost", "188.40.119.105");
//						logger.info("\ns-------> sPrm2: " + sPrm2 + "\n");
						String[] photoURLs = sPrm2.split("jpg");
						alert.setPhotoURLs(photoURLs);
					}
				}

				
				
				AlertState aStates[] = AlertState.values();
				i = 0;
				while(i <= aStates.length && alert.getCurrentState() == null) {
					if (aStates[i].getId() == iAlrtState)
						alert.setCurrentState(aStates[i]);
					i++;
				}
//				while(aStates[i].getId() != iAlrtState && i <= aStates.length) {
//					i++;
//				}
				if (sAlertInfo!=null)
					alert.setLowActivity(true);
				else
					alert.setLowActivity(false);
//				alert.setCurrentState(aStates[i]);
				alert.setStartDateTime(dAlrtStatusDateTime);
				alert.setTitle(sAlrtDescription);
				alert.setAlertSessionId(Integer.parseInt(iAlrtSessionId));
				
				
				if (location != null){
					alert.setLocation(location.parse());
				}
				
				alert.setEventCode(eventCode);
				alert.setDeviceType(deviceType);
				alert.setMobileId(mobileId);
				
			}catch(Exception ex) {
				throw new ParseException(ex,"Unexpected parse error");
			}
		return alert;
	}

	
	

	public String getiAlrtSessionId() {
		return iAlrtSessionId;
	}


	public void setiAlrtSessionId(String iAlrtSessionId) {
		this.iAlrtSessionId = iAlrtSessionId;
	}


	public String getiAlrtId() {
		return iAlrtId;
	}


	public void setiAlrtId(String iAlrtId) {
		this.iAlrtId = iAlrtId;
	}


	public int getiAlrtQId() {
		return iAlrtQId;
	}


	public void setiAlrtQId(int iAlrtQId) {
		this.iAlrtQId = iAlrtQId;
	}


	public int getiAlrtGrpId() {
		return iAlrtGrpId;
	}


	public void setiAlrtGrpId(int iAlrtGrpId) {
		this.iAlrtGrpId = iAlrtGrpId;
	}


	public int getiAlrtType() {
		return iAlrtType;
	}


	public void setiAlrtType(int iAlrtType) {
		this.iAlrtType = iAlrtType;
	}


	public String getsAlrtType() {
		return sAlrtType;
	}


	public void setsAlrtType(String sAlrtType) {
		this.sAlrtType = sAlrtType;
	}


	public int getiAlrtState() {
		return iAlrtState;
	}


	public void setiAlrtState(int iAlrtState) {
		this.iAlrtState = iAlrtState;
	}


	public Date getdAlrtStatusDateTime() {
		return dAlrtStatusDateTime;
	}


	public void setdAlrtStatusDateTime(Date dAlrtStatusDateTime) {
		this.dAlrtStatusDateTime = dAlrtStatusDateTime;
	}


	public String getsAlrtDescription() {
		return sAlrtDescription;
	}


	public void setsAlrtDescription(String sAlrtDescription) {
		this.sAlrtDescription = sAlrtDescription;
	}


	public String getsPrm1() {
		return sPrm1;
	}


	public void setsPrm1(String sPrm1) {
		this.sPrm1 = sPrm1;
	}


	public String getsPrm2() {
		return sPrm2;
	}


	public void setsPrm2(String sPrm2) {
		this.sPrm2 = sPrm2;
	}


	public String getsPrm3() {
		return sPrm3;
	}


	public void setsPrm3(String sPrm3) {
		this.sPrm3 = sPrm3;
	}


	public int getiSubStsGrpId() {
		return iSubStsGrpId;
	}


	public void setiSubStsGrpId(int iSubStsGrpId) {
		this.iSubStsGrpId = iSubStsGrpId;
	}


	public int getiPatientId() {
		return iPatientId;
	}


	public void setiPatientId(int iPatientId) {
		this.iPatientId = iPatientId;
	}


	public int getiCpId() {
		return iCpId;
	}


	public void setiCpId(int iCpId) {
		this.iCpId = iCpId;
	}


	public String getsAlertInfo() {
		return sAlertInfo;
	}


	public void setsAlertInfo(String sAlertInfo) {
		this.sAlertInfo = sAlertInfo;
	}


	public LocationPrsr getLocation() {
		return location;
	}


	public void setLocation(LocationPrsr location) {
		this.location = location;
	}


	public int getEventCode() {
		return eventCode;
	}


	public void setEventCode(int eventCode) {
		this.eventCode = eventCode;
	}


	public int getDeviceType() {
		return deviceType;
	}


	public void setDeviceType(int deviceType) {
		this.deviceType = deviceType;
	}


	public String getMobileId() {
		return mobileId;
	}


	public void setMobileId(String mobileId) {
		this.mobileId = mobileId;
	}
	
}
