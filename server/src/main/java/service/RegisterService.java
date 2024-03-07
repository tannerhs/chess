package service;

import dataAccess.*;
import model.AuthData;
import model.UserData;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import requests.RegisterRequest;
import responses.RegisterResponse;

public class RegisterService {
    UserDAO users;
    AuthDAO auth;
    String username;
    String password;
    String email;
    public RegisterService(RegisterRequest request) throws BadRequestException {
        if(request.addUser()==null || request.users()==null || request.auth()==null){
            throw new BadRequestException("");
        }
        this.users = request.users();
        this.auth= request.auth();
        this.username = request.addUser().username();
        this.password = request.addUser().password();
        this.email = request.addUser().email();
    }

    public RegisterResponse register() throws PlayerFieldTakenException, BadRequestException, DataAccessException {
        int statusCode=200;  //success unless..
        AuthData addedAuth=null;
        String errorMessage=null;
        if(username==null || password==null || email==null) {
            throw new BadRequestException("{\"message\": \"Error: bad request\"}");
        }
        else {
            //add user to database
            BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
            UserData addedUser = new UserData(username,encoder.encode(password),email);
            if (users.addUser(addedUser)==true) {
                //create and add new auth token
                addedAuth = auth.createAuth(username);
            }
            else {
                throw new PlayerFieldTakenException("{\"message\": \"Error: already taken\"}");
            }

        }
        //create response and return
        RegisterResponse response = new RegisterResponse(addedAuth,statusCode,errorMessage);
        return response;
    }
}
