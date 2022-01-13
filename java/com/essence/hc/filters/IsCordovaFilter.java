package com.essence.hc.filters;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import org.springframework.util.StringUtils;

/**
 * IsCordovaFilter checks the existence of the isCordova request param in the
 * login request.
 */
public class IsCordovaFilter implements Filter {

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {

		// If there is a previous loggin in this session that has marked the session as
		// "Cordova",
		// we maintain it for the future (Cordova = CSS restyling because of ESC17-2455
		HttpServletRequest req = (HttpServletRequest) request;

		boolean isCordova = Boolean.valueOf(String.valueOf(req.getSession().getAttribute("isCordova")));

		if (!isCordova) {
			if (StringUtils.hasText(req.getParameter("isCordova"))) {
				final String dbValue = req.getParameter("isCordova");
				req.getSession().setAttribute("isCordova", dbValue);
			} else {
				req.getSession().setAttribute("isCordova", "false");
			}
		}
		chain.doFilter(request, response);

	}

	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
	}

	@Override
	public void destroy() {
	}

}