package com.essence.hc.util;

import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Custom vendor configuration
 * 
 * @author adrian.casado
 *
 */
public class VendorConfig {

	private static Logger logger = LoggerFactory.getLogger("VendorConfig");

	public static final int DEFAULT_VENDOR = 2;
	private static final String VENDOR_CONFIG_FOLDER = "config/vendors/";
	private static final String VENDOR_CONFIG_FILE = "vendor.properties";

	private static final String ALLOW_OPTION_TAKE_PHOTO_KEY = "allow_option_take_photo";
	private static final String ALLOW_OPTION_TAKE_PHOTO_DEFAULT = "true";

	private static final String ALLOW_OPTION_MORE_ACTIONS_KEY = "allow_option_more_actions";
	private static final String ALLOW_OPTION_MORE_ACTIONS_DEFAULT = "true";

	private static final String SHOW_POWERED_BY_KEY = "show_powered_by";
	private static final String SHOW_POWERED_BY_DEFAULT = "true";

	private static final String SHOW_POWERED_BY_LOGO_KEY = "show_powered_by_logo";
	private static final String SHOW_POWERED_BY_LOGO_DEFAULT = "true";

	private static final String IS_POWERED_BY_CLICKABLE = "is_powered_by_clickable";
	private static final String IS_POWERED_BY_CLICKABLE_DEFAULT = "false";

	private static final String SHOW_VERSION_NUMBER_KEY = "show_version_number";
	private static final String SHOW_VERSION_NUMBER_DEFAULT = "false";

	private static final int NUMBER_OF_MOBILE_ACTIVE_TABS = 4;

	private static final String SHOW_ID_NUMBER_KEY = "show_identification_number";
	private static final String SHOW_ID_NUMBER_DEFAULT = "true";

	private static final String USE_EMBEDDED_MAP_KEY = "use_embedded_map";

	private static final String MAPS_API_KEY_KEY = "maps_api_key";
	private static final String MAPS_ACCURACY_CIRCLE_STYLE_STROKE_COLOR = "accuracy_circle_style_stroke_color";
	private static final String MAPS_ACCURACY_CIRCLE_STYLE_STROKE_WIDTH = "accuracy_circle_style_stroke_width";
	private static final String MAPS_ACCURACY_CIRCLE_STYLE_FILL_COLOR = "accuracy_circle_style_fill_color";

	private static final String SHOW_COPYRIGHT_SYMBOL = "show_copyright_symbol";
	private static final String SHOW_COPYRIGHT_SYMBOL_DEFAULT = "true";

	private static Properties props;

	/**
	 * Gets the value to know if the vendor has enable the take photo option
	 * 
	 * @param vendorId
	 *            The user vendor id
	 * @return false if vendor has disable the take photo option, true in other case
	 */

	public static boolean isAllowedOptionTakePhoto(int vendorId) {

		try {

			String configValue = getConfigValue(Integer.toString(vendorId), ALLOW_OPTION_TAKE_PHOTO_KEY,
					ALLOW_OPTION_TAKE_PHOTO_DEFAULT);
			return Boolean.parseBoolean(configValue);

		} catch (Exception ex) {
			logger.error("Error while getting allow option take photo. Returning default value.", ex);
		}

		return Boolean.valueOf(ALLOW_OPTION_TAKE_PHOTO_DEFAULT);
	}

	/**
	 * Gets the value to know if the vendor has enable the more actions option
	 * 
	 * @param vendorId
	 *            The user vendor id
	 * @return false if vendor has disable the more actions option, true in other
	 *         case
	 */

	public static boolean isAllowedOptionMoreActions(int vendorId) {

		try {

			String configValue = getConfigValue(Integer.toString(vendorId), ALLOW_OPTION_MORE_ACTIONS_KEY,
					ALLOW_OPTION_MORE_ACTIONS_DEFAULT);
			return Boolean.parseBoolean(configValue);

		} catch (Exception ex) {
			logger.error("Error while getting allow option more actions. Returning default value.", ex);
		}

		return Boolean.valueOf(ALLOW_OPTION_MORE_ACTIONS_DEFAULT);
	}

	public static String obtainNumberOfMobileActiveTabs(int vendorId) {

		int numberOfMobileActiveTabs = NUMBER_OF_MOBILE_ACTIVE_TABS;

		if (!isAllowedOptionTakePhoto(vendorId)) {
			numberOfMobileActiveTabs--;
		}

		if (!isAllowedOptionMoreActions(vendorId)) {
			numberOfMobileActiveTabs--;
		}

		return Integer.toString(numberOfMobileActiveTabs);

	}

	/**
	 * Gets the value to know if it is allowed to show power by text
	 * 
	 * @param vendorId
	 * @return
	 */
	public static boolean isShowablePoweredBy(int vendorId) {

		try {

			String configValue = getConfigValue(Integer.toString(vendorId), SHOW_POWERED_BY_KEY,
					SHOW_POWERED_BY_DEFAULT);
			return Boolean.parseBoolean(configValue);

		} catch (Exception ex) {
			logger.error("Error while getting show powered by property. Returning default value.", ex);
		}

		return Boolean.valueOf(SHOW_POWERED_BY_DEFAULT);
	}

	/**
	 * Gets the value to know if it is allowed to show power by logo brand
	 * 
	 * @param vendorId
	 * @return
	 */
	public static boolean isShowablePoweredByLogo(int vendorId) {

		try {

			String configValue = getConfigValue(Integer.toString(vendorId), SHOW_POWERED_BY_LOGO_KEY,
					SHOW_POWERED_BY_LOGO_DEFAULT);
			return Boolean.parseBoolean(configValue);

		} catch (Exception ex) {
			logger.error("Error while getting show powered by logo property. Returning default value.", ex);
		}

		return Boolean.valueOf(SHOW_POWERED_BY_LOGO_DEFAULT);
	}

	public static boolean isShowableVersionNumber(int vendorId) {

		try {

			String configValue = getConfigValue(Integer.toString(vendorId), SHOW_VERSION_NUMBER_KEY,
					SHOW_VERSION_NUMBER_DEFAULT);
			return Boolean.parseBoolean(configValue);

		} catch (Exception ex) {
			logger.error("Error while getting show powered by logo property. Returning default value.", ex);
		}

		return Boolean.valueOf(SHOW_VERSION_NUMBER_DEFAULT);
	}

	/**
	 * Gets the value to know if it is allowed to show the copyright symboñ
	 * 
	 * @param vendorId
	 * @return
	 */
	public static boolean isShowableCopyrightSymbol(int vendorId) {

		try {

			String configValue = getConfigValue(Integer.toString(vendorId), SHOW_COPYRIGHT_SYMBOL,
					SHOW_COPYRIGHT_SYMBOL_DEFAULT);
			return Boolean.parseBoolean(configValue);

		} catch (Exception ex) {
			logger.error("Error while getting show copyright symbol property. Returning default value.", ex);
		}

		return Boolean.valueOf(SHOW_POWERED_BY_DEFAULT);
	}

	/**
	 * Gets the value to know if the powered by logo must be clickable to go default
	 * page
	 * 
	 * @param vendorId
	 * @return
	 */
	public static boolean isPoweredByClickable(int vendorId) {

		try {

			String configValue = getConfigValue(Integer.toString(vendorId), IS_POWERED_BY_CLICKABLE,
					IS_POWERED_BY_CLICKABLE_DEFAULT);
			return Boolean.parseBoolean(configValue);

		} catch (Exception ex) {
			logger.error("Error while getting is powered by clickable property. Returning default value.", ex);
		}

		return Boolean.valueOf(IS_POWERED_BY_CLICKABLE_DEFAULT);
	}

	/**
	 * Gets the value to know if it is allowed to show identification number
	 * 
	 * @param vendorId
	 * @return
	 */
	public static boolean isShowableIdentificationNumber(int vendorId) {

		try {

			String configValue = getConfigValue(Integer.toString(vendorId), SHOW_ID_NUMBER_KEY, SHOW_ID_NUMBER_DEFAULT);
			return Boolean.parseBoolean(configValue);

		} catch (Exception ex) {
			logger.error(
					"Error while getting show identification number in day story property. Returning default value.",
					ex);
		}

		return Boolean.valueOf(SHOW_ID_NUMBER_DEFAULT);
	}

	public static String useEmbeddedMap(int vendorId) {

		try {

			String configValue = getConfigValue(Integer.toString(vendorId), USE_EMBEDDED_MAP_KEY,
					"none").trim();
			if (!configValue.equals("none") && !configValue.equals("googlemaps") && !configValue.equals("openlayers")) {
				throw new IllegalArgumentException(configValue);
			}
			return configValue;
		} catch (IllegalArgumentException ex) {
			logger.error("ATTENTION: Map accepted values are none | googlemaps | openlayers. Invalid VALUE {} - Default (none) is applied", ex.getMessage());
		} catch (Exception ex) {
			logger.error("Error while getting use embedded map. Returning default value.", ex);
		}

		return "none";
	}
	
	public static boolean usesExternalMap(int vendorId) {

		if (useEmbeddedMap(vendorId).equals("none")) {
			return true;
		}
		return false;
	}
	
	public static boolean usesGoogleMaps(int vendorId) {

		if (useEmbeddedMap(vendorId).equals("googlemaps")) {
			return true;
		}
		return false;
	}
	
	public static boolean usesOpenLayers(int vendorId) {

		if (useEmbeddedMap(vendorId).equals("openlayers")) {
			return true;
		}
		return false;
	}

	public static int getMapAccuracyStylingStrokeWidth(int vendorId) {

		try {

			int configValue = Integer.parseInt(getConfigValue(Integer.toString(vendorId), MAPS_ACCURACY_CIRCLE_STYLE_STROKE_WIDTH,
					"3"));
			return configValue;

		} catch (Exception ex) {
			logger.error("Error while getting google maps key. Returning default value.", ex);
		}

		return 3;
	}
	
	public static String getMapAccuracyStylingStrokeColor(int vendorId) {

		try {

			String configValue = getConfigValue(Integer.toString(vendorId), MAPS_ACCURACY_CIRCLE_STYLE_STROKE_COLOR,
					"blue");
			return configValue;

		} catch (Exception ex) {
			logger.error("Error while getting google maps key. Returning default value.", ex);
		}

		return "blue";
	}
	
	public static String getMapAccuracyStylingFillColor(int vendorId) {

		try {

			String configValue = getConfigValue(Integer.toString(vendorId), MAPS_ACCURACY_CIRCLE_STYLE_FILL_COLOR,
					"rgba(173,216,230,.7)");
			return configValue;

		} catch (Exception ex) {
			logger.error("Error while getting google maps key. Returning default value.", ex);
		}

		return "rgba(173,216,230,.7)";
	}
	
	public static String obtainMapsApiKey(int vendorId) {

		try {

			String configValue = getConfigValue(Integer.toString(vendorId), MAPS_API_KEY_KEY,
					"");
			return configValue;

		} catch (Exception ex) {
			logger.error("Error while getting google maps key. Returning default value.", ex);
		}

		return "";

	}
	
	/**
	 * Searches for the property with the specified key and returns its value If the
	 * key is not found the default value argument will be returned
	 * 
	 * @param key
	 * @param defaultValue
	 * @return value associated with the given configuration key
	 */
	private static String getConfigValue(String vendorId, String key, String defaultValue) {
		if (props == null)
			loadVendorConfigFile(vendorId);

		return props.getProperty(key, defaultValue);
	}

	/**
	 * Loads the vendor configuration/settings file
	 */
	private static void loadVendorConfigFile(String vendorId) {
		logger.info("--------------------------- Loading Config File ------------------------------");

		props = new Properties();
		try {
			props.load(AppConfig.class.getClassLoader()
					.getResourceAsStream(VENDOR_CONFIG_FOLDER + vendorId + "_" + VENDOR_CONFIG_FILE));
			logger.info("Properties for vendor " + vendorId + " loaded");

		} catch (Exception ex) {
			logger.warn("Default properties for vendor loaded");
		}
	}
}
