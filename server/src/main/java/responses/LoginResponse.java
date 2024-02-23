package responses;

import dataAccess.AuthDAO;  //FIXME
import model.AuthData;

public record LoginResponse(AuthData authData) {  //make authData type?
}
