package service;

import dataAccess.*;
import model.AuthData;
import model.GameData;
import requests.JoinGameRequest;


import javax.naming.AuthenticationException;

public class JoinGameService {
    String playerColor;
    int gameID;
    String authToken;
    UserDAO users;
    AuthDAO auth;
    GameDAO games;

    public JoinGameService(JoinGameRequest request) {
        this.playerColor= request.playerColor();
        this.gameID= request.gameID();
        this.authToken=request.authToken();
        this.users=request.users();
        this.auth= request.auth();
        this.games= request.games();
    }

    public Object joinGame() throws BadRequestException, AuthenticationException, DataAccessException, PlayerFieldTakenException, UnauthorizedAccessException {
        GameData gameToJoin = games.getGameByID(gameID);
        System.out.println("joinGame service reached");

        if(gameToJoin==null || games==null ) {
            throw new BadRequestException("{\"message\": \"Error: bad request\"}");
        }
        else if(auth==null) {  //authentication exists but not valid
            throw new AuthenticationException("{\"message\": \"Error: unauthorized\"}");
        }
        else if(auth.getAuth(authToken)==null) {
            throw new AuthenticationException("{\"message\": \"Error: unauthorized\"}");
        }
        else if(nonstandardColor(playerColor)) {
            throw new PlayerFieldTakenException("{\"message\": \"Error: already taken\" }");
        }
        else if (gameToJoin.whiteUsername()!="" && gameToJoin.whiteUsername()!=null && playerColor=="WHITE") {  //using .equals() throws an error here
            throw new PlayerFieldTakenException("{\"message\": \"Error: already taken\" }");
        }
        else if (gameToJoin.blackUsername()!="" && gameToJoin.blackUsername()!=null && playerColor=="BLACK") {  //ditto
            throw new PlayerFieldTakenException("{\"message\": \"Error: already taken\" }");
        }
        else {
            System.out.println("playerColor");
            AuthData currentAuthData = auth.getAuth(authToken);
            String username = currentAuthData.username();
            games.joinGame(gameID,username,playerColor);
            System.out.println("m games.size(): "+games.size());

            return "";
        }

    }

    public boolean nonstandardColor(String playerColor) {
        if(playerColor==null) {
            return false;
        }
        else if(playerColor.equals("WHITE") || playerColor.equals("BLACK") || playerColor.equals("")) {
            return false;  //no color is fine
        }
        return true;
    }
}
