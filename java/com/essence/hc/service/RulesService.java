package com.essence.hc.service;

import java.util.LinkedHashMap;

import org.springframework.stereotype.Service;

import com.essence.hc.model.ResponseStatus;
import com.essence.hc.model.Vendor.ServiceType;

@Service
public interface RulesService {
	
	/**
	 * returns account rules
	 * @param panel
	 * @return
	 */
	public Object getDevices(String account);

	/**
	 * returns rules policy
	 * @return
	 */
	public LinkedHashMap<Object, Object> getRulesPolicy(ServiceType serviceType);
	
	/**
	 * returns account rules as raw JSON data
	 * @param resident
	 * @return
	 */
	public Object getAccountRules(String account, ServiceType st);
	
	/**
	 * sets account rules by sending raw JSON data
	 * @param body body of the request
	 * @return
	 */
	public ResponseStatus setAccountRulesRawBody(Object body);
	
}
