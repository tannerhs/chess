package requests;

import dataAccess.AuthDAO;
import dataAccess.GameDAO;
import dataAccess.UserDAO;

public record CreateGameRequest(String authToken, String gameName, AuthDAO auth, GameDAO games) {
}
