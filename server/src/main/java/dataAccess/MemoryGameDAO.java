package dataAccess;

import model.GameData;
import responses.CreateGameResponse;
import java.util.ArrayList;
import java.util.List;

public class MemoryGameDAO implements GameDAO {
    static List<GameData> games = new ArrayList<>();
    static int gameIDCounter=0;
    @Override
    public void clear() {
        while(!games.isEmpty()) {
            games.removeFirst();  //removes 0th item
        }
    }


    @Override
    public CreateGameResponse createGame(String gameName) throws BadRequestException {
        System.out.println("reached createGame");
        if(games==null) {
            throw new BadRequestException("{ \"message\": \"Error: bad request\" }");
        }
        gameIDCounter+=1;
        games.add(new GameData(gameIDCounter,gameName));
        return new CreateGameResponse(gameIDCounter);
    }

    @Override
    public List<GameData> listGames() {
        return games;
    }

    @Override
    public GameData getGameByID(int gameID) {
        if(games==null || games.isEmpty()) return null;
        for(int i=0; i<games.size();i++) {
            if(gameID==games.get(i).gameID()) {
                return games.get(i);
            }
        }
        return null;
    }

    @Override
    public int getGameIndex(int gameID) {
        if(games==null || games.isEmpty()) return -1;
        for(int i=0; i<games.size();i++) {
            if(gameID==games.get(i).gameID()) {
                return i;
            }
        }
        return -1;
    }

    @Override
    public void joinGame(int gameID, String username, String playerColor) throws Exception {
        GameData currentGame = getGameByID(gameID);
        int gameIndex = getGameIndex(gameID);
        if(gameIndex==-1) {
            System.out.println("is this reached?");
        }
        else if(playerColor == null || playerColor.isEmpty()){  //needs to be ==null, not .equals()
            //observer
        }
        else if(playerColor.equals("WHITE")) {
            games.set(gameIndex,new GameData(gameID,username, currentGame.blackUsername(), currentGame.gameName(), currentGame.game()) );
            System.out.println("games.size(): "+games.size());
        }
        else if(playerColor.equals("BLACK")){
            games.set(gameIndex,new GameData(gameID, currentGame.whiteUsername(), username, currentGame.gameName(), currentGame.game()) );
            System.out.println("games.size(): "+games.size());
        }
        else {
            throw new Exception("{\"message\": \"Error: already taken\" }");
            //observer
        }

    }


    @Override
    public int size() {
        if(games==null) {
            return 0;
        }
        return games.size();
    }
}
