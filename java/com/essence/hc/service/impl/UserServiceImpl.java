package com.essence.hc.service.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.ServletContext;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.encoding.PasswordEncoder;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.essence.hc.exceptions.BadCredentialsException;
import com.essence.hc.exceptions.PasswordExpiredException;
import com.essence.hc.model.AccountInformation;
import com.essence.hc.model.AccountInformationUser;
import com.essence.hc.model.Alert;
import com.essence.hc.model.AlertPreferences;
import com.essence.hc.model.ExternalAPILogin;
import com.essence.hc.model.ExternalAPIResidentMonitoring;
import com.essence.hc.model.FreePanel;
import com.essence.hc.model.HCCommand;
import com.essence.hc.model.Language;
import com.essence.hc.model.Panel;
import com.essence.hc.model.Patient;
import com.essence.hc.model.ResponseStatus;
import com.essence.hc.model.ServicePackageInformation;
import com.essence.hc.model.SystemStatus.StatusTypes;
import com.essence.hc.model.SystemUser;
import com.essence.hc.model.ThirdPartyUser;
import com.essence.hc.model.User;
import com.essence.hc.model.User.CaregiverType;
import com.essence.hc.model.User.UserType;
import com.essence.hc.model.Vendor;
import com.essence.hc.model.Vendor.ServiceType;
import com.essence.hc.model.VendorConfigurationSettings;
import com.essence.hc.model.comparators.PatientByEnableActiveServiceComparator;
import com.essence.hc.model.comparators.SystemUserByAccountNumberComparator;
import com.essence.hc.model.comparators.SystemUserByGeneralIdComparator;
import com.essence.hc.model.comparators.SystemUserByNameComparator;
import com.essence.hc.model.comparators.SystemUserByRoleComparator;
import com.essence.hc.model.comparators.SystemUserByServiceTypeComparator;
import com.essence.hc.persistence.UserDAO;
import com.essence.hc.persistenceexternal.ExternalAccountDAO;
import com.essence.hc.persistenceexternal.ExternalConfigurationDAO;
import com.essence.hc.persistenceexternal.ExternalLoginDAO;
import com.essence.hc.persistenceexternal.ExternalUsersDAO;
import com.essence.hc.service.UserService;
import com.essence.hc.util.Util;
import com.essence.security.SecurityService;

@Service
public class UserServiceImpl implements UserService {

	protected final Logger logger = LoggerFactory.getLogger(getClass());

	@Autowired
	PasswordEncoder passwordEncoder;
	@Autowired
	private UserDAO dao;
	@Autowired
	private ExternalLoginDAO externalLoginDao;
	@Autowired
	private ExternalAccountDAO externalAccountDao;
	@Autowired
	private ExternalUsersDAO externalUsersDao;
	@Autowired
	private ExternalConfigurationDAO externalConfigurationDao;
	@Autowired
	SecurityService securityService;
	@Autowired
	ServletContext context;

	/**
	 * Nuevo usuario
	 */
	@Override
	public int NewUser(String login, String password, String name) {
		return 0; // dao.newUser(login, password, 1, name);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.essence.hc.service.UserService#GetUserWithSecurity(java.lang.String,
	 * java.lang.String, java.lang.String, java.lang.String, java.lang.Boolean)
	 */
	public User GetUserWithSecurity(String login, String password, String passwordSHA, String country, String service,
			Boolean s) {

		// 1. Login and GetToken
		ExternalAPILogin externalLoginResponse = externalLoginDao.login(login, password);
		if (externalLoginResponse.getResponse().equals("111")) {
			// Password Expired.
			throw new PasswordExpiredException(externalLoginResponse.getPasswordExpirationReason(),
					externalLoginResponse.getPasswordPolicy());
		}
		if (externalLoginResponse.getResponse().equals("2")  || externalLoginResponse.getResponse().equals("67") || externalLoginResponse.getResponse().equals("66")) {
			// Wrong password - Return null - bad credentials exception is going to be
			// thrown
			throw new BadCredentialsException();
		}

		// 2. Get user info by token
		// User user = dao.getUserByCredentials(login, passwordSHA, country, service,
		// s);
		User user = externalUsersDao.getLoggedinUserInfoWithToken(externalLoginResponse.getToken());
		List<GrantedAuthority> AUTHORITIES = new ArrayList<GrantedAuthority>();
		AUTHORITIES.add(new SimpleGrantedAuthority("ROLE_USER"));
		if (user.getUserType() != null)
			AUTHORITIES.add(new SimpleGrantedAuthority(user.getUserType().toString()));
		user.setAUTHORITIES(AUTHORITIES);
		user.setEnabled(true);
		user.setVendor(new Vendor());

		if (user != null) { // If user == null, return null and BadCredentialsException is going to be
			// thrown
			logger.info("\nUser {} successfully authenticated\n", user.getUsername());

			// User succesfully logged in, there is a token value
			user.setToken(externalLoginResponse.getToken());
			if (externalLoginResponse.getPasswordExpirationData() != null) {
				// Case: User succesfully logged in must check expiration dates against
				// aboutToExpire dates
				user.setPasswordExpirationData(externalLoginResponse.getPasswordExpirationData());
			}
			if (externalLoginResponse.getEula() != null) {
				// Case: User succesfully logged in must check expiration dates against
				// aboutToExpire dates
				user.setEula(externalLoginResponse.getEula());
			}
		}

		if (user.getUserType() == UserType.ROLE_CAREGIVER) {

			List<Patient> patients = this.getMinSystemUsersForCaregiver(externalLoginResponse.getToken());
			user.setPatients(patients);
		}

		return user;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.essence.hc.service.UserService#GetUserWithToken(java.lang.String)
	 */
	public User getUserWithToken(String token) {

		// Get user info using token

		User user = externalUsersDao.getLoggedinUserInfoWithToken(token);
		List<GrantedAuthority> AUTHORITIES = new ArrayList<GrantedAuthority>();
		AUTHORITIES.add(new SimpleGrantedAuthority("ROLE_USER"));
		if (user.getUserType() != null)
			AUTHORITIES.add(new SimpleGrantedAuthority(user.getUserType().toString()));
		user.setAUTHORITIES(AUTHORITIES);
		user.setEnabled(true);
		user.setVendor(new Vendor());

		if (user != null) { // If user == null, return null and BadCredentialsException is going to be
			// thrown
			logger.info("\nUser {} successfully authenticated\n", user.getUsername());

			// User successfully logged in, there is a token value
			user.setToken(token);
		}

		if (user.getUserType() == UserType.ROLE_CAREGIVER) {

			List<Patient> patients = this.getMinSystemUsersForCaregiver(token);
			user.setPatients(patients);
		}

		return user;
	}

	public ThirdPartyUser getThirdPartyUserWithToken(String token, String externalPartyAuthenticationProvider) {
		return externalLoginDao.thirdPartyLogin(token, externalPartyAuthenticationProvider);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.essence.hc.service.UserService#logout(com.essence.hc.model.User)
	 */
	public void logout(User user) {
		if (user == null) {
			return;
		}

		if (!user.isSessionStartedByExternalSystem()) {
			externalLoginDao.logout(user.getToken());
			user.setToken(null);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.essence.hc.service.UserService#prepareSession(java.lang.String)
	 */
	@Deprecated
	public User prepareSession() {
		User currentUser = securityService.getPrincipal();
		logger.info("\n---Preparing session for user {}---\n", currentUser.getFirstName());
		currentUser.setPatients(this.getPatients(currentUser.getId()));
		currentUser.setCurrentAlerts(this.getAlerts(currentUser.getId()));
		logger.info("\n--- Session Prepared ---\n");
		return currentUser;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.essence.hc.service.UserService#getPatients(java.lang.String)
	 */
	@Deprecated
	public List<Patient> getPatients(String userId) {
		logger.info("Retrieving Patients for user {}", userId);
		return dao.getPatients(userId);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.essence.hc.service.UserService#getPatients(com.essence.hc.model.User)
	 */
	public List<Patient> getPatients(User user) {
		return user.getPatients();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.essence.hc.service.UserService#setCurrentPatient(com.essence.hc.model.
	 * User, com.essence.hc.model.Patient)
	 */
	public void setCurrentPatient(User user, Patient newCurPatient) {
		user.setCurrentPatient(newCurPatient);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.essence.hc.service.UserService#getAlerts(java.lang.String)
	 */
	@Override
	public List<Alert> getAlerts(String userId) {
		logger.info("Retrieving Alerts for user {} ", userId);
		return dao.getAlerts(userId);

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.essence.hc.service.UserService#getOpenAlerts(java.lang.String)
	 */
	@Deprecated
	public List<Alert> getOpenAlerts(String userId, boolean includeRecentlyClosed) {
		logger.info("Retrieving Open Alerts for user {} ", userId);
		List<Alert> openAlerts = null;
		List<Alert> alerts = this.getAlerts(userId);
		if (alerts != null) {
			openAlerts = new ArrayList<Alert>();
			for (Alert alert : alerts) {
				if (!alert.isClosed()) {
					openAlerts.add(alert);
				} else {
					if (includeRecentlyClosed) {
						long currentTime = new Date().getTime();
						long alertTime = alert.getEndDateTime().getTime();
						// logger.info("\n\nLa alerta {} lleva cerrada: {} milisegundos \n\n",
						// alert.getId(), currentTime - alertTime);
						if (currentTime - alertTime < Util.CLOSED_ALERT_TIMEOUT) {
							openAlerts.add(alert);
						}
					}
				}
			}
		}
		return openAlerts;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.essence.hc.service.UserService#getCommands(java.lang.String,
	 * com.essence.hc.model.SystemStatus.StatusTypes)
	 */
	public Map<String, HCCommand> getCommands(String userId, StatusTypes systemStatus) {
		return dao.getCommands(userId, systemStatus);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.essence.hc.service.UserService#addUser(com.essence.hc.model.SystemUser)
	 */
	@Override
	public ResponseStatus addUser(SystemUser user) {
		return externalUsersDao.addUser(user);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.essence.hc.service.UserService#deleteUser(java.lang.String)
	 */
	@Override
	public ResponseStatus deleteUserById(String userId) {
		SystemUser user = new SystemUser();
		user.setUserId(Integer.valueOf(userId));

		return externalUsersDao.deleteUser(user);
	}

	@Override
	public ResponseStatus deleteUser(SystemUser user) {
		return externalUsersDao.deleteUser(user);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.essence.hc.service.UserService#getUser(java.lang.String)
	 */
	@Override
	public SystemUser getUser(String userId) {

		SystemUser systemUser = dao.getUser(userId);

		if (systemUser.getUserType() == UserType.ROLE_MONITORED) {
			setPatientAvailableReports((Patient) systemUser);
		}
		
		return systemUser;
	}

	private void setPatientAvailableReports(Patient patient) {

		User currentUser = (User) securityService.getPrincipal();
		List<Patient> patients = currentUser.getCurrentPatients();

		if (patients != null) {
			for(Patient resident: patients) {
				if (resident.getUserId() == patient.getUserId()) {
					patient.setAvailableReports(resident.getAvailableReports());
					break;
				}
			}
		} else {
			List<ExternalAPIResidentMonitoring> residentMonitoringInfo = currentUser.getResidentsMonitoring();
			if (residentMonitoringInfo != null) {
				for(ExternalAPIResidentMonitoring residentMonitoring: residentMonitoringInfo) {
					if (residentMonitoring.getUserId() == patient.getUserId()) {
						patient.setAvailableReports(residentMonitoring.getAvailableReports());
						break;
					}
				}
			}
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.essence.hc.service.UserService#DeleteUser(java.lang.String)
	 */
	@Override
	public List<SystemUser> getSystemUsers(String iUserId, String iUserRol, String iVendorId, int iNumOfItems,
			String iPageNum, String sRoleFltr, String sSearchCriteria, String sCriteriaValue, String orderBy,
			Boolean sortAsc, String[] panelTypeFilter, ServiceType[] serviceTypeFilter, Boolean enableActiveService) {

		return dao.getSystemUsers(iUserId, iUserRol, iVendorId, iNumOfItems, iPageNum, sRoleFltr, sSearchCriteria,
				sCriteriaValue, orderBy, sortAsc, panelTypeFilter, serviceTypeFilter, enableActiveService);

	}

	@Override
	public List<SystemUser> getSystemUsersForCaregiver(String sSearchCriteria, String sCriteriaValue, String orderBy,
			Boolean sortAsc, UserType[] userTypes, ServiceType[] serviceTypes, Boolean enableActiveService) {

		List<SystemUser> result = new ArrayList<SystemUser>();
		List<Patient> patients = externalAccountDao.getPermittedAccounts(null);

		// filter by user type
		List<UserType> userTypesFilter = new ArrayList<UserType>();
		List<UserType> residentFilter = new ArrayList<UserType>();
		if (userTypes == null) {
			userTypesFilter.add(UserType.ROLE_CAREGIVER);
			userTypesFilter.add(UserType.ROLE_MONITORED);
			residentFilter.add(UserType.ROLE_MONITORED);
		} else {
			for (UserType ut : userTypes) {
				userTypesFilter.add(ut);
				if (ut.equals(UserType.ROLE_MONITORED)) {
					residentFilter.add(ut);
				}
			}
		}

		List<ServiceType> serviceTypeList = null;
		if (serviceTypes != null) {
			serviceTypeList = Arrays.asList(serviceTypes);
		}

		Set<String> caregivers = new HashSet<String>();
		for (Patient p : patients) {

			List<SystemUser> users;
			if (p.isMaster()) {
				users = externalUsersDao.getUsersForAccount(p.getInstallation().getPanel(), userTypesFilter, null);
			} else if (!residentFilter.isEmpty()) {
				users = externalUsersDao.getUsersForAccount(p.getInstallation().getPanel(), residentFilter, null);
			} else {
				users = new ArrayList<SystemUser>();
			}
			for (SystemUser u : users) {
				u.setCaregiverType(p.getCaregiverType());
				u.setAssigned(true);
				if (u.isResident()) {
					((Patient) u).setInstallation(p.getInstallation());
					Panel panel = p.getInstallation().getPanel();
					u.setAddress(p.getAddress());
					u.setPhone(p.getPhone());

					// filter by service type
					if (serviceTypeList == null || serviceTypeList.contains(panel.getServiceType())) {
						if (panel.getServiceType() != null) {
							u.setServiceType(panel.getServiceType());
						}
						u.setAccountNumber(panel.getServiceProviderAccountNumber());

						// filter by enableActiveService
						if (enableActiveService == null || enableActiveService.equals(panel.isEnableActiveService())) {
							// filter by search criteria
							if (meetsSearch(u, sSearchCriteria, sCriteriaValue)) {
								result.add(u);
							}
						}
					}
				} else { // caregiver
					if (!caregivers.contains(u.getNick())) {
						caregivers.add(u.getNick());
						// filter by search criteria
						if (meetsSearch(u, sSearchCriteria, sCriteriaValue)) {
							result.add(u);
						}
					}
				}
			}
		}

		// order
		Comparator<SystemUser> comparator = null;
		if ("ServiceType".equals(orderBy)) {
			comparator = new SystemUserByServiceTypeComparator();
		} else if ("AccountNumber".equals(orderBy)) {
			comparator = new SystemUserByAccountNumberComparator();
		} else if ("GeneralId".equals(orderBy)) {
			comparator = new SystemUserByGeneralIdComparator();
		} else if ("SecurityLevelId".equals(orderBy)) {
			comparator = new SystemUserByRoleComparator();
		} else if ("FirstName".equals(orderBy)) {
			comparator = new SystemUserByNameComparator();
		} else if ("EnableActiveService".equals(orderBy)) {
			comparator = new PatientByEnableActiveServiceComparator();
		}
		if (sortAsc != null && !sortAsc) {
			comparator = Collections.reverseOrder(comparator);
		}
		if (comparator != null) {
			Collections.sort(result, comparator);
		}

		return result;
	}

	private boolean meetsSearch(SystemUser user, String searchCriteria, String value) {
		if (value == null || value.isEmpty()) {
			return true;
		}
		String field = "";
		try {
			if ("nameFilter".equals(searchCriteria)) {
				field = user.getFirstName() + ' ' + user.getLastName();
			} else if ("resCodeFilter".equals(searchCriteria)) {
				field = user.getAccountNumber();
			} else if ("generalIdFilter".equals(searchCriteria)) {
				field = user.getGeneralId();
			} else if ("addressFilter".equals(searchCriteria)) {
				field = user.getAddress();
			} else if ("mobileFilter".equals(searchCriteria)) {
				field = user.getMobile();
			} else if ("landlineFilter".equals(searchCriteria)) {
				field = user.getPhone();
			} else if ("accountFilter".equals(searchCriteria) && user.isResident()) {
				field = ((Patient) user).getInstallation().getPanel().getAccount();
			}
			return field.toLowerCase().contains(value.toLowerCase());
		} catch (Exception e) {
			return false;
		}
	}

	@Override
	public ResponseStatus updateUser(SystemUser user) {
		// return dao.updateUser(user);
		return externalUsersDao.updateUser(user);
	}

	@Override
	public ResponseStatus alertConfigSave(String iUsrId1, String iUsrId2, Map<String, Boolean> alerts) {
		return dao.AlertConfigSave(iUsrId1, iUsrId2, alerts);
	}

	@Override
	public ResponseStatus updatePatient(String loggedUser, SystemUser currentUser, SystemUser patient,
			Map<String, Boolean> alerts) {
		// ResponseStatus status = updateUser(patient);
		ResponseStatus status = externalUsersDao.updateUser(patient);
		// Checking if the update user response was successful
		if (!status.isOK())
			return status;

		if (alerts != null) {
			status = alertConfigSave(String.valueOf(patient.getUserId()), "0", alerts);
			if (!status.isOK())
				return status;
		}

		status = externalAccountDao.updateAccount((Patient) patient, currentUser);
		return status;
	}

	@Override
	public String getVersion() {
		String systemVersion;
		String apiVersion = dao.getVersion();
		Util.setAPIVersion(apiVersion);
		systemVersion = Util.getSystemVersion();

		/*
		 * version stored in servlet context so all jsp's can read it with no need to
		 * put it in the model
		 */
		context.setAttribute("version", systemVersion);

		return systemVersion;
	}

	@Override
	public List<String> getPredefinedTextByCommandId(String commandId) {
		return dao.getPredefinedTextByCommandId(commandId);
	}

	@Override
	public List<Language> getLanguages() {
		User currentUser = securityService.getPrincipal();
		if (currentUser == null || currentUser.getToken() == null) {
			logger.info("getting languages from old API");
			return dao.getLanguages();
		} else {
			return externalUsersDao.getLanguages();
		}
	};

	@Override
	public void loadVendorConfigurationSettings(Vendor vendor) {
		VendorConfigurationSettings vcs = externalConfigurationDao.getVendorConfigurationSettings();
		vendor.setConfigSettings(vcs);
		List<ServiceType> sts = new ArrayList<>();
		for (ServicePackageInformation spi : vcs.getServicePackages()) {
			sts.add(getServiceTypePerCodeName(spi.getCodeName()));
		}
		ServiceType[] sta = new ServiceType[sts.size()];
		sta = sts.toArray(sta);
		vendor.setServiceTypes(sta);
	}

	/*
	 * 2.4.5 Historically CodeNames were: {Express, Analytics} and our servicetype
	 * enum is related to that names. We define this function in order to grant
	 * backward compatibility.
	 */
	private ServiceType getServiceTypePerCodeName(String codeName) {
		switch (codeName) {
		case "Pro":
			return ServiceType.ANALYTICS;
		case "Family":
			return ServiceType.EXPRESS;
		case "PERS-E":
			return ServiceType.PERSE;
		case "Umbrella":
			return ServiceType.HELP_ANYWHERE;
		default:
			throw new RuntimeException(String.format(
					"There is some inconsistency with the backend server responses. CodeName: %s is not an expected codeName",
					codeName));
		}
	}

	@Override
	public void loadVendorTimeZones(Vendor vendor, int languageId) {
		vendor.setTimeZones(externalConfigurationDao.getTimeZones(languageId));
	}

	@Override
	public ResponseStatus setUserPreferences(int userId, int vendorId, int languageId) {
		return dao.setUserPreferences(userId, vendorId, languageId);
	}

	@PreAuthorize("isAuthenticated()")
	public AlertPreferences getAlertPreferences(int userId, int vendorId) {
		return dao.getAlertPreferences(userId, vendorId);
	}

	@PreAuthorize("isAuthenticated()")
	public ResponseStatus setAlertPreferences(int userId, int vendorId, int relatedUserId,
			Map<String, Boolean> listAlertTypes) {
		if (userId == relatedUserId) {
			// Admin
			return dao.setAdminPreferences(userId, listAlertTypes);
		} else {
			// Caregiver <-> Resident
			return dao.setAlertPreferences(userId, vendorId, relatedUserId, listAlertTypes);
		}
	}

	@Override
	@PreAuthorize("isAuthenticated()")
	public AlertPreferences getCaregiverAlertPreferences(SystemUser caregiver, SystemUser resident) {
		logger.info("getting caregiver alert preferences by means of getUsersForAccount");
		List<UserType> userTypes = new ArrayList<UserType>();
		userTypes.add(UserType.ROLE_CAREGIVER);
		List<SystemUser> caregivers = externalUsersDao
				.getUsersForAccount(((Patient) resident).getInstallation().getPanel(), userTypes, null);
		for (SystemUser u : caregivers) {
			// TODO: check which field we should compare with
			if (u.getNick().equals(caregiver.getNick())) {
				return u.getAlertPrefs();
			}
		}
		return new AlertPreferences();
	}

	@Override
	@PreAuthorize("isAuthenticated()")
	public ResponseStatus setCaregiverAlertPreferences(SystemUser caregiver, Patient resident, boolean master,
			boolean isAdd, Map<String, Boolean> listAlertTypes, Map<String, Boolean> commMethods) {
		logger.info("setting caregiver alert preferences by means of associateUserToAccount");
		return externalUsersDao.assignCaregiver(resident, caregiver, master, isAdd, listAlertTypes, commMethods);
	}

	@Override
	@PreAuthorize("isAuthenticated()")
	public boolean isCaregiverMaster() {
		logger.info("getting if caregiver is at least master of one caregiver");

		boolean isMaster = false;

		List<Patient> patients = externalAccountDao.getPermittedAccounts(null);

		for (Patient patient : patients) {
			if (patient.getCaregiverType() == CaregiverType.MASTER) {
				return true;
			}
		}

		return isMaster;
	}

	@Override
	public Boolean IsValidUserName(String userName, int editingUserId) {
		int res = dao.IsValidUserName(userName, editingUserId).getNumErr();
		return res == -1 ? null : (res == 1 ? true : false);
	}

	@Override
	public Boolean IsValidGeneralId(String generalId, int editingUserId) {
		int res = dao.IsValidGeneralId(generalId, editingUserId).getNumErr();
		return res == -1 ? null : (res == 1 ? true : false);
	}

	public User getLoggedinUserInfo() {
		return this.externalUsersDao.getLoggedinUserInfo();
	}

	@Override
	public ResponseStatus deleteAccount(String account) {
		return this.externalAccountDao.deleteAccount(account);
	}

	@Override
	public List<FreePanel> getFreePanels(ServiceType[] serviceTypeFilter, String [] panelTypeFilter) {
		return this.externalAccountDao.getFreePanels(serviceTypeFilter, panelTypeFilter);
	}

	@Override
	public List<Patient> getMinSystemUsersForCaregiver(String token) {

		List<Patient> pList = new ArrayList<Patient>();
		List<Patient> patients = externalAccountDao.getPermittedAccountsByToken(token, null);

		for (Patient pat : patients) {
			AccountInformation accountInformation = externalUsersDao.getAccountInformationWithToken(token,
					pat.getInstallation().getPanel().getAccount());
			for (AccountInformationUser u : accountInformation.getUsers()) {
				if (!StringUtils.hasText(u.getCareGiverType())) { // Exclude caregivers from the users in the account
					Patient p = new Patient();
					p.setUserId(u.getUserId());
					p.setFirstName(u.getFirstName() + " " + u.getLastName());
					p.setServiceType(ServiceType.fromString(Util.mapFromNewToOldServicePackageNaming(
							accountInformation.getAccountDetails().getServicePackageCode())));
					pList.add(p);
				}
			}
		}

		return pList;

	}

	@Override
	public AccountInformation getAccountInformation(String account) {

		return  externalUsersDao.getAccountInformation(account);
	}
}
