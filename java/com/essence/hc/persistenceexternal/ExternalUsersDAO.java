package com.essence.hc.persistenceexternal;

import java.util.List;
import java.util.Map;

import com.essence.hc.model.AccountInformation;
import com.essence.hc.model.Language;
import com.essence.hc.model.Panel;
import com.essence.hc.model.Patient;
import com.essence.hc.model.ResponseStatus;
import com.essence.hc.model.SystemUser;
import com.essence.hc.model.User;
import com.essence.hc.model.User.CaregiverType;
import com.essence.hc.model.User.UserType;

public interface ExternalUsersDAO 
{
	 /**
	  * returns the list of languages available
	  * @return languages list
	  */
	 public List<Language> getLanguages();
	 
	 /**
		 * Assign caregiver to a resident  
		 * @param resident
		 * @param caregiver
		 * @param master is assigned as master?
		 * @param alertPrefs alert preferences
		 * @param commMethods communication methods
		 * @return ResponseStatus
		 */
	  public ResponseStatus assignCaregiver(Patient resident, SystemUser caregiver, Boolean master, Boolean isAdd, Map<String, Boolean> alertPrefs, Map<String, Boolean> commMethods);
	  
	   /**
		 * Unassign caregiver from a resident  
		 * @param resident
		 * @param caregiver
		 * @return ResponseStatus
		 */
	   public ResponseStatus unassignCaregiver(Patient resident, SystemUser caregiver);
	   
	   /**
		 * Adds a new user
		 * @param user
		 * @return
		 */
	    public ResponseStatus addUser(SystemUser user);
	    
	    /**
		 * Create a caregiver and assign to a resident  
		 * @param resident
		 * @param caregiver
		 * @param master is assigned as master?
		 * @param alertPrefs alert preferences
		 * @param commMethods communication methods
		 * @return ResponseStatus
		 */
		public ResponseStatus addUserAndAssign(Patient resident, SystemUser caregiver, Boolean master, Map<String, Boolean> alertPrefs, Map<String, Boolean> commMethods);
		
		/**
		 * Updates the information for a user
		 * @param user
		 * @return
		 */
		 public ResponseStatus updateUser(SystemUser user);
		 
		 public ResponseStatus deleteUser(SystemUser user);
		 
		 /**
		 * returns the list of users related to an account
		 * @param patient
		 * @param userTypes
		 * @param caregiverTypes
		 * @return
		 */
		 public List<SystemUser> getUsersForAccount(Panel panel, List<UserType> userTypes, List<CaregiverType> caregiverTypes);
		 
		 public User getLoggedinUserInfo();
		 
		 public User getLoggedinUserInfoWithToken(String token);
		 
		 public AccountInformation getAccountInformation(String accountNumber);
		 public AccountInformation getAccountInformationWithToken(String token, String accountNumber);
}
