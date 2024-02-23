package dataAccess;

import model.AuthData;
import model.GameData;
import model.UserData;

public interface GameDAO {

    public void clear();
    //public void clearGame(int gameID);
    public int createGame(GameData gameData);
    public GameData getGame(int gameID);
    public void updateGame(GameData gameData);
    public GameData[] listGames();

    GameData get(int i);

    int size();
}
