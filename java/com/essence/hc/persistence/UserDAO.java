/**
 * 
 */
package com.essence.hc.persistence;

import java.util.List;
import java.util.Map;

import com.essence.hc.model.Alert;
import com.essence.hc.model.AlertPreferences;
import com.essence.hc.model.HCCommand;
import com.essence.hc.model.Language;
import com.essence.hc.model.Patient;
import com.essence.hc.model.ResponseStatus;
import com.essence.hc.model.SystemStatus.StatusTypes;
import com.essence.hc.model.Vendor.ServiceType;
import com.essence.hc.model.SystemUser;

/**
 * @author oscar.canalejo
 *
 */
public interface UserDAO {
	
	@Deprecated
	public List<Patient> getPatients(String userId);
	
	public List<Alert> getAlerts(String userId);
	
	public List<String> saveAlert(String userId, Alert alert);
	
	public Map<String, HCCommand> getCommands(String userId, StatusTypes systemStatus);
	
	public ResponseStatus addUser(SystemUser user);

	public List<SystemUser> getSystemUsers(String iUserId, String iUserRol, String iVendorId, int iNumOfItems, String iPageNum, 
			String sRoleFltr, String sSearchCriteria, String sCriteriaValue, String orderBy, Boolean sortAsc, 
			String[] panelTypeFilter, ServiceType[] serviceTypeFilter, Boolean enableActiveService);
	
	@Deprecated
	public ResponseStatus deleteUser(String userId);
	
	public ResponseStatus updateUser(SystemUser user);

	public SystemUser getUser(String userId);
		
	public ResponseStatus AlertConfigSave(String iUsrId1, String iUsrId2, Map<String, Boolean> alerts);

	public String getVersion();
	
	public List<String> getPredefinedTextByCommandId (String commandId);
	
	public List<Language> getLanguages();
	
	public ResponseStatus setUserPreferences(int userId, int vendorId, int languageId);
	
	public AlertPreferences getAlertPreferences(int userId, int vendorId);
	
	@Deprecated
	public ResponseStatus setAlertPreferences(int userId, int vendorId, int relatedUserId,Map<String, Boolean> alertPrefs);
	
	public ResponseStatus setAdminPreferences(int userId, Map<String, Boolean> alertPrefs);
	
	public ResponseStatus IsValidUserName(String userName,int editingUserId);
		
	public ResponseStatus IsValidGeneralId(String generalId,int editingUserId);
		
}
