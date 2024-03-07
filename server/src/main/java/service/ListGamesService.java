package service;

import com.google.gson.Gson;
import dataAccess.*;
import requests.ListGamesRequest;
import javax.naming.AuthenticationException;
import java.util.HashSet;


public class ListGamesService {
    String authToken;
    AuthDAO auth;
    GameDAO games;

    public ListGamesService(ListGamesRequest request) {
        this.authToken= request.authToken();
        this.auth= request.auth();
        this.games= request.games();
    }

    public String listGames() throws BadRequestException, AuthenticationException, UnauthorizedAccessException, DataAccessException {
        System.out.println("listGames service reached");
        System.out.println("auth: "+auth);

        if(auth==null || authToken==null) {
            throw new BadRequestException("{\"message\": \"Error: bad request\"}");
        }
        else if(auth.getAuth(authToken)==null) {  //authentication exists but not valid
            System.out.println("bad authentication");
            throw new UnauthorizedAccessException("{\"message\": \"Error: unauthorized\"}");
        }
        else if(auth.getAuth(authToken)==null) {  //authentication exists but not valid
            throw new AuthenticationException("{\"message\": \"Error: unauthorized\"}");
        }
        else {
            Gson serializer = new Gson();
            if(games==null) {
                return"";
            }
            else if(games.size()==0) {
                String response = "{\"games\" : "+serializer.toJson(new HashSet(games.listGames()),HashSet.class) + "}";
                return response;
            }


            String response = "{\"games\" : "+serializer.toJson(new HashSet(games.listGames()),HashSet.class) + "}";
            return response;
        }
    }
}
