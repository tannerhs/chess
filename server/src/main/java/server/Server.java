package server;

import com.google.gson.Gson;
import dataAccess.*;
import handlers.ClearHandler;
import handlers.LoginHandler;
import handlers.RegisterHandler;
import spark.*;

import java.util.Collection;
import java.util.HashMap;

public class Server {

    static UserDAO usersDAO; //pass around as needed
    static GameDAO gamesDAO;  //pass 'round as needed
    static AuthDAO authDAO;  //pass around as needed

    public Server(){
        usersDAO= new MemoryUserDAO();
        gamesDAO = new MemoryGameDAO();
        authDAO = new MemoryAuthDAO();
    }

    public int run(int desiredPort) {
        Spark.port(desiredPort);

        Spark.staticFiles.location("web");

        // Register your endpoints and handle exceptions here.
        createRoutes();


        Spark.awaitInitialization();
        return Spark.port();
    }

    public void stop() {
        Spark.stop();
        Spark.awaitStop();
    }

    public void createRoutes() {
        Spark.get("/hello", (req, res) -> "Hello BYU!");
        System.out.println("before lambda");
        Spark.delete("/db",(req,res) -> new ClearHandler().handleRequest(req,res,usersDAO,gamesDAO,authDAO));  //clear application
        Spark.post("/user",(req, res) -> new RegisterHandler().handleRequest(req,res, usersDAO,authDAO));
        Spark.post("/session", (req,res) -> new LoginHandler().handleRequest(req,res,usersDAO,authDAO));
        System.out.println("after lambda");

        //register and clear

        //data access and data model clases

    }

    private Object ClearApplication(Request req, Response res) {
        return null;
    }
    private Object addName(Request req, Response res) {
        //names.add(req.params(""));
        return listNames(req, res);
    }

    private Object listNames(Request req, Response res) {
        res.type("application/json");
        return new Gson().toJson("");
    }
    private static <T> T getBody(Request request, Class<T> clazz) {
        return null;
//        var body = new Gson().fromJson(request.body(), clazz);
//        if (body == null) {
//            throw new RuntimeException("missing required body");
//        }
//        return body;
    }




}
