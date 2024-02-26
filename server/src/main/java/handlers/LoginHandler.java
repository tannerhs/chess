package handlers;

import com.google.gson.Gson;
import dataAccess.AuthDAO;
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
        Gson serializer = new Gson();
        LoginInfo loginInfo = serializer.fromJson(req.body(), LoginInfo.class);
        LoginRequest loginRequest = new LoginRequest(loginInfo.username(), loginInfo.password(), users, auth);
        LoginService loginService = new LoginService(loginRequest);
        Gson deserializer = new Gson();
        LoginResponse loginResponse = loginService.login();
        AuthData addedAuth=loginResponse.addedAuth();
        int statusCode = loginResponse.statusCode();
        String message = loginResponse.errorMessage();
        String response = "lol";
        if(statusCode==200) {
            response =deserializer.toJson(addedAuth, AuthData.class);  //call register service and convert result to json

        }
        else {
            //response ="{message: "+deserializer.toJson(message, String.class)+"}";
            response = message;
        }
        res.status(statusCode);
        res.body(response);
        return response;
    }
}