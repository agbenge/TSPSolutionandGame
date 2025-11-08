package softcare.game;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.AnimationDrawable;
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Looper;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;
import androidx.constraintlayout.widget.Group;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.snackbar.Snackbar;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import softcare.game.model.CityInfo;
import softcare.game.model.CodeX;
import softcare.game.model.Game;
import softcare.game.model.GameShare;
import softcare.game.model.GameViewModel;
import softcare.game.model.LevelAdapter;
import softcare.game.model.Location;
import softcare.game.model.TaskManager;
import softcare.game.model.Tsp;
import softcare.game.model.TspCode;
import softcare.gui.OnPointListener;
import softcare.gui.PlotGame;
import softcare.gui.StyleDialog;
import softcare.util.Util;

public class GameActivity extends AppCompatActivity implements OnPointListener {
    private GameViewModel gameViewModel;
    private LinearLayout answersContainer;
    private LinearLayout optionsContainer;
    private PlotGame plotGame;
    private TextView level;
    private TextView scores;
    private TextView time;
    private View menu;
    private long startTime;
    private SharedPreferences gameSettings;
    private int[] soundIds;
    private int bSoundStreamId = -1;
    private CountUpTimer countUPTimer;
    private CountDownTimer countDownTimer;
    protected StyleDialog dialog;
    protected TaskManager taskManager;
    private Group playGroup;
    private GameShare bestGame;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
        gameSettings = getSharedPreferences("game_settings",
                Activity.MODE_PRIVATE);

        taskManager = TaskManager.getInstance();
        playGroup = findViewById(R.id.playGroup);
        _init();
        viewModelListener();
        setAnimations();
        startSound();

 findViewById(R.id.close).setOnClickListener(v -> {
     Game game = null;
     try {
         if (gameViewModel != null) {
             game = gameViewModel.getGame();
         }
     } catch (Exception e) {
         Log.e(CodeX.tag, "Error getting game: " + e.getMessage(), e);
     }

     dialogBack(game);
 });
     getOnBackPressedDispatcher().addCallback(this, new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                Game game = null;
                try {
                    if (gameViewModel != null) {
                        game = gameViewModel.getGame();
                    }
                } catch (Exception e) {
                    Log.e(CodeX.tag, "Error getting game: " + e.getMessage(), e);
                }

                dialogBack(game);
            }
        });
    }

    private SoundPool soundPool;

    private void startSound() {
        AudioAttributes audioAttributes = new AudioAttributes.Builder()
                .setUsage(AudioAttributes.USAGE_GAME)
                .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                .build();
        soundPool = new SoundPool.Builder().
                setMaxStreams(10).
                setAudioAttributes(audioAttributes)
                .build();

        setVolumeControlStream(AudioManager.STREAM_MUSIC);
        soundIds = new int[10];
        soundIds[0] = soundPool.load(this, R.raw.b_sound, 1);
        soundIds[1] = soundPool.load(this, R.raw.answered, 1);
        soundIds[2] = soundPool.load(this, R.raw.k_unanswered, 1);
        soundIds[3] = soundPool.load(this, R.raw.k_win, 1);
        soundIds[4] = soundPool.load(this, R.raw.k_loose, 1);
        soundIds[5] = soundPool.load(this, R.raw.k_game_over, 1);
        soundIds[6] = soundPool.load(this, R.raw.k_keys1, 1);
        soundIds[7] = soundPool.load(this, R.raw.zoom_in, 1);
        soundIds[8] = soundPool.load(this, R.raw.zoom_out, 1);
        if (gameSettings.getBoolean("b_sound", false)) {
            new Handler(Looper.getMainLooper()).postDelayed(() -> bSoundStreamId = soundPool.play(soundIds[0], 1, 1, 2, -1, 1.0f), 2000);

        }
        // loop 0 no loop; 1 loop forever

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
        findViewById(R.id.zoom_in).setOnClickListener(this::zoomIn);
        findViewById(R.id.zoom_out).setOnClickListener(this::zoomOut);
    }

    protected GameShare getCurrentGame(String tspKey,String gameKey){
         return  openGame(tspKey, gameKey);
    }
//    protected GameShare getBestGame(String tspKey,String gameKey){
//        return  openGame(tspKey, gameKey);
//    }
    protected void dialogResume() {
        if (dialog == null) dialog = new StyleDialog(this);
        dialog.setContentView(R.layout.pop_progress);
        ProgressBar progressBar= dialog.findViewById(R.id.progressBar);
        dialog.show();
        dialog.setCancelable(false);

        taskManager.runTask(() -> {
            GameShare gameShare = getCurrentGame(CodeX.tspKey, CodeX.gameKey);
           progressBar.setIndeterminate(false);
           progressBar.setProgress(50);
            if (bestGame != null) {
                if (bestGame.getGame() == null || bestGame.getTsp() == null)
                    bestGame = openGame(CodeX.bestTspKey, CodeX.bestGameKey);
                Log.d(CodeX.tag, "Step openGame loaded");
                progressBar.setProgress(75);
            } else bestGame = openGame(CodeX.bestTspKey, CodeX.bestGameKey);
            Log.d(CodeX.tag, "Step openGame 2 loaded");
            progressBar.setProgress(80);
            if(dialog!=null) {
                if (dialog.isShowing()) {
                    progressBar.setProgress(99);
                    runOnUiThread(() -> dialogResume(gameShare.getTsp(), gameShare.getGame()));
                    Log.d(CodeX.tag, "Step openGame loaded");
                } else   Log.e(CodeX.tag, "Step process interrupted loading 1... Dialog not found");

            }else   Log.e(CodeX.tag, "Step process interrupted loading 2... Dialog not found");

        });
    }


    private void dialogBack( Game game ) {
        if (dialog == null) {
            dialog = new StyleDialog(this);
        }
           if(game!=null) {
               pauseTimer();
           }
        dialog.setContentView(R.layout.pop_game_end);
        ((TextView) dialog.findViewById(R.id.title)).setText(getString(R.string.exit_game_msg));
        ((TextView) dialog.findViewById(R.id.msg)).setText(getString(R.string.exit_game_msg_));
        ((TextView) dialog.findViewById(R.id.try_again)).setText(R.string.yes);
        ((TextView) dialog.findViewById(R.id.return_btn)).setText(R.string.no);

        dialog.findViewById(R.id.return_btn).setOnClickListener(v -> {
            dialog.cancel();
            if(game!=null) resumeTimer(game);
        });
        dialog.findViewById(R.id.share_img_btn).setVisibility(View.GONE);
        dialog.findViewById(R.id.try_again).setOnClickListener(v -> {
            dialog.cancel();
            if(taskManager!=null) taskManager.shutdownNow();
            super.finish();
        });
        dialog.setCancelable(false);
        dialog.show();

    }
    private void dialogBack(Tsp tsp, Game game ) {
        if (dialog == null) dialog = new StyleDialog(this);
        if(game!=null) pauseTimer();
        dialog.setContentView(R.layout.pop_game_end);
        ((TextView) dialog.findViewById(R.id.title)).setText(getString(R.string.exit_game_msg));
        ((TextView) dialog.findViewById(R.id.msg)).setText(getString(R.string.exit_game_msg_));
        ((TextView) dialog.findViewById(R.id.try_again)).setText(R.string.yes);
        ((TextView) dialog.findViewById(R.id.return_btn)).setText(R.string.no);

        dialog.findViewById(R.id.return_btn).setOnClickListener(v -> dialogResume(tsp,game));
        dialog.findViewById(R.id.share_img_btn).setVisibility(View.GONE);
        dialog.findViewById(R.id.try_again).setOnClickListener(v -> {
            dialog.cancel();
            if(taskManager!=null) taskManager.shutdownNow();
            super.finish();
        });
        dialog.setCancelable(false);
        dialog.show();

    }
    private void dialogSharing(Tsp tsp) {
         StyleDialog dialogS = new StyleDialog(this);
      dialogS.setContentView(R.layout.pop_progress);
      dialogS.show();
      dialogS.setCancelable(false);
      taskManager.runTask(() -> {
          Gson gson = new Gson();
          Location gs= new Location(CityInfo.getCitiesInfoNames(tsp.getCities()), tsp.getPointXY());
          String text = gson.toJson(gs, Location.class);
          Intent sendIntent = new Intent();
          sendIntent.setAction(Intent.ACTION_SEND);
          sendIntent.setType("text/*");
          sendIntent.putExtra(Intent.EXTRA_TEXT, text);
          sendIntent.putExtra(Intent.EXTRA_TITLE, "Share Game");
          if(dialogS.isShowing())
              runOnUiThread(() -> {dialogS.cancel();
                  startActivity(Intent.createChooser(sendIntent, "Share with"));
              });
      });


  }
    private void dialogMenu() {
        pauseTimer();
        if (dialog == null) dialog = new StyleDialog(this);
        dialog.setContentView(R.layout.pop_progress);
        dialog.show();
//
        if (bestGame != null)
            if (bestGame.getGame() != null && bestGame.getTsp() != null) {
                dialogMenu(bestGame.getTsp(), bestGame.getGame());
                return;
            }

        dialog.setOnCancelListener(null);
        taskManager.runTask(() -> {
            bestGame = openGame(CodeX.bestTspKey, CodeX.bestGameKey);
            if(dialog!=null)
                if(dialog.isShowing())
            runOnUiThread(() -> dialogMenu(bestGame.getTsp(), bestGame.getGame()));
        });


    }
    protected void saveGave(String tspKey, String gameKey,
                            SharedPreferences.Editor editor, String jsonTsp, String jsonGame) {
        editor.putString(gameKey, jsonGame);
        editor.putString(tspKey, jsonTsp);
        editor.apply();
    }

    protected void saveGave(Tsp tsp, Game game) {
        taskManager.runTask(() -> {
            SharedPreferences.Editor editor = gameSettings.edit();
            Gson gson = new Gson();
            String jsonTsp = gson.toJson(tsp);
            String jsonGame = gson.toJson(game);
            Log.d(CodeX.tag, (tsp != null) + " is saving " + (game != null));
            saveGave(CodeX.tspKey, CodeX.gameKey, editor,  jsonTsp, jsonGame );
            Log.d(CodeX.tag,   " Game fully saved " );
            });
    }


    private void addOption(Button b, int i) {
        b.setOnClickListener(v -> {
            optionsContainer.removeView(b);
            addAnswer(b, i);
            Game game = gameViewModel.getGameLiveData().getValue();

            assert game != null;
            game.addDirection(i);

            gameViewModel.setGameLiveData(game);
            // plotGame.plotPath(game.getDirection());
            if (optionsContainer.getChildCount() == 0L) {
                finishGame();
            }
            if (gameSettings.getBoolean("k_sound", true))
                soundPool.play(soundIds[1], 1, 1, 1, 0, 1.0f);
        });
        optionsContainer.addView(b);
    }

    private void addAnswer(Button b, int i) {
        b.setOnClickListener(v -> {
            answersContainer.removeView(b);
            addOption(b, i);
            Game game = gameViewModel.getGameLiveData().getValue();
            assert game != null;
            game.removeDirection(i);
            gameViewModel.setGameLiveData(game);
            // plotGame.plotPath(game.getDirection());
            if (gameSettings.getBoolean("k_sound", true))
                soundPool.play(soundIds[2], 1, 1, 1, 0, 1.0f);

        });
        answersContainer.addView(b);
    }


    private void viewModelListener() {
        gameViewModel.getTspLiveData().observe(this, tsp -> {
            Game game = gameViewModel.getGame();
            if (tsp != null && game != null) {
                answersContainer.removeAllViews();
                Log.d(CodeX.tag, " getTspLiveData().observe  Resume timer ");
                resumeTimer(game);
                level.setText(String.valueOf(game.getLevel()));
                scores.setText(String.valueOf(game.getScores()));
                time.setText(Util.timeDisplay(game.getUsedTime()));
                if (tsp.getTspActions() == TspCode.SOLVED) {
                    final int size = tsp.getCities().size() + 1;
                    int[] playId = new int[size];
                    playId[0] = plotGame.getId();

                    int i = 0;
                    for (CityInfo city : tsp.getCities()) {

                        Button b = new Button(GameActivity.this);
                        b.setId(i);
                        playId[i] = i;
                        b.setText(city.getName());
                        b.setBackgroundResource(R.drawable.btn_b );
                        b.setTextColor(getResources().getColor(R.color.white));
                        if (game.getDirection().contains(i))
                            addAnswer(b, i);
                        else
                            addOption(b, i);
                        //  Log.d(CodeX.tag," i =="+i+"  "+city);
                        i++;
                    }
                    playGroup.setReferencedIds(playId);
                    plotGame.plotGame(tsp.getPointXY(), tsp.getCities());
                    plotGame.plotPath(game.getDirection());
                    if (optionsContainer.getChildCount() < 1) finishGame();

                }
            } else {
                Snackbar.make(plotGame, "Inputs empty start a new problem", Snackbar.LENGTH_LONG).show();
                if (game == null) Log.e(CodeX.tag, " game null");
                if (tsp == null) Log.e(CodeX.tag, " Tsp null");
            }
        });
        gameViewModel.getUpdateCodeLiveData().observe(this, errorCode -> Snackbar.make(plotGame, "Error " + errorCode.toString(), Snackbar.LENGTH_LONG).setAction("Action", null).show());
        gameViewModel.getProgressXLiveData().observe(this, progressX -> Log.d("game", progressX.toString()));
        gameViewModel.getGameLiveData().observe(this, game -> {

            if (game != null) {
                plotGame.plotPath(game.getDirection());
            }

        });
        plotGame.setOnPointListener(this);
        menu.setOnClickListener(v -> dialogMenu());
    }

    private void finishGame() {
        pauseTimer();
        Tsp tsp = gameViewModel.getTspLiveData().getValue();
        Game game = gameViewModel.getGameLiveData().getValue();
        if (tsp != null) {
            if (game != null) {
                String result = game.getResult(tsp, this);

                if (Util.formDouble(tsp.getCost()) >=
                        Util.formDouble(game.getCost())) {
                    dialogWin(game, result, tsp);

                } else {
                    dialogLose(game, result);
                }
            }
        }
    }

    private void dialogWin(Game game, String result, Tsp tsp) {
        if (dialog == null) dialog = new StyleDialog(this);
        dialog.setContentView(R.layout.pop_game_end);
        ((TextView) dialog.findViewById(R.id.title)).setText(getString(R.string.win));
        ((TextView) dialog.findViewById(R.id.msg)).setText(result);
        ((TextView) dialog.findViewById(R.id.try_again)).setText(R.string.next);
       // game.win(tsp.getCost() > game.getCost(), System.currentTimeMillis() - startTime);
      //  gameViewModel.setGameLiveData(game);
        level.setText(String.valueOf(game.getLevel()+1));
        scores.setText(String.valueOf(game.getWinScores(tsp.getCost() > game.getCost(),
                System.currentTimeMillis() - startTime)));
        dialog.findViewById(R.id.return_btn).setOnClickListener(v -> {
            dialog.cancel();
            activatePlay_Win_lose(game, result, tsp);
            if (gameSettings.getBoolean("k_sound", true))
                soundPool.play(soundIds[6], 1, 1, 1, 0, 1.0f);
        });
        dialog.findViewById(R.id.share_img_btn).setOnClickListener(v -> share( ));
        dialog.findViewById(R.id.try_again).setOnClickListener(v -> {
            dialog.cancel();
            next(tsp.getCost() > game.getCost(),
                    System.currentTimeMillis() - startTime,tsp, game);
            if (gameSettings.getBoolean("k_sound", true))
                soundPool.play(soundIds[6], 1, 1, 1, 0, 1.0f);


        });
        dialog.setCancelable(false);
        dialog.show();
        if (gameSettings.getBoolean("k_sound", true))
            soundPool.play(soundIds[3], 1, 1, 1, 0, 1.0f);


        setBest(game, tsp);
    }


    protected void next( boolean b, long l,Tsp tsp, Game game) {
        game.next(b,l);
        gameViewModel.next(game);

    }

    protected void setBest(Game game, Tsp tsp) {
        Log.d(CodeX.tag, "Incoming  L=" + game.getLevel() + "  S=" + game.getScores()
                + " Time " + game.getGameLifeTime());
        SharedPreferences.Editor editor = gameSettings.edit();
        taskManager.runTask(() -> {

            Gson gson = new Gson();
            String jsonTsp = gameSettings.getString("b_tsp", null);
            String jsonGame = gameSettings.getString("b_game", null);
            if (jsonGame != null && jsonTsp != null) {
                if (bestGame == null)
                    bestGame = openGame(CodeX.bestTspKey, CodeX.bestGameKey);

                if (bestGame.getGame() != null && bestGame.getTsp() != null) {
                    compareBest(bestGame.getGame(), game, tsp, editor, gson);
                    return;
                }


            }
            editBest(game, tsp, editor, gson);

        });


    }

    private void compareBest(Game gameStored, Game game, Tsp tsp, SharedPreferences.Editor editor, Gson gson) {

        long level = gameStored.getLevel();
        long scores = gameStored.getScores();
        long time = gameStored.getGameLifeTime();

        Log.d(CodeX.tag, "Stored  L=" + level + "  S=" + scores
                + " Time " + time);
        if (game.getLevel() > level)
            editBest(game, tsp, editor, gson);
        else if (game.getLevel() == level)
            if (game.getScores() > scores)
                editBest(game, tsp, editor, gson);
            else if (game.getScores() == scores)
                if (game.getGameLifeTime() < time)
                    editBest(game, tsp, editor, gson);

    }

    private void editBest(Game game, Tsp tsp, SharedPreferences.Editor editor, Gson gson) {

        String jsonTsp = gson.toJson(tsp);
        String jsonGame = gson.toJson(game);
        editor.putString(CodeX.bestGameKey, jsonGame);
        editor.putString(CodeX.bestTspKey, jsonTsp);
        editor.apply();
        editor.commit();
        bestGame = new GameShare(tsp, game);
        Snackbar.make(plotGame, getString(R.string.b_played),
                Snackbar.LENGTH_LONG).setAction(R.string.share
                , v -> share()).show();
    }

    private void dialogLose(Game game, String result) {
        if (game.getTryAgain() > 3) {
            dialogGameOver(game);
            return;
        }
        if (dialog == null) dialog = new StyleDialog(this);
        dialog.setContentView(R.layout.pop_game_end);
        dialog.findViewById(R.id.share_img_btn).setOnClickListener(v -> share( ));
        ((TextView) dialog.findViewById(R.id.msg)).setText(result);

        dialog.findViewById(R.id.return_btn).setOnClickListener(v -> {
            dialog.cancel();
            activatePlay_Win_lose(game, result, null);
            if (gameSettings.getBoolean("k_sound", true))
                soundPool.play(soundIds[6], 1, 1, 1, 0, 1.0f);
        });
        dialog.findViewById(R.id.try_again).setOnClickListener(v -> {
            dialog.cancel();
            game.tryAgain();
            gameViewModel.tryAgain(game, gameSettings.getBoolean("timing", false)||gameSettings.getBoolean("refresh_points", false));

             if (gameSettings.getBoolean("k_sound", true))
                soundPool.play(soundIds[6], 1, 1, 1, 0, 1.0f);

        });
        dialog.setCancelable(false);
        dialog.show();
        if (gameSettings.getBoolean("k_sound", true))
            soundPool.play(soundIds[4], 1, 1, 1, 0, 1.0f);
    }

    private void playActive(View v, boolean b) {
        if (v == null) v = findViewById(R.id.btn_play);
        if (b) {
            v.setVisibility(View.VISIBLE);
            optionsContainer.setVisibility(View.INVISIBLE);
            answersContainer.setVisibility(View.INVISIBLE);
            plotGame.setEnabled(false);
        } else {
            v.setVisibility(View.GONE);
            optionsContainer.setVisibility(View.VISIBLE);
            answersContainer.setVisibility(View.VISIBLE);
            plotGame.setEnabled(true);
        }
    }

    private void activatePlay_Win_lose(Game game, String result, Tsp tsp) {
        Button b = findViewById(R.id.btn_play);
        playActive(b, true);
        b.setOnClickListener(v -> {
            playActive(v, false);
            if (tsp == null) {
                if (result == null) {
                    if (game != null) {
                        if (gameViewModel.getGameLiveData().getValue() == null)
                            selectNewGame();
                    }
                } else {
                    dialogLose(game, result);
                }
            } else {
                dialogWin(game, result, tsp);
            }


        });
    }


    private void activatePlayResume(Game game, Tsp tsp) {
        Button b = findViewById(R.id.btn_play);
        playActive(b, true);
        b.setOnClickListener(v -> {
            playActive(v, false);
            if (game == null && tsp == null) dialogResume();
            dialogResume(tsp, game);
        });


    }

    @NonNull
    private GameShare openGame(String tspKey, String gameKey) {
        Gson gson = new Gson();
        String jsonTsp = gameSettings.getString(tspKey, null);
        String jsonGame = gameSettings.getString(gameKey, null);
        if (jsonGame != null && jsonTsp != null) {
            try {
                return new GameShare(gson.fromJson(jsonTsp, Tsp.class), gson.fromJson(jsonGame, Game.class));
            }catch (Exception e){
                Log.e(CodeX.tag, "Error opening game: " + e.getMessage(), e);
              resetGame();
            }
        }
        return new GameShare(null, null);
    }

    private void resetGame() {
        SharedPreferences.Editor editor = gameSettings.edit();
        editor.putString(CodeX.bestGameKey, null);
        editor.putString(CodeX.bestTspKey, null);
        editor.putString(CodeX.tspKey, null);
        editor.putString(CodeX.gameKey,null);
        editor.apply();
        editor.commit();
        bestGame = null;
        Toast.makeText(GameActivity.this,
                "We are sorry to reset your scores(It won't happen again in the future)",
                Toast.LENGTH_LONG).show();
    }


    private void dialogGameOver(Game game) {
        if (dialog == null) dialog = new StyleDialog(this);
        dialog.setContentView(R.layout.pop_game_ended);
        ((TextView) dialog.findViewById(R.id.title)).setText(getString(R.string.game_over));
        ((TextView) dialog.findViewById(R.id.level)).setText(String.valueOf(game.getLevel()));
        ((TextView) dialog.findViewById(R.id.scores)).setText(String.valueOf(game.getLevel()));
        ((TextView) dialog.findViewById(R.id.time)).setText(Util.timeDisplay(game.getGameLifeTime()));
        ((TextView) dialog.findViewById(R.id.try_again)).setText(getString(R.string.new_));
        dialog.findViewById(R.id.return_btn).setOnClickListener(v -> {
            dialog.cancel();
            activatePlay_Win_lose(game, null, null);

            if (gameSettings.getBoolean("k_sound", true))
                soundPool.play(soundIds[6], 1, 1, 1, 0, 1.0f);
        });
        dialog.show();
        dialog.findViewById(R.id.share_img_btn).setOnClickListener(v -> share( ));
        dialog.findViewById(R.id.try_again).setOnClickListener(v -> {
            dialog.cancel();
            selectNewGame();

            if (gameSettings.getBoolean("k_sound", true))
                soundPool.play(soundIds[6], 1, 1, 1, 0, 1.0f);
        });
        dialog.setCancelable(false);
        dialog.show();
        if (gameSettings.getBoolean("k_sound", true))
            soundPool.play(soundIds[5], 1, 1, 1, 0, 1.0f);
    }

    private void dialogResume(Tsp tsp, Game game) {
        if (dialog == null) dialog = new StyleDialog(this);
        dialog.setContentView(R.layout.pop_game_ended);
        dialog.setCancelable(true);
        dialog.setCanceledOnTouchOutside(false);
        dialog.findViewById(R.id.share_img_btn).setOnClickListener(v -> share( ));
        dialog.setOnCancelListener(dialog -> dialogBack(tsp,game));
        ((TextView) dialog.findViewById(R.id.title)).setText(getString(R.string.welcome));
        if (game != null && tsp != null) {
            ((TextView) dialog.findViewById(R.id.level)).setText(String.valueOf(game.getLevel()));
            ((TextView) dialog.findViewById(R.id.scores)).setText(String.valueOf(game.getScores()));
            ((TextView) dialog.findViewById(R.id.time)).setText(Util.timeDisplay(game.getGameLifeTime()));

            ((TextView) dialog.findViewById(R.id.try_again)).setText(R.string.resume);
            ((TextView) dialog.findViewById(R.id.return_btn)).setText(R.string.new_);

            dialog.findViewById(R.id.return_btn).setOnClickListener(v -> {
                dialog.setOnCancelListener(null);
                dialog.cancel();
                findViewById(R.id.btn_play).setVisibility(View.GONE);
                selectNewGame();
                if (gameSettings.getBoolean("k_sound", true))
                    soundPool.play(soundIds[6], 1, 1, 1, 0, 1.0f);


            });
            dialog.findViewById(R.id.try_again).setOnClickListener(v -> {
                findViewById(R.id.btn_play).setVisibility(View.GONE);
                dialog.setOnCancelListener(null);
                dialog.cancel();
                if(tsp.getCities().size()==tsp.getPointXY().size())
                gameViewModel.resumeGame(tsp, game);
                else{
                    Log.e(CodeX.tag, "Game is corrupt; cities not equal points " );
                    Log.d(CodeX.tag,  tsp.toString());
                    Toast.makeText(GameActivity.this,
                             "Game is corrupt; cities not equal points",
                            Toast.LENGTH_LONG).show();
                    dialog.setOnCancelListener(null);
                    dialog.cancel();
                    findViewById(R.id.btn_play).setVisibility(View.GONE);
                    selectNewGame();
                    if (gameSettings.getBoolean("k_sound", true))
                        soundPool.play(soundIds[6], 1, 1, 1, 0, 1.0f);


                }
                if (gameSettings.getBoolean("k_sound", true))
                    soundPool.play(soundIds[6], 1, 1, 1, 0, 1.0f);
            });
        } else {
            ((TextView) dialog.findViewById(R.id.try_again)).setText(R.string.new_);
            dialog.findViewById(R.id.try_again).setOnClickListener(v -> {
                dialog.setOnCancelListener(null);
                dialog.cancel();
                findViewById(R.id.btn_play).setVisibility(View.GONE);
                selectNewGame();

                if (gameSettings.getBoolean("k_sound", true))
                    soundPool.play(soundIds[6], 1, 1, 1, 0, 1.0f);

            });
            dialog.findViewById(R.id.return_btn).setOnClickListener(v -> {
                dialog.cancel();
                activatePlayResume(game, tsp);

            });
        }


        dialog.show();
        dialog.setCanceledOnTouchOutside(false);

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (soundPool != null) soundPool.release();
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d(CodeX.tag, "onResume()" );
        if (gameViewModel.getGameLiveData().getValue() == null) {
            dialogResume();
        } else {
            if (dialog != null) {
                if (dialog.isShowing()) {
                    Log.d(CodeX.tag, " Dialog is showing" );
                    return;

                }
            }
            if(countDownTimer==null&&countUPTimer==null){
                Log.d(CodeX.tag, " Resume timer in onResume");
                resumeTimer(gameViewModel.getGameLiveData().getValue());
            }
        }
    }


    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onPause() {
        Log.d(CodeX.tag, "onPause()" );
        pauseTimer();
        super.onPause();


    }

    @Override
    public void onPoint(int index) {
        Button b = optionsContainer.findViewById(index);
        if (b == null) {
            b = answersContainer.findViewById(index);
        }
        if (b != null) {
            b.callOnClick();
        }
       else {
            Log.e(CodeX.tag, "Index  button error " + index);
        }
    }


    private void pauseTimer() {
        stopTime();
        Game game = gameViewModel.getGame();
        Tsp tsp = gameViewModel.getTsp();
        if (game != null && tsp != null) {
            game.pause(System.currentTimeMillis() - startTime);
            gameViewModel.setGameLiveData(game);
            saveGave(tsp, game);
        } else {
            Log.e(CodeX.tag, "Null Game or Tsp Cannot pause or save");
        }

    }

    private void stopTime() {
        if (countUPTimer != null) countUPTimer.cancel();
        if (countDownTimer != null) countDownTimer.cancel();
        countUPTimer= null;
        countDownTimer = null;
    }


    private void resumeTimer(Game game) {
        stopTime();
        Log.d(CodeX.tag, "  resume ... ");
        startTime = System.currentTimeMillis();
        if (gameSettings.getBoolean("timing", false)) {
            Log.d(CodeX.tag, game.getTiming() + " timing, used time" + game.getUsedTime());

            countUPTimer = null;
            countDownTimer = new CountDownTimer(game.getTiming() - game.getUsedTime(), 1000) {
                @Override
                public void onTick(long millisUntilFinished) {
                   // Log.d(CodeX.tag, " time change " + S.timeDisplay(millisUntilFinished));
                    time.setText(Util.timeDisplay(millisUntilFinished));
                }

                @Override
                public void onFinish() {
                    optionsContainer.removeAllViews();
                    answersContainer.removeAllViews();
                    dialogLose(game, getString(R.string.time_up_msg));

                }
            };
            countDownTimer.start();
        } else {
            countDownTimer = null;
            countUPTimer = new CountUpTimer(1000 * 60 * 60,
                    1000, game.getUsedTime()) {
                @Override
                public void onTickDown(long timeCount) {
                   // Log.d(CodeX.tag, "  count up output ... " + timeCount);
                    time.setText(Util.timeDisplay(timeCount));
                }

                @Override
                public void onFinish() {
                    dialogLose(game, "Sorry you have take too much time to figure out the solution try again");
                    optionsContainer.removeAllViews();
                    answersContainer.removeAllViews();
                }
            };
            countUPTimer.start();

        }
    }

    private void setAnimations() {
        ((AnimationDrawable) level.getBackground()).start();
        ((AnimationDrawable) scores.getBackground()).start();
        ((AnimationDrawable) time.getBackground()).start();
    }

    private void dialogMenu(Tsp tsp, Game game) {
        if (dialog == null) dialog = new StyleDialog(this);
        dialog.setContentView(R.layout.pop_game_menu);
        SharedPreferences.Editor editor = gameSettings.edit();
        SwitchCompat timing = dialog.findViewById(R.id.timing);
        SwitchCompat kSound = dialog.findViewById(R.id.k_sound);
        SwitchCompat bSound = dialog.findViewById(R.id.b_sound);
        SwitchCompat refreshingPoints = dialog.findViewById(R.id.refresh_points);
        SwitchCompat keys = dialog.findViewById(R.id.keys);
        timing.setChecked(gameSettings.getBoolean("timing", false));
        kSound.setChecked(gameSettings.getBoolean("k_sound", true));
        bSound.setChecked(gameSettings.getBoolean("b_sound", false));
        refreshingPoints.setChecked(gameSettings.getBoolean("refresh_points", true));
        keys.setChecked(gameSettings.getBoolean("keys", true));

        if (game != null && tsp != null) {
            ((TextView) dialog.findViewById(R.id.level)).setText(String.valueOf(game.getLevel()));
            ((TextView) dialog.findViewById(R.id.scores)).setText(String.valueOf(game.getScores()));
            ((TextView) dialog.findViewById(R.id.time)).setText(Util.timeDisplay(game.getGameLifeTime()));

            dialog.findViewById(R.id.share_img_btn).setOnClickListener(v -> share( ));
        }
        timing.setOnCheckedChangeListener((buttonView, isChecked) -> {
            editor.putBoolean("timing", isChecked);
            editor.apply();
            if (gameSettings.getBoolean("k_sound", true))
                soundPool.play(soundIds[6], 1, 1, 1, 0, 1.0f);
        });
        kSound.setOnCheckedChangeListener((buttonView, isChecked) -> {
            editor.putBoolean("k_sound", isChecked);
            editor.apply();
            if (gameSettings.getBoolean("k_sound", true))
                soundPool.play(soundIds[6], 1, 1, 1, 0, 1.0f);
        });
        bSound.setOnCheckedChangeListener((buttonView, isChecked) -> {
            editor.putBoolean("b_sound", isChecked);
            editor.apply();
            if (isChecked)
                if (bSoundStreamId == -1)
                    bSoundStreamId = soundPool.play(soundIds[0], 1, 1, 2, -1, 1.0f);
                else soundPool.resume(bSoundStreamId);
            else soundPool.pause(bSoundStreamId);

            if (gameSettings.getBoolean("k_sound", true))
                soundPool.play(soundIds[6], 1, 1, 1, 0, 1.0f);
        });
        refreshingPoints.setOnCheckedChangeListener((buttonView, isChecked) -> {
            editor.putBoolean("refresh_points", isChecked);
            editor.apply();
            if (gameSettings.getBoolean("k_sound", true))
                soundPool.play(soundIds[6], 1, 1, 1, 0, 1.0f);
        });
        keys.setOnCheckedChangeListener((buttonView, isChecked) -> {
            editor.putBoolean("keys", isChecked);
            editor.apply();
            setKeys(isChecked);
            if (gameSettings.getBoolean("k_sound", true))
                soundPool.play(soundIds[6], 1, 1, 1, 0, 1.0f);
        });
        dialog.findViewById(R.id.return_btn).setOnClickListener(v -> {
            dialog.cancel();
            if (gameSettings.getBoolean("k_sound", true))
                soundPool.play(soundIds[6], 1, 1, 1, 0, 1.0f);
            if (gameViewModel.getGameLiveData().getValue() != null) {
                Log.d(CodeX.tag, " resume Timer  in cancel menu" + plotGame.getZoom());
                resumeTimer(gameViewModel.getGameLiveData().getValue());
            }
        });
        dialog.show();
    }

    public void zoomIn(View v) {
        plotGame.zoomIn();
        Log.d(CodeX.tag, " Zoom In -------- zoom is " + plotGame.getZoom());
        if (gameSettings.getBoolean("k_sound", true))
            soundPool.play(soundIds[7], 1, 1, 1, 0, 1.0f);
    }

    public void zoomOut(View v) {
        plotGame.zoomOut();
        Log.d(CodeX.tag, " Zoom out -------- zoom is " + plotGame.getZoom());
        if (gameSettings.getBoolean("k_sound", true))
            soundPool.play(soundIds[8], 1, 1, 1, 0, 1.0f);
    }


    private void setKeys(boolean on) {
        if (on) {
            optionsContainer.setVisibility(View.VISIBLE);
            answersContainer.setVisibility(View.VISIBLE);
        } else {

            optionsContainer.setVisibility(View.GONE);
            answersContainer.setVisibility(View.GONE);
        }
    }

    public void postShareGame(Location share) {
        gameViewModel.startGameShare(share);
    }



    abstract static class CountUpTimer extends CountDownTimer {
        private final long duration;
        private final long usedTime;

        public CountUpTimer(long millisInFuture,
                            long countDownInterval, long usedTime) {
            super(millisInFuture - usedTime, countDownInterval);
            this.duration = millisInFuture - usedTime;
            this.usedTime = usedTime;
        }

        @Override
        public void onTick(long millisUntilFinished) {
            onTickDown((duration - millisUntilFinished) + usedTime);
        }

        public abstract void onTickDown(long timeCountMillis);

    }

    protected void selectNewGame() {
        if (dialog == null) dialog = new StyleDialog(this);
        dialog.setContentView(R.layout.pop_progress);
        dialog.show();

        if (bestGame != null)
            if (bestGame.getGame() != null && bestGame.getTsp() != null) {
                selectNewGame(dialog, bestGame.getGame());
                return;
            }
        dialog.setOnCancelListener(null);
        taskManager.runTask(() -> {
            bestGame = openGame(CodeX.bestTspKey, CodeX.bestGameKey);
            if(dialog!=null)if(dialog.isShowing())
            runOnUiThread(() -> selectNewGame(dialog, bestGame.getGame()));
        });
    }

    protected void selectNewGame(StyleDialog dialog, Game highestGame) {
        dialog.setContentView(R.layout.pop_game_resume);
        if (highestGame != null) {
            ((TextView) dialog.findViewById(R.id.level)).setText(String.valueOf(highestGame.getLevel()));
            ((TextView) dialog.findViewById(R.id.scores)).setText(String.valueOf(highestGame.getScores()));
            ((TextView) dialog.findViewById(R.id.time)).setText(Util.timeDisplay(highestGame.getGameLifeTime()));

            dialog.findViewById(R.id.return_btn).setOnClickListener(v -> {
                dialog.cancel();
                activatePlayResume(null, null);
            });
            dialog.show();
            dialog.setCancelable(false);
            dialog.show();
            RecyclerView recycler = dialog.findViewById(R.id.levels);
            String name = "Level";
            LevelAdapter levelAdapter = new LevelAdapter(this, name);
            recycler.setAdapter(levelAdapter);


            DisplayMetrics dm = new DisplayMetrics();
            getWindowManager().getDefaultDisplay().getMetrics(dm);


            int spanCount = dm.densityDpi / 70;
            GridLayoutManager gridLayoutManager = new GridLayoutManager(this, spanCount);
            List<Integer> l = new ArrayList<>();
            for (int i = 0; i < (Math.max(highestGame.getNodes(), 20)); i++)
                l.add(i);

            recycler.setLayoutManager(gridLayoutManager);
            levelAdapter.changeSize(highestGame.getLevel(), l);
            levelAdapter.setPointClickListener(i -> {
                gameViewModel.newGame(i);
                playActive(null, false);
                dialog.cancel();
            });
        } else {
            dialog.cancel();
            gameViewModel.newGame(0);
        }


    }
    private void share( ) {
        final CharSequence[] options = {
                getString(R.string.highestStageGame),
                getString(R.string.currentStageGame),
                getString(R.string.cancel)};
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(getString(R.string.share));
        builder.setItems(options, (dialog, item) -> {
            if (options[item].equals(getString(R.string.highestStageGame))) {
                Tsp tsp = null;
                if(bestGame!=null) tsp=bestGame.getTsp();
                if(tsp!=null) {
                    dialogSharing(tsp);
                    dialog.dismiss();
                } else{
                    Toast.makeText(GameActivity.this,
                            getText(R.string.unable_to_share),Toast.LENGTH_LONG).show();
                }
                 dialog.dismiss();
            } else if (options[item].equals(getString(R.string.currentStageGame))) {
              Tsp tsp = gameViewModel.getTsp();
              if(tsp!=null) {
                  dialogSharing(tsp);
                  dialog.dismiss();
              } else{
                  Toast.makeText(GameActivity.this,
                          getText(R.string.unable_to_share),Toast.LENGTH_LONG).show();
              }
            } else if (options[item].equals(getString(R.string.cancel))) {
                dialog.dismiss();
            }
        });
        builder.show();

    }
}



