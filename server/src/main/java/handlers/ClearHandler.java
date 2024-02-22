package handlers;

import dataAccess.AuthDAO;
import dataAccess.GameDAO;
import dataAccess.UserDAO;
import service.ClearAppService;
import spark.Request;
import spark.Response;

import java.util.Collection;


public class ClearHandler {
    static final int FAILURE = 500;

    public Object handleRequest(Request req, Response res, UserDAO users, GameDAO games, AuthDAO auth) {
        //call ServerClass
        System.out.println("ClearHandler handleRequest()");
        ClearAppService clearAppService = new ClearAppService();
        res.status(FAILURE);
        res.type("application/json");
        res.body("{}");
        return "{}";
    }


}
