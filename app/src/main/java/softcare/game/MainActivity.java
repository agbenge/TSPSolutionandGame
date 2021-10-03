package softcare.game;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.helper.widget.Flow;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import softcare.game.model.CodeX;
import softcare.game.model.Game;
import softcare.game.model.LevelAdapter;
import softcare.gui.OnPointListener;
import softcare.gui.PointClickListener;
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

    protected void selectNewGame( ) {
        StyleDialog dialog = new StyleDialog(this);
        dialog.setContentView(R.layout.pop_progress);
        dialog.show();  selectNewGame(dialog ) ;
    }
    protected void selectNewGame(StyleDialog dialog ) {
        dialog.setContentView(R.layout.pop_game_resume);

        dialog.show();
        dialog.setCancelable(false);
        dialog.show();
        RecyclerView recycler=  dialog.findViewById(R.id.levels);
        String name="Level";
        LevelAdapter levelAdapter= new LevelAdapter(this,name);
        recycler.setAdapter(levelAdapter);


        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);


        int  spanCount=  dm.densityDpi/50;
        Log.d(CodeX.tag,  dm.densityDpi+" s "+spanCount);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this,spanCount);
        List<Integer> l = new ArrayList<>();
        for(int i=0;i< 50;i++) l.add(i);

        recycler.setLayoutManager(gridLayoutManager );
        levelAdapter.changeSize(14, l);
        levelAdapter.setPointClickListener(i -> {
            Toast.makeText(MainActivity.this," starting level "+i,Toast.LENGTH_LONG).show();
            dialog.cancel();
        });
    }
    public  void game(View v){
        startActivity(new Intent(this, GameActivity.class));
    }
    public  void solution(View v){
 startActivity(new Intent(this, SolutionActivity.class));
    }
}