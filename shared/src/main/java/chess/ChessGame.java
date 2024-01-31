package chess;

import java.util.Collection;
import java.util.HashSet;
import java.util.Objects;

import static chess.ChessPiece.PieceType.KING;

/**
 * For a class that can manage a chess game, making moves on a board
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessGame {
    private static final int BOARD_LENGTH = 8;
    private static final int BOARD_WIDTH = 8;

    TeamColor team;
    ChessBoard board;
    public ChessGame() {

    }

    /**
     * @return Which team's turn it is
     */
    public TeamColor getTeamTurn() {
        return team;
    }

    /**
     * Set's which teams turn it is
     *
     * @param team the team whose turn it is
     */
    public void setTeamTurn(TeamColor team) {
        this.team=team;
    }

    /**
     * Enum identifying the 2 possible teams in a chess game
     */
    public enum TeamColor {
        WHITE,
        BLACK
    }

    /**
     * Gets a valid moves for a piece at the given location
     *
     * @param startPosition the piece to get valid moves for
     * @return Set of valid moves for requested piece, or null if no piece at
     * startPosition
     */
    public Collection<ChessMove> validMoves(ChessPosition startPosition) {
        Collection<ChessMove> validMoves=board.getPiece(startPosition).pieceMoves(board,startPosition);
        //remove moves that put you in check or checkmate
        return validMoves;
    }

    /**
     * Makes a move in a chess game
     *
     * @param move chess move to preform
     * @throws InvalidMoveException if move is invalid
     */
    public void makeMove(ChessMove move) throws InvalidMoveException {  //check to see if in valid moves
        try {
            Collection<ChessMove> validMoves = validMoves(move.startPosition);
            if (validMoves.contains(move)) {
                ChessPosition currentPosition  = move.getStartPosition();
                ChessPiece piece = board.getPiece(currentPosition);
                board.addPiece(move.getEndPosition(),piece);
                board.addPiece(move.getStartPosition(),null);
            }
            else {
                throw new InvalidMoveException("aaaah");
            }
        } catch (InvalidMoveException e) {
            System.out.println("invalid move exception");
        }

    }

    /**
     * Determines if the given team is in check
     *
     * @param teamColor which team to check for check
     * @return True if the specified team is in check
     */
    public boolean isInCheck(TeamColor teamColor) {
        //1 find king of specified color
        ChessPosition kingPosition = findKing(team);
        //2 see if the moves of any of the other team coincide with that spot
        getAllTeamNextMoves(team, getAllTeamPieces(team));
        //for (piece:)
        return true;
    }

    /**
     * Determines if the given team is in checkmate
     *
     * @param teamColor which team to check for checkmate
     * @return True if the specified team is in checkmate
     */
    public boolean isInCheckmate(TeamColor teamColor) {

        return true;
    }

    /**
     * Determines if the given team is in stalemate, which here is defined as having
     * no valid moves
     *
     * @param teamColor which team to check for stalemate
     * @return True if the specified team is in stalemate, otherwise false
     */
    public boolean isInStalemate(TeamColor teamColor) {
        if(isInCheck(teamColor)&& !teamHasValidMoves(teamColor)) {
            return false;
        }
        //if for every piece on board of that color there are no valid moves that don't put you in check
        //and you are not already in check, then it is stalemate
        return true;
    }

    /**
     * Sets this game's chessboard with a given board
     *
     * @param board the new board to use
     */
    public void setBoard(ChessBoard board) {
        this.board=board;
    }

    /**
     * Gets the current chessboard
     *
     * @return the chessboard
     */
    public ChessBoard getBoard() {
       return board;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ChessGame chessGame)) return false;
        return team == chessGame.team && Objects.equals(board, chessGame.board);
    }

    @Override
    public int hashCode() {
        return Objects.hash(team, board);
    }


    public boolean teamHasValidMoves(TeamColor team) {

        //go through each piece on team and see if any one has a nonzero amount of validMoves
        return false;
    }

    public ChessPosition findKing(TeamColor team) {  //find position of given color of king for checking check and checkmate status
        ChessPosition pos;
        ChessPiece piece;
        ChessPiece kingPiece = new ChessPiece(team,KING);  //piece to evaluate equality against
        for(int r=1; r<=BOARD_LENGTH; r++) {  //I already accounted for no 0-indexing in my loop start point
            for(int c=1; c<= BOARD_WIDTH; c++) {
                pos = new ChessPosition(r,c);
                piece = board.getPiece(pos);
                if(kingPiece.equals(piece)) {  //this checks for team and piece equality but takes care of null too
                    return pos;
                }
            }
        }
        return null;  //should never happen...
    }

    Collection<ChessPosition> getAllTeamPieces(TeamColor team) {  //takes given color and gives set of positions of all squares with those team members
        return null;  //useful when computing check to see next move positions of other team
    }

    Collection<ChessMove> getAllTeamNextMoves(TeamColor team,Collection<ChessPosition> teamPiecePositions) {  //takes list of positions of team pieces and returns set of all possible moves
        return null;
    }

}
