package service;

import dataAccess.AuthDAO;
import dataAccess.BadRequestException;
import dataAccess.GameDAO;
import model.GameData;
import requests.ListGamesRequest;
import responses.CreateGameResponse;
import responses.JoinGameResponse;
import responses.ListGamesResponse;

import javax.naming.AuthenticationException;


public class ListGamesService {
    String authToken;
    AuthDAO auth;
    GameDAO games;

    public ListGamesService(ListGamesRequest request) {
        this.authToken= request.authToken();
        this.auth= request.auth();
        this.games= request.games();
    }

    public ListGamesResponse listGames() throws BadRequestException, AuthenticationException {
        int gameID=-1;
        if(auth==null) {
            throw new BadRequestException("{\"message\": \"Error: bad request\"}");
        }
        else if(auth.getAuth(authToken)==null) {  //authentication exists but not valid
            throw new AuthenticationException("{\"message\": \"Error: unauthorized\"}");
        }
        else {
            ListGamesResponse response = new ListGamesResponse(games);
            return response;
        }
    }
}
