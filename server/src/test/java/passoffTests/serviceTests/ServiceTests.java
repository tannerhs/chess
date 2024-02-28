package passoffTests.serviceTests;
import dataAccess.*;
import model.UserData;
import org.eclipse.jetty.server.Authentication;
import org.eclipse.jetty.util.log.Log;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import requests.*;
import responses.LoginResponse;
import responses.RegisterResponse;
import service.*;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class ServiceTests {
    UserDAO users;
    AuthDAO auth;
    GameDAO games;
    String authToken;

    @BeforeEach
    public void setup() {
        users = new MemoryUserDAO();
        auth=new MemoryAuthDAO();
        games=new MemoryGameDAO();
    }

    @Test
    public void RegisterServiceConstructorPosTest() {
        try {
            UserData addUser = new UserData("ths29","la-di-la","smile@gmail.com");
            RegisterRequest request = new RegisterRequest(users,auth,addUser);
            RegisterService service = new RegisterService(request);
            assertNotNull(service);
        }
        catch (Exception e) {
            //
        }

    }

    @Test
    public void RegisterServiceConstructorNegTest() {
        String message="success";

        try {  //try to register with bad request
            RegisterRequest request = new RegisterRequest(users,auth,null);
            RegisterService service = new RegisterService(request);
            assertNotNull(service);
            service.register();
        }
        catch (BadRequestException e){
            message="failure";
        }
        catch (PlayerFieldTakenException e) {
            message="failure2";
        }
        catch (Exception e) {
            message="null pointer";
        }
        assertEquals(message,"failure");


    }

    @Test
    public void registerPosTest() {
        String message = "success";
        try {
            UserData addUser = new UserData("ths29","la-di-la","smile@gmail.com");
            RegisterRequest request = new RegisterRequest(users,auth,addUser);
            RegisterService service = new RegisterService(request);
            service.register();
        }
        catch(Exception e) {
            message="failure";
        }
        assertEquals(message,"success");
    }

    @Test
    public void registerNegTest() {
        String message = "success";
        try {
            UserData addUser = new UserData("ths","la-di-la","smile@gmail.com");
            RegisterRequest request = new RegisterRequest(users,auth,addUser);
            RegisterService service = new RegisterService(request);
            service.register();

            UserData otherUser = new UserData("ths","lol","smiiiile@gmail.com");
            RegisterRequest request2 = new RegisterRequest(users,auth,otherUser);
            RegisterService service2 = new RegisterService(request2);
            service2.register();
        }
        catch (PlayerFieldTakenException e) {
            message="failure";
        }
        catch(Exception e) {
            message="failure 2";
        }
        assertEquals(message,"failure");
    }



    @Test
    public void LogoutConstructorPosTest() {
        try {
            LogoutRequest request = new LogoutRequest(authToken,auth);
            LogoutService service = new LogoutService(request);
            assertNotNull(service);
        }
        catch (Exception e) {
            System.out.println("shouldn't print out ever");
        }
    }

    @Test public void LogoutConstructorNegTest() {
        String message="success";
        try {
            LogoutRequest request = new LogoutRequest(authToken,null);
            LogoutService service = new LogoutService(request);
            assertNotNull(service);
            service.logout();
        }
        catch (UnauthorizedAccessException e) {
            message="failure";
        }
        catch(Exception e) {
            message="failure 2";
        }
        assertEquals(message,"failure");
    }

    @Test
    public void logoutPosTest() {
        String message="success";
        try {
            //login
            UserData addUser = new UserData("ths28","la-di-la","smile@gmail.com");
            RegisterRequest regRequest = new RegisterRequest(users,auth,addUser);
            RegisterService regService = new RegisterService(regRequest);
            RegisterResponse response= regService.register();
            String authToken=response.addedAuth().authToken();

            //logout
            LogoutRequest request = new LogoutRequest(authToken,auth);
            LogoutService service = new LogoutService(request);
            service.logout();
        }
        catch (UnauthorizedAccessException e) {
            message="failure";
            System.out.println("shouldn't happen");
        }
        catch (BadRequestException e) {
            message="failure2";
        }
        catch (PlayerFieldTakenException e) {
            message="failure3";
        }
        assertEquals(message,"success");
    }

    @Test
    public void logoutNegTest(){  //try to logout again
        String message="success";
        try {
            //login
            UserData anotherUser = new UserData("hi","hola","hola@gmail.com");
            RegisterRequest regRequest2 = new RegisterRequest(users,auth,anotherUser);
            RegisterService regService2 = new RegisterService(regRequest2);
            RegisterResponse response2= regService2.register();
            String authToken2=response2.addedAuth().authToken();

            //logout
            LogoutRequest request = new LogoutRequest("random not valid",auth);
            LogoutService service = new LogoutService(request);
            service.logout();
        }
        catch (UnauthorizedAccessException e) {
            message="failure";
        }
        catch (BadRequestException e) {
            message="failure2";
        }
        catch (PlayerFieldTakenException e) {
            message="failure3";
        }
        assertEquals(message,"failure");
    }


    @Test
    public void clearServiceConstructorPosTest() {
        try{
            ClearAppService clearService = new ClearAppService(new ClearAppRequest(users,games,auth));
            assertNotNull(clearService);
        }
        catch(Exception e) {
            System.out.println("shouldn't happen");
        }
    }

    @Test
    public void clearServiceConstructorNegTest() {
        String message="success";
        try{
            ClearAppService clearService = new ClearAppService(new ClearAppRequest(null,games,auth));
        }
        catch(Exception e) {
            message="failure";
        }
        assertEquals(message,"failure");
    }


    @Test
    public void clearServicePosTest() {
        String message="success";
        try{
            ClearAppService clearService = new ClearAppService(new ClearAppRequest(new MemoryUserDAO(),new MemoryGameDAO(), new MemoryAuthDAO()));
            clearService.clearApp();
        }
        catch(Exception e) {
            message="failure";
        }
        assertEquals(message,"success");
    }

    @Test
    public void clearServiceNegTest() {
        String message="success";
        try{
            ClearAppService clearService = new ClearAppService(new ClearAppRequest(null,games,auth));
            clearService.clearApp();
        }
        catch(Exception e) {
            message="failure";
        }
        assertEquals(message,"failure");
    }


    @Test
    public void loginServiceConstructorPos() {
        assertNotNull(new LoginService(new LoginRequest("meh","mmm2",users,auth)));
    }

    @Test
    public void loginServiceConstructorNeg() {
        try{
            assertEquals((new LoginService(new LoginRequest("meh","mmm2",users,null))).login(),new UnauthorizedAccessException("{\"message\": \"Error: unauthorized\"}"));

        }
        catch (Exception e) {
            //
        }
    }
    @Test
    public void loginServicePos() {
        try {
            RegisterResponse response = new RegisterService(new RegisterRequest(users,auth,new UserData("ham","123","i@mail.com"))).register();
            new LogoutService(new LogoutRequest(response.addedAuth().authToken(),auth));
            LoginResponse res = new LoginService(new LoginRequest("ham","123",users,auth)).login();
        }
        catch(BadRequestException e) {
            //
        }
        catch(UnauthorizedAccessException e) {
            //
        }
        catch (PlayerFieldTakenException e) {
            //
        }
    }
    @Test
    public void loginServiceNeg() {
        try{
            assertEquals((new LoginService(new LoginRequest("meh","mmm2",users,auth))).login(),new UnauthorizedAccessException("{\"message\": \"Error: unauthorized\"}"));

        }
        catch (Exception e) {
            //
        }
    }

    @Test
    public void ListGamesConstructorPos() {
        try {
            RegisterResponse res = (new RegisterService(new RegisterRequest(users,auth,new UserData("t","tl","@")))).register();
            assertNotNull(new ListGamesService(new ListGamesRequest("",auth,games)));
        }
        catch (Exception e) {
            //
        }

    }
    @Test
    public void ListGamesConstructorNeg() {
        assertNotNull(new ListGamesService(new ListGamesRequest("none",auth,games)));
    }

    @Test
    public void listGamesPos(){
        try {
            assertNotNull(new ListGamesService(new ListGamesRequest("none",auth,games)).listGames());
        }
        catch (Exception e) {
            //
        }
    }

    @Test
    public void listGamesNeg(){
        String message="success";
        try {
            assertNotNull(new ListGamesService(new ListGamesRequest("none",auth,games)).listGames());
        }
        catch (Exception e) {
            message="failure";
        }
        assertEquals(message,"failure");
    }

    @Test
    public void CreateGameConstructorPos() {
        try{
            String authToken = (new RegisterService(new RegisterRequest(users,auth,new UserData("fun","120","@")))).register().addedAuth().authToken();
            assertNotNull(new CreateGameService(new CreateGameRequest(authToken,"game1",auth,games)));
        }
        catch(Exception e) {
            //
        }
    }

    @Test
    public void CreateGameConstructorNeg() {
        try{
            String authToken = (new RegisterService(new RegisterRequest(users,auth,new UserData("fun","120","@")))).register().addedAuth().authToken();
            assertNotNull(new CreateGameService(new CreateGameRequest("nonez","game1",auth,games)));
        }
        catch(Exception e) {
            //
        }
    }

    @Test
    public void CreateGamePos() {
        String message="success";
        try{
            String authToken = (new RegisterService(new RegisterRequest(users,auth,new UserData("fun","120","@")))).register().addedAuth().authToken();
            assertNotNull((new CreateGameService(new CreateGameRequest(authToken,"game5",auth,games))).createGame());
        }
        catch(Exception e) {
            message="failure";
        }
        assertEquals(message,"success");
    }

    @Test
    public void CreateGameNeg() {
        String message="success";
        try{
            String authToken = (new RegisterService(new RegisterRequest(users,auth,new UserData("fun","120","@")))).register().addedAuth().authToken();
            assertNotNull((new CreateGameService(new CreateGameRequest("dunno","game1",auth,games))).createGame());
        }
        catch(Exception e) {
            message="failure";
        }
        assertEquals(message,"failure");
    }


}
