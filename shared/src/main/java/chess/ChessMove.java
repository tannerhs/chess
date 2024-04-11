package chess;
import java.util.HashMap;
import java.util.Objects;

import static chess.ChessPiece.*;

/**
 * Represents moving a chess piece on a chessboard
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessMove {
    ChessPosition startPosition;
    ChessPosition endPosition;
    PieceType promotionPiece;
    HashMap<Integer, String> columnsByName = new HashMap<>();


    public ChessMove(ChessPosition startPosition, ChessPosition endPosition,
                     ChessPiece.PieceType promotionPiece) {
        this.startPosition=startPosition;
        this.endPosition=endPosition;
        this.promotionPiece=promotionPiece;

        columnsByName.put(1,"a");
        columnsByName.put(2,"b");
        columnsByName.put(3,"c");
        columnsByName.put(4,"d");
        columnsByName.put(5,"e");
        columnsByName.put(6,"f");
        columnsByName.put(7,"g");
        columnsByName.put(8,"h");
    }

    /**
     * @return ChessPosition of starting location
     */
    public ChessPosition getStartPosition() {
        return startPosition;
    }

    /**
     * @return ChessPosition of ending location
     */
    public ChessPosition getEndPosition() {
        return endPosition;
    }

    /**
     * Gets the type of piece to promote a pawn to if pawn promotion is part of this
     * chess move
     *
     * @return Type of piece to promote a pawn to, or null if no promotion
     */
    public ChessPiece.PieceType getPromotionPiece() {
        return promotionPiece;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ChessMove chessMove)) return false;
        return Objects.equals(startPosition, chessMove.startPosition) && Objects.equals(endPosition, chessMove.endPosition) && promotionPiece == chessMove.promotionPiece;
    }

    @Override
    public int hashCode() {
        return Objects.hash(startPosition, endPosition, promotionPiece);
    }



    @Override
    public String toString() {
        String col = columnsByName.get(this.getEndPosition().getColumn());
        String row = Integer.toString(this.getEndPosition().getRow());
        String printedMove = col+row;
        return printedMove;
    }
}
