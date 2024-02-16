package dataAccess;

import model.*;

import java.util.Map;

public interface UserDAO {
    //Map<UserData> users = new Map<UserData>();
    public void clear();
    public void createUser(UserData userData);
    public UserData getUser(String username);
    public void upodateUser(String username);  //
    public int createGame(GameData gameData);
    public GameData getGame(int gameID);
    public void updateGame(GameData gameData);
    public GameData[] listGames();
    public String createAuth(String username);  //token is string
    public AuthData getAuth(String token);
    public void delete(String token);
}
