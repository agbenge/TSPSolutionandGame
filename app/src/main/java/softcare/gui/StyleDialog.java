package softcare.gui;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.util.DisplayMetrics;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;

import androidx.annotation.NonNull;

public class StyleDialog extends Dialog {

    public StyleDialog(@NonNull Context context) {
        super(context);
        Window window = getWindow();
        if (window != null) {
            window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            // Optional: Add animation if you want
            // window.getAttributes().windowAnimations = R.style.DialogAnimation;
        }
    }

    @Override
    public void show() {
        super.show();

        Window window = getWindow();
        if (window != null) {
            DisplayMetrics metrics = getContext().getResources().getDisplayMetrics();

            int screenWidth = metrics.widthPixels;
            float density = metrics.density;

            // Use 90% of the screen width
            int desiredWidth = (int) (screenWidth * 0.9);

            // But don’t exceed 800dp (for landscape)
            int maxWidthPx = (int) (800 * density + 0.5f);

            int finalWidth = Math.min(desiredWidth, maxWidthPx);

            window.setLayout(finalWidth, ViewGroup.LayoutParams.WRAP_CONTENT);
            window.setDimAmount(0.6f); // background dim level
            window.addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        }
    }

    @Override
    public void setContentView(int layoutResID) {
        super.setContentView(layoutResID);
    }
}
