package service;

import dataAccess.*;
import requests.ClearAppRequest;
import responses.ClearAppResponse;

public class ClearAppService {
    ClearAppRequest clearRequest;
    public  ClearAppService(ClearAppRequest clearRequest){
        this.clearRequest=clearRequest;

    }

    public ClearAppResponse clearApp() {
        clearRequest.userDAO().clear();
        clearRequest.authDAO().clearAll();
        clearRequest.gameDAO().clear();
        return new ClearAppResponse("database cleared");
    }
}
