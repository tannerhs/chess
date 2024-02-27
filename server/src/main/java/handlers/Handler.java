package handlers;

import dataAccess.AuthDAO;
import dataAccess.GameDAO;
import dataAccess.UserDAO;
import spark.Request;
import spark.Response;


public class Handler {

    public Object handleRequest(Request req, Response res, UserDAO users, AuthDAO auth) {
        return null;
    }

    public Object handleRequest(Request req, Response res, AuthDAO auth) {
        return null;
    }
}
