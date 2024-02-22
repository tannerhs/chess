package service;

import dataAccess.AuthDAO;
import dataAccess.GameDAO;
import dataAccess.MemoryAuthDAO;
import dataAccess.UserDAO;
import model.UserData;
import org.eclipse.jetty.util.log.Log;
import requests.LoginRequest;
import responses.LoginResponse;
import spark.Request;
import spark.Response;

import java.util.Collection;

public class LoginService {  //FIXME
    LoginRequest request;
    LoginResponse response;

    public LoginResponse LoginService(LoginRequest request, UserDAO users , GameDAO games, AuthDAO auth) {
        this.request=request;
        UserData user = users.getUser(request.username());
        if (user==null) {
            LoginResponse response = new LoginResponse(new MemoryAuthDAO()); //fields all null
        }

//        AuthDAO addAuth =createAuth(request.username());
//        auth.add(addAuth.getAuth(),auth);

        LoginResponse response = new LoginResponse(auth);
        return response;
    }
    UserDAO getUser(String username) {  //if it returns null, create a user since it doesn't exist yet
        return null;
    }

    UserDAO createUser(String username, String password) {
        //
    }

    AuthDAO createAuth(String username) {
        //calls method in ---
        return null;
    }


}
