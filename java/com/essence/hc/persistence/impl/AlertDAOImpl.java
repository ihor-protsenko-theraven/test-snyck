package com.essence.hc.persistence.impl;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.cache.Cache;
import org.springframework.cache.Cache.ValueWrapper;
import org.springframework.cache.CacheManager;

import com.essence.hc.eil.requestprocessors.RequestProcessor;
import com.essence.hc.eil.requestprocessors.impl.DummyRequestProcessor;
import com.essence.hc.model.Alert;
import com.essence.hc.model.Alert.AlertState;
import com.essence.hc.model.Alert.IssueType;
import com.essence.hc.model.AlertCloseReason;
import com.essence.hc.model.EventLog;
import com.essence.hc.model.LowActivityReport;
import com.essence.hc.model.Patient;
import com.essence.hc.model.ResponseStatus;
import com.essence.hc.model.User;
import com.essence.hc.model.User.UserType;
import com.essence.hc.model.Vendor.ServiceType;
import com.essence.hc.persistence.AlertDAO;
import com.essence.hc.persistence.exceptions.DataAccessException;
import com.essence.hc.util.Util;

/**
 * @author oscar.canalejo
 *
 */
public class AlertDAOImpl implements AlertDAO {

	protected final Logger logger = LoggerFactory.getLogger(getClass());

	private static final String MONITORING_CACHE = "patients_in_alarm";

	@Autowired
	@Qualifier("requestProcessor")
	RequestProcessor reqProcessor;
	@Autowired
	private CacheManager cacheManager;

	// CONSTANTS
	private static final String CACHENAME = "alerts";
	// Manual alerts
	/*
	 * private final String MANUAL_ALERT_DESCRIPTION_GENERIC_TECNICHAL =
	 * "#{c}E_ALERT_TYPE_ACTVT_TECH_GENERAL|*|"; //"General Tech Alert: "; private
	 * final String MANUAL_ALERT_DESCRIPTION_DEVICE_TECHNICAL =
	 * "#{c}E_ALERT_TYPE_MANUAL_TECH_DEVICE|"; //"Device Tech Alert: "; private
	 * final String MANUAL_ALERT_DESCRIPTION_MEDICAL =
	 * "#{c}E_ALERT_TYPE_ACTVT_MANUAL_SICK|"; //"Sick: ";
	 */

	private final int MANUAL_ALERT_TYPE_MEDICAL = 1;
	private final int MANUAL_ALERT_TYPE_TECHNICAL_GEN = 2;
	private final int MANUAL_ALERT_TYPE_TECHNICAL_DEV = 3;

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.essence.hc.persistence.AlertDAO#getByUserId(java.lang.String)
	 */
	public List<Alert> getByUserId(String userId) {
		HashMap<String, String> reqParams = new HashMap<String, String>();
		reqParams.put("userId", userId);
		ArrayList<Alert> alertList = null;
		Alert[] alerts = reqProcessor.performRequest(Alert[].class, "alerts", reqParams);
		if (alerts != null) {
			alertList = new ArrayList<Alert>();
			for (Alert newAlert : alerts) {
				if (this.get(newAlert.getId()) == null) {
					this.put(newAlert.getId(), newAlert);
				} else {
					newAlert = this.get(newAlert.getId());
				}
				alertList.add(newAlert);
			}
		}
		return alertList;
	}

	public List<AlertCloseReason> getAlertCloseReasons(String languageKey) {
		HashMap<String, String> reqParams = new HashMap<String, String>();
		if (languageKey != null) {
			reqParams.put("language", languageKey);
		}
		return reqProcessor.performRequest(ArrayList.class, "alert_close_reasons", reqParams);
	};

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.essence.hc.persistence.AlertDAO#closeAlertSession(java.lang.String)
	 */
	public ResponseStatus closeAlertSession(String alertId, String userId, String patientId, String sessionId,
			int reasonCode, String message) {
		HashMap<String, String> reqParams = new HashMap<String, String>();
		reqParams.put("iparentId", alertId);
		reqParams.put("iUsr", userId);
		reqParams.put("iPtient", patientId);
		reqParams.put("iSession", sessionId);
		reqParams.put("Code", Integer.toString(reasonCode));
		try {
			if (message.indexOf(',') > 0)
				message = message.substring(0, message.indexOf(','));
			reqParams.put("smsgTxt", URLEncoder.encode(message, "UTF-8"));
		} catch (UnsupportedEncodingException e) {
			throw new DataAccessException(e);
		}
		reqParams.put("sType", "VERIFIED");
		reqParams.put("iPrivateId", "0");

		ResponseStatus response = reqProcessor.performRequest(ResponseStatus.class, "close", reqParams);

		return response;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.essence.hc.persistence.AlertDAO#eventLog(java.lang.String)
	 */
	public EventLog eventLog(String userId, String patientId, String sessionId, String logId, String languageKey,
			UserType userType) {
		HashMap<String, String> reqParams = new HashMap<String, String>();
		reqParams.put("iUser", userId);
		reqParams.put("iPatientId", patientId);
		reqParams.put("iSession", sessionId);
		reqParams.put("iPrvLogId", logId);
		reqParams.put("languageKey", languageKey);
		reqParams.put("userType", String.valueOf(userType.getId()));
		EventLog response = reqProcessor.performRequest(EventLog.class, "event_log", reqParams);

		return response;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.essence.hc.persistence.AlertDAO#getAlert(java.lang.String)
	 */
	public Alert getAlert(String alertId, String languageKey) {
		HashMap<String, String> reqParams = new HashMap<String, String>();
		reqParams.put("iAlrtId", alertId);
		reqParams.put("languageKey", languageKey);
		return reqProcessor.performRequest(Alert.class, "alert", reqParams);
	}

	public LowActivityReport getLowActivityReport(String alertId, String languageKey) {
		HashMap<String, String> reqParams = new HashMap<String, String>();
		reqParams.put("iAlertRecId", alertId);
		reqParams.put("languageKey", languageKey);
		return reqProcessor.performRequest(LowActivityReport.class, "low_activity", reqParams);
		// return prsr.parse();
		/*
		 * FIXME: Remove following Dummy-Call block when remote service will be fully
		 * functional
		 * 
		 * String fileName = (Integer.parseInt(alertId)%2 == 0)? "low_activity_02" :
		 * "low_activity_01"; DummyRequestProcessor reqProcessor = new
		 * DummyRequestProcessor(); LowActivityPrsr prsr =
		 * reqProcessor.performRequest(LowActivityPrsr.class, fileName, reqParams);
		 * return prsr.parse();
		 */
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.essence.hc.persistence.PatientDAO#getById(java.lang.String)
	 */
	@Deprecated
	@Override
	// @Cacheable(cacheName="alerts")
	public Alert getById(String id) {
		Alert alert = this.get(id);
		if (alert == null) {
			HashMap<String, String> reqParams = new HashMap<String, String>();
			reqParams.put("alertId", id);
			alert = reqProcessor.performRequest(Alert.class, "alert", reqParams);
			if (alert != null) {
				this.put(alert.getId(), alert);
			}
		}
		return alert;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.essence.hc.persistence.AlertDAO#update(java.lang.String)
	 */
	public synchronized void update(Alert alert) {
		this.put(alert.getId(), alert);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.essence.hc.persistence.AlertDAO#UpdateAlertState(int,
	 * com.essence.hc.model.Alert.AlertState, int)
	 */
	public synchronized ResponseStatus updateAlertState(int alertId, AlertState newState, int patientId) {
		HashMap<String, String> reqParams = new HashMap<String, String>();
		reqParams.put("iAlrtId", String.valueOf(alertId));
		reqParams.put("iNewState", String.valueOf(newState.getId()));
		reqParams.put("iPatientId", String.valueOf(patientId));
		return reqProcessor.performRequest(ResponseStatus.class, "update_alert_state", reqParams);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.essence.hc.persistence.AlertDAO#save(com.essence.hc.model.Alert)
	 */
	@Override
	public void save(Alert alert) {
		this.put(alert.getId(), alert);
	}

	public List<Alert> getSessionAlerts(String userId, String patientId, String sessionId) {
		HashMap<String, String> reqParams = new HashMap<String, String>();
		reqParams.put("iUser", userId);
		reqParams.put("iPatientId", patientId);
		reqParams.put("iSession", sessionId);
		return reqProcessor.performRequest(ArrayList.class, "session_alerts", reqParams);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.essence.hc.persistence.PatientDAO#getPatientInfo(java.lang.String)
	 */
	// @CacheEvict(value=MONITORING_CACHE, key="#p0.concat('-').concat(#p1)",
	// condition="#force==true")
	// @Cacheable( value=MONITORING_CACHE, key="#p0.concat('-').concat(#p1)",
	// condition="#orderBy==null")
	@Override
	public List<Patient> getPatientsInAlarm(int userId, int vendorId, int rolId, String orderBy, Boolean sortAsc,
			ServiceType[] serviceTypeFilter, Boolean enableActiveService) {

		logger.info("\n\nGetting Patients In Alarm (Monitoring Tab) for user {}.....................\n\n", userId);

		HashMap<String, String> reqParams = new HashMap<String, String>();
		reqParams.put("iuserId", String.valueOf(userId));
		reqParams.put("iVendorId", String.valueOf(vendorId));
		reqParams.put("iUserRol", String.valueOf(rolId));

		if (orderBy != null) {
			reqParams.put("orderBy", orderBy);
			reqParams.put("sortAsc", sortAsc == null ? "true" : String.valueOf(sortAsc));
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
			return reqProcessor.performRequest(ArrayList.class, "patient_in_alarm", reqParams);
		} else {
			return reqProcessor.performRequest(ArrayList.class, "patient_in_alarm", reqParams);
		}

	}

	public List<Patient> getAlertCount(User user) {

		HashMap<String, String> reqParams = new HashMap<String, String>();
		reqParams.put("iVendorId", String.valueOf(user.getVendorId()));
		reqParams.put("iUserId", user.getId());
		reqParams.put("iUserRol", String.valueOf(user.getUserType().getId()));
		return reqProcessor.performRequest(ArrayList.class, "alert_count", reqParams);
	}

	@Override
	public List<ResponseStatus> GetManualAlertTypes() {
		HashMap<String, String> reqParams = new HashMap<String, String>();
		reqParams.put("iuserId", "1");
		return reqProcessor.performRequest(ArrayList.class, "alert_types", reqParams);
	}

	@Override
	public Map<String, Boolean> getAlertConfiguration(String userId, String userType, String residentId) {
		HashMap<String, String> reqParams = new HashMap<String, String>();
		reqParams.put("iUsrId", userId);
		reqParams.put("userType", userType);
		reqParams.put("relatedUserId", residentId);
		return reqProcessor.performRequest(HashMap.class, "alert_configuration", reqParams);
	}

	public ResponseStatus setManualAlert(int userID, int devicID, int alert) {

		HashMap<String, String> reqParams = new HashMap<String, String>();
		reqParams.put("userID", String.valueOf(userID));
		reqParams.put("devicID", String.valueOf(devicID));
		reqParams.put("alert", String.valueOf(alert));

		return reqProcessor.performRequest(ResponseStatus.class, "manual_alert", reqParams);
	}

	public ResponseStatus createManualAlert(String iUserId, String iAlrtType, String sAlertTxt, String deviceId) {
		// By default is a technical alert
		HashMap<String, String> reqParams = new HashMap<String, String>();
		int iType = 0;
		int iAlertState = 0;

		logger.info("************************************************");
		logger.info("iAlrtType: " + iAlrtType);

		if (iAlrtType.equals(IssueType.RESIDENT_SICK.toString())) {
			logger.info("IssueType.RESIDENT_SICK: " + IssueType.RESIDENT_SICK);

			iType = MANUAL_ALERT_TYPE_MEDICAL;
			iAlertState = 0;
		} else if (iAlrtType.equals(IssueType.GENERIC_INSTALLATION_ISSUE.toString())) {
			iType = MANUAL_ALERT_TYPE_TECHNICAL_GEN;
			logger.info("IssueType.GENERIC_INSTALLATION_ISSUE: " + IssueType.GENERIC_INSTALLATION_ISSUE);
		} else if (iAlrtType.equals(IssueType.DEVICE_ISSUE.toString())) {
			iType = MANUAL_ALERT_TYPE_TECHNICAL_DEV;
			logger.info("IssueType.DEVICE_ISSUE: " + IssueType.DEVICE_ISSUE);

			reqParams.put("installId", deviceId);
		}

		reqParams.put("userID", iUserId);
		reqParams.put("alertType", String.valueOf(iType));
		reqParams.put("alertTxt", sAlertTxt);
		reqParams.put("alertState", String.valueOf(iAlertState));

		return reqProcessor.performRequest(ResponseStatus.class, "create_manual_alert", reqParams);
	}

	/**
	 * ----- Cache Handling -------
	 */
	private Alert get(String key) {
		logger.info("Getting Cached Alert: " + key);
		ValueWrapper element = getCache().get(key);
		if (element != null) {
			return (Alert) element.get();
		}
		return null;
	}

	private void put(final String key, final Alert value) {
		logger.info("Caching Alert: " + key);
		getCache().put(key, value);
	}

	private void remove(String key) {
		logger.info("Removing Cached Alert: " + key);
		getCache().evict(key);
	}

	private Cache getCache() {
		return cacheManager.getCache(CACHENAME);
	}

}
