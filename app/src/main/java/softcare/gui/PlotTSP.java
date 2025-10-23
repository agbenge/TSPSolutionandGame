package softcare.gui;

import android.app.Activity;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;

import androidx.annotation.Nullable;

import java.util.Arrays;
import java.util.List;

import softcare.game.R;
import softcare.game.model.CodeX;
import softcare.util.Util;

public class PlotTSP extends androidx.appcompat.widget.AppCompatImageView {
    //circle and text colors
    protected int circleColor, labelColor ,lineColor,
            boardColor, boardBorderWidth, boardBorderColor,
            textSize, lineWidth, circleBorderWidth;

    int circleRadius ;
    protected Paint circlePaint;
    protected Paint linePaint;
    protected Paint labelPaint;
    private int contentWidth , contentHeight, spaceX ;


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
            circleBorderWidth = a.getInteger(R.styleable.PlotTSP_circleWidth,5);
        } finally {
            a.recycle();
        }

         spaceX= textSize>circleRadius? textSize:circleRadius;
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
        circlePaint.setStrokeWidth(circleBorderWidth);
        labelPaint.setColor(labelColor);
        labelPaint.setTextAlign(Paint.Align.CENTER);
        labelPaint.setTextSize(textSize);
        linePaint.setStyle(Paint.Style.STROKE);
        linePaint.setColor(lineColor);
        linePaint.setStrokeWidth(lineWidth);
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

   private int totalHeight, totalWidth;

    protected  double getYZoomDouble(double value){

        return  (((value+ invertNegavative +1) * zoom) - 1 * zoom) +spaceX+getPaddingTop();
    }

    protected  double getXZoomDouble(double value){

        return  (((value+ invertNegavative +1) * zoom) - 1 * zoom) +spaceX+getPaddingRight();
    }
    protected  float getYZoom(double value){
        return (float) getYZoomDouble(value);
    }
    protected  float getXZoom(double value){
        return (float) getXZoomDouble(value);
    }
    protected  int getYZoom(int value){
        return (int)  getYZoomDouble(value);
    }
    protected  int getXZoom(int value){
        return (int)  getXZoomDouble(value);
    }
    @Override protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int minW =   getXZoom(contentWidth) +getPaddingRight() ;
        int minh =  getYZoom(contentHeight)+getPaddingBottom()  ;
        //totalWidth = resolveSizeAndState(minW, widthMeasureSpec, MeasureSpec.UNSPECIFIED);
       // totalHeight= resolveSizeAndState(minh, heightMeasureSpec, MeasureSpec.UNSPECIFIED );
        setMeasuredDimension(minW,minh);
    }
    protected void drawWithZoom(Canvas canvas) {
        if(zoom<=0)zoom=initZoom;
        Path gc = new Path();
        gc.reset();
        float x = getXZoom ( pointXY.get(0).x ) ;
        float y = getYZoom(pointXY.get(0).y );
        gc.moveTo(x, y);
        float i_x=x,i_y=y;
        for (Integer i : path) {

            x = getXZoom(pointXY.get(i).x  ) ;
            y = getYZoom(pointXY.get(i).y ) ;
            gc.lineTo(x, y);
           canvas.drawCircle(x, y, circleRadius, circlePaint);
            canvas.drawText(cities.get(i), x, y, labelPaint);
        }
        gc.lineTo(i_x, i_y);
       canvas.drawPath(gc, linePaint);
       canvas.drawRect(getXZoom(0)-spaceX,getYZoom(0)-spaceX,getXZoom(contentWidth),getYZoom(contentHeight),circlePaint);
       //testD(canvas);
    }


    private void testD(Canvas canvas) {
        int minW =  getPaddingLeft()+getXZoom(contentWidth) +getPaddingRight() +textSize;
        int minh = getPaddingTop() +getYZoom(contentHeight)+getPaddingBottom() +textSize;

        canvas.drawText("  cH "+ Util.doubleToString(minh)  , +textSize, 100, labelPaint);
        canvas.drawText("    cW "+ Util.doubleToString(minh)  , +textSize, 200, labelPaint);
        canvas.drawText( "    Ty   "+ Util.doubleToString(totalHeight), +200, 300, labelPaint);
        canvas.drawText( "     Tx "+ Util.doubleToString(totalWidth), +200, 400, labelPaint);
        canvas.drawText( "Text"+initZoom ,  0+textSize, 0+textSize, labelPaint);
          canvas.drawCircle(450,500,20,circlePaint);

        /*
        text are draw from center to all side
         */
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
    protected double zoom = 1;
    double invertNegavative = 0;



    public void plot(List<PointXY> pointXY,
                     List<String> cities,
                     List<Integer> path) {
        this.pointXY = pointXY;
        this.cities = cities;
        this.path = path;
        measureDim();
        refresh();

    }

    protected void measureDim() {
        for (PointXY p : pointXY) {
            if (p.x < invertNegavative)   invertNegavative = p.x;
            if (p.y < invertNegavative)  invertNegavative = p.y;
            if (p.y > contentHeight)  contentHeight = (int)p.y;
            if (p.x >contentWidth)  contentWidth = (int)p.x;
        }
        contentHeight++;
        contentWidth++;
        double zoomInto = invertNegavative;
        if (invertNegavative < 0)   invertNegavative = invertNegavative * -1;
        else invertNegavative=0;

        contentHeight= (int) (contentHeight+invertNegavative);
        contentWidth= (int) (contentWidth+invertNegavative);
        calculateZoom(contentHeight,contentWidth);
    }



    double zoomIntoValue=0;
    public void zoomIn() {
        zoom    += zoomingBy;
        // circleRadius +=zoomingBy;
        // textSize+=zoomingBy;
        //Log.e(CodeX.tag," zoom in 1 "+zoom);
        refresh();
        Log.e(CodeX.tag," zoom in 2 "+zoom);

    }

    public void zoomOut() {
        zoom -= zoomingBy;
        // circleRadius-=zoomingBy;
        //  textSize-=zoomingBy;
        Log.e(CodeX.tag," zoom out 1 "+zoom);
        refresh();
        // Log.e(CodeX.tag," zoom out 2 "+zoom);

    }
    private  double zoomingBy=1;
    protected double initZoom=100;
    protected    void calculateZoom(int contentHeight, int contentWidth) {
        if(!isInEditMode())  try {
            DisplayMetrics dm = new DisplayMetrics();
            ((Activity) context).getWindowManager().getDefaultDisplay().getMetrics(dm);

            float max = contentHeight > contentWidth ? contentHeight : contentWidth;
            float minScreen = dm.heightPixels < dm.widthPixels ? dm.heightPixels : dm.widthPixels;

            initZoom=  (minScreen -100 ) / max;
            zoomingBy= initZoom/5;
            setZoom( initZoom);          }catch ( Exception e){
            Log.e(CodeX.tag," PLotTsp class should only be use in activity");
        }
    }
    private void initValues() {
        this.pointXY = Arrays.asList(
                new PointXY(1, 1),
                new PointXY(4, 4),
                new PointXY(0, 0),
                new PointXY(7, 5),
                new PointXY(5, 3),
                new PointXY(7, 7) );
        this.cities = Arrays.asList(
                "A",
                "B",
                "C","D","E","F" );
        this.path = Arrays.asList(
                2,
                0,
                1 ,5,3,4,2);
        measureDim();
    }

    public double getZoom() {
        return zoom;
    }

    public void setZoom(double zoom) {
        this.zoom = zoom;
    }
}
