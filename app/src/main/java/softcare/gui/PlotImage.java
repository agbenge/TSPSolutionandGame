package softcare.gui;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

import softcare.game.R;
import softcare.util.Util;

public class PlotImage  extends androidx.appcompat.widget.AppCompatImageView {


    private Paint highlightPaint;
    private int highlightColor;
    public PlotImage(@NonNull   Context context) {
        super(context);
    }

    public PlotImage(@NonNull   Context context, @Nullable   AttributeSet attrs) {
        super(context, attrs);
        _init(context,attrs);
    }

    public PlotImage(@NonNull Context context, @Nullable  AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        _init(context, attrs);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        highlightPaint.setStyle(Paint.Style.STROKE);
        highlightPaint.setAntiAlias(true);
        highlightPaint.setColor(highlightColor);
        highlightPaint.setStrokeWidth(3f);
        highlightPaint.setTextSize(20f);
        int i=0;
            if (locations != null)
                for (PointXY p : locations) {

                    float circleRadius=30;
                    canvas.drawCircle((float)p.x,(float)p.y, circleRadius, highlightPaint);
                    canvas.drawText( names.get(i),(float)p.x,(float)p.y,   highlightPaint);
                    i++;
                }
        }



    private boolean  isAddingPoint;
    List<String> names= new ArrayList<>();
    List<PointXY> locations= new ArrayList<>();

    public boolean isAddingPoint() {
        return isAddingPoint;
    }

    public List<String> getNames() {
        return names;
    }

    @Override
    public void setImageDrawable(@Nullable  Drawable drawable) {
        super.setImageDrawable(drawable);
         names= new ArrayList<>();
        locations= new ArrayList<>();
    }

    public List<PointXY> getLocations() {
        return locations;
    }

    public void setAddingPoint(boolean addingPoint) {
        isAddingPoint = addingPoint;
    }

    private void _init(Context context, AttributeSet attrs) {
        highlightPaint = new Paint();
        TypedArray a = context.getTheme().obtainStyledAttributes(attrs,
                R.styleable.PlotTSP, 0, 0);

        try {    highlightColor = a.getInteger(R.styleable.PlotGame_highlightColor, Color.MAGENTA);

        } finally {
            a.recycle();
        }
        setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (isAddingPoint) {
                    locations.add(new PointXY(event.getX(),event.getY()));
                    names.add(Util.getLocationEmoji(names.size()));
            refresh();
                }
                return false;
            }
        });
    }

    public void undo() {
        if(!locations.isEmpty()){
            names.remove(names.size()-1);
            locations.remove(locations.size()-1);
            refresh();
        }
    }

    private void refresh() {
        invalidate();
        requestLayout();
    }
}
