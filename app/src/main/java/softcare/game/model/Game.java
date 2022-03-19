package softcare.game.model;

import android.content.Context;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import softcare.game.R;
import softcare.gui.PointXY;
import softcare.util.S;

public class Game {
    private int level;
    private int scores;
    public   final int defaultLevelToNode= 3;
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

    public int getBound() {
        return level+10;
    }

    public int getLevel() {
        return level;
    }

    public int getTiming() {
        int TIME_LIMIT = 5000;
        return getLevel()*2000+ TIME_LIMIT;
    }

    public double getCost() {
        return cost;
    }

    public int getNodes() {
        return level + defaultLevelToNode;
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

    public void next( boolean betterThanAlg, long _usedTime) {
        long allTimeAtLevelX= this.usedTime+_usedTime;
        level++;
        // add mechanism for increasing level
        scores= scores +level+ (int)(((getTiming()/1000)*level)
                /allTimeAtLevelX)+   ( betterThanAlg?getNodes():0);

        gameLifeTime+=allTimeAtLevelX;
        this. usedTime=0L;


        tryAgain=0;
        direction = new ArrayList<>();
        this. usedTime=0L;
    }
    public int getWinScores(boolean betterThanAlg, long _usedTime) {
        long allTimeAtLevelX= this.usedTime+_usedTime;
        int level =this.level+1;
        // add mechanism for increasing level
        return scores +level+ (int)(((getTiming()/1000)*level)
                /allTimeAtLevelX)+   ( betterThanAlg?getNodes():0);
    }

    public void tryAgain() {
        direction = new ArrayList<>();
        tryAgain++;
        gameLifeTime+=usedTime;
        this. usedTime=0L;
    }

    public String getResult(Tsp tsp , Context context) { /// presenting path as result
        double cost = 0;
        if (getDirection() != null) {
            if (getDirection().size() < 2) {
                Log.e(CodeX.tag,"Action have no defined output yet... Size less than 2");
                return context.getString(R.string.undefined_output);
            }
        } else{
                Log.e(CodeX.tag,"Action have no defined output yet... Null Error");
            return  context.getString(R.string.undefined_output);
        }
        String res1="";
        String res = "\n";
        res += context.getString(R.string.path);
        double[] dist = new double[getDirection().size()];
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
        res1 += context.getString(R.string.distances_by_u) + S.formDouble(cost)+
                context.getString(R.string.distance_by_alg)+ S.formDouble(tsp.getCost());


        this.cost = cost;
        return res1+res;

    }


    public void pause(long _usedTime) {
        usedTime+=_usedTime;
    }

}
