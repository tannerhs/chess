package service;

import dataAccess.*;
import requests.ClearAppRequest;
import responses.ClearAppResponse;

public class ClearAppService {
    ClearAppRequest clearRequest;
    public  ClearAppService(ClearAppRequest clearRequest) throws Exception {
        this.clearRequest=clearRequest;
        if(clearRequest.userDAO()==null || clearRequest.gameDAO()==null || clearRequest.authDAO()==null) {
            throw new Exception("{ \"message\": \"Error: description\" }");
        }

    }

    public ClearAppResponse clearApp()  {
        clearRequest.userDAO().clear();
        clearRequest.authDAO().clearAll();
        clearRequest.gameDAO().clear();
        return new ClearAppResponse("database cleared");
    }
}
