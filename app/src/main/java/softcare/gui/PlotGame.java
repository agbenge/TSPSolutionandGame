package softcare.gui;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.os.Build;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;

import java.util.Arrays;
import java.util.List;

import softcare.game.R;

public class PlotGame extends PlotTSP{


    public PlotGame(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);

    }

    public PlotGame(Context context, @Nullable  AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public PlotGame(Context context, @Nullable  AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
  }
    public PlotGame(Context context) {
        super(context);
    }
    @Override
    protected void onDraw(Canvas canvas) {
        super.setColorsAndSizes();
         drawWithZoom(canvas);
 }
     @Override protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

    }


    @Override
    protected void drawWithZoom(Canvas canvas) {
        if(zoom<1)zoom=1;
        float x = 0;
        float y =  0;
      int   i=0;
        for (PointXY p: pointXY ) {
            x = (float) (((p.x + invertNegavative  + 1) * zoom) -1 * zoom) + padding +40;
            y = (float) ((p.y + invertNegavative  + 1) * zoom - 1* zoom) + padding +40;
            canvas.drawCircle(x, y, circleRadius, circlePaint);
            canvas.drawText(cities.get(i), x, y, labelPaint);
            i++;
        }
        drawPath(canvas);

    }

    protected void drawPath(Canvas canvas) {
        if(path==null) return;
        if(zoom<1)zoom=1;
        Path gc = new Path();
        gc.reset();
        float x = (float) (((pointXY.get(0).x + invertNegavative +1) * zoom) - 1 * zoom) + padding +40;
        float y = (float) (((pointXY.get(0).y + invertNegavative  + 1) * zoom) - 1* zoom) + padding +40;
        gc.moveTo(x, y);
        float i_x=x,i_y=y;
        for (Integer i : path) {
            x = (float) (((pointXY.get(i).x + invertNegavative  + 1) * zoom) -1 * zoom) + padding +40;
            y = (float) ((pointXY.get(i).y + invertNegavative  + 1) * zoom - 1* zoom) + padding +40;
            gc.lineTo(x, y);
        }
        gc.lineTo(i_x, i_y);
        canvas.drawPath(gc, linePaint);

    }

/// my methods

    public void plot(List<PointXY> pointXY, List<String> cities,
                     List<Integer> path ) {
        super.pointXY = pointXY;
        this.cities = cities;
        this.path = path;
        this.distance = distance;
       measureDim();
        refresh();

    }
    public void redrawPaths(List<PointXY> pointXY) {
        this.pointXY = pointXY;
        refresh();

    }


}
