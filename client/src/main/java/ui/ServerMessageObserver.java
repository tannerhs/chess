package ui;

import com.google.gson.Gson;
import webSocketMessages.serverMessages.LoadGame;
import webSocketMessages.serverMessages.ServerMessage;

public class ServerMessageObserver {
    public void notify(String message) {  //print stuff based on notification received (print string or game)
        ServerMessage notification = new Gson().fromJson(message, ServerMessage.class);
        switch(notification.getServerMessageType()) {
            case LOAD_GAME : {
                LoadGame loadGame = new Gson().fromJson(message, LoadGame.class);
                //do something useful like print whatever it is, game or string
                break;
            }
        }
    }

}
