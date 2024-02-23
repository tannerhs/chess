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
    LoginRequest request;
    LoginResponse response;

    //login an existing user
    public LoginResponse LoginService(LoginRequest request, UserDAO users , GameDAO games, AuthDAO auth) {
        this.request=request;
        UserData user = users.getUser(request.username());
        if (user==null) {
            LoginResponse response = new LoginResponse(new AuthData("","")); //fields all null
        }

        AuthData responseAuth = getAuth(request.username(),auth);
        LoginResponse response = new LoginResponse(responseAuth);
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
