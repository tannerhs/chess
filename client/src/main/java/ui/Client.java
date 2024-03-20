package ui;

import chess.ChessBoard;
import chess.ChessGame;
import chess.ChessPiece;
import model.UserData;
import requests.*;
import responses.CreateGameResponse;
import responses.ListGamesResponse;
import responses.LoginResponse;
import responses.RegisterResponse;

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

    private static ChessGame game;
    private static ChessPiece[][] board;
    private static ServerFacade facade = new ServerFacade();

    private static Boolean quitProgram=false;
    private static String currentUserAuthToken;

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
        out.println("Welcome to the CS 240 prelogin menu!  Press 1 to get help.\n>>>");
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
                        currentUserAuthToken=loginResponse.addedAuth().authToken();
                        //out.printf("authToken added with login: %s\n",currentUserAuthToken);
                    }
                    catch(Exception e) {  //FIXME specify what the error is, like "username taken" or incorrect password!
                        out.print(e.getMessage());
                        loggedIn=false;
                    }
                    break;
                case 4: //Register
                    String registerResponse;
                    try {
                        UserData addUser = registerRepl();
                        registerResponse=  facade.register(addUser);  //FIXME CHECK TO MAKE SURE NO ERRORS THROWN?
                        loggedIn=true;
//                        if(registerResponse==null || registerResponse.addedAuth()==null){
//                            out.println("null register response");
//                            //out.println(registerResponse.errorMessage());
//                            out.println(registerResponse.statusCode());
//                        }
                        currentUserAuthToken=registerResponse;
                        out.printf("authToken added with register: %s\n", currentUserAuthToken);
                        out.print("Successfully registered");
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
        out.println("Welcome to the postlogin menu! Press 1 to see the options.\n>>>");
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
                        facade.logout(logoutRequest);  //no logout response
                        currentUserAuthToken=null;
                        //out.printf("currentUserAuthToken: %s",currentUserAuthToken);
                    }
                    catch(Exception e) {
                        out.print(e.getMessage());
                    }
                    break;
                case 3:  //Create Game
                    try {
                        //String gameName = createGameRepl();
                        CreateGameRequest createGameRequest = createGameRepl();
                               // new CreateGameRequest(currentUserAuthToken,gameName);
                        CreateGameResponse createGameResponse= facade.creatGame(currentUserAuthToken,createGameRequest);
                        out.printf("gameID of newly created game: %s\n",createGameResponse.gameID());
                    }
                    catch(Exception e) {
                        out.print(e.getMessage());
                    }
                    break;
                case 4: //List Games
                    try {
                        System.out.printf("currentUserAuthToken: %s",currentUserAuthToken);
                        ListGamesResponse listGamesResponse= facade.listGames(currentUserAuthToken);
                        out.printf("%s\n",listGamesResponse.response());
                    }
                    catch(Exception e) {
                        out.print(e.getMessage());
                    }
                    break;
                case 5: //Join Game
                    //get game indicated, then print it out
                    drawChessBoard(out, new ChessGame());
                    break;
                case 6:  //Join Observer
                    //get game indicated, then print it out
                    drawChessBoard(out, new ChessGame());
                    break;
                default:
                    //ask for more input
                    out.printf("Invalid input, returning to postlogin menu\n");
                    break;
            }
        }


        out.print(SET_TEXT_COLOR_WHITE);
        out.print(SET_BG_COLOR_BLACK);
        out.print("testing");

    }


    private static void preloginHelpMenu(PrintStream out) {
        out.printf("Please select one of the below options by typing its number:%n" +
                "1. Help%n" +
                "2. Quit%n" +
                "3. Login%n" +
                "4. Register%n"
                +"%n>>> ");
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
                "6. Join Observer%n" +
                "%n>>> ");
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
            System.out.printf("Registered %s with password \"%s\" and email %s%n", username,password,email);
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


    public static void drawChessBoard(PrintStream out, ChessGame gameIn) {  //call when you join or observe a gameC
        game=gameIn;
        //game.setTeamTurn(ChessGame.TeamColor.BLACK);
        //ChessBoard boardObj = game.getBoard();
        ChessBoard boardObj = new ChessBoard();
        boardObj.setStartBoard();  //can print out board for debugging if you want
        board=boardObj.getBoardArray();



        out.print(ERASE_SCREEN);



        drawHeaders(out);  //top header


        out.print(SET_BG_COLOR_DARK_GREY);
        out.print(SET_TEXT_COLOR_WHITE);

        for (int boardRow = BOARD_SIZE_IN_SQUARES-1; boardRow >= 0; boardRow--) {

            drawRowOfSquares(out, boardRow);

            if (boardRow >0 ) {
                drawVerticalLine(out);
                out.print(SET_BG_COLOR_DARK_GREY);
            }
        }
        drawHeaders(out);  //bottom header
    }

    private static void drawHeaders(PrintStream out) {
        out.print(SET_BG_COLOR_DARK_GREY);
        String[] whiteHeaders = { " a ", " b ", " c ", " d ", " e ", " f ", " g ", " h "};
        String[] blackHeaders = {" h ", " g ", " f ", " e ", " d ", " c ", " b ", " a "};
        String[] headers = (game.getTeamTurn()== ChessGame.TeamColor.WHITE)? whiteHeaders:blackHeaders;
        out.print("   ");  //offset for side column

        for (int boardCol = 0; boardCol < BOARD_SIZE_IN_SQUARES; ++boardCol) {
            out.print(SET_TEXT_COLOR_BLACK);

            drawHeader(out, headers[boardCol]);

            if (boardCol < BOARD_SIZE_IN_SQUARES - 1) {
                out.print(EMPTY.repeat(LINE_WIDTH_IN_CHARS));
            }
        }

        out.println();
    }


    private static void drawHeader(PrintStream out, String headerText) {
        int prefixLength = SQUARE_SIZE_IN_CHARS / 2;
        int suffixLength = SQUARE_SIZE_IN_CHARS - prefixLength - 1;

        out.print(EMPTY.repeat(prefixLength));
        printHeaderText(out, headerText);
        out.print(EMPTY.repeat(suffixLength));
    }

    private static void printHeaderText(PrintStream out, String player) {
        out.print(SET_BG_COLOR_DARK_GREY);
        out.print(SET_TEXT_COLOR_BLACK);
        //out.print(SET_TEXT_COLOR_GREEN);

        out.print(player);
        out.print(SET_BG_COLOR_DARK_GREY);
    }

    private static void drawRowOfSquares(PrintStream out, int boardRow) {

        for (int squareRow = 0; squareRow < SQUARE_SIZE_IN_CHARS; ++squareRow) {
            for (int boardCol = 0; boardCol < BOARD_SIZE_IN_SQUARES; ++boardCol) {
                //


                if (squareRow == SQUARE_SIZE_IN_CHARS / 2) {
                    int prefixLength = SQUARE_SIZE_IN_CHARS / 2;
                    int suffixLength = SQUARE_SIZE_IN_CHARS - prefixLength - 1;


                    if(game.getTeamTurn()== ChessGame.TeamColor.WHITE) {
                        char player = (board[boardRow][boardCol]==null)? ' ' : board[boardRow][boardCol].toChar();
                        if(boardCol==0) {
                            //print side column labels
                            out.print(SET_BG_COLOR_DARK_GREY);
                            out.print(SET_TEXT_COLOR_BLACK);
                            out.print(" ");
                            out.print(boardRow+1);
                            out.print(" ");
                        }


                        if(((boardRow+1)%2)!=((boardCol+1)%2)) {
                            out.print(SET_BG_COLOR_LIGHT_GREY);
                            out.print(EMPTY.repeat(prefixLength));
                            printPlayerLightBackground(out, player);
                            out.print(EMPTY.repeat(suffixLength));
                        }
                        else {
                            out.print(SET_BG_COLOR_BLACK);
                            out.print(EMPTY.repeat(prefixLength));
                            printPlayerDarkBackground(out, player);
                            out.print(EMPTY.repeat(suffixLength));

                        }

                        if(boardCol==7) {
                            //print side column labels
                            out.print(SET_BG_COLOR_DARK_GREY);
                            out.print(SET_TEXT_COLOR_BLACK);
                            out.print(" ");
                            out.print(boardRow+1);
                            out.print(" ");
                        }
                    }
                    else if (game.getTeamTurn()== ChessGame.TeamColor.BLACK) {  //flip board with 7-x operations
                        char player = (board[7-boardRow][7-boardCol]==null)? ' ' : board[7-boardRow][7-boardCol].toChar();
                        if(boardCol==0) {
                            //print side column labels
                            out.print(SET_BG_COLOR_DARK_GREY);
                            out.print(SET_TEXT_COLOR_BLACK);
                            out.print(" ");
                            out.print(8-boardRow);
                            out.print(" ");
                        }


                        if(((boardRow+1)%2)!=((boardCol+1)%2)) {
                            out.print(SET_BG_COLOR_LIGHT_GREY);
                            out.print(EMPTY.repeat(prefixLength));
                            printPlayerLightBackground(out, player);
                            out.print(EMPTY.repeat(suffixLength));
                        }
                        else {
                            out.print(SET_BG_COLOR_BLACK);
                            out.print(EMPTY.repeat(prefixLength));
                            printPlayerDarkBackground(out, player);
                            out.print(EMPTY.repeat(suffixLength));

                        }

                        if(boardCol==7) {
                            //print side column labels
                            out.print(SET_BG_COLOR_DARK_GREY);
                            out.print(SET_TEXT_COLOR_BLACK);
                            out.print(" ");
                            out.print(8-boardRow);
                            out.print(" ");
                        }
                    }
                }
                else {
                    if(game.getTeamTurn()== ChessGame.TeamColor.WHITE) {
                        if(boardCol==0) {
                            //print side column line
                            out.print(SET_BG_COLOR_DARK_GREY);
                            out.print(SET_TEXT_COLOR_BLACK);
                            out.print("   ");
                        }
                        if (((boardRow + 1) % 2) != ((boardCol + 1) % 2)) {
                            out.print(SET_BG_COLOR_LIGHT_GREY);
                        }
                        else {
                            out.print(SET_BG_COLOR_BLACK);
                        }
                    }
                    else {
                        if(boardCol==0) {
                            //print side column line
                            out.print(SET_BG_COLOR_DARK_GREY);
                            out.print(SET_TEXT_COLOR_BLACK);
                            out.print("   ");
                        }
                        if (((boardRow + 1) % 2) != ((boardCol + 1) % 2)) {
                            out.print(SET_BG_COLOR_LIGHT_GREY);
                        }
                        else {
                            out.print(SET_BG_COLOR_BLACK);
                        }
                    }
                    out.print(EMPTY.repeat(SQUARE_SIZE_IN_CHARS));
                }

                if (boardCol < BOARD_SIZE_IN_SQUARES - 1) {
                    // Draw right line
                    setRed(out);
                    out.print(EMPTY.repeat(LINE_WIDTH_IN_CHARS));
                }

                out.print(SET_BG_COLOR_DARK_GREY);

            }

            out.println();
        }
    }

    private static void drawVerticalLine(PrintStream out) {

        int boardSizeInSpaces = BOARD_SIZE_IN_SQUARES * SQUARE_SIZE_IN_CHARS +
                (BOARD_SIZE_IN_SQUARES - 1) * LINE_WIDTH_IN_CHARS;

        for (int lineRow = 0; lineRow < LINE_WIDTH_IN_CHARS; ++lineRow) {
            setRed(out);
            out.print(EMPTY.repeat(boardSizeInSpaces));

            out.print(SET_BG_COLOR_DARK_GREY);
            out.println();
        }
    }


    private static void setRed(PrintStream out) {
        out.print(SET_BG_COLOR_RED);
        out.print(SET_TEXT_COLOR_RED);
    }


    private static void printPlayerLightBackground(PrintStream out, char player) {
        out.print(SET_BG_COLOR_LIGHT_GREY);
        if(isUpperCase(player)) {  //white team color
            out.print(SET_TEXT_COLOR_RED);
        }
        else {  //black team color
            out.print(SET_TEXT_COLOR_BLUE);
        }
        out.print(" ");
        out.print(toUpperCase(player));
        out.print(" ");
    }

    private static void printPlayerDarkBackground(PrintStream out, char player) {
        out.print(SET_BG_COLOR_BLACK);
        if(isUpperCase(player)) {  //white team color
            out.print(SET_TEXT_COLOR_RED);
        }
        else {  //black team color
            out.print(SET_TEXT_COLOR_BLUE);
        }
        out.print(" ");
        out.print(toUpperCase(player));
        out.print(" ");
    }

}
