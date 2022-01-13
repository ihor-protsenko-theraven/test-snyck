package com.essence.hc.service.impl;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.regex.Pattern;
import java.util.regex.Matcher;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import com.essence.hc.model.Alert;
import com.essence.hc.model.AlertCloseReason;
import com.essence.hc.model.EventLog;
import com.essence.hc.model.LowActivityReport;
import com.essence.hc.model.Panel;
import com.essence.hc.model.Patient;
import com.essence.hc.model.Report.ReportType;
import com.essence.hc.model.ResponseStatus;
import com.essence.hc.model.SesLogFrm;
import com.essence.hc.model.StepCountAvailableReport;
import com.essence.hc.model.Alert.AlertState;
import com.essence.hc.model.User.UserType;
import com.essence.hc.model.User;
import com.essence.hc.model.Vendor.ServiceType;
import com.essence.hc.persistence.AlertDAO;
import com.essence.hc.persistence.ExternalDAO;
import com.essence.hc.persistence.UserDAO;
import com.essence.hc.persistenceexternal.ExternalAlertDAO;
import com.essence.hc.service.AlertService;

@Service
public class AlertServiceImpl implements AlertService {

	protected final Logger logger = LoggerFactory.getLogger(getClass());
	
//	@Autowired
//	RequestProcessor reqProcessor;
	@Autowired
	private AlertDAO dao;
	@Autowired
	private UserDAO userDAO;
	@Autowired
	private ExternalAlertDAO externalAlertDAO;
		
	/* (non-Javadoc)
	 * @see com.essence.hc.service.AlertService#getAlert(java.lang.String)
	 */
	public Alert getAlert(String alertId,String languageKey) {
		logger.info("\nGetting Alert: {}\n", alertId);
		Alert alert = dao.getAlert(alertId,languageKey);
//		Alert alert = dao.getById(id);
//		logger.info("\nAlert Actions: {}\n", alert.getTakenActions());
		return alert;
	}

	/* (non-Javadoc)
	 * @see com.essence.hc.service.AlertService#getLowActivity(java.lang.String)
	 */
	public LowActivityReport getLowActivityReport(String alertId, String languageKey) {
		logger.info("\nGetting Low Activity Report for alert {}\n", alertId);
		return dao.getLowActivityReport(alertId,languageKey);
	}
	
	/* (non-Javadoc)
	 * @see com.essence.hc.service.AlertService#closeAlert(int, int)
	 */
	public ResponseStatus updateAlertState(int alertId, AlertState newState, int patientId) {
		return dao.updateAlertState(alertId, newState, patientId);
	}
	
	/* (non-Javadoc)
	 * @see com.essence.hc.service.AlertService#getAlertCloseReasons(String languageKey)
	 */
	public List<AlertCloseReason> getAlertCloseReasons(String languageKey) {
		return dao.getAlertCloseReasons(languageKey);
	}
	
	/* (non-Javadoc)
	 * @see com.essence.hc.service.closeAlertSession(java.lang.String)
	 */
	public ResponseStatus closeAlertSession(int accountNumber, int closeReasonCode, String closeReasonName, String handlingConclusionName, String handlingDescription, int sessionId){
		return externalAlertDAO.closeAlertSession(accountNumber, closeReasonCode, closeReasonName, handlingConclusionName, handlingDescription, sessionId);
	}
	
	/* (non-Javadoc)
	 * @see com.essence.hc.service.closeAlertSession(java.lang.String)
	 */
	public EventLog eventLog(String userId, String patientId, String sessionId, String logId,String languageKey,UserType userType,MessageSource msgSrc,Locale locale){
		
		EventLog log = dao.eventLog(userId, patientId, sessionId, logId,languageKey,userType);
		
		// Replace alert close reason translations
		if (log == null || log.getpFrm() == null)
			return log;
		List<AlertCloseReason> reasons = null;
		Pattern pattern = Pattern.compile("\\Q{\\E[^\\Q}\\E]*\\Q}\\E");
		for (SesLogFrm item: log.getpFrm()) {
			if (item.getsFuncTxt() != null) {
				for (int i = 0; i < item.getsFuncTxt().length; i++) {
					String funcText = item.getsFuncTxt()[i];
					Matcher matcher = pattern.matcher(funcText);
					while (matcher.find()) {
						if (reasons == null) {
							reasons = dao.getAlertCloseReasons(languageKey);
						}
						funcText = matcher.replaceFirst(closeReasonTranslation(matcher.group(), reasons, msgSrc, locale));
						item.getsFuncTxt()[i] = funcText;
						matcher = pattern.matcher(funcText);
					}
				}
			}
		}
		
		return log;
	}
	
	private String closeReasonTranslation(String reasonName, List<AlertCloseReason> reasons, MessageSource msgSrc, Locale locale) {
		reasonName = reasonName.substring(1, reasonName.length() -1);
		for (AlertCloseReason reason: reasons) {
			if (reasonName.equals(reason.getName())) {
				return reason.getText();
			}
		}
		try {
			return msgSrc.getMessage("history.reason." + reasonName, null, locale);
		} catch (Exception e) {
			logger.warn(e.getMessage());
			return reasonName;
		}
	}
	
	/* (non-Javadoc)
	 * @see com.essence.hc.service.AlertService#getAlertSession(java.lang.String, java.lang.String, java.lang.String)
	 */
	public List<Alert> getSessionAlerts(String userId, String patientId, String sessionId){
		return dao.getSessionAlerts(userId, patientId, sessionId);
	}
	
	/* (non-Javadoc)
	 * @see com.essence.hc.service.AlertService#saveAlert(com.essence.hc.model.Alert)
	 */
	@Override
	public void saveAlert(String userId, Alert alert){
		logger.info("Saving Alert {}", alert.getId());
		dao.save(alert);
		userDAO.saveAlert(userId, alert);
	}
	
	/* (non-Javadoc)
	 * @see com.essence.hc.service.PatientService#getPatientInfo(java.lang.String)
	 */
	@Override
	public List<Patient> getPatientsInAlarm(int userId, int vendorId,int rolId, String orderBy, Boolean sortAsc, ServiceType[] serviceTypeFilter, Boolean enableActiveService) {
		
		List<Patient> patientsInAlarm = dao.getPatientsInAlarm(userId, vendorId, rolId, orderBy, sortAsc, serviceTypeFilter, enableActiveService);
		
		return setAvailableReports(patientsInAlarm);
	}
	
	private List<Patient> setAvailableReports(List<Patient> patients) {
		
		for(Patient patient : patients) {
			patient.setAvailableReports(patient.obtainDefaultAvailableReports());
			if(isAvailableStepCountReport(patient)) {
				patient.addAvailableReport(ReportType.STEP_COUNT);
			}
		}
		
		return patients;
	}
	
	private boolean isAvailableStepCountReport(Patient patient) {
		
		if(patient.getActivityLevel() != null) {
			List<StepCountAvailableReport> stepCountReports = patient.getActivityLevel().getStepCountAvailableReport();
			
			StepCountAvailableReport stepCountReport = null;
			Iterator<StepCountAvailableReport> it = stepCountReports.iterator();
			boolean showStepCountReportOption = false;
			
			while(it.hasNext() && showStepCountReportOption == false){
				stepCountReport = it.next();
				if (stepCountReport.getFirstStepCount() != null){
					return true;
				}
			}
		}
		
		return false;
	}
	
	/* (non-Javadoc)
	 * @see com.essence.hc.service.PatientService#getAlertCount(java.lang.String)
	 */
	@Override
	public List<Patient> getAlertCount(User user) {
		return dao.getAlertCount(user);
	}

	@Override
	@PreAuthorize("isAuthenticated()")
	public List<ResponseStatus> GetManualAlertTypes() {
		return dao.GetManualAlertTypes();
	}
	
	@Override
	@PreAuthorize("isAuthenticated()")
	public ResponseStatus setManualAlert(int userID, int devicID, int alert) {
		return dao.setManualAlert(userID, devicID, alert);
	}
	@Override
	@PreAuthorize("isAuthenticated()")
	public Map<String, Boolean> getAlertConfiguration(String userId, String userType, String residentId){
		return dao.getAlertConfiguration(userId, userType,residentId);
	}

	/* OLD METHOD
	 * @Override
	@PreAuthorize("isAuthenticated()")
	public ResponseStatus createManualAlert(Patient resident, String iAlrtType, String sAlertTxt, String deviceId) {
		return dao.createManualAlert(String.valueOf(resident.getUserId()), iAlrtType, sAlertTxt, deviceId);
//		return externalDao.createManualAlert(resident, iAlrtType, sAlertTxt, deviceId);
	}*/
	
	@Override
	@PreAuthorize("isAuthenticated()")
	public ResponseStatus createManualAlert(Patient resident, String iAlrtType, String sAlertTxt, String deviceId) 
	{
		 return externalAlertDAO.createManualAlert(resident, iAlrtType, sAlertTxt, deviceId);
	}

}
