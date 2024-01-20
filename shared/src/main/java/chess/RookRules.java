package chess;

import java.util.Collection;
import java.util.HashSet;

import chess.ChessPiece.PieceType;

import static chess.ChessBoard.*;

public class RookRules extends ChessPieceRule {
    PieceType pieceType= PieceType.ROOK;
    public RookRules(ChessPosition currentPosition, ChessBoard board, ChessGame.TeamColor team) {
        super(currentPosition,board,team);
    }

    Collection<ChessMove> getValidMoves() {
        //begin set of instructions common to all piece rules
        Collection<ChessMove> validMoves=new HashSet<>();
        System.out.println("--------------------------------");
        //get current position
        int pieceRow = currentPosition.getRow();
        int pieceCol = currentPosition.getColumn();
        int moveRow=pieceRow;
        int moveCol=pieceCol;

        if(moveCol>BOARD_LENGTH || moveCol<BOTTOM_ROW || moveRow>BOARD_LENGTH || moveRow<BOTTOM_ROW) {
            return validMoves;  //if position is not valid, return empty set
        }

        ChessPosition movePosition= new ChessPosition(moveRow,moveCol);
        boolean keepLooking;
        keepLooking=true;
        //end set of instructions common to all piece rules

        //move up
        while(keepLooking && moveRow<BOARD_LENGTH) {
            moveRow++;
            movePosition=new ChessPosition(moveRow,moveCol);  //make  a new position with updated row and column
            System.out.println("rook move up");

            //begin if statements common to queen, rook and bishop
            if((board.getPiece(movePosition)==null)) {  //if square empty or of opposite team color add move to valid moves
                validMoves.add(new ChessMove(this.currentPosition,movePosition, null));
            }
            else if (board.getPiece(movePosition).getTeamColor()!=team) {
                validMoves.add(new ChessMove(this.currentPosition,movePosition, null));
                keepLooking=false;
                //you can't move past a piece you capture
            }
            else {  //can't move past your own piece
                keepLooking=false;
            }
            //end if statements common to queen, rook and bishop
        }

        //move down
        moveRow=pieceRow;
        moveCol=pieceCol;
        keepLooking=true;
        while(keepLooking && moveRow>BOTTOM_ROW) {
            moveRow--;
            movePosition=new ChessPosition(moveRow,moveCol);  //make  a new position with updated row and column

            System.out.println("rook move down");

            //begin if statements common to queen, rook and bishop
            if((board.getPiece(movePosition)==null)) {  //if square empty or of opposite team color add move to valid moves
                validMoves.add(new ChessMove(this.currentPosition,movePosition, null));
            }
            else if (board.getPiece(movePosition).getTeamColor()!=team) {
                validMoves.add(new ChessMove(this.currentPosition,movePosition, null));
                keepLooking=false;
                //you can't move past a piece you capture
            }
            else {  //can't move past your own piece
                keepLooking=false;
            }
            //end if statements common to queen, rook and bishop
        }

        //move  right
        moveRow=pieceRow;
        moveCol=pieceCol;
        keepLooking=true;
        while(keepLooking && moveCol<RIGHT_COL) {
            moveCol++;
            movePosition=new ChessPosition(moveRow,moveCol);  //make  a new position with updated row and column

            System.out.println("rook moves right");

            //begin if statements common to queen, rook and bishop
            if((board.getPiece(movePosition)==null)) {  //if square empty or of opposite team color add move to valid moves
                validMoves.add(new ChessMove(this.currentPosition,movePosition, null));
            }
            else if (board.getPiece(movePosition).getTeamColor()!=team) {
                validMoves.add(new ChessMove(this.currentPosition,movePosition, null));
                keepLooking=false;
                //you can't move past a piece you capture
            }
            else {  //can't move past your own piece
                keepLooking=false;
            }
            //end if statements common to queen, rook and bishop
        }

        //move left
        moveRow=pieceRow;
        moveCol=pieceCol;
        keepLooking=true;
        while(keepLooking && moveCol>LEFT_COL) {
            moveCol--;
            movePosition=new ChessPosition(moveRow,moveCol);  //make  a new position with updated row and column

            System.out.println("rook move left");

            //begin if statements common to queen, rook and bishop
            if((board.getPiece(movePosition)==null)) {  //if square empty or of opposite team color add move to valid moves
                validMoves.add(new ChessMove(this.currentPosition,movePosition, null));
            }
            else if (board.getPiece(movePosition).getTeamColor()!=team) {
                validMoves.add(new ChessMove(this.currentPosition,movePosition, null));
                keepLooking=false;
                //you can't move past a piece you capture
            }
            else {  //can't move past your own piece
                keepLooking=false;
            }
            //end if statements common to queen, rook and bishop
        }
        System.out.println("*******************");
        return validMoves;
    }






}


