package chess.rules;
import chess.ChessBoard;
import chess.ChessMove;
import chess.ChessPiece;
import chess.ChessPosition;

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

    public void moveSetup(int rowIncrement, int colIncrement) {
        int currentRow = currentPosition.getRow();
        int currentCol = currentPosition.getColumn();
        int moveRow = currentRow;
        int moveCol = currentCol;
        ChessPosition movePosition;
        boolean keepLooking=true;

        while(keepLooking && ((moveRow<8 && rowIncrement==1) || (moveRow>1 && rowIncrement==-1) || (moveCol<8 && colIncrement==1) || (moveCol>1 && colIncrement==-1))) {
            moveRow+=rowIncrement;
            moveCol+=colIncrement;
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
        moveSetup(1,0);
    }


    public void moveDown() {
        moveSetup(-1,0);
    }


    public void moveRight() {
        moveSetup(0,1);
    }


    public void moveLeft() {
        moveSetup(0,-1);
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
        //otherwise own piece blocking you, do nothing
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
        //otherwise own piece blocking you, do nothing
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
        //otherwise own piece blocking you, do nothing
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
        //otherwise own piece blocking you, do nothing
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
        //otherwise own piece blocking you, do nothing
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
        //otherwise own piece blocking you, do nothing
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