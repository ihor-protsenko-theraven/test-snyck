package com.essence.hc.persistenceexternal.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.essence.hc.model.ExternalAPIInstallationDevices;
import com.essence.hc.model.FreePanel;
import com.essence.hc.model.Panel;
import com.essence.hc.model.Panel.PanelFeature;
import com.essence.hc.model.Patient;
import com.essence.hc.model.ResponseStatus;
import com.essence.hc.model.SystemUser;
import com.essence.hc.model.User.CaregiverType;
import com.essence.hc.model.Vendor.ServiceType;
import com.essence.hc.persistence.impl.ExternalDAOImpl;
import com.essence.hc.persistenceexternal.ExternalAccountDAO;
import com.essence.hc.util.Util;

public class ExternalAccountDAOImpl extends ExternalDAOImpl implements ExternalAccountDAO {
	@Override
	public ResponseStatus updateAccount(Patient patient, SystemUser currentUser) {
		HashMap<String, Object> reqParams = new HashMap<String, Object>();
		Panel panel = patient.getInstallation().getPanel();
		Map<String, Boolean> supportedFeatures = new HashMap<String, Boolean>();

		reqParams.put("accountNumber", panel.getAccount());
		// reqParams.put("accountIdentifier", panel.getAccount());
		reqParams.put("name", panel.getName());
		reqParams.put("address", patient.getAddress());
		reqParams.put("homePhone", patient.getPhone());
		reqParams.put("hasPets", panel.hasPets());
		reqParams.put("installationNotes", panel.getInstallNotes());
		reqParams.put("simNumber", panel.getSimNumber());
		reqParams.put("landlineNumber", panel.getLandlineNumber());
		reqParams.put("dtmfCode", Integer.parseInt(panel.getiDTMFCode()));
		reqParams.put("panelSerialNumber", panel.getSerialNumber());
		reqParams.put("serviceProviderAccountNumber", patient.getAccountNumber());
		reqParams.put("serviceProviderSerialNumber", panel.getCustomerSerialNumber());
		// reqParams.put("panelId", panel.getPanelId());
		reqParams.put("panelId", Integer.parseInt(panel.getPanelId()));
		reqParams.put("timeZone", panel.getTimeZone());

		// EIC15-2105 -> Not needed to send settings and supported features (ONLY if the
		// user is a CareGiver)
		if (currentUser.isAdmin()) {
			Map<String, Map> settings = new HashMap<String, Map>();
			reqParams.put("settings", settings);

			Map<String, String[]> activeEmergencyCall = new HashMap<String, String[]>();
			settings.put("activeEmergencyCall", activeEmergencyCall);

			String[] rolesAllowed = panel.getPanelSettings().getActiveEmergencyRolesAllowed();
			activeEmergencyCall.put("allowChangesBy", rolesAllowed);

			supportedFeatures.put(PanelFeature.ACTIVE_SERVICE.getFeatureKey(), panel.isEnableActiveService());
			reqParams.put("supportedFeatures", supportedFeatures);
		}

		return performRequest(ResponseStatus.class, "external_update_account", reqParams);

	}

	@Override
	public List<Patient> getPermittedAccounts(List<CaregiverType> caregiverTypes) {
		HashMap<String, Object> reqParams = new HashMap<String, Object>();
		if (caregiverTypes != null) {
			reqParams.put("careGiverTypeFilter", this.caregiverTypeFilters(caregiverTypes));
		}

		if (Util.isDebug()) {
			return performDummyRequest(List.class, "external_permitted_accounts", reqParams);
		}
		return performRequest(List.class, "external_permitted_accounts", reqParams);
	}

	/**
	 * To be used in authentication process where the token is not set yet in the
	 * user
	 */
	@Override
	public List<Patient> getPermittedAccountsByToken(String token, List<CaregiverType> caregiverTypes) {
		HashMap<String, Object> reqParams = new HashMap<String, Object>();

		if (caregiverTypes != null) {
			reqParams.put("careGiverTypeFilter", this.caregiverTypeFilters(caregiverTypes));
		}

		HashMap<String, String> reqHeaders = new HashMap<String, String>();
		reqHeaders.put("Authorization", "Token " + token);

		return performRequest(List.class, "external_permitted_accounts", reqParams, reqHeaders);
	}

	@Override
	public Object getDevices(String account) {
		HashMap<String, Object> reqParams = new HashMap<String, Object>();
		reqParams.put("accountIdentifier", account);
		return performUnparsedRequest("external_get_devices", reqParams);
	}

	@Override
	public ExternalAPIInstallationDevices getExternalAPIInstallationDevices(String account) {
		HashMap<String, Object> reqParams = new HashMap<String, Object>();
		reqParams.put("accountIdentifier", account);

		return performRequest(ExternalAPIInstallationDevices.class, "external_get_devices", reqParams);
		// return performDummyRequest(ExternalAPIInstallationDevices.class,
		// "external_get_devices", reqParams);

	}

	@Override
	public ResponseStatus deleteAccount(String account) {
		HashMap<String, Object> reqParams = new HashMap<String, Object>();
		reqParams.put("account", account);
		return performRequest(ResponseStatus.class, "external_delete_account", reqParams);
	}

	@Override
	public List<FreePanel> getFreePanels(ServiceType[] serviceTypeFilter, String[] panelTypeFilter) {

		String[] servicePackagesFilterList = null;
		if (serviceTypeFilter != null && serviceTypeFilter.length > 0) {
			servicePackagesFilterList = new String[serviceTypeFilter.length];
			for (int idx = 0; idx < serviceTypeFilter.length; idx++) {
				servicePackagesFilterList[idx] = (serviceTypeFilter[idx].equals(ServiceType.PERSE)) ? "Pers-E"
						: Util.mapFromOldToNewServicePackageNaming(serviceTypeFilter[idx].getName());
			}
		}

		HashMap<String, Object> reqParams = new HashMap<String, Object>();
		if (servicePackagesFilterList != null) {
			reqParams.put("servicePackagesFilterList", servicePackagesFilterList);
		}
		if (panelTypeFilter != null) {
			reqParams.put("panelTypeFilter", panelTypeFilter);
		}

		if (Util.isDebug()) {
			return performDummyRequest(List.class, "external_get_free_panels", reqParams);
		}
		List<FreePanel> freePanels = performRequest(List.class, "external_get_free_panels", reqParams);

		if (servicePackagesFilterList != null && servicePackagesFilterList.length == 1) {
			// We can be sure that this free panel is potentially assigned to the given
			// servicePackage
			for (FreePanel fp : freePanels) {
				fp.setPotentialServiceType(
						ServiceType.fromString(Util.mapFromNewToOldServicePackageNaming(servicePackagesFilterList[0])));
			}
		}

		return freePanels;
	}
}
