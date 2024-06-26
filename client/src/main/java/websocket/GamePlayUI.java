package websocket;

import chess.ChessGame;
import chess.ChessMove;
import chess.ChessPiece;
import chess.ChessPosition;
import com.google.gson.Gson;
import ui.Client;
import ui.DrawChessBoard;
import webSocketMessages.userCommands.MakeMove;

import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Scanner;

import static chess.ChessGame.TeamColor.WHITE;
import static ui.EscapeSequences.SET_BG_COLOR_BLACK;
import static ui.EscapeSequences.SET_TEXT_COLOR_WHITE;

public class GamePlayUI {
    String authToken;
    //pass in game itself
    Integer gameID;
    ChessGame updatedGame;

    HashMap<String,Integer> columnsByName = new HashMap<>();


    public GamePlayUI(String authToken, Integer gameID) {
        this.authToken=authToken;
        this.gameID=gameID;

        columnsByName.put("a",1);
        columnsByName.put("b",2);
        columnsByName.put("c",3);
        columnsByName.put("d",4);
        columnsByName.put("e",5);
        columnsByName.put("f",6);
        columnsByName.put("g",7);
        columnsByName.put("h",8);
    }
    public void gamePlayMenu(PrintStream out, Client client, WebSocketCommunicator webSocketCommunicator, ChessGame.TeamColor currentUserColor) {
        //while command is not leave,
        Boolean observer = (currentUserColor==null);
        out.print(SET_TEXT_COLOR_WHITE);
        out.print(SET_BG_COLOR_BLACK);
//        Boolean validInput=false;
        Boolean leave = false;
        int selection = 8;
        out.printf("Welcome to the gameplay menu! Press 1 to see the options.\n");
        while (!leave) {  //prelogin page
            leave = false;
            out.printf(">>>");
            selection = client.readInputNumber();
            switch (selection) {
                case 1:
                    gamePlayHelpMenu(out);  //print it out
                    break;
                case 2:
                    //redraw
                    System.out.printf("currentUserColor: %s\n",currentUserColor);
                    //get current user color:
                    ChessGame.TeamColor redrawTeamColor = (currentUserColor!=null)? currentUserColor:WHITE;
                    System.out.printf("redrawTeamColor: %s\n",redrawTeamColor);
                    client.drawMostRecentBoard(out, redrawTeamColor);
                    break;
                case 3:
                    //leave
                    System.out.println("Leaving game");
                    webSocketCommunicator.leave(authToken,gameID);
                    leave=true;
                    break;
                case 4:
                    webSocketCommunicator.makeMove(out,client,webSocketCommunicator,authToken,gameID);
//                    client.drawMostRecentBoard(out, ChessGame.TeamColor.WHITE);
                    break;
                case 5:
                    //resign
                    System.out.println("Resigning from game");
                    webSocketCommunicator.resign(authToken,gameID);;//should fail for observer
                    ChessGame game = client.getMostRecentGame();
                    //should not leave game
                    break;
                case 6:
                    //take as input loc of piece
                    //highlight legal moves, do it in drawChessBoard with another parameter
                    System.out.println("Type in the row and column of the square for which you want to highlight moves");
                    String[] labels = {"startPosCol","startPosRow"};
                    String[] positions = client.generalRepl(labels);
                    Integer col=(columnsByName.containsKey(positions[0]))? columnsByName.get(positions[0]): -1;
                    Integer row = Integer.parseInt(positions[1]);
                    ChessPosition startPos = new ChessPosition(row,col);
                    System.out.printf("chessposition: %s\n",startPos);
                    System.out.printf("currentUserColor for highlight: %s\n",currentUserColor);
                    ChessGame.TeamColor redrawTeamColor2 = (currentUserColor!=null)? currentUserColor:WHITE;
                    System.out.printf("highlight redrawTeamColor: %s\n",redrawTeamColor2);
                    client.drawMostRecentBoard(out,redrawTeamColor2);
                    client.drawHighlightedMostRecentBoard(out,redrawTeamColor2,startPos);
//                    webSocketCommunicator.highlightMove(out,currentUserColor);
//                    client.drawMostRecentBoard(out, redrawTeamColor);
                    break;
            }
        }
    }

    private void gamePlayHelpMenu(PrintStream out) {
        out.printf("Please select one of the below options by typing its number:%n" +
                "1. Help%n" +
                "2. Redraw chess board%n" +
                "3. Leave%n" +
                "4. Make move%n"+
                "5. Resign%n" +
                "6. Highlight legal moves\n\n");
    }



}
