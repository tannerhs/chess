package dataAccess;

import model.UserData;

import java.util.ArrayList;
import java.util.List;

public class MemoryUserDAO implements UserDAO{
    static List<UserData> usersInstance=new ArrayList<UserData>();  //static allows you to not forget everything

}
