package webSocketMessages.serverMessages;

public class Error extends ServerMessage {
    public Error(String errorMessage) {
        super(ServerMessage.ServerMessageType.ERROR);
        this.errorMessage=errorMessage;
    }

    public String getErrorMessage() {
        return errorMessage;
    }
    String errorMessage;
}
