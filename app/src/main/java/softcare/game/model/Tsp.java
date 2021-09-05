package softcare.game.model;

import java.util.ArrayList;
import java.util.List;

import softcare.gui.PointXY;
import softcare.gui.Test;

public class Tsp {
    public Tsp() {
        cities= new ArrayList<>();
        direction=new ArrayList<>();
        header="";
        pointXY=new ArrayList<>();
    }
    private  String result;
   private TspCode tspCode = TspCode.EMPTY;
    private List<String> cities;
    private List<Integer> direction;
    private  String header;
    private List<PointXY> pointXY;
    private int defaultSize = 1024;
    private double matrix[][] = new double[defaultSize][defaultSize];
    private Alg alg =Alg.EMPTY;
  private  long duration =0L;
    private   double []distance;
    private  double cost;


    public void addCity(String _city){
         cities.add(_city);
     }
    public void addDirection(int _direction){
        direction.add(_direction);
    }
    public void addDirection(List<Integer> _directions){
        direction.addAll(_directions);
    }
    public void addPointXY(PointXY _pointXY){
        pointXY.add(_pointXY);
    }
    public void addHeader(String _header){
        header=header+_header;
    }

    public List<String> getCities() {
        return cities;
    }

    public void setCities(List<String> cities) {
        this.cities = cities;
    }

    public List<Integer> getDirection() {
        return direction;
    }

    public void setDirection(List<Integer> direction) {
        this.direction = direction;
    }

    public List<PointXY> getPointXY() {
        return pointXY;
    }

    public void setPointXY(List<PointXY> pointXY) {
        this.pointXY = pointXY;
    }

    public int getDefaultSize() {
        return defaultSize;
    }

    public void setDefaultSize(int defaultSize) {
        this.defaultSize = defaultSize;
    }

    public double[][] getMatrix() {
        return matrix;
    }

    public void setMatrix(double[][] matrix) {
        this.matrix = matrix;
    }

    public String getHeader() {
        return header;
    }

    public void setHeader(String header) {
        this.header = header;
    }

    public TspCode getTspActions() {
        return tspCode;
    }

    public void setTspActions(TspCode tspCode) {
        this.tspCode = tspCode;
    }

    public Alg getAlg() {
        return alg;
    }

    public void setAlg(Alg alg) {
        this.alg = alg;
    }

    public long getDuration() {
        return duration;
    }

    public void setDuration(long duration) {
        this.duration = duration;
    }

    public double[] getDistance() {
        return distance;
    }

    public void setDistance(double[] distance) {
        this.distance = distance;
    }

    public double getCost() {
        return cost;
    }

    public void setCost(double cost) {
        this.cost = cost;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public String getResult() {
        return result;
    }

    public int[][] getDataInt() {
        int[][] res = new int[cities.size()][cities.size()];

        for (int i = 0; i < cities.size(); i++) {
            for (int j = 0; j < cities.size(); j++) {
                res[i][j] = (int) matrix[i][j];
            }
        }

        return res;
    }

    public double[][] getDataDouble() {
        double[][] res = new double[cities.size()][cities.size()];

        for (int i = 0; i < cities.size(); i++) {
            for (int j = 0; j < cities.size(); j++) {
                res[i][j] = matrix[i][j];
            }
        }

        return res;

    }

    public boolean updateMatrix(int x, double data[]) {
        if (x < matrix.length) {
            for (int i = 0; i <= x; i++) {
                if (i == x) {
                    matrix[i][x] = 0;
                } else {
                    matrix[i][x] = data[i];
                    matrix[x][i] = data[i];
                }

            }
            return true;
        }

        return false;

    }


    void countDistancesAndUpdateMatrix() {
        int size = pointXY.size();
        if (size > defaultSize) {
            matrix = new double[size][size];
        }
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                matrix[i][j] = distance(pointXY.get(i), pointXY.get(j));
            }
        }
    }
    void setLocation() {

        if (cities != null) {
            int size = cities.size();
            {
                pointXY = new ArrayList<>();
                pointXY.add(new PointXY(0, 0));
                pointXY.add(new PointXY(0, matrix[0][1]));
                for (int i = 2; i < size; i++) {
                    pointXY.add(Test.getPoint(pointXY.get(0), pointXY.get(1),matrix[0][i], matrix[1][i]));
                }
            }
        }

        if (cities != null) {
            int size = cities.size();

            for (int i = 0; i < size; i++) {
                for (int j = 0; j < size; j++) {
                    matrix[i][j] = distance(pointXY.get(i), pointXY.get(j));
                }
            }
        }

    }

    void setLocation( int in) {
        if(pointXY==null)
            pointXY = new ArrayList<>();
        if (cities != null) {
            int size = cities.size();
            switch(in){
                case 0:{
                    pointXY.add(in,new PointXY(0, 0));
                    return;

                }
                case 1:{
                    pointXY.add(in,new PointXY(0,matrix[0][1]));
                    return;

                }
                default:{
                    pointXY.add(in, Test.getPoint(pointXY.get(0), pointXY.get(1), matrix[0][in], matrix[1][in]));
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


 public  void _addCityXY(String name, PointXY _pointXY){
         pointXY.add(_pointXY);
         cities.add(name);
         countDistancesAndUpdateMatrix();
 }

 public void   _addCity(String name, double[] data){
     cities.add(name);
     updateMatrix(cities.size() - 1, data);
     setLocation(cities.size() - 1);

 }
}
