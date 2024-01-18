package chess;

import java.util.Arrays;

/**
 * A chessboard that can hold and rearrange chess pieces.
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessBoard {
    //be careful, only the boardlength for a 0-indexed array; starting at 1,
    public static final int BOARD_LENGTH=8;
    public static final int BOTTOM_ROW=1;
    private ChessPiece[][] board = new ChessPiece[BOARD_LENGTH][BOARD_LENGTH];  //make and initialize 2-d array
    public ChessBoard() {
        
    }

    /**
     * Adds a chess piece to the chessboard
     *
     * @param position where to add the piece to
     * @param piece    the piece to add
     */
    public void addPiece(ChessPosition position, ChessPiece piece) {  //input positions start at 1,1 and are not 0-indexed
        if (position.getRow()>=1 && position.getRow()<=8 && position.getColumn()>=1 && position.getColumn()<=8) {
            board[(position.getRow() - 1)][(position.getColumn() - 1)] = piece;
        }
    }

    /**
     * Gets a chess piece on the chessboard
     *
     * @param position The position to get the piece from
     * @return Either the piece at the position, or null if no piece is at that
     * position
     */
    public ChessPiece getPiece(ChessPosition position) {  //array holding pieces is 0-indexed, but ChessPositions are not
        if (position!=null) {
            return board[position.getRow()-1][position.getColumn()-1];
        }
        return null;
    }

    /**
     * Sets the board to the default starting board
     * (How the game of chess normally starts)
     */
    public void resetBoard() {
        for(int r=0; r<BOARD_LENGTH; r++) {
            for(int c=0; c<BOARD_LENGTH;c++) {
                board[r][c]=null;
            }
        }
        //set pieces at starting positions
        setStartingPieces();
        return;
    }

    public void setStartingPieces(){  //hardcode starting values; check to see if white or black should be at bottom of board
        ChessPosition currentPosition=new ChessPosition(1,1);
        addPiece(currentPosition,new ChessPiece(ChessGame.TeamColor.WHITE,ChessPiece.PieceType.ROOK));
        currentPosition=new ChessPosition(1,2);
        addPiece(currentPosition,new ChessPiece(ChessGame.TeamColor.WHITE,ChessPiece.PieceType.KNIGHT));
        currentPosition=new ChessPosition(1,3);
        addPiece(currentPosition,new ChessPiece(ChessGame.TeamColor.WHITE,ChessPiece.PieceType.BISHOP));
        currentPosition=new ChessPosition(1,4);
        addPiece(currentPosition,new ChessPiece(ChessGame.TeamColor.WHITE,ChessPiece.PieceType.QUEEN));
        currentPosition=new ChessPosition(1,5);
        addPiece(currentPosition,new ChessPiece(ChessGame.TeamColor.WHITE,ChessPiece.PieceType.KING));
        currentPosition=new ChessPosition(1,6);
        addPiece(currentPosition,new ChessPiece(ChessGame.TeamColor.WHITE,ChessPiece.PieceType.BISHOP));
        currentPosition=new ChessPosition(1,7);
        addPiece(currentPosition,new ChessPiece(ChessGame.TeamColor.WHITE,ChessPiece.PieceType.KNIGHT));
        currentPosition=new ChessPosition(1,8);
        addPiece(currentPosition,new ChessPiece(ChessGame.TeamColor.WHITE,ChessPiece.PieceType.ROOK));

        //black non-pawn pieces
        currentPosition=new ChessPosition(8,1);
        addPiece(currentPosition,new ChessPiece(ChessGame.TeamColor.BLACK,ChessPiece.PieceType.ROOK));
        currentPosition=new ChessPosition(8,2);
        addPiece(currentPosition,new ChessPiece(ChessGame.TeamColor.BLACK,ChessPiece.PieceType.KNIGHT));
        currentPosition=new ChessPosition(8,3);
        addPiece(currentPosition,new ChessPiece(ChessGame.TeamColor.BLACK,ChessPiece.PieceType.BISHOP));
        currentPosition=new ChessPosition(8,4);
        addPiece(currentPosition,new ChessPiece(ChessGame.TeamColor.BLACK,ChessPiece.PieceType.QUEEN));
        currentPosition=new ChessPosition(8,5);
        addPiece(currentPosition,new ChessPiece(ChessGame.TeamColor.BLACK,ChessPiece.PieceType.KING));
        currentPosition=new ChessPosition(8,6);
        addPiece(currentPosition,new ChessPiece(ChessGame.TeamColor.BLACK,ChessPiece.PieceType.BISHOP));
        currentPosition=new ChessPosition(8,7);
        addPiece(currentPosition,new ChessPiece(ChessGame.TeamColor.BLACK,ChessPiece.PieceType.KNIGHT));
        currentPosition=new ChessPosition(8,8);
        addPiece(currentPosition,new ChessPiece(ChessGame.TeamColor.BLACK,ChessPiece.PieceType.ROOK));

        for(int c=1; c<=8; c++) {  //put pawns up
            currentPosition= new ChessPosition(2,c);
            addPiece(currentPosition, new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.PAWN));
            currentPosition= new ChessPosition(7,c);
            addPiece(currentPosition, new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.PAWN));
        }


    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ChessBoard that = (ChessBoard) o;
        return Arrays.deepEquals(board, that.board);
    }

    @Override
    public int hashCode() {
        return Arrays.deepHashCode(board);
    }

    public String toString() {
        StringBuilder output=new StringBuilder("|");
        for(int r=8; r>0; r--) {
            for(int c=1; c<8; c++) {
                ChessPiece currentSpot = getPiece(new ChessPosition(r,c));
                if (currentSpot!=null) {
                    output.append(getPiece(new ChessPosition(r,c)).toString());
                }
                else {
                    output.append(" ");
                }

                output.append("|");
            }
        }
        return output.toString();
    }
}
