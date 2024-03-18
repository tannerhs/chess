package ui;

import requests.LoginRequest;
import requests.RegisterRequest;
import responses.LoginResponse;

//7 methods for 7 endpoints
//take in Request object and return Response object

//also create ClientCommunicator class called by server facade, do get, post, delete update etc.. http

public class ServerFacade {  //represents your server to the client, provides simple way to do it
        //2-3 lines of code in each since calls client communicator

    public LoginResponse login(LoginRequest request) {
        System.out.print("server facade login method reached\n");
        return null;
    }

    public RegisterRequest register(RegisterRequest request) {
        System.out.print("server facade register method reached\n");
        return null;
    }
}
