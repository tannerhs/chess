package service;

import dataAccess.AuthDAO;
import dataAccess.GameDAO;
import dataAccess.MemoryAuthDAO;
import dataAccess.UserDAO;
import model.AuthData;
import model.UserData;
import org.eclipse.jetty.util.log.Log;
import requests.LoginRequest;
import responses.LoginResponse;
import spark.Request;
import spark.Response;

import java.util.Collection;
import java.util.List;

public class LoginService {  //FIXME
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
    public LoginResponse login() {
        int statusCode=200;
        String errorMessage="lol";
        AuthData responseAuth=null;
        UserData user = users.getUser(username);
        if (user==null || password==null) {
            responseAuth = new AuthData(null,null); //fields all null
            statusCode=500;
            errorMessage="{\"message\": \"Error: null user\"}";
        }
        else if(password.equals(user.password())){
            responseAuth = auth.createAuth(username);
        }
        else {
            //
        }

        LoginResponse response = new LoginResponse(responseAuth,statusCode, errorMessage);
        return response;
    }
    UserData getUser(String username, UserDAO users) {  //if it returns null, can't log in, return error

        return null;  //FIXME
    }


    AuthData createAuth(String username,AuthDAO auth) {
        //calls method in AuthDAO
        AuthData addedAuth = auth.createAuth(username);
        return addedAuth;
    }

    AuthData getAuth(String username, AuthDAO auth) {
        List<AuthData> list = auth.getAuthDataList();
        for(int i=0; i<list.size(); i++) {
            if(list.get(i).username()==username) {
                return list.get(i);
            }
        }
        return null;
    }


}
