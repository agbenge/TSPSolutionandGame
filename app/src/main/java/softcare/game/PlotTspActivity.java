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
            ( (TextView)findViewById(R.id.time)).setText(S.timeDisplay2(tspResult.getTime()));
            ( (TextView)findViewById(R.id.result)).setText(tspResult.getResult()  );
            if( tspResult.getImagePath()!=null) {
                 setPic(tspResult.getImagePath());
                Log.d(CodeX.tag ,tspResult.getTime()+" Img Received");
            } else{
                Log.d(CodeX.tag ,tspResult.getTime()+" Img not received");
            }
            Log.d(CodeX.tag ,tspResult.getTime()+"  Data Received Okay "+ tspResult.getCost());
        }else{
            Log.e(CodeX.tag ,"Null Data Received");
        }

    }


    private void setPic(String path) {
        // Get the dimensions of the View
        int targetW = plotTSP.getMeasuredWidth();
        int targetH = plotTSP.getMeasuredWidth();
         if(targetH==0) targetH=1;
        if(targetW==0) targetW=1;
        // Get the dimensions of the bitmap
        BitmapFactory.Options bmOptions = new BitmapFactory.Options();
        bmOptions.inJustDecodeBounds = true;

        BitmapFactory.decodeFile(path, bmOptions);

        int photoW = bmOptions.outWidth;
        int photoH = bmOptions.outHeight;

        // Determine how much to scale down the image
        int scaleFactor = Math.max(1, Math.min(photoW/targetW, photoH/targetH));

        // Decode the image file into a Bitmap sized to fill the View
        bmOptions.inJustDecodeBounds = false;
        bmOptions.inSampleSize = scaleFactor;
        bmOptions.inPurgeable = true;

        Bitmap bitmap = BitmapFactory.decodeFile(path, bmOptions);
        plotTSP.setImageBitmap(bitmap);
    }
    public void zoomIn(View v) {
        plotTSP.zoomIn();
    }

    public void zoomOut(View v) {
        plotTSP.zoomOut();
    }

}