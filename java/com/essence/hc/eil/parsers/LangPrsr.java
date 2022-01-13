package com.essence.hc.eil.parsers;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.codehaus.jackson.annotate.JsonProperty;

import com.essence.hc.eil.exceptions.ParseException;
import com.essence.hc.model.Language;

@JsonIgnoreProperties(ignoreUnknown = true)
public class LangPrsr implements IParser<Language> {



//	private Logger logger = LoggerFactory.getLogger(getClass());
	
	private String sCountryName;
	private String sInLanguage;
	private int iLanguageID;
	private String sLanguageKey;
	private String sLanguageName;
	private boolean sRightToLeft;
	
	public LangPrsr() {
	}


	@Override
	public Language parse() {
//		logger.info("\nParsing " + getClass() + "...\n");
		
		Language language = new Language();

		try{
			
			language.setCountryName(sCountryName);
			language.setInLanguage(sInLanguage);
			language.setLanguageID(iLanguageID);
			language.setLanguageKey(sLanguageKey);
			language.setLanguageName(sLanguageName);
			language.setRightToLeft(sRightToLeft);

		}catch(Exception ex) {
			throw new ParseException(ex,"Unexpected parse error");
		}
		return language;
	}

	@JsonProperty("CountryName")
	public String getsCountryName() {
		return sCountryName;
	}

	@JsonProperty("CountryName")
	public void setsCountryName(String sCountryName) {
		this.sCountryName = sCountryName;
	}

	@JsonProperty("InLanguage")
	public String getsInLanguage() {
		return sInLanguage;
	}

	@JsonProperty("InLanguage")
	public void setsInLanguage(String sInLanguage) {
		this.sInLanguage = sInLanguage;
	}

	@JsonProperty("LanguageID")
	public int getsLanguageID() {
		return iLanguageID;
	}

	@JsonProperty("LanguageID")
	public void setsLanguageID(int iLanguageID) {
		this.iLanguageID = iLanguageID;
	}
	
	@JsonProperty("LanguageId")
	public void setsLanguageId(int iLanguageId) {
		this.iLanguageID = iLanguageId;
	}

	@JsonProperty("LanguageKey")
	public String getsLanguageKey() {
		return sLanguageKey;
	}

	@JsonProperty("LanguageKey")
	public void setsLanguageKey(String sLanguageKey) {
		this.sLanguageKey = sLanguageKey.trim();
	}

	@JsonProperty("LanguageName")
	public String getsLanguageName() {
		return sLanguageName;
	}

	@JsonProperty("LanguageName")
	public void setsLanguageName(String sLanguageName) {
		this.sLanguageName = sLanguageName;
	}

	@JsonProperty("RightToLeft")
	public boolean issRightToLeft() {
		return sRightToLeft;
	}

	@JsonProperty("RightToLeft")
	public void setsRightToLeft(boolean sRightToLeft) {
		this.sRightToLeft = sRightToLeft;
	}

	
	
	
		
}
