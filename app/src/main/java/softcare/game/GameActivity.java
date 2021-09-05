package softcare.game;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;
import android.util.Log;

import softcare.game.model.CodeX;
import softcare.game.model.GameViewModel;
import softcare.game.model.SolutionViewModel;

public class GameActivity extends AppCompatActivity {
    private GameViewModel gameViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
        gameViewModel = new ViewModelProvider(this).get(GameViewModel.class);


    }
}