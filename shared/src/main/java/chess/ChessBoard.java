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

        System.out.println(toString());

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
//
//    public boolean isInCheck(ChessGame.TeamColor teamColor) throws CloneNotSupportedException, InvalidMoveException {
//        //for each move the other team can make that doesn't put them in check,
//        //if its end position is the current position of the king of teamColor,
//        //then teamColor is in check
//
//        //1 find king of specified color
//        ChessPosition kingPosition = findKing(teamColor);
//        //
//        System.out.print("kingPos: ");
//        System.out.print(kingPosition.getRow());
//        System.out.print(",");
//        System.out.println(kingPosition.getColumn());
//        System.out.println();
//        //2 see if the moves of any of the other team coincide with that spot
//        Collection<ChessMove> enemyMoves= getAllTeamNextMoves(((teamColor== WHITE)? BLACK:WHITE), getAllTeamPieces(teamColor));
//        //
//        for (ChessMove move: enemyMoves) {
//            System.out.print("endPos: ");
//            System.out.print(move.getEndPosition().getRow());
//            System.out.print(",");
//            System.out.println(move.getEndPosition().getColumn());
//            if(move.getEndPosition().equals(kingPosition)) {
//                return true;
//            }
//        }
//        return false;
//    }






//
//    public boolean teamHasValidMoves(ChessGame.TeamColor team) throws CloneNotSupportedException, InvalidMoveException {
//        if(getAllTeamNextMoves(team, getAllTeamPieces(team)).size()==0) {
//            return false;
//        }
//        //go through each piece on team and see if any one has a nonzero amount of validMoves, else
//        return true;
//    }
//
//    public ChessPosition findKing(ChessGame.TeamColor team) {  //find position of given color of king for checking check and checkmate status
//        ChessPosition pos;
//        ChessPiece piece;
//        ChessPiece kingPiece = new ChessPiece(team,KING);  //piece to evaluate equality against
//        for(int r=1; r<=BOARD_LENGTH; r++) {  //I already accounted for no 0-indexing in my loop start point
//            for(int c=1; c<= BOARD_WIDTH; c++) {
//                pos = new ChessPosition(r,c);
//                piece = this.getPiece(pos);
//
//                if(kingPiece.equals(piece)) {  //this checks for team and piece equality but takes care of null too
//                    return pos;
//                }
//            }
//        }
//        System.out.println("null value returned by findKing");
//        System.out.println(this.getPiece(new ChessPosition(6,4)));
//        System.out.println(kingPiece);
//        System.out.println(kingPiece.equals(new ChessPosition(6,4)));
//        return null;  //should never happen...
//    }
//
//    Collection<ChessPosition> getAllTeamPieces(ChessGame.TeamColor team) {  //takes given color and gives set of positions of all squares with those team members
//        Collection<ChessPosition> allTeamPositions = new HashSet<>();
//        ChessPosition pos;
//        ChessPiece piece;
//        for(int r=1; r<=BOARD_LENGTH; r++) {  //I already accounted for no 0-indexing in my loop start point
//            for(int c=1; c<= BOARD_WIDTH; c++) {
//                pos = new ChessPosition(r,c);
//                piece = this.getPiece(pos);
//                if(piece!=null && piece.getTeamColor()==team) {  //this only checks color for non-null pieces
//                    allTeamPositions.add(pos);
//                }
//            }
//        }
//        return allTeamPositions;  //useful when computing check to see next move positions of other team
//    }
//
//
//    //takes list of positions of team pieces and returns set of all possible next moves
//    // to determine check and checkmate for other team; also checks to make sure to remove all moves that
//    //put the playing team in check
//    Collection<ChessMove> getAllTeamNextMoves(ChessGame.TeamColor team, Collection<ChessPosition> teamPiecePositions) throws CloneNotSupportedException, InvalidMoveException {
//        //FIXME make sure this move does not put playing team in check
//        Collection<ChessMove> allTeamPositions = new HashSet<>();
//        ChessPosition pos;
//        ChessPiece piece;
//        for(int r=1; r<=BOARD_LENGTH; r++) {  //I already accounted for no 0-indexing in my loop start point
//            for(int c=1; c<= BOARD_WIDTH; c++) {
//                pos = new ChessPosition(r,c);
//                piece = this.getPiece(pos);
//                if(piece!=null && piece.getTeamColor()==team) {  //this only checks color for non-null pieces
//                    for(ChessMove move: piece.pieceMoves(this,pos )) {
//                        //check move to see if it puts playing team in check, FIXME
//                        if(!movePutsOwnTeamInCheck(move,team)) {
//                            allTeamPositions.add(move);
//                        }
//                    }
//                }
//            }
//        }
//        return allTeamPositions;  //useful when computing check to see next move positions of other team
//    }
//

//    public boolean movePutsOwnTeamInCheck(ChessMove move, ChessGame.TeamColor team) throws CloneNotSupportedException, InvalidMoveException {
//        //make clone of board to
//        ChessBoard cloneBoard = (ChessBoard) this.clone();
//        cloneBoard.makeMove(move,team);
//        if(!this.isInCheck(team) && cloneBoard.isInCheck(team)) {  //not in check before move but yes after move
//            return true;
//        }
//        //FIXME, clone board and such
//        return true;
//    }
//
//    /**
//     * Makes a move in a chess game
//     *
//     * @param move chess move to preform
//     * @throws InvalidMoveException if move is invalid
//     */
//    public void makeMove(ChessMove move, ChessGame.TeamColor team) throws InvalidMoveException, CloneNotSupportedException {  //check to see if in valid moves
//        try {  //assume team making move is team variable
//            ChessPiece piece = this.getPiece(move.getStartPosition());
//            if(!validMove(move,team) || !piece.pieceMoves(this, move.getStartPosition()).contains(move)) {
//                throw new InvalidMoveException("invalid move");
//            }
//            Collection<ChessMove> validMoves = validMoves(move.getStartPosition());
//            if (validMoves.contains(move)) {
//                ChessPosition currentPosition  = move.getStartPosition();
//                piece = this.getPiece(currentPosition);
//                this.addPiece(move.getEndPosition(),piece);
//                this.addPiece(move.getStartPosition(),null);
//            }
//            else {
//                throw new InvalidMoveException("aaaah");
//            }
//        } catch (InvalidMoveException e) {
//            System.out.println("invalid move exception");
//        }
//
//    }
//
//    public boolean validMove(ChessMove move, ChessGame.TeamColor team) throws CloneNotSupportedException, InvalidMoveException {
//        if(!move.getEndPosition().validPosition() || !move.getStartPosition().validPosition() || (isPawnPromotionMove(move,team)&&move.getPromotionPiece()==null)
//                || movePutsOwnTeamInCheck(move, team) || moveDoesNotRemoveCheckWhenPossible(move,team)) {  //check if pawn promotion piece is not null if pawn is promoting
//            return false;
//        }
//        else {
//            return true;
//        }
//    }
//
//    public boolean isPawnPromotionMove(ChessMove move, ChessGame.TeamColor team) {  //FIXME, MAGIC NUMBER
//        ChessPiece piece = this.getPiece(move.getStartPosition());
//        if(piece.getPieceType()==PAWN && ((team==WHITE && move.getStartPosition().getRow()==7)||(team==BLACK && move.getStartPosition().getRow()==2))) {
//            return true;
//        }
//        else return false;
//    }
//
//    public boolean moveDoesNotRemoveCheckWhenPossible(ChessMove move, ChessGame.TeamColor team) {
//        //FIXME
//        return true;
//    }
//
//    /**
//     * Gets a valid moves for a piece at the given location
//     *
//     * @param startPosition the piece to get valid moves for
//     * @return Set of valid moves for requested piece, or null if no piece at
//     * startPosition
//     */
//    public Collection<ChessMove> validMoves(ChessPosition startPosition) {  //FIXME
//        Collection<ChessMove> validMoves=this.getPiece(startPosition).pieceMoves(this,startPosition);
//        //remove moves that put you in check or checkmate
//        //FIXME
//        //validMoves = removeNonValidBoardMoves(validMoves);
//        return validMoves;
//    }

    @Override
    protected Object clone() throws CloneNotSupportedException {
        ChessBoard clone = new ChessBoard();  //FIXME MAGIC NUMBERS
        for(int r=1; r<=8; r++) {
            for(int c=1; c<=8; c++) {
                ChessPosition pos = new ChessPosition(r,c);
                clone.addPiece(pos,this.getPiece(pos));
            }
        }
        return clone;
    }
}
