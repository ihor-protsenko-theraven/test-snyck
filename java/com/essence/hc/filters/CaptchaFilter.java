package com.essence.hc.filters;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

// import nl.captcha.Captcha;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
/*
public class CaptchaFilter implements Filter {

	protected final Logger logger = LoggerFactory.getLogger(getClass());
	
	private String html; 
	

	@Override
	public void destroy() {}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response,
			FilterChain chain) throws IOException, ServletException {
		//logger.debug("testing Captcha!");
		HttpSession session = null;
		// First we need to test if we are in captcha challenge
		try{
			HttpServletRequest req = (HttpServletRequest) request;
			session = req.getSession();
			Captcha captcha = (Captcha) session.getAttribute("CAPTCHA_OBJECT");
			//After get the object the sessionAttr is destroyed  
			session.removeAttribute("CAPTCHA_OBJECT");
			// only Method POST (=> form submit) needs to test Captcha 
			if (captcha != null && req.getMethod().equalsIgnoreCase("POST")){
				// We in captcha challenge
				try {
					logger.debug("In captcha challenge!");
					// Get input from the user
					String code = request.getParameter("NAME");
					// Get Answer from session object
					if (code != null && captcha.isCorrect(code)){
						// Captcha is correct. Can continue
						logger.debug("Captcha is ok {} = {}", code, captcha.getAnswer());
						chain.doFilter(request, response);
					} else{
						// JSON message
						logger.debug("Captcha is KO {} != {}", code, captcha.getAnswer());
						//prepares the response
						HttpServletResponse resp = (HttpServletResponse )response;
						resp.setContentType("application/json;charset=UTF-8");
						resp.setHeader("Cache-Control", "no-store");
						resp.setHeader("Cache-Control", "no-cache");
						resp.setHeader("Pragma", "no-cache");
						resp.setHeader("Expires", "Thu, 01 Jan 1970 00:00:00 GMT");
						response.getWriter().write(html);
						response.flushBuffer();
						logger.info("response {} sent from Captcha", html);
					}
						
				} catch (Exception e){
						// JSON MESSAGE
					logger.error("Exception comparing captcha", e);
					response.setContentType("application/json;charset=UTF-8");
					response.getWriter().write(html);
					response.flushBuffer();
				}
			} else{
				// Not in captcha challenge. Continue normally
				chain.doFilter(request, response);
			}
				
		} catch (IllegalStateException e){
			// There was a session error (session invalid)
			response.setContentType("application/json;charset=UTF-8");
			logger.error("Error in captchaFilter", e);
			response.getWriter().write(html);
		}
	}
	
	@Override
	public void init(FilterConfig cfg) throws ServletException {		
	}

	public String getHtml() {
		return html;
	}

	public void setHtml(String html) {
		this.html = html;
		this.html="{\"code\":2,\"message\":\"Captcha error\",\"documentRoot\":\"\",\"inCaptchaChallenge\":true}";
	}

}
*/