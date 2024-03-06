package service;

import dataAccess.*;
import requests.CreateGameRequest;
import responses.CreateGameResponse;

import javax.naming.AuthenticationException;

public class CreateGameService {
    String authToken;
    String gameName;
    AuthDAO auth;
    GameDAO games;
    public CreateGameService(CreateGameRequest request) {
        this.authToken= request.authToken();
        this.gameName= request.gameName();
        this.auth=request.auth();
        this.games=request.games();
    }

    public CreateGameResponse createGame() throws BadRequestException, UnauthorizedAccessException, DataAccessException {
        System.out.println("createGame method in service class reached");
        if(auth==null || gameName.equals("")) {
            throw new BadRequestException("{\"message\": \"Error: bad request\"}");
        }
        else if(auth.getAuth(authToken)==null) {  //authentication exists but not valid
            System.out.println("bad authToken");
            throw new UnauthorizedAccessException("{\"message\": \"Error: unauthorized\"}");
        }
        else {
            CreateGameResponse response =games.createGame(gameName);
            return response;
        }

    }
}
