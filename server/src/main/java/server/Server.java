package server;

import dataAccess.*;
import handlers.*;
import spark.*;

import static dataAccess.DatabaseManager.*;

public class Server {

    static UserDAO usersDAO; //pass around as needed
    static GameDAO gamesDAO;  //pass 'round as needed
    static AuthDAO authDAO;  //pass around as needed

    public Server(){
        try {
            usersDAO = new DatabaseUserDAO();
            gamesDAO = new DatabaseGameDAO();
            authDAO = new DatabaseAuthDAO();
            DatabaseManager.createDatabase();
        }
        catch( Exception e) {
            e.printStackTrace();
        }
    }

    public int run(int desiredPort) {
        Spark.port(desiredPort);

        Spark.staticFiles.location("web");

        // Register your endpoints and handle exceptions here.
        createRoutes();

        Spark.init();
        Spark.awaitInitialization();
        return Spark.port();
    }

    public void stop() {
        Spark.stop();
        Spark.awaitStop();
    }

    public void createRoutes() {
        Spark.delete("/db",(req,res) -> new ClearHandler().handleRequest(req,res,usersDAO,gamesDAO,authDAO));  //clear application
        Spark.post("/user",(req, res) -> new RegisterHandler().handleRequest(req,res, usersDAO,authDAO));
        Spark.post("/session", (req,res) -> new LoginHandler().handleRequest(req,res,usersDAO,authDAO));
        Spark.delete("/session", (req,res) -> new LogoutHandler().handleRequest(req,res,authDAO));
        Spark.post("/game", (req,res) -> new CreateGameHandler().handleRequest(req,res,authDAO,gamesDAO));
        Spark.get("/game", (req,res) -> new ListGamesHandler().handleRequest(req,res, authDAO, gamesDAO));
        Spark.put("/game", (req,res) -> new JoinGameHandler().handleRequest(req,res,usersDAO,authDAO,gamesDAO));
    }






}
