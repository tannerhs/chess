package websocket;

import com.google.gson.Gson;
import org.eclipse.jetty.websocket.api.*;
//import javax.websocket.*;
import webSocketMessages.serverMessages.Notification;

import java.util.ArrayList;
import java.util.HashMap;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;


import java.util.Map;

public class WebSocketSessions {
    public final HashMap<Integer, HashMap<String, Session>> connections = new HashMap<Integer, HashMap<String, Session>>();

    public void addSessionToGame(int gameID, String authToken,Session session) {
//        HashMap<String,Session> oldEntry = connections.get(gameID);
//        HashMap<String,Session> addEntry = new HashMap<>();
        HashMap<String,Session> addEntry;
        if(connections.containsKey(gameID)) {
            System.out.printf("update existing hash map, add %s\n",authToken);
            addEntry = connections.get(gameID);
            if(addEntry.containsKey(authToken)) {  //fixme, remove if-else
                System.out.println("uhhhh");
            }
            else {
                addEntry.put(authToken,session);
            }
        }
        else {
            System.out.printf("new hash map, add %s\n",authToken);
            addEntry = new HashMap<>();
            addEntry.put(authToken,session);
        }

//        for( String authToken2:oldEntry.keySet()) {
//            Session value = oldEntry.get(authToken2);
//            addEntry.put(authToken2,value);
//        }
        connections.put(gameID, addEntry);  //overwrite current entry with updated one with added Map entry
        System.out.printf("gameid/auth/session added to WebSocketSessions: %d, %s, %s\n", gameID,authToken,session);
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
        HashMap<String, Session> allPlayersAndObservers =connections.get(gameID);

        //can have valid auth token with invalid session
        //could be logged in but left game etc.
        List<String> badTokens = new ArrayList<>();


        for (String token:allPlayersAndObservers.keySet()) {
            //System.out.
            if(!token.equals(exceptThisAuthToken)) {
                System.out.printf("notification sent, gameID: %d\n", gameID);
                Session session = allPlayersAndObservers.get(token);
                String sendMessage=new Gson().toJson(notification);
                System.out.printf("notification sent to %s: %s\n",token,sendMessage);
                System.out.printf("notification sent by %s \n", exceptThisAuthToken);
                if(!session.isOpen()) {
                    //add to list to remove from map after for loop (so no exceptions)
                    badTokens.add(token);
                }
                else {
                    session.getRemote().sendString(sendMessage);
                }
//                session.getBasicRemote().sendText(sendMessage);
                //
            }
        }


        for (String badToken: badTokens) {
            allPlayersAndObservers.remove(badToken);
        }
        connections.put(gameID, allPlayersAndObservers);
    }
}
