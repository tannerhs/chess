package handlers;

import com.google.gson.Gson;
import dataAccess.*;
import requests.CreateGameRequest;
import responses.CreateGameResponse;
import service.CreateGameService;
import spark.Request;
import spark.Response;

import javax.naming.AuthenticationException;

public class CreateGameHandler {
    Request req;
    Response res;
    GameDAO games;

    public Object handleRequest(Request req, Response res, AuthDAO auth, GameDAO games) {
        System.out.println("create game");
        String authToken = req.headers("Authorization");
        String gameName = req.body();
        String message;
        CreateGameRequest request = new CreateGameRequest(authToken,gameName,auth, games);
        try{
            CreateGameService creatGameService = new CreateGameService(request);
            CreateGameResponse response=creatGameService.createGame();
            Gson deserializer = new Gson();
            message=deserializer.toJson(response, CreateGameResponse.class);
            return message;
        }
        catch (BadRequestException e) {
            message =e.getMessage();
            res.status(400);
            return message;
        }
        catch (UnauthorizedAccessException |DataAccessException e) {
            message=e.getMessage();
            res.status(401);
            return message;
        }
        catch (Exception e) {
            System.out.println("rando exception");
            res.status(500);
            return e.getMessage();
        }

    }

}
