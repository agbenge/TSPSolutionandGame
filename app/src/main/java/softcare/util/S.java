package softcare.util;

import java.io.File;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Formatter;
import java.util.List;
import java.util.Random;
import java.util.concurrent.TimeUnit;

import softcare.gui.PointXY;

public class S {
	public  static  String TAG="game";
public 	static  void print (List<String> sub) {
		  System.out.println();
		  System.out.println("start");
		for(String s :sub) {
			System.out.println(s);
		}
		 System.out.println("end");
		  System.out.println( );
	}
	  
	  public static double  formDouble(double d) {
 return new Double(new DecimalFormat("##.00").format(d)).doubleValue();
	  }

	public static String doubleToString(double d) {
        Formatter f = new Formatter();
        String s =String.valueOf(f.format("%.2f", d));
        f.close();
        return s;
	}


	public static String timeDisplay(long time ) {
	long m= TimeUnit.MILLISECONDS.toMinutes(time);
	long s=TimeUnit.MILLISECONDS.toSeconds(time)-
			TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(time));
		Formatter f = new Formatter();
		return String.valueOf(f.format("%02d:%02d", m,s));
	}
	public static String timeDisplay2(long time ) {
		long m= TimeUnit.MILLISECONDS.toMinutes(time);
		long s=TimeUnit.MILLISECONDS.toSeconds(time)-
				TimeUnit.MINUTES.toSeconds(m);
		long ms=time
		      -TimeUnit.MINUTES.toMillis(m)
				- TimeUnit.SECONDS.toMillis(s)
				 ;
		Formatter f = new Formatter();
		return String.valueOf(f.format("%02d:%02d:%03d%s", m,s,ms,"ms"));
	}
	public static int  getRandomInt(Random r, int lower, int upper){
		return   r.nextInt(upper-lower)+lower;
	}
	public static String getName(int i){
		switch (i){
			case 0: return "A";
			case 1: return "B";
			case 2: return "C";
			case 3: return "D";
			case 4: return "E";
			case 5: return "F";
			case 6: return "G";
			case 7: return "H";
			case 8: return "I";
			case 9: return "J";
			case 10: return "K";
			case 11: return "L";
			case 12: return "M";
			case 13: return "N";
			case 14: return "O";
			case 15: return "P";
			case 16: return "Q";
			case 17: return "R";
			case 18: return "S";
			case 19: return "T";
			case 20: return "U";
			case 21: return "V";
			case 22: return "W";
			case 23: return "X";
			case 24: return "Y";
			case 25: return "Z";
		}

		return String.valueOf(i);
	}


	// project folder and sub folders
	public static final String PATH_PROJECT = File.separator + "Path Finder";
	public static final String PATH_IMAGES = PATH_PROJECT + File.separator + "Images";

	public static String DATE_PATTERN = "dd/MM/yyyy HH:mm";

	public  static Date getDateFromString(String s)   {
		try {
			SimpleDateFormat sdf=  new SimpleDateFormat(DATE_PATTERN);
			Date d= sdf.parse(s);
			Long l= d.getTime();
			return d;
		}catch (Exception e){
			e.printStackTrace();
		}
		return  new Date(0) ;
	}



}