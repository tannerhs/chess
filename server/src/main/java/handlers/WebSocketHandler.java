package handlers;

//import org.eclipse.jetty.websocket.api.Session;
//import org.eclipse.jetty.server.session.Session;
import chess.ChessGame;
import com.google.gson.Gson;
import org.eclipse.jetty.websocket.api.annotations.*;
import org.eclipse.jetty.websocket.api.*;
import webSocketMessages.serverMessages.LoadGame;
import webSocketMessages.serverMessages.ServerMessage;
import webSocketMessages.userCommands.JoinPlayer;
import webSocketMessages.userCommands.UserGameCommand;

import java.io.IOException;

import static webSocketMessages.userCommands.UserGameCommand.CommandType.JOIN_PLAYER;

@WebSocket
public class WebSocketHandler {
    //
    @OnWebSocketMessage
    public void onMessage(Session session, String message) throws Exception {
        System.out.printf("Received: %s", message);
        //deserialize user game commands, which edits data more directly in the case of makeMove by calling DAO
        //deserialize UserGameCommand to get command type
        UserGameCommand userGameCommand = new Gson().fromJson(message,UserGameCommand.class);
        //deserialize specific command:
        switch(userGameCommand.getCommandType()) {
            case JOIN_PLAYER: {
                JoinPlayer joinPlayer = new Gson().fromJson(message,JoinPlayer.class);
                joinObserver(session,joinPlayer);
                break;
            }
            //TODO finish case statement
        }
        //depending on UserGameCommand message type, do stuff
        ChessGame myGame = new ChessGame();
        String serverMessage = new Gson().toJson(new LoadGame(ServerMessage.ServerMessageType.LOAD_GAME,myGame));

        //send server messages, receive client messages
        session.getRemote().sendString(serverMessage);
        //send back server message if needed
    }

    @OnWebSocketConnect
    public void onConnect(Session session) {
        //keep track of sessions in session manager class, store sessions form onMessage in session map
    }

    public void joinObserver(Session session, JoinPlayer command) throws IOException {
        //send proper message and take care of stuff
        ChessGame myGame = new ChessGame();
        String message=new Gson().toJson(new LoadGame(ServerMessage.ServerMessageType.LOAD_GAME,myGame));
        session.getRemote().sendString(message);
    }


    @OnWebSocketClose
    public void onClose(int i, String message) {  //pass session in causes error...
        //remove stuff from map
    }


}
