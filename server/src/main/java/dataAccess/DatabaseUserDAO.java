package dataAccess;

import model.UserData;
import java.sql.*;
import java.util.Properties;

import static dataAccess.DatabaseManager.*;

public class DatabaseUserDAO implements UserDAO {

    //public void configureDatabase() {
    //
    //}

    @Override
    public void clear() throws DataAccessException {
            try (Connection conn = DatabaseManager.getConnection()) {
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
