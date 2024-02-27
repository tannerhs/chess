package chess.rules;

import chess.ChessBoard;
import chess.ChessMove;
import chess.ChessPosition;

import java.util.Collection;

public class QueenRules extends PieceRules {
    public QueenRules(ChessBoard board, ChessPosition currentPosition) {
        super(board, currentPosition);
    }

    @Override
    public Collection<ChessMove> getValidMoves(ChessBoard board, ChessPosition currentPosition) {
        diagnolMoves();
        moveUp();
        moveDown();
        moveRight();
        moveLeft();
        return validMoves;
    }
}
