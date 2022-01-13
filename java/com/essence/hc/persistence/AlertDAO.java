/**
 * 
 */
package com.essence.hc.persistence;

import java.util.List;
import java.util.Map;

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

/**
 * @author oscar.canalejo
 *
 */
public interface AlertDAO {

	
	public Alert getAlert(String id,String languageKey);
	
	public LowActivityReport getLowActivityReport(String alertId, String languageKey);
	
	@Deprecated
	public Alert getById(String id);
	
	public List<Alert> getByUserId(String userId);
	
	public void update(Alert alert);
	
	public ResponseStatus updateAlertState(int alertId, AlertState newState, int patientId);
	
	public void save(Alert alert);
	
	public List<AlertCloseReason> getAlertCloseReasons(String languageKey);

	public ResponseStatus closeAlertSession(String alertId, String userId, String patientId, String sessionId, int reasonCode, String message);

	public EventLog eventLog(String userId, String patientId, String sessionId, String logId,String languageKey,UserType userType);

	public List<Alert> getSessionAlerts(String userId, String patientId, String sessionId);

	public List<Patient> getPatientsInAlarm(int userId, int vendorId, int rolId, String orderBy, Boolean sortAsc, ServiceType[] serviceTypeFilter, Boolean enableActiveService);
	
	public List<Patient> getAlertCount(User user);
	
	public List<ResponseStatus> GetManualAlertTypes();
	
	public ResponseStatus setManualAlert(int userID, int devicID, int alert);

	public Map<String, Boolean> getAlertConfiguration(String userId, String residentId, String userType);
	
	public ResponseStatus createManualAlert (String iUserId, String iAlrtType, String sAlertTxt, String deviceId);
}
