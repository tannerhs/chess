package chess;

import java.util.Collection;

public class BishopRules extends PieceRules {

    BishopRules(ChessBoard board, ChessPosition currentPosition) {
        super(board, currentPosition);
    }

    @Override
    public Collection<ChessMove> getValidMoves(ChessBoard board, ChessPosition currentPosition) {
        return diagnolMoves();
        //return validMoves;
    }
}
