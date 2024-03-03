package dataAccess;

import model.UserData;

public class DatabaseUserDAO implements UserDAO {
    @Override
    public void clear() {

    }

    @Override
    public void createUser(String username, String password, String email) {

    }

    @Override
    public UserData getUser(String username) {
        return null;
    }

    @Override
    public boolean addUser(UserData addedUser) {
        return false;
    }

    @Override
    public int size() {
        return 0;
    }
}
