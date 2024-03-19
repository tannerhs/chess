package bodyResponses;

import chess.ChessGame;

import java.util.HashSet;
import java.util.List;

public record ListGamesBodyResponse(HashSet<ChessGame> games) {
}
