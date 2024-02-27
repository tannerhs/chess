package dataAccess;

import model.*;

public interface UserDAO {
    public void clear();
    public void createUser(String username, String password,String email);
    public UserData getUser(String username);

    public boolean addUser(UserData addedUser);

    int size();
}
