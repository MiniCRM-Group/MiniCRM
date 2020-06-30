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

import com.google.appengine.api.datastore.*;
import com.google.gson.FieldNamingPolicy;
import com.google.gson.GsonBuilder;
import com.google.step.data.Lead;
import com.google.step.utils.AdvertiserUtil;
import com.google.step.utils.UserAuthenticationUtil;

import java.util.List;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;

import com.google.gson.Gson;

/**
 * Handles GET request to return Lead data in JSON to the client.
 */
@WebServlet("/api/leads")
public class LeadsServlet extends HttpServlet {
    private static final Gson gson = new GsonBuilder()
            .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
            .create();

    /**
     * Returns JSON representing all leads in the datastore sorted by time in response to a GET request
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
        DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
        Query query = new Query("Lead")
                .setAncestor(AdvertiserUtil.createAdvertiserKey(UserAuthenticationUtil.getCurrentUser()))
                .addSort("date", Query.SortDirection.DESCENDING);
        PreparedQuery queryResults = datastore.prepare(query);
        List<Lead> leads = new ArrayList<>();
        for (Entity leadEntity : queryResults.asIterable()) {
            leads.add(new Lead(leadEntity));
        }
        response.setContentType("application/json;");
        response.getWriter().println(gson.toJson(leads));
    }
}
