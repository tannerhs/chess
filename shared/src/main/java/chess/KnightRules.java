package chess;

import java.util.Collection;
import java.util.HashSet;

import chess.ChessPiece.PieceType;

import static chess.ChessBoard.*;

public class KnightRules extends ChessPieceRule {
    PieceType pieceType = PieceType.KNIGHT;

    public KnightRules(ChessPosition currentPosition, ChessBoard board, ChessGame.TeamColor team) {
        super(currentPosition,board,team);

    }

    Collection<ChessMove> getValidMoves() {
        //begin set of instructions common to all piece rules
        Collection<ChessMove> validMoves = new HashSet<>();
        System.out.println("--------------------------------");
        //get current position
        int pieceRow = currentPosition.getRow();
        int pieceCol = currentPosition.getColumn();
        int moveRow = pieceRow;
        int moveCol = pieceCol;

        if (moveCol > BOARD_LENGTH || moveCol < BOTTOM_ROW || moveRow > BOARD_LENGTH || moveRow < BOTTOM_ROW) {
            return validMoves;  //if position is not valid, return empty set
        }

        ChessPosition movePosition = new ChessPosition(moveRow, moveCol);
        //end set of instructions common to all piece rules

        //move right one and up two

        //move left one and up two

        //move up one and right two

        //move up one and left two

        //move down one and right two

        //move down one and left two

        //move right one and down two

        //move left one and down two

        return validMoves;
    }
}
