package com.essence.hc.eil.parsers;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;

import com.essence.hc.eil.exceptions.ParseException;
import com.essence.hc.model.ActivityLevel;
import com.essence.hc.model.Patient;
import com.essence.hc.model.SystemStatus;

@JsonIgnoreProperties(ignoreUnknown = true)
public class MonitoringElementPrsr implements IParser<Patient> {

//	private Logger logger = LoggerFactory.getLogger(getClass());
	 
     private String iCpId;                   
     private SystemUserPrsr userInfo;
//     private PatientBasicInfoPrsr userInfo;
     private SystemStatusPrsr sysStatus;
     private ActivityLevelPrsr actvtStatus;
                  
	
	public MonitoringElementPrsr() {
	}


	@Override
	public Patient parse() {
		Patient patient = (Patient) userInfo.parse();
		SystemStatus systemStatus = new SystemStatus();			
		ActivityLevel activityLevel = new ActivityLevel();
			
		try {	
			
			patient.setCpId(iCpId);
			if (sysStatus!=null){
				systemStatus = sysStatus.parse();
				patient.setSystemStatus(systemStatus);
			}
			
			if (actvtStatus!=null){
				activityLevel = actvtStatus.parse();
				patient.setActivityLevel(activityLevel);
			}
			
			}catch(Exception ex) {
				throw new ParseException(ex,"Unexpected parse error");
			}
		return patient;
	}



	public String getiCpId() {
		return iCpId;
	}


	public void setiCpId(String iCpId) {
		this.iCpId = iCpId;
	}




	public SystemStatusPrsr getSysStatus() {
		return sysStatus;
	}


	public void setSysStatus(SystemStatusPrsr sysStatus) {
		this.sysStatus = sysStatus;
	}


	public ActivityLevelPrsr getActvtStatus() {
		return actvtStatus;
	}


	public void setActvtStatus(ActivityLevelPrsr actvtStatus) {
		this.actvtStatus = actvtStatus;
	}


	public SystemUserPrsr getUserInfo() {
		return userInfo;
	}


	public void setUserInfo(SystemUserPrsr userInfo) {
		this.userInfo = userInfo;
	}


		
}
