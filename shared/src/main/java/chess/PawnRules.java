package chess;

import java.util.Collection;
import java.util.HashSet;

import static chess.ChessBoard.BOARD_LENGTH;
import static chess.ChessBoard.BOTTOM_ROW;
import static chess.ChessPiece.PieceType.*;

public class PawnRules extends ChessPieceRule {
    public PawnRules(ChessPosition currentPosition, ChessBoard board, ChessGame.TeamColor team) {
        super(currentPosition,board,team);
    }

    Collection<ChessMove> getValidMoves() {
        Collection<ChessMove> validMoves;
        //get current position
        int pieceRow = currentPosition.getRow();
        int pieceCol = currentPosition.getColumn();
        if(team== ChessGame.TeamColor.WHITE) {
            validMoves=getValidWhitePawnMoves(pieceRow,pieceCol);
        }
        else {
            validMoves=getValidBlackPawnMoves(pieceRow,pieceCol);
        }
        return validMoves;

    }



    Collection<ChessMove> getValidWhitePawnMoves(int pieceRow, int pieceCol) {
        Collection<ChessMove> validMoves = new HashSet<>();
        int moveRow = pieceRow;
        int moveCol = pieceCol;

        if (!currentPosition.validPosition()) {
            return validMoves;  //if current position is not valid, return empty set
        }

        if(pieceRow<BOARD_LENGTH -1) {
            if(pieceRow==BOTTOM_ROW+1) {  //extra move: move two spaces
                //up two initial pawn move
                moveRow = pieceRow + 2;
                moveCol = pieceCol;
                ChessPosition endPosition = new ChessPosition(moveRow, moveCol);
                if (endPosition.validPosition() && board.getPiece(endPosition) == null && board.getPiece(new ChessPosition(pieceRow+1,pieceCol))==null ) {
                    validMoves.add(new ChessMove(currentPosition, endPosition, null));
                }
            }

            //move up unless can capture a piece diagonally

            //up move
            moveRow = pieceRow + 1;
            moveCol = pieceCol;
            ChessPosition endPosition = new ChessPosition(moveRow, moveCol);
            if (endPosition.validPosition() && board.getPiece(endPosition) == null) {
                validMoves.add(new ChessMove(currentPosition, endPosition, null));
            }

            //up and left capture move
            moveRow = pieceRow + 1;
            moveCol = pieceCol-1;
            endPosition = new ChessPosition(moveRow, moveCol);
            if (endPosition.validPosition() && board.getPiece(endPosition)!=null && board.getPiece(endPosition).getTeamColor()!=team) {
                validMoves.add(new ChessMove(currentPosition, endPosition, null));
            }

            //up and right capture move
            moveRow = pieceRow + 1;
            moveCol = pieceCol+1;
            endPosition = new ChessPosition(moveRow, moveCol);
            if (endPosition.validPosition() && board.getPiece(endPosition)!=null && board.getPiece(endPosition).getTeamColor()!=team) {
                validMoves.add(new ChessMove(currentPosition, endPosition, null));
            }
        }
        else {  //promotion case
            ChessPiece.PieceType[] promotionOptions={QUEEN,ROOK,BISHOP,KNIGHT};
            for(ChessPiece.PieceType type:promotionOptions) {
                //move up unless can capture a piece diagonally

                //up move
                moveRow = pieceRow + 1;
                moveCol = pieceCol;
                ChessPosition endPosition = new ChessPosition(moveRow, moveCol);
                if (endPosition.validPosition() && board.getPiece(endPosition) == null) {
                    validMoves.add(new ChessMove(currentPosition, endPosition, type));
                }

                //up and left capture move
                moveRow = pieceRow + 1;
                moveCol = pieceCol-1;
                endPosition = new ChessPosition(moveRow, moveCol);
                if (endPosition.validPosition() && board.getPiece(endPosition)!=null && board.getPiece(endPosition).getTeamColor()!=team) {
                    validMoves.add(new ChessMove(currentPosition, endPosition, type));
                }

                //up and right capture move
                moveRow = pieceRow + 1;
                moveCol = pieceCol+1;
                endPosition = new ChessPosition(moveRow, moveCol);
                if (endPosition.validPosition() && board.getPiece(endPosition)!=null && board.getPiece(endPosition).getTeamColor()!=team) {
                    validMoves.add(new ChessMove(currentPosition, endPosition, type));
                }
            }
        }


        return validMoves;
    }

    Collection<ChessMove> getValidBlackPawnMoves(int pieceRow, int pieceCol) {
        Collection<ChessMove> validMoves = new HashSet<>();
        int moveRow = pieceRow;
        int moveCol = pieceCol;

        if (!currentPosition.validPosition()) {
            return validMoves;  //if current position is not valid, return empty set
        }

        if(pieceRow>BOTTOM_ROW+1) {
            if(pieceRow==BOARD_LENGTH-1) {  //extra move: move two spaces
                //down two initial pawn move
                moveRow = pieceRow - 2;
                moveCol = pieceCol;
                ChessPosition endPosition = new ChessPosition(moveRow, moveCol);
                if (endPosition.validPosition() && board.getPiece(endPosition) == null  && board.getPiece(new ChessPosition(pieceRow-1,pieceCol))==null) {
                    validMoves.add(new ChessMove(currentPosition, endPosition, null));
                }
            }

            //move down unless can capture a piece diagonally

            //up move
            moveRow = pieceRow - 1;
            moveCol = pieceCol;
            ChessPosition endPosition = new ChessPosition(moveRow, moveCol);
            if (endPosition.validPosition() && board.getPiece(endPosition) == null) {
                validMoves.add(new ChessMove(currentPosition, endPosition, null));
            }

            //up and left capture move
            moveRow = pieceRow - 1;
            moveCol = pieceCol-1;
            endPosition = new ChessPosition(moveRow, moveCol);
            if (endPosition.validPosition() && board.getPiece(endPosition)!=null && board.getPiece(endPosition).getTeamColor()!=team) {
                validMoves.add(new ChessMove(currentPosition, endPosition, null));
            }

            //up and right capture move
            moveRow = pieceRow - 1;
            moveCol = pieceCol+1;
            endPosition = new ChessPosition(moveRow, moveCol);
            if (endPosition.validPosition() && board.getPiece(endPosition)!=null && board.getPiece(endPosition).getTeamColor()!=team) {
                validMoves.add(new ChessMove(currentPosition, endPosition, null));
            }
        }
        else {  //promotion case
            ChessPiece.PieceType[] promotionOptions={QUEEN,ROOK,BISHOP,KNIGHT};
            for(ChessPiece.PieceType type:promotionOptions) {
                //move up unless can capture a piece diagonally

                //up move
                moveRow = pieceRow - 1;
                moveCol = pieceCol;
                ChessPosition endPosition = new ChessPosition(moveRow, moveCol);
                if (endPosition.validPosition() && board.getPiece(endPosition) == null) {
                    validMoves.add(new ChessMove(currentPosition, endPosition, type));
                }

                //up and left capture move
                moveRow = pieceRow - 1;
                moveCol = pieceCol-1;
                endPosition = new ChessPosition(moveRow, moveCol);
                if (endPosition.validPosition() && board.getPiece(endPosition)!=null && board.getPiece(endPosition).getTeamColor()!=team) {
                    validMoves.add(new ChessMove(currentPosition, endPosition, type));
                }

                //up and right capture move
                moveRow = pieceRow - 1;
                moveCol = pieceCol+1;
                endPosition = new ChessPosition(moveRow, moveCol);
                if (endPosition.validPosition() && board.getPiece(endPosition)!=null && board.getPiece(endPosition).getTeamColor()!=team) {
                    validMoves.add(new ChessMove(currentPosition, endPosition, type));
                }
            }
        }


        return validMoves;
    }



}
