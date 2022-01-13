package com.essence.hc.eil.parsers;

import java.util.ArrayList;
import java.util.List;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.codehaus.jackson.annotate.JsonProperty;

import com.essence.hc.eil.exceptions.ParseException;
import com.essence.hc.model.Language;

@JsonIgnoreProperties(ignoreUnknown = true)
public class ListLanguagesPrsr implements IParser<List<Language>> {
	
	private String response;
	private String responseDescription;
	private boolean value;
	List<LangPrsr> languageList;
	
	public ListLanguagesPrsr() {
	}


	@Override
	public List<Language> parse() {

		List<Language> languages = new ArrayList <Language>();
		
		try { 
			if(languageList != null)		 
				for(LangPrsr i: languageList){
					languages.add((Language) i.parse());
				}
			
			}catch(Exception ex) {
				throw new ParseException(ex,"Unexpected parse error");
			}
			Language.setLanguageList(languages);

		return  languages;
	}

	/* Getters and setters are duplicated because this parser is used for both APIs. 
	 * The response has the same form, but property names are different.
	 */
	
	@JsonProperty("Response")
	public String getResponse() {
		return response;
	}

	@JsonProperty("Response")
	public void setResponse(String response) {
		this.response = response;
	}

	@JsonProperty("ResponseDescription")
	public String getResponseDescription() {
		return responseDescription;
	}

	@JsonProperty("ResponseDescription")
	public void setResponseDescription(String responseDescription) {
		this.responseDescription = responseDescription;
	}

	@JsonProperty("Value")
	public boolean isValue() {
		return value;
	}

	@JsonProperty("Value")
	public void setValue(boolean value) {
		this.value = value;
	}
	
	public List<LangPrsr> getLanguageList() {
		return languageList;
	}


	public void setLanguageList(List<LangPrsr> languageList) {
		this.languageList = languageList;
	}

	@JsonProperty("Languages")
	public List<LangPrsr> getLanguages() {
		return languageList;
	}

	@JsonProperty("Languages")
	public void setLanguages(List<LangPrsr> languageList) {
		this.languageList = languageList;
	}
	
	
}
