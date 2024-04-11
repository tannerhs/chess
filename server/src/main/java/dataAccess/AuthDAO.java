package dataAccess;

import model.AuthData;

import javax.xml.crypto.Data;

public interface AuthDAO {
    public void clearAll() throws DataAccessException;
    public void deleteAuth(String authToken) throws UnauthorizedAccessException, DataAccessException;
    public AuthData createAuth(String username) throws DataAccessException;  //token is string

    public AuthData getAuth(String token) throws DataAccessException, UnauthorizedAccessException;
    public AuthData getAuthByUsername(String username) throws DataAccessException;

    int getAuthIndex(String token);

    int size();

    void configureDatabase() throws DataAccessException;
}
