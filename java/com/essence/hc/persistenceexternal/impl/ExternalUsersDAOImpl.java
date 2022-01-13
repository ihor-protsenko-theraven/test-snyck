package com.essence.hc.persistenceexternal.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.essence.hc.eil.parsers.ExternalAPIPanelPrsr;
import com.essence.hc.eil.parsers.ExternalAPISystemUserPrsr;
import com.essence.hc.model.AccountInformation;
import com.essence.hc.model.Language;
import com.essence.hc.model.Panel;
import com.essence.hc.model.Patient;
import com.essence.hc.model.ResponseStatus;
import com.essence.hc.model.SystemUser;
import com.essence.hc.model.User;
import com.essence.hc.model.User.CaregiverType;
import com.essence.hc.model.User.UserType;
import com.essence.hc.persistence.impl.ExternalDAOImpl;
import com.essence.hc.persistenceexternal.ExternalUsersDAO;
import com.essence.hc.util.Util;

public class ExternalUsersDAOImpl extends ExternalDAOImpl implements ExternalUsersDAO {
	@Override
	public List<Language> getLanguages() {
		List<Language> languages = performRequest(List.class, "external_languages", null);
		return languages;
	}

	@Override
	public ResponseStatus assignCaregiver(Patient resident, SystemUser caregiver, Boolean master, Boolean isAdd,
			Map<String, Boolean> alertPrefs, Map<String, Boolean> commMethods) {
		HashMap<String, Object> reqParams = new HashMap<String, Object>();
		reqParams.put("panelId", resident.getInstallation().getPanel().getPanelId());
		if (caregiver.getUserId() != 0) {
			reqParams.put("userId", caregiver.getUserId());
		} else {
			reqParams.put("userName", caregiver.getNick());
		}
		if (master != null && master) {
			reqParams.put("CareGiverType", "MasterCareGiver");
		} else {
			reqParams.put("CareGiverType", "StandardCareGiver");
		}

		if (isAdd != null) {
			reqParams.put("IsAdd", isAdd);
		}

		if (alertPrefs != null)
			reqParams.put("alertPreferences", alertPrefs);
		if (commMethods != null)
			reqParams.put("communicationMethods", commMethods);

		return performRequest(ResponseStatus.class, "external_associate", reqParams);
	}

	@Override
	public ResponseStatus unassignCaregiver(Patient resident, SystemUser caregiver) {
		HashMap<String, Object> reqParams = new HashMap<String, Object>();
		reqParams.put("panelId", resident.getInstallation().getPanel().getPanelId());
		reqParams.put("userId", caregiver.getUserId());
		return performRequest(ResponseStatus.class, "external_disassociate", reqParams);
	}

	@Override
	public ResponseStatus addUser(SystemUser user) {
		HashMap<String, Object> reqParams = new HashMap<String, Object>();

		ExternalAPISystemUserPrsr userGen = new ExternalAPISystemUserPrsr();

		userGen.load(user);

		// Set date to format yyyy-mm-dd
		if (user.getBirthDate() != null) {
			String dateString = Util.formatDate(user.getBirthDate(), Util.DATEFORMAT_INPUT);
			userGen.setBirthDate(dateString);
		}

		reqParams.put("user", userGen);

		if (user instanceof Patient) {
			ExternalAPIPanelPrsr panelGen = new ExternalAPIPanelPrsr();
			Panel p = ((Patient) user).getInstallation().getPanel();
			p.setServicePackageCode(Util.mapFromOldToNewServicePackageNaming(user.getServiceType().getName()));
			panelGen.load(p);
			reqParams.put("panel", panelGen);
		}

		return performRequest(ResponseStatus.class, "external_add_user", reqParams);
		// return performDummyRequest(ResponseStatus.class, "external_add_user",
		// reqParams);
	}

	@Override
	public ResponseStatus addUserAndAssign(Patient resident, SystemUser caregiver, Boolean master,
			Map<String, Boolean> alertPrefs, Map<String, Boolean> commMethods) {

		HashMap<String, Object> reqParams = new HashMap<String, Object>();

		ExternalAPISystemUserPrsr userGen = new ExternalAPISystemUserPrsr();
		userGen.load(caregiver);

		// Set date to format yyyy-mm-dd
		if (caregiver.getBirthDate() != null) {
			String dateString = Util.formatDate(caregiver.getBirthDate(), Util.DATEFORMAT_INPUT);
			userGen.setBirthDate(dateString);
		}

		reqParams.put("user", userGen);

		reqParams.put("panelId", resident.getInstallation().getPanel().getPanelId());
		if (caregiver.getUserId() != 0) {
			reqParams.put("userId", caregiver.getUserId());
		} else {
			reqParams.put("userName", caregiver.getNick());
		}
		if (master != null && master) {
			reqParams.put("CareGiverType", "MasterCareGiver");
		} else {
			reqParams.put("CareGiverType", "StandardCareGiver");
		}

		if (alertPrefs != null)
			reqParams.put("alertPreferences", alertPrefs);
		if (commMethods != null)
			reqParams.put("communicationMethods", commMethods);

		return performRequest(ResponseStatus.class, "external_add_and_associate_user", reqParams);
		// return performDummyRequest(ResponseStatus.class,
		// "external_add_and_associate_user", reqParams);
	}

	@Override
	public ResponseStatus updateUser(SystemUser user) {
		HashMap<String, Object> reqParams = new HashMap<String, Object>();

		ExternalAPISystemUserPrsr userGen = new ExternalAPISystemUserPrsr();
		userGen.load(user);

		// Set date to format yyyy-mm-dd
		if (user.getBirthDate() != null) {
			String dateString = Util.formatDate(user.getBirthDate(), Util.DATEFORMAT_INPUT);
			userGen.setBirthDate(dateString);
		}

		reqParams.put("User", userGen);
		return performRequest(ResponseStatus.class, "external_update_user", reqParams);
	}

	@Override
	public List<SystemUser> getUsersForAccount(Panel panel, List<UserType> userTypes,
			List<CaregiverType> caregiverTypes) {
		HashMap<String, Object> reqParams = new HashMap<String, Object>();
		reqParams.put("accountIdentifier", panel.getAccount()); // TODO: remove when not needed
		reqParams.put("panelId", panel.getPanelId());
		if (userTypes != null) {
			reqParams.put("userTypesFilter", this.userTypeFilters(userTypes));
		}
		if (caregiverTypes != null) {
			reqParams.put("careGiverTypeFilter", this.caregiverTypeFilters(caregiverTypes));
		}
		return performRequest(List.class, "external_get_users_for_account", reqParams);
	}

	@Override
	public ResponseStatus deleteUser(SystemUser user) {

		HashMap<String, Object> reqParams = new HashMap<String, Object>();

		reqParams.put("userId", user.getUserId());

		if (user.getNick() != null)
			reqParams.put("userName", user.getNick());
		if (user.getEmail() != null)
			reqParams.put("email", user.getEmail());
		if (user.getGeneralId() != null)
			reqParams.put("identificationNumber", user.getGeneralId());

		return performRequest(ResponseStatus.class, "external_delete_user", reqParams);

	}

	@Override
	public User getLoggedinUserInfo() {
		return performRequest(User.class, "external_get_loggedin_user_info", null);
	}

	@Override
	public User getLoggedinUserInfoWithToken(String token) {
		HashMap<String, String> reqHeaders = new HashMap<String, String>();
		reqHeaders.put("Authorization", "Token " + token);
		return performRequest(User.class, "external_get_loggedin_user_info", null, reqHeaders);
	}

	@Override
	public AccountInformation getAccountInformation(String accountNumber) {
		HashMap<String, Object> reqParams = new HashMap<String, Object>();
		reqParams.put("account", accountNumber);
		return performRequest(AccountInformation.class, "external_get_account_information", reqParams);
	}
	
	public AccountInformation getAccountInformationWithToken(String token, String accountNumber) {
		HashMap<String, String> reqHeaders = new HashMap<String, String>();
		reqHeaders.put("Authorization", "Token " + token);
		HashMap<String, Object> reqParams = new HashMap<String, Object>();
		reqParams.put("account", accountNumber);
		return performRequest(AccountInformation.class, "external_get_account_information", reqParams, reqHeaders);
	}

}
