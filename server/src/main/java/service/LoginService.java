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
    public LoginResponse login() throws UnauthorizedAccessException, DataAccessException {
        int statusCode=200;
        String errorMessage="lol";
        AuthData responseAuth=null;
        if(users==null) {
            throw new UnauthorizedAccessException("{\"message\": \"Error: unauthorized\"}");
        }
        else {
            String hashed_password = users.getPassword(username);
            if (hashed_password==null) {
                throw new UnauthorizedAccessException("{\"message\": \"Error: unauthorized\"}");
            }
            else if (!hashed_password.equals(password)) {
                System.out.println("password: "+ password);
                System.out.println("user.password(): "+hashed_password);
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
