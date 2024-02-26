package service;

import dataAccess.AuthDAO;
import dataAccess.BadRequestException;
import dataAccess.GameDAO;
import requests.CreateGameRequest;
import responses.createGameResponse;

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

    public createGameResponse createGame() throws BadRequestException,AuthenticationException {
        int gameID=-1;
        if(auth==null || gameName.equals("")) {
            throw new BadRequestException("{\"message\": \"Error: bad request\"}");
        }
        else if(auth.getAuth(authToken)==null) {  //authentication exists but not valid
            throw new AuthenticationException("{\"message\": \"Error: unauthorized\"}");
        }
        else {
            createGameResponse response =games.createGame(gameName);
            return response;
        }

    }
}
