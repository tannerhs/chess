package clientTests;

import client_requests.*;
import model.UserData;
import org.junit.jupiter.api.*;
import client_responses.RegisterResponse;
import server.Server;
import ui.ServerFacade;


public class ServerFacadeTests {

    private static Server server;
    static ui.ServerFacade facade;
    static String testUserAuthToken=null;

    @BeforeAll
    public static void init() {
        server = new Server();
        var port = server.run(8080);
        System.out.println("Started test HTTP server on " + port);
        facade = new ServerFacade(8080);
        System.out.println("Started test HTTP client on " + port);

    }

    @AfterAll
    static void stopServer() {
        server.stop();
    }

    @Test
    void registerPos() throws Exception {
        String addedToken="no";
        UserData addedUser = new UserData("player12", "password", "p1@email.com");
        RegisterResponse res = facade.register(addedUser);
        if(res.statusCode()!=200) {
            System.out.println(res.statusCode());
        }
        else {
            addedToken=res.addedAuth().authToken();
            System.out.printf("Successfully registered\n");
        }
        Assertions.assertTrue(addedToken.length() > 10);
        testUserAuthToken=addedToken;
    }

    @Test
    void registerNeg() throws Exception {
        UserData addedUser = new UserData("player11", "password", "p1@email.com");
        RegisterResponse res = facade.register(addedUser);
        res = facade.register(new UserData("player11", "password", "p1@email.com") );
        Assertions.assertEquals(res.addedAuth() ,null);
    }


    @Test
    void logoutPos() throws Exception {
        UserData addedUser = new UserData("player13", "password", "p1@email.com");
        RegisterResponse res2 = facade.register(addedUser);
        var res = facade.logout(new LogoutRequest(testUserAuthToken));
        Assertions.assertEquals(res.statusCode(),200);
    }

    @Test
    void logoutNeg() throws Exception {
        var res = facade.logout(new LogoutRequest("dummy-auth"));
        Assertions.assertNotEquals(res.statusCode(),200);

    }



    @Test
    public void loginPosTest() throws Exception {
        var res = facade.register(new UserData("loginPos","login","login"));
        facade.logout(new LogoutRequest(res.addedAuth().authToken()) );
        var res2=facade.login(new LoginRequest("loginPos","login"));
        Assertions.assertEquals(res2.statusCode(),200);
    }

    @Test
    public void loginNegTest() throws Exception {
        var res = facade.login(new LoginRequest("loginNeg","login"));
        Assertions.assertNotEquals(res.statusCode(),200);
    }

    @Test void clearApp() throws Exception {
        var res = facade.register(new UserData("newLogin","login","login"));
        var res2=facade.ClearApp(res.addedAuth().authToken());
        Assertions.assertEquals(res2.statusCode(),200);
    }

    @Test void createGamesPos() throws Exception {
        var res = facade.register(new UserData("lugin","ok","ok"));
        var res2=facade.createGame(res.addedAuth().authToken(),new CreateGameRequest("gameName1"));
        Assertions.assertEquals(res2.statusCode(),200);
    }

    @Test void createGamesNeg() throws Exception {
        var res2=facade.createGame("bad-auth",new CreateGameRequest("gameName1"));
        Assertions.assertNotEquals(res2.statusCode(),200);
    }

    @Test void listGamesPos() throws Exception {
        var res = facade.register(new UserData("lucaa","ok","ok"));
        var res2=facade.listGames(res.addedAuth().authToken());
        Assertions.assertEquals(res2.statusCode(),200);
    }

    @Test void listGamesNeg() throws Exception {
        var res = facade.register(new UserData("lucaa","ok","ok"));
        var res2=facade.listGames("no-existy-auth-token");
        Assertions.assertNotEquals(res2.statusCode(),200);
    }

    @Test void joinGamePos() throws Exception {
        var res = facade.register(new UserData("lucas","ok","ok"));
        var res2=facade.createGame(res.addedAuth().authToken(),new CreateGameRequest("gameName1"));
        var res3=facade.joinGame(res.addedAuth().authToken(),new JoinGameRequest("WHITE",res2.gameID()));
        Assertions.assertEquals(res3.statusCode(),200);
    }

    @Test void joinGameNeg() throws Exception {
        var res3=facade.joinGame("nope",new JoinGameRequest("WHITE",-1));
        Assertions.assertNotEquals(res3.statusCode(),200);
    }

    @Test void joinGameObserverPos() throws Exception {
        var res = facade.register(new UserData("lucaso","ok","ok"));
        var res2=facade.createGame(res.addedAuth().authToken(),new CreateGameRequest("gameName2"));
        var res3=facade.joinGame(res.addedAuth().authToken(),new JoinGameRequest("red",res2.gameID()));
        Assertions.assertEquals(res3.statusCode(),200);
        facade.ClearApp(res.addedAuth().authToken());
    }

    @Test void joinGameObserverNeg() throws Exception {
        var res3=facade.joinGame("nah bro",new JoinGameRequest("red",-1));
        Assertions.assertNotEquals(res3.statusCode(),200);
    }




}
