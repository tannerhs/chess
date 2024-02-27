package handlers;

import com.google.gson.Gson;
import dataAccess.AuthDAO;
import dataAccess.GameDAO;
import requests.ListGamesRequest;
import responses.ListGamesResponse;
import service.ListGamesService;
import spark.Request;
import spark.Response;

public class ListGamesHandler {
    public Object handleRequest(Request req, Response res, AuthDAO auth, GameDAO games) {
        String authToken = req.headers("Authorization");
        String message;
        ListGamesRequest request = new ListGamesRequest(authToken,auth,games);
        try{
            ListGamesService listGamesService = new ListGamesService(request);
            message=listGamesService.listGames();
            //Gson deserializer = new Gson();
            //message="{"
            //for()
            //message=deserializer.toJson(response, GameD.class);
            res.body(message);
            return message;
        }
//        catch (BadRequestException e) {
//            message =e.getMessage();
//            res.status(400);
//            return message;
//        }
//        catch (AuthenticationException e) {
//            message=e.getMessage();
//            res.status(401);
//            return message;
//        }
        catch (Exception e) {
            return e.getMessage();
        }
    }
}
