package chess;

import java.util.Collection;

public class RookRules extends PieceRules {

    RookRules(ChessBoard board, ChessPosition currentPosition) {
        super(board, currentPosition);
    }

    @Override
    public Collection<ChessMove> getValidMoves(ChessBoard board, ChessPosition currentPosition) {
        moveUp();
        moveDown();
        moveRight();
        moveLeft();
        return validMoves;
    }
}
