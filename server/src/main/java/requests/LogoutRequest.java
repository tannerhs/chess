package requests;

import dataAccess.AuthDAO;
import dataAccess.UserDAO;
import model.AuthData;

public record LogoutRequest(String authToken, AuthDAO auth) {

}
