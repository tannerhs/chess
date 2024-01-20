package chess;

import java.util.Collection;

import static chess.ChessBoard.*;

public abstract class ChessPieceRule {
    ChessPosition currentPosition;
    ChessPiece.PieceType pieceType;

    ChessBoard board;

    ChessGame.TeamColor team;

    //you will not have instances of this parent class, only the child classes
    //for specific piece types; instances of those classes will be the set of valid moves for that piece
    //type given a starting position
    public ChessPieceRule(ChessPosition currentPosition, ChessBoard board, ChessGame.TeamColor team) {
        this.currentPosition = currentPosition;
        this.board = board;
        this.team = team;
    }

    abstract Collection<ChessMove> getValidMoves();

    boolean validMove(ChessMove proposedMove) {
        ChessPosition endPosition=proposedMove.getEndPosition();
        int moveRow=endPosition.getRow();
        int moveCol=endPosition.getColumn();

        //if endPosition not on board or final spot is occupied by the team's own piece, invalid move
        if(!endPosition.validPosition() || board.getPiece(endPosition).getTeamColor()==team) {
            return false;
        }
        else {
            return true;
        }
    }
}


