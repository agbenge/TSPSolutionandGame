package softcare.game;

import android.content.Context;
import android.widget.EditText;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import softcare.gui.PointXY;

public class SolutionModel {
    /*
    String header="";
    public List<String> mCities;
    public List<Integer> mDirection;
    int defaultSize = 1024;
    private double mMatrix[][] = new double[defaultSize][defaultSize];
    private double mCost;
    private int alg = 2;// genetic first
    private int waitTime=500;
    private String algName;
    private EditText  startInput;
    private EditText startInputXY;
    protected boolean readTSP_XY(String tsp) {
        mCities.clear();
        header="";
        if (tsp != null) {
            String lines[] = tsp.split("\n");

            List<PointXY> p = new ArrayList<>();
            int pp=1;
            for (String li:lines) {
                header+=li+"\n";
                if(li.trim().contains("NODE_COORD_SECTION")) {
                    break;
                }

                pp++;
            }

            for (int i = pp; i < lines.length - 1; i++) {
                System.out.println(" Result "+lines[i]);
                String[] name_X_Y = lines[i].trim().split("\\s+");
                System.out.println(" Debug size "+name_X_Y.length);
                if (name_X_Y.length == 3) {
                    mCities.add(name_X_Y[0]);
                    try {
                        double x = Double.parseDouble(name_X_Y[1]);
                        double y = Double.parseDouble(name_X_Y[2]);
                        p.add(new PointXY(x, y));
                    } catch (Exception e) {
                        return false;
                    }
                } else {
                    for (String s : name_X_Y) {
                        System.out.println(" ERR Debug data " + s);
                    }

                    return false;
                }
                if (ps != null) {
                    ps.pro(0.5 + ((i / lines.length) / 2), " setting " + i + " of " + lines.length);
                    if (!ps.back) {
                        return false;
                    }
                }
            }
            System.out.println(" read ts  " + (lines.length - 7));
            pointXY.addAll(p);
            return true;

        }
        return false;
    }

    protected String getPreviewXY() {
        String res = " ";
        if (pointXY != null) {

            res=header;
            for (int i = 0; i < mCities.size(); i++) {
                res = res + mCities.get(i).replace(" ", "_") +
                        "    " + pointXY.get(i).getX() + " "
                        + pointXY.get(i).getY();
                res += "\n";
            }

        }

        return res + "EOF";
    }

    private Thread backgroundThread;
    ProgressPopUp ps;

    protected String geFulltResult(long d, int alg) {
        double cost=0;
        if (mDirection.size() < 2) {
            return "Action have no defined output yet...";
        }
        String res = "Movement\n";
        res += "\tPath: ";
        double  dist[] = new double[mDirection.size()];
        int prevouse = mDirection.get(0);
        if (mDirection != null)
            for (int x = 1; x < mDirection.size(); x++) {
                int i = mDirection.get(x);
                res += mCities.get(prevouse) + "\t";
                res += "\t" + mMatrix[prevouse][i] + "\tto\t";

                dist[prevouse]=mMatrix[prevouse][i];
                cost =cost+mMatrix[prevouse][i];
                prevouse = i;
            }

        res += mCities.get(prevouse) + "\t";
        if(mCost==0) {
            mCost=cost;
        }
        res += "\n Total distances \t" + mCost;
        if(d>0L) {
            double time = (double)d/(double)1000;
            if(time!=1)
                res += "\n Total time in seconds\t" + time+" seconds";
            else
                res += "\n Total time  in seconds\t" + time+" second";

        }else {
            res += "\n Total time in seconds\t" + " is zero seconds";
        }
        //res += "\n Total time \t" + d;
        res += "\n\n  Path: ";
        prevouse = mDirection.get(0);
        if (mDirection != null)
            for (int x = 0; x < mDirection.size(); x++) {
                int i = mDirection.get(x);
                res += mCities.get(i) + "\t";
            }
        //res += "\n Total time in millsec\t" + d+"  ";
        //res += "\n  Path: " + mDirection.toString();
        //Plot(List<PointXY>pointXY, List<String> cities,	List<Integer> path,
        //<Double[]distance )
        gui.Plot p= new gui.Plot(pointXY,mCities,mDirection,dist);


        switch (alg) {
            case 1: {

                p.plot(null, "Result  Dnynamic  Algorithm ", res);

                break;
            }

            case 2: {
                p.plot(null, "Result Genetic Algorithm  ", res);

                break;
            }

            case 3: {
                p.plot(null, "Result TSP Nearest Neighbour ", res);
                break;
            }}

        System.out.println("COST + "+cost);
        return res;

    }



    protected long startAgl(double[][] data, int alg) {
        long start = System.currentTimeMillis();

        mDirection = new ArrayList<Integer>();
        mCost = 0;
        long duration = 0l;
        long end;
        switch (alg) {
            case 1: {

                if (mCities.size() <= 20) {
                    algorithm.TspDynamicProgrammingIterative dyn = new algorithm.TspDynamicProgrammingIterative(0, data);
                    dyn.solve();
                    System.out.println(dyn.getTour().toString());
                    mDirection = dyn.getTour();
                    System.out.println(dyn.getTourCost());
                    mCost = dyn.getTourCost();
                    end = System.currentTimeMillis();
                    duration = end - start;
                    System.out.println(duration);
                    System.out.println();
                } else {
                    System.out.println("error size is " + mCities.size());
                    util.S.notice(null, "Notice", "Cities are too much for Dynamic algorithm", "Use another algorithm");
                }

                break;
            }

            case 2: {

                int dataInt[][] = new int[mCities.size()][mCities.size()];
                dataInt = getDataInt(data);
                start = System.currentTimeMillis();
                algorithm.Salesmensch geneticAlgorithm = new
                        algorithm.Salesmensch(mCities.size(), algorithm.SelectionType.ROULETTE, dataInt, 0, 0);
                algorithm.SalesmanGenome result =  geneticAlgorithm.optimize();
                end = System.currentTimeMillis();
                System.out.println(result);
                mDirection.add(result.getStartingCity());
                mDirection.addAll(result.getGenome());
                mDirection.add(result.getStartingCity());
                mCost = result.getFitness();
                duration = end - start;
                System.out.println(duration);

                break;
            }

            case 3: {

                int dataInt[][] = new int[mCities.size()][mCities.size()];
                dataInt = getDataInt(data);
                start = System.currentTimeMillis();
                algorithm.TSPNearestNeighbour tsp = new algorithm.TSPNearestNeighbour();
                tsp.tsp(dataInt);

			 //System.out.println(dyn.getTour().toString());
			 //mDirection = dyn.getTour();
			//	System.out.println(dyn.getTourCost());
				//mCost = dyn.getTourCost();
                System.out.println();
                System.out.println("  cosst +>> "+ String.valueOf(tsp.getCost()));
                System.out.println("  direction +>> "+ String.valueOf(tsp.getTour())); end = System.currentTimeMillis();
                System.out.println();
                mDirection = tsp.getTour();
                mCost =tsp.getCost();
                duration = end - start;
                System.out.println(duration);

                break;
            }
        }
        return duration;
    }

    private int[][] getDataInt(double[][] data) {
        int[][] res = new int[mCities.size()][mCities.size()];

        for (int i = 0; i < mCities.size(); i++) {
            for (int j = 0; j < mCities.size(); j++) {
                res[i][j] = (int) data[i][j];
            }
        }

        return res;
    }

    public String readFile(File file) throws IOException {
        FileInputStream in = new FileInputStream(file);
        String res = "";
        BufferedReader buff = new BufferedReader(new FileReader(file));
        String readLine = "";
        if (ps != null)
            ps.pro(0, "Reading file");
        int l = 0;
        while ((readLine = buff.readLine()) != null) {
            res += readLine + "\n";
            l++;
            if (ps != null)
                ps.pro(l, "Reading file...  " + l + " lines");
        }

        return res;
    }

    public boolean readTSP(String data) {
        System.out.println(data);

        String resH ="";

        mCities.clear();
        String seperator = "\\s+";
        String lines[] = data.split("\n");
        boolean res = true;
        clear();
        String name = "TSP" + (lines.length-1);
        String comment = "Faith TSP solution ";
        String type = "TSP";
        String edgeType = "EUC_2D";
        resH = name + "\n";
        resH += "TYPE: " + type + "\n";
        resH += "COMMENT: " + comment + "\n";
        resH += "DIMENSION: " +  (lines.length-1) + "\n";
        resH += "EDGE_WEIGHT_TYPE: " + edgeType + "\n";
        resH += "NODE_COORD_SECTION\n";
        header=resH;


        if (lines != null) {
            int x = -1, y=-1;
            int k = 0;
            for (String line : lines) {
                String columns[] = line.trim().split(seperator);
                if (ps != null) { ps.pro((((double)x / (double)lines.length) /(double) 2), " setting " + k + " of " + lines.length); }
                System.out.println(y+" PROCESSING LINE "+line  );
                if(y==-1) {y++;continue;}
                x=-1;
                if (columns != null&columns.length==lines.length ) {
                    for (String column : columns) {
                        if (x < y) { if (x != -1) {
                            mMatrix[x][y] = Double.parseDouble(column.trim().replace(" ", ""));
                            mMatrix[y][x] =mMatrix[x][y];
                            System.out.print("  |" +column+ "|  ==> "+mMatrix[x][y]
                                    +" position "+x+" , "+y );
                            x++;
                        } else { mCities.add(column); x=0; }
                        }else {
                            break; }
                    }
                } else {
                    System.out.println(" col null or miss match lenght");
                    System.out.println(" col    lenght "+columns.length+
                            "  lines " +lines.length);

                    for (String column : columns) {
                        System.out.println(" col    data "+column );
                    }

                    res = false;
                    break;
                }
                System.out.println(  );
                System.out.print(y+" LinE "  );
                System.out.println(  );
                y++;
            }



        }
        System.out.println(getPreview());

        return res;

    }

    EditText input;

    protected void clear() {
        if (mCities != null)
            mCities.clear();
        if (pointXY != null)
            pointXY.clear();
    }

    public boolean updateMatrix(int x, double data[]) {
        // mMatrix.length
        if (x < mMatrix.length) {
            for (int i = 0; i <= x; i++) {
                if (i == x) {
                    mMatrix[i][x] = 0;
                } else {
                    mMatrix[i][x] = data[i];
                    mMatrix[x][i] = data[i];
                }

            }
            // updatePreview();
            return true;
        }

        return false;

    }

    private void updatePreview() {
        String res = "";
        for (String c : mCities) {
            res = res + "\t" + c;
        }
        res += "\n";
        for (int i = 0; i < mCities.size(); i++) {
            for (int j = 0; j < mCities.size(); j++) {
                res = res + "\t" + mMatrix[i][j];
            }
            res += "\n";
        }

        System.out.println(res);
        // input.setText(res);

    }

    private String getPreview() {
        String res = "";
        for (String c : mCities) {
            res = res + "\t" + c;
        }
        res += "\n";
        for (int i = 0; i < mCities.size(); i++) {
            res = res + mCities.get(i);
            for (int j = 0; j < mCities.size(); j++) {
                res = res + "\t" + S.doubleToString(mMatrix[i][j]);
            }
            if (ps != null)
                ps.pro((double) ((double) i / (double) mCities.size()),
                        "Reading ajacent matrix " + i + " rows of " + mCities.size() + " rows");
            res += "\n";

        }

        return res;

    }

    private double[][] getData() {
        double[][] res = new double[mCities.size()][mCities.size()];

        for (int i = 0; i < mCities.size(); i++) {
            for (int j = 0; j < mCities.size(); j++) {
                res[i][j] = mMatrix[i][j];
            }
        }

        return res;

    }

    private String getSave() {
        String res = "";

        for (int i = 0; i < mCities.size(); i++) {
            res += mCities.get(i) + ",";
            res += "\n";
        }
        for (int i = 0; i < mCities.size(); i++) {
            res += mCities.get(i) + ",";
            for (int j = 0; j < mCities.size(); j++) {
                res += mMatrix[i][j] + ",";
            }
            res += "\n";
        }

        return res;

    }

    private String getSave_temp() {
        String res = "";

        for (int i = 0; i < mCities.size(); i++) {
            res += mCities.get(i) + ",";
            for (int j = 0; j < i; j++) {
                res += mMatrix[i][j] + ",";
            }
            res += "\n";
        }

        return res;

    }



    List<PointXY> pointXY;



    void countDistancesAndUpdateMatrix() {
        int size = pointXY.size();
        if (size > defaultSize) {
            clear();
            mMatrix = new double[size][size];
        }
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                mMatrix[i][j] = distance(pointXY.get(i), pointXY.get(j));
            }
        }
    }

    void setLocation() {

        if (mCities != null) {
            int size = mCities.size();
            {
                pointXY = new ArrayList<>();
                pointXY.add(new PointXY(0, 0));
                pointXY.add(new PointXY(0, mMatrix[0][1]));
                for (int i = 2; i < size; i++) {
                    pointXY.add(Test.getPoint(pointXY.get(0), pointXY.get(1), mMatrix[0][i], mMatrix[1][i]));
                }
            }
        }
		/*
		 *
		if (mCities != null) {
			int size = mCities.size();

			for (int i = 0; i < size; i++) {
				for (int j = 0; j < size; j++) {
					mMatrix[i][j] = distance(pointXY.get(i), pointXY.get(j));
				}
			}
		}

    }

    void setLocation( int in) {
        if(pointXY==null)
            pointXY = new ArrayList<>();
        if (mCities != null) {
            int size = mCities.size();
            switch(in){
                case 0:{
                    pointXY.add(in,new PointXY(0, 0));
                    return;

                }
                case 1:{
                    pointXY.add(in,new PointXY(0, mMatrix[0][1]));
                    return;

                }
                default:{
                    pointXY.add(in, gui.Test.getPoint(pointXY.get(0), pointXY.get(1), mMatrix[0][in], mMatrix[1][in]));
                }
            }
            {




            }
        }

    }
    private double distance(PointXY p1, PointXY p2) {
        return euclidean(p1.x - p2.x, p1.y - p2.y);
    }

    private double euclidean(double dx, double dy) {
        return Math.sqrt(dx * dx + dy * dy);
    }

}
*/
}
