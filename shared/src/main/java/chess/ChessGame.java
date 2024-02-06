package chess;

import java.util.Collection;
import java.util.HashSet;
import java.util.Objects;

import static chess.ChessGame.TeamColor.BLACK;
import static chess.ChessGame.TeamColor.WHITE;
import static chess.ChessPiece.PieceType.KING;
import static chess.ChessPiece.PieceType.PAWN;

/**
 * For a class that can manage a chess game, making moves on a board
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessGame {
    private static final int BOARD_LENGTH = 8;
    private static final int BOARD_WIDTH = 8;
    private static final int PAWN_PROMOTION_ROW_BLACK = 2;
    static final int PAWN_PROMOTION_ROW_WHITE = 7;

    TeamColor team=WHITE;  //initialize to WHITE since white starts
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
    public Collection<ChessMove> validMoves(ChessPosition startPosition) {  //FIXME
        if(board.getPiece(startPosition)==null) {
            return null;
        }
        else {
            Collection<ChessMove> validMoves;
            Collection<ChessMove> pieceMoves =board.getPiece(startPosition).pieceMoves(board,startPosition);
            //remove moves that put you in check or checkmate
            TeamColor teamColor = board.getPiece(startPosition).getTeamColor();
            validMoves = removeNonValidBoardMoves(pieceMoves,teamColor);

            return validMoves;
        }
    }

    private Collection<ChessMove> removeNonValidBoardMoves(Collection<ChessMove> pieceMoves, TeamColor teamColor) {
        if(pieceMoves==null) {
            return null;
        }

        Collection<ChessMove> validMoves = new HashSet<>();  //create copy so you can iterate over one and change the other
        for(ChessMove move: pieceMoves) {
            if(move==null) {
                //validMoves.remove(null);
            }
            else if(movePutsOwnTeamInCheck(move)) {
                //validMoves.remove(move);
            }
            else if (!move.startPosition.validPosition() || !move.endPosition.validPosition()) {
                //validMoves.remove(move);
            }
            else if(moveLeavesTeamInCheck(move)) {
                //validMoves.remove(move);
            }
            else {
                validMoves.add(move);
            }

        }
        return validMoves;
    }

    /**
     * Makes a move in a chess game
     *
     * @param move chess move to preform
     * @throws InvalidMoveException if move is invalid
     */
    public void makeMove(ChessMove move) throws InvalidMoveException {  //check to see if in valid moves
        Collection<ChessMove> validMoves = validMoves(move.startPosition);
        ChessPosition currentPosition  = move.getStartPosition();
        ChessPiece piece = board.getPiece(currentPosition);
        TeamColor pieceTeam = piece.getTeamColor();

        if (validMoves.contains(move) ) {  //FIXME && pieceTeam==this.team\
            if(isPawnPromotionMove(move)) {
                board.addPiece(move.getEndPosition(),new ChessPiece(pieceTeam,move.getPromotionPiece()));
                board.addPiece(move.getStartPosition(),null);
            }
            else {
                board.addPiece(move.getEndPosition(),piece);
                board.addPiece(move.getStartPosition(),null);
            }

        }
        else {
            throw new InvalidMoveException("aaaah invalid move");
        }
    }

    private boolean isPawnPromotionMove(ChessMove move) {
        ChessPiece piece = board.getPiece(move.getStartPosition());
        if(piece.getPieceType()==PAWN && ((piece.getTeamColor()==WHITE && move.getStartPosition().getRow()==PAWN_PROMOTION_ROW_WHITE)||
                (piece.getTeamColor()==BLACK && move.getStartPosition().getRow()==PAWN_PROMOTION_ROW_BLACK))) {
            return true;
        }
        else {
            return false;
        }
    }

    /**
     * Determines if the given team is in check
     *
     * @param teamColor which team to check for check
     * @return True if the specified team is in check
     */
    public boolean isInCheck(TeamColor teamColor) {
        //for each move the other team can make (as determined by pieceMoves)
        //if its end position is the current position of the king of teamColor,
        //then teamColor is in check

        //1) find king of specified color
        ChessPosition kingPosition = findKing(teamColor);
        //
        if(kingPosition==null) {
            System.out.println("kingPos: null");
            return false;  //FIXME?? if return statement not here, then isInCheck tests fail
        }


        //2) see if the moves of any of the other team coincide with that spot
        Collection<ChessMove> enemyMoves= getAllTeamNextMoves(((teamColor== WHITE)? BLACK:WHITE), getAllTeamPieces(team));
        //
        for (ChessMove move: enemyMoves) {
            if(move.getEndPosition().equals(kingPosition)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Determines if the given team is in checkmate
     *
     * @param teamColor which team to check for checkmate
     * @return True if the specified team is in checkmate
     */
    public boolean isInCheckmate(TeamColor teamColor) {
        //if in check and all next moves still leave teamColor team in check...
        if(isInCheck(teamColor)) {
            // see if the moves of teamColor can get it out of check
            Collection<ChessMove> teamNextMoves= getAllTeamNextMoves(teamColor, getAllTeamPieces(teamColor));
            //
            for (ChessMove move: teamNextMoves) {  //make move on cloned board and see if still in check

                try {
                    //1) make move on cloned board
                    ChessGame gameClone = (ChessGame) this.clone();
                    //make move on cloned board to see if it puts team in check
                    ChessPiece piece = gameClone.board.getPiece(move.startPosition);  //get piece
                    gameClone.board.addPiece(move.endPosition,piece);  //put in new location
                    gameClone.board.addPiece(move.startPosition,null);  //remove from old location

                    //2) check is still in check
                    if(!gameClone.isInCheck(teamColor)) {
                        return false;
                    }
                }
                catch (CloneNotSupportedException e) {
                    System.out.println("clone not supported exception");
                }


            }

            return true;
        }
        else {  //cannot be in checkmate if not in check
            return false;
        }
    }



    /**
     * Determines if the given team is in stalemate, which here is defined as having
     * no valid moves
     *
     * @param teamColor which team to check for stalemate
     * @return True if the specified team is in stalemate, otherwise false
     */
    public boolean isInStalemate(TeamColor teamColor) {
        if(isInCheckmate(teamColor)) {
            return false;  //in checkmate
        }
        else if(!isInCheck(teamColor) && !teamHasValidMoves(teamColor)) {
            return true;
        }
        else {
            return false;
        }
        //if for every piece on board of that color there are no valid moves that don't put you in check
        //and you are not already in check, then it is stalemate
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


    public boolean teamHasValidMoves(TeamColor teamColor) {
        for (ChessPosition pos: getAllTeamPieces(teamColor)) {
            if (!validMoves(pos).isEmpty()) {
                return true;
            }
        }
        //go through each piece on team and see if any one has a nonzero amount of validMoves, else
        return false;
    }

    public ChessPosition findKing(TeamColor teamColor) {  //find position of given color of king for checking check and checkmate status
        ChessPosition pos;
        ChessPiece piece;
        ChessPiece kingPiece = new ChessPiece(teamColor,KING);  //piece to evaluate equality against
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
        Collection<ChessPosition> allTeamPositions = new HashSet<>();
        ChessPosition pos;
        ChessPiece piece;
        for(int r=1; r<=BOARD_LENGTH; r++) {  //I already accounted for no 0-indexing in my loop start point
            for(int c=1; c<= BOARD_WIDTH; c++) {
                pos = new ChessPosition(r,c);
                piece = board.getPiece(pos);
                if(piece!=null && piece.getTeamColor()==team) {  //this only checks color for non-null pieces
                    allTeamPositions.add(pos);
                }
            }
        }
        return allTeamPositions;  //useful when computing check to see next move positions of other team
    }


    //takes list of positions of team pieces and returns set of all possible next moves
    // to determine check and checkmate for other team; DOES NOT make sure to remove all moves that
    //put the playing team in check, as that becomes irrelevant once enemy king is dead and that
    //leads to a recursive loop
    Collection<ChessMove> getAllTeamNextMoves(TeamColor team,Collection<ChessPosition> teamPiecePositions) {
        //DO NOT check to make sure moves are valid, that leads to a recursive loop with isInCheck,
            //just use pieceMoves
        Collection<ChessMove> allTeamPositions = new HashSet<>();
        ChessPosition pos;
        ChessPiece piece;
        for(int r=1; r<=BOARD_LENGTH; r++) {  //I already accounted for no 0-indexing in my loop start point
            for(int c=1; c<= BOARD_WIDTH; c++) {
                pos = new ChessPosition(r,c);
                piece = board.getPiece(pos);
                if(piece!=null && piece.getTeamColor()==team) {  //this only checks color for non-null pieces
                    allTeamPositions.addAll(piece.pieceMoves(board, pos));
                }
            }
        }
        return allTeamPositions;  //useful when computing check to see next move positions of other team
    }


    public boolean movePutsOwnTeamInCheck(ChessMove move) {
        try {
            ChessGame gameClone = (ChessGame) this.clone();
            //make move on cloned board to see if it puts team in check
            ChessPiece piece = gameClone.board.getPiece(move.startPosition);  //get piece
            TeamColor teamColor = piece.getTeamColor();  //This is essential since the tests get weird!
            gameClone.board.addPiece(move.endPosition,piece);  //put in new location
            gameClone.board.addPiece(move.startPosition,null);  //remove from old location

            //check to see if moving into check when not currently in it
            if(!this.isInCheck(teamColor) && gameClone.isInCheck(teamColor)) {
                return true;
            }
            else {
                return false;
            }


        }
        catch (CloneNotSupportedException e) {
            System.out.println("clone not supported exception");
        }

        return true;
    }


    //move leaves team in check
    public boolean moveLeavesTeamInCheck(ChessMove move) {
        try {
            ChessGame gameClone = (ChessGame) this.clone();
            //make move on cloned board to see if it puts team in check
            ChessPiece piece = gameClone.board.getPiece(move.startPosition);  //get piece
            TeamColor teamColor = piece.getTeamColor();  //This is essential since the tests get weird!
            gameClone.board.addPiece(move.endPosition,piece);  //put in new location
            gameClone.board.addPiece(move.startPosition,null);  //remove from old location

            //check to see if moving into check when not currently in it
            if(this.isInCheck(teamColor) && gameClone.isInCheck(teamColor)) {
                return true;
            }
            else {
                return false;
            }


        }
        catch (CloneNotSupportedException e) {
            System.out.println("clone not supported exception");
        }

        return true;
    }


    @Override
    protected Object clone() throws CloneNotSupportedException {
        ChessGame clone = new ChessGame();  // = (ChessGame) super.clone();
        try {
            clone.board=(ChessBoard) this.board.clone();
            //team already equals team
        }
        catch (CloneNotSupportedException e) {
            System.out.println("clone not supported");
        }

        return clone;

    }
}


