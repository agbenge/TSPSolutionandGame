package softcare.game.model;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import softcare.algorithm.SelectionType;
import softcare.algorithm.TSPNearestNeighbour;
import softcare.algorithm.TspDynamicProgrammingIterative;
import softcare.gui.PointXY;

public class GameViewModel extends ViewModel {

    private MutableLiveData<Tsp> tspLiveData;
    private MutableLiveData<ProgressX> progressXLiveData;
    private MutableLiveData<UpdateCode> updateCodeLiveData;
    private MutableLiveData<Game> gameLiveData;
    public MutableLiveData<Tsp> getTspLiveData() {
        if (tspLiveData == null) tspLiveData = new MutableLiveData<>();
        return tspLiveData;
    }

    public MutableLiveData<UpdateCode> getUpdateCodeLiveData() {
        if (updateCodeLiveData == null) updateCodeLiveData = new MutableLiveData<>();
        return updateCodeLiveData;
    }

    public MutableLiveData<Game> getGameLiveData() {
        if(gameLiveData ==null) gameLiveData =new MutableLiveData<>();
        return gameLiveData;
    }

    public Game getGame() {
        if (gameLiveData != null)
            return gameLiveData.getValue();
        return null;
    }
    public Tsp getTsp() {
        if (tspLiveData != null)
            return tspLiveData.getValue();
        return null;
    }

    public MutableLiveData<ProgressX> getProgressXLiveData() {
        if (progressXLiveData == null) progressXLiveData = new MutableLiveData<>();
        return progressXLiveData;
    }


    public void startAgl(Tsp tsp) {
        long start = System.currentTimeMillis();
        long duration;
        long end;

        switch (tsp.getAlg()) {
            case DYN: {

                if (tsp.getCities().size() <= 20) {
                    TspDynamicProgrammingIterative dyn = new TspDynamicProgrammingIterative(0, tsp.getDataDouble());
                    dyn.solve();
                    System.out.println(dyn.getTour().toString());
                    tsp.setDirection(dyn.getTour());
                    System.out.println(dyn.getTourCost());
                    tsp.setCost(dyn.getTourCost());
                    end = System.currentTimeMillis();
                    duration = end - start;
                    tsp.setDuration(duration);
                    System.out.println(duration);
                    System.out.println();
                    tsp.setTspActions(TspCode.SOLVED);
                    tspLiveData.postValue(tsp);
                } else {
                    System.out.println("error size is " + tsp.getCities().size());
                    updateCodeLiveData.postValue(UpdateCode.DYN_MAX_REACHED);
                }

                break;
            }



            case KNN: {

                start = System.currentTimeMillis();
                TSPNearestNeighbour knn = new TSPNearestNeighbour();
                knn.tsp(tsp.getDataInt());

                //System.out.println(dyn.getTour().toString());
                //mDirection = dyn.getTour();
                //	System.out.println(dyn.getTourCost());
                //mCost = dyn.getTourCost();
                //System.out.println("  cost +>> " + String.valueOf(knn.getCost()));
               // System.out.println("  direction +>> " + String.valueOf(knn.getTour()));
                end = System.currentTimeMillis();
                System.out.println();
                tsp.setDirection(knn.getTour());
                tsp.setCost(knn.getCost());
                duration = end - start;
                tsp.setDuration(duration);
                System.out.println(duration);

                tsp.setTspActions(TspCode.SOLVED);
                tspLiveData.postValue(tsp);
                break;
            }
            case EMPTY: {
                updateCodeLiveData.postValue(UpdateCode.NO_ALG);
                break;
            }
            default:
                throw new IllegalStateException("Unexpected value: " + tsp.getAlg());
        }

    }



    private PointXY getUniquePoint(List<PointXY> existingPoints, Random random, Game game, int index) {
        int bound = game.getBound();
        PointXY point;

        int safetyCounter = 0; // Prevent infinite loop if all points are taken
        do {
            int x = random.nextInt(bound);
            int y = random.nextInt(bound);
            point = new PointXY(x, y);
            safetyCounter++;
        } while (containsPoint(existingPoints, point) && safetyCounter < bound * bound);

        if (containsPoint(existingPoints, point)) {
            Log.w(CodeX.tag, "Failed to find unique point after many attempts!");
        }

        Log.d(CodeX.tag, index + " ---> x: " + point.x + "  y: " + point.y);
        return point;
    }

    private boolean containsPoint(List<PointXY> points, PointXY newPoint) {
        for (PointXY p : points) {
            if (p.x == newPoint.x && p.y == newPoint.y) {
                return true;
            }
        }
        return false;
    }

    public void start(Game game) {
        Random r= new Random();
        List<PointXY> p=  new ArrayList<>();
        List<CityInfo> emojiInfos=  new ArrayList<>();
        for (int i = 0; i<game.getNodes() ;i++){
             p.add(getUniquePoint(p,r,game,i));
             emojiInfos.add(CityInfo.getEmoji(i));
        }
        Tsp tsp = getTspLiveData().getValue();

        if(tsp==null) {
            tsp = new Tsp();
            tsp.setPointXY(p);
            tsp.setCities(emojiInfos);
            tsp.countDistancesAndUpdateMatrix();
        }else {
                tsp.setPointXY(p);
                tsp.setCities(emojiInfos);
                tsp.countDistancesAndUpdateMatrix();
        }



        /// run in background thread...
        if(emojiInfos.size()<20){
            tsp.setAlg(Alg.DYN);
        }else {
            tsp.setAlg(Alg.KNN);
        }
        if(emojiInfos.size()>2)
        startAgl(tsp);
        else {
            updateCodeLiveData.setValue(UpdateCode.NOT_SOLVED);
        }
    }






    public void setGameLiveData(Game _game) {
       getGameLiveData().setValue(_game);
    }
    public void tryAgain(Game game, boolean refreshPoints) {
        if(refreshPoints) start(game);
        else {
            Tsp tsp = getTspLiveData().getValue();
            tspLiveData.setValue(tsp);
        }

    }


    public void next(Game game) {
        start(game);
    }
    public void newGame(int i) {
        Game game = new Game();
        game.setLevel(i);
        setGameLiveData(game);
        start(game);
    }

    public void resumeGame(Tsp tsp, Game game) {
        getGameLiveData().setValue(game );
        getTspLiveData().setValue(tsp);
    }

    public void startGameShare(@NonNull Location share) {
        Game game= new Game();
        game.setLevel(share.getNames().size()-game.defaultLevelToNode);
        Tsp tsp = new Tsp();
        tsp.setCities(CityInfo.getCitiesEmoji(share.getNames().size()));
        tsp.setPointXY(share.getLocations());
        tsp.countDistancesAndUpdateMatrix();
        getGameLiveData().postValue(game);
        if(share.getNames().size()<20){
            tsp.setAlg(Alg.DYN);
        }else {
            tsp.setAlg(Alg.KNN);
        }
        if(share.getNames().size()>2)
            startAgl(tsp);
        else
            updateCodeLiveData.setValue(UpdateCode.NOT_SOLVED);
    }
}
