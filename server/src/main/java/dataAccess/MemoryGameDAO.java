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
        if(games==null || games.size()==0) {
            return;
        }
        for(int i=0; i<games.size(); i++) {
            games.remove(0);
        }

    }





    @Override
    public CreateGameResponse createGame(String gameName) {
        gameIDCounter+=1;
        games.add(new GameData(gameIDCounter,gameName));
        return new CreateGameResponse(gameIDCounter);
    }

    @Override
    public GameData getGameByIndex(int index) {
        return games.get(index);
    }

    @Override
    public void updateGame(GameData gameData) {

    }

    @Override
    public List<GameData> listGames() {
        return games;
    }

    @Override
    public GameData getGameByID(int gameID) {
        if(games==null || games.size()==0) return null;
        for(int i=0; i<games.size();i++) {
            if(gameID==games.get(i).gameID()) {
                return games.get(i);
            }
        }
        return null;
    }

    @Override
    public int getGameIndex(int gameID) {
        if(games==null || games.size()==0) return -1;
        for(int i=0; i<games.size();i++) {
            if(gameID==games.get(i).gameID()) {
                return i;
            }
        }
        return -1;
    }

    @Override
    public void joinGame(int GameID, String username, String playerColor) throws Exception {
        GameData currentGame = getGameByID(GameID);
        int gameIndex = getGameIndex(GameID);
        if(gameIndex==-1) {
            //
        }
        else if(playerColor==null || playerColor==""){
            //observer
        }
        else if(playerColor.equals("WHITE")) {
            games.set(gameIndex,new GameData(GameID,username, currentGame.blackUsername(), currentGame.gameName(), currentGame.game()) );
            System.out.println("games.size(): "+games.size());
        }
        else if(playerColor.equals("BLACK")){
            games.set(gameIndex,new GameData(GameID, currentGame.whiteUsername(), username, currentGame.gameName(), currentGame.game()) );
            System.out.println("games.size(): "+games.size());
        }
        else {
            throw new Exception("{\"message\": \"Error: already taken\" }");
            //observer
        }
        //return null;
    }

    //public GameData get(int gameID) //FIXME

    String getGameListJson() {
        //
        return "";
    }


    @Override
    public int size() {
        if(games==null) {
            return 0;
        }
        return games.size();
    }
}
