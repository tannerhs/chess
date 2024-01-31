package chess;

import java.util.Collection;

public class KingRules extends PieceRules{
    KingRules(ChessBoard board, ChessPosition currentPosition) {
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
