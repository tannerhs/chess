package websocket;

import chess.ChessGame;
import client_responses_http.*;
import client_responses_ws.JoinGameResponseWS;
import com.google.gson.Gson;

import ui.Client;
import ui.ServerMessageObserver;
import webSocketMessages.ResponseException;
import webSocketMessages.userCommands.JoinObserver;
import webSocketMessages.userCommands.JoinPlayer;
import webSocketMessages.userCommands.UserGameCommand;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

import javax.websocket.*;

public class WebSocketCommunicator extends Endpoint {
    int port;
    Session session;
    ServerMessageObserver notificationHandler;


    public WebSocketCommunicator(int port, String url, Client me) throws ResponseException {
        this.port = port;
        try {
            url = url.replace("http", "ws");
            URI socketURI = new URI(url + "/connect");
//            URI socketURI = new URI("ws://localhost:8080/connect");
            //this.notificationHandler = new ServerMessageObserver();

            WebSocketContainer container = ContainerProvider.getWebSocketContainer();
            this.session = container.connectToServer(this, socketURI);
//            session = ContainerProvider.getWebSocketContainer().connectToServer(new Endpoint() {
//                @Override
//                public void onOpen(Session session, EndpointConfig endpointConfig) {}
//            }, socketURI);
            if(this.session==null) {
                System.out.println("session initialization failed in constructor");
            }

            //set message handler
            this.session.addMessageHandler(new MessageHandler.Whole<String>() {
                @Override
                public void onMessage(String message) {  //every time a message is received, this runs
                    me.notify(message);
                    //
                }
            });
        } catch (DeploymentException | IOException | URISyntaxException ex) {
            throw new ResponseException(500, ex.getMessage());
        } catch (Exception e) {
            //all other excceptions;
            e.printStackTrace();
        }

        //onOpen(session,null);
    }

    public void send(String msg) throws Exception {
        this.session.getBasicRemote().sendText(msg);
    }

    public void login(String authToken) throws ResponseException {
        //add to map of Server Sessions. FIXME; server side!

    }

    public void logout(String authToken) throws ResponseException {  //FIXME replace action with server message
        try {
//            var action = new Action(Action.Type.EXIT, visitorName);
//            this.session.getBasicRemote().sendText(new Gson().toJson(action));
            this.session.close();
        } catch (IOException ex) {
            throw new ResponseException(500, ex.getMessage());
        }
        //remove from map of Server Sessions. FIXME; server side!
    }

    public JoinGameResponseWS joinGame(String authToken, ChessGame.TeamColor joinAsColor, JoinGameResponseHttp joinGameResponseHttp, Session session) throws Exception {
        System.out.println("JoinGameResponseWS reached");
        if(session==null) {
            System.out.println("session initialization failed in joinGame");
        }
        //send JOIN_PLAYER or JOIN_OBSERVER
        //receive LOAD_GAME
        if(joinGameResponseHttp.statusCode()==200 || ) {
            if(joinAsColor==null) {
                try {
                    UserGameCommand joinObserver = new JoinObserver(authToken, joinGameResponseHttp.gameID());
                    assert session != null;
                    String message = new Gson().toJson(joinObserver);
                    session.getBasicRemote().sendText(message);  //send user command
                }
                catch (Exception e) {
                    //if invalid UserGameCommand, only inform root
                    return new JoinGameResponseWS(null,joinGameResponseHttp.statusCode(), joinGameResponseHttp.statusMessage());
                }
            }
            else {
                try {
                    UserGameCommand joinPlayer = new JoinPlayer(authToken, joinGameResponseHttp.gameID(),joinAsColor);
                    assert session != null;
                    String message = new Gson().toJson(joinPlayer);
                    session.getBasicRemote().sendText(message);  //send user command
                }
                catch (Exception e) {
                    //if invalid UserGameCommand, only inform root
                    System.out.println("invalid UserGameCommand");
                    return new JoinGameResponseWS(null,joinGameResponseHttp.statusCode(), joinGameResponseHttp.statusMessage());
                }
            }
        }
        else {  //if join unsuccessful
            System.out.println("joinGame response code not 200");
            try {
                UserGameCommand joinPlayer = new JoinPlayer(authToken, joinGameResponseHttp.gameID(),null);
                assert session != null;
                String message = new Gson().toJson(joinPlayer);
                session.getBasicRemote().sendText(message);  //send user command
            }
            catch (Exception e) {
                //if invalid UserGameCommand, only inform root
                System.out.println("invalid UserGameCommand");
                return new JoinGameResponseWS(null,joinGameResponseHttp.statusCode(), joinGameResponseHttp.statusMessage());
            }
        }
        return null;
    }

//    @Override
//    public void onOpen(Session session, EndpointConfig endpointConfig) {
//        System.out.println("Websocket initialized");
//    }

    public Session getSession() {
        return session;
    }


    @Override
    public void onOpen(Session session, javax.websocket.EndpointConfig endpointConfig) {

    }
}
