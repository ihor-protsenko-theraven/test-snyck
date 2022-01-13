/**
 * 
 */
package com.essence.hc.service.impl;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.essence.hc.exceptions.AppRuntimeException;
import com.essence.hc.model.Activity;
import com.essence.hc.model.ActivityIndex;
import com.essence.hc.model.ActivityLevel;
import com.essence.hc.model.ActivityReportDetail;
import com.essence.hc.model.Alert;
import com.essence.hc.model.Alert.AlertState;
import com.essence.hc.model.Alert.AlertType;
import com.essence.hc.model.DayStoryXPDevice;
import com.essence.hc.model.Device;
import com.essence.hc.model.Device.DeviceState;
import com.essence.hc.model.Report.ReportType;
import com.essence.hc.model.Event;
import com.essence.hc.model.Installation;
import com.essence.hc.model.Panel;
import com.essence.hc.model.Patient;
import com.essence.hc.model.ResponseStatus;
import com.essence.hc.model.StepCountAvailableReport;
import com.essence.hc.model.SystemStatus;
import com.essence.hc.model.SystemUser;
import com.essence.hc.model.User.UserType;
import com.essence.hc.model.Vendor.ServiceType;
import com.essence.hc.model.comparators.DeviceIndexForMonthlyReportComparator;
import com.essence.hc.model.comparators.EventStartTimeComparator;
import com.essence.hc.persistence.AlertDAO;
import com.essence.hc.persistence.PatientDAO;
import com.essence.hc.persistence.UserDAO;
import com.essence.hc.persistenceexternal.ExternalUsersDAO;
import com.essence.hc.service.PatientService;
import com.essence.hc.util.Util;

/**
 * @author oscar.canalejo
 *
 */
public class PatientServiceImpl implements PatientService {

	protected final Logger logger = LoggerFactory.getLogger(getClass());
	
	@Autowired
	PatientDAO dao;
	@Autowired
	UserDAO daoUser;
	@Autowired
	AlertDAO daoAlert;
	@Autowired
	private ExternalUsersDAO externalUsersDao;
	
	/* (non-Javadoc)
	 * @see com.essence.hc.service.PatientService#getPatientInfo(java.lang.String)
	 */
	@Override
	public Patient getPatient(String patientId) {
		logger.info("Getting Patient: " + patientId);
		Patient patient = null;
		patient = dao.getById(patientId);
		return patient;
	}

	/* (non-Javadoc)
	 * @see com.essence.hc.service.PatientService#getPatientInstallation(java.lang.String)
	 */
	@Deprecated
	@Override
	public Installation getPatientInstallation(String patientId) {
		return this.getPatient(patientId).getInstallation();
	}

	/* (non-Javadoc)
	 * @see com.essence.hc.service.PatientService#getInstallationDevices(java.lang.String)
	 */
	@Deprecated
	@Override
	public List<Device> getInstallationDevices(String patientId) {
		return this.getPatient(patientId).getInstallation().getDevices();
	}

	/* (non-Javadoc)
	 * @see com.essence.hc.service.PatientService#getActivityLevel(java.lang.String)
	 */
	@Override
	public ActivityLevel getActivityLevel(String patientId) {
//		Calendar cal = Calendar.getInstance();
//		Date timeUntil = cal.getTime();
//		cal.add(Calendar.HOUR_OF_DAY, -6);
//		Date timeFrom = cal.getTime();
//		ActivityLevel aLevel = dao.getActivityLevel(patientId, timeFrom, timeUntil);
		ActivityLevel aLevel = dao.getActivityLevel(patientId);
		return aLevel;
	}

	/* (non-Javadoc)
	 * @see com.essence.hc.service.PatientService#getActivityIndex(java.lang.String, java.util.Date)
	 */
	@Override
	@Deprecated
	public List<ActivityIndex> getActivityIndexOld(String residentId, Date fromDate, Date toDate){
		logger.warn("deprecated service: getActivityIndexOld");
		// changing toDate to take the day before excluding the last one
		toDate.setTime(toDate.getTime() - 24*60*60*1000);
		return dao.getActivityIndexOld(residentId, fromDate, toDate, "activity_index_report_old");
	}
	
	/* (non-Javadoc)
	 * @see com.essence.hc.service.PatientService#getActivityIndex(java.lang.String, java.util.Date)
	 */
	@Override
	public List<ActivityReportDetail> getActivityIndex(String residentId, Date fromDate, Date toDate){
		// changing toDate to take the day before excluding the last one
		toDate.setTime(toDate.getTime());
		return dao.getActivityIndex(residentId, fromDate, toDate);
	}
	
	/* (non-Javadoc)
	 * @see com.essence.hc.service.PatientService#getMonthlyReport(java.lang.String, java.util.Date, java.util.Date)
	 */
	@Override
	public List<ActivityIndex> getMonthlyReport(String residentId, Date fromDate, Date toDate) {
		// changing toDate to take the day before excluding the last one
		toDate.setTime(toDate.getTime());
		List<ActivityIndex> result = dao.getActivityIndexOld(residentId, fromDate, toDate, "monthly_report"); 
		for (ActivityIndex ai: result) {
			Collections.sort(ai.getDeviceIndexData(), new DeviceIndexForMonthlyReportComparator());
		}
		return result;
	}
	
	/* (non-Javadoc)
	 * @see com.essence.hc.service.PatientService#getDayStoryExpress(java.lang.String, java.util.Date, boolean)
	 */
	public Map<String, List<?>> getDayStoryXP(String residentId, Date date) {
		Map<String, List<?>> dayStoryMap = dao.getDayStoryXP(residentId, date);
		if (dayStoryMap != null) {
			Date dateTo = getCurrentPanelTime(dayStoryMap);
			for (DayStoryXPDevice xpDevice : ((List<DayStoryXPDevice>)dayStoryMap.get("Devices"))) {
				if(xpDevice.getPosaData() != null) {
					calcActivitiesDatesAndCells(xpDevice.getPosaData(), date, false, dateTo);
				}
				if(xpDevice.getActivityData() != null) {
					calcActivitiesDatesAndCells(xpDevice.getActivityData(), date, true, dateTo);
				}
			}
			calcActivitiesDatesAndCells((List<Activity>)dayStoryMap.get("OutOfHomeData"), date, false, dateTo);
		}
		return dayStoryMap;
	}

	public Date getCurrentPanelTime(Map<String, List<?>> dayStoryMap) {
		List<Date> currentPanelTimeList =  (List<Date>) dayStoryMap.get("CurrentPanelTime");
		Date dateTo = new Date();
		if (currentPanelTimeList != null && currentPanelTimeList.size() > 0){
			dateTo =  currentPanelTimeList.get(0);
		}
		return dateTo;
	}
	
	/**
	 * Calculates and sets both original and visible start/end times 
	 * for each activity in the specified collection.
	 * The calculation is done by comparing with a given comparison date
	 * (in the case of Day Story report, comparison date is the report requested date)
	 * @param activities
	 * @param compareDate
	 */
	private void calcActivitiesDates(List<Activity> activities, Date compareDate, Date dateTo) {
		
		SimpleDateFormat dateFormat = new SimpleDateFormat(Util.DATEFORMAT_INPUT);
		SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm:ss");

    	try {
			/*
			 * We need to compare activity and report date in order to know if 
			 * the activity started before and/or ended after the report date
			 * The comparison must be done in 'yyyy-MM-dd' format, without HH:mm part
			 */
    		compareDate = dateFormat.parse(dateFormat.format(compareDate)); // change format to '"yyyy-MM-dd' and convert it back to Date
    		
    		for (Iterator<Activity> it = activities.iterator(); it.hasNext();) {
    			Activity act = it.next();
    			calculateActivityStartDateTime(compareDate, dateFormat, timeFormat, act);
    			calculateActivityEndDateTime(compareDate, dateFormat, timeFormat, act, dateTo);
			}
    	}catch(ParseException ex) {
    		throw new AppRuntimeException(ex);
    	}
	}

	private void calculateActivityEndDateTime(Date compareDate, SimpleDateFormat dateFormat, SimpleDateFormat timeFormat, Activity act, Date dateTo) throws ParseException {
		Date endTime = (act.getOriginalEndTime() == null) ? dateTo:act.getOriginalEndTime();
		Date originalDate = dateFormat.parse(dateFormat.format(endTime));
		Date originalTime = timeFormat.parse(timeFormat.format(endTime));
		
		if(originalDate.after(compareDate)) {
			act.setEndDateTime(timeFormat.parse("23:59:59"));
			act.setOriginalEndTime(originalTime);
		}else{
			act.setEndDateTime(originalTime);
			act.setOriginalEndTime(originalTime);
		}
	}

	private void calculateActivityStartDateTime(Date compareDate, SimpleDateFormat dateFormat, SimpleDateFormat timeFormat, Activity act) throws ParseException {
		if(act.getOriginalStartTime() != null){
			Date originalDate = dateFormat.parse(dateFormat.format(act.getOriginalStartTime())); // change format to 'yyyy-MM-dd' and convert it back to Date
			Date originalTime = timeFormat.parse(timeFormat.format(act.getOriginalStartTime())); // change format to 'HH:mm' and convert it back to Date					
			
			if(originalDate.before(compareDate)) {
				act.setStartDateTime(timeFormat.parse("00:00:00"));
			} else {
				act.setStartDateTime(originalTime);
			}
			act.setOriginalStartTime(originalTime);
		}else{
			act.setStartDateTime(timeFormat.parse("00:00:00"));
		}
	}	
	
	/**
	 * Calculates and sets firstCell and lastCell (slots in which the activity span starts and ends in the 48-cell report) 
	 * for each activity in the specified collection.
	 * @param activities
	 * @param isDoor they are door activities
	 */
	private void calcActivitiesCells(List<Activity> activities, boolean isDoor) {
		if (activities == null) return;
		SimpleDateFormat dateFormat = new SimpleDateFormat(Util.DATEFORMAT_INPUT);	
		Activity prevAct = null;
		Activity prevCloseAct = null;
		Collections.sort(activities, new EventStartTimeComparator()); // the algorithm needs the activities to be ordered
		
		try {
			long referenceTime = (dateFormat.parse("1970-01-01")).getTime();
			
			for (Activity act : activities) {
				
				long threshold = 5 * 60 * 1000; // time that is dismissed if an activity takes shorter than that in a given cell
				if (act.getOriginalEndTime() == null) {
					threshold = 15 * 60 * 1000; // 15 minutes to approximate to nearest half an hour slot
				} else if (act!= null && act.getEndDateTime() != null && act.getStartDateTime() != null) {
					long duration = act.getEndDateTime().getTime() - act.getStartDateTime().getTime();
					if (duration < 2 * threshold) {
						threshold = duration / 2;
					}
				} 
										
				act.setFirstCell((int)((act.getStartDateTime().getTime() + threshold  - referenceTime) / (30*60*1000)));	
				
				if (isDoor) {
					act.setLastCell(act.getFirstCell());
					
					if (act.getDevice().getState() == DeviceState.ON) {
						if (prevAct != null && prevAct.getFirstCell() == act.getFirstCell()) {
							Activity container = prevAct;
							while (container.getContainedIn() != null) {
								container = container.getContainedIn();
							}
							container.addCoincidentActivity(act);
							act.setContainedIn(container);
						}
						prevAct = act;
					} else if (act.getDevice().getState() == DeviceState.OFF) {
						if (prevCloseAct != null && prevCloseAct.getFirstCell() == act.getFirstCell()) {
							Activity container = prevCloseAct;
							while (container.getContainedIn() != null) {
								container = container.getContainedIn();
							}
							container.addCoincidentActivity(act);
							act.setContainedIn(container);
						}
						prevCloseAct = act;
					}
			
				} else {
					if (act.getOriginalEndTime() == null) {
						act.setLastCell((int)((act.getEndDateTime().getTime() - referenceTime) / (30*60*1000)) + 1);
					} else {
						act.setLastCell((int)((act.getEndDateTime().getTime() - threshold  - referenceTime) / (30*60*1000)) + 1);
					}
					if (act.getFirstCell() == act.getLastCell()) {
						act.setLastCell(act.getLastCell() + 1);
					}
				
					// Collision avoidance system: prevent activities from overlapping
					if (prevAct != null && prevAct.getLastCell() > act.getFirstCell()) {
						
						int length1 = prevAct.getLastCell() - prevAct.getFirstCell();
						int length2 = act.getLastCell() - act.getFirstCell();
						
						if (length1 > 1 && length2 > 2) {
							// Both are long, remove a cell from the one that has less time in it
							long timeInCellPrev = (prevAct.getEndDateTime().getTime() - referenceTime) - (prevAct.getLastCell() - 1) * 30 * 60 * 1000 ;
							long timeInCellCurr = (act.getFirstCell() + 1) * 30 * 60 * 1000 - (act.getStartDateTime().getTime() - referenceTime);
							if (timeInCellPrev < timeInCellCurr) {
								// Previous activity has less time in the cell, make it shorter
								prevAct.setLastCell(act.getFirstCell());
							} else {
								// Current activity has less time in the cell, make it shorter
								act.setFirstCell(prevAct.getLastCell());
							}
						} else if (length1 > 1) {
							// Current one is 1-cell long, make previous activity a cell shorter
							prevAct.setLastCell(act.getFirstCell());
						} else if (length2 > 1) {
							// Previous one is 1-cell long, make current activity a cell shorter
							act.setFirstCell(prevAct.getLastCell());
						} else {
							// Both are 1-cell long, combine them
							Activity container = prevAct;
							while (container.getContainedIn() != null) {
								container = container.getContainedIn();
							}
							container.addCoincidentActivity(act);
							act.setContainedIn(container);
						}
					}
					
					prevAct = act;
				}
				
			}
						
		}catch(ParseException ex) {
			throw new AppRuntimeException(ex);
		}
	}
	
	/**
	 * Calculates and sets both original, visible start/end times and cells
	 * for each activity in the specified collection.
	 * The calculation is done by comparing with a given comparison date
	 * (in the case of Day Story report, comparison date is the report requested date)
	 * @param activities
	 * @param compareDate
	 * @param isDoor the collection is made up of door activities
	 */
	private void calcActivitiesDatesAndCells(List<Activity> activities, Date compareDate, boolean isDoor, Date dateTo) {
		calcActivitiesDates(activities, compareDate, dateTo);
		calcActivitiesCells(activities, isDoor);
	}
		
	/* (non-Javadoc)
	 * @see com.essence.hc.service.PatientService#getDayStory(java.lang.String, java.util.Date)
	 */
	public List<Event> getDayStory(String patientId, Date date, boolean wholeDay){

		if (wholeDay) {
		    Calendar cal = Calendar.getInstance();
		    cal.setTime(date);
		    cal.set(Calendar.HOUR, 23);
		    cal.set(Calendar.MINUTE, 59);
		    cal.set(Calendar.SECOND, 59);
		    date = cal.getTime();
		}
		return dao.getDayStory(patientId, date);
	}
	
	/* (non-Javadoc)
	 * @see com.essence.hc.service.PatientService#getEvents(java.lang.String, 
	 * java.util.Date, java.util.Date)
	 */
	public List<Event> getEvents(String patientId, Date timeFrom, Date timeUntil){
		return dao.getEvents(patientId, timeFrom, timeUntil);
	}

	/* (non-Javadoc)
	 * @see com.essence.hc.service.PatientService#getAlertHistory(java.lang.String, int, int, com.essence.hc.model.Alert.AlertType[], com.essence.hc.model.Alert.AlertState[])
	 */
	public List<Alert> getAlertHistory(String patientId, int lastAlertId, int page, int perPage, AlertType[] alertTypes, AlertState[] alertStates,String languageKey,int userId, UserType userType){
//		if (alertTypes == null) {
//			alertTypes = new AlertType[] {AlertType.ACTIVITY,AlertType.SOS,AlertType.TECHNICAL,AlertType.VIDEO,AlertType.ADVISORY};
//		}
		return dao.getAlerts(patientId, lastAlertId, page, perPage, alertTypes, alertStates,languageKey,userId,userType);
	}
	
	/* (non-Javadoc)
	 * @see com.essence.hc.service.PatientService#getAlertHistory(java.lang.String, int, int, com.essence.hc.model.Alert.AlertType[])
	 */
	public List<Alert> getAlertHistory(String patientId, int page, int perPage, AlertType[] alertTypes){
		return dao.getAlerts(patientId, page, perPage, alertTypes);
	}

	/* (non-Javadoc)
	 * @see com.essence.hc.service.PatientService#getSystemStatus(java.lang.String)
	 */
	@Override
	public SystemStatus getSystemStatus(String patientId,String languageKey) {
		return dao.getSystemStatus(patientId,languageKey);
	}
	
	
	
	/* (non-Javadoc)
	 * @see com.essence.hc.service.PatientService#getPatientAlarms(java.lang.String)
	 */
	@Override
	public List<Patient> getPatientsInfo(String userId, String vendorId, int rolId, int page, int perPage, 
			String sNameFltr, String sIdFltr, String orderBy, Boolean sortAsc, String[] panelTypeFilter, ServiceType[] serviceTypeFilter, Boolean enableActiveService) {
		
		List<Patient> patientsInfo = dao.getPatientsInfo(userId, vendorId, rolId,page,perPage,sNameFltr,sIdFltr, orderBy, sortAsc, panelTypeFilter, serviceTypeFilter, enableActiveService);
		
		return setAvailableReports(patientsInfo);
	}
	
	private List<Patient> setAvailableReports(List<Patient> patients) {
		
		for(Patient patient : patients) {
			patient.setAvailableReports(patient.obtainDefaultAvailableReports());
			if(isAvailableStepCountReport(patient)) {
				patient.addAvailableReport(ReportType.STEP_COUNT);
			}
		}
		
		return patients;
	}
	
	private boolean isAvailableStepCountReport(Patient patient) {
		
		if(patient.getActivityLevel() != null) {
			List<StepCountAvailableReport> stepCountReports = patient.getActivityLevel().getStepCountAvailableReport();
			
			StepCountAvailableReport stepCountReport = null;
			Iterator<StepCountAvailableReport> it = stepCountReports.iterator();
			boolean showStepCountReportOption = false;
			
			while(it.hasNext() && showStepCountReportOption == false){
				stepCountReport = it.next();
				if (stepCountReport.getFirstStepCount() != null){
					return true;
				}
			}
		}
		
		return false;
	}
	
	
	/* (non-Javadoc)
	 * @see com.essence.hc.service.PatientService#getPatientReport(java.lang.String)
	 */
	@Override
	public Patient getPatientReport(String vendorId) {
		return dao.getPatientReport(vendorId);
	}
	
	@Override
	public List<Patient> getCareGiverPatients(String iUserId, String iVendorId, int iNumOfItems, String iPageNum,
			String sSearchCriteria, String sCriteriaValue, ServiceType[] serviceTypeFilter, Boolean enableActiveService){
		
		List<Patient> patients = dao.getCareGiverPatients(iUserId, iVendorId, iNumOfItems, iPageNum, sSearchCriteria, sCriteriaValue, serviceTypeFilter, enableActiveService);
		//if (assigned) markPatientsAssigned(patients);
		return patients;
	}
	
	@Override
	public List<SystemUser> getPatientCareGivers(String iUserId, String iVendorId, int iNumOfItems, String iPageNum,
			String sSearchCriteria, String sCriteriaValue){
		
		List<SystemUser> lstCaregivers = dao.getPatientCareGivers(iUserId, iVendorId, iNumOfItems, 
				iPageNum, sSearchCriteria, sCriteriaValue);
		//if (assigned) markAssignedSystemUsers(lstCaregivers);
		return lstCaregivers;
	}
	
	@Override
	public List<SystemUser> getPatientCareGivers(Patient patient, String sSearchCriteria, String sCriteriaValue) {
		List<UserType> userTypes = new ArrayList<UserType>();
		userTypes.add(UserType.ROLE_CAREGIVER);
		List<SystemUser> caregivers = externalUsersDao.getUsersForAccount(patient.getInstallation().getPanel(), userTypes, null);
		
		for (Iterator<SystemUser> it = caregivers.iterator(); it.hasNext();) {
			SystemUser c = it.next();
			c.setAssigned(true);
			if (!meetsSearch(c, sSearchCriteria, sCriteriaValue)) {
				it.remove();
			}
		}		
		
		return caregivers;
	}
	
	private boolean meetsSearch(SystemUser user, String searchCriteria, String value) {
		if (value == null || value.isEmpty()) {
			return true;
		}
		
		// EIC15-2456: Filters updated according to changes in ResidentCode / AccountNumber
		
		String field = "";
		try {
			if ("nameFilter".equals(searchCriteria)) {
				field = user.getFirstName() + ' ' + user.getLastName();
			} else if ("accountFilter".equals(searchCriteria)) {
				field = user.getAccountNumber();
			} else if ("generalIdFilter".equals(searchCriteria)) {
				field = user.getGeneralId();
			} else if ("addressFilter".equals(searchCriteria)) {
				field = user.getAddress();
			} else if ("mobileFilter".equals(searchCriteria)) {
				field = user.getMobile();
			} else if ("landlineFilter".equals(searchCriteria)) {
				field = user.getPhone();
			} else if ("resCodeFilter".equals(searchCriteria) && user.isResident()) {
				field = ((Patient) user).getResidentCode();
			}
			return field.toLowerCase().contains(value.toLowerCase());
		} catch (Exception e) {
			return false;
		}
	}
	
	/* (non-Javadoc)
	 * @see com.essence.hc.service.PatientService#getAddUserAssign(int)
	 */
	@Override
	public ResponseStatus assignCaregiver(Patient resident, SystemUser caregiver, boolean master, boolean isAdd) {
		return externalUsersDao.assignCaregiver(resident, caregiver, new Boolean(master), new Boolean(isAdd), null, null);
	}
	
	@Override
	public ResponseStatus assignNewCaregiver(Patient resident, SystemUser caregiver, boolean master) {
		return externalUsersDao.addUserAndAssign(resident, caregiver, new Boolean(master), null, null);
	}
	
	/* (non-Javadoc)
	 * @see com.essence.hc.service.PatientService#DlUserAssign(int)
	 */
	@Override
	public ResponseStatus unassignCaregiver(Patient resident, SystemUser caregiver) {
		return externalUsersDao.unassignCaregiver(resident, caregiver);
//		return dao.DlUserAssign(resident.getUserId(), caregiver.getUserId(), "Del");
	}

	/**
	 * With this method the Patient objects passed in are checked like assigned.
	 * The result returned is the same List modified.
	 * 
	 * @param patients
	 * @return {@link List<SystemUser>}
	
	private List<Patient> markPatientsAssigned(List<Patient> patients) {
		if (patients == null) return null;
		
		Iterator<Patient> iter = patients.iterator();
		
		while(iter.hasNext()) {
			iter.next().setAssigned(true);
		}
		
		return patients;
	} */
	
	/**
	 * With this method we obtain the cameras of a patient
	 * 
	 * @param userId
	 * @return {@link List<Device>}
	 */
	@Override
	public List<Device> getCameraList (String userId){
		return dao.getCameraList(userId);
	}
	
	/**
	 * With this method we obtain the photos of a camera
	 * 
	 * @param userId
	 * @return {@link List<Device>}
	 */
	@Override
	public String getPhotos (String userId, Date fromDate, Date toDate){
		return dao.getPhotos(userId, fromDate, toDate);
	}
	
	/**
	 * Request Photo
	 * 
	 * @param userId
	 * @return {@link List<Device>}
	 */
	@Override
	public ResponseStatus requestPhoto (String caregiverId,String residenId, String deviceId, String alertSessionId){
		return dao.requestPhoto(caregiverId, residenId, deviceId, alertSessionId);
	}
	
	public void setEmergencyCallRelatedInfoForPatient(Patient patient) {
		
		// EIC15-1416
		
		Panel panel = patient.getInstallation().getPanel();
		
		List<UserType> userTypes = new ArrayList<UserType>();
		userTypes.add(UserType.ROLE_MONITORED);
		
		List<Patient> patients = new ArrayList<Patient>();
		for(SystemUser su : externalUsersDao.getUsersForAccount(panel, userTypes, null)) {
			if (su.getUserId() == patient.getUserId()) {
				patient.setPatientSettings(((Patient)su).getPatientSettings());
			}
		}
	}

}
