package com.essence.hc.controllers;

import java.io.IOException;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.essence.hc.eil.exceptions.AuthenticationException;
import com.essence.hc.model.Patient;
import com.essence.hc.model.ResponseStatus;
import com.essence.hc.model.RuleValidationError;
import com.essence.hc.model.RuleValidationErrorResponseStatus;
import com.essence.hc.model.ThirdPartyUser;
import com.essence.hc.model.User;
import com.essence.hc.model.Vendor.ServiceType;
import com.essence.hc.service.RulesService;
import com.essence.hc.util.Util;
import com.essence.security.SecurityService;

/**
 * Controller for rules management
 * 
 * @author ruben.sanchez
 *
 */
@Controller
@RequestMapping("/admin/rules")
public class RulesController {

	protected final Logger logger = (Logger) LogManager.getLogger(RulesController.class);

	@Autowired
	private SecurityService securityService;
	@Autowired
	private RulesService rulesService;

	@RequestMapping(value = "policy", method = RequestMethod.GET, produces = "application/json")
	public @ResponseBody Object getRulesPolicy() {

		final ServiceType serviceType = securityService.getPrincipal().getCurrentPatient().getServiceType();

		Map<Object, Object> map = rulesService.getRulesPolicy(serviceType);

		// Extract serviceType from the response: Response has not null
		map = (Map) map.get("policies");

		if (map.containsKey(Util.mapFromOldToNewServicePackageNaming(serviceType.getName()))) {
			return map.get(Util.mapFromOldToNewServicePackageNaming(serviceType.getName()));
		}

		throw new RuntimeException(
				String.format("We were expecting a response for the GetRulesPolicy request containing an %s field.",
						serviceType.getName()));
	}

	@RequestMapping(value = "{panelId}", method = RequestMethod.GET, produces = "application/json")
	public @ResponseBody Object getAccountRules(@PathVariable("panelId") String panelId) {
		Patient resident = securityService.getPrincipal().getCurrentPatient();
		String account = resident.getInstallation().getPanel().getAccount();
		ServiceType st = resident.getServiceType();
		return rulesService.getAccountRules(account, st);
	}

	@RequestMapping(value = "{panelId}/devices", method = RequestMethod.GET, produces = "application/json")
	public @ResponseBody Object getDevices(@PathVariable("panelId") String panelId) {
		
		// EIC15-2678 PanelId is no longer necessary, but in order not break the client we kept the endpoints
		
		Patient resident = securityService.getPrincipal().getCurrentPatient();
		String account = resident.getInstallation().getPanel().getAccount();
		
		return rulesService.getDevices(account);
	}

	@RequestMapping(value = "set-rules", method = RequestMethod.POST)
	public ResponseEntity<String> setAccountRules(@RequestBody Object body) {

		Map m = (Map) body;

		Patient resident = securityService.getPrincipal().getCurrentPatient();
		String account = resident.getInstallation().getPanel().getAccount();

		final ServiceType serviceType = resident.getServiceType();

		m.put("serviceType", serviceType.getName());
		if (StringUtils.hasText(account)) {
			m.put("accountIdentifier", account);
		}

		ResponseStatus status = rulesService.setAccountRulesRawBody(body);
		if (status.isOK()) {
			return new ResponseEntity<String>("OK", HttpStatus.OK);
		} else {

			if (status.getNumErr() == 76) {
				// Validation error

				if (status instanceof RuleValidationErrorResponseStatus) {

					RuleValidationErrorResponseStatus rs = (RuleValidationErrorResponseStatus) status;
					List<RuleValidationError> errors = rs.getRuleValidationErrors();

					// Build response based on errors
					if (errors != null && errors.size() > 0) {
						// Acording CareAtHome 2.4.5ProRulesHLD-0.55 3 exception
						// types, just 1 validationerror
						RuleValidationError error = errors.get(0);

						if (error.getValidatedEntity().equals("Rule")
								&& error.getValidationCategory().equals("MaxQuantity")
								&& error.getValidationSubCategory().equals("SpecificRule")) {
							// specific rule reached its limit description
							return new ResponseEntity<String>(status.getMessageErr(), HttpStatus.NOT_ACCEPTABLE); // 406
						} else if (error.getValidatedEntity().equals("Rule")
								&& error.getValidationCategory().equals("MaxQuantity")
								&& error.getValidationSubCategory().equals("Rule")) {
							// with rule reached the limit permitted
							return new ResponseEntity<String>(status.getMessageErr(), HttpStatus.EXPECTATION_FAILED); // 417

						} else if (error.getValidatedEntity().equals("Rule")
								&& error.getValidationCategory().equals("WeekDays")
								&& error.getValidationSubCategory().equals("Value")) {
							// specific missing days of week description
							return new ResponseEntity<String>(status.getMessageErr(), HttpStatus.UNPROCESSABLE_ENTITY); // 422
						}
						else if (error.getValidatedEntity().equals("Rule")
								&& error.getValidationCategory().equals("Device")
								&& error.getValidationSubCategory().equals("Type")) {
							// specific missing days of week description
							return new ResponseEntity<String>(status.getMessageErr(), HttpStatus.I_AM_A_TEAPOT); // 418
						}
					}

					return new ResponseEntity<String>(status.getMessageErr(), HttpStatus.BAD_REQUEST);// 400
				} else {
					return new ResponseEntity<String>(status.getMessageErr(), HttpStatus.BAD_REQUEST);
				}

			}

			if (status.getNumErr() == 123) {
				logger.error(
						"ATTENTION: The requested account {} does not exist. This show some type of app inconsistency.",
						account);

			} else if (status.getNumErr() == 124) {
				logger.error(
						"ATTENTION: The authorization when consuming External API failed: no token, or token empty, invalid, or expired.");

			} else if (status.getNumErr() == 10) {
				logger.error("ATTENTION: Database error while saving data");

			} else if (status.getNumErr() == 1) {
				logger.error("ATTENTION: The request failed due to an unknow backend exception");

			} else if ((status.getNumErr() == 167) || (status.getNumErr() == -167)) {
				return new ResponseEntity<String>(status.getMessageErr(), HttpStatus.BANDWIDTH_LIMIT_EXCEEDED); // 509
			}
			
			// Otherwise: 10 or 1
			return new ResponseEntity<String>(status.getMessageErr(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	// VIEWS
	@RequestMapping(value = "/resident/{residentId}", method = RequestMethod.GET)
	public String showRulesList(ModelMap model, Locale locale, @PathVariable("residentId") String residentId) {

		Patient currentPatient = securityService.getPrincipal().getCurrentPatient();

		model.addAttribute("user", currentPatient);

		return "adminRules";
	}

	@RequestMapping(value = "/periods/{residentId}", method = RequestMethod.GET)
	public String showPeriodsList(ModelMap model, Locale locale, @PathVariable("residentId") String residentId) {

		Patient currentPatient = securityService.getPrincipal().getCurrentPatient();

		model.addAttribute("user", currentPatient);

		return "adminPeriods";
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
