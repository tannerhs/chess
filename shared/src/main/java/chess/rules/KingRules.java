package chess.rules;

import chess.ChessBoard;
import chess.ChessMove;
import chess.ChessPosition;

import java.util.Collection;

public class KingRules extends PieceRules{
    public KingRules(ChessBoard board, ChessPosition currentPosition) {
        super(board, currentPosition);
    }

    @Override
    public Collection<ChessMove> getValidMoves(ChessBoard board, ChessPosition currentPosition) {
        moveDownOne();
        moveLeftOne();
        moveUpOne();
        moveRightOne();
        moveLefttUpOne();
        moveRightDownOne();
        moveRightUpOne();
        moveLeftDownOne();
        return validMoves;
    }
}
