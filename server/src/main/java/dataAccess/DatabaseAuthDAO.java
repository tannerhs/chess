package dataAccess;

import model.AuthData;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.UUID;

public class DatabaseAuthDAO implements AuthDAO{
    @Override
    public void clearAll() throws DataAccessException {
        try (Connection conn = DatabaseManager.getConnection()) {
            PreparedStatement preparedStatement = conn.prepareStatement("TRUNCATE TABLE users");
            preparedStatement.executeUpdate();
        }
        catch (SQLException e) {
            throw new DataAccessException(e.getMessage());
        }
    }

    @Override
    public void deleteAuth(String authToken) throws UnauthorizedAccessException {

    }

    @Override
    public AuthData createAuth(String username) throws DataAccessException {  //since username is primary key.. I dunno
        try (Connection conn = DatabaseManager.getConnection()) {
            String token = UUID.randomUUID().toString();
            AuthData addedAuth = new AuthData(token,username);
            String statement = "CREATE DATABASE IF NOT EXISTS " + token;

            try (var preparedStatement = conn.prepareStatement("INSERT INTO auth (authToken, username) VALUES(?, ?)")) {
                preparedStatement.setString(1, token);
                preparedStatement.setString(2, username);  //FIXME correct username?

                preparedStatement.executeUpdate();

                var resultSet = preparedStatement.getGeneratedKeys();
                var ID = 0;
                if (resultSet.next()) {
                    ID = resultSet.getInt(1);
                }
                System.out.println("ID: "+ ID);
            }
            return addedAuth;
        } catch (SQLException e) {
            throw new DataAccessException(e.getMessage());
        }

    }

    @Override
    public AuthData getAuth(String token) {
        return null;
    }

    @Override
    public int getAuthIndex(String token) {
        return 0;
    }

    @Override
    public int size() {
        return 0;
    }
}
