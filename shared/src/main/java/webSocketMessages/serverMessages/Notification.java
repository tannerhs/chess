package webSocketMessages.serverMessages;

public class Notification {
    String message;

    public Notification(String notificationMessage) {
        this.message=notificationMessage;
    }

    public String getMessage() {
        return message;
    }
}
