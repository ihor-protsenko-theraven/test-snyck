/**
 * 
 */
package com.essence.hc.service;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.List;

import org.apache.commons.chain.Context;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.essence.hc.eil.requestprocessors.RequestProcessor;
import com.essence.hc.model.Action;
import com.essence.hc.model.ResponseStatus;
import com.essence.hc.persistence.exceptions.DataAccessException;

/**
 * @author oscar.canalejo
 *
 */
@Service
public class SendMessageAction extends Action {

	private static final long serialVersionUID = 1L;
	protected final Logger logger = LoggerFactory.getLogger(getClass());

	@Autowired
	@Qualifier("requestProcessor")
	RequestProcessor reqProcessor;
	@Autowired
	UserService userService;
	
	/*
	 * Attributes
	 */
	private String message;
	private String recipientUserId;
	private String privateId;
	private List<String> predefinedTexts;
	

	
	@Override
	public synchronized void init() {
		super.init();
		if (attributes != null && !attributes.isEmpty()) {
			this.message = attributes.get("text-msg");
			this.recipientUserId = attributes.get("recipientUserId");
			this.privateId = attributes.get("privateId");
			if (this.message == null || this.message.isEmpty()) {
				this.message = attributes.get("radio-msg");
			}
		}
	}

	@Override
	public synchronized void getData() {
		this.predefinedTexts = userService.getPredefinedTextByCommandId(this.name);
	}
	
	@Override
	public synchronized boolean execute(Context context) throws Exception {

		logger.info("\nExecuting Action 'Send Message'...");
		super.execute(context);
	
		HashMap<String, String> reqParams = new HashMap<String, String>();
		reqParams.put("iparentId", this.parentId);
		reqParams.put("iUsr", this.author.getId());
		reqParams.put("iPtient", this.patientId);
		reqParams.put("iSession", this.alertId);
		try {
//			reqParams.put("smsgTxt", URLEncoder.encode("test","UTF-8"));
			reqParams.put("smsgTxt", URLEncoder.encode(this.message,"UTF-8"));
		} catch (UnsupportedEncodingException e) {
			throw new DataAccessException(e);
		}
		reqParams.put("sType", "broadcast");
		reqParams.put("iPrivateId", this.privateId);
		
		ResponseStatus resp = reqProcessor.performRequest(ResponseStatus.class, "broadcast_message", reqParams);
		
		return resp.getNumErr() >= 0;
		
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getRecipientUserId() {
		return recipientUserId;
	}

	public void setRecipientUserId(String recipientUserId) {
		this.recipientUserId = recipientUserId;
	}

	public List<String> getPredefinedTexts() {
		return predefinedTexts;
	}

	public void setPredefinedTexts(List<String> predefinedTexts) {
		this.predefinedTexts = predefinedTexts;
	}

	public String getPrivateId() {
		return privateId;
	}

	public void setPrivateId(String privateId) {
		this.privateId = privateId;
	}


}
