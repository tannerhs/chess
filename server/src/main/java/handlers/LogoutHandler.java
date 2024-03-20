package handlers;

import dataAccess.AuthDAO;
import dataAccess.DataAccessException;
import dataAccess.UnauthorizedAccessException;
import requests.LogoutRequest;
import service.LogoutService;
import spark.Request;
import spark.Response;

public class LogoutHandler extends Handler {

    @Override
    public Object handleRequest(Request req, Response res, AuthDAO auth) {

        String message="";
        try {
            System.out.println("Logout");
            if(req.headers()!=null) {
                String authToken = req.headers("Authorization");
                LogoutRequest logoutRequest = new LogoutRequest(authToken, auth);
                LogoutService logoutService = new LogoutService(logoutRequest);
                logoutService.logout();
            }
            else {
                //throw invalid input
            }

            //no need for LogoutResponse class, blank when successful and exception handles message otherwise
        }
        catch(UnauthorizedAccessException | DataAccessException e) {
            message=e.getMessage();
            res.body(e.getMessage());
            res.status(401);
        }
        catch (Exception e) {  //catch DataAccessExceptions etc.
            message=e.getMessage();
            res.body(e.getMessage());
            res.status(500);
        }

        res.body(message);
        return message;
    }
}
