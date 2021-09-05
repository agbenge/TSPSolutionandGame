package softcare.game;

//import com.google.android.gms.tasks.Task;
//import com.google.android.gms.tasks.Tasks;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar; ;
import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.util.Log;
import android.view.View;

import softcare.game.databinding.ActivitySolutionBinding;
import softcare.gui.PointXY;

public class SolutionActivity extends AppCompatActivity {

    private ActivitySolutionBinding binding;


    private SolutionModel solutionViewModel;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivitySolutionBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Toolbar toolbar = binding.toolbar;
        setSupportActionBar(toolbar);
        CollapsingToolbarLayout toolBarLayout = binding.toolbarLayout;
        toolBarLayout.setTitle(getTitle());

       binding.addD.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               Tsp tsp = solutionViewModel.getTsp();
                Intent intent = new Intent(SolutionActivity.this, AddCityDActivity.class);
               if(tsp!=null) {
                   intent.putExtra("cities",tsp.getCities().toArray());
               }
               addD.launch(intent);
               //Snackbar.make(view, "Unable to intiallsed", Snackbar.LENGTH_LONG) .setAction("Action", null).show();
            }
        });
        binding.addL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SolutionActivity.this, AddCityLActivity.class);
                addL.launch(intent);
                //Snackbar.make(view, "Unable to intiallsed", Snackbar.LENGTH_LONG) .setAction("Action", null).show();
            }
        });
        solutionViewModel = new ViewModelProvider(this).get(SolutionModel.class);
       solutionViewModel.getTspLiveData().observe(this, new Observer<Tsp>() {

            @Override
            public void onChanged(Tsp tsp) {
     if(tsp!=null){
         binding.outputMatrix.setText(solutionViewModel.getPreview());
         binding.outputLocations.setText(solutionViewModel.getPreviewXY());
     }
            }
        });
        solutionViewModel.getErrorCodeLiveData().observe(this, new Observer<ErrorCode>() {

            @Override
            public void onChanged(ErrorCode errorCode) {
                Snackbar.make(binding.getRoot(), "Error "+errorCode.toString(), Snackbar.LENGTH_LONG) .setAction("Action", null).show();

            }
        });
        solutionViewModel.getProgressXLiveData().observe(this, new Observer<ProgressX>() {
            @Override
            public void onChanged(ProgressX progressX) {
                Log.d("game",progressX.toString());
            }
        });





    }

    ActivityResultLauncher<Intent> addD= registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    int resultCode= result.getResultCode();
                    Log.d(CodeX.tag," here "+(resultCode==RESULT_OK));
                    if(resultCode==RESULT_OK) {
                        Intent data = result.getData();
                        String name = data.getStringExtra("cityName");
                        double[] distance = data.getDoubleArrayExtra("data");
                        solutionViewModel.addCity(name, distance);

                    }
                }
            }
    );
    ActivityResultLauncher<Intent> addL= registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    int resultCode= result.getResultCode();
                    Log.d(CodeX.tag," here "+(resultCode==RESULT_OK));
                    if(resultCode==RESULT_OK) {
                        Intent data = result.getData();

                        String name = data.getStringExtra("cityName");
                        double  x = data.getDoubleExtra("x", 0);
                        double  y = data.getDoubleExtra("y", 0);
                        solutionViewModel.addCityXY(name, new PointXY(x,y));
                    }
                }
            }
    );


}