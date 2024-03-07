package dataAccess;

import model.AuthData;

import java.sql.*;
import java.util.UUID;

public class DatabaseAuthDAO implements AuthDAO{
    @Override
    public void clearAll() throws DataAccessException {
        try (Connection conn = CustomDatabaseManager.getConnection()) {
            PreparedStatement preparedStatement = conn.prepareStatement("TRUNCATE TABLE users");
            preparedStatement.executeUpdate();
        }
        catch (SQLException e) {
            throw new DataAccessException(e.getMessage());
        }
    }

    @Override
    public void deleteAuth(String authToken) throws UnauthorizedAccessException, DataAccessException {
        try(Connection conn = DatabaseManager.getConnection()) {
            PreparedStatement deleteStatement = conn.prepareStatement("DELETE FROM auth WHERE authToken='"+authToken+"'", Statement.RETURN_GENERATED_KEYS);
            int rows_deleted= deleteStatement.executeUpdate();
            ResultSet rs = deleteStatement.getGeneratedKeys();
            if(rows_deleted==0) {
                throw new UnauthorizedAccessException("{\"message\": \"Error: unauthorized\"}");
            }
        }
        catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    @Override
    public AuthData createAuth(String username) throws DataAccessException {  //since username is primary key.. I dunno
        try (Connection conn = CustomDatabaseManager.getConnection()) {
            String token = UUID.randomUUID().toString();
            AuthData addedAuth = new AuthData(token,username);
            //String statement = "CREATE DATABASE IF NOT EXISTS " + token;
            System.out.println("createAuth reached");

            try (var preparedStatement = conn.prepareStatement("INSERT INTO auth (authToken, username) VALUES(?, ?)")) {
                preparedStatement.setString(1, token);
                preparedStatement.setString(2, username);  //FIXME correct username?

                preparedStatement.executeUpdate();

//                var resultSet = preparedStatement.getGeneratedKeys();
//                var ID = 0;
//                if (resultSet.next()) {
//                    ID = resultSet.getInt(1);
//                }
//                System.out.println("ID: "+ ID);
            }
            return addedAuth;
        } catch (SQLException e) {
            throw new DataAccessException(e.getMessage());
        }

    }

    @Override
    public AuthData getAuth(String token) throws DataAccessException {
        try(Connection conn = DatabaseManager.getConnection()) {
            PreparedStatement query = conn.prepareStatement("SELECT authToken, username FROM auth WHERE authToken='"+token+"'");
            ResultSet rs=query.executeQuery();
            if(rs.next()) {
                String username = rs.getString("username");
                AuthData addedAuth = new AuthData(token,username);
                return addedAuth;
            }
            throw new DataAccessException("no valid token");
        }
        catch(SQLException e) {
            System.out.println(e.getMessage());
        }
        return null;

    }

    @Override
    public int getAuthIndex(String token) {  //FIXME remove usage or replace with sql equivalent
        return 0;
    }

    @Override
    public int size() {
        return 0;
    }

    @Override
    public void configureDatabase() throws DataAccessException {
        try(Connection conn = DatabaseManager.getConnection()){
            String createAuthTable = """
                    CREATE TABLE IF NOT EXISTS auth(
                    authToken VARCHAR(255) NOT NULL,
                    username MEDIUMTEXT NOT NULL,
                    PRIMARY KEY (authToken)
                    )""";
            try (PreparedStatement createAuthTableStatement = conn.prepareStatement(createAuthTable)) {
                createAuthTableStatement.executeUpdate();
            }
        }
        catch (SQLException e){
            System.out.println(e.getMessage());
        }

    }
}
