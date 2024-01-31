package chess;

import java.util.Collection;

public class PawnRules extends PieceRules {

    PawnRules(ChessBoard board, ChessPosition currentPosition) {
        super(board,currentPosition);
    }
    @Override
    public Collection<ChessMove> getValidMoves(ChessBoard board, ChessPosition currentPosition) {
        Collection<ChessMove> myValidMoves=getPawnMoves(board,currentPosition);
        return myValidMoves;
    }


}
