package com.essence.hc.persistenceexternal.impl;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;

import com.essence.hc.model.Patient;
import com.essence.hc.model.ResponseStatus;
import com.essence.hc.model.Alert;
import com.essence.hc.model.Alert.AlertHandlingConclusion;
import com.essence.hc.model.Alert.IssueType;
import com.essence.hc.model.Alert.ManualAlertType;
import com.essence.hc.model.Panel;
import com.essence.hc.persistence.exceptions.DataAccessException;
import com.essence.hc.persistence.impl.ExternalDAOImpl;
import com.essence.hc.persistenceexternal.ExternalAlertDAO;

public class ExternalAlertDAOImpl extends ExternalDAOImpl implements ExternalAlertDAO 
{
	@Override
	public ResponseStatus createManualAlert(Patient resident, String iAlrtType, String sAlertTxt, String deviceId) 
	{
		Panel myPanel = resident.getInstallation().getPanel();
		int panelId = Integer.valueOf(myPanel.getPanelId());
		int userId =  resident.getUserId();
		//String parameter1 = "";
		//String parameter2 = "";
		ManualAlertType alertType = null;
		String alertState = "ST_NEW";
		
		//TODO
		/*
		 * installId – this is the key which represents the system ID (identity column value in the DB) of the device in case of a technical error in a specific device.
   This is only relevant for the TechnicalDevice alert type (see below for current usage in internal web API)
   The parameter shouldn’t be required for other alert types

		 */
		
		HashMap<String, Object> reqParams = new HashMap<String, Object>();
		
		logger.info("************************************************");
		logger.info("iAlrtType: " + iAlrtType);
				
		if (iAlrtType.equals(IssueType.RESIDENT_SICK.toString())) 
		{
			logger.info("IssueType.RESIDENT_SICK: " + IssueType.RESIDENT_SICK);
            alertType = Alert.ManualAlertType.MANUAL_ALERT_TYPE_MEDICAL;
		} 
		else if (iAlrtType.equals(IssueType.GENERIC_INSTALLATION_ISSUE.toString())) 
		{
            logger.info("IssueType.GENERIC_INSTALLATION_ISSUE: " + IssueType.GENERIC_INSTALLATION_ISSUE);
            alertType = ManualAlertType.MANUAL_ALERT_TYPE_TECHNICAL_GENERAL;
        }
		else if (iAlrtType.equals(IssueType.DEVICE_ISSUE.toString())) 
		{
            logger.info("IssueType.DEVICE_ISSUE: " + IssueType.DEVICE_ISSUE);
            alertType = ManualAlertType.MANUAL_ALERT_TYPE_TECHNICAL_DEVICE;
            
            reqParams.put("installId", Integer.valueOf(deviceId));
		}
	
		reqParams.put("panelId", panelId);
		reqParams.put("userId", userId);
		reqParams.put("alertType", alertType.getValue());
		reqParams.put("alertState", alertState);
		reqParams.put("alertText", sAlertTxt);
		//reqParams.put("parameter1", parameter1);
		//reqParams.put("parameter2", parameter2);
				
		return performRequest(ResponseStatus.class, "external_create_manual_alert", reqParams);
	}
	
	@Override
	public ResponseStatus closeAlertSession(int accountNumber, int closeReasonCode, String closeReasonName, String handlingConclusionName, String handlingDescription, int sessionId){
		HashMap<String, Object> reqParams = new HashMap<String, Object>();
		
		HashMap<String, Object> closeReason = new HashMap<String, Object>();
		HashMap<String, Object> sessionData = new HashMap<String, Object>();
		
		reqParams.put("accountNumber", accountNumber);
		reqParams.put("overrideRequesterName", false);
		
		closeReason.put("reasonCode", closeReasonCode);
		reqParams.put("closeReason", closeReason);
		
		if(handlingConclusionName.equals(AlertHandlingConclusion.HANDLED_BY_USER.toString())){
			reqParams.put("handlingConclusion", AlertHandlingConclusion.HANDLED_BY_USER.getKey());
			
		}else if(handlingConclusionName.equals(AlertHandlingConclusion.FALSE_ALARM_BY_USER.toString())){
			reqParams.put("handlingConclusion", AlertHandlingConclusion.FALSE_ALARM_BY_USER.getKey());
			
		}else{
			reqParams.put("handlingConclusion", "");
		}
		
		reqParams.put("handlingDescription", handlingDescription);
		
		sessionData.put("sessionId", sessionId);
		reqParams.put("sessionData", sessionData);
		
		ResponseStatus response = performRequest(ResponseStatus.class, "external_close_events", reqParams);
		
		return response;
	}
}
