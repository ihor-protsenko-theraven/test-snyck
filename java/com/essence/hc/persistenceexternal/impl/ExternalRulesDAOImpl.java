package com.essence.hc.persistenceexternal.impl;

import java.util.HashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.essence.hc.model.ResponseStatus;
import com.essence.hc.model.RuleValidationErrorResponseStatus;
import com.essence.hc.model.Vendor.ServiceType;
import com.essence.hc.persistence.impl.ExternalDAOImpl;
import com.essence.hc.persistenceexternal.ExternalRulesDAO;
import com.essence.hc.util.Util;

public class ExternalRulesDAOImpl extends ExternalDAOImpl implements ExternalRulesDAO {
	
	final Logger logger = LoggerFactory.getLogger(getClass());
	
	@Override
	public Object getRulesPolicy(ServiceType serviceType) {
		HashMap<String, Object> reqParams = new HashMap<String, Object>();
		if (serviceType != null) {
			reqParams.put("serviceTypeFilter", new String[] { Util.mapFromOldToNewServicePackageNaming(serviceType.getName()) });
			logger.debug("GetRulesPolicy - Filtering response per service type: {}", serviceType.getName());
		}
		return performUnparsedRequest("external_get_rules_policy", reqParams);
	}

	@Override
	public Object getAccountRules(String account, ServiceType st) {
		HashMap<String, Object> reqParams = new HashMap<String, Object>();
		reqParams.put("accountIdentifier", account);
		reqParams.put("serviceType", Util.mapFromOldToNewServicePackageNaming(st.getName()));
		return performUnparsedRequest("external_get_account_rules", reqParams);
	}

	@Override
	public ResponseStatus setAccountRulesRawBody(Object body) {
		ResponseStatus rs = performRequestRawBody(ResponseStatus.class, "external_set_account_rules", body);
		if (rs.getNumErr() == 76) {
			rs = new RuleValidationErrorResponseStatus(rs);
		}
		return rs;
	}
}
