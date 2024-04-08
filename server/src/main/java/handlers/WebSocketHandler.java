package handlers;

//import org.eclipse.jetty.websocket.api.Session;
//import org.eclipse.jetty.server.session.Session;
import chess.ChessGame;
import chess.ChessPosition;
import com.google.gson.Gson;
import dataAccess.*;
import model.GameData;
import org.eclipse.jetty.websocket.api.annotations.*;
import webSocketMessages.serverMessages.Error;
import webSocketMessages.serverMessages.LoadGame;
import webSocketMessages.serverMessages.Notification;
import webSocketMessages.serverMessages.ServerMessage;
import webSocketMessages.userCommands.JoinObserver;
import webSocketMessages.userCommands.JoinPlayer;
import webSocketMessages.userCommands.MakeMove;
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
        //if(session not valid)
        //if player tries to join as color they are not already registered as


        System.out.printf("Received: %s", message);
        //deserialize user game commands, which edits data more directly in the case of makeMove by calling DAO
        //deserialize UserGameCommand to get command type
        UserGameCommand userGameCommand = new Gson().fromJson(message,UserGameCommand.class);
        String authToken = userGameCommand.getAuthString();
        String username="rando";

        //deserialize specific command:
        System.out.printf("commandType: %s\n",userGameCommand.getCommandType());
        switch(userGameCommand.getCommandType()) {
            case JOIN_OBSERVER:
                System.out.println("JOIN_OBSERVER case reached");
                JoinObserver joinObserver= new Gson().fromJson(message, JoinObserver.class);
                int gameID = joinObserver.getGameID();
                GameData myGameData = games.getGameByID(gameID);
                ChessGame myGame;
                //if null session...what do I do? TODO
                if(authToken==null||auth.getAuth(authToken)==null ||
                        myGameData==null || myGameData.game()==null) {  //403, spot taken already
                    System.out.println("spot taken");
                    String sendMessage2=new Gson().toJson(new Error("NOPE! That color is already taken."));
                    System.out.printf("sendMessage: %s\n",sendMessage2);
                    session.getRemote().sendString(sendMessage2);  //error message, send to root only
                    break;
                }
                else {
                    String sendMessage=new Gson().toJson(new LoadGame(myGameData.game()));
                    System.out.printf("sendMessage: %s\n",sendMessage);
//                session.getBasicRemote().sendText(sendMessage);
                    session.getRemote().sendString(sendMessage);
                    //send load_game to root
                    //now broadcast notification to everyone playing or observing this game
                    username = auth.getAuth(authToken).username();
                    String notificationMessage = username+" joined the game as an observer.\n";
                    Notification notification = new Notification(notificationMessage);
                    connections.addSessionToGame(joinObserver.getGameID(),authToken,session);
                    connections.broadcast(gameID,notification,authToken);  //FIXME, notifications not working yet
                    break;
                }


            case JOIN_PLAYER:
                System.out.println("JOIN_PLAYER case reached");

                JoinPlayer joinPlayer= new Gson().fromJson(message, JoinPlayer.class);
                int gameID2 = joinPlayer.getGameID();
                GameData myGameData2 = games.getGameByID(gameID2);

                //fixme, what does auth return when null?
                //what about null session?
                if(joinPlayer.getPlayerColor()==null || authToken==null||auth.getAuth(authToken)==null ||
                        myGameData2==null || myGameData2.game()==null || joinPlayer.getPlayerColor()==null
                        || (joinPlayer.getPlayerColor()== ChessGame.TeamColor.WHITE && !myGameData2.whiteUsername().equals(auth.getAuth(authToken).username()))
                        || (joinPlayer.getPlayerColor()==ChessGame.TeamColor.BLACK && !myGameData2.blackUsername().equals(auth.getAuth(authToken).username()))) {  //403, spot taken already

                    System.out.println("spot taken");
                    String sendMessage2=new Gson().toJson(new Error("NOPE! That color is already taken."));
                    System.out.printf("sendMessage: %s\n",sendMessage2);
                    session.getRemote().sendString(sendMessage2);  //error message, send to root only
                }
                //send load_game to root
                else {
                    username = auth.getAuth(authToken).username();

                    ChessGame myGame2 = myGameData2.game();

                    //if successful message...
                    String sendMessage2=new Gson().toJson(new LoadGame(myGame2));
                    System.out.printf("sendMessage: %s\n",sendMessage2);
//                session.getBasicRemote().sendText(sendMessage);
                    session.getRemote().sendString(sendMessage2);

                    //now broadcast notification to everyone playing or observing this game
                    String username2 = auth.getAuth(authToken).username();
                    String notificationMessage2 = username2+" joined the game as a player.\n";
                    Notification notification2 = new Notification(notificationMessage2);
                    connections.addSessionToGame(joinPlayer.getGameID(),authToken,session);
                    connections.broadcast(gameID2,notification2,authToken);  //FIXME, notifications not working yet
                }
                break;
            case MAKE_MOVE:
                System.out.println("MAKE_MOVE case reached");
                MakeMove makeMove = new Gson().fromJson(message,MakeMove.class);
                username = auth.getAuth(authToken).username();
                Integer gameID3 = makeMove.getGameID();
                GameData gameData = games.getGameByID(gameID3);
                ChessGame game = gameData.game();

                //determine actual color in database
                ChessGame.TeamColor userColor;
                if(gameData.whiteUsername().equals(username)) {
                    userColor = ChessGame.TeamColor.WHITE;
                }
                else if (gameData.blackUsername().equals(username)) {
                    userColor= ChessGame.TeamColor.BLACK;
                }
                else {
                    userColor=null;
                }
                ChessPosition startPos = makeMove.getMove().getStartPosition();
                ChessGame.TeamColor moveColor = game.getBoard().getPiece(startPos).getTeamColor();

                //compare userColor to color indicated in move and current team in game
                if(userColor==null || !userColor.equals(game.getTeamTurn()) || !moveColor.equals(userColor)) {
                    //TODO send error message
                    //
                }
                break;
            case LEAVE:
                //
                break;
            case RESIGN:
                break;
            //TODO finish case statement
        }
        //depending on UserGameCommand message type, do stuff


        //send server messages, receive client messages
        //send back server message if needed
    }

//    Boolean validJoinGameRequest(Session session, int gameID) {
//        Boolean valid = false;
//        //get auth token associated with it and game and see if that player has successfully done the
//        //http join gme
//        //call method to do this: connections.get(gameID);
//        //get color
//        //get username with authToken, then check to make sure it is in game object in right color
//        return valid;
//    }

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
