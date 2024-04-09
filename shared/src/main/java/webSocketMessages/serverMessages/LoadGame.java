package webSocketMessages.serverMessages;

import chess.ChessGame;
import model.GameData;

public class LoadGame extends ServerMessage {
    LoadGameObject game;
    public LoadGame(LoadGameObject game) {
        super(ServerMessageType.LOAD_GAME);
        this.game=game;
    }


    public ChessGame getGame() {
        return game.gameData().game();
    }

    public GameData getGameData( ) {
        return game.gameData();
    }

    public LoadGameObject getLoadGameObject() {
        return game;
    }

}
