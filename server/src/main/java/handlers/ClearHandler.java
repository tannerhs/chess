package handlers;

import dataAccess.AuthDAO;
import dataAccess.GameDAO;
import dataAccess.UserDAO;
import requests.ClearAppRequest;
import service.ClearAppService;
import spark.Request;
import spark.Response;

import java.util.Collection;


public class ClearHandler {
    static final int FAILURE = 500;

    public Object handleRequest(Request req, Response res, UserDAO users, GameDAO games, AuthDAO auth) {
        //call ServerClass
        System.out.println("ClearHandler handleRequest()");
        ClearAppRequest clearRequest = new ClearAppRequest(users,games,auth);
        ClearAppService clearAppService = new ClearAppService(clearRequest);
        res.status(FAILURE);
        res.type("application/json");
        res.body("{}");
        return "{}";
    }


}
