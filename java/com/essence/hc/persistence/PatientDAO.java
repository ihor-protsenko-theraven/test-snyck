/**
 * 
 */
package com.essence.hc.persistence;

import java.util.Date;
import java.util.List;
import java.util.Map;

import com.essence.hc.model.Activity;
import com.essence.hc.model.ActivityIndex;
import com.essence.hc.model.ActivityLevel;
import com.essence.hc.model.ActivityReportDetail;
import com.essence.hc.model.Alert;
import com.essence.hc.model.Alert.AlertState;
import com.essence.hc.model.Alert.AlertType;
import com.essence.hc.model.Device;
import com.essence.hc.model.Event;
import com.essence.hc.model.Patient;
import com.essence.hc.model.ResponseStatus;
import com.essence.hc.model.SystemStatus;
import com.essence.hc.model.SystemUser;
import com.essence.hc.model.User.UserType;
import com.essence.hc.model.Vendor.ServiceType;

/**
 * @author oscar.canalejo
 *
 */
public interface PatientDAO {

	
	public Patient getById(String id);
	
	public List<Patient> getByUserId(String userId);

	/**
	 * Retrive Patient's current activity/movement level data registered
	 * @param patientId patient identification
	 * @return current activity level data for the specified patient
	 */
	public ActivityLevel getActivityLevel(String patientId);
	
	/**
	 * Get Day Story for Express Residents 
	 * @param residentId
	 * @param date
	 */
	public Map<String, List<?>> getDayStoryXP(String residentId, Date date);
	
	/**
	 * Get Activity Index data for Express Residents
	 * @param residentId
	 * @param fromDate
	 * @param toDate
	 * @param reportName
	 * @return
	 */
	public List<ActivityIndex> getActivityIndexOld(String residentId, Date fromDate, Date toDate, String reportName);


	/**
	 * Get Activity Report Detail data for Express Residents
	 * @param residentId
	 * @param fromDate
	 * @param toDate
	 * @return
	 */
	public List<ActivityReportDetail> getActivityIndex(String residentId, Date fromDate, Date toDate);
	
	/**
	 * Retrive Patient's Events occurred at a specific date
	 * @param patientId patient identification
	 * @param date Current day or another specific date 
	 * @return the corresponding {@link Event} list
	 */
	public List<Event> getDayStory(String patientId, Date date);
	
	/**
	 * Retrieve Patient's Events (Activities, Alerts) occurred in a date-time period 
	 * @param patientId patient identification
	 * @param timeFrom Date/Time from
	 * @param timeUntil Date/Time until
	 * @return an {@link Event} list with events that match the passed params. 
	 */
	public List<Event> getEvents(String patientId, Date timeFrom, Date timeUntil);

	/**
	 * Retrieve Patient's Alerts
	 * @param patientId patient identification
	 * @param lastAlertId id of the last alert received, for pagination purposes
	 * @param page page to retrieve
	 * @param perPage number of elements to retrieve
	 * @param alertTypes an array of {@link AlertType} values for filtering the result. 
	 * 	A null value means no filtering (return all).
	 * @param alertStates an array of {@link AlertState} values for filtering the result. 
	 * 	A null value means no filtering (return all).
	 * @return an {@link Alert} list with those alerts that match the passed params
	 */
	public List<Alert> getAlerts(String patientId, int lastAlertId, int page, int perPage, AlertType[] alertTypes, AlertState[] alertStates,String languageKey,int userId, UserType userType);

	@Deprecated
	public List<Alert> getAlerts(String patientId, Date timeFrom, Date timeUntil,
								   AlertType type, AlertState state);
	@Deprecated
	public List<Alert> getAlerts(String patientId, int page, int perPage, AlertType[] alertTypes);

	/**
	 * Patient's System Status Retrieval
	 * @param patientId patient identification
	 * @return {@link SystemStatus}
	 */
	public SystemStatus getSystemStatus(String patientId,String languageKey);
	
	
	
	/**
	 * Patient's Information
	 * @param vendorId user identification
	 * @return {@link SystemStatus}
	 */
	public List<Patient> getPatientsInfo(String userId, String vendorId, int rolId,  int page, int perPage, 
			String NameFltr, String IdFltr, String orderBy, Boolean sortAsc, String[] panelTypeFilter, ServiceType[] serviceTypeFilter, Boolean enableActiveService);
	
	
	/**
	 * Patient's Information
	 * @param vendorId user identification
	 * @return {@link SystemStatus}
	 */
	public Patient getPatientReport(String vendorId);
	
	/**
	 * List of Patients assigned or assignable to a Caregiver
	 * @param iUserId
	 * @param iNumOfItems
	 * @param iPageNum
	 * @param assigned
	 * @param sSearchCriteria
	 * @param sCriteriaValue
	 * @return
	 */
	public List<Patient> getCareGiverPatients(String iUserId, String iVendorId, int iNumOfItems, 
			String iPageNum, String sSearchCriteria, String sCriteriaValue, ServiceType[] serviceTypeFilter, Boolean enableActiveService);
	
	/**
	 * List of CareGivers assigned or assignable to a Patient  
	 * @param iUserId
	 * @param iNumOfItems
	 * @param iPageNum
	 * @param assigned
	 * @param sSearchCriteria
	 * @param sCriteriaValue
	 * @param includeAdmin
	 * @return
	 */
	public List<SystemUser> getPatientCareGivers(String iUserId, String iVendorId, int iNumOfItems, String iPageNum, String sSearchCriteria, String sCriteriaValue);
	
	
	/**
	 * Assign Caregiver  
	 * @param iEdtUsr
	 * @return ResponseStatus
	 */
	public ResponseStatus UserAssignCmd(int iUid, int iAsgnId, String sAsgnCmd );
	
	/**
	 * Unassign Caregiver  
	 * @param iEdtUsr
	 * @return ResponseStatus
	 */
	public ResponseStatus DlUserAssign(int iUid, int iAsgnId, String sAsgnCmd );
	
	/**
	 * Get List Cameras  
	 * @param userId
	 * @return List<Device>
	 */
	public List<Device> getCameraList (String userId);
	
	
	/**
	 * Get patient photos from camera device
	 * @param userId, fromDate, toDate
	 * @return List<Device>
	 */
	public String getPhotos (String userId, Date fromDate, Date toDate);
	
	
	/**
	 * Request Photo
	 * @param userId, deviceId
	 * @return List<Device>
	 */
	public ResponseStatus requestPhoto (String caregiverId,String residenId, String deviceId, String alertSessionId);

}
