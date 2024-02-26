package dataAccess;

import model.AuthData;
import model.GameData;
import model.UserData;
import responses.createGameResponse;

public interface GameDAO {

    public void clear();
    //public void clearGame(int gameID);
    public createGameResponse createGame(String gameName);

    public GameData getGame(int gameID);
    public void updateGame(GameData gameData);
    public GameData[] listGames();

    GameData get(int i);

    int size();
}
