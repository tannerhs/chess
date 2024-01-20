package chess;

import java.util.Collection;
import java.util.HashSet;

import chess.ChessPiece.PieceType;

import static chess.ChessBoard.*;

public class KnightRules extends ChessPieceRule {
    PieceType pieceType = PieceType.KNIGHT;

    public KnightRules(ChessPosition currentPosition, ChessBoard board, ChessGame.TeamColor team) {
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


        //end set of instructions common to all piece rules

        //move right one and up two
        moveRow+=2;
        moveCol++;
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

        //move left one and up two
        moveRow = pieceRow;  //reset to starting position
        moveCol = pieceCol;  //reset to starting position
        moveRow+=2;
        moveCol--;
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

        //move up one and right two
        moveRow = pieceRow;  //reset to starting position
        moveCol = pieceCol;  //reset to starting position
        moveRow++;
        moveCol+=2;
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

        //move up one and left two
        moveRow = pieceRow;  //reset to starting position
        moveCol = pieceCol;  //reset to starting position
        moveRow++;
        moveCol-=2;
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

        //move down one and right two
        moveRow = pieceRow;  //reset to starting position
        moveCol = pieceCol;  //reset to starting position
        moveRow--;
        moveCol+=2;
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

        //move down one and left two
        moveRow = pieceRow;  //reset to starting position
        moveCol = pieceCol;  //reset to starting position
        moveRow--;
        moveCol-=2;
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

        //move right one and down two
        moveRow = pieceRow;  //reset to starting position
        moveCol = pieceCol;  //reset to starting position
        moveRow-=2;
        moveCol++;
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

        //move left one and down two
        moveRow = pieceRow;  //reset to starting position
        moveCol = pieceCol;  //reset to starting position
        moveRow-=2;
        moveCol--;
        endPosition = new ChessPosition(moveRow,moveCol);
        if(endPosition.validPosition()){
            if(board.getPiece(endPosition)==null || board.getPiece(endPosition).getTeamColor()!=team) {
                validMoves.add(new ChessMove(currentPosition,endPosition,null));
                //error checking
                System.out.print("case 8: ");
                System.out.print("{");
                System.out.print(moveRow);
                System.out.print(",");
                System.out.print(moveCol);
                System.out.println("}");
            }
        }

        return validMoves;
    }
}
