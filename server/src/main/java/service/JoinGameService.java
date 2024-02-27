package service;

import dataAccess.*;
import model.AuthData;
import model.GameData;
import requests.JoinGameRequest;
import responses.JoinGameResponse;
import responses.ListGamesResponse;

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

    public Object joinGame() throws Exception {
        GameData gameToJoin = games.getGameByID(gameID);

        //|| users.getUser(currentAuthData.username())==null

        if(gameToJoin==null || games==null ) {  //FIXME non-white/black/nonspecified color
            throw new BadRequestException("{\"message\": \"Error: bad request\"}");
        }
        else if(auth==null) {  //authentication exists but not valid
            throw new AuthenticationException("{\"message\": \"Error: unauthorized\"}");
        }
        else if(auth.getAuthIndex(authToken)==-1) {
            throw new AuthenticationException("{\"message\": \"Error: unauthorized\"}");
        }
        else if(nonstandardColor(playerColor)) {
            throw new PlayerFieldTakenException("{\"message\": \"Error: already taken\" }");
        }
        else if (gameToJoin.whiteUsername()!="" && gameToJoin.whiteUsername()!=null && playerColor.equals("WHITE")) {
            throw new PlayerFieldTakenException("{\"message\": \"Error: already taken\" }");
        }
        else if (gameToJoin.blackUsername()!="" && gameToJoin.blackUsername()!=null && playerColor.equals("BLACK")) {
            throw new PlayerFieldTakenException("{\"message\": \"Error: already taken\" }");
        }
//        else if (nonstandardColor(playerColor) ||(gameToJoin.whiteUsername()!="" && playerColor.equals("WHITE")) ||
//                (gameToJoin.blackUsername()!="" && playerColor.equals("BLACK"))) {  //already taken
//            throw new PlayerFieldTakenException("{\"message\": \"Error: already taken\" }");
//        }
        else {
            System.out.println("playerColor");
            AuthData currentAuthData = auth.getAuth(authToken);
            String username = currentAuthData.username();
            games.joinGame(gameID,username,playerColor);
            //JoinGameResponse response = new JoinGameResponse();  //does nothing
            return "";
        }
        //return new JoinGameResponse(games);
        //return null;
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
