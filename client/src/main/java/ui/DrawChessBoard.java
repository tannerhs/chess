package ui;

import chess.*;

import java.io.PrintStream;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import static java.lang.Character.*;
import static ui.EscapeSequences.*;
import static ui.EscapeSequences.SET_BG_COLOR_DARK_GREY;


public class DrawChessBoard {

    private static final int BOARD_SIZE_IN_SQUARES = 8;
    private static final int SQUARE_SIZE_IN_CHARS = 3;
    private static final int LINE_WIDTH_IN_CHARS = 0;
    private static final String EMPTY = "   ";
    private static ChessGame game;
    private static ChessPiece[][] board;
    private static ChessGame.TeamColor printFromPerspective;

    private static Collection<ChessPosition> highlightLocs;
    private static ChessPosition startPos;


    public DrawChessBoard(PrintStream out, ChessGame gameIn, ChessGame.TeamColor printFromPerspective, ChessPosition startPos) {  //call when you join or observe a gameC
        game=gameIn;
        highlightLocs=new HashSet();
        this.startPos=startPos;
        this.printFromPerspective=printFromPerspective;
        if(startPos!=null) {
        }

        if(game.validMoves(startPos)!=null) {
            for(ChessMove validMove: game.validMoves(startPos)) {
                highlightLocs.add(validMove.getEndPosition());
            }
        }

        ChessBoard boardObj = new ChessBoard();
        boardObj.setStartBoard();  //can print out board for debugging if you want
//        board=boardObj.getBoardArray();
        board=gameIn.getBoard().getBoardArray();
        if (board==null || board[0][0]==null) {
            System.out.println("AHHHHHH");
        }



//        out.print(ERASE_SCREEN);



        drawHeaders(out);  //top header


        out.print(SET_BG_COLOR_DARK_GREY);
        out.print(SET_TEXT_COLOR_WHITE);

        for (int boardRow = BOARD_SIZE_IN_SQUARES-1; boardRow >= 0; boardRow--) {

            drawRowOfSquares(out, boardRow);

        }
        drawHeaders(out);  //bottom header

        //change back to normal terminal print settings
        out.printf(SET_BG_COLOR_BLACK);
        out.printf(SET_TEXT_COLOR_WHITE);
    }

    private static void drawHeaders(PrintStream out) {
        out.print(SET_BG_COLOR_DARK_GREY);
        String[] whiteHeaders = { " a ", " b ", " c ", " d ", " e ", " f ", " g ", " h "};
        String[] blackHeaders = {" h ", " g ", " f ", " e ", " d ", " c ", " b ", " a "};
        String[] headers = (printFromPerspective== ChessGame.TeamColor.WHITE)? whiteHeaders:blackHeaders;
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
        //
        out.print(SET_BG_COLOR_DARK_GREY);
    }

    private static void drawRowOfSquares(PrintStream out, int boardRow) {

        for (int squareRow = 0; squareRow < SQUARE_SIZE_IN_CHARS; ++squareRow) {
            for (int boardCol = 0; boardCol < BOARD_SIZE_IN_SQUARES; ++boardCol) {
                drawRowOfSquareInsideForLoops(out,squareRow, boardCol,boardRow);
            }
            out.println();
        }
    }

    private static void drawRowOfSquareInsideForLoops(PrintStream out, int squareRow, int boardCol,int boardRow) {
        if (squareRow == SQUARE_SIZE_IN_CHARS / 2) {
            printMiddleRowOfSquare(out,boardCol,boardRow);
        }
        else {
            printAllOtherRowsOfSquare(out,boardCol,boardRow);
        }
        if (boardCol < BOARD_SIZE_IN_SQUARES - 1) {
            // Draw right line
            setRed(out);
            out.print(EMPTY.repeat(LINE_WIDTH_IN_CHARS));  //KEY LINE
        }
        out.print(SET_BG_COLOR_DARK_GREY);
    }
    private static void printPlayerLightYellowBackground(PrintStream out, char player) {
        out.print(SET_BG_COLOR_LIGHT_YELLOW);
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


    private static void printMiddleRowOfSquare(PrintStream out,int boardCol,int boardRow) {
        int prefixLength = SQUARE_SIZE_IN_CHARS / 2;
        int suffixLength = SQUARE_SIZE_IN_CHARS - prefixLength - 1;

        if(printFromPerspective== ChessGame.TeamColor.WHITE) {
            printMiddleRowOfWhiteSquare(out,boardCol,boardRow,suffixLength,prefixLength);
        }
        else if (printFromPerspective== ChessGame.TeamColor.BLACK) {
            printMiddleRowOfBlackSquare(out,boardCol,boardRow,suffixLength,prefixLength);
        }
    }

    private static void printMiddleRowOfWhiteSquare(PrintStream out,int boardCol,int boardRow, int suffixLength, int prefixLength) {
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
            if(startPos!=null && highlightLocs.contains(new ChessPosition(boardRow+1,boardCol+1))) {
                out.print(SET_BG_COLOR_GREEN);
                out.print(EMPTY.repeat(prefixLength));
                printPlayerLightGreenBackground(out,player);
                out.print(EMPTY.repeat(suffixLength));
            }
            else if (startPos!=null && startPos.equals(new ChessPosition(boardRow+1,boardCol+1))) {
                out.print(SET_BG_COLOR_LIGHT_YELLOW);
                out.print(EMPTY.repeat(prefixLength));
                printPlayerLightYellowBackground(out,player);
                out.print(EMPTY.repeat(suffixLength));
            }
            else {
                out.print(SET_BG_COLOR_LIGHT_GREY);
                out.print(EMPTY.repeat(prefixLength));
                printPlayerLightBackground(out, player);
                out.print(EMPTY.repeat(suffixLength));

            }
        }
        else {
            if(startPos!=null && highlightLocs.contains(new ChessPosition(boardRow+1,boardCol+1))) {
                out.print(SET_BG_COLOR_DARK_GREEN);
                out.print(EMPTY.repeat(prefixLength));
                printPlayerDarkGreenBackground(out,player);
                out.print(EMPTY.repeat(suffixLength));
            }
            else if (startPos!=null && startPos.equals(new ChessPosition(boardRow+1,boardCol+1))) {
                out.print(SET_BG_COLOR_DARK_YELLOW);
                out.print(EMPTY.repeat(prefixLength));
                printPlayerDarkYellowBackground(out,player);
                out.print(EMPTY.repeat(suffixLength));
            }
            else {
                out.print(SET_BG_COLOR_BLACK);
                out.print(EMPTY.repeat(prefixLength));
                printPlayerDarkBackground(out, player);
                out.print(EMPTY.repeat(suffixLength));
            }

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

    private static void printMiddleRowOfBlackSquare(PrintStream out,int boardCol,int boardRow, int suffixLength, int prefixLength) {
      //flip board with 7-x operations
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
            if(startPos!=null && highlightLocs.contains(new ChessPosition(8-boardRow,8-boardCol))) {
                out.print(SET_BG_COLOR_GREEN);
                out.print(EMPTY.repeat(prefixLength));
                printPlayerLightGreenBackground(out,player);
                out.print(EMPTY.repeat(suffixLength));
            }
            else if (startPos!=null && startPos.equals(new ChessPosition(8-boardRow,8-boardCol))) {
                out.print(SET_BG_COLOR_LIGHT_YELLOW);
                out.print(EMPTY.repeat(prefixLength));
                printPlayerLightYellowBackground(out,player);
                out.print(EMPTY.repeat(suffixLength));
            }
            else {
                out.print(SET_BG_COLOR_LIGHT_GREY);
                out.print(EMPTY.repeat(prefixLength));
                printPlayerLightBackground(out, player);
                out.print(EMPTY.repeat(suffixLength));

            }
        }
        else {
            if(startPos!=null && highlightLocs.contains(new ChessPosition(8-boardRow,8-boardCol))) {
                out.print(SET_BG_COLOR_DARK_GREEN);
                out.print(EMPTY.repeat(prefixLength));
                printPlayerDarkGreenBackground(out,player);
                out.print(EMPTY.repeat(suffixLength));
            }
            else if (startPos!=null && startPos.equals(new ChessPosition(8-boardRow,8-boardCol))) {
                out.print(SET_BG_COLOR_DARK_YELLOW);
                out.print(EMPTY.repeat(prefixLength));
                printPlayerDarkYellowBackground(out,player);
                out.print(EMPTY.repeat(suffixLength));
            }
            else {
                out.print(SET_BG_COLOR_BLACK);
                out.print(EMPTY.repeat(prefixLength));
                printPlayerDarkBackground(out, player);
                out.print(EMPTY.repeat(suffixLength));
            }
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
    private static void printAllOtherRowsOfSquare(PrintStream out, int boardCol, int boardRow) {
            if(printFromPerspective== ChessGame.TeamColor.WHITE) {
                if(boardCol==0) {
                    //print side column line
                    out.print(SET_BG_COLOR_DARK_GREY);
                    out.print(SET_TEXT_COLOR_BLACK);
                    out.print("   ");
                }
                if (((boardRow + 1) % 2) != ((boardCol + 1) % 2)) {
                    if(startPos!=null && highlightLocs.contains(new ChessPosition(boardRow+1,boardCol+1))) {
                        out.print(SET_BG_COLOR_GREEN);
                    }
                    else if (startPos!=null && startPos.equals(new ChessPosition(boardRow+1,boardCol+1))) {
                        out.print(SET_BG_COLOR_LIGHT_YELLOW);
                    }
                    else {
                        out.print(SET_BG_COLOR_LIGHT_GREY);
                    }
                }
                else {
                    if(startPos!=null && highlightLocs.contains(new ChessPosition(boardRow+1,boardCol+1))) {
                        out.print(SET_BG_COLOR_DARK_GREEN);
                    }
                    else if (startPos!=null && startPos.equals(new ChessPosition(boardRow+1,boardCol+1))) {
                        out.print(SET_BG_COLOR_DARK_YELLOW);
                    }
                    else {
                        out.print(SET_BG_COLOR_BLACK);
                    }
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
                    if(startPos!=null && highlightLocs.contains(new ChessPosition(8-boardRow,8-boardCol))) {
                        out.print(SET_BG_COLOR_GREEN);
                    }
                    else if (startPos!=null && startPos.equals(new ChessPosition(8-boardRow,8-boardCol))) {
                        out.print(SET_BG_COLOR_LIGHT_YELLOW);
                    }
                    else {
                        out.print(SET_BG_COLOR_LIGHT_GREY);
                    }
                }
                else {
                    if(startPos!=null && highlightLocs.contains(new ChessPosition(8-boardRow,8-boardCol))) {
                        out.print(SET_BG_COLOR_DARK_GREEN);
                    }
                    else if (startPos!=null && startPos.equals(new ChessPosition(8-boardRow,8-boardCol))) {
                        out.print(SET_BG_COLOR_DARK_YELLOW);
                    }
                    else {
                        out.print(SET_BG_COLOR_BLACK);
                    }
                }
            }

            out.print(EMPTY.repeat(SQUARE_SIZE_IN_CHARS));  //KEY LINE
    }

    private static void printPlayerDarkYellowBackground(PrintStream out, char player) {
        out.print(SET_BG_COLOR_DARK_YELLOW);
        printPlayer(out,player);
    }


    private static void printPlayerDarkGreenBackground(PrintStream out, char player) {
        out.print(SET_BG_COLOR_DARK_GREEN);
        printPlayer(out,player);
    }

    private static void printPlayerLightGreenBackground(PrintStream out, char player) {
        out.print(SET_BG_COLOR_GREEN);
        printPlayer(out,player);
    }

    private static void setRed(PrintStream out) {
        out.print(SET_BG_COLOR_RED);
        out.print(SET_TEXT_COLOR_RED);
    }


    private static void printPlayerLightBackground(PrintStream out, char player) {
        out.print(SET_BG_COLOR_LIGHT_GREY);
        printPlayer(out,player);
    }

    private static void printPlayerDarkBackground(PrintStream out, char player) {
        out.print(SET_BG_COLOR_BLACK);
        printPlayer(out,player);
    }

    private static void printPlayer(PrintStream out, char player) {
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
