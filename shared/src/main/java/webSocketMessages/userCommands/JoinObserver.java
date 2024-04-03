package webSocketMessages.userCommands;

import chess.ChessGame;
public class JoinObserver extends UserGameCommand {
    int gameID;
    public JoinObserver(String authToken, int gameID) {
        super(authToken);
        super.setCommandType(CommandType.JOIN_OBSERVER);
        this.gameID=gameID;
    }

    public int getGameID() {
        return gameID;
    }
}
