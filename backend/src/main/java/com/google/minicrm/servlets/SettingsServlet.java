package com.google.minicrm.servlets;

import javax.servlet.http.HttpServlet;

import com.google.minicrm.interfaces.ClientResponse;

@WebServlet("/api/settings")
public class SettingsServlet extends HttpServlet {
    
    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
      if (!UserAuthenticationUtil.isAuthenticated()) {
        response.sendError(401, "Log in with Google to continue."); //401 Unauthorized
        return;
      }
      Query query = new Query(Campaign.KIND_NAME)
          .setAncestor(Advertiser.generateKey(UserAuthenticationUtil.getCurrentUser()))
          .addSort("date", Query.SortDirection.DESCENDING);
      DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
      PreparedQuery preparedQuery = datastore.prepare(query);
  
      List<Campaign> campaigns = StreamSupport.stream(preparedQuery.asIterable().spliterator(), false)
          .map(Campaign::new).collect(Collectors.toList());
  
      response.setContentType("application/json;");
      response.getWriter().println(new CampaignsResponse(campaigns).toJson());
    }
}