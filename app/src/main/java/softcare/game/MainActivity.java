package softcare.game;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
     /*
        String[] a={ "my love", "my money"};
       List<String> al=   Arrays.asList(a);
        Intent i=new Intent(this, GameActivity.class);
        String a1[]  = al.toArray(new String[0]);
        i.putExtra("a",a1);
        startActivity(i); */
    }

    public  void game(View v){
        startActivity(new Intent(this, GameActivity.class));
    }
    public  void solution(View v){
 startActivity(new Intent(this, SolutionActivity.class));
    }
}