/**
 * 
 */
package com.essence.hc.service;

import org.apache.commons.chain.Context;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.essence.hc.eil.requestprocessors.RequestProcessor;
import com.essence.hc.model.Action;
import com.essence.hc.model.Alert;
import com.essence.hc.model.Alert.AlertType;
import com.essence.hc.util.Util;
import com.essence.hc.model.Patient;

/**
 * @author oscar.canalejo
 *
 */
@Service
public class CallAmbulanceAction extends Action {

	private static final long serialVersionUID = 1L;
	protected final Logger logger = LoggerFactory.getLogger(getClass());

	@Autowired
	@Qualifier("requestProcessor")
	RequestProcessor reqProcessor;
	@Autowired
	AlertService alertService;

	private String telephone;
	
	public CallAmbulanceAction() {
		this.telephone = "112";
	}

	/* (non-Javadoc)
	 * @see org.apache.commons.chain.Command#execute(org.apache.commons.chain.Context)
	 */
	@Override
	public synchronized boolean execute(Context context) throws Exception {
		
		logger.info("\nExecuting Action 'Call Ambulance'...");
		super.execute(context);
		
		/**
		 * TODO: this whole action should be Transactional
		 */
		
		/*
		 * TODO
		 * Step 1: Action must be reported to Essence API
		 */
		/*
			HashMap<String, String> reqParams = new HashMap<String, String>();
			reqParams.put("user", author.getId());
			reqParams.put("alert", cause.getId());
			reqParams.put("parent", parentId);
			Response resp = reqProcessor.performRequest(Response.class, "call_fire_dept", reqParams);
			if (resp.hasError()){
				throw new RemoteIntegrationException(resp.getResponseCode()); 
			}
		 */
		
		/*
		 * TODO: 
		 * Step 2: ¿¿ should we save this action in a user's actions cache ??
		 */
		
		/* 
		 * Step 3: Cause Alert Update/Create
		 */
		Patient patient = this.author.getCurrentPatient(); //patientService.getPatient(this.getAuthor().getCurrentPatient().getId());
		if(this.cause != null) {
			cause.addAction(this);
		}else{
			this.cause = new Alert(patient, AlertType.USER);
			this.cause.setTitle(this.title);
			this.cause.setName(this.name);
		}
		alertService.saveAlert(this.author.getId(), (Alert) this.cause);
		
		return true;
	}

	public String getTelephone() {
		return telephone;
	}

	public void setTelephone(String telephone) {
		this.telephone = telephone;
	}

}
