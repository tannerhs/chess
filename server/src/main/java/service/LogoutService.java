package service;

import dataAccess.AuthDAO;

import dataAccess.BadRequestException;
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
        if(auth==null || authToken==null) {
            throw new UnauthorizedAccessException("{ \"message\": \"Error: unauthorized\" }");
        }
        auth.deleteAuth(authToken);
    }

}
