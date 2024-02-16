package server;

import com.google.gson.Gson;
import handlers.RegisterHandler;
import spark.*;

public class Server {

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
        Spark.delete("/db",(req,res) -> new RegisterHandler().handleRequest(req,res));  //clear application

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
