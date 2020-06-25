package com.google.step.utils;

import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;
import com.google.appengine.api.users.User;

public final class UserAuthenticationUtil {
    private static final String REDIRECT_URL = "/";
    private UserService userService;

    public UserAuthenticationUtil() {
        userService = UserServiceFactory.getUserService();
    }

    public boolean isAuthenticated() {
        if (!userService.isUserLoggedIn()) {
            return false;
        } else { //user is logged in
            User user = userService.getCurrentUser();
            if (!AdvertiserUtil.advertiserExistsInDatastore(user)) {
                AdvertiserUtil.putAdvertiserInDatastore(user);
            }
            return true;
        }

    }

    public User getCurrentUser() {
        return userService.getCurrentUser();
    }
}
