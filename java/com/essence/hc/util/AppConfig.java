/**
 * 
 */
package com.essence.hc.util;

import java.util.Arrays;
import java.util.List;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.essence.hc.model.User.UserType;

/**
 * Application Settings and Configuration Management
 * 
 * @author oscar.canalejo
 *
 */
public class AppConfig {

	private static Logger logger = LoggerFactory.getLogger("AppConfig");
	
//	private static final String API_URIS = "api_uris.properties";
//	private static final String API_PARSERS = "api_parsers.properties";
	private static final String CONFIG_FILE = "config.properties";
	private static final String EXPIRATION_KEY = "expiration_time.";
	private static final long EXPIRATION_DEFAULT = -1;
	private static final String ALERT_STATUS_INTERVAL_KEY = "alert_status.update_interval";
	private static final String ALERT_STATUS_INTERVAL_DEFAULT = "30000";
	private static final String DEBUG_KEY = "debug_mode";	
	private static final String APP_VERSION_KEY = "app_version";
	private static final String APP_VERSION_DEFAULT = "2.0_99999";
	
	private static final String TRUSTED_LOGIN_REQUEST_SITES_KEY = "trusted_login_request_sites";
	private static final String TRUSTED_LOGIN_REQUEST_SITES_DEFAULT = "";
	
	private static Properties props;
	
	
	/**
	 * Returns the application version number
	 * @return a string which identifies the app version 
	 * plus the latest version control repository revision number  
	 */
	public static String getVersion() {
		try {
			return getConfigValue(APP_VERSION_KEY, APP_VERSION_DEFAULT);
			
		}catch(Exception ex) {
			logger.error("Error while getting app version. Returning default value.", ex);
		}
		return APP_VERSION_DEFAULT;
	}
	
	/**
	 * Returns the System Setting for Debug Mode
	 * @return true if Debug Mode is On. false otherwise
	 */
	public static boolean getDebug() {
		boolean debug = false;
		String value = null; 
		try { 
			value = getConfigValue(DEBUG_KEY, "false");
			debug = Boolean.valueOf(value);
//			logger.info("--------------------------- debug = {}", debug);
			
		}catch(Exception ex) {
			logger.error("Error en getDebug(). Returning default value.", ex);
		}
		return debug;
	}
	
	/**
	 * Returns the app config/settings value for
	 * Session Expiration Time for a given user's role
	 * @param userType
	 * @return expiration time in milliseconds. 
	 * A -1 value means that session doesn't expire
	 */
	public static long getSessionExpirationTime(UserType userType) {
		try {  
			return Long.parseLong(getConfigValue(EXPIRATION_KEY + userType, String.valueOf(EXPIRATION_DEFAULT)));
			
		}catch(Exception ex) {
			logger.error("Error while getting session expiration time. Returning default value.", ex);
		}
		return EXPIRATION_DEFAULT;
	}
	
	
	/**
	 * Returns the app config/settings value for
	 * Alert Status Refresh Interval in milliseconds
	 * @return alert status refresh interval
	 */
	public static long getAlertStatusInterval() {
		String interval = null;
		try {  
			interval = getConfigValue(ALERT_STATUS_INTERVAL_KEY, ALERT_STATUS_INTERVAL_DEFAULT);
		}catch(Exception ex) {
			interval = ALERT_STATUS_INTERVAL_DEFAULT;
			logger.error("Error while getting alert status refresh interval. Returning default value.", ex);
		}
		return Long.parseLong(interval);
	}
	
	/**
	 * Returns the trusted sited allowed to required login bypass
	 * @return comma separated string with trusted origins
	 */
	public static String getTrustedRequestLoginSites(){
	
		try {
			
			String configValue = getConfigValue(TRUSTED_LOGIN_REQUEST_SITES_KEY, TRUSTED_LOGIN_REQUEST_SITES_DEFAULT);
			return configValue;
			
		}catch(Exception ex) {
			logger.error("Error while getting trusted login request sites. Returning default value.", ex);
		}
		
		return TRUSTED_LOGIN_REQUEST_SITES_DEFAULT;
	}
	
	
	/**
	 * Loads the configuration/settings file
	 */
	private static void loadConfigFile(){
		logger.info("--------------------------- Loading Config File ------------------------------");
		
		props = new Properties();
		try{
			props.load(AppConfig.class.getClassLoader().getResourceAsStream(CONFIG_FILE));
	
		}catch(Exception ex){
			logger.error("Unable to load config properties !!", ex);
		}
	}
	
	/**
	 * Searches for the property with the specified key and returns its value 
	 * If the key is not found the default value argument will be returned
	 * @param key
	 * @param defaultValue
	 * @return value associated with the given configuration key
	 */
	private static String getConfigValue(String key, String defaultValue) {
		if (props == null)
			loadConfigFile();
		
		return props.getProperty(key, defaultValue);
	}
	
	/*private static void setConfigValue(String key, String value){
		if(props == null){
			loadConfigFile();
		}
		
		props.setProperty(key, value);
	}*/
	
	///////////////////////////////////////////////////////////
	
//	/**
//	 * Initialization tasks
//	 */
//	private static void initUris(){
//		instance = new Util();
//		instance.loadUris();
//	}

//	/**
//	 * Gets the value for the specified key
//	 * @param key 
//	 * @return value for the key provided, or null if key is not found or any problem occurred
//	 */
//	public static String getValueUris(String key){
//		if  (instance == null){
//			initUris();
//		}
//		try{
//			return instance.props.getProperty(key);
//		
//		}catch(Exception ex){
//			logger.error("Unable to get value for key: " + key);
//			ex.printStackTrace();
//			return null;
//		}
//	}
	
	/**
	 * Loads configuration values to keep them in memory
	 */
//	private void loadUris(){
//		props = new Properties();
//		try{
//			props.load(getClass().getClassLoader().getResourceAsStream(API_URIS));
//
//		}catch(Exception ex){
//			logger.error("Unable to load config properties !!");
//			ex.printStackTrace();
//		}
//	}
	
	
	
//	/**
//	 * Initialization tasks
//	 */
//	private static void initParsers(){
//		instance = new Util();
//		instance.loadParsers();
//	}

//	/**
//	 * Gets the value for the specified key
//	 * @param key 
//	 * @return value for the key provided, or null if key is not found or any problem occurred
//	 */
//	public static String getValueParsers(String key){
//		if  (instance == null){
//			initParsers();
//		}
//		try{
//			return instance.props.getProperty(key);
//		
//		}catch(Exception ex){
//			logger.error("Unable to get value for key: " + key);
//			ex.printStackTrace();
//			return null;
//		}
//	}
	
//	/**
//	 * Loads configuration values to keep them in memory
//	 */
//	private void loadParsers(){
//		props = new Properties();
//		try{
//			props.load(getClass().getClassLoader().getResourceAsStream(API_PARSERS));
//
//		}catch(Exception ex){
//			logger.error("Unable to load config properties !!");
//			ex.printStackTrace();
//		}
//	}
	
}
