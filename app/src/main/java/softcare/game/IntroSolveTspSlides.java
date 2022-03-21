package softcare.game;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.github.appintro.AppIntro2;
import com.github.appintro.AppIntroCustomLayoutFragment;
import com.github.appintro.AppIntroFragment;
import com.github.appintro.AppIntroPageTransformerType;
import com.heinrichreimersoftware.materialintro.slide.SimpleSlide;

public class IntroSolveTspSlides extends AppIntro2 {
    private String T = "softcare";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
          addSolveTspSlide();
        setTransformer(AppIntroPageTransformerType.Flow.INSTANCE);
        setColorTransitionsEnabled(true);
    }

    private void addSolveTspSlide() {
        addSlide(AppIntroFragment.createInstance(
                getString(R.string.intro_s1_t),
                getString(R.string.intro_s1_m),
                R.drawable.s1,
                R.color.design_default_color_primary
        ));

        addSlide(AppIntroFragment.createInstance(
                getString(R.string.intro_s2_t),
                getString(R.string.intro_s2_m),
                R.drawable.s2,
                R.color.blueDark
        ));
        addSlide(AppIntroFragment.createInstance(
                getString(R.string.intro_s3_t),
                getString(R.string.intro_s3_m),
                R.drawable.s3,
                R.color.design_default_color_primary
        ));
        addSlide(AppIntroFragment.createInstance(
                getString(R.string.intro_s4_t),
                getString(R.string.intro_s4_m),
                R.drawable.s4,
                R.color.blueDark
        ));
        addSlide(AppIntroFragment.createInstance(
                getString(R.string.intro_s5_t),
                getString(R.string.intro_s5_m),
                R.drawable.s5,
                R.color.design_default_color_primary
        ));
        addSlide(AppIntroFragment.createInstance(
                getString(R.string.intro_s6_t),
                getString(R.string.intro_s6_m),
                R.drawable.s6,
                R.color.blueDark
        ));
        addSlide(AppIntroFragment.createInstance(
                getString(R.string.intro_s7_t),
                getString(R.string.intro_s7_m),
                R.drawable.s7,
                R.color.design_default_color_primary
        ));
        addSlide(AppIntroFragment.createInstance(
                getString(R.string.intro_s8_t),
                getString(R.string.intro_s8_m),
                R.drawable.s8,
                R.color.blueDark
        ));

    }

    @Override
    protected void onSkipPressed(@Nullable Fragment currentFragment) {
        super.onSkipPressed(currentFragment);
      //  startActivity(new Intent(IntroSolveTspSlides.this, SolutionActivity.class));
        finish();
    }

    @Override
    protected void onIntroFinished() {
        super.onIntroFinished();
        startActivity(new Intent(IntroSolveTspSlides.this, SolutionActivity.class));
        finish();
    }
}


