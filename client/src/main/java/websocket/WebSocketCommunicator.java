package websocket;

import chess.ChessGame;
import chess.ChessMove;
import chess.ChessPiece;
import chess.ChessPosition;
import client_responses_http.*;
import client_responses_ws.JoinGameResponseWS;
import com.google.gson.Gson;

import ui.Client;
import ui.ServerMessageObserver;
import webSocketMessages.ResponseException;
import webSocketMessages.userCommands.*;

import java.io.IOException;
import java.io.PrintStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Scanner;

import javax.websocket.*;

public class WebSocketCommunicator extends Endpoint {
    int port;
    Session session;
    ServerMessageObserver notificationHandler;
    HashMap<String,Integer> columnsByName = new HashMap<>();
    HashMap<Integer,ChessPiece.PieceType> promotionPieceTypes = new HashMap<>();  //for use in promotion
    Client myClient;

    public WebSocketCommunicator(int port, String url, Client me) throws ResponseException {
        this.port = port;
        myClient=me;
        try {
            //set up columns by name hash map
            columnsByName.put("a",1);
            columnsByName.put("b",2);
            columnsByName.put("c",3);
            columnsByName.put("d",4);
            columnsByName.put("e",5);
            columnsByName.put("f",6);
            columnsByName.put("g",7);
            columnsByName.put("h",8);

            promotionPieceTypes.put(1,ChessPiece.PieceType.ROOK);
            promotionPieceTypes.put(2,ChessPiece.PieceType.KNIGHT);
            promotionPieceTypes.put(3,ChessPiece.PieceType.BISHOP);
            promotionPieceTypes.put(4,ChessPiece.PieceType.QUEEN);
            promotionPieceTypes.put(5,ChessPiece.PieceType.KING);

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

    public Client getMyClient() {
        return myClient;
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
        if(joinGameResponseHttp.statusCode()==200 ) {
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

    public void makeMove(PrintStream out, Client client, WebSocketCommunicator webSocketCommunicator, String authToken, Integer gameID) {

        //make move
        //read in move
        String[] labels = {"startPosCol","startPosRow", "endPosCol", "endPosRow"};
        String[] positions = generalRepl(labels);
        Integer startPosCol=(columnsByName.containsKey(positions[0]))? columnsByName.get(positions[0]): -1;
        Integer startPosRow = Integer.parseInt(positions[1]);
        ChessPosition startPos= new ChessPosition(startPosRow,startPosCol);
        Integer endPosCol=(columnsByName.containsKey(positions[2]))? columnsByName.get(positions[2]): -1;
        Integer endPosRow = Integer.parseInt(positions[3]);
        ChessPosition endPos = new ChessPosition( endPosRow,endPosCol);
        ChessPiece.PieceType promotionPiece=null;

        //if piece in startPos is a pawn and about to promote
        if(client.getMostRecentGame().getBoard().getPiece(startPos)!=null && client.getMostRecentGame().getBoard().getPiece(startPos).getPieceType().equals(ChessPiece.PieceType.PAWN)) {
            if((client.getMostRecentGame().getBoard().getPiece(startPos).getTeamColor().equals(ChessGame.TeamColor.WHITE) &&
                    startPosRow==7)
                    || (client.getMostRecentGame().getBoard().getPiece(startPos).getTeamColor().equals(ChessGame.TeamColor.BLACK) &&
                    startPosRow==2)) {
                listPromotionPieces(out);
                Integer index = readInputNumber();
                promotionPiece=promotionPieceTypes.get(index);
            }
        }

        ChessMove move = new ChessMove(startPos,endPos,promotionPiece);
        MakeMove makeMove = new MakeMove(authToken,gameID,move);
        String makeMoveString = new Gson().toJson(makeMove);
        try {
            session.getBasicRemote().sendText(makeMoveString);  //send user command
        }
        catch (Exception e) {
            System.out.println("MakeMove user command NOT sent");
        }

        //get game to update somehow...
        //makeMove does not automatically send load game to root; try with other users
    }

    public void leave(String authToken, Integer gameID) {
        Leave leave = new Leave(authToken,gameID);
        String leaveString = new Gson().toJson(leave);
        try {
            System.out.println("Leave user command sent by root");
            session.getBasicRemote().sendText(leaveString);
        }
        catch(Exception e) {
            System.out.println("Leave user command NOT sent");
        }
    }

    public boolean resign(String authToken, Integer gameID) {
        Resign resign = new Resign(authToken,gameID);
        String resignString = new Gson().toJson(resign);
        try {
            System.out.println("Leave user command sent by root");
            session.getBasicRemote().sendText(resignString);
        }
        catch(Exception e) {
            System.out.println("Leave user command NOT sent");
            return false;
        }
        return true;
    }
    private String[] generalRepl(String[] inputLabels) {
        String[] inputParams=new String[inputLabels.length];
        Boolean validInput=false;
        for(int i=0; i<inputLabels.length;i++) {
            validInput=false;
            while (!validInput) {
                System.out.printf("%s:%n>>>",inputLabels[i]);
                Scanner scanner = new Scanner(System.in);
                String line = scanner.nextLine();
                String[] words = line.split(" ");
                if (!words[0].isEmpty()) {
                    inputParams[i] = words[0];
                    validInput = true;
                } else {  //invalid input (empty string)
                    System.out.printf("invalid %s%n",inputLabels[i]);
                }
            }
        }
        return inputParams;
    }



    private  int readInputNumber() {
        int selection = 8;
        Scanner scanner = new Scanner(System.in);
        String line = scanner.nextLine();
        String[]  numbers = line.split(" ");
        if(numbers.length==0) {
            selection=8;  //default
        }
        else if(numbers[0].length()==0) {
            selection=9;
        }
        else {
            selection = Integer.parseInt(numbers[0]);  //just use first number, ignore others
        }
        return selection;
    }

    private void listPromotionPieces(PrintStream out) {
        out.printf("Choose your promotion piece type by typing in the corresponding number:\n");
        for(Integer index:promotionPieceTypes.keySet()) {
            out.printf("%d: %s\n",index,promotionPieceTypes.get(index));
        }
        out.printf(">>>");
    }

    public Session getSession() {
        return session;
    }


    @Override
    public void onOpen(Session session, javax.websocket.EndpointConfig endpointConfig) {

    }

}
