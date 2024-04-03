package webSocketMessages.serverMessages;

public class Notification extends ServerMessage {
    String message;

    public Notification(String notificationMessage) {
        super(ServerMessageType.NOTIFICATION);
        this.message=notificationMessage;
    }

    public String getMessage() {
        return message;
    }
}
