package chess;

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
    ChessPiece.PieceType type;
    //can't store BishopRules or ChessPieceRule instances as class variables but local variables ok

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
    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition) {  //called in chessBoard or ChessGame
        Collection<ChessMove> validMoves = new HashSet<ChessMove>();
        ChessPieceRule rule;
        if(myPosition==null || board==null) { //error checking
            return validMoves;
        }
        ChessPiece currentPiece= board.getPiece(myPosition);
        if(currentPiece==null) {
            return validMoves;
        }


        if(currentPiece.getPieceType()==PieceType.BISHOP) {
            //find valid moves based on current position, add to validMoves
            //call ChessPieceRule child class BishopRules method to get those moves
            rule=new BishopRules(myPosition,board,this.getTeamColor());
            validMoves=rule.getValidMoves();
        }
        else if(currentPiece.getPieceType()==PieceType.ROOK) {
            //find valid moves based on current position, add to validMoves
            //call ChessPieceRule child class BishopRules method to get those moves
            rule=new RookRules(myPosition,board,this.getTeamColor());
            validMoves=rule.getValidMoves();
        }
        else if (currentPiece.getPieceType()==PieceType.QUEEN) {
            rule=new QueenRules(myPosition,board, this.getTeamColor());
            validMoves=rule.getValidMoves();
        }
        else if (currentPiece.getPieceType()==PieceType.KNIGHT) {
            rule=new KnightRules(myPosition,board, this.getTeamColor());
            validMoves=rule.getValidMoves();
        }
        else if (currentPiece.getPieceType()==PieceType.KING) {
            rule=new KingRules(myPosition,board, this.getTeamColor());
            validMoves=rule.getValidMoves();
        }
        else if (currentPiece.getPieceType()==PieceType.PAWN) {
            rule = new PawnRules(myPosition,board,this.getTeamColor());
            validMoves=rule.getValidMoves();
        }
        else {
            System.out.println("piece type does not exist");
            //throw new InvalidMoveException("piece type does not exist.");
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ChessPiece that = (ChessPiece) o;
        return team == that.team && type == that.type;
    }

    @Override
    public int hashCode() {
        return Objects.hash(team, type);
    }
}
