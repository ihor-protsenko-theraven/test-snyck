package com.essence.hc.model;

import java.io.Serializable;
import java.util.List;

/**
* Language Class
* 
* @author daniel.alcantarilla
*
*/

public class Language implements Serializable{
	
	private static final long serialVersionUID = 1L;
	
	private int LanguageID;
	private String LanguageName;
	private String LanguageKey;
	private String CountryName;
	private boolean RightToLeft;
	private String InLanguage;
   
	private static List<Language> languageList;
	
   /**
    * Returns the language that corresponds to the specified languageId
    * @param languageId
    * @return Language object that matchs with the specified languageId. Null if not found
    */
   public static Language getLanguage(int languageId) {
	   if (languageList != null) {
		   for(Language lang : languageList) {
			   if(lang.LanguageID == languageId)
				   return lang;
		   }
	   }
	   return null;
   }
   
	public int getLanguageID() {
		return LanguageID;
	}
	public void setLanguageID(int sLanguageID) {
		LanguageID = sLanguageID;
	}
	public String getLanguageName() {
		return LanguageName;
	}
	public void setLanguageName(String languageName) {
		LanguageName = languageName;
	}
	public String getLanguageKey() {
		return LanguageKey;
	}
	public void setLanguageKey(String languageKey) {
		LanguageKey = languageKey;
	}
	public String getCountryName() {
		return CountryName;
	}
	public void setCountryName(String countryName) {
		CountryName = countryName;
	}
	public boolean isRightToLeft() {
		return RightToLeft;
	}
	public void setRightToLeft(boolean rightToLeft) {
		RightToLeft = rightToLeft;
	}
	public String getInLanguage() {
		return InLanguage;
	}
	public void setInLanguage(String inLanguage) {
		InLanguage = inLanguage;
	}

	public static List<Language> getLanguageList() {
		return Language.languageList;
	}

	public static void setLanguageList(List<Language> languageList) {
		Language.languageList = languageList;
	}

       
       
      
}
