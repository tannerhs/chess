package dataAccess;

import chess.ChessGame;
import com.google.gson.Gson;
import com.mysql.cj.x.protobuf.MysqlxPrepare;
import model.GameData;
import responses.CreateGameResponse;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import static java.sql.Statement.RETURN_GENERATED_KEYS;

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
        int ID = -1;
        if(gameName==null) {
            throw new BadRequestException("{ \"message\": \"Error: bad request\" }");
        }
        //gameIDCounter+=1;
        //System.out.println("gameIDCounter: "+gameIDCounter);
        System.out.println("gameName: "+gameName);
        try(Connection conn = DatabaseManager.getConnection()) {
            try(PreparedStatement createGameStatement = conn.prepareStatement("INSERT INTO games (gameName, game) VALUES (?,?)", Statement.RETURN_GENERATED_KEYS)) {

                //createGameStatement.setInt(1,gameIDCounter);
                createGameStatement.setString(1,gameName);
                // Serialize and store the friend JSON.
                var json = new Gson().toJson(new ChessGame());
                Blob blob = conn.createBlob();  //make blob to store game in
                blob.setBytes(1, json.getBytes());
                createGameStatement.setBlob(2, blob);
                createGameStatement.executeUpdate();

                ResultSet rs = createGameStatement.getGeneratedKeys();
                if(rs.next()) {
                    ID = rs.getInt(1);
                    System.out.println("generatedID: "+ID);
                }
                if(ID==-1) {
                    System.out.println("ID is -1, game not created");
                    return null;
                }
            }

            return new CreateGameResponse(ID);

        }
        catch(SQLException e) {
            System.out.println(e.getMessage());
        }
        return null;
    }

    @Override
    public List<GameData> listGames() throws DataAccessException {
        List<GameData> listedGames= new ArrayList<>();
        try(Connection conn = DatabaseManager.getConnection()) {
            PreparedStatement listGamesStatement= conn.prepareStatement("SELECT gameID, whiteUsername, blackUsername, gameName, game FROM games");
            ResultSet rs =listGamesStatement.executeQuery();
            while(rs.next() ){
                int gameID = rs.getInt("gameID");
                String whiteUsername = rs.getString("whiteUsername");
                String blackUsername = rs.getString("blackUsername");
                Blob blob = rs.getBlob("gameName");
                byte[] bdata = blob.getBytes(1,(int) blob.length());
                String gameName = new String(bdata);
                // Deserialize from json and store the game.
                ChessGame game = new Gson().fromJson(rs.getString("game"), ChessGame.class) ;
                GameData addGameData = new GameData(gameID,whiteUsername,blackUsername,gameName,game);
                listedGames.add(addGameData);
                System.out.println("listOGames: "+listedGames.toString());
            }
        }
        catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return listedGames;
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
                    gameID INT NOT NULL AUTO_INCREMENT,
                    whiteUsername MEDIUMTEXT,
                    blackUsername MEDIUMTEXT,
                    gameName MEDIUMTEXT NOT NULL,
                    game MEDIUMBLOB NOT NULL,
                    PRIMARY KEY (gameID)
                    )""";
            try (PreparedStatement createGamesTableStatement = conn.prepareStatement(createGamesTable)) {
                createGamesTableStatement.executeUpdate();
            }
//            try(PreparedStatement setDefaultGameID = conn.prepareStatement("ALTER TABLE games CHANGE gameID gameID INT(10)AUTO_INCREMENT PRIMARY KEY")) {
//                setDefaultGameID.executeUpdate();
//            }
//            try(PreparedStatement setDefaultGameID = conn.prepareStatement("ALTER TABLE games ALTER gameID SET DEFAULT 1")) {
//                //setDefaultGameID.executeUpdate();
//            }

        }
        catch (SQLException e){
            System.out.println(e.getMessage());
        }

    }
}
