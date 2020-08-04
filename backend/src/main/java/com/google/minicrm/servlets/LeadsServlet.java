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
import com.google.appengine.api.datastore.PreparedQuery;
import com.google.appengine.api.datastore.Query;
import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.minicrm.data.Advertiser;
import com.google.minicrm.data.Lead;
import com.google.minicrm.data.LeadStatus;
import com.google.minicrm.interfaces.ClientResponse;
import com.google.minicrm.utils.DatastoreUtil;
import com.google.minicrm.utils.UserAuthenticationUtil;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Handles requests to /api/leads regarding anything related to the advertiser's leads.
 */
@WebServlet("/api/leads")
public final class LeadsServlet extends HttpServlet {

  private static final Gson gson = new GsonBuilder()
      .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
      .create();

  /**
   * Returns JSON representing all leads in the datastore owned by the current logged in user
   * sorted by time.
   *
   * HTTP Response Status Codes:
   * - 200 OK: on success
   * - 401 Unauthorized: if not logged in with Google
   *
   * @param request  the HTTP Request
   * @param response the HTTP Response
   * @throws IOException if an input exception occurs with the response writer
   */
  @Override
  public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
    if (!UserAuthenticationUtil.isAuthenticated()) {
      response.sendError(401, "Log in with Google to continue."); //401 Unauthorized
      return;
    }
    DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
    Query query = new Query(Lead.KIND_NAME)
        .setAncestor(Advertiser.generateKey(UserAuthenticationUtil.getCurrentUser()))
        .addSort("date", Query.SortDirection.DESCENDING);
    PreparedQuery preparedQuery = datastore.prepare(query);

    List<Lead> leads = StreamSupport.stream(preparedQuery.asIterable().spliterator(), false)
        .map(Lead::new).collect(Collectors.toList());

    response.setContentType("application/json;");
    response.getWriter().println(new LeadsResponse(leads).toJson());
  }

  /**
   * Updates the advertiser-modifiable fields (status and notes) of a lead specified by leadId.
   * Authentication required.
   *
   * Required parameters in the content body:
   *  - leadId : String uniquely identifying a lead
   * Optional parameters to be updated:
   *  - status : the status of the lead represented as a String
   *  - notes : the notes of the lead represented as a String
   * Accepted content-encodings:
   *  - application/x-www-form-urlencoded
   *  - application/json
   *
   * HTTP Response Status Codes:
   * - 204 No Content: lead edited successfully
   * - 400 Bad Request: if the request does not contain the required fields or has an invalid status
   * - 401 Unauthorized: if not logged in with Google
   * - 404 Not Found: if the lead with the specified lead_id is not found
   * - 415 Not Supported: if content body is not a valid type
   *
   * @param request  the HTTP Request. Expecting parameter form_id with the form_id to add
   * @param response the HTTP Response
   * @throws IOException if an input exception occurs with the response writer or reader
   */
  @Override
  public void doPut(HttpServletRequest request, HttpServletResponse response) throws IOException {
    if (!UserAuthenticationUtil.isAuthenticated()) {
      response.sendError(401, "Log in with Google to continue."); //401 Unauthorized
      return;
    }

    //read the content body
    String contentType = request.getContentType();
    String leadId;
    String status;
    String notes;
    if (contentType.contains("application/x-www-form-urlencoded")) {
      leadId = request.getParameter("leadId");
      status = request.getParameter("status");
      notes = request.getParameter("notes");
    } else if (contentType.contains("application/json")) {
      Gson gson = new Gson();
      Map<String, String> jsonMap = gson.fromJson(request.getReader(), Map.class);
      if (jsonMap == null) {
        response.sendError(400, "Content body with leadId required.");
        return;
      }
      leadId = jsonMap.get("leadId");
      status = jsonMap.get("status");
      notes = jsonMap.get("notes");
    } else {
      response.sendError(415,
          "Content type not supported. Try application/x-www-form-urlencoded or application/json.");
      return;
    }

    //check that the required parameters are provided
    if (leadId == null || leadId.isEmpty()) {
      response.sendError(400, "leadId is not specified.");
      return;
    }

    //get the lead
    DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
    Key advertiserKey = Advertiser.generateKey(UserAuthenticationUtil.getCurrentUser());
    Key leadKey = Lead.generateKey(advertiserKey, leadId);
    Lead lead;
    try {
      lead = new Lead(datastore.get(leadKey));
    } catch (EntityNotFoundException e) {
      response.sendError(404, "Specified lead not found.");
      return;
    }

    //modify the provided data fields
    //check for status
    if (status != null && !status.isEmpty()) { //status is provided
      LeadStatus leadStatus;
      try {
        leadStatus = LeadStatus.valueOf(status);
      } catch (IllegalArgumentException e) {
        response.sendError(400,
            "Invalid status code. Valid status codes: NEW, OPEN, WORKING, CLOSED_CONVERTED, CLOSED_NOT_CONVERTED");
        return;
      }
      lead.setStatus(leadStatus);
    }
    //check for notes
    if (notes != null) { //notes is provided. blank string is valid.
      lead.setNotes(notes);
    }

    //store the updated lead
    DatastoreUtil.put(lead);
    response.setStatus(204);
  }

  /**
   * Response object providing all of an advertiser's leads
   */
  private final class LeadsResponse implements ClientResponse {

    /**
     * List of the User's forms
     */
    private final List<Lead> leads;

    /**
     * Constructor for response to send back to user containing the webhook
     *
     * @param leads     list of all the user's leads
     */
    LeadsResponse(List<Lead> leads) {
      this.leads = new ArrayList<>(leads);
    }

    @Override
    public String toJson() {
      Gson gson = new GsonBuilder().disableHtmlEscaping().create();
      return gson.toJson(this);
    }
  }
}
