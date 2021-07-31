package softcare.gui;
import android.content.Context;
import android.util.AttributeSet;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.TextView;
import softcare.game.R;

public class ViewCityDistance extends FrameLayout {
	private int index;
	private boolean ok=false;
	private double input;
	public boolean isOkay() {
		return this.ok;
	}

		public ViewCityDistance(String name, int index, Context context) {
			super(context);
			initView(name,index,context);
		}

		private void initView(String name, int index,Context context) {
			inflate(getContext(), R.layout.item_city_distance, this);
			TextView aNum = this.findViewById(R.id.error);
			aNum.setText("empty number");
			aNum.setTextColor(context.getResources().getColor(R.color.red));
			EditText numS = this.findViewById(R.id.city_edit);
			this.index=index;
			TextView  cityLable =this.findViewById(R.id.city_l);
			cityLable.setText("Distance to "+name+" is ");

			numS.setOnKeyListener((v, keyCode, event) ->   {

					try {
						if(numS.getText().toString().isEmpty()) {
							aNum.setTextColor(context.getResources().getColor(R.color.red));
							aNum.setText("empty number");
							ok=false;
						}else {
							input=	Double.parseDouble(numS.getText().toString());
							aNum.setTextColor(context.getResources().getColor(R.color.green));
							aNum.setText("a number");

						}
					}catch(Exception e) {
						aNum.setTextColor(context.getResources().getColor(R.color.red));
						aNum.setText("not a number");
						input=-1;
						ok= false;
					}
 return  false;
				});
		}
	public double getDistance() {
		return input;  
		
	}
public int getIndex() {
		return this.index;
	}
}
