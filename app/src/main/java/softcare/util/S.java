package softcare.util;

import java.text.DecimalFormat;
import java.util.Formatter;
import java.util.List;
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
	 

			

}
