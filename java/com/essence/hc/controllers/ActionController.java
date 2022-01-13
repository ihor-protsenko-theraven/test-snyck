package com.essence.hc.controllers;

import java.io.IOException;
import java.util.Locale;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringEscapeUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.ContextLoader;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.essence.hc.eil.exceptions.AuthenticationException;
import com.essence.hc.exceptions.AppRuntimeException;
import com.essence.hc.model.Action;
import com.essence.hc.model.HCCommand;
import com.essence.hc.model.Patient;
import com.essence.hc.model.ThirdPartyUser;
import com.essence.hc.model.User;
import com.essence.hc.service.ActionService;
import com.essence.hc.service.AlertService;
import com.essence.hc.service.UserService;
import com.essence.security.SecurityService;

/**
 * Main actions Controller
 * @author manuel.garcia
 *
 */

@Controller
@RequestMapping("/actions")
public class ActionController {
	
		protected final Logger logger = (Logger) LogManager.getLogger(ActionController.class);
	
	@Autowired
	private UserService userService;
	@Autowired
	private SecurityService securityService;
	@Autowired
	ActionService actionService;
	@Autowired
	AlertService alertService;

	private ApplicationContext appContext = ContextLoader.getCurrentWebApplicationContext();
	
	/**
	 * User Commands available by user role
	 * @param Model
	 * @return List of commands the user can do
	 */
	@RequestMapping(value="/list", method = RequestMethod.GET)
	public String getUserCommands(ModelMap model,
			@RequestParam(value="alertId",required=false) String alertId){
		
		logger.info("\ngetting actions for user\n");
		
		User currentUser = securityService.getPrincipal();
		Patient currentPatient = currentUser.getCurrentPatient();
//		List<String> messages = userService.getPredefinedTextByCommandId("call_police");
		
		Map<String, HCCommand> commands = userService.getCommands(currentUser.getId(), 
				currentPatient.getSystemStatus().getStatusType());
		model.addAttribute("commands", commands);
		model.addAttribute("alertId", alertId);
		model.addAttribute("tlPolice", currentUser.getTlfPolice());
		return "takeAction";
	}
	
	/**
	 * Creates and prepares 'actionCommand' model attribute
	 * before an action is processed
	 * 
	 * <p><i><b>NOTE: This method will be automatically executed 
	 * just before any other method call in this controller</b></i></p>
	 *   
	 * @param actionId Action required to be processed
	 * @param model The implicit view model object
	 * @return a ready for use instance of Action, binded to 'actionCommand' model attribute
	 */
//	@RequestMapping(value="{action_name}")
//	@ModelAttribute("actionCommand")
//	public Action prepareAction(@PathVariable(value="action_name") String actionName,
//								 @RequestParam(value="alert",required=false) String alertId,
//								 @RequestParam(value="parent",required=false) String parentActionId) {
//		
//		logger.info("\n\n Preparing Action {}\n\n", actionName);
//		/*
//		 * Looks for actionId in the user's command catalog
//		 * if found, gets the corresponding Action Bean from Spring's context
//		 * and prepares it.
//		 */
//		if (actionName != null){
//			try{
//				Map<String, HCCommand> commands = userService.getCommands(securityService.getPrincipal().getId());
//				if (commands != null && commands.containsKey(actionName)){
//					logger.debug("\nCommand found at user's catalog!\n");
//					Action action = (Action) appContext.getBean(actionName);
//					action.init();
//					action.setAuthor(securityService.getPrincipal());
//					if(alertId != null) action.setCause(alertService.getAlert(alertId));
//					action.setParentId(parentActionId);
//
//					return action;
//				}else{
//					logger.warn("\nCommand Not found at user's catalog\n");
//				}
//			}catch(Exception ex){
//				logger.warn("\nCommand Not Found or Missing Request Param 'action'\n");
//			}
//		}
//		return null;
//	}
	
//	/**
//	 * Get the Action's page
//	 * @return String representing the name of the view
//	 * which will be rendered for the specified actionId
//	 * 
//	 * <p><i>NOTE: The returned view name must be equals 
//	 * to the string specified in actionId Uri param </i></p>
//	 */
//	@RequestMapping(value="{action_name}/view", method=RequestMethod.GET)
//	public String action(ModelMap model, @PathVariable("action_name") String actionName,
//										@RequestParam(value="alertId",required=false) String alertId,
//										@RequestParam(value="parent",required=false) String parentActionId) {
//		
//		User currentUser = (User) securityService.getPrincipal();
//		Patient currentPatient = currentUser.getCurrentPatient();
//		logger.info("\n\n Requesting Action Page for Action {} \n\n", currentPatient.getSystemStatus().getAlertSessionId());
//		
//		
//		Action action = prepareAction(actionName, alertId, parentActionId);
//		model.addAttribute("alertId", currentPatient.getSystemStatus().getAlertSessionId());
//		model.addAttribute("action", action);
//		
////		List<Device> a = ((TakePhotoAction) action).getCameraList();
//		
//		return actionName;
//	}
//	
//	/**
//	 * Action execution
//	 * 
//	 * @param model Implicit view model object
//	 * @param actionId action id for the action required to execute
//	 * @return view name to be rendered
//	 */
//	@RequestMapping(value="{action_name}/execute", method = RequestMethod.GET)
//	public String takeAction(	@PathVariable("action_name") String actionName,
//								@RequestParam(value="alertId",required=false) String alertId,
//								@RequestParam(value="parent",required=false) String parentActionId,
//								@RequestParam(value="deviceId",required=false) String deviceId,
//								 RedirectAttributes ra) {
//		
//		Action action = prepareAction(actionName, alertId, parentActionId);
////		action.setDeviceId(deviceId);
//	
//		logger.info("\n\n Executing Action {} \n\n", actionName);
//		String redirect_to = "/home/handle_open_issue";
//		if(action.getAlertId() != null) {
//			redirect_to = "/alerts/" + action.getAlertId();
//		}
//		// Action Execution
//		actionService.execute(action);
//
//		
//		
////		return actionName;
//		return "redirect:/actions/list";
////		return actionName;
//    }
	
	/**
	 * Get the Action's page
	 * @return String representing the name of the view
	 * which will be rendered for the specified actionId
	 * 
	 * <p><i>NOTE: The returned view name must be equals 
	 * to the string specified in actionId Uri param </i></p>
	 */
	@RequestMapping(value="{action_name}/view", method=RequestMethod.GET)
	public String action(ModelMap model, @PathVariable("action_name") String actionName,
										@RequestParam(value="alertId",required=false) String alertId,
										@RequestParam(value="parentId",required=false) String parentActionId) {
		
		logger.info("\n\n Requesting Action Page for Action {} \n\n", actionName);
		User currentUser = (User) securityService.getPrincipal();
		Patient currentPatient = currentUser.getCurrentPatient();
		
		Action action = prepareAction(actionName, null);
		action.getData();
		
		model.addAttribute("alertId", currentPatient.getSystemStatus().getAlertSessionId());
		model.addAttribute("parentId", parentActionId);
		model.addAttribute("action", action);
		
		return actionName;
	}
	
	/**
	 * Action execution
	 * 
	 * @param actionName action to execute
	 * @param action_attributes map which contains the attribute values for action execution 
	 * @return view name to be rendered
	 */
	@RequestMapping(value="{action_name}/execute", method = RequestMethod.POST)
	public String takeAction(ModelMap model, @PathVariable("action_name") String actionName,
								@RequestParam(required=false) Map<String,String> action_attributes, 
								RedirectAttributes ra) {
		User currentUser = (User) securityService.getPrincipal();
		Patient currentPatient = currentUser.getCurrentPatient();
	
		logger.info("\n\n Executing Action {} \n\n", actionName);

		actionName = StringEscapeUtils.escapeHtml4(actionName);
		Action action = prepareAction(actionName, action_attributes);
		action.getData();
		boolean result = actionService.execute(action);
		
		model.addAttribute("action", action);
		model.addAttribute("name_patient", currentPatient.getFirstName());
		
		
		return actionName + ( result ? "_success" :  "_fail" );
    }	
	

	/**
	 * Action execution Call Police
	 * 
	 * @param actionName action to execute
	 * @param action_attributes map which contains the attribute values for action execution 
	 * @return boolean
	 */
	@RequestMapping(value="{action_name}/call", method = RequestMethod.GET, produces="application/json")
	public @ResponseBody boolean takeActionCall(ModelMap model, Locale locale,@PathVariable("action_name") String actionName,
			@RequestParam(required=false) Map<String,String> action_attributes, 
			RedirectAttributes ra){
		
			User currentUser = (User) securityService.getPrincipal();
			Patient currentPatient = currentUser.getCurrentPatient();
			logger.info("\n\n Executing Action {} \n\n", actionName);
			action_attributes.put("sessionId", String.valueOf(currentPatient.getSystemStatus().getAlertSessionId()));
			Action action = prepareAction(actionName, action_attributes);
			action.getData();
			boolean result = actionService.execute(action);
				
			return result;
	}
	
	
	/**
	 * Loads, initializes and prepares action for been used 
	 * @param actionName action identification
	 * @return {@link Action}
	 */
	private Action prepareAction(String actionName, Map<String,String> action_attributes) {

		logger.info("\n\n Preparing Action {}\n\n", actionName);
		/*
		* Looks for actionId in the user's command catalog
		* if found, gets the corresponding Action Bean from Spring's context
		* and prepares it.
		*/
		if (actionName != null){
			try{
				/*
				 * Check action is in user's commands
				 */
				User currentUser = securityService.getPrincipal();
				Patient currentPatient = currentUser.getCurrentPatient();
				
				
				Map<String, HCCommand> commands = userService.getCommands(currentUser.getId(), 
						currentPatient.getSystemStatus().getStatusType());
				
				if (commands != null && commands.containsKey(actionName)){
					/*
					 * Load and initialization of Action bean
					 */
					logger.info("\nCommand found at user's catalog!\n");
					Action action = (Action) appContext.getBean(actionName);
					action.setName(actionName);
					action.setAttributes(action_attributes);
					action.init();
					
					return action;
				}else{
					logger.info("\nCommand Not found at user's catalog\n");
					throw new AppRuntimeException("User cannot execute that action");
				}
			}catch(Exception ex){
				logger.info("\nAction Not Found or Missing Request Param 'action'\n");
				ex.printStackTrace();
			}
		}
		return null;
	}		
	
//	/**
//	 * Creates and prepares 'actionCommand'
//	 * before an action is processed
//	 * 
//	 *   
//	 * @param actionId Action required to be processed
//	 * @param model The implicit view model object
//	 * @return a ready for use instance of Action, binded to 'actionCommand' model attribute
//	 */
//	private Action prepareAction(String actionName, String alertId, String parentActionId) {
//
//		logger.info("\n\n Preparing Action {}\n\n", actionName);
//		/*
//		* Looks for actionId in the user's command catalog
//		* if found, gets the corresponding Action Bean from Spring's context
//		* and prepares it.
//		*/
//		if (actionName != null){
//			try{
//				User currentUser = securityService.getPrincipal();
//				Patient currentPatient = currentUser.getCurrentPatient();
//				Map<String, HCCommand> commands = userService.getCommands(currentUser.getId(), 
//						currentPatient.getSystemStatus().getStatusType());
//				if (commands != null && commands.containsKey(actionName)){
//					logger.debug("\nCommand found at user's catalog!\n");
//					Action action = (Action) appContext.getBean(actionName);
////					action.init();
//					action.setAuthor(securityService.getPrincipal());
//					logger.info("AlertId -------------------------------------------------->{}",alertId);
//					if(alertId != null) {
////						action.setCause(alertService.getAlert(alertId));
//						action.setAlertId(alertId);
//					}
//					action.setParentId(parentActionId);
//					action.init();
//					return action;
//				}else{
//					logger.warn("\nCommand Not found at user's catalog\n");
//				}
//			}catch(Exception ex){
//				logger.warn("\nCommand Not Found or Missing Request Param 'action'\n");
//				ex.printStackTrace();
//			}
//		}
//			return null;
//	}
	
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
	public void handleException(Exception ex, HttpServletRequest request) throws Exception {
		logger.error("\nError Handling Action\n",ex);
		throw ex;
	}

}
