package handlers;

import spark.Request;
import spark.Response;

public abstract class Handler {
    public abstract Object handleRequest(Request req, Response res);  //take in HTTP stuff, return a json
}
