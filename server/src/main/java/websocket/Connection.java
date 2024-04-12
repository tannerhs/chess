package websocket;

import org.eclipse.jetty.websocket.api.Session;
import java.io.IOException;
public class Connection {
    public String username;
    int gameID;
    public Session session;

    public Connection(String username, int gameID, Session session) {
        this.username = username;
        this.gameID=gameID;
        this.session = session;
    }

}
