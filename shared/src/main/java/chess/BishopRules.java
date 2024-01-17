package chess;


import java.util.Collection;
import java.util.HashSet;

public class BishopRules extends ChessPieceRule {  //create a new set of bishop rules for each starting position
    ChessPiece.PieceType pieceType=ChessPiece.PieceType.BISHOP;
    //all other variables defined in parent class

    public BishopRules(ChessPosition currentPosition, ChessBoard board, ChessGame.TeamColor team) {
        this.currentPosition=currentPosition;
        this.board=board;
        this.team=team;
    }

    @Override
    Collection<ChessMove> getValidMoves() {
        Collection<ChessMove> validMoves=new HashSet<>();
        //get current position
        int pieceRow = currentPosition.getRow();
        int pieceCol = currentPosition.getColumn();
        int moveRow=pieceRow;
        int moveCol=pieceCol;
        ChessPosition movePosition= new ChessPosition(moveRow,moveCol);

        //first move up and left
        while((moveRow<8)&&(moveCol>1)) {  //FIXME MAGIC NUMBERS
            moveRow++;
            moveCol++;
            movePosition=new ChessPosition(moveRow,moveCol);  //make  a new position with updated row and column
            if((board.getPiece(movePosition)==null) || (board.getPiece(movePosition).getTeamColor()!=team)) {  //if square empty or of opposite team color add move to valid moves
                validMoves.add(new ChessMove(this.currentPosition,movePosition, ChessPiece.PieceType.BISHOP));
            }
        }
        //next move up and right
        //then down and left
        //finally down then right
        return null;
    }
}
