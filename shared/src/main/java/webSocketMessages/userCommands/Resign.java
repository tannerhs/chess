package webSocketMessages.userCommands;

public class Resign extends UserGameCommand{
    Integer gameID;
    public Resign(String authToken, Integer gameID) {
        super(authToken);
        this.gameID=gameID;
        super.setCommandType(CommandType.RESIGN);

    }

    public Integer getGameID() {
        return gameID;
    }
}
