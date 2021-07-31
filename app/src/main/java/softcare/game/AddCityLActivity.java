package softcare.game;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import softcare.gui.CityXY;
import softcare.gui.PointXY;
import softcare.gui.ViewCityDistance;
import softcare.util.S;

public class AddCityLActivity extends AppCompatActivity {


    private CityXY control;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_city_dactivity);
          addCityXY();

    }






    protected void addCityXY() {
        control = findViewById(R.id.control);
        //control.


    }
    
}