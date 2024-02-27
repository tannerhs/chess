package chess.rules;

import chess.ChessBoard;
import chess.ChessMove;
import chess.ChessPosition;

import java.util.Collection;

public class BishopRules extends PieceRules {

    public BishopRules(ChessBoard board, ChessPosition currentPosition) {
        super(board, currentPosition);
    }

    @Override
    public Collection<ChessMove> getValidMoves(ChessBoard board, ChessPosition currentPosition) {
        return diagnolMoves();
        //return validMoves;
    }

}
