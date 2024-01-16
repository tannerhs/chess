package chess;

/**
 * A chessboard that can hold and rearrange chess pieces.
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessBoard {
    public static final int BOARD_LENGTH=8;
    private ChessPiece[][] board = new ChessPiece[BOARD_LENGTH][BOARD_LENGTH];  //make and initialize 2-d array
    public ChessBoard() {
        
    }

    /**
     * Adds a chess piece to the chessboard
     *
     * @param position where to add the piece to
     * @param piece    the piece to add
     */
    public void addPiece(ChessPosition position, ChessPiece piece) {  //FIXME ide says board is never written to

        //board[0][0]=new ChessPiece(piece.getTeamColor(),piece.getPieceType());
        board[position.getRow()][position.getColumn()]=piece;
    }

    /**
     * Gets a chess piece on the chessboard
     *
     * @param position The position to get the piece from
     * @return Either the piece at the position, or null if no piece is at that
     * position
     */
    public ChessPiece getPiece(ChessPosition position) {
        return board[position.getRow()][position.getColumn()];
    }

    /**
     * Sets the board to the default starting board
     * (How the game of chess normally starts)
     */
    public void resetBoard() {
        board = new ChessPiece[BOARD_LENGTH][BOARD_LENGTH];  //is this acceptable with java's automatic garbage collection or should I clear each element from the 2d array?
    }
}
