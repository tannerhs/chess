package ui;

import chess.ChessGame;
import chess.ChessPiece;
import client_requests.JoinGameRequest;
import client_requests.LoginRequest;
import client_responses_http.*;
import client_responses_ws.JoinGameResponseWS;
import com.google.gson.Gson;
import model.GameData;
import model.UserData;
import client_requests.*;
import webSocketMessages.serverMessages.LoadGame;
import webSocketMessages.serverMessages.Notification;
import webSocketMessages.serverMessages.ServerMessage;

import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Scanner;

import static ui.EscapeSequences.*;

public class Client implements ServerMessageObserver{

    private static final int BOARD_SIZE_IN_SQUARES = 8;
    private static final int SQUARE_SIZE_IN_CHARS = 3;
    private static final String EMPTY = "   ";

    private int port=8080;  //client/serverFacade port
    private ServerFacade facade = new ServerFacade(port, this);

    private Boolean quitProgram=false;
    private String currentUserAuthToken;

    //error messages
    static final String ERROR_MESSAGE_403="Error: already taken\n";
    static final String ERROR_MESSAGE_400="Error: bad request\n";
    static final String ERROR_MESSAGE_401="Error: unauthorized\n";
    static final String ERROR_MESSAGE_500="Error: bad request\n";
    static final String UNKOWN_ERROR_MESSAGE="Error: unknown error";

    public ServerFacade clientSetup() {
        return facade;
    }

    public void notify(String message) {  //print stuff based on notification received (print string or game)
        ServerMessage serverMessage = new Gson().fromJson(message, ServerMessage.class);
        switch(serverMessage.getServerMessageType()) {
            case LOAD_GAME : {
                LoadGame loadGame = new Gson().fromJson(message, LoadGame.class);
                System.out.println(loadGame.getGame());  //fixme
                //do something useful like print whatever it is, game or string
                break;
            }
            case NOTIFICATION: {
                Notification notification = new Gson().fromJson(message, Notification.class);
                System.out.println(notification.getMessage());  //print to terminal for all players who receive it
            }
        }
    }

    public static void main(String[] args) {  //pass in... game? team color?
        Client myClient = new Client();
        myClient.run();
    }

    private void run() {
        var piece = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.PAWN);
        System.out.println("♕ 240 Chess Client: " + piece);
        PrintStream out = new PrintStream(System.out, true, StandardCharsets.UTF_8);
        while(!quitProgram) {
            try {
                preloginMenu(out);  //stays here until successfully registers, logs in, or quits
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
            if(quitProgram) {  //if prelogin causes program to quit, then leave
                continue;
            }
            postloginMenu(out);  //stays here until you log out, upon which you return to the prelogin menu
        }
    }

    private void preloginMenu(PrintStream out) throws Exception {
        out.print(SET_TEXT_COLOR_WHITE);
        out.print(SET_BG_COLOR_BLACK);
        Boolean loggedIn=false;
        Boolean quit = false;
        int selection=8;  //default
        out.printf("Welcome to the CS 240 prelogin menu!  Press 1 to get help.\n");
        while (!loggedIn && !quit) {  //prelogin page
            out.printf(">>>");
            selection = readInputNumber();

            switch (selection) {
                case 1:  //Help
                    preloginHelpMenu(out);
                    break;
                case 2: //Quit
                    //quit early
                    quitProgram=true;  //global variable for quitting in main
                    quit=true;
                    out.print("Exiting program...\n");
                    break;
                case 3:  //Login
                    try {
                        LoginRequest loginRequest = loginRepl();
                        loggedIn=true;
                        LoginResponse loginResponse= facade.login(loginRequest);
                        if(loginResponse.statusCode()!=200) {
                            printErrorMessage(out,loginResponse.statusCode());
                            loggedIn=false;
                        }
                        else {
                            currentUserAuthToken=loginResponse.addedAuth().authToken();
                            out.printf("Login success!\n");
                        }
                    }
                    catch(Exception e) {  //this should never be called...all exceptions should be handled
                        out.print(e.getMessage());
                        loggedIn=false;
                    }

                    break;
                case 4: //Register
                    RegisterResponse registerResponse;
                    try {
                        UserData addUser = registerRepl();
                        registerResponse=  facade.register(addUser);  //FIXME CHECK TO MAKE SURE NO ERRORS THROWN?
                        if(registerResponse.statusCode()!=200) {
                            printErrorMessage(out,registerResponse.statusCode());
                            loggedIn=false;
                        }
                        else {
                            loggedIn=true;
                            currentUserAuthToken=registerResponse.addedAuth().authToken();
                            System.out.printf("Successfully registered\n");
                        }

                    }
                    catch(Exception e) {  //FIXME specify what the error is, like "username taken" or incorrect password!
                        out.print(e.getMessage());
                        loggedIn=false;
                    }
                    //
                    break;
                default:
                    //ask for more input
                    out.print("Invalid input\n");
                    break;
            }
        }
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

    private void postloginMenu(PrintStream out) {
        out.print(SET_TEXT_COLOR_WHITE);
        out.print(SET_BG_COLOR_BLACK);
//        Boolean validInput=false;
        Boolean loggedOut=false;
        int selection = 8;
        out.printf("Welcome to the postlogin menu! Press 1 to see the options.\n");
        while (!loggedOut) {  //prelogin page
            loggedOut=false;
            out.printf(">>>");
            selection = readInputNumber();

            switch (selection) {
                case 1:  //Help
                    postloginHelpMenu(out);
                    break;
                case 2: //Logout
                    try {
                        loggedOut=true;
                        LogoutRequest logoutRequest = new LogoutRequest(currentUserAuthToken);
                        LogoutResponse logoutResponse= facade.logout(logoutRequest);  //no logout response
                        if(logoutResponse.statusCode()!=200) {
                            printErrorMessage(out,logoutResponse.statusCode());
                        }
                        else{
                            currentUserAuthToken=null;
                        }
                    }
                    catch(Exception e) {
                        out.print(e.getMessage());
                    }
                    break;
                case 3:  //Create Game  //FIXME enforce unique game name
                    try {
                        CreateGameRequest createGameRequest = createGameRepl();
                        CreateGameResponse createGameResponse= facade.createGame(currentUserAuthToken,createGameRequest);
                        if(createGameResponse.statusCode()!=200) {
                            printErrorMessage(out,createGameResponse.statusCode());
                        }
                        else {  //we want to keep the gameID private
                            out.printf("Game successfully added!\n");
                        }
                    }
                    catch(Exception e) {
                        out.print(e.getMessage());
                    }
                    break;
                case 4: //List Games
                    try {
//                        System.out.printf("currentUserAuthToken: %s",currentUserAuthToken);
                        ListGamesResponse listGamesResponse= facade.listGames(currentUserAuthToken);
                        if(listGamesResponse.statusCode()!=200){
                            printErrorMessage(out,listGamesResponse.statusCode());
                        }
                        else {
                            //FIXME
                            //out.printf("%s\n",listGamesResponse.response());
                            List<GameData> gameList=listGamesResponse.games();
                            for(int i=0; i<gameList.size();i++) {
                                GameData game = gameList.get(i);
                                out.printf("%d) Name:%s\t\tWhite username:%s\t\tBlack username:%s\n",i+1, game.gameName(),game.whiteUsername(),game.blackUsername());
                            }
                        }
                    }
                    catch(Exception e) {
                        out.print(e.getMessage());
                    }
                    break;
                case 5: //Join Game

                    //list out games to choose from (by ID)
                    //out.printf("Please type in the gameID of the game you would like to join:\n");
                    try {
                        ListGamesResponse listGamesResponse= facade.listGames(currentUserAuthToken);
                    }
                    catch(Exception e) {  //should never be reached
                        out.printf(e.getMessage());
                    }

                    //get game indicated, then print it out; currently just makes new game
                    joinGameLogic(out, false);
                    break;
                case 6:  //Join Observer
                    //get game indicated, then print it out
                    joinGameLogic(out, true);
                    break;
                default:
                    //ask for more input
                    out.printf("Invalid input, returning to postlogin menu\n");
                    break;
            }
        }


        out.print(SET_TEXT_COLOR_WHITE);
        out.print(SET_BG_COLOR_BLACK);

    }

    private JoinGameRequest joinGameRepl(PrintStream out, Boolean onlyObserve) {
        out.printf("Please enter the number for one of the following games:\n");
        ListGamesResponse listGamesResponse;
        List<GameData> gameList=null;
        try {
            listGamesResponse = facade.listGames(currentUserAuthToken);

            if(listGamesResponse.statusCode()!=200) {
                printErrorMessage(out, listGamesResponse.statusCode());
                System.out.println("This should never be reached");
            }
            gameList = listGamesResponse.games();
            for(int i=0; i<gameList.size();i++) {
                GameData game = gameList.get(i);
                out.printf("%d) Name:%s\t\tWhite username:%s\t\tBlack username:%s\n",i+1, game.gameName(),game.whiteUsername(),game.blackUsername());
            }

        }
        catch (Exception e) {
            out.printf(e.getMessage());
        }

        //out.printf(">>>");
        int index=readInputNumber()-1 ;//array is 0-indexed

        //take index and get corresponding gameID
        int gameID=(gameList==null || index<0 || index>=gameList.size())?-1:gameList.get(index).gameID();

        int colorSelection=0;
        String color="";
        if(!onlyObserve) {  //if playing, get desired player color (if available)
            out.printf("Please select a color by typing in the corresponding number:\n\t1) WHITE\n\t2) BLACK\n>>>");
            while(colorSelection!=1 && colorSelection!=2) {
                colorSelection=readInputNumber();
            }
            color=(colorSelection==1)?"WHITE":"BLACK";

        }
        return new JoinGameRequest(color,gameID);
    }


    private void preloginHelpMenu(PrintStream out) {
        out.printf("Please select one of the below options by typing its number:%n" +
                "1. Help%n" +
                "2. Quit%n" +
                "3. Login%n" +
                "4. Register\n\n");
    }

    private void postloginHelpMenu(PrintStream out) {
        out.printf("Please select one of the below options by typing its number:%n" +
                "1. Help%n" +
                "2. Logout%n" +
                "3. Create Game%n" +
                "4. List Games%n"+
                "5. Join Game%n" +
                "6. Join Observer\n\n");
    }

    private UserData registerRepl() {
        System.out.printf("Register%n");
        String[] labels=new String[3];
        labels[0]="username";
        labels[1]="password";
        labels[2]="email";
        String[] output = new String[3];
        output=generalRepl(labels);
        return new UserData(output[0],output[1],output[2]);
    }

    private LoginRequest loginRepl() {
        System.out.printf("Login%n");

        String[] labels=new String[2];
        labels[0]="username";
        labels[1]="password";
        String[] output;
        output=generalRepl(labels);
        System.out.println("login general repl returned");
        return new LoginRequest(output[0],output[1]);
    }


    private CreateGameRequest createGameRepl() {
        String[] inputLabels = new String[1];
        inputLabels[0]="gameName";
        String[] input= generalRepl(inputLabels);
        return new CreateGameRequest(input[0]);
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

    private void drawVerticalLine(PrintStream out, int lineWidthInChars) {

        int boardSizeInSpaces = BOARD_SIZE_IN_SQUARES * SQUARE_SIZE_IN_CHARS +
                (BOARD_SIZE_IN_SQUARES - 1) * lineWidthInChars;

        for (int lineRow = 0; lineRow < lineWidthInChars; ++lineRow) {
            out.print(SET_BG_COLOR_BLACK);
            out.print(SET_TEXT_COLOR_BLACK);
            out.print(EMPTY.repeat(boardSizeInSpaces));
            out.println();
        }
    }



    private void printErrorMessage(PrintStream out, int statusCode) {
        if(statusCode==400) {
            out.printf(ERROR_MESSAGE_400);
        }
        else if(statusCode==401) {
            out.printf(ERROR_MESSAGE_401);
        }
        else if(statusCode==403) {
            out.printf(ERROR_MESSAGE_403);
        }
        else if(statusCode==500) {
            out.printf(ERROR_MESSAGE_500);
        }
        else {
            out.printf("%s with status code %d",UNKOWN_ERROR_MESSAGE,statusCode);
        }
    }

    private void joinGameLogic(PrintStream out, Boolean onlyObserve) {
        JoinGameResponseHttp joinGameResponse=null;
        try{
            JoinGameRequest joinGameRequest= joinGameRepl(out,onlyObserve);  //asks for color
            joinGameResponse = facade.joinGame(currentUserAuthToken,joinGameRequest);
        }
        catch (Exception e) {
            out.println(e.getMessage());
            return;  //don't print out board if unhandled error encountered
        }

        //if exception was one I accounted for, it should never be thrown to the user, so joinGameResponse should have the correct statusCode (returned before exception can be thrown to user)
        if(joinGameResponse.statusCode()!=200) {
            printErrorMessage(out,joinGameResponse.statusCode());
        }
        else {
            ChessGame game = joinGameResponse.game();
            new DrawChessBoard(out, game);  //draws it in orientation of currentTeam at bottom
            //draw line
            drawVerticalLine(out,2);  //line width of 2 characters
            out.print(SET_BG_COLOR_BLACK);
            out.print(SET_TEXT_COLOR_WHITE);
        }

//        JoinGameResponseHttp joinGameResponseHttp;
//        try{
//            JoinGameRequest joinGameRequest= joinGameRepl(out,onlyObserve);  //asks for color
//            joinGameResponseHttp =facade.joinGame(currentUserAuthToken,joinGameRequest);
////            JoinGameResponseWS joinGameResponseWS =facade.joinGameWS(currentUserAuthToken,joinGameRequest);
//            if(joinGameResponseHttp.statusCode()!=200) {
//                printErrorMessage(out, joinGameResponseHttp.statusCode());
//            }
//            else {
//                ChessGame game = joinGameResponseHttp.game();
//                new DrawChessBoard(out, game);  //draws it in orientation of currentTeam at bottom
//                //draw line
//                //drawVerticalLine(out,2);  //line width of 2 characters
//                out.print(SET_BG_COLOR_BLACK);
//                out.print(SET_TEXT_COLOR_WHITE);
//            }
//        }
//        catch (Exception e) {
//            out.println(e.getMessage());
//            return;  //don't print out board if unhandled error encountered
//        }


    }


}
