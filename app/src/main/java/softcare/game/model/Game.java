package softcare.game.model;

import android.content.Context;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import softcare.game.R;

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

    public String getResult(Tsp tsp, Context context) {
        if (getDirection() == null || getDirection().size() < 2) {
            Log.e(CodeX.tag, "Action has no defined output yet... Invalid path");
            return context.getString(R.string.undefined_output);
        }

        double cost = 0;
       // double[] dist = new double[getDirection().size()];
        StringBuilder pathBuilder = new StringBuilder();
        int previous = getDirection().get(0);

        pathBuilder.append(context.getString(R.string.path)).append(": ");

        for (int x = 1; x < getDirection().size(); x++) {
            int current = getDirection().get(x);
            double segment = tsp.getMatrix()[previous][current];
            cost += segment;
           // dist[previous] = segment;  // add distance to previous city


            // Append city and segment info
            pathBuilder.append(tsp.getCities().get(previous).getName())
//                    .append(" (")
//                    .append(Util.formDouble(segment))
//                    .append(")")
                    .append("→");

            // Update previous
            previous = current;
        }

        // Add return to start city
        cost += tsp.getMatrix()[previous][getDirection().get(0)];
        pathBuilder.append(tsp.getCities().get(previous).getName())
                .append("→")
                .append(tsp.getCities().get(getDirection().get(0)).getName());

        // Determine win or lose
        double expected = tsp.getCost();
        boolean isWin = Math.abs(cost - expected) < 0.001 || cost <= expected;

        this.cost = cost;

        // Use the GameMessage class
        return GameMessage.buildResultMessage(
                context,
                isWin,
                expected,
                cost,
                pathBuilder.toString()
        );
    }


    public void pause(long _usedTime) {
        usedTime+=_usedTime;
    }

}
