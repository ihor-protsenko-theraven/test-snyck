package com.essence.hc.service.impl;

import java.util.LinkedHashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.essence.hc.model.ResponseStatus;
import com.essence.hc.model.Vendor.ServiceType;
import com.essence.hc.persistenceexternal.ExternalAccountDAO;
import com.essence.hc.persistenceexternal.ExternalRulesDAO;
import com.essence.hc.service.RulesService;

public class RulesServiceImpl implements RulesService {
	
	protected final Logger logger = LoggerFactory.getLogger(getClass());
	
	@Autowired
	private ExternalAccountDAO externalAccountDao;
	@Autowired
	private ExternalRulesDAO externalRulesDao;

	@Override
	public Object getDevices(String account) {
		return externalAccountDao.getDevices(account);
	}

	@Override
	public LinkedHashMap<Object, Object> getRulesPolicy(ServiceType serviceType) {
		LinkedHashMap<Object, Object> rulesPolicy = (LinkedHashMap<Object, Object>) externalRulesDao.getRulesPolicy(serviceType);
		return rulesPolicy;	
	}

	@Override
	public Object getAccountRules(String account, ServiceType st) {
		return externalRulesDao.getAccountRules(account, st);
	}
	
	@Override
	public ResponseStatus setAccountRulesRawBody(Object body) {
		return externalRulesDao.setAccountRulesRawBody(body);
	}

}
