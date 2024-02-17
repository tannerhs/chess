package dataAccess;

import model.AuthData;
import model.UserData;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class MemoryAuthDAO implements AuthDAO{
    static List<AuthData> auth=new ArrayList<AuthData>();  //static allows you to not forget everything

    @Override
    public void clearAll() {
        while (auth.size()>0) {
            auth.remove(0);
        }
    }

    @Override
    public void deleteAuth(String authToken) {
        //
    }

    @Override
    public String createAuth(String username) {
        String token = UUID.randomUUID().toString();
        auth.add(new AuthData(token,username));
        return token;
    }

    @Override
    public AuthData getAuth(String token) {  //error check
        int index= getAuthIndex(token);
        return (index==-1) ? auth.get(index) : null;
    }

    public int getAuthIndex(String token) {
        for(int i=0; i<auth.size();i++) {
            if(auth.get(i).authToken()==token) {
                return i;
            }
        }
        return -1;
    }

    @Override
    public void delete(String token) {
        int index=getAuthIndex(token);
        auth.remove(index);
    }
}
