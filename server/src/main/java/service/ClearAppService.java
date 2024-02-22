package service;

import dataAccess.*;

public class ClearAppService {
    public ClearAppService(){
        UserDAO userDAO = new MemoryUserDAO();
        userDAO.clear();
        AuthDAO authDAO = new MemoryAuthDAO();
        authDAO.clearAll();
        GameDAO gameDAO = new MemoryGameDAO();
        gameDAO.clear();

        //
    }

    public ClearAppService(UserDAO userDAO) {
        //
    }
}
