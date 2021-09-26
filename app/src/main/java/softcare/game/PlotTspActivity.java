package softcare.game;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.Constraints;

import android.app.Dialog;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import softcare.game.model.CodeX;
import softcare.game.model.TspData;
import softcare.game.model.TspResult;
import softcare.gui.PlotTSP;
import softcare.util.S;

public class PlotTspActivity extends AppCompatActivity {

    private PlotTSP plotTSP;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_plot_tsp);
        plotTSP = findViewById(R.id.plotTSP);
        TspResult tspResult=getIntent().getParcelableExtra("result");
        if(tspResult!=null) {
            plotTSP.plot(tspResult.getLocations(), tspResult.getCities(),  tspResult.getPath());
            ( (TextView)findViewById(R.id.scores)).setText(String.valueOf(S.formDouble(tspResult.getCost())));
            ( (TextView)findViewById(R.id.time)).setText(S.timeDisplay(tspResult.getTime()));
            ( (TextView)findViewById(R.id.result)).setText(tspResult.getResult()  );
            if( tspResult.getImagePath()!=null) {
                Bitmap thumbnail = (BitmapFactory.decodeFile(tspResult.getImagePath()));
                plotTSP.setImageBitmap(thumbnail);
            }
            Log.d(CodeX.tag ,"  Data Received Okay "+ tspResult.getResult());
        }else{
            Log.e(CodeX.tag ,"Null Data Received");
        }

    }

    public void zoomIn(View v) {
        plotTSP.zoomIn();
    }

    public void zoomOut(View v) {
        plotTSP.zoomOut();
    }

}