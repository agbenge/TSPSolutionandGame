package softcare.util;

import java.io.File;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Formatter;
import java.util.List;
import java.util.Random;
import java.util.concurrent.TimeUnit;

public class Util {
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
	public static String getLocationEmoji(int i) {
		switch (i) {
			case 0: return "📍"; // Pin - perfect starting point
			case 1: return "🌍"; // Earth (Africa/Europe)
			case 2: return "🏙️"; // Cityscape
			case 3: return "🏔️"; // Mountain
			case 4: return "🏝️"; // Island
			case 5: return "🌈"; // Rainbow
			case 6: return "🏰"; // Castle
			case 7: return "🏖️"; // Beach
			case 8: return "🌋"; // Volcano
			case 9: return "✈️"; // Airplane
			case 10: return "🚀"; // Rocket
			case 11: return "🏞️"; // National park
			case 12: return "🌆"; // City at dusk
			case 13: return "🏕️"; // Camping
			case 14: return "🌄"; // Sunrise over mountains
			case 15: return "🏯"; // Japanese castle
			case 16: return "🕌"; // Mosque
			case 17: return "⛩️"; // Shinto shrine
			case 18: return "🛕"; // Hindu temple
			case 19: return "🏛️"; // Classical building
			case 20: return "🏗️"; // Construction site
			case 21: return "🚗"; // Car
			case 22: return "🚢"; // Ship
			case 23: return "🛫"; // Plane departure
			case 24: return "🛬"; // Plane arrival
			case 25: return "🛳️"; // Passenger ship
			case 26: return "🚉"; // Train station
			case 27: return "🛣️"; // Highway
			case 28: return "🌧️"; // Rain
			case 29: return "☀️"; // Sun
			case 30: return "🌨️"; // Snow
			case 31: return "⛈️"; // Thunderstorm
			case 32: return "🌙"; // Moon
			case 33: return "⭐"; // Star
			case 34: return "🏠"; // House
			case 35: return "🏡"; // House with garden
			case 36: return "🏢"; // Office building
			case 37: return "🏫"; // School
			case 38: return "🏥"; // Hospital
			case 39: return "🌅"; // Sunrise
			case 40: return "🌉"; // Bridge at night
			case 41: return "🗺️"; // World map
			case 42: return "🌏"; // Earth (Asia/Australia)
			case 43: return "🌎"; // Earth (Americas)
			case 44: return "🌤️"; // Sun behind cloud
			case 45: return "💡"; // Idea/light (discovery moment)
			case 46: return "🎯"; // Target
			case 47: return "🔭"; // Telescope (exploration)
			case 48: return "🚁"; // Helicopter
			case 49: return "🚜"; // Tractor (fields)
			case 50: return "🧭"; // Compass
			default: return "📌" + i; // fallback
		}
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