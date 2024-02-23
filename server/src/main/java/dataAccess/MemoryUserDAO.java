package dataAccess;

import model.AuthData;
import model.UserData;

import java.util.ArrayList;
import java.util.List;

public class MemoryUserDAO implements UserDAO{
    static List<UserData> users=new ArrayList<UserData>();  //static allows you to not forget everything

    @Override
    public void clear() {
        while (users.size()>0) {
            users.remove(0);
        }
    }

    @Override
    public void createUser(String username, String password,String email) {
        users.add(new UserData(username,password,email));
    }

    @Override
    public UserData getUser(String username) {
        for(UserData user: users) {
            if (username==user.username()) {
                return user;
            }
        }
        return null;
    }

    @Override
    public void addUser(UserData addedUser) {
        users.add(addedUser);
    }


    public int getUserIndex(String username) {
        for(int i=0; i<users.size(); i++) {
            if (username==users.get(i).username()) {
                return i;
            }
        }
        return -1;
    }

    @Override
    public UserData get(int i) {
        return users.get(i);
    }

    @Override
    public int size() {
        return users.size();
    }
}
