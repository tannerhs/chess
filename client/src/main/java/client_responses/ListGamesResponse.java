package client_responses;


import bodyResponses.ListGamesBodyResponse;

public record ListGamesResponse(String response, int statusCode, String statusMessage) {
}