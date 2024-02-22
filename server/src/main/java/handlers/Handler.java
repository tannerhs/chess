package handlers;

import dataAccess.AuthDAO;
import dataAccess.GameDAO;
import dataAccess.UserDAO;
import spark.Request;
import spark.Response;

import java.util.Collection;

public class Handler {
    public Object handleRequest(Request req, Response res) {
        return null;
    }  //take in HTTP stuff, return a json
    public Object handleRequest(Request reg, Response res, Collection<UserDAO> users, Collection<GameDAO> games, Collection<AuthDAO> auth) {
        return null;
    }
}
