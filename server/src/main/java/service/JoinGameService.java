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

    public JoinGameResponse joinGame() throws BadRequestException, AuthenticationException, PlayerFieldTakenException {
        GameData gameToJoin = games.getGameByID(gameID);
        AuthData currentAuthData = auth.getAuth(authToken);

        //|| users.getUser(currentAuthData.username())==null

        if(gameToJoin==null || games==null ) {  //FIXME non-white/black/nonspecified color
            throw new BadRequestException("{\"message\": \"Error: bad request\"}");
        }
        else if(auth==null ||currentAuthData==null) {  //authentication exists but not valid
            throw new AuthenticationException("{\"message\": \"Error: unauthorized\"}");
        }
        else if (nonstandardColor(playerColor) ||(gameToJoin.whiteUsername()!="empty" && playerColor.equals("WHITE")) ||
                (gameToJoin.blackUsername()!="empty" && playerColor.equals("BLACK"))) {  //already taken
            throw new PlayerFieldTakenException("{\"message\": \"Error: already taken\" }");
        }
        else {
            String username = currentAuthData.username();
            games.joinGame(gameID,username,playerColor);
            JoinGameResponse response = new JoinGameResponse();  //does nothing
            return response;
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
