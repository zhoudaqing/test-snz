package io.terminus.snz.web.filters;

import io.terminus.snz.web.interceptors.LoginInterceptor;
import org.jasig.cas.client.validation.Cas10TicketValidationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.IOException;

/**
 * Author: Effet
 * Mail: ef@terminus.io
 * Date: 8/12/14
 */
public class CasLoginTicketValidationFilter extends Cas10TicketValidationFilter {

    @Override
    protected boolean preFilter(final ServletRequest servletRequest, final ServletResponse servletResponse, final FilterChain filterChain) throws IOException, ServletException {
        final HttpServletRequest request = (HttpServletRequest) servletRequest;
        final HttpSession session = request.getSession(false);

        Object rawUrl = session.getAttribute(LoginInterceptor.CAS_CURRENT_URL);

        String url = null;
        if (rawUrl == null) {
            url = request.getScheme() + "://" + request.getServerName();
            if (request.getServerPort() != 80) {
                url += ":" + request.getServerPort();
            }
            request.getSession().setAttribute(LoginInterceptor.CAS_CURRENT_URL, url);
        } else {
            url = (String)rawUrl;
        }

        setServerName(url);
        log.debug("Ticket Validation => setServerName : " + url);
        return true;
    }
}
