package com.essence.hc.persistence.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import com.essence.hc.eil.requestprocessors.RequestProcessor;
import com.essence.hc.eil.requestprocessors.impl.DummyRequestProcessor;
import com.essence.hc.model.User;
import com.essence.hc.model.User.CaregiverType;
import com.essence.hc.model.User.UserType;
import com.essence.hc.persistence.ExternalDAO;
import com.essence.security.SecurityService;

public class ExternalDAOImpl implements ExternalDAO {
	
	protected final Logger logger = LoggerFactory.getLogger(getClass());
	
	@Autowired
	@Qualifier("requestProcessorExternal")
	protected RequestProcessor reqProcessor;
	
	@Autowired
	SecurityService securityService;
	
	protected <T> T performRequest(Class<T> resp, String reqName, HashMap<String, ?> reqParams, HashMap<String, String> reqHeaders) {
		return reqProcessor.performRequest(resp, reqName, reqParams, reqHeaders);
	}
	
	protected <T> T performRequest(Class<T> resp, String reqName, HashMap<String, ?> reqParams) {
		HashMap<String, String> reqHeaders = createTokenHeader();
		return reqProcessor.performRequest(resp, reqName, reqParams, reqHeaders);
	}
	
	protected <T> T performDummyRequest(Class<T> resp, String reqName, HashMap<String, ?> reqParams) {
		RequestProcessor dummyReqProcessor = new DummyRequestProcessor();
		HashMap<String, String> reqHeaders = createTokenHeader();
		return dummyReqProcessor.performRequest(resp, reqName, reqParams, reqHeaders);
	}
	
	protected Object performUnparsedRequest(String reqName, HashMap<String, ?> reqParams) {
		HashMap<String, String> reqHeaders = createTokenHeader();
		return reqProcessor.performUnparsedRequest(reqName, reqParams, reqHeaders);
	}
	
	public Object performUnparsedDummyRequest(String reqName, HashMap<String, ?> reqParams) {
		RequestProcessor dummyReqProcessor = new DummyRequestProcessor();
		HashMap<String, String> reqHeaders = createTokenHeader();
		return dummyReqProcessor.performUnparsedRequest(reqName, reqParams, reqHeaders);
	}
	
	protected <T> T performRequestRawBody(Class<T> resp,String reqName, Object body) {
		HashMap<String, String> reqHeaders = createTokenHeader();
		return reqProcessor.performRequestRawBody(resp, reqName, body, reqHeaders);
	}
	
	private <T> T performDummyRequestRawBody(Class<T> resp,String reqName, Object body) {
		RequestProcessor dummyReqProcessor = new DummyRequestProcessor();
		HashMap<String, String> reqHeaders = createTokenHeader();
		return dummyReqProcessor.performRequestRawBody(resp, reqName, body, reqHeaders);
	}
	
	private HashMap<String, String> createTokenHeader() {	
		User currentUser = securityService.getPrincipal();
		if (currentUser != null) {
			String token = currentUser.getToken();
			if (token != null) {
				return createTokenHeader(token);
			}
		}
		return null;
	}
	
	private HashMap<String, String> createTokenHeader(String token) {
		HashMap<String, String> reqHeaders = new HashMap<String, String>();
		reqHeaders.put("Authorization", "Token " + token);
		return reqHeaders;
	}

	
	protected List<String> userTypeFilters(List<UserType> userTypes) {
		List<String> result = new ArrayList<String>();
		for (UserType t:userTypes) {
			switch(t) {
			case ROLE_ADMIN:
				result.add("Administrator");
				break;
			case ROLE_CAREGIVER:
				result.add("CareGiver");
				break;
			case ROLE_MONITORED:
				result.add("Resident");
				break;
			}
		}
		return result;
	}
	
	protected List<String> caregiverTypeFilters(List<CaregiverType> caregiverTypes) {
		List<String> result = new ArrayList<String>();
		for (CaregiverType t:caregiverTypes) {
			switch(t) {
			case MASTER:
				result.add("MasterCareGiver");
				break;
			case STANDARD:
				result.add("StandardCareGiver");
				break;
			}
		}
		return result;
	}

}
