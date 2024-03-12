package clientTests;

import org.junit.jupiter.api.*;
import server.Server;


public class ServerFacadeTests {

    private static Server server;
//    static ui.ServerFacade facade;
    @BeforeAll
    public static void init() {
        server = new Server();
        var port = server.run(0);
        System.out.println("Started test HTTP server on " + port);
//        facade = new ui.ServerFacade(port);
    }

    @AfterAll
    static void stopServer() {
        server.stop();
    }


//    @Test
//    void register() throws Exception {
//        var authData = facade.register("player1", "password", "p1@email.com");
//        assertTrue(authData.authToken().length() > 10);
//    }
    @Test
    public void sampleTest() {
        Assertions.assertTrue(true);
    }

}
