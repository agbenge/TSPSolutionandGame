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

public class SolutionModel extends ViewModel {

    private MutableLiveData<Tsp> tspLiveData;
    private MutableLiveData<ProgressX> progressXLiveData;
    private MutableLiveData<ErrorCode> errorCodeLiveData;

    public MutableLiveData<Tsp> getTspLiveData() {
        if (tspLiveData == null) tspLiveData = new MutableLiveData();
        return tspLiveData;
    }

    public MutableLiveData<ErrorCode> getErrorCodeLiveData() {
        if (errorCodeLiveData == null) errorCodeLiveData = new MutableLiveData();
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

    protected boolean readTSP_XY(String tspString) {
        ProgressX progressX = new ProgressX("Reading TSP X, Y");
        Tsp tsp = new Tsp();
        if (tspString != null) {
            String lines[] = tspString.split("\n");
            int pp = 1;
            for (String li : lines) {
                tsp.addHeader(li + "\n");
                if (li.trim().contains("NODE_COORD_SECTION")) {
                    break;
                }

                pp++;
            }

            for (int i = pp; i < lines.length - 1; i++) {
                System.out.println(" Result " + lines[i]);
                String[] name_X_Y = lines[i].trim().split("\\s+");
                System.out.println(" Debug size " + name_X_Y.length);
                if (name_X_Y.length == 3) {
                    tsp.addCity(name_X_Y[0]);
                    try {
                        double x = Double.parseDouble(name_X_Y[1]);
                        double y = Double.parseDouble(name_X_Y[2]);
                        tsp.addPointXY(new PointXY(x, y));
                    } catch (Exception e) {
                        return false;
                    }
                } else {
                    for (String s : name_X_Y) {
                        System.out.println(" ERR Debug data " + s);
                    }

                    return false;
                }
                //ps.setProgress(0.5 + ((i / lines.length) / 2), " setting " + i + " of " + lines.length);

                progressX.setProgress(i);
                progressX.setSize(lines.length);
                progressXLiveData.postValue(progressX);
                if (stop) {
                    return false;
                }
            }
            System.out.println(" read ts  " + (lines.length - 7));
            tsp.setTspActions(TspCode.READ);
            tspLiveData.postValue(tsp);
            return true;

        }
        return false;
    }

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


    public boolean readTSP(String data) {
        System.out.println(data);
        ProgressX progressX = new ProgressX("Reading Tsp x, y ");
        String resH = "";
        Tsp tsp = new Tsp();
        String seperator = "\\s+";
        String lines[] = data.split("\n");
        boolean res = true;
        String name = "TSP" + (lines.length - 1);
        String comment = "Faith TSP solution ";
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
        if (lines != null) {
            int x = -1, y = -1;
            int k = 0;
            progressX.setSize(lines.length);
            for (String line : lines) {
                String columns[] = line.trim().split(seperator);
                progressX.setProgressPercent((((double) x / (double) lines.length) / (double) 2));
                progressX.setProgress(k);


                System.out.println(y + " PROCESSING LINE " + line);
                if (y == -1) {
                    y++;
                    continue;
                }
                x = -1;
                if (columns != null & columns.length == lines.length) {
                    for (String column : columns) {
                        if (x < y) {
                            if (x != -1) {
                                mMatrix[x][y] = Double.parseDouble(column.trim().replace(" ", ""));
                                mMatrix[y][x] = mMatrix[x][y];
                                System.out.print("  |" + column + "|  ==> " + mMatrix[x][y]
                                        + " position " + x + " , " + y);
                                x++;
                            } else {
                                tsp.addCity(column);
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


        }
        tsp.setMatrix(mMatrix);
        if (res) {
            tsp.setTspActions(TspCode.READ);

            tspLiveData.postValue(tsp);
        } else
            errorCodeLiveData.postValue(ErrorCode.DATA_FORMAT);

        return res;

    }


    /// preview full Result,
    public String getPreviewXY() {
        Tsp tsp = tspLiveData.getValue();
        String res = " ";
        if (TspCode.READ != tsp.getTspActions() && TspCode.UPDATE != tsp.getTspActions()) {
            errorCodeLiveData.postValue(ErrorCode.NOT_READ_OR_UPDATED);
            return "";
        }

        res = tsp.getHeader();
        for (int i = 0; i < tsp.getCities().size(); i++) {
            res = res + tsp.getCities().get(i).replace(" ", "_") +
                    "    " + tsp.getPointXY().get(i).getX() + " "
                    + tsp.getPointXY().get(i).getY();
            res += "\n";
        }

        return res + "EOF";
    }


    public String getPreview() {

        Tsp tsp = tspLiveData.getValue();
        if (TspCode.READ != tsp.getTspActions() && TspCode.UPDATE != tsp.getTspActions()) {
            errorCodeLiveData.postValue(ErrorCode.NOT_READ_OR_UPDATED);
            return "";
        }
        String res = "";
        for (String c : tsp.getCities()) {
            res = res + "\t" + c;
        }
        res += "\n";
        for (int i = 0; i < tsp.getCities().size(); i++) {
            res = res + tsp.getCities().get(i);
            for (int j = 0; j < tsp.getCities().size(); j++) {
                res = res + "\t" + S.doubleToString(tsp.getMatrix()[i][j]);
            }

            res += "\n";

        }

        return res;

    }

    public String getSave() {
        String res = "";
        Tsp tsp = tspLiveData.getValue();
        if (TspCode.READ != tsp.getTspActions() && TspCode.UPDATE != tsp.getTspActions()) {
            errorCodeLiveData.postValue(ErrorCode.NOT_READ_OR_UPDATED);
            return "";
        }
        for (int i = 0; i < tsp.getCities().size(); i++) {
            res += tsp.getCities().get(i) + ",";
            res += "\n";
        }
        for (int i = 0; i < tsp.getCities().size(); i++) {
            res += tsp.getCities().get(i) + ",";
            for (int j = 0; j < tsp.getCities().size(); j++) {
                res += tsp.getMatrix()[i][j] + ",";
            }
            res += "\n";
        }

        return res;

    }

    public String getSave_temp() {
        String res = "";
        Tsp tsp = tspLiveData.getValue();
        if (TspCode.READ != tsp.getTspActions() && TspCode.UPDATE != tsp.getTspActions()) {
            errorCodeLiveData.postValue(ErrorCode.NOT_READ_OR_UPDATED);
            return "";
        }
        for (int i = 0; i < tsp.getCities().size(); i++) {
            res += tsp.getCities().get(i) + ",";
            for (int j = 0; j < i; j++) {
                res += tsp.getMatrix()[i][j] + ",";
            }
            res += "\n";
        }

        return res;

    }

    private String updatePreview() {
        String res = "";
        Tsp tsp = tspLiveData.getValue();
        if (TspCode.READ != tsp.getTspActions() && TspCode.UPDATE != tsp.getTspActions()) {
            errorCodeLiveData.postValue(ErrorCode.NOT_READ_OR_UPDATED);
            return "";
        }
        for (String c : tsp.getCities()) {
            res = res + "\t" + c;
        }
        res += "\n";
        for (int i = 0; i < tsp.getCities().size(); i++) {
            for (int j = 0; j < tsp.getCities().size(); j++) {
                res = res + "\t" + tsp.getMatrix()[i][j];
            }
            res += "\n";
        }

        System.out.println(res);
        return res;

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

    public void addCity(String name, double[] distance) {
        Log.d(CodeX.tag," D adding "+name );
        Tsp tsp = tspLiveData.getValue();
        if (tsp != null) {
            if(distance!=null)
                 updateTsp(tsp,name,distance);
        }else {
            if(distance==null){
                tsp= new Tsp();
                updateTsp(tsp,name,distance);
                return;
            }
            Log.e(CodeX.tag,"Tsp value is null");
        }
    }

    private void updateTsp(Tsp tsp, String name,double[] distance) {
        tsp.addCity(name);
        if (tsp.updateMatrix(tsp.getCities().size()-1, distance)) {
            tsp.setLocation(tsp.getCities().size()-1);
            tsp.setTspActions(TspCode.UPDATE);
            tspLiveData.setValue(tsp);
        }  else {
            Log.e(CodeX.tag,"data unable to update");
            errorCodeLiveData.postValue(ErrorCode.UNKNOWN);
        }
    }

    public  void addCityXY(String name, PointXY _pointXY){
        Log.d(CodeX.tag,"L adding "+name);
        Tsp tsp = tspLiveData.getValue();
        if(tsp==null)  tsp= new Tsp();
            tsp.addPointXY(_pointXY);
            tsp.addCity(name);
            tsp.countDistancesAndUpdateMatrix();

    }























    /*private void open(File file )   {
        if (file.isFile()) {
            Runnable task = () -> {
                try {
                    String tspString = readFile(file);
                    if (ps != null)
                        ps.setProgress(0.5, "Setting result ");
                    if (readTSP(tspString)) {
                        setLocation();
                        String pv = getPreview();
                        String pv2 = getPreviewXY();
                        Platform.runLater(() ->
                                startInput.setText(pv));
                        startInputXY.setText(pv2);
                    } else {
                        System.out.print(mCities.size());
                        Platform.runLater(
                                () -> util.S.notice(stage, "Error", "File type or struct not match",
                                        "use the interface to add cities or read file format"));
                        clear();
                    }
                    ps.prepareStop();
                    ;

                    try {
                        Thread.sleep(waitTime);

                    } catch (InterruptedException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }

                    ps.stop();
                    ;
                } catch (IOException e) {
                    e.printStackTrace();
                    Platform.runLater(() -> util.S.notice(stage, "Error", "IOException", e.getMessage()));

                }
            };
            // Run the task in a background thread
            backgroundThread = new Thread(task);
            ps = new util.ProgressPopUp(backgroundThread);
            ps.notice(stage, "Loading TSP");
            // Terminate the running thread if the application exits
            backgroundThread.setDaemon(true);
            backgroundThread.start();

        }

    }



      public String readFile(File file) throws IOException {
        FileInputStream in = new FileInputStream(file);
        String res = "";
        BufferedReader buff = new BufferedReader(new FileReader(file));
        String readLine = "";
        if (ps != null)
            ps.setProgress(0, "Reading file");
        int l = 0;
        while ((readLine = buff.readLine()) != null) {
            res += readLine + "\n";
            l++;
            if (ps != null)
                ps.setProgress(l, "Reading file...  " + l + " lines");
        }

        return res;
    }

     */
}
