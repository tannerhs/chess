package client_responses;

import chess.ChessGame;

public record JoinGameResponse(ChessGame game, int statusCode, String statusMessage) {
}
