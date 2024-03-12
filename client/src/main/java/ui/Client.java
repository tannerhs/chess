package ui;

import chess.ChessBoard;
import chess.ChessGame;
import chess.ChessPiece;

import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.util.Random;

import static ui.EscapeSequences.*;

public class Client {

    private static final int BOARD_SIZE_IN_SQUARES = 8;
    private static final int SQUARE_SIZE_IN_CHARS = 3;
    private static final int LINE_WIDTH_IN_CHARS = 1;
    private static final String EMPTY = "   ";
    private static final String P = " P ";
    private static final String p = " p ";
    private static Random rand = new Random();

    private static ChessGame game;
    private static ChessPiece[][] board;

    public static void main(String[] args) {
        game=new ChessGame();
        //ChessBoard boardObj = game.getBoard();
        ChessBoard boardObj = new ChessBoard();
        boardObj.setStartBoard();
        board=boardObj.getBoardArray();


        var out = new PrintStream(System.out, true, StandardCharsets.UTF_8);

        out.print(ERASE_SCREEN);

        drawHeaders(out);

        drawChessBoard(out);

        out.print(SET_BG_COLOR_BLACK);
        out.print(SET_TEXT_COLOR_WHITE);
    }
    public static void drawChessBoard(PrintStream out) {

        for (int boardRow = 0; boardRow < BOARD_SIZE_IN_SQUARES; ++boardRow) {

            drawRowOfSquares(out);

            if (boardRow < BOARD_SIZE_IN_SQUARES - 1) {
                drawVerticalLine(out);
                setBlack(out);
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
        out.print(SET_BG_COLOR_BLACK);
        out.print(SET_TEXT_COLOR_GREEN);

        out.print(player);

        setBlack(out);
    }

    private static void drawRowOfSquares(PrintStream out) {

        for (int squareRow = 0; squareRow < SQUARE_SIZE_IN_CHARS; ++squareRow) {
            for (int boardCol = 0; boardCol < BOARD_SIZE_IN_SQUARES; ++boardCol) {
                setWhite(out);

                if (squareRow == SQUARE_SIZE_IN_CHARS / 2) {
                    int prefixLength = SQUARE_SIZE_IN_CHARS / 2;
                    int suffixLength = SQUARE_SIZE_IN_CHARS - prefixLength - 1;

                    out.print(EMPTY.repeat(prefixLength));
                    printPlayer(out, board[1][1].toCharString());
                    out.print(EMPTY.repeat(suffixLength));
                }
                else {
                    out.print(EMPTY.repeat(SQUARE_SIZE_IN_CHARS));
                }

                if (boardCol < BOARD_SIZE_IN_SQUARES - 1) {
                    // Draw right line
                    setRed(out);
                    out.print(EMPTY.repeat(LINE_WIDTH_IN_CHARS));
                }

                setBlack(out);
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

            setBlack(out);
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

    private static void printPlayer(PrintStream out, String player) {
        out.print(SET_BG_COLOR_WHITE);
        out.print(SET_TEXT_COLOR_BLACK);

        out.print(player);

        setWhite(out);
    }

}
