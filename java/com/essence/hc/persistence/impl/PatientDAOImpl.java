package com.essence.hc.persistence.impl;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.cache.Cache;
import org.springframework.cache.Cache.ValueWrapper;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.Cacheable;

import com.essence.hc.eil.requestprocessors.RequestProcessor;
import com.essence.hc.eil.requestprocessors.impl.DummyRequestProcessor;
import com.essence.hc.model.Activity.ActivityType;
import com.essence.hc.model.ActivityIndex;
import com.essence.hc.model.ActivityLevel;
import com.essence.hc.model.ActivityReportDetail;
import com.essence.hc.model.Alert;
import com.essence.hc.model.Alert.AlertState;
import com.essence.hc.model.Alert.AlertType;
import com.essence.hc.model.Device;
import com.essence.hc.model.Event;
import com.essence.hc.model.MissingInformation.Reason;
import com.essence.hc.model.MissingInformationActivityWrapper;
import com.essence.hc.model.Patient;
import com.essence.hc.model.ResponseStatus;
import com.essence.hc.model.SystemStatus;
import com.essence.hc.model.SystemUser;
import com.essence.hc.model.User.UserType;
import com.essence.hc.model.Vendor.ServiceType;
import com.essence.hc.persistence.PatientDAO;
import com.essence.hc.persistence.exceptions.DataAccessException;
import com.essence.hc.util.Util;

public class PatientDAOImpl implements PatientDAO {

	protected final Logger logger = LoggerFactory.getLogger(getClass());

	@Autowired
	@Qualifier("requestProcessor")
	RequestProcessor reqProcessor;
	@Autowired
	private CacheManager cacheManager;

	private static final String PATIENTS_CACHE = "patients";
	private static final String PATIENT_EVENTS_CACHE = "patient_events";

	private static final int DEFAULT_PER_PAGE = 10;

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.essence.hc.persistence.PatientDAO#getByUserId(java.lang.String)
	 */
	public List<Patient> getByUserId(String userId) {
		HashMap<String, String> reqParams = new HashMap<String, String>();
		reqParams.put("userId", userId);
		List<Patient> s = null;
		Patient[] patients = reqProcessor.performRequest(Patient[].class, "patients", reqParams);
		if (patients != null) {
			s = new ArrayList<Patient>();
			for (Patient newPatient : patients) {
				s.add(newPatient);
				this.put(newPatient.getStringUserId(), newPatient);
			}
		}
		return s;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.essence.hc.persistence.PatientDAO#getById(java.lang.String)
	 */
	@Override
	// @Cacheable(PATIENTS_CACHE)
	public Patient getById(String patientId) {
		Patient patient = this.get(patientId);
		if (patient == null) {
			HashMap<String, String> reqParams = new HashMap<String, String>();
			reqParams.put("patientId", patientId);
			patient = reqProcessor.performRequest(Patient.class, "patient", reqParams);
			if (patient != null) {
				this.put(patient.getStringUserId(), patient);
			}
		}
		return patient;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.essence.hc.persistence.PatientDAO#getActivityLevel(java.lang.String,
	 * java.util.Date, java.util.Date)
	 */
	@Override
	public ActivityLevel getActivityLevel(String patientId) {
		HashMap<String, String> reqParams = new HashMap<String, String>();
		reqParams.put("iPatientid", patientId);
		return reqProcessor.performRequest(ActivityLevel.class, "activity_level", reqParams);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.essence.hc.persistence.PatientDAO#getDayStory(java.lang.String,
	 * java.util.Date)
	 */
	@Cacheable(value = PATIENT_EVENTS_CACHE, key = "#p0.concat('-').concat(#p1.getTime())")
	// @Cacheable(value=PATIENT_EVENTS_CACHE,
	// key="#p0.concat('-').concat(#p1.getYear()).concat(#p1.getMonth()).concat(#p1.getDay())")
	public List<Event> getDayStory(String patientId, Date date) {

		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		logger.info("\n\nGetting Day Story for patient {} at date/time {}\n\n", patientId, dateFormat.format(date));

		return this.getEvents(patientId, date, date);

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.essence.hc.persistence.PatientDAO#getActivityIndex(java.lang.String,
	 * java.util.Date, java.util.Date)
	 */
	@Override
	public List<ActivityIndex> getActivityIndexOld(String residentId, Date fromDate, Date toDate, String reportName) {
		DateFormat dateFormat = new SimpleDateFormat(Util.DATEFORMAT_INPUT);
		HashMap<String, String> reqParams = new HashMap<String, String>();
		reqParams.put("patientId", residentId);
		try {
			reqParams.put("start", URLEncoder.encode(dateFormat.format(fromDate), "UTF-8"));
			reqParams.put("end", URLEncoder.encode(dateFormat.format(toDate), "UTF-8"));
		} catch (UnsupportedEncodingException e) {
			throw new DataAccessException(e);
		}

		// FIXME: ONLY FOR TESTING!! remove before going to production envs.
		if (Util.isDebug()) {
			RequestProcessor reqProcessor = new DummyRequestProcessor();
			return reqProcessor.performRequest(ArrayList.class, reportName, reqParams);
		} else {
			return reqProcessor.performRequest(ArrayList.class, reportName, reqParams);
		}
	}

	/*
	 * ADL 2.2 - New Activity Index Report
	 */
	@Override
	public List<ActivityReportDetail> getActivityIndex(String residentId, Date fromDate, Date toDate) {
		DateFormat dateFormat = new SimpleDateFormat(Util.DATEFORMAT_INPUT);
		HashMap<String, String> reqParams = new HashMap<String, String>();
		reqParams.put("patientId", residentId);
		try {
			reqParams.put("start", URLEncoder.encode(dateFormat.format(fromDate), "UTF-8"));
			reqParams.put("end", URLEncoder.encode(dateFormat.format(toDate), "UTF-8"));
		} catch (UnsupportedEncodingException e) {
			throw new DataAccessException(e);
		}

		// FIXME: ONLY FOR TESTING!! remove before going to production envs.
		if (Util.isDebug()) {
			RequestProcessor reqProcessor = new DummyRequestProcessor();
			return reqProcessor.performRequest(ArrayList.class, "activity_index_report", reqParams);
		} else {
			return reqProcessor.performRequest(ArrayList.class, "activity_index_report", reqParams);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.essence.hc.persistence.PatientDAO#getDayStoryXP(java.lang.String,
	 * java.util.Date)
	 */
	@Override
	public Map<String, List<?>> getDayStoryXP(String residentId, Date date) {

		DateFormat dateFormat = new SimpleDateFormat(Util.DATEFORMAT_INPUT);
		HashMap<String, String> reqParams = new HashMap<String, String>();
		reqParams.put("patientId", residentId);
		try {
			reqParams.put("selectedDate", URLEncoder.encode(dateFormat.format(date), "UTF-8"));
		} catch (UnsupportedEncodingException e) {
			throw new DataAccessException(e);
		}

		// FIXME: ONLY FOR TESTING!! remove before going to production envs.
		if (Util.isDebug()) {
			RequestProcessor reqProcessor = new DummyRequestProcessor();
			return reqProcessor.performRequest(Map.class, "day_story_express", reqParams);
		} else {
			return reqProcessor.performRequest(Map.class, "day_story_express", reqParams);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.essence.hc.persistence.EventDAO#getEvents(java.lang.String,
	 * java.util.Date, java.util.Date)
	 */
	@Override
	public List<Event> getEvents(String patientId, Date timeFrom, Date timeUntil) {

		DateFormat dateFormat = new SimpleDateFormat(Util.DATEFORMAT_ESSENCE);

		/*
		 * try { timeFrom = dateFormat.parse("2018-02-19 02:01:02"); } catch (Exception
		 * e) { }
		 */

		HashMap<String, String> reqParams = new HashMap<String, String>();
		reqParams.put("ipatientId", patientId);

		logger.info("our DayStory---------------->{}", dateFormat.format(timeFrom));

		try {
			reqParams.put("sDateSlctd", URLEncoder.encode(dateFormat.format(timeFrom), "UTF-8"));
		} catch (UnsupportedEncodingException e) {
			throw new DataAccessException(e);
		}
		StringBuffer aTypes = new StringBuffer();
		for (ActivityType a : ActivityType.getActivityProducingDevices()) {
			if (aTypes.length() > 0)
				aTypes.append(",");
			aTypes.append(a.toString());
		}
		reqParams.put("actvtFilters", aTypes.toString());

		List<Event> events = null;
		if (Util.isDebug()) {
			RequestProcessor reqProcessor = new DummyRequestProcessor();
			events = reqProcessor.performRequest(ArrayList.class, "day_story", reqParams);
		} else {
			events = reqProcessor.performRequest(ArrayList.class, "day_story", reqParams);
		}

		Map<Groupkey, List<MissingInformationActivityWrapper>> map = new HashMap<>();
		List<Event> notRelatedEvents = new ArrayList<>();
		for (Event event : events) {

			if (MissingInformationActivityWrapper.class.isAssignableFrom(event.getClass())) {
				MissingInformationActivityWrapper mi = (MissingInformationActivityWrapper) event;

				// Exclude: Missing information bars that are not related to the current date,
				// because they end
				// before current report day at start of the day, or they start after reporty
				// day at midnight
				if (!isMissingInformationActivityRequestedDayRelated(mi, timeFrom)) {
					notRelatedEvents.add(mi);
					continue;
				}

				// The activities that carry MissingInformation data need to be
				// set to the current date
				mi.setActivityDatesBasedOnAGivenDate(dateFormat.format(timeFrom));

				// EIC15-2070: If there are multiple “missing data” periods in the same
				// 30-minute slot, the report shall show only one period,
				// prioritising a period created due to supervision loss.
				// Group by first and lasCell
				Groupkey key = new Groupkey(mi.getFirstCell(), mi.getLastCell());
				if (map.containsKey(key)) {
					map.get(key).add(mi);
				} else {
					List<MissingInformationActivityWrapper> mis = new ArrayList<>();
					mis.add(mi);
					map.put(key, mis);
				}
			}
		}

		if (notRelatedEvents.size() > 0) {
			events.removeAll(notRelatedEvents);
		}

		// Reduce Events
		for (Groupkey key : map.keySet()) {
			events.removeAll(map.get(key));
			events.add(reduceToOne(map.get(key)));
		}

		return events;
	}

	/**
	 * An event is related a given date if it overlaps with that date. ie, an event
	 * is not related if its end time is before the given date at start of the date
	 * OR its start time is after the given date at midnight.
	 * 
	 * @param event
	 * @param timeFrom
	 * @return
	 */
	private boolean isMissingInformationActivityRequestedDayRelated(MissingInformationActivityWrapper mi,
			Date timeFrom) {

		SimpleDateFormat formatter = new SimpleDateFormat(Util.DATETIMEFORMAT_INPUT);

		try {

			Date endTime = formatter.parse(mi.getMissingInformation().getEndTime()); // can be null
			if (endTime == null) {
				return true;
			}
			if (Util.isBeforeBaseDateAtStartOfTheDay(endTime, timeFrom) || Util
					.isAfterBaseDateAtEndOfTheDay(formatter.parse(mi.getMissingInformation().getStartTime()), timeFrom))
				return false;

			return true;
		} catch (ParseException t) {
			throw new RuntimeException("Unexpected date format", t);
		}

	}

	/**
	 * For missing information bars we just want supervision lost
	 * 
	 * @param mis
	 *            - missing information activities that have the same start and last
	 *            cell
	 * @return
	 */
	private MissingInformationActivityWrapper reduceToOne(List<MissingInformationActivityWrapper> mis) {
		// 1. supervision lost greatets priority
		for (MissingInformationActivityWrapper mi : mis) {
			if (mi.getMissingInformation().getReason().equals(Reason.supervisionLoss)) {
				return mi;
			}
		}

		// 2. Any not unknown
		for (MissingInformationActivityWrapper mi : mis) {
			if (!mi.getMissingInformation().getReason().equals(Reason.unknown)) {
				return mi;
			}
		}
		// 3. Then one unknown
		return mis.get(mis.size() - 1);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.essence.hc.persistence.PatientDAO#getAlerts(java.lang.String, int,
	 * int, com.essence.hc.model.Alert.AlertType[],
	 * com.essence.hc.model.Alert.AlertState[])
	 */
	public List<Alert> getAlerts(String patientId, int lastAlertId, int page, int perPage, AlertType[] alertTypes,
			AlertState[] alertStates, String languageKey, int userId, UserType userType) {

		String reqName = "history";
		if (page < 0)
			page = 0;
		if (perPage <= 0)
			perPage = DEFAULT_PER_PAGE;

		// Types
		String alertTypeFilter = new String("ALL");
		if (alertTypes != null) {
			ArrayList<String> ids = new ArrayList<String>();
			for (AlertType type : alertTypes) {
				ids.add(String.valueOf(type.getId()));
			}
			alertTypeFilter = StringUtils.join(ids, ",");
		}
		// States
		String alertStateFilter = new String("ALL");
		if (alertStates != null) {
			ArrayList<String> ids = new ArrayList<String>();
			for (AlertState type : alertStates) {
				if (type.getId() == 2)
					ids.add("1,2");
				else
					ids.add(String.valueOf(type.getId()));
			}
			alertStateFilter = StringUtils.join(ids, ",");
		}

		HashMap<String, String> reqParams = new HashMap<String, String>();
		reqParams.put("iPatient", patientId);
		reqParams.put("iNumOfItems", String.valueOf(perPage));
		reqParams.put("iPageNum", String.valueOf(page)); // Not needed anymore,
															// kept for
															// compatibility
															// with older
															// versions of API
		reqParams.put("iLastAlertId", String.valueOf(lastAlertId));
		reqParams.put("sAlrtType", alertTypeFilter);
		reqParams.put("sAlrtState", alertStateFilter);
		reqParams.put("languageKey", languageKey);
		reqParams.put("userId", String.valueOf(userId));
		reqParams.put("userType", String.valueOf(userType.getId()));

		return reqProcessor.performRequest(ArrayList.class, reqName, reqParams);
	}

	@Deprecated
	public List<Alert> getAlerts(String patientId, Date timeFrom, Date timeUntil, AlertType type, AlertState state) {

		String reqName = "history";
		HashMap<String, String> reqParams = new HashMap<String, String>();
		reqParams.put("iPatient", patientId);
		reqParams.put("iNumOfItems", "10");
		reqParams.put("iPageNum", "0");
		reqParams.put("sAlrtType", "0");

		List<Alert> alerts = reqProcessor.performRequest(ArrayList.class, reqName, reqParams);

		return alerts;
	}

	@Deprecated
	public List<Alert> getAlerts(String patientId, int page, int perPage, AlertType[] alertTypes) {

		String reqName = "history";

		if (page < 0)
			page = 0;

		if (perPage <= 0)
			perPage = DEFAULT_PER_PAGE;

		String alertTypeFilter = new String("0");
		if (alertTypes != null) {
			ArrayList<String> ids = new ArrayList<String>();
			for (AlertType type : alertTypes) {
				ids.add(String.valueOf(type.getId()));
			}
			alertTypeFilter = StringUtils.join(ids, ",");
		}

		HashMap<String, String> reqParams = new HashMap<String, String>();
		reqParams.put("iPatient", patientId);
		reqParams.put("iNumOfItems", String.valueOf(perPage));
		reqParams.put("iPageNum", String.valueOf(page));
		reqParams.put("sAlrtType", alertTypeFilter);

		return reqProcessor.performRequest(ArrayList.class, reqName, reqParams);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.essence.hc.persistence.PatientDAO#getSystemStatus(java.lang.String)
	 */
	@Override
	public SystemStatus getSystemStatus(String patientId, String languageKey) {

		HashMap<String, String> reqParams = new HashMap<String, String>();
		reqParams.put("iPatient", patientId);
		reqParams.put("languageKey", languageKey);

		return reqProcessor.performRequest(SystemStatus.class, "system_status", reqParams);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.essence.hc.persistence.PatientDAO#getPatientsInfo(java.lang.String,
	 * java.lang.String, int, int, int, java.lang.String, java.lang.String,
	 * java.lang.String, java.lang.Boolean)
	 */
	@Cacheable(value = PATIENTS_CACHE, key = "#p0.concat('-').concat(#p2).concat('-').concat(#p3)", condition = "#sNameFltr == null and #sIdFltr == null and #orderBy == null and #serviceTypeFilter == null and #enableActiveService == null")
	@Override
	public List<Patient> getPatientsInfo(String userId, String vendorId, int rolId, int page, int perPage,
			String sNameFltr, String sIdFltr, String orderBy, Boolean sortAsc, String[] panelTypeFilter,
			ServiceType[] serviceTypeFilter, Boolean enableActiveService) {

		logger.info(
				"\n\nGetting Patients Info (Analysis Tab) for user {}, page {} and {} patients per page.....................\n\n",
				userId, page, perPage);

		HashMap<String, String> reqParams = new HashMap<String, String>();
		reqParams.put("iUserId", userId);
		reqParams.put("iVendorId", vendorId);
		reqParams.put("iUserRol", String.valueOf(rolId));
		reqParams.put("iNumOfItems", String.valueOf(perPage));
		reqParams.put("iPageNum", String.valueOf(page));
		if (sNameFltr != null) {
			reqParams.put("sNameFltr", sNameFltr);
		}
		if (sIdFltr != null) {
			reqParams.put("sIdFltr", sIdFltr);
		}
		if (orderBy != null) {
			reqParams.put("orderBy", orderBy);
			reqParams.put("sortAsc", sortAsc == null ? "true" : String.valueOf(sortAsc));
		}
		
		String panelTypeString = Util.buildPanelTypeFilterString(panelTypeFilter);
		if (org.springframework.util.StringUtils.hasText(panelTypeString)) {
			reqParams.put("panelTypeFilter", panelTypeString);
		}
		
		String serviceTypeString = Util.buildServicePackagesFilterStringFromServiceTypes(serviceTypeFilter);
		if (org.springframework.util.StringUtils.hasText(serviceTypeString)) {
			reqParams.put("servicePackagesFilter", serviceTypeString);
		}
		
		if (enableActiveService != null) {
			reqParams.put("enableActiveService", enableActiveService.toString());
		}

		if (Util.isDebug()) {
			RequestProcessor reqProcessor = new DummyRequestProcessor();
			return reqProcessor.performRequest(ArrayList.class, "patient_info", reqParams);
		} else {
			return reqProcessor.performRequest(ArrayList.class, "patient_info", reqParams);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.essence.hc.persistence.PatientDAO#getPatientReport(java.lang.String)
	 */
	@Override
	public Patient getPatientReport(String vendorId) {
		HashMap<String, String> reqParams = new HashMap<String, String>();
		reqParams.put("Id", vendorId);
		return reqProcessor.performRequest(Patient.class, "patient_report", reqParams);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.essence.hc.persistence.PatientDAO#getCareGiverPatients(java.lang.
	 * String, int, java.lang.String, boolean, java.lang.String, java.lang.String)
	 */
	public List<Patient> getCareGiverPatients(String iUserId, String iVendorId, int iNumOfItems, String iPageNum,
			String sSearchCriteria, String sCriteriaValue, ServiceType[] serviceTypeFilter,
			Boolean enableActiveService) {

		iPageNum = (iPageNum == null ? "0" : iPageNum);

		HashMap<String, String> reqParams = new HashMap<String, String>();
		reqParams.put("iUserId", iUserId);
		reqParams.put("iVendorId", iVendorId);
		reqParams.put("iNumOfItems", String.valueOf(iNumOfItems));
		reqParams.put("iPageNum", iPageNum);
		// reqParams.put("assigned", String.valueOf(assigned));
		if (sSearchCriteria != null) {
			reqParams.put("sSearchCriteria", sSearchCriteria);
			reqParams.put("sCriteriaValue", sCriteriaValue);
		}
		String serviceTypeString = (serviceTypeFilter == null || serviceTypeFilter.length > 1) ? "Pro,Family"
				: Util.mapFromOldToNewServicePackageNaming(serviceTypeFilter[0].getName());
		if (org.springframework.util.StringUtils.hasText(serviceTypeString)) {
			reqParams.put("servicePackagesFilter", serviceTypeString);
		}
		if (enableActiveService != null) {
			reqParams.put("enableActiveService", enableActiveService.toString());
		}

		// reqParams.put("sSearchCriteria", (sSearchCriteria == null ||
		// sSearchCriteria.isEmpty()) ? null : sSearchCriteria);
		// reqParams.put("sCriteriaValue", (sCriteriaValue == null ||
		// sCriteriaValue.isEmpty()) ? null : sCriteriaValue);

		if (Util.isDebug()) {
			RequestProcessor reqProcessor = new DummyRequestProcessor();
			return reqProcessor.performRequest(ArrayList.class, "caregiver_patients", reqParams);
		}

		return reqProcessor.performRequest(ArrayList.class, "caregiver_patients", reqParams);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.essence.hc.persistence.PatientDAO#getPatientCareGivers(java.lang.
	 * String, int, java.lang.String, boolean, java.lang.String, java.lang.String,
	 * boolean)
	 */
	public List<SystemUser> getPatientCareGivers(String iUserId, String iVendorId, int iNumOfItems, String iPageNum,
			String sSearchCriteria, String sCriteriaValue) {

		iPageNum = (iPageNum == null ? "0" : iPageNum);

		HashMap<String, String> reqParams = new HashMap<String, String>();
		reqParams.put("iUserId", iUserId);
		reqParams.put("iVendorId", iVendorId);
		reqParams.put("iNumOfItems", String.valueOf(iNumOfItems));
		reqParams.put("iPageNum", iPageNum);
		// reqParams.put("includeAdmin", String.valueOf(includeAdmin));

		if (sSearchCriteria != null) {
			reqParams.put("sSearchCriteria", sSearchCriteria);
			reqParams.put("sCriteriaValue", sCriteriaValue);
		}

		return reqProcessor.performRequest(ArrayList.class, "patient_caregivers", reqParams);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.essence.hc.persistence.PatientDAO#getAddUserAssign(int)
	 */
	public ResponseStatus UserAssignCmd(int iUid, int iAsgnId, String sAsgnCmd) {

		HashMap<String, String> reqParams = new HashMap<String, String>();
		reqParams.put("iUid", String.valueOf(iUid));
		reqParams.put("iAsgnId", String.valueOf(iAsgnId));
		reqParams.put("sAsgnCmd", String.valueOf(sAsgnCmd));

		return reqProcessor.performRequest(ResponseStatus.class, "assign_caregiver", reqParams);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.essence.hc.persistence.PatientDAO#DlUserAssign(int)
	 */
	public ResponseStatus DlUserAssign(int iUid, int iAsgnId, String sAsgnCmd) {

		HashMap<String, String> reqParams = new HashMap<String, String>();
		reqParams.put("iUid", String.valueOf(iUid));
		reqParams.put("iAsgnId", String.valueOf(iAsgnId));
		reqParams.put("sAsgnCmd", String.valueOf(sAsgnCmd));

		return reqProcessor.performRequest(ResponseStatus.class, "unassign_caregiver", reqParams);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.essence.hc.persistence.PatientDAO#getCameraList(int)
	 */
	@Override
	public List<Device> getCameraList(String iUserId) {
		HashMap<String, String> reqParams = new HashMap<String, String>();

		reqParams.put("usrId", iUserId);

		return reqProcessor.performRequest(ArrayList.class, "camera_list", reqParams);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.essence.hc.persistence.PatientDAO#getPhotos(int)
	 */
	@Override
	public String getPhotos(String userId, Date fromDate, Date toDate) {
		DateFormat dateFormat = new SimpleDateFormat(Util.DATEFORMAT_ESSENCE);
		HashMap<String, String> reqParams = new HashMap<String, String>();
		try {
			reqParams.put("fromDate", URLEncoder.encode(dateFormat.format(fromDate), "UTF-8"));
			reqParams.put("toDate", URLEncoder.encode(dateFormat.format(toDate), "UTF-8"));
		} catch (UnsupportedEncodingException e) {
			throw new DataAccessException(e);
		}

		reqParams.put("userId", userId);

		return reqProcessor.performRequest(String.class, "photos_list", reqParams);
	}

	public ResponseStatus requestPhoto(String caregiverId, String residenId, String deviceId, String alertSessionId) {
		HashMap<String, String> reqParams = new HashMap<String, String>();

		reqParams.put("caregiverId", caregiverId);
		reqParams.put("residentId", residenId);
		reqParams.put("deviceId", deviceId);
		reqParams.put("alertSessionId", alertSessionId);

		return reqProcessor.performRequest(ResponseStatus.class, "request_photo", reqParams);
	}

	/* ---------- Cache Handling ---------------- */

	private Patient get(String key) {
		logger.info("Getting Cached Patient: " + key);
		ValueWrapper element = getCache().get(key);
		if (element != null) {
			return (Patient) element.get();
		}
		return null;
		// return (Patient) getCache().get(key).get();
	}

	private void put(final String key, final Patient value) {
		logger.info("Caching Patient: " + key);
		getCache().put(key, value);
	}

	private void remove(final String key) {
		logger.info("Remove Caching Patient: " + key);
		((PatientDAOImpl) getCache()).remove(key);
	}

	private Cache getCache() {
		return cacheManager.getCache(PATIENTS_CACHE);
	}

	// /* (non-Javadoc)
	// * @see
	// com.essence.hc.persistence.PatientDAO#addPatient(com.essence.hc.model.Patient)
	// */
	// @Override
	// public ResponseStatus addPatient(Patient patient) {
	// HashMap<String, String> reqParams = new HashMap<String, String>();
	//
	// reqParams.put("sAddress", patient.getAddress());
	// reqParams.put("semail", patient.getEmail());
	// reqParams.put("spswrd", patient.getEncodedPasswd());
	// reqParams.put("fname", patient.getFirstName());
	// reqParams.put("lname", patient.getLastName());
	// reqParams.put("mobile", patient.getMobile());
	// reqParams.put("slgin", patient.getNick());
	// reqParams.put("phone", patient.getPhone());
	// reqParams.put("sUsrType", String.valueOf(patient.getUserType().getId()));
	// reqParams.put("gender", String.valueOf(patient.getGender().getId()));
	// reqParams.put("birthDate", (patient.getBirthDate() == null) ? "null" :
	// Util.formatDate(patient.getBirthDate(),Util.DATEFORMAT_OUTPUT_PLAIN));
	// reqParams.put("vendorId", String.valueOf(patient.getVendorId()));
	// //reqParams.put("patientQuestion", patient.getQuestion());
	// //reqParams.put("patientAnswer", patient.getAnswer());
	// reqParams.put("account", (patient.getInstallation().getPanelId() == null
	// || patient.getInstallation().getPanelId().isEmpty()) ? "null" :
	// patient.getInstallation().getPanelId());
	// reqParams.put("simNum", (patient.getInstallation().getSimNumber() == null
	// || patient.getInstallation().getSimNumber().isEmpty()) ? "null" :
	// patient.getInstallation().getSimNumber());
	//
	// return reqProcessor.performRequest(ResponseStatus.class, "add_user",
	// reqParams);
	// }

	static class Groupkey {

		private final int[] key;

		public Groupkey(int firstCell, int lastCell) {
			key = new int[] { firstCell, lastCell };
		}

		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result + Arrays.hashCode(key);
			return result;
		}

		@Override
		public boolean equals(Object obj) {
			if (this == obj)
				return true;
			if (obj == null)
				return false;
			if (getClass() != obj.getClass())
				return false;
			Groupkey other = (Groupkey) obj;
			if (!Arrays.equals(key, other.key))
				return false;
			return true;
		}

	}

}
