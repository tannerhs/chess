package chess.rules;

import chess.ChessBoard;
import chess.ChessMove;
import chess.ChessPosition;

import java.util.Collection;

public class PawnRules extends PieceRules {

    public PawnRules(ChessBoard board, ChessPosition currentPosition) {
        super(board,currentPosition);
    }
    @Override
    public Collection<ChessMove> getValidMoves(ChessBoard board, ChessPosition currentPosition) {
        Collection<ChessMove> myValidMoves=getPawnMoves(board,currentPosition);
        return myValidMoves;
    }


}
