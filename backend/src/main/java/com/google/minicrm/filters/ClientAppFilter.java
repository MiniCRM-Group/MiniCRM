package com.google.minicrm.filters;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Filters any Angular routing URLs from requests made by Angular app.
 */
@WebFilter("/*")
public final class ClientAppFilter implements Filter {

  @Override
  public void init(FilterConfig filterConfig) {
  }

  @Override
  public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse,
      FilterChain filterChain) throws IOException, ServletException {
    HttpServletRequest request = (HttpServletRequest) servletRequest;
    HttpServletResponse response = (HttpServletResponse) servletResponse;
    String requestUrl = request.getRequestURL().toString();
    String path;
    try {
      path = new URI(requestUrl).getPath();
    } catch (URISyntaxException e) {
      response.sendError(400, e.getMessage());
      return;
    }
    if (isValidUrl(path)) {
      //allowed, continue navigation
      filterChain.doFilter(servletRequest, servletResponse);
    } else {
      //Angular URL, send back to index.html
      RequestDispatcher dispatcher = servletRequest.getRequestDispatcher("/");
      dispatcher.forward(servletRequest, servletResponse);
    }
  }

  @Override
  public void destroy() {
  }

  private boolean isValidUrl(String url) {
    // valid urls start with /api (for API endpoints) or /_ah (for other GCP URLs)
    return url.startsWith("/api") || url.startsWith("/_ah");
  }
}
