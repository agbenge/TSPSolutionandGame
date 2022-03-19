package softcare.game;


import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.Toast;

import com.heinrichreimersoftware.materialintro.app.IntroActivity;
import com.heinrichreimersoftware.materialintro.slide.FragmentSlide;
import com.heinrichreimersoftware.materialintro.slide.SimpleSlide;

public class HelperSlider   extends IntroActivity {
    private String T="softcare";
    SimpleSlide appTsp;
    SimpleSlide gameTsp;
Context context;
/*
repositories {
    maven { url "https://jitpack.io" }
}

 */
// implementation 'com.github.AppIntro:AppIntro:6.1.0'


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
 context= getApplicationContext();
      setButtonBackVisible(false);
         setButtonNextVisible(false);
        setButtonCtaVisible(true);
         setButtonCtaTintMode(BUTTON_CTA_TINT_MODE_TEXT);
        addSlide(new FragmentSlide.Builder()
                .background(R.color.design_default_color_primary)
                .backgroundDark(R.color.design_default_color_primary_dark)
                .fragment(R.layout.welcoome_slide_fragment, R.style.Base_Theme_AppCompat_Light)
                .build());
gameTsp= new SimpleSlide.Builder()
                .title(R.string.intro_g1_t)
                .description(R.string.intro_g1_m)
                .image(R.drawable.tsp_ico)
                .background(R.color.design_default_color_primary)
                .backgroundDark(R.color.design_default_color_primary_dark)
                .build();
        addSlide(gameTsp);

        addSlide(new SimpleSlide.Builder()
                .title(R.string.intro_g2_t)
                .description(R.string.intro_g2_m)
                .image(R.drawable.tsp_ico)
                .background(R.color.design_default_color_primary)
                .backgroundDark(R.color.design_default_color_primary_dark)
                .scrollable(true)
                .build());

        addSlide(new SimpleSlide.Builder()
                .title(R.string.intro_g3_t)
                .description(R.string.intro_g3_m)
                .image(R.drawable.tsp_ico)
                .background(R.color.design_default_color_primary)
                .backgroundDark(R.color.design_default_color_primary_dark)
                .scrollable(true)
                .build());

        addSlide(new SimpleSlide.Builder()
                .title(R.string.intro_g4_t)
                .description(R.string.intro_g4_m)
                .image(R.drawable.tsp_ico)
                .background(R.color.design_default_color_primary)
                .backgroundDark(R.color.design_default_color_primary_dark)
                .scrollable(true)
                .build());
        addSlide(new SimpleSlide.Builder()
                .title(R.string.intro_g5_t)
                .description(R.string.intro_g5_m)
                .image(R.drawable.tsp_ico)
                .background(R.color.design_default_color_primary)
                .backgroundDark(R.color.design_default_color_primary_dark)
                .scrollable(true)
                .build());
        addSlide(new SimpleSlide.Builder()
                .title(R.string.intro_g6_t)
                .description(R.string.intro_g6_m)
                .image(R.drawable.tsp_ico)
                .background(R.color.design_default_color_primary)
                .backgroundDark(R.color.design_default_color_primary_dark)
                .scrollable(true)
                .build());
        addSlide(new SimpleSlide.Builder()
                .title(R.string.intro_g7_t)
                .description(R.string.intro_g7_m)
                .image(R.drawable.tsp_ico)
                .background(R.color.design_default_color_primary)
                .backgroundDark(R.color.design_default_color_primary_dark)
                .scrollable(true)
                .build());
         ///no eight
        addSlide(new SimpleSlide.Builder()
                .title(R.string.intro_g9_t)
                .description(R.string.intro_g9_m)
                .image(R.drawable.tsp_ico)
                .background(R.color.design_default_color_primary)
                .backgroundDark(R.color.design_default_color_primary_dark)
                .scrollable(true)
                .build());
        addSlide(new SimpleSlide.Builder()
                .title(R.string.intro_g10_t)
                .description(R.string.intro_g10_m)
                .image(R.drawable.tsp_ico)
                .background(R.color.design_default_color_primary)
                .backgroundDark(R.color.design_default_color_primary_dark)
                .scrollable(true)
                .build());
        addSlide(new SimpleSlide.Builder()
                .title(R.string.intro_g11_t)
                .description(R.string.intro_g11_m)
                .image(R.drawable.tsp_ico)
                .background(R.color.design_default_color_primary)
                .backgroundDark(R.color.design_default_color_primary_dark)
                .scrollable(true)
                .build());
        addSlide(new SimpleSlide.Builder()
                .title(R.string.intro_g12_t)
                .description(R.string.intro_g12_m)
                .image(R.drawable.tsp_ico)
                .background(R.color.design_default_color_primary)
                .backgroundDark(R.color.design_default_color_primary_dark)
                .scrollable(true)
                .build());


        ///App TSP
        appTsp = new SimpleSlide.Builder()
                .title(R.string.intro_s1_t)
                .description(R.string.intro_s1_m)
                .image(R.drawable.tsp_ico)
                .background(R.color.design_default_color_primary)
                .backgroundDark(R.color.design_default_color_primary_dark)
                .scrollable(true)
                .build();
        addSlide(appTsp);
        addSlide(new SimpleSlide.Builder()
                .title(R.string.intro_s2_t)
                .description(R.string.intro_s2_m)
                .image(R.drawable.tsp_ico)
                .background(R.color.design_default_color_primary)
                .backgroundDark(R.color.design_default_color_primary_dark)
                .scrollable(true)
                .build());
        addSlide(new SimpleSlide.Builder()
                .title(R.string.intro_s3_t)
                .description(R.string.intro_s3_m)
                .image(R.drawable.tsp_ico)
                .background(R.color.design_default_color_primary)
                .backgroundDark(R.color.design_default_color_primary_dark)
                .scrollable(true)
                .build());
        addSlide(new SimpleSlide.Builder()
                .title(R.string.intro_s4_t)
                .description(R.string.intro_s4_m)
                .image(R.drawable.tsp_ico)
                .background(R.color.design_default_color_primary)
                .backgroundDark(R.color.design_default_color_primary_dark)
                .scrollable(true)
                .build());
        addSlide(new SimpleSlide.Builder()
                .title(R.string.intro_s5_t)
                .description(R.string.intro_s5_m)
                .image(R.drawable.tsp_ico)
                .background(R.color.design_default_color_primary)
                .backgroundDark(R.color.design_default_color_primary_dark)
                .scrollable(true)
                .build());
        addSlide(new SimpleSlide.Builder()
                .title(R.string.intro_s6_t)
                .description(R.string.intro_s6_m)
                .image(R.drawable.tsp_ico)
                .background(R.color.design_default_color_primary)
                .backgroundDark(R.color.design_default_color_primary_dark)
                .scrollable(true)
                .build());


       // / ((CheckBox)findViewById(R.id.checkBox)).setOnCheckedChangeListener((v,isCheck)->{
         //   Log.i(T,"Check change"+isCheck);
       // });

    }

    public void solveTspHelp(View view) {
        goToSlide(12);
    }
    public void gameHelp(View view) {
        goToSlide(1);
    }
    public void disableHelp(View view) {
        Toast.makeText(context, "Disable help clicked",Toast.LENGTH_SHORT).
                show();
    }



}

class G extends AppInt


