package websocket;

import com.google.gson.Gson;
import dataAccess.GameDAO;
import org.eclipse.jetty.websocket.api.*;
import webSocketMessages.serverMessages.LoadGame;
import webSocketMessages.serverMessages.Notification;
import webSocketMessages.serverMessages.ServerMessage;

import java.net.URI;
import java.util.HashMap;
import java.util.Scanner;

import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.ConcurrentHashMap;


import java.util.Map;

public class WebSocketSessions {
    public final Map<Integer, Map<String, Session>> connections = new ConcurrentHashMap<>();

    public void addSessionToGame(int gameID, String authToken,Session session) {
        Map<String,Session> addEntry = new HashMap<>();
        addEntry.put(authToken,session);
        connections.put(gameID, addEntry);
    }

    public void removeSessionFromGame(String username, int gameID, Session session) {
        String authToken = "fixme";
        //connections.put(gameID, Map<authToken,session>());
    }

    public void removeSession(Session session) {
        //
    }

    Map<String,Session> getSessionsForGame(int gameID) {
        //
        return null;
    }

    public void broadcast(int gameID, Notification notification, String exceptThisAuthToken) throws IOException {  //broadcasts a message to all in a game
        System.out.println("broadcasting now");
        Map<String, Session> allPlayersAndObservers =connections.get(gameID);
        for (String token:allPlayersAndObservers.keySet()) {
            if(!token.equals(exceptThisAuthToken)) {
                Session session = (Session) connections.get(token);
                String sendMessage=new Gson().toJson(notification);
                session.getRemote().sendString(sendMessage);
            }
        }
    }
}
