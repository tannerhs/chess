package requests;

import dataAccess.AuthDAO;
import dataAccess.GameDAO;
import dataAccess.UserDAO;

public record ClearAppRequest(UserDAO userDAO, GameDAO gameDAO, AuthDAO authDAO) {
}
