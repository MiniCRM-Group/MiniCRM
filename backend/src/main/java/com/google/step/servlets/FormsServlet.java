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

import com.google.appengine.api.datastore.KeyFactory;
import com.google.appengine.api.users.User;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.step.utils.AdvertiserUtil;
import com.google.step.utils.UserAuthenticationUtil;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.security.SecureRandom;
import java.util.Random;

@WebServlet("/api/forms")
public class FormsServlet extends HttpServlet {
    private static final String ID_URL_PARAM = "id";
    private char[] alphanumerics = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789".toCharArray();

    /**
     * Returns JSON representing the advertiser's webhook and google key
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
        User user = UserAuthenticationUtil.getCurrentUser();
        //generate URL-Safe Key string
        String advertiserKeyString = KeyFactory.keyToString(AdvertiserUtil.createAdvertiserKey(user));
        String webhookUrl = request.getScheme() + "://" +
                request.getServerName() + ":" +
                request.getServerPort() + "/api/webhook?" + ID_URL_PARAM + "=" +
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
