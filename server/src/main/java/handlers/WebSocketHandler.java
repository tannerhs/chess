package handlers;

//import org.eclipse.jetty.websocket.api.Session;
//import org.eclipse.jetty.server.session.Session;
import chess.ChessGame;
import chess.ChessMove;
import chess.ChessPiece;
import chess.ChessPosition;
import com.google.gson.Gson;
import dataAccess.*;
import model.GameData;
import org.eclipse.jetty.websocket.api.annotations.*;
import server.Server;
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
    UserDAO userDAO = new DatabaseUserDAO();
    Server myServer;

    public WebSocketHandler(GameDAO gameDAO,AuthDAO authDAO, UserDAO userDAO, Server myServer) {
        this.gameDAO=gameDAO;
        this.authDAO=authDAO;
        this.userDAO=userDAO;
        this.myServer=myServer;
        System.out.println("WebSocketHandler constructor reached");
    }

    public String toChar(ChessGame.TeamColor team,ChessPiece.PieceType type) {
        if(team==null || type==null) {
            return "x";
        }
        else {
            if(type== ChessPiece.PieceType.PAWN) {
                return "";
            }
            else if(type== ChessPiece.PieceType.BISHOP) {
                return "B";
            }
            else if (type== ChessPiece.PieceType.KNIGHT) {
                return "N";
            }
            else if (type== ChessPiece.PieceType.ROOK) {
                return "R";
            }
            else if (type== ChessPiece.PieceType.KING) {
                return "K";
            }
            else if (type== ChessPiece.PieceType.QUEEN) {
                return "Q";
            }
            return "x";
        }
    }


    @OnWebSocketMessage
    public void onMessage(Session session, String message) throws Exception {
        //if(session not valid)
        //if player tries to join as color they are not already registered as
        gameDAO= myServer.getGamesDAO();
        authDAO= myServer.getAuthDAO();
        userDAO= myServer.getUsersDAO();


        System.out.printf("Received: %s", message);
        //deserialize user game commands, which edits data more directly in the case of makeMove by calling DAO
        //deserialize UserGameCommand to get command type
        UserGameCommand userGameCommand = new Gson().fromJson(message,UserGameCommand.class);
        String authToken = userGameCommand.getAuthString();
        String username="rando";
        String whiteUsername=null;
        String blackUsername=null;
        String sendMessage=null;
        ChessGame.TeamColor userColor=null;
        Integer gameID=-1;
        GameData oldGameData=null;
        String notificationMessage = "Uninitialized";

        if(authDAO.getAuth(authToken)!=null) {
            username = authDAO.getAuth(authToken).username();
        }

        //deserialize specific command:
        System.out.printf("commandType: %s\n",userGameCommand.getCommandType());
        switch(userGameCommand.getCommandType()) {
            case JOIN_OBSERVER:
                System.out.println("JOIN_OBSERVER case reached");
                JoinObserver joinObserver= new Gson().fromJson(message, JoinObserver.class);
                gameID = joinObserver.getGameID();
                GameData myGameData = gameDAO.getGameByID(gameID);
                ChessGame myGame;
                GameData gameData=null;
                //if null session...what do I do? TODO
                if(authToken==null|| authDAO.getAuth(authToken)==null ||
                        myGameData==null || myGameData.game()==null) {  //403, spot taken already
                    System.out.println("spot taken");
                    sendMessage=new Gson().toJson(new Error("NOPE! That color is already taken."));
                    System.out.printf("sendMessage: %s\n",sendMessage);
                    session.getRemote().sendString(sendMessage);  //error message, send to root only
//                    break;
                }
                else {
                    sendMessage=new Gson().toJson(new LoadGame(new LoadGameObject(myGameData,WHITE,null)));
                    System.out.printf("sendMessage: %s\n",sendMessage);
//                session.getBasicRemote().sendText(sendMessage);
                    session.getRemote().sendString(sendMessage);
                    //send load_game to root
                    //now broadcast notification to everyone playing or observing this game
                    username = authDAO.getAuth(authToken).username();
                    notificationMessage = username+" joined the game as an observer.\n";
                    Notification notification = new Notification(notificationMessage);
                    connections.addSessionToGame(joinObserver.getGameID(),authToken,session);
                    connections.broadcast(gameID,notification,null,authToken);  //otherTeamAuthToken only for load game
//                    break;
                }
                break;

            case JOIN_PLAYER:
                System.out.println("JOIN_PLAYER case reached");

                JoinPlayer joinPlayer= new Gson().fromJson(message, JoinPlayer.class);
                gameID = joinPlayer.getGameID();
                GameData myGameData2 = gameDAO.getGameByID(gameID);
                oldGameData=null;  //used in Leave and Resign

                //fixme, what does auth return when null?
                //what about null session?
                if(joinPlayer.getPlayerColor()==null || authToken==null|| authDAO.getAuth(authToken)==null ||
                        myGameData2==null || myGameData2.game()==null || joinPlayer.getPlayerColor()==null
                        || (joinPlayer.getPlayerColor()== WHITE && (myGameData2.whiteUsername()==null|| !myGameData2.whiteUsername().equals(authDAO.getAuth(authToken).username())))
                        || (joinPlayer.getPlayerColor()==ChessGame.TeamColor.BLACK && (myGameData2.blackUsername()==null ||!myGameData2.blackUsername().equals(authDAO.getAuth(authToken).username())))) {  //403, spot taken already

                    System.out.println("spot taken");
                    sendMessage=new Gson().toJson(new Error("NOPE! That color is already taken."));
                    System.out.printf("sendMessage: %s\n",sendMessage);
                    session.getRemote().sendString(sendMessage);  //error message, send to root only
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
                    sendMessage=new Gson().toJson(new LoadGame(new LoadGameObject(myGameData2,usernameColor,null)));
                    System.out.printf("sendMessage: %s\n",sendMessage);
//                session.getBasicRemote().sendText(sendMessage);
                    session.getRemote().sendString(sendMessage);

                    //now broadcast notification to everyone playing or observing this game
                    username = authDAO.getAuth(authToken).username();
                    notificationMessage = username+" joined the game as the "+joinPlayer.getPlayerColor().toString()+" player.\n";
                    Notification notification2 = new Notification(notificationMessage);
                    connections.addSessionToGame(joinPlayer.getGameID(),authToken,session);
                    connections.broadcast(gameID,notification2,null,authToken);  //otherTeamAuthToken only for load game
                }
                break;
            case MAKE_MOVE:
                System.out.println("MAKE_MOVE case reached");

                MakeMove makeMove = new Gson().fromJson(message,MakeMove.class);
                username = authDAO.getAuth(authToken).username();
                gameID = makeMove.getGameID();
                gameData = gameDAO.getGameByID(gameID);
                ChessGame game = gameData.game();

                //determine actual color in database
                if(username.equals(gameData.whiteUsername())) {
                    userColor = WHITE;
                }
                else if (username.equals(gameData.blackUsername())) {
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

                    sendMessage=new Gson().toJson(new Error("NOPE! Invalid move."));
                    System.out.printf("sendMessage: %s\n",sendMessage);
                    session.getRemote().sendString(sendMessage);  //error message, send to root only
                }
                else {  //if valid move...
                    //make move and load game for all users observing or playing game
                    //if successful message...

                    String otherTeamAuthToken=null;
                    String otherTeamUsername=null;
                    //if other player not null, get
                    if(userColor.equals(BLACK) && authDAO.getAuthByUsername(gameData.whiteUsername())!=null) {
                        System.out.println("hallelujha");
                        otherTeamAuthToken = authDAO.getAuthByUsername(gameData.whiteUsername()).authToken();
                        otherTeamUsername= gameData.whiteUsername();
                    }
                    else if (userColor.equals(WHITE) && authDAO.getAuthByUsername(gameData.blackUsername())!=null) {
                        System.out.println("hallelujah for reals");
                        otherTeamAuthToken = authDAO.getAuthByUsername(gameData.blackUsername()).authToken();
                        otherTeamUsername= gameData.blackUsername();
                    }
                    else {
                        System.out.println("Make Move else clause");
                        System.out.printf("userColor: %s\n",userColor);
                        System.out.printf("blackUsername: %s\n",gameData.blackUsername());
                        System.out.printf("authDAO.getAuth(gameData.blackUsername()): %s\n",authDAO.getAuth(gameData.blackUsername()));
                        System.out.printf("whiteUsername: %s\n",gameData.whiteUsername());
                        System.out.printf("authDAO.getAuth(gameData.whiteUsername()): %s\n",authDAO.getAuth(gameData.whiteUsername()));
                        System.out.printf("username: %s\n",username);
                    }
                    ChessPiece piece = game.getBoard().getPiece(makeMove.getMove().getStartPosition());  //get piece type before making move

                    //loadGame send
                    gameData.game().makeMove(makeMove.getMove());
                    gameDAO.updateGame(gameData);
                    //update game in gameData3
                    gameData.game().setTeamTurn((userColor==WHITE)?BLACK:WHITE);  //redundant
                    sendMessage=new Gson().toJson(new LoadGame(new LoadGameObject(gameData,userColor,otherTeamAuthToken)));
                    System.out.printf("sendMessage: %s\n",sendMessage);
                    session.getRemote().sendString(sendMessage);  //reload game for root
                    connections.broadcast(gameID, new LoadGame(new LoadGameObject(gameData,userColor,otherTeamAuthToken)),otherTeamAuthToken,authToken);

                    //now broadcast notification to everyone else playing or observing this game
                    String pieceString="?";
                    if(piece!=null) {
                        pieceString = this.toChar(game.getTeamTurn(), piece.getPieceType());
                    }
                    ChessMove makeYourMove = makeMove.getMove();
                    notificationMessage = username + " made the move "+pieceString+ makeYourMove.toString()+ "\n";
                    Notification notification1=new Notification(notificationMessage);
                    connections.broadcast(gameID,notification1,null,authToken);  //otherTeamAuthToken only for load game

                    if(game.isInCheckmate(game.getTeamTurn())) {  //if next player is left in Check or Checkmate print it out w/ notification
                        notificationMessage ="You put " + otherTeamUsername+ " in checkmate";
                        session.getRemote().sendString(new Gson().toJson(new Notification(notificationMessage)));
                        connections.broadcast(gameID,new Notification(username+" put "+otherTeamUsername  +" in checkmate."),null,authToken);
                    }
                    else if(game.isInCheck(game.getTeamTurn())) {  //if next player is left in Check or Checkmate print it out w/ notification
                        notificationMessage ="You put " + otherTeamUsername+ " in check";
                        session.getRemote().sendString(new Gson().toJson(new Notification(notificationMessage)));
                        connections.broadcast(gameID,new Notification(username+" put "+otherTeamUsername+" in check."),null,authToken);
                    }

                    else if(game.isInStalemate(game.getTeamTurn())) {  //if next player is left in Check or Checkmate print it out w/ notification
                        notificationMessage ="Stalemate";
                        session.getRemote().sendString(new Gson().toJson(new Notification(notificationMessage)));
                        connections.broadcast(gameID,new Notification("Stalemate"),null,authToken);
                    }
                }
                break;
            case LEAVE:
                System.out.println("LEAVE case reached");
                //call
                Leave leave = new Gson().fromJson(message,Leave.class);
                gameID = leave.getGameID();
                 connections.removeSession(gameID,authToken);



                //remove username from game and update white/black username in game dao if you are a player
                oldGameData =  gameDAO.getGameByID(leave.getGameID());
                whiteUsername= oldGameData.whiteUsername();
                blackUsername = oldGameData.blackUsername();

                System.out.printf("username: %s\n",username);
                System.out.printf("whiteUsername: %s\n",whiteUsername);
                System.out.printf("blackUsername: %s\n",blackUsername);
                if(username.equals(whiteUsername))  {
                    System.out.println("remove whiteUsername");
                    gameDAO.removeWhiteUsername(oldGameData.gameID());
                    whiteUsername=null;
                }
                else if(username.equals(blackUsername)) {
                    System.out.println("remove blackUsername");
                    gameDAO.removeBlackUsername(oldGameData.gameID());
                    blackUsername=null;
                }

//                GameData updatedGameData = new GameData(oldGameData.gameID(),whiteUsername,blackUsername, oldGameData.gameName(), oldGameData.game());

                //go back to postlogin menu-- taken care of in GameUI
                //notify root of successful leave action
                session.getRemote().sendString(new Gson().toJson(new Notification("You left the game.")));
                //send notification to everyone else
                notificationMessage = username+" left the game.";
                Notification leaveNotification = new Notification(notificationMessage);
                connections.broadcast(gameID,leaveNotification,null,authToken);

                break;
            case RESIGN:
                System.out.println("RESIGN case reached");
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
                    notificationMessage = username+" resigned from the game.";
                    Notification resignNotification = new Notification(notificationMessage);
                    connections.broadcast(gameID,resignNotification,null,authToken);
                }
                else {
                    //send error message
                    String errorMessage = "NOPE!  Observers can't resign from a game, and you can't resign from a finished game.  Please select leave instead.";
                    Error error = new Error(errorMessage);
                    session.getRemote().sendString(new Gson().toJson(error));  //error message, send to root only
                }


                break;
        }
        //depending on UserGameCommand message type, do stuff

        //send server messages, receive client messages
        //send back server message if needed
    }


    @OnWebSocketError
    public void onError(Throwable throwable) {
        //
    }




}
