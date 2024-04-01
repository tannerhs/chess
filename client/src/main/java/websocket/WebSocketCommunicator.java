package websocket;

import bodyResponses.CreateGameBodyResponse;
import bodyResponses.LoginBodyResponse;
import chess.ChessGame;
import client_requests.CreateGameRequest;
import client_requests.JoinGameRequest;
import client_requests.LoginRequest;
import client_requests.LogoutRequest;
import client_responses.*;
import com.google.gson.Gson;
import jakarta.websocket.EndpointConfig;
import model.AuthData;
import model.GameData;
import model.UserData;
import org.glassfish.tyrus.core.wsadl.model.Endpoint;
import ui.Client;
import ui.ServerMessageObserver;
import webSocketMessages.ResponseException;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

import webSocketMessages.serverMessages.LoadGame;
import webSocketMessages.serverMessages.ServerMessage;
import webSocketMessages.userCommands.UserGameCommand;

import javax.websocket.*;

public class WebSocketCommunicator extends Endpoint {
    int port=8080;
    Session session;
    ServerMessageObserver notificationHandler;


    public WebSocketCommunicator(int port, String url) throws ResponseException {
        this.port = port;
        try {
            url = url.replace("http", "ws");
            URI socketURI = new URI(url + "/connect");
            this.notificationHandler = new ServerMessageObserver();

            WebSocketContainer container = ContainerProvider.getWebSocketContainer();
            this.session = container.connectToServer(this, socketURI);

            //set message handler
            this.session.addMessageHandler(new MessageHandler.Whole<String>() {
                @Override
                public void onMessage(String message) {  //every time a message is received, this runs
                    notificationHandler.notify(message);
                }
            });
        } catch (DeploymentException | IOException | URISyntaxException ex) {
            throw new ResponseException(500, ex.getMessage());
        } catch (Exception e) {
            //all other excceptions;
        }

        onOpen(session,null);
    }

    public void send(String msg) throws Exception {
        this.session.getBasicRemote().sendText(msg);
    }



    public void onOpen(Session session, EndpointConfig endpointConfig) {
        System.out.println("Websocket initialized");
    }


}
