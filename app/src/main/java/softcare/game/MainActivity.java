package softcare.game;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.snackbar.Snackbar;

import java.util.Calendar;
import java.util.Date;

import softcare.gui.StyleDialog;
import softcare.util.Util;

public class MainActivity extends AppCompatActivity{
   private boolean soundState;
    private SharedPreferences gameSettings;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        gameSettings = getSharedPreferences("game_settings",
                 Activity.MODE_PRIVATE);


        findViewById(R.id.solution_btn).setOnClickListener(this::solution);
        findViewById(R.id.game_btn).setOnClickListener(this::game);
        findViewById(R.id.contact_us_btn).setOnClickListener(this::contactUs);
        findViewById(R.id.help_btn).setOnClickListener(this::help);
        findViewById(R.id.about_btn).setOnClickListener(this::about);
        ImageView sound= findViewById(R.id.sound_btn);
        findViewById(R.id.share_btn).setOnClickListener(this::shareApp);
       if(gameSettings.getBoolean("k_sound", true)|| gameSettings.getBoolean("b_sound", true)){
           sound.setImageResource(R.drawable.ic_baseline_volume_up_24);
           soundState=true;
       }else{
           sound.setImageResource( R.drawable.ic_baseline_volume_off_24);
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
                sound.setImageResource(R.drawable.ic_baseline_volume_up_24);
                Toast.makeText(MainActivity.this,R.string.sound_enable,Toast.LENGTH_SHORT).show();

            }else{
                sound.setImageResource(R.drawable.ic_baseline_volume_off_24);
                Toast.makeText(MainActivity.this,R.string.sound_disable,Toast.LENGTH_SHORT).show();
            }

        });



//        if(isNewUser()) {
//            isNewUser(false);
//        }
//            else {
//            reinstallOn2(12, 6, 2023);
//        }


            if(!gameSettings.getBoolean("disable_help",false))
                help(null);


    }


    private void shareApp(View view) {
        Intent sendIntent = new Intent(Intent.ACTION_SEND);
        sendIntent.setType("text/*");
        sendIntent.putExtra(Intent.EXTRA_TEXT, "https://play.google.com/store/apps/details?id=softcare.game");
        sendIntent.putExtra(Intent.EXTRA_TITLE, "Sharing Path Finder link");
        startActivity(Intent.createChooser(sendIntent, "Sharing App"));
    }

    private void contactUs(View view) {
        // Show a small options dialog to choose between Email or WhatsApp
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Contact Us")
                .setItems(new CharSequence[]{"Email Support", "WhatsApp Support"}, (dialog, which) -> {
                    if (which == 0) {
                        contactByEmail();
                    } else {
                        contactByWhatsApp();
                    }
                })
                .show();
    }

    private void contactByEmail() {
        try {
            Intent emailIntent = new Intent(Intent.ACTION_SENDTO);
            emailIntent.setData(Uri.parse("mailto:raphaelraymondproduct@gmali.com")); // Replace with your email
            emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Support Request");
            emailIntent.putExtra(Intent.EXTRA_TEXT, "Hello,\n\nI need help with...");
            startActivity(Intent.createChooser(emailIntent, "Send Email"));
        } catch (Exception e) {
            Toast.makeText(this, "No email app found", Toast.LENGTH_SHORT).show();
        }
    }

    private void contactByWhatsApp() {
        String phoneNumber = "+2349022588943"; // Replace with your WhatsApp support number
        String message = "Hello, I need help with your app.";

        try {
            String url = "https://wa.me/" + phoneNumber.replace("+", "") + "?text=" + Uri.encode(message);
            Intent whatsappIntent = new Intent(Intent.ACTION_VIEW);
            whatsappIntent.setData(Uri.parse(url));
            startActivity(whatsappIntent);
        } catch (Exception e) {
            Toast.makeText(this, "WhatsApp not installed", Toast.LENGTH_SHORT).show();
        }
    }



    public  void game(View v){
        startActivity(new Intent(this, GameActivity.class));
    }
    public  void solution(View v){
 startActivity(new Intent(this, SolutionActivity.class));
    }

    public void about(View v) {

        StyleDialog dialog = new StyleDialog(this);
        dialog.setContentView(R.layout.about);
        dialog.show();
        dialog.findViewById(R.id.return_btn).setOnClickListener(k-> dialog.cancel());

    }
    public void help(View v ) {
        startActivity(new Intent(this, IntroMain.class));
    }
 //   final private String prefName = "game_settings";
//    public  boolean  reinstallOn(int day, int month, int year){
//        Date now= new Date();
//        now.setTime(System.currentTimeMillis());
//        Calendar c = Calendar.getInstance();
//        int hour = c.get(Calendar.HOUR_OF_DAY);
//        int minute = c.get(Calendar.MINUTE);
//        Date end= Util.getDateFromString(day + "/" + (month + 1) + "/" + year + " " + hour + ":" + minute);
//        if(end.before(now)){
//            System.out.println("Time is over");
//            return true;
//        }else{
//            System.out.println("Time  remains ");
//            return false;
//        }
//    }
//
//    public  boolean reinstallOn2(int day, int month, int year){
//        boolean res=reinstallOn(day,month,year);
//        SharedPreferences.Editor editor = gameSettings.edit();
//        editor.putBoolean("grace", !res);
//        editor.apply();
//        editor.commit();
//        if(res){
//          final int warnCount=  gameSettings.getInt("warn_count",0);
//            editor.putInt("warn_count", warnCount+1);
//            editor.apply();
//            editor.commit();
//          StyleDialog d= new StyleDialog(this);
//          d.setContentView(R.layout.update_app);
//          d.show();
//          d.setCancelable(false);
//          d.findViewById(R.id.return_btn).setOnClickListener(v->{
//              d.cancel();
//              if(warnCount>15) onBackPressed();
//          });
//            if(warnCount>15)
//                ((TextView) d.findViewById(R.id.msg)).setText(R.string.warn_over);
//          else {
//             String s=getString(R.string.warn_msg)
//                     +" "+(15-warnCount)+" "+getString(R.string.time_s_);
//                ((TextView) d.findViewById(R.id.msg)).setText(s);
//            }
//
//            d.findViewById(R.id.update).setOnClickListener(v->{
//                d.cancel();
//
//                    String url1 =  "market://details?id="    +getApplicationContext().getPackageName() ;
//                    String url2 =  "https://play.google.com/store/apps/details?id=com.softcare.game"    ;
//                    try {
//                        startActivity(new Intent(Intent.ACTION_VIEW,  Uri.parse(url1)));
//                    } catch (Exception e) {
//                        e.printStackTrace();
//                        try {
//                           startActivity(new Intent(Intent.ACTION_VIEW,  Uri.parse(url2)));
//                        } catch ( Exception e2){
//                            Snackbar.make(v, getString(R.string.error_occurred), Snackbar.LENGTH_LONG).show();
//
//
//                            e2.printStackTrace();
//                        }
//
//                    }
//
//                finish();
//            });
//            d.findViewById(R.id.button).setOnClickListener(this::help);
//        }
//
//        return res;
//    }
//    public void isNewUser(boolean update  ) {
//        SharedPreferences.Editor editor = gameSettings.edit();
//        editor.putBoolean("newUser", update);
//        editor.apply();
//        editor.commit();
//    }
//    public boolean isNewUser() {
//        return gameSettings.getBoolean("newUser", true);
//    }
//

}