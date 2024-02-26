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
        String authToken = req.headers("Authorization").toString();
        System.out.println("authToken?:" + authToken);
        System.out.println("req body: "+req.body());
        LogoutRequest logoutRequest = new LogoutRequest(authToken, auth);
        String message="";
        try {
            LogoutService logoutService = new LogoutService(logoutRequest);
            //Gson deserializer = new Gson();
            logoutService.logout();
            //statusCode = logoutResponse.statusCode();
            //message = logoutResponse.errorMessage();

            //FIXME remove LogoutResponse class, blank when successful and exception handles message otherwise
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
