/**
 * 
 */
package com.essence.hc.service;

import java.util.HashMap;

import org.apache.commons.chain.Context;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.essence.hc.eil.requestprocessors.RequestProcessor;
import com.essence.hc.model.Action;
import com.essence.hc.model.ResponseStatus;

/**
 * @author oscar.canalejo
 *
 */
@Service
public class CallPoliceDeptAction extends Action {

	private static final long serialVersionUID = 1L;
	protected final Logger logger = LoggerFactory.getLogger(getClass());

	@Autowired
	@Qualifier("requestProcessor")
	RequestProcessor reqProcessor;

	private String telephone;
	
	@Override
	public synchronized void init() {
		super.init();
	}

	@Override
	public synchronized boolean execute(Context context) throws Exception {
		HashMap<String, String> reqParams = new HashMap<String, String>();
		reqParams.put("vendorId", String.valueOf(this.author.getVendorId()));
		reqParams.put("userId", this.author.getId());
		reqParams.put("residentId", this.patientId);
		reqParams.put("sessionId", this.alertId);
		ResponseStatus resp = reqProcessor.performRequest(ResponseStatus.class, "get_police_telephone", reqParams);
		if (resp.getNumErr() >= 0) {
			this.telephone = resp.getMessageErr().trim();
			return true;
		}
			
		return false;
	}

	public String getTelephone() {
		return telephone;
	}

	public void setTelephone(String telephone) {
		this.telephone = telephone;
	}
}
