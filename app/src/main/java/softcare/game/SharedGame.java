package softcare.game;

import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import android.widget.Toast;

import com.google.gson.Gson;

import softcare.game.model.CodeX;
import softcare.game.model.Game;
import softcare.game.model.GameShare;
import softcare.game.model.Location;
import softcare.game.model.Tsp;
import softcare.gui.StyleDialog;

public class SharedGame extends GameActivity {

    @Override
    protected void setBest(Game game, Tsp tsp) {
        //super.setBest(game, tsp);
    }

    @Override
    protected void saveGave(String tspKey, String gameKey, SharedPreferences.Editor editor, String jsonTsp, String jsonGame) {
        super.saveGave(CodeX.tspSharedKey, CodeX.gameSharedKey, editor, jsonTsp, jsonGame);
    }

    @Override
    protected GameShare getCurrentGame(String tspKey, String gameKey) {
        return super.getCurrentGame(CodeX.tspSharedKey,  CodeX.gameSharedKey);
    }

    @Override
    protected void next( boolean b, long l, Tsp tsp,Game game) {
        ///super.next(game, tsp);
        Toast.makeText(this,
                "You cannot continue from where your fried stopped, Screen short and share",
                Toast.LENGTH_LONG).show();
    }

    @Override
    protected void selectNewGame() {
       String textReceived = getIntent().getStringExtra(Intent.EXTRA_TEXT);
        if (textReceived!=null){
            if (dialog == null) dialog = new StyleDialog(this);
            dialog.setContentView(R.layout.pop_progress_2);
            dialog.show();


            dialog.setOnCancelListener(null);
            taskManager.runTask(() -> {
                try {

                    Location gameShare = openGame(textReceived);
                    if (dialog != null) dialog.cancel();
                    if(gameShare!=null) {
                            if (dialog != null) if (dialog.isShowing())
                                    SharedGame.super.postShareGame(gameShare);
                    } else   runOnUiThread(() -> Toast.makeText(this, "Unrecognised Format 2",Toast.LENGTH_LONG).show());


                }catch (Exception e) {
                    Log.e(CodeX.tag, e.getLocalizedMessage());
                    e.printStackTrace();
                    runOnUiThread(() -> {
                        Toast.makeText(this, " Error: Unrecognised Format 3", Toast.LENGTH_LONG).show();
                        onBackPressed();
                    });

                }


            });
        }else{
            Toast.makeText(this, "Unrecognised Format",Toast.LENGTH_LONG).show();
        }


        }

    private Location openGame(String gameShared) {
        Gson gson = new Gson();
        if (gameShared != null )
            return   gson.fromJson(gameShared, Location.class);
        return null;
    }

}
