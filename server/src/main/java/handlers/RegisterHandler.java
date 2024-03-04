package handlers;

import com.google.gson.Gson;
import dataAccess.*;
import model.AuthData;
import model.UserData;
import requests.RegisterRequest;
import responses.RegisterResponse;
import service.RegisterService;
import spark.Request;
import spark.Response;

public class RegisterHandler extends Handler {
    @Override
    public Object handleRequest(Request req, Response res,UserDAO users, AuthDAO auth) {
        try {
            System.out.println("Register");
            Gson serializer = new Gson();
            UserData addUser = serializer.fromJson(req.body(), UserData.class);
            RegisterRequest regRequest = new RegisterRequest(users,auth,addUser);
            RegisterService regService = new RegisterService(regRequest);
            Gson deserializer = new Gson();
            RegisterResponse regResponse = regService.register();
            AuthData addedAuth=regResponse.addedAuth();

            String response =deserializer.toJson(addedAuth, AuthData.class);  //call register service and convert result to json

            res.body(response);
            return response;
        }
        catch(BadRequestException e) {
            res.status(400);
            return e.getMessage();
        }
        catch(PlayerFieldTakenException e) {
            res.status(403);
            return e.getMessage();
        }
        catch(DataAccessException e) {
            res.status(500);
            return e.getMessage();
        }

    }

}
