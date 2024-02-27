package responses;

import model.AuthData;

public record LoginResponse(AuthData addedAuth, int statusCode, String errorMessage) {  //make authData type?
}
