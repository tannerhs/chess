package client_responses_ws;

import chess.ChessGame;

public record JoinGameResponseWS(ChessGame game, int statusCode, String statusMessage) {
}
