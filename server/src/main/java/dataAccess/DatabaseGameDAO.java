package dataAccess;

import model.GameData;
import responses.CreateGameResponse;

import java.util.List;

public class DatabaseGameDAO implements GameDAO {
    @Override
    public void clear() {
        
    }

    @Override
    public CreateGameResponse createGame(String gameName) throws BadRequestException {
        return null;
    }

    @Override
    public List<GameData> listGames() {
        return null;
    }

    @Override
    public GameData getGameByID(int i) {
        return null;
    }

    @Override
    public int getGameIndex(int gameID) {
        return 0;
    }

    @Override
    public void joinGame(int gameID, String username, String playerColor) throws Exception {

    }

    @Override
    public int size() {
        return 0;
    }
}
