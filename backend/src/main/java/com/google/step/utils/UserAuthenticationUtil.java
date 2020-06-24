package com.google.step.utils;

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
    
    public void authenticate(HttpServletResponse response) throws IOException {
        if(!userService.isUserLoggedIn()) {
            response.sendRedirect(REDIRECT_URL);
        }
    }

    public User getCurrentUser() {
        return userService.getCurrentUser();
    }
}
