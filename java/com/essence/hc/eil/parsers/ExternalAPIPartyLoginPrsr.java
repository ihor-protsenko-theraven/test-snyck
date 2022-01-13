package com.essence.hc.eil.parsers;

import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.dozer.DozerBeanMapper;
import org.dozer.Mapper;
import org.apache.logging.log4j.Logger;
import org.slf4j.LoggerFactory;

import com.essence.hc.eil.exceptions.ParseException;
import com.essence.hc.model.ThirdPartyUser;
import com.essence.hc.model.User;

@JsonIgnoreProperties(ignoreUnknown = true)
public class ExternalAPIPartyLoginPrsr implements IParser<ThirdPartyUser>{

	
	//private UserLoginPrsr user;
	private Map<String, String> externalPartyLoginResponseData;
	private String token;
	
	protected final Logger logger =(Logger) LogManager.getLogger(ExternalAPIPartyLoginPrsr.class);
	
	public ExternalAPIPartyLoginPrsr() {
	}
	
	@Override
	public ThirdPartyUser parse() {
		
		ThirdPartyUser thirdPartyUser = new ThirdPartyUser();
		
		try{
			
			thirdPartyUser.setLogoutRedirectUrl(this.externalPartyLoginResponseData.get("logoutRedirectUrl"));
			thirdPartyUser.setErrorRedirectUrl(this.externalPartyLoginResponseData.get("errorRedirectUrl"));
			thirdPartyUser.setExternalPartyAuthenticationProviderType(this.externalPartyLoginResponseData.get("externalPartyAuthenticationProviderType"));
			
			thirdPartyUser.setToken(this.token);
		
		}catch(Exception ex){
			logger.error("Error parsing ExternlAPIPartyLogin");
			throw new ParseException(ex,"Unexpected parse error");
		}
		
		return thirdPartyUser;
	}

	/*public UserLoginPrsr getUser() {
		return user;
	}

	public void setUser(UserLoginPrsr user) {
		this.user = user;
	}*/

	public Map<String, String> getExternalPartyLoginResponseData() {
		return externalPartyLoginResponseData;
	}

	public void setExternalPartyLoginResponseData(Map<String, String> externalPartyLoginResponseData) {
		this.externalPartyLoginResponseData = externalPartyLoginResponseData;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}
	
	
	
}
