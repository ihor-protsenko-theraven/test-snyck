package com.essence.hc.controllers;

import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.essence.hc.controllers.helper.HelperController;
import com.essence.hc.eil.errors.impl.CreateUserErrorHelper;
import com.essence.hc.eil.exceptions.AuthenticationException;
import com.essence.hc.exceptions.CaregiverAssignmentException;
import com.essence.hc.model.AlertPreferences;
import com.essence.hc.model.Device;
import com.essence.hc.model.ExternalAPIDevice;
import com.essence.hc.model.ExternalAPIInstallationDevices;
import com.essence.hc.model.InstallationDevices;
import com.essence.hc.model.Language;
import com.essence.hc.model.PasswordPolicy;
import com.essence.hc.model.Patient;
import com.essence.hc.model.ResponseStatus;
import com.essence.hc.model.SystemUser;
import com.essence.hc.model.ThirdPartyUser;
import com.essence.hc.model.User;
import com.essence.hc.model.User.CaregiverType;
import com.essence.hc.model.User.UserGender;
import com.essence.hc.model.User.UserType;
import com.essence.hc.model.Vendor.ServiceType;
import com.essence.hc.service.InstallationService;
import com.essence.hc.service.PasswordPolicyService;
import com.essence.hc.service.PatientService;
import com.essence.hc.service.UserService;
import com.essence.hc.util.Util;
import com.essence.security.SecurityService;

/**
 * Controller for residents management by caregivers
 * 
 * @author ruben.sanchez
 *
 */
@Controller
@RequestMapping("/admin/residents")
public class CaregiverController {

	protected final Logger logger = (Logger) LogManager.getLogger(CaregiverController.class);

	@Autowired
	private MessageSource msgSrc;
	@Autowired
	private UserService userService;
	@Autowired
	private SecurityService securityService;
	@Autowired
	private PatientService patientService;
	@Autowired
	private InstallationService installationService;
	
	@Autowired
	private PasswordPolicyService passwordPolicyService;


	private CreateUserErrorHelper myCreateUserErrorHelper = new CreateUserErrorHelper();
	private static final UserGender[] VIEWABLE_USER_GENDER = new UserGender[] {UserGender.FEMALE, UserGender.MALE};
	private static final CaregiverType[] VIEWABLE_CAREGIVER_TYPE = new CaregiverType[] {CaregiverType.STANDARD, CaregiverType.MASTER};	
	/**
	 * Show residents list for the logged in caregiver
	 * 
	 * @param model
	 * @param locale
	 * @param sTypeFilter
	 * @param q
	 * @param qcriteria
	 * @param sort
	 * @param asc
	 * @return
	 */
	@RequestMapping(value = "", method = { RequestMethod.GET, RequestMethod.POST })
	public String showResidents(ModelMap model, Locale locale,
			@RequestParam(value = "serviceTypeFilter[]", required = false) ServiceType[] sTypeFilter,
			@RequestParam(value = "q", required = false) String q,
			@RequestParam(value = "qcriteria", required = false) String qcriteria,
			@RequestParam(value = "sort", required = false) String sort,
			@RequestParam(value = "asc", required = false) Boolean asc,
			@RequestParam(value = "enableActiveService[]", required = false) final Boolean[] enableActiveService) {

		Boolean enableActiveServiceFilter = (enableActiveService != null && enableActiveService.length == 1)
				? enableActiveService[0]
				: null;
		UserType[] userTypes = { UserType.ROLE_MONITORED };
		List<SystemUser> listSystemUsers = userService.getSystemUsersForCaregiver(qcriteria, q, sort, asc, userTypes,
				sTypeFilter, enableActiveServiceFilter);
		SystemUser user = userService.getUser(securityService.getPrincipal().getId());

		model.addAttribute("patients", listSystemUsers);
		model.addAttribute("user", user);
		model.addAttribute("service_type_filter", sTypeFilter);
		model.addAttribute("sort", sort);
		model.addAttribute("asc", (asc != null) ? asc : true);
		model.addAttribute("adminBoolean", true);
		model.addAttribute("enableActiveService", enableActiveService);

		if (HelperController.isAjax()) {
			return "adminCaregiverResidentsList";
		} else {
			return "adminCaregiverResidents";
		}
	}

	/**
	 * Edit resident form
	 * 
	 * @param model
	 * @param locale
	 * @param userId
	 * @return
	 */
	@RequestMapping(value = "{id}/edit", method = RequestMethod.GET)
	public String showEditUser(ModelMap model, Locale locale, @PathVariable("id") String userId) {

		SystemUser user = userService.getUser(userId);

		model.addAttribute("user_gender", VIEWABLE_USER_GENDER);
		model.addAttribute("user_resident_type", UserType.ROLE_MONITORED);
		model.addAttribute("user", user);
		model.addAttribute("free_panels",
							userService.getFreePanels(new ServiceType[] { user.getServiceType() }, new String[] {"CP"}));
		model.addAttribute("list_languages", userService.getLanguages());
		model.addAttribute("language_select", user.getLanguage());

		// NEW CODE

		if (user.getUserType() == UserType.ROLE_MONITORED) {
			model.addAttribute("timeZone_select", ((Patient) user).getInstallation().getPanel().getTimeZone());
			model.addAttribute("list_timeZones", securityService.getPrincipal().getVendor().getTimeZones());
		}

		// END NEW CODE

		return "adminEditUser";
	}

	/**
	 * List Caregivers for a Patient
	 * 
	 * @param model
	 * @param locale
	 * @param userId
	 * @return
	 */
	@RequestMapping(value = "{userId}/caregivers", method = { RequestMethod.GET, RequestMethod.POST })
	public String showListResidentCaregivers(ModelMap model, Locale locale, @PathVariable("userId") String userId,
			@RequestParam(value = "q", required = false) String q,
			@RequestParam(value = "qcriteria", required = false) String qcriteria) {

		Patient user = (Patient) userService.getUser(userId);
		List<SystemUser> caregivers = patientService.getPatientCareGivers(user, qcriteria, q);

		model.addAttribute("list_caregivers", caregivers);
		model.addAttribute("user", user);

		if (HelperController.isAjax()) {
			return "adminResidentCaregiversList";
		} else {
			return "adminResidentCaregivers";
		}
	}

	/**
	 * Show assign existing caregiver modal form
	 * 
	 * @param model
	 * @param locale
	 * @param userId
	 * @return
	 */
	@RequestMapping(value = "{id}/assign-caregiver", method = RequestMethod.GET)
	public String showAssignCaregiverModal(ModelMap model, Locale locale, @PathVariable("id") String userId) {
		model.addAttribute("residentId", userId);
		model.addAttribute("caregiver_types", VIEWABLE_CAREGIVER_TYPE);
		return "adminAssignCaregiver";
	}

	/**
	 * Assign existing caregiver
	 * 
	 * @param model
	 * @param locale
	 * @param userId
	 * @param caregiverUsername
	 * @param caregiverType
	 * @return
	 */
	@RequestMapping(value = "{id}/assign-caregiver", method = RequestMethod.POST)
	public String assignCaregiver(ModelMap model, Locale locale, final RedirectAttributes redirectAttributes,
			@PathVariable("id") String userId, @RequestParam(value = "user_nick") String caregiverUsername,
			@RequestParam(value = "caregiver_type", required = false) String caregiverType) {

		Patient resident = (Patient) userService.getUser(userId);
		SystemUser caregiver = new SystemUser();
		caregiver.setNick(caregiverUsername);
		model.addAttribute("resident", resident);
		boolean master = false;
		if (CaregiverType.MASTER.toString().equals(caregiverType)) {
			master = true;
		}

		// isAdd always true because is the functionallity that assigns an existing
		// caregiver that should not be assigned to the resident
		ResponseStatus status = patientService.assignCaregiver(resident, caregiver, master, true);

		if (status.isOK()) {
			return HelperController.redirectWithFlashAttributes(redirectAttributes,
					"redirect:/admin/residents/" + userId + "/caregivers", "caregivers_resident.assign_success",
					"caregivers_resident.add_existing",status.getMessageErr());
		} else {
			return HelperController.redirectWithFlashAttributes(redirectAttributes,
					"redirect:/admin/residents/" + userId + "/caregivers", "assign.error." + status.getMessageErr(),
					"caregivers_resident.add_existing", status.getMessageErr());
		}
	}

	/**
	 * New caregiver form
	 * 
	 * @param model
	 * @param locale
	 * @return
	 */
	@RequestMapping(value = "{id}/new-caregiver", method = RequestMethod.GET)
	public String showNewCaregiver(ModelMap model, Locale locale, @PathVariable("id") String residentId,
			@RequestParam Map<String, String> allRequestParams) {

		SystemUser resident = userService.getUser(residentId);

		// TODO: So far 1 passwordPolicy per HSP, future pp per userType
		PasswordPolicy pp = passwordPolicyService.getPasswordPolicy();

		// views variables
		model.addAttribute("resident", resident);
		model.addAttribute("caregiver_types", VIEWABLE_CAREGIVER_TYPE);
		Set<CaregiverType> disabledTypes = new HashSet<CaregiverType>();
		for (CaregiverType t : VIEWABLE_CAREGIVER_TYPE) {
			if (allRequestParams.containsKey("no" + t.toString().toLowerCase())) {
				disabledTypes.add(t);
			}
		}
		model.addAttribute("disabled_types", disabledTypes);
		model.addAttribute("user_gender", VIEWABLE_USER_GENDER);
		model.addAttribute("list_languages", userService.getLanguages());
		model.addAttribute("password_policy", pp);

		return "adminNewCaregiver";
	}

	/**
	 * Create new caregiver and assign to a resident
	 * 
	 * @param model
	 * @param locale
	 * @param redirectAttributes
	 * @param residentId
	 * @param allRequestParams
	 * @return
	 * @throws ParseException
	 */
	@RequestMapping(value = "{id}/new-caregiver", method = RequestMethod.POST)
	public String addNewCaregiver(ModelMap model, Locale locale, final RedirectAttributes redirectAttributes,
			@PathVariable("id") String residentId, @RequestParam Map<String, String> allRequestParams)
			throws ParseException {

		Patient resident = (Patient) userService.getUser(residentId);
		SystemUser newUser = new User();
		newUser.setUserType(UserType.ROLE_CAREGIVER);
		PasswordPolicy pp = passwordPolicyService.getPasswordPolicy();
		fillCaregiverData(newUser, allRequestParams, locale);
		boolean master = false;
		if (CaregiverType.MASTER.toString().equals(allRequestParams.get("user[caregiver_type]"))) {
			master = true;
		}
		
		ResponseStatus status = patientService.assignNewCaregiver(resident, newUser, master); 
		
		if (status.isOK()){
			return HelperController.redirectWithFlashAttributes(redirectAttributes, "redirect:/admin/residents/" + residentId + "/caregivers",
					"admin.users.create.success.message", "caregivers_resident.add_new", status.getMessageErr());
		} else {

			if ("RegisterFailedUserAlreadyExists".equals(status.getMessageErr())) {
				model.addAttribute("flashMessage", "admin.users.duplicate");
			} else if ((status.getNumErr() == 167) || (status.getNumErr () == -167))   {
				model.addAttribute("flashMessage", myCreateUserErrorHelper.getErrorMessage(status.getNumErr()));
			} else {
				model.addAttribute("flashMessage", "assign.error." + status.getMessageErr());
			}
			
			if (status.getNumErr() == 112 ) {
				logger.debug("Att: the password did not meet with policy");
			}

			model.addAttribute("numErr", status.getNumErr());
			model.addAttribute("resident", resident);
			model.addAttribute("caregiver_types", VIEWABLE_CAREGIVER_TYPE);
			model.addAttribute("user_gender", VIEWABLE_USER_GENDER);
			model.addAttribute("list_languages", userService.getLanguages());
			model.addAttribute("flashMessageArgument", status.getMessageErr());
			model.addAttribute("flashTitle", "caregivers_resident.add_new");
			model.addAttribute("flashError", true);
			model.addAttribute("newUser", newUser);
			model.addAttribute("password_policy", pp);

			return "adminNewCaregiver";
		}
	}

	/**
	 * Fills a User instance with request params
	 * 
	 * @param user
	 * @param reqParams
	 */
	private void fillCaregiverData(SystemUser user, Map<String, String> reqParams, Locale locale) {

		if (!Util.isBlank(reqParams.get("user[nick]")))
			user.setNick(reqParams.get("user[nick]"));

		if (!Util.isBlank(reqParams.get("user[password]")))
			user.setPasswd(reqParams.get("user[password]"));

		if (!Util.isBlank(reqParams.get("user[confirmPassword]")))
			user.setPasswdConfirmation(reqParams.get("user[confirmPassword]"));

		if (!Util.isBlank(reqParams.get("user[name]")))
			user.setFirstName(reqParams.get("user[name]"));

		if (!Util.isBlank(reqParams.get("user[lastName]")))
			user.setLastName(reqParams.get("user[lastName]"));

		if (!Util.isBlank(reqParams.get("user[email]")))
			user.setEmail(reqParams.get("user[email]"));

		if (reqParams.get("user[address]") != null)
			user.setAddress(reqParams.get("user[address]"));

		if (reqParams.get("user[generalId]") != null)
			user.setGeneralId(reqParams.get("user[generalId]"));

		if (!Util.isBlank(reqParams.get("user[cellPhone]")))
			user.setMobile(reqParams.get("user[cellPhone]"));

		if (reqParams.get("user[phoneAtHome]") != null)
			user.setPhone(reqParams.get("user[phoneAtHome]"));

		if (!Util.isBlank(reqParams.get("user[gender]")))
			user.setGender(UserGender.valueOf(reqParams.get("user[gender]")));

		if (reqParams.get("user[birthdate]") != null)
			user.setBirthDate(Util.parseDate(reqParams.get("user[birthdate]"), locale));

		if (!Util.isBlank(reqParams.get("user[languageId]")))
			user.setLanguage(Language.getLanguage(Integer.parseInt(reqParams.get("user[languageId]"))));

		if (!Util.isBlank(reqParams.get("user[mobileModeOnDesktop]")))
			user.setMobileModeOnDesktop(reqParams.get("user[mobileModeOnDesktop]").equals("true"));
		else
			user.setMobileModeOnDesktop(false);

	}

	/**************************************************************************/
	/***************************** ASSOCIATIONS *******************************/
	/**************************************************************************/

	/**
	 * Assign Caregiver to Resident Assign Resident to Caregiver
	 * 
	 * @param model
	 * @param locale
	 * @param userId
	 * @param residentId
	 * @return
	 * @throws CaregiverAssignmentException
	 */
	@RequestMapping(value = "{userId}/residents/assign/{targetId}", method = RequestMethod.POST)
	public String assignResidentCaregiver(ModelMap model, Locale locale, @PathVariable("userId") String userId,
			@PathVariable("targetId") String targetId, @RequestParam(value = "master", required = false) String master,
			@RequestParam(value = "isAdd", required = false) String isAdd) throws CaregiverAssignmentException {

		SystemUser caregiver = userService.getUser(userId);
		Patient resident = (Patient) userService.getUser(targetId);
		boolean isMaster = false;
		if (master != null)
			isMaster = true;

		boolean isAddAssign = false;
		if (isAdd != null)
			isAddAssign = true;

		ResponseStatus status = patientService.assignCaregiver(resident, caregiver, isMaster, isAddAssign);
		// if ((status.getNumErr() >= 0)) {
		if (status.isOK()) {
			resident.setAssigned(true);
			if (isMaster)
				resident.setCaregiverType(CaregiverType.MASTER);
			else
				resident.setCaregiverType(CaregiverType.STANDARD);
			model.addAttribute("user", resident);
			return "adminCaregiverResidentItem";
		}

		throw new CaregiverAssignmentException(
				msgSrc.getMessage("assign.error." + status.getMessageErr(), null, locale));
		// return "";
	}

	@RequestMapping(value = "{targetId}/caregivers/assign/{userId}", method = RequestMethod.POST)
	public String assignCaregiverResident(ModelMap model, Locale locale, @PathVariable("userId") String userId,
			@PathVariable("targetId") String targetId, @RequestParam(value = "master", required = false) String master,
			@RequestParam(value = "isAdd", required = false) String isAdd) throws CaregiverAssignmentException {

		SystemUser caregiver = userService.getUser(targetId);
		Patient resident = (Patient) userService.getUser(userId);
		boolean isMaster = false;
		if (master != null)
			isMaster = true;

		boolean isAddAssign = false;
		if (isAdd != null)
			isAddAssign = true;

		ResponseStatus status = patientService.assignCaregiver(resident, caregiver, isMaster, isAddAssign);
		// if ((status.getNumErr() >= 0)) {
		if (status.isOK()) {
			caregiver.setAssigned(true);
			if (isMaster)
				caregiver.setCaregiverType(CaregiverType.MASTER);
			else
				caregiver.setCaregiverType(CaregiverType.STANDARD);
			model.addAttribute("caregiver", caregiver);
			model.addAttribute("user", resident);
			return "adminResidentCaregiverItem";
		}

		throw new CaregiverAssignmentException(
				msgSrc.getMessage("assign.error." + status.getMessageErr(), null, locale));
		// return "";
	}

	/**
	 * Unassign Caregiver resident unassign resident to a caregiver
	 * 
	 * @param model
	 * @param locale
	 * @param userId
	 * @param residentId
	 * @return
	 * @throws CaregiverAssignmentException
	 */
	@RequestMapping(value = "{targetId}/caregivers/unassign/{userId}", method = RequestMethod.POST)
	public String unassignResidentCaregiver(ModelMap model, Locale locale, @PathVariable("userId") String userId,
			@PathVariable("targetId") String targetId) throws CaregiverAssignmentException {

		SystemUser caregiver = userService.getUser(targetId);
		Patient resident = (Patient) userService.getUser(userId);

		ResponseStatus status = patientService.unassignCaregiver(resident, caregiver);

		// if ((status.getNumErr() >= 0)) {
		if (status.isOK()) {
			caregiver.setAssigned(false);
			model.addAttribute("caregiver", caregiver);
			model.addAttribute("user", resident);
			return "adminResidentCaregiverItem";
		}

		// view variables
		throw new CaregiverAssignmentException(
				msgSrc.getMessage("assign.error." + status.getMessageErr(), null, locale));
		// return "";
	}

	@RequestMapping(value = "{userId}/residents/unassign/{targetId}", method = RequestMethod.POST)
	public String unassignCaregiverResident(ModelMap model, Locale locale, @PathVariable("userId") String userId,
			@PathVariable("targetId") String targetId) throws CaregiverAssignmentException {

		SystemUser caregiver = userService.getUser(userId);
		Patient resident = (Patient) userService.getUser(targetId);

		ResponseStatus status = patientService.unassignCaregiver(resident, caregiver);

		// if ((status.getNumErr() >= 0)) {
		if (status.isOK()) {
			caregiver.setAssigned(false);
			model.addAttribute("user", resident);
			return "adminCaregiverResidentItem";
		}

		// view variables
		throw new CaregiverAssignmentException(
				msgSrc.getMessage("assign.error." + status.getMessageErr(), null, locale));
		// return "";
	}

	@ExceptionHandler(CaregiverAssignmentException.class)
	public ResponseEntity<String> handleAssignmentError(CaregiverAssignmentException e) {
		return new ResponseEntity<String>(e.getMessage(), HttpStatus.CONFLICT);
	}

	/**************************************************************************/
	/*************************** END ASSOCIATIONS *****************************/
	/**************************************************************************/

	/**************************************************************************/
	/**************** CAREGIVER-RESIDENT ALERT PREFERENCES ********************/
	/**************************************************************************/

	/**
	 * Get Alert Types Preferences Retrieves a user's alert type preferences
	 * regarding another user's fired alerts Valid for: - Caregiver<->Resident
	 * associations - Admins global alert type preferences (both user id params will
	 * be the same)
	 * 
	 * @param model
	 * @param locale
	 * @param userId
	 * @param targetId
	 * @return
	 */
	@RequestMapping(value = "{userId}/alertsConf/{targetId}", method = RequestMethod.GET)
	public String getAlertPreferences(ModelMap model, Locale locale, @PathVariable("userId") String userId,
			@PathVariable("targetId") String targetId,
			@RequestParam(value = "master", required = false) String master) {

		SystemUser caregiver = userService.getUser(userId);
		if (master != null) {
			caregiver.setCaregiverType(CaregiverType.MASTER);
		} else {
			caregiver.setCaregiverType(CaregiverType.STANDARD);
		}
		SystemUser resident = null;
		AlertPreferences alertPreferences;
		if (targetId == null || targetId.isEmpty() || targetId.equals(userId)) {
			// Admin
			alertPreferences = userService.getAlertPreferences(Integer.valueOf(userId),
					Integer.valueOf(this.getVendorId()));
		} else {
			// Caregiver<->Resident association
			resident = userService.getUser(targetId);
			alertPreferences = userService.getCaregiverAlertPreferences(caregiver, resident);
			
			// For non-Pro service types, show Possible Fall alerts only for users with EPA or FALL_DETECTOR (RFD)
			if (!resident.getServiceType().equals(ServiceType.ANALYTICS)) {
				ExternalAPIInstallationDevices deviceList = installationService.DeviceListByExternalAPI(resident.getAccountNumber(), Integer.toString(resident.getUserId()));
				boolean isEnabledPossibleFallAlerts = false;
				for(ExternalAPIDevice d: deviceList.getDevicesList()) {
					if (d.getActivityType() != null && ("EPA".equals(d.getActivityType().name()) 
							|| "FALL_DETECTOR".equals(d.getActivityType().name()))) {
						isEnabledPossibleFallAlerts = true;
					}
				}
				if (!isEnabledPossibleFallAlerts) {
					alertPreferences.getActivityAlerts().remove("bGetFall");
				}
			}
		}

		Map<String, Map<String, Boolean>> alertGroupsMap = new LinkedHashMap<String, Map<String, Boolean>>();
		boolean allMarked = false;

		if (alertPreferences != null) {
			alertGroupsMap.put("activity_alerts_config", alertPreferences.getActivityAlerts());
			alertGroupsMap.put("emergency_alerts_config", alertPreferences.getEmergencyAlerts());
			alertGroupsMap.put("safety_alerts_config", alertPreferences.getSafetyAlerts());
			alertGroupsMap.put("technical_alerts_config", alertPreferences.getTechnicalAlerts());
			alertGroupsMap.put("security_alerts_config", alertPreferences.getSecurityAlerts());
			alertGroupsMap.put("advisory_alerts_config", alertPreferences.getAdvisoryAlerts());

			allMarked = true;
			for (String alertGroupKey : alertGroupsMap.keySet()) {
				logger.trace("******************** alertGroupKey: {}", alertGroupKey);
				if (!alertGroupsMap.get(alertGroupKey).isEmpty()) {
					logger.trace("alertGroupsMap.get({}): {}", alertGroupKey, alertGroupsMap.get(alertGroupKey));
					for (Boolean alertValue : alertGroupsMap.get(alertGroupKey).values()) {
						if (alertValue != null && !alertValue) {
							allMarked = false;
							break;
						}
					}
				} else {
					logger.trace("empty group");
				}
			}
		}

		List<Boolean> listNotifications = new ArrayList<Boolean>(
				alertPreferences.getAlertReportNotifications().values());
		List<Boolean> listReports = new ArrayList<Boolean>(alertPreferences.getDailyReportNotifications().values());

		model.addAttribute("alert_groups", alertGroupsMap);
		model.addAttribute("alert_types_all_marked", allMarked);
		model.addAttribute("notifications", listNotifications);
		model.addAttribute("reports", listReports);
		model.addAttribute("caregiver", caregiver);
		if (resident != null)
			model.addAttribute("resident", resident);

		return "adminAlertsConf";
	}

	/**
	 * Get Alert Types Preferences for current user Retrieves a user's alert type
	 * preferences regarding another user's fired alerts Valid for: -
	 * Caregiver<->Resident associations
	 * 
	 * @param model
	 * @param locale
	 * @param targetId
	 * @return
	 */
	@RequestMapping(value = "user/alertsConf/{targetId}", method = RequestMethod.GET)
	public String getCurrentUserAlertPreferences(ModelMap model, Locale locale,
			@PathVariable("targetId") String targetId,
			@RequestParam(value = "master", required = false) String master) {

		SystemUser user = securityService.getPrincipal();
		return getAlertPreferences(model, locale, Integer.toString(user.getUserId()), targetId, master);
	}

	/**
	 * Set Alert Types Preferences for Caregiver<->Resident associations
	 * 
	 * @param model
	 * @param locale
	 * @param userId
	 * @param targetId
	 * @param alertTypes
	 * @return
	 */
	@RequestMapping(value = "{userId}/alertsConf/{targetId}", method = RequestMethod.POST)
	public String setCaregiverAlertPreferences(ModelMap model, Locale locale, @PathVariable("userId") String userId,
			@PathVariable("targetId") String targetId, @RequestParam(value = "master", required = false) String master,
			@RequestParam(value = "isAdd", required = false) String isAdd,
			@RequestParam(value = "user[alert_types][]", required = false) String[] alertTypes,
			@RequestParam(value = "user[all_alert_types][]", required = false) String[] allAlertTypes) {

		SystemUser caregiver = userService.getUser(targetId);
		Patient resident = (Patient) userService.getUser(userId);
		boolean isMaster = false;
		if (master != null)
			isMaster = true;

		boolean isAddAssign = false;
		if (isAdd != null)
			isAddAssign = true;

		Map<String, Boolean> listAlertTypes = new HashMap<String, Boolean>();
		Map<String, Boolean> commMethods = new HashMap<String, Boolean>();
		Set<String> enabledAlerts;
		if (alertTypes != null && alertTypes.length > 0)
			enabledAlerts = new HashSet<String>(Arrays.asList(alertTypes));
		else
			enabledAlerts = new HashSet<String>();
		if (allAlertTypes != null) {
			for (int i = 0; i < allAlertTypes.length; i++) {
				String alertName = allAlertTypes[i];
				boolean toCommMethods = false;
				// Conversion of alert names to those of the external API
				if ("bGetPanic".equals(alertName)) {
					alertName = "sos";
				} else if ("bGetDoor".equals(alertName)) {
					alertName = "doorOpen";
				} else if ("bGetTech".equals(alertName)) {
					alertName = "technical";
				} else if ("bGetActvt".equals(alertName)) {
					alertName = "lowActivity";
				} else if ("bGetNtAtHm".equals(alertName)) {
					alertName = "notAtHome";
				} else if ("bGetSmoke".equals(alertName)) {
					alertName = "smoke";
				} else if ("bGetWtLkge".equals(alertName)) {
					alertName = "waterLeakage";
				} else if ("bGetFall".equals(alertName)) {
					alertName = "possibleFall";
				} else if ("alertNotificationEmail".equals(alertName)) {
					alertName = "sendAlertsByEmail";
					toCommMethods = true;
				} else if ("alertNotificationSMS".equals(alertName)) {
					alertName = "sendAlertsBySMS";
					toCommMethods = true;
				} else if ("dailyReportSMS".equals(alertName)) {
					alertName = "sendReportsBySMS";
					toCommMethods = true;
				} else if ("dailyReportEmail".equals(alertName)) {
					alertName = "sendReportsByEmail";
					toCommMethods = true;
				}
				boolean enable = false;
				if (enabledAlerts.contains(allAlertTypes[i]))
					enable = true;
				if (toCommMethods)
					commMethods.put(alertName, enable);
				else
					listAlertTypes.put(alertName, enable);
			}
		}

		ResponseStatus status = userService.setCaregiverAlertPreferences(caregiver, resident, isMaster, isAddAssign,
				listAlertTypes, commMethods);

		if (status.isOK()) {
			return HelperController.successResponseView(model, "admin.users.alerts.update.success.message",
					"alert.account");
		} else {
			return HelperController.errorResponseView(model, status.getMessageErr(), "alert.account");
		}
	}

	/**************************************************************************/
	/**************** END CAREGIVER-RESIDENT ALERT PREFERENCES ****************/
	/**************************************************************************/

	/**
	 * 'Shortcut' method. Returns the current user's vendor id
	 * 
	 * @return
	 */
	protected String getVendorId() {
		User currentUser = (User) securityService.getPrincipal();
		if (currentUser != null) {
			return String.valueOf(currentUser.getVendorId());
		} else {
			return null;
		}
	}

	@ExceptionHandler(AuthenticationException.class)
	public void handleAuthenticationException(Exception ex, HttpServletRequest request, HttpServletResponse response)
			throws IOException {

		logger.error("\n");
		logger.error("------------- AUTHENTICATION EXCEPTION DETECTED -------------");
		logger.error("\n- Error {}, while processing request: {}.", ex.getMessage(), request.getRequestURI());
		logger.error("-----------------------------------------------------");
		logger.error("\n");

		User currentUser = (User) securityService.getPrincipal();

		if (currentUser instanceof ThirdPartyUser) {
			request.getSession().invalidate();
			response.sendRedirect(((ThirdPartyUser) currentUser).getErrorRedirectUrl());
		} else {
			request.getSession().invalidate();
			response.sendError(HttpServletResponse.SC_UNAUTHORIZED);
		}
	}
}
