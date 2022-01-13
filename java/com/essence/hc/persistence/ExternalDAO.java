package com.essence.hc.persistence;

import java.util.Map;
import java.util.List;

import com.essence.hc.model.AlertPreferences;
import com.essence.hc.model.Language;
import com.essence.hc.model.Panel;
import com.essence.hc.model.Patient;
import com.essence.hc.model.ResponseStatus;
import com.essence.hc.model.SystemUser;
import com.essence.hc.model.User.CaregiverType;
import com.essence.hc.model.User.UserType;
import com.essence.hc.model.Vendor.ServiceType;

public interface ExternalDAO {

	/**
	 * logins into the family installation API and returns the session token
	 * @param username
	 * @param password
	 * @return token
	 */
	//public String login(String username, String password);
	
	/**
	 * logouts from the family installation API, taking the token from the user in the security context
	 * @return logout completed
	 */
	//public boolean logout();
	
	/**
	 * logouts from the family installation API, getting the token as parameter
	 * @param token
	 * @return logout completed
	 */
	//public boolean logout(String token);
	
	/**
	 * returns the list of languages available
	 * @return languages list
	 */
	//public List<Language> getLanguages();
	
	/**
	 * Assign caregiver to a resident  
	 * @param resident
	 * @param caregiver
	 * @param master is assigned as master?
	 * @param alertPrefs alert preferences
	 * @param commMethods communication methods
	 * @return ResponseStatus
	 */
	//public ResponseStatus assignCaregiver(Patient resident, SystemUser caregiver, Boolean master, Boolean isAdd, Map<String, Boolean> alertPrefs, Map<String, Boolean> commMethods);
	
	/**
	 * Unassign caregiver from a resident  
	 * @param resident
	 * @param caregiver
	 * @return ResponseStatus
	 */
	//public ResponseStatus unassignCaregiver(Patient resident, SystemUser caregiver);
	
	/**
	 * returns a map with the vendor configuration settings
	 * @return
	 */
	//public Map<ServiceType, Map<CaregiverType, Integer>> getVendorConfigurationSettings();
	
	/**
	 * Adds a new user
	 * @param user
	 * @return
	 */
	//public ResponseStatus addUser(SystemUser user);
	
	/**
	 * Create a caregiver and assign to a resident  
	 * @param resident
	 * @param caregiver
	 * @param master is assigned as master?
	 * @param alertPrefs alert preferences
	 * @param commMethods communication methods
	 * @return ResponseStatus
	 */
	//public ResponseStatus addUserAndAssign(Patient resident, SystemUser caregiver, Boolean master, Map<String, Boolean> alertPrefs, Map<String, Boolean> commMethods);
	
	/**
	 * Updates the information for a user
	 * @param user
	 * @return
	 */
	//public ResponseStatus updateUser(SystemUser user);
	
	/**
	 * Updates the information for an account
	 * @param patient
	 * @return
	 */
	//public ResponseStatus updateAccount(Patient patient);
	
	/**
	 * returns the list of users related to an account
	 * @param patient
	 * @param userTypes
	 * @param caregiverTypes
	 * @return
	 */
	//public List<SystemUser> getUsersForAccount(Panel panel, List<UserType> userTypes, List<CaregiverType> caregiverTypes);
	
	/**
	 * returns the list of residents for the logged in user
	 * @param master include residents assigned as master
	 * @param standard include residents assigned as standard
	 * @return list of residents
	 */
	//public List<Patient> getPermittedAccounts(List<CaregiverType> caregiverTypes);
	
	/**
	 * returns account rules
	 * @param panel
	 * @return
	 */
	//public Object getDevices(String panelId);
	
	/**
	 * returns rules policy
	 * @return
	 */
	//public Object getRulesPolicy();
	
	/**
	 * returns account rules
	 * @param panel
	 * @return
	 */
	//public Object getAccountRules(String panelId);
	
	/**
	 * Sets account rules by sending JSON data in the body of the request
	 * @param body JSON body for the request
	 * @return
	 */
	//public ResponseStatus setAccountRulesRawBody(Object body);
	
	/**
	 * Creates a manual alert
	 * @param iUserId user id
	 * @param panelId panel id 
	 * @param iAlrtType alert type 
	 * @param sAlertTxt alert description
	 * @param deviceId id of the related device (if any)
	 * @return
	 */
	//public ResponseStatus createManualAlert (Patient resident, String iAlrtType, String sAlertTxt, String deviceId);
		
}
