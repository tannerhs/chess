package requests;

import dataAccess.AuthDAO;
import dataAccess.GameDAO;

public record ListGamesRequest(String authToken, AuthDAO auth, GameDAO games) {

}
