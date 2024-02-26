package model;

import chess.ChessGame;

public record GameData(int gameID, String whiteUsername, String blackUsername, String gameName, ChessGame game) {
    //canonical constructor
    public GameData(int gameID, String whiteUsername, String blackUsername, String gameName, ChessGame game) {
        this.gameID=gameID;
        this.whiteUsername=whiteUsername;
        this.blackUsername=blackUsername;
        this.gameName=gameName;
        this.game=game;
    }
    public GameData(int gameID, String gameName) {
        this(gameID,null,null,gameName,new ChessGame());
    }

}
