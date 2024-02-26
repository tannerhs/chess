package server;

import com.google.gson.Gson;
import dataAccess.*;
import handlers.ClearHandler;
import handlers.LoginHandler;
import handlers.LogoutHandler;
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

        Spark.init();
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
        //Spark.delete("/session", (req,res) -> "uh hi");
        Spark.delete("/session", (req,res) -> new LogoutHandler().handleRequest(req,res,authDAO));
        System.out.println("after lambda");

        //register and clear

        //data access and data model classes

    }






}
