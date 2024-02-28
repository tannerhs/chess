package dataAccess;

import model.GameData;
import responses.CreateGameResponse;

import java.util.List;

public interface GameDAO {

    public void clear();
    public CreateGameResponse createGame(String gameName) throws BadRequestException;

    public List<GameData> listGames();

    GameData getGameByID(int i);


    int getGameIndex(int gameID);

    void joinGame(int gameID, String username, String playerColor) throws Exception;

    int size();
}
