package handlers;

import dataAccess.AuthDAO;
import dataAccess.GameDAO;
import dataAccess.UserDAO;
import requests.ClearAppRequest;
import responses.ClearAppResponse;
import service.ClearAppService;
import spark.Request;
import spark.Response;

import java.util.Collection;


public class ClearHandler {
    static final int FAILURE = 500;

    public Object handleRequest(Request req, Response res, UserDAO users, GameDAO games, AuthDAO auth) {
        try {
            System.out.println("clear");
            ClearAppRequest clearRequest = new ClearAppRequest(users,games,auth);
            ClearAppService clearAppService = new ClearAppService(clearRequest);  //init service object
            clearAppService.clearApp();  //now clear
            res.body("{}");
            return "{}";
        }
        catch (Exception e) {
            System.out.println("clear exception should never happen");
            res.status(500);
            return e.getMessage();
        }

    }


}
