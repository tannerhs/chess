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


        //depending on UserGameCommand message type, do stuff

        //send back server message if needed
    }

    @OnWebSocketConnect
    public void onConnect(Session session) {
        //keep track of sessions in sesion manager class, store sessions form onMessage in session map
    }

    @OnWebSocketClose
    public void onClose(Session session) {
        //remov stuff from map
    }


}
