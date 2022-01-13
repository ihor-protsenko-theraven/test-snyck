package com.essence.hc.persistence.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.cache.Cache;
import org.springframework.cache.Cache.ValueWrapper;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.security.authentication.encoding.PasswordEncoder;

import com.essence.hc.eil.parsers.AlertConfigPrsr;
import com.essence.hc.eil.requestprocessors.RequestProcessor;
import com.essence.hc.eil.requestprocessors.impl.DummyRequestProcessor;
import com.essence.hc.model.Alert;
import com.essence.hc.model.AlertPreferences;
import com.essence.hc.model.HCCommand;
import com.essence.hc.model.Language;
import com.essence.hc.model.Panel;
import com.essence.hc.model.Patient;
import com.essence.hc.model.ResponseStatus;
import com.essence.hc.model.SystemStatus.StatusTypes;
import com.essence.hc.model.SystemUser;
import com.essence.hc.model.Vendor;
import com.essence.hc.model.Vendor.ServiceType;
import com.essence.hc.persistence.AlertDAO;
import com.essence.hc.persistence.PatientDAO;
import com.essence.hc.persistence.UserDAO;
import com.essence.hc.util.Util;

/**
 * @author oscar.canalejo
 *
 */
public class UserDAOImpl implements UserDAO {

	protected final Logger logger = LoggerFactory.getLogger(getClass());

	@Autowired
	private CacheManager cacheManager;
	@Autowired
	private PatientDAO patientDAO;
	@Autowired
	private AlertDAO alertDAO;
	@Autowired
	@Qualifier("requestProcessor")
	private RequestProcessor reqProcessor;
	@Autowired
	PasswordEncoder passwordEncoder;

	private static final String USER_PATIENTS_CACHE = "userPatients";
	private static final String USER_ALERTS_CACHE = "userAlerts";
	// private static final String USER_COMMANDS_CACHE = "userCommands";

	/**
	 * Deberiamos atacar a la bbdd pero por ahora creamos un usuario con lo que
	 * entre siempre que se llame 'test'
	 */
	// @Override
	// public User getUserByCredentials(String login, String password,
	// String country, String service, boolean s) {
	// // DUMMY: Test valido. el resto no
	// User user = null;
	// // Creamos el token de seguridad
	// if (StringUtils.containsIgnoreCase(login, "test")){
	// List<GrantedAuthority> AUTHORITIES = new ArrayList<GrantedAuthority>();
	// AUTHORITIES.add(new SimpleGrantedAuthority("ROLE_USER"));
	// if (StringUtils.containsIgnoreCase(login, "owner"))
	// AUTHORITIES.add(new SimpleGrantedAuthority("ROLE_OWNER"));
	// if (StringUtils.containsIgnoreCase(login, "admin"))
	// AUTHORITIES.add(new SimpleGrantedAuthority("ROLE_ADMIN"));
	// user = new User(login,password,AUTHORITIES);
	// user.setMobile("696734567");
	// user.setId(password);
	// }
	// return user;
	// }

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.essence.hc.persistence.UserDAO#getVendor(java.lang.String)
	 */
	public Vendor getVendor(int vendorId) {
		HashMap<String, String> reqParams = new HashMap<>();
		reqParams.put("vendorId", String.valueOf(vendorId));

		return reqProcessor.performRequest(Vendor.class, "get_vendor", reqParams);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.essence.hc.persistence.UserDAO#getPatients(java.lang.String)
	 */
	public List<Patient> getPatients(String userId) {
		/*
		 * Retrieve related patient ids and get them one by one.
		 */
		List<Patient> patients = null;
		List<String> patientIds = this.getPatientIds(userId);
		if (patientIds != null) {
			logger.info("Processing cached ids...");
			patients = new ArrayList<Patient>();
			for (String patientId : patientIds) {
				patients.add(patientDAO.getById(patientId));
			}
		}
		return patients;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.essence.hc.persistence.UserDAO#getAlerts(java.lang.String)
	 */
	public List<Alert> getAlerts(String userId) {
		/**
		 * TODO: ¿¿Cómo se actualizan Las nuevas alertas que puede haber en el servidor
		 * remoto de Essence??
		 */
		/*
		 * Retrieve related ids from and then get alerts one by one.
		 */
		List<Alert> alerts = null;
		List<String> alertIds = this.getAlertIds(userId);
		if (alertIds != null) {
			logger.info("Processing cached ids...");
			alerts = new ArrayList<Alert>();
			for (String alertId : alertIds) {
				alerts.add(alertDAO.getById(alertId));
			}
		}
		return alerts;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.essence.hc.persistence.UserDAO#saveNewAlert(java.lang.String,
	 * com.essence.hc.model.Alert)
	 */
	@Override
	@CachePut(USER_ALERTS_CACHE)
	public List<String> saveAlert(String userId, Alert alert) {
		List<String> alertIds = this.getAlertIds(userId);
		if (alertIds == null) {
			alertIds = new ArrayList<String>();
		}
		// Only save new alerts
		if (!alertIds.contains(alert.getId())) {
			logger.info("Saving Alert {} at user's Alerts Cache", alert.getId());
			alertIds.add(alert.getId());
		}
		return alertIds;
	}

	/**
	 * Return a list of Alert ids for the specified user
	 * 
	 * @param userId
	 *            User identificator
	 * @return the list of alert ids for the user
	 */
	/*
	 * @Cacheable("userAlerts") NOTE: Caching Annotations don't work for private
	 * methods nor for those methods invoked from inside the object (as in this
	 * case) more info:
	 * http://static.springsource.org/spring/docs/3.1.x/spring-framework-reference/
	 * html/cache.html
	 */
	private List<String> getAlertIds(String userId) {

		List<String> alertIds = (List<String>) this.get(USER_ALERTS_CACHE, userId);
		if (alertIds == null) {
			logger.info("Alert ids are not cached yet. Retrieving them from datasource...");
			List<Alert> alerts = alertDAO.getByUserId(userId);
			if (alerts != null) {
				alertIds = new ArrayList<String>();
				for (Alert newAlert : alerts) {
					alertIds.add(newAlert.getId());
				}
				this.put(USER_ALERTS_CACHE, userId, alertIds);
			}
		}
		return alertIds;
	}

	/**
	 * Return a list of Patient ids for the specified user
	 * 
	 * @param userId
	 *            User identificator
	 * @return the list of alert ids for the user
	 */
	/*
	 * @Cacheable("userPatients") NOTE: Caching Annotations don't work for private
	 * methods nor for those methods invoked from inside the object (as in this
	 * case) more info:
	 * http://static.springsource.org/spring/docs/3.1.x/spring-framework-reference/
	 * html/cache.html
	 */
	private List<String> getPatientIds(String userId) {

		List<String> patientIds = (List<String>) this.get(USER_PATIENTS_CACHE, userId);
		if (patientIds == null) {
			logger.info("Patients ids are not cached yet. Retrieving them from datasource...");
			List<Patient> patients = patientDAO.getByUserId(userId);
			if (patients != null) {
				patientIds = new ArrayList<String>();
				for (Patient newPatient : patients) {
					patientIds.add(newPatient.getStringUserId());
				}
				this.put(USER_PATIENTS_CACHE, userId, patientIds);
			}
		}
		return patientIds;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.essence.hc.persistence.UserDAO#getCommands(java.lang.String)
	 */
	// @Cacheable(USER_COMMANDS_CACHE)
	public Map<String, HCCommand> getCommands(String userId, StatusTypes systemStatus) {

		logger.info("\nRetrieving user commands...\n");

		Map<String, HCCommand> commandMap = null;
		HashMap<String, String> reqParams = new HashMap<String, String>();
		reqParams.put("userId", userId);
		reqParams.put("alertSystemStsId", String.valueOf(systemStatus.getStatus()));
		// RequestProcessor reqProcessor = new DummyRequestProcessor(); // TODO THIS
		// SENTENCE IS REQUIRED ONLY FOR TESTING
		commandMap = reqProcessor.performRequest(Map.class, "user_commands", reqParams);
		// if (commands != null){
		// commandMap = new HashMap<String, HCCommand>();
		// for(HCCommand c: commands){
		// logger.info("\nCommand retrieved: {}\n",c.getName());
		// commandMap.put(c.getName(), c);
		// }
		// }
		return commandMap;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.essence.hc.persistence.UserDAO#addUser(com.essence.hc.model.SystemUser)
	 */
	@Override
	public ResponseStatus addUser(SystemUser user) {

		return reqProcessor.performRequest(ResponseStatus.class, "add_user", populateUserReqParams(user));

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.essence.hc.persistence.UserDAO#updateUser(com.essence.hc.model.
	 * SystemUser)
	 */
	@CacheEvict(value = "patients", allEntries = true) // patients Cache eviction after successfully user update
	@Override
	public ResponseStatus updateUser(SystemUser user) {

		return reqProcessor.performRequest(ResponseStatus.class, "update_user", populateUserReqParams(user));

	}

	@CacheEvict(value = "patients", allEntries = true) // patients Cache eviction after successfully user deletion
	@Override
	public ResponseStatus deleteUser(String userId) {
		HashMap<String, String> reqParams = new HashMap<String, String>();

		reqParams.put("userId", userId);

		ResponseStatus resp = reqProcessor.performRequest(ResponseStatus.class, "delete_user", reqParams);
		// remove("patients", userId);
		return resp;
	}

	@Override
	public SystemUser getUser(String userId) {
		HashMap<String, String> reqParams = new HashMap<String, String>();

		reqParams.put("id", userId);
		return reqProcessor.performRequest(SystemUser.class, "get_user", reqParams);
	}

	/***
	 * 
	 * @param user
	 * @return
	 */
	private HashMap<String, String> populateUserReqParams(SystemUser user) {
		HashMap<String, String> reqParams = new HashMap<String, String>();

		reqParams.put("userId", (user.getUserId() > 0) ? String.valueOf(user.getUserId()) : null);
		reqParams.put("vendorId", String.valueOf(user.getVendorId()));
		reqParams.put("fname", user.getFirstName());
		reqParams.put("lname", user.getLastName());
		reqParams.put("sAddress", user.getAddress());
		reqParams.put("generalId", user.getGeneralId());
		reqParams.put("telehealth", String.valueOf(user.getTelehealth()));
		reqParams.put("mobile", user.getMobile());
		reqParams.put("phone", user.getPhone());
		reqParams.put("semail", user.getEmail());
		reqParams.put("sUsrType", String.valueOf(user.getUserType().getId()));
//		reqParams.put("residentCode", user.getRes());
		reqParams.put("mobileModeOnDesktop", String.valueOf(user.getMobileModeOnDesktop()));
		reqParams.put("gender", (user.getGender() != null) ? String.valueOf(user.getGender().getId()) : null);
		reqParams.put("languageId", String.valueOf(user.getLanguage().getLanguageID()));
		reqParams.put("birthDate",
				(user.getBirthDate() != null) ? Util.formatDate(user.getBirthDate(), Util.DATEFORMAT_OUTPUT_PLAIN)
						: null);
		if (user.getServiceType() != null)
			reqParams.put("serviceType", user.getServiceType().getName());

		if (user.getEncodedPasswd() == null) // && user.getPasswd() != null)
			user.setEncodedPasswd(passwordEncoder.encodePassword(user.getPasswd(), "ADL"));
		reqParams.put("spswrd", user.getEncodedPasswd());

		// reqParams.put("userQuestion", user.getQuestion());
		// reqParams.put("userAnswer", user.getAnswer());

		/*
		 * Resident(Patient) specific fields
		 */
		if (user instanceof Patient) {
			Panel panel = ((Patient) user).getInstallation().getPanel();
			if (panel != null) {
				reqParams.put("account", panel.getAccount());
				reqParams.put("simNum", panel.getSimNumber());
				reqParams.put("landlineNumber", panel.getLandlineNumber());
				reqParams.put("serialNum", panel.getSerialNumber());
				reqParams.put("iDTMFCode", panel.getiDTMFCode());
				reqParams.put("serialNumCustomer", panel.getCustomerSerialNumber());
				reqParams.put("installNotes", panel.getInstallNotes());
				reqParams.put("hasPets", String.valueOf(panel.hasPets()));
				/*
				 * FIXME: Resident users should not require login info The following is a
				 * temporal fix until backend API will accept resident users creation without
				 * login info
				 */
				user.setNick("Resident_" + panel.getAccount());
			}
		}

		reqParams.put("slgin", user.getNick());

		return reqParams;
	}

	@Override
	public List<SystemUser> getSystemUsers(String iUserId, String iUserRol, String iVendorId, int iNumOfItems,
			String iPageNum, String sRoleFltr, String sSearchCriteria, String sCriteriaValue, String orderBy,
			Boolean sortAsc, String[] panelTypeFilter, ServiceType[] serviceTypeFilter, Boolean enableActiveService) {

		HashMap<String, String> reqParams = new HashMap<String, String>();
		reqParams.put("iUserId", String.valueOf(iUserId));
		reqParams.put("iUserRol", iUserRol);
		reqParams.put("iVendorId", String.valueOf(iVendorId));
		reqParams.put("iNumOfItems", String.valueOf(iNumOfItems));
		reqParams.put("iPageNum", String.valueOf(iPageNum));
		reqParams.put("sRoleFltr", sRoleFltr);
		if (sSearchCriteria != null) {
			reqParams.put("sSearchCriteria", sSearchCriteria);
			reqParams.put("sCriteriaValue", sCriteriaValue);
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

		// try {
		//// reqParams.put("sSearchCriteria",
		// URLEncoder.encode(sSearchCriteria,"UTF-8"));
		//// reqParams.put("sCriteriaValue", URLEncoder.encode(sCriteriaValue,"UTF-8"));
		// } catch (UnsupportedEncodingException e) {
		// throw new DataAccessException(e);
		// }
		// reqParams.put("sSearchCriteria", sSearchCriteria);
		// reqParams.put("sCriteriaValue", sCriteriaValue);

		if (Util.isDebug()) {
			return new DummyRequestProcessor().performRequest(ArrayList.class, "system_users", reqParams);
		}
//
		return reqProcessor.performRequest(ArrayList.class, "system_users", reqParams);
	}

	@Override
	public ResponseStatus AlertConfigSave(String iUsrId1, String iUsrId2, Map<String, Boolean> alerts) {

		HashMap<String, String> reqParams = new HashMap<String, String>();

		reqParams.put("iUsrId1", iUsrId1);
		reqParams.put("iUsrId2", iUsrId2);

		this.prepareAlertPreferences(alerts, reqParams);

		return reqProcessor.performRequest(ResponseStatus.class, "save_config_alerts", reqParams);
	}

	@Override
	public String getVersion() {
		HashMap<String, String> reqParams = new HashMap<String, String>();
		reqParams.put("version", "1");
		ResponseStatus response = reqProcessor.performRequest(ResponseStatus.class, "get_version", reqParams);
		return response.getMessageErr();
	}

	@Override
	public List<String> getPredefinedTextByCommandId(String commandId) {
		HashMap<String, String> reqParams = new HashMap<String, String>();
		reqParams.put("commandName", commandId);
		List<String> response = reqProcessor.performRequest(ArrayList.class, "message_commands", reqParams);
		return response;
	}

	@Override
	public ResponseStatus setUserPreferences(int userId, int vendorId, int languageId) {
		HashMap<String, String> reqParams = new HashMap<String, String>();
		reqParams.put("userId", String.valueOf(userId));
		reqParams.put("vendorId", String.valueOf(vendorId));
		reqParams.put("languageId", String.valueOf(languageId));

		return reqProcessor.performRequest(ResponseStatus.class, "set_preferences", reqParams);
	}

	/* ---------- Cache Handling ---------------- */

	private Object get(String cacheName, String key) {
		logger.info("Getting Cached Element. Cache name: {} , Key: {} ", cacheName, key);
		ValueWrapper element = getCache(cacheName).get(key);
		if (element != null)
			return element.get();
		return null;
	}

	private void put(final String cacheName, final String key, final Object value) {
		logger.info("Caching Element at Cache: {} , with Key: {} ", cacheName, key);
		getCache(cacheName).put(key, value);
	}

	// private void remove(final String cacheName, final String key) {
	// logger.info("Removing Element in Cache: {} , with Key: {} ", cacheName, key);
	// getCache(cacheName).evict(key);
	// }

	private Cache getCache(final String cacheName) {
		return cacheManager.getCache(cacheName);
	}

	@Override
	public List<Language> getLanguages() {
		HashMap<String, String> reqParams = new HashMap<String, String>();
		reqParams.put("lang", "all");
		List<Language> ret = reqProcessor.performRequest(ArrayList.class, "list_languages", reqParams);

		return ret;
	}

	@Override
	public AlertPreferences getAlertPreferences(int userId, int vendorId) {
		HashMap<String, String> reqParams = new HashMap<String, String>();
		reqParams.put("userId", String.valueOf(userId));
		reqParams.put("vendorId", String.valueOf(vendorId));
		reqParams.put("relatedUserId", String.valueOf(userId));

		AlertPreferences preferences = reqProcessor.performRequest(AlertPreferences.class, "get_AlertPreferences",
				reqParams);

		return preferences;
	}

	@Override
	@Deprecated
	public ResponseStatus setAlertPreferences(int userId, int vendorId, int relatedUserId,
			Map<String, Boolean> alerts) {
		logger.warn("Call to deprecated method setAlertPreferences");

		HashMap<String, String> reqParams = new HashMap<String, String>();

		reqParams.put("userId", String.valueOf(userId));
		reqParams.put("relatedUserId", String.valueOf(relatedUserId));
		reqParams.put("vendorId", String.valueOf(vendorId));

		this.prepareAlertPreferences(alerts, reqParams);

		return reqProcessor.performRequest(ResponseStatus.class, "set_AlertPreferences", reqParams);
	}

	/**
	 * Prepares request params for user's alert preferences
	 * 
	 * @param alerts
	 * @param reqParams
	 */
	private void prepareAlertPreferences(Map<String, Boolean> alerts, Map<String, String> reqParams) {
		// weird map for matching alert names??!!
		HashMap<String, String> alertConf = new HashMap<String, String>();

		alertConf.put(AlertConfigPrsr.ALERT_PREFS_NAMES.get("Panic"), "bPanic");
		alertConf.put(AlertConfigPrsr.ALERT_PREFS_NAMES.get("LowActivity"), "bActvt");
		alertConf.put(AlertConfigPrsr.ALERT_PREFS_NAMES.get("SmokeAlarm"), "bSmk");
		alertConf.put(AlertConfigPrsr.ALERT_PREFS_NAMES.get("WaterLeakageAlarm"), "bLkg");
		alertConf.put(AlertConfigPrsr.ALERT_PREFS_NAMES.get("PossibleFall"), "bFll");
		alertConf.put(AlertConfigPrsr.ALERT_PREFS_NAMES.get("DoorOpen"), "bDoor");
		alertConf.put(AlertConfigPrsr.ALERT_PREFS_NAMES.get("NotAtHome"), "bAtHm");
		alertConf.put(AlertConfigPrsr.ALERT_PREFS_NAMES.get("Technical"), "bGetTech");
		alertConf.put("alertNotificationEmail", "alertsByEmail");
		alertConf.put("alertNotificationSMS", "alertsBySMS");
		alertConf.put("dailyReportSMS", "reportBySMS");
		alertConf.put("dailyReportEmail", "reportByEmail");

		Iterator<String> iter = alertConf.keySet().iterator();
		if (alerts != null) {
			while (iter.hasNext()) {
				String it = iter.next();
				String value = (alerts.containsKey(it)) ? "1" : "0";
				reqParams.put(alertConf.get(it), value);
			}
		}
		/*
		 * New for v2.0 Express New alert types in v.2.0 Express require the value to be
		 * sent as boolean
		 */
		/*
		 * Deprecated if (alerts.containsKey("ActivityAlert"))
		 * reqParams.put("activityAlert", String.valueOf(alerts.get("ActivityAlert")));
		 */

		if (alerts.containsKey("ExtremeTemperature"))
			reqParams.put("ExtremeTemperature", String.valueOf(alerts.get("ExtremeTemperature")));

		if (alerts.containsKey("UnexpectedEntryExit"))
			reqParams.put("UnexpectedEntryExit", String.valueOf(alerts.get("UnexpectedEntryExit")));

		if (alerts.containsKey("AtHomeForTooLong"))
			reqParams.put("AtHomeForTooLong", String.valueOf(alerts.get("AtHomeForTooLong")));
		
		/*
		 * New for v2.2
		 */
		if (alerts.containsKey("AbnormalActivity"))
			reqParams.put("abnormal", String.valueOf(alerts.get("AbnormalActivity")));

		if (alerts.containsKey("bGetActvt")) // LowActivity
			reqParams.put("lowActivity", String.valueOf(alerts.get("bGetActvt")));

		if (alerts.containsKey("ExcessiveActivity"))
			reqParams.put("excessiveActivity", String.valueOf(alerts.get("ExcessiveActivity")));

		if (alerts.containsKey("Presence"))
			reqParams.put("presence", String.valueOf(alerts.get("Presence")));

	}

	@Override
	public ResponseStatus setAdminPreferences(int userId, Map<String, Boolean> alerts) {

		HashMap<String, String> reqParams = new HashMap<String, String>();
		reqParams.put("userId", String.valueOf(userId));

		this.prepareAlertPreferences(alerts, reqParams);

		return reqProcessor.performRequest(ResponseStatus.class, "set_AdminPreferences", reqParams);
	}

	@Override
	public ResponseStatus IsValidUserName(String userName, int editingUserId) {
		HashMap<String, String> reqParams = new HashMap<String, String>();
		reqParams.put("userName", userName);
		reqParams.put("editingUserId", String.valueOf(editingUserId));
		ResponseStatus response = reqProcessor.performRequest(ResponseStatus.class, "IsValidUserName", reqParams);

		return response;
	}

	@Override
	public ResponseStatus IsValidGeneralId(String generalId, int editingUserId) {
		HashMap<String, String> reqParams = new HashMap<String, String>();
		reqParams.put("generalId", generalId);
		reqParams.put("editingUserId", String.valueOf(editingUserId));
		ResponseStatus response = reqProcessor.performRequest(ResponseStatus.class, "IsValidGeneralId", reqParams);

		return response;
	}
}
