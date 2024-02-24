package responses;

import dataAccess.AuthDAO;  //FIXME
import model.AuthData;

public record LoginResponse(AuthData addedAuth, int statusCode, String errorMessage) {  //make authData type?
}
