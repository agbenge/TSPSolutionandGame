package softcare.game.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import softcare.gui.PointXY;
import softcare.util.S;

public class Game {
    private int level;
    private int scores;
   private  final int TIME_LIMIT= 5000;
    private  int min;
    private double cost;
    private  long usedTime;
    private long gameLifeTime;

    public void setLevel(int level) {
        this.level = level;
    }

    public Game() {
        this.level = 0 ;
        direction = new ArrayList<>();
    }

    public int getMin() {
        return min;
    }

    public long getUsedTime() {
        return usedTime;
    }

    public int getTryAgain() {
        return tryAgain;
    }

    public long getGameLifeTime() {
        return gameLifeTime;
    }


    public int getScores() {
        return scores;
    }

    public void setMin(int min) {
        this.min = min;
    }
    public int getBound() {
        return level+10;
    }

    public int getLevel() {
        return level;
    }

    public int getTiming() {
        return getLevel()*2000+TIME_LIMIT;
    }

    public double getCost() {
        return cost;
    }

    public int getNodes() {
        return level + 3;
    }
 private int tryAgain;
    private List<Integer> direction;

    public List<Integer> getDirection() {
        return direction;
    }
   public void addDirection(Integer dir){
        direction.add(dir) ;
    }
    public void removeDirection(Integer dir){
        direction.remove(dir) ;
    }

    public void next( ) {
        tryAgain=0;
        direction = new ArrayList<>();
        this. usedTime=0L;
    }
    public void win(boolean betterThanAlg, long _usedTime) {
        long allTimeAtLevelX= this.usedTime+_usedTime;
        level++;
        // add mechanism for increasing level
        scores= scores +level+ (int)(((getTiming()/1000)*level)
                /allTimeAtLevelX)+   ( betterThanAlg?getNodes():0);

        gameLifeTime+=allTimeAtLevelX;
        this. usedTime=0L;
    }

    public void tryAgain() {
        direction = new ArrayList<>();
        tryAgain++;
        gameLifeTime+=usedTime;
        this. usedTime=0L;
    }

    public String getResult(Tsp tsp) { /// presenting path as result
        double cost = 0;
        if (getDirection() != null) {
            if (getDirection().size() < 2) {
                return "Action have no defined output yet...";
            }
        } else return "Action have no defined output yet... Null Error";
        String res1="";
        String res = "\n";
        res += "\tPath: ";
        double dist[] = new double[getDirection().size()];
        int prevouse = getDirection().get(0);

        for (int x = 1; x < getDirection().size(); x++) {
            int i = getDirection().get(x);
            res += tsp.getCities().get(prevouse) + "\t";
            res += "\t" +  S.formDouble(tsp.getMatrix()[prevouse][i]) + "\tto\t";

            dist[prevouse] = tsp.getMatrix()[prevouse][i];
            cost = cost + tsp.getMatrix()[prevouse][i];

            prevouse = i;
        }
        cost = cost + tsp.getMatrix()[prevouse][getDirection().get(0)];
        res += tsp.getCities().get(prevouse) + "\t";
        res1 += "\nTotal distance by you \t" + S.formDouble(cost)+
                "\nExpected cost is "+S.formDouble(tsp.getCost());


        this.cost = cost;
        return res1+res;

    }


    public void pause(long _usedTime) {
        usedTime+=_usedTime;
    }

}
