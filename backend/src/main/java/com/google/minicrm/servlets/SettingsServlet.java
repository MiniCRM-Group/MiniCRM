package com.google.minicrm.servlets;


import com.google.appengine.api.datastore.*;
import com.google.appengine.repackaged.org.apache.commons.codec.language.bm.Lang;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.minicrm.data.*;
import com.google.minicrm.interfaces.ClientResponse;
import com.google.minicrm.utils.UserAuthenticationUtil;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

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
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        if(!UserAuthenticationUtil.isAuthenticated()) {
            resp.sendError(401, "Log in with Google to continue."); //401 Unauthorized
            return;
        }
        Query query = new Query(Settings.KIND_NAME)
                .setAncestor(Advertiser.generateKey(UserAuthenticationUtil.getCurrentUser()));
        DatastoreService datastoreService = DatastoreServiceFactory.getDatastoreService();;
        PreparedQuery preparedQuery = datastoreService.prepare(query);
        Settings settings = new Settings(preparedQuery.asSingleEntity());
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
        private List<Map<String, String>> supportedLanguages;
        private List<Map<String, String>> supportedNotifsFreqs;

        SettingsResponse(Settings settings) {
            this.settings = settings;
            this.supportedCurrencies = Currency.supportedCurrencies;
            this.supportedLanguages = Language.supportedLanguages;
            this.supportedNotifsFreqs = NotificationsFrequency.supportedNotificationsFrequencies;
        }

        @Override
        public String toJson() {
            Gson gson = new GsonBuilder().disableHtmlEscaping().create();
            return gson.toJson(this);
        }
    }
}
