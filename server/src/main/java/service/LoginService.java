package service;

import dataAccess.*;
import model.AuthData;
import model.UserData;
import org.eclipse.jetty.util.log.Log;
import requests.LoginRequest;
import responses.LoginResponse;
import spark.Request;
import spark.Response;

import java.util.Collection;
import java.util.List;

public class LoginService {
    String username;
    String password;
    UserDAO users;
    AuthDAO auth;

    public LoginService(LoginRequest request) {
        this.username=request.username();
        this.password=request.password();
        this.users = request.users();
        this.auth= request.auth();
    }

    //login an existing user
    public LoginResponse login() throws UnauthorizedAccessException {
        int statusCode=200;
        String errorMessage="lol";
        AuthData responseAuth=null;
        if(users==null) {
            throw new UnauthorizedAccessException("{\"message\": \"Error: unauthorized\"}");
        }
        else {
            UserData user = users.getUser(username);
            if ( user==null || password==null) {
                throw new UnauthorizedAccessException("{\"message\": \"Error: unauthorized\"}");
            }
            else if (!user.password().equals(password)) {
                System.out.println("password: "+ password);
                System.out.println("user.password(): "+user.password());
                throw new UnauthorizedAccessException("{\"message\": \"Error: unauthorized\"}");
            }
            else {
                responseAuth = auth.createAuth(username);
            }
        }


        LoginResponse response = new LoginResponse(responseAuth,statusCode, errorMessage);
        return response;
    }




}
