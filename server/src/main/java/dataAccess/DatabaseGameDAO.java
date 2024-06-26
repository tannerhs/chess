package dataAccess;

import chess.ChessBoard;
import chess.ChessGame;
import com.google.gson.Gson;
import model.GameData;
import responses.CreateGameResponse;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DatabaseGameDAO implements GameDAO {
    static int gameIDCounter=0;

    public void updateGame(GameData gameData) throws DataAccessException {
        Integer index=getGameIndex(gameData.gameID());
        //how to edit something in the database...
//        games.
        try (Connection conn = CustomDatabaseManager.getConnection()) {
            var json = new Gson().toJson(gameData.game());
            Blob blob = conn.createBlob();  //make blob to store game in
            blob.setBytes(1, json.getBytes());

            PreparedStatement preparedStatement = conn.prepareStatement("UPDATE games SET game=? WHERE gameID=?");
            preparedStatement.setBlob(1, blob);
            preparedStatement.setInt(2,gameData.gameID());
            preparedStatement.executeUpdate();
        }
        catch (SQLException | DataAccessException e) {
            System.out.println("updateGame in DatabaseGameDAO failed");
            throw new DataAccessException(e.getMessage());
        }
    }

    @Override
    public void removeWhiteUsername(Integer gameID) throws DataAccessException {
        //how to edit something in the database...
//        games.
        try (Connection conn = CustomDatabaseManager.getConnection()) {
            PreparedStatement preparedStatement = conn.prepareStatement("UPDATE games SET whiteUsername=null WHERE gameID=?");
            preparedStatement.setInt(1,gameID);
            preparedStatement.executeUpdate();
        }
        catch (SQLException | DataAccessException e) {
            System.out.println("removeWhiteUsername in DatabaseGameDAO failed");
            throw new DataAccessException(e.getMessage());
        }
    }

    @Override
    public void removeBlackUsername(Integer gameID) throws DataAccessException {
        //how to edit something in the database...
//        games.
        try (Connection conn = CustomDatabaseManager.getConnection()) {
            PreparedStatement preparedStatement = conn.prepareStatement("UPDATE games SET blackUsername=null WHERE gameID=?");
            preparedStatement.setInt(1,gameID);
            preparedStatement.executeUpdate();
        }
        catch (SQLException | DataAccessException e) {
            System.out.println("removeWhiteUsername in DatabaseGameDAO failed");
            throw new DataAccessException(e.getMessage());
        }
    }
    @Override
    public void clear() throws DataAccessException {
        try (Connection conn = CustomDatabaseManager.getConnection()) {
            PreparedStatement preparedStatement = conn.prepareStatement("TRUNCATE TABLE games");
            preparedStatement.executeUpdate();
        }
        catch (SQLException e) {
            throw new DataAccessException(e.getMessage());
        }
    }

    @Override
    public CreateGameResponse createGame(String gameName) throws BadRequestException, DataAccessException {
        System.out.println("reached createGame");
        int gameID = -1;
        if(gameName==null) {
            throw new BadRequestException("{ \"message\": \"Error: bad request\" }");
        }
        System.out.println("gameName: "+gameName);
        try(Connection conn = DatabaseManager.getConnection()) {
            try(PreparedStatement createGameStatement = conn.prepareStatement("INSERT INTO games (gameName, game) VALUES (?,?)", Statement.RETURN_GENERATED_KEYS)) {

                createGameStatement.setString(1,gameName);
                // Serialize and store the friend JSON.
                ChessGame addGame = new ChessGame();
                ChessBoard addBoard = new ChessBoard();
                addBoard.resetBoard();
                addGame.setBoard(addBoard);
                var json = new Gson().toJson(addGame);
                Blob blob = conn.createBlob();  //make blob to store game in
                blob.setBytes(1, json.getBytes());
                createGameStatement.setBlob(2, blob);
                createGameStatement.executeUpdate();

                ResultSet rs = createGameStatement.getGeneratedKeys();
                if(rs.next()) {
                    gameID = rs.getInt(1);
                    System.out.println("generatedID: "+gameID);
                }
                if(gameID==-1) {
                    System.out.println("gameID is -1, game not created");
                    return null;
                }
            }

            return new CreateGameResponse(gameID);

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
                String gameName = rs.getString("gameName");
                Blob blob = rs.getBlob("game");
                byte[] bdata = blob.getBytes(1,(int) blob.length());
                String game = new String(bdata);
                // Deserialize from json and store the game.
                ChessGame gameObj = new Gson().fromJson(game, ChessGame.class);
                GameData addGameData = new GameData(gameID,whiteUsername,blackUsername,gameName,gameObj);
                listedGames.add(addGameData);
                System.out.println("listOGames: "+addGameData.toString());
            }
        }
        catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return listedGames;
    }

    @Override
    public GameData getGameByID(int gameID) throws DataAccessException {
        int count=0;
        String whiteUsername;
        String blackUsername;
        String gameName;
        try(Connection conn = DatabaseManager.getConnection()) {
            try(PreparedStatement getGameStatement = conn.prepareStatement("SELECT whiteUsername, blackUsername, gameName, game FROM games WHERE gameID="+gameID)) {
                ResultSet rs =getGameStatement.executeQuery();
                if(!rs.next()) {
                    return null;
                }
                else {
                    whiteUsername =rs.getString("whiteUsername");
                    blackUsername =rs.getString("blackUsername");
                    gameName =rs.getString("gameName");
                    Blob blob = rs.getBlob("game");
                    byte[] bdata = blob.getBytes(1,(int) blob.length());
                    String game = new String(bdata);
                    // Deserialize from json and store the game.
                    ChessGame gameObj = new Gson().fromJson(game, ChessGame.class);
                    GameData getGame = new GameData(gameID,whiteUsername,blackUsername,gameName,gameObj);
                    return getGame;

                }
            }
        }
        catch(SQLException e){
            System.out.println(e.getMessage());
        }



        System.out.println("null statement reached in getGameByID");
        return null;
    }

    @Override
    public int getGameIndex(int gameID) {
        return 0;
    }

    @Override
    public void joinGame(int gameID, String username, String playerColor) throws PlayerFieldTakenException, DataAccessException {
        GameData currentGame = getGameByID(gameID);
        System.out.println("joinGame() in DatabaseGameDAO reached");
        if(playerColor == null || playerColor.isEmpty()){  //needs to be ==null, not .equals()
            //observer
        }
        else if(playerColor.equals("WHITE")) {
            try(Connection conn = DatabaseManager.getConnection()) {
                try(PreparedStatement addPlayerStatement =conn.prepareStatement("UPDATE games SET whiteUsername='"+username+"' WHERE gameID="+gameID)) {
                    addPlayerStatement.executeUpdate();
                }
            }
            catch (SQLException e) {
                System.out.println(e.getMessage());
            }
        }
        else if(playerColor.equals("BLACK")){
            try(Connection conn = DatabaseManager.getConnection()) {
                try(PreparedStatement addPlayerStatement =conn.prepareStatement("UPDATE games SET blackUsername='"+username+"' WHERE gameID="+gameID)) {
                    addPlayerStatement.executeUpdate();
                }
            }
            catch (SQLException e) {
                System.out.println(e.getMessage());
            }
        }
        else {
            throw new PlayerFieldTakenException("{\"message\": \"Error: already taken\" }");
            //observer
        }
    }

    @Override
    public int size() throws DataAccessException {
        int size=0;
        try(Connection conn = DatabaseManager.getConnection()) {
            try(PreparedStatement getNumGames = conn.prepareStatement("SELECT gameID FROM games")) {
               ResultSet rs = getNumGames.executeQuery();
                while(rs.next()) {
                    size++;
                }
            }
        }
        catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return size;
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

        }
        catch (SQLException e){
            System.out.println(e.getMessage());
        }

    }
}
