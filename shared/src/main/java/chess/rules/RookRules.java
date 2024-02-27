package chess.rules;

import chess.ChessBoard;
import chess.ChessMove;
import chess.ChessPosition;

import java.util.Collection;

public class RookRules extends PieceRules {

    public RookRules(ChessBoard board, ChessPosition currentPosition) {
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
