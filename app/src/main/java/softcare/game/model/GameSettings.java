package softcare.game.model;

public class GameSettings {
    private int level;
    private int  miniDist;
    private  int maxDist;
    private int  timing;

    public int getNodes() {
        return level+2;
    }
}
