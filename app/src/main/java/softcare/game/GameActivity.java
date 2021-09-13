package softcare.game;

import android.app.Activity;
import android.content.SharedPreferences;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.material.snackbar.Snackbar;

import softcare.game.model.CodeX;
import softcare.game.model.Game;
import softcare.game.model.GameViewModel;
import softcare.game.model.Tsp;
import softcare.game.model.TspCode;
import softcare.gui.OnPointListener;
import softcare.gui.PlotGame;
import softcare.gui.StyleDialog;

public class GameActivity extends AppCompatActivity implements OnPointListener {
    private GameViewModel gameViewModel;
    private LinearLayout answersContainer;
    private LinearLayout optionsContainer;
    private PlotGame plotGame;
    private TextView level;
    private TextView scores;
    private TextView time;
    private TextView menu;
    private long startTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
          _init();
        viewModelListener();
      if( gameViewModel.getGame().getValue()==null) resume(readStoredGame());
       setAnimations();
    }

    private void _init() {
        level = findViewById(R.id.level);
        scores = findViewById(R.id.scores);
        time = findViewById(R.id.time);
        menu = findViewById(R.id.menu);
        plotGame = findViewById(R.id.plotGame);
        answersContainer = findViewById(R.id.answersContainer);
        optionsContainer = findViewById(R.id.optionsContainer);
        gameViewModel = new ViewModelProvider(this).get(GameViewModel.class);

    }


    private Game readStoredGame() {
        SharedPreferences s =getSharedPreferences("game_settings",
                Activity.MODE_PRIVATE);
        return  new Game(3,   10);
    }
    private void storedGame() {

        SharedPreferences s =getSharedPreferences("game_settings",
                Activity.MODE_PRIVATE);
        SharedPreferences.Editor editor = s.edit();
        editor.apply();
        editor.commit();
    }

    private void addOption(Button b, int i) {
        b.setOnClickListener(v -> answer(b,i));
        optionsContainer.addView(b);
    }
    private void addAnswer(Button b, int i) {
        b.setOnClickListener(v -> {
            answersContainer.removeView(b);
            addOption(b, i);
            Game game=  gameViewModel.getGame().getValue();
            game.removeDirection(i);
            gameViewModel.setGame(game);
           // plotGame.plotPath(game.getDirection());


        });
        answersContainer.addView(b);
    }
   private void answer(Button b, int i){
        optionsContainer.removeView(b);
        addAnswer(b, i);
       Game game=  gameViewModel.getGame().getValue();

        game.addDirection(i);

       gameViewModel.setGame(game);
       // plotGame.plotPath(game.getDirection());
        if (optionsContainer.getChildCount() == 0L) {
            finishGame();
        }
    }
    private void viewModelListener() {
        gameViewModel.getTspLiveData().observe(this, tsp -> {
            Game game=  gameViewModel.getGame().getValue();
            if (tsp != null&&game!=null) {

                plotGame.setZoom(game.getBound() * 5);
                level.setText(String.valueOf(game.getLevel()));
                scores.setText(String.valueOf(game.getScores()));
                if (tsp.getTspActions() == TspCode.SOLVED) {
                    int i = 0;
                    for (String city : tsp.getCities()) {
                        Button b = new Button(GameActivity.this);
                        b.setId(i);
                        b.setText(city);
                        b.setBackground(getResources().getDrawable(R.drawable.btn_b));
                        b.setTextColor(getResources().getColor(R.color.white));
                        addOption(b, i);
                        //  Log.d(CodeX.tag," i =="+i+"  "+city);
                        i++;
                    }
                    plotGame.plotGame(tsp.getPointXY(), tsp.getCities());

                }
            } else {
                Snackbar.make(plotGame, "Inputs empty start a new problem", Snackbar.LENGTH_LONG).show();

            }
        });
        gameViewModel.getErrorCodeLiveData().observe(this, errorCode -> Snackbar.make(plotGame, "Error " + errorCode.toString(), Snackbar.LENGTH_LONG).setAction("Action", null).show());
        gameViewModel.getProgressXLiveData().observe(this, progressX -> Log.d("game", progressX.toString()));
        gameViewModel.getGame().observe(this, game -> {
            if(game!=null) {
                plotGame.plotPath(game.getDirection());
            }

        });
plotGame.setOnPointListener(this);
menu.setOnClickListener(v -> menu());
    }
    private void finishGame() {
        answersContainer.removeAllViews();
        Tsp tsp = gameViewModel.getTspLiveData().getValue();
        Game game =gameViewModel.getGame().getValue();
        if (tsp != null) {
            if (game != null) {
                String result = game.getResult(tsp);
                if (tsp.getCost() >=game.getCost()) {
                    win(game, result,tsp);
                } else {
                   loose(game, result);
                }
            }
        }
    }

    private void win(Game game,String result, Tsp tsp) {
        StyleDialog ad = new StyleDialog(this);
        ad.setContentView(R.layout.pop_game_end);
        ((TextView) ad.findViewById(R.id.title)).setText("You Wine");
        ((TextView) ad.findViewById(R.id.msg)).setText(result);
        ((TextView) ad.findViewById(R.id.try_again)).setText("Next");
        ad.findViewById(R.id.msg).setOnClickListener(v -> {

        });
        ad.findViewById(R.id.take_rest).setOnClickListener(v -> {
            ad.cancel();
            takeRest(game);
        });
        ad.findViewById(R.id.save_game).setOnClickListener(v -> gameViewModel.saveGame());
        ad.findViewById(R.id.try_again).setOnClickListener(v -> {
            ad.cancel();
            game.next(tsp.getCost() >game.getCost(),System.currentTimeMillis()-startTime);
            gameViewModel.next(game);
        });
        ad.show();
    }

    private void loose(Game game,String result) {
        if(game.getTryAgain()< 3) {
             gameOver(game);
            return;
        }
        StyleDialog ad = new StyleDialog(this);
        ad.setContentView(R.layout.pop_game_end);

        ((TextView) ad.findViewById(R.id.title)).setText("You Lose");
        ((TextView) ad.findViewById(R.id.msg)).setText(result);
        ad.findViewById(R.id.msg).setOnClickListener(v -> {

        });
        ad.findViewById(R.id.take_rest).setOnClickListener(v -> { takeRest(game);
             takeRest(game);
        });
        ad.findViewById(R.id.save_game).setOnClickListener(v -> {
            if( gameViewModel.getGame().getValue()==null) gameViewModel.onCreateGame(readStoredGame());
        });
        ad.findViewById(R.id.try_again).setOnClickListener(v -> {

            game.tryAgain();
            gameViewModel.tryAgain(game);
        });
        ad.show();
    }

    private void takeRest(Game game) {
        StyleDialog ad = new StyleDialog(this);
        ad.setContentView(R.layout.pop_game_ended);
            ((TextView) ad.findViewById(R.id.title)).setText("Stop Game");
        ((TextView) ad.findViewById(R.id.level)).setText(String.valueOf(game.getLevel()));
        ((TextView) ad.findViewById(R.id.scores)).setText(String.valueOf(game.getLevel()));
        ((TextView) ad.findViewById(R.id.time)).setText(String.valueOf(game.getGameLifeTime()));
        ((TextView) ad.findViewById(R.id.try_again)).setText("Exit");
        ad.findViewById(R.id.msg).setOnClickListener(v -> {

        });
        ad.findViewById(R.id.try_again).setVisibility(View.GONE);
        ad.findViewById(R.id.save_game).setOnClickListener(v -> gameViewModel.saveGame());
        ad.findViewById(R.id.take_rest).setOnClickListener(v -> {
            ad.cancel();
            onBackPressed();
        });
        ad.show();
        ad.setCancelable(false);
    }

    private void gameOver(Game game) {
        StyleDialog ad = new StyleDialog(this);
        ad.setContentView(R.layout.pop_game_ended);
        ((TextView) ad.findViewById(R.id.title)).setText("Game Over");
        ((TextView) ad.findViewById(R.id.level)).setText(String.valueOf(game.getLevel()));
        ((TextView) ad.findViewById(R.id.scores)).setText(String.valueOf(game.getLevel()));
        ((TextView) ad.findViewById(R.id.time)).setText(String.valueOf(game.getGameLifeTime()));
        ((TextView) ad.findViewById(R.id.try_again)).setText("New");
        ad.findViewById(R.id.msg).setOnClickListener(v -> {

        });
        ad.findViewById(R.id.take_rest).setVisibility(View.INVISIBLE);
        ad.findViewById(R.id.save_game).setOnClickListener(v -> gameViewModel.saveGame());
        ad.findViewById(R.id.try_again).setOnClickListener(v -> {
            if( gameViewModel.getGame().getValue()==null) gameViewModel.onCreateGame(readNewGame());
        });
        ad.show();
        ad.setCancelable(false);
    }
    private void   resume(Game game){
        
        StyleDialog ad = new StyleDialog(this);
        ad.setContentView(R.layout.pop_game_ended); 
        ((TextView) ad.findViewById(R.id.title)).setText("Welcome");
        ((TextView) ad.findViewById(R.id.level)).setText(String.valueOf(game.getLevel()));
        ((TextView) ad.findViewById(R.id.scores)).setText(String.valueOf(game.getLevel()));
        ((TextView) ad.findViewById(R.id.time)).setText(String.valueOf(game.getGameLifeTime()));
        ((TextView) ad.findViewById(R.id.try_again)).setText("Resume");
        ((TextView) ad.findViewById(R.id.take_rest)).setText("New");

        ad.findViewById(R.id.save_game).setVisibility(View.GONE);
        ad.findViewById(R.id.take_rest).setOnClickListener(v -> {
            ad.cancel();
            if( gameViewModel.getGame().getValue()==null) gameViewModel.onCreateGame(readNewGame());

        });
        ad.findViewById(R.id.try_again).setOnClickListener(v -> {
            ad.cancel();
            if( gameViewModel.getGame().getValue()==null) gameViewModel.onCreateGame(readStoredGame());
        });
        ad.show();
        ad.setCancelable(false);
    }
    private Game readNewGame() {
        return  new Game(1,   5000);
    }

    @Override
    protected void onResume() {
        super.onResume();
        startTime= System.currentTimeMillis();
    }


    @Override
    protected void onPause() {
        super.onPause();
        gameViewModel.pause(System.currentTimeMillis()-startTime);
    }

    @Override
    public void onPoint(int index) {
       Button b =optionsContainer.findViewById(index);
       if(b==null)b =answersContainer.findViewById(index);
        //answer(b,index);
        if(b!=null) b.performClick();
        Log.d(CodeX.tag, "index "+index);
    }







    private void menu() {
        StyleDialog ad = new StyleDialog(this);
        ad.setContentView(R.layout.pop_game_meue);
        SharedPreferences s =getSharedPreferences("game_settings",
                Activity.MODE_PRIVATE);
        SharedPreferences.Editor editor = s.edit();

        ((Switch)  ad.findViewById(R.id.timing)).setOnCheckedChangeListener((buttonView, isChecked) -> {
            editor.putBoolean("timing", isChecked);
            editor.apply();
            editor.commit();
        });
        ((Switch)  ad.findViewById(R.id.sound)).setOnCheckedChangeListener((buttonView, isChecked) -> {
            editor.putBoolean("sound", isChecked);
            editor.apply();
            editor.commit();
        });
        ((Switch)  ad.findViewById(R.id.refresh_points)).setOnCheckedChangeListener((buttonView, isChecked) -> {
            editor.putBoolean("refresh_points", isChecked);
            editor.apply();
            editor.commit();
        });

        ad.show();
    }







    private void setAnimations() {
        ( (AnimationDrawable) level.getBackground()).start();
        ( (AnimationDrawable) scores.getBackground()).start();
        ( (AnimationDrawable) time.getBackground()).start();
    }

    public void zoomIn(View v) {
        plotGame.zoomIn();
        Log.d(CodeX.tag, " Zoom In -------- zoom is " + plotGame.getZoom());
    }

    public void zoomOut(View v) {
        plotGame.zoomOut();
        Log.d(CodeX.tag, " Zoom out -------- zoom is " + plotGame.getZoom());
    }

}