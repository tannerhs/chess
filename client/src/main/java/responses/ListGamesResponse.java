package responses;


import bodyResponses.ListGamesBodyResponse;

public record ListGamesResponse(ListGamesBodyResponse response, int statusCode, String statusMessage) {
}
