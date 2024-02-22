package handlers;

import com.google.gson.Gson;
import dataAccess.AuthDAO;
import dataAccess.GameDAO;
import dataAccess.UserDAO;
import requests.LoginRequest;
import service.LoginService;
import spark.Request;
import spark.Response;

import java.util.Collection;

public class RegisterHandler {
    public Object handleRequest(Request req, Response res,UserDAO users, AuthDAO auth) {
        Gson serializer = new Gson();
        LoginRequest loginRequest = serializer.fromJson(req.body(), LoginRequest.class);
        LoginService loginService = new LoginService();
        return null;
    }

}
