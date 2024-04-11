package service;

import dataAccess.*;
import model.AuthData;
import model.UserData;
import org.eclipse.jetty.util.log.Log;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
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
        this.password= request.password();
        this.users = request.users();
        this.auth= request.auth();
    }

    //login an existing user
    public LoginResponse login() throws UnauthorizedAccessException, DataAccessException {
        int statusCode=200;
        String errorMessage="lol";
        AuthData responseAuth=null;
        if(users==null) {
            System.out.println("users null");
            throw new UnauthorizedAccessException("{\"message\": \"Error: unauthorized\"}");
        }
        else {
            BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

            String hashedPassword = users.getPassword(username);
            System.out.println("hashedPassword: '"+(hashedPassword==null)+"'");
            System.out.println("user entered password: '"+password+"'");

            if (hashedPassword==null) {
                System.out.println("no password");
                throw new UnauthorizedAccessException("{\"message\": \"Error: unauthorized\"}");
            }
            else if (!encoder.matches(password,hashedPassword)) {
                System.out.println("password: "+ password);
                System.out.println("user.password(): "+hashedPassword);
                System.out.println("password doesn't match");
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
