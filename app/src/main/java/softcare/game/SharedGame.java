package softcare.game;

import android.widget.TextView;
import android.widget.Toast;

import softcare.game.model.Game;
import softcare.game.model.GameShare;
import softcare.game.model.Tsp;
import softcare.gui.StyleDialog;

public class SharedGame extends GameActivity {

    @Override
    protected void setBest(Game game, Tsp tsp) {
        //super.setBest(game, tsp);
    }

    @Override
    protected void saveGave(Tsp tsp, Game game) {
       /// super.saveGave(tsp, game);
        Toast.makeText(this,
                "Not saving it from friend ",
                Toast.LENGTH_LONG).show();
    }

    @Override
    protected void next(Game game, Tsp tsp) {
        ///super.next(game, tsp);
        Toast.makeText(this,
                "You cannot continue from where your fried stop, Screen short and share",
                Toast.LENGTH_LONG).show();
    }
}
