package handlers;

import dataAccess.AuthDAO;
import dataAccess.GameDAO;
import dataAccess.UserDAO;
import spark.Request;
import spark.Response;

import java.util.Collection;

public abstract class Handler {
    public Object handleRequest(Request req, Response res) {
        return null;
    }  //take in HTTP stuff, return a json

    public abstract Object handleRequest(Request req, Response res, UserDAO users, AuthDAO auth);
}
