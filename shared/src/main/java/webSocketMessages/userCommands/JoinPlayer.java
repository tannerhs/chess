package webSocketMessages.userCommands;

import chess.ChessGame;

public class JoinPlayer extends UserGameCommand{
    int gameID;
    ChessGame.TeamColor playerColor;
    CommandType commandType = CommandType.JOIN_PLAYER;
    public JoinPlayer(String authToken, int gameID, ChessGame.TeamColor playerColor) {
        super(authToken);
        super.setCommandType(CommandType.JOIN_PLAYER);
        this.gameID=gameID;
        this.playerColor=playerColor;
    }
}
