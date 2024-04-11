package dataAccess;

import model.GameData;
import responses.CreateGameResponse;

import javax.xml.crypto.Data;
import java.util.List;

public interface GameDAO {
    public void updateGame(GameData gameData) throws DataAccessException;

    void removeWhiteUsername(Integer gameID) throws DataAccessException;

    void removeBlackUsername(Integer gameID) throws DataAccessException;

    public void clear() throws DataAccessException;
    public CreateGameResponse createGame(String gameName) throws BadRequestException, DataAccessException;

    public List<GameData> listGames() throws DataAccessException;

    GameData getGameByID(int gameID) throws DataAccessException;


    int getGameIndex(int gameID);

    void joinGame(int gameID, String username, String playerColor) throws PlayerFieldTakenException, DataAccessException;

    int size() throws DataAccessException;

    void configureDatabae() throws DataAccessException;
}
