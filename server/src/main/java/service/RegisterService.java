package service;

import dataAccess.AuthDAO;
import dataAccess.UserDAO;
import model.AuthData;
import model.UserData;
import requests.RegisterRequest;
import responses.RegisterResponse;

public class RegisterService {
    UserDAO users;
    AuthDAO auth;
    String username;
    String password;
    String email;
    public RegisterService(RegisterRequest request) {
        this.users = request.users();
        this.auth= request.auth();
        this.username = request.addUser().username();
        this.password = request.addUser().password();
        this.email = request.addUser().email();
    }

    public RegisterResponse register() {
        int statusCode=200;  //success unless..
        AuthData addedAuth=null;
        String errorMessage=null;
        if(username==null || password==null || email==null) {
            statusCode=400;
            errorMessage = "Error: bad request";
        }
        else {
            //add user to database
            UserData addedUser = new UserData(username, password,email);
            users.addUser(addedUser);
            //create and add new auth token
            addedAuth = auth.createAuth(username);
            statusCode=200;  //success
            //create response and return
        }

        RegisterResponse response = new RegisterResponse(addedAuth,statusCode,errorMessage);
        return response;
    }
}
