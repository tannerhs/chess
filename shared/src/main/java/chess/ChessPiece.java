package chess;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

/**
 * Represents a single chess piece
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessPiece {

    ChessGame.TeamColor team;
    ChessPiece.PieceType type;

    public ChessPiece(ChessGame.TeamColor pieceColor, ChessPiece.PieceType type) {
        this.team=pieceColor;
        this.type=type;
    }

    /**
     * The various different chess piece options
     */
    public enum PieceType {
        KING,
        QUEEN,
        BISHOP,
        KNIGHT,
        ROOK,
        PAWN
    }

    /**
     * @return Which team this chess piece belongs to
     */
    public ChessGame.TeamColor getTeamColor() {
        return team;
    }

    /**
     * @return which type of chess piece this piece is
     */
    public PieceType getPieceType() {
        return type;
    }

    /**
     * Calculates all the positions a chess piece can move to
     * Does not take into account moves that are illegal due to leaving the king in
     * danger
     *
     * @return Collection of valid moves
     */
    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition) {
        ChessPiece currentPiece= board.getPiece(myPosition);
        Collection<ChessMove> validMoves = new HashSet<ChessMove>();

        if(currentPiece.getPieceType()==PieceType.BISHOP) {
            //find valid moves based on current position, add to validMoves
            //call ChessPieceRule child class BishopRules method to get those moves
            //validMoves=method();
        }
        return validMoves;
    }

    public String toString(){
        if(team== ChessGame.TeamColor.WHITE) {
            if(type==PieceType.PAWN) {
                return "P";
            }
            else if(type== PieceType.BISHOP) {
                return "B";
            }
            else if (type==PieceType.KNIGHT) {
                return "N";
            }
            else if (type==PieceType.ROOK) {
                return "R";
            }
            else if (type==PieceType.KING) {
                return "K";
            }
            else if (type==PieceType.QUEEN) {
                return "Q";
            }
        }
        else if (team== ChessGame.TeamColor.BLACK) {
            if(type==PieceType.PAWN) {
                return "p";
            }
            else if(type== PieceType.BISHOP) {
                return "b";
            }
            else if (type==PieceType.KNIGHT) {
                return "n";
            }
            else if (type==PieceType.ROOK) {
                return "r";
            }
            else if (type==PieceType.KING) {
                return "k";
            }
            else if (type==PieceType.QUEEN) {
                return "q";
            }
        }
        return "no valid piece found";

    }
}
