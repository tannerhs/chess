package chess;

import java.util.Collection;
import java.util.HashSet;

import static chess.ChessGame.TeamColor.*;

public abstract class PieceRules {
    Collection<ChessMove> validMoves;
    ChessBoard board;
    ChessPosition currentPosition;
    ChessPiece piece;
    PieceRules(ChessBoard board, ChessPosition currentPosition) {
        this.validMoves=new HashSet<>();
        this.board=board;
        this.currentPosition=currentPosition;
        piece=board.getPiece(currentPosition);
    }

    public abstract Collection<ChessMove> getValidMoves(ChessBoard board, ChessPosition currentPosition);

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

    public Collection<ChessMove> diagnolMoves() {
        //System.out.println("diagnol moves reached");
        diagnolUpLeft();
        diagnolUpRight();
        diagnolDownLeft();
        diagnolDownRight();
        return validMoves;
    }

    public void diagnolUpLeft() {
        int currentRow = currentPosition.getRow();
        int currentCol = currentPosition.getColumn();
        int moveRow = currentRow;
        int moveCol = currentCol;
        ChessPosition movePosition;
        boolean keepLooking=true;

        while(keepLooking && moveRow<8 && moveCol>1) {
            moveRow++;
            moveCol--;
            movePosition=new ChessPosition(moveRow,moveCol);
            if(board.getPiece(movePosition)==null) {
                validMoves.add(new ChessMove(currentPosition,movePosition,null));
            }
            else if (board.getPiece(movePosition).getTeamColor()!= piece.getTeamColor()) {
                validMoves.add(new ChessMove(currentPosition,movePosition,null));
                keepLooking=false;  //stop after taking enemy piece
            }
            else {  //own piece blocking you
                keepLooking=false;
            }
        }
    }


    public void diagnolUpRight() {
        int currentRow = currentPosition.getRow();
        int currentCol = currentPosition.getColumn();
        int moveRow = currentRow;
        int moveCol = currentCol;
        ChessPosition movePosition;
        boolean keepLooking=true;

        while(keepLooking && moveRow<8 && moveCol<8) {
            moveRow++;
            moveCol++;
            movePosition=new ChessPosition(moveRow,moveCol);
            if(board.getPiece(movePosition)==null) {
                validMoves.add(new ChessMove(currentPosition,movePosition,null));
            }
            else if (board.getPiece(movePosition).getTeamColor()!= piece.getTeamColor()) {
                validMoves.add(new ChessMove(currentPosition,movePosition,null));
                keepLooking=false;  //stop after taking enemy piece
            }
            else {  //own piece blocking you
                keepLooking=false;
            }
        }
    }

    public void diagnolDownLeft() {
        int currentRow = currentPosition.getRow();
        int currentCol = currentPosition.getColumn();
        int moveRow = currentRow;
        int moveCol = currentCol;
        ChessPosition movePosition;
        boolean keepLooking=true;

        while(keepLooking && moveRow>1 && moveCol>1) {
            moveRow--;
            moveCol--;
            movePosition=new ChessPosition(moveRow,moveCol);
            if(board.getPiece(movePosition)==null) {
                validMoves.add(new ChessMove(currentPosition,movePosition,null));
            }
            else if (board.getPiece(movePosition).getTeamColor()!= piece.getTeamColor()) {
                validMoves.add(new ChessMove(currentPosition,movePosition,null));
                keepLooking=false;  //stop after taking enemy piece
            }
            else {  //own piece blocking you
                keepLooking=false;
            }
        }
    }

    public void diagnolDownRight() {
        int currentRow = currentPosition.getRow();
        int currentCol = currentPosition.getColumn();
        int moveRow = currentRow;
        int moveCol = currentCol;
        ChessPosition movePosition;
        boolean keepLooking=true;

        while(keepLooking && moveRow>1 && moveCol<8) {
            moveRow--;
            moveCol++;
            movePosition=new ChessPosition(moveRow,moveCol);
            if(board.getPiece(movePosition)==null) {
                validMoves.add(new ChessMove(currentPosition,movePosition,null));
            }
            else if (board.getPiece(movePosition).getTeamColor()!= piece.getTeamColor()) {
                validMoves.add(new ChessMove(currentPosition,movePosition,null));
                keepLooking=false;  //stop after taking enemy piece
            }
            else {  //own piece blocking you
                keepLooking=false;
            }
        }
    }

    public void moveUp() {
        int currentRow = currentPosition.getRow();
        int currentCol = currentPosition.getColumn();
        int moveRow = currentRow;
        int moveCol = currentCol;
        ChessPosition movePosition;
        boolean keepLooking=true;

        while(keepLooking && moveRow<8) {
            moveRow++;
            movePosition=new ChessPosition(moveRow,moveCol);
            if(board.getPiece(movePosition)==null) {
                validMoves.add(new ChessMove(currentPosition,movePosition,null));
            }
            else if (board.getPiece(movePosition).getTeamColor()!= piece.getTeamColor()) {
                validMoves.add(new ChessMove(currentPosition,movePosition,null));
                keepLooking=false;  //stop after taking enemy piece
            }
            else {  //own piece blocking you
                keepLooking=false;
            }
        }
    }


    public void moveDown() {
        int currentRow = currentPosition.getRow();
        int currentCol = currentPosition.getColumn();
        int moveRow = currentRow;
        int moveCol = currentCol;
        ChessPosition movePosition;
        boolean keepLooking=true;

        while(keepLooking && moveRow>1) {
            moveRow--;
            movePosition=new ChessPosition(moveRow,moveCol);
            if(board.getPiece(movePosition)==null) {
                validMoves.add(new ChessMove(currentPosition,movePosition,null));
            }
            else if (board.getPiece(movePosition).getTeamColor()!= piece.getTeamColor()) {
                validMoves.add(new ChessMove(currentPosition,movePosition,null));
                keepLooking=false;  //stop after taking enemy piece
            }
            else {  //own piece blocking you
                keepLooking=false;
            }
        }
    }


    public void moveRight() {
        int currentRow = currentPosition.getRow();
        int currentCol = currentPosition.getColumn();
        int moveRow = currentRow;
        int moveCol = currentCol;
        ChessPosition movePosition;
        boolean keepLooking=true;

        while(keepLooking && moveCol<8) {
            moveCol++;
            movePosition=new ChessPosition(moveRow,moveCol);
            if(board.getPiece(movePosition)==null) {
                validMoves.add(new ChessMove(currentPosition,movePosition,null));
            }
            else if (board.getPiece(movePosition).getTeamColor()!= piece.getTeamColor()) {
                validMoves.add(new ChessMove(currentPosition,movePosition,null));
                keepLooking=false;  //stop after taking enemy piece
            }
            else {  //own piece blocking you
                keepLooking=false;
            }
        }
    }


    public void moveLeft() {
        int currentRow = currentPosition.getRow();
        int currentCol = currentPosition.getColumn();
        int moveRow = currentRow;
        int moveCol = currentCol;
        ChessPosition movePosition;
        boolean keepLooking=true;

        while(keepLooking && moveCol>1) {
            moveCol--;
            movePosition=new ChessPosition(moveRow,moveCol);
            if(board.getPiece(movePosition)==null) {
                validMoves.add(new ChessMove(currentPosition,movePosition,null));
            }
            else if (board.getPiece(movePosition).getTeamColor()!= piece.getTeamColor()) {
                validMoves.add(new ChessMove(currentPosition,movePosition,null));
                keepLooking=false;  //stop after taking enemy piece
            }
            else {  //own piece blocking you
                keepLooking=false;
            }
        }
    }


    public void moveLeftOne() {  //king
        int currentRow = currentPosition.getRow();
        int currentCol = currentPosition.getColumn();
        int moveRow = currentRow;
        int moveCol = currentCol;
        ChessPosition movePosition;

        moveCol--;
        movePosition=new ChessPosition(moveRow,moveCol);
        if(movePosition.validPosition() && board.getPiece(movePosition)==null) {
            validMoves.add(new ChessMove(currentPosition,movePosition,null));
        }
        else if (movePosition.validPosition() && board.getPiece(movePosition).getTeamColor()!= piece.getTeamColor()) {
            validMoves.add(new ChessMove(currentPosition,movePosition,null));
        }
        else {  //own piece blocking you

        }

    }

    public void moveRightOne() {  //king
        int currentRow = currentPosition.getRow();
        int currentCol = currentPosition.getColumn();
        int moveRow = currentRow;
        int moveCol = currentCol;
        ChessPosition movePosition;

        moveCol++;
        movePosition=new ChessPosition(moveRow,moveCol);
        if(movePosition.validPosition() && board.getPiece(movePosition)==null) {
            validMoves.add(new ChessMove(currentPosition,movePosition,null));
        }
        else if (movePosition.validPosition() && board.getPiece(movePosition).getTeamColor()!= piece.getTeamColor()) {
            validMoves.add(new ChessMove(currentPosition,movePosition,null));
        }
        else {  //own piece blocking you

        }

    }

    public void moveRightUpOne() {  //king
        int currentRow = currentPosition.getRow();
        int currentCol = currentPosition.getColumn();
        int moveRow = currentRow;
        int moveCol = currentCol;
        ChessPosition movePosition;

        moveRow++;
        moveCol++;
        movePosition=new ChessPosition(moveRow,moveCol);
        if(movePosition.validPosition() && board.getPiece(movePosition)==null) {
            validMoves.add(new ChessMove(currentPosition,movePosition,null));
        }
        else if (movePosition.validPosition() && board.getPiece(movePosition).getTeamColor()!= piece.getTeamColor()) {
            validMoves.add(new ChessMove(currentPosition,movePosition,null));
        }
        else {  //own piece blocking you

        }

    }

    public void moveRightDownOne() {  //king
        int currentRow = currentPosition.getRow();
        int currentCol = currentPosition.getColumn();
        int moveRow = currentRow;
        int moveCol = currentCol;
        ChessPosition movePosition;

        moveRow--;
        moveCol++;
        movePosition=new ChessPosition(moveRow,moveCol);
        if(movePosition.validPosition() && board.getPiece(movePosition)==null) {
            validMoves.add(new ChessMove(currentPosition,movePosition,null));
        }
        else if (movePosition.validPosition() && board.getPiece(movePosition).getTeamColor()!= piece.getTeamColor()) {
            validMoves.add(new ChessMove(currentPosition,movePosition,null));
        }
        else {  //own piece blocking you

        }

    }

    public void moveLefttUpOne() {  //king
        int currentRow = currentPosition.getRow();
        int currentCol = currentPosition.getColumn();
        int moveRow = currentRow;
        int moveCol = currentCol;
        ChessPosition movePosition;

        moveRow++;
        moveCol--;
        movePosition=new ChessPosition(moveRow,moveCol);
        if(movePosition.validPosition() && board.getPiece(movePosition)==null) {
            validMoves.add(new ChessMove(currentPosition,movePosition,null));
        }
        else if (movePosition.validPosition() && board.getPiece(movePosition).getTeamColor()!= piece.getTeamColor()) {
            validMoves.add(new ChessMove(currentPosition,movePosition,null));
        }
        else {  //own piece blocking you

        }

    }

    public void moveLeftDownOne() {  //king
        int currentRow = currentPosition.getRow();
        int currentCol = currentPosition.getColumn();
        int moveRow = currentRow;
        int moveCol = currentCol;
        ChessPosition movePosition;

        moveRow--;
        moveCol--;
        movePosition=new ChessPosition(moveRow,moveCol);
        if(movePosition.validPosition() && board.getPiece(movePosition)==null) {
            validMoves.add(new ChessMove(currentPosition,movePosition,null));
        }
        else if (movePosition.validPosition() && board.getPiece(movePosition).getTeamColor()!= piece.getTeamColor()) {
            validMoves.add(new ChessMove(currentPosition,movePosition,null));
        }
        else {  //own piece blocking you

        }

    }

    public void moveUpOne() {  //king
        int currentRow = currentPosition.getRow();
        int currentCol = currentPosition.getColumn();
        int moveRow = currentRow;
        int moveCol = currentCol;
        ChessPosition movePosition;

        moveRow++;
        movePosition=new ChessPosition(moveRow,moveCol);
        if(movePosition.validPosition() && board.getPiece(movePosition)==null) {
            validMoves.add(new ChessMove(currentPosition,movePosition,null));
        }
        else if (movePosition.validPosition() && board.getPiece(movePosition).getTeamColor()!= piece.getTeamColor()) {
            validMoves.add(new ChessMove(currentPosition,movePosition,null));
        }
        else {  //own piece blocking you

        }

    }

    public void moveDownOne() {  //king
        int currentRow = currentPosition.getRow();
        int currentCol = currentPosition.getColumn();
        int moveRow = currentRow;
        int moveCol = currentCol;
        ChessPosition movePosition;

        moveRow--;
        movePosition=new ChessPosition(moveRow,moveCol);
        if(movePosition.validPosition() && board.getPiece(movePosition)==null) {
            validMoves.add(new ChessMove(currentPosition,movePosition,null));
        }
        else if (movePosition.validPosition() && board.getPiece(movePosition).getTeamColor()!= piece.getTeamColor()) {
            validMoves.add(new ChessMove(currentPosition,movePosition,null));
        }
        else {  //own piece blocking you

        }

    }

    public void knightMoves(){
        int currentRow = currentPosition.getRow();
        int currentCol = currentPosition.getColumn();
        ChessPosition movePosition;


        //move right one up 2
        int moveRow = currentRow;
        int moveCol = currentCol;
        moveRow+=2;
        moveCol+=1;
        movePosition=new ChessPosition(moveRow,moveCol);
        if(movePosition.validPosition() && (board.getPiece(movePosition)==null || board.getPiece(movePosition).getTeamColor()!= piece.getTeamColor())) {
            validMoves.add(new ChessMove(currentPosition,movePosition,null));
        }

        //move left one up 2
        moveRow = currentRow;
        moveCol = currentCol;
        moveRow+=2;
        moveCol-=1;
        movePosition=new ChessPosition(moveRow,moveCol);
        if(movePosition.validPosition() && (board.getPiece(movePosition)==null || board.getPiece(movePosition).getTeamColor()!= piece.getTeamColor())) {
            validMoves.add(new ChessMove(currentPosition,movePosition,null));
        }

        //move left one down 2
        moveRow = currentRow;
        moveCol = currentCol;
        moveRow-=2;
        moveCol-=1;
        movePosition=new ChessPosition(moveRow,moveCol);
        if(movePosition.validPosition() && (board.getPiece(movePosition)==null || board.getPiece(movePosition).getTeamColor()!= piece.getTeamColor())) {
            validMoves.add(new ChessMove(currentPosition,movePosition,null));
        }

        //move right one down two
        moveRow = currentRow;
        moveCol = currentCol;
        moveRow-=2;
        moveCol+=1;
        movePosition=new ChessPosition(moveRow,moveCol);
        if(movePosition.validPosition() && (board.getPiece(movePosition)==null || board.getPiece(movePosition).getTeamColor()!= piece.getTeamColor())) {
            validMoves.add(new ChessMove(currentPosition,movePosition,null));
        }





        //move right two up one
        moveRow = currentRow;
        moveCol = currentCol;
        moveRow+=1;
        moveCol+=2;
        movePosition=new ChessPosition(moveRow,moveCol);
        if(movePosition.validPosition() && (board.getPiece(movePosition)==null || board.getPiece(movePosition).getTeamColor()!= piece.getTeamColor())) {
            validMoves.add(new ChessMove(currentPosition,movePosition,null));
        }

        //move right two down one
        moveRow = currentRow;
        moveCol = currentCol;
        moveRow-=1;
        moveCol+=2;
        movePosition=new ChessPosition(moveRow,moveCol);
        if(movePosition.validPosition() && (board.getPiece(movePosition)==null || board.getPiece(movePosition).getTeamColor()!= piece.getTeamColor())) {
            validMoves.add(new ChessMove(currentPosition,movePosition,null));
        }

        //move left two up one
        moveRow = currentRow;
        moveCol = currentCol;
        moveRow+=1;
        moveCol-=2;
        movePosition=new ChessPosition(moveRow,moveCol);
        if(movePosition.validPosition() && (board.getPiece(movePosition)==null || board.getPiece(movePosition).getTeamColor()!= piece.getTeamColor())) {
            validMoves.add(new ChessMove(currentPosition,movePosition,null));
        }

        //move left two down one
        moveRow = currentRow;
        moveCol = currentCol;
        moveRow-=1;
        moveCol-=2;
        movePosition=new ChessPosition(moveRow,moveCol);
        if(movePosition.validPosition() && (board.getPiece(movePosition)==null || board.getPiece(movePosition).getTeamColor()!= piece.getTeamColor())) {
            validMoves.add(new ChessMove(currentPosition,movePosition,null));
        }

    }

    public void promotionMove(ChessPosition movePosition){  //make all four promotion piece variations
        validMoves.add(new ChessMove(currentPosition,movePosition, ChessPiece.PieceType.QUEEN));
        validMoves.add(new ChessMove(currentPosition,movePosition, ChessPiece.PieceType.KNIGHT));
        validMoves.add(new ChessMove(currentPosition,movePosition, ChessPiece.PieceType.BISHOP));
        validMoves.add(new ChessMove(currentPosition,movePosition, ChessPiece.PieceType.ROOK));
    }

}
