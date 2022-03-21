package softcare.game;


import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import com.github.appintro.AppIntro2;
import com.github.appintro.AppIntroFragment;
import com.github.appintro.AppIntroPageTransformerType;

public class IntroGameSlides extends AppIntro2 {
    private String T = "softcare";
    @Override
    protected void onSkipPressed(@Nullable Fragment currentFragment) {
        super.onSkipPressed(currentFragment);
      //  startActivity(new Intent(IntroGameSlides.this, GameActivity.class));
        finish();
    }

    @Override
    protected void onIntroFinished() {
        super.onIntroFinished();
        startActivity(new Intent(IntroGameSlides.this,GameActivity.class));
        finish();
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addGameSlide() ;
       // setImmersiveMode();
        setTransformer(AppIntroPageTransformerType.Flow.INSTANCE);
        setColorTransitionsEnabled(true);



    }


    private void addGameSlide() {
        addSlide(AppIntroFragment.createInstance(
                getString(R.string.intro_g1_t),
                getString(R.string.intro_g1_m),
                R.drawable.g1,
               R.color.design_default_color_primary)
        );
        addSlide(AppIntroFragment.createInstance(
                getString(R.string.intro_g2_t),
                getString(R.string.intro_g2_m),
                R.drawable.g2,
                R.color.blueDark
        ));
        addSlide(AppIntroFragment.createInstance(
                getString(R.string.intro_g3_t),
                getString(R.string.intro_g3_m),
                R.drawable.g3,
                R.color.design_default_color_primary
        ));
        addSlide(AppIntroFragment.createInstance(
                getString(R.string.intro_g4_t),
                getString(R.string.intro_g4_m),
                R.drawable.g4,
                R.color.blueDark
        ));
        addSlide(AppIntroFragment.createInstance(
                getString(R.string.intro_g5_t),
                getString(R.string.intro_g5_m),
                R.drawable.g5,
                R.color.design_default_color_primary
        ));
        addSlide(AppIntroFragment.createInstance(
                getString(R.string.intro_g6_t),
                getString(R.string.intro_g6_m),
                R.drawable.g6,
                R.color.blueDark
        ));

        addSlide(AppIntroFragment.createInstance(
                getString(R.string.intro_g7_t),
                getString(R.string.intro_g7_m),
                R.drawable.g7,
                R.color.design_default_color_primary
        ));
        addSlide(AppIntroFragment.createInstance(
                getString(R.string.intro_g9_t),
                getString(R.string.intro_g9_m),
                R.drawable.g9,
                R.color.blueDark
        ));
        addSlide(AppIntroFragment.createInstance(
                getString(R.string.intro_g10_t),
                getString(R.string.intro_g10_m),
                R.drawable.g10,
                R.color.design_default_color_primary
        ));
        addSlide(AppIntroFragment.createInstance(
                getString(R.string.intro_g11_t),
                getString(R.string.intro_g11_m),
                R.drawable.g11,
                R.color.blueDark
        ));
        addSlide(AppIntroFragment.createInstance(
                getString(R.string.intro_g12_t),
                getString(R.string.intro_g12_m),
                R.drawable.g12,
                R.color.design_default_color_primary
        ));
    }


}


/*
        // Fade Transition
        setTransformer(AppIntroPageTransformerType.Fade.INSTANCE);

        // Show/hide status bar
        showStatusBar(true);

        //Speed up or down scrolling
        setScrollDurationFactor(2);

        //Enable the color "fade" animation between two slides (make sure the slide implements SlideBackgroundColorHolder)
        setColorTransitionsEnabled(true);

        //Prevent the back button from exiting the slides
        setSystemBackButtonLocked(true);

        //Activate wizard mode (Some aesthetic changes)
        setWizardMode(true);

        //Show/hide skip button
        setSkipButtonEnabled(true);

        //Enable immersive mode (no status and nav bar)
        setImmersiveMode();

        //Enable/disable page indicators
        setIndicatorEnabled(true);

        //Dhow/hide ALL buttons
        setButtonsEnabled(true);


 */