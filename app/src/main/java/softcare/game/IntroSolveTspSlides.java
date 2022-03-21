package softcare.game;


import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.Toast;

import com.github.appintro.AppIntro2;
import com.github.appintro.AppIntroCustomLayoutFragment;
import com.github.appintro.AppIntroFragment;
import com.github.appintro.AppIntroPageTransformerType;
import com.heinrichreimersoftware.materialintro.slide.SimpleSlide;

public class IntroSolveTspSlider extends AppIntro2 {
    private String T = "softcare";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
          addSolveTspSlide();
    }

    private void addSolveTspSlide() {
        addSlide(AppIntroFragment.createInstance(
                getString(R.string.intro_s1_t),
                getString(R.string.intro_s1_m),
                R.drawable.tsp_ico,
                R.color.design_default_color_primary
        ));

        addSlide(AppIntroFragment.createInstance(
                getString(R.string.intro_s2_t),
                getString(R.string.intro_s2_m),
                R.drawable.tsp_ico,
                R.color.design_default_color_primary
        ));
        addSlide(AppIntroFragment.createInstance(
                getString(R.string.intro_s3_t),
                getString(R.string.intro_s3_m),
                R.drawable.tsp_ico,
                R.color.design_default_color_primary
        ));
        addSlide(AppIntroFragment.createInstance(
                getString(R.string.intro_s4_t),
                getString(R.string.intro_s4_m),
                R.drawable.tsp_ico,
                R.color.design_default_color_primary
        ));
        addSlide(AppIntroFragment.createInstance(
                getString(R.string.intro_s5_t),
                getString(R.string.intro_s5_m),
                R.drawable.tsp_ico,
                R.color.design_default_color_primary
        ));
        addSlide(AppIntroFragment.createInstance(
                getString(R.string.intro_s6_t),
                getString(R.string.intro_s6_m),
                R.drawable.tsp_ico,
                R.color.design_default_color_primary
        ));
        addSlide(AppIntroFragment.createInstance(
                getString(R.string.intro_s7_t),
                getString(R.string.intro_s7_m),
                R.drawable.tsp_ico,
                R.color.design_default_color_primary
        ));

    }


}


