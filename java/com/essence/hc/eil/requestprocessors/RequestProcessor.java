package com.essence.hc.eil.requestprocessors;

import java.util.HashMap;

import com.essence.hc.eil.parsers.AccountInformationPrsr;
import com.essence.hc.eil.parsers.ActivityLevelPrsr;
import com.essence.hc.eil.parsers.ActivityReportListPrsr;
import com.essence.hc.eil.parsers.AlertConfigPrsr;
import com.essence.hc.eil.parsers.AlertPreferencesPrsr;
import com.essence.hc.eil.parsers.AlertPrsr;
import com.essence.hc.eil.parsers.CommandListPrsr;
import com.essence.hc.eil.parsers.DayStoryPrsr;
import com.essence.hc.eil.parsers.DevicePrsr;
import com.essence.hc.eil.parsers.EventLogPrsr;
import com.essence.hc.eil.parsers.ExternalAPIGetMonitoringResponsePrsr;
import com.essence.hc.eil.parsers.ExternalAPIListDevicesPrsr;
import com.essence.hc.eil.parsers.ExternalAPIListFreePanelPrsr;
import com.essence.hc.eil.parsers.ExternalAPIListSystemUserPrsr;
import com.essence.hc.eil.parsers.ExternalAPILoggedInSystemUserPrsr;
import com.essence.hc.eil.parsers.ExternalAPILoginPrsr;
import com.essence.hc.eil.parsers.ExternalAPIPartyLoginPrsr;
import com.essence.hc.eil.parsers.ExternalAPIResponseStatusPrsr;
import com.essence.hc.eil.parsers.ExternalAPIStepCountReportPrsr;
import com.essence.hc.eil.parsers.ExternalAPITimeZonesPrsr;
import com.essence.hc.eil.parsers.HistoryPrsr;
import com.essence.hc.eil.parsers.ListActivityIndexPrsr;
import com.essence.hc.eil.parsers.ListAlertCloseReasonPrsr;
import com.essence.hc.eil.parsers.ListCamerasPrsr;
import com.essence.hc.eil.parsers.ListDayStoryExpressPrsr;
import com.essence.hc.eil.parsers.ListDevicesPrsr;
import com.essence.hc.eil.parsers.ListLanguagesPrsr;
import com.essence.hc.eil.parsers.ListMessageStatusPrsr;
import com.essence.hc.eil.parsers.ListPanelsPrsr;
import com.essence.hc.eil.parsers.ListSystemUsersPrsr;
import com.essence.hc.eil.parsers.LowActivityPrsr;
import com.essence.hc.eil.parsers.MonitoringPrsr;
import com.essence.hc.eil.parsers.PasswordPolicyPrsr;
import com.essence.hc.eil.parsers.PermittedAccountsPrsr;
import com.essence.hc.eil.parsers.PredefinedTxtPrsr;
import com.essence.hc.eil.parsers.ReportPrsr;
import com.essence.hc.eil.parsers.ResponseStatusPrsr;
import com.essence.hc.eil.parsers.SystemStatusPrsr;
import com.essence.hc.eil.parsers.SystemUserPrsr;
import com.essence.hc.eil.parsers.TotalAlertCountPrsr;
import com.essence.hc.eil.parsers.VendorConfigurationSettingsPrsr;

/**
 * Defines the methods needed to provide an integration tier with any external
 * system which performs our application requests
 *
 * (Somehow, a sort of Data Source)
 * 
 * @author oscar-canalejo
 *
 */
public interface RequestProcessor {

	/**
	 * TODO: Put all the following constants in a more suitable place, like a XML
	 * resource file
	 */
	public static final HashMap<String, String> API_URIS = new HashMap<String, String>() {
		{
			put("activity_level", "/api/Patient/GetActvtStatus");
			put("day_story", "/api/Patient/GetDayStory");
			put("history", "/api/Alert/GetAlertHistory");
			put("alert", "/api/Alert/GetAlert");
			put("system_status", "/api/Alert/GetSystemStatus");
			put("close", "/api/Cmd/msgSave");
			put("event_log", "/api/Alert/GetSessionLog");
			put("session_alerts", "/api/Alert/GetSessionAlerts");
			put("patient_info", "/api/admin/GetAnalysis");
			put("patient_in_alarm", "/api/admin/GetMonitoring");
			put("patient_report", "/api/admin/GetWeeklyReport");
			put("alert_count", "/api/alert/GetAlertCount");
			put("list_devices", "/api/admin/GetDeviceListByUserID");
			put("get_device", "/api/admin/GetDeviceByID"); // unused TODO deprecate
			put("add_device", "/api/admin/DeviceAddByUserID");
			put("update_device", "/api/admin/DeviceUpdateByUserID");
			put("delete_device", "/api/admin/DeviceDeleteByUserID");
			put("free_accounts", "/api/admin/GetFreeAccounts");
			put("manual_alert", "/api/admin/setManualAlert");
			put("alert_types", "/api/admin/GetManualAlertTypes");
			put("system_users", "/api/admin/GetUsers");
			put("add_user", "/api/admin/AddUser");
			put("delete_user", "/api/admin/Deleteuser");
			put("get_user", "/api/admin/GetUserInfo");
			put("update_user", "/api/admin/UpdateUser");
			put("caregiver_patients", "/api/admin/GetCareGiverPatients");
			put("patient_caregivers", "/api/admin/GetPatientCareGivers");
			put("assign_caregiver", "/api/admin/UserAssignCmd");
			put("unassign_caregiver", "/api/admin/UserAssignCmd");
			put("alert_configuration", "/api/admin/GetUserAlertConfig");
			put("save_config_alerts", "/api/admin/AlertConfigSave");
			put("camera_list", "/api/user/GetCameraList");
			put("photos_list", "/api/user/GetPhotos");
			put("user_commands", "/api/Admin/GetCommandList");
			put("message_commands", "/api/Admin/GetPredefinedTextByCommand");
			put("request_photo", "/api/user/RequestPhoto");
			put("create_manual_alert", "/api/alert/CreateManualAlert");
			put("broadcast_message", "/api/Cmd/msgSave");
			put("get_police_telephone", "/api/user/GetPoliceNumberAndLogCall");
			put("get_panel_telephone", "/api/user/GetPanelNumberAndLogCall");
			put("get_version", "/api/admin/GetVersion");
			put("update_alert_state", "/api/alert/UpdateAlertState");
			put("alert_close_reasons", "/api/alert/GetAlertCloseReason");
			put("low_activity", "/api/analytic/GetRecordAlertInfo");
			put("list_languages", "/api/user/GetLanguages");
			put("set_preferences", "/api/admin/setUserPreferences");
			put("set_AlertPreferences", "/api/admin/SetAlertPreferences");
			put("get_AlertPreferences", "/api/admin/GetAlertPreferences");
			put("set_AdminPreferences", "/api/admin/SetAdminPatientPrefs");
			put("IsValidUserName", "/api/admin/IsValidUserName");
			put("IsValidGeneralId", "/api/admin/IsValidGeneralId");
			put("day_story_express", "/api/reports/DayStoryReport");
			put("monthly_report", "/api/reports/MonthlyReport");
			put("activity_index_report_old", "/api/reports/ActivityIndexReport");
			put("activity_index_report", "/api/reports/ActivityIndexReport");

			// -------------> Family Installation API methods <-------------

			// ------------- login -------------
			put("external_login", "/api/login/Login");
			put("external_logout", "/api/login/Logout");
			put("external_party_login", "/api/login/ExternalPartyLogin");

			// ------------- users -------------
			put("external_languages", "/api/users/getLanguages");
			put("external_associate", "/api/users/associateUserToAccount");
			put("external_disassociate", "/api/users/disassociateUserFromAccount");
			put("external_add_user", "/api/users/AddUser");
			put("external_add_and_associate_user", "/api/users/AddAndAssociateUser");
			put("external_update_user", "/api/users/UpdateUser");
			put("external_get_users_for_account", "/api/users/GetUsersForAccount");
			put("external_get_loggedin_user_info", "/api/users/GetLoggedInUserInfo");
			put("external_delete_user", "/api/users/DeleteUser");

			// ------------- Devices -------------
			put("external_update_device", "/api/devices/UpdateDevice");
			put("external_get_devices", "/api/Account/GetDevices");
			put("external_remove_device_from_account", "/api/devices/RemoveDeviceFromAccount");

			// ------------- Account -------------
			put("external_update_account", "/api/Account/UpdateAccountInformation");
			put("external_permitted_accounts", "/api/Account/GetPermittedAccounts");
			put("external_delete_account", "/api/Account/DeleteAccount");
			put("external_get_free_panels", "/api/Account/GetFreePanels");
			put("external_get_account_information", "/api/account/GetAccountInformation");

			// ------------- Configuration -------------
			put("external_vendor_config", "/api/Configuration/GetVendorConfigurationSettings");
			put("external_get_time_zones", "/api/Configuration/GetTimeZones");

			// ------------- Rules -------------
			put("external_get_rules_policy", "/api/Rules/GetRulesPolicy");
			put("external_get_account_rules", "/api/Rules/GetAccountRules");
			put("external_set_account_rules", "/api/Rules/SetAccountRules");

			// ------------- Alert -------------
			put("external_create_manual_alert", "/api/Alerts/CreateManualAlert");
			put("external_close_events", "/api/Alerts/CloseEvents");

			// ------------- Reports -------------
			put("external_get_step_count", "/api/report/GetStepCount");

			// ------------- Password Policy -------------
			put("external_reset_password", "/api/users/ResetPassword");
			put("external_password_policy", "/api/Configuration/GetPasswordPolicy");
			put("external_change_expired_password", "/api/users/ChangeExpiredPassword");
			put("external_change_password", "/api/users/ChangePassword");
			put("external_change_user_password", "/api/users/ChangeUserPassword");
			put("external_accept_eula", "/api/users/acceptEula");

			// ------------- Monitoring -------------
			put("external_get_monitoring", "/api/monitoring/GetMonitoring");
		}
	};
	public static final HashMap<String, Class<?>> API_PARSERS = new HashMap<String, Class<?>>() {
		{
			put("activity_level", ActivityLevelPrsr.class);
			put("day_story", DayStoryPrsr.class);
			put("history", HistoryPrsr.class);
			put("system_status", SystemStatusPrsr.class);
			put("close", ResponseStatusPrsr.class);
			put("event_log", EventLogPrsr.class);
			put("alert", AlertPrsr.class);
			put("session_alerts", HistoryPrsr.class);
			put("patient_info", MonitoringPrsr.class);
			put("patient_in_alarm", MonitoringPrsr.class);
			put("patient_report", ReportPrsr.class);
			put("alert_count", TotalAlertCountPrsr.class);
			put("list_devices", ListDevicesPrsr.class);
			put("get_device", DevicePrsr.class);
			put("add_device", ResponseStatusPrsr.class);
			put("update_device", ResponseStatusPrsr.class);
			put("delete_device", ResponseStatusPrsr.class);
			put("free_accounts", ListPanelsPrsr.class);
			put("manual_alert", ResponseStatusPrsr.class);
			put("alert_types", ListMessageStatusPrsr.class);
			put("system_users", ListSystemUsersPrsr.class);
			put("add_user", ResponseStatusPrsr.class);
			put("delete_user", ResponseStatusPrsr.class);
			put("get_user", SystemUserPrsr.class);
			put("update_user", ResponseStatusPrsr.class);
			put("caregiver_patients", MonitoringPrsr.class);
			put("patient_caregivers", ListSystemUsersPrsr.class);
			put("assign_caregiver", ResponseStatusPrsr.class);// Review
			put("unassign_caregiver", ResponseStatusPrsr.class);// ReviewAlertConfigPrsr
			put("alert_configuration", AlertConfigPrsr.class);
			put("save_config_alerts", ResponseStatusPrsr.class);
			put("camera_list", ListCamerasPrsr.class);
			put("request_photo", ResponseStatusPrsr.class);
			put("create_manual_alert", ResponseStatusPrsr.class);
			put("broadcast_message", ResponseStatusPrsr.class);
			put("get_version", ResponseStatusPrsr.class);
			put("get_police_telephone", ResponseStatusPrsr.class);
			put("get_panel_telephone", ResponseStatusPrsr.class);
			put("user_commands", CommandListPrsr.class);
			put("message_commands", PredefinedTxtPrsr.class);
			put("update_alert_state", ResponseStatusPrsr.class);
			put("alert_close_reasons", ListAlertCloseReasonPrsr.class);
			put("low_activity", LowActivityPrsr.class);
			put("list_languages", ListLanguagesPrsr.class);
			put("set_preferences", ResponseStatusPrsr.class);
			put("set_AlertPreferences", ResponseStatusPrsr.class);
			put("get_AlertPreferences", AlertPreferencesPrsr.class);
			put("set_AdminPreferences", ResponseStatusPrsr.class);
			put("IsValidUserName", ResponseStatusPrsr.class);
			put("IsValidGeneralId", ResponseStatusPrsr.class);
			put("day_story_express", ListDayStoryExpressPrsr.class);
			put("activity_index_report_old", ListActivityIndexPrsr.class);
			put("activity_index_report", ActivityReportListPrsr.class);
			put("monthly_report", ListActivityIndexPrsr.class);

			// -------------> Family Installation API methods <-------------

			// ------------- login -------------
			put("external_login", ExternalAPILoginPrsr.class);
			put("external_logout", ExternalAPIResponseStatusPrsr.class);
			put("external_party_login", ExternalAPIPartyLoginPrsr.class);

			// ------------- users -------------
			put("external_languages", ListLanguagesPrsr.class);
			put("external_associate", ExternalAPIResponseStatusPrsr.class);
			put("external_disassociate", ExternalAPIResponseStatusPrsr.class);
			put("external_add_user", ExternalAPIResponseStatusPrsr.class);
			put("external_add_and_associate_user", ExternalAPIResponseStatusPrsr.class);
			put("external_update_user", ExternalAPIResponseStatusPrsr.class);
			put("external_get_users_for_account", ExternalAPIListSystemUserPrsr.class);
			put("external_get_loggedin_user_info", ExternalAPILoggedInSystemUserPrsr.class);
			put("external_delete_user", ExternalAPIResponseStatusPrsr.class);
			
			// ------------- Account -------------
			put("external_update_account", ExternalAPIResponseStatusPrsr.class);
			put("external_permitted_accounts", PermittedAccountsPrsr.class);
			put("external_delete_account", ExternalAPIResponseStatusPrsr.class);
			put("external_get_free_panels", ExternalAPIListFreePanelPrsr.class);
			put("external_get_account_information", AccountInformationPrsr.class);
			
			// ------------- Devices -------------
			put("external_get_devices",ExternalAPIListDevicesPrsr.class);
			put("external_update_device",ExternalAPIResponseStatusPrsr.class);
			put("external_remove_device_from_account",ExternalAPIResponseStatusPrsr.class);

			// ------------- Configuration -------------
			put("external_vendor_config", VendorConfigurationSettingsPrsr.class);
			put("external_get_time_zones", ExternalAPITimeZonesPrsr.class);

			// ------------- Rules -------------
			put("external_set_account_rules", ExternalAPIResponseStatusPrsr.class);

			// ------------- Alert -------------
			put("external_create_manual_alert", ExternalAPIResponseStatusPrsr.class);
			put("external_close_events", ExternalAPIResponseStatusPrsr.class);

			// ------------- Reports -------------
			put("external_get_step_count", ExternalAPIStepCountReportPrsr.class);

			// ------------- Password Policy -------------
			put("external_reset_password", ExternalAPIResponseStatusPrsr.class);
			put("external_password_policy", PasswordPolicyPrsr.class);
			put("external_change_expired_password", ExternalAPIResponseStatusPrsr.class);
			put("external_change_password", ExternalAPIResponseStatusPrsr.class);
			put("external_change_user_password", ExternalAPIResponseStatusPrsr.class);
			put("external_accept_eula", ExternalAPIResponseStatusPrsr.class);

			// ------------- Monitoring -------------
			put("external_get_monitoring", ExternalAPIGetMonitoringResponsePrsr.class);
		}
	};

	/**
	 * Performs the request processing
	 * 
	 * @param resp
	 *            Generic Class type. Represents the class type of the request to
	 *            perform
	 * @param reqName
	 *            String name of the request
	 * @param reqParams
	 *            Map of params of the request
	 * @param reqHeaders
	 *            Map of headers added to the request
	 * @return
	 */
	public <T> T performRequest(Class<T> resp, String reqName, HashMap<String, ?> reqParams,
			HashMap<String, String> reqHeaders);

	public <T> T performRequest(Class<T> resp, String reqName, HashMap<String, ?> reqParams);

	/**
	 * Performs the request processing sending the body as string
	 * 
	 * @param resp
	 *            Generic Class type. Represents the class type of the request to
	 *            perform
	 * @param reqName
	 *            String name of the request
	 * @param body
	 *            Object body of the request
	 * @param reqHeaders
	 *            Map of headers added to the request
	 * @return
	 */
	public <T> T performRequestRawBody(Class<T> resp, String reqName, Object body, HashMap<String, String> reqHeaders);

	public <T> T performRequestRawBody(Class<T> resp, String reqName, Object body);

	/**
	 * Performs a request and returns the response parsed as a generic Object, with
	 * no use of our parsers
	 * 
	 * @param reqName
	 *            String name of the request
	 * @param params
	 *            String params or body of the request
	 * @param reqHeaders
	 *            Map of headers added to the request
	 * @return response
	 */
	public Object performUnparsedRequest(String reqName, HashMap<String, ?> reqParams,
			HashMap<String, String> reqHeaders);

	public Object performUnparsedRequest(String reqName, HashMap<String, ?> reqParams);

}
