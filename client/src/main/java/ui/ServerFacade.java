package ui;

import bodyResponses.CreateGameBodyResponse;
import bodyResponses.ListGamesBodyResponse;
import bodyResponses.LoginBodyResponse;
import com.google.gson.Gson;
import model.AuthData;
import model.UserData;
import requests.*;
import responses.CreateGameResponse;
import responses.ListGamesResponse;
import responses.LoginResponse;
import responses.RegisterResponse;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Map;

//7 methods for 7 endpoints
//take in Request object and return Response object

//also create ClientCommunicator class called by server facade, do get, post, delete update etc.. http

public class ServerFacade {  //represents your server to the client, provides simple way to do it
        //2-3 lines of code in each since calls client communicator

    public LoginResponse login(LoginRequest loginRequest) throws Exception {
            System.out.print("server facade login method reached\n");

            // Specify the desired endpoint
            URI uri = new URI("http://localhost:8080/session");
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

            System.out.print("finished connecting\n");

            //Receive the response body
            var statusCode = http.getResponseCode();
            var statusMessage = http.getResponseMessage();


            // Read the response body
            LoginBodyResponse body=null;
            try (InputStream respBody = http.getInputStream()) {
                InputStreamReader inputStreamReader = new InputStreamReader(respBody);
                System.out.printf("respBody for register: %s\n",respBody);
                body = new Gson().fromJson(inputStreamReader, LoginBodyResponse.class);
                System.out.printf("= Response =========\n[%d] %s\n\n%s\n\n", statusCode, statusMessage, body);
                System.out.printf("body response username: %s\nbody response authToken: %s\n",body.username(),body.authToken());
                AuthData addedAuth = new AuthData(body.authToken(),body.username());
                System.out.printf("login addedAuth (before return): %s \n",addedAuth);
                LoginResponse loginResponse =new LoginResponse(addedAuth,statusCode,statusMessage);
                return loginResponse;
            }
    }

    public String register(UserData addUser) throws Exception {
        System.out.print("server facade register method reached\n");

        // Specify the desired endpoint
        URI uri = new URI("http://localhost:8080/user");
        HttpURLConnection http = (HttpURLConnection) uri.toURL().openConnection();
        http.setRequestMethod("POST");

        // Specify that we are going to write out data
        http.setDoOutput(true);

        // Write out a header
        http.addRequestProperty("Content-Type", "application/json");

        // Write out the body
        try (OutputStream outputStream = http.getOutputStream()) {
            var jsonBody = new Gson().toJson(addUser);  //maybe var or some json thing
            outputStream.write(jsonBody.getBytes());
        }


        // Make the request
        http.connect();

        //System.out.print("finished connecting\n");

        //Receive the response body
        var statusCode = http.getResponseCode();
        var statusMessage = http.getResponseMessage();



        // Read the response body
        AuthData resp=null;
        try (InputStream respBody = http.getInputStream()) {
            InputStreamReader inputStreamReader = new InputStreamReader(respBody);
            System.out.printf("respBody for register: %s\n",respBody);
            resp = new Gson().fromJson(inputStreamReader, AuthData.class);
            System.out.printf("= Response =========\n[%d] %s\n\n%s\n\n", statusCode, statusMessage, resp);
            return resp.authToken();
        }
    }

    public void logout(LogoutRequest logoutRequest) throws Exception {
        System.out.print("server facade logout method reached\n");

        // Specify the desired endpoint
        URI uri = new URI("http://localhost:8080/session");
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

        // Output the response body
        try (InputStream respBody = http.getInputStream()) {
            InputStreamReader inputStreamReader = new InputStreamReader(respBody);
            new Gson().fromJson(inputStreamReader, RegisterResponse.class);  //should be nothing
        }
    }


    public ListGamesResponse listGames(String authToken) throws Exception {
        System.out.print("server facade listGames method reached\n");

        // Specify the desired endpoint
        URI uri = new URI("http://localhost:8080/game");
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

        ListGamesBodyResponse response=null;
        // Output the response body
        try (InputStream respBody = http.getInputStream()) {
            InputStreamReader inputStreamReader = new InputStreamReader(respBody);
            response = new Gson().fromJson(inputStreamReader, ListGamesBodyResponse.class);
        }
        return new ListGamesResponse(response, statusCode,statusMessage);
    }

    public CreateGameResponse creatGame(String authToken, CreateGameRequest createGameRequest) throws Exception {
        System.out.print("server facade listGames method reached\n");

        // Specify the desired endpoint
        URI uri = new URI("http://localhost:8080/game");
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

        CreateGameBodyResponse response=null;
        // Output the response body
        try (InputStream respBody = http.getInputStream()) {
            InputStreamReader inputStreamReader = new InputStreamReader(respBody);
            response = new Gson().fromJson(inputStreamReader, CreateGameBodyResponse.class);
        }
        return new CreateGameResponse(response.gameID(),statusCode,statusMessage);
    }

//    public joinGame throws Exception {
//        //
//    }
}
