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
import com.google.appengine.api.datastore.PreparedQuery;
import com.google.appengine.api.datastore.Query;
import com.google.appengine.api.users.User;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.minicrm.data.Advertiser;
import com.google.minicrm.data.Form;
import com.google.minicrm.interfaces.ClientResponse;
import com.google.minicrm.utils.UserAuthenticationUtil;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
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
   * Response object providing a user's webhook and all their forms to a GET request
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
