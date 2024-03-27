package ui;

import bodyResponses.CreateGameBodyResponse;
import bodyResponses.LoginBodyResponse;
import chess.ChessGame;
import client_responses.*;
import com.google.gson.Gson;
import model.AuthData;
import model.GameData;
import model.UserData;
import client_requests.*;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URI;
import java.util.List;

//7 methods for 7 endpoints
//take in Request object and return Response object

//add makeMove method

//also create ClientCommunicator class called by server facade, do get, post, delete update etc.. http

public class ServerFacade {
    int port=8080;
    HttpCommunicator httpCommunicator;
    WebSocketCommunicator webSocketCommunicator;

    public ServerFacade(int port) {
        this.port=port;
        httpCommunicator=new HttpCommunicator(port);
        //WebSocketCommunicator = new WebSocketCommunicator(port);  //FIXME different or same port?
    }
    //represents your server to the client, provides simple way to do it
        //2-3 lines of code in each since calls client communicator

    public ClearAppResponse ClearApp(String authToken) throws Exception {
        return httpCommunicator.ClearApp(authToken);
    }

    public LoginResponse login(LoginRequest loginRequest) throws Exception {
        return httpCommunicator.login(loginRequest);
    }

    public RegisterResponse register(UserData addUser) throws Exception {
        return httpCommunicator.register(addUser);
    }

    public LogoutResponse logout(LogoutRequest logoutRequest) throws Exception {
        return httpCommunicator.logout(logoutRequest);
    }


    public ListGamesResponse listGames(String authToken) throws Exception {
        return httpCommunicator.listGames(authToken);
    }

    public CreateGameResponse createGame(String authToken, CreateGameRequest createGameRequest) throws Exception {
       return httpCommunicator.createGame(authToken,createGameRequest);
    }

    public JoinGameResponse joinGame(String authToken,JoinGameRequest joinGameRequest) throws Exception {
        return httpCommunicator.joinGame(authToken,joinGameRequest);
    }
}
