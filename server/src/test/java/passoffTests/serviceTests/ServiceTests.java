package passoffTests.serviceTests;
import dataAccess.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class ServiceTests {
    UserDAO userDAO;
    AuthDAO authDAO;
    GameDAO gameDAO;
    @Test
    public void createAuthSuccessTest() {  //use AuthDAO so this applies to next stage as well
        String username = "ths29";
        assertNotNull(authDAO.createAuth(username), "Response did not return authentication String");
    }

    @BeforeEach
    public void setup() {
        userDAO = new MemoryUserDAO();
        authDAO=new MemoryAuthDAO();
        gameDAO=new MemoryGameDAO();
    }
    @Test
    public void registerUser() {

        //create user with username
        String username = "ths29";
        String password = "1234";
        String email = "smile@gmail.com";
        userDAO.createUser(username, password, email);
        password="5678";
        email = "grin@gmail.com";
        //then try to do it again, should fail
        //assertNull(userDAO.createUser(username,password,email));
    }
}
