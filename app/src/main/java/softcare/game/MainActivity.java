package softcare.game;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.AnimationDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import softcare.game.model.CodeX;
import softcare.game.model.LevelAdapter;
import softcare.gui.StyleDialog;
import softcare.util.S;

public class MainActivity extends AppCompatActivity {
   private boolean soundState;
    private SharedPreferences gameSettings;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        gameSettings = getSharedPreferences("game_settings",
                 Activity.MODE_PRIVATE);

       ImageView sound= findViewById(R.id.sound);

       if(gameSettings.getBoolean("k_sound", true)|| gameSettings.getBoolean("b_sound", true)){
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
                Toast.makeText(MainActivity.this,R.string.sound_enable,Toast.LENGTH_SHORT).show();

            }else{
                sound.setImageDrawable(getResources().getDrawable(R.drawable.ic_baseline_volume_off_24));
                Toast.makeText(MainActivity.this,R.string.sound_disable,Toast.LENGTH_SHORT).show();
            }

        });
        ((AnimationDrawable) sound.getBackground()).start();
        ((AnimationDrawable)((ImageView)findViewById(R.id.ico_main)).getDrawable()).start();
        reinstallOn(this,12,6,22);
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

    public void help(View v ) {
        StyleDialog dialog = new StyleDialog(this);
        dialog.setContentView(R.layout.help);
        dialog.show();
        dialog.findViewById(R.id.return_btn).setOnClickListener(k-> dialog.cancel());
        dialog.findViewById(R.id.contact_us).setOnClickListener(k-> {
            String subject = getString(R.string.app_name);
            String[] addresses = getResources().getStringArray(R.array.email_addresses);
            Intent intent = new Intent(Intent.ACTION_SENDTO);
            intent.setData(Uri.parse("mailto:"));
            intent.putExtra(Intent.EXTRA_EMAIL, addresses);
            intent.putExtra(Intent.EXTRA_SUBJECT, subject);
            if (intent.resolveActivity(getPackageManager()) != null) {
                startActivity(intent);}

        });
    }

    final private String prefName = "game_settings";
    public  boolean  reinstallOn(int day, int month, int year){
        Date now= new Date();
        now.setTime(System.currentTimeMillis());
        Calendar c = Calendar.getInstance();
        int hour = c.get(Calendar.HOUR_OF_DAY);
        int minute = c.get(Calendar.MINUTE);
        Date end= S.getDateFromString(day + "/" + (month + 1) + "/" + year + " " + hour + ":" + minute);
        if(end.before(now)){
            System.out.println("Time is over");
            return true;
        }else{
            System.out.println("Time  remains ");
            return false;
        }
    }

    public  boolean reinstallOn(AppCompatActivity activity,int day, int month, int year){
        boolean res=reinstallOn(day,month,year);

        SharedPreferences.Editor editor = gameSettings.edit();
        editor.putBoolean("grace", !res);
        editor.apply();
        editor.commit();
        if(res){
          final int warnCount=  gameSettings.getInt("warn_count",0);
            editor.putInt("warn_count", warnCount+1);
          StyleDialog d= new StyleDialog(this);
          d.setContentView(R.layout.update_app);
          d.show();
          d.setCancelable(false);
          d.findViewById(R.id.return_btn).setOnClickListener(v->{
              d.cancel();
              if(warnCount>15) onBackPressed();
          });
            if(warnCount>15)
                ((TextView) d.findViewById(R.id.msg)).setText(R.string.warn_over);
          else  ((TextView) d.findViewById(R.id.msg)).setText(getString(R.string.warn_msg)
            +" "+(15-warnCount)+" "+getString(R.string.time_s_));

            d.findViewById(R.id.update).setOnClickListener(v->{
                d.cancel();
                finish();
            });
            d.findViewById(R.id.help).setOnClickListener(v->{
                help(v);
            });
        }

        return res;
    }
    public void isNewUser(Context context, boolean update  ) {
        SharedPreferences s = context.getSharedPreferences(prefName, Activity.MODE_PRIVATE);
        SharedPreferences.Editor editor = s.edit();
        editor.putBoolean("newUser", update);
        editor.apply();
        editor.commit();
        return ;
    }
    public boolean isNewUser(Context context  ) {
        SharedPreferences s = context.getSharedPreferences(prefName, Activity.MODE_PRIVATE);
        return s.getBoolean("newUser", true);
    }


}