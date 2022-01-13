/**
 * 
 */
package com.essence.hc.service;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import com.essence.hc.model.ActivityIndex;
import com.essence.hc.model.ActivityLevel;
import com.essence.hc.model.ActivityReportDetail;
import com.essence.hc.model.Alert;
import com.essence.hc.model.Alert.AlertState;
import com.essence.hc.model.Alert.AlertType;
import com.essence.hc.model.Device;
import com.essence.hc.model.Event;
import com.essence.hc.model.Installation;
import com.essence.hc.model.Panel;
import com.essence.hc.model.Patient;
import com.essence.hc.model.ResponseStatus;
import com.essence.hc.model.SystemStatus;
import com.essence.hc.model.SystemUser;
import com.essence.hc.model.User.UserType;
import com.essence.hc.model.Vendor.ServiceType;

/**
 * Patient Service
 * @author oscar.canalejo
 *
 */
@Service
public interface PatientService {
	
	@PreAuthorize("isAuthenticated()")
	public Patient getPatient(String patientId);
	
	@Deprecated
	@PreAuthorize("isAuthenticated()")
	public Installation getPatientInstallation(String patientId);
	
	@Deprecated
	@PreAuthorize("isAuthenticated()")
	public List<Device> getInstallationDevices(String patientId);
	/**
	 * Get last 6 hours of Activity Detection Levels 
	 * registered by the patient's installation devices
	 * @param patientId The patient identification
	 * @return Activity Level object with detailed info 
	 */
	@PreAuthorize("isAuthenticated()")
	public ActivityLevel getActivityLevel(String patientId);

	/**
	 * Get Patient's Day Story info (patient's activity in a specific date) 
	 * @param patientId patient identification
	 * @param date selected date
	 * @return an {@link Event} list with the events retrieved 
	 */
	@PreAuthorize("isAuthenticated()")
	public List<Event> getDayStory(String patientId, Date date, boolean wholeDay);
	
	/**
	 * Get Day Story report. Only for residents of service type Express 
	 * @param patientId
	 * @param date
	 * @return
	 */
	@PreAuthorize("isAuthenticated()")
	public Map<String, List<?>> getDayStoryXP(String patientId, Date date);
	
	/**
	 * Activity Index Report. Only for residents of service type Express
	 * @param residentId
	 * @param parsedDate
	 * @return
	 */
	@PreAuthorize("isAuthenticated()")
	@Deprecated
	public List<ActivityIndex> getActivityIndexOld(String residentId, Date fromDate, Date toDate);	

	/**
	 * Activity Index Report by Week. Only for residents of service type Express. Add in ADL 2.2
	 * @param residentId
	 * @param parsedDate
	 * @return
	 */
	@PreAuthorize("isAuthenticated()")
	public List<ActivityReportDetail> getActivityIndex(String residentId, Date fromDate, Date toDate);	
	
	/**
	 * Monthly Report. Only for residents of service type Express
	 * @param residentId
	 * @param fromDate
	 * @param toDate
	 * @return
	 */
	@PreAuthorize("isAuthenticated()")
	public List<ActivityIndex> getMonthlyReport(String residentId, Date fromDate, Date toDate);

	/**
	 * Get Patient's Events (Activities and Alerts) 
	 * registered between two dates or two times in a day
	 * @param patientId The patient identification
	 * @param timeFrom Date/Time from
	 * @param timeUntil Date/Time until
	 * @return an {@link Event} list with the events found 
	 */
	@PreAuthorize("isAuthenticated()")
	public List<Event> getEvents(String patientId, Date timeFrom, Date timeUntil);

	/**
	 * Retrieve History of Patient's Alerts
	 * @param patientId The patient identification
	 * @param lastAlertId id of the last alert received, for pagination purposes
	 * @param timeFrom Date/Time from
	 * @param timeUntil Date/Time until
	 * @param type Type of alert wanted. Must be a {@link AlertType} enum value. A null value means all.
	 * @param state State the alerts must be in. Must be a value from the {@link AlertState} enum. A null value means all.
	 * @param reqName name of the request (mainly used to determine the name of the file in dummy mode)
	 * @return an {@link Alert} list with those alerts that match the passed params 
	 */
	@PreAuthorize("isAuthenticated()")
	public List<Alert> getAlertHistory(String id, int lastAlertId, int page, int perPage,AlertType[] type, AlertState[] state,String languageKey, int userId, UserType userType);
	
	@Deprecated
	@PreAuthorize("isAuthenticated()")
	public List<Alert> getAlertHistory(String id, int page, int perPage,AlertType[] type);
	
	/**
	 * Return Patient's info about Alert Status
	 * (is the patient in alarm?)
	 * @param patientId The patient identification
	 * @return {@link SystemStatus}
	 */
	@PreAuthorize("isAuthenticated()")
	public SystemStatus getSystemStatus(String patientId,String languageKey);
	
	
	
	/**
	 * Return a List of Patients, -including their basic + activity info-,
     * associated with the specified Vendor/Caregiver (depending on the user's rol)
	 * @param vendortId The user identification
	 * @return {@link SystemStatus}
	 */
	@PreAuthorize("isAuthenticated()")
	public List<Patient> getPatientsInfo(String userId, String vendorId, int rolId, int page, int perPage, 
			String sNameFltr, String sIdFltr, String orderBy, Boolean sortAsc, String[] panelTypeFilter, ServiceType[] serviceTypeFilter, Boolean enableActiveService);
	
	
	/**
	 * Return Patient's Report  
	 * @param vendortId The user identification
	 * @return {@link SystemStatus}
	 */
	@PreAuthorize("isAuthenticated()")
	public Patient getPatientReport(String vendorId);
	

	@PreAuthorize("isAuthenticated()")
	public List<Patient> getCareGiverPatients(String iUserId, String iVendorId, int iNumOfItems, String iPageNum, 
			 String sSearchCriteria, String sCriteriaValue, ServiceType[] serviceTypeFilter, Boolean enableActiveService);
	
	@PreAuthorize("isAuthenticated()")
	public List<SystemUser> getPatientCareGivers(String iUserId, String iVendorId, int iNumOfItems, String iPageNum, 
			 String sSearchCriteria, String sCriteriaValue);
	
	@PreAuthorize("isAuthenticated()")
	public List<SystemUser> getPatientCareGivers(Patient patient, String sSearchCriteria, String sCriteriaValue);
	
	/**
	 * Returns the resident caregivers (assigned or unassigned).
	 * 
	 * @param iUserId
	 * @param iNumOfItems
	 * @param iPageNum
	 * @param bOrderedBy Tells the method which caregivers are showed the first ones, true (assigned), false (unassigned)
	 * @param sSearchCriteria
	 * @param sCriteriaValue
	 * @param includeAdmin
	 * @return {@link List<SystemUser>}
	 
	@PreAuthorize("isAuthenticated()")
	public List<SystemUser> getPatientCaregiversOrdered(String iUserId, String iVendorId, int iNumOfItems, String iPageNum, 
			boolean bOrderedBy, String sSearchCriteria, String sCriteriaValue, boolean includeAdmin);
	*/
	/**
	 * Returns the resident caregivers (assigned or unassigned).
	 * 
	 * @param iUserId
	 * @param iNumOfItems
	 * @param iPageNum
	 * @param bOrderedBy Tells the method which caregivers are showed the first ones, true (assigned), false (unassigned)
	 * @param includeAdmin
	 * @return {@link List<SystemUser>}
	 
	@PreAuthorize("isAuthenticated()")
	public List<SystemUser> getPatientCaregiversOrdered(String iUserId, String iVendorId, int iNumOfItems, String iPageNum, 
			boolean bOrderedBy, boolean includeAdmin);
	*/
	
	/**
	 * Returns the resident patients (assigned or unassigned).
	 * 
	 * @param iUserId
	 * @param iNumOfItems
	 * @param iPageNum
	 * @param bOrderedBy Tells the method which patients are showed the first ones, true (assigned), false (unassigned)
	 * @param sSearchCriteria
	 * @param sCriteriaValue
	 * @return {@link List<Patient>}

	@PreAuthorize("isAuthenticated()")
	public List<Patient> getCaregiverPatientsOrdered(String iUserId, String iVendorId, int iNumOfItems, String iPageNum, 
			boolean bOrderedBy, String sSearchCriteria, String sCriteriaValue);
	
	/**
	 * Returns the resident patients (assigned or unassigned).
	 * 
	 * @param iUserId
	 * @param iNumOfItems
	 * @param iPageNum
	 * @param bOrderedBy Tells the method which patients are showed the first ones, true (assigned), false (unassigned)
	 * @return {@link List<Patient>}
	 
	@PreAuthorize("isAuthenticated()")
	public List<Patient> getCaregiverPatientsOrdered(String iUserId, String iVendorId, int iNumOfItems, String iPageNum, 
			boolean bOrderedBy);
	 */
	/**
	 * Assign Caregiver  
	 * @param resident
	 * @param caregiver
	 * @return ResponseStatus
	 */
	@PreAuthorize("isAuthenticated()")
	public ResponseStatus assignCaregiver(Patient resident, SystemUser caregiver, boolean master, boolean isAdd);
	
	/**
	 * Unassign Caregiver  
	 * @param iEdtUsr
	 * @return ResponseStatus
	 */
	@PreAuthorize("isAuthenticated()")
	public ResponseStatus unassignCaregiver(Patient resident, SystemUser caregiver);
	
	/**
	 * Create and assign Caregiver  
	 * @param resident
	 * @param caregiver
	 * @return ResponseStatus
	 */
	@PreAuthorize("isAuthenticated()")
	public ResponseStatus assignNewCaregiver(Patient resident, SystemUser caregiver, boolean master);
	
	/**
	 * Get List Cameras  
	 * @param userId
	 * @return List<Device>
	 */
	@PreAuthorize("isAuthenticated()")
	public List<Device> getCameraList(String iUserId);
	
	
	/**
	 * Get patient photos
	 * @param userId, fromDate, toDate
	 * @return List<Device>
	 */
	@PreAuthorize("isAuthenticated()")
	public String getPhotos (String userId, Date fromDate, Date toDate);
	
	/**
	 * requestPhoto
	 * @param userId, deviceId
	 * @return List<Device>
	 */
	@PreAuthorize("isAuthenticated()")
	public ResponseStatus requestPhoto (String caregiverId,String residenId, String deviceId, String alertSessionId);
	
	@PreAuthorize("isAuthenticated()")
	public Date getCurrentPanelTime(Map<String, List<?>> dayStoryMap);
	
	public void setEmergencyCallRelatedInfoForPatient(Patient patient);
}
