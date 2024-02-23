package requests;

import dataAccess.AuthDAO;
import dataAccess.UserDAO;
import model.UserData;

public record RegisterRequest(UserDAO users, AuthDAO auth, UserData addUser) {
}
