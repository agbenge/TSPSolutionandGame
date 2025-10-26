package softcare.game.model;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.io.InputStream;
import java.io.OutputStream;

import softcare.algorithm.SalesmanGenome;
import softcare.algorithm.Salesmensch;
import softcare.algorithm.SelectionType;
import softcare.algorithm.TSPNearestNeighbour;
import softcare.algorithm.TspDynamicProgrammingIterative;
import softcare.gui.PointXY;
import softcare.util.Util;

public class SolutionViewModel extends ViewModel {

    private MutableLiveData<Tsp> tspLiveData;
    private MutableLiveData<ProgressX> progressXLiveData;
    private MutableLiveData<UpdateCode> updateCodeLiveData;
    public MutableLiveData<Tsp> getTspLiveData() {
        if (tspLiveData == null) tspLiveData = new MutableLiveData<>();
        return tspLiveData;
    }

    public MutableLiveData<UpdateCode> getUpdateCodeLiveData() {
        if (updateCodeLiveData == null) updateCodeLiveData = new MutableLiveData<>();
        return updateCodeLiveData;
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

    boolean stop;

    protected void readTSP_XY(String tspString) {
        ProgressX progressX = new ProgressX("Reading TSP X, Y");
        Tsp tsp = new Tsp();
        boolean isOkay=false;
        if (tspString != null) {
            String[] lines = tspString.split("\n");
            int pp = 1;
            for (String li : lines) {
                tsp.addHeader(li + "\n");
                if (li.trim().contains("NODE_COORD_SECTION")) {
                    isOkay=true;
                    break;
                }
                pp++;
            }
if(isOkay) {
    for (int i = pp; i < lines.length - 1; i++) {
        System.out.println(" Result " + lines[i]);
        String[] name_X_Y = lines[i].trim().split("\\s+");
        System.out.println(" Debug size " + name_X_Y.length);
        if (name_X_Y.length == 3) {
            tsp.addCity(CityInfo.getCityInfo(i));
            try {
                double x = Double.parseDouble(name_X_Y[1]);
                double y = Double.parseDouble(name_X_Y[2]);
                tsp.addPointXY(new PointXY(x, y));
            } catch (Exception e) {

                updateCodeLiveData.postValue(UpdateCode.DATA_FORMAT);
                return;
            }
        } else {
            for (String s : name_X_Y) {
                System.out.println(" ERR Debug data " + s);
            }

            return;
        }
        //ps.setProgress(0.5 + ((i / lines.length) / 2), " setting " + i + " of " + lines.length);

        progressX.setProgress(i);
        progressX.setSize(lines.length);
        progressXLiveData.postValue(progressX);
        if (stop) {

            updateCodeLiveData.postValue(UpdateCode.DATA_FORMAT);
            return;
        }
    }
}else{
updateCodeLiveData.postValue(UpdateCode.DATA_FORMAT);
return;
}
            System.out.println(" read ts  " + (lines.length - 7));
            tsp.setTspActions(TspCode.READ);
            tsp.countDistancesAndUpdateMatrix();
            tspLiveData.postValue(tsp);

        }
    }

    public void startAgl(Alg alg, TaskManager taskManager) {

        Tsp tsp = tspLiveData.getValue();
        if (tsp != null){
            taskManager.runTask(() -> {
                long start = System.currentTimeMillis();
                long duration;
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
                            tsp.setResult(getResult(tsp));
                            tspLiveData.postValue(tsp);


                        } else {
                            System.out.println("error size is " + tsp.getCities().size());
                            updateCodeLiveData.postValue(UpdateCode.DYN_MAX_REACHED);
                        }

                        break;
                    }

                    case GEN: {


                        start = System.currentTimeMillis();
                        Salesmensch geneticAlgorithm = new
                                Salesmensch(tsp.getCities().size(),
                                SelectionType.ROULETTE,
                                tsp.getDataInt(), 0, 0);
                        Log.d(CodeX.tag, "call optimised");
                        SalesmanGenome result = geneticAlgorithm.optimize();
                        Log.d(CodeX.tag, "finish");
                        end = System.currentTimeMillis();
                        System.out.println(result);
                        tsp.addDirection(result.getStartingCity());
                        tsp.addDirection(result.getGenome());
                        tsp.addDirection(result.getStartingCity());
                        tsp.setCost(result.getFitness());
                        duration = end - start;
                        tsp.setDuration(duration);
                        System.out.println(duration);

                        tsp.setTspActions(TspCode.SOLVED);
                        tsp.setResult(getResult(tsp));
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
                        // System.out.println("  cosst +>> " + String.valueOf(knn.getCost()));
                        // System.out.println("  direction +>> " + String.valueOf(knn.getTour()));
                        end = System.currentTimeMillis();
                        System.out.println();
                        tsp.setDirection(knn.getTour());
                        tsp.setCost(knn.getCost());
                        duration = end - start;
                        tsp.setDuration(duration);
                        System.out.println(duration);

                        tsp.setTspActions(TspCode.SOLVED);
                        tsp.setResult(getResult(tsp));
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

            });
    }else{ updateCodeLiveData.postValue(UpdateCode.UNKNOWN); }
    }


    public boolean readTSP_D(String data) {
        System.out.println(data);
        ProgressX progressX = new ProgressX("Reading Tsp x, y ");
        String resH;
        Tsp tsp = new Tsp();
        String seperator = "\\s+";
        String[] lines = data.split("\n");
        boolean res = true;
        String name = "TSP" + (lines.length - 1);
        String comment = "Raph-Ray TSP solution ";
        String type = "TSP";
        String edgeType = "EUC_2D";
        resH = name + "\n";
        resH += "TYPE: " + type + "\n";
        resH += "COMMENT: " + comment + "\n";
        resH += "DIMENSION: " + (lines.length - 1) + "\n";
        resH += "EDGE_WEIGHT_TYPE: " + edgeType + "\n";
        resH += "NODE_COORD_SECTION\n";
        tsp.setHeader(resH);

        double[][] mMatrix = new double[tsp.getDefaultSize()][tsp.getDefaultSize()];
        int x = -1, y = -1;
        int k = 0;
        progressX.setSize(lines.length);
        for (String line : lines) {
            String[] columns = line.trim().split(seperator);
            progressX.setProgressPercent((((double) x / (double) lines.length) / (double) 2));
            progressX.setProgress(k);
            System.out.println(y + " PROCESSING LINE " + line);
            if (y == -1) {
                y++;
                continue;
            }
            x = -1;
            if (columns.length == lines.length) {


                for (String column : columns) {
                    if (x < y) {
                        if (x != -1) {
                            mMatrix[x][y] = Double.parseDouble(column.trim().replace(" ", ""));
                            mMatrix[y][x] = mMatrix[x][y];
                            System.out.print("  |" + column + "|  ==> " + mMatrix[x][y]
                                    + " position " + x + " , " + y);
                            x++;
                        } else {
                            tsp.addCity(new CityInfo( tsp.getCities().size()+1,column, column+"["+(tsp.getCities().size()+1)+"]"));
                            x = 0;
                        }
                    } else {
                        break;
                    }
                }
            } else {
                System.out.println(" col null or miss match lenght");
                System.out.println(" col    lenght " + columns.length +
                        "  lines " + lines.length);

                for (String column : columns) {
                    System.out.println(" col    data " + column);
                }

                res = false;
                break;
            }
            System.out.println();
            System.out.print(y + " LinE ");
            System.out.println();
            y++;
        }


        tsp.setMatrix(mMatrix);

        if (res) {
            tsp.setLocation();
            tsp.setTspActions(TspCode.READ);
            tspLiveData.postValue(tsp);
        } else
            updateCodeLiveData.postValue(UpdateCode.DATA_FORMAT);

        return res;

    }


    /// preview full Result,
    public String getPreviewXY() {
        Tsp tsp = tspLiveData.getValue();
        if (TspCode.READ != tsp.getTspActions() && TspCode.UPDATE != tsp.getTspActions()) {
            updateCodeLiveData.postValue(UpdateCode.NOT_READ_OR_UPDATED);
            return "";
        }

        StringBuilder  res = new StringBuilder(tsp.getHeader());
        for (int i = 0; i < tsp.getCities().size(); i++) {
            res.append(tsp.getCities().get(i).getName().replace(" ", "_")).append("    ").append(Util.doubleToString(tsp.getPointXY().get(i).getX())).append("\t\t ").append(Util.doubleToString(tsp.getPointXY().get(i).getY()));
            res.append("\n");
        }

        return res+"";
    }


    public String getPreview() {

        Tsp tsp = tspLiveData.getValue();
        if(tsp==null){
            updateCodeLiveData.postValue(UpdateCode.NOT_READ_OR_UPDATED);
            return "";
        }
        if (TspCode.READ != tsp.getTspActions() && TspCode.UPDATE != tsp.getTspActions()) {
            updateCodeLiveData.postValue(UpdateCode.NOT_READ_OR_UPDATED);
            return "";
        }
        StringBuilder res = new StringBuilder();
        for (CityInfo c : tsp.getCities()) {
            res.append("\t").append(c.getName());
        }
        res.append("\n");
        for (int i = 0; i < tsp.getCities().size(); i++) {
            res.append(tsp.getCities().get(i));
            for (int j = 0; j < tsp.getCities().size(); j++) {
                res.append("\t").append(Util.doubleToString(tsp.getMatrix()[i][j]));
            }

            res.append("\n");

        }

        return res.toString();

    }


    protected String getResult(Tsp tsp) { /// presenting path as result

        if (TspCode.SOLVED != tsp.getTspActions()) {
            updateCodeLiveData.postValue(UpdateCode.NOT_SOLVED);
            return "";
        }
        double cost = 0;
        if (tsp.getDirection() != null) {
            if (tsp.getDirection().size() < 2) {
                return "Action have no defined output yet...";
            }
        } else return "Action have no defined output yet... Null Error";
        StringBuilder res = new StringBuilder("Movement\n");
        res.append("\tPath: ");
        double[] dist = new double[tsp.getDirection().size()];
        int prevouse = tsp.getDirection().get(0);

        for (int x = 1; x < tsp.getDirection().size(); x++) {
            int i = tsp.getDirection().get(x);
            res.append(tsp.getCities().get(prevouse).getName()).append("\t");
            res.append("\t").append(Util.doubleToString(tsp.getMatrix()[prevouse][i])).append("\tto\t");

            dist[prevouse] = tsp.getMatrix()[prevouse][i];
            cost = cost + tsp.getMatrix()[prevouse][i];
            prevouse = i;
        }

        res.append(tsp.getCities().get(prevouse).getName()).append("\t");
        if (tsp.getCost() == 0) {
            tsp.setCost(cost);
        }
        res.append("\n Total distances \t").append(Util.doubleToString(tsp.getCost()));
        if (tsp.getDuration() > 0L) {
            double time = (double) tsp.getDuration() / (double) 1000;
            if (time != 1)
                res.append("\n Total time in seconds\t").append(time).append(" seconds");
            else
                res.append("\n Total time  in seconds\t").append(time).append(" second");

        } else {
            res.append("\n Total time in seconds\t" + " is zero seconds");
        }
        //res += "\n Total time \t" + d;
        res.append("\n\n  Path: ");
        prevouse = tsp.getDirection().get(0);
        for (int x = 0; x < tsp.getDirection().size(); x++) {
            int i = tsp.getDirection().get(x);
            res.append(tsp.getCities().get(i).getName()).append("\t");
        }
        return res.toString();

    }

    public void addCity(String name, double[] distance) {
        Log.d(CodeX.tag, " D adding " + name);
        Tsp tsp = tspLiveData.getValue();
        if (tsp != null) {
            if (distance != null)
                updateTsp(tsp, name, distance);
        } else {
            if (distance == null) {
                tsp = new Tsp();
                updateTsp(tsp, name, distance);
                return;
            }
            Log.e(CodeX.tag, "Tsp value is null");
        }
    }

    private void updateTsp(Tsp tsp, String name, double[] distance) {
        tsp.addCity(new CityInfo(tsp.getCities().size()+1, name, name+"["+(tsp.getCities().size()+1)+"]"));
        if (tsp.updateMatrix(tsp.getCities().size() - 1, distance)) {
            tsp.setLocation(tsp.getCities().size() - 1);
            tsp.setTspActions(TspCode.UPDATE);
            tspLiveData.setValue(tsp);
        } else {
            Log.e(CodeX.tag, "data unable to update");
            updateCodeLiveData.postValue(UpdateCode.UNKNOWN);
        }
    }

    public void addCityXY(String name, PointXY _pointXY) {
        Log.d(CodeX.tag, "L adding " + name);
        Tsp tsp = tspLiveData.getValue();
        if (tsp == null) tsp = new Tsp();
        tsp.addPointXY(_pointXY);
        tsp.addCity(new CityInfo(tsp.getCities().size()+1, name, name+"["+(tsp.getCities().size()+1)+"]" ));
        tsp.countDistancesAndUpdateMatrix();
        tsp.setTspActions(TspCode.UPDATE);
        tspLiveData.setValue(tsp);
    }

    public void clear() {
        if (tspLiveData != null)
            tspLiveData.setValue(null);
    }

    public void addMapData(TspData data, String title) {
        Tsp tsp = new Tsp();
        tsp.setPointXY(data.getLocations());
        tsp.setCities(CityInfo.getCitiesFromNames(data.getCities()));
        tsp.countDistancesAndUpdateMatrix();
        tsp.setTspActions(TspCode.UPDATE);
        tsp.setHeader(title);
        tspLiveData.setValue(tsp);
        int i = 0;
        for (String name : data.getCities()) {
            Log.d(CodeX.tag, " Received Name " + name + "   x "
                    + data.getLocations().get(i).x + "  y" + data.getLocations().get(i).y);
            i++;
        }
    }

    public void addLocData(TspData data, String title) {
        Tsp tsp = new Tsp();
        tsp.setPointXY(data.getLocations());
        tsp.setCities(CityInfo.getCitiesFromNames(data.getCities()));
        tsp.countDistancesAndUpdateMatrix();
        tsp.setHeader(title);
        tsp.setTspActions(TspCode.UPDATE);
        tspLiveData.setValue(tsp);
        int i = 0;
        for (String name : data.getCities()) {
            Log.d(CodeX.tag, " Received Name " + name + "   x "
                    + data.getLocations().get(i).x + "  y" + data.getLocations().get(i).y);
            i++;
        }
    }


    public void openFile(InputStream inputStream, TaskManager taskManager, boolean isLocation) {
        taskManager.runTask(() -> {
            stop = false;
            if (inputStream != null) {
                long s = System.currentTimeMillis();
                int counter = 0;
                try {
                    StringBuilder text = new StringBuilder();
                    int cn = inputStream.read();
                    while (cn != -1 && !stop && counter < 2) {
                        char c = (char) cn;
                        text.append(c);
                        cn = inputStream.read();
                        if (System.currentTimeMillis() - s > 5000) {  // updating at 10 seconds
                            s = System.currentTimeMillis();
                            stop = true;
                            counter++;
                        }
                    }
                    inputStream.close();

                    if (stop) {
                        updateCodeLiveData.setValue(UpdateCode.FILE_TOO_LARGE);
                    } else if (counter == 2) {
                        updateCodeLiveData.setValue(UpdateCode.FILE_TOO_LARGE);

                    } else {
                        if (isLocation) readTSP_XY(text.toString());
                        else readTSP_D(text.toString());
                    }

                } catch (Exception e) {
                    updateCodeLiveData.setValue(UpdateCode.FIlE_NOT_OPEN);
                    e.printStackTrace();
                }
            }

        });
    }


    public void saveFile(OutputStream out, TaskManager taskManager, String text) {
        taskManager.runTask(() -> {
            if(text!=null && !text.isEmpty()) {
                if (out != null) {
                    try {
                        out.write(text.getBytes());
                        out.flush();
                        out.close();
                        updateCodeLiveData.postValue(UpdateCode.FILE_SAVE);
                    } catch (Exception e) {
                        Log.e(CodeX.tag, "save error " + e.getLocalizedMessage());
                        e.printStackTrace();
                        updateCodeLiveData.postValue(UpdateCode.FIlE_NOT_SAVE);
                    }
                }
            }else updateCodeLiveData.postValue(UpdateCode.FIlE_NOT_SAVE);

        });





    }



}
