package webSocketMessages.userCommands;

public class Leave extends UserGameCommand{
    Integer gameID;
    public Leave(String authToken, Integer gameID) {
        super(authToken);
        this.gameID=gameID;
    }
}
