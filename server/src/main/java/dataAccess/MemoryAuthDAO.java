package dataAccess;

import model.AuthData;

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
    public void deleteAuth(String authToken) throws UnauthorizedAccessException {
        if(getAuth(authToken)==null) {  //if authToken does not exist, cannot remove an authToken from auth
            throw new UnauthorizedAccessException("{\"message\": \"Error: unauthorized\"}");
        }
        for(int i=0; i<auth.size(); i++) {
            if(auth.get(i).authToken().equals(authToken)) {
                auth.remove(i);
                return;
            }
        }
    }

    @Override
    public AuthData createAuth(String username) {  //does not check that username is in users, FIXME
        String token = UUID.randomUUID().toString();
        AuthData addedAuth = new AuthData(token,username);
        auth.add(addedAuth);
        return addedAuth;
    }


    @Override
    public AuthData getAuth(String token) {  //error check
        int index= getAuthIndex(token);
        System.out.println("getAuthIndex returns "+index);
        return (index==-1) ? null : auth.get(index);
    }

    @Override
    public int getAuthIndex(String token) {
        for(int i=0; i<auth.size();i++) {
            if(auth.get(i).authToken().equals(token)) {
                return i;
            }
        }
        return -1;
    }


    @Override
    public int size() {
        return auth.size();
    }


}
