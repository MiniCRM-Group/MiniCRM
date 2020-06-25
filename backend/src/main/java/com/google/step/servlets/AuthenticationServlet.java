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

package com.google.step.servlets;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;
import com.google.appengine.api.users.User;
import com.google.gson.Gson;

@WebServlet("/authentication")
public class AuthenticationServlet extends HttpServlet {
    /**
     * Checks state of user in this class.
     */
    private static UserService userService;

    @Override
    public void init() {
        userService = UserServiceFactory.getUserService();
    }

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String url;
        boolean loggedIn = userService.isUserLoggedIn();
        if(loggedIn){
            url = userService.createLogoutURL("/");
        }
        else{
            url = userService.createLoginURL("/");
        }
        AuthenticationResponse authenticationResponse = new AuthenticationResponse(url, loggedIn);
        response.setContentType("application/json;");
        response.getWriter().println(authenticationResponse.getJson());
    }

    /**
     * Response object providing URL for logging in or logging out.
     */

    private final class AuthenticationResponse {
        /**
         * URL for user to login/logout.
         */
        private String url;

        /**
         * True if user is logged in and false otherwise.
         */
        private boolean loggedIn;

        /**
         * Constructor for response to send back to user for authentication.
         * @param url URL for logging in or out.
         * @param loggedIn Whether user is logged in or out.
         */
        AuthenticationResponse(String url, boolean loggedIn){
            this.url = url;
            this.loggedIn = loggedIn;
        }

        public String getJson(){
            Gson gson = new Gson();
            return gson.toJson(this);
        }
    }
}
