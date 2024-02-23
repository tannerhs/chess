package service;

import dataAccess.*;
import requests.ClearAppRequest;

public class ClearAppService {
    public ClearAppService(ClearAppRequest clearRequest){

        clearRequest.userDAO().clear();
        clearRequest.authDAO().clearAll();
        clearRequest.gameDAO().clear();

        //
    }

    public ClearAppService(UserDAO userDAO) {
        //
    }
}
