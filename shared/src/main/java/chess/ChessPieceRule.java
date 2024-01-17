package chess;

import java.util.Collection;

public abstract class ChessPieceRule {
    ChessPosition currentPosition;
    ChessPiece.PieceType pieceType;

    ChessBoard board;

    ChessGame.TeamColor team;

    //no constructor since you will not have instances of this parent class, only the child classes
    //for specific piece types; instances of those classes will be the set of valid moves for that piece
    //type given a starting position

    abstract Collection<ChessMove> getValidMoves();
}


