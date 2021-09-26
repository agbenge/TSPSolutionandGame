package softcare.game;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.helper.widget.Flow;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import softcare.game.model.Game;
import softcare.game.model.Tsp;
import softcare.gui.StyleDialog;
import softcare.util.S;

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
        ((AnimationDrawable)((ImageView)findViewById(R.id.ico_main)).getDrawable()).start();

    }
    protected void saveGave( ) {
        StyleDialog ad = new StyleDialog(this);
        ad.setContentView(R.layout.pop_game_resume);
        ((TextView) ad.findViewById(R.id.title)).setText("Shortest path ");
        ad.findViewById(R.id.return_btn).setOnClickListener(v -> {
            ad.cancel();

        });
        ad.show();
        ad.findViewById(R.id.try_again).setOnClickListener(v -> {
            ad.cancel();

        });
        ad.setCancelable(false);
        ad.show();
       ConstraintLayout c= ad.findViewById(R.id.levels) ;
        Flow f=  ad.findViewById(R.id.levels_flow) ;
        for (int i=0;i<50;i++){
            Button b= new Button(this);
            b.setText("Level "+i+1);
            b.setId(i);
            c.addView(b,i);
            f.addView(b);
        }

    }
    public  void game(View v){
        startActivity(new Intent(this, GameActivity.class));
    }
    public  void solution(View v){
 startActivity(new Intent(this, SolutionActivity.class));
    }
}