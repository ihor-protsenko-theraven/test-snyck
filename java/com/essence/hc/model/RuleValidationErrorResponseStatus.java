package com.essence.hc.model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.codehaus.jackson.map.DeserializationConfig.Feature;
import org.codehaus.jackson.map.ObjectMapper;
import org.springframework.util.StringUtils;

/**
 * ResposeStatus for a 76 code response status on a SetAccountRules. body is
 * required and with just a ResponseStatus, particular Validatoin Error was
 * missed.
 *
 */
public class RuleValidationErrorResponseStatus extends ResponseStatus {

	public RuleValidationErrorResponseStatus(ResponseStatus rs) {

		if (rs.getNumErr() != 76) {
			// We just cannot build this instance from a 76 response statu
			throw new IllegalArgumentException();
		}

		this.setMessageErr(rs.getMessageErr());
		this.setNumErr(rs.getNumErr());
		this.setOK(rs.isOK());

		ObjectMapper jsonMapper = new ObjectMapper();
		jsonMapper.configure(Feature.FAIL_ON_UNKNOWN_PROPERTIES, false);

		try {
			if (StringUtils.hasText(rs.getValue())) {

				// Data needs to be escaped, right now.
				String s = rs.getValue().replaceAll("\\\"", "\"");
				RuleValidationError[] rArr = jsonMapper.readValue(s, RuleValidationError[].class);

				this.ruleValidationErrors = new ArrayList<>();
				// Value is a String[]
				for (RuleValidationError rve : Arrays.asList(rArr)) {
					this.ruleValidationErrors.add(rve);
				}
			}
		} catch (Throwable e) {
			throw new RuntimeException(String.format("Can not parse this response: %s", rs));
		}
	}

	private List<RuleValidationError> ruleValidationErrors;

	public List<RuleValidationError> getRuleValidationErrors() {
		return ruleValidationErrors;
	}

	public void setRuleValidationErrors(List<RuleValidationError> ruleValidationErrors) {
		this.ruleValidationErrors = ruleValidationErrors;
	}

}
