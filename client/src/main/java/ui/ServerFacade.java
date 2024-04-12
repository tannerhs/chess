package ui;

import chess.ChessGame;
import client_responses_http.*;
import client_responses_ws.JoinGameResponseWS;
import model.UserData;
import client_requests.*;
import websocket.GamePlayUI;
import websocket.WebSocketCommunicator;

import java.io.PrintStream;
import java.nio.charset.StandardCharsets;

import static ui.EscapeSequences.SET_BG_COLOR_BLACK;
import static ui.EscapeSequences.SET_TEXT_COLOR_WHITE;

//7 methods for 7 endpoints
//take in Request object and return Response object

//add makeMove method

//also create ClientCommunicator class called by server facade, do get, post, delete update etc.. http

public class ServerFacade {
    int port=8080;
    HttpCommunicator httpCommunicator;
    WebSocketCommunicator webSocketCommunicator;

    public ServerFacade(int port, Client myClient) {
        this.port=port;
        httpCommunicator=new HttpCommunicator(port);
        String url = "ws://localhost:"+port;
        try {
            webSocketCommunicator = new WebSocketCommunicator(port,url, myClient);  //same port
        }
        catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
    //represents your server to the client, provides simple way to do it
        //2-3 lines of code in each since calls client communicator

    public ClearAppResponse clearApp(String authToken) throws Exception {
        return httpCommunicator.clearApp(authToken);
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

    public JoinGameResponseHttp joinGame(String authToken, JoinGameRequest joinGameRequest) throws Exception {
        JoinGameResponseHttp joinGameResponseHttp = httpCommunicator.joinGame(authToken,joinGameRequest);

        PrintStream out = new PrintStream(System.out, true, StandardCharsets.UTF_8);
        //if exception was one I accounted for, it should never be thrown to the user, so joinGameResponseHttp should have the correct statusCode (returned before exception can be thrown to user)

        Boolean observe = joinGameRequest.playerColor().isEmpty();  //that is what I put in by default
        ChessGame.TeamColor joinAsColor=null;
        if(joinGameRequest.playerColor().equals("BLACK")){
            joinAsColor=ChessGame.TeamColor.BLACK;
        }
        else if(joinGameRequest.playerColor().equals("WHITE")) {
            joinAsColor= ChessGame.TeamColor.WHITE;
        }
        else if(joinGameRequest.playerColor().isEmpty()) {
            joinAsColor=null;  //when color is "", we observer
        }
        else {
            System.out.println("ServerFacade joinGame received invalid color");
        }
        if(webSocketCommunicator.getSession()==null) {
            System.out.println("webSocketCommunicator.getSession()==null");
        }
        JoinGameResponseWS joinGameResponseWS= webSocketCommunicator.joinGame(authToken, joinAsColor, joinGameResponseHttp,webSocketCommunicator.getSession());  //add session as param??

        return joinGameResponseHttp;
    }

    public void startGameplayMenu(PrintStream out, String currentUserAuthToken, JoinGameResponseHttp joinGameResponse, ChessGame.TeamColor joinAsColor) {
        out.print(SET_BG_COLOR_BLACK);
        out.print(SET_TEXT_COLOR_WHITE);
        GamePlayUI gamePlayUI = new GamePlayUI(currentUserAuthToken, joinGameResponse.gameID());
        gamePlayUI.gamePlayMenu(out,webSocketCommunicator.getMyClient(),webSocketCommunicator, joinAsColor);
    }


}
