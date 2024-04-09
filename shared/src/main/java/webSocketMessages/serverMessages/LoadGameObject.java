package webSocketMessages.serverMessages;

import chess.ChessGame;
import model.GameData;

public record LoadGameObject(GameData gameData, ChessGame.TeamColor printAsTeamColor,String otherTeamAuthToken) {
}
