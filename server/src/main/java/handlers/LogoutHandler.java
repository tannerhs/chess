package handlers;

import com.google.gson.Gson;
import dataAccess.AuthDAO;
import dataAccess.DataAccessException;
import dataAccess.UnauthorizedAccessException;
import dataAccess.UserDAO;
import requests.LogoutRequest;
import responses.LogoutResponse;
import service.LogoutService;
import spark.Request;
import spark.Response;

public class LogoutHandler extends Handler {

//    @Override  //keeps compiler happy, not used, FIXME
//    public Object handleRequest(Request req, Response res, UserDAO users, AuthDAO auth) {
//        return null;
//    }

    @Override
    public Object handleRequest(Request req, Response res, AuthDAO auth) {
        //Gson serializer = new Gson();
        String authToken = req.headers().toString();
        LogoutRequest logoutRequest = new LogoutRequest(authToken, auth);
        String message="";
        int statusCode=200;
        try {
            LogoutService logoutService = new LogoutService(logoutRequest);
            //Gson deserializer = new Gson();
            logoutService.logout();
            //statusCode = logoutResponse.statusCode();
            //message = logoutResponse.errorMessage();
        }
        catch(UnauthorizedAccessException e) {
            message=e.getMessage();
            res.status(401);
        }
        catch (Exception e) {  //catch DataAccessExceptions etc.
            //throw DataAccessException
            message=e.getMessage();
            res.status(500);
        }

        res.body(message);
        return message;
    }
}
