package com.google.step.filters;

import javax.servlet.annotation.WebFilter;
import java.io.IOException;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.logging.Logger;
import java.util.logging.Level;
import java.net.URI;
import java.net.URISyntaxException;

@WebFilter("/*")
public class ClientAppFilter implements Filter {
    private Logger logger;

    @Override
    public void init(FilterConfig filterConfig) {
        logger = Logger.getLogger("com.google.step.clientappfilter");
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;
        String requestUrl = request.getRequestURL().toString();
        String path = "";
        try {
            path = new URI(requestUrl).getPath();
        } catch(URISyntaxException e) {
            logger.log(Level.SEVERE, "bad uri");
        }
        if (validUrl(path)) {
            //allowed, continue navigation
            filterChain.doFilter(servletRequest, servletResponse);
            logger.log(Level.WARNING, "valid endpoint");
        } else {
            //Angular URL, send back to index.html
            RequestDispatcher dispatcher = servletRequest.getRequestDispatcher("/");
            dispatcher.forward(servletRequest, servletResponse);
            logger.log(Level.WARNING, "invalid endpoint");
        }
    }

    @Override
    public void destroy() {

    }

    private boolean validUrl(String url) {
        //implement how to validate the URL
        return url.startsWith("/api") || url.startsWith("/_ah");
    }
}