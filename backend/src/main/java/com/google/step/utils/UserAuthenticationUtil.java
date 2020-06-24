package com.google.step.utils;

import com.google.step.utils.UserDatastoreUtil;

import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;
import com.google.appengine.api.users.User;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public final class UserAuthenticationUtil {
    private static final String REDIRECT_URL = "/";
    private UserService userService;

    public UserAuthenticationUtil() {
        userService = UserServiceFactory.getUserService();
    }
    
    public boolean isAuthenticated() throws IOException {
        if (!userService.isUserLoggedIn()) {
            return false;
        } else { //user is logged in
            User user = userService.getCurrentUser();
            if (!UserDatastoreUtil.userExistsInDatastore(user)) {
                UserDatastoreUtil.putUserInDatastore(user);
            }
            return true;
        }

    }

    public User getCurrentUser() {
        return userService.getCurrentUser();
    }
}
