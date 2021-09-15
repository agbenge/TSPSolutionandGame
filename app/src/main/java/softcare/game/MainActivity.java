package softcare.game;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MainActivity extends AppCompatActivity {
   private boolean soundState;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
       final SharedPreferences gameSettings = getSharedPreferences("game_settings",
                Activity.MODE_PRIVATE);

       ImageView sound= findViewById(R.id.sound);

       if(gameSettings.getBoolean("k_sound", true)||gameSettings.getBoolean("b_sound", true)){
                      sound.setImageDrawable(getResources().getDrawable(R.drawable.ic_baseline_volume_up_24));
                      soundState=true;
       }else{
           sound.setImageDrawable(getResources().getDrawable(R.drawable.ic_baseline_volume_off_24));
           soundState=false;
       }
        sound.setOnClickListener(v -> {
            SharedPreferences.Editor editor= gameSettings.edit();
            soundState= !soundState;
            editor.putBoolean("k_sound", soundState) ;
            editor.putBoolean("b_sound", soundState);
            editor.apply();
            editor.commit();

            if(gameSettings.getBoolean("k_sound", true)||
                    gameSettings.getBoolean("b_sound", true)){
                sound.setImageDrawable(getResources().getDrawable(R.drawable.ic_baseline_volume_up_24));
            }else{
                sound.setImageDrawable(getResources().getDrawable(R.drawable.ic_baseline_volume_off_24));
            }

        });
        ((AnimationDrawable) sound.getBackground()).start();
    }

    public  void game(View v){
        startActivity(new Intent(this, GameActivity.class));
    }
    public  void solution(View v){
 startActivity(new Intent(this, SolutionActivity.class));
    }
}