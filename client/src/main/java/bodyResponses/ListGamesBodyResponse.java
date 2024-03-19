package bodyResponses;

import chess.ChessGame;

import java.util.List;

public record ListGamesBodyResponse(List<ChessGame> games) {
}
