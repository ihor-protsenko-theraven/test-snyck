package com.essence.hc.model;

import java.text.SimpleDateFormat;
import java.util.Locale;
import javax.servlet.http.HttpServletRequest;
import org.apache.commons.lang.StringUtils;

public class DateFormatHelper{
	
	 public static final String TIME_FORMAT_SESSION_VARIABLE = "timeFormatter";
	 public static final String DATE_FORMAT_SESSION_VARIABLE = "dateFormatter";
	 public static final String DATE_FORMAT_CALENDAR_SESSION_VARIABLE = "dateCalendarFormatter";

	 private static final String DEFAULT_LANGUAGE = "en";
	 private static final String DEFAULT_COUNTRY = "US";

	 private static final String CHINESE_LOCALE_KEY = "zh-CN";
	 
	 private static final String TIME_FORMAT_24H = "HH:mm";
	 private static final String DATE_FORMAT_ISO8601_EXTENDED = "yyyy/MM/dd";
	 
	 
	 
	 public static void setSessionDateFormat(int languageId, HttpServletRequest request){
		 
		  String languageKey = Language.getLanguage(languageId).getLanguageKey().substring(0,2);
  		  String countryKey = Language.getLanguage(languageId).getLanguageKey().substring(3,5);
  		  String localeKey = languageKey + "-" + countryKey;
  		  
  		  boolean validLocale = isValidLocale(languageKey, countryKey);

		  if (!validLocale){
			   languageKey = DEFAULT_LANGUAGE;
			   countryKey = DEFAULT_COUNTRY;
			   localeKey = DEFAULT_LANGUAGE + "-" + DEFAULT_COUNTRY;
		  }

		  Locale locale = new Locale(languageKey, countryKey);
  		 
  		  String timeFormatter = generateTimeFormat(locale, localeKey);
		  String dateFormatter = generateDateFormat(locale, localeKey);
		  String dateCalendarFormatter = generateMobileCalendarDateFormat(dateFormatter, localeKey);

  		  request.getSession().setAttribute(TIME_FORMAT_SESSION_VARIABLE, timeFormatter);
  		  request.getSession().setAttribute(DATE_FORMAT_SESSION_VARIABLE, dateFormatter);
  		  request.getSession().setAttribute(DATE_FORMAT_CALENDAR_SESSION_VARIABLE, dateCalendarFormatter);
  	 }

	
	 
	 private static boolean isValidLocale (String languageKey, String countryKey){
		  boolean returnValue = false;
		  boolean languageVerified = false;
		  boolean countryVerified = false;

		  String[] isoLanguages = Locale.getISOLanguages();

 		  for (int i=0;i<isoLanguages.length;i++){
 		       if (languageKey.equals(isoLanguages[i]))
 		    	    languageVerified = true;
 		  }

 		  if (languageVerified){
 			  String[] isoCountries = Locale.getISOCountries();

			  for (int i=0;i<isoCountries.length;i++){
			       if (countryKey.equals(isoCountries[i]))
			    	    countryVerified = true;
			  }

			  if (countryVerified)
			       returnValue = true;
 		  }

		  return returnValue;
	 }

	 private static String generateTimeFormat(Locale locale, String localeKey){
		 
		 SimpleDateFormat simpleDateFormat = (SimpleDateFormat) SimpleDateFormat.getTimeInstance(SimpleDateFormat.SHORT, locale);
		 
		 if( localeKey.compareTo(CHINESE_LOCALE_KEY) == 0){
			  return TIME_FORMAT_24H;
		  }else{
			  return simpleDateFormat.toPattern();
		  }
	 }
	 
	 private static String generateDateFormat(Locale locale, String localeKey){
		 
		 SimpleDateFormat simpleDateFormat = (SimpleDateFormat) SimpleDateFormat.getDateInstance(SimpleDateFormat.SHORT, locale);
		 
		 if( localeKey.compareTo(CHINESE_LOCALE_KEY) == 0){
			  return DATE_FORMAT_ISO8601_EXTENDED;
		  }else{
			  return sanitizeDateFormatter(simpleDateFormat.toPattern());
		  }
	 }
	 
	 
	 private static String sanitizeDateFormatter(String dateFormatter){
		  String newDateFormatter = dateFormatter;

		  int dayDigits = StringUtils.countMatches(newDateFormatter, "d");

		  if (dayDigits == 1)
			  newDateFormatter = newDateFormatter.replace("d", "dd");

		  int monthDigits = StringUtils.countMatches(newDateFormatter, "M");

		  if (monthDigits == 1)
			  newDateFormatter = newDateFormatter.replace("M", "MM");

		  int yearDigits = StringUtils.countMatches(newDateFormatter, "y");

		  if (yearDigits == 1) {
			  newDateFormatter = newDateFormatter.replace("y", "yyyy");
		  } else if (yearDigits == 2) {
			  newDateFormatter = newDateFormatter.replace("yy", "yyyy");
		  }
		  
		  return newDateFormatter;
	 }

	 private static String generateMobileCalendarDateFormat (String dateFormatter, String localeKey){
		  String returnValue = dateFormatter.replace("/", " ");
		  returnValue = returnValue.replace(".", " ");
		  returnValue = returnValue.replace("-", " ");
		  if( !localeKey.equals(CHINESE_LOCALE_KEY)){
			  returnValue = returnValue.replace("MM", "MMM");  
		  }
		  return returnValue;
	 }
}