package softcare.gui;

import android.app.Activity;
import android.app.Dialog;
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
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;

import java.util.Arrays;
import java.util.List;

import softcare.game.R;
import softcare.game.model.CodeX;

public class PlotTSP extends androidx.appcompat.widget.AppCompatImageView {
    //circle and text colors
    private int circleColor, labelColor ,lineColor,
            boardColor, boardBorderWidth, boardBorderColor,
            textSize, lineWidth, circleBorderWidth;

    int padding = 10, circleRadius ;
  protected Paint circlePaint;
    protected Paint linePaint;
    protected Paint labelPaint;
    private int contentWidth , contentHeight ;


    public PlotTSP(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);

    }

    public PlotTSP(Context context, @Nullable  AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);

    }



    public PlotTSP(Context context) {
        super(context);
    }
protected Context context;
    protected void init(Context context, @Nullable AttributeSet attrs) {
        circlePaint = new Paint();
        linePaint = new Paint();
        labelPaint = new Paint();
        this.context=context;
        TypedArray a = context.getTheme().obtainStyledAttributes(attrs,
                R.styleable.PlotTSP, 0, 0);

        try {
            //get the text and colors specified using the names in attrs.xml
            circleColor = a.getInt(R.styleable.PlotTSP_circleColor, Color.BLUE);
            lineColor = a.getInteger(R.styleable.PlotTSP_lineColor, Color.MAGENTA);
            labelColor = a.getInteger(R.styleable.PlotTSP_labelColor, Color.GREEN);//0 is default
            textSize = a.getInteger(R.styleable.PlotTSP_textSize, 40);
            lineWidth = a.getInteger(R.styleable.PlotTSP_lineWidth, 5);
            boardColor = a.getInteger(R.styleable.PlotTSP_boardColor, Color.LTGRAY);
            boardBorderColor = a.getInteger(R.styleable.PlotTSP_lineWidth, Color.BLACK);
            circleRadius=a.getInteger(R.styleable.PlotTSP_circleRadiusTsp, textSize);
            zoom = a.getInteger(R.styleable.PlotTSP_zoomValue, 1);
        } finally {
            a.recycle();
        }

        padding= getPaddingTop();
        if(padding<getPaddingBottom())
            padding=getPaddingBottom();
        if(padding<getPaddingRight())
            padding =getPaddingRight();
        if(padding<getPaddingLeft())
            padding =getPaddingLeft();
        if (isInEditMode()) initValues();
    }


    @Override
    protected void onDraw(Canvas canvas) {
        //draw the View
        setColorsAndSizes();


         drawWithZoom(canvas);


    }

    protected void setColorsAndSizes() {

        circlePaint.setStyle(Paint.Style.STROKE);
        circlePaint.setAntiAlias(true);
        circlePaint.setColor(circleColor);
        labelPaint.setColor(labelColor);
        labelPaint.setTextAlign(Paint.Align.CENTER);
        labelPaint.setTextSize(textSize);
        linePaint.setStyle(Paint.Style.STROKE);
        linePaint.setColor(lineColor);
        linePaint.setStrokeWidth(lineWidth);
    }

    private void drawCircle(Canvas canvas) {
        int viewWidthHalf = this.getMeasuredWidth() / 2;
        int viewHeightHalf = this.getMeasuredHeight() / 2;
        int radius = 0;
        if (viewWidthHalf > viewHeightHalf)
            radius = viewHeightHalf - 10;
        else
            radius = viewWidthHalf - 10;
          canvas.drawCircle(viewWidthHalf, viewHeightHalf, radius, circlePaint);
        /// canvas.drawText("circleText", viewWidthHalf, viewHeightHalf, paint);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        // Calculate the radius from the width and height.
        // in order to calculate positions, dimensions, and any other values related to your view's size, instead of recalculating them every time you draw:

    }

    public void refresh() {
        invalidate();
        requestLayout();
    }

    public int getCircleColor() {
        return circleColor;
    }

    public void setCircleColor(int circleColor) {
        this.circleColor = circleColor;
    }

    public int getLabelColor() {
        return labelColor;
    }

    public void setLabelColor(int labelColor) {
        this.labelColor = labelColor;
    }

 double ty,tx;

     @Override protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        int minw = (int) ((((contentWidth + invertNegavative +1) * zoom) - 1 * zoom)
                + ( padding +40));
        int w = resolveSizeAndState(minw, widthMeasureSpec, MeasureSpec.EXACTLY);
        int minh = (int) ((((contentHeight + invertNegavative +1) * zoom) - 1 * zoom)
                +( padding +40));
        int h = resolveSizeAndState(minh, heightMeasureSpec, MeasureSpec.EXACTLY );
// (((pointXY.get(0).x + invertNegavative +1) * zoom) - 1 * zoom) + padding +40;
        setMeasuredDimension(w,  minh);
        ty=h;
        tx=w;  }


    protected void drawWithZoom(Canvas canvas) {
        if(zoom<=0)zoom=1;
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

            canvas.drawCircle(x, y, textSize , circlePaint);
            canvas.drawText(cities.get(i), x, y+textSize/2, labelPaint);
        }
        gc.lineTo(i_x, i_y);
       canvas.drawPath(gc, linePaint);
       // canvas.drawText("c w "+(int)tx, 100, 100, labelPaint);
       // canvas.drawText("c w "+(int)ty, 100, 200, labelPaint);

    }


    private int currentShapeIndex = 0;

    @Override
    public Parcelable onSaveInstanceState() {
        // Construct bundle

        Bundle bundle = new Bundle();
        // Store base view state

        bundle.putParcelable("instanceState", super.onSaveInstanceState());
        // Save our custom view state to bundle

        bundle.putInt("currentShapeIndex", this.currentShapeIndex);
        // ... store any other custom state here ...

        // Return the bundle

        return bundle;
    }

    @Override
    public void onRestoreInstanceState(Parcelable state) {
        // Checks if the state is the bundle we saved

        if (state instanceof Bundle) {
            Bundle bundle = (Bundle) state;
            // Load back our custom view state

            this.currentShapeIndex = bundle.getInt("currentShapeIndex");
            // ... load any other custom state here ...

            // Load base view state back

            state = bundle.getParcelable("instanceState");
        }
        // Pass base view state on to super

        super.onRestoreInstanceState(state);
    }
/// my methods

    protected List<PointXY> pointXY;
    protected List<String> cities;
    protected List<Integer> path;
   protected double[] distance;
    protected double zoom = 1;
    double invertNegavative = 0;

    public void plot(List<PointXY> pointXY, List<String> cities,
                     List<Integer> path, double[] distance) {
        this.pointXY = pointXY;
        this.cities = cities;
        this.path = path;
        this.distance = distance;
       measureDim();
        refresh();

    }

    public void plot(List<PointXY> pointXY, List<String> cities,
                     List<Integer> path) {
        this.pointXY = pointXY;
        this.cities = cities;
        this.path = path;
        measureDim();
        refresh();

    }
private double zoomInto=-1;
    protected void measureDim() {
        for (PointXY p : pointXY) {
            if (p.x < invertNegavative)   invertNegavative = p.x;
            if (p.y < invertNegavative)  invertNegavative = p.y;
            if (p.y > contentHeight)  contentHeight = (int)p.y;
            if (p.x >contentWidth)  contentWidth = (int)p.x;
    }
        contentHeight++;
        contentWidth++;
        zoomInto=invertNegavative;
        if (invertNegavative < 0)   invertNegavative = invertNegavative * -1;
        else invertNegavative=0;

        contentHeight= (int) (contentHeight+invertNegavative);
        contentWidth= (int) (contentWidth+invertNegavative);
    calculateZoom(contentHeight,contentWidth);
    }


   private void zoom() {
        System.out.println("INVERT NEGATIVE  " + invertNegavative);


    }

   double zoomIntoValue=0;
    public void zoomIn() {
        zoom = zoom + 4;
        if(circleRadius<100) {
            circleRadius ++;
            textSize++;
        }
        refresh();
    }

    public void zoomOut() {
        if (zoom > 2) {
            zoom = zoom - 2;
            if(circleRadius>20) {
                circleRadius--;
                textSize--;
            }
        } else if(zoomInto>0){
            zoomIntoValue= zoomIntoValue -3;
            zoomInto=zoomInto-3;        }
        refresh();
    }

    protected    void calculateZoom(int contentHeight, int contentWidth) {

      float max= contentHeight>contentWidth? contentHeight:contentWidth;
       // DisplayMetrics dm= new DisplayMetrics();
        //getWindow().getWindowManager().getDefaultDisplay().getMetrics(dm);
        setZoom((int)(getWidth())/max) ;
       // Log.e(CodeX.tag," m width ="+dm.widthPixels);
           }
    private void initValues() {
                this.pointXY = Arrays.asList(
                        new PointXY(1, 1),
                        new PointXY(4, 4),
                        new PointXY(0, 0),
                new PointXY(7, 5),
                new PointXY(5, 3),
                new PointXY(6, 2) );
        this.cities = Arrays.asList(
                "A",
                "B",
                "C","d","e","f" );
        this.path = Arrays.asList(
                0,
                1,
                2 ,5,4,3);
        this.distance = new double[]{3, 4, 3 };
        measureDim();
        /*

         */
    }

    public double getZoom() {
        return zoom;
    }

    public void setZoom(double zoom) {
        this.zoom = zoom;
    }
}
