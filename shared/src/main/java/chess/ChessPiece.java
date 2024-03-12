package chess;

import chess.rules.*;

import java.util.Collection;
import java.util.HashSet;
import java.util.Objects;


/**
 * Represents a single chess piece
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessPiece {
    ChessGame.TeamColor team;
    PieceType type;

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
        Collection<ChessMove> myValidMoves = new HashSet<>();
        if(!myPosition.validPosition() || board.getPiece(myPosition)==null) {
            return myValidMoves;
        }
        if(board.getPiece(myPosition)==null) {
            return myValidMoves;
        }


        //select proper methods
        if (board.getPiece(myPosition).getPieceType() == PieceType.PAWN) {
            //System.out.println("pawn piece in ChessPiecem.java");
            PawnRules rule6 = new PawnRules(board, myPosition);
            myValidMoves = rule6.getValidMoves(board,myPosition);
        }
        else if (board.getPiece(myPosition).getPieceType() == ChessPiece.PieceType.BISHOP) {
            //System.out.println("bishop piece in ChessPiecem.java");
            BishopRules rule5 = new BishopRules(board,myPosition);
            myValidMoves=rule5.getValidMoves(board, myPosition);
        }
        else if (board.getPiece(myPosition).getPieceType() == ChessPiece.PieceType.ROOK) {
            //System.out.println("bishop piece in ChessPiecem.java");
            RookRules rule5 = new RookRules(board,myPosition);
            myValidMoves=rule5.getValidMoves(board, myPosition);
        }
        else if (board.getPiece(myPosition).getPieceType() == ChessPiece.PieceType.QUEEN) {
            //System.out.println("bishop piece in ChessPiecem.java");
            QueenRules rule5 = new QueenRules(board,myPosition);
            myValidMoves=rule5.getValidMoves(board, myPosition);
        }
        else if (board.getPiece(myPosition).getPieceType() == ChessPiece.PieceType.KING) {
            //System.out.println("bishop piece in ChessPiecem.java");
            KingRules rule5 = new KingRules(board,myPosition);
            myValidMoves=rule5.getValidMoves(board, myPosition);
        }
        else if (board.getPiece(myPosition).getPieceType() == ChessPiece.PieceType.KNIGHT) {
            //System.out.println("bishop piece in ChessPiecem.java");
            KnightRules rule5 = new KnightRules(board,myPosition);
            myValidMoves=rule5.getValidMoves(board, myPosition);
        }

        //myValidMoves.add(new ChessMove(myPosition,new ChessPosition(1,1),null));
        return myValidMoves;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ChessPiece that)) return false;
        return team == that.team && type == that.type;
    }

    @Override
    public int hashCode() {
        return Objects.hash(team, type);
    }

    public String toCharString() {
        if(team== ChessGame.TeamColor.WHITE) {
            if(type==PieceType.PAWN) {
                return " P ";
            }
            else if(type== PieceType.BISHOP) {
                return " B ";
            }
            else if (type==PieceType.KNIGHT) {
                return " N ";
            }
            else if (type==PieceType.ROOK) {
                return " R ";
            }
            else if (type==PieceType.KING) {
                return " K ";
            }
            else if (type==PieceType.QUEEN) {
                return " Q ";
            }
        }
        else if (team== ChessGame.TeamColor.BLACK) {
            if(type==PieceType.PAWN) {
                return " p ";
            }
            else if(type== PieceType.BISHOP) {
                return " b ";
            }
            else if (type==PieceType.KNIGHT) {
                return " n ";
            }
            else if (type==PieceType.ROOK) {
                return " r ";
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

    @Override
    public String toString() {
        return "ChessPiece{" +
                "team=" + team +
                ", type=" + type +
                '}';
    }
}
