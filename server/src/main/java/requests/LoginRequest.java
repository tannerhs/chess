package requests;

import dataAccess.AuthDAO;
import dataAccess.UserDAO;

public record LoginRequest(String username, String password, String email, UserDAO users, AuthDAO auth) {

}
