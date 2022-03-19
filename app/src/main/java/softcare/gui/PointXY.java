package softcare.gui;

import android.os.Parcel;
import android.os.Parcelable;

public class PointXY implements Parcelable {
	public double x;
	public double y;
	public PointXY(double x, double y) {
		super();
		this.x = x;
		this.y = y;
	}

	protected PointXY(Parcel in) {
		x = in.readDouble();
		y = in.readDouble();
	}

	public static final Creator<PointXY> CREATOR = new Creator<PointXY>() {
		@Override
		public PointXY createFromParcel(Parcel in) {
			return new PointXY(in);
		}

		@Override
		public PointXY[] newArray(int size) {
			return new PointXY[size];
		}
	};

	public double getX() {
		return x;
	}
	public void setX(double x) {
		this.x = x;
	}
	public double getY() {
		return y;
	}
	public void setY(double y) {
		this.y = y;
	}
	public String getStore() {
		return  x+"<"+y;
	}

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeDouble(x);
		dest.writeDouble(y);
	}

	@Override
	public String toString() {
		return "PointXY{" +
				"x=" + x +
				", y=" + y +
				'}';
	}
}