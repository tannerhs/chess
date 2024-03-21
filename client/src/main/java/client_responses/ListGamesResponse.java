package client_responses;


import bodyResponses.ListGamesBodyResponse;
import model.GameData;

import java.util.List;

public record ListGamesResponse(List<GameData> games, int statusCode, String statusMessage) {
}
