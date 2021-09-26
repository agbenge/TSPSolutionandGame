package softcare.gui;

import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;

import softcare.game.R;

public class ViewCityDistance extends FrameLayout {
    private int index;
    private boolean ok = false;
    private double input;

    public boolean isOkay() {
        return this.ok;
    }

    String name = "A";

    public ViewCityDistance(@NonNull Context context, int index, String name) {
        super(context);
        this.index = index;
        this.name = name;
        initView(name, index, context);
    }

    public ViewCityDistance(@NonNull Context context, @Nullable AttributeSet attrs, int index, String name) {
        super(context, attrs);
        this.index = index;
        this.name = name;
        initView(name, index, context);
    }

    public ViewCityDistance(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr, int index, String name) {
        super(context, attrs, defStyleAttr);
        this.index = index;
        this.name = name;
        initView(name, index, context);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public ViewCityDistance(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes, int index, String name) {
        super(context, attrs, defStyleAttr, defStyleRes);
        this.index = index;
        this.name = name;
        initView(name, index, context);
    }


    private void initView(String name, int index, Context context) {
        inflate(context, R.layout.item_city_distance, this );
        TextView aNum = this.findViewById(R.id.error);
        aNum.setText("Empty");
        aNum.setTextColor(context.getResources().getColor(R.color.red));
        EditText numS = this.findViewById(R.id.city_edit);
        this.index = index;
        TextView cityLabel = this.findViewById(R.id.city_l);
        cityLabel.setText("to " + name + " is ");

        numS.setOnKeyListener((v, keyCode, event) -> {

            try {
                if (numS.getText().toString().isEmpty()) {
                    aNum.setTextColor(context.getResources().getColor(R.color.red));
                    aNum.setText("Empty");
                    ok = false;
                } else {
                    input = Double.parseDouble(numS.getText().toString());
                    aNum.setTextColor(context.getResources().getColor(R.color.green));
                    aNum.setText("Okay");
                    ok=true;
                }
            } catch (Exception e) {
                aNum.setTextColor(context.getResources().getColor(R.color.red));
                aNum.setText("Error");
                input = -1;
                ok = false;
            }
            return false;
        });
    }

    public double getDistance() {
        return input;

    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    public int getIndex() {
        return this.index;
    }
}
