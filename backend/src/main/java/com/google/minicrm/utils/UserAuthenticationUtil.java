package com.google.minicrm.utils;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.users.User;
import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;
import com.google.minicrm.data.Advertiser;

/**
 * Handles authentication with Google UserService.
 */
public final class UserAuthenticationUtil {

  private static UserService userService = UserServiceFactory.getUserService();

  /**
   * Checks whether the client is logged in with Google or not. If they are logged in with Google
   * but do not exist in our database, this method automatically adds them to the database.
   * @return true if they are logged in, false otherwise.
   */
  public static boolean isAuthenticated() {
    if (!userService.isUserLoggedIn()) {
      return false;
    } else { //user is logged in
      User user = userService.getCurrentUser();
      if (!AdvertiserUtil.advertiserExistsInDatastore(user)) {
        Advertiser newAdvertiser = new Advertiser(user);
        DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
        datastore.put(newAdvertiser.asEntity());
      }
      return true;
    }
  }

  /**
   * @return the current google user as provided by the UserService API
   */
  public static User getCurrentUser() {
    return userService.getCurrentUser();
  }
}
