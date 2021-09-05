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
import android.view.View;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;

import java.util.Arrays;
import java.util.List;

import softcare.game.R;

public class PlotTSP extends View {
    //circle and text colors
    private int circleColor, labelColor ,lineColor,
            boardColor, boardBorderWidth, boardBorderColor,
            textSize, lineWidth, circleBorderWidth;

    int padding = 10, circleRadius = 10;
    private Paint circlePaint;
    private Paint linePaint;
    private Paint labelPaint;
    private int contentWidth = 400, contentHeight = 500;


    public PlotTSP(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);

    }

    public PlotTSP(Context context, @Nullable  AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);

    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public PlotTSP(Context context, @Nullable  AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context, attrs);

    }

    public PlotTSP(Context context) {
        super(context);
    }

    private void init(Context context, @Nullable AttributeSet attrs) {
        circlePaint = new Paint();
        linePaint = new Paint();
        labelPaint = new Paint();
        TypedArray a = context.getTheme().obtainStyledAttributes(attrs,
                R.styleable.PlotTSP, 0, 0);


        int padding = 10, circleRadius = 10;
        try {
            //get the text and colors specified using the names in attrs.xml
            circleColor = a.getInt(R.styleable.PlotTSP_circleColor, Color.BLUE);
            lineColor = a.getInteger(R.styleable.PlotTSP_lineColor, Color.MAGENTA);
            labelColor = a.getInteger(R.styleable.PlotTSP_labelColor, Color.GREEN);//0 is default
            textSize = a.getInteger(R.styleable.PlotTSP_textSize, 40);
            lineWidth = a.getInteger(R.styleable.PlotTSP_lineWidth, 5);
            boardColor = a.getInteger(R.styleable.PlotTSP_boardColor, Color.LTGRAY);
            boardBorderColor = a.getInteger(R.styleable.PlotTSP_lineWidth, Color.BLACK);

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

    private void setColorsAndSizes() {

        circlePaint.setStyle(Paint.Style.FILL);
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


     @Override protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int mode = MeasureSpec.getMode(widthMeasureSpec);
        int size = MeasureSpec.getSize(widthMeasureSpec);
        /*
        MeasureSpec.Exactly : It specifies our view should be of Exact size specified in XML like layout_width = "100dp"
MeasureSpec.AT_MOST : It specifies our view can be at max as possible while using wrap_content
MeasureSpec.UNSPECIFIED : It can take as much space the view wants
         */
        //  size = Math.min(measuredWidth, measuredHeight);
        // setMeasuredDimension(newsize, newsize);


        int textPadding = 10;
        // Resolve the width based on our minimum and the measure spec

        int minw = contentWidth + getPaddingLeft() + getPaddingRight();
        int w = resolveSizeAndState(minw, widthMeasureSpec, 0);

        // Ask for a height that would let the view get as big as it can

        int minh = contentHeight + getPaddingBottom() + getPaddingTop();
        //   if (displayShapeName)    minh += textYOffset + textPadding;

        int h = resolveSizeAndState(minh, heightMeasureSpec, MeasureSpec.UNSPECIFIED );

        // Calling this method determines the measured width and height

        // Retrieve with getMeasuredWidth or getMeasuredHeight methods later

        setMeasuredDimension(w,  h);
    }

    protected Path getTrianglePath() {
        int shapeHeight = 20, shapeWidth = 60;
        Path trianglePath = new Path();
        trianglePath.moveTo(0, shapeHeight);
        trianglePath.lineTo(shapeWidth, shapeHeight);
        trianglePath.lineTo(shapeWidth / 2, 0);
        return trianglePath;
    }

    protected void drawWithZoom(Canvas canvas) {
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

            canvas.drawCircle(x, y, circleRadius, circlePaint);
            canvas.drawText(cities.get(i), x, y, labelPaint);
        }
        gc.lineTo(i_x, i_y);
       canvas.drawPath(gc, linePaint);
        canvas.drawText(String.valueOf(padding), 111,111, labelPaint);


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

    private List<PointXY> pointXY;
    private List<String> cities;
    private List<Integer> path;
    double[] distance;
    private double zoom = 1;
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
private double zoomInto=-1;
    private void measureDim() {
        int heightestValue = 0;
        for (PointXY p : pointXY) {
            if (p.x < invertNegavative) {
                invertNegavative = p.x;
            }
            if (p.y < invertNegavative) {
                invertNegavative = p.y;
            }
            if (p.y > heightestValue)
                heightestValue = (int)p.y+1;
            if (p.x > heightestValue)
                heightestValue = (int)p.x+1;

        }
        zoomInto=invertNegavative;
        if (invertNegavative < 0)
            invertNegavative = invertNegavative * -1;
        else invertNegavative=0;
        contentHeight= (int) (heightestValue+invertNegavative);
        contentWidth= (int) (heightestValue+invertNegavative);
    calculateZoom(contentHeight,contentWidth);
    }


   private void zoom() {
        System.out.println("INVERT NEGATIVE  " + invertNegavative);
/*        canvas.clearRect(0, 0, w,h); // First clear the canvas
        // Set drawing parameters
        gc.setStroke(Color.RED);
        gc.setFill(Color.BLUE);
        gc.setLineWidth(1);
        */


    }

   double zoomIntoValue=0;
    public void zoomOut() {
        zoom = zoom + 4;
        refresh();
    }

    public void zoomIn() {
        if (zoom > 2) {
            zoom = zoom - 2;
        } else if(zoomInto>0){
            zoomIntoValue= zoomIntoValue -3;
            zoomInto=zoomInto-3;
        }
        refresh();
    }
int defaultZoom=0;
    private   void calculateZoom(int contentHeight, int contentWidth) {
        float max= contentHeight>contentWidth? contentHeight:contentWidth;
    }
    private void initValues() {
                this.pointXY = Arrays.asList(
                new PointXY(0, 0),
                new PointXY(0, 3),
                new PointXY(4, 3),
                new PointXY(4, 0));
        this.cities = Arrays.asList(
                "A",
                "B",
                "C",
                "D");
        this.path = Arrays.asList(
                0,
                1,
                2,
                3);
        this.distance = new double[]{3, 4, 3, 4};
        measureDim();
    }
}
