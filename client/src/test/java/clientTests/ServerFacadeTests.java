package clientTests;

import model.UserData;
import org.junit.jupiter.api.*;
import client_requests.LoginRequest;
import client_requests.LogoutRequest;
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
        testUserAuthToken=res.addedAuth().authToken();
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



}
