package client_responses;

import model.AuthData;

public record RegisterResponse(AuthData addedAuth, int statusCode, String errorMessage) {  //errorMessage is null for successful requests
}
