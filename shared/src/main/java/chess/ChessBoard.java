package chess;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;

//import static chess.ChessGame.BOARD_LENGTH;
//import static chess.ChessGame.BOARD_WIDTH;
import static chess.ChessGame.TeamColor.BLACK;
import static chess.ChessGame.TeamColor.WHITE;
import static chess.ChessPiece.PieceType.*;

/**
 * A chessboard that can hold and rearrange chess pieces.
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessBoard {
    ChessPiece[][] board;
    public ChessBoard() {
        board = new ChessPiece[8][8];
    }
    public final static int BOARD_LENGTH =8;
    public final static int BOARD_WIDTH =8;

    /**
     * Adds a chess piece to the chessboard
     *
     * @param position where to add the piece to
     * @param piece    the piece to add
     */
    public void addPiece(ChessPosition position, ChessPiece piece) {
        if (position.validPosition()) {
            board[position.getRow()-1][position.getColumn()-1]=piece;
        }
    }

    /**
     * Gets a chess piece on the chessboard
     *
     * @param position The position to get the piece from
     * @return Either the piece at the position, or null if no piece is at that
     * position
     */
    public ChessPiece getPiece(ChessPosition position) {
        if(position.validPosition()) {
            return board[position.getRow()-1][position.getColumn()-1];
        }
        return null;
    }

    /**
     * Sets the board to the default starting board
     * (How the game of chess normally starts)
     */
    public void resetBoard() {
        for(int r=0; r<8; r++){
            for (int c=0; c<8; c++) {
                board[r][c]=null;
            }
        }
        setStartBoard();
    }

    public void setStartBoard() {

        //set white non-pawn pieces
        this.addPiece(new ChessPosition(1,1), new ChessPiece(WHITE,ROOK));
        this.addPiece(new ChessPosition(1,2), new ChessPiece(WHITE,KNIGHT));
        this.addPiece(new ChessPosition(1,3), new ChessPiece(WHITE,BISHOP));
        this.addPiece(new ChessPosition(1,4), new ChessPiece(WHITE,QUEEN));
        this.addPiece(new ChessPosition(1,5), new ChessPiece(WHITE,KING));
        this.addPiece(new ChessPosition(1,6), new ChessPiece(WHITE,BISHOP));
        this.addPiece(new ChessPosition(1,7), new ChessPiece(WHITE,KNIGHT));
        this.addPiece(new ChessPosition(1,8), new ChessPiece(WHITE,ROOK));

        //set black non-pawn pieces
        this.addPiece(new ChessPosition(8,1), new ChessPiece(BLACK,ROOK));
        this.addPiece(new ChessPosition(8,2), new ChessPiece(BLACK,KNIGHT));
        this.addPiece(new ChessPosition(8,3), new ChessPiece(BLACK,BISHOP));
        this.addPiece(new ChessPosition(8,4), new ChessPiece(BLACK,QUEEN));
        this.addPiece(new ChessPosition(8,5), new ChessPiece(BLACK,KING));
        this.addPiece(new ChessPosition(8,6), new ChessPiece(BLACK,BISHOP));
        this.addPiece(new ChessPosition(8,7), new ChessPiece(BLACK,KNIGHT));
        this.addPiece(new ChessPosition(8,8), new ChessPiece(BLACK,ROOK));

        //set pawns
        int row =2;
        for(int c=1; c<=8;c++){
            this.addPiece(new ChessPosition(row,c),new ChessPiece(WHITE,PAWN));
        }
        row =7;
        for(int c=1; c<=8;c++){
            this.addPiece(new ChessPosition(row,c),new ChessPiece(BLACK,PAWN));
        }

        //System.out.println(toString());

    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ChessBoard that)) return false;
        return Arrays.deepEquals(board, that.board);
    }

    @Override
    public int hashCode() {
        return Arrays.deepHashCode(board);
    }

    @Override
    public String toString() {
        return "ChessBoard{" +
                "board=" + Arrays.deepToString(board) +
                '}';
    }


    @Override
    protected Object clone() throws CloneNotSupportedException {
        ChessBoard clone = new ChessBoard();  //MAGIC NUMBERS ok-ish for this class
        for(int r=1; r<=8; r++) {
            for(int c=1; c<=8; c++) {
                ChessPosition pos = new ChessPosition(r,c);
                clone.addPiece(pos,this.getPiece(pos));
            }
        }
        return clone;
    }

    public ChessPiece[][] getBoardArray() {
        return board;
    }
}
