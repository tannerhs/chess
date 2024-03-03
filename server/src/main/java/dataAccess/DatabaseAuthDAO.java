package dataAccess;

import model.AuthData;

public class DatabaseAuthDAO implements AuthDAO{
    @Override
    public void clearAll() {

    }

    @Override
    public void deleteAuth(String authToken) throws UnauthorizedAccessException {

    }

    @Override
    public AuthData createAuth(String username) {
        return null;
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
