package client_responses_http;

import model.GameData;

import java.util.List;

public record ListGamesObjects(List<GameData> games) {
}
