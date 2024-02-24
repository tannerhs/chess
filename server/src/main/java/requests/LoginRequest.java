package requests;

import dataAccess.AuthDAO;
import dataAccess.UserDAO;

public record LoginRequest(String username, String password, UserDAO users, AuthDAO auth) {

}
