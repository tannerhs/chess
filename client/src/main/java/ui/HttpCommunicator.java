package ui;

import bodyResponses.CreateGameBodyResponse;
import bodyResponses.LoginBodyResponse;
import chess.ChessGame;
import client_requests.CreateGameRequest;
import client_requests.JoinGameRequest;
import client_requests.LoginRequest;
import client_requests.LogoutRequest;
import client_responses_http.*;
import com.google.gson.Gson;
import model.AuthData;
import model.GameData;
import model.UserData;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URI;
import java.util.List;

public class HttpCommunicator {
    int port=8080;

    public HttpCommunicator(int port) {
        this.port=port;
    }
    //represents your server to the client, provides simple way to do it
    //2-3 lines of code in each since calls client communicator

    public ClearAppResponse clearApp(String authToken) throws Exception {
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
//                System.out.printf("responseText: %s\n",responseText);
                response = new Gson().fromJson(responseText, ListGamesObjects.class);
//                System.out.printf("ListGamesObjects thing: %s\n",response);
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
            System.out.printf("response: %s\n",response);
            return new CreateGameResponse(response.gameID(),statusCode,statusMessage);
        }
    }

    public JoinGameResponseHttp joinGame(String authToken, JoinGameRequest joinGameRequest) throws Exception {
        System.out.printf("joinGameRequest- %s\n",joinGameRequest);

        System.out.println("joinGame in HttpCommunicator reached");
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
        int gameID= joinGameRequest.gameID();

        CreateGameBodyResponse response=null;
        if(statusCode!=200) {  //return before http exception can be thrown
            System.out.println("Http communicator join game status code not 200");
            System.out.printf("status code: %d\n",statusCode);
            System.out.printf("joinGameRequest: %s\n",joinGameRequest);
            return new JoinGameResponseHttp(null,gameID,statusCode,statusMessage);
        }
        else {
            //no response body
            ChessGame game = new ChessGame();
            if("WHITE".equals(joinGameRequest.playerColor()) || "BLACK".equals(joinGameRequest.playerColor())) {  //only if valid color and join game, not just observer
                game.setTeamTurn(ChessGame.TeamColor.valueOf(joinGameRequest.playerColor()));
            }
            return new JoinGameResponseHttp(game, gameID, statusCode,statusMessage);
        }
    }
}
