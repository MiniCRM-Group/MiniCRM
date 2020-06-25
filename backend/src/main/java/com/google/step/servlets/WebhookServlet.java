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

import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;
import com.google.appengine.api.users.User;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.FieldNamingPolicy;
import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.PreparedQuery;
import com.google.appengine.api.datastore.Query;
import com.google.appengine.api.datastore.Query.SortDirection;

import com.google.step.data.Lead;

import java.io.IOException;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Random;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.step.utils.AdvertiserUtil;
import com.google.step.utils.UserAuthenticationUtil;

/**
 * Servlet to act as the webhook to receive lead data from the Google Ads server
 *  Responds to GET requests with JSON with lead data.
 */
@WebServlet("/api/webhook")
public class WebhookServlet extends HttpServlet {
  private Lead myLead;
  private static final Gson gson = new GsonBuilder()
          .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
          .create();
  private char[] alphanumerics = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789".toCharArray();

  /**
   * Returns JSON representing the advertiser's webhook and google key
   * @param request       the HTTP Request
   * @param response      the HTTP Response
   * @throws IOException  if an input exception occurs with the response writer
   */
  @Override
  public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
    if (!UserAuthenticationUtil.isAuthenticated()) {
      response.sendRedirect("/");
      return;
    }
    User user = UserAuthenticationUtil.getCurrentUser();
    //generate URL-Safe Key string
    String advertiserKeyString = KeyFactory.keyToString(AdvertiserUtil.createAdvertiserKey(user));
    String webhookUrl = request.getScheme() + "://" +
            request.getServerName() + ":" +
            request.getServerPort() + "/api/webhook?id=" +
            advertiserKeyString;
    //generate random google key (currently 20 chars)
    Random rand = new SecureRandom();
    StringBuilder googleKeyBuilder = new StringBuilder(20);
    for (int i = 0; i < 20; i++) {
      googleKeyBuilder.append(alphanumerics[rand.nextInt(alphanumerics.length)]);
    }
    response.setContentType("application/json;");
    response.getWriter().println(new WebhookResponse(webhookUrl, googleKeyBuilder.toString()).toJson());
  }

  /**
   * Accepts a POST request containing JSON in the body describing a lead from Google Ads server.
   * @param request       the HTTP Request
   * @param response      the HTTP Response
   * @throws IOException  if an output exception occurs with the request reader
   */
  @Override
  public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
    DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
    myLead = Lead.fromReader(request.getReader());
    datastore.put(myLead.asEntity());
  }

  /**
   * Response object providing a user's webhook and randomly generated google_key
   */
  private final class WebhookResponse {
    /**
     * Webhook URL for Advertiser to put in Google Ads.
     */
    private String webhookUrl;

    /**
     * Randomly generated Google Key
     */
    private String googleKey;

    /**
     * Constructor for response to send back to user containing the webhook and google key
     * @param webhookUrl url to receive lead data from Google Ads
     * @param googleKey  randomly generated google key string
     */
    WebhookResponse(String webhookUrl, String googleKey) {
      this.webhookUrl = webhookUrl;
      this.googleKey = googleKey;
    }

    public String toJson(){
      Gson gson = new GsonBuilder().disableHtmlEscaping().create();
      return gson.toJson(this);
    }
  }
}
