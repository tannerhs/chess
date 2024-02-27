package service;

import com.google.gson.Gson;
import dataAccess.AuthDAO;
import dataAccess.BadRequestException;
import dataAccess.GameDAO;
import dataAccess.UnauthorizedAccessException;
import model.GameData;
import requests.ListGamesRequest;
import responses.CreateGameResponse;
import responses.JoinGameResponse;
import responses.ListGamesResponse;

import javax.naming.AuthenticationException;
import java.util.HashSet;
import java.util.List;


public class ListGamesService {
    String authToken;
    AuthDAO auth;
    GameDAO games;

    public ListGamesService(ListGamesRequest request) {
        this.authToken= request.authToken();
        this.auth= request.auth();
        this.games= request.games();
    }

    public String listGames() throws BadRequestException, AuthenticationException, UnauthorizedAccessException {
        int gameID=-1;
        if(auth==null) {
            throw new BadRequestException("{\"message\": \"Error: bad request\"}");
        }
        else if(auth.getAuth(authToken)==null) {  //authentication exists but not valid
            System.out.println("bad authentication");
            throw new UnauthorizedAccessException("{\"message\": \"Error: unauthorized\"}");
        }
        else if(auth.getAuthIndex(authToken)==-1) {  //authentication exists but not valid
            System.out.println("bad authentication2");
            throw new AuthenticationException("{\"message\": \"Error: unauthorized\"}");
        }
        else {
            System.out.println("else reached");
            Gson serializer = new Gson();

            if(games==null) {
                System.out.println("games null");
                return"";
            }
            else if(games.size()==0) {
                System.out.println("games size 0");
                String response = "{\"games\" : "+serializer.toJson(new HashSet(games.listGames()),HashSet.class) + "}";
                return response;
                //return "";
                //return "{\"games\" : \"\"}";
            }
//            else if (games.size()==1) {
//                return "{\"games\" : "+serializer.toJson(games.getGameByIndex(0), GameData.class)+ "}";
//            }
            System.out.println("auth.getAuthIndex(authToken): "+auth.getAuthIndex(authToken));

            String response = "{\"games\" : "+serializer.toJson(new HashSet(games.listGames()),HashSet.class) + "}";
            return response;
            //return "{}";
        }
    }
}
