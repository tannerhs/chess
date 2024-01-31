package chess;

import java.util.Collection;

public class KnightRules extends PieceRules{
    KnightRules(ChessBoard board, ChessPosition currentPosition) {
        super(board, currentPosition);
    }

    @Override
    public Collection<ChessMove> getValidMoves(ChessBoard board, ChessPosition currentPosition) {
        knightMoves();
        return validMoves;

    }
}
