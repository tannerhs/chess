package client_responses_http;

import model.AuthData;

public record LoginResponse(AuthData addedAuth, int statusCode, String errorMessage) {  //make authData type?
}
