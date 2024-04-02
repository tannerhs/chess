package client_responses_http;

import model.AuthData;

public record RegisterResponse(AuthData addedAuth, int statusCode, String errorMessage) {  //errorMessage is null for successful requests
}
