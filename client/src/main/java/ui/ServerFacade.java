package ui;

import bodyResponses.CreateGameBodyResponse;
import bodyResponses.LoginBodyResponse;
import client_responses.*;
import com.google.gson.Gson;
import model.AuthData;
import model.GameData;
import model.UserData;
import client_requests.*;
import client_responses.*;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URI;
import java.util.HashSet;
import java.util.List;

//7 methods for 7 endpoints
//take in Request object and return Response object

//add makeMove method

//also create ClientCommunicator class called by server facade, do get, post, delete update etc.. http

public class ServerFacade {
    int port=8080;

    public ServerFacade(int port) {
        this.port=port;
    }
    //represents your server to the client, provides simple way to do it
        //2-3 lines of code in each since calls client communicator

    public ClearAppResponse ClearApp(String authToken) throws Exception {
        // Specify the desired endpoint
        URI uri = new URI("http://localhost:"+port+"/db");
        HttpURLConnection http = (HttpURLConnection) uri.toURL().openConnection();
        http.setRequestMethod("DELETE");

        // Specify that we are going to write out data
        http.setDoOutput(true);

        // Write out a header
        http.addRequestProperty("Authorization",authToken);

        // no body

        // Make the request
        http.connect();

        //Receive the response body
        var statusCode = http.getResponseCode();
        var statusMessage = http.getResponseMessage();

        return new ClearAppResponse(statusCode,statusMessage);
    }

    public LoginResponse login(LoginRequest loginRequest) throws Exception {
            // Specify the desired endpoint
            URI uri = new URI("http://localhost:"+port+"/session");
            HttpURLConnection http = (HttpURLConnection) uri.toURL().openConnection();
            http.setRequestMethod("POST");

            // Specify that we are going to write out data
            http.setDoOutput(true);

            // Write out a header
            http.addRequestProperty("Content-Type", "application/json");

            // Write out the body
            try (OutputStream outputStream = http.getOutputStream()) {
                var jsonBody = new Gson().toJson(loginRequest);  //maybe var or some json thing
                outputStream.write(jsonBody.getBytes());
            }

            // Make the request
            http.connect();

            //Receive the response body
            var statusCode = http.getResponseCode();
            var statusMessage = http.getResponseMessage();

            if(statusCode!=200) {  //return before http exception can be thrown
                return new LoginResponse(null,statusCode,statusMessage);
            }
            else {
                // Read the response body
                LoginBodyResponse body=null;
                try (InputStream respBody = http.getInputStream()) {
                    InputStreamReader inputStreamReader = new InputStreamReader(respBody);
                    body = new Gson().fromJson(inputStreamReader, LoginBodyResponse.class);
                    AuthData addedAuth = new AuthData(body.authToken(),body.username());
                    LoginResponse loginResponse =new LoginResponse(addedAuth,statusCode,statusMessage);
                    return loginResponse;
                }
            }
    }

    public RegisterResponse register(UserData addUser) throws Exception {
        // Specify the desired endpoint
        URI uri = new URI("http://localhost:"+port+"/user");
        HttpURLConnection http = (HttpURLConnection) uri.toURL().openConnection();
        http.setRequestMethod("POST");

        // Specify that we are going to write out data
        http.setDoOutput(true);

        // Write out a header
        //http.addRequestProperty("Content-Type", "application/json");

        // Write out the body
        try (OutputStream outputStream = http.getOutputStream()) {
            var jsonBody = new Gson().toJson(addUser);  //maybe var or some json thing
            outputStream.write(jsonBody.getBytes());
        }

        // Make the request
        http.connect();

        //Receive the response body
        var statusCode = http.getResponseCode();
        var statusMessage = http.getResponseMessage();

        if(statusCode!=200) {
            System.out.println("no bueno");
            System.out.println(statusMessage);
            return new RegisterResponse(null,statusCode,statusMessage);
        }
        else {
            // Read the response body
            AuthData resp;
            try (InputStream respBody = http.getInputStream()) {
                InputStreamReader inputStreamReader = new InputStreamReader(respBody);
                resp = new Gson().fromJson(inputStreamReader, AuthData.class);
                return new RegisterResponse(resp,statusCode,statusMessage) ;
            }
        }
    }

    public LogoutResponse logout(LogoutRequest logoutRequest) throws Exception {
        // Specify the desired endpoint
        URI uri = new URI("http://localhost:"+port+"/session");
        HttpURLConnection http = (HttpURLConnection) uri.toURL().openConnection();
        http.setRequestMethod("DELETE");

        // Specify that we are going to write out data
        http.setDoOutput(true);

        // Write out a header
        String authString = logoutRequest.authToken();
        http.addRequestProperty("Authorization",authString);

        // no body

        // Make the request
        http.connect();

        //Receive the response body
        var statusCode = http.getResponseCode();
        var statusMessage = http.getResponseMessage();

        return new LogoutResponse(statusCode,statusMessage);
    }


    public ListGamesResponse listGames(String authToken) throws Exception {
        // Specify the desired endpoint
        URI uri = new URI("http://localhost:"+port+"/game");
        HttpURLConnection http = (HttpURLConnection) uri.toURL().openConnection();
        http.setRequestMethod("GET");

        // Specify that we are going to write out data
        http.setDoOutput(true);

        // Write out a header
        http.addRequestProperty("Authorization",authToken);

        //no body in this http request, only header

        // Make the request
        http.connect();

        //Receive the response body
        var statusCode = http.getResponseCode();
        var statusMessage = http.getResponseMessage();

        if(statusCode!=200) {
            return new ListGamesResponse(null,statusCode,statusMessage);
        }
        else {
            ListGamesObjects response=null;
            List<GameData> gameList;
            // Output the response body
            try (InputStream respBody = http.getInputStream()) {
                InputStreamReader inputStreamReader = new InputStreamReader(respBody);
                String responseText = new String(respBody.readAllBytes());
                System.out.printf("responseText: %s\n",responseText);
                response = new Gson().fromJson(responseText, ListGamesObjects.class);
                System.out.printf("ListGamesObjects thing: %s\n",response);
                gameList = response.games();
                        //.iterator().next();  //get first item, list of Games
                //gameList= new HashSet<GameData>();
            }
            return new ListGamesResponse(gameList, statusCode,statusMessage);
        }
    }

    public CreateGameResponse createGame(String authToken, CreateGameRequest createGameRequest) throws Exception {
        // Specify the desired endpoint
        URI uri = new URI("http://localhost:"+port+"/game");
        HttpURLConnection http = (HttpURLConnection) uri.toURL().openConnection();
        http.setRequestMethod("POST");

        // Specify that we are going to write out data
        http.setDoOutput(true);

        // Write out a header
        http.addRequestProperty("Authorization",authToken);

        // Write out the body for http request
        try (OutputStream outputStream = http.getOutputStream()) {
            String jsonBody = new Gson().toJson(createGameRequest);  //maybe var or some json thing
            outputStream.write(jsonBody.getBytes());
        }

        // Make the request
        http.connect();

        //Receive the response body
        var statusCode = http.getResponseCode();
        var statusMessage = http.getResponseMessage();

        if(statusCode!=200) {
            return new CreateGameResponse(-1,statusCode,statusMessage);
        }
        else {
            CreateGameBodyResponse response;
            // Output the response body
            try (InputStream respBody = http.getInputStream()) {
                InputStreamReader inputStreamReader = new InputStreamReader(respBody);
                response = new Gson().fromJson(inputStreamReader, CreateGameBodyResponse.class);
            }
            return new CreateGameResponse(response.gameID(),statusCode,statusMessage);
        }
    }

    public JoinGameResponse joinGame(String authToken,JoinGameRequest joinGameRequest) throws Exception {
        // Specify the desired endpoint
        URI uri = new URI("http://localhost:"+port+"/game");
        HttpURLConnection http = (HttpURLConnection) uri.toURL().openConnection();
        http.setRequestMethod("PUT");

        // Specify that we are going to write out data
        http.setDoOutput(true);

        // Write out a header
        http.addRequestProperty("Authorization",authToken);

        // Write out the body for http request
        try (OutputStream outputStream = http.getOutputStream()) {
            String jsonBody = new Gson().toJson(joinGameRequest);  //maybe var or some json thing
            outputStream.write(jsonBody.getBytes());
        }

        // Make the request
        http.connect();

        //Receive the response body
        var statusCode = http.getResponseCode();
        var statusMessage = http.getResponseMessage();  //http message not very descriptive, we want exception message returned:


        CreateGameBodyResponse response=null;
        if(statusCode!=200) {  //return before http exception can be thrown
            return new JoinGameResponse(statusCode,statusMessage);
        }
        else {

            // Output the response body
            try (InputStream respBody = http.getInputStream()) {
                InputStreamReader inputStreamReader = new InputStreamReader(respBody);
                response = new Gson().fromJson(inputStreamReader, CreateGameBodyResponse.class);
            }
            return new JoinGameResponse(statusCode,statusMessage);
        }
    }
}
