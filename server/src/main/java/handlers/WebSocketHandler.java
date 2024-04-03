package handlers;

//import org.eclipse.jetty.websocket.api.Session;
//import org.eclipse.jetty.server.session.Session;
import chess.ChessGame;
import com.google.gson.Gson;
import dataAccess.*;
import org.eclipse.jetty.websocket.api.annotations.*;
import webSocketMessages.serverMessages.LoadGame;
import webSocketMessages.serverMessages.Notification;
import webSocketMessages.serverMessages.ServerMessage;
import webSocketMessages.userCommands.JoinObserver;
import webSocketMessages.userCommands.UserGameCommand;
import websocket.WebSocketSessions;
//import javax.websocket.*;
import org.eclipse.jetty.websocket.api.Session;
//import webSocketMessages.Notification;

import java.util.HashMap;
import java.util.Map;

@WebSocket
public class WebSocketHandler {
    WebSocketSessions connections= new WebSocketSessions();
    Map<Session,String> sessions = new HashMap<Session,String>();
    GameDAO games = new DatabaseGameDAO();
    AuthDAO auth = new DatabaseAuthDAO();
    UserDAO users = new DatabaseUserDAO();

    public WebSocketHandler() {
        System.out.println("WebSocketHandler constructor reached");
    }

    @OnWebSocketMessage
    public void onMessage(Session session, String message) throws Exception {
        System.out.printf("Received: %s", message);
        //deserialize user game commands, which edits data more directly in the case of makeMove by calling DAO
        //deserialize UserGameCommand to get command type
        UserGameCommand userGameCommand = new Gson().fromJson(message,UserGameCommand.class);
        String authToken = userGameCommand.getAuthString();
        //deserialize specific command:
        System.out.printf("commandType: %s\n",userGameCommand.getCommandType());
        switch(userGameCommand.getCommandType()) {
            case JOIN_OBSERVER:
                System.out.println("JOIN_OBSERVER case reached");
                //send load_game to root
                JoinObserver joinObserver= new Gson().fromJson(message, JoinObserver.class);
                int gameID = joinObserver.getGameID();
                ChessGame myGame = games.getGameByID(gameID).game();
                String sendMessage=new Gson().toJson(new LoadGame(ServerMessage.ServerMessageType.LOAD_GAME,myGame));
                System.out.printf("sendMessage: %s\n",sendMessage);
//                session.getBasicRemote().sendText(sendMessage);
                session.getRemote().sendString(sendMessage);


                //now broadcast notification to everyone playing or observing this game
                String username = auth.getAuth(authToken).username();
                String notificationMessage = username+" joined the game as an observer.\n";
                Notification notification = new Notification(notificationMessage);
                connections.addSessionToGame(joinObserver.getGameID(),authToken,session);
                connections.broadcast(gameID,notification,authToken);  //FIXME, notifications not working yet
                break;
            case JOIN_PLAYER:
                break;
            //TODO finish case statement
        }
        //depending on UserGameCommand message type, do stuff


        //send server messages, receive client messages
        //send back server message if needed
    }

//    @OnWebSocketConnect
//    public void onConnect(Session session) {
//        //String username ="um";  //FIXME
//        //sessions.put(session,username);
//        //keep track of sessions in session manager class, store sessions form onMessage in session map
//    }
//
//
//
//    @OnWebSocketClose
//    public void onClose(Session session) {  //pass session in causes error...
//        //remove stuff from map
//    }

    @OnWebSocketError
    public void onError(Throwable throwable) {
        //
    }


}
