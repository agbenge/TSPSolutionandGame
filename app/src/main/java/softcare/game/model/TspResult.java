package softcare.game.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import softcare.gui.PointXY;

public class TspResult extends TspData   {
   private List<Integer> path;
   private double cost;
   private  long time;
   private String result;
   private String imagePath;

    public TspResult(List<String> cities, List<PointXY> locations, List<Integer> path, double cost, long time, String result, String imagePath) {
        super(cities, locations);
        this.path = path;
        this.cost = cost;
        this.time = time;
        this.result = result;
        this.imagePath = imagePath;
    }

    public List<Integer> getPath() {
        return path;
    }

    public double getCost() {
        return cost;
    }

    public long getTime() {
        return time;
    }

    public String getResult() {
        return result;
    }

    public String getImagePath() {
        return imagePath;
    }



    public TspResult(Parcel in) {
        super(in);
        List<Integer> al= new ArrayList<>();
        in.readList(al,Integer.class.getClassLoader());
       this.path  =al ;
        this.result = in.readString();
        this.imagePath = in.readString();
        this.cost= in.readDouble();
        this.time = in.readLong();
    }
    public static final Creator<TspResult> CREATOR = new Creator<TspResult>() {
        @Override
        public TspResult createFromParcel(Parcel in) {
            return new TspResult(in);
        }

        @Override
        public TspResult[] newArray(int size) {
            return new TspResult[size];
        }
    };

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        super.writeToParcel(dest, flags);
       dest.writeList(path);
        dest.writeString(result);
        dest.writeString(imagePath);
        dest.writeDouble(cost);
        dest.writeLong(time);

    }

    @Override
    public String toString() {
        return "TspResult{" +
                "path=" + path +
                ", cost=" + cost +
                ", time=" + time +
                ", result='" + result + '\'' +
                ", imagePath='" + imagePath + '\'' +
                '}';
    }
}
