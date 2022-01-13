/**
 * 
 */
package com.essence.hc.eil.requestprocessors.impl;

import java.io.IOException;
import java.io.InputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.apache.commons.io.IOUtils;
import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.JsonParser;
import org.codehaus.jackson.map.DeserializationConfig.Feature;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.essence.hc.eil.exceptions.ParseException;
import com.essence.hc.eil.exceptions.RemoteIntegrationException;
import com.essence.hc.eil.parsers.IParser;
import com.essence.hc.eil.requestprocessors.RequestProcessor;

/**
 * Dummy Request Handler useful for testing purposes
 * 
 * @author oscar.canalejo
 *
 */
public class DummyRequestProcessor implements RequestProcessor {

	private static final String JSON_DIR = "jsonDummies/";
	private static final String JSON_EXT = ".json";

	private Logger logger = LoggerFactory.getLogger(getClass());

	// 2014-02-13T11:07:20
	private DateFormat fmt = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");

	@Override
	public <T> T performRequest(Class<T> respType, String reqName, HashMap<String, ?> reqParams) {
		return performRequest(respType, reqName, reqParams, null);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.essence.hc.eil.requesthandlers.RequestHandler#performRequest(java.lang.
	 * Class, java.lang.String)
	 */
	public <T> T performRequest(Class<T> respType, String reqName, HashMap<String, ?> reqParams,
			HashMap<String, String> reqHeaders) {

		String body = "";

		try {
			if (reqParams != null && !reqParams.isEmpty()) {
				ObjectMapper paramJsonMapper = new ObjectMapper();
				paramJsonMapper.configure(Feature.FAIL_ON_UNKNOWN_PROPERTIES, false);
				body += "\n" + paramJsonMapper.writeValueAsString(reqParams);
			}
		} catch (JsonGenerationException e) {
			throw new RemoteIntegrationException(e, "Error Performing Request " + reqName);
		} catch (JsonMappingException e) {
			throw new RemoteIntegrationException(e, "Error Performing Request " + reqName);
		} catch (IOException e) {
			throw new RemoteIntegrationException(e, "Error Performing Request " + reqName);
		}

		return performRequestRawBody(respType, reqName, body, reqHeaders);
	}

	@Override
	public Object performUnparsedRequest(String reqName, HashMap<String, ?> reqParams) {
		return performUnparsedRequest(reqName, reqParams, new HashMap<String, String>());
	}

	@Override
	public Object performUnparsedRequest(String reqName, HashMap<String, ?> reqParams,
			HashMap<String, String> reqHeaders) {

		String dummyFilePath = this.getDummiesPath(reqName);
		String resp = null;
		Object result = null;

		try {

			String log = "Performing unparsed dummy Request: " + reqName;
			if (reqHeaders != null && !reqHeaders.isEmpty()) {
				Iterator<String> iter = reqHeaders.keySet().iterator();
				while (iter.hasNext()) {
					String key = iter.next();
					String value = reqHeaders.get(key);
					log += "\n[" + key + ": " + value + "]";
				}
			}
			logger.warn(log);

			InputStream inputStream = this.getClass().getClassLoader().getResourceAsStream(dummyFilePath);
			resp = IOUtils.toString(inputStream, "UTF-8");

			logger.warn("Dummy response for {}:\n {}\n", dummyFilePath, resp);

			ObjectMapper jsonMapper = new ObjectMapper();
			jsonMapper.configure(Feature.FAIL_ON_UNKNOWN_PROPERTIES, false);
			// MANOLO: add date format to avoid errors because of the timezone
			jsonMapper.setDateFormat(fmt);
			result = jsonMapper.readValue(resp, Map.class);

		} catch (Exception e) {
			throw new RemoteIntegrationException(e, "Error Performing Request for " + dummyFilePath);
		}
		return result;
	}

	@Override
	public <T> T performRequestRawBody(Class<T> respType, String reqName, Object body,
			HashMap<String, String> reqHeaders) {

		InputStream inputStream;
		ObjectMapper jsonMapper = new ObjectMapper();
		jsonMapper.configure(Feature.FAIL_ON_UNKNOWN_PROPERTIES, false);
		jsonMapper.configure(JsonParser.Feature.ALLOW_NON_NUMERIC_NUMBERS, true); // parser will allow values:
		// NaN, +INF, -INF, +Infinite, -Infinite
		// as floating-point numeric

		T resp = null;
		String dummyFilePath = this.getDummiesPath(reqName);

		try {
			/*
			 * Json Configuration for deserializer to instantiate correct subtype of a
			 * polymorphic value
			 */
			// mapper.enableDefaultTyping(); // default to using
			// DefaultTyping.OBJECT_AND_NON_CONCRETE

			inputStream = this.getClass().getClassLoader().getResourceAsStream(dummyFilePath);

			Class<?> parserClass = API_PARSERS.get(reqName);
			if (parserClass == null)
				throw new ParseException("Couldn't resolve local parser class");

			String log = "Performing dummy Request: " + reqName + ", of type " + parserClass.getName();
			if (reqHeaders != null && !reqHeaders.isEmpty()) {
				Iterator<String> iter = reqHeaders.keySet().iterator();
				while (iter.hasNext()) {
					String key = iter.next();
					String value = reqHeaders.get(key);
					log += "\n[" + key + ": " + value + "]";
				}
			}
			if (body != null) {
				log += "\n" + body;
			}
			logger.warn(log);

			// if (reqName == "day_story") {
			// String theString = IOUtils.toString(inputStream, "UTF-8");
			// logger.warn("File content: {}", theString);
			// return null;
			// }

			// mapper.getTypeFactory().constructCollectionType(List.class, MyClass.class));
			// resp = jsonMapper.readValue(inputStream, respType);

			// MANOLO: add date format to avoid errors because of the timezone
			jsonMapper.setDateFormat(fmt);
			IParser<T> parser = (IParser<T>) jsonMapper.readValue(inputStream, parserClass);
			if (parser != null) {
				try {
					String response = (String) parser.getClass().getMethod("getResponse").invoke(parser);
					if ("123".equals(response)) {
						throw new com.essence.hc.eil.exceptions.AuthenticationException("Invalid Token");
					}
				} catch (NoSuchMethodException e) {
						// We dont process the getResonse if there is no getResponseMethod
				} catch (Exception e) {
					throw new com.essence.hc.eil.exceptions.AuthenticationException("Invalid Token");
				}
				resp = parser.parse();
			}

			if (resp == null) {
				logger.warn("response is null");
			}

			logger.warn("Dummy response for {}: \n{}\n", dummyFilePath, jsonMapper.writeValueAsString(resp));

		} catch (JsonGenerationException e) {
			throw new RemoteIntegrationException(e, "Error Performing Request for " + dummyFilePath);
		} catch (JsonMappingException e) {
			throw new RemoteIntegrationException(e, "Error Performing Request for " + dummyFilePath);
		} catch (IOException e) {
			throw new RemoteIntegrationException(e, "Error Performing Request for " + dummyFilePath);
		}
		return resp;
	}

	@Override
	public <T> T performRequestRawBody(Class<T> resp, String reqName, Object body) {
		return performRequestRawBody(resp, reqName, body, new HashMap<String, String>());
	}

	/**
	 * @param reqName
	 *            request literal
	 * @return Name of the xml file to use as response for the actual request
	 */
	private String getDummiesPath(String reqName) {
		return JSON_DIR + reqName.toLowerCase() + JSON_EXT;
	}

}
