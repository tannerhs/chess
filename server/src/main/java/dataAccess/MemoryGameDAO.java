package dataAccess;

import model.GameData;

import java.util.ArrayList;
import java.util.List;

public class MemoryGameDAO implements GameDAO {
    static List<GameData> games = new ArrayList<>();
    @Override
    public void clear() {
        for(int i=0; i<games.size(); i++) {
            games.remove(0);
        }

    }

    @Override
    public int createGame(GameData gameData) {
        return 0;
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
