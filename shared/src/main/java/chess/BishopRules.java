package chess;


import java.util.Collection;
import java.util.HashSet;

import static chess.ChessBoard.BOARD_LENGTH;
import static chess.ChessBoard.BOTTOM_ROW;

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
        while((moveRow<BOARD_LENGTH)&&(moveCol>BOTTOM_ROW)) {
            moveRow++;
            moveCol--;
            movePosition=new ChessPosition(moveRow,moveCol);  //make  a new position with updated row and column
            if((board.getPiece(movePosition)==null)) {  //if square empty or of opposite team color add move to valid moves
                validMoves.add(new ChessMove(this.currentPosition,movePosition, ChessPiece.PieceType.BISHOP));
            }
            else {
                validMoves.add(new ChessMove(this.currentPosition,movePosition, ChessPiece.PieceType.BISHOP));
                continue;  //you can't move past a piece you capture or your own piece as a bishop
            }
        }

        //next move up and right
        moveRow=pieceRow;
        moveCol=pieceCol;
        while((moveRow<BOARD_LENGTH)&&(moveCol>BOTTOM_ROW)) {
            moveRow++;
            moveCol++;
            movePosition=new ChessPosition(moveRow,moveCol);  //make  a new position with updated row and column
            if((board.getPiece(movePosition)==null)) {  //if square empty or of opposite team color add move to valid moves
                validMoves.add(new ChessMove(this.currentPosition,movePosition, ChessPiece.PieceType.BISHOP));
            }
            else {
                validMoves.add(new ChessMove(this.currentPosition,movePosition, ChessPiece.PieceType.BISHOP));
                continue;  //you can't move past a piece you capture or your own piece as a bishop
            }
        }

        //then down and left
        moveRow=pieceRow;
        moveCol=pieceCol;
        while((moveRow<BOARD_LENGTH)&&(moveCol>BOTTOM_ROW)) {
            moveRow--;
            moveCol--;
            movePosition=new ChessPosition(moveRow,moveCol);  //make  a new position with updated row and column
            if((board.getPiece(movePosition)==null)) {  //if square empty or of opposite team color add move to valid moves
                validMoves.add(new ChessMove(this.currentPosition,movePosition, ChessPiece.PieceType.BISHOP));
            }
            else {
                validMoves.add(new ChessMove(this.currentPosition,movePosition, ChessPiece.PieceType.BISHOP));
                continue;  //you can't move past a piece you capture or your own piece as a bishop
            }
        }

        //finally down then right
        moveRow=pieceRow;
        moveCol=pieceCol;
        while((moveRow<BOARD_LENGTH)&&(moveCol>BOTTOM_ROW)) {
            moveRow--;
            moveCol++;
            movePosition=new ChessPosition(moveRow,moveCol);  //make  a new position with updated row and column
            if((board.getPiece(movePosition)==null)) {  //if square empty or of opposite team color add move to valid moves
                validMoves.add(new ChessMove(this.currentPosition,movePosition, ChessPiece.PieceType.BISHOP));
            }
            else {
                validMoves.add(new ChessMove(this.currentPosition,movePosition, ChessPiece.PieceType.BISHOP));
                continue;  //you can't move past a piece you capture or your own piece as a bishop
            }
        }

        return validMoves;
    }
}
