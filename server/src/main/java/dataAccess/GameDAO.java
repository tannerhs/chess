package dataAccess;

import model.GameData;
import responses.CreateGameResponse;

import java.util.List;

public interface GameDAO {

    public void clear();
    //public void clearGame(int gameID);
    public CreateGameResponse createGame(String gameName);

    //public GameData getGame(int gameID);

    GameData getGameByIndex(int index);

    public void updateGame(GameData gameData);
    public List<GameData> listGames();

    GameData getGameByID(int i);


    int getGameIndex(int gameID);

    void joinGame(int GameID, String username, String playerColor);

    int size();
}
