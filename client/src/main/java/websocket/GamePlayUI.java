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

import static ui.EscapeSequences.SET_BG_COLOR_BLACK;
import static ui.EscapeSequences.SET_TEXT_COLOR_WHITE;

public class GamePlayUI {
    String authToken;
    //pass in game itself
    Integer gameID;
    ChessGame updatedGame;




    public GamePlayUI(String authToken, Integer gameID) {
        this.authToken=authToken;
        this.gameID=gameID;
    }
    public void gamePlayMenu(PrintStream out, Client client, WebSocketCommunicator webSocketCommunicator, ChessGame.TeamColor currentUserColor) {
        //while command is not leave,
        out.print(SET_TEXT_COLOR_WHITE);
        out.print(SET_BG_COLOR_BLACK);
//        Boolean validInput=false;
        Boolean leave = false;
        int selection = 8;
        out.printf("Welcome to the gameplay menu! Press 1 to see the options.\n");
        while (!leave) {  //prelogin page
            leave = false;
            out.printf(">>>");
            selection = readInputNumber();
            switch (selection) {
                case 1:
                    gamePlayHelpMenu(out);  //print it out
                    break;
                case 2:
                    //redraw
                    //get current user color:
//                    DrawChessBoard drawChessBoard = new DrawChessBoard(out, new ChessGame(), currentUserColor);
                    ChessGame mostRecentGame =client.getMostRecentGame();  //fixme!
                    System.out.println("Most recent game:");
                    System.out.println(mostRecentGame.getBoard().getBoardArray()[0][0]);
                    DrawChessBoard drawChessBoard = new DrawChessBoard(out, mostRecentGame, currentUserColor);

                    break;
                case 3:
                    //leave
                    System.out.println("Leaving game");
                    //FIXME remove from hash map in Client
                    leave=true;
                    break;
                case 4:
                    webSocketCommunicator.makeMove(out,client,webSocketCommunicator,authToken,gameID);
                    break;
                case 5:
                    //resign
                    webSocketCommunicator.leave(authToken,gameID);
                    break;
                case 6:
                    //take as input loc of piece
                    //highlight legal moves, do it in drawChessBoard with another parameter
                    webSocketCommunicator.resign(authToken,gameID);
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

    //generalRepl, returns list of Strings, takes as input parameter names to request
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




}
