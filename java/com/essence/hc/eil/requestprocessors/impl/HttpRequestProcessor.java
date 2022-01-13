/**
 * 
 */
package com.essence.hc.eil.requestprocessors.impl;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.regex.Pattern;

import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.map.DeserializationConfig.Feature;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;

import com.essence.hc.eil.exceptions.AuthenticationException;
import com.essence.hc.eil.exceptions.ParseException;
import com.essence.hc.eil.exceptions.RemoteIntegrationException;
import com.essence.hc.eil.parsers.IParser;
import com.essence.hc.eil.requestprocessors.RequestProcessor;

/**
 * JSON implementation of our RequestProcessor interface, for remote http
 * services integration
 * 
 * @author oscar.canalejo
 *
 */
public class HttpRequestProcessor implements RequestProcessor {

	private static Pattern REGEX_JSON_PASSWORD = Pattern.compile("(?<=(?i)p(?>assword|wd)\\\":\\\")[^\\\"]++");

	private Logger logger = LoggerFactory.getLogger(getClass());

	private String target_url; // API URL. Set at eilIntegrationLayerContext.xml

	// 2014-02-13T11:07:20
	private DateFormat fmt = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");

	public <T> T performRequest(Class<T> respType, String reqName, HashMap<String, ?> reqParams) {
		return performRequest(respType, reqName, reqParams, new HashMap<String, String>());
	}

	@Override
	public <T> T performRequest(Class<T> respType, String reqName, HashMap<String, ?> reqParams,
			HashMap<String, String> reqHeaders) {

		logger.info("\n========== REQUEST IN PROCESS: {} ===============", reqName);

		T resp = null;

		try {
			/*
			 * Remote Service Calling
			 */
			String wsResponse = call(reqName, reqParams, null, reqHeaders);

			showHiddenLogs("Web Service Response " + reqName + "\n" + wsResponse + "\n");

			/*
			 * Response Mapping & Parsing
			 */
			Class<?> parserClass = API_PARSERS.get(reqName);
			if (parserClass == null)
				throw new ParseException("Couldn't resolve local parser class");

			ObjectMapper jsonMapper = new ObjectMapper();
			jsonMapper.configure(Feature.FAIL_ON_UNKNOWN_PROPERTIES, false);

			// TODO: MANOLO: add date format to avoid errors because of the timezone
			jsonMapper.setDateFormat(fmt);
			IParser<T> parser = (IParser<T>) jsonMapper.readValue(wsResponse, parserClass);
			if (parser != null) {

				if (reqName.startsWith("external_")) {

					boolean relogin = false;

					try {
						String response = (String) parser.getClass().getMethod("getResponse").invoke(parser);
						if ("123".equals(response)) {
							relogin = true;

						}
					} catch (Exception e) {
					}

					if (relogin) {
						throw new AuthenticationException("Invalid token");
					}

				}

				resp = parser.parse();

			}

			logger.info("=============================================\n");

		} catch (JsonGenerationException e) {
			throw new RemoteIntegrationException(e, "Error on Json Generation");
		} catch (JsonMappingException e) {
			throw new RemoteIntegrationException(e, "Error While Mapping Response");
		} catch (IOException e) {
			throw new RemoteIntegrationException(e, "Error Performing Request");
		} catch (ParseException e) {
			throw new RemoteIntegrationException(e, "Error While Parsing Response");
		}
		return resp;
	}

	@Override
	public <T> T performRequestRawBody(Class<T> respType, String reqName, Object body,
			HashMap<String, String> reqHeaders) {

		logger.info("\n========== REQUEST IN PROCESS: {} ===============", reqName);

		T resp = null;

		try {
			/*
			 * Remote Service Calling
			 */
			String wsResponse = call(reqName, null, body, reqHeaders);
			showHiddenLogs("Web Service Response " + reqName + "\n" + wsResponse + "\n");

			/*
			 * Response Mapping & Parsing
			 */
			Class<?> parserClass = API_PARSERS.get(reqName);
			if (parserClass == null)
				throw new ParseException("Couldn't resolve local parser class");

			ObjectMapper jsonMapper = new ObjectMapper();
			jsonMapper.configure(Feature.FAIL_ON_UNKNOWN_PROPERTIES, false);
			// MANOLO: add date format to avoid errors because of the timezone
			jsonMapper.setDateFormat(fmt);
			IParser<T> parser = (IParser<T>) jsonMapper.readValue(wsResponse, parserClass);

			if (parser != null) {

				if (reqName.startsWith("external_")) {

					boolean relogin = false;

					try {
						String response = (String) parser.getClass().getMethod("getResponse").invoke(parser);
						if ("123".equals(response)) {
							relogin = true;

						}
					} catch (Exception e) {
					}

					if (relogin) {
						throw new AuthenticationException("Invalid token");
					}

				}

				resp = parser.parse();
			}

			logger.info("=============================================\n");

		} catch (JsonGenerationException e) {
			throw new RemoteIntegrationException(e, "Error on Json Generation");
		} catch (JsonMappingException e) {
			throw new RemoteIntegrationException(e, "Error While Mapping Response");
		} catch (IOException e) {
			throw new RemoteIntegrationException(e, "Error Performing Request");
		} catch (ParseException e) {
			throw new RemoteIntegrationException(e, "Error While Parsing Response");
		}
		return resp;
	}

	@Override
	public <T> T performRequestRawBody(Class<T> resp, String reqName, Object body) {
		return performRequestRawBody(resp, reqName, body, new HashMap<String, String>());
	}

	@Override
	public Object performUnparsedRequest(String reqName, HashMap<String, ?> reqParams) {
		return performUnparsedRequest(reqName, reqParams, new HashMap<String, String>());
	}

	@Override
	public Object performUnparsedRequest(String reqName, HashMap<String, ?> reqParams,
			HashMap<String, String> reqHeaders) {

		String wsResponse = null;
		Object result = null;
		try {
			logger.info("\n========== UNPARSED REQUEST IN PROCESS: {} ===============", reqName);
			/*
			 * Remote Service Calling
			 */
			wsResponse = call(reqName, reqParams, null, reqHeaders);
			showHiddenLogs("Web Service Response " + reqName + "\n" + wsResponse + "\n");
			ObjectMapper jsonMapper = new ObjectMapper();
			jsonMapper.configure(Feature.FAIL_ON_UNKNOWN_PROPERTIES, false);
			// MANOLO: add date format to avoid errors because of the timezone
			jsonMapper.setDateFormat(fmt);
			result = jsonMapper.readValue(wsResponse, Object.class);

			if (reqName.startsWith("external_")) {

				boolean relogin = false;

				try {
					String response = ((Integer) ((LinkedHashMap) result).get("Response")).toString();

					if ("123".equals(response)) {
						relogin = true;

					}
				} catch (Exception e) {
				}

				if (relogin) {
					throw new AuthenticationException("Invalid token");
				}

			}

		} catch (JsonGenerationException e) {
			throw new RemoteIntegrationException(e, "Error on Json Generation");
		} catch (JsonMappingException e) {
			throw new RemoteIntegrationException(e, "Error While Mapping Response");
		} catch (IOException e) {
			throw new RemoteIntegrationException(e, "Error Performing Request");
		} catch (ParseException e) {
			throw new RemoteIntegrationException(e, "Error While Parsing Response");
		}
		return result;
	}

	/**
	 * Performs a request call to the remote services
	 * 
	 * @param reqName
	 *            name of the request
	 * @param hsmParams
	 *            a {@link HashMap} with the params necessary to compose the request
	 * @param body
	 *            body of the request, to be serialized to JSON, only used if
	 *            hsmParams is null for methods other than GET
	 * @param hsmHeaders
	 *            a {@link HashMap} with the headers necessary to compose the
	 *            request
	 * @return String The response from the web service
	 */
	protected String call(String reqName, HashMap<String, ?> hsmParams, Object body,
			HashMap<String, String> hsmHeaders) {

		String request = null;
		URL url = null;
		HttpURLConnection con = null;
		InputStream is = null;
		BufferedReader br = null;
		StringBuilder out = null;
		String response = null;
		int responseCode = 0;

		try {
			// System.setProperty("http.keepAlive", "false"); // disables all connection
			// reuse.
			// // (Work around pre-Froyo bugs in HTTP connection reuse)
			// System.setProperty("http.maxConnections", "10");

			// Install the all-trusting trust manager
			// SSLContext ssl = SSLContext.getInstance("TLSv1");
			// ssl.init(null, new TrustManager[] { new SimpleX509TrustManager() }, null);
			// SSLSocketFactory factory = ssl.getSocketFactory();

			String method = "GET";
			// FIXME: Temporal Ñapa
			if (reqName.equals("day_story_express") || reqName.equals("monthly_report")
					|| reqName.equals("activity_index_report") || reqName.equals("activity_index_report_old")
					|| reqName.startsWith("external_")) {
				method = "POST";
				request = getUri(null, reqName);
			} else {
				request = getUri(hsmParams, reqName);
			}

			url = new URL(request);
			StringBuffer log = new StringBuffer("\nRequest: " + method + " " + request + "\n");

			con = (HttpURLConnection) url.openConnection();

			// Add headers
			Iterator<String> iter = hsmHeaders.keySet().iterator();
			while (iter.hasNext()) {
				String key = iter.next();
				String value = hsmHeaders.get(key);
				con.setRequestProperty(key, value);

				// EIC15-2536: Password must not be shown in the logs
				if (key.compareToIgnoreCase("password") == 0) {
					log.append("[" + key + ": (hide) ]\n");
				} else
					log.append("[" + key + ": " + value + "]\n");
			}

			if (!"GET".equals(method)) {
				con.setRequestMethod(method);
				con.setRequestProperty("Content-Type", "application/json");

				con.setDoOutput(true);
				DataOutputStream wr = new DataOutputStream(con.getOutputStream());

				if (hsmParams != null) {
					body = hsmParams;
				}
				if (body != null) {
					ObjectMapper jsonMapper = new ObjectMapper();
					jsonMapper.configure(Feature.FAIL_ON_UNKNOWN_PROPERTIES, false);
					jsonMapper.writeValue(wr, body);
					log.append(jsonMapper.writeValueAsString(body) + "\n");
				}

				wr.flush();
				wr.close();
			}

			showHiddenLogs(log.toString());

			// con.setSSLSocketFactory(factory);
			// con.setHostnameVerifier(new CustomizedHostnameVerifier());

			// con.setRequestProperty("connection", "close");
			// con.setReadTimeout(20000);
			// con.setConnectTimeout(20000);

			responseCode = con.getResponseCode(); // (ej.: 200 = OK)

			is = con.getInputStream();
			br = new BufferedReader(new InputStreamReader(is, "UTF-8"));
			out = new StringBuilder();
			// ByteArrayOutputStream out = new ByteArrayOutputStream();
			// BufferedWriter bw = new BufferedWriter(new
			// OutputStreamWriter(out));
			String input;
			while ((input = br.readLine()) != null) {
				out.append(input + "\n");
				// bw.write(input);
			}
			response = out.toString();
			response = response.replace("\n", "");
			response = response.replace("\t", "");

		} catch (Exception ex) {
			logger.error("\n\nError on Calling. " + ex.getClass() + " - " + ex.getMessage() + "\n\n");
			if (responseCode > 0)
				logger.error("\n\nResponse Code was: " + responseCode);
			throw new RemoteIntegrationException(ex, HttpStatus.INTERNAL_SERVER_ERROR.toString());

		} finally {
			try {
				if (is != null)
					is.close();
				if (br != null)
					br.close();
				if (con != null)
					con.disconnect();
				// bw.close();
				// out.close();
			} catch (Exception ex) {
				logger.error("Error closing resources");
			}
		}
		return response;
	}

	/**
	 * This method generate the uri with the parameters passed in.
	 * 
	 * @param params
	 * @return the new formed uri
	 */
	protected String getUri(HashMap<String, ?> params, String reqName) {

		StringBuffer uri = new StringBuffer(this.target_url + API_URIS.get(reqName));
		if (params != null) {
			uri.append("?");
			Iterator<String> iter = params.keySet().iterator();
			while (iter.hasNext()) {
				String key = iter.next();
				if (params.get(key) != null && params.get(key).toString().trim().length() > 0) {
					if (params.get(key).getClass() == String.class) {
						try {
							uri.append(key + "=" + URLEncoder.encode(params.get(key).toString(), "UTF-8") + "&");
						} catch (UnsupportedEncodingException e) {
							e.printStackTrace();
						}
					}
					// uri += (key + "=" + params.get(key) + "&");
				}
			}
			return uri.substring(0, uri.lastIndexOf("&"));
		} else {
			return uri.toString();
		}
	}

	public String getTarget_url() {
		return target_url;
	}

	public void setTarget_url(String target_url) {
		this.target_url = target_url;
	}

	/**
	 * This method cleans a log of visible passwords
	 * 
	 * @param String
	 *            Log to be deleted
	 * @return String with passwords deleted
	 */
	public String showHiddenLogs(String logline) {

		String hiddenLog = REGEX_JSON_PASSWORD.matcher(logline).replaceAll("(hide)");

		logger.info(hiddenLog);

		return hiddenLog;
	}

}
