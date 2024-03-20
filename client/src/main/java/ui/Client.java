package ui;

import chess.ChessBoard;
import chess.ChessGame;
import chess.ChessPiece;
import model.UserData;
import requests.*;
import responses.*;

import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;

import static java.lang.Character.isUpperCase;
import static java.lang.Character.toUpperCase;
import static ui.EscapeSequences.*;

public class Client {

    private static final int BOARD_SIZE_IN_SQUARES = 8;
    private static final int SQUARE_SIZE_IN_CHARS = 3;
    private static final int LINE_WIDTH_IN_CHARS = 0;
    private static final String EMPTY = "   ";

    private static ServerFacade facade = new ServerFacade();

    private static Boolean quitProgram=false;
    private static String currentUserAuthToken;

    //error messages
    static final String ERROR_MESSAGE_403="Error: already taken\n";
    static final String ERROR_MESSAGE_400="Error: bad request\n";
    static final String ERROR_MESSAGE_401="Error: unauthorized\n";
    static final String ERROR_MESSAGE_500="Error: bad request\n";
    static final String UNKOWN_ERROR_MESSAGE="Error: unknown error";

    public static void main(String[] args) {  //pass in... game? team color?

        var piece = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.PAWN);
        System.out.println("♕ 240 Chess Server: " + piece);
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

    private static void preloginMenu(PrintStream out) throws Exception {
        out.print(SET_TEXT_COLOR_WHITE);
        out.print(SET_BG_COLOR_BLACK);
        Boolean loggedIn=false;
        Boolean quit = false;
        int selection=8;  //default
        out.printf("Welcome to the CS 240 prelogin menu!  Press 1 to get help.\n>>>");
        while (!loggedIn && !quit) {  //prelogin page

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
                            //out.printf("authToken added with register: %s\n", currentUserAuthToken);
                            System.out.printf("Successfully registered");
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


    private static int readInputNumber() {
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

    private static void postloginMenu(PrintStream out) {
        out.print(SET_TEXT_COLOR_WHITE);
        out.print(SET_BG_COLOR_BLACK);
//        Boolean validInput=false;
        Boolean loggedOut=false;
        int selection = 8;
        out.printf("Welcome to the postlogin menu! Press 1 to see the options.\n>>>");
        while (!loggedOut) {  //prelogin page
            loggedOut=false;
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
                        //out.printf("currentUserAuthToken: %s",currentUserAuthToken);
                    }
                    catch(Exception e) {
                        out.print(e.getMessage());
                    }
                    break;
                case 3:  //Create Game  //FIXME enforce unique game name
                    try {
                        //String gameName = createGameRepl();
                        CreateGameRequest createGameRequest = createGameRepl();
                               // new CreateGameRequest(currentUserAuthToken,gameName);
                        CreateGameResponse createGameResponse= facade.createGame(currentUserAuthToken,createGameRequest);
                        if(createGameResponse.statusCode()!=200) {
                            printErrorMessage(out,createGameResponse.statusCode());
                        }
                        else {
                            out.printf("gameID of newly created game: %s\n",createGameResponse.gameID());
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
                            out.printf("%s\n",listGamesResponse.response());
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

    private static JoinGameRequest joinGameRepl(PrintStream out, Boolean onlyObserve) {
        out.printf("Please enter the gameID for one of the following games:\n");
        try {
            String listGames = facade.listGames(currentUserAuthToken).response();
            out.printf("\t%s\n",listGames);
        }
        catch (Exception e) {
            out.printf(e.getMessage());
        }

        out.printf(">>>");
        int gameID=readInputNumber();
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


    private static void preloginHelpMenu(PrintStream out) {
        out.printf("Please select one of the below options by typing its number:%n" +
                "1. Help%n" +
                "2. Quit%n" +
                "3. Login%n" +
                "4. Register\n\n"
                +">>>");
//        out.println("Help Menu:");
//        out.println("If you have not created an account, register.  Otherwise, login to see more options.");
//        out.println("When you are finished playing, please select quit by typing in its number.");
//        out.println("------------");
    }

    private static void postloginHelpMenu(PrintStream out) {
        out.printf("Please select one of the below options by typing its number:%n" +
                "1. Help%n" +
                "2. Logout%n" +
                "3. Create Game%n" +
                "4. List Games%n"+
                "5. Join Game%n" +
                "6. Join Observer\n\n" +
                ">>>");
    }

    private static UserData registerRepl() {
        Boolean validInput=false;
        String username="";
        String password="";
        String email="";
        while(!validInput) {
            System.out.printf("Register%n");

            Boolean validUsername=false;
            while(!validUsername) {
                System.out.printf("username:%n>>>");
                Scanner scanner = new Scanner(System.in);
                String line = scanner.nextLine();
                String[]  words = line.split(" ");
                if(!words[0].isEmpty()) {
                    username=words[0];
                    validUsername=true;
                }
                else {  //invalid input (empty string)
                    System.out.printf("invalid username%n");
                    continue;
                }
            }

            Boolean validPassword=false;
            while(!validPassword) {
                Scanner scanner = new Scanner(System.in);
                System.out.printf("password:%n>>>");
                String line = scanner.nextLine();
                String[] words = line.split(" ");
                password=words[0];
                if(!words[0].isEmpty()) {
                    password=words[0];
                    validPassword=true;
                }
                else {  //invalid input (empty string)
                    System.out.printf("invalid password%n");
                    continue;
                }
            }

            Boolean validEmail=false;
            while(!validEmail) {
                System.out.printf("email:%n>>>");
                Scanner scanner = new Scanner(System.in);
                String line = scanner.nextLine();
                String[] words = line.split(" ");
                email=words[0];
                if(!words[0].isEmpty()) {
                    email=words[0];
                    validEmail=true;
                }
                else {  //invalid input (empty string)
                    System.out.printf("invalid email%n");
                    continue;
                }
            }
            validInput=true;


        }
        return new UserData(username,password,email);
    }

    private static LoginRequest loginRepl() {
        String username="";
        String password="";
        Boolean validInput=false;
        while(!validInput) {
            System.out.printf("Login%n");

            Boolean validUsername = false;
            while (!validUsername) {
                System.out.printf("username:%n>>>");
                Scanner scanner = new Scanner(System.in);
                String line = scanner.nextLine();
                String[] words = line.split(" ");
                if (!words[0].isEmpty()) {
                    username = words[0];
                    validUsername = true;
                } else {  //invalid input (empty string)
                    System.out.printf("invalid username%n");
                    continue;
                }
            }

            Boolean validPassword = false;  //valid in the sense of valid input, not valid as in it is correct
            while (!validPassword) {
                Scanner scanner = new Scanner(System.in);
                System.out.printf("password:%n>>>");
                String line = scanner.nextLine();
                String[] words = line.split(" ");
                password = words[0];
                if (!words[0].isEmpty()) {
                    password = words[0];
                    validPassword = true;
                    validInput=true;  //username and password both in valid format
                } else {  //invalid input (empty string)
                    System.out.printf("invalid password%n");
                    continue;
                }
            }
        }
        return new LoginRequest(username,password);

    }


    private static CreateGameRequest createGameRepl() {
        String[] inputLabels = new String[1];
        inputLabels[0]="gameName";
        String[] input= generalRepl(inputLabels);
        return new CreateGameRequest(input[0]);
    }




    //generalRepl, returns list of Strings, takes as input parameter names to request
    private static String[] generalRepl(String[] inputLabels) {
            String[] inputParams=new String[inputLabels.length];
            Boolean validInput=false;
            for(int i=0; i<inputLabels.length;i++) {
                Boolean validUsername = false;
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
                        continue;
                    }
                }
            }
            return inputParams;
    }

    private static void drawVerticalLine(PrintStream out, int lineWidthInChars) {

        int boardSizeInSpaces = BOARD_SIZE_IN_SQUARES * SQUARE_SIZE_IN_CHARS +
                (BOARD_SIZE_IN_SQUARES - 1) * lineWidthInChars;

        for (int lineRow = 0; lineRow < lineWidthInChars; ++lineRow) {
            out.print(SET_BG_COLOR_BLACK);
            out.print(SET_TEXT_COLOR_BLACK);
            out.print(EMPTY.repeat(boardSizeInSpaces));

            //out.print(SET_BG_COLOR_DARK_GREY);
            out.println();
        }
    }



    private static void printErrorMessage(PrintStream out, int statusCode) {
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
            out.printf("%s with status code %i",UNKOWN_ERROR_MESSAGE,statusCode);
        }
    }

    private static void joinGameLogic(PrintStream out, Boolean onlyObserve) {
        JoinGameResponse joinGameResponse=null;
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
            ChessGame game = new ChessGame();  //
            game.setTeamTurn(ChessGame.TeamColor.BLACK);
            new DrawChessBoard(out, game);
            //draw line
            drawVerticalLine(out,2);  //line width of 2 characters
            //draw in other
            game.setTeamTurn(ChessGame.TeamColor.WHITE);
            new DrawChessBoard(out,game);
        }
    }

}
