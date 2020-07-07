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
import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.minicrm.data.Lead;
import com.google.minicrm.utils.AdvertiserUtil;
import com.google.minicrm.utils.UserAuthenticationUtil;
import java.io.IOException;
import java.util.List;
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
        .setAncestor(AdvertiserUtil.createAdvertiserKey(UserAuthenticationUtil.getCurrentUser()))
        .addSort("date", Query.SortDirection.DESCENDING);
    PreparedQuery preparedQuery = datastore.prepare(query);

    List<Lead> leads = StreamSupport.stream(preparedQuery.asIterable().spliterator(), false)
        .map(Lead::new).collect(Collectors.toList());

    response.setContentType("application/json;");
    response.getWriter().println(gson.toJson(leads));
  }
}
