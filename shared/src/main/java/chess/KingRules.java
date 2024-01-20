package chess;

import java.util.Collection;
import java.util.HashSet;

public class KingRules extends ChessPieceRule {
    ChessPiece.PieceType pieceType = ChessPiece.PieceType.KING;

    public KingRules(ChessPosition currentPosition, ChessBoard board, ChessGame.TeamColor team) {
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

        if (!currentPosition.validPosition()) {
            return validMoves;  //if current position is not valid, return empty set
        }

        //move up and left
        moveRow=pieceRow+1;
        moveCol=pieceCol-1;
        ChessPosition endPosition = new ChessPosition(moveRow,moveCol);
        if(endPosition.validPosition()){
            if(board.getPiece(endPosition)==null || board.getPiece(endPosition).getTeamColor()!=team) {
                validMoves.add(new ChessMove(currentPosition,endPosition,null));

                //error checking
                System.out.print("case 1: ");
                System.out.print("{");
                System.out.print(moveRow);
                System.out.print(",");
                System.out.print(moveCol);
                System.out.println("}");
            }
        }

        //move up
        moveRow=pieceRow+1;
        moveCol=pieceCol;
        endPosition = new ChessPosition(moveRow,moveCol);
        if(endPosition.validPosition()){
            if(board.getPiece(endPosition)==null || board.getPiece(endPosition).getTeamColor()!=team) {
                validMoves.add(new ChessMove(currentPosition,endPosition,null));

                //error checking
                System.out.print("case 1: ");
                System.out.print("{");
                System.out.print(moveRow);
                System.out.print(",");
                System.out.print(moveCol);
                System.out.println("}");
            }
        }

        //move up and right
        moveRow=pieceRow+1;
        moveCol=pieceCol+1;
        endPosition = new ChessPosition(moveRow,moveCol);
        if(endPosition.validPosition()){
            if(board.getPiece(endPosition)==null || board.getPiece(endPosition).getTeamColor()!=team) {
                validMoves.add(new ChessMove(currentPosition,endPosition,null));

                //error checking
                System.out.print("case 2: ");
                System.out.print("{");
                System.out.print(moveRow);
                System.out.print(",");
                System.out.print(moveCol);
                System.out.println("}");
            }
        }

        //move left
        moveRow=pieceRow;
        moveCol=pieceCol-1;
        endPosition = new ChessPosition(moveRow,moveCol);
        if(endPosition.validPosition()){
            if(board.getPiece(endPosition)==null || board.getPiece(endPosition).getTeamColor()!=team) {
                validMoves.add(new ChessMove(currentPosition,endPosition,null));

                //error checking
                System.out.print("case 3: ");
                System.out.print("{");
                System.out.print(moveRow);
                System.out.print(",");
                System.out.print(moveCol);
                System.out.println("}");
            }
        }

        //move right
        moveRow=pieceRow;
        moveCol=pieceCol+1;
        endPosition = new ChessPosition(moveRow,moveCol);
        if(endPosition.validPosition()){
            if(board.getPiece(endPosition)==null || board.getPiece(endPosition).getTeamColor()!=team) {
                validMoves.add(new ChessMove(currentPosition,endPosition,null));

                //error checking
                System.out.print("case 4: ");
                System.out.print("{");
                System.out.print(moveRow);
                System.out.print(",");
                System.out.print(moveCol);
                System.out.println("}");
            }
        }

        //move down and left
        moveRow=pieceRow-1;
        moveCol=pieceCol-1;
        endPosition = new ChessPosition(moveRow,moveCol);
        if(endPosition.validPosition()){
            if(board.getPiece(endPosition)==null || board.getPiece(endPosition).getTeamColor()!=team) {
                validMoves.add(new ChessMove(currentPosition,endPosition,null));

                //error checking
                System.out.print("case 5: ");
                System.out.print("{");
                System.out.print(moveRow);
                System.out.print(",");
                System.out.print(moveCol);
                System.out.println("}");
            }
        }

        //move down
        moveRow=pieceRow-1;
        moveCol=pieceCol;
        endPosition = new ChessPosition(moveRow,moveCol);
        if(endPosition.validPosition()){
            if(board.getPiece(endPosition)==null || board.getPiece(endPosition).getTeamColor()!=team) {
                validMoves.add(new ChessMove(currentPosition,endPosition,null));

                //error checking
                System.out.print("case 6: ");
                System.out.print("{");
                System.out.print(moveRow);
                System.out.print(",");
                System.out.print(moveCol);
                System.out.println("}");
            }
        }

        //move down and right
        moveRow=pieceRow-1;
        moveCol=pieceCol+1;
        endPosition = new ChessPosition(moveRow,moveCol);
        if(endPosition.validPosition()){
            if(board.getPiece(endPosition)==null || board.getPiece(endPosition).getTeamColor()!=team) {
                validMoves.add(new ChessMove(currentPosition,endPosition,null));

                //error checking
                System.out.print("case 7: ");
                System.out.print("{");
                System.out.print(moveRow);
                System.out.print(",");
                System.out.print(moveCol);
                System.out.println("}");
            }
        }


        //end set of instructions common to all piece rules
        return validMoves;
    }
}
