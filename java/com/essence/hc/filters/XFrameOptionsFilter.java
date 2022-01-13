package com.essence.hc.filters;

import org.apache.commons.lang.StringUtils;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Filter to avoid clickjaking attacks.
 *
 * See https://www.owasp.org/index.php/Clickjacking_Defense_Cheat_Sheet.
 *
 * Modes supported:
 *  - DENY: prevents any domain from framing the content.
 *  - SAMEORIGIN, which only allows the current site to frame the content.
 *  - ALLOW-FROM uri, which permits the specified 'uri' to frame this page.
 *      Not all browsers support this mode.
 *
 * Any other value will default to DENY.
 *
 * Sets X-Frame-Options and Content-Security-Policy (for frame-ancestors) headers.
 *
 */
public class XFrameOptionsFilter implements Filter {
    private static String MODE_DENY = "DENY";
    private static String MODE_SAMEORIGIN = "SAMEORIGIN";
    private static String MODE_ALLOWFROM = "ALLOW-FROM";

    private String mode;
    private String url;
    private String domain;

    public void init(FilterConfig filterConfig) throws ServletException {
        mode = filterConfig.getInitParameter("mode");
        url = filterConfig.getInitParameter("url");

        // Mode: DENY, SAMEORIGIN, ALLOW-FROM. Any other value will default to SAMEORIGIN
        if (!mode.equals(MODE_DENY) && !mode.equals(MODE_SAMEORIGIN) && !mode.equals(MODE_ALLOWFROM)) {
            mode = MODE_DENY;
        }

        // If ALLOW-FROM, make sure a valid url is given, otherwise fallback to deny
        if (mode.equals(MODE_ALLOWFROM)) {
            if (StringUtils.isEmpty(url)) {
                mode = MODE_DENY;
            } else {
                try {
                    URL urlValue = new URL(url);
                    domain = urlValue.getHost() +
                            ((urlValue.getPort() == -1) ? "" : ":" + urlValue.getPort());

                } catch (MalformedURLException ex) {
                    mode = MODE_DENY;
                }
            }
        }
    }


    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse,
                         FilterChain filterChain) throws IOException, ServletException {

        HttpServletResponse response = (HttpServletResponse) servletResponse;
        response.addHeader("X-Frame-Options", getXFrameOptionsValue());
        response.addHeader("Content-Security-Policy", getContentSecurityPolicyFramAncestorsValue());

        filterChain.doFilter(servletRequest, response);
    }


    public void destroy() {
    }


    /**
     * Calculates the X-Frame-Options header value.
     *
     * @return X-Frame-Options header value.
     */
    private String getXFrameOptionsValue() {
        if (mode.equals(MODE_ALLOWFROM)) {
            return mode + " " + url;
        } else {
            return mode;
        }
    }


    /**
     * Calculates the Content-Security-Policy header frame-ancestors value.
     *
     * @return Content-Security-Policy header frame-ancestors value.
     */
    private String getContentSecurityPolicyFramAncestorsValue() {
        if (mode.equals(MODE_SAMEORIGIN)) {
            return "frame-ancestors 'self'";
        } else if (mode.equals(MODE_ALLOWFROM)) {
            return "frame-ancestors " + domain;
        } else {
            return "frame-ancestors 'none'";
        }
    }
}