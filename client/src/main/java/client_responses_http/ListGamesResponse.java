package client_responses_http;


import model.GameData;

import java.util.List;

public record ListGamesResponse(List<GameData> games, int statusCode, String statusMessage) {
}
