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

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.FetchOptions;
import com.google.appengine.api.datastore.KeyFactory;
import com.google.appengine.api.datastore.PreparedQuery;
import com.google.appengine.api.datastore.Query;
import com.google.appengine.api.users.User;
import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.step.data.Form;
import com.google.step.utils.AdvertiserUtil;
import com.google.step.utils.UserAuthenticationUtil;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;

@WebServlet("/api/forms")
public class FormsServlet extends HttpServlet {
    private static final String ID_URL_PARAM = "id";
    private char[] alphanumerics = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789".toCharArray();

    /**
     * Returns JSON representing the advertiser's unique webhook and all their Form data.
     * @param request       the HTTP Request
     * @param response      the HTTP Response
     * @throws IOException  if an input exception occurs with the response writer or reader
     */
    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        if (!UserAuthenticationUtil.isAuthenticated()) {
            response.sendRedirect("/");
            return;
        }
        String webhookUrl = generateUserWebhook(request, UserAuthenticationUtil.getCurrentUser());
        Query query = new Query("Form")
                .setAncestor(AdvertiserUtil.createAdvertiserKey(UserAuthenticationUtil.getCurrentUser()))
                .addSort("date", Query.SortDirection.DESCENDING);
        DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
        PreparedQuery preparedQuery = datastore.prepare(query);
        List<Form> forms = new ArrayList<>();
        for (Entity formEntity : preparedQuery.asIterable()) {
            forms.add(new Form(formEntity));
        }
        response.setContentType("application/json;");
        response.getWriter().println(new FormsResponse(webhookUrl, forms).toJson());
    }

    /**
     * Returns JSON representing the advertiser's webhook and google key
     * Request body needs to contain form_id and form_name in application/x-www-form-urlencoded or application/json
     * Example POST request using application/x-www-form-urlencoded:
     * fetch('http://localhost:8080/api/forms', {
     *     method: 'POST',
     *     body: params,
     * })
     * .then(res => res.json())
     * .then(console.log)
     *
     *  where params is a URLSearchParams object with keys form_id and form_name.
     *  URLSearchParams automatically sets encoding to application/x-www-form-urlencoded.
     *  Example POST request using application/json:
     *  fetch('http://localhost:8080/api/forms', {
     *   method: 'POST',
     *   body: JSON.stringify({
     *     form_id: "1234",
     *     form_name: "exampleForm"
     *   }),
     *   headers: {
     *     'Content-type': 'application/json; charset=UTF-8'
     *   }
     * })
     * .then(res => res.json())
     * .then(console.log)
     *
     * Note: form_id should be a string not a number without quotation marks.
     * @param request       the HTTP Request. Expecting parameter form_id with the form_id to add
     * @param response      the HTTP Response
     * @throws IOException  if an input exception occurs with the response writer or reader
     */
    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        if (!UserAuthenticationUtil.isAuthenticated()) {
            response.sendRedirect("/");
            return;
        }
        //TODO: Change to JSON instead of urlencoding
        long formId;
        String formName;
        if (request.getContentType().contains("application/x-www-form-urlencoded")) {
            System.out.println("form-urlencoding!");
            formId = Long.parseLong(request.getParameter("form_id"));
            formName = request.getParameter("form_name");
        } else if (request.getContentType().contains("application/json")) {
            System.out.println("JSON!");
            Gson gson = new Gson();
            Map<String, String> jsonMap = gson.fromJson(request.getReader(), Map.class);
            formId = Long.parseLong(jsonMap.get("form_id"));
            formName = jsonMap.get("form_name");
        } else {
            response.sendError(415,
                    "Content type not supported. Try application/x-www-form-urlencoded or application/json.");
            return;
        }

        DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
        Query query = new Query("Form")
                .setFilter(new Query.FilterPredicate("formId", Query.FilterOperator.EQUAL, formId))
                .setFilter(new Query.FilterPredicate("verified", Query.FilterOperator.EQUAL, true))
                .setKeysOnly();
        PreparedQuery queryResults = datastore.prepare(query);
        if (queryResults.asList(FetchOptions.Builder.withDefaults()).size() > 0) { //there already exists a verified form with this id
            response.sendError(403, "Form ID already claimed by anoter user.");
        }


        String webhookUrl = generateUserWebhook(request, UserAuthenticationUtil.getCurrentUser());
        //generate random google key (currently 20 chars)
        Random rand = new SecureRandom();
        StringBuilder googleKeyBuilder = new StringBuilder(20);
        for (int i = 0; i < 20; i++) {
            googleKeyBuilder.append(alphanumerics[rand.nextInt(alphanumerics.length)]);
        }
        String googleKey = googleKeyBuilder.toString();

        //store the form entity in the datastore
        Form newForm = new Form(formId,
                formName,
                AdvertiserUtil.createAdvertiserKey(UserAuthenticationUtil.getCurrentUser()),
                googleKey);
        datastore.put(newForm.asEntity());

        response.setContentType("application/json;");
        response.getWriter().println(new WebhookResponse(webhookUrl, googleKey, formId).toJson());
    }

    /**
     * @return the webhook for this user with a URL-Safe Key String uniquely identifying the user
     */
    private String generateUserWebhook(HttpServletRequest request, User user) {
        //generate URL-Safe Key string
        String advertiserKeyString = KeyFactory.keyToString(AdvertiserUtil.createAdvertiserKey(user));
        return request.getScheme() + "://" +
                request.getServerName() + ":" +
                request.getServerPort() + "/api/webhook?" + ID_URL_PARAM + "=" +
                advertiserKeyString;
    }

    /**
     * Response object providing a user's webhook and randomly generated google_key to a POST request
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
         * The id of the form
         */
        private long formId;

        /**
         * Constructor for response to send back to user containing the webhook and google key
         * @param webhookUrl url to receive lead data from Google Ads
         * @param googleKey  randomly generated google key string
         * @param formId     id of the form
         */
        WebhookResponse(String webhookUrl, String googleKey, long formId) {
            this.webhookUrl = webhookUrl;
            this.googleKey = googleKey;
            this.formId = formId;
        }

        public String toJson(){
            Gson gson = new GsonBuilder().disableHtmlEscaping().create();
            return gson.toJson(this);
        }
    }

    /**
     * Response object providing a user's webhook and all their forms to a GET request
     */
    private final class FormsResponse {
        /**
         * Webhook URL for Advertiser to put in Google Ads.
         */
        private String webhookUrl;

        /**
         * List of the User's forms
         */
        private List<Form> forms;

        /**
         * Constructor for response to send back to user containing the webhook
         * @param webhookUrl url to receive lead data from Google Ads
         * @param forms      list of all the user's forms
         */
        FormsResponse(String webhookUrl, List<Form> forms) {
            this.webhookUrl = webhookUrl;
            this.forms = new ArrayList<>(forms);
        }

        public String toJson(){
            Gson gson = new GsonBuilder().disableHtmlEscaping().create();
            return gson.toJson(this);
        }
    }
}
