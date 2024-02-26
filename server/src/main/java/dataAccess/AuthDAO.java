package dataAccess;

import model.AuthData;

import java.util.List;

public interface AuthDAO {
    public void clearAll();
    public void deleteAuth(String authToken) throws UnauthorizedAccessException;
    public AuthData createAuth(String username);  //token is string

    List<AuthData> getAuthDataList();

    public AuthData getAuth(String token);

//    public void delete(String token);

    public AuthData get(int i);

    int size();
}
