package softcare.game;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import softcare.gui.ViewCityDistance;
import softcare.util.S;

public class AddCityDActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_city_dactivity);
        try{
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                addCity( Arrays.stream(getIntent().getStringArrayExtra("cities")).sorted().toArray(String[]::new));
            }else {

                addCity(  getIntent().getStringArrayExtra("cities"));

            }


        }catch (Exception e){

        }

    }





    public void addCity( String [] existingCities) {
        final List<ViewCityDistance> citiesDistance = new ArrayList<>();
        int x = 0;

        LinearLayout root = findViewById(R.id.linear_list);
        for (String cityName : existingCities) {
           ViewCityDistance c= new ViewCityDistance(cityName, x, this);
            citiesDistance.add(c);
            root.addView(c,x);
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
                            startActivity(intent);
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

                        Intent intent = new Intent(this, SolutionActivity.class);
                        intent.putExtra("cityName",newCityName.getText().toString());
                        //intent.putExtra("data", a);
                        intent.setFlags(RESULT_OK);
                        startActivity(intent);
                        finish();
                    }
                } else {

                    Snackbar.make( newCityName,     "City name is empty, Make sure  city name not empty ", Snackbar.LENGTH_LONG).show();

                }
        });

    }

    
    
}