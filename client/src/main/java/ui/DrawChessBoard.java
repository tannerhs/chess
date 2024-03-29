package ui;

import chess.ChessBoard;
import chess.ChessGame;
import chess.ChessPiece;
import chess.ChessPosition;

import java.io.PrintStream;

import static java.lang.Character.isUpperCase;
import static java.lang.Character.toUpperCase;
import static ui.EscapeSequences.*;
import static ui.EscapeSequences.SET_BG_COLOR_DARK_GREY;


public class DrawChessBoard {

    private static final int BOARD_SIZE_IN_SQUARES = 8;
    private static final int SQUARE_SIZE_IN_CHARS = 3;
    private static final int LINE_WIDTH_IN_CHARS = 0;
    private static final String EMPTY = "   ";
    private static ChessGame game;
    private static ChessPiece[][] board;

    public DrawChessBoard(PrintStream out, ChessGame gameIn) {  //call when you join or observe a gameC
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

        //change back to normal terminal print settings
        out.printf(SET_BG_COLOR_BLACK);  //fixme
        out.printf(SET_TEXT_COLOR_WHITE);
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

    //FIXME
    private static void highlightMoves(ChessPosition position) {
        //List<Move> validMoves=ChessPiece.validMoves(position);

    }
}
