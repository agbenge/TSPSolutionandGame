package softcare.game;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.CheckBox;

public class IntroMain extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.welcoome_slide_fragment);
        SharedPreferences gameSettings = getSharedPreferences("game_settings",
                Activity.MODE_PRIVATE);
        CheckBox c= findViewById(R.id.checkBox);
    c.setChecked(gameSettings.getBoolean("disable_help",false));
    c.setOnCheckedChangeListener((v,b)->{

            SharedPreferences.Editor editor= gameSettings.edit();
            editor.putBoolean("disable_help",b) ;
            editor.apply();
            editor.commit();
        });
        findViewById(R.id.textView14).setOnClickListener(v->{
          startActivity(new Intent(IntroMain.this, IntroGameSlides.class));
          finish();
        });
        findViewById(R.id.textView15).setOnClickListener(v->{
            startActivity(new Intent(IntroMain.this, IntroSolveTspSlides.class));
            finish();
        });
        findViewById(R.id.button2).setOnClickListener(v->{
            finish();
        });

    }


}