package chess;

import java.util.Objects;

import static chess.ChessBoard.BOARD_LENGTH;
import static chess.ChessBoard.BOTTOM_ROW;

/**
 * Represents a single square position on a chess board
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessPosition {
    private int row;
    private int col;
    public ChessPosition(int row, int col) {
        if((row<=BOARD_LENGTH) && (row>=BOTTOM_ROW) && (col<=BOARD_LENGTH) && col>=BOTTOM_ROW) {
            this.row=row;
            this.col=col;
        }
        else{
            return;
        }

    }

    /**
     * @return which row this position is in
     * 1 codes for the bottom row
     */
    public int getRow() {
        return row;
    }

    /**
     * @return which column this position is in
     * 1 codes for the left row
     */
    public int getColumn() {
        return col;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ChessPosition that = (ChessPosition) o;
        return row == that.row && col == that.col;
    }

    @Override
    public int hashCode() {
        return Objects.hash(row, col);
    }
}
