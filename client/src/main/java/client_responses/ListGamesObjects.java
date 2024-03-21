package client_responses;

import model.GameData;

import java.util.HashSet;
import java.util.List;

public record ListGamesObjects(List<GameData> games) {
}
