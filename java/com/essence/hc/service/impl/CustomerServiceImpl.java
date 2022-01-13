/**
 * 
 */
package com.essence.hc.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;

import com.essence.hc.model.Action;
import com.essence.hc.model.Language;
import com.essence.hc.service.ActionService;
import com.essence.hc.service.InstallationService;
import com.essence.hc.service.UserService;

/**
 * @author oscar.canalejo
 *
 */
public class CustomerServiceImpl implements InitializingBean, DisposableBean{
	
	@Autowired
	UserService userService;
	@Autowired
	InstallationService installationService;
	
	private static Logger logger = LoggerFactory.getLogger("Config");
	
	public void afterPropertiesSet() throws Exception {
		System.out.println("\nInitial tasks running..................................................\n");
		
		// Languages List -> Now retrieved when user logins from the new API
		// Language.setLanguageList(userService.getLanguages());
		// System Version num
		String systemVersion = userService.getVersion();
		logger.info("SYSTEM VERSION.............{}", systemVersion);
		System.out.println("\nInitial tasks completed..................................................\n");
	}

	@Override
	public void destroy() throws Exception {
		// TODO Auto-generated method stub
		
	}
	

}
