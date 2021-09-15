package softcare.game;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.drawable.AnimationDrawable;
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Looper;
import android.preference.PreferenceManager;
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
import softcare.util.S;

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
    private SharedPreferences gameSettings;
    private int[] soundIds;
    private int bSoundStreamId;
    private CountUp countUPTimer;
    private CountDownTimer countDownTimer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
        gameSettings = getSharedPreferences("game_settings",
                Activity.MODE_PRIVATE);
        _init();
        viewModelListener();
        if (gameViewModel.getGame().getValue() == null) dialogResume(readStoredGame());
        setAnimations();
        startSound();
    }

    private SoundPool soundPool;

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (soundPool != null) soundPool.release();
    }

    private void startSound() {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            AudioAttributes audioAttributes = new AudioAttributes.Builder()
                    .setUsage(AudioAttributes.USAGE_GAME)
                    .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                    .build();
            soundPool = new SoundPool.Builder().
                    setMaxStreams(10).
                    setAudioAttributes(audioAttributes)
                    .build();
        } else {
            soundPool = new SoundPool(10, AudioManager.STREAM_MUSIC, 0);
        }

        setVolumeControlStream(AudioManager.STREAM_MUSIC);
        //0 background; 1 answered 2 unanswered,  3 win ,
        // 4 loose, 5 game over, 6 button presssed. 7 zoomin 8 zoom 0ut

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
new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
    @Override
    public void run() {
        bSoundStreamId = soundPool.play(soundIds[0], 1, 1, 2, -1, 1.0f);

    }
},2000);

        }
        // looop 0 no loop; 1 loop forever

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
       findViewById(R.id.btn_play).setOnClickListener(v -> {
            v.setVisibility(View.GONE);
           dialogResume(readStoredGame());
        });
    }


    private Game readStoredGame() {

        return new Game(3);
    }

    private void storedGame() {

        SharedPreferences s = getSharedPreferences("game_settings",
                Activity.MODE_PRIVATE);
        SharedPreferences.Editor editor = s.edit();
        editor.apply();
        editor.commit();
    }

    private void addOption(Button b, int i) {
        b.setOnClickListener(v -> {
            optionsContainer.removeView(b);
            addAnswer(b, i);
            Game game = gameViewModel.getGame().getValue();

            game.addDirection(i);

            gameViewModel.setGame(game);
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
            Game game = gameViewModel.getGame().getValue();
            game.removeDirection(i);
            gameViewModel.setGame(game);
            // plotGame.plotPath(game.getDirection());
            if (gameSettings.getBoolean("k_sound", true))
                soundPool.play(soundIds[2], 1, 1, 1, 0, 1.0f);

        });
        answersContainer.addView(b);
    }


    private void viewModelListener() {
        gameViewModel.getTspLiveData().observe(this, tsp -> {
            Game game = gameViewModel.getGame().getValue();
            if (tsp != null && game != null) {

                answersContainer.removeAllViews();
                //  plotGame.setZoom(game.getBound() * 5);
                if (gameSettings.getBoolean("timing", false)) {
                    Log.d(CodeX.tag," timing");
                    countDownTimer = new CountDownTimer(game.getTiming(), 1000) {
                        @Override
                        public void onTick(long millisUntilFinished) {
                            Log.d(CodeX.tag," time change "+S.timeDisplay(millisUntilFinished));
                            time.setText(S.timeDisplay(millisUntilFinished));
                        }

                        @Override
                        public void onFinish() {
                            dialogLoose(game, "You time finish try again pints will be refresh");

                        }
                    };
                    countDownTimer.start();
                } else {
                    countUPTimer = new CountUp(1000 * 60 * 60, 1000) {
                        @Override
                        public void onTick(long millisUntilFinished) {
                            time.setText(S.timeDisplay(millisUntilFinished));
                        }

                        @Override
                        public void onFinish() {
                            dialogLoose(game, "Sorry you have take too much time to figure out the solution try again");

                        }
                    };
        countUPTimer.start();

                }
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
            if (game != null) {
                plotGame.plotPath(game.getDirection());
            }

        });
        plotGame.setOnPointListener(this);
        menu.setOnClickListener(v -> dialogMenu());
    }

    private void finishGame() {
        if(countDownTimer!=null)countDownTimer.cancel();
        if(countUPTimer!=null)countUPTimer.cancel();
        Tsp tsp = gameViewModel.getTspLiveData().getValue();
        Game game = gameViewModel.getGame().getValue();
        if (tsp != null) {
            if (game != null) {
                String result = game.getResult(tsp);
                if (S.formDouble(tsp.getCost()) >= S.formDouble(game.getCost())) {
                    dialogWin(game, result, tsp);
                } else {
                    dialogLoose(game, result);
                }
            }
        }
    }

    private void dialogWin(Game game, String result, Tsp tsp) {
        setBest(game);
        StyleDialog ad = new StyleDialog(this);
        ad.setContentView(R.layout.pop_game_end);
        ((TextView) ad.findViewById(R.id.title)).setText("You are a Winner");
        ((TextView) ad.findViewById(R.id.msg)).setText(result);
        ((TextView) ad.findViewById(R.id.try_again)).setText("Next");

        ad.findViewById(R.id.return_btn).setOnClickListener(v -> {
            ad.cancel();
            activatePlayButton(game, result, tsp);
            if (gameSettings.getBoolean("k_sound", true))
                soundPool.play(soundIds[6], 1, 1, 1, 0, 1.0f);
        });

        ad.findViewById(R.id.try_again).setOnClickListener(v -> {
            ad.cancel();
            game.next(tsp.getCost() > game.getCost(), System.currentTimeMillis() - startTime);
            gameViewModel.next(game);
            if (gameSettings.getBoolean("k_sound", true))
                soundPool.play(soundIds[6], 1, 1, 1, 0, 1.0f);
        });
        ad.setCancelable(false);
        ad.show();
        if (gameSettings.getBoolean("k_sound", true))
            soundPool.play(soundIds[3], 1, 1, 1, 0, 1.0f);

    }

    private void setBest(Game game) {
        SharedPreferences.Editor editor = gameSettings.edit();
        long level = gameSettings.getLong("b_level", 0L);
        long scores = gameSettings.getLong("b_score", 0L);
        long time = gameSettings.getLong("b_time", 0L);
        if (game.getLevel() > level)
            editBest(game, editor);
        else if (game.getLevel() == level)
            if (game.getScores() > scores)
                editBest(game, editor);
            else if (game.getScores() == scores)
                if (game.getTiming() < time)
                    editBest(game, editor);


    }

    private void editBest(Game game, SharedPreferences.Editor editor) {
        editor.putLong("b_level", game.getLevel());
        editor.putLong("b_scores", game.getLevel());
        editor.putLong("b_time", game.getLevel());
        editor.apply();
        editor.commit();
    }

    private void dialogLoose(Game game, String result) {
        if (game.getTryAgain() > 3) {
            dialogGameOver(game);
            return;
        }
        StyleDialog ad = new StyleDialog(this);
        ad.setContentView(R.layout.pop_game_end);
        ((TextView) ad.findViewById(R.id.msg)).setText(result);

        ad.findViewById(R.id.return_btn).setOnClickListener(v -> {
            ad.cancel();
            activatePlayButton(game, result, null);
            if (gameSettings.getBoolean("k_sound", true))
                soundPool.play(soundIds[6], 1, 1, 1, 0, 1.0f);
        });
        ad.findViewById(R.id.try_again).setOnClickListener(v -> {
            ad.cancel();
            game.tryAgain();
            gameViewModel.tryAgain(game, gameSettings.getBoolean("timing", false));
            if (gameSettings.getBoolean("k_sound", true))
                soundPool.play(soundIds[6], 1, 1, 1, 0, 1.0f);

        });
        ad.setCancelable(false);
        ad.show();
        if (gameSettings.getBoolean("k_sound", true))
            soundPool.play(soundIds[4], 1, 1, 1, 0, 1.0f);
    }

    private void activatePlayButton(Game game, String result, Tsp tsp) {
        Button b = findViewById(R.id.btn_play);
        b.setVisibility(View.VISIBLE);
        b.setOnClickListener(v -> {
            b.setVisibility(View.GONE);
            if (tsp == null) {
                if (result == null) {
                    if (game == null) {
                        dialogResume(readStoredGame());
                    } else {
                        if (gameViewModel.getGame().getValue() == null)
                            gameViewModel.onCreateGame(readNewGame());
                    }
                } else   dialogLoose(game, result);
            } else dialogWin(game, result, tsp);
        });

    }

    private void dialogTakeRest(Game game, String result, Tsp tsp) {
        StyleDialog ad = new StyleDialog(this);
        ad.setContentView(R.layout.pop_game_ended);
        ((TextView) ad.findViewById(R.id.title)).setText("Stop Game");
        ((TextView) ad.findViewById(R.id.level)).setText(String.valueOf(game.getLevel()));
        ((TextView) ad.findViewById(R.id.scores)).setText(String.valueOf(game.getLevel()));
        ((TextView) ad.findViewById(R.id.time)).setText(String.valueOf(game.getGameLifeTime()));
        ((TextView) ad.findViewById(R.id.try_again)).setText("Return");
        ((TextView) ad.findViewById(R.id.return_btn)).setText("Exit");

        ad.findViewById(R.id.try_again).setOnClickListener(v -> {
            ad.cancel();
            if (tsp == null) dialogLoose(game, result);
            else dialogWin(game, result, tsp);

        });

        ad.findViewById(R.id.return_btn).setOnClickListener(v -> {
            ad.cancel();
            onBackPressed();
        });
        ad.show();
        ad.setCancelable(false);
    }

    private void dialogGameOver(Game game) {
        StyleDialog ad = new StyleDialog(this);
        ad.setContentView(R.layout.pop_game_ended);
        ((TextView) ad.findViewById(R.id.title)).setText("Game Over");
        ((TextView) ad.findViewById(R.id.level)).setText(String.valueOf(game.getLevel()));
        ((TextView) ad.findViewById(R.id.scores)).setText(String.valueOf(game.getLevel()));
        ((TextView) ad.findViewById(R.id.time)).setText(String.valueOf(game.getGameLifeTime()));
        ((TextView) ad.findViewById(R.id.try_again)).setText("New");
        ad.findViewById(R.id.return_btn).setOnClickListener(v -> {
            ad.cancel();
            activatePlayButton(game, null, null);

            if (gameSettings.getBoolean("k_sound", true))
                soundPool.play(soundIds[6], 1, 1, 1, 0, 1.0f);
        });
        ad.show();
        ad.findViewById(R.id.try_again).setOnClickListener(v -> {
            ad.cancel();
                gameViewModel.onCreateGame(readNewGame());

            if (gameSettings.getBoolean("k_sound", true))
                soundPool.play(soundIds[6], 1, 1, 1, 0, 1.0f);
        });
        ad.setCancelable(false);
        ad.show();
        if (gameSettings.getBoolean("k_sound", true))
            soundPool.play(soundIds[5], 1, 1, 1, 0, 1.0f);
    }

    private void dialogResume(Game game) {

        StyleDialog ad = new StyleDialog(this);
        ad.setContentView(R.layout.pop_game_ended);
        ((TextView) ad.findViewById(R.id.title)).setText("Welcome");
        ((TextView) ad.findViewById(R.id.level)).setText(String.valueOf(game.getLevel()));
        ((TextView) ad.findViewById(R.id.scores)).setText(String.valueOf(game.getLevel()));
        ((TextView) ad.findViewById(R.id.time)).setText(String.valueOf(S.timeDisplay(game.getGameLifeTime())));
        ((TextView) ad.findViewById(R.id.try_again)).setText("Resume");
        ((TextView) ad.findViewById(R.id.return_btn)).setText("New");


        ad.findViewById(R.id.return_btn).setOnClickListener(v -> {
            ad.cancel();
            findViewById(R.id.btn_play).setVisibility(View.GONE);
            if (gameViewModel.getGame().getValue() == null)
                gameViewModel.onCreateGame(readNewGame());
            if (gameSettings.getBoolean("k_sound", true))
                soundPool.play(soundIds[6], 1, 1, 1, 0, 1.0f);

        });
        ad.findViewById(R.id.try_again).setOnClickListener(v -> {
            findViewById(R.id.btn_play).setVisibility(View.GONE);
            ad.cancel();
            if (gameViewModel.getGame().getValue() == null)
                gameViewModel.onCreateGame(readStoredGame());
            if (gameSettings.getBoolean("k_sound", true))
                soundPool.play(soundIds[6], 1, 1, 1, 0, 1.0f);
        });
        ad.show();
        ad.setCanceledOnTouchOutside(false);
    }

    private Game readNewGame() {
        return new Game(1);
    }

    @Override
    protected void onResume() {
        super.onResume();
        startTime = System.currentTimeMillis();
    }


    @Override
    protected void onPause() {
        super.onPause();
        gameViewModel.pause(System.currentTimeMillis() - startTime);
    }

    @Override
    public void onPoint(int index) {
        Button b = optionsContainer.findViewById(index);
        if (b == null) b = answersContainer.findViewById(index);
        //answer(b,index);
        if (b != null) b.performClick();
        Log.d(CodeX.tag, "index " + index);
    }


    private void dialogMenu (){
        pauseGame(startTime);
        StyleDialog ad = new StyleDialog(this);
        ad.setContentView(R.layout.pop_game_menu);
        SharedPreferences.Editor editor = gameSettings.edit();
        Switch timing = ad.findViewById(R.id.timing);
        Switch kSound = ad.findViewById(R.id.k_sound);
        Switch bSound = ad.findViewById(R.id.b_sound);
        Switch refreshingPoints = ad.findViewById(R.id.refresh_points);
        Switch keys = ad.findViewById(R.id.keys);
        timing.setChecked(gameSettings.getBoolean("timing", false));
        kSound.setChecked(gameSettings.getBoolean("k_sound", true));
        bSound.setChecked(gameSettings.getBoolean("b_sound", false));
        refreshingPoints.setChecked(gameSettings.getBoolean("refresh_points", true));
        keys.setChecked(gameSettings.getBoolean("keys", true));
        ((TextView) ad.findViewById(R.id.level)).setText("" + gameSettings.getLong("b_level", 0L));
        ((TextView) ad.findViewById(R.id.scores)).setText("" + gameSettings.getLong("b_score", 0L));
        ((TextView) ad.findViewById(R.id.time)).setText("" + gameSettings.getLong("b_time", 0L));
        timing.setOnCheckedChangeListener((buttonView, isChecked) -> {
            editor.putBoolean("timing", isChecked);
            editor.apply();
            editor.commit();
            if (gameSettings.getBoolean("k_sound", true))
                soundPool.play(soundIds[6], 1, 1, 1, 0, 1.0f);
        });
        kSound.setOnCheckedChangeListener((buttonView, isChecked) -> {
            editor.putBoolean("k_sound", isChecked);
            editor.apply();
            editor.commit();
            if (gameSettings.getBoolean("k_sound", true))
                soundPool.play(soundIds[6], 1, 1, 1, 0, 1.0f);
        });
        bSound.setOnCheckedChangeListener((buttonView, isChecked) -> {
            editor.putBoolean("b_sound", isChecked);
            editor.apply();
            editor.commit();
            if (isChecked)
                soundPool.resume(bSoundStreamId);
            else soundPool.pause(bSoundStreamId);

            if (gameSettings.getBoolean("k_sound", true))
                soundPool.play(soundIds[6], 1, 1, 1, 0, 1.0f);
        });
        refreshingPoints.setOnCheckedChangeListener((buttonView, isChecked) -> {
            editor.putBoolean("refresh_points", isChecked);
            editor.apply();
            editor.commit();
            if (gameSettings.getBoolean("k_sound", true))
                soundPool.play(soundIds[6], 1, 1, 1, 0, 1.0f);
        });
        keys.setOnCheckedChangeListener((buttonView, isChecked) -> {
            editor.putBoolean("keys", isChecked);
            editor.apply();
            editor.commit();
            setKeys(isChecked);
            if (gameSettings.getBoolean("k_sound", true))
                soundPool.play(soundIds[6], 1, 1, 1, 0, 1.0f);
        });
        ad.findViewById(R.id.return_btn).setOnClickListener(v -> {
            ad.cancel();
            resumeGame();
        });
        ad.show();
    }

    private void pauseGame(long startTime) {
    }
    private void resumeGame() {
    }

    private void setAnimations() {
        ((AnimationDrawable) level.getBackground()).start();
        ((AnimationDrawable) scores.getBackground()).start();
        ((AnimationDrawable) time.getBackground()).start();
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

    private void setSound(boolean on) {

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


    abstract class CountUp extends CountDownTimer {

        private long duration;

        public CountUp(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);
            this.duration = millisInFuture;
        }


        @Override
        public void onTick(long millisUntilFinished) {
            long time = duration - millisUntilFinished;
            onTick(time);
        }

        @Override
        public void onFinish() {

        }
    }
}



