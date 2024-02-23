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
        //add user to database
        UserData addedUser = new UserData(username, password,email);
        users.addUser(addedUser);
        //create and add new auth token
        AuthData addedAuth = auth.createAuth(username);
        //create response and return
        RegisterResponse response = new RegisterResponse(addedAuth);
        return response;
    }
}
