package com.essence.hc.service;

import java.util.List;
import java.util.Map;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import com.essence.hc.model.AccountInformation;
import com.essence.hc.model.Alert;
import com.essence.hc.model.AlertPreferences;
import com.essence.hc.model.FreePanel;
import com.essence.hc.model.HCCommand;
import com.essence.hc.model.Language;
import com.essence.hc.model.Patient;
import com.essence.hc.model.ResponseStatus;
import com.essence.hc.model.SystemStatus.StatusTypes;
import com.essence.hc.model.SystemUser;
import com.essence.hc.model.ThirdPartyUser;
import com.essence.hc.model.User;
import com.essence.hc.model.User.UserType;
import com.essence.hc.model.Vendor;
import com.essence.hc.model.Vendor.ServiceType;

@Service
public interface UserService {

	public int NewUser(String login, String password, String name);

	/**
	 * User Auth against the security authority
	 */
	public User GetUserWithSecurity(String login, String password, String passwordSHA, String Country, String Service , Boolean s);
	
	/**
	 * Get user with already defined token
	 */
	public User getUserWithToken(String token);
	

	/**
	 * User logging against third party system
	 * @return third party user
	 */
	public ThirdPartyUser getThirdPartyUserWithToken(String token, String externalPartyAuthenticationProvider);

	/**
	 * Logout user from the application
	 * @param user
	 */
	public void logout(User user);
	
	@Deprecated
	@PreAuthorize("isAuthenticated()")
	public User prepareSession();
	
	@Deprecated
	@PreAuthorize("isAuthenticated()")
	public List<Patient> getPatients(String userId);

	@PreAuthorize("isAuthenticated()")
	public List<Patient> getPatients(User user);

	@PreAuthorize("isAuthenticated()")
	public void setCurrentPatient(User user, Patient newCurPatient);
	
	@Deprecated
	@PreAuthorize("isAuthenticated()")
	public List<Alert> getAlerts(String userId);
	
	@Deprecated
	//@PreAuthorize("isAuthenticated() and hasPermission('alert', 16) and principal.id == #userId")
	@PreAuthorize("isAuthenticated()")
	public List<Alert> getOpenAlerts(String userId, boolean includeRecentlyClosed);
	
	@PreAuthorize("isAuthenticated()")
	public Map<String, HCCommand> getCommands(String userId, StatusTypes systemStatus);
	
	@PreAuthorize("isAuthenticated()")
	public ResponseStatus deleteUserById(String userId);

	@PreAuthorize("isAuthenticated()")
	public ResponseStatus addUser(SystemUser user);
	
	@PreAuthorize("isAuthenticated()")
	public SystemUser getUser(String userId);
	
	@PreAuthorize("isAuthenticated()")
	public ResponseStatus updateUser(SystemUser user);
	
	@PreAuthorize("isAuthenticated()")
	public ResponseStatus updatePatient(String loggedUser, SystemUser CurrentUser, SystemUser patient, Map<String, Boolean> alerts);
	
	@PreAuthorize("isAuthenticated()")
	public ResponseStatus deleteUser(SystemUser user);
	
	@PreAuthorize("isAuthenticated()")
	public List<SystemUser> getSystemUsers(String iUserId, String iUserRol, String iVendorId, int iNumOfItems, String iPageNum, 
			String sRoleFltr, String sSearchCriteria, String sCriteriaValue, String orderBy, Boolean sortAsc, 
			String[] panelTypeFilter, ServiceType[] serviceTypeFilter, Boolean enableActiveService);
	
	@PreAuthorize("isAuthenticated()")
	public List<SystemUser> getSystemUsersForCaregiver(String sSearchCriteria, String sCriteriaValue, 
			String orderBy, Boolean sortAsc, UserType[] userTypes, ServiceType[] serviceTypes, Boolean enableActiveService);
	
	@PreAuthorize("isAuthenticated()")
	public ResponseStatus alertConfigSave(String iUsrId1, String iUsrId2, Map<String, Boolean> alerts);
	
	public String getVersion();
	
	@PreAuthorize("isAuthenticated()")
	public List<String> getPredefinedTextByCommandId (String commandId);
	
	public List<Language> getLanguages();
	
	public void loadVendorConfigurationSettings(Vendor vendor);
	
	public void loadVendorTimeZones(Vendor vendor, int languageId);
	
	@PreAuthorize("isAuthenticated()")
	public ResponseStatus setUserPreferences(int userId, int vendorId, int languageId);
	
	@PreAuthorize("isAuthenticated()")
	public AlertPreferences getAlertPreferences(int userId, int vendorId);
	
	@PreAuthorize("isAuthenticated()")
	public ResponseStatus setAlertPreferences(int userId, int vendorId, int relatedUserId,Map<String, Boolean> listAlertTypes);
	
	@PreAuthorize("isAuthenticated()")
	public AlertPreferences getCaregiverAlertPreferences(SystemUser caregiver, SystemUser resident);
	
	@PreAuthorize("isAuthenticated()")
	public ResponseStatus setCaregiverAlertPreferences(SystemUser caregiver, Patient resident, boolean master, boolean isAdd, Map<String, Boolean> listAlertTypes, Map<String, Boolean> commMethods);

	@PreAuthorize("isAuthenticated()")
	public Boolean IsValidUserName(String userName, int editingUserId);
	
	@PreAuthorize("isAuthenticated()")
	public boolean isCaregiverMaster();
	
	@PreAuthorize("isAuthenticated()")
	public Boolean IsValidGeneralId(String generalId, int editingUserId);
	
	public User getLoggedinUserInfo();
	
	@PreAuthorize("isAuthenticated()")
	public ResponseStatus deleteAccount(String account);
	
//	@PreAuthorize("isAuthenticated()")
	public List<FreePanel> getFreePanels(ServiceType[] serviceTypeFilter, String [] panelTypeFilter);

	public List<Patient> getMinSystemUsersForCaregiver(String token);
	
	public AccountInformation getAccountInformation(String account);
}
