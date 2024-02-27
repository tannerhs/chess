package handlers;

import com.google.gson.Gson;
import dataAccess.AuthDAO;
import dataAccess.UnauthorizedAccessException;
import dataAccess.UserDAO;
import model.AuthData;
import model.UserData;
import org.eclipse.jetty.util.log.Log;
import requests.LoginInfo;
import requests.LoginRequest;
import requests.RegisterRequest;
import responses.LoginResponse;
import responses.RegisterResponse;
import service.LoginService;
import service.RegisterService;
import spark.Request;
import spark.Response;

public class LoginHandler extends Handler {
    @Override
    public Object handleRequest(Request req, Response res, UserDAO users, AuthDAO auth) {

        try{
            System.out.println("Login");
            Gson serializer = new Gson();
            LoginInfo loginInfo = serializer.fromJson(req.body(), LoginInfo.class);
            LoginRequest loginRequest = new LoginRequest(loginInfo.username(), loginInfo.password(), users, auth);
            LoginService loginService = new LoginService(loginRequest);
            Gson deserializer = new Gson();
            LoginResponse loginResponse = loginService.login();
            AuthData addedAuth=loginResponse.addedAuth();
            String response =deserializer.toJson(addedAuth, AuthData.class);  //call register service and convert result to json

            res.body(response);
            return response;
        }
        catch(UnauthorizedAccessException e){
            res.status(401);
            return e.getMessage();
        }


    }
}
