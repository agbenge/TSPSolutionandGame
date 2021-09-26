package softcare.game.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

import softcare.gui.PointXY;

public class TspData  implements Parcelable {
    List<String> cities;
    List<PointXY> locations;

    public TspData(List<String> cities, List<PointXY> locations) {
        this.cities = cities;
        this.locations = locations;
    }

    protected TspData(Parcel in) {
        cities = in.createStringArrayList();
        locations = in.createTypedArrayList(PointXY.CREATOR);

    }

    public static final Creator<TspData> CREATOR = new Creator<TspData>() {
        @Override
        public TspData createFromParcel(Parcel in) {
            return new TspData(in);
        }

        @Override
        public TspData[] newArray(int size) {
            return new TspData[size];
        }
    };

    public List<String> getCities() {
        return cities;
    }

    public List<PointXY> getLocations() {
        return locations;
    }





    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
      dest.writeStringList(this.cities);
       dest.writeTypedList(this.locations);
    }

}
