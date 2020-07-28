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
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.EntityNotFoundException;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.PreparedQuery;
import com.google.appengine.api.datastore.Query;
import com.google.appengine.api.users.User;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.minicrm.data.Advertiser;
import com.google.minicrm.data.Campaign;
import com.google.minicrm.interfaces.ClientResponse;
import com.google.minicrm.utils.DatastoreUtil;
import com.google.minicrm.utils.UserAuthenticationUtil;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.lang3.ArrayUtils;

/**
 * Handles requests to /api/campaigns regarding anything related to the advertiser's campaigns.
 */
@WebServlet("/api/campaigns")
public final class CampaignsServlet extends HttpServlet {

  /**
   * Returns JSON representing the advertiser's unique webhook and all their Campaign data.
   * Authentication required.
   *
   * HTTP Response Status Codes:
   * - 200 OK: Success
   * - 401 Unauthorized: if not logged in with Google
   *
   * @param request  the HTTP Request
   * @param response the HTTP Response
   * @throws IOException if an input exception occurs with the response writer or reader
   */
  @Override
  public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
    if (!UserAuthenticationUtil.isAuthenticated()) {
      response.sendError(401, "Log in with Google to continue."); //401 Unauthorized
      return;
    }
    Query query = new Query(Campaign.KIND_NAME)
        .setAncestor(Advertiser.generateKey(UserAuthenticationUtil.getCurrentUser()))
        .addSort("date", Query.SortDirection.DESCENDING);
    DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
    PreparedQuery preparedQuery = datastore.prepare(query);

    List<Campaign> campaigns = StreamSupport.stream(preparedQuery.asIterable().spliterator(), false)
        .map(Campaign::new).collect(Collectors.toList());

    response.setContentType("application/json;");
    response.getWriter().println(new CampaignsResponse(campaigns).toJson());
  }

  /**
   * Renames the campaign owned by the current user specified by the campaignId to campaignName.
   * Authentication required.
   *
   * Required parameters in the content body:
   *  - campaignId: the id of the campaign to be renamed
   *  - campaignName: the new name of the campaign
   * Accepted content-encodings:
   *  - application/x-www-form-urlencoded
   *  - application/json
   * HTTP Response Status Codes:
   * - 204 No Content: on successful rename
   * - 400 Bad Request: if the request does not contain the required fields
   * - 401 Unauthorized: if the user is not logged in with google
   * - 415 Not Support: if the content body is not a valid type
   *
   * @param request   the HTTP request
   * @param response  the HTTP response
   * @throws IOException if any IO error occurs
   */
  @Override
  public void doPut(HttpServletRequest request, HttpServletResponse response) throws IOException {
    if (!UserAuthenticationUtil.isAuthenticated()) {
      response.sendError(401, "Log in with Google to continue."); //401 Unauthorized
      return;
    }

    //read the content body
    String contentType = request.getContentType();
    String strCampaignId;
    String campaignName;
    if (contentType.contains("application/x-www-form-urlencoded")) {
      strCampaignId = request.getParameter("campaignId");
      campaignName = request.getParameter("campaignName");
    } else if (contentType.contains("application/json")) {
      Gson gson = new Gson();
      Map<String, String> jsonMap = gson.fromJson(request.getReader(), Map.class);
      if (jsonMap == null) {
        response.sendError(400, "Content body with formId and formName required.");
        return;
      }
      strCampaignId = jsonMap.get("campaignId");
      campaignName = jsonMap.get("campaignName");
    } else {
      response.sendError(415,
          "Content type not supported. Try application/x-www-form-urlencoded or application/json.");
      return;
    }

    //request validation
    //check that the required parameters are provided
    if (strCampaignId == null || strCampaignId.isEmpty()) {
      response.sendError(400, "campaignId is not specified.");
      return;
    }
    if (campaignName == null || campaignName.isEmpty()) {
      response.sendError(400, "campaignName is not specified.");
      return;
    }
    //check campaign id is a long
    long campaignId;
    try {
      campaignId = Long.parseLong(strCampaignId);
    } catch (NumberFormatException e) {
      response.sendError(400, "campaignId should be an integer number.");
      return;
    }

    //get the campaign
    DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
    Key advertiserKey = Advertiser.generateKey(UserAuthenticationUtil.getCurrentUser());
    Key campaignKey = Campaign.generateKey(advertiserKey, campaignId);
    Campaign campaign;
    try {
      campaign = new Campaign(datastore.get(campaignKey));
    } catch (EntityNotFoundException e) {
      response.sendError(404, "Specified campaign not found.");
      return;
    }

    //modify the campaign name
    campaign.setCampaignName(campaignName);

    //store the updated lead
    DatastoreUtil.put(campaign);
    response.setStatus(204);
  }

  /**
   * Deletes the campaigns owned by the current user specified by the campaignIds array specified in
   * the request headers or as a url parameter.
   * Authentication required.
   *
   * HTTP Response Status Codes:
   * - 204 No Content: on successful deletion.
   * Note: returns 204 No Content even if the campaign to be deleted never existed in the first place.
   * Instead, guarantees that it doesn't exist anymore in the datastore.
   * - 400 Bad Request: if the request does not contain the required fields
   * - 401 Unauthorized: if not logged in with Google
   *
   * @param request  the HTTP Request
   * @param response the HTTP Response
   * @throws IOException if any IO error occurs
   */
  @Override
  public void doDelete(HttpServletRequest request, HttpServletResponse response)
      throws IOException {

    if (!UserAuthenticationUtil.isAuthenticated()) {
      response.sendError(401, "Log in with Google to continue."); //401 Unauthorized
      return;
    }
    String[] strCampaignIds = request.getParameterValues("campaignIds[]");
    if (strCampaignIds == null || strCampaignIds.length == 0) {
      response.sendError(400, "campaignIds[] not specified or is empty.");
      return;
    }
    long[] campaignIds = Arrays.stream(strCampaignIds).mapToLong(Long::parseLong).toArray();
    User user = UserAuthenticationUtil.getCurrentUser();

    DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
    datastore.delete(Campaign.generateKeys(Advertiser.generateKey(user),campaignIds));

    response.setStatus(204); //Success - 204 No Content
  }

  /**
   * Response object providing an advertiser's campaigns to a GET request
   */
  private final class CampaignsResponse implements ClientResponse {

    /**
     * List of the User's campaigns
     */
    private final List<Campaign> campaigns;

    /**
     * Constructor for response to send back to user containing the webhook
     *
     * @param campaigns      list of all the user's campaigns
     */
    CampaignsResponse(List<Campaign> campaigns) {
      this.campaigns = new ArrayList<>(campaigns);
    }

    @Override
    public String toJson() {
      Gson gson = new GsonBuilder().disableHtmlEscaping().create();
      return gson.toJson(this);
    }
  }
}
