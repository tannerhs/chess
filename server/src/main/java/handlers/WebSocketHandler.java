package handlers;

//import org.eclipse.jetty.websocket.api.Session;
//import org.eclipse.jetty.server.session.Session;
import org.eclipse.jetty.websocket.api.annotations.*;
import org.eclipse.jetty.websocket.api.*;
import spark.Spark;

@WebSocket
public class WebSocketHandler {
    //
    @OnWebSocketMessage
    public void onMessage(Session session, String message) throws Exception {
        //deserialize user game commands, which edits data more directly in the case of makeMove by calling DAO
        System.out.printf("Received: %s", message);
        session.getRemote().sendString("WebSocket response: " + message);
    }


}
