package handlers;

import com.google.gson.Gson;
import dataAccess.AuthDAO;
import dataAccess.GameDAO;
import dataAccess.UserDAO;
import model.AuthData;
import model.UserData;
import requests.LoginRequest;
import requests.RegisterRequest;
import responses.RegisterResponse;
import service.LoginService;
import service.RegisterService;
import spark.Request;
import spark.Response;

import java.util.Collection;

public class RegisterHandler {
    public Object handleRequest(Request req, Response res,UserDAO users, AuthDAO auth) {
        //System.out.println("RegisterHandler.handleRequest()");
        Gson serializer = new Gson();
        LoginRequest loginRequest = serializer.fromJson(req.body(), LoginRequest.class);
        UserData addUser = serializer.fromJson(req.body(), UserData.class);
        RegisterRequest regRequest = new RegisterRequest(users,auth,addUser);
        RegisterService regService = new RegisterService(regRequest);
        Gson deserializer = new Gson();
        RegisterResponse regResponse = regService.register();
        AuthData addedAuth=regResponse.addedAuth();
        int statusCode = regResponse.statusCode();
        String message = regResponse.errorMessage();
        String response = "lol";
        if(statusCode==200) {
            response =deserializer.toJson(addedAuth, AuthData.class);  //call register service and convert result to json

        }
        else {
            response ="{message: "+deserializer.toJson(message, String.class)+"}";
        }
        res.status(statusCode);
        res.body(response);
        //System.out.println(res);
        return response;
    }

}
