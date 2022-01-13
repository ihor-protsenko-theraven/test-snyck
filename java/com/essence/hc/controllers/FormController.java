package com.essence.hc.controllers;

import java.util.Locale;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.SessionAttributes;

import com.essence.hc.controllers.javaBeans.FormBean;
import com.essence.hc.model.Alert;
import com.essence.hc.model.Alert.AlertType;
import com.essence.hc.model.Patient;
import com.essence.hc.service.AlertService;
import com.essence.hc.util.Util;
import com.essence.security.SecurityService;

@Controller
@RequestMapping("/alert_form")
@SessionAttributes("formBean")
public class FormController {

	@Autowired
	private AlertService alertService;
	@Autowired
	private SecurityService securityService;

	protected final Logger logger = (Logger) LogManager.getLogger(FormController.class);
	
	// Invoked initially to create the "form" attribute
	// Once created the "form" attribute comes from the HTTP session (see @SessionAttributes)
	@ModelAttribute("formBean")
	public FormBean createFormBean() {
		return new FormBean();
	}
	
	@RequestMapping(method=RequestMethod.GET)
	public void alert_form() {
	}

	@RequestMapping(method=RequestMethod.POST)
	public String processSubmit(FormBean formBean, BindingResult result, Model model) {

		logger.info("\n\nSaving New Alert....\n\n");
		
		if (result.hasErrors()) {
			return null;
		}

		Patient patient = new Patient();
		patient.setUserId(Integer.parseInt(formBean.getPatientId()));
	
		Alert alert = new Alert(patient, AlertType.valueOf(formBean.getType()));
		alert.setTitle(formBean.getDescription());
		alert.setName(alert.getType().toString().toLowerCase());
//		alert.setCurrentState(AlertState.NEW);
//		alert.setStartDateTime(new Date());
//		alert.setEndDateTime(new Date());
//		alert.setType(AlertType.valueOf(formBean.getType()));
//		alert.setPatient(patient);
		
		alertService.saveAlert(securityService.getPrincipal().getId(),alert);
		
//		logger.info("patientId: {}",formBean.getPatientId());
//		logger.info("description: {}",formBean.getDescription());
//		logger.info("start: {}",formBean.getStartDateTime());
//		logger.info("end: {}",formBean.getEndDateTime());
//		logger.info("type: {}",formBean.getType());
		
		// Typically you would save to a db and clear the "form" attribute from the session 
		// via SessionStatus.setCompleted(). For the demo we leave it in the session.
//		String message = "Form submitted successfully.  Bound " + formBean;
		// Success response handling
		
		// store a success message for rendering on the next request after redirect
		// redirect back to the form to render the success message along with newly bound values
//		redirectAttrs.addFlashAttribute("message", message);
		return "redirect:/alert_form";			
	}
	
}
