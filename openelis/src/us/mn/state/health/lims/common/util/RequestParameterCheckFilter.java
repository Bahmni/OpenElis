package us.mn.state.health.lims.common.util;

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

public class RequestParameterCheckFilter implements Filter {
    private final Logger logger = LogManager.getLogger(this.getClass());

    public void destroy() {
    }

    public void doFilter(ServletRequest request, ServletResponse response,
                         FilterChain chain) throws IOException, ServletException {
        request.setCharacterEncoding("UTF8");
        HttpServletRequest httpServletRequest = (HttpServletRequest) request;
        if (httpServletRequest.getServletPath().endsWith(".do")) {
            logger.debug(String.format("Using safe request: %s", httpServletRequest.getServletPath()));
            chain.doFilter(new SafeRequest(httpServletRequest), response);
        }
        else
            chain.doFilter(request, response);
    }

    public void init(FilterConfig filterConfig) throws ServletException {
    }
}
