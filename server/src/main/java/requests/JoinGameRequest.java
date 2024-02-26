package requests;

import dataAccess.AuthDAO;
import dataAccess.GameDAO;
import dataAccess.UserDAO;

public record JoinGameRequest(String playerColor, int gameID, String authToken, UserDAO users, AuthDAO auth, GameDAO games) {
}
