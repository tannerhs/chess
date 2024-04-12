package websocket;

import chess.ChessGame;
import com.google.gson.Gson;
import org.eclipse.jetty.websocket.api.*;
//import javax.websocket.*;
import webSocketMessages.serverMessages.LoadGame;
import webSocketMessages.serverMessages.LoadGameObject;
import webSocketMessages.serverMessages.ServerMessage;

import java.util.ArrayList;
import java.util.HashMap;

import java.io.IOException;
import java.util.List;


import java.util.Map;

import static chess.ChessGame.TeamColor.BLACK;
import static chess.ChessGame.TeamColor.WHITE;

public class WebSocketSessions {
    public final HashMap<Integer, HashMap<String, Session>> connections = new HashMap<Integer, HashMap<String, Session>>();

    public void addSessionToGame(int gameID, String authToken,Session session) {
//        HashMap<String,Session> oldEntry = connections.get(gameID);
//        HashMap<String,Session> addEntry = new HashMap<>();
        HashMap<String,Session> addEntry;
        if(connections.containsKey(gameID)) {
            System.out.printf("update existing hash map, add %s\n",authToken);
            addEntry = connections.get(gameID);
            if(addEntry.containsKey(authToken)) {
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

        connections.put(gameID, addEntry);  //overwrite current entry with updated one with added Map entry
        System.out.printf("gameid/auth/session added to WebSocketSessions: %d, %s, %s\n", gameID,authToken,session);
    }


    public void removeSession(int gameID, String authToken) {
        HashMap<String,Session> editEntry = connections.get(gameID);
        editEntry.remove(authToken);
        connections.put(gameID,editEntry);  //overwrite entry with removal
    }


    public void broadcast(int gameID, ServerMessage serverMessage, String otherTeamAuthToken, String exceptThisAuthToken) throws IOException {  //broadcasts a message to all in a game
        System.out.println("broadcasting now");
        System.out.printf("otherTeamAuthToken: %s\n",otherTeamAuthToken);
        HashMap<String, Session> allPlayersAndObservers =connections.get(gameID);

        //can have valid auth token with invalid session
        //could be logged in but left game etc.
        List<String> badTokens = new ArrayList<>();
        ServerMessage.ServerMessageType serverMessageType = serverMessage.getServerMessageType();

        for (String token:allPlayersAndObservers.keySet()) {
//            String username =
            //System.out.
            if(!token.equals(exceptThisAuthToken)) {
                System.out.printf("not exceptThisAuthToken: %s\n",token);
                Session session = allPlayersAndObservers.get(token);
                if(serverMessageType.equals(ServerMessage.ServerMessageType.LOAD_GAME)) {
                    System.out.println("Load Game message detected");
                    LoadGame loadGame = (LoadGame)serverMessage;

                    if(token.equals(otherTeamAuthToken)) {  //other player??
                        System.out.println("changing color of load game request");
                        ChessGame.TeamColor otherTeamColor = (loadGame.getLoadGameObject().printAsTeamColor().equals(WHITE)? BLACK:WHITE);
                        serverMessage= new LoadGame(new LoadGameObject(loadGame.getGameData(),otherTeamColor, loadGame.getLoadGameObject().otherTeamAuthToken()));
                    }
                    else {
                        //obeserver, set print-as color to white
                        System.out.printf("observer token being printed: %s\n",token);
                        serverMessage= new LoadGame(new LoadGameObject(loadGame.getGameData(), WHITE, loadGame.getLoadGameObject().otherTeamAuthToken()));
                    }
                }

                String sendMessage=new Gson().toJson(serverMessage);

                if(!session.isOpen()) {
                    //add to list to remove from map after for loop (so no exceptions)
                    badTokens.add(token);
                }
                else {
                    session.getRemote().sendString(sendMessage);
                }
            }
        }


        for (String badToken: badTokens) {
            allPlayersAndObservers.remove(badToken);
        }
        connections.put(gameID, allPlayersAndObservers);
    }
}
