package com.essence.hc.controllers;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.essence.hc.eil.exceptions.AuthenticationException;
import com.essence.hc.model.Alert;
import com.essence.hc.model.AlertCloseReason;
import com.essence.hc.model.Alert.AlertState;
import com.essence.hc.model.Alert.AlertType;
import com.essence.hc.model.EventLog;
import com.essence.hc.model.HCCommand;
import com.essence.hc.model.LowActivityReport;
import com.essence.hc.model.Patient;
import com.essence.hc.model.ResponseStatus;
import com.essence.hc.model.SystemUser;
import com.essence.hc.model.ThirdPartyUser;
import com.essence.hc.model.User;
import com.essence.hc.service.AlertService;
import com.essence.hc.service.UserService;
import com.essence.security.SecurityService;

/**
 * Class to serve alert request from clients
 * @author manuel.garcia at essence
 *
 */
@Controller
@RequestMapping("/alerts")
public class AlertController {

	@Autowired
	private UserService userService;
	@Autowired
	private AlertService alertService;
	@Autowired
	private SecurityService securityService;
	@Autowired
	private MessageSource msgSrc;
	
	protected final Logger logger = (Logger) LogManager.getLogger(AlertController.class);
	
	/**
	 * Alert Detail
	 * Retrieves detailed info for an alert
	 * @param model
	 * @param locale
	 * @param alertId
	 * @return
	 */
	@RequestMapping(value="{alertId}", method = RequestMethod.GET)
	public String getAlert(ModelMap model, Locale locale,@PathVariable("alertId") String alertId) {
		User        currentUser = (User) securityService.getPrincipal();
		Alert a = alertService.getAlert(alertId,currentUser.getLanguage().getLanguageKey());
		Patient currentPatient = securityService.getPrincipal().getCurrentPatient();
		
		/*
		 * Get Additional info in case alert is of 'Low Activity' type 
		 */
//		if (a.getType() == Alert.AlertType.SOS || a.getType() == Alert.AlertType.ACTIVITY){
//			if(a.getTitle().contains("low activity")) {
		if (a.isLowActivity()){
				LowActivityReport aReport = alertService.getLowActivityReport(a.getId(),currentUser.getLanguage().getLanguageKey());
				model.addAttribute("lowActivityReport", aReport);
		
		}
		/*
		 * Update status to viewed if it was new
		 */
		if (a.getCurrentState() == AlertState.NEW) {
			alertService.updateAlertState(Integer.parseInt(a.getId()), AlertState.VIEWED, currentPatient.getUserId());
		}
		
		// retrieve in advance list of available commands, so we don't show the take action button if there isn't any command
		Map<String, HCCommand> commands = null;
		if (a != null) {
			commands = userService.getCommands(currentUser.getId(), currentPatient.getSystemStatus().getStatusType());
		}
		
		model.addAttribute("commands", commands);
		
		// list of available reasons to close an event
		List<AlertCloseReason> reasons = null;
		if (a != null) {
			reasons = alertService.getAlertCloseReasons(currentUser.getLanguage().getLanguageKey());
		}
		model.addAttribute("reasons", reasons);
		
		model.addAttribute("alerta", a);
		model.addAttribute("resident", currentPatient);
		
//		List<Action> takenActions = a.getTakenActions();
//		model.addAttribute("takenActions", takenActions);
		// The result message depends of the type & severity of event
//		if ((a.getType() == Alert.AlertType.SOS)|| (a.getType() == Alert.AlertType.FALL))
			return "notificationAlert";
//		else if ((a.getType() == Alert.AlertType.EQUIPMENT)||(a.getType() == Alert.AlertType.DOOR)||(a.getType() == Alert.AlertType.USER))
//			return "notificationAlert";
//		else {
//			if (true) // TODO: Need a field to identify photos from others
//				return "notificationPhoto";
//			else
//				return "notificationInfo";
//		}
	}

	/**
	 * Detail of a Photo Alert
	 * @param model
	 * @param locale
	 * @param alertId
	 * @return
	 */
	@RequestMapping(value="{alertId}/photo_detail", method = RequestMethod.GET)
	public String getPhotoDetail(ModelMap model, Locale locale,@PathVariable("alertId") String alertId) {
		User        currentUser = (User) securityService.getPrincipal();
		Alert a = alertService.getAlert(alertId,currentUser.getLanguage().getLanguageKey());
		/*
		 * Photo Alerts "without session" must be closed when viewed
		 * (photo alerts without session are those photos requested 
		 * and viewed when the system is not in alarm status) 
		 */
		if (a.getType() == AlertType.VIDEO && a.getAlertSessionId() == 0 && !a.isClosed()) {
//		if (a.getType() == AlertType.VIDEO && !a.isClosed()) {
			Patient currentPatient = securityService.getPrincipal().getCurrentPatient();
			alertService.updateAlertState(Integer.parseInt(a.getId()), AlertState.CLOSED, currentPatient.getUserId());
		}
		
		model.addAttribute("alerta", a);
//		model.addAttribute("resident", currentPatient);
		
		return "photoDetail";
	}
	
	/**
	 * Alert Session Closing
	 * @param model
	 * @param locale
	 * @param alertSessionId
	 * @return
	 */
	@RequestMapping(value="{alertId}/close", method = RequestMethod.POST, produces="application/json")
	public @ResponseBody int closeAlert(HttpServletRequest request, ModelMap model, Locale locale,@PathVariable("alertId") String alertId,
															    @RequestParam(value="sessionId", required=true) String sessionId,
																@RequestParam(value="smsgTxt", required=true) String smsgTxt,
																@RequestParam(value="smsgReason", required=true) String smsgReason,
																@RequestParam(value="smsgDetails", required=true) String smsgDetails) {
		logger.info("closing alert {}...",alertId);

		//User currentUser = securityService.getPrincipal();
		//Patient currentPatient = securityService.getPrincipal().getCurrentPatient();
		User           currentUser    = (User) securityService.getPrincipal();
		Patient        currentPatient = currentUser.getCurrentPatient();
		ResponseStatus response;
		
		int accountNumber = Integer.parseInt(currentPatient.getInstallation().getPanel().getAccount());
		
		int index = smsgReason.indexOf("-");
		String reasonCodeString = smsgReason.substring(0, index);
		String closeReasonName = smsgReason.substring(index + 1);
		int closeReasonCode = Integer.parseInt(reasonCodeString);
		
		String handlingConclusionName = smsgTxt;
		String handlingDescription = smsgDetails;
		
		int sessionID = Integer.parseInt(sessionId);

		response = alertService.closeAlertSession(accountNumber, closeReasonCode, closeReasonName, handlingConclusionName, handlingDescription, sessionID);
		
		model.addAttribute("alerta", response);
		return response.getNumErr();
	}
	
	/**
	 * Alert Event Log
	 * @param model
	 * @param locale
	 * @param alertSessionId
	 * @return
	 */
	@RequestMapping(value="{sessionId}/eventLog", method = RequestMethod.GET)
	public String ShowEventLog(ModelMap model, Locale locale,@PathVariable("sessionId") String sessionId) {
		if (logger.isInfoEnabled())
			logger.info("Request of eventLog");
		User currentUser = (User) securityService.getPrincipal();
		Patient currentPatient = currentUser.getCurrentPatient();
		EventLog response = alertService.eventLog(currentUser.getId(), String.valueOf(currentPatient.getUserId()), sessionId, "0",currentUser.getLanguage().getLanguageKey(),currentUser.getUserType(), msgSrc, locale);
		
		// return notificationAlert with the alert now closed
		model.addAttribute("events", response);
		model.addAttribute("currentUserId", currentUser.getId());
		model.addAttribute("patientId", currentPatient.getUserId());
		model.addAttribute("sessionId", sessionId);
		
		return "eventsLog";	
	}
	
	/**
	 * Alert Pop Up
	 * This method returns the html code to raise a popUp window of the given alert
	 * @param model
	 * @param locale
	 * @param alertId
	 * @return
	 */
	@RequestMapping(value="{alertId}/showAlarm", method = RequestMethod.GET)
	public String ShowNotPopup(ModelMap model, Locale locale,@PathVariable("alertId") String alertId) {
		logger.info("getting detail data from {} into HTML", alertId);;
		User        currentUser = (User) securityService.getPrincipal();
		Alert alert = alertService.getAlert(alertId,currentUser.getLanguage().getLanguageKey());

		//Patient p = patientService.getPatient(alert.getPatient().getId());
		model.addAttribute("alertData", alert);
		//model.addAttribute("patientData", p);
		
		// The result message depends of the type & severity of event
//		if ((alert.getType() == Alert.AlertType.SOS)|| (alert.getType() == Alert.AlertType.FALL))
//			return "alarm";
//		else if ((alert.getType() == Alert.AlertType.EQUIPMENT)||(alert.getType() == Alert.AlertType.DOOR)||(alert.getType() == Alert.AlertType.USER))
//			return "alert";
//		else {
//			if (true) // TODO: Need a field to identify photos from others
//				return "photo";
//			else
				return "info";
//		}
	}

	/**
	 * This action shows a menu with the related actions the user can do when click over the icon of other user in the events log.
	 * 
	 * @param model
	 * @param locale
	 * @param userId
	 * @param sessionId
	 * @param msgId
	 * @return A modal template with the actions the logger user can do. 
	 */
	@RequestMapping(value="{sessionId}/take-action/{userId}", method = RequestMethod.GET)
	public String getPhoneUser(ModelMap model, Locale locale, 
			@PathVariable("userId") String userId, 
			@PathVariable("sessionId") String sessionId,
			@RequestParam(value="msgId", required=true) String msgId) {
		SystemUser user = userService.getUser(userId);
		boolean isLoggedUser = false;
		if (securityService.getPrincipal().getUserId() == user.getUserId()) {
			isLoggedUser = true;
		}
		
		HashMap<String, HCCommand> commands = (HashMap<String, HCCommand>) userService.getCommands(String.valueOf(securityService.getPrincipal().getUserId()), 
				securityService.getPrincipal().getCurrentPatient().getSystemStatus().getStatusType());
		
		if (commands.keySet().contains("broadcast")) {
			model.addAttribute("hasBroadcastMessage", true);
		}
		
		model.addAttribute("user", user);
		model.addAttribute("msgId", msgId);
		model.addAttribute("sessionId", sessionId);
		model.addAttribute("isLoggedUser", isLoggedUser);
		
		return "callUser";
	}
	
	@ExceptionHandler(AuthenticationException.class)
	public void handleAuthenticationException(Exception ex, HttpServletRequest request, HttpServletResponse response) throws IOException {
		
		logger.error("\n");
		logger.error("------------- AUTHENTICATION EXCEPTION DETECTED -------------");
		logger.error("\n- Error {}, while processing request: {}.", ex.getMessage(), request.getRequestURI());
		logger.error("-----------------------------------------------------");
		logger.error("\n");
		
		User currentUser = (User) securityService.getPrincipal();
		
		if(currentUser instanceof ThirdPartyUser){
			request.getSession().invalidate();
			response.sendRedirect(((ThirdPartyUser) currentUser).getErrorRedirectUrl());
		}else{
			request.getSession().invalidate();
			response.sendError( HttpServletResponse.SC_UNAUTHORIZED);
		}
	}
	
	@ExceptionHandler(Exception.class)
	public String handleException(Exception ex, HttpServletRequest request) {
		logger.error("\nError Handling Alert\n",ex);
		ex.printStackTrace();
	    return ex.getClass().getSimpleName();
	  }
	
	
}
