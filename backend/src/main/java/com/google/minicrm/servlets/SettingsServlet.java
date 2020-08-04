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
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.PreparedQuery;
import com.google.appengine.api.datastore.Query;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.stream.MalformedJsonException;
import com.google.i18n.phonenumbers.NumberParseException;
import com.google.minicrm.data.Advertiser;
import com.google.minicrm.data.Currency;
import com.google.minicrm.data.NotificationsFrequency;
import com.google.minicrm.data.Settings;
import com.google.minicrm.interfaces.ClientResponse;
import com.google.minicrm.utils.UserAuthenticationUtil;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.validator.ValidatorException;

@WebServlet("/api/settings")
public final class SettingsServlet extends HttpServlet {
    /**
     * Accepts a GET request for the user's settings and responds with a settings JSON to view
     * notification, language, and currency configs.
     *
     * HTTP Response Status Codes:
     * - 200 OK: success
     * - 401 Unauthorized: if no advertiser is not logged in
     *
     * @param req  the HTTP Request
     * @param resp the HTTP Response
     * @throws IOException if an output exception occurs with the request reader
     */
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        if(!UserAuthenticationUtil.isAuthenticated()) {
            resp.sendError(401, "Log in with Google to continue."); //401 Unauthorized
            return;
        }
        Query query = new Query(Settings.KIND_NAME)
                .setAncestor(Advertiser.generateKey(UserAuthenticationUtil.getCurrentUser()));
        DatastoreService datastoreService = DatastoreServiceFactory.getDatastoreService();
        PreparedQuery preparedQuery = datastoreService.prepare(query);
        Settings settings = new Settings(preparedQuery.asSingleEntity());
        resp.setContentType("application/json;");
        resp.getWriter().println(new SettingsResponse(settings).toJson());
    }

    /**
     * Accepts a PUT request to edit the user's settings and responds with an edited settings JSON to view
     * notification, language, and currency configs.
     *
     * HTTP Response Status Codes:
     * - 200 OK: success
     * - 400 Bad Request: if JSON body is missing. phone number can't be parsed, or if phone number of email are invalid
     *
     * @param req  the HTTP Request
     * @param resp the HTTP Response
     * @throws IOException if an output exception occurs with the request reader
     */
    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        if(!UserAuthenticationUtil.isAuthenticated()) {
            resp.sendError(401, "Log in with Google to continue."); //401 Unauthorized
            return;
        }
        Key advertiserKey = Advertiser.generateKey(UserAuthenticationUtil.getCurrentUser());
        DatastoreService datastoreService = DatastoreServiceFactory.getDatastoreService();
        Settings settings;
        try {
            settings = Settings.fromReader(req.getReader(), advertiserKey);
        } catch (NumberParseException e) {
            resp.sendError(400, "Error parsing phone number: " + e.getMessage());
            return;
        } catch (ValidatorException e) {
            resp.sendError(400, "Invalid phone number or email: " + e.getMessage());
            return;
        } catch (MalformedJsonException e) {
            resp.sendError(400, "Problem reading body JSON" + e.getMessage());
            return;
        }
        datastoreService.put(settings.asEntity());
        resp.setContentType("application/json;");
        resp.getWriter().println(new SettingsResponse(settings).toJson());
    }

    /**
     * Provides settings info as well as lists of supported currencies, languages, and notification frequencies
     * so that web client can offer selections for editing settings.
     */
    private final class SettingsResponse implements ClientResponse {
        private Settings settings;
        private List<Map<String, String>> supportedCurrencies;
        private List<Map<String, String>> supportedNotifsFreqs;

        SettingsResponse(Settings settings) {
            this.settings = settings;
            this.supportedCurrencies = Currency.supportedCurrencies;
            this.supportedNotifsFreqs = NotificationsFrequency.supportedNotificationsFrequencies;
        }

        @Override
        public String toJson() {
            Gson gson = new GsonBuilder().disableHtmlEscaping().create();
            return gson.toJson(this);
        }
    }
}
