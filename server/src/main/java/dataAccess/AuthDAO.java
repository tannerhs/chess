package dataAccess;

import model.AuthData;
import model.GameData;
import model.UserData;

public interface AuthDAO {
    public void clearAll();
    public void deleteAuth(String authToken);
    public String createAuth(String username);  //token is string
    public AuthData getAuth(String token);

    public void delete(String token);
}
