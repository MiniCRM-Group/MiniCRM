package com.google.minicrm.filters;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.PreparedQuery;
import com.google.appengine.api.datastore.Query;
import com.google.appengine.repackaged.org.apache.commons.codec.language.bm.Lang;
import com.google.minicrm.data.Advertiser;
import com.google.minicrm.data.Language;
import com.google.minicrm.data.Settings;
import com.google.minicrm.utils.UserAuthenticationUtil;

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
    if (isValidApiUrl(path)) {
      //allowed, continue navigation
      filterChain.doFilter(servletRequest, servletResponse);
    } else {
      redirectToClient(servletRequest, servletResponse, path);
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
  private boolean isValidApiUrl(String url) {
    // valid urls start with /api (for API endpoints) or /_ah (for other GCP URLs)
    return url.startsWith("/api") || url.startsWith("/_ah");
  }

  private void redirectToClient(ServletRequest servletRequest, ServletResponse servletResponse,
                                String path) throws IOException, ServletException {
    RequestDispatcher dispatcher;
    if (UserAuthenticationUtil.isAuthenticated()) {
      Settings settings = UserAuthenticationUtil.getCurrentUserSettings();
      Language lang = settings.getLanguage();
      dispatcher = servletRequest.getRequestDispatcher(localizePath("/" +
              (lang == Language.ENGLISH ? "" : lang.getIsoCode())));
    } else {
      dispatcher = servletRequest.getRequestDispatcher(localizePath(path));
    }
    dispatcher.forward(servletRequest, servletResponse);
  }

  private String localizePath(String url) {
    if (url.startsWith("/hi")) {
      return "/hi/";
    } else if (url.startsWith("/es")) {
      return "/es/";
    } else if (url.startsWith("/pt")) {
      return "/pt/";
    } else {
      return "/en/";
    }
  }
}
