package softcare.game.model;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import softcare.algorithm.SalesmanGenome;
import softcare.algorithm.Salesmensch;
import softcare.algorithm.SelectionType;
import softcare.algorithm.TSPNearestNeighbour;
import softcare.algorithm.TspDynamicProgrammingIterative;
import softcare.gui.PointXY;
import softcare.util.S;

public class GameViewModel extends ViewModel {

    private MutableLiveData<Tsp> tspLiveData;
    private MutableLiveData<ProgressX> progressXLiveData;
    private MutableLiveData<ErrorCode> errorCodeLiveData;

    public MutableLiveData<Tsp> getTspLiveData() {
        if (tspLiveData == null) tspLiveData = new MutableLiveData<>();
        return tspLiveData;
    }

    public MutableLiveData<ErrorCode> getErrorCodeLiveData() {
        if (errorCodeLiveData == null) errorCodeLiveData = new MutableLiveData<>();
        return errorCodeLiveData;
    }

    public Tsp getTsp() {
        if (tspLiveData == null)
            return tspLiveData.getValue();
        return null;
    }

    public MutableLiveData<ProgressX> getProgressXLiveData() {
        if (progressXLiveData == null) progressXLiveData = new MutableLiveData();
        return progressXLiveData;
    }

    boolean stop;


    public long startAgl(Alg alg) {
        Tsp tsp = tspLiveData.getValue();
        long start = System.currentTimeMillis();
        long duration = 0L;
        long end;
        tsp.setAlg(alg);
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
                    errorCodeLiveData.postValue(ErrorCode.DYN_MAX_REACHED);
                }

                break;
            }

            case GEN: {
                start = System.currentTimeMillis();
                Salesmensch geneticAlgorithm = new
                        Salesmensch(tsp.getCities().size(), SelectionType.ROULETTE, tsp.getDataInt(), 0, 0);
                SalesmanGenome result = geneticAlgorithm.optimize();
                end = System.currentTimeMillis();
                System.out.println(result);
                tsp.addDirection(result.getStartingCity());
                tsp.addDirection(result.getGenome());
                tsp.addDirection(result.getStartingCity());
                tsp.setCost(result.getFitness());
                duration = end - start;
                System.out.println(duration);

                tsp.setTspActions(TspCode.SOLVED);
                tspLiveData.postValue(tsp);
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
                System.out.println();
                System.out.println("  cosst +>> " + String.valueOf(knn.getCost()));
                System.out.println("  direction +>> " + String.valueOf(knn.getTour()));
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
                errorCodeLiveData.postValue(ErrorCode.NO_ALG);
                break;
            }
            default:
                throw new IllegalStateException("Unexpected value: " + tsp.getAlg());
        }

        return duration;
    }


    protected String geFullResult() { /// presenting path as result
        Tsp tsp = tspLiveData.getValue();
        if (TspCode.SOLVED != tsp.getTspActions()) {
            errorCodeLiveData.postValue(ErrorCode.NOT_SOLVED);
            return "";
        }
        double cost = 0;
        if (tsp.getDirection() != null) {
            if (tsp.getDirection().size() < 2) {
                return "Action have no defined output yet...";
            }
        } else return "Action have no defined output yet... Null Error";
        String res = "Movement\n";
        res += "\tPath: ";
        double dist[] = new double[tsp.getDirection().size()];
        int prevouse = tsp.getDirection().get(0);

        for (int x = 1; x < tsp.getDirection().size(); x++) {
            int i = tsp.getDirection().get(x);
            res += tsp.getDirection().get(prevouse) + "\t";
            res += "\t" + tsp.getMatrix()[prevouse][i] + "\tto\t";

            dist[prevouse] = tsp.getMatrix()[prevouse][i];
            cost = cost + tsp.getMatrix()[prevouse][i];
            prevouse = i;
        }

        res += tsp.getCities().get(prevouse) + "\t";
        if (tsp.getCost() == 0) {
            tsp.setCost(cost);
        }
        res += "\n Total distances \t" + tsp.getCost();
        if (tsp.getDuration() > 0L) {
            double time = (double) tsp.getDuration() / (double) 1000;
            if (time != 1)
                res += "\n Total time in seconds\t" + time + " seconds";
            else
                res += "\n Total time  in seconds\t" + time + " second";

        } else {
            res += "\n Total time in seconds\t" + " is zero seconds";
        }
        //res += "\n Total time \t" + d;
        res += "\n\n  Path: ";
        prevouse = tsp.getDirection().get(0);
        for (int x = 0; x < tsp.getDirection().size(); x++) {
            int i = tsp.getDirection().get(x);
            res += tsp.getCities().get(i) + "\t";
        }
        return res;

    }


}
