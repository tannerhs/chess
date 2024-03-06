package dataAccess;

import model.AuthData;

public interface AuthDAO {
    public void clearAll() throws DataAccessException;
    public void deleteAuth(String authToken) throws UnauthorizedAccessException;
    public AuthData createAuth(String username) throws DataAccessException;  //token is string

    public AuthData getAuth(String token);

    int getAuthIndex(String token);

    int size();

    void configureDatabase() throws DataAccessException;
}
