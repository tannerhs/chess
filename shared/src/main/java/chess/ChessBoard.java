package chess;

/**
 * A chessboard that can hold and rearrange chess pieces.
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessBoard {
    //be careful, only the boardlength for a 0-indexed array; starting at 1,
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
    public void addPiece(ChessPosition position, ChessPiece piece) {  //input positions start at 1,1 and are not 0-indexed

        board[(position.getRow()-1)][(position.getColumn()-1)]=piece;
    }

    /**
     * Gets a chess piece on the chessboard
     *
     * @param position The position to get the piece from
     * @return Either the piece at the position, or null if no piece is at that
     * position
     */
    public ChessPiece getPiece(ChessPosition position) {  //array holding pieces is 0-indexed, but ChessPositions are not

        return board[position.getRow()-1][position.getColumn()-1];
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
        ChessPosition currentPosition=new ChessPosition(8,8);
        addPiece(currentPosition,new ChessPiece(ChessGame.TeamColor.WHITE,ChessPiece.PieceType.ROOK));

    }
}
