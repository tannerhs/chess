package dataAccess;

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
    public void createUser(String username, String password,String email)  {
        UserData addedUser = new UserData(username,password,email);
        if(users.contains(addedUser)) {
            //throw new PlayerFieldTakenException("");
            //return false;
        }
        else {
            users.add(addedUser);
            //return true;
        }
    }

    @Override
    public boolean addUser(UserData addedUser) {
        if(users.contains(addedUser)) {
            return false;
        }
        else {
            createUser(addedUser.username(), addedUser.password(), addedUser.email());
            //users.add(addedUser);
            return true;
        }

    }

    @Override
    public UserData getUser(String username) {
        for(int i=0; i<users.size();i++) {
            System.out.println("users.get(i).username(): " + users.get(i).username());
            if (username.equals(users.get(i).username())) {
                return users.get(i);
            }
        }
        return null;
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
    public int size() {
        return users.size();
    }
}
