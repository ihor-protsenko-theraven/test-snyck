package com.essence.hc.controllers;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.NoSuchMessageException;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.i18n.SessionLocaleResolver;
import org.springframework.web.util.WebUtils;

import com.essence.hc.eil.exceptions.AuthenticationException;
import com.essence.hc.exceptions.AppRuntimeException;
import com.essence.hc.exceptions.NewPasswordInvalidByHistoryException;
import com.essence.hc.exceptions.NewPasswordInvalidByPolicyException;
import com.essence.hc.exceptions.PasswordInvalidException;
import com.essence.hc.model.Activity;
import com.essence.hc.model.Activity.ActivityType;
import com.essence.hc.model.ActivityLevel;
import com.essence.hc.model.ActivityLevel.ActivityDetection;
import com.essence.hc.model.ActivityReportDetail;
import com.essence.hc.model.Alert;
import com.essence.hc.model.Alert.AlertState;
import com.essence.hc.model.Alert.AlertType;
import com.essence.hc.model.DateFormatHelper;
import com.essence.hc.model.Device;
import com.essence.hc.model.Event;
import com.essence.hc.model.Language;
import com.essence.hc.model.PasswordPolicy;
import com.essence.hc.model.Patient;
import com.essence.hc.model.ResponseStatus;
import com.essence.hc.model.RetriableResponse;
import com.essence.hc.model.StepCountDeviceReport;
import com.essence.hc.model.SystemStatus;
import com.essence.hc.model.SystemStatus.StatusTypes;
import com.essence.hc.model.SystemUser;
import com.essence.hc.model.ThirdPartyUser;
import com.essence.hc.model.User;
import com.essence.hc.service.PasswordPolicyService;
import com.essence.hc.service.PatientService;
import com.essence.hc.service.ReportsService;
import com.essence.hc.service.UserService;
import com.essence.hc.util.Util;
import com.essence.security.SecurityService;

@Controller
@RequestMapping("/home")
public class HomeController {

	private MessageSource msgSrc;

	@Autowired
	public void AccountsController(MessageSource msgSrc) {
		this.msgSrc = msgSrc;
	}

	@Autowired
	private UserService userService;

	@Autowired
	private SecurityService securityService;

	@Autowired
	private PatientService patientService;

	@Autowired
	ReportsService reportsService;

	@Autowired
	private PasswordPolicyService passwordPolicyService;

	protected static final Logger logger =(Logger) LogManager.getLogger(HomeController.class);

	private static final AlertType[] VIEWABLE_ALERT_TYPES = new AlertType[] { AlertType.ACTIVITY, AlertType.SOS,
			AlertType.TECHNICAL, AlertType.VIDEO, AlertType.ADVISORY };

	private static final AlertState[] VIEWABLE_ALERT_STATES = new AlertState[] { AlertState.NEW, AlertState.IN_PROGRESS,
			AlertState.CLOSED };

	private static final ActivityType[] VIEWABLA_ACTIVITY_TYPES_REPORT = new ActivityType[] {
			ActivityType.BATHROOM_SENSOR, ActivityType.BEDROOM_SENSOR, ActivityType.FRIDGE_DOOR,
			ActivityType.FRONT_DOOR, ActivityType.LIVING_ROOM,
			// ActivityType.STUDY_ROOM,
			ActivityType.TOILET_ROOM_SENSOR, ActivityType.DINING_ROOM, ActivityType.OTHER_ROOM };

	// @RequestMapping(value="logoutPopup", method = RequestMethod.GET)
	// public String Logout(ModelMap model){
	//// securityService.logOut();
	//// logger.info("User logged out");
	// return "redirect:ADL/logout";
	// }
	//

	@RequestMapping(value = "/logoutPopup", method = RequestMethod.GET)
	public String setLogout(ModelMap model, Locale locale, HttpSession session) {
		logger.info("______________________User logged out____________________");

		// return "redirect:main";
		return "redirect:login";
	}

	/**
	 * Home Page
	 * 
	 * @param model
	 * @param locale
	 * @param session
	 * @return
	 * @throws ParseException
	 */
	@RequestMapping(value = "/main", method = RequestMethod.GET)
	public String ShowHomePage(ModelMap model, Locale locale, HttpSession session) throws ParseException {
		logger.info("*************************HOME MAIN*******************************");

		// User user = userService.prepareSession();
		User currentUser = (User) securityService.getPrincipal();
		/*
		 * get user's patient list and current patient (if any)
		 */
		List<Patient> patientList = userService.getPatients(currentUser);
		Patient currentPatient = currentUser.getCurrentPatient();
		if (currentPatient == null) {
			logger.info("\n\nWe have no patient selected yet\n\n");
			if (patientList != null) {
				if (patientList.size() == 1) {
					currentPatient = patientList.get(0);

					int patientId = currentPatient.getUserId();
					currentPatient = (Patient) userService.getUser(String.valueOf(patientId));
					currentUser.setCurrentPatient(currentPatient);

				} else {
					/**
					 * TODO 1.- Si 1 (y solo 1) pacientes en alerta, se asigna automáticamente 2.-
					 * Si ningún paciente en alerta, mostrar pantalla selección paciente
					 */
				}
			} else {
				/**
				 * TODO El usuario no tiene pacientes No se puede hacer nada salvo informar al
				 * usuario
				 */
				throw new AppRuntimeException("User has no patients assigned");
			}
		}
		/*
		 * Preparing view model
		 */
		model.addAttribute("patientList", patientList);
		// Language language= currentUser.getLanguage();
		logger.info("Id Locale Current User...................{}...................{}",
				currentUser.getLanguage().getLanguageID(), currentUser.getLanguage().getInLanguage());
		model.addAttribute("language_select", currentUser.getLanguage().getInLanguage());
		if (currentPatient != null) {
			/*
			 * Get Patient's System status
			 */
			currentPatient.setSystemStatus(patientService.getSystemStatus(currentPatient.getStringUserId(),
					currentUser.getLanguage().getLanguageKey()));
			model.addAttribute("currentPatient", currentPatient);
			/*
			 * Get Patient's Activity Level [TODO: Think about doing this from an Ajax
			 * request when home page is yet loaded]
			 */
			ActivityLevel activityLevel = patientService.getActivityLevel(currentPatient.getStringUserId());
			model.addAttribute("patientActivity", activityLevel);
			return "home";
		} else {
			/*
			 * Will show patient selection page Get System Status for each patient in the
			 * list
			 */
			for (Patient p : patientList) {
				p.setSystemStatus(patientService.getSystemStatus(p.getStringUserId(),
						currentUser.getLanguage().getLanguageKey()));
				model.addAttribute("currentPatient", p);
			}
			return "selectPatient";
		}

	}

	/**
	 * Patient Switch
	 * 
	 * @param model
	 * @param locale
	 * @param session
	 * @param patientId
	 * @return
	 */
	@RequestMapping(value = "/setPatient", method = RequestMethod.GET)
	public String setPatient(ModelMap model, Locale locale, HttpSession session,
			@RequestParam(value = "patientId", required = false) String patientId) {

		User currentUser = (User) securityService.getPrincipal();
		List<Patient> patientList = userService.getPatients(currentUser);
		for (Patient patient : patientList) {
			if (patient.getStringUserId().equals(patientId)) {

				// patient data in the list (returned by getUserAuthenticate) is not plenty
				// full, as for example Installation info is not provided.
				// currentUser.setCurrentPatient(patient);
				currentUser.setCurrentPatient((Patient) userService.getUser(patientId));

				model.addAttribute("currentPatient", patient);
			}
		}
		Patient currentPatient = currentUser.getCurrentPatient();
		ActivityLevel activityLevel = patientService.getActivityLevel(currentPatient.getStringUserId());
		model.addAttribute("patientList", patientList);
		model.addAttribute("patientActivity", activityLevel);
		return "redirect:main";
	}

	/**
	 * Patient Switch
	 * 
	 * @param model
	 * @param locale
	 * @return
	 */

	@RequestMapping(value = "selectLang", method = RequestMethod.GET)
	public String selectLanguages(ModelMap model, Locale locale) {
		User currentUser = (User) securityService.getPrincipal();
		// model.addAttribute("language_select",userService.getLanguages().get(currentUser.getLanguageId()).getInLanguage());
		model.addAttribute("list_languages", userService.getLanguages());
		model.addAttribute("language_select", currentUser.getLanguage().getLanguageID());
		return "selectLanguage";
	}

	@RequestMapping(value = "showEula", method = RequestMethod.GET)
	public String showEula(ModelMap model, Locale locale) {
		User currentUser = (User) securityService.getPrincipal();
		// model.addAttribute("language_select",userService.getLanguages().get(currentUser.getLanguageId()).getInLanguage());
		model.addAttribute("eula", currentUser.getEula());
		return "showEulaMobile";
	}

	@RequestMapping(value = "/changeLang/{languageId}", method = RequestMethod.GET)
	public String changeLanguage(@PathVariable("languageId") int languageId, ModelMap model, HttpSession session,
			HttpServletRequest request) {
		User currentUser = (User) securityService.getPrincipal();

		if (userService.setUserPreferences(currentUser.getUserId(), currentUser.getVendorId(), languageId)
				.getNumErr() >= 0) {
			currentUser.setLanguage(Language.getLanguage(languageId));
			userService.loadVendorTimeZones(currentUser.getVendor(), languageId);
			logger.info("Locale My Session...{}......................................{}", languageId,
					Language.getLanguage(languageId).getLanguageKey().substring(0, 2));
			Locale locale = new Locale(Language.getLanguage(languageId).getLanguageKey().substring(0, 2),
					Language.getLanguage(languageId).getLanguageKey().substring(3, 5));
			WebUtils.setSessionAttribute(request, SessionLocaleResolver.LOCALE_SESSION_ATTRIBUTE_NAME, locale);
		}

		// LocaleResolver localeResolver =
		// RequestContextUtils.getLocaleResolver(request);
		// localeResolver.setLocale(request, null, new Locale ("EN"));

		// SET DATE FORMAT IN SESSION VARIABLE
		DateFormatHelper.setSessionDateFormat(languageId, request);

		return "redirect:/home/main";
	}

	/**
	 * Get new alerts
	 */
	@RequestMapping(value = "alertsnew/{numOccurs}", method = RequestMethod.GET)
	public @ResponseBody RetriableResponse<List<Alert>> getHistory(Model model, Locale locale,
			@PathVariable("numOccurs") String numOccurs) {
		System.out.println(
				"---------------------------------------- I´m here ------------------------------------------------");
		User currentUser = (User) securityService.getPrincipal();
		Patient currentPatient = securityService.getPrincipal().getCurrentPatient();

		List<Alert> alerts = new ArrayList<Alert>();

		AlertState[] alertState = new AlertState[1];
		alertState[0] = AlertState.NEW;

		alerts = patientService.getAlertHistory(currentPatient.getStringUserId(), 0, 0, Integer.valueOf(numOccurs),
				AlertType.values(), alertState, currentUser.getLanguage().getLanguageKey(), currentUser.getUserId(),
				currentUser.getUserType());

		RetriableResponse<List<Alert>> response = new RetriableResponse<List<Alert>>();
		response.setResponse(alerts);
		return response;
	}

	/**
	 * Get current alert status of the current patient's system Maintains clients up
	 * to date. It's a passive notification system
	 * 
	 * @param model
	 * @return JSON object with the alerts pending
	 */
	@RequestMapping(value = "status", method = RequestMethod.GET)
	public @ResponseBody RetriableResponse<Patient> getSystemStatus(Model model, Locale locale) {
		User currentUser = (User) securityService.getPrincipal();
		Patient currentPatient = securityService.getPrincipal().getCurrentPatient();
		logger.info("Current user lang: {}", currentUser.getLanguage().getLanguageKey());
		currentPatient.setSystemStatus(patientService.getSystemStatus(currentPatient.getStringUserId(),
				currentUser.getLanguage().getLanguageKey()));

		// Call update Last Location and Activity Level
		ActivityLevel activityLevel = patientService.getActivityLevel(currentPatient.getStringUserId());
		try {
			activityLevel.getLastLocation()
					.setAlias(msgSrc.getMessage(activityLevel.getLastLocation().getLocationType(), null, locale));
		} catch (NoSuchMessageException e) {
			activityLevel.getLastLocation().setAlias("");
			logger.info("Message not found: " + e.getMessage());
		}
		// activityLevel.getLastLocation().setDescription(msgSrc.getMessage(activityLevel.getLastLocation().getLocationType(),null,
		// locale));
		currentPatient.setActivityLevel(activityLevel);

		if (currentPatient.getSystemStatus() != null && currentPatient.getSystemStatus().getAlert() != null) {
			Alert mainAlert = patientService
					.getSystemStatus(currentPatient.getStringUserId(), currentUser.getLanguage().getLanguageKey())
					.getAlert();
			if (mainAlert != null) {
				currentPatient.getSystemStatus().getAlert().setTitle(mainAlert.getTitle());
			}
		}

		RetriableResponse<Patient> response = new RetriableResponse<Patient>();
		response.setResponse(currentPatient);

		// Session Timeout Handling
		if (currentUser.isSessionExpired()) {
			currentUser.setSessionExpired(false);
			response.setControlFlag(true);
		}
		return response;
	}

	/**
	 * Get current alert status of each patient associated with user
	 * 
	 * @param model
	 * @return JSON object with the alerts pending
	 */
	@RequestMapping(value = "status/all", method = RequestMethod.GET)
	public @ResponseBody Map<String, Object> getSystemStatus(ModelMap model, Locale locale) {

		Map<String, Object> response = new HashMap<>();
		List<Patient> patientList = null;

		User currentUser = securityService.getPrincipal();
		patientList = currentUser.getPatients();
		for (Patient p : patientList) {
			p.setSystemStatus(
					patientService.getSystemStatus(p.getStringUserId(), currentUser.getLanguage().getLanguageKey()));
		}
		response.put("residents", patientList);
		response.put("vendorServiceTypes", currentUser.getVendor().getServiceTypes());
		return response;
	}

	/**
	 * Get i18 location.
	 * 
	 * @param model
	 * @return i18
	 */
	@RequestMapping(value = "i18js", method = RequestMethod.GET)
	public String getI18(ModelMap model, Locale locale) {

		return "i18";
	}

	/**
	 * Day Story
	 * 
	 * @param model
	 * @param locale
	 * @return
	 */
	@RequestMapping(value = "day_story", method = RequestMethod.GET)
	public String ShowDayStory(ModelMap model, Locale locale) {

		Patient currentPatient = securityService.getPrincipal().getCurrentPatient();
		Date patientPanelDate = this.calculatePatientPanelDate(currentPatient.getStringUserId());

		List<Event> events = patientService.getDayStory(currentPatient.getStringUserId(), patientPanelDate, false);
		List<Activity> activities = new ArrayList<Activity>();
		List<Alert> alerts = new ArrayList<Alert>();
		if ((events != null) && (events.size() != 0)) {
			for (Event e : events) {
				if (Activity.class.isAssignableFrom(e.getClass())) {
					activities.add((Activity) e);
				} else {
					alerts.add((Alert) e);
				}
			}

		} else
			logger.info("no activities for the selected date");

		model.addAttribute("activities", activities);
		model.addAttribute("alerts", alerts);
		model.addAttribute("panelDate", patientPanelDate);

		model.addAttribute("types", VIEWABLA_ACTIVITY_TYPES_REPORT);
		return "dayStory";
	}

	@RequestMapping(value = "day_story/{date}", method = RequestMethod.GET)
	public String ShowDayStoryPast(@PathVariable("date") @DateTimeFormat(pattern = "yyyyMMdd") Date date, Model model) {

		logger.info("Requesting dayStory datePicker {}", date);

		Patient currentPatient = securityService.getPrincipal().getCurrentPatient();
		Date patientPanelDate = this.calculatePatientPanelDate(currentPatient.getStringUserId());

		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
		List<Event> events;

		if (sdf.format(date).equals(sdf.format(patientPanelDate))) {
			events = patientService.getDayStory(currentPatient.getStringUserId(), patientPanelDate, false);
		} else {
			events = patientService.getDayStory(currentPatient.getStringUserId(), date, true);
		}

		List<Activity> activities = new ArrayList<Activity>();
		List<Alert> alerts = new ArrayList<Alert>();
		if (events.size() != 0) {
			for (Event e : events) {
				if (Activity.class.isAssignableFrom(e.getClass())) {
					activities.add((Activity) e);
				} else {
					alerts.add((Alert) e);
				}
			}

			model.addAttribute("date", date);
			model.addAttribute("activities", activities);
			model.addAttribute("alerts", alerts);

		} else
			logger.info("no activities for the selected date");

		model.addAttribute("panelDate", patientPanelDate);
		model.addAttribute("types", VIEWABLA_ACTIVITY_TYPES_REPORT);

		return "dayStory";
	}

	// The concrete date and time of the panel, without info about the
	// timezone where the panel is located.
	private Date calculatePatientPanelDate(String userId) {

		Patient resident = (Patient) userService.getUser(userId);

		SimpleDateFormat dateFormatter = new SimpleDateFormat(Util.DATETIMEFORMAT_INPUT);
		Date patientPanelDate = null;

		try {
			patientPanelDate = dateFormatter.parse(resident.getPanelTime());
		} catch (ParseException e1) {
			this.logger.error("Error parsing resident time zone");
		}

		return patientPanelDate;
	}

	@RequestMapping(value = "day_story_list/{date}", method = RequestMethod.GET)
	public String ShowDayStoryListDate(@PathVariable("date") @DateTimeFormat(pattern = "yyyyMMdd") Date date,
			Model model) {
		logger.info("Requesting dayStory datePicker {}", date);

		Patient currentPatient = securityService.getPrincipal().getCurrentPatient();

		Date patientPanelDate = this.calculatePatientPanelDate(currentPatient.getStringUserId());

		List<Event> events = patientService.getDayStory(currentPatient.getStringUserId(), date, true);
		List<Activity> activities = new ArrayList<Activity>();
		List<Alert> alerts = new ArrayList<Alert>();
		if (events.size() != 0) {
			for (Event e : events) {
				if (Activity.class.isAssignableFrom(e.getClass())) {
					activities.add((Activity) e);
				} else {
					alerts.add((Alert) e);
				}
			}
			model.addAttribute("date", date);
			model.addAttribute("activities", activities);
			model.addAttribute("alerts", alerts);
		} else
			logger.info("no activities for the selected date");

		model.addAttribute("panelDate", patientPanelDate);

		return "dayStoryList";
	}

	@RequestMapping(value = "day_story_list", method = RequestMethod.GET)
	public String ShowDayStoryList(ModelMap model, Locale locale) {

		Patient currentPatient = securityService.getPrincipal().getCurrentPatient();

		Date patientPanelDate = this.calculatePatientPanelDate(currentPatient.getStringUserId());

		List<Event> events = patientService.getDayStory(currentPatient.getStringUserId(), patientPanelDate, false);
		List<Activity> activities = new ArrayList<Activity>();
		List<Alert> alerts = new ArrayList<Alert>();
		if (events != null && events.size() != 0) {
			for (Event e : events) {
				if (Activity.class.isAssignableFrom(e.getClass())) {
					activities.add((Activity) e);
				} else {
					alerts.add((Alert) e);
				}
			}
			model.addAttribute("activities", activities);
			model.addAttribute("alerts", alerts);
		} else
			logger.info("no activities for the selected date");

		model.addAttribute("panelDate", patientPanelDate);

		return "dayStoryList";

	}

	/**
	 * Day Story Report for Express Residents
	 * 
	 * @param model
	 * @param locale
	 * @param date
	 * @return
	 * @throws ParseException
	 */
	@RequestMapping(value = "day_story_xp/{date}", method = RequestMethod.GET)
	public String ShowDayStoryXP(ModelMap model, Locale locale, @PathVariable("date") String date)
			throws ParseException {

		Patient currentPatient = securityService.getPrincipal().getCurrentPatient();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
		Date parsedDate = sdf.parse(date);
		Map<String, List<?>> dayStoryMap = patientService.getDayStoryXP(currentPatient.getStringUserId(), parsedDate);
		Date currentPanelTime = patientService.getCurrentPanelTime(dayStoryMap);
		model.addAttribute("dayStoryMap", dayStoryMap);
		model.addAttribute("date", parsedDate);
		model.addAttribute("now", sdf.parse(sdf.format(currentPanelTime)));
		model.addAttribute("now_ms", sdf.parse(sdf.format(currentPanelTime)).getTime());
		model.addAttribute("panelDay", sdf.parse(sdf.format(currentPanelTime)));
		model.addAttribute("resident", currentPatient);

		return "dayStoryExpress";
	}

	@RequestMapping(value = "day_story_list_xp/{date}", method = RequestMethod.GET)
	public String ShowDayStoryListXP(ModelMap model, Locale locale, @PathVariable("date") String date)
			throws ParseException {

		Patient currentPatient = securityService.getPrincipal().getCurrentPatient();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
		Date parsedDate = sdf.parse(date);

		Map<String, List<?>> dayStoryMap = patientService.getDayStoryXP(currentPatient.getStringUserId(), parsedDate);

		model.addAttribute("dayStoryMap", dayStoryMap);
		model.addAttribute("date", parsedDate);
		model.addAttribute("resident", currentPatient);

		return "dayStoryListExpress";
	}

	@RequestMapping(value = "activity_level", method = RequestMethod.GET)
	public String ShowActivity(ModelMap model, Locale locale) {
		// TODO: call service layer to obtain DY data
		logger.info("Request of Activity Lvl");
		Patient currentPatient = securityService.getPrincipal().getCurrentPatient();
		// List<ActivityDetection> activity =
		// patientService.getActivityLevel(currentPatient.getId()).getActivityDetections();
		// model.addAttribute("activity",activity);

		ActivityLevel activityLevel = patientService.getActivityLevel(currentPatient.getStringUserId());
		List<ActivityDetection> activityDetections = activityLevel.getActivityDetections();
		model.addAttribute("activity", activityDetections);
		return "activityLevel";

	}

	@RequestMapping(value = "activity_index/{date}", method = RequestMethod.GET)
	public String ShowActivityIndex(ModelMap model, Locale locale) {
		logger.info("Request of Activity index");

		// List<ActivityDetection> activity =
		// patientService.getActivityLevel(currentPatient.getId()).getActivityDetections();
		// model.addAttribute("activity",activity);

		model.addAttribute("date", Util.getCurrentDate());
		return "activityIndex";

	}

	/**
	 * Activity Index Report for Express Residents
	 * 
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "activity_index/{date}.json", method = RequestMethod.GET, produces = "application/json")
	public @ResponseBody List<ActivityReportDetail> getActivityIndex(@PathVariable("date") String date, Model model)
			throws ParseException {

		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
		Date toDate = sdf.parse(date);
		Calendar cal = Calendar.getInstance();
		cal.setTime(toDate);
		cal.add(Calendar.DAY_OF_MONTH, -6);
		Date fromDate = cal.getTime();

		SystemUser resident = securityService.getPrincipal().getCurrentPatient();
		List<ActivityReportDetail> activityIndexData = patientService.getActivityIndex(resident.getStringUserId(),
				fromDate, toDate);

		return activityIndexData;
	}

	@RequestMapping(value = "step_count/{residentId}/{deviceId}", method = RequestMethod.GET, produces = "application/json")
	public String getStepCountDeviceReport(@PathVariable("residentId") String residentId,
			@PathVariable("deviceId") String deviceId, Model model) throws ParseException {

		Patient resident = (Patient) userService.getUser(residentId);
		String residentAccount = resident.getInstallation().getPanel().getAccount();

		SimpleDateFormat dateFormatter = new SimpleDateFormat(Util.DATETIME_TMZ_FORMAT_INPUT);

		Date residentDate = dateFormatter.parse(resident.getPanelTime());
		Date lastDate = Util.subtractDays(residentDate, 1);
		Date firstDate = Util.subtractDays(lastDate, 30);

		List<StepCountDeviceReport> deviceReports = reportsService
				.getStepCount(residentAccount, null, null, firstDate, lastDate, null).getStepCountDeviceReports();

		for (StepCountDeviceReport stepCountDeviceReport : deviceReports) {

			if (stepCountDeviceReport.getWearableDevice().getDeviceId() == Integer.parseInt(deviceId)) {
				// model.addAttribute("stepCountDeviceReport", stepCountDeviceReport);
				model.addAttribute("deviceLabel", stepCountDeviceReport.getWearableDevice().getLabel());
				model.addAttribute("deviceId", deviceId);
			}
		}

		model.addAttribute("residentDate", residentDate);
		model.addAttribute("residentDateMS", residentDate.getTime());

		return "stepCountActivity";

	}

	@RequestMapping(value = "step_count/list/{residentId}", method = RequestMethod.GET)
	public String getStepCountList(@PathVariable("residentId") String residentId, Model model) throws ParseException {

		User currentUser = (User) securityService.getPrincipal();
		Patient currentPatient = currentUser.getCurrentPatient();
		String residentAccount = currentPatient.getInstallation().getPanel().getAccount();

		SimpleDateFormat dateFormatter = new SimpleDateFormat(Util.DATETIME_TMZ_FORMAT_INPUT);

		Date lastDate = dateFormatter.parse(currentPatient.getPanelTime());
		lastDate = Util.subtractDays(lastDate, 1);
		Date firstDate = Util.subtractDays(lastDate, 30);

		List<StepCountDeviceReport> deviceReports = reportsService
				.getStepCount(residentAccount, null, null, firstDate, lastDate, null).getStepCountDeviceReports();
		model.addAttribute("deviceReports", deviceReports);

		if (currentPatient != null) {
			model.addAttribute("currentPatient", currentPatient);
		}

		return "stepCountList";
	}

	// /**
	// * Maintains clients up to date. It's a passive notification system
	// * User requests for actual object and app returns new data from the caching
	// system
	// * The result contains basically alerts and status changes from the
	// user-patient pair
	// * @param ObjectId
	// * @param model
	// * @return JSON object with the alerts pending
	// */
	// @RequestMapping(value="getActivityLevel/{objId}", method = RequestMethod.GET)
	// public @ResponseBody ActivityLevel NotificatorActivity(@PathVariable("objId")
	// String patientId,
	// Model model){
	// if (logger.isDebugEnabled())
	// logger.debug("Client {} requesting alerts");
	// // Automatically the framework serializes output to JSON
	// ActivityLevel list = patientService.getActivityLevel(patientId);
	//// int a = list.getActivityDetections().get(0).getRegisteredLevel();
	// return list;
	// }

	/**
	 * Shows the current alert status with the list of alerts involved
	 * 
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "status_detail", method = RequestMethod.GET)
	public String getStatusDetail(ModelMap model, Locale locale,
			@RequestParam(value = "page", required = false, defaultValue = "0") Integer page,
			@RequestParam(value = "lastAlert", required = false, defaultValue = "0") Integer lastAlert) {

		List<Alert> alerts = null;
		AlertType[] types = null;
		AlertState[] states = null;

		User currentUser = (User) securityService.getPrincipal();
		Patient currentPatient = securityService.getPrincipal().getCurrentPatient();
		SystemStatus sysStatus = currentPatient.getSystemStatus();
		logger.info("\n\nSystem Status is: {}\n\n", sysStatus.getStatusType());

		if (sysStatus.getStatusType() == StatusTypes.ALARM
				|| sysStatus.getStatusType() == StatusTypes.ALARM_IN_PROGRESS) {

			types = new AlertType[] { AlertType.ACTIVITY, AlertType.SOS, AlertType.VIDEO }; // , AlertType.USER};
			states = new AlertState[] { AlertState.NEW, AlertState.VIEWED, AlertState.IN_PROGRESS };

		} else {
			if (sysStatus.getStatusType() == StatusTypes.TECH_ALARM) {
				types = new AlertType[] { AlertType.TECHNICAL };
				states = new AlertState[] { AlertState.NEW, AlertState.VIEWED, AlertState.IN_PROGRESS };

			} else if (sysStatus.getStatusType() == StatusTypes.USER_PHOTO_REQUEST) {
				types = new AlertType[] { AlertType.VIDEO };
				states = new AlertState[] { AlertState.NEW, AlertState.VIEWED, AlertState.IN_PROGRESS };

			} else if (sysStatus.getStatusType() == StatusTypes.ADVISORY) {
				types = new AlertType[] { AlertType.TECHNICAL };
				states = new AlertState[] { AlertState.NEW, AlertState.VIEWED, AlertState.IN_PROGRESS };

			} else if (sysStatus.getStatusType() == StatusTypes.NO_ALERTS) {
				types = new AlertType[] { AlertType.TECHNICAL, AlertType.ACTIVITY, AlertType.SOS, AlertType.VIDEO,
						AlertType.ADVISORY };
				states = new AlertState[] { AlertState.CLOSED };
			}
		}

		alerts = patientService.getAlertHistory(currentPatient.getStringUserId(), lastAlert, page, 10, types, states,
				currentUser.getLanguage().getLanguageKey(), currentUser.getUserId(), currentUser.getUserType());

		/*
		 * Preparing response for view layer
		 */
		model.addAttribute("alerts", alerts);
		model.addAttribute("types", VIEWABLE_ALERT_TYPES);
		model.addAttribute("states", VIEWABLE_ALERT_STATES);
		model.addAttribute("selectedTypes", types);
		model.addAttribute("selectedStates", states);

		return "history";
	}

	@RequestMapping(value = "history", method = RequestMethod.GET)
	public String ShowHistory(ModelMap model, Locale locale,
			@RequestParam(value = "type[]", required = false) AlertType[] type,
			@RequestParam(value = "state[]", required = false) AlertState[] state,
			@RequestParam(value = "page", required = false, defaultValue = "0") Integer page,
			@RequestParam(value = "lastAlert", required = false, defaultValue = "0") Integer lastAlert) {
		User currentUser = (User) securityService.getPrincipal();
		Patient currentPatient = securityService.getPrincipal().getCurrentPatient();
		List<Alert> alerts;
		alerts = patientService.getAlertHistory(currentPatient.getStringUserId(), lastAlert, page, 10, type, state,
				currentUser.getLanguage().getLanguageKey(), currentUser.getUserId(), currentUser.getUserType());

		/*
		 * Preparing response for view layer
		 */
		model.addAttribute("alerts", alerts);
		model.addAttribute("types", VIEWABLE_ALERT_TYPES);
		model.addAttribute("states", VIEWABLE_ALERT_STATES);
		model.addAttribute("selectedTypes", type);
		model.addAttribute("selectedStates", state);

		if (type != null || state != null)
			model.addAttribute("isFiltered",
					((type != null && type.length > 0) || (state != null && state.length > 0)) ? true : false);
		else
			model.addAttribute("isFiltered", false);

		return "history";
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

		return "changePasswordMobile";
	}

	@RequestMapping(value = "changePassword", method = RequestMethod.POST, produces = "application/json")
	public @ResponseBody ChangePassResponse processChangePassword(ModelMap model, HttpServletRequest request,
			@RequestParam(value = "username") String username,
			@RequestParam(value = "currentPassword") String currentPassword,
			@RequestParam(value = "newPassword") String newPassword) {

		ChangePassResponse response = new ChangePassResponse(-1, request.getContextPath(), "validations.servererror");

		try {
			passwordPolicyService.changeMyPassword(currentPassword, newPassword);
			response.retCode = 0;
			response.message = "validations.passwordchanged";

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

	/**************************************************************************/
	/**************************** TAKE PHOTO **********************************/
	/**************************************************************************/

	/**
	 * List photo devices
	 *
	 * @param locale
	 * @return
	 */
	@RequestMapping(value = "/take-photo", method = RequestMethod.GET)
	public String showTakePhotoList(ModelMap model, Locale locale) {
		Patient currentPatient = securityService.getPrincipal().getCurrentPatient();

		List<Device> cameraList = patientService.getCameraList(currentPatient.getStringUserId());
		model.addAttribute("cameraList", cameraList);

		return "takePhoto";
	}

	/**
	 * Request the photo device
	 * 
	 * @param deviceId
	 * @return
	 */
	@RequestMapping(value = "/execute-photo/{deviceId}", method = RequestMethod.GET)
	public String takePhoto(ModelMap model, Locale locale, @PathVariable(value = "deviceId") String deviceId) {

		User currentUser = (User) securityService.getPrincipal();
		Patient currentPatient = securityService.getPrincipal().getCurrentPatient();
		List<Device> cameraList = patientService.getCameraList(String.valueOf(currentPatient.getUserId()));
		for (Device device : cameraList) {
			if (device.getId().equalsIgnoreCase(deviceId)) {
				model.addAttribute("nameDevice", device.getLabel());
			}
		}

		// deviceId,alertSession
		ResponseStatus photo = patientService.requestPhoto(currentUser.getId(), currentPatient.getStringUserId(),
				deviceId, null);
		model.addAttribute("name_patient", currentPatient.getFirstName());

		if (photo.getNumErr() < 0)
			return "takePhotoRequestFail";
		else
			return "takePhotoRequestSuccess";

	}

	/**
	 * Request the photo device
	 * 
	 * @param deviceId
	 * @return
	 */
	@RequestMapping(value = "/get-photos", method = RequestMethod.GET)
	public String getPhotos(ModelMap model, Locale locale, @RequestParam("deviceId") String deviceId) {
		Patient currentPatient = securityService.getPrincipal().getCurrentPatient();
		Calendar cal = Calendar.getInstance();
		String photo = patientService.getPhotos(currentPatient.getStringUserId(), cal.getTime(), cal.getTime());

		return "takePhotoRequestSuccess";
	}

	/**************************************************************************/
	/************************** END TAKE PHOTO ********************************/
	/**************************************************************************/

	/**
	 * Retrieve photo detail from itemId
	 * 
	 * @param map
	 * @param locale
	 * @param itemId
	 * @return
	 */
	@RequestMapping(value = "/photo/{itemId}", method = RequestMethod.GET)
	public String showPhotoDetail(ModelMap map, Locale locale, @PathVariable String itemId) {

		// TODO: call service to retrieve item photos

		return "photoDetail";
	}

	@RequestMapping(value = "info", method = RequestMethod.GET)
	public String ShowInfoMenu(ModelMap model, Locale locale) {
		// TODO: call service layer to obtain DY data
		logger.info("Request of Info Menu");
		return "menuInfo";

	}

	@RequestMapping(value = "photo", method = RequestMethod.GET)
	public String ShowTakePhoto(ModelMap model, Locale locale) {
		// TODO: call service layer to obtain DY data
		logger.info("Request to take a photo");
		List<com.essence.hc.model.Device> devices = securityService.getPrincipal().getCurrentPatient().getInstallation()
				.getDevices();
		Patient currentPatient = securityService.getPrincipal().getCurrentPatient();
		User currentUser = (User) securityService.getPrincipal();
		model.addAttribute("devices", devices);
		model.addAttribute("user", currentUser);
		// model.addAttribute("patient",currentPatient);
		return "takePhoto";

	}

	@RequestMapping(value = "photos/{objId}/detail", method = RequestMethod.GET)
	public String GetPhotoDetail(@PathVariable("objId") String photoId, ModelMap model, Locale locale) {
		// TODO: call service layer to obtain DY data
		logger.info("Request of photo {} detail", photoId);
		return "photoDetail";
	}

	@RequestMapping(value = "patient/{objId}/data", method = RequestMethod.GET)
	public @ResponseBody Patient ShowPatientData(@PathVariable("objId") String objectId, Model model) {
		// TODO: call service layer to obtain DY data
		Patient patientInfo = patientService
				.getPatient(securityService.getPrincipal().getCurrentPatient().getStringUserId());
		model.addAttribute("patientInfo", patientInfo);
		return patientInfo;

	}

	// @RequestMapping(value="patient/{objId}/status", method = RequestMethod.GET)
	// public @ResponseBody ActivityLevel ShowPatientStatus(@PathVariable("objId")
	// String objectId,
	// Model model){
	// // TODO: call service layer to obtain DY data
	// ActivityLevel status = patientService.getActivityLevel(objectId);
	// return status;
	//
	// }

	@RequestMapping(value = "handle_open_issue", method = RequestMethod.GET)
	public String ShowHandleOpenIssue(ModelMap model, Locale locale) {
		// TODO: call service layer to obtain DY data
		logger.info("Request of Info Menu");
		List<Alert> listOpen = userService.getOpenAlerts(securityService.getPrincipal().getId(), true);
		model.addAttribute("openIssues", listOpen);
		return "handleOpenIssue";

	}

	@RequestMapping(value = "Information", method = RequestMethod.GET)
	public String ShowInformation(ModelMap model, Locale locale) {
		// TODO: call service layer to obtain DY data
		logger.info("Request of Info Menu");
		return "information";

	}

	@RequestMapping(value = "preferences", method = RequestMethod.GET)
	public String ShowPreferences(ModelMap model, Locale locale) {
		// TODO: call service layer to obtain DY data
		logger.info("Request of Info Menu");
		return "preferences";

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
		} else if ("fit".equals(currentUser.getRedirectedFromApplication().toLowerCase())) {
			currentUser.setTokenExpired(true);
			response.sendRedirect("/ADL/locallogin?isTokenExpired=true");
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

		public ChangePassResponse() {
		};

		public ChangePassResponse(int retCode, String message, String documentRoot) {
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

	}

}
