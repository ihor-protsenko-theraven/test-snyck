/**
 * 
 */
package com.essence.hc.util;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

import javax.servlet.http.HttpSession;

import org.joda.time.DateTime;
import org.springframework.security.authentication.encoding.ShaPasswordEncoder;
import org.springframework.util.StringUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.essence.hc.model.DateFormatHelper;
import com.essence.hc.model.User;
import com.essence.hc.model.Vendor.ServiceType;

/**
 * Class for miscellaneous Constants and Utility Methods
 * 
 * @author oscar.canalejo
 *
 */
public class Util {

	public static final String DATEFORMAT_INPUT = "yyyy-MM-dd";
	public static final String DATETIMEFORMAT_INPUT = "yyyy-MM-dd'T'HH:mm:ss";
	public static final String DATETIME_TMZ_FORMAT_INPUT = "yyyy-MM-dd'T'HH:mm:ssX";
	public static final String DATEFORMAT_OUTPUT = "yyyy/MM/dd";
	public static final String DATEFORMAT_OUTPUT_PLAIN = "yyyyMMdd";
	public static final String DATEFORMAT = "yyyy/MM/dd HH:mm:ss";
	public static final String HISTORY_DATEFORMAT = "dd/MM/yyyy HH:mm";
	public static final String HISTORY_DATEFORMAT_US = "MM/dd/yyyy HH:mm";
	public static final String HISTORY_DATEFORMAT_JP = "yyyy/MM/dd HH:mm";
	public static final String DATEFORMAT_ESSENCE = "yyyy-MM-dd HH:mm:ss";
	public static final String DATEFORMAT_EXTERNAL_API = "yyyy-MM-dd HH:mm:ss.000";
	public static final String TIMEFORMAT_SHORT = "HH:mm";
	public static final String TIMEFORMAT = "HH:mm:ss";
	public static final long CLOSED_ALERT_TIMEOUT = 300000; // milliseconds
	private static String frontVersion = null;
	private static String apiVersion = "na";

	/**
	 * Checks if the String parameter is whitespace, empty ("") or null.
	 * 
	 * @param str
	 * @return true if str is null or empty, false otherwise
	 */
	public static boolean isBlank(String str) {
		return !StringUtils.hasText(str);
	}

	/**
	 * Formats Date to String
	 * 
	 * @param date
	 * @return
	 */
	public static String formatDate(Date date, String dateFormat) {
		if (date != null) {
			if (dateFormat == null)
				dateFormat = "yyyyMMdd";
			DateFormat formatter = new SimpleDateFormat(dateFormat);
			return formatter.format(date);
		} else {
			return null;
		}
	}

	/**
	 * Parses String to Date
	 * 
	 * @param date
	 * @return
	 */
	public static Date parseDate(String date, String dateFormat) throws ParseException {
		// try {
		SimpleDateFormat formatter = new SimpleDateFormat(dateFormat);
		formatter.setTimeZone(TimeZone.getTimeZone("UTC"));
		// FIXME: date.substring limit date format patterns
		return formatter.parse(date.substring(0, 10));
		// }catch(ParseException ex) {
		// return null;
		// }
	}

	/**
	 * Parses String to Date
	 * 
	 * @param date
	 * @return
	 */
	public static Date parseDateTime(String date) throws ParseException {
		// try {
		return parseDateTime(date, DATETIMEFORMAT_INPUT);
		// }catch(ParseException ex) {
		// return null;
		// }
	}

	public static Date parseDateTime(String date, String dateFormat) throws ParseException {
		// try {
		SimpleDateFormat formatter = new SimpleDateFormat(dateFormat);
		return formatter.parse(date);
		// }catch(ParseException ex) {
		// return null;
		// }
	}

	/**
	 * This method checks if the passed in String date corresponds with any of these
	 * date formats: - "dd/MM/yyyy", - "dd-MM-yyyy", - "MM/dd/yyyy", - "MM-dd-yyyy",
	 * - "yyyy/MM/dd", - "yyyy-MM-dd"
	 * 
	 * If is matches one of the mentioned formats the a Date object is returned,
	 * else a null object is returned.
	 * 
	 * @param inputDate
	 * @return
	 */
	public static Date parseDate(String inputDate, Locale loc) {
		Date outDate = null;

		SimpleDateFormat sdfInput = (SimpleDateFormat) SimpleDateFormat.getDateInstance(SimpleDateFormat.SHORT, loc);
		String format = sdfInput.toPattern();

		if (format.startsWith("d")) {
			format = "dd/MM/yyyy";
		} else if (format.startsWith("M")) {
			format = "MM/dd/yyyy";
		} else if (format.startsWith("y")) {
			format = "yyyy/MM/dd";
		}

		sdfInput.applyPattern(format);
		sdfInput.setLenient(false);

		try {
			outDate = sdfInput.parse(inputDate);
		} catch (ParseException e) {
			System.err.println("format not valid: " + format);
		}

		return outDate;
	}

	/**
	 * Returns current Date from {@link Calendar}
	 * 
	 * @return current date
	 */
	public static Date getCurrentDate() {
		return Calendar.getInstance().getTime();
	}

	/**
	 * Returns time part of the date received
	 * 
	 * @param date
	 *            from which is wanted to extract the time part
	 * @return time part of the date, formated as "HH:mm:ss"
	 */
	public static String getTime(Date date) {
		return (date != null) ? new SimpleDateFormat(TIMEFORMAT).format(date) : null;
	}

	/*
	 * new SimpleDateFormat(format, locale); doesn't work properly.
	 */
	public static String getDateTime(Date date, String format) {
		if (date == null || format == null)
			return null;
		return new SimpleDateFormat(format).format(date);
	}

	public static Locale getLocale(User user) {
		if (user == null || user.getLanguage() == null || user.getLanguage().getLanguageKey() == null)
			return null;
		return new Locale(user.getLanguage().getLanguageKey());
	}

	public static Date subtractDays(Date date, int numberOfDays) {

		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		cal.add(Calendar.DATE, -numberOfDays);

		return cal.getTime();
	}

	public static boolean isUSA(String locale) {
		return "US".equalsIgnoreCase(extractCountry(locale));
	}

	private static String extractCountry(String locale) {
		String[] splittedLocale = locale.split("_");
		if (splittedLocale == null)
			return locale;
		return splittedLocale[splittedLocale.length - 1];
	}

	/*
	 * Returns internationalized message value for the specified key
	 * 
	 * @param key the message key. Must be an existing key code at the corresponding
	 * message file
	 * 
	 * @return internationalized message value
	 *
	 * This function is not needed anymore. In case it is needed in the future it
	 * should be moved to some other class, to keep all methods static here
	 * 
	 * public String getI18NMessage(String key) { if (messageSource != null) return
	 * messageSource.getMessage(key, null, LocaleContextHolder.getLocale()); else
	 * return "i18n text not found"; }
	 */

	/**
	 * Returns the String object encoded
	 * 
	 * @deprecated
	 * @param passwd
	 * @return the same password passed in encoded
	 */
	public static String encodePassword(String passwd) {
		ShaPasswordEncoder passwordEncoder = new ShaPasswordEncoder();
		return passwordEncoder.encodePassword(passwd, "ADL");
	}

	public static String getFrontVersion() {
		if (frontVersion == null) {
			frontVersion = AppConfig.getVersion();
		}
		return frontVersion;
	}

	public static void setFrontVersion(String frontVersion) {
		Util.frontVersion = frontVersion;
	}

	public static String getAPIVersion() {
		return apiVersion;
	}

	public static void setAPIVersion(String apiVersion) {
		Util.apiVersion = apiVersion;
	}

	public static String getSystemVersion() {
		return getFrontVersion() + "_" + getAPIVersion();
	}

	/**
	 * Check if the passed String contains RTL encoding characters
	 * 
	 * @param s
	 * @return
	 */
	public static boolean isRTL(String s) {
		boolean rtl = false;
		if (s != null) {
			char[] chars = s.toCharArray();
			for (char c : chars) {
				if (c >= 0x5D0 && c <= 0x6ff) {
					rtl = true; // Text contains RTL character
					break;
				}
			}
		}
		return rtl;
	}

	/**
	 * Returns DEBUG
	 */
	public static boolean isDebug() {
		return AppConfig.getDebug();
	}

	// 12-24 HOURS DATE FORMAT METHOD
	public static String formatDateByClockType(String originalDate, String originalDateFormat, String newDateFormat) {
		if (isBlank(originalDate)) {

			return "";
		}
		String formattedDate = null;

		try {
			SimpleDateFormat originalFormat = new SimpleDateFormat(originalDateFormat);
			SimpleDateFormat targetFormat = new SimpleDateFormat(newDateFormat);
			Date date = originalFormat.parse(originalDate);
			formattedDate = targetFormat.format(date);
		} catch (ParseException e) {
			e.printStackTrace();
			formattedDate = null;
		}

		return formattedDate;
	}

	public static String formatFullDate(Date fullDate) {

		if (fullDate != null) {

			HttpSession currentSession = obtainCurrentHttpSession();

			String format = (String) currentSession.getAttribute(DateFormatHelper.DATE_FORMAT_SESSION_VARIABLE) + " "
					+ (String) currentSession.getAttribute(DateFormatHelper.TIME_FORMAT_SESSION_VARIABLE);

			return new SimpleDateFormat(format).format(fullDate);

		} else {
			return null;
		}
	}

	public static HttpSession obtainCurrentHttpSession() {

		ServletRequestAttributes attr = (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
		return attr.getRequest().getSession();
	}

	/**
	 * Translates Analytics and Express to Pro and Family respectively
	 * 
	 * @param st
	 * @return
	 */
	public static String mapFromOldToNewServicePackageNaming(String st) {
		if (!StringUtils.hasText(st)) {
			return st;
		}
		if (st.equals("Analytics")) {
			return "Pro";
		} else if (st.equals("Express")) {
			return "Family";
		} else if (st.equals("PERS-E")) {
			return st;
		} else if (st.equals("Umbrella")) {
			return st;
		}

		throw new RuntimeException(String.format(
				"Unexpected old Service package name: %s. Expected values are: Analytics, Express, PERS-E, Umbrella", st));
	}

	/**
	 * Translates Pro and Family to Analytics and Express respectively
	 * 
	 * @param st
	 * @return
	 */
	public static String mapFromNewToOldServicePackageNaming(String st) {
		if (!StringUtils.hasText(st)) {
			return st;
		}
		if (st.equals("Pro")) {
			return "Analytics";
		} else if (st.equals("Family")) {
			return "Express";
		} else if (st.toUpperCase().equals("PERS-E")) {
			return st.toUpperCase();
		} else if (st.equals("Umbrella")) {
			return st;
		}

		throw new RuntimeException(String
				.format("Unexpected old Service package name: %s. Expected values are: Pro, Family and PERS-E", st));
	}
	
	public static String buildPanelTypeFilterString(String[] panelTypeArr) {
		
		if (panelTypeArr == null) {
			return null;
		}

		StringBuilder sb = new StringBuilder();
		for (String type : Arrays.asList(panelTypeArr)) {
			if (StringUtils.hasText(sb)) {
				sb.append(",");
			}
			sb.append(type);
		}
		return sb.toString();
	}

	public static String buildServicePackagesFilterStringFromServiceTypes(ServiceType[] serviceTypeArr) {

		if (serviceTypeArr == null || serviceTypeArr.length > ServiceType.values().length ) {
			return null;
		}

		StringBuilder sb = new StringBuilder();
		for (ServiceType type : Arrays.asList(serviceTypeArr)) {
			if (StringUtils.hasText(sb)) {
				sb.append(",");
			}
			sb.append(Util.mapFromOldToNewServicePackageNaming(type.getName()));
		}
		return sb.toString();
	}

	/**
	 * Checks if a given date is before a base date at start of the day.
	 * 
	 * @param aDate
	 * @param baseDate
	 * @return
	 */
	public static boolean isBeforeBaseDateAtStartOfTheDay(Date aDate, Date baseDate) {

		if (aDate == null || baseDate == null) {
			throw new IllegalArgumentException("Null argument is not accepted");
		}

		DateTime aDateTime = new DateTime(aDate);
		DateTime baseDateTime = new DateTime(baseDate);
		return aDateTime.isBefore(baseDateTime.withTimeAtStartOfDay());
	}

	/**
	 * Checks if a given date is after a base date at end of the day.
	 * 
	 * @param aDate
	 * @param baseDate
	 * @return
	 */
	public static boolean isAfterBaseDateAtEndOfTheDay(Date aDate, Date baseDate) {

		if (aDate == null || baseDate == null) {
			throw new IllegalArgumentException("Null argument is not accepted");
		}

		DateTime aDateTime = new DateTime(aDate);
		DateTime baseDateTime = new DateTime(baseDate);
		return aDateTime.isAfter(baseDateTime.withTime(23, 59, 59, 999));
	}
}
