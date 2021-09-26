package softcare.game.model;

public class GameShare {
    private  Tsp tsp;
    private Game game;

    public GameShare(Tsp tsp, Game game) {
        this.tsp = tsp;
        this.game = game;
    }

    public Tsp getTsp() {
        return tsp;
    }

    public Game getGame() {
        return game;
    }
}
