package handlers;

import com.google.gson.Gson;
import dataAccess.*;
import requests.JoinGameRequest;
import service.JoinGameService;
import spark.Request;
import spark.Response;

import javax.naming.AuthenticationException;

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
            service.joinGame();
        }
        catch (BadRequestException e) {
            message = e.getMessage();
            res.status(400);
        }
        catch(AuthenticationException e) {
            message=e.getMessage();
            res.status(401);
        }
        catch(PlayerFieldTakenException e) {
            message=e.getMessage();
            res.status(403);
        }
        catch(Exception e) {
            //
            res.status(500);
        }

        return message;
    }
}
