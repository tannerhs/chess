package dataAccess;

import model.UserData;
import java.sql.*;

public class DatabaseUserDAO implements UserDAO {

    @Override
    public void configureDatabase() throws DataAccessException {
        try {
            DatabaseManager.createDatabase();
            try(Connection conn = DatabaseManager.getConnection()) {
                String createUsersTable = """
                    CREATE TABLE IF NOT EXISTS users(
                    username VARCHAR(255) NOT NULL,
                    password MEDIUMTEXT NOT NULL,
                    email MEDIUMTEXT NOT NULL,
                    PRIMARY KEY (username)
                    )""";
                try (PreparedStatement createUsersTableStatement = conn.prepareStatement(createUsersTable)) {
                    createUsersTableStatement.executeUpdate();
                }
            }
        }
        catch(SQLException e){
            System.out.println(e.getMessage());
        }

    }

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
    public void createUser(String username, String password, String email) {

    }

    @Override
    public UserData getUser(String username) {
        return null;
    }

    @Override
    public boolean addUser(UserData addedUser) {
        return false;
    }

    @Override
    public int size() {
        return 0;
    }
}
