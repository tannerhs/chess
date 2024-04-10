package handlers;

//import org.eclipse.jetty.websocket.api.Session;
//import org.eclipse.jetty.server.session.Session;
import chess.ChessGame;
import chess.ChessPosition;
import com.google.gson.Gson;
import dataAccess.*;
import model.GameData;
import org.eclipse.jetty.websocket.api.annotations.*;
import webSocketMessages.serverMessages.*;
import webSocketMessages.serverMessages.Error;
import webSocketMessages.userCommands.*;
import websocket.WebSocketSessions;
//import javax.websocket.*;
import org.eclipse.jetty.websocket.api.Session;
//import webSocketMessages.Notification;

import java.util.HashMap;
import java.util.Map;

import static chess.ChessGame.TeamColor.BLACK;
import static chess.ChessGame.TeamColor.WHITE;

@WebSocket
public class WebSocketHandler {
    WebSocketSessions connections= new WebSocketSessions();
    Map<Session,String> sessions = new HashMap<Session,String>();
    GameDAO gameDAO = new DatabaseGameDAO();  //FIXME!!!  need these kept in common and passed from server?
    AuthDAO authDAO = new DatabaseAuthDAO();
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
        if(authDAO.getAuth(authToken)!=null) {
            username = authDAO.getAuth(authToken).username();
        }

        //deserialize specific command:
        System.out.printf("commandType: %s\n",userGameCommand.getCommandType());
        switch(userGameCommand.getCommandType()) {
            case JOIN_OBSERVER:
                System.out.println("JOIN_OBSERVER case reached");
                JoinObserver joinObserver= new Gson().fromJson(message, JoinObserver.class);
                int gameID = joinObserver.getGameID();
                GameData myGameData = gameDAO.getGameByID(gameID);
                ChessGame myGame;
                //if null session...what do I do? TODO
                if(authToken==null|| authDAO.getAuth(authToken)==null ||
                        myGameData==null || myGameData.game()==null) {  //403, spot taken already
                    System.out.println("spot taken");
                    String sendMessage1=new Gson().toJson(new Error("NOPE! That color is already taken."));
                    System.out.printf("sendMessage: %s\n",sendMessage1);
                    session.getRemote().sendString(sendMessage1);  //error message, send to root only
                    break;
                }
                else {
                    String sendMessage2=new Gson().toJson(new LoadGame(new LoadGameObject(myGameData,WHITE,null)));
                    System.out.printf("sendMessage: %s\n",sendMessage2);
//                session.getBasicRemote().sendText(sendMessage);
                    session.getRemote().sendString(sendMessage2);
                    //send load_game to root
                    //now broadcast notification to everyone playing or observing this game
                    username = authDAO.getAuth(authToken).username();
                    String notificationMessage = username+" joined the game as an observer.\n";
                    Notification notification = new Notification(notificationMessage);
                    connections.addSessionToGame(joinObserver.getGameID(),authToken,session);
                    connections.broadcast(gameID,notification,null,authToken);  //otherTeamAuthToken only for load game
                    break;
                }


            case JOIN_PLAYER:
                System.out.println("JOIN_PLAYER case reached");

                JoinPlayer joinPlayer= new Gson().fromJson(message, JoinPlayer.class);
                int gameID2 = joinPlayer.getGameID();
                GameData myGameData2 = gameDAO.getGameByID(gameID2);
                GameData oldGameData=null;  //used in Leave and Resign

                //fixme, what does auth return when null?
                //what about null session?
                if(joinPlayer.getPlayerColor()==null || authToken==null|| authDAO.getAuth(authToken)==null ||
                        myGameData2==null || myGameData2.game()==null || joinPlayer.getPlayerColor()==null
                        || (joinPlayer.getPlayerColor()== WHITE && (myGameData2.whiteUsername()==null|| !myGameData2.whiteUsername().equals(authDAO.getAuth(authToken).username())))
                        || (joinPlayer.getPlayerColor()==ChessGame.TeamColor.BLACK && (myGameData2.blackUsername()==null ||!myGameData2.blackUsername().equals(authDAO.getAuth(authToken).username())))) {  //403, spot taken already

                    System.out.println("spot taken");
                    String sendMessage1=new Gson().toJson(new Error("NOPE! That color is already taken."));
                    System.out.printf("sendMessage: %s\n",sendMessage1);
                    session.getRemote().sendString(sendMessage1);  //error message, send to root only
                }
                //send load_game to root
                else {
                    username = authDAO.getAuth(authToken).username();
                    ChessGame.TeamColor usernameColor = joinPlayer.getPlayerColor();
                    ChessGame myGame2 = myGameData2.game();
                    String otherTeamAuthToken=null;
                    //if other player not null, get
                    if((usernameColor.equals(BLACK) && authDAO.getAuth(myGameData2.whiteUsername())!=null)) {
                        otherTeamAuthToken = authDAO.getAuth(myGameData2.whiteUsername()).authToken();
                    }
                    else if (usernameColor.equals(WHITE) && authDAO.getAuth(myGameData2.blackUsername())!=null) {
                        otherTeamAuthToken = authDAO.getAuth(myGameData2.blackUsername()).authToken();
                    }

                    //if successful message...
                    String sendMessage2=new Gson().toJson(new LoadGame(new LoadGameObject(myGameData2,usernameColor,null)));
                    System.out.printf("sendMessage: %s\n",sendMessage2);
//                session.getBasicRemote().sendText(sendMessage);
                    session.getRemote().sendString(sendMessage2);

                    //now broadcast notification to everyone playing or observing this game
                    String username2 = authDAO.getAuth(authToken).username();
                    String notificationMessage2 = username2+" joined the game as the "+joinPlayer.getPlayerColor().toString()+" player.\n";
                    Notification notification2 = new Notification(notificationMessage2);
                    connections.addSessionToGame(joinPlayer.getGameID(),authToken,session);
                    connections.broadcast(gameID2,notification2,null,authToken);  //otherTeamAuthToken only for load game
                }
                break;
            case MAKE_MOVE:
                System.out.println("MAKE_MOVE case reached");

                MakeMove makeMove = new Gson().fromJson(message,MakeMove.class);
                username = authDAO.getAuth(authToken).username();
                Integer gameID3 = makeMove.getGameID();
                GameData gameData3 = gameDAO.getGameByID(gameID3);
                ChessGame game = gameData3.game();

                //determine actual color in database
                ChessGame.TeamColor userColor;
                if(username.equals(gameData3.whiteUsername())) {
                    userColor = WHITE;
                }
                else if (username.equals(gameData3.blackUsername())) {
                    userColor= BLACK;
                }
                else {
                    userColor=null;
                }
                ChessPosition startPos = makeMove.getMove().getStartPosition();

                ChessGame.TeamColor moveColor=null;
                if(game.getBoard().getPiece(startPos)!=null) {
                    moveColor = game.getBoard().getPiece(startPos).getTeamColor();
                }

                System.out.printf("userColor: %s\n",userColor);
                System.out.printf("moveColor: %s\n",moveColor);

                //compare userColor to color indicated in move and current team in game or move is invalid
                if(userColor==null || moveColor==null
                        || !userColor.equals(game.getTeamTurn())
                        || !moveColor.equals(userColor)
                        || game.validMoves(startPos)==null
                        || !game.validMoves(startPos).contains(makeMove.getMove())
                        || game.isGameOver()
                        ) {
                    //print out why invalid move
                    System.out.println("invalid move");

                    if(userColor==null) {
                        System.out.println("userColor null");
                    }
                    else if(moveColor==null) {
                        System.out.println("moveColor null");
                    }
                    else if (!userColor.equals(game.getTeamTurn())) {
                        System.out.println("!userColor.equals(game.getTeamTurn())");
                    }
                    else if(!moveColor.equals(userColor)) {
                        System.out.println("!moveColor.equals(userColor)");
                        System.out.printf("userColor: %s\n",userColor.toString());
                        System.out.printf("moveColor: %s\n",moveColor.toString());
                    }
                    else if(game.validMoves(startPos)==null) {
                        System.out.println("game.validMoves(startPos)==null");
                    }
                    else if (!game.validMoves(startPos).contains(makeMove.getMove())) {
                        System.out.println("!game.validMoves(startPos).contains(makeMove.getMove())");
                    }
                    else if (game.isGameOver()) {
                        System.out.println("game.isGameOver()");
                    }

                    String sendMessage1=new Gson().toJson(new Error("NOPE! Invalid move."));
                    System.out.printf("sendMessage: %s\n",sendMessage1);
                    session.getRemote().sendString(sendMessage1);  //error message, send to root only
                }
                else {  //if valid move...
                    //make move and load game for all users observing or playing game
                    //if successful message...
//                    username = auth.getAuth(authToken).username();
//                    ChessGame.TeamColor usernameColor = makeMove.getPlayerColor();
//                    ChessGame myGame2 = gameData3.game();

                    String otherTeamAuthToken=null;
                    //if other player not null, get
                    if((userColor.equals(BLACK) && authDAO.getAuth(gameData3.whiteUsername())!=null)) {
                        otherTeamAuthToken = authDAO.getAuth(gameData3.whiteUsername()).authToken();
                    }
                    else if (userColor.equals(WHITE) && authDAO.getAuth(gameData3.blackUsername())!=null) {
                        otherTeamAuthToken = authDAO.getAuth(gameData3.blackUsername()).authToken();
                    }

                    //loadGame send
                    gameData3.game().makeMove(makeMove.getMove());
                    gameDAO.updateGame(gameData3);
                    //update game in gameData3
                    //games.makeMove(game);
                    gameData3.game().setTeamTurn((userColor==WHITE)?BLACK:WHITE);
                    String sendMessage2=new Gson().toJson(new LoadGame(new LoadGameObject(gameData3,userColor,otherTeamAuthToken)));
                    System.out.printf("sendMessage: %s\n",sendMessage2);
                    session.getRemote().sendString(sendMessage2);  //reload game for root
                    connections.broadcast(gameID3, new LoadGame(new LoadGameObject(gameData3,userColor,otherTeamAuthToken)),otherTeamAuthToken,authToken);


                    //now broadcast notification to everyone else playing or observing this game
                    String notificationMessage1 = username + "made the move" +makeMove.getMove().toString()+ "---\n";
                    Notification notification1=new Notification(notificationMessage1);
                    connections.broadcast(gameID3,notification1,null,authToken);  //otherTeamAuthToken only for load game
                }
                break;
            case LEAVE:
                //call
                Leave leave = new Gson().fromJson(message,Leave.class);
                gameID = leave.getGameID();
                 connections.removeSession(gameID,authToken);

                //remove username from game and update white/black username in game dao if you are a player
                oldGameData =  gameDAO.getGameByID(leave.getGameID());
                String whiteUsername= oldGameData.whiteUsername();
                String blackUsername = oldGameData.blackUsername();
                if(username.equals(whiteUsername))  {
                    whiteUsername=null;
                }
                else if(username.equals(blackUsername)) {
                    blackUsername=null;
                }
                GameData updatedGameData = new GameData(oldGameData.gameID(),whiteUsername,blackUsername, oldGameData.gameName(), oldGameData.game());
                gameDAO.updateGame(updatedGameData);
                //go back to postlogin menu-- taken care of in GameUI
                //notify root of successful leave action
                session.getRemote().sendString(new Gson().toJson(new Notification("You resigned from the game.")));
                //send notification to everyone else
                String notificationMessage = username+"left the game.";
                Notification leaveNotification = new Notification(notificationMessage);
                connections.broadcast(gameID,leaveNotification,null,authToken);

                break;
            case RESIGN:
                Resign resign = new Gson().fromJson(message,Resign.class);
                gameID = resign.getGameID();
                oldGameData =  gameDAO.getGameByID(gameID);
                whiteUsername= oldGameData.whiteUsername();
                blackUsername = oldGameData.blackUsername();
                System.out.printf("username: %s\n",username);
                System.out.printf("whiteUsername: %s\n",whiteUsername);
                System.out.printf("blackUsername: %s\n",blackUsername);
                if((username.equals(whiteUsername) || username.equals(blackUsername)) && !oldGameData.game().isGameOver())  {
                    connections.removeSession(gameID,authToken);

                    //user does NOT leave the game
                    //mark game as over so no moves can be made!
                    oldGameData =  gameDAO.getGameByID(resign.getGameID());
                    ChessGame endedGame = oldGameData.game();
                    endedGame.setGameOver(true);
                    gameDAO.updateGame(new GameData(oldGameData.gameID(), oldGameData.whiteUsername(), oldGameData.blackUsername(), oldGameData.gameName(), endedGame));

                    //notify root of successful resign
                    session.getRemote().sendString(new Gson().toJson(new Notification("You resigned from the game.")));
                    //notify other users
                    String notificationMessage2 = username+"resigned from the game.";
                    Notification resignNotification = new Notification(notificationMessage2);
                    connections.broadcast(gameID,resignNotification,null,authToken);
                }
                else {
                    //send error message
                    String errorMessage = "NOPE!  Observers can't resign from a game.  Please select leave instead.";
                    Error error = new Error(errorMessage);
                    session.getRemote().sendString(new Gson().toJson(error));  //error message, send to root only
                }


                break;
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
