package handlers;

import com.google.gson.Gson;
import dataAccess.AuthDAO;
import dataAccess.BadRequestException;
import dataAccess.GameDAO;
import dataAccess.UnauthorizedAccessException;
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
            res.body(message);
            return message;
        }
        catch (BadRequestException e) {
            message =e.getMessage();
            res.status(400);
            return message;
        }
        catch (UnauthorizedAccessException e) {
            message=e.getMessage();
            res.status(401);
            return message;
        }
        catch (Exception e) {
            return e.getMessage();
        }
    }
}
