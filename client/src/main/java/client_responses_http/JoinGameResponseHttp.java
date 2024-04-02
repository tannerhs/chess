package client_responses_http;

import chess.ChessGame;

public record JoinGameResponseHttp(ChessGame game, int gameID, int statusCode, String statusMessage) {
}
