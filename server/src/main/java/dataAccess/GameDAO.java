package dataAccess;

import model.GameData;
import responses.CreateGameResponse;

import java.util.List;

public interface GameDAO {

    public void clear();
    public CreateGameResponse createGame(String gameName);

    public List<GameData> listGames();

    GameData getGameByID(int i);


    int getGameIndex(int gameID);

    void joinGame(int GameID, String username, String playerColor) throws Exception;

    int size();
}
