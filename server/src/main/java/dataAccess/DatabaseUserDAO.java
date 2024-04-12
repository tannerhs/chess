package dataAccess;

import model.UserData;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

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
    public void createUser(String username, String password, String email) throws DataAccessException {
        try (Connection conn = CustomDatabaseManager.getConnection()) {
            //UserData addedUser = new UserData(username,password,email);
            //String statement = "CREATE DATABASE IF NOT EXISTS " + token;
            System.out.println("createUser reached");


            try (var preparedStatement = conn.prepareStatement("INSERT INTO users (username, password, email) VALUES(?, ?,?)")) {
                preparedStatement.setString(1, username);
                preparedStatement.setString(2, password);
                preparedStatement.setString(3,email);

                preparedStatement.executeUpdate();

            }
        } catch (SQLException e) {
            throw new DataAccessException(e.getMessage());
        }
    }


    @Override
    public String getPassword(String username) throws DataAccessException {  //return hashed password
            try (Connection conn = CustomDatabaseManager.getConnection()) {
                //UserData addedUser = new UserData(username,password,email);
                //String statement = "CREATE DATABASE IF NOT EXISTS " + token;
                System.out.println("getPassword reached");

                try (var preparedStatement = conn.prepareStatement("SELECT username, password FROM users WHERE username='"+username+"'")) {
                    ResultSet resultSet= preparedStatement.executeQuery();
                    if (resultSet.next()) {
                        String name = resultSet.getString("username");
                        String password=resultSet.getString("password");
                        System.out.println("returning password");
                        return password;
                    }
                    System.out.println("returned null password");
                    return null;  //if user doesn't exist
                    //System.out.println("Hashed password: "+ hashed_password);
                }
            } catch (SQLException e) {
                throw new DataAccessException(e.getMessage());
            }
    }

    @Override
    public UserData getUser(String username) throws DataAccessException {
        return null;
    }

    @Override
    public boolean addUser(UserData addedUser) throws DataAccessException {
        try(Connection conn = DatabaseManager.getConnection()) {
            try (PreparedStatement findUserStatement = conn.prepareStatement("SELECT username FROM users WHERE username='"+addedUser.username()+"'")){
                ResultSet resultSet=findUserStatement.executeQuery();
                if (!resultSet.next()) {  //if no users of that username already...
                    try(PreparedStatement addUserStatement= conn.prepareStatement("INSERT INTO users(username,password,email) VALUES(?,?,?)")){
                        addUserStatement.setString(1, addedUser.username());
                        addUserStatement.setString(2, addedUser.password());
                        addUserStatement.setString(3,addedUser.email());

                        addUserStatement.executeUpdate();

                    }
                    return true;

                }
                return false;
                //System.out.println("ID: "+ ID);
            }
        }
        catch(SQLException e) {
            System.out.println(e.getMessage());
        }
        createUser(addedUser.username(), addedUser.password(), addedUser.email());
        return true;
    }

    @Override
    public int size() {
        return 0;
    }
}
