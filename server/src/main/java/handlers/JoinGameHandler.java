package handlers;

import com.google.gson.Gson;
import dataAccess.AuthDAO;
import dataAccess.GameDAO;
import dataAccess.UserDAO;
import requests.JoinGameRequest;
import service.JoinGameService;
import spark.Request;
import spark.Response;

public class JoinGameHandler {

    public Object handleRequest(Request req, Response res, UserDAO users, AuthDAO auth, GameDAO games) {
        String message="";
        Gson deserializer = new Gson();
        JoinGameSparkRequestBody bodyVars = deserializer.fromJson(req.body(),JoinGameSparkRequestBody.class);
        String playerColor=bodyVars.playerColor();
        int gameID = bodyVars.gameID();
        String authToken=req.headers("Authorization");
        try{
            JoinGameRequest request = new JoinGameRequest(playerColor,gameID, authToken,users,auth,games);
            JoinGameService service = new JoinGameService(request);
        } catch(Exception e) {
            //
            res.status(500);
        }

        return message;
    }
}
