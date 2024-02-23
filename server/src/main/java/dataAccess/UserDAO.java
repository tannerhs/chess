package dataAccess;

import model.*;

import java.util.Map;

public interface UserDAO {
    public void clear();
    public void createUser(String username, String password,String email);
    public UserData getUser(String username);

    public void addUser(UserData addedUser);

    UserData get(int i);

    int size();
}
