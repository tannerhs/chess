package ui;

import chess.ChessBoard;
import chess.ChessGame;
import chess.ChessPiece;

import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.util.Random;

import static java.lang.Character.isUpperCase;
import static ui.EscapeSequences.*;

public class Client {

    private static final int BOARD_SIZE_IN_SQUARES = 8;
    private static final int SQUARE_SIZE_IN_CHARS = 3;
    private static final int LINE_WIDTH_IN_CHARS = 1;
    private static final String EMPTY = "   ";

    private static ChessGame game;
    private static ChessPiece[][] board;

    public static void main(String[] args) {
        game=new ChessGame();
        //ChessBoard boardObj = game.getBoard();
        ChessBoard boardObj = new ChessBoard();
        boardObj.setStartBoard();
        board=boardObj.getBoardArray();
        System.out.println(board[2][2]);


        var out = new PrintStream(System.out, true, StandardCharsets.UTF_8);

        out.print(ERASE_SCREEN);

        drawHeaders(out);

        drawChessBoard(out);

        out.print(SET_BG_COLOR_DARK_GREY);
        out.print(SET_TEXT_COLOR_WHITE);
    }
    public static void drawChessBoard(PrintStream out) {

        for (int boardRow = BOARD_SIZE_IN_SQUARES-1; boardRow >= 0; boardRow--) {

            drawRowOfSquares(out, boardRow);

            if (boardRow >0 ) {
                drawVerticalLine(out);
                out.print(SET_BG_COLOR_DARK_GREY);
            }
        }
    }

    private static void drawHeaders(PrintStream out) {

        setBlack(out);
        String[] whiteHeaders = { " a ", " b ", " c ", " d ", " e ", " f ", " g ", " h "};
        String[] blackHeaders = {" h ", " g ", " e ", " f ", " e ", " d ", " c ", " b ", " a "};
        String[] headers = (game.getTeamTurn()== ChessGame.TeamColor.WHITE)? whiteHeaders:blackHeaders;
        for (int boardCol = 0; boardCol < BOARD_SIZE_IN_SQUARES; ++boardCol) {
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
        out.print(SET_TEXT_COLOR_GREEN);

        out.print(player);
        out.print(SET_BG_COLOR_DARK_GREY);
    }

    private static void drawRowOfSquares(PrintStream out, int boardRow) {

        for (int squareRow = 0; squareRow < SQUARE_SIZE_IN_CHARS; ++squareRow) {
            for (int boardCol = 0; boardCol < BOARD_SIZE_IN_SQUARES; ++boardCol) {
                //
                out.print(SET_BG_COLOR_WHITE);

                if (squareRow == SQUARE_SIZE_IN_CHARS / 2) {
                    int prefixLength = SQUARE_SIZE_IN_CHARS / 2;
                    int suffixLength = SQUARE_SIZE_IN_CHARS - prefixLength - 1;


                    char player = (board[boardRow][boardCol]==null)? ' ' : board[boardRow][boardCol].toChar();
                    if(game.getTeamTurn()== ChessGame.TeamColor.WHITE) {
                        if(((boardRow+1)%2)!=((boardCol+1)%2)) {
                            out.print(SET_BG_COLOR_WHITE);
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
                    }
                    else if (game.getTeamTurn()== ChessGame.TeamColor.BLACK) {  //FIXME
                        if(((boardRow+1)%2)==1 && ((boardCol+1)%2)==0) {
                            printPlayerLightBackground(out, player);

                        }
                        else {
                            printPlayerDarkBackground(out, player);
                        }
                    }
                }
                else {
                    if(game.getTeamTurn()== ChessGame.TeamColor.WHITE) {
                        if (((boardRow + 1) % 2) != ((boardCol + 1) % 2)) {
                            out.print(SET_BG_COLOR_WHITE);
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

    private static void setWhite(PrintStream out) {
        out.print(SET_BG_COLOR_WHITE);
        out.print(SET_TEXT_COLOR_WHITE);
    }

    private static void setRed(PrintStream out) {
        out.print(SET_BG_COLOR_RED);
        out.print(SET_TEXT_COLOR_RED);
    }

    private static void setBlack(PrintStream out) {
        out.print(SET_BG_COLOR_BLACK);
        out.print(SET_TEXT_COLOR_BLACK);
    }

    private static void printPlayerLightBackground(PrintStream out, char player) {
        out.print(SET_BG_COLOR_LIGHT_GREY);
        if(isUpperCase(player)) {  //white team color
            out.print(SET_TEXT_COLOR_BLUE);
        }
        else {  //black team color
            out.print(SET_TEXT_COLOR_RED);
        }
        out.print(" ");
        out.print(player);
        out.print(" ");
    }

    private static void printPlayerDarkBackground(PrintStream out, char player) {
        out.print(SET_BG_COLOR_BLACK);
        if(isUpperCase(player)) {  //white team color
            out.print(SET_TEXT_COLOR_BLUE);
        }
        else {  //black team color
            out.print(SET_TEXT_COLOR_RED);
        }
        out.print(" ");
        out.print(player);
        out.print(" ");
    }

}
