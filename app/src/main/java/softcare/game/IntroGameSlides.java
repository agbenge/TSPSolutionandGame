package softcare.game;


import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.Toast;

import com.github.appintro.AppIntro;
import com.github.appintro.AppIntro2;
import com.github.appintro.AppIntroCustomLayoutFragment;
import com.github.appintro.AppIntroFragment;
import com.github.appintro.AppIntroPageTransformerType;
import com.github.appintro.model.SliderPage;
import com.heinrichreimersoftware.materialintro.app.IntroActivity;
import com.heinrichreimersoftware.materialintro.slide.FragmentSlide;
import com.heinrichreimersoftware.materialintro.slide.SimpleSlide;

public class GameHelperSlider extends AppIntro2 {
    private String T = "softcare";
    SimpleSlide appTsp;
    SimpleSlide gameTsp;
    Context context;

    @Override
    protected void onPageSelected(int position) {
        super.onPageSelected(position);
        if (position > 0) {
            setButtonsEnabled(true);
        }
        if (position >= 10) {

        }


    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = getApplicationContext();
        addSlide(AppIntroCustomLayoutFragment.newInstance(R.layout.welcoome_slide_fragment)
        );

 addGameSlide();
          addSolveTspSlide();








 
        //Show/hide skip button
        setSkipButtonEnabled(true);

        //Enable immersive mode (no status and nav bar)
        setImmersiveMode();

        //Enable/disable page indicators
        setIndicatorEnabled(false);

        //Dhow/hide ALL buttons
        setButtonsEnabled(true);





       // Fade Transition
        setTransformer(AppIntroPageTransformerType.Depth.INSTANCE);
        // Show/hide status bar
        showStatusBar(false);
        //Speed up or down scrolling
        setScrollDurationFactor(2);
        //Enable the color "fade" animation between two slides (make sure the slide implements SlideBackgroundColorHolder)
       // setColorTransitionsEnabled(true);
        //Prevent the back button from exiting the slides
        setSystemBackButtonLocked(false);
        //Activate wizard mode (Some aesthetic changes)
        setWizardMode(false);
        try {
            CheckBox c = getSupportFragmentManager().getFragments().get(0).getActivity().findViewById(R.id.checkBox);
            if (c != null)
                c.setOnCheckedChangeListener((v, isCheck) -> {
                    Log.i(T, "Check change" + isCheck);
                });
            else Log.e(T, "Check Null");

        } catch (Exception e) {
            Log.e(T, "Error " + e.getLocalizedMessage());
        }

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

    private void addGameSlide() {

        addSlide(AppIntroFragment.createInstance(
                getString(R.string.intro_g1_t),
                getString(R.string.intro_g1_m),
                R.drawable.tsp_ico,
               R.color.design_default_color_primary)
        );
        addSlide(AppIntroFragment.createInstance(
                getString(R.string.intro_g2_t),
                getString(R.string.intro_g2_m),
                R.drawable.tsp_ico,
                R.color.design_default_color_primary
        ));
        addSlide(AppIntroFragment.createInstance(
                getString(R.string.intro_g3_t),
                getString(R.string.intro_g3_m),
                R.drawable.tsp_ico,
                R.color.design_default_color_primary
        ));
        addSlide(AppIntroFragment.createInstance(
                getString(R.string.intro_g4_t),
                getString(R.string.intro_g4_m),
                R.drawable.tsp_ico,
                R.color.design_default_color_primary
        ));
        addSlide(AppIntroFragment.createInstance(
                getString(R.string.intro_g5_t),
                getString(R.string.intro_g5_m),
                R.drawable.tsp_ico,
                R.color.design_default_color_primary
        ));
        addSlide(AppIntroFragment.createInstance(
                getString(R.string.intro_g6_t),
                getString(R.string.intro_g6_m),
                R.drawable.tsp_ico,
                R.color.design_default_color_primary
        ));

        addSlide(AppIntroFragment.createInstance(
                getString(R.string.intro_g7_t),
                getString(R.string.intro_g7_m),
                R.drawable.tsp_ico,
                R.color.design_default_color_primary
        ));
        addSlide(AppIntroFragment.createInstance(
                getString(R.string.intro_g9_t),
                getString(R.string.intro_g9_m),
                R.drawable.tsp_ico,
                R.color.design_default_color_primary
        ));
        addSlide(AppIntroFragment.createInstance(
                getString(R.string.intro_g10_t),
                getString(R.string.intro_g10_m),
                R.drawable.tsp_ico,
                R.color.design_default_color_primary
        ));
        addSlide(AppIntroFragment.createInstance(
                getString(R.string.intro_g11_t),
                getString(R.string.intro_g11_m),
                R.drawable.tsp_ico,
                R.color.design_default_color_primary
        ));
        addSlide(AppIntroFragment.createInstance(
                getString(R.string.intro_g12_t),
                getString(R.string.intro_g12_m),
                R.drawable.tsp_ico,
                R.color.design_default_color_primary
        ));
    }

    public void solveTspHelp(View view) {
        goToSlide(12);
    }
    public void gameHelp(View view) {
          goToSlide(1);
    }
    public void disableHelp(View view) {
        Toast.makeText(context, "Disable help clicked", Toast.LENGTH_SHORT).
                show();
    }

    private void goToSlide(int index) {
        for (int i = 0; i < index; i++) goToNextSlide();
    }
}


/*

 addSlide(AppIntroFragment.createInstance(
                "Explore",
                "Feel free to explore the rest of the library demo!",
                R.drawable.ic_slide4
        ));

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
















        kotlin


        // Toggle Indicator Visibility
isIndicatorEnabled = true

// Change Indicator Color
setIndicatorColor(
    selectedIndicatorColor = getColor(R.color.red),
    unselectedIndicatorColor = getColor(R.color.blue)
)

// Switch from Dotted Indicator to Progress Indicator
setProgressIndicator()

// Supply your custom `IndicatorController` implementation
indicatorController = MyCustomIndicator(/> initialize me </)

        AppIntro supports a wizards mode where the Skip button will be replaced with the back arrow. This comes handy if you're presenting a Wizard to your user with a set of skip they need to do, and they might frequently go back and forth.

        You can enable it with:

        isWizardMode = true

        setTransformer(AppIntroPageTransformerType.Fade)
        setTransformer(AppIntroPageTransformerType.Zoom)
        setTransformer(AppIntroPageTransformerType.Flow)
        setTransformer(AppIntroPageTransformerType.SlideOver)
        setTransformer(AppIntroPageTransformerType.Depth)

// You can customize your parallax parameters in the constructors.
        setTransformer(AppIntroPageTransformerType.Parallax(
                titleParallaxFactor = 1.0,
                imageParallaxFactor = -1.0,
                descriptionParallaxFactor = 2.0
        ))

        AppIntro offers the possibility to animate the color transition between two slides background. This feature is disabled by default, and you need to enable it on your AppIntro with:

isColorTransitionsEnabled = true
Once you enable it, the color will be animated between slides with a gradient. Make sure you provide a backgroundColor parameter in your slides.

If you're providing custom Fragments, you can let them support the color transition by implementing the SlideBackgroundColorHolder interface.



AppIntro is shipped with two top-level layouts that you can use. The default layout (AppIntro) has textual buttons, while the alternative layout has buttons with icons.




All the parameters are optional, so you're free to customize your slide as you wish.

If you need to programmatically create several slides
you can also use the SliderPage class. This class can be passed
to AppIntroFragment.createInstance(sliderPage: SliderPage) that will create a new slide starting from that instance.


 */

