package com.essence.hc.service;

import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.springframework.context.MessageSource;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import com.essence.hc.model.Alert;
import com.essence.hc.model.Alert.AlertState;
import com.essence.hc.model.AlertCloseReason;
import com.essence.hc.model.User.UserType;
import com.essence.hc.model.Vendor.ServiceType;
import com.essence.hc.model.EventLog;
import com.essence.hc.model.LowActivityReport;
import com.essence.hc.model.Patient;
import com.essence.hc.model.ResponseStatus;
import com.essence.hc.model.User;

@Service
public interface AlertService {

	@PreAuthorize("isAuthenticated()")
	public Alert getAlert(String id,String languageKey);
	@PreAuthorize("isAuthenticated()")
	public LowActivityReport getLowActivityReport(String alertId,String languageKey);
	@PreAuthorize("isAuthenticated()")
	public ResponseStatus updateAlertState(int alertId, AlertState newState, int patientId);
	@PreAuthorize("isAuthenticated()")
	public List<AlertCloseReason> getAlertCloseReasons(String languageKey);
	@PreAuthorize("isAuthenticated()")
	public ResponseStatus closeAlertSession(int accountNumber, int closeReasonCode, String closeReasonName, String handlingConclusionName, String handlingDescription, int sessionId);
	@PreAuthorize("isAuthenticated()")
	public EventLog eventLog(String userId, String patientId, String sessionId, String LogId,String languageKey,UserType userType,MessageSource msgSrc,Locale locale);
	@PreAuthorize("isAuthenticated()")
	public void saveAlert(String userId, Alert alert);
	@PreAuthorize("isAuthenticated()")
	public List<Alert> getSessionAlerts(String userId, String patientId, String sessionId);
	@PreAuthorize("isAuthenticated()")
	public List<Patient> getPatientsInAlarm(int userId, int vendorId,int rolId, String orderBy, Boolean sortAsc, ServiceType[] serviceTypeFilter, Boolean enableActiveService);
	@PreAuthorize("isAuthenticated()")
	public List<Patient> getAlertCount(User user);
	@PreAuthorize("isAuthenticated()")
	public List<ResponseStatus> GetManualAlertTypes();
	@PreAuthorize("isAuthenticated()")
	public ResponseStatus setManualAlert(int userID, int devicID, int alert);
	@PreAuthorize("isAuthenticated()")
	public Map<String, Boolean> getAlertConfiguration(String userId, String userType , String residentId);
	@PreAuthorize("isAuthenticated()")
	public ResponseStatus createManualAlert (Patient resident, String iAlrtType, String sAlertTxt, String deviceId);
	
	

}
