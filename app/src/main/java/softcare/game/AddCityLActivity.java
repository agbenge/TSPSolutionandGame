package softcare.game;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.List;

import softcare.game.model.CodeX;
import softcare.game.model.LevelAdapter;
import softcare.game.model.Location;
import softcare.game.model.LocationAdapter;
import softcare.game.model.TspData;
import softcare.gui.PointXY;
import softcare.gui.StyleDialog;
import softcare.util.S;

public class AddCityLActivity extends AppCompatActivity implements View.OnKeyListener {


    private boolean ok=false;
    private String x;
    private String y;
    private Location locations;
    private LocationAdapter locationAdapter;

    @Override    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_city_lactivity);
        context=this;
        locations = new Location();
        RecyclerView recycler=   findViewById(R.id.locations);

        locationAdapter = new LocationAdapter(this);
        recycler.setAdapter(locationAdapter);
        recycler.setLayoutManager(new LinearLayoutManager(this));

        locationAdapter.setPointClickListener(i -> {

        });
    }










    public boolean isOkay() {
        return this.ok;
    }
    EditText x_input ;
    EditText y_input ;
    EditText city ;
    public TextView msg;
    private Context context;

    public void addCity(StyleDialog dialog) {
        x_input = dialog.findViewById(R.id.city_lat);
        y_input = dialog.findViewById(R.id.city_log);
        city = dialog.findViewById(R.id.city_name);
        msg = dialog.findViewById(R.id.msg);
        x = getString(R.string.latitude);
        y = getString(R.string.longitude);
        x_input.setOnKeyListener(this);
        y_input.setOnKeyListener(this);
        city.setOnKeyListener(this);
        msg.setText(getString(R.string.empty_fields));
        msg.setTextColor(context.getResources().getColor(R.color.red));


    }
    private void callTest( String x, String y ) {
        if(city.getText().toString().isEmpty()) {
            ok=false;
            msg.setText(getString(R.string.name_is_empty));
            return;
        }else {
            ok= true;
            msg.setText( getString(R.string.okay));

            ok=	test(x_input,  x);
            if(ok)ok=	test(y_input, y);
        }
    }

    private boolean test( EditText numS, String msgText){
        Intent i= new Intent();
        double d [][]= null;

        try {
            if(numS.getText().toString().isEmpty()) {
                msg.setTextColor(context.getResources().getColor(R.color.red));
                msg.setText(getString(R.string.empty)+msgText);
                return false;
            }else {
                Double.parseDouble(numS.getText().toString());
                msg.setTextColor(context.getResources().getColor(R.color.green));
                msg.setText(getString(R.string.okay));
                return true;

            }
        }catch(Exception e) {
            msg.setTextColor(context.getResources().getColor(R.color.red));
            msg.setText(getString(R.string.not_a_number));
            return false;
        }
    }


    public double getPointX() {
        if(ok) {
            try {  return	Double.parseDouble(x_input.getText().toString());
            }catch(Exception e) {
                e.printStackTrace(); }
        }

        return -1;
    }
    public double getPointY() {
        if(ok) {
            try {  return	Double.parseDouble(y_input.getText().toString());
            }catch(Exception e) {
                e.printStackTrace(); }
        }

        return -1;
    }
    public String getName() {
        return city.getText().toString();
    }
    public void clear( Context context) {
        city.setText("");
        y_input.setText("");
        x_input.setText("");
        msg.setText(getString(R.string.empty_fields));
        msg.setTextColor(context.getResources().getColor(R.color.red));

    }

    @Override
    public boolean onKey(View v, int keyCode, KeyEvent event) {
        callTest( x,y);
        return false;
    }



    public void finish(View v){
        if(locations.getNames().size()>=3) {
            Intent intent = new Intent(this, SolutionActivity.class);
            TspData data = new TspData(locations.getNames(), locations.getLocations());
            intent.putExtra("data", data);
            intent.setFlags(RESULT_OK);
            setResult(RESULT_OK, intent);
            finish();
        }else{
            Toast.makeText(this, getString(R.string.ad_some_points),Toast.LENGTH_LONG).show();
        }

    }

    public void add(View v){
        StyleDialog dialog= new StyleDialog(this);
dialog.setContentView(R.layout.pop_add_city_l);
dialog.show();
        dialog.setCanceledOnTouchOutside(false);
addCity(dialog);
        dialog.findViewById(R.id.go).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isOkay()){
                    dialog.cancel();
                    locations.addPoint(getName(),getPointX(),getPointY());
                    locationAdapter.changeData(locations);
                }else{
                    Snackbar.make(x_input, "Please file data currently", Snackbar.LENGTH_LONG) .setAction("Action", null).show();

                }
            }
        });

        dialog.findViewById(R.id.cancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    dialog.cancel();
            }
        });
    }

    public void edit(int i){
        StyleDialog dialog= new StyleDialog(this);
        dialog.setContentView(R.layout.pop_add_city_l);
        dialog.show();
        dialog.setCanceledOnTouchOutside(false);
        addCity(dialog);
        dialog.findViewById(R.id.go).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isOkay()){
                    if( locations.editPoint(getName(),getPointX(),getPointY(),i)) {
                        locationAdapter.changeData(locations);
                        dialog.cancel();
                    }

                }else{
                    Snackbar.make(x_input, "Please file data currently", Snackbar.LENGTH_LONG).show();

                }
            }
        });

        dialog.findViewById(R.id.cancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.cancel();
            }
        });
    }
    public void undo(View v){
locations.undo();
    }


}