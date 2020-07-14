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
import com.google.minicrm.data.Form;
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
 * Handles requests to /api/forms regarding anything related to the advertiser's forms.
 */
@WebServlet("/api/forms")
public final class FormsServlet extends HttpServlet {

  /**
   * Returns JSON representing the advertiser's unique webhook and all their Form data.
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
    Query query = new Query(Form.KIND_NAME)
        .setAncestor(Advertiser.generateKey(UserAuthenticationUtil.getCurrentUser()))
        .addSort("date", Query.SortDirection.DESCENDING);
    DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
    PreparedQuery preparedQuery = datastore.prepare(query);

    List<Form> forms = StreamSupport.stream(preparedQuery.asIterable().spliterator(), false)
        .map(Form::new).collect(Collectors.toList());

    response.setContentType("application/json;");
    response.getWriter().println(new FormsResponse(forms).toJson());
  }

  /**
   * Renames the form owned by the current user specified by the formId to formName.
   * Authentication required.
   *
   * Required parameters in the content body:
   *  - formId: the id of the form to be renamed
   *  - formName: the new name of the form
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
    String strFormId;
    String formName;
    if (contentType.contains("application/x-www-form-urlencoded")) {
      strFormId = request.getParameter("formId");
      formName = request.getParameter("formName");
    } else if (contentType.contains("application/json")) {
      Gson gson = new Gson();
      Map<String, String> jsonMap = gson.fromJson(request.getReader(), Map.class);
      strFormId = jsonMap.get("formId");
      formName = jsonMap.get("formName");
    } else {
      response.sendError(415,
          "Content type not supported. Try application/x-www-form-urlencoded or application/json.");
      return;
    }

    //request validation
    //check that the required parameters are provided
    if (strFormId == null || strFormId.isEmpty()) {
      response.sendError(400, "formId is not specified.");
      return;
    }
    if (formName == null || formName.isEmpty()) {
      response.sendError(400, "formName is not specified.");
      return;
    }
    //check form id is a long
    long formId;
    try {
      formId = Long.parseLong(strFormId);
    } catch (NumberFormatException e) {
      response.sendError(400, "formId should be an integer number.");
      return;
    }

    //get the form
    DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
    Key advertiserKey = Advertiser.generateKey(UserAuthenticationUtil.getCurrentUser());
    Key formKey = Form.generateKey(advertiserKey, formId);
    Form form;
    try {
      form = new Form(datastore.get(formKey));
    } catch (EntityNotFoundException e) {
      response.sendError(404, "Specified form not found.");
      return;
    }

    //modify the form name
    form.setFormName(formName);

    //store the updated lead
    DatastoreUtil.put(form);
    response.setStatus(204);
  }

  /**
   * Deletes the forms owned by the current user specified by the formIds array specified in
   * the request headers or as a url parameter.
   * Authentication required.
   *
   * HTTP Response Status Codes:
   * - 204 No Content: on successful deletion.
   * Note: returns 204 No Content even if the form to be deleted never existed in the first place.
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
    String[] strFormIds = request.getParameterValues("formIds[]");
    if (strFormIds == null || strFormIds.length == 0) {
      response.sendError(400, "formIds[] not specified or is empty.");
      return;
    }
    long[] formIds = Arrays.stream(strFormIds).mapToLong(Long::parseLong).toArray();
    User user = UserAuthenticationUtil.getCurrentUser();

    DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
    datastore.delete(Form.generateKeys(Advertiser.generateKey(user),formIds));

    response.setStatus(204); //Success - 204 No Content
  }

  /**
   * Response object providing an advertiser's forms to a GET request
   */
  private final class FormsResponse implements ClientResponse {

    /**
     * List of the User's forms
     */
    private final List<Form> forms;

    /**
     * Constructor for response to send back to user containing the webhook
     *
     * @param forms      list of all the user's forms
     */
    FormsResponse(List<Form> forms) {
      this.forms = new ArrayList<>(forms);
    }

    @Override
    public String toJson() {
      Gson gson = new GsonBuilder().disableHtmlEscaping().create();
      return gson.toJson(this);
    }
  }
}
