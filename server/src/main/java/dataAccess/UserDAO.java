package dataAccess;

import model.*;

public interface UserDAO {
    public void clear() throws DataAccessException;
    public void createUser(String username, String password, String email) throws DataAccessException;

    String getPassword(String username) throws DataAccessException;

    public UserData getUser(String username) throws DataAccessException;

    public boolean addUser(UserData addedUser) throws DataAccessException;

    int size();

    public void configureDatabase() throws DataAccessException;
}
