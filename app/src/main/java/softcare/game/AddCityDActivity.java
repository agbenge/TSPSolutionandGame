package softcare.game;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.List;

import softcare.game.model.CodeX;
import softcare.gui.ViewCityDistance;
import softcare.util.S;

public class AddCityDActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_city_dactivity);

addCity();

    }


    public void addCity() {
        final List<ViewCityDistance> citiesDistance   = new ArrayList<>();     int x = 0;
        LinearLayout root = findViewById(R.id.linear_list);
        if(getIntent().getStringArrayExtra("Cities")!=null)
        for (String cityName : getIntent().getStringArrayExtra("Cities") ) {
                ViewCityDistance c = new ViewCityDistance(this, x, cityName);
            LinearLayout.LayoutParams layoutParams =
                    new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,  LinearLayout.LayoutParams.WRAP_CONTENT);
                citiesDistance.add(c);
                root.addView(c, x,layoutParams);
                x++;
            }
        Button go = findViewById(R.id.add);
        EditText newCityName = findViewById(R.id.new_city_name);

        go.setOnClickListener(v ->  {

                if (!newCityName.getText().toString().isEmpty()) {
                    boolean isOkay = true;
                    /// updateMatrix();
                 int  i  = 0;
                    if (citiesDistance.size() > 0) {
                        double[] data = new double[citiesDistance.size()];
                        for (ViewCityDistance c : citiesDistance) {
                            if (!c.isOkay()) {
                                isOkay = false;

                            }
                            data[i] = c.getDistance();
                            i++;
                        }
                        Log.d(S.TAG, " sending data size "+ data.length);
                        if (isOkay) {

                            Intent intent = new Intent(this, SolutionActivity.class);
                            intent.putExtra("cityName",newCityName.getText().toString());
                            intent.putExtra("data",data);
                            intent.setFlags(RESULT_OK);
                            Log.d(CodeX.tag,"Sending name and data ");
                            setResult(RESULT_OK,intent);
                            finish();
                           // mCities.add(newCityName.getText());
                           // updateMatrix(mCities.size() - 1, data);
                          //  setLocation(mCities.size() - 1);

                            Snackbar.make( newCityName,   "   Adding city", Snackbar.LENGTH_LONG).show();


                            // send data to main activity

                        } else {
                        Snackbar.make( newCityName,   " A Field empty or not a number Make sure  you all fields are numbers", Snackbar.LENGTH_LONG).show();
                        }
                    } else {
                        //mCities.add(newCityName.getText());
                       // updateMatrix(mCities.size() - 1, null);
                       // setLocation(mCities.size() - 1);
                       // stage.close();
                        Snackbar.make( newCityName,   "   Adding city", Snackbar.LENGTH_LONG).show();
                        Log.d(CodeX.tag,"Sending only name");
                        Intent intent = new Intent(this, SolutionActivity.class);
                        intent.putExtra("cityName",newCityName.getText().toString());
                        //intent.putExtra("data", a);
                        intent.setFlags(RESULT_OK);
                        setResult(RESULT_OK,intent);

                        finish();
                    }
                } else {

                    Snackbar.make( newCityName,     "City name is empty, Make sure  city name not empty ", Snackbar.LENGTH_LONG).show();

                }
        });

    }

    
    
}