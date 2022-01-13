package com.essence.hc.controllers;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang3.StringEscapeUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.ContextLoader;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.i18n.SessionLocaleResolver;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.util.WebUtils;

import com.essence.hc.controllers.helper.HelperController;
import com.essence.hc.eil.errors.impl.CreateUserErrorHelper;
import com.essence.hc.eil.errors.impl.UpdateUserErrorHelper;
import com.essence.hc.eil.exceptions.AuthenticationException;
import com.essence.hc.eil.exceptions.OperationNotAllowedException;
import com.essence.hc.exceptions.NewPasswordInvalidByHistoryException;
import com.essence.hc.exceptions.NewPasswordInvalidByPolicyException;
import com.essence.hc.exceptions.PasswordInvalidException;
import com.essence.hc.model.AccountInformation;
import com.essence.hc.model.Action;
import com.essence.hc.model.Activity;
import com.essence.hc.model.Activity.ActivityType;
import com.essence.hc.model.ActivityIndex;
import com.essence.hc.model.ActivityLevel;
import com.essence.hc.model.ActivityLevel.ActivityDetection;
import com.essence.hc.model.ActivityReportDetail;
import com.essence.hc.model.Alert;
import com.essence.hc.model.Alert.AlertState;
import com.essence.hc.model.Alert.AlertType;
import com.essence.hc.model.Alert.IssueType;
import com.essence.hc.model.AlertCloseReason;
import com.essence.hc.model.AlertPreferences;
import com.essence.hc.model.DateFormatHelper;
import com.essence.hc.model.Device;
import com.essence.hc.model.DeviceAssociation;
import com.essence.hc.model.Event;
import com.essence.hc.model.EventLog;
import com.essence.hc.model.ExternalAPIDevice;
import com.essence.hc.model.ExternalAPIDeviceUpdateRequest;
import com.essence.hc.model.Event.EventType;
import com.essence.hc.model.ExternalAPIInstallationDevices;
import com.essence.hc.model.ExternalAPIResidentMonitoring;
import com.essence.hc.model.FreePanel;
import com.essence.hc.model.HCCommand;
import com.essence.hc.model.Installation;
import com.essence.hc.model.InstallationDevices;
import com.essence.hc.model.ItemsFilter;
import com.essence.hc.model.Language;
import com.essence.hc.model.LowActivityReport;
import com.essence.hc.model.Order;
import com.essence.hc.model.Order.OrderByType;
import com.essence.hc.model.Order.OrderDirection;
import com.essence.hc.model.Panel;
import com.essence.hc.model.PanelSettings;
import com.essence.hc.model.PanelSettings.ActiveRolesTypeAllowed;
import com.essence.hc.model.PasswordPolicy;
import com.essence.hc.model.Patient;
import com.essence.hc.model.PatientSettings;
import com.essence.hc.model.PatientSettings.ActivePhoneType;
import com.essence.hc.model.ResponseStatus;
import com.essence.hc.model.RetriableResponse;
import com.essence.hc.model.ServicePackageInformation;
import com.essence.hc.model.StepCountDeviceReport;
import com.essence.hc.model.SystemStatus;
import com.essence.hc.model.SystemStatus.StatusTypes;
import com.essence.hc.model.SystemUser;
import com.essence.hc.model.ThirdPartyUser;
import com.essence.hc.model.User;
import com.essence.hc.model.User.UserGender;
import com.essence.hc.model.User.UserType;
import com.essence.hc.model.Vendor;
import com.essence.hc.model.Vendor.ServiceType;
import com.essence.hc.model.ControlPanelAssociation;
import com.essence.hc.service.ActionService;
import com.essence.hc.service.AlertService;
import com.essence.hc.service.DevicesService;
import com.essence.hc.service.InstallationService;
import com.essence.hc.service.MonitoringService;
import com.essence.hc.service.PasswordPolicyService;
import com.essence.hc.service.PatientService;
import com.essence.hc.service.ReportsService;
import com.essence.hc.service.UserService;
import com.essence.hc.util.AppConfig;
import com.essence.hc.util.Util;
import com.essence.security.SecurityService;

@Controller
@RequestMapping("/admin")
public class AdminController {

	private MessageSource msgSrc;

	private CreateUserErrorHelper myCreateUserErrorHelper = new CreateUserErrorHelper();
	private UpdateUserErrorHelper myUpdateUserErrorHelper = new UpdateUserErrorHelper();

	@Autowired
	public void AccountsController(MessageSource msgSrc) {
		this.msgSrc = msgSrc;
	}

	@Autowired
	private UserService userService;
	@Autowired
	private DevicesService devicesService;
	@Autowired
	private SecurityService securityService;
	@Autowired
	private PatientService patientService;
	@Autowired
	private AlertService alertService;
	@Autowired
	private InstallationService installationService;
	@Autowired
	private ActionService actionService;
	@Autowired
	private ReportsService reportsService;
	@Autowired
	private MonitoringService monitoringService;

	@Autowired
	private PasswordPolicyService passwordPolicyService;

	private ApplicationContext appContext = ContextLoader.getCurrentWebApplicationContext();

	protected final Logger logger = (Logger) LogManager.getLogger(AdminController.class);

	private static final EventType[] VIEWABLE_EVENT_TYPES = new EventType[] { EventType.ACTIVITY, EventType.SOS, EventType.TECHNICAL };
	
	private static final AlertType[] UMBRELLA_VIEWABLE_ALERT_TYPES = new AlertType[] { AlertType.SOS,AlertType.TECHNICAL};

	private static final AlertType[] VIEWABLE_ALERT_TYPES = new AlertType[] { AlertType.ACTIVITY, AlertType.SOS,
			AlertType.TECHNICAL, AlertType.VIDEO, AlertType.ADVISORY };

	private static final AlertState[] VIEWABLE_ALERT_STATES = new AlertState[] { AlertState.NEW, AlertState.IN_PROGRESS,
			AlertState.CLOSED };

	private static final UserType[] VIEWABLE_USER_TYPES = new UserType[] { UserType.ROLE_ADMIN, UserType.ROLE_CAREGIVER,
			UserType.ROLE_MONITORED };

	private static final UserGender[] VIEWABLE_USER_GENDER = new UserGender[] { UserGender.FEMALE, UserGender.MALE };

	// private static final ActivityType[] VIEWABLA_ACTIVITY_TYPES =
	// new ActivityType[] {
	// ActivityType.FRIDGE_DOOR,
	// ActivityType.FRONT_DOOR,
	// ActivityType.BEDROOM_SENSOR,
	// ActivityType.TOILET_ROOM_SENSOR,
	// ActivityType.BATHROOM_SENSOR,
	// // ActivityType.STUDY_ROOM,
	// ActivityType.LIVING_ROOM,
	// ActivityType.DINING_ROOM,
	// ActivityType.SMOKE_DETECTOR,
	// ActivityType.WATER_LEAKAGE,
	// ActivityType.EP,
	// ActivityType.SPBP,
	// ActivityType.OTHER_ROOM
	// };

	private static final ActivityType[] VIEWABLA_ACTIVITY_TYPES_REPORT = new ActivityType[] {
			ActivityType.BATHROOM_SENSOR, ActivityType.BEDROOM_SENSOR, ActivityType.FRIDGE_DOOR,
			ActivityType.FRONT_DOOR, ActivityType.LIVING_ROOM,
			// ActivityType.STUDY_ROOM,
			ActivityType.TOILET_ROOM_SENSOR, ActivityType.DINING_ROOM, ActivityType.OTHER_ROOM };

	private final int RESULT_SIZE = 20;
	private static final String APPLICATION_NAME = "ADL";

	// public static final java.util.Locale ENGLISH;
	// private final Locale locale = Locale.ENGLISH;
	// public static Locale newLocale = Locale.ENGLISH;
	// public static synchronized void setDefault("en","US");

	/**************************************************************************/
	/*****************************
	 * ADMIN ADL
	 ************************************/
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

	/**
	 * @deprecated Monitoring partial
	 * @param model
	 * @param locale
	 * @return
	 */
	@RequestMapping(value = "/monitor", method = RequestMethod.GET)
	public String ShowMonitoring(ModelMap model, Locale locale) {
		User currentUser = (User) securityService.getPrincipal();
		/*
		 * get user's patient list and current patient (if any)
		 */
		List<Patient> patientList = userService.getPatients(currentUser);
		model.addAttribute("patientList", patientList);

		for (Patient p : patientList) {
			// System Status for patient list
			p.setSystemStatus(patientService.getSystemStatus(String.valueOf(p.getUserId()),
					currentUser.getLanguage().getLanguageKey()));
			model.addAttribute("currentPatient", p);

			// Activity Level for patient list
			ActivityLevel activityLevel = patientService.getActivityLevel(String.valueOf(p.getUserId()));
			model.addAttribute("patientActivity", activityLevel);
		}

		return "adminHome";
	}

	/**
	 * Analysis Tab
	 * 
	 * @param model
	 * @param locale
	 * @return
	 */
	@RequestMapping(value = { "/residents_info", "/residents_info_search" }, method = { RequestMethod.GET,
			RequestMethod.POST })
	public String ShowResidentsInfo(ModelMap model, Locale locale,
			@RequestParam(value = "panelTypeFilter[]", required = false) String[] pTypeFilter,
			@RequestParam(value = "serviceTypeFilter[]", required = false) ServiceType[] sTypeFilter,
			@RequestParam(value = "q", required = false) String q,
			@RequestParam(value = "qcriteria", required = false) String qcriteria,
			@RequestParam(value = "page", required = false) String page, @RequestParam(required = false) String sort,
			@RequestParam(required = false) final Boolean asc,
			@RequestParam(value = "enableActiveService[]", required = false) final Boolean[] enableActiveService) {

		String userId = null;
		User currentUser = (User) securityService.getPrincipal();
		int pageNumber = (page == null) ? 0 : Integer.valueOf(page);
		int sizePerPage = 20;
		List<Patient> residents;
		// App Rule: Service Type filtering does not accept more than one value
		Boolean enableActiveServiceFilter = (enableActiveService != null && enableActiveService.length == 1)
				? enableActiveService[0]
				: null;

		userId = currentUser.getId();

		if (!StringUtils.hasText(sort)) {
			sort = "serviceType";
		}

		if (q != null && qcriteria != null && !q.equals("")) {
			residents = patientService.getPatientsInfo(userId, this.getVendorId(), currentUser.getUserType().getId(),
					pageNumber, sizePerPage, qcriteria, q, sort, asc, pTypeFilter, sTypeFilter, enableActiveServiceFilter);
		} else {
			residents = patientService.getPatientsInfo(userId, this.getVendorId(), currentUser.getUserType().getId(),
					pageNumber, sizePerPage, null, null, sort, asc, pTypeFilter, sTypeFilter, enableActiveServiceFilter);
		}

		currentUser.setCurrentPatients(residents);

		model.addAttribute("residents", residents);
		model.addAttribute("panel_type_filter", pTypeFilter);
		model.addAttribute("service_type_filter", sTypeFilter);
		model.addAttribute("q", q);
		model.addAttribute("qcriteria", qcriteria);
		model.addAttribute("sort", sort);
		model.addAttribute("asc", (asc != null) ? asc : true);
		model.addAttribute("enableActiveService", enableActiveService);
		if (HelperController.isAjax()) {
			return "adminResidentInfoPage";
		} else {
			return "adminResidentInfo";
		}
	}

	/**
	 * Monitoring Tab
	 * 
	 * @param model
	 * @param locale
	 * @return
	 */
	@RequestMapping(value = "/residents_in_alarm", method = RequestMethod.GET)
	public String ShowResidentsInAlarm(ModelMap model, Locale locale,
			@RequestParam(value = "panelTypeFilter[]", required = false) String[] pTypeFilter,
			@RequestParam(value = "serviceTypeFilter[]", required = false) ServiceType[] sTypeFilter,
			@RequestParam(value = "page", required = false) String page,
			@RequestParam(required = false) final String sort, @RequestParam(required = false) final Boolean asc,
			@RequestParam(value = "enableActiveService[]", required = false) final Boolean[] enableActiveService,
			@RequestParam(value = "eventTypeFilter[]", required = false) EventType[] eTypeFilter) {
		
		User currentUser = (User) securityService.getPrincipal();
		
		int pageNumber = (page == null) ? 0 : Integer.valueOf(page);
		int sizePerPage = 20;
		ItemsFilter itemsFilter = new ItemsFilter();
		itemsFilter.setSkip(pageNumber*sizePerPage);
		itemsFilter.setTake(sizePerPage);

		// App Rule: Service Type filtering does not accept more than one value
		Boolean enableActiveServiceFilter = (enableActiveService != null && enableActiveService.length == 1)
				? enableActiveService[0]
				: null;
				
		List<Order> orders = new ArrayList<>();
		if (sort != null) {
			Order order = new Order();
			order.setOrderBy(OrderByType.getOrderByType(sort).toString());
			order.setDirection(OrderDirection.getOrderDirection(asc).toString());
			orders.add(order);
		}
		
		List<ExternalAPIResidentMonitoring> residentsAlarm = 
				monitoringService.getMonitoringInfo(enableActiveServiceFilter, pTypeFilter, sTypeFilter, eTypeFilter, itemsFilter, orders);
		
		currentUser.setResidentsMonitoring(residentsAlarm);

		model.addAttribute("residentsAlarm", residentsAlarm);
		model.addAttribute("panel_type_filter", pTypeFilter);
		model.addAttribute("service_type_filter", sTypeFilter);
		model.addAttribute("event_type_filter", eTypeFilter);
		model.addAttribute("sort", sort);
		model.addAttribute("asc", (asc != null) ? asc : true);
		model.addAttribute("enableActiveService", enableActiveService);
		model.addAttribute("event_types", VIEWABLE_EVENT_TYPES);
		
		if (HelperController.isAjax()) {
			return "adminResidentInAlarmPage";
		} else {
			return "adminResidentInAlarm";
		}
		
	}

	/**
	 * Alert Count
	 * 
	 * @param model
	 * @param locale
	 * @return
	 */
	@RequestMapping(value = "/alertCount", method = RequestMethod.GET, produces = "application/json")
	public @ResponseBody RetriableResponse<List<Patient>> ShowAlertCount(ModelMap model, Locale locale) {
		User currentUser = (User) securityService.getPrincipal();
		List<Patient> patientAlerts = alertService.getAlertCount(currentUser);
		RetriableResponse<List<Patient>> response = new RetriableResponse<List<Patient>>();
		response.setResponse(patientAlerts);
		response.setRetryTime(AppConfig.getAlertStatusInterval());

		// Session Timeout Handling
		if (currentUser.isSessionExpired()) {
			currentUser.setSessionExpired(false);
			response.setControlFlag(true);
		}
		return response;
	}

	@RequestMapping(value = "IsValidGeneralId", method = RequestMethod.GET, produces = "application/json")
	public @ResponseBody Boolean IsValidGeneralId(@RequestParam("user[generalId]") String generalId) {
		return userService.IsValidGeneralId(generalId, 0);
	}

	@RequestMapping(value = "IsValidGeneralId/{editingUserId}", method = RequestMethod.GET, produces = "application/json")
	public @ResponseBody Boolean IsValidGeneralId(@RequestParam("user[generalId]") String generalId,
			@PathVariable("editingUserId") int editingUserId) {
		return userService.IsValidGeneralId(generalId, editingUserId);
	}

	@RequestMapping(value = "IsValidUserName", method = RequestMethod.GET, produces = "application/json")
	public @ResponseBody Boolean IsValidUserName(@RequestParam("user[nick]") String userName) {

		// logger.info("IsValidUserName: " +
		// userService.IsValidUserName(userName));
		return userService.IsValidUserName(userName, 0);

	}

	@RequestMapping(value = "IsValidUserName/{editingUserId}", method = RequestMethod.GET, produces = "application/json")
	public @ResponseBody Boolean IsValidUserName(@RequestParam("user[nick]") String userName,
			@PathVariable("editingUserId") int editingUserId) {

		// logger.info("IsValidUserName: " +
		// userService.IsValidUserName(userName));
		return userService.IsValidUserName(userName, editingUserId);

	}

	@RequestMapping(value = "activity_level/{residentId}", method = RequestMethod.GET)
	public String ShowActivity(ModelMap model, Locale locale, @PathVariable("residentId") String residentId) {

		ActivityLevel activityLevel = patientService.getActivityLevel(residentId);
		List<ActivityDetection> activityDetections = activityLevel.getActivityDetections();
		SystemUser resident = userService.getUser(residentId);

		model.addAttribute("activity", activityDetections);
		model.addAttribute("resident", resident);

		return "adminActivityLevel";

	}

	/**
	 * Activity Index Report for Express Residents
	 * 
	 * @param residentId
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "activity_index/{residentId}/{date}", method = RequestMethod.GET)
	public String ShowActivityIndex(@PathVariable("residentId") String residentId, @PathVariable("date") String date,
			Model model) throws ParseException {

		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
		Date toDate = sdf.parse(date);

		SystemUser resident = userService.getUser(residentId);

		model.addAttribute("date", toDate);
		model.addAttribute("resident", resident);

		return "adminActivityIndex";
	}

	/**
	 * New Activity Index Report for Express Residents
	 * 
	 * @param residentId
	 * @param model
	 * @return
	 */
	@Deprecated
	@RequestMapping(value = "activity_index_old/{residentId}/{date}", method = RequestMethod.GET)
	public String ShowNewactivityIndex(@PathVariable("residentId") String residentId, @PathVariable("date") String date,
			Model model) throws ParseException {

		logger.warn("deprecated controller: activity_index_old");
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
		Date toDate = sdf.parse(date);

		SystemUser resident = userService.getUser(residentId);

		model.addAttribute("date", toDate);
		model.addAttribute("resident", resident);

		return "adminActivityIndexOld";
	}

	@RequestMapping(value = "activity_index_old/{residentId}/{date}.json", method = RequestMethod.GET, produces = "application/json")
	@Deprecated
	public @ResponseBody List<ActivityIndex> getActivityIndexOld(@PathVariable("residentId") String residentId,
			@PathVariable("date") String date, Model model) throws ParseException {

		logger.warn("deprecated controller: activity_index_old JSON");
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
		Date toDate = sdf.parse(date);
		Calendar cal = Calendar.getInstance();
		cal.setTime(toDate);
		cal.add(Calendar.DAY_OF_MONTH, -7);
		Date fromDate = cal.getTime();

		SystemUser resident = userService.getUser(residentId);
		List<ActivityIndex> activityIndexData;

		try {
			activityIndexData = patientService.getActivityIndexOld(residentId, fromDate, toDate);
		} catch (Exception e) {
			throw new ParseException(e.getMessage(), 0);
		}

		model.addAttribute("activityIndexData", activityIndexData);
		model.addAttribute("fromDate", fromDate);
		model.addAttribute("toDate", toDate);
		model.addAttribute("resident", resident);

		return activityIndexData;
	}

	/*
	 * Add in ADL 2.2
	 */
	@RequestMapping(value = "activity_index/{residentId}/{date}.json", method = RequestMethod.GET, produces = "application/json")
	public @ResponseBody List<ActivityReportDetail> getActivityIndex(@PathVariable("residentId") String residentId,
			@PathVariable("date") String date, Model model) throws ParseException {

		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
		Date toDate = sdf.parse(date);
		Calendar cal = Calendar.getInstance();
		cal.setTime(toDate);
		// cal.add(Calendar.DAY_OF_MONTH, -7);
		cal.add(Calendar.DATE, -6);
		Date fromDate = cal.getTime();

		SystemUser resident = userService.getUser(residentId);
		List<ActivityReportDetail> activityIndexData = patientService.getActivityIndex(residentId, fromDate, toDate);

		model.addAttribute("activityIndexData", activityIndexData);
		model.addAttribute("fromDate", fromDate);
		model.addAttribute("toDate", toDate);
		model.addAttribute("resident", resident);

		return activityIndexData;
	}

	@RequestMapping(value = "step_count/{residentId}/{date}.json", method = RequestMethod.GET, produces = "application/json")
	public @ResponseBody List<StepCountDeviceReport> getStepCount(@PathVariable("residentId") String residentId,
			@PathVariable("date") String date, Model model) throws ParseException {
		try {

			// EIC15-2456: Resident Account is sent in Resident Code for 2.4.5 Release
			// EIC15-2500: Step Count is waiting por Essence Panel Account, no Customer
			// Account

			Patient resident = (Patient) userService.getUser(residentId);
			String residentAccount = resident.getInstallation().getPanel().getAccount();

			SimpleDateFormat dateFormatter = new SimpleDateFormat("yyyyMMdd");

			Date lastDate = dateFormatter.parse(date);

			// EIC15-2318: Not needed to substract days
			// lastDate = Util.subtractDays(lastDate, 1);
			Date firstDate = Util.subtractDays(lastDate, 30);

			List<StepCountDeviceReport> deviceReports = reportsService
					.getStepCount(residentAccount, null, null, firstDate, lastDate, null).getStepCountDeviceReports();

			model.addAttribute("date", date);
			return deviceReports;

		} catch (Exception e) {
			throw new ParseException(e.getMessage(), 0);
		}
	}

	/**
	 * Step Count Activity
	 * 
	 * @param residentId
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "step_count/{residentId}/{date}", method = RequestMethod.GET)
	public String ShowStepCountActivity(@PathVariable("residentId") String residentId,
			@PathVariable("date") String date, Model model) throws ParseException {
		try {
			SimpleDateFormat dateFormatter = new SimpleDateFormat("yyyyMMdd");
			Date parsedDate = dateFormatter.parse(date);

			dateFormatter.applyPattern("yyyy-MM-dd");

			Patient resident = (Patient) userService.getUser(residentId);
			Date residentDate = dateFormatter.parse(resident.getPanelTime());

			model.addAttribute("residentId", residentId);
			model.addAttribute("date", parsedDate);
			model.addAttribute("residentDate", residentDate.getTime());
			model.addAttribute("resident", resident);

			return "adminStepCountActivity";
		} catch (Exception e) {
			throw new ParseException(e.getMessage(), 0);
		}
	}

	@RequestMapping(value = "step_count_average/{residentId}.json", method = RequestMethod.GET, produces = "application/json")
	public @ResponseBody List<StepCountDeviceReport> getStepCount(@PathVariable("residentId") String residentId,
			Model model) throws ParseException {
		try {

			Patient resident = (Patient) userService.getUser(residentId);
			String residentAccount = resident.getInstallation().getPanel().getAccount();

			SimpleDateFormat dateFormatter = new SimpleDateFormat(Util.DATETIME_TMZ_FORMAT_INPUT);

			Date lastDate = dateFormatter.parse(resident.getPanelTime());
			// EIC15-2318: Not needed to substract days
			// lastDate = Util.subtractDays(lastDate, 1);
			Date firstDate = Util.subtractDays(lastDate, 30);

			List<StepCountDeviceReport> deviceReports = reportsService
					.getStepCount(residentAccount, null, null, firstDate, lastDate, null).getStepCountDeviceReports();

			return deviceReports;

		} catch (Exception e) {
			throw new ParseException(e.getMessage(), 0);
		}
	}

	/**
	 * Day Story Report for Express Residents (for the current date)
	 * 
	 * @param model
	 * @param locale
	 * @param residentId
	 * @return
	 */
	@RequestMapping(value = "day_story_xp/{residentId}", method = RequestMethod.GET)
	public String ShowDayStoryXP(ModelMap model, Locale locale, @PathVariable("residentId") String residentId) throws ParseException {

		Patient resident = (Patient) userService.getUser(residentId);
		
		SimpleDateFormat sdf = new SimpleDateFormat(Util.DATETIMEFORMAT_INPUT);
		
		Date parsedDate = sdf.parse(resident.getPanelTime());

		InstallationDevices deviceList = installationService.DeviceListByUserId(residentId);
		Map<String, List<?>> dayStoryMap = patientService.getDayStoryXP(residentId, parsedDate);
		Date currentPanelTime = patientService.getCurrentPanelTime(dayStoryMap);
		
		sdf = new SimpleDateFormat(Util.DATEFORMAT_OUTPUT_PLAIN);
		Date panelDay = sdf.parse(sdf.format(currentPanelTime));

		model.addAttribute("dayStoryMap", dayStoryMap);
		model.addAttribute("date", parsedDate);
		model.addAttribute("panelDay", panelDay);
		model.addAttribute("now", currentPanelTime);
		model.addAttribute("resident", resident);
		model.addAttribute("devices", deviceList.getDevicesList());

		if (HelperController.isAjax()) {
			return "adminDayStoryGraphicXp";
		} else {
			return "adminDayStoryExpress";
		}
	}
	
	/**
	 * Day Story Report for Express Residents (for a past date)
	 * 
	 * @param model
	 * @param locale
	 * @param residentId
	 * @param date
	 * @return
	 */
	@RequestMapping(value = "day_story_xp/{residentId}/{date}", method = RequestMethod.GET)
	public String ShowDayStoryXPPast(ModelMap model, Locale locale, @PathVariable("residentId") String residentId,
			@PathVariable("date") String date) throws ParseException {

		SimpleDateFormat sdf = new SimpleDateFormat(Util.DATEFORMAT_OUTPUT_PLAIN);
		Date parsedDate = sdf.parse(date);

		SystemUser resident = userService.getUser(residentId);

		InstallationDevices deviceList = installationService.DeviceListByUserId(residentId);
		Map<String, List<?>> dayStoryMap = patientService.getDayStoryXP(residentId, parsedDate);
		Date currentPanelTime = patientService.getCurrentPanelTime(dayStoryMap);
		Date panelDay = sdf.parse(sdf.format(currentPanelTime));

		model.addAttribute("dayStoryMap", dayStoryMap);
		model.addAttribute("date", parsedDate);
		model.addAttribute("panelDay", panelDay);
		model.addAttribute("now", currentPanelTime);
		model.addAttribute("resident", resident);
		model.addAttribute("devices", deviceList.getDevicesList());

		if (HelperController.isAjax()) {
			return "adminDayStoryGraphicXp";
		} else {
			return "adminDayStoryExpress";
		}
	}

	/**
	 * Day Story for the current date
	 * 
	 * @param model
	 * @param locale
	 * @return
	 */
	@RequestMapping(value = "day_story/{residentId}", method = RequestMethod.GET)
	public String ShowDayStory(ModelMap model, Locale locale, @PathVariable("residentId") String residentId) {

		List<Activity> activities = new ArrayList<Activity>();
		List<Alert> alerts = new ArrayList<Alert>();
		Patient resident = (Patient) userService.getUser(residentId);

		SimpleDateFormat dateFormatter = new SimpleDateFormat(Util.DATETIMEFORMAT_INPUT);
		Date patientPanelDate = null;

		try {
			patientPanelDate = dateFormatter.parse(resident.getPanelTime());
		} catch (ParseException e1) {
			this.logger.error("Error parsing resident time zone");
		}

		List<Event> events = patientService.getDayStory(residentId, patientPanelDate, false);

		// Separate Activity events from Alert events
		if ((events != null) && (events.size() != 0)) {
			for (Event e : events) {
				if (Activity.class.isAssignableFrom(e.getClass())) {
					activities.add((Activity) e);
				} else {
					alerts.add((Alert) e);
				}
			}
			model.addAttribute("activities", activities);
			model.addAttribute("alerts", alerts);

		} else {
			logger.info("activities not");
		}

		model.addAttribute("date", patientPanelDate);
		model.addAttribute("types", VIEWABLA_ACTIVITY_TYPES_REPORT);
		model.addAttribute("resident", resident);

		return "adminDayStory";
	}

	/**
	 * Day Story for a past date
	 * 
	 * @param model
	 * @param locale
	 * @return
	 */
	@RequestMapping(value = "day_story/{residentId}/{date}", method = RequestMethod.GET)
	public String ShowDayStoryPast(@PathVariable("residentId") String residentId, @PathVariable("date") String date,
			Model model) throws ParseException {

		Patient resident = (Patient) userService.getUser(residentId);
		SimpleDateFormat dateFormatter = new SimpleDateFormat(Util.DATETIMEFORMAT_INPUT);
		Date patientPanelDate = null;

		try {
			patientPanelDate = dateFormatter.parse(resident.getPanelTime());
		} catch (ParseException e1) {
			this.logger.error("Error parsing resident time zone");
		}

		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
		Date parsedDate = sdf.parse(date);

		List<Event> events;

		if (sdf.format(parsedDate).equals(sdf.format(patientPanelDate))) {
			events = patientService.getDayStory(residentId, patientPanelDate, false);
		} else {
			events = patientService.getDayStory(residentId, parsedDate, true);
		}

		List<Activity> activities = new ArrayList<Activity>();
		List<Alert> alerts = new ArrayList<Alert>();

		// SystemUser resident = userService.getUser(residentId);

		if (events != null) {
			if (events.size() != 0) {
				for (Event e : events) {
					if (Activity.class.isAssignableFrom(e.getClass())) {
						activities.add((Activity) e);
					} else {
						alerts.add((Alert) e);
					}
				}
				model.addAttribute("date", parsedDate);
				model.addAttribute("activities", activities);
				model.addAttribute("alerts", alerts);

			} else
				logger.info("activities not");
		} else {
			logger.error("GetDaySory returned null for {}", residentId);
		}

		model.addAttribute("types", VIEWABLA_ACTIVITY_TYPES_REPORT);
		model.addAttribute("date", parsedDate);
		model.addAttribute("resident", resident);

		if (HelperController.isAjax()) {
			return "adminDayStoryGraphic";
		} else {
			return "adminDayStory";
		}
	}

	/**
	 * Alert Detail Retrieves detailed info for an alert
	 * 
	 * @param model
	 * @param locale
	 * @param alertId
	 * @return
	 */
	@RequestMapping(value = "/event-info/{alertId}", method = RequestMethod.GET)
	public String getAlert(ModelMap model, Locale locale, @PathVariable("alertId") String alertId,
			@RequestParam(value = "viewed", required = false) String viewedAlertId) {
		User currentUser = (User) securityService.getPrincipal();
		Alert alert = alertService.getAlert(alertId, currentUser.getLanguage().getLanguageKey());

		final Patient patient = currentUser.getCurrentPatient();

		EventLog eLog = alertService.eventLog(String.valueOf(currentUser.getUserId()),
				String.valueOf(patient.getUserId()), String.valueOf(alert.getAlertSessionId()), "0",
				currentUser.getLanguage().getLanguageKey(), currentUser.getUserType(), msgSrc, locale);

		/*
		 * Update status of previously viewed alert if param viewed is present
		 */
		if (viewedAlertId != null && !viewedAlertId.isEmpty()) {
			alertService.updateAlertState(Integer.parseInt(viewedAlertId), AlertState.VIEWED, patient.getUserId());
		}
		/*
		 * Photo Alerts "without session" must be closed when viewed (photo alerts
		 * without session are those photos requested and viewed when the system is not
		 * in alarm status)
		 */
		logger.info("\n\nAlert Details. Alert type: {}; Alert session Id: {}; Alert status: {}\n\n", alert.getType(),
				alert.getAlertSessionId(), alert.isClosed());
		if (alert.getType() == AlertType.VIDEO && alert.getAlertSessionId() == 0 && !alert.isClosed()) {
			alertService.updateAlertState(Integer.parseInt(alert.getId()), AlertState.CLOSED, patient.getUserId());
			alert.setCurrentState(AlertState.CLOSED);
			/*
			 * Get Additional info in case alert is of 'Low Activity' type
			 */
		} else if (alert.isLowActivity()) {
			// if ((alert.getType() == Alert.AlertType.SOS || alert.getType() ==
			// Alert.AlertType.ACTIVITY) && alert.getTitle().contains("low
			// activity")) {
			LowActivityReport aReport = alertService.getLowActivityReport(alert.getId(),
					currentUser.getLanguage().getLanguageKey());
			model.addAttribute("lowActivityReport", aReport);
		}
		/*
		 * Update status to viewed if it was new
		 */
		if (alert.getCurrentState() == AlertState.NEW) {
			alertService.updateAlertState(Integer.parseInt(alert.getId()), AlertState.VIEWED, patient.getUserId());
			alert.setCurrentState(AlertState.VIEWED);
		}
		model.addAttribute("selectedAlert", alert);
		String alertTitle = alert.getTitle();
		String detail1 = "";
		String detail2 = "";
		int index = alertTitle.indexOf(':');

		if (index > 0 && !(Character.isDigit(alertTitle.charAt(index - 1))
				&& Character.isDigit(alertTitle.charAt(index + 1)))) { // if :
																		// is
																		// surrounded
																		// by
																		// digits
																		// it is
																		// time
			detail1 = (alert.getTitle().substring(0, (alert.getTitle().indexOf(':'))));
			detail2 = (alert.getTitle().substring((alert.getTitle().indexOf(':') + 1), alert.getTitle().length()));
			model.addAttribute("detail1", detail1);
			model.addAttribute("detail2", detail2);
		} else {
			detail1 = alert.getTitle();
			// detail2 = alert.getTitle();
			model.addAttribute("detail1", detail1);
			// model.addAttribute("detail2", detail2);
		}

		// retrieve in advance list of available commands, so we don't show the
		// take action button if there isn't any command
		Map<String, HCCommand> commands = userService.getCommands(currentUser.getId(), eLog.getSessionState());

		model.addAttribute("commands", commands);
		model.addAttribute("eventlog", eLog);
		model.addAttribute("currentUserId", currentUser.getId());
		model.addAttribute("patientId", patient.getUserId());
		model.addAttribute("sessionState", eLog.getSessionState());
		model.addAttribute("currentResident", patient);

		return "adminEventInfo";
	}

	/**
	 * Alert Event Log Retrieves detailed info for an alert
	 * 
	 * @param model
	 * @param locale
	 * @param alertId
	 * @return
	 */
	@RequestMapping(value = "/event-log/{alertId}", method = RequestMethod.GET)
	public String getAlertEventLog(ModelMap model, Locale locale, @PathVariable("alertId") String alertId) {

		EventLog eventLog = null;
		User currentUser = (User) securityService.getPrincipal();

		Patient currentPatient = currentUser.getCurrentPatient();

		StatusTypes status = null;
		// Get Alert Detail
		Alert alert = alertService.getAlert(alertId, currentUser.getLanguage().getLanguageKey());
		// Get Alert's Event Log
		if (alert.getAlertSessionId() > 0) {
			eventLog = alertService.eventLog(currentUser.getId(), String.valueOf(currentPatient.getUserId()),
					String.valueOf(alert.getAlertSessionId()), "0", currentUser.getLanguage().getLanguageKey(),
					currentUser.getUserType(), msgSrc, locale);
			status = eventLog.getSessionState();
		} else if (alert.getType() == AlertType.VIDEO) {
			status = StatusTypes.USER_PHOTO_REQUEST;
		}

		model.addAttribute("eventdetail", alert);
		model.addAttribute("events", eventLog);
		model.addAttribute("alertId", alertId);
		model.addAttribute("sessionState", status);
		model.addAttribute("currentUserId", currentUser.getId());
		model.addAttribute("patientId", currentPatient.getUserId());

		return "adminEventLog";
	}

	@RequestMapping(value = "/history/{residentId}", method = RequestMethod.GET)
	public String ShowHistory(ModelMap model, Locale locale, @PathVariable("residentId") String residentId,
			@RequestParam(value = "type[]", required = false) AlertType[] type,
			@RequestParam(value = "state[]", required = false) AlertState[] state) {
		User currentUser = (User) securityService.getPrincipal();
		
		SystemUser resident = userService.getUser(residentId);

		List<Alert> alerts = patientService.getAlertHistory(residentId, 0, 0, 20, type, state,
				currentUser.getLanguage().getLanguageKey(), currentUser.getUserId(), currentUser.getUserType());

		// Preparing response for view layer
		model.addAttribute("alerts", alerts);
		if (resident.getServiceType().equals(ServiceType.HELP_ANYWHERE)) {
			model.addAttribute("types", UMBRELLA_VIEWABLE_ALERT_TYPES);
		} else {
			model.addAttribute("types", VIEWABLE_ALERT_TYPES);
		}
		model.addAttribute("states", VIEWABLE_ALERT_STATES);
		model.addAttribute("currentUserId", currentUser.getId());	
		model.addAttribute("patientId", currentUser.getCurrentPatient().getUserId());
		model.addAttribute("selectedTypes", type);
		model.addAttribute("selectedStates", state);
		model.addAttribute("currentResident", currentUser.getCurrentPatient());
		model.addAttribute("roleUser", (currentUser.getUserType() == UserType.ROLE_ADMIN));

		if (alerts != null && !alerts.isEmpty()) {

			EventLog eventLog = null;
			StatusTypes status = null;

			// Get First Alert's Detail
			Alert firstAlert = alertService.getAlert(alerts.get(0).getId(), currentUser.getLanguage().getLanguageKey());// Event
																														// detail
			// Get First Alert's Event Log
			if (firstAlert.getAlertSessionId() > 0) {
				eventLog = alertService.eventLog(currentUser.getId(), residentId,
						String.valueOf(firstAlert.getAlertSessionId()), "0", currentUser.getLanguage().getLanguageKey(),
						currentUser.getUserType(), msgSrc, locale);
				status = eventLog.getSessionState();
			} else if (firstAlert.getType() == AlertType.VIDEO) {
				status = StatusTypes.USER_PHOTO_REQUEST;
			}

			if (firstAlert.isLowActivity()) {
				LowActivityReport aReport = alertService.getLowActivityReport(firstAlert.getId(),
						currentUser.getLanguage().getLanguageKey());
				model.addAttribute("lowActivityReport", aReport);
			}

			model.addAttribute("selectedAlert", firstAlert);

			String alertTitle = firstAlert.getTitle();
			String detail1 = "";
			String detail2 = "";
			int index = alertTitle.indexOf(':');

			if (index > 0 && !(Character.isDigit(alertTitle.charAt(index - 1))
					&& Character.isDigit(alertTitle.charAt(index + 1)))) { // if
																			// :
																			// is
																			// surrounded
																			// by
																			// digits
																			// it
																			// is
																			// time
				detail1 = (firstAlert.getTitle().substring(0, (firstAlert.getTitle().indexOf(':'))));
				detail2 = (firstAlert.getTitle().substring((firstAlert.getTitle().indexOf(':') + 1),
						firstAlert.getTitle().length()));
				model.addAttribute("detail1", detail1);
				model.addAttribute("detail2", detail2);
			} else {
				model.addAttribute("detail1", alertTitle);
			}

			model.addAttribute("events", eventLog);
			model.addAttribute("sessionState", status);

			// retrieve in advance list of available commands, so we don't show
			// the take action button if there isn't any command
			Map<String, HCCommand> commands = null;
			if (eventLog != null) {
				commands = userService.getCommands(currentUser.getId(), eventLog.getSessionState());
			}

			model.addAttribute("commands", commands);

			// list of available reasons to close an event
			List<AlertCloseReason> reasons = null;
			reasons = alertService.getAlertCloseReasons(currentUser.getLanguage().getLanguageKey());
			model.addAttribute("reasons", reasons);

		} else {
			model.addAttribute("selectedAlert", null);
		}

		return "adminHistory";
	}

	@RequestMapping(value = "/history/{patientId}/{page}", method = RequestMethod.GET)
	public String ShowNextPageHistory(ModelMap model, Locale locale, @PathVariable("patientId") String patientId,
			@PathVariable("page") String page,
			@RequestParam(value = "lastAlert", required = false, defaultValue = "0") Integer lastAlertId,
			@RequestParam(value = "type[]", required = false) AlertType[] type,
			@RequestParam(value = "state[]", required = false) AlertState[] state) {
		int pageNumber = (page == null) ? 0 : Integer.valueOf(page);
		User currentUser = securityService.getPrincipal();
		List<Alert> alerts = patientService.getAlertHistory(patientId, lastAlertId, pageNumber, RESULT_SIZE, type,
				state, currentUser.getLanguage().getLanguageKey(), currentUser.getUserId(), currentUser.getUserType());

		// Preparing response for view layer
		model.addAttribute("alerts", alerts);

		return "adminPageHistory";
	}

	/**
	 * General Take Photo Tab
	 * 
	 * @param model
	 * @param locale
	 * @param residentId
	 * @return
	 */
	@RequestMapping(value = "/take-photo/{residentId}", method = RequestMethod.GET)
	public String showTakePhoto(ModelMap model, Locale locale, @PathVariable("residentId") String residentId) {

		List<Device> cameraList = patientService.getCameraList(residentId);
		model.addAttribute("residentId", residentId);
		model.addAttribute("cameraList", cameraList);

		return "adminTakePhoto";
	}

	/**
	 * Photo Requesting for General Take Photo Tab
	 * 
	 * @param model
	 * @param locale
	 * @param residentId
	 * @param deviceId
	 * @return
	 */
	@RequestMapping(value = "/execute-photo/{residentId}/{deviceId}", method = RequestMethod.GET)
	public String takePhoto(ModelMap model, Locale locale, @PathVariable("residentId") String residentId,
			@PathVariable("deviceId") String deviceId) {

		User currentUser = (User) securityService.getPrincipal();
		List<Device> cameraList = patientService.getCameraList(residentId);
		for (Device device : cameraList) {
			if (device.getId().equalsIgnoreCase(deviceId)) {
				model.addAttribute("nameDevice", device.getLabel());
			}
		}
		/*
		 * OCANALEJO 10/03/2014 user pressed general take photo button. So we need to
		 * retrieve patient's main session id
		 */
		int sessionId = 0;
		SystemStatus systemStatus = patientService.getSystemStatus(
				String.valueOf(currentUser.getCurrentPatient().getUserId()),
				currentUser.getLanguage().getLanguageKey());
		if (systemStatus != null) {
			sessionId = systemStatus.getAlertSessionId();
		}
		ResponseStatus photo = patientService.requestPhoto(currentUser.getId(), residentId, deviceId,
				String.valueOf(sessionId));

		if (photo.getNumErr() < 0)
			return "adminTakePhotoRequestFail";
		else
			return "adminTakePhotoRequestSuccess";
	}

	/**
	 * User Commands available by user role
	 * 
	 * @param Model
	 * @return List of commands the user can do
	 */
	@RequestMapping(value = "/take-action/{residentId}/list", method = RequestMethod.GET)
	public String getUserCommands(ModelMap model,
			@RequestParam(value = "sessionState", required = false) StatusTypes sessionState,
			@RequestParam(value = "alertId", required = false) String alertId,
			@RequestParam(value = "sessionId", required = false) String sessionId,
			@PathVariable("residentId") String residentId) {

		logger.info("\ngetting actions for user\n");

		User currentUser = securityService.getPrincipal();
		// SystemStatus systemStatus =
		// patientService.getSystemStatus(residentId);

		// List<String> messages =
		// userService.getPredefinedTextByCommandId("call_police");
		// This call was made just before, to know if there are available events
		// for this user and session state
		// The code could be improved to store the result somewhere and avoid
		// this extra call to the API
		// Or even better, the available commands could be shown using
		// javascript, making this mathod unnecessary
		Map<String, HCCommand> commands = userService.getCommands(currentUser.getId(), sessionState);

		model.addAttribute("commands", commands);
		model.addAttribute("parentId", alertId);
		model.addAttribute("sessionId", sessionId);
		model.addAttribute("residentId", residentId);

		return "adminTakeAction";
	}

	/**
	 * Get the Action's page
	 * 
	 * @return String representing the name of the view which will be rendered for
	 *         the specified actionId
	 * 
	 *         <p>
	 *         <i>NOTE: The returned view name must be equals to the string
	 *         specified in actionId Uri param </i>
	 *         </p>
	 */
	@RequestMapping(value = "/take-action/{residentId}/{action_name}/view", method = RequestMethod.GET)
	public String action(ModelMap model, @PathVariable("action_name") String actionName,
			@PathVariable("residentId") String residentId,
			@RequestParam(value = "sessionId", required = false) String sessionId,
			@RequestParam(value = "parentId", required = false) String parentActionId) {

		logger.info("\n\n Requesting Action Page for Action {} \n\n", actionName);
		// User currentUser = (User) securityService.getPrincipal();
		// SystemStatus systemStatus =
		// patientService.getSystemStatus(residentId);

		Action action = prepareAction(actionName, residentId, null);
		action.getData();

		model.addAttribute("residentId", residentId);
		model.addAttribute("alertId", sessionId);
		model.addAttribute("parentId", parentActionId);
		model.addAttribute("action", action);

		return "admin_" + actionName;
	}

	/**
	 * Action execution
	 * 
	 * @param actionName
	 *            action to execute
	 * @param action_attributes
	 *            map which contains the attribute values for action execution
	 * @return view name to be rendered
	 */
	@RequestMapping(value = "/take-action/{residentId}/{action_name}/execute", method = RequestMethod.POST)
	public String takeAction(ModelMap model, @PathVariable("action_name") String actionName,
			@PathVariable("residentId") String residentId,
			@RequestParam(required = false) Map<String, String> action_attributes, RedirectAttributes ra) {

		logger.info("\n\n Executing Action {} \n\n", actionName);

		Action action = prepareAction(actionName, residentId, action_attributes);
		action.getData();
		boolean result = actionService.execute(action);

		model.addAttribute("action", action);
		logger.info("\n\n Executing GetData GetCameraList {} \n\n");
		return "admin_" + actionName + (result ? "_success" : "_fail");
	}

	/**
	 * Loads, initializes and prepares action for been used
	 * 
	 * @param actionName
	 *            action identification
	 * @return {@link Action}
	 */
	private Action prepareAction(String actionName, String residentId, Map<String, String> action_attributes) {

		logger.info("\n\n Preparing Action {}\n\n", actionName);
		/*
		 * Looks for actionId in the user's command catalog if found, gets the
		 * corresponding Action Bean from Spring's context and prepares it.
		 */
		if (actionName != null) {
			try {
				/*
				 * Check action is in user's commands
				 */
				// User currentUser = securityService.getPrincipal();
				// SystemStatus systemStatus =
				// patientService.getSystemStatus(residentId);

				// Map<String, HCCommand> commands =
				// userService.getCommands(currentUser.getId(),
				// systemStatus.getStatusType());

				// if (commands != null && commands.containsKey(actionName)){
				/*
				 * Load and initialization of Action bean
				 */
				logger.info("\nCommand found at user's catalog!\n");
				Action action = (Action) appContext.getBean(actionName);
				action.setName(actionName);
				action.setAttributes(action_attributes);
				action.init();

				return action;
				// }else{
				// logger.info("\nCommand Not found at user's catalog\n");
				// throw new AppRuntimeException("User cannot execute that
				// action");
				// }
			} catch (Exception ex) {
				logger.info("\nAction Not Found or Missing Request Param 'action'\n");
				ex.printStackTrace();
			}
		}
		return null;
	}

	/**
	 * This action shows a menu with the related actions the user can do when click
	 * over the icon of other user in the events log.
	 * 
	 * @param model
	 * @param locale
	 * @param userId
	 * @param sessionId
	 * @param msgId
	 * @return A modal template with the actions the logger user can do.
	 */
	@RequestMapping(value = "{residentId}/alerts/{sessionId}/take-action/{userId}", method = RequestMethod.GET)
	public String getPhoneUser(ModelMap model, Locale locale, @PathVariable("residentId") String residentId,
			@PathVariable("userId") String userId, @PathVariable("sessionId") String sessionId,
			@RequestParam(value = "msgId", required = true) String msgId) {
		User currentUser = securityService.getPrincipal();
		SystemUser user = userService.getUser(userId);
		SystemStatus systemStatus = patientService.getSystemStatus(residentId,
				currentUser.getLanguage().getLanguageKey());

		boolean isLoggedUser = false;
		if (securityService.getPrincipal().getUserId() == user.getUserId()) {
			isLoggedUser = true;
		}

		HashMap<String, HCCommand> commands = (HashMap<String, HCCommand>) userService
				.getCommands(String.valueOf(securityService.getPrincipal().getUserId()), systemStatus.getStatusType());

		if (commands.keySet().contains("broadcast")) {
			model.addAttribute("hasBroadcastMessage", true);
		}

		model.addAttribute("user", user);
		model.addAttribute("msgId", msgId);
		model.addAttribute("sessionId", sessionId);
		model.addAttribute("isLoggedUser", isLoggedUser);

		return "adminCalluser";
	}

	/**
	 * @deprecated Alert Event Log
	 * @param model
	 * @param locale
	 * @param alertSessionId
	 * @return
	 */
	@RequestMapping(value = "{sessionId}/eventLog", method = RequestMethod.GET)
	public String ShowEventLog(ModelMap model, Locale locale, @PathVariable("sessionId") String sessionId) {
		logger.info("Request of eventLog");
		User currentUser = (User) securityService.getPrincipal();
		Patient currentPatient = currentUser.getCurrentPatient();
		EventLog response = alertService.eventLog(currentUser.getId(), String.valueOf(currentPatient.getUserId()),
				sessionId, "0", currentUser.getLanguage().getLanguageKey(), currentUser.getUserType(), msgSrc, locale);
		// return notificationAlert with the alert now closed
		model.addAttribute("events", response);
		model.addAttribute("currentUserId", currentUser.getId());
		return "eventsLog";
	}

	/**
	 * Alert Closing closes an alert session
	 * 
	 * @param model
	 * @param locale
	 * @param alertSessionId
	 * @return
	 */
	@RequestMapping(value = "/alert/{alertId}/close", method = RequestMethod.POST, produces = "application/json")
	public String ShowNotClose(HttpServletRequest request, ModelMap model, Locale locale,
			final RedirectAttributes redirectAttributes, @PathVariable("alertId") String alertId,
			@RequestParam(value = "sessionId", required = true) String sessionId,
			@RequestParam(value = "smsgTxt", required = true) String smsgTxt,
			@RequestParam(value = "smsgReason", required = true) String smsgReason,
			@RequestParam(value = "smsgDetails", required = true) String smsgDetails) {
		logger.info("Request of close");
		User currentUser = (User) securityService.getPrincipal();
		Patient currentPatient = currentUser.getCurrentPatient();
		ResponseStatus responseStatus;
		String redirect;

		int accountNumber = Integer.parseInt(currentPatient.getInstallation().getPanel().getAccount());

		int index = smsgReason.indexOf("-");
		String reasonCodeString = smsgReason.substring(0, index);
		String closeReasonName = smsgReason.substring(index + 1);
		int closeReasonCode = Integer.parseInt(reasonCodeString);

		String handlingConclusionName = smsgTxt;
		String handlingDescription = smsgDetails;

		int sessionID = Integer.parseInt(sessionId);

		responseStatus = alertService.closeAlertSession(accountNumber, closeReasonCode, closeReasonName,
				handlingConclusionName, handlingDescription, sessionID);

		if (responseStatus.isOK()) {
			redirect = "redirect:/admin/history/" + String.valueOf(currentPatient.getUserId());
			return redirect;
		} else {
			String errorMessage = "err.genericServerError";
			if ( (responseStatus.getNumErr() == 167) || (responseStatus.getNumErr() != 167)) {
				errorMessage = "xss.error";
			}
			return HelperController.redirectWithFlashAttributes(redirectAttributes,
					"redirect:/admin/history/" + String.valueOf(currentPatient.getUserId()), errorMessage,
					"err.popupTitle",responseStatus.getMessageErr(), true);
		}
	}

	/**
	 * Weekly Report
	 * 
	 * @param residentId
	 * @param date
	 * @param model
	 * @return
	 * @throws ParseException
	 */
	@RequestMapping(value = "report/{residentId}/{date}", method = RequestMethod.GET)
	public String ShowReport(@PathVariable("residentId") String residentId, @PathVariable("date") String date,
			Model model) throws ParseException {

		Date parsedDate = new SimpleDateFormat("yyyyMMdd").parse(date);

		ArrayList<List<Activity>> weekly = new ArrayList<List<Activity>>();

		/////////////////////
		Calendar cal = Calendar.getInstance();
		cal.setTime(parsedDate);
		Date reportDay = cal.getTime();
		// cal.add(Calendar.DATE, -1);
		// Date initDay = cal.getTime();
		////////////////////

		// Date reportDay = new Date(parsedDate.getTime());
		Date initDay = new Date(parsedDate.getTime());

		// The array of data for the top four graphics
		int[][] graphData = new int[4][7];
		Date[] dates = new Date[7];

		// Repeat this for seven days of report
		cal.add(Calendar.DATE, 1); // So the loop can start in the report date
		for (int i = 1; i < 8; i++) {
			cal.add(Calendar.DATE, -1);
			reportDay = cal.getTime();
			// reportDay = new Date(parsedDate.getTime()-24*60*60*1000*i);
			dates[i - 1] = reportDay;
			logger.info("**************** Date Time at Controller: {}",
					new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format(reportDay));
			List<Event> events = patientService.getDayStory(residentId, reportDay, true);
			List<Activity> activities = new ArrayList<Activity>();
			List<Alert> alerts = new ArrayList<Alert>();
			if ((events != null) && (events.size() != 0)) {
				for (Event e : events) {
					if (Activity.class.isAssignableFrom(e.getClass())) {
						Activity act = (Activity) e;
						activities.add(act);
						long activityTime  = (act.getEndDateTime().getTime() - act.getStartDateTime().getTime());
						int activityTimePercentOf12hScale = (int) (activityTime / 432000); // put directly the % of 12h scale
						switch (act.getType()) {
						case FRONT_DOOR: // Not at home-> graph 0
							graphData[0][i - 1] += activityTimePercentOf12hScale;
							break;
						case TOILET_ROOM_SENSOR: // RestRoom -> graph 1
							graphData[1][i - 1] += activityTimePercentOf12hScale;
							break;
						case FRIDGE_DOOR: // Meals -> graph 4
							// case DINING_ROOM:
							graphData[3][i - 1] += activityTimePercentOf12hScale;
							break;
						case BEDROOM_SENSOR: // Sleep -> graph 3
							graphData[2][i - 1] += activityTimePercentOf12hScale;
							break;
						default:
							break;
						}
					} else {
						alerts.add((Alert) e);
					}
				}

				if (activities != null)
					weekly.add(activities);

				logger.info("Requesting dayStory datePicker {}", weekly.size());

				// model.addAttribute("activities",activities);
				// model.addAttribute("alerts",alerts);

			} else {
				logger.info("activities not");
				weekly.add(null);
			}

		}
		logger.info("activities not---------------------------->{}", weekly.size());
		Patient patientInfo = patientService.getPatientReport(residentId);
		SystemUser resident = userService.getUser(residentId);
		model.addAttribute("index", weekly.size());
		model.addAttribute("patientInfo", patientInfo);
		model.addAttribute("resident", resident);
		model.addAttribute("types", VIEWABLA_ACTIVITY_TYPES_REPORT);
		model.addAttribute("graphs", graphData);
		model.addAttribute("dates", dates);
		model.addAttribute("initDay", initDay);
		model.addAttribute("weekly", weekly);
		model.addAttribute("date", parsedDate);

		if (HelperController.isAjax()) {
			return "adminReportPage";
		} else {
			return "adminReport";
		}
	}

	@RequestMapping(value = "/report/{residentId}", method = RequestMethod.GET)
	public String ShowCurrentReport(@PathVariable("residentId") String residentId, Model model) throws ParseException {

		Patient resident = (Patient) userService.getUser(residentId);

		SimpleDateFormat originalFormat = new SimpleDateFormat(Util.DATETIME_TMZ_FORMAT_INPUT, Locale.JAPAN);
		SimpleDateFormat targetFormat = new SimpleDateFormat(Util.DATEFORMAT_OUTPUT_PLAIN);
		Date date = originalFormat.parse(resident.getPanelTime());
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		cal.add(Calendar.DATE, -1);
		date = cal.getTime();
		String formattedDate = targetFormat.format(date);

		return "redirect:/admin/report/" + residentId + "/" + formattedDate;
	}

	/**
	 * Monthly Report
	 * 
	 * @param residentId
	 * @param date
	 * @param model
	 * @return
	 * @throws ParseException
	 */
	@RequestMapping(value = "monthly/{residentId}/{date}", method = RequestMethod.GET)
	public String ShowMonthlyReport(@PathVariable("residentId") String residentId, @PathVariable("date") String date,
			Model model) throws ParseException {

		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
		Date toDate = sdf.parse(date);

		SystemUser resident = userService.getUser(residentId);

		model.addAttribute("date", toDate);
		model.addAttribute("resident", resident);

		return "adminMonthly";
	}

	/**
	 * Monthly Report
	 * 
	 * @param residentId
	 * @param date
	 * @param model
	 * @return
	 * @throws ParseException
	 */
	@RequestMapping(value = "monthly/{residentId}/{date}.json", method = RequestMethod.GET, produces = "application/json")
	public @ResponseBody List<ActivityIndex> getMonthlyReport(@PathVariable("residentId") String residentId,
			@PathVariable("date") String date, Model model) throws ParseException {

		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
		Date toDate = sdf.parse(date);
		Calendar cal = Calendar.getInstance();
		cal.setTime(toDate);
		cal.add(Calendar.DAY_OF_MONTH, -30);
		Date fromDate = cal.getTime();

		SystemUser resident = userService.getUser(residentId);
		List<ActivityIndex> monthlyReportData = patientService.getMonthlyReport(residentId, fromDate, toDate);

		model.addAttribute("monthlyReportData", monthlyReportData);
		model.addAttribute("fromDate", fromDate);
		model.addAttribute("toDate", toDate);
		model.addAttribute("resident", resident);

		return monthlyReportData;
	}

	/**************************************************************************/
	/************************* ADMIN USERS ************************************/
	/**************************************************************************/

	/**
	 * Show Administration user list
	 * 
	 * @param model
	 * @param locale
	 * @return
	 */
	@RequestMapping(value = "users", method = RequestMethod.GET)
	public String showUsers_ini(ModelMap model, Locale locale,
			@RequestParam(value = "userTypes[]", required = false) UserType[] type,
			@RequestParam(value = "panelTypeFilter[]", required = false) String[] pTypeFilter,
			@RequestParam(value = "serviceTypeFilter[]", required = false) ServiceType[] sTypeFilter,
			@RequestParam(value = "q", required = false) String q,
			@RequestParam(value = "qcriteria", required = false) String qcriteria,
			@RequestParam(value = "page", required = false, defaultValue = "0") String page,
			@RequestParam(value = "sort", required = false) String sort,
			@RequestParam(value = "asc", required = false) Boolean asc,
			@RequestParam(value = "enableActiveService[]", required = false) final Boolean[] enableActiveService) {

		User currentUser = (User) securityService.getPrincipal();
		List<SystemUser> listSystemUsers;
		// App Rule: Service Type filtering does not accept more than one value
		Boolean enableActiveServiceFilter = (enableActiveService != null && enableActiveService.length == 1)
				? enableActiveService[0]
				: null;

		// if (q!=null)
		// logger.info("Hebreo...........................................{}",q);
		if (currentUser.isAdmin()) {
			if (q != null && !q.isEmpty() && qcriteria != null && !qcriteria.equals("") && !q.equals("")) {

				// TODO: sorting parameters to each getSystemUsers func
				if (type == null) {
					listSystemUsers = userService.getSystemUsers(currentUser.getId(),
							String.valueOf(currentUser.getUserType().getId()), this.getVendorId(), RESULT_SIZE, page,
							null, qcriteria, q, sort, asc, pTypeFilter, sTypeFilter, enableActiveServiceFilter);
				} else {
					String rolFilter = String.valueOf(type[0].getId());
					for (int i = 1; i < type.length; i++) {
						rolFilter = rolFilter + "," + String.valueOf(type[i].getId());
					}
					listSystemUsers = userService.getSystemUsers(currentUser.getId(),
							String.valueOf(currentUser.getUserType().getId()), this.getVendorId(), RESULT_SIZE, page,
							rolFilter, qcriteria, q, sort, asc, pTypeFilter, sTypeFilter, enableActiveServiceFilter);
				}

			} else {

				if (type == null) {
					listSystemUsers = userService.getSystemUsers(currentUser.getId(),
							String.valueOf(currentUser.getUserType().getId()), this.getVendorId(), RESULT_SIZE, page,
							null, null, null, sort, asc, pTypeFilter, sTypeFilter, enableActiveServiceFilter);
				} else {
					String rolFilter = String.valueOf(type[0].getId());
					for (int i = 1; i < type.length; i++) {
						rolFilter = rolFilter + "," + String.valueOf(type[i].getId());
					}
					listSystemUsers = userService.getSystemUsers(currentUser.getId(),
							String.valueOf(currentUser.getUserType().getId()), this.getVendorId(), RESULT_SIZE, page,
							rolFilter, null, null, sort, asc, pTypeFilter, sTypeFilter, enableActiveServiceFilter);
				}

			}
		} else { // caregiver
			listSystemUsers = userService.getSystemUsersForCaregiver(qcriteria, q, sort, asc, type, sTypeFilter,
					enableActiveServiceFilter);
		}

		// views variables
		model.addAttribute("user_types", VIEWABLE_USER_TYPES);
		model.addAttribute("selected_types", type);
		model.addAttribute("list_system_users", listSystemUsers);
		model.addAttribute("q", q);
		model.addAttribute("qcriteria", qcriteria);
		model.addAttribute("sort", sort);
		model.addAttribute("asc", (asc != null) ? asc : true);
		model.addAttribute("enableActiveService", enableActiveService);

		if (HelperController.isAjax()) {
			return "adminListUsers";
		} else {
			return "adminUsers";
		}
	}

	/**
	 * Show Administration user list
	 * 
	 * @param model
	 * @param locale
	 * @return
	 */
	@RequestMapping(value = "search_users", method = RequestMethod.POST)
	public String showUsers(ModelMap model, Locale locale,
			@RequestParam(value = "userTypes[]", required = false) UserType[] type,
			@RequestParam(value = "panelTypeFilter[]", required = false) String[] pTypeFilter,
			@RequestParam(value = "serviceTypeFilter[]", required = false) ServiceType[] sTypeFilter,
			@RequestParam(value = "q", required = false) String q,
			@RequestParam(value = "qcriteria", required = false) String qcriteria,
			@RequestParam(value = "page", required = false, defaultValue = "0") String page,
			@RequestParam(required = false) final String sort, @RequestParam(required = false) final Boolean asc,
			@RequestParam(value = "enableActiveService[]", required = false) final Boolean[] enableActiveService) {

		User currentUser = (User) securityService.getPrincipal();
		List<SystemUser> listSystemUsers;

		// App Rule: Service Type filtering does not accept more than one value
		Boolean enableActiveServiceFilter = (enableActiveService != null && enableActiveService.length == 1)
				? enableActiveService[0]
				: null;

		if (currentUser.isAdmin()) {
			if (q != null && q.trim().length() > 0 && qcriteria != null && !qcriteria.equals("") && !q.equals("")) {

				if (type == null) {
					listSystemUsers = userService.getSystemUsers(currentUser.getId(),
							String.valueOf(currentUser.getUserType().getId()), this.getVendorId(), RESULT_SIZE, page,
							null, qcriteria, q, sort, asc, pTypeFilter, sTypeFilter, enableActiveServiceFilter);
				} else {
					String rolFilter = String.valueOf(type[0].getId());
					for (int i = 1; i < type.length; i++) {
						rolFilter = rolFilter + "," + String.valueOf(type[i].getId());
					}
					listSystemUsers = userService.getSystemUsers(currentUser.getId(),
							String.valueOf(currentUser.getUserType().getId()), this.getVendorId(), RESULT_SIZE, page,
							rolFilter, qcriteria, q, sort, asc, pTypeFilter, sTypeFilter, enableActiveServiceFilter);
				}

			} else {

				if (type == null) {
					listSystemUsers = userService.getSystemUsers(currentUser.getId(),
							String.valueOf(currentUser.getUserType().getId()), this.getVendorId(), RESULT_SIZE, page,
							null, null, null, sort, asc, pTypeFilter, sTypeFilter, enableActiveServiceFilter);
				} else {
					String rolFilter = String.valueOf(type[0].getId());
					for (int i = 1; i < type.length; i++) {
						rolFilter = rolFilter + "," + String.valueOf(type[i].getId());
					}
					listSystemUsers = userService.getSystemUsers(currentUser.getId(),
							String.valueOf(currentUser.getUserType().getId()), this.getVendorId(), RESULT_SIZE, page,
							rolFilter, null, null, sort, asc, pTypeFilter, sTypeFilter, enableActiveServiceFilter);
				}

			}
		} else { // caregiver
			listSystemUsers = userService.getSystemUsersForCaregiver(qcriteria, q, sort, asc, type, sTypeFilter,
					enableActiveServiceFilter);
		}

		// views variables
		model.addAttribute("user_types", VIEWABLE_USER_TYPES);
		model.addAttribute("selected_types", type);
		model.addAttribute("panel_type_filter", pTypeFilter);
		model.addAttribute("service_type_filter", sTypeFilter);
		model.addAttribute("list_system_users", listSystemUsers);
		model.addAttribute("q", q);
		model.addAttribute("qcriteria", qcriteria);
		model.addAttribute("sort", sort);
		model.addAttribute("asc", (asc != null) ? asc : true);
		model.addAttribute("enableActiveService", enableActiveService);

		if (HelperController.isAjax()) {
			return "adminListUsers";
		} else {
			return "adminUsers";
		}
	}

	/**
	 * Show Administration user list
	 * 
	 * @param model
	 * @param locale
	 * @return
	 */
	@RequestMapping(value = "search_users", method = RequestMethod.GET)
	public String showUsersLanguage(ModelMap model, Locale locale,
			@RequestParam(value = "userTypes[]", required = false) UserType[] type,
			@RequestParam(value = "panelTypeFilter[]", required = false) String[] pTypeFilter,
			@RequestParam(value = "serviceTypeFilter[]", required = false) ServiceType[] sTypeFilter,
			@RequestParam(value = "q", required = false) String q,
			@RequestParam(value = "qcriteria", required = false) String qcriteria,
			@RequestParam(value = "page", required = false, defaultValue = "0") String page,
			@RequestParam(required = false) final String sort, @RequestParam(required = false) final Boolean asc,
			@RequestParam(value = "enableActiveService[]", required = false) final Boolean[] enableActiveService) {

		User currentUser = (User) securityService.getPrincipal();
		List<SystemUser> listSystemUsers;
		// App Rule: Service Type filtering does not accept more than one value
		Boolean enableActiveServiceFilter = (enableActiveService != null && enableActiveService.length == 1)
				? enableActiveService[0]
				: null;

		if (currentUser.isAdmin()) {
			if (q != null && q.trim().length() > 0 && qcriteria != null && !qcriteria.equals("") && !q.equals("")) {

				if (type == null) {
					listSystemUsers = userService.getSystemUsers(currentUser.getId(),
							String.valueOf(currentUser.getUserType().getId()), this.getVendorId(), RESULT_SIZE, page,
							null, qcriteria, q, sort, asc, pTypeFilter, sTypeFilter, enableActiveServiceFilter);
				} else {
					String rolFilter = String.valueOf(type[0].getId());
					for (int i = 1; i < type.length; i++) {
						rolFilter = rolFilter + "," + String.valueOf(type[i].getId());
					}
					listSystemUsers = userService.getSystemUsers(currentUser.getId(),
							String.valueOf(currentUser.getUserType().getId()), this.getVendorId(), RESULT_SIZE, page,
							rolFilter, qcriteria, q, sort, asc, pTypeFilter, sTypeFilter, enableActiveServiceFilter);
				}

			} else {

				if (type == null) {
					listSystemUsers = userService.getSystemUsers(currentUser.getId(),
							String.valueOf(currentUser.getUserType().getId()), this.getVendorId(), RESULT_SIZE, page,
							null, null, null, sort, asc, pTypeFilter, sTypeFilter, enableActiveServiceFilter);
				} else {
					String rolFilter = String.valueOf(type[0].getId());
					for (int i = 1; i < type.length; i++) {
						rolFilter = rolFilter + "," + String.valueOf(type[i].getId());
					}
					listSystemUsers = userService.getSystemUsers(currentUser.getId(),
							String.valueOf(currentUser.getUserType().getId()), this.getVendorId(), RESULT_SIZE, page,
							rolFilter, null, null, sort, asc, pTypeFilter, sTypeFilter, enableActiveServiceFilter);
				}

			}
		} else { // caregiver
			listSystemUsers = userService.getSystemUsersForCaregiver(qcriteria, q, sort, asc, type, sTypeFilter,
					enableActiveServiceFilter);
		}

		// views variables
		model.addAttribute("user_types", VIEWABLE_USER_TYPES);
		model.addAttribute("selected_types", type);
		model.addAttribute("panel_type_filter", pTypeFilter);
		model.addAttribute("service_type_filter", sTypeFilter);
		model.addAttribute("list_system_users", listSystemUsers);
		model.addAttribute("q", q);
		model.addAttribute("qcriteria", qcriteria);
		model.addAttribute("sort", sort);
		model.addAttribute("asc", (asc != null) ? asc : true);
		model.addAttribute("enableActiveService", enableActiveService);

		if (HelperController.isAjax()) {
			return "adminListUsers";
		} else {
			return "adminUsers";
		}
	}

	/**
	 * New user form
	 * 
	 * @param model
	 * @param locale
	 * @return
	 */
	@RequestMapping(value = "users/new", method = RequestMethod.GET)
	public String showNewUser(ModelMap model, Locale locale) {

		// Gets the free panels
		User currentUser = (User) securityService.getPrincipal();
		ServiceType[] serviceTypes = currentUser.getVendor().getServiceTypes();

		// TODO: So far 1 passwordPolicy per HSP, future pp per userType
		PasswordPolicy pp = passwordPolicyService.getPasswordPolicy();

		List<ServicePackageInformation> servicePackages = currentUser.getVendor()
				.getConfigSettings().getServicePackages();
		Set<String> panelTypesAll = new HashSet<>();
		Set<ServiceType> serviceTypesAll = new HashSet<>();
		for (ServicePackageInformation servicePackage : servicePackages) {
			if (servicePackage.isEnabledForNewAccount()) {
				String stName = Util.mapFromNewToOldServicePackageNaming(servicePackage.getCodeName());
				ServiceType st = ServiceType.fromString(stName);
				serviceTypesAll.add(st);
				for (ControlPanelAssociation controlPanelAssociation : servicePackage.getControlPanelAssociations()) {
					String panelType = controlPanelAssociation.getPanelDeviceType();
					panelTypesAll.add(panelType);
				}
			}
		}

		List<FreePanel> freePanels = new ArrayList<>();
		for (ServiceType serviceType : serviceTypes) {
			for (String panelType : panelTypesAll) {
				freePanels.addAll(userService.getFreePanels(new ServiceType[] {serviceType}, new String[] {panelType}));
			}
		}
		
		// views variables
		model.addAttribute("user_types", VIEWABLE_USER_TYPES);
		model.addAttribute("user_gender", VIEWABLE_USER_GENDER);
		model.addAttribute("free_panels", freePanels);
		model.addAttribute("list_languages", userService.getLanguages());
		model.addAttribute("list_timeZones", securityService.getPrincipal().getVendor().getTimeZones());
		model.addAttribute("password_policy", pp);
		model.addAttribute("panel_types_all", panelTypesAll);
		model.addAttribute("service_types_all", serviceTypesAll);
		return "adminNewUser";
	}

	/**
	 * Create new user
	 * 
	 * @param model
	 * @param locale
	 * @return
	 * @throws ParseException
	 */
	@RequestMapping(value = "users", method = RequestMethod.POST)
	public String createUser(ModelMap model, Locale locale, final RedirectAttributes redirectAttributes,
			@RequestParam Map<String, String> allRequestParams,
			@RequestParam(value = "user[redirectURL]", required = false) String redirectURL) throws ParseException {

		// Gets the free panels
		User currentUser = (User) securityService.getPrincipal();
		ServiceType[] serviceTypes = currentUser.getVendor().getServiceTypes();
		
		List<ServicePackageInformation> servicePackages = currentUser.getVendor()
				.getConfigSettings().getServicePackages();
		Set<String> panelTypesAll = new HashSet<>();
		Set<ServiceType> serviceTypesAll = new HashSet<>();
		for (ServicePackageInformation servicePackage : servicePackages) {
			if (servicePackage.isEnabledForNewAccount()) {
				String stName = Util.mapFromNewToOldServicePackageNaming(servicePackage.getCodeName());
				ServiceType st = ServiceType.fromString(stName);
				serviceTypesAll.add(st);
				for (ControlPanelAssociation controlPanelAssociation : servicePackage.getControlPanelAssociations()) {
					String panelType = controlPanelAssociation.getPanelDeviceType();
					panelTypesAll.add(panelType);
				}
			}
		}
		
		List<FreePanel> freePanels = new ArrayList<FreePanel>();
		for (ServiceType serviceType : serviceTypes) {
			for (String panelType : panelTypesAll) {
				freePanels.addAll(userService.getFreePanels(new ServiceType[] {serviceType}, new String[] {panelType}));
			}
		}

		SystemUser newUser = null;
		PasswordPolicy pp = passwordPolicyService.getPasswordPolicy();
		boolean isMonitored = false;

		if (allRequestParams.containsKey("user[role]")) {
			UserType userType = UserType.valueOf(allRequestParams.get("user[role]"));
			if (userType == UserType.ROLE_MONITORED) {
				newUser = new Patient();
				isMonitored = true;
			} else {
				newUser = new User();
			}
			newUser.setUserType(userType);
		}

		this.fillUserData(newUser, allRequestParams, locale);

		// EIC15-2298: To prevent login/password sent for residents
		// because of cache in IE/Firefox we assure that
		// login/password fields are delete if they don´t make sense.
		if (isMonitored == true) {
			newUser.setNick(null);
			newUser.setPasswd("");
		}

		newUser.setVendorId(Integer.parseInt(this.getVendorId()));

		// Checking the fields
		// if (newUser.checkAttibutes())
		// return errorResponseView(model, "admin.users.create.success.message",
		// "admin.users.create.title");
		ResponseStatus status = userService.addUser(newUser);

		// ResponseStatus status = userService.AlertConfigSave("1", "1", null);

		if (!status.isOK()) {

			if (status.getNumErr() == 112) {
				logger.debug("Att: the password did not meet with policy");
			}
			if (status.getNumErr() == 173) {
				logger.debug("TeleHealth service is not supported");
			}
			model.addAttribute("numErr", status.getNumErr());
			model.addAttribute("user_types", VIEWABLE_USER_TYPES);
			model.addAttribute("user_gender", VIEWABLE_USER_GENDER);
			model.addAttribute("free_panels", freePanels);
			model.addAttribute("list_languages", userService.getLanguages());
			model.addAttribute("list_timeZones", securityService.getPrincipal().getVendor().getTimeZones());
			model.addAttribute("flashTitle", "admin.users.create.title");
			model.addAttribute("flashError", true);
			model.addAttribute("flashMessageCode", myCreateUserErrorHelper.getErrorMessage(status.getNumErr()));			
			model.addAttribute("flashMessageArgument", status.getMessageErr());
			model.addAttribute("newUser", newUser);
			model.addAttribute("password_policy", pp);
			model.addAttribute("panel_types_all", panelTypesAll);
			model.addAttribute("service_types_all", serviceTypesAll);

			return "adminNewUser";
		} else {
			// return successResponseView(model,
			// "admin.users.create.success.message",
			// "admin.users.create.title");
			String redirect;

			if (redirectURL != null) {

				redirect = StringEscapeUtils.escapeHtml("redirect:" + redirectURL.substring(
						redirectURL.indexOf(APPLICATION_NAME) + APPLICATION_NAME.length(), redirectURL.length()));
			} else {
				redirect = "redirect:/admin/users";
			}

			return HelperController.redirectWithFlashAttributes(redirectAttributes, redirect,
					"admin.users.create.success.message", "admin.users.create.title",status.getMessageErr());
		}

	}

	/**
	 * Fills a User instance with request params
	 * 
	 * @param user
	 * @param reqParams
	 */
	private void fillUserData(SystemUser user, Map<String, String> reqParams, Locale locale) {

		if (!Util.isBlank(reqParams.get("user[nick]")))
			user.setNick(reqParams.get("user[nick]"));

		if (!Util.isBlank(reqParams.get("user[password]")))
			user.setPasswd(reqParams.get("user[password]"));

		if (!Util.isBlank(reqParams.get("user[confirmPassword]")))
			user.setPasswdConfirmation(reqParams.get("user[confirmPassword]"));

		// user.setQuestion( reqParams.get("user[question]") );
		// user.setAnswer( reqParams.get("user[answer]") );

		if (!Util.isBlank(reqParams.get("user[name]")))
			user.setFirstName(reqParams.get("user[name]"));

		if (!Util.isBlank(reqParams.get("user[lastName]")))
			user.setLastName(reqParams.get("user[lastName]"));

		if (reqParams.get("user[email]") != null)
			user.setEmail(reqParams.get("user[email]"));

		if (reqParams.get("user[address]") != null)
			user.setAddress(reqParams.get("user[address]"));

		if (reqParams.get("user[generalId]") != null)
			user.setGeneralId(reqParams.get("user[generalId]"));

		if (reqParams.get("user[cellPhone]") != null)
			user.setMobile(reqParams.get("user[cellPhone]"));

		if (reqParams.get("user[phoneAtHome]") != null)
			user.setPhone(reqParams.get("user[phoneAtHome]"));
		
		if (reqParams.get("user[teleHealth]") != null)
			user.setTelehealth(reqParams.get("user[teleHealth]"));
		else
			user.setTelehealth("false");
		
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

		if (!Util.isBlank(reqParams.get("user[serviceType]"))) {
			String st = reqParams.get("user[serviceType]");
			if (st.toLowerCase().contains("pers")) {
				st = ServiceType.PERSE.getName();
			}
			user.setServiceType(ServiceType.fromString(st));
		}

		// Checking if user is a resident
		if (user.getUserType() == UserType.ROLE_MONITORED) {
			if (user.getUserId() == 0) {
				((Patient) user).setInstallation(new Installation());
				((Patient) user).getInstallation().setPanel(new Panel());
				// Language will be the one in use by the user who is creating
				// the resident
				// user.setLanguage(securityService.getPrincipal().getLanguage());
			}

			if (!Util.isBlank(reqParams.get("user[accountNumber]"))) {
				user.setAccountNumber(reqParams.get("user[accountNumber]"));
			}

			PatientSettings patientSettings = new PatientSettings();

			if (!Util.isBlank(reqParams.get("user[activeOutgoingCall]"))) {
				patientSettings.setEnableCall(true);
			} else {
				patientSettings.setEnableCall(false);
			}

			if (!Util.isBlank(reqParams.get("user[activeDefaultPhoneNumber]"))) {
				patientSettings.setPhoneNumberType(ActivePhoneType.DEFAULT.getPhoneType());
			} else {
				patientSettings.setPhoneNumberType(ActivePhoneType.CUSTOM.getPhoneType());
			}

			if (reqParams.get("user[activeEmergencyPhone]") != null) {
				patientSettings.setCustomPhoneNumber(reqParams.get("user[activeEmergencyPhone]"));
			}

			Patient patient = (Patient) user;
			patient.setPatientSettings(patientSettings);

			Panel panel = null;
			if (patient.getInstallation() == null) {
				patient.setInstallation(new Installation());
			}
			if (patient.getInstallation().getPanel() != null) {
				panel = ((Patient) user).getInstallation().getPanel();
			} else {
				panel = new Panel();
			}

			if (!Util.isBlank(reqParams.get("user[simNumber]")))
				panel.setSimNumber(reqParams.get("user[simNumber]"));
			if (!Util.isBlank(reqParams.get("user[landlineNumber]"))) {
				panel.setLandlineNumber(reqParams.get("user[landlineNumber]"));
			}
			if (!Util.isBlank(reqParams.get("user[panel]"))) {
				panel.setAccount(reqParams.get("user[panel]"));
			}
			if (reqParams.get("user[dtmfCode]") != null) {
				panel.setiDTMFCode(reqParams.get("user[dtmfCode]"));
			}
			if (reqParams.get("user[serialNumber]") != null)
				panel.setSerialNumber(reqParams.get("user[serialNumber]"));
			if (reqParams.get("user[serialNumCustomer]") != null) {
				panel.setCustomerSerialNumber(reqParams.get("user[serialNumCustomer]"));
			}
			if (reqParams.get("user[installNotes]") != null)
				panel.setInstallNotes(reqParams.get("user[installNotes]"));
			if (!Util.isBlank(reqParams.get("user[hasPets]")))
				panel.setHasPets(reqParams.get("user[hasPets]").equals("true"));
			else
				panel.setHasPets(false);
			if (reqParams.get("user[timeZone]") != null)
				panel.setTimeZone(reqParams.get("user[timeZone]"));

			if (!Util.isBlank(reqParams.get("user[activeService]"))) {
				panel.setEnableActiveService(reqParams.get("user[activeService]").equals("true"));
			} else {
				panel.setEnableActiveService(false);
			}
			
			if (!Util.isBlank(reqParams.get("user[panelType]"))) {
				panel.setPanelType(reqParams.get("user[panelType]"));
			}
			if (user.getServiceType() != null) {
				panel.setServicePackageCode(user.getServiceType().getName());
			}

			PanelSettings panelSettings = new PanelSettings();

			if (!Util.isBlank(reqParams.get("user[activeAllowResidentOverride]"))) {
				patientSettings.setAllowModifications(true);
				panelSettings.setActiveEmergencyRolesAllowed(
						new String[] { ActiveRolesTypeAllowed.ADMINISTRATOR.getActiveRoleTypeAllowed(),
								ActiveRolesTypeAllowed.CAREGIVER.getActiveRoleTypeAllowed(),
								ActiveRolesTypeAllowed.RESIDENT.getActiveRoleTypeAllowed() });
			} else {
				panelSettings.setActiveEmergencyRolesAllowed(
						new String[] { ActiveRolesTypeAllowed.ADMINISTRATOR.getActiveRoleTypeAllowed() });
			}

			panel.setPanelSettings(panelSettings);

			panel.setServiceProviderAccountNumber(user.getAccountNumber());

			patient.getInstallation().setPanel(panel);
		}

	}

	/**
	 * Edit user form for current user
	 * 
	 * @param model
	 * @param locale
	 * @return
	 */
	@RequestMapping(value = "user/edit", method = RequestMethod.GET)
	public String showEditCurrentUser(ModelMap model, Locale locale) {
		SystemUser user = securityService.getPrincipal();
		model.addAttribute("isCurrentUser", true);
		return showEditUser(model, locale, Integer.toString(user.getUserId()));
	}

	/**
	 * Edit user form
	 * 
	 * @param model
	 * @param locale
	 * @param userId
	 * @return
	 */
	@RequestMapping(value = "users/{id}/edit", method = RequestMethod.GET)
	public String showEditUser(ModelMap model, Locale locale, @PathVariable("id") String userId) {

		PasswordPolicy pp = passwordPolicyService.getPasswordPolicy();
		SystemUser user = userService.getUser(userId);

		model.addAttribute("password_policy", pp);
		buildEditUserModelMap(model, user);

		// model.addAttribute("list_timeZones",
		// securityService.getPrincipal().getVendor().getTimeZones());

		return "adminEditUser";
	}

	private void buildEditUserModelMap(ModelMap model, SystemUser user) {

		/*
		 * Alert Preferences
		 */
		AlertPreferences alertPreferences = user.getAlertPrefs();
		Map<String, Map<String, Boolean>> alertGroupsMap = new LinkedHashMap<String, Map<String, Boolean>>();

		if (user.getUserType() == UserType.ROLE_MONITORED && alertPreferences != null) {

			alertGroupsMap.put("technical_alerts_config", user.getAlertPrefs().getTechnicalAlerts());

			alertGroupsMap.put("emergency_alerts_config", user.getAlertPrefs().getEmergencyAlerts());
			if (!user.getServiceType().equals(ServiceType.HELP_ANYWHERE)) {
				alertGroupsMap.put("safety_alerts_config", user.getAlertPrefs().getSafetyAlerts());
			}

			// 2.5.8 The only HELP_Anywhere or PERSE activity is bGetFall (PERSE: TFS-8699)
			if (user.getServiceType().equals(ServiceType.HELP_ANYWHERE) 
					|| user.getServiceType().equals(ServiceType.PERSE)) {
				final Map<String, Boolean> actAlerts = new HashMap<>();
				for (String key : user.getAlertPrefs().getActivityAlerts().keySet()) {
					if (key.equals("bGetFall")) {
						actAlerts.put(key, user.getAlertPrefs().getActivityAlerts().get(key));
					}
				}
				user.getAlertPrefs().setActivityAlerts(actAlerts);
			}

			alertGroupsMap.put("activity_alerts_config", user.getAlertPrefs().getActivityAlerts());
		

			// For non-Pro service types, show Possible Fall alerts only for users with EPA or FALL_DETECTOR (RFD)
			if (!user.getServiceType().equals(ServiceType.ANALYTICS)) {
				ExternalAPIInstallationDevices deviceList = installationService.DeviceListByExternalAPI(user.getAccountNumber(), Integer.toString(user.getUserId()));
				boolean isEnabledPossibleFallAlerts = false;
				for(ExternalAPIDevice d: deviceList.getDevicesList()) {
					if (d.getActivityType() != null && ("EPA".equals(d.getActivityType().name()) 
							|| "FALL_DETECTOR".equals(d.getActivityType().name()))) {
						isEnabledPossibleFallAlerts = true;
					}
				}
				if (!isEnabledPossibleFallAlerts) {
					user.getAlertPrefs().getActivityAlerts().remove("bGetFall");
				}
			}

			if (!user.getServiceType().equals(ServiceType.PERSE)
					&& !user.getServiceType().equals(ServiceType.HELP_ANYWHERE)) {
				alertGroupsMap.put("security_alerts_config", user.getAlertPrefs().getSecurityAlerts());
				alertGroupsMap.put("advisory_alerts_config", user.getAlertPrefs().getAdvisoryAlerts());
			}

			boolean allMarked = true;
			for (String alertGroupKey : alertGroupsMap.keySet()) {
				logger.info("******************** alertGroupKey: {}", alertGroupKey);
				if (!alertGroupsMap.get(alertGroupKey).isEmpty()) {
					logger.info("alertGroupsMap.get({}): {}", alertGroupKey, alertGroupsMap.get(alertGroupKey));
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
			model.addAttribute("alert_groups", alertGroupsMap);
			model.addAttribute("alert_types_all_marked", allMarked);

		}

		if (user.getUserType() == UserType.ROLE_MONITORED) {

			ServiceType st = (user.getServiceType().equals(ServiceType.PERSE)) ? ServiceType.EXPRESS
					: user.getServiceType();

			// Gets the free panels if resident not HELP_ANY
			if (!user.getServiceType().equals(ServiceType.HELP_ANYWHERE)) {
				model.addAttribute("free_panels",
						userService.getFreePanels(new ServiceType[] { st }, new String[] { "CP" }));
			}

			final Patient patient = (Patient) user;
			if (patient.getInstallation() != null && patient.getInstallation().getPanel() != null) {
				// this.patientService.setEmergencyCallRelatedInfoForPatient(patient);

				model.addAttribute("timeZone_select", ((Patient) user).getInstallation().getPanel().getTimeZone());
			}

			model.addAttribute("list_timeZones", securityService.getPrincipal().getVendor().getTimeZones());
		}

		model.addAttribute("user_gender", VIEWABLE_USER_GENDER);
		model.addAttribute("user_resident_type", UserType.ROLE_MONITORED);
		model.addAttribute("user", user);

		model.addAttribute("list_languages", userService.getLanguages());
		model.addAttribute("language_select", user.getLanguage());

	}

	/**
	 * Update user information for current user
	 * 
	 * @param model
	 * @param locale
	 * @return
	 */
	@RequestMapping(value = "user", method = RequestMethod.POST, produces = "application/json")
	public String updateCurrentUser(ModelMap model, Locale locale, HttpSession session, HttpServletRequest request,
			final RedirectAttributes redirectAttributes, // TODO: this declare
															// the fn to return
															// redirect and
															// refresh the page
			@RequestParam Map<String, String> allRequestParams,
			@RequestParam(value = "user[alert_types][]", required = false) String[] alertTypes) throws ParseException {
		SystemUser user = securityService.getPrincipal();
		return updateUser(model, locale, session, request, redirectAttributes, Integer.toString(user.getUserId()),
				allRequestParams, alertTypes, null);
	}

	/**
	 * Update user information
	 * 
	 * @param model
	 * @param locale
	 * @param userId
	 * @return
	 */
	@RequestMapping(value = { "users/{id}",
			"residents/{id}" }, method = RequestMethod.POST, produces = "application/json")
	// public @ResponseBody FlashJSONResponse<ResponseStatus>
	// updateUser(ModelMap model, Locale locale, // TODO: this declare the fn to
	// return json
	public String updateUser(ModelMap model, Locale locale, HttpSession session, HttpServletRequest request,
			final RedirectAttributes redirectAttributes, // TODO: this declare
															// the fn to return
															// redirect and
															// refresh the page
			@PathVariable("id") String userId, @RequestParam Map<String, String> allRequestParams,
			@RequestParam(value = "user[alert_types][]", required = false) String[] alertTypes,
			@RequestParam(value = "user[redirectURL]", required = false) String redirectURL) throws ParseException {

		User currentUser = (User) securityService.getPrincipal();
		PasswordPolicy pp = passwordPolicyService.getPasswordPolicy();
		SystemUser user = userService.getUser(userId);
		ResponseStatus status = null;

		String redirect;

		if (redirectURL != null) {
			redirect = "redirect:" + redirectURL
					.substring(redirectURL.indexOf(APPLICATION_NAME) + APPLICATION_NAME.length(), redirectURL.length());
		} else if (currentUser.isAdmin()) {
			redirect = "redirect:/admin/users";
		} else {
			redirect = "redirect:/admin/residents";
		}

		this.fillUserData(user, allRequestParams, locale);

		// Checking if user is a resident
		if (user.getUserType() == UserType.ROLE_MONITORED) {
			Map<String, Boolean> listAlertTypes = null;
			if (currentUser.isAdmin()) {
				listAlertTypes = new HashMap<String, Boolean>();
				if (alertTypes != null) {
					for (int i = 0; i < alertTypes.length; i++) {
						listAlertTypes.put(alertTypes[i], true);
					}
				}
			}
			// ResponseStatus status = userService.alertConfigSave(userId,
			// residentId, listAlertTypes);
			status = userService.updatePatient(securityService.getPrincipal().getId(), currentUser, user,
					listAlertTypes);
		} else {
			status = userService.updateUser(user);
		}

		if (!status.isOK()) {
			// pop up
			model.addAttribute("flashTitleCode", "admin.users.update.title");
			model.addAttribute("flashMessageCode", myUpdateUserErrorHelper.getErrorMessage(status.getNumErr()));
			model.addAttribute("flashMessageArgument", status.getMessageErr());
			model.addAttribute("flashError", true);
			model.addAttribute("password_policy", pp);
			buildEditUserModelMap(model, user);
			return "adminEditUser";
		} else {
			// return successResponseView(model,
			// "admin.users.create.success.message",
			// "admin.users.create.title");
			// jsonResponse = new
			// FlashJSONResponse<ResponseStatus>(msgSrc.getMessage("admin.users.create.title",
			// null, locale),
			// msgSrc.getMessage("admin.users.create.success.message", null,
			// locale), status, true);
			if (currentUser.getUserId() == user.getUserId()) {
				Language lang = user.getLanguage();
				currentUser.setLanguage(lang);
				Locale newlocale = new Locale(lang.getLanguageKey().substring(0, 2),
						lang.getLanguageKey().substring(3, 5));
				WebUtils.setSessionAttribute(request, SessionLocaleResolver.LOCALE_SESSION_ATTRIBUTE_NAME, newlocale);
			}
			if (currentUser.getUserId() == user.getUserId())
				return HelperController.redirectWithFlashAttributes(redirectAttributes,
						"redirect:/admin/residents_in_alarm/", "admin.users.edit.success.message",
						"admin.users.update.title",status.getMessageErr());
			else
				return HelperController.redirectWithFlashAttributes(redirectAttributes, redirect,
						"admin.users.edit.success.message", "admin.users.update.title",status.getMessageErr());
		}

		// return jsonResponse;
	}

	/**
	 * Disable user
	 * 
	 * @param model
	 * @param locale
	 * @param userId
	 * @return
	 */
	@RequestMapping(value = "users/{id}/disable/{userRole}", method = RequestMethod.POST)
	public String disableUser(ModelMap model, Locale locale, @PathVariable("id") String userId,
			@PathVariable("userRole") String userRole) {
		logger.info("DISABLING USER");

		try {

			ResponseStatus status;

			if (userRole.equals(UserType.ROLE_MONITORED.toString())) {
				Patient patient = (Patient) userService.getUser(userId);
				status = userService.deleteAccount(patient.getAccountNumber());
			} else {
				status = userService.deleteUserById(userId);
			}

			if (status.getNumErr() < 0) {
				return HelperController.errorResponseView(model, status.getMessageErr(), "admin.users.disable.title");
			} else {
				return HelperController.successResponseView(model, "admin.users.disable.success.message",
						"admin.users.disable.title");
			}

		} catch (Exception e) {
			return HelperController.errorResponseView(model, "admin.users.disable.fail.message",
					"admin.users.disable.title");
		}

	}

	/**
	 * Show password modal form for current user
	 * 
	 * @param model
	 * @param locale
	 * @return
	 */
	@RequestMapping(value = "user/new-password", method = RequestMethod.GET)
	public String showNewPasswordModalCurrentUser(ModelMap model, Locale locale) {
		SystemUser user = securityService.getPrincipal();
		model.addAttribute("isCurrentUser", true);
		return showNewPasswordModal(model, locale, Integer.toString(user.getUserId()));
	}

	/**
	 * Show password modal form
	 * 
	 * @param model
	 * @param locale
	 * @param userId
	 * @return
	 */
	@RequestMapping(value = "users/{id}/new-password", method = RequestMethod.GET)
	public String showNewPasswordModal(ModelMap model, Locale locale, @PathVariable("id") String userId) {

		PasswordPolicy pp = passwordPolicyService.getPasswordPolicy();
		User currentUser = (User) securityService.getPrincipal();

		logger.debug("Current user id " + currentUser.getUserId() + ". Service USer " + userId + ".");

		// views variables

		model.addAttribute("user", userService.getUser(userId));
		model.addAttribute("password_policy", pp);

		if (Integer.parseInt(userId) == currentUser.getUserId())
			model.addAttribute("isCurrentUser", true);
		else
			model.addAttribute("isCurrentUser", false);

		return "adminChangePassword";
	}

	/**
	 * Update password for current user
	 * 
	 * @param model
	 * @param locale
	 * @param newPassword
	 * @param newPasswordConfirmation
	 * @return
	 */
	@RequestMapping(value = "user/new-password", method = RequestMethod.POST)
	public String updatePasswordCurrentUser(ModelMap model, Locale locale,
			@RequestParam(value = "adl_user[current-password]", required = false) String currentPassword,
			@RequestParam(value = "adl_user[new-password]", required = false) String newPassword,
			@RequestParam(value = "adl_user[new-password-confirmation]", required = false) String newPasswordConfirmation) {
		SystemUser user = securityService.getPrincipal();

		logger.info("UPDATE PASSWORD CURRENT USER");

		if (newPassword.equals(newPasswordConfirmation)) {

			try {
				passwordPolicyService.changeMyPassword(currentPassword, newPassword);
				return HelperController.successResponseView(model, "admin.users.edit.chagepassword.success",
						"admin.users.edit.chagepassword.title");
			} catch (NewPasswordInvalidByPolicyException e) {
				return HelperController.errorResponseView(model, "admin.users.error.passwordstrength",
						"admin.users.edit.chagepassword.title");
			} catch (NewPasswordInvalidByHistoryException e) {
				return HelperController.errorResponseView(model, "admin.users.error.passwordhistory",
						"admin.users.edit.chagepassword.title");
			} catch (PasswordInvalidException e) {
				return HelperController.errorResponseView(model, "admin.users.error.passparam_wrong",
						"admin.users.edit.chagepassword.title");
			} catch (AuthenticationException e) {
				// Needs to be rethrown so the exception handler shows de logout screen
				throw e;
			} catch (Exception e) {
				return HelperController.errorResponseView(model, "admin.users.disable.fail.message",
						"admin.users.edit.chagepassword.title");
			}

		}

		return HelperController.errorResponseView(model,
				msgSrc.getMessage("admin.users.edit.chagepassword.not_match", null, locale),
				"admin.users.edit.chagepassword.title");

	}

	/**
	 * Update password
	 * 
	 * @param model
	 * @param locale
	 * @param userId
	 * @param newPassword
	 * @param newPasswordConfirmation
	 * @return
	 */
	@RequestMapping(value = "users/{id}/new-password", method = RequestMethod.POST)
	public String updatePassword(ModelMap model, Locale locale, @PathVariable("id") String userId,
			@RequestParam(value = "adl_user[current-password]", required = false) String currentPassword,
			@RequestParam(value = "adl_user[new-password]", required = false) String newPassword,
			@RequestParam(value = "adl_user[new-password-confirmation]", required = false) String newPasswordConfirmation) {

		model.addAttribute("user", userService.getUser(userId));

		String userName = userService.getUser(userId).getNick();

		if (newPassword.equals(newPasswordConfirmation)) {

			try {
				passwordPolicyService.changeUserPassword(userName, newPasswordConfirmation);
				return HelperController.successResponseView(model, "admin.users.edit.chagepassword.success",
						"admin.users.edit.chagepassword.title");
			} catch (NewPasswordInvalidByPolicyException e) {
				return HelperController.errorResponseView(model, "admin.users.error.passwordstrength",
						"admin.users.edit.chagepassword.title");
			} catch (NewPasswordInvalidByHistoryException e) {
				return HelperController.errorResponseView(model, "admin.users.error.passwordhistory",
						"admin.users.edit.chagepassword.title");
			} catch (PasswordInvalidException e) {
				return HelperController.errorResponseView(model, "admin.users.error.passparam_wrong",
						"admin.users.edit.chagepassword.title");
			} catch (OperationNotAllowedException e) {
				return HelperController.errorResponseView(model, "admin.users.error.operationnotallowed",
						"admin.users.edit.chagepassword.title");
			} catch (AuthenticationException e) {
				// Needs to be rethrown so the exception handler shows de logout screen
				throw e;
			} catch (Exception e) {
				return HelperController.errorResponseView(model, "admin.users.disable.fail.message",
						"admin.users.edit.chagepassword.title");
			}
		}

		return HelperController.errorResponseView(model,
				msgSrc.getMessage("admin.users.edit.chagepassword.not_match", null, locale),
				"admin.users.edit.chagepassword.title");

	}

	/**************************************************************************/
	/************************* END ADMIN USERS ********************************/
	/**************************************************************************/

	/**************************************************************************/
	/**************************** ASSIGNED USERS ******************************/
	/**************************************************************************/

	/**
	 * Set Alert Types Preferences for Admins
	 * 
	 * @param model
	 * @param locale
	 * @param userId
	 * @param alertTypes
	 * @return
	 */
	@RequestMapping(value = "users/{userId}/alertsConf", method = RequestMethod.POST)
	public String setAdminAlertPreferences(ModelMap model, Locale locale, @PathVariable("userId") String userId,
			@RequestParam(value = "user[alert_types][]", required = false) String[] alertTypes) {

		Map<String, Boolean> listAlertTypes = new HashMap<String, Boolean>();
		if (alertTypes != null) {
			for (int i = 0; i < alertTypes.length; i++) {
				listAlertTypes.put(alertTypes[i], true);
			}
		}
		// logger.info("listAlertTypes...........................................{}",listAlertTypes.entrySet());
		ResponseStatus status = userService.setAlertPreferences(Integer.parseInt(userId),
				Integer.parseInt(this.getVendorId()), Integer.parseInt(userId), listAlertTypes);

		if (status.getNumErr() < 0) {
			return HelperController.errorResponseView(model, status.getMessageErr(), "alert.account");
		} else {
			return HelperController.successResponseView(model, "admin.users.alerts.update.success.message",
					"alert.account");
		}
	}

	/**
	 * List Caregivers for a Patient
	 * 
	 * @param model
	 * @param locale
	 * @param userId
	 * @return
	 */
	@RequestMapping(value = "users/{userId}/caregivers", method = RequestMethod.GET)
	public String showListResidentCaregivers(ModelMap model, Locale locale, @PathVariable("userId") String userId,
			@RequestParam(value = "q", required = false) String q,
			@RequestParam(value = "qcriteria", required = false) String qcriteria,
			// @RequestParam(value="only_assigned", required = false) boolean
			// assigned,
			@RequestParam(value = "page", required = false) String page) {
		logger.info("CAREGIVERS CURRENT PAGE: " + page);

		SystemUser user = userService.getUser(userId);
		List<SystemUser> caregivers;
		// if (!assigned){
		// caregivers = patientService.getPatientCaregiversOrdered(userId,
		// this.getVendorId(), RESULT_SIZE, page, true, qcriteria, q, false);
		// }else{
		caregivers = patientService.getPatientCareGivers(userId, this.getVendorId(), RESULT_SIZE, page, qcriteria, q);
		// }
		model.addAttribute("list_caregivers", caregivers);
		model.addAttribute("user", user);

		if (HelperController.isAjax()) {
			return "adminResidentCaregiversList";
		} else {
			return "adminResidentCaregivers";
		}
	}

	/**
	 * Search List Caregivers for a Patient
	 * 
	 * @param model
	 * @param locale
	 * @param userId
	 * @return
	 */
	@RequestMapping(value = "users/{userId}/caregivers_search", method = RequestMethod.POST)
	public String searchListResidentCaregivers(ModelMap model, Locale locale, @PathVariable("userId") String userId,
			@RequestParam(value = "q", required = false) String q,
			@RequestParam(value = "qcriteria", required = false) String qcriteria,
			// @RequestParam(value="only_assigned", required = false) boolean
			// assigned,
			@RequestParam(value = "page", required = false) String page) {
		logger.info("CAREGIVERS CURRENT PAGE: " + q.isEmpty());

		SystemUser user = userService.getUser(userId);
		List<SystemUser> caregivers;
		// if (!assigned){
		// if (!q.isEmpty())
		// caregivers = patientService.getPatientCaregiversOrdered(userId,
		// this.getVendorId(), RESULT_SIZE, page, true, qcriteria, q, false);
		// else
		// caregivers = patientService.getPatientCaregiversOrdered(userId,
		// this.getVendorId(), RESULT_SIZE, page, true, null, q, false);
		// }else{
		if (!q.isEmpty())
			caregivers = patientService.getPatientCareGivers(userId, this.getVendorId(), RESULT_SIZE, page, qcriteria,
					q);
		else
			caregivers = patientService.getPatientCareGivers(userId, this.getVendorId(), RESULT_SIZE, page, null, q);
		// }
		model.addAttribute("list_caregivers", caregivers);
		model.addAttribute("user", user);

		if (HelperController.isAjax()) {
			return "adminResidentCaregiversList";
		} else {
			return "adminResidentCaregivers";
		}
	}

	/**
	 * Search List Caregivers for a Patient
	 * 
	 * @param model
	 * @param locale
	 * @param userId
	 * @return
	 */
	@RequestMapping(value = "users/{userId}/caregivers_search", method = RequestMethod.GET)
	public String searchListResidentCaregiversLanguage(ModelMap model, Locale locale,
			@PathVariable("userId") String userId, @RequestParam(value = "q", required = false) String q,
			@RequestParam(value = "qcriteria", required = false) String qcriteria,
			// @RequestParam(value="only_assigned", required = false) boolean
			// assigned,
			@RequestParam(value = "page", required = false) String page) {
		logger.info("CAREGIVERS CURRENT PAGE: " + page);

		SystemUser user = userService.getUser(userId);
		List<SystemUser> caregivers;
		// if (!assigned){
		// if (!q.isEmpty())
		// caregivers = patientService.getPatientCaregiversOrdered(userId,
		// this.getVendorId(), RESULT_SIZE, page, true, qcriteria, q, false);
		// else
		// caregivers = patientService.getPatientCaregiversOrdered(userId,
		// this.getVendorId(), RESULT_SIZE, page, true, null, q, false);
		// }else{
		// if (!q.isEmpty())
		caregivers = patientService.getPatientCareGivers(userId, this.getVendorId(), RESULT_SIZE, page, qcriteria, q);
		// else
		// caregivers = patientService.getPatientCareGivers(userId,
		// this.getVendorId(), RESULT_SIZE, page, true, null, q, true);
		// }
		model.addAttribute("list_caregivers", caregivers);
		model.addAttribute("user", user);

		if (HelperController.isAjax()) {
			return "adminResidentCaregiversList";
		} else {
			return "adminResidentCaregivers";
		}
	}

	/**
	 * List Patients for a Caregiver
	 * 
	 * @param model
	 * @param locale
	 * @param userId
	 * @return
	 */
	@RequestMapping(value = "users/{userId}/residents", method = RequestMethod.GET)
	public String showListCaregiverResidents(ModelMap model, Locale locale, @PathVariable("userId") String userId,
			@RequestParam(value = "serviceTypeFilter[]", required = false) ServiceType[] sTypeFilter,
			@RequestParam(value = "q", required = false) String q,
			@RequestParam(value = "qcriteria", required = false) String qcriteria,
			// @RequestParam(value="only_assigned", required = false) boolean
			// assigned,
			@RequestParam(required = false) final String sort, @RequestParam(required = false) final Boolean asc,
			@RequestParam(value = "enableActiveService[]", required = false) final Boolean[] enableActiveService,
			@RequestParam(value = "page", required = false) String page) {

		User currentUser = (User) securityService.getPrincipal();
		SystemUser user = userService.getUser(userId);
		List<Patient> patients;
		// App Rule: Service Type filtering does not accept more than one value
		// ServiceType serviceTypeFilter = (sTypeFilter == null || sTypeFilter.length >
		// 1) ? null : sTypeFilter[0];
		Boolean enableActiveServiceFilter = (enableActiveService != null && enableActiveService.length == 1)
				? enableActiveService[0]
				: null;

		// if(currentUser.getUserType() == UserType.ROLE_ADMIN && !assigned) {
		// patients = patientService.getCaregiverPatientsOrdered(userId,
		// this.getVendorId(), RESULT_SIZE, page, true, qcriteria, q);
		// }else {
		patients = patientService.getCareGiverPatients(userId, this.getVendorId(), RESULT_SIZE, page, qcriteria, q,
				sTypeFilter, enableActiveServiceFilter);
		// }

		model.addAttribute("patients", patients);

		model.addAttribute("user", user);

		model.addAttribute("adminBoolean", currentUser.getUserType() == UserType.ROLE_ADMIN);
		model.addAttribute("service_type_filter", sTypeFilter);
		model.addAttribute("sort", sort);
		model.addAttribute("asc", (asc != null) ? asc : true);
		model.addAttribute("enableActiveService", enableActiveService);

		if (HelperController.isAjax()) {
			return "adminCaregiverResidentsList";
		} else {
			return "adminCaregiverResidents";
		}
	}

	/**
	 * Search List Patients for a Caregiver
	 * 
	 * @param model
	 * @param locale
	 * @param userId
	 * @return
	 */
	@RequestMapping(value = "users/{userId}/residents_search", method = RequestMethod.POST)
	public String searchListCaregiverResidents(ModelMap model, Locale locale, @PathVariable("userId") String userId,
			@RequestParam(value = "serviceTypeFilter[]", required = false) ServiceType[] sTypeFilter,
			@RequestParam(value = "q", required = false) String q,
			@RequestParam(value = "qcriteria", required = false) String qcriteria,
			// @RequestParam(value="only_assigned", required = false) boolean
			// assigned,
			@RequestParam(required = false) final String sort, @RequestParam(required = false) final Boolean asc,
			@RequestParam(value = "enableActiveService[]", required = false) final Boolean[] enableActiveService,
			@RequestParam(value = "page", required = false) String page) {

		SystemUser user = userService.getUser(userId);
		List<Patient> patients;
		User currentUser = (User) securityService.getPrincipal();
		// App Rule: Service Type filtering does not accept more than one value
		// ServiceType serviceTypeFilter = (sTypeFilter == null || sTypeFilter.length >
		// 1) ? null : sTypeFilter[0];
		Boolean enableActiveServiceFilter = (enableActiveService != null && enableActiveService.length == 1)
				? enableActiveService[0]
				: null;

		if (q == null || q.isEmpty())
			qcriteria = null;

		// if(currentUser.getUserType() == UserType.ROLE_ADMIN && !assigned) {
		// patients = patientService.getCaregiverPatientsOrdered(userId,
		// this.getVendorId(), RESULT_SIZE, page, true, qcriteria, q);
		// }else {
		patients = patientService.getCareGiverPatients(userId, this.getVendorId(), RESULT_SIZE, page, qcriteria, q,
				sTypeFilter, enableActiveServiceFilter);
		// }

		model.addAttribute("patients", patients);
		model.addAttribute("user", user);
		model.addAttribute("adminBoolean", currentUser.getUserType() == UserType.ROLE_ADMIN);
		model.addAttribute("service_type_filter", sTypeFilter);
		model.addAttribute("q", q);
		model.addAttribute("qcriteria", qcriteria);
		model.addAttribute("sort", sort);
		model.addAttribute("asc", (asc != null) ? asc : true);
		model.addAttribute("enableActiveService", enableActiveService);

		if (HelperController.isAjax()) {
			return "adminCaregiverResidentsList";
		} else {
			return "adminCaregiverResidents";
		}

	}

	/**
	 * Search List Patients for a Caregiver
	 * 
	 * @param model
	 * @param locale
	 * @param userId
	 * @return
	 */
	@RequestMapping(value = "users/{userId}/residents_search", method = RequestMethod.GET)
	public String searchListCaregiverResidentsLanguage(ModelMap model, Locale locale,
			@PathVariable("userId") String userId,
			@RequestParam(value = "serviceTypeFilter[]", required = false) ServiceType[] sTypeFilter,
			@RequestParam(value = "q", required = false) String q,
			@RequestParam(value = "qcriteria", required = false) String qcriteria,
			// @RequestParam(value="only_assigned", required = false) boolean
			// assigned,
			@RequestParam(required = false) final String sort, @RequestParam(required = false) final Boolean asc,
			@RequestParam(value = "enableActiveService[]", required = false) final Boolean[] enableActiveService,
			@RequestParam(value = "page", required = false) String page) {

		SystemUser user = userService.getUser(userId);
		List<Patient> patients;
		// User currentUser = (User) securityService.getPrincipal();
		// App Rule: Service Type filtering does not accept more than one value
		// ServiceType serviceTypeFilter = (sTypeFilter == null || sTypeFilter.length >
		// 1) ? null : sTypeFilter[0];
		Boolean enableActiveServiceFilter = (enableActiveService != null && enableActiveService.length == 1)
				? enableActiveService[0]
				: null;

		// if(currentUser.getUserType() == UserType.ROLE_ADMIN && !assigned) {
		// patients = patientService.getCaregiverPatientsOrdered(userId,
		// this.getVendorId(), RESULT_SIZE, page, true, qcriteria, q);
		// }else {
		patients = patientService.getCareGiverPatients(userId, this.getVendorId(), RESULT_SIZE, page, qcriteria, q,
				sTypeFilter, enableActiveServiceFilter);
		// }

		model.addAttribute("patients", patients);
		model.addAttribute("user", user);
		model.addAttribute("service_type_filter", sTypeFilter);
		model.addAttribute("q", q);
		model.addAttribute("qcriteria", qcriteria);
		model.addAttribute("sort", sort);
		model.addAttribute("asc", (asc != null) ? asc : true);
		model.addAttribute("enableActiveService", enableActiveService);

		if (HelperController.isAjax()) {
			return "adminCaregiverResidentsList";
		} else {
			return "adminCaregiverResidents";
		}

	}

	/**
	 * Resident Alerts show resident configuration alerts form
	 * 
	 * @param model
	 * @param locale
	 * @param userId
	 * @param caregiverId
	 * @return
	 */
	@RequestMapping(value = "users/{userId}/caregivers/{caregiverId}/{userTypeId}/alerts", method = RequestMethod.GET)
	public String caregiverAlerts(ModelMap model, Locale locale, @PathVariable("userId") String userId,
			@PathVariable("caregiverId") String caregiverId, @PathVariable("userTypeId") String userTypeId) {

		SystemUser user = userService.getUser(userId);
		SystemUser caregiver = userService.getUser(caregiverId);

		// view variables
		model.addAttribute("user", user);
		model.addAttribute("caregiver", caregiver);
		model.addAttribute("alert_types", VIEWABLE_ALERT_TYPES);

		return "adminCaregiverAlerts";
	}

	/**************************************************************************/
	/************************** END ASSIGNED USERS ****************************/
	/**************************************************************************/

	/**************************************************************************/
	/*************************
	 * DEVICES INSTALLATION
	 *****************************/
	/**************************************************************************/

	/**
	 * List Devices list devices for user id
	 * 
	 * @param model
	 * @param locale
	 * @return
	 */
	@RequestMapping(value = "users/{residentId}/devices", method = RequestMethod.GET)
	public String ShowListDevices(ModelMap model, Locale locale, @PathVariable("residentId") String userId) {

		Patient currentPatient = securityService.getPrincipal().getCurrentPatient();
		String account = currentPatient.getInstallation().getPanel().getAccount();
		ExternalAPIInstallationDevices devices = installationService.DeviceListByExternalAPI(account, userId);
		AccountInformation accountInformation = userService.getAccountInformation(account);
		
		// view variables
		model.addAttribute("user", currentPatient);
		model.addAttribute("devices", devices.getDevicesList());
		model.addAttribute("isSyncronized", devices.getIsSyncronized());
		model.addAttribute("panelDetails", accountInformation.getPanelDetails());

		model.addAttribute("hasCorrelatedDevOption", installationService.hasCorrelatedDevOpt(currentPatient));

		return "adminListDevices";
	}

	/**
	 * New Device form shows new device form
	 * 
	 * @param model
	 * @param locale
	 * @param userId
	 * @return
	 */
	@RequestMapping(value = "users/{residentId}/devices/new", method = RequestMethod.GET)
	public String newDevice(ModelMap model, Locale locale, @PathVariable("residentId") String userId) {

		List<Integer> freeDevicesAdl = new ArrayList<Integer>();
		List<Integer> freeDevicesSafety = new ArrayList<Integer>();
		List<Device> restRoomDevices = new ArrayList<Device>();
		Set<ActivityType> installableDevices = new TreeSet<ActivityType>();

		InstallationDevices deviceList = installationService.DeviceListByUserId(userId);
		prepareDeviceLists(installableDevices, deviceList.getDevicesList(), freeDevicesAdl, freeDevicesSafety,
				restRoomDevices, null);

		// view variables
		model.addAttribute("activity_types", installableDevices);
		model.addAttribute("user_id", userId);
		model.addAttribute("freeDevicesAdl", freeDevicesAdl);
		model.addAttribute("freeDevicesSafety", freeDevicesSafety);
		model.addAttribute("restRoomDevices", restRoomDevices);
		model.addAttribute("hasCorrelatedDevOption",
				installationService.hasCorrelatedDevOpt(securityService.getPrincipal().getCurrentPatient()));

		return "adminNewDevice";
	}

	/**
	 * Prepares Lists for - free (not already chosen) devices - restroom devices
	 * 
	 * @param deviceList
	 * @param freeDevicesAdl
	 * @param freeDevicesSafety
	 * @param restRoomDevices
	 */
	private void prepareDeviceLists(Set<ActivityType> installableDevices, List<Device> deviceList,
			List<Integer> freeDevicesAdl, List<Integer> freeDevicesSafety, List<Device> restRoomDevices,
			Integer deviceId) {

		/*
		 * Available (installable) Devices depending on the current resident's Service
		 * Type
		 */
		installableDevices
				.addAll(installationService.getInstallableDevices(securityService.getPrincipal().getCurrentPatient()));

		Integer[] roomsAdl = { 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20 };
		Integer[] roomsSafety = { 1, 2, 3, 4, 5, 6, 7, 8, 9, 10 };
		Set<Integer> roomsSetAdl = new HashSet<Integer>(Arrays.asList(roomsAdl));
		Set<Integer> roomsSetSafety = new HashSet<Integer>(Arrays.asList(roomsSafety));
		freeDevicesAdl.addAll(roomsSetAdl);
		freeDevicesSafety.addAll(roomsSetSafety);
		restRoomDevices.addAll(deviceList);

		for (Device d : deviceList) {
			if (d.getFamilyId() == 1 && (deviceId == null || d.getInstallationID() != deviceId)) {
				freeDevicesAdl.remove(new Integer(d.getDevRoomID()));
			} else if (d.getFamilyId() == 2 && (deviceId == null || d.getInstallationID() != deviceId)) {
				freeDevicesSafety.remove(new Integer(d.getDevRoomID()));
			}
			if (d.getActivityTypeID() != ActivityType.TOILET_ROOM_SENSOR) {
				restRoomDevices.remove(d);
			}
			if (d.getCorrelatedDev() != null && (deviceId == null || d.getInstallationID() != deviceId)) {
				restRoomDevices.remove(d.getCorrelatedDev());
			}
		}
		Collections.sort(freeDevicesAdl);
		Collections.sort(freeDevicesSafety);
	}

	/**
	 * Prepares Lists for - free (not already chosen) devices - restroom devices ->
	 * Adapted to 2.5.5 ExternalAPIDevice structure
	 * 
	 * @param deviceList
	 * @param freeDevicesAdl
	 * @param freeDevicesSafety
	 * @param restRoomDevices
	 */
	private void prepareExternalAPIDeviceLists(Set<ActivityType> installableDevices, List<ExternalAPIDevice> deviceList,
			List<Integer> freeDevicesAdl, List<Integer> freeDevicesSafety, List<ExternalAPIDevice> restRoomDevices, 
			List<ExternalAPIDevice> pirDevices, String deviceIdentifier) {

		/*
		 * Available (installable) Devices depending on the current resident's Service
		 * Type
		 */
		installableDevices
				.addAll(installationService.getInstallableDevices(securityService.getPrincipal().getCurrentPatient()));

		Integer[] roomsAdl = { 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20 };
		Integer[] roomsSafety = { 1, 2, 3, 4, 5, 6, 7, 8, 9, 10 };
		Set<Integer> roomsSetAdl = new HashSet<Integer>(Arrays.asList(roomsAdl));
		Set<Integer> roomsSetSafety = new HashSet<Integer>(Arrays.asList(roomsSafety));
		freeDevicesAdl.addAll(roomsSetAdl);
		freeDevicesSafety.addAll(roomsSetSafety);
		restRoomDevices.addAll(deviceList);
		pirDevices.addAll(deviceList);
		String pirDeviceType = "MotionDetector";

		for (ExternalAPIDevice d : deviceList) {
			if (d.getDeviceFamily() == 1
					&& (deviceIdentifier == null || d.getDeviceIdentifier().compareTo(deviceIdentifier) != 0)) {
				freeDevicesAdl.remove(new Integer(d.getDeviceId()));
			} else if (d.getDeviceFamily() == 2
					&& (deviceIdentifier == null || d.getDeviceIdentifier().compareTo(deviceIdentifier) != 0)) {
				freeDevicesSafety.remove(new Integer(d.getDeviceId()));
			}
			if (d.getActivityType() != ActivityType.TOILET_ROOM_SENSOR) {
				restRoomDevices.remove(d);
			}
			if (d.getCorrelatedDev() != null
					&& (deviceIdentifier == null || d.getDeviceIdentifier().compareTo(deviceIdentifier) != 0)) {
				restRoomDevices.remove(d.getCorrelatedDev());
			}
			if (!pirDeviceType.equals(d.getDeviceType())) {
				pirDevices.remove(d);
			}

		}
		Collections.sort(freeDevicesAdl);
		Collections.sort(freeDevicesSafety);
	}

	/**
	 * Create Device add device to user with id
	 * 
	 * @param model
	 * @param locale
	 * @param userId
	 * @return
	 */
	@RequestMapping(value = "users/{residentId}/devices", method = RequestMethod.POST)
	public String createDevice(ModelMap model, Locale locale, final RedirectAttributes redirectAttributes,
			@PathVariable("residentId") int userId,
			@RequestParam(value = "deviceAdl[number]", required = false) String deviceIdAdl,
			@RequestParam(value = "device[number]", required = false) String deviceId,
			@RequestParam("device[activityType]") int activityTypeId,
			@RequestParam(value = "device[description]", required = false) String label,
			@RequestParam(value = "device[ipd]", defaultValue = "0") int isIPD,
			@RequestParam(value = "device[relatedDevID]", defaultValue = "0") int relatedDevID) {

		ResponseStatus status = new ResponseStatus();
		if (deviceIdAdl != "" && deviceIdAdl != null) {
			status = installationService.DeviceAddByUserId(userId, 1, activityTypeId, Integer.valueOf(deviceIdAdl),
					label, (isIPD == 1 ? true : false), relatedDevID);
		} else
			status = installationService.DeviceAddByUserId(userId, 2, activityTypeId, Integer.valueOf(deviceId), label,
					(isIPD == 1 ? true : false), relatedDevID);

		if (status.getNumErr() < 0) {
			
			String statusMsg = "admin.users.disable.fail.message";
			// return errorResponseView(model, status.getMessageErr(),
			// "admin.users.devices.create.title");
			if ((status.getNumErr() == 167) || (status.getNumErr() == -167)) statusMsg = "xss.error";
			
			return HelperController.redirectWithFlashAttributes(redirectAttributes,
					"redirect:/admin/users/" + userId + "/devices", statusMsg,
					"admin.users.devices.create.title",status.getMessageErr(),true);
		} else {
			// return successResponseView(model,
			// "admin.users.devices.create.success.message",
			// "admin.users.devices.create.title");
			return HelperController.redirectWithFlashAttributes(redirectAttributes,
					"redirect:/admin/users/" + userId + "/devices", "admin.users.devices.create.success.message",
					"admin.users.devices.create.title",status.getMessageErr(),false);
		}
	}

	/**
	 * Edit Device form shows edit device form
	 * 
	 * @param model
	 * @param locale
	 * @param userId
	 * @param deviceId
	 * @return
	 */
	@RequestMapping(value = "users/{residentId}/devices/{deviceType}/{deviceIdentifier}/edit", method = RequestMethod.GET)
	public String editDevice(ModelMap model, Locale locale, @PathVariable("residentId") String userId,
			@PathVariable("deviceIdentifier") String deviceIdentifier, @PathVariable("deviceType") int oldDeviceType) {

		Patient currentPatient = securityService.getPrincipal().getCurrentPatient();
		String account = currentPatient.getInstallation().getPanel().getAccount();
		ExternalAPIInstallationDevices iDevices = installationService.DeviceListByExternalAPI(account, userId);

		List<ExternalAPIDevice> deviceList = iDevices.getDevicesList();
		ExternalAPIDevice device = null;

		for (ExternalAPIDevice dev : deviceList) {
			if (dev.getDeviceIdentifier().compareTo(deviceIdentifier) == 0) {
				device = dev;
				break;
			}
		}
		
		// get AssociatedActivitySensor if exists
		String associatedActivitySensorType = "AssociatedActivitySensor";
		ExternalAPIDevice associatedActivitySensorDevice = null;
		for (DeviceAssociation deviceAssociation : device.getDeviceAssociations()) {
			if ((deviceAssociation.getAssociatedDevice() != null) && (associatedActivitySensorType.equals(deviceAssociation.getType()))) {
				associatedActivitySensorDevice = deviceAssociation.getAssociatedDevice();
				break;
			}
		}

		// get helper lists
		List<Integer> freeDevicesAdl = new ArrayList<Integer>();
		List<Integer> freeDevicesSafety = new ArrayList<Integer>();
		List<ExternalAPIDevice> restRoomDevices = new ArrayList<ExternalAPIDevice>();
		List<ExternalAPIDevice> pirDevices = new ArrayList<ExternalAPIDevice>();
		Set<ActivityType> installableDevices = new TreeSet<>();
		prepareExternalAPIDeviceLists(installableDevices, deviceList, freeDevicesAdl, freeDevicesSafety,
				restRoomDevices, pirDevices, deviceIdentifier);

		// view variables
		// ActivityType[] a = new ActivityType[installableDevices.size()];
		model.addAttribute("deviceType", oldDeviceType);
		model.addAttribute("device", device);
		model.addAttribute("activity_types", installableDevices);
		model.addAttribute("user_id", userId);
		model.addAttribute("deviceIdentifier", deviceIdentifier);
		// model.addAttribute("deviceId", deviceId);
		model.addAttribute("freeDevicesAdl", freeDevicesAdl);
		model.addAttribute("freeDevicesSafety", freeDevicesSafety);
		model.addAttribute("restRoomDevices", restRoomDevices);
		model.addAttribute("pirDevices", pirDevices);
		model.addAttribute("associatedActivitySensorDevice", associatedActivitySensorDevice);
		model.addAttribute("isSyncronized", iDevices.getIsSyncronized());
		model.addAttribute("hasCorrelatedDevOption",
				installationService.hasCorrelatedDevOpt(securityService.getPrincipal().getCurrentPatient()));

		return "adminEditDevice";
	}

	/**
	 * Update Device update device with device id and user id
	 * 
	 * @param model
	 * @param locale
	 * @param userId
	 * @param deviceId
	 * @return
	 */
	@RequestMapping(value = "users/{residentId}/devices/{deviceIdentifier}", method = RequestMethod.POST)
	public String updateDevice(ModelMap model, Locale locale, final RedirectAttributes redirectAttributes,
			@PathVariable("residentId") int userId,
			// @PathVariable("deviceType") int oldDeviceType,
			@PathVariable("deviceIdentifier") String deviceIdentifier,
			@RequestParam(value = "deviceAdl[number]", required = false) String deviceIdAdl,
			@RequestParam(value = "device[family]") int devFamily,
			@RequestParam(value = "device[number]", required = false) String newDeviceId,
			@RequestParam(value = "deviceBedSensor[number]", required = false) String deviceBedSensorId,
			@RequestParam("device[activityType]") int activityTypeId,
			@RequestParam(value = "device[description]", required = false) String label,
			@RequestParam(value = "device[ipd]", defaultValue = "0") int isIPD,
			@RequestParam(value = "device[relatedDevID]", defaultValue = "") String relatedDevID,
			@RequestParam(value = "deviceBedSensor[associated]", defaultValue = "") String associatedDeviceIdentifier) {

		ResponseStatus status = new ResponseStatus();
		Integer devId = null;
		Integer devType = null;
		String statusMsg = "admin.users.devices.update.success.message";
		boolean associatedActivitySensorIsPIR = true;
		
		if (!Util.isBlank(deviceIdAdl)) {
			devId = Integer.valueOf(deviceIdAdl);
			devType = 1;
		} else if (!Util.isBlank(deviceBedSensorId)) {
			devId = Integer.valueOf(deviceBedSensorId);
			devType = 1;
		} else if (!Util.isBlank(newDeviceId)) {
			devId = Integer.valueOf(newDeviceId);
			devType = 2;
		}
		
		Boolean isError = false;
		
		if (devId != null) {
			if (activityTypeId == ActivityType.BED_SENSOR.getId()) {
				String associatedActivitySensorType = "AssociatedActivitySensor";
				// in the API there can be more that one sensor assigned, so we need to remove all of them first
				removePreviousAssociatedActivitySensors(userId, deviceIdentifier, associatedActivitySensorType);
				
				status = updateDeviceExternalAPI(deviceIdentifier, associatedDeviceIdentifier, associatedActivitySensorType, label);
				if (!associatedDeviceIdentifier.isEmpty() && (associatedDeviceIdentifier != null)) {
					associatedActivitySensorIsPIR = 
							associatedActivitySensorIsPIR(userId, deviceIdentifier, associatedActivitySensorType);
				}
			} else {
				status = installationService.ExternalAPIDeviceUpdateByUserId(userId, devType, devFamily, devId,
					activityTypeId, label, (isIPD == 1 ? true : false), 0, deviceIdentifier, devType, relatedDevID);
			}
			if (status.getNumErr() != 0) {
				isError = true;
				// Xss attacks handles the error messages on their own way.
				if ((status.getNumErr() == 167) || (status.getNumErr() == -167))
				{
					statusMsg = "xss.error";
				} else  {
					statusMsg = "admin.users.disable.fail.message";
				}
			}
		} else {
			statusMsg = "admin.users.disable.fail.message";
		}
		if (associatedActivitySensorIsPIR) {
			return HelperController.redirectWithFlashAttributes(redirectAttributes,
				"redirect:/admin/users/" + userId + "/devices", statusMsg, "admin.users.devices.update.title","", isError);
		} else {
			statusMsg = "admin.users.devices.update.fail.not_PIR";
			return HelperController.redirectWithFlashAttributes(redirectAttributes,
				"redirect:/admin/users/" + userId + "/devices", statusMsg, "admin.users.devices.update.title","", true);
		}
	}

	/**
	 * Send ExternalAPIDeviceUpdate requests to remove all previous AssociatedActivitySensors
	 * 
	 * @param userId 
	 * @param deviceIdentifier
	 * @param associatedActivitySensorType
	 */
	private void removePreviousAssociatedActivitySensors(Integer userId, String deviceIdentifier, 
			String associatedActivitySensorType) {
		
		Patient currentPatient = securityService.getPrincipal().getCurrentPatient();
		String account = currentPatient.getInstallation().getPanel().getAccount();
		ExternalAPIInstallationDevices iDevices = installationService.DeviceListByExternalAPI(account, userId.toString());
		List<ExternalAPIDevice> deviceList = iDevices.getDevicesList();
		ExternalAPIDevice device = null;

		for (ExternalAPIDevice dev : deviceList) {
			if (dev.getDeviceIdentifier().compareTo(deviceIdentifier) == 0) {
				device = dev;
				break;
			}
		}
		
		for (DeviceAssociation deviceAssociation : device.getDeviceAssociations()) {
			if ((deviceAssociation.getAssociatedDevice() != null) && associatedActivitySensorType.equals(deviceAssociation.getType())) {
				updateDeviceExternalAPI(deviceIdentifier, deviceAssociation.getValue(), null, null);
			}
		}
	}

	/**
	 * Check if the first AssociatedActivitySensor from DeviceAssociations is PIR (deviceType is 'MotionDetector')
	 * 
	 * @param userId 
	 * @param deviceIdentifier
	 * @param associatedActivitySensorType
	 */
	private boolean associatedActivitySensorIsPIR(Integer userId, String deviceIdentifier, String associatedActivitySensorType) {
		
		boolean associatedActivitySensorIsPIR = false;
		String deviceTypePIR = "MotionDetector";
		
		Patient currentPatient = securityService.getPrincipal().getCurrentPatient();
		String account = currentPatient.getInstallation().getPanel().getAccount();
		ExternalAPIInstallationDevices iDevices = installationService.DeviceListByExternalAPI(account, userId.toString());
		List<ExternalAPIDevice> deviceList = iDevices.getDevicesList();
		ExternalAPIDevice device = null;

		for (ExternalAPIDevice dev : deviceList) {
			if (dev.getDeviceIdentifier().compareTo(deviceIdentifier) == 0) {
				device = dev;
				break;
			}
		}
		
		for (DeviceAssociation deviceAssociation : device.getDeviceAssociations()) {
			if ((deviceAssociation.getAssociatedDevice() != null) && associatedActivitySensorType.equals(deviceAssociation.getType())) {
				if (deviceTypePIR.equals(deviceAssociation.getAssociatedDevice().getDeviceType())) {
					associatedActivitySensorIsPIR = true;
				};
				break;
			}
		}
		
		return associatedActivitySensorIsPIR;
	}

	/**
	 * Update device - send ExternalAPIDeviceUpdate request and get response
	 * 
	 * @param deviceIdentifier
	 * @param associatedDeviceIdentifier
	 * @param associatedActivitySensorType
	 * @param label
	 */
	private ResponseStatus updateDeviceExternalAPI(String deviceIdentifier, 
			String associatedDeviceIdentifier, String associatedActivitySensorType, String label) {
		
		Patient currentPatient = securityService.getPrincipal().getCurrentPatient();
		String account = currentPatient.getInstallation().getPanel().getAccount();
		ExternalAPIDeviceUpdateRequest externalAPIDeviceUpdateRequest = new ExternalAPIDeviceUpdateRequest();

		if (!associatedDeviceIdentifier.isEmpty() && (associatedDeviceIdentifier != null)) {
			Map<String, String> associationMap = new HashMap<>();
			associationMap.put("associatedDeviceIdentifier", associatedDeviceIdentifier);
			associationMap.put("associationType", associatedActivitySensorType);
			
			List<Map<String, String>> associations = new ArrayList<>();
			associations.add(associationMap);
			
			externalAPIDeviceUpdateRequest.setAssociations(associations);
		}
		externalAPIDeviceUpdateRequest.setAccount(account);
		externalAPIDeviceUpdateRequest.setDeviceIdentifier(deviceIdentifier);
		externalAPIDeviceUpdateRequest.setLabel(label);
		
		return devicesService.setDeviceRawBody(externalAPIDeviceUpdateRequest);
	}

	/**
	 * Delete Device delete device with device id and user id
	 * 
	 * @param model
	 * @param locale
	 * @param userId
	 * @param deviceId
	 * @return
	 */
	@RequestMapping(value = "users/{residentId}/devices/{deviceId}/{deviceType}/destroy", method = RequestMethod.POST)
	public String destroyDevice(ModelMap model, Locale locale, final RedirectAttributes redirectAttributes,
			@PathVariable("residentId") String userId, @PathVariable("deviceId") int deviceId,
			@PathVariable("deviceType") int deviceType) {
		// installationService.DeviceDeleteByUserId(userId, deviceId)
		ResponseStatus status = installationService.DeviceDeleteByUserId(Integer.parseInt(userId), deviceType,
				deviceId);

		if (status.getNumErr() < 0) {
			// return errorResponseView(model, status.getMessageErr(),
			// "admin.users.devices.create.title");
			return HelperController.redirectWithFlashAttributes(redirectAttributes,
					"redirect:/admin/users/" + userId + "/devices", "admin.users.disable.fail.message",
					"admin.users.devices.delete.title",status.getMessageErr());
		} else {
			// return successResponseView(model,
			// "admin.users.devices.create.success.message",
			// "admin.users.devices.create.title");
			return HelperController.redirectWithFlashAttributes(redirectAttributes,
					"redirect:/admin/users/" + userId + "/devices", "admin.users.devices.delete.success.message",
					"admin.users.devices.delete.title",status.getMessageErr());
		}
	}

	/**
	 * Remove Device for account (only for Umbrella service package)
	 * 
	 * @param model
	 * @param locale
	 * @param redirectAttributes
	 * @param userId
	 * @param account
	 * @param deviceIdentifier
	 * @return
	 */
	@RequestMapping(value = "users/{residentId}/devices/{account}/{deviceIdentifier}/removeFromAccount", method = RequestMethod.POST)
	public String removeDeviceFromAccount(ModelMap model, Locale locale, final RedirectAttributes redirectAttributes,
			@PathVariable("residentId") String userId, @PathVariable("account") String account,
			@PathVariable("deviceIdentifier") String deviceIdentifier) {

		ResponseStatus status = devicesService.ExternalAPIDeviceRemoveFromAccount(account, deviceIdentifier);

		if (status.getNumErr() < 0) {
			return HelperController.redirectWithFlashAttributes(redirectAttributes,
					"redirect:/admin/users/" + userId + "/devices", "admin.users.disable.fail.message",
					"admin.users.devices.remove.title",status.getMessageErr());
		} else if (status.getNumErr() == 0){
			return HelperController.redirectWithFlashAttributes(redirectAttributes,
					"redirect:/admin/users/" + userId + "/devices", "admin.users.devices.remove.success.message",
					"admin.users.devices.remove.title",status.getMessageErr());
		} else if (status.getNumErr() == 30){
			return HelperController.redirectWithFlashAttributes(redirectAttributes,
					"redirect:/admin/users/" + userId + "/devices", "admin.users.devices.remove.fail_030.message",
					"admin.users.devices.remove.title",status.getMessageErr());
		} else if (status.getNumErr() == 31){
			return HelperController.redirectWithFlashAttributes(redirectAttributes,
					"redirect:/admin/users/" + userId + "/devices", "admin.users.devices.remove.fail_031.message",
					"admin.users.devices.remove.title",status.getMessageErr());
		} else if (status.getNumErr() == 89){
			return HelperController.redirectWithFlashAttributes(redirectAttributes,
					"redirect:/admin/users/" + userId + "/devices", "admin.users.devices.remove.fail_089.message",
					"admin.users.devices.remove.title",status.getMessageErr());
		} else if (status.getNumErr() == 115){
			return HelperController.redirectWithFlashAttributes(redirectAttributes,
					"redirect:/admin/users/" + userId + "/devices", "admin.users.devices.remove.fail_115.message",
					"admin.users.devices.remove.title",status.getMessageErr());
		} else if (status.getNumErr() == 123){
			return HelperController.redirectWithFlashAttributes(redirectAttributes,
					"redirect:/admin/users/" + userId + "/devices", "admin.users.devices.remove.fail_123.message",
					"admin.users.devices.remove.title",status.getMessageErr());
		} else {
			return HelperController.redirectWithFlashAttributes(redirectAttributes,
					"redirect:/admin/users/" + userId + "/devices", "admin.users.devices.remove.fail_001.message",
					"admin.users.devices.remove.title",status.getMessageErr());
		}
	}

	/**
	 * List Free Accounts
	 * 
	 * @param model
	 * @param locale
	 * @return
	 */
	@RequestMapping(value = "listFreeAccounts", method = RequestMethod.GET)
	public String ShowFreeAccounts(ModelMap model, Locale locale) {
		installationService.GetFreeAccounts();
		return "adminAdministratorDetail";

	}

	/**
	 * List Alert Types
	 * 
	 * @param model
	 * @param locale
	 * @return
	 */
	@RequestMapping(value = "listAlertTypes", method = RequestMethod.GET)
	public String ShowAlertTypes(ModelMap model, Locale locale) {
		alertService.GetManualAlertTypes();
		return "adminAdministratorDetail";

	}

	/**************************************************************************/
	/*************************
	 * END DEVICES INSTALLATION
	 *************************/
	/**************************************************************************/

	/**
	 * AdministratorDetail partial
	 * 
	 * @param model
	 * @param locale
	 * @return
	 */
	@RequestMapping(value = "administratorDetail", method = RequestMethod.GET)
	public String ShowAdminDetail(ModelMap model, Locale locale) {
		// TODO: call service layer to obtain DY data
		return "adminAdministratorDetail";

	}

	/**
	 * AdministratorMonitoringUsers partial
	 * 
	 * @param model
	 * @param locale
	 * @return
	 */
	@RequestMapping(value = "adminMonitoringUsers", method = RequestMethod.GET)
	public String ShowAdminMonitoringUsers(ModelMap model, Locale locale) {
		// TODO: call service layer to obtain DY data
		return "adminMonitoringUsers";

	}

	/**
	 * AdministratorConfigAlerts partial
	 * 
	 * @param model
	 * @param locale
	 * @return
	 */
	@RequestMapping(value = "adminConfigAlerts", method = RequestMethod.GET)
	public String ShowAdminConfigAlerts(ModelMap model, Locale locale) {
		// TODO: call service layer to obtain DY data
		return "adminConfigAlerts";

	}

	/**
	 * AdministratorConfigCaregivers partial
	 * 
	 * @param model
	 * @param locale
	 * @return
	 */
	@RequestMapping(value = "adminConfigCaregivers", method = RequestMethod.GET)
	public String ShowAdminConfigCaregivers(ModelMap model, Locale locale) {
		// TODO: call service layer to obtain DY data
		return "adminConfigCaregivers";

	}

	/**************************************************************************/
	/*************************
	 * END ADMIN ADL
	 ************************************/
	/**************************************************************************/

	@RequestMapping(value = "new-alert/{residentId}", method = RequestMethod.GET)
	public String showNewAlert(ModelMap model, Locale locale, @PathVariable("residentId") String reidentId) {
		InstallationDevices devices = installationService.DeviceListByUserId(reidentId);

		model.addAttribute("issueTypes", IssueType.values());
		model.addAttribute("deviceNumbers", devices.getDevicesList());

		return "adminNewAlert";
	}

	@RequestMapping(value = "put-alert", method = RequestMethod.POST, produces = "application/json")
	public String putAlert(ModelMap model, Locale locale, final RedirectAttributes redirectAttributes,
			@RequestParam(value = "pIssueType", required = false) String pIssueType,
			@RequestParam(value = "pDeviceNumber", required = false) String pDeviceNumber,
			@RequestParam(value = "pDescription", required = false) String pDescription) {

		Patient resident = securityService.getPrincipal().getCurrentPatient();
		ResponseStatus status = alertService.createManualAlert(resident, pIssueType, pDescription, pDeviceNumber);

		if (status.getNumErr() != 0) {
			String errorMessage = "admin.users.disable.fail.message";
			
			if ((status.getNumErr() == 167) || (status.getNumErr() == -167))
			{
				errorMessage = "xss.error";
			}
			
			return HelperController.redirectWithFlashAttributes(redirectAttributes,
					"redirect:/admin/history/" + securityService.getPrincipal().getCurrentPatient().getUserId(),
					errorMessage, "history.new_alert",status.getMessageErr(),true);
		} else {
			return HelperController.redirectWithFlashAttributes(redirectAttributes,
					"redirect:/admin/history/" + securityService.getPrincipal().getCurrentPatient().getUserId(),
					"new_alert.success", "history.new_alert",status.getMessageErr());
		}
	}

	@RequestMapping(value = "/changeLang", method = RequestMethod.POST)
	public ModelAndView changeLanguage(ModelMap model, HttpSession session, HttpServletRequest request,
			@RequestParam("languageId") int languageId, @RequestParam("currentURL") String currentURL) {
		User currentUser = (User) securityService.getPrincipal();

		if (userService.setUserPreferences(currentUser.getUserId(), currentUser.getVendorId(), languageId)
				.getNumErr() >= 0) {
			currentUser.setLanguage(Language.getLanguage(languageId));
			userService.loadVendorTimeZones(currentUser.getVendor(), languageId);
			Locale locale = new Locale(Language.getLanguage(languageId).getLanguageKey().substring(0, 2),
					Language.getLanguage(languageId).getLanguageKey().substring(3, 5));
			WebUtils.setSessionAttribute(request, SessionLocaleResolver.LOCALE_SESSION_ATTRIBUTE_NAME, locale);
		}
		logger.info("currentURL...........................................{}", currentURL);

		// SET DATE FORMAT IN SESSION VARIABLE
		DateFormatHelper.setSessionDateFormat(languageId, request);
		currentURL = StringEscapeUtils.escapeHtml4(currentURL);

		return new ModelAndView("redirect:" + currentURL);
	}

	@RequestMapping(value = "changePassword", method = RequestMethod.GET)
	public String showChangePassword(ModelMap model, HttpServletRequest request,
			@RequestParam(value = "logout", required = false) String logout) {
		User currentUser = (User) securityService.getPrincipal();

		// TODO: So far 1 passwordPolicy per HSP, future pp per userType
		PasswordPolicy pp = passwordPolicyService.getPasswordPolicy();

		model.addAttribute("isLogged", true);
		model.addAttribute("userName", currentUser.getUsername());

		try {
			pp.setMaximumLength(pp.getMaximumLength());
			pp.setMinimumLength(pp.getMinimumLength());
			pp.setCharGroupsMinimum(pp.getCharGroupsMinimum());

		} catch (Exception e) {

		}

		model.addAttribute("password_policy", pp);

		return "changePassword";
	}

	@RequestMapping(value = "changePassword", method = RequestMethod.POST, produces = "application/json")
	public @ResponseBody ChangePassResponse processChangePassword(ModelMap model, HttpServletRequest request,
			@RequestParam(value = "username") String username,
			@RequestParam(value = "currentPassword") String currentPassword,
			@RequestParam(value = "newPassword") String newPassword) {

		ChangePassResponse response = new ChangePassResponse(-1, request.getContextPath(), "validations.servererror",
				true);

		try {
			passwordPolicyService.changeMyPassword(currentPassword, newPassword);
			response.retCode = 0;
			response.message = "validations.passwordchanged";

			// OMAR -> Autologin.
		} catch (NewPasswordInvalidByPolicyException e) {
			response.retCode = 2;
			// xv.2.5.1 -> These texts are keys for javascript client errors
			// (app-en_LANG.js)
			response.message = "validations.passwordstrength";
		} catch (NewPasswordInvalidByHistoryException e) {
			response.retCode = 3;
			response.message = "validations.passwordhistory";
		} catch (PasswordInvalidException e) {
			response.retCode = 4;

			response.message = "validations.passparam_wrong";
		} catch (Throwable t) {
			response.retCode = 1;
			response.message = "validations.servererror";
		}
		return response;
	}

	@ExceptionHandler(IOException.class)
	public void handleSocketException(Exception ex, HttpServletRequest request) throws Exception {
		logger.error("\n");
		logger.error("------------- IO EXCEPTION DETECTED -------------");
		logger.error("\n- While processing request: {}." + "\n- See Error stack trace below:", request.getRequestURI(),
				ex);
		logger.error("-----------------------------------------------------");
		logger.error("\n");
	}

	@ExceptionHandler(ParseException.class)
	public ResponseEntity<String> handleParseException(Exception ex, HttpServletRequest request) throws Exception {
		logger.error("\n");
		logger.error("------------- PARSE EXCEPTION DETECTED -------------");
		logger.error("\n- While processing request: {}." + "\n- See Error stack trace below:", request.getRequestURI(),
				ex);
		logger.error("-----------------------------------------------------");
		logger.error("\n");

		return new ResponseEntity<String>(ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
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

	@ExceptionHandler(Exception.class)
	public void handleException(Exception ex, HttpServletRequest request) throws Exception {
		logger.error("\n");
		logger.error("------------- UNEXPECTED ERROR OCCURRED -------------");
		logger.error("\n- While processing request: {}." + "\n- User session has been invalidated. "
				+ "\n- See Error stack trace below:", request.getRequestURI(), ex);
		logger.error("-----------------------------------------------------");
		logger.error("\n");

		// securityService.logOut();
		// request.getSession().invalidate();
		throw ex;
	}

	private class ChangePassResponse {
		private int retCode;
		private String message;
		private String documentRoot; // webApp path to jscript
		boolean isLogged;

		public ChangePassResponse() {
		};

		public ChangePassResponse(int retCode, String message, String documentRoot, boolean isLogged) {
			this.retCode = retCode;
			this.setDocumentRoot(message);
			this.setMessage(message);
		}

		public int getRetCode() {
			return retCode;
		}

		public void setRetCode(int newCode) {
			retCode = newCode;
		}

		public String getMessage() {
			return message;
		}

		public void setMessage(String newMessage) {
			message = newMessage;
		}

		public String getDocumentRoot() {
			return documentRoot;
		}

		public void setDocumentRoot(String documentRoot) {
			this.documentRoot = documentRoot;
		}

		public boolean getIsLogged() {
			return isLogged;
		}

		public void setIsLogged(boolean newIsLogged) {
			isLogged = newIsLogged;
		}

	}

}
