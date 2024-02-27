package service;

import com.google.gson.Gson;
import dataAccess.AuthDAO;
import dataAccess.BadRequestException;
import dataAccess.GameDAO;
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

    public String listGames() throws BadRequestException, AuthenticationException {
        int gameID=-1;
        if(auth==null) {
            throw new BadRequestException("{\"message\": \"Error: bad request\"}");
        }
        else if(auth.getAuth(authToken)==null || auth.getAuthIndex(authToken)==-1) {  //authentication exists but not valid
            throw new AuthenticationException("{\"message\": \"Error: unauthorized\"}");
        }
        else {
            Gson serializer = new Gson();
//            String response = "{";
//            for(int i=0; i<games.size();i++) {
//                response+=serializer.toJson(games.getGameByIndex(i),GameData.class);
//                if(i!=games.size()-1) {
//                    response+=", ";
//                }
//            }
//            response+="}";

            if(games==null) {
                return"";
            }
            else if(games.size()==0) {
                return "";
                //return "{\"games\" : \"\"}";
            }
//            else if (games.size()==1) {
//                return "{\"games\" : "+serializer.toJson(games.getGameByIndex(0), GameData.class)+ "}";
//            }
            String response = "{\"games\" : "+serializer.toJson(new HashSet(games.listGames()),HashSet.class) + "}";
            return response;
            //return "{}";
        }
    }
}
