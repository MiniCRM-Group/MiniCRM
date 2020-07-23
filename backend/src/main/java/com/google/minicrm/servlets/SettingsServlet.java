package com.google.minicrm.servlets;


import com.google.appengine.api.datastore.*;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.minicrm.data.Advertiser;
import com.google.minicrm.data.Settings;
import com.google.minicrm.interfaces.ClientResponse;
import com.google.minicrm.utils.UserAuthenticationUtil;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/api/settings")
public final class SettingsServlet extends HttpServlet {
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

    private class SettingsResponse implements ClientResponse {
        private Settings settings;

        SettingsResponse(Settings settings) {
            this.settings = settings;
        }

        @Override
        public String toJson() {
            Gson gson = new GsonBuilder().disableHtmlEscaping().create();
            return gson.toJson(this);
        }
    }
}
