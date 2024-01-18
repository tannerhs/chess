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
        //
        if(moveCol>BOARD_LENGTH || moveCol<BOTTOM_ROW || moveRow>BOARD_LENGTH || moveRow<BOTTOM_ROW) {
            return validMoves;  //if position is not valid, return empty set
        }

        ChessPosition movePosition= new ChessPosition(moveRow,moveCol);
        boolean keepLooking;
        keepLooking=true;
        //first move up and left
        while((moveRow<BOARD_LENGTH)&&(moveCol>BOTTOM_ROW) && keepLooking) {
            moveRow++;
            moveCol--;
            movePosition=new ChessPosition(moveRow,moveCol);  //make  a new position with updated row and column
            if((board.getPiece(movePosition)==null)) {  //if square empty or of opposite team color add move to valid moves
                validMoves.add(new ChessMove(this.currentPosition,movePosition, null));
                System.out.print("1 new move added at row ");
                System.out.print(moveRow);
                System.out.print(" and col ");
                System.out.println(moveCol);
            }
            else if (board.getPiece(movePosition).getTeamColor()!=team) {
                validMoves.add(new ChessMove(this.currentPosition,movePosition, null));
                keepLooking=false;
                //you can't move past a piece you capture

                System.out.print("1 new move added at row ");
                System.out.print(moveRow);
                System.out.print(" and col ");
                System.out.println(moveCol);
            }
            else {  //can't move past your own piece
                keepLooking=false;
            }
        }

        //next move up and right
        moveRow=pieceRow;
        moveCol=pieceCol;
        keepLooking=true;
        while((moveRow<BOARD_LENGTH)&&(moveCol<BOARD_LENGTH) && keepLooking) {
            moveRow++;
            moveCol++;
            movePosition=new ChessPosition(moveRow,moveCol);  //make  a new position with updated row and column
            if((board.getPiece(movePosition)==null)) {  //if square empty or of opposite team color add move to valid moves
                validMoves.add(new ChessMove(this.currentPosition,movePosition, null));
                System.out.print("2 new move added at row ");
                System.out.print(moveRow);
                System.out.print(" and col ");
                System.out.println(moveCol);
            }
            else if (board.getPiece(movePosition).getTeamColor()!=team) {
                validMoves.add(new ChessMove(this.currentPosition,movePosition, null));
                keepLooking=false;
                //you can't move past a piece you capture

                System.out.print("2 new move added at row ");
                System.out.print(moveRow);
                System.out.print(" and col ");
                System.out.println(moveCol);
            }
            else {  //can't move past your own piece
                keepLooking=false;
            }
        }

        //then down and left
        moveRow=pieceRow;
        moveCol=pieceCol;
        keepLooking=true;
        while((moveRow>BOTTOM_ROW)&&(moveCol>BOTTOM_ROW) && keepLooking) {
            moveRow--;
            moveCol--;
            movePosition=new ChessPosition(moveRow,moveCol);  //make  a new position with updated row and column
            if((board.getPiece(movePosition)==null)) {  //if square empty or of opposite team color add move to valid moves
                validMoves.add(new ChessMove(this.currentPosition,movePosition, null));
                System.out.print("3 new move added at row ");
                System.out.print(moveRow);
                System.out.print(" and col ");
                System.out.println(moveCol);
            }
            else if (board.getPiece(movePosition).getTeamColor()!=team) {
                validMoves.add(new ChessMove(this.currentPosition,movePosition, null));
                keepLooking=false;
                //you can't move past a piece you capture

                System.out.print("3 new move added at row ");
                System.out.print(moveRow);
                System.out.print(" and col ");
                System.out.println(moveCol);
            }
            else {  //can't move past your own piece
                keepLooking=false;
            }
        }

        //finally down then right
        moveRow=pieceRow;
        moveCol=pieceCol;
        keepLooking=true;
        while((moveRow>BOTTOM_ROW)&&(moveCol<BOARD_LENGTH) && keepLooking) {
            moveRow--;
            moveCol++;
            movePosition=new ChessPosition(moveRow,moveCol);  //make  a new position with updated row and column
            if((board.getPiece(movePosition)==null)) {  //if square empty or of opposite team color add move to valid moves
                validMoves.add(new ChessMove(this.currentPosition,movePosition, null));
                System.out.print("4 new move added at row ");
                System.out.print(moveRow);
                System.out.print(" and col ");
                System.out.println(moveCol);
            }
            else if (board.getPiece(movePosition).getTeamColor()!=team) {
                validMoves.add(new ChessMove(this.currentPosition,movePosition, null));
                keepLooking=false;
                //you can't move past a piece you capture

                System.out.print("4 new move added at row ");
                System.out.print(moveRow);
                System.out.print(" and col ");
                System.out.println(moveCol);
            }
            else {  //can't move past your own piece
                keepLooking=false;
            }
        }


        return validMoves;
    }
}
