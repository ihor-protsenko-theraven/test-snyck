package com.essence.hc.persistenceexternal;

import java.util.List;

import com.essence.hc.model.ExternalAPIInstallationDevices;
import com.essence.hc.model.FreePanel;
import com.essence.hc.model.Patient;
import com.essence.hc.model.ResponseStatus;
import com.essence.hc.model.SystemUser;
import com.essence.hc.model.User.CaregiverType;
import com.essence.hc.model.Vendor.ServiceType;

public interface ExternalAccountDAO {
	/**
	 * Updates the information for an account
	 * 
	 * @param patient
	 * @return
	 */
	public ResponseStatus updateAccount(Patient patient, SystemUser currentUser);

	/**
	 * returns the list of residents for the logged in user
	 * 
	 * @param master
	 *            include residents assigned as master
	 * @param standard
	 *            include residents assigned as standard
	 * @return list of residents
	 */
	public List<Patient> getPermittedAccounts(List<CaregiverType> caregiverTypes);

	public List<Patient> getPermittedAccountsByToken(String token, List<CaregiverType> caregiverTypes);

	/**
	 * returns account rules
	 * 
	 * @param panel
	 * @return
	 */
	public Object getDevices(String account);

	public ExternalAPIInstallationDevices getExternalAPIInstallationDevices(String account);

	public ResponseStatus deleteAccount(String account);

	/**
	 * 
	 * @param servicePackagesFilterList
	 *            - a collecion with "Family", "Pro", "Pers-E"
	 * @return
	 */
	public List<FreePanel> getFreePanels(ServiceType[] serviceTypeFilter, String[] panelTypeFilter);
}
