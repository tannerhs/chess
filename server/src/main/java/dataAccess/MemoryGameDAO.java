package dataAccess;

import model.GameData;
import responses.createGameResponse;

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
    public createGameResponse createGame(String gameName) {
        int gameID=gameIDCounter+1;
        games.add(new GameData(gameID,gameName));
        return new createGameResponse(gameID);
    }

    @Override
    public GameData getGame(int gameID) {
        return null;
    }

    @Override
    public void updateGame(GameData gameData) {

    }

    @Override
    public GameData[] listGames() {
        return new GameData[0];
    }

    @Override
    public GameData get(int i) {
        return games.get(i);
    }

    //public GameData get(int gameID) //FIXME


    @Override
    public int size() {
        return games.size();
    }
}
