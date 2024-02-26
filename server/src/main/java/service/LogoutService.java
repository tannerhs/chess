package service;

import dataAccess.AuthDAO;

import dataAccess.UnauthorizedAccessException;
import requests.LogoutRequest;

public class LogoutService {
    String authToken;
    AuthDAO auth;
    public LogoutService(LogoutRequest request) {
        this.authToken = request.authToken();
        this.auth = request.auth();
    }

    public void logout() throws UnauthorizedAccessException {  //user cannot logout another user, ie AuthData can only remove itself
        auth.deleteAuth(authToken);
    }

}
