// Copyright 2019 Google LLC
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
//     https://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.

package com.google.minicrm.servlets;

import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;
import com.google.gson.Gson;
import com.google.minicrm.data.Language;
import com.google.minicrm.interfaces.ClientResponse;
import com.google.minicrm.utils.UserAuthenticationUtil;

import java.io.IOException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Handles requests to /api/login to check if user is logged in and returns the corresponding URL
 * for logging in/out.
 */
@WebServlet("/api/login")
public final class LoginServlet extends HttpServlet {

  /**
   * Checks state of user in this class.
   */
  private static UserService userService;

  @Override
  public void init() {
    userService = UserServiceFactory.getUserService();
  }

  /**
   * Returns JSON representing whether or not the client is logged in with Google and a
   * corresponding login/logout URL.
   *
   * HTTP Response Status Codes:
   * - 200 OK: Success
   *
   * @param request  the HTTP Request
   * @param response the HTTP Response
   * @throws IOException if an input exception occurs with the response writer or reader
   */
  @Override
  public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
    Language language = Language.fromIsoCode(request.getParameter("language"));
    String url;
    boolean loggedIn = userService.isUserLoggedIn();
    if (loggedIn) {
      url = userService.createLogoutURL("/" + language.getIsoCode());
    } else {
      url = userService.createLoginURL("/" + language.getIsoCode());
    }
    LoginClientResponse loginClientResponse = new LoginClientResponse(url, loggedIn);
    response.setContentType("application/json;");
    response.getWriter().println(loginClientResponse.toJson());
  }

  /**
   * Response object providing URL for logging in or logging out and whether the user is logged in
   * or not.
   */
  private final class LoginClientResponse implements ClientResponse {

    private String url;
    private boolean loggedIn;

    /**
     * Constructor for response to send back to user for authentication.
     *
     * @param url      URL for logging in or out.
     * @param loggedIn Whether user is logged in or out.
     */
    LoginClientResponse(String url, boolean loggedIn) {
      this.url = url;
      this.loggedIn = loggedIn;
    }

    @Override
    public String toJson() {
      Gson gson = new Gson();
      return gson.toJson(this);
    }
  }
}
