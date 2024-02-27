package chess.rules;

import chess.ChessBoard;
import chess.ChessMove;
import chess.ChessPosition;

import java.util.Collection;

import static chess.ChessGame.TeamColor.WHITE;

public class PawnRules extends PieceRules {

    public PawnRules(ChessBoard board, ChessPosition currentPosition) {
        super(board,currentPosition);
    }
    @Override
    public Collection<ChessMove> getValidMoves(ChessBoard board, ChessPosition currentPosition) {
        Collection<ChessMove> myValidMoves=getPawnMoves(board,currentPosition);
        return myValidMoves;
    }

    public Collection<ChessMove> getPawnMoves(ChessBoard board, ChessPosition currentPosition) {
        //System.out.println("pawn moves reached");
        int currentRow = currentPosition.getRow();
        int currentCol = currentPosition.getColumn();
        int moveRow = currentRow;
        int moveCol = currentCol;
        ChessPosition movePosition;

        if(piece.getTeamColor()==WHITE) {
            if(currentRow==2) {  //first move
                moveRow++;
                movePosition = new ChessPosition(moveRow,moveCol);
                if(movePosition.validPosition() && board.getPiece(movePosition)==null) {
                    validMoves.add(new ChessMove(currentPosition,movePosition,null));
                    //now see if can go two spaces
                    moveRow++;
                    movePosition=new ChessPosition(moveRow,moveCol);
                    if(movePosition.validPosition() && board.getPiece(movePosition)==null) {
                        validMoves.add(new ChessMove(currentPosition,movePosition,null));
                    }
                }
                //diagnol moves attacking only if piece there
                getDiagnolPawnMovesWhite();
            }
            else if(currentRow==7) {
                moveRow++;
                movePosition = new ChessPosition(moveRow,moveCol);
                if(movePosition.validPosition() && board.getPiece(movePosition)==null) {
                    promotionMove(movePosition);
                }
                moveCol++; //diagnol attack one
                movePosition = new ChessPosition(moveRow,moveCol);

                if (board.getPiece(movePosition)!=null && piece.getTeamColor()!=board.getPiece(movePosition).getTeamColor()) {
                    promotionMove(movePosition);
                }
                moveCol-=2;  //second diagnol promotoion
                movePosition = new ChessPosition(moveRow,moveCol);
                if (board.getPiece(movePosition)!=null && piece.getTeamColor()!=board.getPiece(movePosition).getTeamColor()) {
                    promotionMove(movePosition);
                }

            }
            else {  //middle of board
                moveRow++;
                movePosition = new ChessPosition(moveRow,moveCol);
                if(movePosition.validPosition() && board.getPiece(movePosition)==null) {
                    validMoves.add(new ChessMove(currentPosition,movePosition,null));
                }
                moveCol=currentCol+1; //diagnol attack one
                movePosition = new ChessPosition(moveRow,moveCol);
                if (board.getPiece(movePosition)!=null && piece.getTeamColor()!=board.getPiece(movePosition).getTeamColor()) {
                    validMoves.add(new ChessMove(currentPosition,movePosition,null));

                }
                moveCol=currentCol-1;  //second diagnol promotoion
                movePosition = new ChessPosition(moveRow,moveCol);
                if (board.getPiece(movePosition)!=null && piece.getTeamColor()!=board.getPiece(movePosition).getTeamColor()) {
                    validMoves.add(new ChessMove(currentPosition,movePosition,null));
                }
            }
        }
        else {  //if black piece
            if(currentRow==7) {  //first move
                moveRow--;
                movePosition = new ChessPosition(moveRow,moveCol);
                if(movePosition.validPosition() && board.getPiece(movePosition)==null) {
                    validMoves.add(new ChessMove(currentPosition,movePosition,null));
                    //now see if can go two spaces
                    moveRow--;
                    movePosition=new ChessPosition(moveRow,moveCol);
                    if(movePosition.validPosition() && board.getPiece(movePosition)==null) {
                        validMoves.add(new ChessMove(currentPosition,movePosition,null));
                    }

                }
                //diagnol moves attacking only if piece there
                getDiagnolPawnMovesBlack();
            }
            else if(currentRow==2) {
                moveRow=currentRow-1;
                movePosition = new ChessPosition(moveRow,moveCol);
                if(movePosition.validPosition() && board.getPiece(movePosition)==null) {
                    promotionMove(movePosition);
                }
                moveCol++; //diagnol attack one
                movePosition = new ChessPosition(moveRow,moveCol);
                if (board.getPiece(movePosition)!=null && piece.getTeamColor()!=board.getPiece(movePosition).getTeamColor()) {
                    promotionMove(movePosition);
                }
                moveCol-=2;  //second diagnol promotoion
                movePosition = new ChessPosition(moveRow,moveCol);
                if (board.getPiece(movePosition)!=null && piece.getTeamColor()!=board.getPiece(movePosition).getTeamColor()) {
                    promotionMove(movePosition);
                }
            }
            else {  //middle of board
                moveRow--;
                movePosition = new ChessPosition(moveRow,moveCol);
                if(movePosition.validPosition() && board.getPiece(movePosition)==null) {
                    validMoves.add(new ChessMove(currentPosition,movePosition,null));
                }
                moveCol++; //diagnol attack one
                movePosition = new ChessPosition(moveRow,moveCol);
                if (board.getPiece(movePosition)!=null && piece.getTeamColor()!=board.getPiece(movePosition).getTeamColor()) {
                    validMoves.add(new ChessMove(currentPosition,movePosition,null));
                }
                moveCol-=2;  //second diagnol promotoion
                movePosition = new ChessPosition(moveRow,moveCol);
                if (board.getPiece(movePosition)!=null && piece.getTeamColor()!=board.getPiece(movePosition).getTeamColor()) {
                    validMoves.add(new ChessMove(currentPosition,movePosition,null));
                }
            }
        }
        return validMoves;
    }

    public Collection<ChessMove> getDiagnolPawnMovesWhite() {
        int currentRow = currentPosition.getRow();
        int currentCol = currentPosition.getColumn();
        int moveRow = currentRow;
        int moveCol = currentCol;
        ChessPosition movePosition;
        moveRow++;

        moveCol--;
        movePosition=new ChessPosition(moveRow,moveCol);
        if(movePosition.validPosition() && board.getPiece(movePosition)!=null && board.getPiece(movePosition).getTeamColor()!= piece.getTeamColor()) {
            validMoves.add(new ChessMove(currentPosition,movePosition,null));
        }
        moveCol+=2;
        movePosition=new ChessPosition(moveRow,moveCol);
        if(movePosition.validPosition() && board.getPiece(movePosition)!=null && board.getPiece(movePosition).getTeamColor()!= piece.getTeamColor()) {
            validMoves.add(new ChessMove(currentPosition,movePosition,null));
        }
        return validMoves;
    }

    public Collection<ChessMove> getDiagnolPawnMovesBlack() {
        int currentRow = currentPosition.getRow();
        int currentCol = currentPosition.getColumn();
        int moveRow = currentRow;
        int moveCol = currentCol;
        ChessPosition movePosition;
        moveRow--;
        moveCol--;
        movePosition=new ChessPosition(moveRow,moveCol);
        if(movePosition.validPosition() && board.getPiece(movePosition)!=null && board.getPiece(movePosition).getTeamColor()!= piece.getTeamColor()) {
            validMoves.add(new ChessMove(currentPosition,movePosition,null));
        }
        moveCol+=2;
        movePosition=new ChessPosition(moveRow,moveCol);
        if(movePosition.validPosition() && board.getPiece(movePosition)!=null && board.getPiece(movePosition).getTeamColor()!= piece.getTeamColor()) {
            validMoves.add(new ChessMove(currentPosition,movePosition,null));
        }
        return validMoves;
    }


}
