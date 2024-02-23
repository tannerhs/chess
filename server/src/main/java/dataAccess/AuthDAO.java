package dataAccess;

import model.AuthData;
import model.GameData;
import model.UserData;

import java.util.Collection;
import java.util.List;

public interface AuthDAO {
    public void clearAll();
    public void deleteAuth(String authToken);
    public String createAuth(String username);  //token is string

    List<AuthData> getAuthDataList();

    public AuthData getAuth(String token);

    public void delete(String token);

    public AuthData get(int i);

    int size();
}
