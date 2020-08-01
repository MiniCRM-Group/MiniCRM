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

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.EntityNotFoundException;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;
import com.google.appengine.api.users.User;
import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.minicrm.data.Advertiser;
import com.google.minicrm.data.Campaign;
import com.google.minicrm.data.Form;
import com.google.minicrm.data.Lead;
import com.google.minicrm.interfaces.ClientResponse;
import com.google.minicrm.utils.DatastoreUtil;
import com.google.minicrm.utils.EmailUtil;
import com.google.minicrm.utils.UserAuthenticationUtil;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.mail.MessagingException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Handles requests to /api/webhook for anything related to each advertisers webhook.
 * Expects POST request from the Google Ads server containing new lead data.
 */
@WebServlet("/api/webhook")
public final class WebhookServlet extends HttpServlet {

  private static final Gson gson = new GsonBuilder()
      .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
      .create();
  private static final String ID_URL_PARAM = "id";

  /**
   * Returns JSON describing the currently logged in user's Google Key and unique webhook.
   * Authentication required.
   *
   * HTTP Response Status Codes:
   * - 200 OK: success
   * - 401 Unauthorized: if not logged in with Google
   *
   * @param request
   * @param response
   * @throws IOException
   */
  @Override
  public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
    if (!UserAuthenticationUtil.isAuthenticated()) {
      response.sendError(401, "Log in with Google to continue."); //401 Unauthorized
      return;
    }
    User user = UserAuthenticationUtil.getCurrentUser();
    Key advertiserKey = Advertiser.generateKey(user);
    DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
    Advertiser advertiser;
    try {
      advertiser = new Advertiser(datastore.get(advertiserKey));
    } catch (EntityNotFoundException e) {
      //shouldn't happen since authentication guarantees the entity exists
      response.sendError(500, e.toString());
      return;
    }
    String googleKey = advertiser.getGoogleKey();
    String webhook = advertiser.generateWebhook(request);

    response.setContentType("application/json;");
    response.getWriter().println(new WebhookResponse(webhook, googleKey).toJson());
  }

  /**
   * Accepts a POST request containing JSON in the body describing a lead from Google Ads server.
   * Expects an url parameter "id" with the advertiserKeyString.
   *
   * HTTP Response Status Codes:
   * - 200 OK: success
   * - 401 Unauthorized: if the advertiser's google key and the lead's google key do not match
   * - 404 Not Found: if the id parameter is not specified or blank or if the id specified is not
   *                  valid
   * - 409 Conflict: if the lead sent already has been successfully sent
   *
   * @param request  the HTTP Request
   * @param response the HTTP Response
   * @throws IOException if an output exception occurs with the request reader
   */
  @Override
  public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
    String advertiserKeyString = request.getParameter(ID_URL_PARAM);
    if (advertiserKeyString == null || advertiserKeyString.equals("")) {
      response.sendError(404, "Invalid webhook url. Need to specify id parameter.");
      return; //stop execution, we expect an id param in the url
    }
    Key advertiserKey = KeyFactory.stringToKey(advertiserKeyString);
    Advertiser advertiser;
    DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
    try {
      advertiser = new Advertiser(datastore.get(advertiserKey));
    } catch (EntityNotFoundException e) {
      response.sendError(404,
          "Invalid webhook url. Advertiser has not registered with our site.");
      return;
    }
    Lead newLead = Lead.fromReader(request.getReader(), advertiserKey);

    //verify the lead - has correct google key for this advertiser
    if (!newLead.getGoogleKey().equals(advertiser.getGoogleKey())) {
      response.sendError(401, "The provided Google Key and webhook do not match.");
      return;
    }

    //de-duplicate the lead
    Key leadKey = Lead.generateKey(advertiserKey, newLead.getLeadId());
    if (DatastoreUtil.exists(leadKey)) {
      response.sendError(409, "This lead already has been successfully sent and exists.");
      return;
    }

    //check if this lead belongs to a new form, if it does make a new form entity
    Key formKey = Form.generateKey(advertiserKey, newLead.getFormId());
    if (!DatastoreUtil.exists(formKey)) { //form does not exist
      long formId = newLead.getFormId();
      Form newForm = new Form(advertiserKey, formId, Long.toString(formId));
      DatastoreUtil.put(newForm);
    }

    //check if this lead belongs to a new campaign, if it does make a new campaign entity
    Key campaignKey = Campaign.generateKey(advertiserKey, newLead.getCampaignId());
    if (!DatastoreUtil.exists(campaignKey)) {
      long campaignId = newLead.getCampaignId();
      Campaign newCampaign = new Campaign(advertiserKey, campaignId, Long.toString(campaignId));
      DatastoreUtil.put(newCampaign);
    }

    //send an email notification to the advertiser that they have a new lead
    try {
      EmailUtil.sendNewLeadEmail(advertiser.getUser());
    } catch (MessagingException e) {
      Logger logger = Logger.getLogger("com.google.step.servlets.WebhookServlet");
      logger.log(Level.SEVERE, "Failed to send new lead email notification.", e);
      //TODO: Determine what happens when the email fails
    }
    datastore.put(newLead.asEntity());
  }

  /**
   * Represents a response to a POST request containing a user's unique webhook and
   * form_id and google_key of the form created.
   */
  private final class WebhookResponse implements ClientResponse {

    /**
     * Webhook URL for Advertiser to put in Google Ads.
     */
    private final String webhookUrl;

    /**
     * Randomly generated Google Key
     */
    private final String googleKey;

    /**
     * Constructor for response to send back to user containing the webhook and google key
     *
     * @param webhookUrl url to receive lead data from Google Ads
     * @param googleKey  randomly generated google key string
     */
    WebhookResponse(String webhookUrl, String googleKey) {
      this.webhookUrl = webhookUrl;
      this.googleKey = googleKey;
    }

    @Override
    public String toJson() {
      Gson gson = new GsonBuilder().disableHtmlEscaping().create();
      return gson.toJson(this);
    }
  }
}