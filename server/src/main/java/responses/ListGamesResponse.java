package responses;

import dataAccess.GameDAO;
import model.GameData;

import java.util.List;

public record ListGamesResponse(GameDAO gameList) {
}
