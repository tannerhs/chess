package ui;

import com.google.gson.Gson;
import webSocketMessages.serverMessages.LoadGame;
import webSocketMessages.serverMessages.ServerMessage;

public interface ServerMessageObserver {
    public void notify(String message);  //print stuff based on notification received (print string or game)


}
