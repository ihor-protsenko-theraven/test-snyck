package com.essence.hc.eil.parsers;

import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.codehaus.jackson.annotate.JsonProperty;
import org.apache.logging.log4j.Logger;
import org.slf4j.LoggerFactory;

import com.essence.hc.eil.exceptions.ParseException;

public class PredefinedTxtPrsr implements IParser<List<String>> {



	private Logger logger = (Logger) LogManager.getLogger(PredefinedTxtPrsr.class);
	
	private String sErrorStr; 	
	private int iErr;
	private String[] data;
	
	public PredefinedTxtPrsr() {
	}


	@Override
	public List<String> parse() {
//		logger.info("\nParsing " + getClass() + "...\n");
		
		List<String> predefinedTxt = new ArrayList<String>();
		
		try{
			for(int i=0;i<data.length; i++){
				predefinedTxt.add(data[i]);
			}
			
			
			logger.info("el deviceroomId es {}");
		}catch(Exception ex) {
			throw new ParseException(ex,"Unexpected parse error");
		}
		return predefinedTxt;
	}


	public String getsErrorStr() {
		return sErrorStr;
	}


	public void setsErrorStr(String sErrorStr) {
		this.sErrorStr = sErrorStr;
	}


	public int getiErr() {
		return iErr;
	}


	public void setiErr(int iErr) {
		this.iErr = iErr;
	}

	@JsonProperty("Data")
	public String[] getData() {
		return data;
	}

	@JsonProperty("Data")
	public void setData(String[] data) {
		this.data = data;
	}
		
}
