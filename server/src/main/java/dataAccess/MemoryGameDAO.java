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
        for(int i=0; i<games.size();i++) {
            if(gameID==games.get(i).gameID()) {
                return games.get(i);
            }
        }
        return null;
    }

    @Override
    public int getGameIndex(int gameID) {
        for(int i=0; i<games.size();i++) {
            if(gameID==games.get(i).gameID()) {
                return i;
            }
        }
        return -1;
    }

    @Override
    public void joinGame(int GameID, String username, String playerColor) {
        GameData currentGame = getGameByID(GameID);
        int gameIndex = getGameIndex(GameID);
        if(playerColor==null){
            //observer
        }
        else if(playerColor=="WHITE") {
            games.set(gameIndex,new GameData(GameID,username, currentGame.blackUsername(), currentGame.gameName(), currentGame.game()) );
            //
        }
        else if(playerColor=="BLACK"){
            games.set(gameIndex,new GameData(GameID, currentGame.whiteUsername(), username, currentGame.gameName(), currentGame.game()) );

        }
        else {
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
        return games.size();
    }
}
