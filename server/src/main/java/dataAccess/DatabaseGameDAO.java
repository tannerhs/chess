package dataAccess;

import chess.ChessGame;
import com.google.gson.Gson;
import model.GameData;
import responses.CreateGameResponse;

import java.sql.Blob;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

public class DatabaseGameDAO implements GameDAO {
    static int gameIDCounter=0;
    @Override
    public void clear() throws DataAccessException {
        try (Connection conn = CustomDatabaseManager.getConnection()) {
            PreparedStatement preparedStatement = conn.prepareStatement("TRUNCATE TABLE users");
            preparedStatement.executeUpdate();
        }
        catch (SQLException e) {
            throw new DataAccessException(e.getMessage());
        }
    }

    @Override
    public CreateGameResponse createGame(String gameName) throws BadRequestException, DataAccessException {
        System.out.println("reached createGame");
        if(gameName==null) {
            throw new BadRequestException("{ \"message\": \"Error: bad request\" }");
        }
        gameIDCounter+=1;
        try(Connection conn = DatabaseManager.getConnection()) {
            try(PreparedStatement createGameStatement = conn.prepareStatement("INSERT INTO games (gameID,gameName, game) VALUES (?,?,?)")) {
                createGameStatement.setInt(1,gameIDCounter);
                createGameStatement.setString(2,gameName);
                // Serialize and store the friend JSON.
                var json = new Gson().toJson(new ChessGame());
                Blob blob = conn.createBlob();  //make blob to store game in
                blob.setBytes(1, json.getBytes());
                createGameStatement.setBlob(3, blob);
                createGameStatement.executeUpdate();
            }
            return new CreateGameResponse(gameIDCounter);

        }
        catch(SQLException e) {
            System.out.println(e.getMessage());
        }
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

    @Override
    public void configureDatabae() throws DataAccessException {
        try(Connection conn = DatabaseManager.getConnection()){
            String createGamesTable = """
                    CREATE TABLE IF NOT EXISTS games(
                    gameID INT NOT NULL,
                    whiteUsername MEDIUMTEXT,
                    blackUsername MEDIUMTEXT,
                    gameName MEDIUMTEXT NOT NULL,
                    game MEDIUMBLOB NOT NULL,
                    PRIMARY KEY (gameID)
                    )""";
            try (PreparedStatement createGamesTableStatement = conn.prepareStatement(createGamesTable)) {
                createGamesTableStatement.executeUpdate();
            }

        }
        catch (SQLException e){
            System.out.println(e.getMessage());
        }

    }
}
