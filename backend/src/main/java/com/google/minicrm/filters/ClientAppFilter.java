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

  /**
   * Directs the client request url to the correct Java Servlet or Angular route.
   *
   * HTTP Response Status Codes:
   * - 200 OK: success
   * - 400 Bad Request: if the request url is invalid
   * @param servletRequest    the HTTP servlet request
   * @param servletResponse   the HTTP servlet response
   * @param filterChain       the filter chain
   * @throws IOException      if an error occurs with the filter chain or the forwarded resource
   * @throws ServletException if an error occurs with the filter chain or the forwarded resource
   */
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

  /**
   * Returns whether or not the passed in url is a valid api url.
   * @param url the String representation of the url
   * @return    true if the url is a valid api url, false otherwise.
   */
  private boolean isValidUrl(String url) {
    // valid urls start with /api (for API endpoints) or /_ah (for other GCP URLs)
    return url.startsWith("/api") || url.startsWith("/_ah");
  }
}
